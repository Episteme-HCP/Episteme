package org.jscience.benchmarks.benchmark;

import org.jscience.core.mathematics.numbers.complex.Complex;
import org.jscience.core.technical.algorithm.FFTProvider;
import java.util.ServiceLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A benchmark that systematically tests all available FFTProviders.
 */
public class SystematicFFTBenchmark implements RunnableBenchmark {

    private static final int SIZE = 4096;
    private Complex[] data;
    private final List<FFTProvider> providers = new ArrayList<>();
    private FFTProvider currentProvider;

    @Override
    public String getName() {
        return "Systematic FFT (" + (currentProvider != null ? currentProvider.getName() : "None") + ")";
    }

    @Override
    public String getDomain() {
        return "Signal Processing";
    }

    @Override
    public void setup() {
        data = generateData(SIZE);
        
        // Discover providers
        providers.clear();
        ServiceLoader<FFTProvider> loader = ServiceLoader.load(FFTProvider.class);
        for (FFTProvider p : loader) {
            providers.add(p);
        }
        
        if (!providers.isEmpty()) {
            currentProvider = providers.get(0);
        }
    }

    public void setProvider(FFTProvider provider) {
        this.currentProvider = provider;
    }

    public List<FFTProvider> getProviders() {
        return providers;
    }

    @Override
    public void run() {
        if (currentProvider != null) {
            currentProvider.transformComplex(data);
        }
    }

    @Override
    public void teardown() {
        data = null;
    }

    private Complex[] generateData(int n) {
        Random r = new Random(42);
        Complex[] d = new Complex[n];
        for (int i = 0; i < n; i++)
            d[i] = Complex.of(r.nextDouble(), r.nextDouble());
        return d;
    }
}
