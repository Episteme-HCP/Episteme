/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.technical.backend.nativ;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
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
            throw new IllegalStateException("Attempted to invoke a null native handle. Library might not be loaded.");
        }
        
        // Pre-call validation: Check for NULL segments that shouldn't be NULL
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof MemorySegment seg) {
                if (seg.equals(MemorySegment.NULL)) {
                    if (logger.isTraceEnabled()) {
                        logger.trace("Native Invoke: Arg[{}] is NULL MemorySegment. This might be intentional (e.g. workspace size query) or a hint for internal behavior.", i);
                    }
                }
            }
        }

        if (logger.isTraceEnabled()) {
            logger.trace("Native Invoke: {} with {} args", handle, args.length);
        }

        try {
            // 1. Primary Attempt: Direct invocation using Panama MethodHandle
            return handle.invokeWithArguments(args);
        } catch (Throwable t1) {
            // 2. Secondary Attempt: Type Coercion fallback
            // This handles cases like Long vs int mismatches on Windows, or double/float mismatches.
            logger.debug("Native Invoke: Call failed for {}. Attempting platform-aware coercion. Error: {}", handle, t1.getMessage());
            
            Object[] coercedArgs = new Object[args.length];
            Class<?>[] ptypes = handle.type().parameterArray();
            boolean coerced = false;
            
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg == null) {
                    coercedArgs[i] = null;
                    continue;
                }
                
                Class<?> expected = ptypes[i];
                if (expected.isPrimitive()) {
                    if (arg instanceof Number num) {
                        if (expected == int.class) {
                            int val = num.intValue();
                            coercedArgs[i] = val;
                            if (arg instanceof Long) coerced = true;
                        } else if (expected == long.class) {
                            coercedArgs[i] = num.longValue();
                        } else if (expected == float.class) {
                            coercedArgs[i] = num.floatValue();
                        } else if (expected == double.class) {
                            coercedArgs[i] = num.doubleValue();
                        } else {
                            coercedArgs[i] = arg;
                        }
                    } else {
                        coercedArgs[i] = arg;
                    }
                } else {
                    coercedArgs[i] = arg;
                }
            }
            
            if (coerced) {
                logger.debug("Native Invoke: Coerced arguments (e.g. Long -> int) for handle {}", handle);
            }
            
            try {
                return handle.invokeWithArguments(coercedArgs);
            } catch (Throwable t2) {
                boolean isAlreadyClosed = t2 instanceof IllegalStateException && 
                                          t2.getMessage() != null && 
                                          t2.getMessage().contains("already closed");
                
                if (isAlreadyClosed) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Native Invoke: Call to {} aborted because segment was already closed.", handle);
                    }
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
                // Rethrow the coercion error as a RuntimeException if it isn't one.
                if (t2 instanceof RuntimeException) throw (RuntimeException) t2;
                throw new RuntimeException("Native boundary failure (after coercion attempt): " + t2.getMessage(), t2);
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
}
