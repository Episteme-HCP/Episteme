package org.jscience.benchmarks.benchmark;

import org.jscience.core.technical.algorithm.ODEProvider;

import java.util.function.BiFunction;

/**
 * A benchmark that systematically tests all available ODEProviders.
 */
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
    @Override public String getDescription() { return "Systematically benchmarks Ordinary Differential Equation (ODE) solvers using a harmonic oscillator simulation."; }
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
