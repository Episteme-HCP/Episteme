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

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import java.util.ArrayList;
import java.util.List;

/**
 * Haar Wavelet Transform.
 * <p>
 * The Haar wavelet is the simplest possible wavelet.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class WaveletTransform {

    private WaveletTransform() {
        // Utility class
    }

    /**
     * Computes the forward Haar Wavelet Transform.
     * <p>
     * The input size must be a power of 2.
     * </p>
     * 
     * @param input the input vector of real numbers
     * @return the transformed vector
     * @throws IllegalArgumentException if input size is not a power of 2
     */
    public static Vector<Real> haarTransform(Vector<Real> input) {
        int n = input.dimension();
        if (n == 0)
            return input;
        if ((n & (n - 1)) != 0) {
            throw new IllegalArgumentException("Input size must be a power of 2");
        }

        List<Real> current = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            current.add(input.get(i));
        }

        List<Real> result = new ArrayList<>(current);

        // In-place-ish computation layer by layer
        int len = n;
        while (len > 1) {
            List<Real> nextLayer = new ArrayList<>(len / 2);
            List<Real> coeffs = new ArrayList<>(len / 2);

            for (int i = 0; i < len; i += 2) {
                Real a = result.get(i);
                Real b = result.get(i + 1);

                // Average (Approximation)
                Real avg = a.add(b).divide(Real.of(2.0));
                // Difference (Detail)
                Real diff = a.subtract(b).divide(Real.of(2.0));

                nextLayer.add(avg);
                coeffs.add(diff);
            }

            // Update result array: [Approximations ... | Details ...]
            for (int i = 0; i < len / 2; i++) {
                result.set(i, nextLayer.get(i));
                result.set(i + len / 2, coeffs.get(i));
            }

            len /= 2;
        }

        return new DenseVector<>(result, null);
    }

    /**
     * Computes the inverse Haar Wavelet Transform.
     * 
     * @param input the transformed vector
     * @return the reconstructed vector
     */
    public static Vector<Real> inverseHaarTransform(Vector<Real> input) {
        int n = input.dimension();
        if (n == 0)
            return input;

        List<Real> result = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            result.add(input.get(i));
        }

        int len = 2;
        while (len <= n) {
            List<Real> newValues = new ArrayList<>(len);

            for (int i = 0; i < len / 2; i++) {
                Real avg = result.get(i);
                Real diff = result.get(i + len / 2);

                // Reconstruct
                // a = avg + diff
                // b = avg - diff
                Real a = avg.add(diff);
                Real b = avg.subtract(diff);

                newValues.add(a);
                newValues.add(b);
            }

            // Update result array
            // We need to place these new values at the beginning
            // But wait, the order in the loop above produces a, b, a, b...
            // which corresponds to indices 0, 1, 2, 3...
            for (int i = 0; i < len; i++) {
                result.set(i, newValues.get(i));
            }

            len *= 2;
        }

        return new DenseVector<>(result, null);
    }
}