/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.distributed;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.numbers.complex.Complex;
import org.jscience.mathematics.analysis.transform.DiscreteFourierTransform;

import java.io.Serializable;

/**
 * Distributed FFT implementation.
 * <p>
 * Provides distributed Fast Fourier Transform capabilities.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DistributedFFT implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Distributed FFT computation.
     * 
     * @param real Real components
     * @param imag Imaginary components
     * @return Transformed data [real, imag]
     */
    public static Real[][] distributedFFT(Real[] real, Real[] imag) {
        int n = real.length;
        if (n <= 1024) { // Threshold for local computation
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
        java.util.concurrent.ForkJoinPool pool = java.util.concurrent.ForkJoinPool.commonPool();
        java.util.concurrent.RecursiveTask<Real[][]> evenTask = new java.util.concurrent.RecursiveTask<>() {
            @Override
            protected Real[][] compute() {
                return distributedFFT(evenReal, evenImag);
            }
        };
        java.util.concurrent.RecursiveTask<Real[][]> oddTask = new java.util.concurrent.RecursiveTask<>() {
            @Override
            protected Real[][] compute() {
                return distributedFFT(oddReal, oddImag);
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
    private static Real[][] computeLocalFFT(Real[] real, Real[] imag) {
        // Use JScience DiscreteFourierTransform
        Complex[] data = new Complex[real.length];
        for (int i = 0; i < real.length; i++) {
            data[i] = Complex.of(real[i], imag[i]);
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
}