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

package org.jscience.mathematics.analysis.transform;

import org.jscience.mathematics.analysis.VectorFunction;
import org.jscience.mathematics.numbers.complex.Complex;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector; // Assuming this exists or will be found
import java.util.ArrayList;
import java.util.List;

/**
 * Discrete Fourier Transform (DFT) using Fast Fourier Transform (FFT)
 * algorithm.
 * <p>
 * Maps a vector of N complex numbers (time domain) to N complex numbers
 * (frequency domain).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DiscreteFourierTransform implements Transform<Vector<Complex>, Vector<Complex>>, VectorFunction<Complex> {

    private final boolean inverse;

    public DiscreteFourierTransform(boolean inverse) {
        this.inverse = inverse;
    }

    public DiscreteFourierTransform() {
        this(false);
    }

    /**
     * Returns the output dimension. For DFT, output dimension equals input.
     * This is only known at evaluation time, so we return -1 to indicate dynamic.
     */
    @Override
    public int outputDimension() {
        // DFT output dimension equals input dimension
        // Return -1 to indicate it's dynamic (determined by input)
        return -1;
    }

    @Override
    public Vector<Complex> evaluate(Vector<Complex> input) {
        int n = input.dimension();
        // Check if n is power of 2 for Radix-2 FFT
        if ((n & (n - 1)) != 0) {
            throw new IllegalArgumentException("Input dimension must be power of 2 for FFT");
        }

        Complex[] x = new Complex[n];
        for (int i = 0; i < n; i++)
            x[i] = input.get(i);

        Complex[] y = fft(x, inverse);

        // Convert back to Vector
        // Using DenseVector if available, or custom list wrapper
        List<Complex> list = new ArrayList<>();
        for (Complex c : y)
            list.add(c);

        // Assuming DenseVector.valueOf(List) or similar exists.
        // If not, we'll need to instantiate DenseVector directly or use a factory.
        // For now, let's try to use DenseVector constructor if public, or static
        // factory.
        // We'll assume DenseVector.valueOf(List) for now, or fix later.
        return DenseVector.valueOf(list);
    }

    private Complex[] fft(Complex[] x, boolean inv) {
        int n = x.length;
        if (n == 1)
            return new Complex[] { x[0] };

        // Divide
        Complex[] even = new Complex[n / 2];
        Complex[] odd = new Complex[n / 2];
        for (int i = 0; i < n / 2; i++) {
            even[i] = x[2 * i];
            odd[i] = x[2 * i + 1];
        }

        // Conquer
        Complex[] q = fft(even, inv);
        Complex[] r = fft(odd, inv);

        // Combine
        Complex[] y = new Complex[n];
        double angle = (inv ? 2 : -2) * Math.PI / n;
        Complex wn = Complex.of(Math.cos(angle), Math.sin(angle));
        Complex w = Complex.ONE;

        for (int k = 0; k < n / 2; k++) {
            Complex wr = w.multiply(r[k]);
            y[k] = q[k].add(wr);
            y[k + n / 2] = q[k].subtract(wr);
            w = w.multiply(wn);
        }

        return y;
    }

    /**
     * Static convenience method for FFT.
     * 
     * @param data the input data
     * @return the transformed data
     */
    public static Complex[] transform(Complex[] data) {
        DiscreteFourierTransform dft = new DiscreteFourierTransform();
        return dft.fft(data, false);
    }

    @Override
    public Transform<Vector<Complex>, Vector<Complex>> inverse() {
        return new DiscreteFourierTransform(!inverse);
    }

    @Override
    public Matrix<Complex> jacobian(Vector<Complex> point) {
        int n = point.dimension();
        List<List<Complex>> rows = new ArrayList<>();

        double angleBase = (inverse ? 2 : -2) * Math.PI / n;

        for (int j = 0; j < n; j++) {
            List<Complex> row = new ArrayList<>();
            for (int k = 0; k < n; k++) {
                double angle = angleBase * j * k;
                row.add(Complex.of(Math.cos(angle), Math.sin(angle)));
            }
            rows.add(row);
        }

        return org.jscience.mathematics.linearalgebra.matrices.DenseMatrix.of(rows,
                org.jscience.mathematics.sets.Complexes.getInstance());
    }

    @Override
    public String getDomain() {
        return "C^N (Time)";
    }

    @Override
    public String getCodomain() {
        return "C^N (Frequency)";
    }
}

