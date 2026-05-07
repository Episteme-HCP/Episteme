/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.benchmarks.benchmark.benchmarks;

import com.google.auto.service.AutoService;
import org.episteme.benchmarks.benchmark.RunnableBenchmark;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.real.RealBig;
import org.episteme.core.mathematics.linearalgebra.providers.CPUDenseLinearAlgebraProvider;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Systematic benchmark for High-Precision Complex Matrix Inversion.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
@AutoService(RunnableBenchmark.class)
public class ComplexMatrixInversionBenchmark implements SystematicBenchmark<LinearAlgebraProvider<Complex>> {

    private static final int SIZE = 16; // Complex inversion is slower
    private static final int DRY_RUN_SIZE = 8;
    
    private Matrix<Complex> a;
    private LinearAlgebraProvider<Complex> currentProvider;
    private boolean dryRun = false;

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }
    @Override public String getIdPrefix() { return "matrix-complex-inverse"; }
    @Override public String getNameBase() { return "Complex Matrix Inversion"; }
    @Override public String getDescription() { return "Dense Complex Matrix Inversion (16x16)"; }
    @Override public String getDomain() { return "Linear Algebra (Complex Inverse)"; }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override public Class<LinearAlgebraProvider<Complex>> getProviderClass() { return (Class) LinearAlgebraProvider.class; }

    @Override
    public void setup() {
        int n = dryRun ? DRY_RUN_SIZE : SIZE;
        Random r = new Random(42);
        a = createHPComplexMatrix(n, r);
        
        if (currentProvider == null) {
            // Default to CPU provider for complex numbers
            currentProvider = new CPUDenseLinearAlgebraProvider<>(Complex.ZERO);
        }
    }

    @Override
    public void run() {
        if (currentProvider != null) {
            currentProvider.inverse(a);
        }
    }

    @Override
    public void teardown() {
        a = null;
    }

    @Override public void setDryRun(boolean dryRun) { this.dryRun = dryRun; }
    @Override public boolean isDryRun() { return dryRun; }
    @Override public void setProvider(LinearAlgebraProvider<Complex> provider) { this.currentProvider = provider; }

    private Matrix<Complex> createHPComplexMatrix(int n, Random r) {
        Complex[][] data = new Complex[n][n];
        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    Real re = RealBig.create(new BigDecimal(String.valueOf(r.nextDouble())));
                    Real im = RealBig.create(new BigDecimal(String.valueOf(r.nextDouble())));
                    data[i][j] = Complex.of(re, im);
                    sum += Math.abs(re.doubleValue()) + Math.abs(im.doubleValue());
                }
            }
            // Ensure dominant diagonal for invertibility
            Real reDiag = RealBig.create(new BigDecimal(String.valueOf(sum + 1.0 + r.nextDouble())));
            Real imDiag = RealBig.create(new BigDecimal(String.valueOf(r.nextDouble())));
            data[i][i] = Complex.of(reDiag, imDiag);
        }
        return Matrix.of(data, Complex.ZERO);
    }
}
