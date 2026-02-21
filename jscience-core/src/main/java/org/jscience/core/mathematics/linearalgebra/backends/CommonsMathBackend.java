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

package org.jscience.core.mathematics.linearalgebra.backends;

import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.linearalgebra.matrices.GenericMatrix;
import org.jscience.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage;
import org.jscience.core.mathematics.linearalgebra.providers.CPUDenseLinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.vectors.GenericVector;
import org.jscience.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.structures.rings.Field;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.core.technical.backend.Backend;
import org.jscience.core.technical.backend.BackendDiscovery;
import org.jscience.core.technical.backend.ExecutionContext;
import org.jscience.core.technical.backend.cpu.CPUBackend;
import org.jscience.core.technical.backend.cpu.CPUExecutionContext;
import com.google.auto.service.AutoService;
import java.lang.reflect.Constructor;

/**
 * CPU compute backend for Apache Commons Math.
 * <p>
 * Commons Math is a general-purpose mathematics and statistics library.
 * This backend wraps the Commons Math linear algebra provider and integrates
 * it into the JScience backend discovery system.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService({Backend.class, CPUBackend.class, LinearAlgebraProvider.class, AlgorithmProvider.class})
public class CommonsMathBackend<E> implements CPUBackend, LinearAlgebraProvider<E> {

    private static boolean commonsAvailable = false;
    private final Field<E> field;
    private final CPUDenseLinearAlgebraProvider<E> cpuProvider;
    private LinearAlgebraProvider<E> commonsImpl;

    static {
        try {
            Class.forName("org.apache.commons.math3.linear.RealMatrix");
            commonsAvailable = true;
        } catch (Throwable t) {
            commonsAvailable = false;
        }
    }

    public CommonsMathBackend() {
        this(null);
    }

    @SuppressWarnings("unchecked")
    public CommonsMathBackend(Field<E> field) {
        this.field = (field != null) ? field : (Field<E>) org.jscience.core.mathematics.sets.Reals.getInstance();
        this.cpuProvider = new CPUDenseLinearAlgebraProvider<>(this.field);
        
        if (commonsAvailable && canUseCommons()) {
            try {
                String innerName = CommonsMathBackend.class.getName() + "$CommonsImpl";
                Class<?> clazz = Class.forName(innerName);
                Constructor<?> ctor = clazz.getDeclaredConstructor(Field.class);
                ctor.setAccessible(true);
                this.commonsImpl = (LinearAlgebraProvider<E>) ctor.newInstance(this.field);
            } catch (Throwable t) {
                this.commonsImpl = null;
            }
        }
    }

    @Override
    public String getType() {
        return BackendDiscovery.TYPE_LINEAR_ALGEBRA;
    }

    @Override
    public String getId() {
        return "commonsmath";
    }

    @Override
    public String getName() {
        return commonsAvailable ? "Apache Commons Math (Optimized)" : "Apache Commons Math";
    }

    @Override
    public String getDescription() {
        return "General purpose mathematics and statistics library.";
    }

    @Override
    public boolean isAvailable() {
        return commonsAvailable;
    }

    @Override
    public int getPriority() {
        return commonsAvailable ? 50 : 0;
    }

    @Override
    public ExecutionContext createContext() {
        return new CPUExecutionContext();
    }

    @Override
    public Object createBackend() {
        return this;
    }

    private boolean canUseCommons() {
        return field != null && 
               (field instanceof org.jscience.core.mathematics.sets.Reals ||
                Real.class.isAssignableFrom(field.zero().getClass()));
    }

    @Override public Vector<E> add(Vector<E> a, Vector<E> b) { if (commonsImpl == null) return cpuProvider.add(a, b); return commonsImpl.add(a, b); }
    @Override public Vector<E> subtract(Vector<E> a, Vector<E> b) { if (commonsImpl == null) return cpuProvider.subtract(a, b); return commonsImpl.subtract(a, b); }
    @Override public Vector<E> multiply(Vector<E> v, E s) { if (commonsImpl == null) return cpuProvider.multiply(v, s); return commonsImpl.multiply(v, s); }
    @Override public E dot(Vector<E> a, Vector<E> b) { if (commonsImpl == null) return cpuProvider.dot(a, b); return commonsImpl.dot(a, b); }
    @Override public Matrix<E> add(Matrix<E> a, Matrix<E> b) { if (commonsImpl == null) return cpuProvider.add(a, b); return commonsImpl.add(a, b); }
    @Override public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) { if (commonsImpl == null) return cpuProvider.subtract(a, b); return commonsImpl.subtract(a, b); }
    @Override public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) { if (commonsImpl == null) return cpuProvider.multiply(a, b); return commonsImpl.multiply(a, b); }
    @Override public Vector<E> multiply(Matrix<E> a, Vector<E> b) { if (commonsImpl == null) return cpuProvider.multiply(a, b); return commonsImpl.multiply(a, b); }
    @Override public Matrix<E> inverse(Matrix<E> a) { if (commonsImpl == null) return cpuProvider.inverse(a); return commonsImpl.inverse(a); }
    @Override public E determinant(Matrix<E> a) { if (commonsImpl == null) return cpuProvider.determinant(a); return commonsImpl.determinant(a); }
    @Override public Vector<E> solve(Matrix<E> a, Vector<E> b) { if (commonsImpl == null) return cpuProvider.solve(a, b); return commonsImpl.solve(a, b); }
    @Override public Matrix<E> transpose(Matrix<E> a) { if (commonsImpl == null) return cpuProvider.transpose(a); return commonsImpl.transpose(a); }
    @Override public Matrix<E> scale(E s, Matrix<E> a) { if (commonsImpl == null) return cpuProvider.scale(s, a); return commonsImpl.scale(s, a); }
    @Override public E norm(Vector<E> a) { if (commonsImpl == null) return cpuProvider.norm(a); return commonsImpl.norm(a); }

    /**
     * Inner implementation class that touches Commons Math types.
     */
    @SuppressWarnings("unused")
    private static class CommonsImpl<E> implements LinearAlgebraProvider<E> {
        private final Field<E> field;

        CommonsImpl(Field<E> field) {
            this.field = field;
        }

        @Override public String getName() { return "Commons Math (Inner)"; }
        @Override public boolean isAvailable() { return true; }
        @Override public int getPriority() { return 50; }

        private org.apache.commons.math3.linear.RealMatrix toCommonsMatrix(Matrix<E> m) {
            double[][] data = new double[m.rows()][m.cols()];
            for (int i = 0; i < m.rows(); i++)
                for (int j = 0; j < m.cols(); j++)
                    data[i][j] = ((Real) m.get(i, j)).doubleValue();
            return org.apache.commons.math3.linear.MatrixUtils.createRealMatrix(data);
        }

        @SuppressWarnings("unchecked")
        private Matrix<E> fromCommonsMatrix(org.apache.commons.math3.linear.RealMatrix cm) {
            int rows = cm.getRowDimension();
            int cols = cm.getColumnDimension();
            DenseMatrixStorage<E> storage = new DenseMatrixStorage<>(rows, cols, (E) Real.ZERO);
            for (int i = 0; i < rows; i++)
                for (int j = 0; j < cols; j++)
                    storage.set(i, j, (E) Real.of(cm.getEntry(i, j)));
            return new GenericMatrix<>(storage, this, field);
        }

        private org.apache.commons.math3.linear.RealVector toCommonsVector(Vector<E> v) {
            double[] data = new double[v.dimension()];
            for (int i = 0; i < v.dimension(); i++)
                data[i] = ((Real) v.get(i)).doubleValue();
            return org.apache.commons.math3.linear.MatrixUtils.createRealVector(data);
        }

        @SuppressWarnings("unchecked")
        private Vector<E> fromCommonsVector(org.apache.commons.math3.linear.RealVector cv) {
            int size = cv.getDimension();
            E[] data = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), size);
            for (int i = 0; i < size; i++)
                data[i] = (E) Real.of(cv.getEntry(i));
            return new GenericVector<>(new DenseVectorStorage<>(data), this, field);
        }

        @Override public Vector<E> add(Vector<E> a, Vector<E> b) { return fromCommonsVector(toCommonsVector(a).add(toCommonsVector(b))); }
        @Override public Vector<E> subtract(Vector<E> a, Vector<E> b) { return fromCommonsVector(toCommonsVector(a).subtract(toCommonsVector(b))); }
        @Override public Vector<E> multiply(Vector<E> v, E s) { return fromCommonsVector(toCommonsVector(v).mapMultiply(((Real) s).doubleValue())); }
        @Override @SuppressWarnings("unchecked")
        public E dot(Vector<E> a, Vector<E> b) { return (E) Real.of(toCommonsVector(a).dotProduct(toCommonsVector(b))); }
        @Override public Matrix<E> add(Matrix<E> a, Matrix<E> b) { return fromCommonsMatrix(toCommonsMatrix(a).add(toCommonsMatrix(b))); }
        @Override public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) { return fromCommonsMatrix(toCommonsMatrix(a).subtract(toCommonsMatrix(b))); }
        @Override public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) { return fromCommonsMatrix(toCommonsMatrix(a).multiply(toCommonsMatrix(b))); }
        @Override public Vector<E> multiply(Matrix<E> a, Vector<E> b) { return fromCommonsVector(toCommonsMatrix(a).operate(toCommonsVector(b))); }
        @Override public Matrix<E> inverse(Matrix<E> a) { return fromCommonsMatrix(new org.apache.commons.math3.linear.LUDecomposition(toCommonsMatrix(a)).getSolver().getInverse()); }
        @Override @SuppressWarnings("unchecked")
        public E determinant(Matrix<E> a) { return (E) Real.of(new org.apache.commons.math3.linear.LUDecomposition(toCommonsMatrix(a)).getDeterminant()); }
        @Override public Vector<E> solve(Matrix<E> a, Vector<E> b) { return fromCommonsVector(new org.apache.commons.math3.linear.LUDecomposition(toCommonsMatrix(a)).getSolver().solve(toCommonsVector(b))); }
        @Override public Matrix<E> transpose(Matrix<E> a) { return fromCommonsMatrix(toCommonsMatrix(a).transpose()); }
        @Override public Matrix<E> scale(E s, Matrix<E> a) { return fromCommonsMatrix(toCommonsMatrix(a).scalarMultiply(((Real) s).doubleValue())); }
        @Override @SuppressWarnings("unchecked")
        public E norm(Vector<E> a) { return (E) Real.of(toCommonsVector(a).getNorm()); }
    }
}
