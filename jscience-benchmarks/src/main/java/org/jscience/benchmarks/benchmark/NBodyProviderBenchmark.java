package org.jscience.benchmarks.benchmark;

import org.openjdk.jmh.annotations.*;

import org.jscience.core.technical.algorithm.NBodyProvider;
import org.jscience.core.technical.algorithm.nbody.MulticoreNBodyProvider;

import java.util.concurrent.TimeUnit;
import java.util.Random;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class NBodyProviderBenchmark {

    @Param({"100", "500"})
    public int numBodies;

    private NBodyProvider multicoreProvider;
    
    private double[] masses;
    private double[] positions;
    private double[] forces;
    private double G;
    private double softening;

    @Setup(Level.Trial)
    public void doSetup() {
        multicoreProvider = new MulticoreNBodyProvider();
        
        masses = new double[numBodies];
        positions = new double[numBodies * 3]; // x, y, z for each body
        forces = new double[numBodies * 3];
        G = 6.67430e-11;
        softening = 1e18;
        
        Random rand = new Random(42);
        for(int i=0; i<numBodies; i++) {
            masses[i] = rand.nextDouble() * 1000;
            positions[i*3] = rand.nextDouble() * 100;
            positions[i*3+1] = rand.nextDouble() * 100;
            positions[i*3+2] = rand.nextDouble() * 100;
        }
    }

    @Benchmark
    public void benchmarkMulticoreNBody() {
        multicoreProvider.computeForces(positions, masses, forces, G, softening);
    }
}
