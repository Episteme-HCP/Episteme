/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.numbers.complex.Complex;

/**
 * Service provider interface for Fast Fourier Transform operations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface FFTProvider extends AlgorithmProvider {

    @Override
    default String getAlgorithmType() {
        return "Fast Fourier Transform";
    }

    /**
     * Performs a 1D forward FFT on double data.
     * @return An array of two arrays: [real_parts, imaginary_parts]
     */
    double[][] transform(double[] real, double[] imag);

    /**
     * Performs a 1D inverse FFT on double data.
     */
    double[][] inverseTransform(double[] real, double[] imag);

    /**
     * Performs a 1D forward FFT on Real data.
     */
    Real[][] transform(Real[] real, Real[] imag);

    /**
     * Performs a 1D inverse FFT on Real data.
     */
    Real[][] inverseTransform(Real[] real, Real[] imag);

    /**
     * Performs a 1D forward FFT on Complex data.
     */
    Complex[] transformComplex(Complex[] data);

    /**
     * Performs a 1D inverse FFT on Complex data.
     */
    Complex[] inverseTransformComplex(Complex[] data);

    /**
     * Performs a 2D forward FFT on double data.
     */
    double[][][] transform2D(double[][] real, double[][] imag);

    /**
     * Performs a 2D inverse FFT on double data.
     */
    double[][][] inverseTransform2D(double[][] real, double[][] imag);

    /**
     * Performs a 2D forward FFT on Real data.
     */
    Real[][][] transform2D(Real[][] real, Real[][] imag);

    /**
     * Performs a 2D inverse FFT on Real data.
     */
    Real[][][] inverseTransform2D(Real[][] real, Real[][] imag);

    /**
     * Performs a 3D forward FFT on double data.
     */
    double[][][][] transform3D(double[][][] real, double[][][] imag);

    /**
     * Performs a 3D inverse FFT on double data.
     */
    double[][][][] inverseTransform3D(double[][][] real, double[][][] imag);

    /**
     * Performs a 3D forward FFT on Real data.
     */
    Real[][][][] transform3D(Real[][][] real, Real[][][] imag);

    /**
     * Performs a 3D inverse FFT on Real data.
     */
    Real[][][][] inverseTransform3D(Real[][][] real, Real[][][] imag);

    @Override
    default String getName() {
        return "FFT Provider";
    }
}
