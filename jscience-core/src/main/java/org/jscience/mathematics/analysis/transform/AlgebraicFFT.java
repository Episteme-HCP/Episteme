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

package org.jscience.mathematics.analysis.transform;

import org.jscience.mathematics.numbers.complex.Complex;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.sets.Complexes;
import org.jscience.mathematics.numbers.real.Real;
import java.util.ArrayList;
import java.util.List;

/**
 * Algebraic Fast Fourier Transform (FFT).
 * <p>
 * Uses the Cooley-Tukey algorithm for efficient computation of the Discrete
 * Fourier Transform (DFT). Designed for mathematical `Vector<Complex>` objects.
 * </p>
 * <p>
 * Formerly known as FastFourierTransform.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class AlgebraicFFT {

    private AlgebraicFFT() {
        // Utility class
    }

    /**
     * Computes the Forward FFT of a complex vector.
     * <p>
     * The input size must be a power of 2.
     * </p>
     * 
     * @param input the input vector of complex numbers
     * @return the transformed vector
     * @throws IllegalArgumentException if input size is not a power of 2
     */
    public static Vector<Complex> transform(Vector<Complex> input) {
        int n = input.dimension();
        if (n == 0)
            return input;
        if ((n & (n - 1)) != 0) {
            throw new IllegalArgumentException("Input size must be a power of 2");
        }

        // Base case
        if (n == 1) {
            return input;
        }

        // Recursive step
        // Split into even and odd
        List<Complex> evenList = new ArrayList<>(n / 2);
        List<Complex> oddList = new ArrayList<>(n / 2);
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) {
                evenList.add(input.get(i));
            } else {
                oddList.add(input.get(i));
            }
        }

        Vector<Complex> even = new DenseVector<>(evenList, Complexes.getInstance());
        Vector<Complex> odd = new DenseVector<>(oddList, Complexes.getInstance());

        Vector<Complex> evenFFT = transform(even);
        Vector<Complex> oddFFT = transform(odd);

        List<Complex> resultList = new ArrayList<>(n);
        // Initialize list with nulls
        for (int i = 0; i < n; i++)
            resultList.add(null);

        for (int k = 0; k < n / 2; k++) {
            double kth = -2 * Math.PI * k / n;
            Complex w = Complex.of(Math.cos(kth), Math.sin(kth));
            Complex oddPart = w.multiply(oddFFT.get(k));

            resultList.set(k, evenFFT.get(k).add(oddPart));
            resultList.set(k + n / 2, evenFFT.get(k).subtract(oddPart));
        }

        return new DenseVector<>(resultList, Complexes.getInstance());
    }

    /**
     * Computes the Inverse FFT of a complex vector.
     * 
     * @param input the input vector of complex numbers
     * @return the inverse transformed vector
     */
    public static Vector<Complex> inverseTransform(Vector<Complex> input) {
        int n = input.dimension();

        // Conjugate input
        List<Complex> conjugatedInput = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            conjugatedInput.add(input.get(i).conjugate());
        }

        // Forward FFT
        Vector<Complex> forward = transform(new DenseVector<>(conjugatedInput, Complexes.getInstance()));

        // Conjugate again and scale by 1/n
        List<Complex> resultList = new ArrayList<>(n);
        Real nReal = Real.of(n);
        for (int i = 0; i < n; i++) {
            resultList.add(forward.get(i).conjugate().divide(Complex.of(nReal, Real.ZERO)));
        }

        return new DenseVector<>(resultList, Complexes.getInstance());
    }
}
