/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.analysis.fft;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.technical.algorithm.AlgorithmProvider;

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
        return "Numerical Analysis";
    }

    float[][] transform(float[] real, float[] imag);
    float[][] inverseTransform(float[] real, float[] imag);
    double[][] transform(double[] real, double[] imag);
    double[][] inverseTransform(double[] real, double[] imag);
    Real[][] transform(Real[] real, Real[] imag);
    Real[][] inverseTransform(Real[] real, Real[] imag);
    Complex[] transformComplex(Complex[] data);
    Complex[] inverseTransformComplex(Complex[] data);

    float[][][] transform2D(float[][] real, float[][] imag);
    float[][][] inverseTransform2D(float[][] real, float[][] imag);
    double[][][] transform2D(double[][] real, double[][] imag);
    double[][][] inverseTransform2D(double[][] real, double[][] imag);
    Real[][][] transform2D(Real[][] real, Real[][] imag);
    Real[][][] inverseTransform2D(Real[][] real, Real[][] imag);
    Complex[][] transformComplex2D(Complex[][] data);
    Complex[][] inverseTransformComplex2D(Complex[][] data);

    float[][][][] transform3D(float[][][] real, float[][][] imag);
    float[][][][] inverseTransform3D(float[][][] real, float[][][] imag);
    double[][][][] transform3D(double[][][] real, double[][][] imag);
    double[][][][] inverseTransform3D(double[][][] real, double[][][] imag);
    Real[][][][] transform3D(Real[][][] real, Real[][][] imag);
    Real[][][][] inverseTransform3D(Real[][][] real, Real[][][] imag);
    Complex[][][] transformComplex3D(Complex[][][] data);
    Complex[][][] inverseTransformComplex3D(Complex[][][] data);

    @Override
    default String getName() {
        return "FFT Provider";
    }
}
