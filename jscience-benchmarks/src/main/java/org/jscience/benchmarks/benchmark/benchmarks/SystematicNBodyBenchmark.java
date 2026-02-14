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

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.algorithm.NBodyProvider;

/**
 * A benchmark that systematically tests all available NBodyAlgorithmProviders.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
@AutoService(RunnableBenchmark.class)
public class SystematicNBodyBenchmark implements SystematicBenchmark<NBodyProvider> {

    private static final int N = 128;
    private Real[] masses;
    private Real[] positions;
    private Real[] velocities;
    private NBodyProvider currentProvider;

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }
    @Override public String getIdPrefix() { return "nbody-systematic"; }
    @Override public String getNameBase() { return "Systematic N-Body"; }
    @Override public Class<NBodyProvider> getProviderClass() { return NBodyProvider.class; }

    @Override
    public String getDescription() {
        return "Systematically benchmarks all discovered N-Body providers (Local, SIMD, Native, etc.) on a " + N + " body simulation.";
    }

    @Override
    public String getDomain() {
        return "Physics";
    }

    @Override
    public void setup() {
        masses = new Real[N];
        positions = new Real[N * 3];
        velocities = new Real[N * 3];
        
        java.util.Random r = new java.util.Random(42);
        for (int i = 0; i < N; i++) {
            masses[i] = Real.of(r.nextDouble() * 1e24);
            positions[i*3] = Real.of(r.nextDouble() * 1e11);
            positions[i*3+1] = Real.of(r.nextDouble() * 1e11);
            positions[i*3+2] = Real.of(r.nextDouble() * 1e11);
            velocities[i*3] = Real.of(r.nextDouble() * 30000);
            velocities[i*3+1] = Real.of(r.nextDouble() * 30000);
            velocities[i*3+2] = Real.of(r.nextDouble() * 30000);
        }
    }

    @Override
    public void setProvider(NBodyProvider provider) {
        this.currentProvider = provider;
    }

    @Override
    public void run() {
        if (currentProvider != null) {
            Real dt = Real.of(3600.0);
            Real G = Real.of(6.67430e-11);
            Real softening = Real.of(1e9);
            currentProvider.step(masses, positions, velocities, dt, G, softening);
        }
    }

    @Override
    public void teardown() {
        masses = null;
        positions = null;
        velocities = null;
    }
}
