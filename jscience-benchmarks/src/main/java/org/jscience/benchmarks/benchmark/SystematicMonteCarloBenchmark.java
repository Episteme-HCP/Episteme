package org.jscience.benchmarks.benchmark;

import org.jscience.core.technical.algorithm.MonteCarloProvider;

/**
 * A benchmark that systematically tests all available MonteCarloProviders.
 */
public class SystematicMonteCarloBenchmark implements SystematicBenchmark<MonteCarloProvider> {

    private MonteCarloProvider currentProvider;

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }
    @Override public String getIdPrefix() { return "mc-systematic"; }
    @Override public String getNameBase() { return "Systematic Monte Carlo"; }
    @Override public Class<MonteCarloProvider> getProviderClass() { return MonteCarloProvider.class; }

    @Override
    public String getDescription() {
        return "Systematically benchmarks all discovered Monte Carlo providers (Local, MultiCore, Parallel, etc.) for Pi estimation.";
    }

    @Override
    public String getDomain() {
        return "Statistics";
    }

    @Override
    public void setup() {
        // Nothing to do
    }

    @Override
    public void setProvider(MonteCarloProvider provider) {
        this.currentProvider = provider;
    }

    @Override
    public void run() {
        if (currentProvider != null) {
            currentProvider.estimatePi(1000000);
        }
    }

    @Override
    public void teardown() {
        // Nothing to do
    }
}
