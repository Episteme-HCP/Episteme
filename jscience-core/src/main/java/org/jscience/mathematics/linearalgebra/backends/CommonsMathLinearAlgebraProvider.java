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
 * Apache Commons Math Linear Algebra Provider.
 * <p>
 * Uses Apache Commons Math library for linear algebra operations.
 * Falls back to CPU provider for non-Real types or when Commons Math
 * is not available.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CommonsMathLinearAlgebraProvider<E> implements LinearAlgebraProvider<E> {

    private static boolean commonsMathAvailable = false;
    private final Field<E> field;
    private final CPUDenseLinearAlgebraProvider<E> cpuProvider;

    static {
        try {
            Class.forName("org.apache.commons.math3.linear.RealMatrix");
            Class.forName("org.apache.commons.math3.linear.LUDecomposition");
            commonsMathAvailable = true;
        } catch (ClassNotFoundException e) {
            commonsMathAvailable = false;
        }
    }

    public CommonsMathLinearAlgebraProvider() {
        this.field = null;
        this.cpuProvider = null;
    }

    public CommonsMathLinearAlgebraProvider(Field<E> field) {
        this.field = field;
        this.cpuProvider = new CPUDenseLinearAlgebraProvider<>(field);
    }

    @Override
    public String getName() {
        return "Commons Math";
    }

    @Override
    public boolean isAvailable() {
        return commonsMathAvailable;
    }

    @Override
    public ExecutionContext createContext() {
        return new CPUExecutionContext();
    }

    @Override
    public int getPriority() {
        return commonsMathAvailable ? 50 : 0;
    }

    private boolean canUseCommonsMath() {
        return commonsMathAvailable && field != null && 
               (field instanceof org.jscience.mathematics.sets.Reals ||
                Real.class.isAssignableFrom(field.zero().getClass()));
    }

    // Conversion helpers
    private org.apache.commons.math3.linear.RealMatrix toCommonsMatrix(Matrix<E> m) {
        double[][] data = new double[m.rows()][m.cols()];
        for (int i = 0; i < m.rows(); i++) {
            for (int j = 0; j < m.cols(); j++) {
                data[i][j] = ((Real) m.get(i, j)).doubleValue();
            }
        }
        return org.apache.commons.math3.linear.MatrixUtils.createRealMatrix(data);
    }

    @SuppressWarnings("unchecked")
    private Matrix<E> fromCommonsMatrix(org.apache.commons.math3.linear.RealMatrix cm) {
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

    private org.apache.commons.math3.linear.RealVector toCommonsVector(Vector<E> v) {
        double[] data = new double[v.dimension()];
        for (int i = 0; i < v.dimension(); i++) {
            data[i] = ((Real) v.get(i)).doubleValue();
        }
        return org.apache.commons.math3.linear.MatrixUtils.createRealVector(data);
    }

    @SuppressWarnings("unchecked")
    private Vector<E> fromCommonsVector(org.apache.commons.math3.linear.RealVector cv) {
        int size = cv.getDimension();
        List<E> data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            data.add((E) Real.of(cv.getEntry(i)));
        }
        return new GenericVector<>(new DenseVectorStorage<>(data), this, field);
    }

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        if (!canUseCommonsMath()) {
            return cpuProvider.add(a, b);
        }
        org.apache.commons.math3.linear.RealVector ca = toCommonsVector(a);
        org.apache.commons.math3.linear.RealVector cb = toCommonsVector(b);
        return fromCommonsVector(ca.add(cb));
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        if (!canUseCommonsMath()) {
            return cpuProvider.subtract(a, b);
        }
        org.apache.commons.math3.linear.RealVector ca = toCommonsVector(a);
        org.apache.commons.math3.linear.RealVector cb = toCommonsVector(b);
        return fromCommonsVector(ca.subtract(cb));
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        if (!canUseCommonsMath()) {
            return cpuProvider.multiply(vector, scalar);
        }
        org.apache.commons.math3.linear.RealVector cv = toCommonsVector(vector);
        double s = ((Real) scalar).doubleValue();
        return fromCommonsVector(cv.mapMultiply(s));
    }

    @Override
    @SuppressWarnings("unchecked")
    public E dot(Vector<E> a, Vector<E> b) {
        if (!canUseCommonsMath()) {
            return cpuProvider.dot(a, b);
        }
        org.apache.commons.math3.linear.RealVector ca = toCommonsVector(a);
        org.apache.commons.math3.linear.RealVector cb = toCommonsVector(b);
        return (E) Real.of(ca.dotProduct(cb));
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        if (!canUseCommonsMath()) {
            return cpuProvider.add(a, b);
        }
        org.apache.commons.math3.linear.RealMatrix ca = toCommonsMatrix(a);
        org.apache.commons.math3.linear.RealMatrix cb = toCommonsMatrix(b);
        return fromCommonsMatrix(ca.add(cb));
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        if (!canUseCommonsMath()) {
            return cpuProvider.subtract(a, b);
        }
        org.apache.commons.math3.linear.RealMatrix ca = toCommonsMatrix(a);
        org.apache.commons.math3.linear.RealMatrix cb = toCommonsMatrix(b);
        return fromCommonsMatrix(ca.subtract(cb));
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (!canUseCommonsMath()) {
            return cpuProvider.multiply(a, b);
        }
        org.apache.commons.math3.linear.RealMatrix ca = toCommonsMatrix(a);
        org.apache.commons.math3.linear.RealMatrix cb = toCommonsMatrix(b);
        return fromCommonsMatrix(ca.multiply(cb));
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (!canUseCommonsMath()) {
            return cpuProvider.multiply(a, b);
        }
        org.apache.commons.math3.linear.RealMatrix ca = toCommonsMatrix(a);
        org.apache.commons.math3.linear.RealVector cb = toCommonsVector(b);
        return fromCommonsVector(ca.operate(cb));
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        if (!canUseCommonsMath()) {
            return cpuProvider.inverse(a);
        }
        org.apache.commons.math3.linear.RealMatrix ca = toCommonsMatrix(a);
        org.apache.commons.math3.linear.LUDecomposition lu = 
            new org.apache.commons.math3.linear.LUDecomposition(ca);
        return fromCommonsMatrix(lu.getSolver().getInverse());
    }

    @Override
    @SuppressWarnings("unchecked")
    public E determinant(Matrix<E> a) {
        if (!canUseCommonsMath()) {
            return cpuProvider.determinant(a);
        }
        org.apache.commons.math3.linear.RealMatrix ca = toCommonsMatrix(a);
        org.apache.commons.math3.linear.LUDecomposition lu = 
            new org.apache.commons.math3.linear.LUDecomposition(ca);
        return (E) Real.of(lu.getDeterminant());
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (!canUseCommonsMath()) {
            return cpuProvider.solve(a, b);
        }
        org.apache.commons.math3.linear.RealMatrix ca = toCommonsMatrix(a);
        org.apache.commons.math3.linear.RealVector cb = toCommonsVector(b);
        org.apache.commons.math3.linear.LUDecomposition lu = 
            new org.apache.commons.math3.linear.LUDecomposition(ca);
        return fromCommonsVector(lu.getSolver().solve(cb));
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        if (!canUseCommonsMath()) {
            return cpuProvider.transpose(a);
        }
        org.apache.commons.math3.linear.RealMatrix ca = toCommonsMatrix(a);
        return fromCommonsMatrix(ca.transpose());
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        if (!canUseCommonsMath()) {
            return cpuProvider.scale(scalar, a);
        }
        org.apache.commons.math3.linear.RealMatrix ca = toCommonsMatrix(a);
        double s = ((Real) scalar).doubleValue();
        return fromCommonsMatrix(ca.scalarMultiply(s));
    }

    @Override
    @SuppressWarnings("unchecked")
    public E norm(Vector<E> a) {
        if (!canUseCommonsMath()) {
            return cpuProvider.norm(a);
        }
        org.apache.commons.math3.linear.RealVector ca = toCommonsVector(a);
        return (E) Real.of(ca.getNorm());
    }

    @Override
    public String getId() {
        return "commonsmath";
    }

    @Override
    public String getDescription() {
        return "Commons Math Linear Algebra Provider - General purpose mathematics library";
    }
}
