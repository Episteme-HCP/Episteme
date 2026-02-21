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
 * CPU compute backend for EJML (Efficient Java Matrix Library).
 * <p>
 * EJML is a high-performance, pure Java linear algebra library focused on
 * dense matrices. This backend wraps the EJML linear algebra provider and
 * integrates it into the JScience backend discovery system.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService({Backend.class, CPUBackend.class, LinearAlgebraProvider.class, AlgorithmProvider.class})
public class EJMLBackend<E> implements CPUBackend, LinearAlgebraProvider<E> {

    private static boolean ejmlAvailable = false;
    private final Field<E> field;
    private final CPUDenseLinearAlgebraProvider<E> cpuProvider;
    private LinearAlgebraProvider<E> ejmlImpl;

    static {
        try {
            Class.forName("org.ejml.simple.SimpleMatrix");
            Class.forName("org.ejml.dense.row.CommonOps_DDRM");
            ejmlAvailable = true;
        } catch (Throwable t) {
            ejmlAvailable = false;
        }
    }

    public EJMLBackend() {
        this(null);
    }

    @SuppressWarnings("unchecked")
    public EJMLBackend(Field<E> field) {
        this.field = (field != null) ? field : (Field<E>) org.jscience.core.mathematics.sets.Reals.getInstance();
        this.cpuProvider = new CPUDenseLinearAlgebraProvider<>(this.field);
        
        if (ejmlAvailable && canUseEJML()) {
            try {
                String innerName = EJMLBackend.class.getName() + "$EJMLImpl";
                Class<?> clazz = Class.forName(innerName);
                Constructor<?> ctor = clazz.getDeclaredConstructor(Field.class);
                ctor.setAccessible(true);
                this.ejmlImpl = (LinearAlgebraProvider<E>) ctor.newInstance(this.field);
            } catch (Throwable t) {
                this.ejmlImpl = null;
            }
        }
    }

    @Override
    public String getType() {
        return BackendDiscovery.TYPE_LINEAR_ALGEBRA;
    }

    @Override
    public String getId() {
        return "ejml";
    }

    @Override
    public String getName() {
        return ejmlAvailable ? "EJML (Optimized)" : "EJML";
    }

    @Override
    public String getDescription() {
        return "High-performance linear algebra library.";
    }

    @Override
    public boolean isAvailable() {
        return ejmlAvailable;
    }

    @Override
    public int getPriority() {
        return ejmlAvailable ? 80 : 0;
    }

    @Override
    public ExecutionContext createContext() {
        return new CPUExecutionContext();
    }

    @Override
    public Object createBackend() {
        return this;
    }

    private boolean canUseEJML() {
        return field != null && 
               (field instanceof org.jscience.core.mathematics.sets.Reals ||
                Real.class.isAssignableFrom(field.zero().getClass()));
    }

    @Override public Vector<E> add(Vector<E> a, Vector<E> b) { if (ejmlImpl == null) return cpuProvider.add(a, b); return ejmlImpl.add(a, b); }
    @Override public Vector<E> subtract(Vector<E> a, Vector<E> b) { if (ejmlImpl == null) return cpuProvider.subtract(a, b); return ejmlImpl.subtract(a, b); }
    @Override public Vector<E> multiply(Vector<E> v, E s) { if (ejmlImpl == null) return cpuProvider.multiply(v, s); return ejmlImpl.multiply(v, s); }
    @Override public E dot(Vector<E> a, Vector<E> b) { if (ejmlImpl == null) return cpuProvider.dot(a, b); return ejmlImpl.dot(a, b); }
    @Override public Matrix<E> add(Matrix<E> a, Matrix<E> b) { if (ejmlImpl == null) return cpuProvider.add(a, b); return ejmlImpl.add(a, b); }
    @Override public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) { if (ejmlImpl == null) return cpuProvider.subtract(a, b); return ejmlImpl.subtract(a, b); }
    @Override public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) { if (ejmlImpl == null) return cpuProvider.multiply(a, b); return ejmlImpl.multiply(a, b); }
    @Override public Vector<E> multiply(Matrix<E> a, Vector<E> b) { if (ejmlImpl == null) return cpuProvider.multiply(a, b); return ejmlImpl.multiply(a, b); }
    @Override public Matrix<E> inverse(Matrix<E> a) { if (ejmlImpl == null) return cpuProvider.inverse(a); return ejmlImpl.inverse(a); }
    @Override public E determinant(Matrix<E> a) { if (ejmlImpl == null) return cpuProvider.determinant(a); return ejmlImpl.determinant(a); }
    @Override public Vector<E> solve(Matrix<E> a, Vector<E> b) { if (ejmlImpl == null) return cpuProvider.solve(a, b); return ejmlImpl.solve(a, b); }
    @Override public Matrix<E> transpose(Matrix<E> a) { if (ejmlImpl == null) return cpuProvider.transpose(a); return ejmlImpl.transpose(a); }
    @Override public Matrix<E> scale(E s, Matrix<E> a) { if (ejmlImpl == null) return cpuProvider.scale(s, a); return ejmlImpl.scale(s, a); }
    @Override public E norm(Vector<E> a) { if (ejmlImpl == null) return cpuProvider.norm(a); return ejmlImpl.norm(a); }

    /**
     * Inner implementation class that touches EJML types.
     */
    @SuppressWarnings("unused")
    private static class EJMLImpl<E> implements LinearAlgebraProvider<E> {
        private final Field<E> field;

        EJMLImpl(Field<E> field) {
            this.field = field;
        }

        @Override public String getName() { return "EJML (Inner)"; }
        @Override public boolean isAvailable() { return true; }
        @Override public int getPriority() { return 80; }

        private org.ejml.simple.SimpleMatrix toEjmlMatrix(Matrix<E> m) {
            org.ejml.simple.SimpleMatrix ejml = new org.ejml.simple.SimpleMatrix(m.rows(), m.cols());
            for (int i = 0; i < m.rows(); i++)
                for (int j = 0; j < m.cols(); j++)
                    ejml.set(i, j, ((Real) m.get(i, j)).doubleValue());
            return ejml;
        }

        @SuppressWarnings("unchecked")
        private Matrix<E> fromEjmlMatrix(org.ejml.simple.SimpleMatrix ejml) {
            int rows = ejml.getNumRows();
            int cols = ejml.getNumCols();
            DenseMatrixStorage<E> storage = new DenseMatrixStorage<>(rows, cols, (E) Real.ZERO);
            for (int i = 0; i < rows; i++)
                for (int j = 0; j < cols; j++)
                    storage.set(i, j, (E) Real.of(ejml.get(i, j)));
            return new GenericMatrix<>(storage, this, field);
        }

        private org.ejml.simple.SimpleMatrix toEjmlVector(Vector<E> v) {
            org.ejml.simple.SimpleMatrix ejml = new org.ejml.simple.SimpleMatrix(v.dimension(), 1);
            for (int i = 0; i < v.dimension(); i++)
                ejml.set(i, 0, ((Real) v.get(i)).doubleValue());
            return ejml;
        }

        @SuppressWarnings("unchecked")
        private Vector<E> fromEjmlVector(org.ejml.simple.SimpleMatrix ejml) {
            int size = ejml.getNumRows();
            E[] data = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), size);
            for (int i = 0; i < size; i++)
                data[i] = (E) Real.of(ejml.get(i, 0));
            return new GenericVector<>(new DenseVectorStorage<>(data), this, field);
        }

        @Override public Vector<E> add(Vector<E> a, Vector<E> b) { return fromEjmlVector(toEjmlVector(a).plus(toEjmlVector(b))); }
        @Override public Vector<E> subtract(Vector<E> a, Vector<E> b) { return fromEjmlVector(toEjmlVector(a).minus(toEjmlVector(b))); }
        @Override public Vector<E> multiply(Vector<E> v, E s) { return fromEjmlVector(toEjmlVector(v).scale(((Real) s).doubleValue())); }
        @Override @SuppressWarnings("unchecked")
        public E dot(Vector<E> a, Vector<E> b) { return (E) Real.of(toEjmlVector(a).transpose().mult(toEjmlVector(b)).get(0, 0)); }
        @Override public Matrix<E> add(Matrix<E> a, Matrix<E> b) { return fromEjmlMatrix(toEjmlMatrix(a).plus(toEjmlMatrix(b))); }
        @Override public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) { return fromEjmlMatrix(toEjmlMatrix(a).minus(toEjmlMatrix(b))); }
        @Override public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) { return fromEjmlMatrix(toEjmlMatrix(a).mult(toEjmlMatrix(b))); }
        @Override public Vector<E> multiply(Matrix<E> a, Vector<E> b) { return fromEjmlVector(toEjmlMatrix(a).mult(toEjmlVector(b))); }
        @Override public Matrix<E> inverse(Matrix<E> a) { return fromEjmlMatrix(toEjmlMatrix(a).invert()); }
        @Override @SuppressWarnings("unchecked")
        public E determinant(Matrix<E> a) { return (E) Real.of(toEjmlMatrix(a).determinant()); }
        @Override public Vector<E> solve(Matrix<E> a, Vector<E> b) { return fromEjmlVector(toEjmlMatrix(a).solve(toEjmlVector(b))); }
        @Override public Matrix<E> transpose(Matrix<E> a) { return fromEjmlMatrix(toEjmlMatrix(a).transpose()); }
        @Override public Matrix<E> scale(E s, Matrix<E> a) { return fromEjmlMatrix(toEjmlMatrix(a).scale(((Real) s).doubleValue())); }
        @Override @SuppressWarnings("unchecked")
        public E norm(Vector<E> a) { return (E) Real.of(toEjmlVector(a).normF()); }
    }
}
