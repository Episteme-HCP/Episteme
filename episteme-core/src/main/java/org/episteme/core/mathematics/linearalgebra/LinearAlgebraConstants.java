/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra;

/**
 * Centralized numerical constants and tolerances for linear algebra operations.
 * Standardizes behavior across native and managed backends.
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public final class LinearAlgebraConstants {

    /** Default tolerance for double-precision comparisons (1e-12). */
    public static final double EPSILON_DOUBLE = 1e-12;

    /** Default tolerance for single-precision comparisons (1e-7). */
    public static final float EPSILON_FLOAT = 1e-7f;

    /** Stability threshold for near-singular matrices. */
    public static final double STABILITY_THRESHOLD = 1e-15;

    /** Default maximum iterations for iterative solvers (BiCGSTAB, GMRES). */
    public static final int MAX_ITERATIONS = 1000;

    /** Default restart parameter for GMRES. */
    public static final int GMRES_RESTART = 30;

    /** Precision in bits for high-precision real numbers (standardized to 256 bits). */
    public static final int DEFAULT_PRECISION_BITS = 256;

    private LinearAlgebraConstants() {
        // Prevent instantiation
    }
}
