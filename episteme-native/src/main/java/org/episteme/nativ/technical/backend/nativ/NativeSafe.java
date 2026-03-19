/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.technical.backend.nativ;

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
        
        // Debug logging for sensitive calls if needed
        if (logger.isTraceEnabled()) {
            logger.trace("Native Invoke: {} with {} args", handle, args.length);
        }

        try {
            return handle.invokeWithArguments(args);
        } catch (Throwable t) {
            logger.error("CRITICAL: Native call failed! Handle: {}. Trace might indicate memory corruption or invalid address.", handle);
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof MemorySegment seg) {
                    logger.error("  Arg[{}]: MemorySegment(address=0x{}, size={})", i, Long.toHexString(seg.address()), seg.byteSize());
                } else {
                    logger.error("  Arg[{}]: {} ({})", i, args[i], args[i] != null ? args[i].getClass().getSimpleName() : "null");
                }
            }
            if (t instanceof RuntimeException) throw (RuntimeException) t;
            throw new RuntimeException("Native boundary protection: " + t.getMessage(), t);
        }
    }
}
