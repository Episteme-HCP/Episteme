/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.mathematics.linearalgebra.backends;

import java.util.ArrayList;
import java.util.List;

import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.matrices.GenericMatrix;
import org.jscience.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage;
import org.jscience.mathematics.linearalgebra.vectors.GenericVector;
import org.jscience.mathematics.linearalgebra.vectors.storage.DenseVectorStorage;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.technical.backend.ExecutionContext;
import org.jscience.technical.backend.cpu.CPUExecutionContext;

import org.ejml.simple.SimpleMatrix;

/**
 * Internal Delegate for EJML Linear Algebra Operations.
 * This class is only loaded if the EJML library is present in the classpath.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class EJMLSupport<E> implements LinearAlgebraProvider<E> {

    private final Field<E> field;

    public EJMLSupport(Field<E> field) {
        this.field = field;
    }

    @Override
    public String getName() {
        return "EJML (Internal)";
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public ExecutionContext createContext() {
        return new CPUExecutionContext();
    }

    // Conversion helpers
    private SimpleMatrix toEjmlMatrix(Matrix<E> m) {
        SimpleMatrix ejml = new SimpleMatrix(m.rows(), m.cols());
        for (int i = 0; i < m.rows(); i++) {
            for (int j = 0; j < m.cols(); j++) {
                ejml.set(i, j, ((Real) m.get(i, j)).doubleValue());
            }
        }
        return ejml;
    }

    @SuppressWarnings("unchecked")
    private Matrix<E> fromEjmlMatrix(SimpleMatrix ejml) {
        int rows = ejml.getNumRows();
        int cols = ejml.getNumCols();
        DenseMatrixStorage<E> storage = new DenseMatrixStorage<>(rows, cols, (E) Real.ZERO);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                storage.set(i, j, (E) Real.of(ejml.get(i, j)));
            }
        }
        return new GenericMatrix<>(storage, this, field);
    }

    private SimpleMatrix toEjmlVector(Vector<E> v) {
        SimpleMatrix ejml = new SimpleMatrix(v.dimension(), 1);
        for (int i = 0; i < v.dimension(); i++) {
            ejml.set(i, 0, ((Real) v.get(i)).doubleValue());
        }
        return ejml;
    }

    @SuppressWarnings("unchecked")
    private Vector<E> fromEjmlVector(SimpleMatrix ejml) {
        int size = ejml.getNumRows();
        E[] data = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), size);
        for (int i = 0; i < size; i++) {
            data[i] = (E) Real.of(ejml.get(i, 0));
        }
        return new GenericVector<>(new DenseVectorStorage<>(data), this, field);
    }

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        SimpleMatrix ea = toEjmlVector(a);
        SimpleMatrix eb = toEjmlVector(b);
        return fromEjmlVector(ea.plus(eb));
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        SimpleMatrix ea = toEjmlVector(a);
        SimpleMatrix eb = toEjmlVector(b);
        return fromEjmlVector(ea.minus(eb));
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        SimpleMatrix ev = toEjmlVector(vector);
        double s = ((Real) scalar).doubleValue();
        return fromEjmlVector(ev.scale(s));
    }

    @Override
    @SuppressWarnings("unchecked")
    public E dot(Vector<E> a, Vector<E> b) {
        SimpleMatrix ea = toEjmlVector(a);
        SimpleMatrix eb = toEjmlVector(b);
        return (E) Real.of(ea.transpose().mult(eb).get(0, 0));
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        SimpleMatrix ea = toEjmlMatrix(a);
        SimpleMatrix eb = toEjmlMatrix(b);
        return fromEjmlMatrix(ea.plus(eb));
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        SimpleMatrix ea = toEjmlMatrix(a);
        SimpleMatrix eb = toEjmlMatrix(b);
        return fromEjmlMatrix(ea.minus(eb));
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        SimpleMatrix ea = toEjmlMatrix(a);
        SimpleMatrix eb = toEjmlMatrix(b);
        return fromEjmlMatrix(ea.mult(eb));
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        SimpleMatrix ea = toEjmlMatrix(a);
        SimpleMatrix eb = toEjmlVector(b);
        return fromEjmlVector(ea.mult(eb));
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        SimpleMatrix ea = toEjmlMatrix(a);
        return fromEjmlMatrix(ea.invert());
    }

    @Override
    @SuppressWarnings("unchecked")
    public E determinant(Matrix<E> a) {
        SimpleMatrix ea = toEjmlMatrix(a);
        return (E) Real.of(ea.determinant());
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        SimpleMatrix ea = toEjmlMatrix(a);
        SimpleMatrix eb = toEjmlVector(b);
        return fromEjmlVector(ea.solve(eb));
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        SimpleMatrix ea = toEjmlMatrix(a);
        return fromEjmlMatrix(ea.transpose());
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        SimpleMatrix ea = toEjmlMatrix(a);
        double s = ((Real) scalar).doubleValue();
        return fromEjmlMatrix(ea.scale(s));
    }

    @Override
    @SuppressWarnings("unchecked")
    public E norm(Vector<E> a) {
        SimpleMatrix ea = toEjmlVector(a);
        return (E) Real.of(ea.normF());
    }
}
