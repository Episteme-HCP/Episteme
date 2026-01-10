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

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;

/**
 * Internal Delegate for Colt Linear Algebra Operations.
 * This class is only loaded if the Colt library is present in the classpath.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ColtSupport<E> implements LinearAlgebraProvider<E> {

    private final Field<E> field;

    public ColtSupport(Field<E> field) {
        this.field = field;
    }

    @Override
    public String getName() {
        return "Colt (Internal)";
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
    private DoubleMatrix2D toColtMatrix(Matrix<E> m) {
        DenseDoubleMatrix2D colt = new DenseDoubleMatrix2D(m.rows(), m.cols());
        for (int i = 0; i < m.rows(); i++) {
            for (int j = 0; j < m.cols(); j++) {
                colt.set(i, j, ((Real) m.get(i, j)).doubleValue());
            }
        }
        return colt;
    }

    @SuppressWarnings("unchecked")
    private Matrix<E> fromColtMatrix(DoubleMatrix2D colt) {
        int rows = colt.rows();
        int cols = colt.columns();
        DenseMatrixStorage<E> storage = new DenseMatrixStorage<>(rows, cols, (E) Real.ZERO);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                storage.set(i, j, (E) Real.of(colt.get(i, j)));
            }
        }
        return new GenericMatrix<>(storage, this, field);
    }

    private DoubleMatrix1D toColtVector(Vector<E> v) {
        DenseDoubleMatrix1D colt = new DenseDoubleMatrix1D(v.dimension());
        for (int i = 0; i < v.dimension(); i++) {
            colt.set(i, ((Real) v.get(i)).doubleValue());
        }
        return colt;
    }

    @SuppressWarnings("unchecked")
    private Vector<E> fromColtVector(DoubleMatrix1D colt) {
        int size = (int) colt.size();
        List<E> data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            data.add((E) Real.of(colt.get(i)));
        }
        return new GenericVector<>(new DenseVectorStorage<>(data), this, field);
    }

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        DoubleMatrix1D ca = toColtVector(a);
        DoubleMatrix1D cb = toColtVector(b);
        ca.assign(cb, cern.jet.math.Functions.plus);
        return fromColtVector(ca);
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        DoubleMatrix1D ca = toColtVector(a);
        DoubleMatrix1D cb = toColtVector(b);
        ca.assign(cb, cern.jet.math.Functions.minus);
        return fromColtVector(ca);
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        DoubleMatrix1D cv = toColtVector(vector);
        double s = ((Real) scalar).doubleValue();
        cv.assign(cern.jet.math.Functions.mult(s));
        return fromColtVector(cv);
    }

    @Override
    @SuppressWarnings("unchecked")
    public E dot(Vector<E> a, Vector<E> b) {
        DoubleMatrix1D ca = toColtVector(a);
        DoubleMatrix1D cb = toColtVector(b);
        return (E) Real.of(ca.zDotProduct(cb));
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        DoubleMatrix2D ca = toColtMatrix(a);
        DoubleMatrix2D cb = toColtMatrix(b);
        ca.assign(cb, cern.jet.math.Functions.plus);
        return fromColtMatrix(ca);
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        DoubleMatrix2D ca = toColtMatrix(a);
        DoubleMatrix2D cb = toColtMatrix(b);
        ca.assign(cb, cern.jet.math.Functions.minus);
        return fromColtMatrix(ca);
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        Algebra algebra = new Algebra();
        DoubleMatrix2D ca = toColtMatrix(a);
        DoubleMatrix2D cb = toColtMatrix(b);
        DoubleMatrix2D result = algebra.mult(ca, cb);
        return fromColtMatrix(result);
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        Algebra algebra = new Algebra();
        DoubleMatrix2D ca = toColtMatrix(a);
        DoubleMatrix1D cb = toColtVector(b);
        DoubleMatrix1D result = algebra.mult(ca, cb);
        return fromColtVector(result);
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        Algebra algebra = new Algebra();
        DoubleMatrix2D ca = toColtMatrix(a);
        DoubleMatrix2D result = algebra.inverse(ca);
        return fromColtMatrix(result);
    }

    @Override
    @SuppressWarnings("unchecked")
    public E determinant(Matrix<E> a) {
        Algebra algebra = new Algebra();
        DoubleMatrix2D ca = toColtMatrix(a);
        return (E) Real.of(algebra.det(ca));
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        Algebra algebra = new Algebra();
        DoubleMatrix2D ca = toColtMatrix(a);
        // Convert vector to column matrix for solve
        DenseDoubleMatrix2D bMatrix = new DenseDoubleMatrix2D(b.dimension(), 1);
        for (int i = 0; i < b.dimension(); i++) {
            bMatrix.set(i, 0, ((Real) b.get(i)).doubleValue());
        }
        DoubleMatrix2D xMatrix = algebra.solve(ca, bMatrix);
        // Convert back to vector
        DenseDoubleMatrix1D result = new DenseDoubleMatrix1D(b.dimension());
        for (int i = 0; i < b.dimension(); i++) {
            result.set(i, xMatrix.get(i, 0));
        }
        return fromColtVector(result);
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        Algebra algebra = new Algebra();
        DoubleMatrix2D ca = toColtMatrix(a);
        DoubleMatrix2D result = algebra.transpose(ca);
        return fromColtMatrix(result);
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        DoubleMatrix2D ca = toColtMatrix(a);
        double s = ((Real) scalar).doubleValue();
        ca.assign(cern.jet.math.Functions.mult(s));
        return fromColtMatrix(ca);
    }

    @Override
    @SuppressWarnings("unchecked")
    public E norm(Vector<E> a) {
        DoubleMatrix1D ca = toColtVector(a);
        return (E) Real.of(Math.sqrt(ca.zDotProduct(ca)));
    }
}
