package org.jscience.benchmarks.benchmark.benchmarks;

import org.jscience.benchmarks.benchmark.RunnableBenchmark;
import com.google.auto.service.AutoService;
import org.jscience.core.media.vision.VisionProvider;
import org.jscience.core.media.vision.ImageOp;

/**
 * Benchmark for Computer Vision Providers (JavaCV, OpenCL, etc.).
 * Measures throughput for basic image operations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
@AutoService(RunnableBenchmark.class)
public class SystematicVisionBenchmark implements SystematicBenchmark<VisionProvider> {

    private VisionProvider provider;
    private Object image;
    private final ImageOp identityOp = (img) -> img;

    @Override public Class<VisionProvider> getProviderClass() { return VisionProvider.class; }
    @Override public String getIdPrefix() { return "vision-throughput"; }
    @Override public String getNameBase() { return "Computer Vision Throughput"; }
    
    @Override public String getId() { return getIdPrefix() + "-default"; }
    @Override public String getName() { return getNameBase(); }
    @Override public String getDescription() { return "Measures basic image processing throughput on " + (provider != null ? provider.getName() : "default backend"); }
    @Override public String getDomain() { return "Computer Vision"; }
    @Override public String getAlgorithmType() { return "Image Processing"; }
    @Override public String getAlgorithmProvider() { return provider != null ? provider.getName() : "None"; }

    @Override
    public void setProvider(VisionProvider provider) {
        this.provider = provider;
    }

    @Override
    public boolean isAvailable() {
        return provider != null && provider.isAvailable();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setup() {
        if (provider == null) throw new IllegalStateException("Provider not set");
        try {
            // Create a dummy 1MP image (1000x1000 ints)
            image = provider.createImage(new int[1000 * 1000], 1000, 1000);
        } catch (Exception e) {
            image = null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
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
