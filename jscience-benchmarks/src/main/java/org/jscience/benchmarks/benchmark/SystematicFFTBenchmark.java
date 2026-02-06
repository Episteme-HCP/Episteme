package org.jscience.benchmarks.benchmark;

import org.jscience.core.mathematics.numbers.complex.Complex;
import org.jscience.core.technical.algorithm.FFTProvider;
import java.util.Random;

/**
 * A benchmark that systematically tests all available FFTProviders.
 */
public class SystematicFFTBenchmark implements SystematicBenchmark<FFTProvider> {

    private static final int SIZE = 4096;
    private Complex[] data;
    private FFTProvider currentProvider;

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }
    @Override public String getIdPrefix() { return "fft-systematic"; }
    @Override public String getNameBase() { return "Systematic FFT"; }
    @Override public Class<FFTProvider> getProviderClass() { return FFTProvider.class; }

    @Override
    public String getDescription() {
        return "Systematically benchmarks all discovered FFT providers (Local, MultiCore, Native, etc.) on a fixed size data set.";
    }

    @Override
    public String getDomain() {
        return "Signal Processing";
    }

    @Override
    public void setup() {
        data = generateData(SIZE);
    }

    @Override
    public void setProvider(FFTProvider provider) {
        this.currentProvider = provider;
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
