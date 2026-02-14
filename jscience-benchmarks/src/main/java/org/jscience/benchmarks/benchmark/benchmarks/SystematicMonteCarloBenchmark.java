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

package org.jscience.benchmarks.benchmark.benchmarks;

import org.jscience.benchmarks.benchmark.RunnableBenchmark;

import com.google.auto.service.AutoService;

import org.jscience.core.technical.algorithm.MonteCarloProvider;

/**
 * A benchmark that systematically tests all available MonteCarloAlgorithmProviders.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
@AutoService(RunnableBenchmark.class)
public class SystematicMonteCarloBenchmark implements SystematicBenchmark<MonteCarloProvider> {

    private MonteCarloProvider currentProvider;

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }
    @Override public String getIdPrefix() { return "mc-systematic"; }
    @Override public String getNameBase() { return "Systematic Monte Carlo"; }
    @Override public Class<MonteCarloProvider> getProviderClass() { return MonteCarloProvider.class; }

    @Override
    public String getDescription() {
        return "Systematically benchmarks all discovered Monte Carlo providers (Local, MultiCore, Parallel, etc.) for Pi estimation.";
    }

    @Override
    public String getDomain() {
        return "Statistics";
    }

    @Override
    public void setup() {
        // Nothing to do
    }

    @Override
    public void setProvider(MonteCarloProvider provider) {
        this.currentProvider = provider;
    }

    @Override
    public void run() {
        if (currentProvider != null) {
            currentProvider.estimatePi(1000000);
        }
    }

    @Override
    public void teardown() {
        // Nothing to do
    }
}
