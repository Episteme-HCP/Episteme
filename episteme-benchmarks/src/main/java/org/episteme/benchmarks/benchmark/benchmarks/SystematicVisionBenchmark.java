package org.episteme.benchmarks.benchmark.benchmarks;

import org.episteme.benchmarks.benchmark.RunnableBenchmark;
import com.google.auto.service.AutoService;
import org.episteme.core.media.vision.VisionAlgorithmBackend;
import org.episteme.core.media.vision.ImageOp;

/**
 * Benchmark for Computer Vision Providers (JavaCV, OpenCL, etc.).
 * Measures throughput for basic image operations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
@AutoService(RunnableBenchmark.class)
public class SystematicVisionBenchmark implements SystematicBenchmark<VisionAlgorithmBackend<?>> {

    private VisionAlgorithmBackend<Object> provider;
    private Object image;
    private final ImageOp<Object> identityOp = (img) -> img;
    private boolean dryRun = false;

    @Override
    @SuppressWarnings("unchecked")
    public Class<VisionAlgorithmBackend<?>> getProviderClass() { 
        return (Class<VisionAlgorithmBackend<?>>) (Class<?>) VisionAlgorithmBackend.class; 
    }
    @Override public String getIdPrefix() { return "vision-throughput"; }
    @Override public String getNameBase() { return "Computer Vision Throughput"; }
    
    @Override public String getId() { return getIdPrefix() + "-default"; }
    @Override public String getName() { return getNameBase(); }
    @Override public String getDescription() { return "Measures basic image processing throughput on " + (provider != null ? provider.getName() : "default backend"); }
    @Override public String getDomain() { return "Computer Vision"; }
    @Override public String getAlgorithmType() { return "Image Processing"; }
    @Override public String getAlgorithmProvider() { return provider != null ? provider.getName() : "None"; }

    @Override
    @SuppressWarnings("unchecked")
    public void setProvider(VisionAlgorithmBackend<?> provider) {
        this.provider = (VisionAlgorithmBackend<Object>) provider;
    }

    @Override
    public boolean isAvailable() {
        return provider != null && provider.isAvailable();
    }

    @Override public void setDryRun(boolean dryRun) { this.dryRun = dryRun; }
    @Override public boolean isDryRun() { return dryRun; }

    @Override
    public void setup() {
        if (provider == null) throw new IllegalStateException("Provider not set");
        try {
            int size = dryRun ? 256 : 1000;
            // Create a dummy image
            image = provider.createImage(new int[size * size], size, size);
        } catch (Exception e) {
            image = null;
        }
    }

    @Override
    public void run() {
        if (image != null) {
            provider.apply(image, identityOp);
        }
    }

    @Override
    public void teardown() {
        image = null;
    }

    @Override
    public int getSuggestedIterations() {
        return 50;
    }
}
