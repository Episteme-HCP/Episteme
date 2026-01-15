/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.technical.backend.math;

import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.linearalgebra.backends.LinearAlgebraProvider;
import org.jscience.technical.backend.ExecutionContext;
import org.jscience.technical.backend.Operation;

/**
 * A LinearAlgebraProvider that (would) offload operations to a remote gRPC service.
 * <p>
 * TEMPORARILY DISABLED due to missing Protobuf generated classes during refactoring.
 * </p>
 */
public class GRPCLinearAlgebraProvider<E> implements LinearAlgebraProvider<E> {

    public GRPCLinearAlgebraProvider(String host, int port, Field<E> field) {
        // Disabled
    }

    @Override
    public String getName() {
        return "gRPC Remote (Disabled)";
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public ExecutionContext createContext() {
        return null;
    }

    public void shutdown() throws InterruptedException {
    }

    // ==================== Stubs ====================

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) { throw new UnsupportedOperationException("Disabled"); }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) { throw new UnsupportedOperationException("Disabled"); }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) { throw new UnsupportedOperationException("Disabled"); }

    @Override
    public Matrix<E> transpose(Matrix<E> a) { throw new UnsupportedOperationException("Disabled"); }

    @Override
    public Matrix<E> inverse(Matrix<E> a) { throw new UnsupportedOperationException("Disabled"); }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) { throw new UnsupportedOperationException("Disabled"); }

    @Override
    public E determinant(Matrix<E> a) { throw new UnsupportedOperationException("Disabled"); }

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) { throw new UnsupportedOperationException("Disabled"); }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) { throw new UnsupportedOperationException("Disabled"); }

    @Override
    public Vector<E> multiply(Vector<E> v, E scalar) { throw new UnsupportedOperationException("Disabled"); }

    @Override
    public E dot(Vector<E> a, Vector<E> b) { throw new UnsupportedOperationException("Disabled"); }

    @Override
    public E norm(Vector<E> a) { throw new UnsupportedOperationException("Disabled"); }

    @Override
    public Vector<E> multiply(Matrix<E> m, Vector<E> v) { throw new UnsupportedOperationException("Disabled"); }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) { throw new UnsupportedOperationException("Disabled"); }
}
