/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.technical.backend.nativ;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Safety wrapper for Native MethodHandle invocations.
 * Provides pre-call validation and detailed logging for boundary protection.
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class NativeSafe {
    private static final Logger logger = LoggerFactory.getLogger(NativeSafe.class);

    /**
     * Safely invokes a MethodHandle with the given arguments.
     * Checks for NULL segments and logs call state if an error occurs.
     */
    public static Object invoke(MethodHandle handle, Object... args) {
        if (handle == null) {
            logger.error("Attempted to invoke a null MethodHandle. This usually means a native symbol lookup failed.");
            throw new IllegalStateException("Attempted to invoke a null native handle. Library might not be loaded.");
        }
        
        // Pre-call validation: Check for NULL segments and log sizes
        if (logger.isTraceEnabled()) {
            StringBuilder sb = new StringBuilder("Native Invoke: ").append(handle).append(" with ").append(args.length).append(" args");
            for (int i = 0; i < args.length; i++) {
                sb.append("\n  Arg[").append(i).append("]: ");
                Object arg = args[i];
                if (arg instanceof MemorySegment seg) {
                    try {
                        sb.append("MemorySegment(address=0x").append(Long.toHexString(seg.address())).append(", size=").append(seg.byteSize()).append(")");
                        if (seg.equals(MemorySegment.NULL)) sb.append(" [NULL]");
                    } catch (UnsupportedOperationException e) {
                        sb.append("MemorySegment(address=UNAVAILABLE, size=").append(seg.byteSize()).append(")");
                    }
                } else {
                    sb.append(arg).append(" (").append(arg != null ? arg.getClass().getSimpleName() : "null").append(")");
                }
            }
            logger.trace(sb.toString());
        }

        try {
            // 1. Primary Attempt: Direct invocation using Panama MethodHandle
            return handle.invokeWithArguments(args);
        } catch (Throwable t1) {
            // 2. Secondary Attempt: Type Coercion fallback
            logger.debug("Native Invoke: Call failed for {}. Attempting platform-aware coercion. Error: {}", handle, t1.getMessage());
            
            Object[] coercedArgs = new Object[args.length];
            Class<?>[] ptypes = handle.type().parameterArray();
            boolean coerced = false;
            
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg == null) {
                    if (ptypes[i] == MemorySegment.class) {
                        coercedArgs[i] = MemorySegment.NULL;
                        coerced = true;
                    } else {
                        coercedArgs[i] = null;
                    }
                    continue;
                }
                
                Class<?> expected = ptypes[i];
                if (expected.isPrimitive()) {
                    if (arg instanceof Number num) {
                        if (expected == int.class) {
                            int val = num.intValue();
                            coercedArgs[i] = val;
                            if (!(arg instanceof Integer)) coerced = true;
                        } else if (expected == long.class) {
                            long val = num.longValue();
                            coercedArgs[i] = val;
                            if (!(arg instanceof Long)) coerced = true;
                        } else if (expected == float.class) {
                            float val = num.floatValue();
                            coercedArgs[i] = val;
                            if (!(arg instanceof Float)) coerced = true;
                        } else if (expected == double.class) {
                            double val = num.doubleValue();
                            coercedArgs[i] = val;
                            if (!(arg instanceof Double)) coerced = true;
                        } else {
                            coercedArgs[i] = arg;
                        }
                    } else {
                        coercedArgs[i] = arg;
                    }
                } else if (expected == MemorySegment.class) {
                    if (arg instanceof MemorySegment) {
                        coercedArgs[i] = arg;
                    } else if (arg instanceof Long val) {
                        coercedArgs[i] = MemorySegment.ofAddress(val);
                        coerced = true;
                    } else {
                        coercedArgs[i] = arg;
                    }
                } else {
                    coercedArgs[i] = arg;
                }
            }
            
            if (coerced && logger.isDebugEnabled()) {
                logger.debug("Native Invoke: Coerced arguments for handle {}", handle);
            }
            
            try {
                return handle.invokeWithArguments(coercedArgs);
            } catch (Throwable t2) {
                boolean isAlreadyClosed = t2 instanceof IllegalStateException && 
                                          t2.getMessage() != null && 
                                          t2.getMessage().contains("already closed");
                
                if (isAlreadyClosed) {
                    logger.error("Native Invoke: Call to {} failed because a segment was already closed.", handle);
                    throw (RuntimeException) t2;
                }

                // 3. Final Fallback: Critical Failure reporting
                logger.error("CRITICAL: Native call failed after coercion! Handle: {}", handle);
                for (int i = 0; i < args.length; i++) {
                    Object a = args[i];
                    if (a instanceof MemorySegment seg) {
                        try {
                            logger.error("  Arg[{}]: MemorySegment(address=0x{}, size={})", i, Long.toHexString(seg.address()), seg.byteSize());
                        } catch (UnsupportedOperationException ex) {
                            logger.error("  Arg[{}]: MemorySegment(address=UNAVAILABLE, size={})", i, seg.byteSize());
                        }
                    } else {
                        logger.error("  Arg[{}]: {} ({})", i, a, a != null ? a.getClass().getSimpleName() : "null");
                    }
                }
                
                if (t2 instanceof RuntimeException) throw (RuntimeException) t2;
                throw new RuntimeException("Native boundary failure: " + t2.getMessage(), t2);
            }
        }
    }

    /**
     * Safely wraps a raw memory address with origin tracking and lifecycle protection.
     * Use this when a native function returns a pointer that needs to be accessed from Java.
     */
    public static ScavengeProtectedSegment scavenge(MemorySegment raw, long size, Arena arena, String origin) {
        if (raw == null || raw.equals(MemorySegment.NULL)) {
            logger.warn("NativeSafe: Attempted to scavenge a NULL segment from {}", origin);
            return new ScavengeProtectedSegment(MemorySegment.NULL, origin, 0);
        }
        try {
            MemorySegment safe = raw.reinterpret(size, arena, segment -> {
                if (logger.isDebugEnabled()) {
                    logger.debug("NativeSafe: Scavenged segment from {} (address=0x{}) has been cleaned up.", 
                        origin, Long.toHexString(segment.address()));
                }
            });
            return new ScavengeProtectedSegment(safe, origin, size);
        } catch (Throwable t) {
            logger.error("NativeSafe: Failed to reinterpret segment from {}: {}", origin, t.getMessage());
            throw new RuntimeException("Scavenging failed for " + origin, t);
        }
    }

    /**
     * Ensures that a buffer is native (direct) for FFM boundary calls.
     * If the buffer is on-heap, it is copied to a new native segment in the provided arena.
     */
    public static MemorySegment ensureNative(java.nio.Buffer buffer, Arena arena) {
        if (buffer == null) return MemorySegment.NULL;
        if (buffer.isDirect()) {
            return MemorySegment.ofBuffer(buffer);
        } else {
            if (buffer instanceof java.nio.DoubleBuffer db) {
                double[] array = new double[db.remaining()];
                int pos = db.position();
                db.get(array);
                db.position(pos);
                return arena.allocateFrom(ValueLayout.JAVA_DOUBLE, array);
            } else if (buffer instanceof java.nio.IntBuffer ib) {
                int[] array = new int[ib.remaining()];
                int pos = ib.position();
                ib.get(array);
                ib.position(pos);
                return arena.allocateFrom(ValueLayout.JAVA_INT, array);
            } else if (buffer instanceof java.nio.FloatBuffer fb) {
                float[] array = new float[fb.remaining()];
                int pos = fb.position();
                fb.get(array);
                fb.position(pos);
                return arena.allocateFrom(ValueLayout.JAVA_FLOAT, array);
            } else if (buffer instanceof java.nio.LongBuffer lb) {
                long[] array = new long[lb.remaining()];
                int pos = lb.position();
                lb.get(array);
                lb.position(pos);
                return arena.allocateFrom(ValueLayout.JAVA_LONG, array);
            }
            throw new UnsupportedOperationException("Unsupported buffer type for native conversion: " + buffer.getClass());
        }
    }

    /**
     * Copies data back from a native segment to a heap buffer if necessary.
     */
    public static void copyBack(MemorySegment nativeSeg, java.nio.Buffer buffer) {
        if (buffer == null || buffer.isDirect()) return;
        int pos = buffer.position();
        if (buffer instanceof java.nio.DoubleBuffer db) {
            double[] array = nativeSeg.toArray(ValueLayout.JAVA_DOUBLE);
            db.put(array);
            db.position(pos);
        } else if (buffer instanceof java.nio.IntBuffer ib) {
            int[] array = nativeSeg.toArray(ValueLayout.JAVA_INT);
            ib.put(array);
            ib.position(pos);
        } else if (buffer instanceof java.nio.FloatBuffer fb) {
            float[] array = nativeSeg.toArray(ValueLayout.JAVA_FLOAT);
            fb.put(array);
            fb.position(pos);
        } else if (buffer instanceof java.nio.LongBuffer lb) {
            long[] array = nativeSeg.toArray(ValueLayout.JAVA_LONG);
            lb.put(array);
            lb.position(pos);
        }
    }
}

