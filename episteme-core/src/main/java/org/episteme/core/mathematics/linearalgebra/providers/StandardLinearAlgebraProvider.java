/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra.providers;

import org.episteme.core.mathematics.linearalgebra.Matrix;
import com.google.auto.service.AutoService;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;

/**
 * Linear Algebra Provider that forces the use of the Standard (Naive/Recursive) algorithm.
 * Intended for benchmarking and comparison purposes.
 */
@AutoService({LinearAlgebraProvider.class})
public class StandardLinearAlgebraProvider<E> extends CPUDenseLinearAlgebraProvider<E> {

    public StandardLinearAlgebraProvider() {
        super(null);
    }

    @Override
    public String getEnvironmentInfo() {
        return "CPU (Standard)";
    }

    @Override
    public String getName() {
        return "Episteme (Standard)";
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (a.cols() != b.rows()) {
            throw new IllegalArgumentException("Matrix inner dimensions must match");
        }
        // Force standard multiply (O(n^3)) via static utility
        return CPUDenseLinearAlgebraProvider.standardMultiply(a, b, field, this);
    }
    
    @Override
    public int getPriority() {
        return -10; // Low priority so it's not picked automatically as default
    }
}
