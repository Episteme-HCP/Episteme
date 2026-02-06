package org.jscience.benchmarks.benchmark;

import org.jscience.core.technical.algorithm.MandelbrotProvider;

/**
 * A benchmark that systematically tests all available MandelbrotProviders.
 */
public class SystematicMandelbrotBenchmark implements SystematicBenchmark<MandelbrotProvider> {

    private MandelbrotProvider currentProvider;
    private final double xMin = -2.0;
    private final double xMax = 0.5;
    private final double yMin = -1.25;
    private final double yMax = 1.25;
    private final int width = 1000;
    private final int height = 1000;
    private final int maxIterations = 256;

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }
    @Override public String getIdPrefix() { return "mandelbrot-systematic"; }
    @Override public String getNameBase() { return "Systematic Mandelbrot Generation"; }
    @Override public String getDescription() { return "Systematically benchmarks Mandelbrot set generation performance through intense floating-point iterations."; }
    @Override public String getDomain() { return "Visualization"; }
    @Override public Class<MandelbrotProvider> getProviderClass() { return MandelbrotProvider.class; }

    @Override
    public void setup() {
        // No heavy setup needed for Mandelbrot compute.
    }

    @Override
    public void setProvider(MandelbrotProvider provider) {
        this.currentProvider = provider;
    }

    @Override
    public void run() {
        if (currentProvider != null) {
            currentProvider.compute(xMin, xMax, yMin, yMax, width, height, maxIterations);
        }
    }

    @Override
    public void teardown() {
    }
}
