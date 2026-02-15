package org.jscience.benchmarks.benchmark.benchmarks;

import com.google.auto.service.AutoService;
import org.jscience.benchmarks.benchmark.RunnableBenchmark;
import org.jscience.core.technical.algorithm.MLProvider;

import java.util.ServiceLoader;

/**
 * Systematic benchmark for Machine Learning providers.
 * Tests K-Means clustering and PCA dimensionality reduction.
 */
@AutoService(RunnableBenchmark.class)
public class SystematicMLBenchmark implements SystematicBenchmark<MLProvider> {

    private MLProvider provider;
    private double[][] kMeansData;
    private double[][] pcaData;
    private static final int NUM_SAMPLES = 1000;
    private static final int NUM_FEATURES = 20;
    private static final int K_CLUSTERS = 5;
    private static final int PCA_COMPONENTS = 5;

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }

    @Override
    public Class<MLProvider> getProviderClass() {
        return MLProvider.class;
    }

    @Override
    public String getIdPrefix() {
        return "ml";
    }

    @Override
    public String getNameBase() {
        return "ML: K-Means + PCA";
    }

    @Override
    public void setProvider(MLProvider provider) {
        this.provider = provider;
    }

    @Override
    public String getDomain() {
        return "Machine Learning";
    }

    @Override
    public String getDescription() {
        return "K-Means clustering (" + NUM_SAMPLES + " samples, " + K_CLUSTERS + " clusters) and PCA (" + NUM_FEATURES + " → " + PCA_COMPONENTS + " dims)";
    }

    @Override
    public String getAlgorithmProvider() {
        return provider != null ? provider.getName() : "Unknown";
    }

    @Override
    public void setup() {
        if (provider == null) {
            ServiceLoader<MLProvider> loader = ServiceLoader.load(MLProvider.class);
            provider = loader.findFirst().orElse(null);
        }
        
        if (provider == null || !provider.isAvailable()) {
            throw new UnsupportedOperationException("No ML provider available");
        }

        // Generate synthetic data for K-Means
        kMeansData = new double[NUM_SAMPLES][NUM_FEATURES];
        for (int i = 0; i < NUM_SAMPLES; i++) {
            for (int j = 0; j < NUM_FEATURES; j++) {
                kMeansData[i][j] = Math.random() * 100.0;
            }
        }

        // Generate synthetic data for PCA
        pcaData = new double[NUM_SAMPLES][NUM_FEATURES];
        for (int i = 0; i < NUM_SAMPLES; i++) {
            for (int j = 0; j < NUM_FEATURES; j++) {
                pcaData[i][j] = Math.random() * 100.0;
            }
        }
    }

    @Override
    public void run() {
        // Test K-Means clustering
        int[] clusters = provider.kMeans(kMeansData, K_CLUSTERS, 100);
        
        // Test PCA dimensionality reduction
        double[][] reduced = provider.pca(pcaData, PCA_COMPONENTS);
        
        // Verify results are non-null
        if (clusters == null || reduced == null) {
            throw new RuntimeException("ML provider returned null results");
        }
    }

    @Override
    public void teardown() {
        kMeansData = null;
        pcaData = null;
    }

    @Override
    public int getSuggestedIterations() {
        return 10; // ML algorithms are expensive
    }
}
