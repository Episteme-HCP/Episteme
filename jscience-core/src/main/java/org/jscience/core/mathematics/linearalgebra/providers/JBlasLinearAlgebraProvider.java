/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.linearalgebra.providers;

import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import com.google.auto.service.AutoService;
import java.lang.reflect.Constructor;
import org.jscience.core.mathematics.structures.rings.Field;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.linearalgebra.matrices.GenericMatrix;
import org.jscience.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage;
import org.jscience.core.mathematics.linearalgebra.vectors.GenericVector;
import org.jscience.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage;

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
@AutoService({LinearAlgebraProvider.class, AlgorithmProvider.class})
public class JBlasLinearAlgebraProvider<E> implements LinearAlgebraProvider<E> {

    private static boolean jblasAvailable = false;
    private final Field<E> field;
    private final CPUDenseLinearAlgebraProvider<E> cpuProvider;
    private LinearAlgebraProvider<E> jblasImpl;

    static {
        try {
            Class.forName("org.jblas.DoubleMatrix");
            Class.forName("org.jblas.Solve");
            jblasAvailable = true;
        } catch (Throwable t) {
            jblasAvailable = false;
        }
    }

    public JBlasLinearAlgebraProvider() {
        this(null);
    }

    @SuppressWarnings("unchecked")
    public JBlasLinearAlgebraProvider(Field<E> field) {
        this.field = (field != null) ? field : (Field<E>) org.jscience.core.mathematics.sets.Reals.getInstance();
        this.cpuProvider = new CPUDenseLinearAlgebraProvider<>(this.field);
        
        if (jblasAvailable && canUseJBlas()) {
            try {
                String innerName = JBlasLinearAlgebraProvider.class.getName() + "$JBlasImpl";
                Class<?> clazz = Class.forName(innerName);
                Constructor<?> ctor = clazz.getDeclaredConstructor(Field.class);
                ctor.setAccessible(true);
                this.jblasImpl = (LinearAlgebraProvider<E>) ctor.newInstance(this.field);
            } catch (Throwable t) {
                this.jblasImpl = null;
            }
        }
    }

    public String getName() {
        return "JBlas (Optimized)";
    }

    @Override
    public boolean isAvailable() {
        return jblasAvailable;
    }

    @Override
    public int getPriority() {
        return jblasAvailable ? 90 : 0;
    }

    private boolean canUseJBlas() {
        return field != null && 
               (field instanceof org.jscience.core.mathematics.sets.Reals ||
                Real.class.isAssignableFrom(field.zero().getClass()));
    }

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        if (jblasImpl == null) return cpuProvider.add(a, b);
        return jblasImpl.add(a, b);
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        if (jblasImpl == null) return cpuProvider.subtract(a, b);
        return jblasImpl.subtract(a, b);
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        if (jblasImpl == null) return cpuProvider.multiply(vector, scalar);
        return jblasImpl.multiply(vector, scalar);
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        if (jblasImpl == null) return cpuProvider.dot(a, b);
        return jblasImpl.dot(a, b);
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        if (jblasImpl == null) return cpuProvider.add(a, b);
        return jblasImpl.add(a, b);
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        if (jblasImpl == null) return cpuProvider.subtract(a, b);
        return jblasImpl.subtract(a, b);
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (jblasImpl == null) return cpuProvider.multiply(a, b);
        return jblasImpl.multiply(a, b);
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (jblasImpl == null) return cpuProvider.multiply(a, b);
        return jblasImpl.multiply(a, b);
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        if (jblasImpl == null) return cpuProvider.inverse(a);
        return jblasImpl.inverse(a);
    }

    @Override
    public E determinant(Matrix<E> a) {
        // JBlas determinant is complex, use CPU fallback
        return cpuProvider.determinant(a);
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (jblasImpl == null) return cpuProvider.solve(a, b);
        return jblasImpl.solve(a, b);
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        if (jblasImpl == null) return cpuProvider.transpose(a);
        return jblasImpl.transpose(a);
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        if (jblasImpl == null) return cpuProvider.scale(scalar, a);
        return jblasImpl.scale(scalar, a);
    }

    @Override
    public E norm(Vector<E> a) {
        if (jblasImpl == null) return cpuProvider.norm(a);
        return jblasImpl.norm(a);
    }

    /**
     * Inner implementation class that touches JBlas types.
     * Instantiated via reflection in the constructor to avoid hard dependency.
     */
    @SuppressWarnings("unused") // Loaded via Class.forName in constructor
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
            E[] data = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), size);
            for (int i = 0; i < size; i++) {
                data[i] = (E) Real.of(jv.get(i));
            }
            return new GenericVector<>(new DenseVectorStorage<>(data), this, field);
        }

        @Override public Vector<E> add(Vector<E> a, Vector<E> b) {
            return fromJBlasVector(toJBlasVector(a).add(toJBlasVector(b)));
        }
        @Override public Vector<E> subtract(Vector<E> a, Vector<E> b) {
            return fromJBlasVector(toJBlasVector(a).sub(toJBlasVector(b)));
        }
        @Override public Vector<E> multiply(Vector<E> v, E s) {
            return fromJBlasVector(toJBlasVector(v).mul(((Real) s).doubleValue()));
        }
        @Override @SuppressWarnings("unchecked")
        public E dot(Vector<E> a, Vector<E> b) {
            return (E) Real.of(toJBlasVector(a).dot(toJBlasVector(b)));
        }
        @Override public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
            return fromJBlasMatrix(toJBlasMatrix(a).add(toJBlasMatrix(b)));
        }
        @Override public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
            return fromJBlasMatrix(toJBlasMatrix(a).sub(toJBlasMatrix(b)));
        }
        @Override public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
            return fromJBlasMatrix(toJBlasMatrix(a).mmul(toJBlasMatrix(b)));
        }
        @Override public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
            return fromJBlasVector(toJBlasMatrix(a).mmul(toJBlasVector(b)));
        }
        @Override public Matrix<E> inverse(Matrix<E> a) {
            return fromJBlasMatrix(org.jblas.Solve.pinv(toJBlasMatrix(a)));
        }
        @Override public E determinant(Matrix<E> a) {
            throw new UnsupportedOperationException("Determinant use CPU");
        }
        @Override public Vector<E> solve(Matrix<E> a, Vector<E> b) {
            return fromJBlasVector(org.jblas.Solve.solve(toJBlasMatrix(a), toJBlasVector(b)));
        }
        @Override public Matrix<E> transpose(Matrix<E> a) {
            return fromJBlasMatrix(toJBlasMatrix(a).transpose());
        }
        @Override public Matrix<E> scale(E s, Matrix<E> a) {
            return fromJBlasMatrix(toJBlasMatrix(a).mul(((Real) s).doubleValue()));
        }
        @Override @SuppressWarnings("unchecked")
        public E norm(Vector<E> a) {
            return (E) Real.of(toJBlasVector(a).norm2());
        }
    }
}
