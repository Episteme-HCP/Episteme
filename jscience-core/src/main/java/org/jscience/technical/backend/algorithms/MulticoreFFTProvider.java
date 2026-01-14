/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.technical.backend.algorithms;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.numbers.complex.Complex;
import org.jscience.mathematics.analysis.transform.DiscreteFourierTransform;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Multicore implementation of FFTProvider using Fork/Join framework.
 * Replaces the former DistributedFFT class.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MulticoreFFTProvider implements FFTProvider {

    @Override
    public String getName() {
        return "Java Multicore FFT";
    }

    @Override
    public Real[][] transform(Real[] real, Real[] imag) {
        return computeFFT(real, imag, true);
    }

    @Override
    public Real[][] inverseTransform(Real[] real, Real[] imag) {
        // Inverse FFT: Swap real/imag parts, transform, swap back and scale
        // But standard inverse FFT algorithm usually is:
        // 1. conjugate input
        // 2. transform
        // 3. conjugate output
        // 4. scale by 1/N
        // Or simply: transform(imag, real) then scale?

        // Let's reuse logic from CUDAFFTProvider for consistency:
        // imag = -imag
        // forward
        // scale

        int n = real.length;
        Real[] negImag = new Real[n];
        for (int i = 0; i < n; i++) {
            negImag[i] = imag[i].negate();
        }

        Real[][] result = computeFFT(real, negImag, true);
        Real[] resReal = result[0];
        Real[] resImag = result[1];

        Real scale = Real.of(1.0 / n);

        for (int i = 0; i < n; i++) {
            resReal[i] = resReal[i].multiply(scale);
            resImag[i] = resImag[i].multiply(scale).negate(); // Conjugate back: neg(negI) = I, but we need correct
                                                              // semantic
                                                              // semantic
            // standard inverse:
            // if X[k] = sum x[n] e^(-i 2pi kn/N)
            // x[n] = 1/N sum X[k] e^(i 2pi kn/N)
            // e^(i...) = cos + i sin
            // e^(-i...) = cos - i sin

            // Using DFFT property: IFFT(x) = 1/N * conj(FFT(conj(x)))
            // conj(a+bi) = a-bi
            // input: real, imag
            // conj input: real, -imag
            // FFT output: R + Ii
            // conj output: R - Ii
            // scale: (R - Ii)/N
        }

        return new Real[][] { resReal, resImag };
    }

    private Real[][] computeFFT(Real[] real, Real[] imag, boolean parallel) {
        int n = real.length;
        if (n <= 1024 || !parallel) { // Threshold for local computation
            return computeLocalFFT(real, imag);
        }

        // Split data
        int half = n / 2;
        Real[] evenReal = new Real[half];
        Real[] evenImag = new Real[half];
        Real[] oddReal = new Real[half];
        Real[] oddImag = new Real[half];

        for (int i = 0; i < half; i++) {
            evenReal[i] = real[2 * i];
            evenImag[i] = imag[2 * i];
            oddReal[i] = real[2 * i + 1];
            oddImag[i] = imag[2 * i + 1];
        }

        // Parallel recursive calls
        ForkJoinPool pool = ForkJoinPool.commonPool();
        RecursiveTask<Real[][]> evenTask = new RecursiveTask<>() {
            @Override
            protected Real[][] compute() {
                // Recurse, potentially sequentially if smaller
                return new MulticoreFFTProvider().computeFFT(evenReal, evenImag, evenReal.length > 1024);
            }
        };
        RecursiveTask<Real[][]> oddTask = new RecursiveTask<>() {
            @Override
            protected Real[][] compute() {
                return new MulticoreFFTProvider().computeFFT(oddReal, oddImag, oddReal.length > 1024);
            }
        };

        pool.execute(evenTask);
        Real[][] oddResult = oddTask.invoke();
        Real[][] evenResult = evenTask.join();

        // Combine results
        Real[] resReal = new Real[n];
        Real[] resImag = new Real[n];

        for (int k = 0; k < half; k++) {
            double angle = -2 * Math.PI * k / n;
            double cos = Math.cos(angle);
            double sin = Math.sin(angle);

            double oddR = oddResult[0][k].doubleValue();
            double oddI = oddResult[1][k].doubleValue();

            double tReal = cos * oddR - sin * oddI;
            double tImag = sin * oddR + cos * oddI;

            double evenR = evenResult[0][k].doubleValue();
            double evenI = evenResult[1][k].doubleValue();

            resReal[k] = Real.of(evenR + tReal);
            resImag[k] = Real.of(evenI + tImag);
            resReal[k + half] = Real.of(evenR - tReal);
            resImag[k + half] = Real.of(evenI - tImag);
        }

        return new Real[][] { resReal, resImag };
    }

    /**
     * Local FFT using JScience.
     */
    private Real[][] computeLocalFFT(Real[] real, Real[] imag) {
        // Use JScience DiscreteFourierTransform
        Complex[] data = new Complex[real.length];
        for (int i = 0; i < real.length; i++) {
            data[i] = Complex.of(real[i].doubleValue(), imag[i].doubleValue());
        }

        // Transform
        Complex[] transformed = DiscreteFourierTransform.transform(data);

        // Extract real and imaginary parts
        Real[] outReal = new Real[transformed.length];
        Real[] outImag = new Real[transformed.length];
        for (int i = 0; i < transformed.length; i++) {
            outReal[i] = Real.of(transformed[i].real());
            outImag[i] = Real.of(transformed[i].imaginary());
        }

        return new Real[][] { outReal, outImag };
    }

    @Override
    public double[][] transform(double[] real, double[] imag) {
        return computeFFTPrimitive(real, imag, true);
    }

    @Override
    public double[][] inverseTransform(double[] real, double[] imag) {
        int n = real.length;
        double[] negImag = new double[n];
        for (int i = 0; i < n; i++) {
            negImag[i] = -imag[i];
        }

        double[][] result = computeFFTPrimitive(real, negImag, true);
        double[] resReal = result[0];
        double[] resImag = result[1];

        double scale = 1.0 / n;
        for (int i = 0; i < n; i++) {
            resReal[i] *= scale;
            resImag[i] = -(resImag[i] * scale);
        }

        return new double[][] { resReal, resImag };
    }

    private double[][] computeFFTPrimitive(double[] real, double[] imag, boolean parallel) {
        int n = real.length;
        if (n <= 1024 || !parallel) {
            return computeLocalFFTPrimitive(real, imag);
        }

        int half = n / 2;
        double[] evenReal = new double[half];
        double[] evenImag = new double[half];
        double[] oddReal = new double[half];
        double[] oddImag = new double[half];

        for (int i = 0; i < half; i++) {
            evenReal[i] = real[2 * i];
            evenImag[i] = imag[2 * i];
            oddReal[i] = real[2 * i + 1];
            oddImag[i] = imag[2 * i + 1];
        }

        ForkJoinPool pool = ForkJoinPool.commonPool();
        RecursiveTask<double[][]> evenTask = new RecursiveTask<>() {
            @Override
            protected double[][] compute() {
                return new MulticoreFFTProvider().computeFFTPrimitive(evenReal, evenImag, evenReal.length > 1024);
            }
        };
        RecursiveTask<double[][]> oddTask = new RecursiveTask<>() {
            @Override
            protected double[][] compute() {
                return new MulticoreFFTProvider().computeFFTPrimitive(oddReal, oddImag, oddReal.length > 1024);
            }
        };

        pool.execute(evenTask);
        double[][] oddResult = oddTask.invoke();
        double[][] evenResult = evenTask.join();

        double[] resReal = new double[n];
        double[] resImag = new double[n];

        for (int k = 0; k < half; k++) {
            double angle = -2 * Math.PI * k / n;
            double cos = Math.cos(angle);
            double sin = Math.sin(angle);

            double oddR = oddResult[0][k];
            double oddI = oddResult[1][k];

            double tReal = cos * oddR - sin * oddI;
            double tImag = sin * oddR + cos * oddI;

            double evenR = evenResult[0][k];
            double evenI = evenResult[1][k];

            resReal[k] = evenR + tReal;
            resImag[k] = evenI + tImag;
            resReal[k + half] = evenR - tReal;
            resImag[k + half] = evenI - tImag;
        }

        return new double[][] { resReal, resImag };
    }

    private double[][] computeLocalFFTPrimitive(double[] real, double[] imag) {
        Complex[] data = new Complex[real.length];
        for (int i = 0; i < real.length; i++) {
            data[i] = Complex.of(real[i], imag[i]);
        }
        Complex[] transformed = DiscreteFourierTransform.transform(data);
        double[] outReal = new double[transformed.length];
        double[] outImag = new double[transformed.length];
        for (int i = 0; i < transformed.length; i++) {
            outReal[i] = transformed[i].real();
            outImag[i] = transformed[i].imaginary();
        }
        return new double[][] { outReal, outImag };
    }
}
