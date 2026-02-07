package org.jscience.benchmarks.benchmark;

import com.google.auto.service.AutoService;

import org.jscience.core.technical.algorithm.SPHFluidProvider;

import java.util.Random;

/**
 * A benchmark that systematically tests all available SPHFluidProviders.
 */
@AutoService(RunnableBenchmark.class)
public class SystematicSPHBenchmark implements SystematicBenchmark<SPHFluidProvider> {

    private SPHFluidProvider currentProvider;
    private final int numParticles = 1000;
    private double[] positions;
    private double[] velocities;
    private double[] densities;
    private double[] pressures;
    private double[] forces;
    private final double dt = 0.01;
    private final double mass = 1.0;
    private final double restDensity = 1000.0;
    private final double stiffness = 2000.0;
    private final double viscosity = 0.1;
    private final double smoothingRadius = 1.0;
    private final double[] gravity = {0.0, -9.81, 0.0};

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }
    @Override public String getIdPrefix() { return "sph-systematic"; }
    @Override public String getNameBase() { return "Systematic SPH Fluid Simulation"; }
    @Override public String getDescription() { return "Systematically benchmarks Smoothed Particle Hydrodynamics (SPH) fluid simulation performance."; }
    @Override public String getDomain() { return "Physics"; }
    @Override public Class<SPHFluidProvider> getProviderClass() { return SPHFluidProvider.class; }

    @Override
    public void setup() {
        positions = new double[numParticles * 3];
        velocities = new double[numParticles * 3];
        densities = new double[numParticles];
        pressures = new double[numParticles];
        forces = new double[numParticles * 3];

        Random rand = new Random(42);
        for (int i = 0; i < numParticles; i++) {
            positions[i * 3] = rand.nextDouble() * 10.0;
            positions[i * 3 + 1] = rand.nextDouble() * 10.0;
            positions[i * 3 + 2] = rand.nextDouble() * 10.0;
            velocities[i * 3] = 0.0;
            velocities[i * 3 + 1] = 0.0;
            velocities[i * 3 + 2] = 0.0;
        }
    }

    @Override
    public void setProvider(SPHFluidProvider provider) {
        this.currentProvider = provider;
    }

    @Override
    public void run() {
        if (currentProvider != null) {
            currentProvider.step(positions, velocities, densities, pressures, forces, 
                    numParticles, dt, mass, restDensity, stiffness, viscosity, smoothingRadius, gravity);
        }
    }

    @Override
    public void teardown() {
        positions = null;
        velocities = null;
        densities = null;
        pressures = null;
        forces = null;
    }
}
