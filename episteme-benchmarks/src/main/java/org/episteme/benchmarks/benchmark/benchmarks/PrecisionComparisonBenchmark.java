/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.benchmarks.benchmark.benchmarks;

import com.google.auto.service.AutoService;
import org.episteme.benchmarks.benchmark.ComparisonBenchmark;
import org.episteme.benchmarks.benchmark.RunnableBenchmark;

/**
 * Comparative benchmark: Native HP (MPFR) vs Standard Double (CPU).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService(RunnableBenchmark.class)
public class PrecisionComparisonBenchmark extends ComparisonBenchmark {

    public PrecisionComparisonBenchmark() {
        super("precision-comparison", "Precision Cost Comparison", 
              "Compares Native High-Precision (MPFR) vs Standard Double Precision (CPU)", 
              "Linear Algebra (Analysis)");
        
        // Add Standard Double Implementation
        addImplementation("Standard Double (CPU)", new SystematicMatrixBenchmark());
        
        // Add High-Precision Implementation
        addImplementation("Native HP (MPFR)", new HighPrecisionLinearAlgebraBenchmark());
        
        // Try to filter the HP benchmark to only use the native provider if available
        // This is done dynamically during run time by the implementation selection
    }

    @Override
    public boolean isAvailable() {
        // Available if at least one implementation is
        return getImplementations().values().stream().anyMatch(RunnableBenchmark::isAvailable);
    }
}
