package org.jscience.core.technical.algorithm;

import java.util.Map;

/**
 * Represents the results of a benchmark for a specific provider.
 */
public class BenchmarkResult {
    private String providerName;
    private String environment;
    private Map<Integer, Double> gflops; // Size -> GFLOPS

    public BenchmarkResult() {}

    public BenchmarkResult(String providerName, String environment, Map<Integer, Double> gflops) {
        this.providerName = providerName;
        this.environment = environment;
        this.gflops = gflops;
    }

    public String getProviderName() { return providerName; }
    public void setProviderName(String providerName) { this.providerName = providerName; }

    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }

    public Map<Integer, Double> getGflops() { return gflops; }
    public void setGflops(Map<Integer, Double> gflops) { this.gflops = gflops; }
}
