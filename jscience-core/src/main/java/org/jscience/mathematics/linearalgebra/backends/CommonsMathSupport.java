/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.LUDecomposition;

/**
 * Internal Delegate for Commons Math Linear Algebra Operations.
 * This class is only loaded if the Commons Math library is present in the classpath.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CommonsMathSupport<E> implements LinearAlgebraProvider<E> {

    private final Field<E> field;

    public CommonsMathSupport(Field<E> field) {
        this.field = field;
    }

    @Override
    public String getName() {
        return "Commons Math (Internal)";
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
    private RealMatrix toCommonsMatrix(Matrix<E> m) {
        double[][] data = new double[m.rows()][m.cols()];
        for (int i = 0; i < m.rows(); i++) {
            for (int j = 0; j < m.cols(); j++) {
                data[i][j] = ((Real) m.get(i, j)).doubleValue();
            }
        }
        return MatrixUtils.createRealMatrix(data);
    }

    @SuppressWarnings("unchecked")
    private Matrix<E> fromCommonsMatrix(RealMatrix cm) {
        int rows = cm.getRowDimension();
        int cols = cm.getColumnDimension();
        DenseMatrixStorage<E> storage = new DenseMatrixStorage<>(rows, cols, (E) Real.ZERO);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                storage.set(i, j, (E) Real.of(cm.getEntry(i, j)));
            }
        }
        return new GenericMatrix<>(storage, this, field);
    }

    private RealVector toCommonsVector(Vector<E> v) {
        double[] data = new double[v.dimension()];
        for (int i = 0; i < v.dimension(); i++) {
            data[i] = ((Real) v.get(i)).doubleValue();
        }
        return MatrixUtils.createRealVector(data);
    }

    @SuppressWarnings("unchecked")
    private Vector<E> fromCommonsVector(RealVector cv) {
        int size = cv.getDimension();
        E[] data = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), size);
        for (int i = 0; i < size; i++) {
            data[i] = (E) Real.of(cv.getEntry(i));
        }
        return new GenericVector<>(new DenseVectorStorage<>(data), this, field);
    }

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        RealVector ca = toCommonsVector(a);
        RealVector cb = toCommonsVector(b);
        return fromCommonsVector(ca.add(cb));
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        RealVector ca = toCommonsVector(a);
        RealVector cb = toCommonsVector(b);
        return fromCommonsVector(ca.subtract(cb));
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        RealVector cv = toCommonsVector(vector);
        double s = ((Real) scalar).doubleValue();
        return fromCommonsVector(cv.mapMultiply(s));
    }

    @Override
    @SuppressWarnings("unchecked")
    public E dot(Vector<E> a, Vector<E> b) {
        RealVector ca = toCommonsVector(a);
        RealVector cb = toCommonsVector(b);
        return (E) Real.of(ca.dotProduct(cb));
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        RealMatrix ca = toCommonsMatrix(a);
        RealMatrix cb = toCommonsMatrix(b);
        return fromCommonsMatrix(ca.add(cb));
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        RealMatrix ca = toCommonsMatrix(a);
        RealMatrix cb = toCommonsMatrix(b);
        return fromCommonsMatrix(ca.subtract(cb));
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        RealMatrix ca = toCommonsMatrix(a);
        RealMatrix cb = toCommonsMatrix(b);
        return fromCommonsMatrix(ca.multiply(cb));
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        RealMatrix ca = toCommonsMatrix(a);
        RealVector cb = toCommonsVector(b);
        return fromCommonsVector(ca.operate(cb));
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        RealMatrix ca = toCommonsMatrix(a);
        LUDecomposition lu = new LUDecomposition(ca);
        return fromCommonsMatrix(lu.getSolver().getInverse());
    }

    @Override
    @SuppressWarnings("unchecked")
    public E determinant(Matrix<E> a) {
        RealMatrix ca = toCommonsMatrix(a);
        LUDecomposition lu = new LUDecomposition(ca);
        return (E) Real.of(lu.getDeterminant());
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        RealMatrix ca = toCommonsMatrix(a);
        RealVector cb = toCommonsVector(b);
        LUDecomposition lu = new LUDecomposition(ca);
        return fromCommonsVector(lu.getSolver().solve(cb));
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        RealMatrix ca = toCommonsMatrix(a);
        return fromCommonsMatrix(ca.transpose());
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        RealMatrix ca = toCommonsMatrix(a);
        double s = ((Real) scalar).doubleValue();
        return fromCommonsMatrix(ca.scalarMultiply(s));
    }

    @Override
    @SuppressWarnings("unchecked")
    public E norm(Vector<E> a) {
        RealVector ca = toCommonsVector(a);
        return (E) Real.of(ca.getNorm());
    }
}
