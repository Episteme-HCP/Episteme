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
                    logger.warn("Native Invoke: Arg[{}] is NULL MemorySegment. This might be intentional (e.g. workspace size query) or a bug.", i);
                }
            }
        }

        if (logger.isTraceEnabled()) {
            logger.trace("Native Invoke: {} with {} args", handle, args.length);
        }

        try {
            return handle.invokeWithArguments(args);
        } catch (Throwable t) {
            logger.error("CRITICAL: Native call failed! Handle: {}", handle);
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof MemorySegment seg) {
                    try {
                        logger.error("  Arg[{}]: MemorySegment(address=0x{}, size={})", i, Long.toHexString(seg.address()), seg.byteSize());
                    } catch (UnsupportedOperationException e) {
                        logger.error("  Arg[{}]: MemorySegment(address=UNAVAILABLE, size={})", i, seg.byteSize());
                    }
                } else {
                    logger.error("  Arg[{}]: {} ({})", i, args[i], args[i] != null ? args[i].getClass().getSimpleName() : "null");
                }
            }
            if (t instanceof RuntimeException) throw (RuntimeException) t;
            throw new RuntimeException("Native boundary protection: " + t.getMessage(), t);
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
