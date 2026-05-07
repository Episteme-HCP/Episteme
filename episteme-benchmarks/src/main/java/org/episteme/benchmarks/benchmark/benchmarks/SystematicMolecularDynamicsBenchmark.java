package org.episteme.benchmarks.benchmark.benchmarks;

import com.google.auto.service.AutoService;
import org.episteme.benchmarks.benchmark.RunnableBenchmark;
import org.episteme.natural.physics.classical.matter.molecular.MolecularDynamicsProvider;

import java.util.ServiceLoader;

/**
 * Systematic benchmark for Molecular Dynamics (MD) providers.
 * Tests Lennard-Jones force calculation and integration.
 */
@AutoService(RunnableBenchmark.class)
public class SystematicMolecularDynamicsBenchmark implements SystematicBenchmark<MolecularDynamicsProvider> {

    private MolecularDynamicsProvider provider;
    private double[] positions;
    private double[] velocities;
    private double[] forces;
    private double[] masses;
    private static final int NUM_PARTICLES = 100;

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }

    @Override
    public Class<MolecularDynamicsProvider> getProviderClass() {
        return MolecularDynamicsProvider.class;
    }

    @Override
    public String getIdPrefix() {
        return "md";
    }

    @Override
    public String getNameBase() {
        return "Molecular Dynamics";
    }

    @Override
    public void setProvider(MolecularDynamicsProvider provider) {
        this.provider = provider;
    }

    @Override
    public void setup() {
        if (provider == null) {
            ServiceLoader<MolecularDynamicsProvider> loader = ServiceLoader.load(MolecularDynamicsProvider.class);
            provider = loader.findFirst().orElse(null);
        }
        
        if (provider == null || !provider.isAvailable()) {
            throw new UnsupportedOperationException("No Molecular Dynamics provider available");
        }

        // Initialize particle system (100 particles in 3D)
        positions = new double[NUM_PARTICLES * 3];
        velocities = new double[NUM_PARTICLES * 3];
        forces = new double[NUM_PARTICLES * 3];
        masses = new double[NUM_PARTICLES];
        
        // Random initial positions and velocities
        for (int i = 0; i < NUM_PARTICLES * 3; i++) {
            positions[i] = Math.random() * 10.0;
            velocities[i] = (Math.random() - 0.5) * 2.0;
        }
        
        for (int i = 0; i < NUM_PARTICLES; i++) {
            masses[i] = 1.0; // Unit mass
        }
    }

    @Override
    public void run() {
        // Calculate Lennard-Jones forces
        provider.calculateNonBondedForces(positions, forces, 1.0, 1.0, 3.0);
        
        // Integrate one timestep
        provider.integrate(positions, velocities, forces, masses, 0.001, 0.0);
    }

    @Override
    public void teardown() {
        positions = null;
        velocities = null;
        forces = null;
        masses = null;
    }

    @Override
    public String getDomain() {
        return "Molecular Dynamics";
    }

    @Override
    public String getDescription() {
        return "Simulates " + NUM_PARTICLES + " particles with Lennard-Jones potential";
    }

    @Override
    public String getAlgorithmProvider() {
        return provider != null ? provider.getName() : "Unknown";
    }

    @Override
    public int getSuggestedIterations() {
        return 50; // MD is computationally intensive
    }
}
