/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.linearalgebra.providers;

import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.sets.Reals;
import com.google.auto.service.AutoService;
import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;

/**
 * Linear Algebra Provider that forces the use of the Standard (Naive/Recursive) algorithm.
 * Intended for benchmarking and comparison purposes.
 */
@AutoService(LinearAlgebraProvider.class)
public class StandardLinearAlgebraProvider<E extends org.jscience.core.mathematics.structures.rings.Field<E>> implements LinearAlgebraProvider<E> {

    public StandardLinearAlgebraProvider() {
    }

    @Override
    public String getName() {
        return "JScience (Standard)";
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (a.cols() != b.rows()) {
            throw new IllegalArgumentException("Matrix inner dimensions must match");
        }
        // Force standard multiply (O(n^3)) via static utility
        // Note: We need a field. Matrix should have it (getScalarRing).
        return CPUDenseLinearAlgebraProvider.standardMultiply(a, b, (org.jscience.core.mathematics.structures.rings.Field<E>) a.getScalarRing(), this);
    }
    
    @Override
    public int getPriority() {
        return -10; // Low priority so it's not picked automatically as default
    }
}
