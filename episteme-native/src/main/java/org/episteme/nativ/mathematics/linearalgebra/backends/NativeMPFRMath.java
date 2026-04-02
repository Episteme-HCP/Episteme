/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import org.episteme.nativ.technical.backend.nativ.NativeSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Common MPFR mathematical operations for both dense and sparse backends.
 */
public class NativeMPFRMath {
    private static final Logger logger = LoggerFactory.getLogger(NativeMPFRMath.class);
    private static final MemoryLayout MPFR_LAYOUT = MemoryLayout.structLayout(
        ValueLayout.JAVA_LONG.withName("_mpfr_prec"),
        ValueLayout.JAVA_INT.withName("_mpfr_sign"),
        ValueLayout.JAVA_LONG.withName("_mpfr_exp"),
        Linker.nativeLinker().defaultLookup().find("malloc").isPresent() ? ValueLayout.ADDRESS.withName("_mpfr_d") : ValueLayout.ADDRESS.withName("_mpfr_d")
    ).withName("mpfr_t");

    public static void complexExp(MemorySegment resR, MemorySegment resI, MemorySegment zR, MemorySegment zI, long prec, Arena arena, 
                                MethodHandle mpfrExp, MethodHandle mpfrSinCos, MethodHandle mpfrMul) {
        MemorySegment expR = arena.allocate(MPFR_LAYOUT);
        MemorySegment sinI = arena.allocate(MPFR_LAYOUT);
        MemorySegment cosI = arena.allocate(MPFR_LAYOUT);
        try {
            NativeSafe.invoke(NativeMPFRDenseLinearAlgebraBackend.MPFR_INIT2, expR, prec);
            NativeSafe.invoke(NativeMPFRDenseLinearAlgebraBackend.MPFR_INIT2, sinI, prec);
            NativeSafe.invoke(NativeMPFRDenseLinearAlgebraBackend.MPFR_INIT2, cosI, prec);

            NativeSafe.invoke(mpfrExp, expR, zR, 0);
            NativeSafe.invoke(mpfrSinCos, sinI, cosI, zI, 0);
            
            NativeSafe.invoke(mpfrMul, resR, expR, cosI, 0);
            NativeSafe.invoke(mpfrMul, resI, expR, sinI, 0);

            NativeSafe.invoke(NativeMPFRDenseLinearAlgebraBackend.MPFR_CLEAR, expR);
            NativeSafe.invoke(NativeMPFRDenseLinearAlgebraBackend.MPFR_CLEAR, sinI);
            NativeSafe.invoke(NativeMPFRDenseLinearAlgebraBackend.MPFR_CLEAR, cosI);
        } catch (Throwable t) {
            logger.error("complexExp failed: {}", t.getMessage());
        }
    }
    
    // ... more helpers could go here, but maybe it's easier to just fix the two backends directly to avoid dependency hell
}
