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

import org.jscience.core.technical.algorithm.SPHFluidProvider;

import java.util.Random;

/**
 * A benchmark that systematically tests all available SPHFluidProviders.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
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
    @Override public String getDomain() { return "Simulation"; }
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
