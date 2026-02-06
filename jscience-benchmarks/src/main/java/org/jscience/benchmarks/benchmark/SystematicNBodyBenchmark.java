package org.jscience.benchmarks.benchmark;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.algorithm.NBodyProvider;

/**
 * A benchmark that systematically tests all available NBodyProviders.
 */
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
