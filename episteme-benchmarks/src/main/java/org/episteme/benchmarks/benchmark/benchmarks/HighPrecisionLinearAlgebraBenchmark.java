/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.benchmarks.benchmark.benchmarks;

import com.google.auto.service.AutoService;
import org.episteme.benchmarks.benchmark.RunnableBenchmark;
import org.episteme.core.mathematics.context.MathContext;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.real.RealBig;
import org.episteme.core.mathematics.sets.Reals;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Systematic benchmark for High-Precision Linear Algebra.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService(RunnableBenchmark.class)
public class HighPrecisionLinearAlgebraBenchmark implements SystematicBenchmark<LinearAlgebraProvider<Real>> {

    private static final int SIZE = 64;
    private static final int DRY_RUN_SIZE = 8;
    
    private Matrix<Real> a;
    private Matrix<Real> b;
    private LinearAlgebraProvider<Real> currentProvider;
    private boolean dryRun = false;

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }
    @Override public String getIdPrefix() { return "linear-algebra-hp-systematic"; }
    @Override public String getNameBase() { return "High-Precision Matrix Multiplication"; }
    @Override public String getDescription() { return "Dense Matrix Multiplication (64x64), High Precision (EXACT)"; }
    @Override public String getDomain() { return "Linear Algebra (High-Precision)"; }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override public Class<LinearAlgebraProvider<Real>> getProviderClass() { return (Class) LinearAlgebraProvider.class; }

    @Override
    public void setup() {
        int n = dryRun ? DRY_RUN_SIZE : SIZE;
        Random r = new Random(42);
        a = createHPMatrix(n, r);
        b = createHPMatrix(n, r);
    }

    @Override
    public void run() {
        if (currentProvider != null) {
            // Force High Precision Context
            MathContext.exact().compute(() -> {
                currentProvider.multiply(a, b);
                return null;
            });
        }
    }

    @Override
    public void teardown() {
        a = null;
        b = null;
    }

    @Override public void setDryRun(boolean dryRun) { this.dryRun = dryRun; }
    @Override public boolean isDryRun() { return dryRun; }
    @Override public void setProvider(LinearAlgebraProvider<Real> provider) { this.currentProvider = provider; }

    private Matrix<Real> createHPMatrix(int n, Random r) {
        Real[][] data = new Real[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // High precision values
                data[i][j] = RealBig.create(new BigDecimal(String.valueOf(r.nextDouble())));
            }
        }
        return Matrix.of(data, Reals.getInstance());
    }
}
