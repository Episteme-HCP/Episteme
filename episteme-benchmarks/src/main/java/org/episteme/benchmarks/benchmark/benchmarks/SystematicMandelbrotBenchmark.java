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

import org.episteme.core.mathematics.analysis.fractals.MandelbrotProvider;

/**
 * A benchmark that systematically tests all available MandelbrotProviders.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
@AutoService(RunnableBenchmark.class)
public class SystematicMandelbrotBenchmark implements SystematicBenchmark<MandelbrotProvider> {

    private MandelbrotProvider currentProvider;
    private final double xMin = -2.0;
    private final double xMax = 0.5;
    private final double yMin = -1.25;
    private final double yMax = 1.25;
    private final int width = 1000;
    private final int height = 1000;
    private final int maxIterations = 256;

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }
    @Override public String getIdPrefix() { return "mandelbrot-systematic"; }
    @Override public String getNameBase() { return "Systematic Mandelbrot Generation"; }
    @Override public String getDescription() { return "Mandelbrot set generation (800x800 viewport, 1000 max iterations)"; }
    @Override public String getDomain() { return "Visualization"; }
    @Override public Class<MandelbrotProvider> getProviderClass() { return MandelbrotProvider.class; }

    @Override
    public void setup() {
        // No heavy setup needed for Mandelbrot compute.
    }

    @Override
    public void setProvider(MandelbrotProvider provider) {
        this.currentProvider = provider;
    }

    @Override
    public void run() {
        if (currentProvider != null) {
            currentProvider.compute(xMin, xMax, yMin, yMax, width, height, maxIterations);
        }
    }

    @Override
    public void teardown() {
    }
}
