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

import org.episteme.core.mathematics.numerical.ode.ODEProvider;

import java.util.function.BiFunction;

/**
 * A benchmark that systematically tests all available ODESolverProviders.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
@AutoService(RunnableBenchmark.class)
public class SystematicODEBenchmark implements SystematicBenchmark<ODEProvider> {

    private ODEProvider currentProvider;
    private final double[] y0 = {1.0, 0.0};
    private final double t0 = 0.0;
    private final double t1 = 10.0;
    private final int steps = 1000;

    /**
     * Harmonic Oscillator: 
     * dy1/dt = y2
     * dy2/dt = -y1
     */
    private final BiFunction<Double, double[], double[]> f = (t, y) -> new double[]{y[1], -y[0]};

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }
    @Override public String getIdPrefix() { return "ode-systematic"; }
    @Override public String getNameBase() { return "Systematic ODE Solver"; }
    @Override public String getDescription() { return "ODE solver (harmonic oscillator, Runge-Kutta integration, 10k steps)"; }
    @Override public String getDomain() { return "Mathematics"; }
    @Override public Class<ODEProvider> getProviderClass() { return ODEProvider.class; }

    @Override
    public void setup() {
        // Simple initial conditions, no heavy setup needed.
    }

    @Override
    public void setProvider(ODEProvider provider) {
        this.currentProvider = provider;
    }

    @Override
    public void run() {
        if (currentProvider != null) {
            currentProvider.solve(f, y0, t0, t1, steps);
        }
    }

    @Override
    public void teardown() {
    }
}
