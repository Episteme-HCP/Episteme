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
 * JBlas Linear Algebra Provider.
 * <p>
 * Uses JBlas (native BLAS/LAPACK) for maximum performance linear algebra
 * operations. Falls back to CPU provider for non-Real types or when JBlas
 * is not available.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class JBlasLinearAlgebraProvider<E> implements LinearAlgebraProvider<E> {

    private static boolean jblasAvailable = false;
    private final Field<E> field;
    private final CPUDenseLinearAlgebraProvider<E> cpuProvider;

    static {
        try {
            Class.forName("org.jblas.DoubleMatrix");
            Class.forName("org.jblas.Solve");
            jblasAvailable = true;
        } catch (ClassNotFoundException e) {
            jblasAvailable = false;
        } catch (UnsatisfiedLinkError e) {
            // Native library not found
            jblasAvailable = false;
        }
    }

    public JBlasLinearAlgebraProvider() {
        this.field = null;
        this.cpuProvider = null;
    }

    public JBlasLinearAlgebraProvider(Field<E> field) {
        this.field = field;
        this.cpuProvider = new CPUDenseLinearAlgebraProvider<>(field);
    }

    @Override
    public String getName() {
        return "JBlas";
    }

    @Override
    public boolean isAvailable() {
        return jblasAvailable;
    }

    @Override
    public ExecutionContext createContext() {
        return new CPUExecutionContext();
    }

    @Override
    public int getPriority() {
        return jblasAvailable ? 90 : 0; // Highest priority due to native performance
    }

    private boolean canUseJBlas() {
        return jblasAvailable && field != null && 
               (field instanceof org.jscience.mathematics.sets.Reals ||
                Real.class.isAssignableFrom(field.zero().getClass()));
    }

    // Conversion helpers
    private org.jblas.DoubleMatrix toJBlasMatrix(Matrix<E> m) {
        double[][] data = new double[m.rows()][m.cols()];
        for (int i = 0; i < m.rows(); i++) {
            for (int j = 0; j < m.cols(); j++) {
                data[i][j] = ((Real) m.get(i, j)).doubleValue();
            }
        }
        return new org.jblas.DoubleMatrix(data);
    }

    @SuppressWarnings("unchecked")
    private Matrix<E> fromJBlasMatrix(org.jblas.DoubleMatrix jm) {
        int rows = jm.rows;
        int cols = jm.columns;
        DenseMatrixStorage<E> storage = new DenseMatrixStorage<>(rows, cols, (E) Real.ZERO);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                storage.set(i, j, (E) Real.of(jm.get(i, j)));
            }
        }
        return new GenericMatrix<>(storage, this, field);
    }

    private org.jblas.DoubleMatrix toJBlasVector(Vector<E> v) {
        double[] data = new double[v.dimension()];
        for (int i = 0; i < v.dimension(); i++) {
            data[i] = ((Real) v.get(i)).doubleValue();
        }
        return new org.jblas.DoubleMatrix(data);
    }

    @SuppressWarnings("unchecked")
    private Vector<E> fromJBlasVector(org.jblas.DoubleMatrix jv) {
        int size = jv.length;
        List<E> data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            data.add((E) Real.of(jv.get(i)));
        }
        return new GenericVector<>(new DenseVectorStorage<>(data), this, field);
    }

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        if (!canUseJBlas()) {
            return cpuProvider.add(a, b);
        }
        org.jblas.DoubleMatrix ja = toJBlasVector(a);
        org.jblas.DoubleMatrix jb = toJBlasVector(b);
        return fromJBlasVector(ja.add(jb));
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        if (!canUseJBlas()) {
            return cpuProvider.subtract(a, b);
        }
        org.jblas.DoubleMatrix ja = toJBlasVector(a);
        org.jblas.DoubleMatrix jb = toJBlasVector(b);
        return fromJBlasVector(ja.sub(jb));
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        if (!canUseJBlas()) {
            return cpuProvider.multiply(vector, scalar);
        }
        org.jblas.DoubleMatrix jv = toJBlasVector(vector);
        double s = ((Real) scalar).doubleValue();
        return fromJBlasVector(jv.mul(s));
    }

    @Override
    @SuppressWarnings("unchecked")
    public E dot(Vector<E> a, Vector<E> b) {
        if (!canUseJBlas()) {
            return cpuProvider.dot(a, b);
        }
        org.jblas.DoubleMatrix ja = toJBlasVector(a);
        org.jblas.DoubleMatrix jb = toJBlasVector(b);
        return (E) Real.of(ja.dot(jb));
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        if (!canUseJBlas()) {
            return cpuProvider.add(a, b);
        }
        org.jblas.DoubleMatrix ja = toJBlasMatrix(a);
        org.jblas.DoubleMatrix jb = toJBlasMatrix(b);
        return fromJBlasMatrix(ja.add(jb));
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        if (!canUseJBlas()) {
            return cpuProvider.subtract(a, b);
        }
        org.jblas.DoubleMatrix ja = toJBlasMatrix(a);
        org.jblas.DoubleMatrix jb = toJBlasMatrix(b);
        return fromJBlasMatrix(ja.sub(jb));
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (!canUseJBlas()) {
            return cpuProvider.multiply(a, b);
        }
        org.jblas.DoubleMatrix ja = toJBlasMatrix(a);
        org.jblas.DoubleMatrix jb = toJBlasMatrix(b);
        return fromJBlasMatrix(ja.mmul(jb));
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (!canUseJBlas()) {
            return cpuProvider.multiply(a, b);
        }
        org.jblas.DoubleMatrix ja = toJBlasMatrix(a);
        org.jblas.DoubleMatrix jb = toJBlasVector(b);
        return fromJBlasVector(ja.mmul(jb));
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        if (!canUseJBlas()) {
            return cpuProvider.inverse(a);
        }
        org.jblas.DoubleMatrix ja = toJBlasMatrix(a);
        return fromJBlasMatrix(org.jblas.Solve.pinv(ja));
    }

    @Override
    public E determinant(Matrix<E> a) {
        if (!canUseJBlas()) {
            return cpuProvider.determinant(a);
        }
        // JBlas doesn't have built-in determinant, use LU decomposition
        // Fall back to CPU for determinant
        return cpuProvider.determinant(a);
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (!canUseJBlas()) {
            return cpuProvider.solve(a, b);
        }
        org.jblas.DoubleMatrix ja = toJBlasMatrix(a);
        org.jblas.DoubleMatrix jb = toJBlasVector(b);
        return fromJBlasVector(org.jblas.Solve.solve(ja, jb));
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        if (!canUseJBlas()) {
            return cpuProvider.transpose(a);
        }
        org.jblas.DoubleMatrix ja = toJBlasMatrix(a);
        return fromJBlasMatrix(ja.transpose());
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        if (!canUseJBlas()) {
            return cpuProvider.scale(scalar, a);
        }
        org.jblas.DoubleMatrix ja = toJBlasMatrix(a);
        double s = ((Real) scalar).doubleValue();
        return fromJBlasMatrix(ja.mul(s));
    }

    @Override
    @SuppressWarnings("unchecked")
    public E norm(Vector<E> a) {
        if (!canUseJBlas()) {
            return cpuProvider.norm(a);
        }
        org.jblas.DoubleMatrix ja = toJBlasVector(a);
        return (E) Real.of(ja.norm2());
    }

    @Override
    public String getId() {
        return "jblas";
    }

    @Override
    public String getDescription() {
        return "JBlas Linear Algebra Provider - Native BLAS/LAPACK acceleration";
    }
}
