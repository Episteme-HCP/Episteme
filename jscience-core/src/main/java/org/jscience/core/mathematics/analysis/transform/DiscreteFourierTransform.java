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

package org.jscience.core.mathematics.analysis.transform;

import org.jscience.core.mathematics.analysis.VectorFunction;
import org.jscience.core.mathematics.numbers.complex.Complex;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.vectors.DenseVector; // Assuming this exists or will be found
import java.util.ArrayList;
import java.util.List;

/**
 * Discrete Fourier Transform (DFT) using Fast Fourier Transform (FFT)
 * algorithm.
 * <p>
 * Maps a vector of N complex numbers (time domain) to N complex numbers
 * (frequency domain).
 * </p>
 * * <p>
 * <b>Reference:</b><br>
 * Bracewell, R. N. (1999). <i>The Fourier Transform and Its Applications</i>. McGraw-Hill.
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
        if ((n & (n - 1)) != 0) {
            throw new IllegalArgumentException("Input dimension must be power of 2 for FFT");
        }

        Complex[] x = new Complex[n];
        for (int i = 0; i < n; i++) x[i] = input.get(i);

        Complex[] y = fft(x, inverse);

        List<Complex> list = new ArrayList<>();
        for (Complex c : y) list.add(c);
        return DenseVector.valueOf(list);
    }

    private Complex[] fft(Complex[] x, boolean inv) {
        int n = x.length;
        
        // Try to use optimized FFT if available
        org.jscience.core.technical.algorithm.FFTProvider provider = 
            org.jscience.core.technical.algorithm.AlgorithmManager.getProvider(org.jscience.core.technical.algorithm.FFTProvider.class);
            
        if (provider != null) {
            double[] real = new double[n];
            double[] imag = new double[n];
            for (int i = 0; i < n; i++) {
                real[i] = x[i].real();
                imag[i] = x[i].imaginary();
            }
            
            double[][] result = inv ? provider.inverseTransform(real, imag) : provider.transform(real, imag);
            
            Complex[] res = new Complex[n];
            for (int i = 0; i < n; i++) {
                res[i] = Complex.of(result[0][i], result[1][i]);
            }
            return res;
        }

        // Fallback to recursive implementation
        if (n == 1) return new Complex[] { x[0] };

        Complex[] even = new Complex[n / 2];
        Complex[] odd = new Complex[n / 2];
        for (int i = 0; i < n / 2; i++) {
            even[i] = x[2 * i];
            odd[i] = x[2 * i + 1];
        }

        Complex[] q = fft(even, inv);
        Complex[] r = fft(odd, inv);

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

        return org.jscience.core.mathematics.linearalgebra.matrices.DenseMatrix.of(rows,
                org.jscience.core.mathematics.sets.Complexes.getInstance());
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


