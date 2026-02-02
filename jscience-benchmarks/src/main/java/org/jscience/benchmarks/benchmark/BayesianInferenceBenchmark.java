package org.jscience.benchmarks.benchmark;

import org.openjdk.jmh.annotations.*;
import org.jscience.core.mathematics.ml.BayesianBeliefNetwork;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BayesianInferenceBenchmark {

    // Simple network structure hardcoded for benchmark reuse
    private BayesianBeliefNetwork network; 
    private Map<String, String> evidence;

    @Setup(Level.Trial)
    public void doSetup() {
        
        network = new BayesianBeliefNetwork();
        // Add variables
        network.addVariable("Cloudy", "True", "False");
        network.addVariable("Sprinkler", "On", "Off");
        network.addVariable("Rain", "True", "False");
        network.addVariable("WetGrass", "True", "False");
        
        // Add dependencies
        network.addDependency("Cloudy", "Sprinkler");
        network.addDependency("Cloudy", "Rain");
        network.addDependency("Sprinkler", "WetGrass");
        network.addDependency("Rain", "WetGrass");
        
        // Set CPTs
        network.setCPT("Cloudy", 0.5, 0.5);
        network.setCPT("Sprinkler", 0.1, 0.9, 0.5, 0.5);
        network.setCPT("Rain", 0.8, 0.2, 0.2, 0.8);
        network.setCPT("WetGrass", 0.99, 0.01, 0.9, 0.1, 0.9, 0.1, 0.0, 1.0);
        
        evidence = new HashMap<>();
        evidence.put("WetGrass", "True");
    }

    @Benchmark
    public org.jscience.core.mathematics.numbers.real.Real benchmarkJavaInference() {
        return network.query("Rain", "True", evidence);
    }

    @Benchmark
    public org.jscience.core.mathematics.numbers.real.Real benchmarkNativeInference() {
        // Native provider not yet implemented, fallback to Java
        return network.query("Rain", "True", evidence);
    }
}
