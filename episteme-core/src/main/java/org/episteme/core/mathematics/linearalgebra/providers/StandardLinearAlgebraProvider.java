/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra.providers;

import org.episteme.core.mathematics.linearalgebra.Matrix;
import com.google.auto.service.AutoService;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.structures.rings.Ring;

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
    @SuppressWarnings("unchecked")
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        // Force standard multiply (O(n^3)) via static utility
        return CPUDenseLinearAlgebraProvider.standardMultiply(a, b, (org.episteme.core.mathematics.structures.rings.Field<E>) (Object) a.getScalarRing(), this);
    }
    
    @Override
    public int getPriority() {
        return -10; // Low priority so it's not picked automatically as default
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        if (a.getScalarRing().zero() instanceof Real) {
            LinearAlgebraProvider<Real> leaf = getLeafProvider((Ring<Real>) a.getScalarRing());
            return wrap((Matrix<E>) (Matrix<?>) leaf.scale((Real) scalar, (Matrix<Real>) a));
        }
        return wrap(super.scale(scalar, a));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> transpose(Matrix<E> a) {
        if (a.getScalarRing().zero() instanceof Real) {
            LinearAlgebraProvider<Real> leaf = getLeafProvider((Ring<Real>) a.getScalarRing());
            return wrap((Matrix<E>) (Matrix<?>) leaf.transpose((Matrix<Real>) a));
        }
        return wrap(super.transpose(a));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (a.getScalarRing().zero() instanceof Real) {
            LinearAlgebraProvider<Real> leaf = getLeafProvider((Ring<Real>) a.getScalarRing());
            return (Vector<E>) (Vector<?>) leaf.multiply((Matrix<Real>) a, (Vector<Real>) b);
        }
        return super.multiply(a, b);
    }

    @SuppressWarnings("unchecked")
    private LinearAlgebraProvider<Real> getLeafProvider(Ring<Real> ring) {
        org.episteme.core.technical.algorithm.OperationContext ctx = org.episteme.core.technical.algorithm.OperationContext.DEFAULT;
        if (ring.zero() instanceof org.episteme.core.mathematics.numbers.real.RealBig) {
            ctx = new org.episteme.core.technical.algorithm.OperationContext.Builder()
                    .addHint(org.episteme.core.technical.algorithm.OperationContext.Hint.HIGH_PRECISION)
                    .build();
        }

        return (LinearAlgebraProvider<Real>) org.episteme.core.technical.algorithm.ProviderSelector.select(
                LinearAlgebraProvider.class,
                ctx,
                p -> p != this && p.isCompatible(ring)
        );
    }

    private Matrix<E> wrap(Matrix<E> m) {
        if (m instanceof GenericMatrix) {
            return ((GenericMatrix<E>) m).withProvider(this);
        }
        return m;
    }

    @Override
    public void shutdown() {
        // No-op
    }
}
