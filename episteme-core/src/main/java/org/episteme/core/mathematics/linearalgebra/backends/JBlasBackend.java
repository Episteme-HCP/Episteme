/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.core.mathematics.linearalgebra.backends;

import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;
import org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage;
import org.episteme.core.mathematics.linearalgebra.vectors.GenericVector;
import org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.structures.rings.Field;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.BackendDiscovery;
import org.episteme.core.technical.backend.ExecutionContext;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.core.technical.backend.cpu.CPUExecutionContext;
import com.google.auto.service.AutoService;
import java.lang.reflect.Constructor;

/**
 * CPU compute backend for JBlas (Java BLAS).
 * <p>
 * JBlas provides linear algebra for Java based on native BLAS/LAPACK libraries,
 * offering high performance for dense matrix operations. This backend wraps the
 * JBlas linear algebra provider and integrates it into the Episteme backend
 * discovery system.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService({Backend.class, CPUBackend.class, LinearAlgebraProvider.class})
public class JBlasBackend<E> implements CPUBackend, LinearAlgebraProvider<E> {

    private static boolean jblasAvailable = false;
    private final Field<E> field;
    private LinearAlgebraProvider<E> jblasImpl;

    static {
        if (Boolean.getBoolean("episteme.jblas.skip")) {
            jblasAvailable = false;
        } else {
            try {
                Class.forName("org.jblas.DoubleMatrix");
                Class.forName("org.jblas.Solve");
                jblasAvailable = true;
            } catch (Throwable t) {
                jblasAvailable = false;
            }
        }
    }

    public JBlasBackend() {
        this(null);
    }

    @SuppressWarnings("unchecked")
    public JBlasBackend(Field<E> field) {
        this.field = (field != null) ? field : (Field<E>) org.episteme.core.mathematics.sets.Reals.getInstance();
        
        if (jblasAvailable && canUseJBlas()) {
            try {
                String innerName = JBlasBackend.class.getName() + "$JBlasImpl";
                Class<?> clazz = Class.forName(innerName);
                Constructor<?> ctor = clazz.getDeclaredConstructor(Field.class);
                ctor.setAccessible(true);
                this.jblasImpl = (LinearAlgebraProvider<E>) ctor.newInstance(this.field);
            } catch (Throwable t) {
                this.jblasImpl = null;
            }
        }
    }

    @Override
    public String getType() {
        return BackendDiscovery.TYPE_LINEAR_ALGEBRA;
    }

    @Override
    public String getId() {
        return "jblas";
    }

    @Override
    public String getName() {
        return jblasAvailable ? "JBlas (Optimized)" : "JBlas";
    }

    @Override
    public String getDescription() {
        return "Linear algebra for Java based on BLAS/LAPACK.";
    }

    @Override
    public boolean isAvailable() {
        return jblasAvailable;
    }

    @Override
    public int getPriority() {
        return jblasAvailable ? 90 : 0;
    }

    @Override
    public ExecutionContext createContext() {
        return new CPUExecutionContext();
    }

    @Override
    public Object createBackend() {
        return this;
    }

    private boolean canUseJBlas() {
        return field != null && 
               (field instanceof org.episteme.core.mathematics.sets.Reals ||
                Real.class.isAssignableFrom(field.zero().getClass()));
    }

    @Override public Vector<E> add(Vector<E> a, Vector<E> b) { checkJBlas(); return jblasImpl.add(a, b); }
    @Override public Vector<E> subtract(Vector<E> a, Vector<E> b) { checkJBlas(); return jblasImpl.subtract(a, b); }
    @Override public Vector<E> multiply(Vector<E> vector, E scalar) { checkJBlas(); return jblasImpl.multiply(vector, scalar); }
    @Override public E dot(Vector<E> a, Vector<E> b) { checkJBlas(); return jblasImpl.dot(a, b); }
    @Override public Matrix<E> add(Matrix<E> a, Matrix<E> b) { checkJBlas(); return jblasImpl.add(a, b); }
    @Override public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) { checkJBlas(); return jblasImpl.subtract(a, b); }
    @Override public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) { checkJBlas(); return jblasImpl.multiply(a, b); }
    @Override public Vector<E> multiply(Matrix<E> a, Vector<E> b) { checkJBlas(); return jblasImpl.multiply(a, b); }
    @Override public Matrix<E> inverse(Matrix<E> a) { checkJBlas(); return jblasImpl.inverse(a); }
    @Override public E determinant(Matrix<E> a) { checkJBlas(); return jblasImpl.determinant(a); }
    @Override public Vector<E> solve(Matrix<E> a, Vector<E> b) { checkJBlas(); return jblasImpl.solve(a, b); }
    @Override public Matrix<E> transpose(Matrix<E> a) { checkJBlas(); return jblasImpl.transpose(a); }
    @Override public Matrix<E> scale(E scalar, Matrix<E> a) { checkJBlas(); return jblasImpl.scale(scalar, a); }
    @Override public E norm(Vector<E> a) { checkJBlas(); return jblasImpl.norm(a); }

    private void checkJBlas() {
        if (jblasImpl == null) {
            throw new UnsupportedOperationException("JBlas implementation not available for this field or environment.");
        }
    }

    /**
     * Inner implementation class that touches JBlas types.
     */
    @SuppressWarnings("unused")
    private static class JBlasImpl<E> implements LinearAlgebraProvider<E> {
        private final Field<E> field;

        JBlasImpl(Field<E> field) {
            this.field = field;
        }

        @Override public String getName() { return "JBlas (Inner)"; }
        @Override public boolean isAvailable() { return true; }
        @Override public int getPriority() { return 90; }

        private org.jblas.DoubleMatrix toJBlasMatrix(Matrix<E> m) {
            double[][] data = new double[m.rows()][m.cols()];
            for (int i = 0; i < m.rows(); i++)
                for (int j = 0; j < m.cols(); j++)
                    data[i][j] = ((Real) m.get(i, j)).doubleValue();
            return new org.jblas.DoubleMatrix(data);
        }

        @SuppressWarnings("unchecked")
        private Matrix<E> fromJBlasMatrix(org.jblas.DoubleMatrix jm) {
            int rows = jm.rows;
            int cols = jm.columns;
            DenseMatrixStorage<E> storage = new DenseMatrixStorage<>(rows, cols, (E) Real.ZERO);
            for (int i = 0; i < rows; i++)
                for (int j = 0; j < cols; j++)
                    storage.set(i, j, (E) Real.of(jm.get(i, j)));
            return new GenericMatrix<>(storage, this, field);
        }

        private org.jblas.DoubleMatrix toJBlasVector(Vector<E> v) {
            double[] data = new double[v.dimension()];
            for (int i = 0; i < v.dimension(); i++)
                data[i] = ((Real) v.get(i)).doubleValue();
            return new org.jblas.DoubleMatrix(data);
        }

        @SuppressWarnings("unchecked")
        private Vector<E> fromJBlasVector(org.jblas.DoubleMatrix jv) {
            int size = jv.length;
            E[] data = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), size);
            for (int i = 0; i < size; i++)
                data[i] = (E) Real.of(jv.get(i));
            return new GenericVector<>(new DenseVectorStorage<>(data), this, field);
        }

        @Override public Vector<E> add(Vector<E> a, Vector<E> b) { return fromJBlasVector(toJBlasVector(a).add(toJBlasVector(b))); }
        @Override public Vector<E> subtract(Vector<E> a, Vector<E> b) { return fromJBlasVector(toJBlasVector(a).sub(toJBlasVector(b))); }
        @Override public Vector<E> multiply(Vector<E> v, E s) { return fromJBlasVector(toJBlasVector(v).mul(((Real) s).doubleValue())); }
        @Override @SuppressWarnings("unchecked")
        public E dot(Vector<E> a, Vector<E> b) { return (E) Real.of(toJBlasVector(a).dot(toJBlasVector(b))); }
        @Override public Matrix<E> add(Matrix<E> a, Matrix<E> b) { return fromJBlasMatrix(toJBlasMatrix(a).add(toJBlasMatrix(b))); }
        @Override public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) { return fromJBlasMatrix(toJBlasMatrix(a).sub(toJBlasMatrix(b))); }
        @Override public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) { return fromJBlasMatrix(toJBlasMatrix(a).mmul(toJBlasMatrix(b))); }
        @Override public Vector<E> multiply(Matrix<E> a, Vector<E> b) { return fromJBlasVector(toJBlasMatrix(a).mmul(toJBlasVector(b))); }
        @Override public Matrix<E> inverse(Matrix<E> a) { return fromJBlasMatrix(org.jblas.Solve.pinv(toJBlasMatrix(a))); }
        @Override public E determinant(Matrix<E> a) { return LinearAlgebraProvider.super.determinant(a); }
        @Override public Vector<E> solve(Matrix<E> a, Vector<E> b) { return fromJBlasVector(org.jblas.Solve.solve(toJBlasMatrix(a), toJBlasVector(b))); }
        @Override public Matrix<E> transpose(Matrix<E> a) { return fromJBlasMatrix(toJBlasMatrix(a).transpose()); }
        @Override public Matrix<E> scale(E s, Matrix<E> a) { return fromJBlasMatrix(toJBlasMatrix(a).mul(((Real) s).doubleValue())); }
        @Override @SuppressWarnings("unchecked")
        public E norm(Vector<E> a) { return (E) org.episteme.core.mathematics.numbers.real.Real.of(toJBlasVector(a).norm2()); }

        @Override public QRResult<E> qr(Matrix<E> a) { return LinearAlgebraProvider.super.qr(a); }
        @Override public SVDResult<E> svd(Matrix<E> a) { return LinearAlgebraProvider.super.svd(a); }
        @Override public EigenResult<E> eigen(Matrix<E> a) { return LinearAlgebraProvider.super.eigen(a); }
        @Override public LUResult<E> lu(Matrix<E> a) { return LinearAlgebraProvider.super.lu(a); }
        @Override public CholeskyResult<E> cholesky(Matrix<E> a) { return LinearAlgebraProvider.super.cholesky(a); }
    }
}
