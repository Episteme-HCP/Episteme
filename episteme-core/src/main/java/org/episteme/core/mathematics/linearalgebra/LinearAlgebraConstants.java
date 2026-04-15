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

    /** Default tolerance for double-precision comparisons. */
    public static double getEpsilonDouble() {
        return org.episteme.core.Episteme.getNumericalConfiguration().getEpsilonDouble();
    }

    /** Default tolerance for single-precision comparisons. */
    public static float getEpsilonFloat() {
        return org.episteme.core.Episteme.getNumericalConfiguration().getEpsilonFloat();
    }

    /** Stability threshold for near-singular matrices. */
    public static double getStabilityThreshold() {
        return org.episteme.core.Episteme.getNumericalConfiguration().getStabilityThreshold();
    }

    /** Default maximum iterations for iterative solvers (BiCGSTAB, GMRES). */
    public static int getMaxIterations() {
        return org.episteme.core.Episteme.getNumericalConfiguration().getMaxIterations();
    }

    /** Default restart parameter for GMRES. */
    public static int getGmresRestart() {
        return org.episteme.core.Episteme.getNumericalConfiguration().getGmresRestart();
    }

    /** Precision in bits for high-precision real numbers. */
    public static int getDefaultPrecisionBits() {
        return org.episteme.core.Episteme.getNumericalConfiguration().getPrecisionBits();
    }

    private LinearAlgebraConstants() {
        // Prevent instantiation
    }
}
