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
 * Colt Linear Algebra Provider.
 * <p>
 * Uses Colt (CERN Open Source Libraries for High Performance Scientific and Technical Computing).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService({LinearAlgebraProvider.class, AlgorithmProvider.class})
public class ColtLinearAlgebraProvider<E> implements LinearAlgebraProvider<E> {

    private static boolean coltAvailable = false;
    private final Field<E> field;
    private final CPUDenseLinearAlgebraProvider<E> cpuProvider;
    private LinearAlgebraProvider<E> coltImpl;

    static {
        try {
            Class.forName("cern.colt.matrix.DoubleMatrix2D");
            coltAvailable = true;
        } catch (Throwable t) {
            coltAvailable = false;
        }
    }

    public ColtLinearAlgebraProvider() {
        this(null);
    }

    @SuppressWarnings("unchecked")
    public ColtLinearAlgebraProvider(Field<E> field) {
        this.field = (field != null) ? field : (Field<E>) org.jscience.core.mathematics.sets.Reals.getInstance();
        this.cpuProvider = new CPUDenseLinearAlgebraProvider<>(this.field);
        
        if (coltAvailable && canUseColt()) {
            try {
                String innerName = ColtLinearAlgebraProvider.class.getName() + "$ColtImpl";
                Class<?> clazz = Class.forName(innerName);
                Constructor<?> ctor = clazz.getDeclaredConstructor(Field.class);
                ctor.setAccessible(true);
                this.coltImpl = (LinearAlgebraProvider<E>) ctor.newInstance(this.field);
            } catch (Throwable t) {
                this.coltImpl = null;
            }
        }
    }

    public String getName() {
        return "Colt (Optimized)";
    }

    @Override
    public boolean isAvailable() {
        return coltAvailable;
    }

    @Override
    public int getPriority() {
        return coltAvailable ? 70 : 0;
    }

    private boolean canUseColt() {
        return field != null && 
               (field instanceof org.jscience.core.mathematics.sets.Reals ||
                Real.class.isAssignableFrom(field.zero().getClass()));
    }

    @Override public Vector<E> add(Vector<E> a, Vector<E> b) { if (coltImpl == null) return cpuProvider.add(a, b); return coltImpl.add(a, b); }
    @Override public Vector<E> subtract(Vector<E> a, Vector<E> b) { if (coltImpl == null) return cpuProvider.subtract(a, b); return coltImpl.subtract(a, b); }
    @Override public Vector<E> multiply(Vector<E> vector, E scalar) { if (coltImpl == null) return cpuProvider.multiply(vector, scalar); return coltImpl.multiply(vector, scalar); }
    @Override public E dot(Vector<E> a, Vector<E> b) { if (coltImpl == null) return cpuProvider.dot(a, b); return coltImpl.dot(a, b); }
    @Override public Matrix<E> add(Matrix<E> a, Matrix<E> b) { if (coltImpl == null) return cpuProvider.add(a, b); return coltImpl.add(a, b); }
    @Override public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) { if (coltImpl == null) return cpuProvider.subtract(a, b); return coltImpl.subtract(a, b); }
    @Override public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) { if (coltImpl == null) return cpuProvider.multiply(a, b); return coltImpl.multiply(a, b); }
    @Override public Vector<E> multiply(Matrix<E> a, Vector<E> b) { if (coltImpl == null) return cpuProvider.multiply(a, b); return coltImpl.multiply(a, b); }
    @Override public Matrix<E> inverse(Matrix<E> a) { if (coltImpl == null) return cpuProvider.inverse(a); return coltImpl.inverse(a); }
    @Override public E determinant(Matrix<E> a) { if (coltImpl == null) return cpuProvider.determinant(a); return coltImpl.determinant(a); }
    @Override public Vector<E> solve(Matrix<E> a, Vector<E> b) { if (coltImpl == null) return cpuProvider.solve(a, b); return coltImpl.solve(a, b); }
    @Override public Matrix<E> transpose(Matrix<E> a) { if (coltImpl == null) return cpuProvider.transpose(a); return coltImpl.transpose(a); }
    @Override public Matrix<E> scale(E scalar, Matrix<E> a) { if (coltImpl == null) return cpuProvider.scale(scalar, a); return coltImpl.scale(scalar, a); }
    @Override public E norm(Vector<E> a) { if (coltImpl == null) return cpuProvider.norm(a); return coltImpl.norm(a); }

    /**
     * Inner implementation class that touches Colt types.
     */
    private static class ColtImpl<E> implements LinearAlgebraProvider<E> {
        private final Field<E> field;

        ColtImpl(Field<E> field) {
            this.field = field;
        }

        @Override public String getName() { return "Colt (Inner)"; }
        @Override public boolean isAvailable() { return true; }
        @Override public int getPriority() { return 70; }

        private cern.colt.matrix.DoubleMatrix2D toColtMatrix(Matrix<E> m) {
            cern.colt.matrix.impl.DenseDoubleMatrix2D colt = new cern.colt.matrix.impl.DenseDoubleMatrix2D(m.rows(), m.cols());
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
            cern.colt.matrix.impl.DenseDoubleMatrix1D colt = new cern.colt.matrix.impl.DenseDoubleMatrix1D(v.dimension());
            for (int i = 0; i < v.dimension(); i++) {
                colt.set(i, ((Real) v.get(i)).doubleValue());
            }
            return colt;
        }

        @SuppressWarnings("unchecked")
        private Vector<E> fromColtVector(cern.colt.matrix.DoubleMatrix1D colt) {
            int size = (int) colt.size();
            E[] data = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), size);
            for (int i = 0; i < size; i++) {
                data[i] = (E) Real.of(colt.get(i));
            }
            return new GenericVector<>(new DenseVectorStorage<>(data), this, field);
        }

        @Override public Vector<E> add(Vector<E> a, Vector<E> b) {
            cern.colt.matrix.DoubleMatrix1D ca = toColtVector(a);
            ca.assign(toColtVector(b), cern.jet.math.Functions.plus);
            return fromColtVector(ca);
        }
        @Override public Vector<E> subtract(Vector<E> a, Vector<E> b) {
            cern.colt.matrix.DoubleMatrix1D ca = toColtVector(a);
            ca.assign(toColtVector(b), cern.jet.math.Functions.minus);
            return fromColtVector(ca);
        }
        @Override public Vector<E> multiply(Vector<E> v, E s) {
            cern.colt.matrix.DoubleMatrix1D cv = toColtVector(v);
            cv.assign(cern.jet.math.Functions.mult(((Real) s).doubleValue()));
            return fromColtVector(cv);
        }
        @Override @SuppressWarnings("unchecked")
        public E dot(Vector<E> a, Vector<E> b) {
            return (E) Real.of(toColtVector(a).zDotProduct(toColtVector(b)));
        }
        @Override public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
            cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a);
            ca.assign(toColtMatrix(b), cern.jet.math.Functions.plus);
            return fromColtMatrix(ca);
        }
        @Override public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
            cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a);
            ca.assign(toColtMatrix(b), cern.jet.math.Functions.minus);
            return fromColtMatrix(ca);
        }
        @Override public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
            return fromColtMatrix(new cern.colt.matrix.linalg.Algebra().mult(toColtMatrix(a), toColtMatrix(b)));
        }
        @Override public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
            return fromColtVector(new cern.colt.matrix.linalg.Algebra().mult(toColtMatrix(a), toColtVector(b)));
        }
        @Override public Matrix<E> inverse(Matrix<E> a) {
            return fromColtMatrix(new cern.colt.matrix.linalg.Algebra().inverse(toColtMatrix(a)));
        }
        @Override @SuppressWarnings("unchecked")
        public E determinant(Matrix<E> a) {
            return (E) Real.of(new cern.colt.matrix.linalg.Algebra().det(toColtMatrix(a)));
        }
        @Override public Vector<E> solve(Matrix<E> a, Vector<E> b) {
            cern.colt.matrix.linalg.Algebra algebra = new cern.colt.matrix.linalg.Algebra();
            cern.colt.matrix.DoubleMatrix2D bMatrix = new cern.colt.matrix.impl.DenseDoubleMatrix2D(b.dimension(), 1);
            for (int i = 0; i < b.dimension(); i++) bMatrix.set(i, 0, ((Real) b.get(i)).doubleValue());
            cern.colt.matrix.DoubleMatrix2D xMatrix = algebra.solve(toColtMatrix(a), bMatrix);
            cern.colt.matrix.impl.DenseDoubleMatrix1D result = new cern.colt.matrix.impl.DenseDoubleMatrix1D(b.dimension());
            for (int i = 0; i < b.dimension(); i++) result.set(i, xMatrix.get(i, 0));
            return fromColtVector(result);
        }
        @Override public Matrix<E> transpose(Matrix<E> a) {
            return fromColtMatrix(new cern.colt.matrix.linalg.Algebra().transpose(toColtMatrix(a)));
        }
        @Override public Matrix<E> scale(E s, Matrix<E> a) {
            cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a);
            ca.assign(cern.jet.math.Functions.mult(((Real) s).doubleValue()));
            return fromColtMatrix(ca);
        }
        @Override @SuppressWarnings("unchecked")
        public E norm(Vector<E> a) {
            return (E) Real.of(Math.sqrt(toColtVector(a).zDotProduct(toColtVector(a))));
        }
    }
}
