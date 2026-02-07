package org.jscience.benchmarks.benchmark;

import com.google.auto.service.AutoService;

import org.jscience.core.technical.algorithm.BayesianInferenceProvider;
import org.jscience.core.technical.algorithm.BayesianInferenceProvider.BayesNodeData;

import java.util.*;

/**
 * A benchmark that systematically tests all available BayesianInferenceProviders.
 */
@AutoService(RunnableBenchmark.class)
public class SystematicBayesianInferenceBenchmark implements SystematicBenchmark<BayesianInferenceProvider> {

    private final List<BayesNodeData> nodes = new ArrayList<>();
    private final Map<String, String> evidence = new HashMap<>();
    private BayesianInferenceProvider currentProvider;

    private static class SimpleBayesNodeData implements BayesNodeData {
        private final String name;
        private final List<String> states;
        private final Map<Map<String, String>, Map<String, Double>> cpt = new HashMap<>();

        public SimpleBayesNodeData(String name, String... states) {
            this.name = name;
            this.states = Arrays.asList(states);
        }

        public void putCPT(Map<String, String> parents, String state, double value) {
            cpt.computeIfAbsent(parents, k -> new HashMap<>()).put(state, value);
        }

        @Override public String getName() { return name; }
        @Override public List<String> getStates() { return states; }
        @Override public Map<Map<String, String>, Map<String, Double>> getCPT() { return cpt; }
    }

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }
    @Override public String getIdPrefix() { return "bayesian-systematic"; }
    @Override public String getNameBase() { return "Systematic Bayesian Inference"; }
    @Override public String getDescription() { return "Systematically benchmarks Bayesian Inference using the standard WetGrass network."; }
    @Override public String getDomain() { return "Machine Learning"; }
    @Override public Class<BayesianInferenceProvider> getProviderClass() { return BayesianInferenceProvider.class; }

    @Override
    public void setup() {
        nodes.clear();
        evidence.clear();

        // 1. Cloudy
        SimpleBayesNodeData cloudy = new SimpleBayesNodeData("Cloudy", "True", "False");
        cloudy.putCPT(Collections.emptyMap(), "True", 0.5);
        cloudy.putCPT(Collections.emptyMap(), "False", 0.5);
        nodes.add(cloudy);

        // 2. Sprinkler (depends on Cloudy)
        SimpleBayesNodeData sprinkler = new SimpleBayesNodeData("Sprinkler", "On", "Off");
        sprinkler.putCPT(Map.of("Cloudy", "True"), "On", 0.1);
        sprinkler.putCPT(Map.of("Cloudy", "True"), "Off", 0.9);
        sprinkler.putCPT(Map.of("Cloudy", "False"), "On", 0.5);
        sprinkler.putCPT(Map.of("Cloudy", "False"), "Off", 0.5);
        nodes.add(sprinkler);

        // 3. Rain (depends on Cloudy)
        SimpleBayesNodeData rain = new SimpleBayesNodeData("Rain", "True", "False");
        rain.putCPT(Map.of("Cloudy", "True"), "True", 0.8);
        rain.putCPT(Map.of("Cloudy", "True"), "False", 0.2);
        rain.putCPT(Map.of("Cloudy", "False"), "True", 0.2);
        rain.putCPT(Map.of("Cloudy", "False"), "False", 0.8);
        nodes.add(rain);

        // 4. WetGrass (depends on Sprinkler and Rain)
        SimpleBayesNodeData wetGrass = new SimpleBayesNodeData("WetGrass", "True", "False");
        wetGrass.putCPT(Map.of("Sprinkler", "On", "Rain", "True"), "True", 0.99);
        wetGrass.putCPT(Map.of("Sprinkler", "On", "Rain", "True"), "False", 0.01);
        wetGrass.putCPT(Map.of("Sprinkler", "On", "Rain", "False"), "True", 0.9);
        wetGrass.putCPT(Map.of("Sprinkler", "On", "Rain", "False"), "False", 0.1);
        wetGrass.putCPT(Map.of("Sprinkler", "Off", "Rain", "True"), "True", 0.9);
        wetGrass.putCPT(Map.of("Sprinkler", "Off", "Rain", "True"), "False", 0.1);
        wetGrass.putCPT(Map.of("Sprinkler", "Off", "Rain", "False"), "True", 0.0);
        wetGrass.putCPT(Map.of("Sprinkler", "Off", "Rain", "False"), "False", 1.0);
        nodes.add(wetGrass);

        evidence.put("WetGrass", "True");
    }

    @Override
    public void setProvider(BayesianInferenceProvider provider) {
        this.currentProvider = provider;
    }

    @Override
    public void run() {
        if (currentProvider != null) {
            currentProvider.query("Rain", "True", evidence, nodes);
        }
    }

    @Override
    public void teardown() {
        nodes.clear();
        evidence.clear();
    }
}
