package org.jscience.benchmarks.benchmark.benchmarks;

import com.google.auto.service.AutoService;
import org.jscience.benchmarks.benchmark.RunnableBenchmark;
import org.jscience.core.physics.fluids.LatticeBoltzmannProvider;

import java.util.ServiceLoader;

/**
 * Systematic benchmark for Lattice Boltzmann Method (LBM) providers.
 * Tests fluid dynamics simulation on a 2D lattice.
 */
@AutoService(RunnableBenchmark.class)
public class SystematicLatticeBoltzmannBenchmark implements SystematicBenchmark<LatticeBoltzmannProvider> {

    private LatticeBoltzmannProvider provider;
    private double[][][] distributionFunction;
    private boolean[][] obstacles;
    private static final int GRID_SIZE = 64;
    private static final int NUM_VELOCITIES = 9; // D2Q9 lattice

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }

    @Override
    public Class<LatticeBoltzmannProvider> getProviderClass() {
        return LatticeBoltzmannProvider.class;
    }

    @Override
    public String getIdPrefix() {
        return "lbm";
    }

    @Override
    public String getNameBase() {
        return "Lattice Boltzmann";
    }

    @Override
    public void setProvider(LatticeBoltzmannProvider provider) {
        this.provider = provider;
    }

    @Override
    public void setup() {
        if (provider == null) {
            ServiceLoader<LatticeBoltzmannProvider> loader = ServiceLoader.load(LatticeBoltzmannProvider.class);
            provider = loader.findFirst().orElse(null);
        }
        
        if (provider == null || !provider.isAvailable()) {
            throw new UnsupportedOperationException("No Lattice Boltzmann provider available");
        }

        // Initialize D2Q9 lattice (64x64 grid, 9 velocity directions)
        distributionFunction = new double[GRID_SIZE][GRID_SIZE][NUM_VELOCITIES];
        obstacles = new boolean[GRID_SIZE][GRID_SIZE];
        
        // Initialize with equilibrium distribution
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                for (int i = 0; i < NUM_VELOCITIES; i++) {
                    distributionFunction[x][y][i] = 1.0 / NUM_VELOCITIES;
                }
                obstacles[x][y] = false;
            }
        }
        
        // Add a circular obstacle in the center
        int centerX = GRID_SIZE / 2;
        int centerY = GRID_SIZE / 2;
        int radius = GRID_SIZE / 8;
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                int dx = x - centerX;
                int dy = y - centerY;
                if (dx * dx + dy * dy < radius * radius) {
                    obstacles[x][y] = true;
                }
            }
        }
    }

    @Override
    public void run() {
        // Perform one LBM evolution step with relaxation parameter omega=1.0
        provider.evolve(distributionFunction, obstacles, 1.0);
    }

    @Override
    public void teardown() {
        distributionFunction = null;
        obstacles = null;
    }

    @Override
    public String getDomain() {
        return "Computational Fluid Dynamics";
    }

    @Override
    public String getDescription() {
        return "LBM fluid simulation on " + GRID_SIZE + "x" + GRID_SIZE + " D2Q9 lattice";
    }

    @Override
    public String getAlgorithmProvider() {
        return provider != null ? provider.getName() : "Unknown";
    }

    @Override
    public int getSuggestedIterations() {
        return 20; // LBM is computationally expensive
    }
}
