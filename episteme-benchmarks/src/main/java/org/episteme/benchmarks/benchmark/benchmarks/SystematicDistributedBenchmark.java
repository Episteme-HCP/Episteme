package org.episteme.benchmarks.benchmark.benchmarks;

import org.episteme.benchmarks.benchmark.RunnableBenchmark;
import com.google.auto.service.AutoService;
import org.episteme.core.distributed.DistributedComputingProvider;
import org.episteme.core.technical.backend.distributed.DistributedContext;
import java.util.concurrent.Future;

/**
 * Benchmark for Distributed Computing Providers (MPI, Spark).
 * Measures the latency of creating a context and submitting a trivial task.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
@AutoService(RunnableBenchmark.class)
public class SystematicDistributedBenchmark implements SystematicBenchmark<DistributedComputingProvider> {

    private DistributedComputingProvider provider;
    private DistributedContext context;

    @Override public Class<DistributedComputingProvider> getProviderClass() { return DistributedComputingProvider.class; }
    @Override public String getIdPrefix() { return "distributed-latency"; }
    @Override public String getNameBase() { return "Distributed Task Latency"; }

    @Override public String getId() { return getIdPrefix() + "-default"; }
    @Override public String getName() { return getNameBase(); }
    @Override public String getDescription() { return "Measures round-trip latency of task submission on " + (provider != null ? provider.getName() : "default backend"); }
    @Override public String getDomain() { return "Distributed Computing"; }
    @Override public String getAlgorithmType() { return "Task Scheduling"; }
    @Override public String getAlgorithmProvider() { return provider != null ? provider.getName() : "None"; }

    @Override
    public void setProvider(DistributedComputingProvider provider) {
        this.provider = provider;
    }

    @Override
    public boolean isAvailable() {
        return provider != null && provider.isAvailable();
    }

    @Override
    public void setup() {
        if (provider == null) throw new IllegalStateException("Provider not set");
        this.context = provider.createDistributedContext();
    }

    @Override
    public void run() {
        if (context != null) {
            try {
                // Submit a trivial task
                Future<String> future = context.submit(() -> "ping");
                String result = future.get(); // Blocks until completion (Round-trip)
                if (!"ping".equals(result)) throw new RuntimeException("Invalid result: " + result);
            } catch (Exception e) {
                throw new RuntimeException("Distributed task failed", e);
            }
        }
    }

    @Override
    public void teardown() {
        if (context != null) {
            context.shutdown();
        }
    }

    @Override
    public int getSuggestedIterations() {
        return 10;
    }
}
