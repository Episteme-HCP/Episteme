package org.jscience.benchmarks.benchmark;

import com.google.auto.service.AutoService;

import org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.mathematics.numbers.real.Real;
import java.util.Random;

/**
 * A benchmark that systematically tests all available LinearAlgebraProviders.
 */
@AutoService(RunnableBenchmark.class)
public class SystematicMatrixBenchmark implements SystematicBenchmark<LinearAlgebraProvider<Real>> {

    private static final int SIZE = 500;
    private RealDoubleMatrix A;
    private RealDoubleMatrix B;
    private LinearAlgebraProvider<Real> currentProvider;

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }
    @Override public String getIdPrefix() { return "matrix-systematic"; }
    @Override public String getNameBase() { return "Systematic Matrix Mult"; }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override public Class<LinearAlgebraProvider<Real>> getProviderClass() { return (Class) LinearAlgebraProvider.class; }

    @Override
    public String getDescription() {
        return "Systematically benchmarks all discovered Linear Algebra providers on a standard 500x500 real matrix multiplication.";
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
    }

    @Override
    public void setProvider(LinearAlgebraProvider<Real> provider) {
        this.currentProvider = provider;
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
