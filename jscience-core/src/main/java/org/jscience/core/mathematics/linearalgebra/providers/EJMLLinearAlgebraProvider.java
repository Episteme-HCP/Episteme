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
 * EJML Linear Algebra Provider.
 * <p>
 * Uses EJML (Efficient Java Matrix Library) for high-performance linear algebra
 * operations. Falls back to CPU provider for non-Real types or when EJML
 * is not available.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService({LinearAlgebraProvider.class, AlgorithmProvider.class})
public class EJMLLinearAlgebraProvider<E> implements LinearAlgebraProvider<E> {

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

    public EJMLLinearAlgebraProvider() {
        this(null);
    }

    @SuppressWarnings("unchecked")
    public EJMLLinearAlgebraProvider(Field<E> field) {
        this.field = (field != null) ? field : (Field<E>) org.jscience.core.mathematics.sets.Reals.getInstance();
        this.cpuProvider = new CPUDenseLinearAlgebraProvider<>(this.field);
        
        if (ejmlAvailable && canUseEJML()) {
            try {
                // Use reflection to load the inner class to stay safe from NoClassDefFoundError
                String innerName = EJMLLinearAlgebraProvider.class.getName() + "$EJMLImpl";
                Class<?> clazz = Class.forName(innerName);
                Constructor<?> ctor = clazz.getDeclaredConstructor(Field.class);
                ctor.setAccessible(true);
                this.ejmlImpl = (LinearAlgebraProvider<E>) ctor.newInstance(this.field);
            } catch (Throwable t) {
                this.ejmlImpl = null;
            }
        }
    }

    public String getName() {
        return "EJML (Optimized)";
    }

    @Override
    public boolean isAvailable() {
        return ejmlAvailable;
    }

    @Override
    public int getPriority() {
        return ejmlAvailable ? 80 : 0;
    }

    private boolean canUseEJML() {
        return field != null && 
               (field instanceof org.jscience.core.mathematics.sets.Reals ||
                Real.class.isAssignableFrom(field.zero().getClass()));
    }

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        if (ejmlImpl == null) return cpuProvider.add(a, b);
        return ejmlImpl.add(a, b);
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        if (ejmlImpl == null) return cpuProvider.subtract(a, b);
        return ejmlImpl.subtract(a, b);
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        if (ejmlImpl == null) return cpuProvider.multiply(vector, scalar);
        return ejmlImpl.multiply(vector, scalar);
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        if (ejmlImpl == null) return cpuProvider.dot(a, b);
        return ejmlImpl.dot(a, b);
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        if (ejmlImpl == null) return cpuProvider.add(a, b);
        return ejmlImpl.add(a, b);
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        if (ejmlImpl == null) return cpuProvider.subtract(a, b);
        return ejmlImpl.subtract(a, b);
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (ejmlImpl == null) return cpuProvider.multiply(a, b);
        return ejmlImpl.multiply(a, b);
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (ejmlImpl == null) return cpuProvider.multiply(a, b);
        return ejmlImpl.multiply(a, b);
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        if (ejmlImpl == null) return cpuProvider.inverse(a);
        return ejmlImpl.inverse(a);
    }

    @Override
    public E determinant(Matrix<E> a) {
        if (ejmlImpl == null) return cpuProvider.determinant(a);
        return ejmlImpl.determinant(a);
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (ejmlImpl == null) return cpuProvider.solve(a, b);
        return ejmlImpl.solve(a, b);
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        if (ejmlImpl == null) return cpuProvider.transpose(a);
        return ejmlImpl.transpose(a);
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        if (ejmlImpl == null) return cpuProvider.scale(scalar, a);
        return ejmlImpl.scale(scalar, a);
    }

    @Override
    public E norm(Vector<E> a) {
        if (ejmlImpl == null) return cpuProvider.norm(a);
        return ejmlImpl.norm(a);
    }

    /**
     * Inner implementation class that touches EJML types.
     * This class is only loaded if EJML is present.
     */
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
            for (int i = 0; i < m.rows(); i++) {
                for (int j = 0; j < m.cols(); j++) {
                    ejml.set(i, j, ((Real) m.get(i, j)).doubleValue());
                }
            }
            return ejml;
        }

        @SuppressWarnings("unchecked")
        private Matrix<E> fromEjmlMatrix(org.ejml.simple.SimpleMatrix ejml) {
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

        private org.ejml.simple.SimpleMatrix toEjmlVector(Vector<E> v) {
            org.ejml.simple.SimpleMatrix ejml = new org.ejml.simple.SimpleMatrix(v.dimension(), 1);
            for (int i = 0; i < v.dimension(); i++) {
                ejml.set(i, 0, ((Real) v.get(i)).doubleValue());
            }
            return ejml;
        }

        @SuppressWarnings("unchecked")
        private Vector<E> fromEjmlVector(org.ejml.simple.SimpleMatrix ejml) {
            int size = ejml.getNumRows();
            E[] data = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), size);
            for (int i = 0; i < size; i++) {
                data[i] = (E) Real.of(ejml.get(i, 0));
            }
            return new GenericVector<>(new DenseVectorStorage<>(data), this, field);
        }

        @Override public Vector<E> add(Vector<E> a, Vector<E> b) {
            return fromEjmlVector(toEjmlVector(a).plus(toEjmlVector(b)));
        }
        @Override public Vector<E> subtract(Vector<E> a, Vector<E> b) {
            return fromEjmlVector(toEjmlVector(a).minus(toEjmlVector(b)));
        }
        @Override public Vector<E> multiply(Vector<E> v, E s) {
            return fromEjmlVector(toEjmlVector(v).scale(((Real) s).doubleValue()));
        }
        @Override @SuppressWarnings("unchecked")
        public E dot(Vector<E> a, Vector<E> b) {
            return (E) Real.of(toEjmlVector(a).transpose().mult(toEjmlVector(b)).get(0, 0));
        }
        @Override public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
            return fromEjmlMatrix(toEjmlMatrix(a).plus(toEjmlMatrix(b)));
        }
        @Override public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
            return fromEjmlMatrix(toEjmlMatrix(a).minus(toEjmlMatrix(b)));
        }
        @Override public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
            return fromEjmlMatrix(toEjmlMatrix(a).mult(toEjmlMatrix(b)));
        }
        @Override public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
            return fromEjmlVector(toEjmlMatrix(a).mult(toEjmlVector(b)));
        }
        @Override public Matrix<E> inverse(Matrix<E> a) {
            return fromEjmlMatrix(toEjmlMatrix(a).invert());
        }
        @Override @SuppressWarnings("unchecked")
        public E determinant(Matrix<E> a) {
            return (E) Real.of(toEjmlMatrix(a).determinant());
        }
        @Override public Vector<E> solve(Matrix<E> a, Vector<E> b) {
            return fromEjmlVector(toEjmlMatrix(a).solve(toEjmlVector(b)));
        }
        @Override public Matrix<E> transpose(Matrix<E> a) {
            return fromEjmlMatrix(toEjmlMatrix(a).transpose());
        }
        @Override public Matrix<E> scale(E s, Matrix<E> a) {
            return fromEjmlMatrix(toEjmlMatrix(a).scale(((Real) s).doubleValue()));
        }
        @Override @SuppressWarnings("unchecked")
        public E norm(Vector<E> a) {
            return (E) Real.of(toEjmlVector(a).normF());
        }
    }
}
