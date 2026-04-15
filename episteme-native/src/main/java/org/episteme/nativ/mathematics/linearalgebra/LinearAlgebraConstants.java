/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.complex.Complex;

/**
 * Standard numerical constants and thresholds for native linear algebra solvers.
 * Ensures consistent behavior across CPU and GPU backends.
 */
public final class LinearAlgebraConstants {
    private LinearAlgebraConstants() {}

    /** Default relative tolerance for iterative solvers (FP64). */
    public static final double DEFAULT_TOLERANCE = 1e-12;
    
    /** Default maximum iterations for iterative solvers. */
    public static final int DEFAULT_MAX_ITERATIONS = 10000;
    
    /** Machine epsilon for double precision. */
    public static final double DBL_EPSILON = 2.2204460492503131e-16;
    
    /** Pivot threshold for LU/Cholesky factorizations. */
    public static final double PIVOT_THRESHOLD = 1e-3;

    /**
     * Returns the tolerance as a Real object.
     */
    public static Real toleranceReal() {
        return Real.of(DEFAULT_TOLERANCE);
    }
    
    /**
     * Returns the tolerance as a Complex object.
     */
    public static Complex toleranceComplex() {
        return Complex.of(Real.of(DEFAULT_TOLERANCE), Real.ZERO);
    }
}
