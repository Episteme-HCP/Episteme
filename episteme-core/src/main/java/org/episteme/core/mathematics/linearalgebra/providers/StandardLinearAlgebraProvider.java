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
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.GenericSVD;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult;
import org.episteme.core.mathematics.linearalgebra.matrices.storage.MatrixStorage;
import org.episteme.core.mathematics.linearalgebra.vectors.GenericVector;
import org.episteme.core.mathematics.linearalgebra.vectors.storage.VectorStorage;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.structures.rings.Field;
import org.episteme.core.technical.algorithm.AlgorithmManager;

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
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        int rows = a.rows();
        int cols = a.cols();
        Ring<E> ring = a.getScalarRing();
        MatrixStorage<E> storage = AlgorithmManager.getRegistry().createStorage(rows, cols, ring, 1.0);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                storage.set(i, j, ring.multiply(scalar, a.get(i, j)));
            }
        }
        return wrap(new GenericMatrix<>(storage, this, ring));
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        int rows = a.rows();
        int cols = a.cols();
        Ring<E> ring = a.getScalarRing();
        MatrixStorage<E> storage = AlgorithmManager.getRegistry().createStorage(cols, rows, ring, 1.0);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                storage.set(j, i, a.get(i, j));
            }
        }
        return wrap(new GenericMatrix<>(storage, this, ring));
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        int rows = a.rows();
        int cols = a.cols();
        if (cols != b.dimension()) throw new IllegalArgumentException("Dimensions do not match");
        Ring<E> ring = a.getScalarRing();
        VectorStorage<E> storage = AlgorithmManager.getRegistry().createVectorStorage(rows, ring, 1.0);
        for (int i = 0; i < rows; i++) {
            E sum = ring.zero();
            for (int j = 0; j < cols; j++) {
                sum = ring.add(sum, ring.multiply(a.get(i, j), b.get(j)));
            }
            storage.set(i, sum);
        }
        return new GenericVector<>(storage, this, ring);
    }

    @Override
    public SVDResult<E> svd(Matrix<E> a) {
        Ring<E> ring = a.getScalarRing();
        if (ring instanceof Field) {
            return GenericSVD.decompose(a, (Field<E>) ring);
        }
        throw new UnsupportedOperationException("SVD requires a Field scalar structure.");
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
