/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.mathematics.analysis.transform.backends;

import java.nio.DoubleBuffer;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.numbers.real.RealDouble;
import org.jscience.core.mathematics.analysis.transform.algorithms.FFTProvider;


/**
 * Native FFT provider using FFTW3 via FFTWBackend.
 */
public class NativeFFTProvider implements FFTProvider {

    private final PanamaFFTBackend fftw;
    private static final int FORWARD = -1;
    private static final int BACKWARD = 1;

    public NativeFFTProvider() {
        this.fftw = new PanamaFFTBackend();
    }

    @Override
    public String getName() {
        return "Native FFTW Provider";
    }

    public boolean isAvailable() {
        return fftw.isAvailable();
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public Real[][] transform(Real[] real, Real[] imag) {
        double[][] res = transform(toDoubleArray(real), toDoubleArray(imag));
        return new Real[][] { toRealArray(res[0]), toRealArray(res[1]) };
    }

    @Override
    public Real[][] inverseTransform(Real[] real, Real[] imag) {
        double[][] res = inverseTransform(toDoubleArray(real), toDoubleArray(imag));
        return new Real[][] { toRealArray(res[0]), toRealArray(res[1]) };
    }

    @Override
    public double[][] transform(double[] real, double[] imag) {
        return executeC2C(real, imag, FORWARD);
    }

    @Override
    public double[][] inverseTransform(double[] real, double[] imag) {
        double[][] res = executeC2C(real, imag, BACKWARD);
        // FFTW does not scale by 1/N automatically for backward
        double scale = 1.0 / real.length;
        for (int i = 0; i < real.length; i++) {
            res[0][i] *= scale;
            res[1][i] *= scale;
        }
        return res;
    }

    @Override
    public double[][][] transform2D(double[][] real, double[][] imag) {
        int rows = real.length;
        int cols = real[0].length;
        DoubleBuffer input = DoubleBuffer.allocate(rows * cols * 2);
        DoubleBuffer output = DoubleBuffer.allocate(rows * cols * 2);

        // Interleave
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int idx = (i * cols + j) * 2;
                input.put(idx, real[i][j]);
                input.put(idx + 1, imag[i][j]);
            }
        }

        fftw.transformComplex2D(rows, cols, input, output, FORWARD);

        // De-interleave
        double[][] outReal = new double[rows][cols];
        double[][] outImag = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int idx = (i * cols + j) * 2;
                outReal[i][j] = output.get(idx);
                outImag[i][j] = output.get(idx + 1);
            }
        }

        return new double[][][] { outReal, outImag };
    }

    private double[][] executeC2C(double[] real, double[] imag, int sign) {
        int n = real.length;
        DoubleBuffer input = DoubleBuffer.allocate(n * 2);
        DoubleBuffer output = DoubleBuffer.allocate(n * 2);

        // Interleave
        for (int i = 0; i < n; i++) {
            input.put(i * 2, real[i]);
            input.put(i * 2 + 1, imag[i]);
        }

        fftw.transformComplex(n, input, output, sign);

        // De-interleave
        double[] outReal = new double[n];
        double[] outImag = new double[n];
        for (int i = 0; i < n; i++) {
            outReal[i] = output.get(i * 2);
            outImag[i] = output.get(i * 2 + 1);
        }

        return new double[][] { outReal, outImag };
    }

    private double[] toDoubleArray(Real[] arr) {
        double[] res = new double[arr.length];
        for (int i = 0; i < arr.length; i++) res[i] = arr[i].doubleValue();
        return res;
    }

    private Real[] toRealArray(double[] arr) {
        Real[] res = new Real[arr.length];
        for (int i = 0; i < arr.length; i++) res[i] = RealDouble.of(arr[i]);
        return res;
    }
}






