/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.mathematics.linearalgebra.backends;

import java.lang.reflect.Constructor;

import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.technical.backend.ExecutionContext;
import org.jscience.technical.backend.cpu.CPUExecutionContext;

/**
 * Colt Linear Algebra Provider.
 * <p>
 * Uses Colt library (cern.colt.matrix) for high-performance linear algebra
 * operations. Falls back to CPU provider for non-Real types or when Colt
 * is not available.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ColtLinearAlgebraProvider<E> implements LinearAlgebraProvider<E> {

    private static boolean coltAvailable = false;
    private final Field<E> field;
    private final CPUDenseLinearAlgebraProvider<E> cpuProvider;
    
    // Delegate to actual Colt implementation if available and verified
    private LinearAlgebraProvider<E> coltImpl;

    static {
        try {
            Class.forName("cern.colt.matrix.DoubleMatrix2D");
            Class.forName("cern.colt.matrix.linalg.Algebra");
            Class.forName("org.jscience.mathematics.linearalgebra.backends.ColtSupport");
            coltAvailable = true;
        } catch (ClassNotFoundException e) {
            coltAvailable = false;
        }
    }

    /**
     * Public no-arg constructor required by ServiceLoader.
     */
    public ColtLinearAlgebraProvider() {
        this(null);
    }

    public ColtLinearAlgebraProvider(Field<E> field) {
        this.field = field;
        this.cpuProvider = new CPUDenseLinearAlgebraProvider<>(field);
        
        if (coltAvailable && field != null) {
            try {
                Class<?> clazz = Class.forName("org.jscience.mathematics.linearalgebra.backends.ColtSupport");
                @SuppressWarnings("unchecked")
                Constructor<LinearAlgebraProvider<E>> ctor = (Constructor<LinearAlgebraProvider<E>>) clazz.getConstructor(Field.class);
                this.coltImpl = ctor.newInstance(field);
            } catch (Throwable t) {
                // Creation failed (LinkageError? Exception?), fallback
                this.coltImpl = null;
            }
        }
    }

    @Override
    public String getName() {
        return "Colt";
    }

    @Override
    public boolean isAvailable() {
        return coltAvailable;
    }

    @Override
    public ExecutionContext createContext() {
        return new CPUExecutionContext();
    }

    @Override
    public int getPriority() {
        return coltAvailable ? 60 : 0;
    }

    private boolean canUseColt() {
        return coltImpl != null && field != null && 
               (field instanceof org.jscience.mathematics.sets.Reals ||
                Real.class.isAssignableFrom(field.zero().getClass()));
    }

    // Delegate methods

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        if (!canUseColt()) return cpuProvider.add(a, b);
        return coltImpl.add(a, b);
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        if (!canUseColt()) return cpuProvider.subtract(a, b);
        return coltImpl.subtract(a, b);
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        if (!canUseColt()) return cpuProvider.multiply(vector, scalar);
        return coltImpl.multiply(vector, scalar);
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        if (!canUseColt()) return cpuProvider.dot(a, b);
        return coltImpl.dot(a, b);
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        if (!canUseColt()) return cpuProvider.add(a, b);
        return coltImpl.add(a, b);
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        if (!canUseColt()) return cpuProvider.subtract(a, b);
        return coltImpl.subtract(a, b);
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (!canUseColt()) return cpuProvider.multiply(a, b);
        return coltImpl.multiply(a, b);
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (!canUseColt()) return cpuProvider.multiply(a, b);
        return coltImpl.multiply(a, b);
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        if (!canUseColt()) return cpuProvider.inverse(a);
        return coltImpl.inverse(a);
    }

    @Override
    public E determinant(Matrix<E> a) {
        if (!canUseColt()) return cpuProvider.determinant(a);
        return coltImpl.determinant(a);
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (!canUseColt()) return cpuProvider.solve(a, b);
        return coltImpl.solve(a, b);
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        if (!canUseColt()) return cpuProvider.transpose(a);
        return coltImpl.transpose(a);
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        if (!canUseColt()) return cpuProvider.scale(scalar, a);
        return coltImpl.scale(scalar, a);
    }

    @Override
    public E norm(Vector<E> a) {
        if (!canUseColt()) return cpuProvider.norm(a);
        return coltImpl.norm(a);
    }
}
