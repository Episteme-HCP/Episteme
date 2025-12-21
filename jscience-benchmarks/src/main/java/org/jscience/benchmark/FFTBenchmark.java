/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.benchmark;

import org.jscience.mathematics.analysis.transform.FastFourierTransform;
import org.jscience.mathematics.numbers.complex.Complex;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.sets.Complexes;

import java.util.Random;

/**
 * Benchmarks for Fast Fourier Transform (FFT) operations.
 * <p>
 * Compares performance of local and distributed FFT implementations.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FFTBenchmark {

    private static final int SIZE = 4096; // Power of 2

    public static void run() {
        System.out.println("\n--- FFT Benchmarks ---");

        benchmarkBasicFFT(SIZE);
        benchmarkJScienceFFT(SIZE);
    }

    private static void benchmarkBasicFFT(int size) {
        Complex[] input = createRandomData(size);

        SimpleBenchmarkRunner.run("Basic Recursive FFT (" + size + " points)", () -> {
            basicFFT(input);
        });
    }

    private static void benchmarkJScienceFFT(int size) {
        Complex[] input = createRandomData(size);
        Vector<Complex> vector = new DenseVector<Complex>(java.util.Arrays.asList(input), Complexes.getInstance());

        SimpleBenchmarkRunner.run("JScience FFT (" + size + " points)", () -> {
            FastFourierTransform.transform(vector);
        });
    }

    private static Complex[] createRandomData(int size) {
        Random r = new Random(42);
        Complex[] data = new Complex[size];
        for (int i = 0; i < size; i++) {
            data[i] = Complex.of(r.nextDouble(), r.nextDouble());
        }
        return data;
    }

    // Simple recursive implementation for comparison
    private static Complex[] basicFFT(Complex[] x) {
        int n = x.length;
        if (n == 1)
            return new Complex[] { x[0] };

        if (n % 2 != 0)
            throw new IllegalArgumentException("n is not a power of 2");

        Complex[] even = new Complex[n / 2];
        Complex[] odd = new Complex[n / 2];
        for (int i = 0; i < n / 2; i++) {
            even[i] = x[2 * i];
            odd[i] = x[2 * i + 1];
        }

        Complex[] q = basicFFT(even);
        Complex[] r = basicFFT(odd);

        Complex[] y = new Complex[n];
        for (int k = 0; k < n / 2; k++) {
            double kth = -2 * k * Math.PI / n;
            Complex wk = Complex.of(Math.cos(kth), Math.sin(kth));
            y[k] = q[k].add(wk.multiply(r[k]));
            y[k + n / 2] = q[k].subtract(wk.multiply(r[k]));
        }
        return y;
    }
}