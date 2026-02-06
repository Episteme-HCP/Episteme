package org.jscience.benchmarks.benchmark;

import org.jscience.core.technical.algorithm.GeneticAlgorithmProvider;

import java.util.function.Function;

/**
 * A benchmark that systematically tests all available GeneticAlgorithmProviders.
 */
public class SystematicGeneticBenchmark implements SystematicBenchmark<GeneticAlgorithmProvider> {

    private GeneticAlgorithmProvider currentProvider;
    private int dimensions = 10;
    private int populationSize = 50;
    private int generations = 100;
    private double mutationRate = 0.01;

    /**
     * Sphere function: f(x) = sum(x_i^2).
     * This is a standard continuous optimization benchmark.
     */
    private final Function<double[], Double> fitnessFunction = x -> {
        double sum = 0;
        for (double val : x) {
            sum += val * val;
        }
        return sum;
    };

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }
    @Override public String getIdPrefix() { return "genetic-systematic"; }
    @Override public String getNameBase() { return "Systematic Genetic Optimization"; }
    @Override public String getDescription() { return "Systematically benchmarks genetic algorithm performance and convergence efficiency using the Sphere function optimization."; }
    @Override public String getDomain() { return "Optimization"; }
    @Override public Class<GeneticAlgorithmProvider> getProviderClass() { return GeneticAlgorithmProvider.class; }

    @Override
    public void setup() {
        // No heavy setup needed for continuous optimization benchmark
    }

    @Override
    public void setProvider(GeneticAlgorithmProvider provider) {
        this.currentProvider = provider;
    }

    @Override
    public void run() {
        if (currentProvider != null) {
            currentProvider.solve(fitnessFunction, dimensions, populationSize, generations, mutationRate);
        }
    }

    @Override
    public void teardown() {
    }
}
