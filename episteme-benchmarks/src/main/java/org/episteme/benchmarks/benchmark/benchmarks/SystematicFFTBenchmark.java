/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.benchmarks.benchmark.benchmarks;

import org.episteme.benchmarks.benchmark.RunnableBenchmark;

import com.google.auto.service.AutoService;

import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.analysis.fft.FFTProvider;
import java.util.Random;

/**
 * A benchmark that systematically tests all available FFTProviders.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
@AutoService(RunnableBenchmark.class)
public class SystematicFFTBenchmark implements SystematicBenchmark<FFTProvider> {

    private static final int SIZE = 4096;
    private static final int POOL_SIZE = 10;
    private Complex[][] dataPool;
    private FFTProvider currentProvider;

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }
    @Override public String getIdPrefix() { return "fft-systematic"; }
    @Override public String getNameBase() { return "Systematic FFT"; }
    @Override public Class<FFTProvider> getProviderClass() { return FFTProvider.class; }

    @Override
    public String getDescription() {
        return "Fast Fourier Transform (4096 complex elements, forward + inverse)";
    }

    @Override
    public String getDomain() {
        return "Signal Processing";
    }

    @Override
    public void setup() {
        dataPool = new Complex[POOL_SIZE][];
        Random r = new Random(42);
        for (int i = 0; i < POOL_SIZE; i++) {
            dataPool[i] = generateData(SIZE, r);
        }
    }

    @Override
    public void setProvider(FFTProvider provider) {
        this.currentProvider = provider;
    }

    @Override
    public void run() {
        if (currentProvider != null) {
            for (int i = 0; i < POOL_SIZE; i++) {
                currentProvider.transformComplex(dataPool[i]);
            }
        }
    }

    @Override
    public void teardown() {
        dataPool = null;
    }

    @Override
    public int getSuggestedIterations() {
        return 100; // Each iteration now does 10 FFTs
    }

    private Complex[] generateData(int n, Random r) {
        Complex[] d = new Complex[n];
        for (int i = 0; i < n; i++)
            d[i] = Complex.of(r.nextDouble(), r.nextDouble());
        return d;
    }
}
