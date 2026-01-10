/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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

    static {
        try {
            Class.forName("cern.colt.matrix.DoubleMatrix2D");
            Class.forName("cern.colt.matrix.linalg.Algebra");
            coltAvailable = true;
        } catch (ClassNotFoundException e) {
            coltAvailable = false;
        }
    }

    public ColtLinearAlgebraProvider() {
        this.field = null;
        this.cpuProvider = null;
    }

    public ColtLinearAlgebraProvider(Field<E> field) {
        this.field = field;
        this.cpuProvider = new CPUDenseLinearAlgebraProvider<>(field);
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
        return coltAvailable && field != null && 
               (field instanceof org.jscience.mathematics.sets.Reals ||
                Real.class.isAssignableFrom(field.zero().getClass()));
    }

    // Conversion helpers
    private cern.colt.matrix.DoubleMatrix2D toColtMatrix(Matrix<E> m) {
        cern.colt.matrix.impl.DenseDoubleMatrix2D colt = 
            new cern.colt.matrix.impl.DenseDoubleMatrix2D(m.rows(), m.cols());
        for (int i = 0; i < m.rows(); i++) {
            for (int j = 0; j < m.cols(); j++) {
                colt.set(i, j, ((Real) m.get(i, j)).doubleValue());
            }
        }
        return colt;
    }

    @SuppressWarnings("unchecked")
    private Matrix<E> fromColtMatrix(cern.colt.matrix.DoubleMatrix2D colt) {
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

    private cern.colt.matrix.DoubleMatrix1D toColtVector(Vector<E> v) {
        cern.colt.matrix.impl.DenseDoubleMatrix1D colt = 
            new cern.colt.matrix.impl.DenseDoubleMatrix1D(v.dimension());
        for (int i = 0; i < v.dimension(); i++) {
            colt.set(i, ((Real) v.get(i)).doubleValue());
        }
        return colt;
    }

    @SuppressWarnings("unchecked")
    private Vector<E> fromColtVector(cern.colt.matrix.DoubleMatrix1D colt) {
        int size = (int) colt.size();
        List<E> data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            data.add((E) Real.of(colt.get(i)));
        }
        return new GenericVector<>(new DenseVectorStorage<>(data), this, field);
    }

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        if (!canUseColt()) {
            return cpuProvider.add(a, b);
        }
        cern.colt.matrix.DoubleMatrix1D ca = toColtVector(a);
        cern.colt.matrix.DoubleMatrix1D cb = toColtVector(b);
        ca.assign(cb, cern.jet.math.Functions.plus);
        return fromColtVector(ca);
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        if (!canUseColt()) {
            return cpuProvider.subtract(a, b);
        }
        cern.colt.matrix.DoubleMatrix1D ca = toColtVector(a);
        cern.colt.matrix.DoubleMatrix1D cb = toColtVector(b);
        ca.assign(cb, cern.jet.math.Functions.minus);
        return fromColtVector(ca);
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        if (!canUseColt()) {
            return cpuProvider.multiply(vector, scalar);
        }
        cern.colt.matrix.DoubleMatrix1D cv = toColtVector(vector);
        double s = ((Real) scalar).doubleValue();
        cv.assign(cern.jet.math.Functions.mult(s));
        return fromColtVector(cv);
    }

    @Override
    @SuppressWarnings("unchecked")
    public E dot(Vector<E> a, Vector<E> b) {
        if (!canUseColt()) {
            return cpuProvider.dot(a, b);
        }
        cern.colt.matrix.DoubleMatrix1D ca = toColtVector(a);
        cern.colt.matrix.DoubleMatrix1D cb = toColtVector(b);
        return (E) Real.of(ca.zDotProduct(cb));
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        if (!canUseColt()) {
            return cpuProvider.add(a, b);
        }
        cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a);
        cern.colt.matrix.DoubleMatrix2D cb = toColtMatrix(b);
        ca.assign(cb, cern.jet.math.Functions.plus);
        return fromColtMatrix(ca);
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        if (!canUseColt()) {
            return cpuProvider.subtract(a, b);
        }
        cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a);
        cern.colt.matrix.DoubleMatrix2D cb = toColtMatrix(b);
        ca.assign(cb, cern.jet.math.Functions.minus);
        return fromColtMatrix(ca);
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (!canUseColt()) {
            return cpuProvider.multiply(a, b);
        }
        cern.colt.matrix.linalg.Algebra algebra = new cern.colt.matrix.linalg.Algebra();
        cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a);
        cern.colt.matrix.DoubleMatrix2D cb = toColtMatrix(b);
        cern.colt.matrix.DoubleMatrix2D result = algebra.mult(ca, cb);
        return fromColtMatrix(result);
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (!canUseColt()) {
            return cpuProvider.multiply(a, b);
        }
        cern.colt.matrix.linalg.Algebra algebra = new cern.colt.matrix.linalg.Algebra();
        cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a);
        cern.colt.matrix.DoubleMatrix1D cb = toColtVector(b);
        cern.colt.matrix.DoubleMatrix1D result = algebra.mult(ca, cb);
        return fromColtVector(result);
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        if (!canUseColt()) {
            return cpuProvider.inverse(a);
        }
        cern.colt.matrix.linalg.Algebra algebra = new cern.colt.matrix.linalg.Algebra();
        cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a);
        cern.colt.matrix.DoubleMatrix2D result = algebra.inverse(ca);
        return fromColtMatrix(result);
    }

    @Override
    @SuppressWarnings("unchecked")
    public E determinant(Matrix<E> a) {
        if (!canUseColt()) {
            return cpuProvider.determinant(a);
        }
        cern.colt.matrix.linalg.Algebra algebra = new cern.colt.matrix.linalg.Algebra();
        cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a);
        return (E) Real.of(algebra.det(ca));
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (!canUseColt()) {
            return cpuProvider.solve(a, b);
        }
        cern.colt.matrix.linalg.Algebra algebra = new cern.colt.matrix.linalg.Algebra();
        cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a);
        // Convert vector to column matrix for solve
        cern.colt.matrix.impl.DenseDoubleMatrix2D bMatrix = 
            new cern.colt.matrix.impl.DenseDoubleMatrix2D(b.dimension(), 1);
        for (int i = 0; i < b.dimension(); i++) {
            bMatrix.set(i, 0, ((Real) b.get(i)).doubleValue());
        }
        cern.colt.matrix.DoubleMatrix2D xMatrix = algebra.solve(ca, bMatrix);
        // Convert back to vector
        cern.colt.matrix.impl.DenseDoubleMatrix1D result = 
            new cern.colt.matrix.impl.DenseDoubleMatrix1D(b.dimension());
        for (int i = 0; i < b.dimension(); i++) {
            result.set(i, xMatrix.get(i, 0));
        }
        return fromColtVector(result);
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        if (!canUseColt()) {
            return cpuProvider.transpose(a);
        }
        cern.colt.matrix.linalg.Algebra algebra = new cern.colt.matrix.linalg.Algebra();
        cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a);
        cern.colt.matrix.DoubleMatrix2D result = algebra.transpose(ca);
        return fromColtMatrix(result);
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        if (!canUseColt()) {
            return cpuProvider.scale(scalar, a);
        }
        cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a);
        double s = ((Real) scalar).doubleValue();
        ca.assign(cern.jet.math.Functions.mult(s));
        return fromColtMatrix(ca);
    }

    @Override
    @SuppressWarnings("unchecked")
    public E norm(Vector<E> a) {
        if (!canUseColt()) {
            return cpuProvider.norm(a);
        }
        cern.colt.matrix.DoubleMatrix1D ca = toColtVector(a);
        return (E) Real.of(Math.sqrt(ca.zDotProduct(ca)));
    }

    @Override
    public String getId() {
        return "colt";
    }

    @Override
    public String getDescription() {
        return "Colt Linear Algebra Provider - High-performance scientific computing";
    }
}
