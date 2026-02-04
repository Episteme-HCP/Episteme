package org.jscience.benchmarks.benchmark;

import org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.jscience.core.technical.algorithm.LinearAlgebraProvider;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.sets.Reals;
import java.util.ServiceLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A benchmark that systematically tests all available LinearAlgebraProviders.
 */
public class SystematicMatrixBenchmark implements RunnableBenchmark {

    private static final int SIZE = 500;
    private RealDoubleMatrix A;
    private RealDoubleMatrix B;
    private final List<LinearAlgebraProvider<Real>> providers = new ArrayList<>();
    private LinearAlgebraProvider<Real> currentProvider;

    @Override
    public String getName() {
        return "Systematic Matrix Mult (" + (currentProvider != null ? currentProvider.getName() : "None") + ")";
    }

    @Override
    public String getDomain() {
        return "Linear Algebra";
    }

    @Override
    public void setup() {
        double[][] dataA = generateData(SIZE);
        double[][] dataB = generateData(SIZE);
        A = RealDoubleMatrix.of(dataA);
        B = RealDoubleMatrix.of(dataB);
        
        // Discover providers
        providers.clear();
        @SuppressWarnings("rawtypes")
        ServiceLoader<LinearAlgebraProvider> loader = ServiceLoader.load(LinearAlgebraProvider.class);
        for (LinearAlgebraProvider<?> p : loader) {
            if (p.isCompatible(Reals.getInstance())) {
               @SuppressWarnings("unchecked")
               LinearAlgebraProvider<Real> realProvider = (LinearAlgebraProvider<Real>) p;
               providers.add(realProvider);
            }
        }
        
        if (!providers.isEmpty()) {
            currentProvider = providers.get(0); // Default to first for individual run
        }
    }

    public void setProvider(LinearAlgebraProvider<Real> provider) {
        this.currentProvider = provider;
    }

    public List<LinearAlgebraProvider<Real>> getProviders() {
        return providers;
    }

    @Override
    public void run() {
        if (currentProvider != null) {
            currentProvider.multiply(A, B);
        }
    }

    @Override
    public void teardown() {
        A = null;
        B = null;
    }

    private double[][] generateData(int n) {
        Random r = new Random(42);
        double[][] d = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                d[i][j] = r.nextDouble();
        return d;
    }
}
