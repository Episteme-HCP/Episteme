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

package org.jscience.benchmarks.benchmark;

import org.jscience.core.mathematics.numbers.complex.Complex;
import org.jscience.core.mathematics.linearalgebra.Vector;

import org.jscience.core.ui.i18n.I18N;
import java.util.Random;

/**
 * Benchmarks for Fast Fourier Transform (FFT) operations.
 * <p>
 * Compares performance of local and distributed FFT implementations.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FFTBenchmark implements RunnableBenchmark {

    private static final int SIZE = 4096; // Power of 2
    private Complex[] input;
    private Vector<Complex> vector;

    @Override
    public String getName() {
        return "FFT (Size 4096)";
    }

    @Override
    public String getDomain() {
        return "Signal Processing";
    }

    @Override
    public void setup() {
        input = createRandomData(SIZE);
        vector = new org.jscience.core.mathematics.linearalgebra.vectors.DenseVector<>(java.util.Arrays.asList(input), org.jscience.core.mathematics.sets.Complexes.getInstance());
    }

    @Override
    public void run() {
        org.jscience.core.mathematics.analysis.transform.AlgebraicFFT.transform(vector);
    }

    @Override
    public void teardown() {
        input = null;
        vector = null;
    }

    @Override
    public int getSuggestedIterations() {
        return 100;
    }

    /**
     * @deprecated Use standard RunnableBenchmark runner
     */
    @Deprecated
    public static void execute() {
        FFTBenchmark b = new FFTBenchmark();
        b.setup();
        SimpleBenchmarkRunner.run(I18N.getInstance().get("benchmark.fft.jscience", SIZE), b::run);
    }

    private static Complex[] createRandomData(int size) {
        Random r = new Random(42);
        Complex[] data = new Complex[size];
        for (int i = 0; i < size; i++) {
            data[i] = Complex.of(r.nextDouble(), r.nextDouble());
        }
        return data;
    }
}


