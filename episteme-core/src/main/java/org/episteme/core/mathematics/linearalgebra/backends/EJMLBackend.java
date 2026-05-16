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
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.core.technical.algorithm.OperationContext.Hint;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.BackendDiscovery;
import org.episteme.core.technical.backend.ExecutionContext;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.core.technical.backend.cpu.CPUExecutionContext;
import com.google.auto.service.AutoService;
import java.lang.reflect.Constructor;

/**
 * CPU compute backend for EJML (Efficient Java Matrix Library).
 * <p>
 * EJML is a high-performance, pure Java linear algebra library focused on
 * dense matrices. This backend wraps the EJML linear algebra provider and
 * integrates it into the Episteme backend discovery system.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService({Backend.class, CPUBackend.class, LinearAlgebraProvider.class})
public class EJMLBackend<E> implements CPUBackend, LinearAlgebraProvider<E> {

    private static boolean ejmlAvailable = false;
    private final Field<E> field;
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
        this.field = (field != null) ? field : (Field<E>) org.episteme.core.mathematics.sets.Reals.getInstance();
        
        if (ejmlAvailable && canUseEJML()) {
            try {
                String innerName = EJMLBackend.class.getName() + "$EJMLImpl";
                Class<?> clazz = Class.forName(innerName);
                Constructor<?> ctor = clazz.getDeclaredConstructor(EJMLBackend.class, Field.class);
                ctor.setAccessible(true);
                this.ejmlImpl = (LinearAlgebraProvider<E>) ctor.newInstance(this, this.field);
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
        return ejmlAvailable && !org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision() && canUseEJML();
    }

    @Override
    public int getPriority() {
        return ejmlAvailable ? 80 : 0;
    }

    @Override
    public double score(OperationContext context) {
        if (!ejmlAvailable || !canUseEJML() || org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision()) return -1.0;
        if (context.hasHint(Hint.COMPLEX)) return -1.0;
        
        double score = getPriority();
        if (context.hasHint(Hint.DENSE)) score += 10.0;
        if (context.hasHint(Hint.SPARSE)) score -= 50.0; // EJML is dense-optimized
        
        // Granular scoring (example: EJML is great at multiplication and inversion)
        if (context.hasHint(Hint.MAT_MUL)) score += 5.0;
        if (context.hasHint(Hint.MAT_INV)) score += 5.0;
        
        return score;
    }

    @Override
    public ExecutionContext createContext() {
        return new CPUExecutionContext();
    }

    @Override
    public Object createBackend() {
        return this;
    }

    @Override
    public void shutdown() {
        // Nothing to release for EJML
    }

    private boolean canUseEJML() {
        if (field == null) return false;
        Object zero = field.zero();
        return (field instanceof org.episteme.core.mathematics.sets.Reals ||
                zero instanceof Real ||
                zero instanceof Double ||
                zero instanceof Float ||
                zero instanceof Integer);
    }

    @Override
    public boolean isCompatible(org.episteme.core.mathematics.structures.rings.Ring<?> ring) {
        if (ring instanceof Field) {
            Field<?> f = (Field<?>) ring;
            Object zero = f.zero();
            return (f instanceof org.episteme.core.mathematics.sets.Reals ||
                    zero instanceof Real ||
                    zero instanceof Double ||
                    zero instanceof Float ||
                    zero instanceof Integer);
        }
        return false;
    }

    @Override public Vector<E> add(Vector<E> a, Vector<E> b) { checkEjml(); return ejmlImpl.add(a, b); }
    @Override public Vector<E> subtract(Vector<E> a, Vector<E> b) { checkEjml(); return ejmlImpl.subtract(a, b); }
    @Override public Vector<E> multiply(Vector<E> v, E s) { checkEjml(); return ejmlImpl.multiply(v, s); }
    @Override public E dot(Vector<E> a, Vector<E> b) { checkEjml(); return ejmlImpl.dot(a, b); }
    @Override public Matrix<E> add(Matrix<E> a, Matrix<E> b) { checkEjml(); return ejmlImpl.add(a, b); }
    @Override public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) { checkEjml(); return ejmlImpl.subtract(a, b); }
    @Override public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) { checkEjml(); return ejmlImpl.multiply(a, b); }
    @Override public Vector<E> multiply(Matrix<E> a, Vector<E> b) { checkEjml(); return ejmlImpl.multiply(a, b); }
    @Override public Matrix<E> inverse(Matrix<E> a) { checkEjml(); return ejmlImpl.inverse(a); }
    @Override public E determinant(Matrix<E> a) { checkEjml(); return ejmlImpl.determinant(a); }
    @Override public Vector<E> solve(Matrix<E> a, Vector<E> b) { checkEjml(); return ejmlImpl.solve(a, b); }
    @Override public Matrix<E> transpose(Matrix<E> a) { checkEjml(); return ejmlImpl.transpose(a); }
    @Override public Matrix<E> scale(E s, Matrix<E> a) { checkEjml(); return ejmlImpl.scale(s, a); }
    @Override public E norm(Vector<E> a) { checkEjml(); return ejmlImpl.norm(a); }
    @Override public E angle(Vector<E> a, Vector<E> b) { checkEjml(); return ejmlImpl.angle(a, b); }

    @Override public QRResult<E> qr(Matrix<E> a) { checkEjml(); return ejmlImpl.qr(a); }
    @Override public SVDResult<E> svd(Matrix<E> a) { checkEjml(); return ejmlImpl.svd(a); }
    @Override public EigenResult<E> eigen(Matrix<E> a) { checkEjml(); return ejmlImpl.eigen(a); }
    @Override public LUResult<E> lu(Matrix<E> a) { checkEjml(); return ejmlImpl.lu(a); }
    @Override public CholeskyResult<E> cholesky(Matrix<E> a) { checkEjml(); return ejmlImpl.cholesky(a); }
    @Override public E trace(Matrix<E> a) { checkEjml(); return ejmlImpl.trace(a); }
    @Override public Matrix<E> exp(Matrix<E> a) { checkEjml(); return ejmlImpl.exp(a); }
    @Override public Matrix<E> log(Matrix<E> a) { checkEjml(); return ejmlImpl.log(a); }
    @Override public Matrix<E> log10(Matrix<E> a) { checkEjml(); return ejmlImpl.log10(a); }
    @Override public Matrix<E> sin(Matrix<E> a) { checkEjml(); return ejmlImpl.sin(a); }
    @Override public Matrix<E> cos(Matrix<E> a) { checkEjml(); return ejmlImpl.cos(a); }
    @Override public Matrix<E> tan(Matrix<E> a) { checkEjml(); return ejmlImpl.tan(a); }
    @Override public Matrix<E> asin(Matrix<E> a) { checkEjml(); return ejmlImpl.asin(a); }
    @Override public Matrix<E> acos(Matrix<E> a) { checkEjml(); return ejmlImpl.acos(a); }
    @Override public Matrix<E> atan(Matrix<E> a) { checkEjml(); return ejmlImpl.atan(a); }
    @Override public Matrix<E> sinh(Matrix<E> a) { checkEjml(); return ejmlImpl.sinh(a); }
    @Override public Matrix<E> cosh(Matrix<E> a) { checkEjml(); return ejmlImpl.cosh(a); }
    @Override public Matrix<E> tanh(Matrix<E> a) { checkEjml(); return ejmlImpl.tanh(a); }
    @Override public Matrix<E> asinh(Matrix<E> a) { checkEjml(); return ejmlImpl.asinh(a); }
    @Override public Matrix<E> acosh(Matrix<E> a) { checkEjml(); return ejmlImpl.acosh(a); }
    @Override public Matrix<E> atanh(Matrix<E> a) { checkEjml(); return ejmlImpl.atanh(a); }
    @Override public Matrix<E> sqrt(Matrix<E> a) { checkEjml(); return ejmlImpl.sqrt(a); }
    @Override public Matrix<E> cbrt(Matrix<E> a) { checkEjml(); return ejmlImpl.cbrt(a); }
    @Override public Matrix<E> pow(Matrix<E> a, E exponent) { checkEjml(); return ejmlImpl.pow(a, exponent); }
    @Override public Vector<E> normalize(Vector<E> v) { checkEjml(); return ejmlImpl.normalize(v); }
    @Override public Vector<E> cross(Vector<E> a, Vector<E> b) { checkEjml(); return ejmlImpl.cross(a, b); }
    @Override public Vector<E> projection(Vector<E> a, Vector<E> b) { checkEjml(); return ejmlImpl.projection(a, b); }
    @Override public Vector<E> solveTriangular(Matrix<E> A, Vector<E> b, boolean upper, boolean transpose, boolean conjugate, boolean unit) { checkEjml(); return ejmlImpl.solveTriangular(A, b, upper, transpose, conjugate, unit); }

    private void checkEjml() {
        if (ejmlImpl == null) {
            throw new UnsupportedOperationException("EJML implementation not available for this field or environment.");
        }
    }

    /**
     * Inner implementation class that touches EJML types.
     */
    @SuppressWarnings("unused")
    private static class EJMLImpl<E> implements LinearAlgebraProvider<E> {
        private final EJMLBackend<E> parent;
        private final Field<E> field;

        EJMLImpl(EJMLBackend<E> parent, Field<E> field) {
            this.parent = parent;
            this.field = field;
        }

        @Override public int getPriority() { return parent.getPriority(); }
        
        @Override
        public E angle(Vector<E> a, Vector<E> b) {
            double dot = toDouble(dot(a, b));
            double nA = toDouble(norm(a));
            double nB = toDouble(norm(b));
            
            if (nA == 0 || nB == 0) return ring().zero();

            // Using atan2 for better numerical stability: theta = atan2(|a x b|, a . b)
            double crossNormSq = Math.max(0, (nA * nA * nB * nB) - (dot * dot));
            double theta = Math.atan2(Math.sqrt(crossNormSq), dot);
            
            return fromDouble(theta);
        }

        private Ring<E> ring() {
            return field;
        }

        private double toDouble(E val) {
            if (val instanceof Real) return ((Real) val).doubleValue();
            if (val instanceof Number) return ((Number) val).doubleValue();
            return Double.parseDouble(val.toString());
        }

        @SuppressWarnings("unchecked")
        private E fromDouble(double val) {
            E zero = field.zero();
            if (zero instanceof org.episteme.core.mathematics.numbers.real.RealFloat) {
                return (E) org.episteme.core.mathematics.numbers.real.RealFloat.of((float) val);
            }
            if (zero instanceof Real) return (E) Real.of(val);
            if (zero instanceof Number) {
                if (zero instanceof Double) return (E) Double.valueOf(val);
                if (zero instanceof Float) return (E) Float.valueOf((float) val);
                if (zero instanceof Integer) return (E) Integer.valueOf((int) val);
                return (E) (Object) val;
            }
            return (E) (Object) val;
        }

        private org.ejml.simple.SimpleMatrix toEjmlMatrix(Matrix<E> m) {
            int rows = m.rows();
            int cols = m.cols();
            
            // Optimization: Direct array access if possible
            if (m instanceof org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix rdm) {
                org.episteme.core.mathematics.linearalgebra.matrices.storage.RealDoubleMatrixStorage storage = rdm.getDoubleStorage();
                double[] data = storage.getData();
                if (data != null) {
                    return org.ejml.simple.SimpleMatrix.wrap(org.ejml.data.DMatrixRMaj.wrap(rows, cols, data.clone()));
                }
            }
            
            org.ejml.simple.SimpleMatrix ejml = new org.ejml.simple.SimpleMatrix(rows, cols);
            for (int i = 0; i < rows; i++)
                for (int j = 0; j < cols; j++) {
                    E val = m.get(i, j);
                    if (val instanceof Real) {
                        ejml.set(i, j, ((Real) val).doubleValue());
                    } else if (val instanceof Number) {
                        ejml.set(i, j, ((Number) val).doubleValue());
                    } else if (val != null) {
                        try {
                            ejml.set(i, j, Double.parseDouble(val.toString()));
                        } catch (Exception e) {
                            throw new ClassCastException("EJMLBackend cannot convert " + val.getClass().getName() + " to double: " + val);
                        }
                    }
                }
            return ejml;
        }

        private Matrix<E> fromEjmlMatrix(org.ejml.simple.SimpleMatrix ejml) {
            int rows = ejml.getNumRows();
            int cols = ejml.getNumCols();
            double[] data = ejml.getDDRM().data;
            
            // Optimization: Direct wrap if using Reals
            if (field instanceof org.episteme.core.mathematics.sets.Reals) {
                return (Matrix<E>) org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix.of(data.clone(), rows, cols, (org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<org.episteme.core.mathematics.numbers.real.Real>)(Object)this);
            }
            
            DenseMatrixStorage<E> storage = new DenseMatrixStorage<>(rows, cols, field.zero());
            for (int i = 0; i < rows; i++)
                for (int j = 0; j < cols; j++)
                    storage.set(i, j, fromDouble(ejml.get(i, j)));
            return new GenericMatrix<>(storage, this, field);
        }

        private org.ejml.simple.SimpleMatrix toEjmlVector(Vector<E> v) {
            int dim = v.dimension();
            
            // Optimization: Direct array access for RealDoubleVector
            if (v instanceof org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector rdv) {
                double[] data = rdv.toDoubleArray(); // This is already a clone or direct access depending on impl
                return org.ejml.simple.SimpleMatrix.wrap(org.ejml.data.DMatrixRMaj.wrap(dim, 1, data));
            }

            org.ejml.simple.SimpleMatrix ejml = new org.ejml.simple.SimpleMatrix(dim, 1);
            for (int i = 0; i < dim; i++) {
                E val = v.get(i);
                if (val instanceof Real) {
                    ejml.set(i, 0, ((Real) val).doubleValue());
                } else if (val instanceof Number) {
                    ejml.set(i, 0, ((Number) val).doubleValue());
                } else if (val != null) {
                    try {
                        ejml.set(i, 0, Double.parseDouble(val.toString()));
                    } catch (Exception e) {
                        throw new ClassCastException("EJMLBackend cannot convert " + val.getClass().getName() + " to double: " + val);
                    }
                }
            }
            return ejml;
        }

        @SuppressWarnings("unchecked")
        private Vector<E> fromEjmlVector(org.ejml.simple.SimpleMatrix ejml) {
            int size = ejml.getNumRows();
            double[] data = ejml.getDDRM().data;

            // Optimization: Direct wrap if using Reals
            if (field instanceof org.episteme.core.mathematics.sets.Reals) {
                return (Vector<E>) org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(data.clone());
            }

            Class<?> componentType = field.zero().getClass();
            if (field.zero() instanceof Real) componentType = Real.class;
            else if (field.zero() instanceof org.episteme.core.mathematics.numbers.complex.Complex) componentType = org.episteme.core.mathematics.numbers.complex.Complex.class;
            
            E[] arr = (E[]) java.lang.reflect.Array.newInstance(componentType, size);
            for (int i = 0; i < size; i++)
                arr[i] = fromDouble(data[i]);
            return new GenericVector<>(new DenseVectorStorage<>(arr), this, field);
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
        @Override public Matrix<E> inverse(Matrix<E> a) {
            org.ejml.simple.SimpleMatrix m = toEjmlMatrix(a);
            if (a.rows() == a.cols()) {
                return fromEjmlMatrix(m.invert());
            } else {
                // Explicitly use pseudo Inverse with SVD
                org.ejml.data.DMatrixRMaj pinv = new org.ejml.data.DMatrixRMaj(a.cols(), a.rows());
                org.ejml.dense.row.CommonOps_DDRM.pinv(m.getDDRM(), pinv);
                return fromEjmlMatrix(org.ejml.simple.SimpleMatrix.wrap(pinv));
            }
        }
        @Override
        public E determinant(Matrix<E> a) { return fromDouble(toEjmlMatrix(a).determinant()); }
        @Override public Vector<E> solve(Matrix<E> a, Vector<E> b) { return fromEjmlVector(toEjmlMatrix(a).solve(toEjmlVector(b))); }
        @Override public Matrix<E> transpose(Matrix<E> a) { return fromEjmlMatrix(toEjmlMatrix(a).transpose()); }
        @Override public Matrix<E> scale(E s, Matrix<E> a) { return fromEjmlMatrix(toEjmlMatrix(a).scale(((Real) s).doubleValue())); }
        @Override
        public E norm(Vector<E> a) { return fromDouble(toEjmlVector(a).normF()); }

        @Override
        public QRResult<E> qr(Matrix<E> a) {
            org.ejml.simple.SimpleMatrix ma = toEjmlMatrix(a);
            org.ejml.interfaces.decomposition.QRDecomposition<org.ejml.data.DMatrixRMaj> qr = 
                org.ejml.dense.row.factory.DecompositionFactory_DDRM.qr(ma.getNumRows(), ma.getNumCols());
            qr.decompose(ma.getDDRM());
            
            org.ejml.data.DMatrixRMaj Q = new org.ejml.data.DMatrixRMaj(ma.getNumRows(), ma.getNumRows());
            org.ejml.data.DMatrixRMaj R = new org.ejml.data.DMatrixRMaj(ma.getNumRows(), ma.getNumCols());
            qr.getQ(Q, false);
            qr.getR(R, false);
            
            return new QRResult<E>(
                fromEjmlMatrix(org.ejml.simple.SimpleMatrix.wrap(Q)),
                fromEjmlMatrix(org.ejml.simple.SimpleMatrix.wrap(R))
            );
        }

        @Override
        @SuppressWarnings("unchecked")
        public SVDResult<E> svd(Matrix<E> a) {
            org.ejml.simple.SimpleMatrix ma = toEjmlMatrix(a);
            org.ejml.interfaces.decomposition.SingularValueDecomposition_F64<org.ejml.data.DMatrixRMaj> svd = 
                org.ejml.dense.row.factory.DecompositionFactory_DDRM.svd(ma.getNumRows(), ma.getNumCols(), true, true, false);
            svd.decompose(ma.getDDRM());
            
            org.ejml.data.DMatrixRMaj U = new org.ejml.data.DMatrixRMaj(ma.getNumRows(), ma.getNumRows());
            org.ejml.data.DMatrixRMaj V = new org.ejml.data.DMatrixRMaj(ma.getNumCols(), ma.getNumCols());
            svd.getU(U, false);
            svd.getV(V, false);
            double[] singleton = svd.getSingularValues();
            
            E[] sData = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), singleton.length);
            for(int i=0; i<singleton.length; i++) sData[i] = fromDouble(singleton[i]);
            Vector<E> S = new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(
                new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(sData), this, field);
                
            return new SVDResult<E>(fromEjmlMatrix(org.ejml.simple.SimpleMatrix.wrap(U)), S, fromEjmlMatrix(org.ejml.simple.SimpleMatrix.wrap(V)));
        }

        @Override
        @SuppressWarnings("unchecked")
        public EigenResult<E> eigen(Matrix<E> a) {
            org.ejml.simple.SimpleMatrix ma = toEjmlMatrix(a);
            org.ejml.interfaces.decomposition.EigenDecomposition_F64<org.ejml.data.DMatrixRMaj> evd = 
                org.ejml.dense.row.factory.DecompositionFactory_DDRM.eig(ma.getNumRows(), true);
            evd.decompose(ma.getDDRM());
            
            org.ejml.data.DMatrixRMaj Vmat = org.ejml.dense.row.EigenOps_DDRM.createMatrixV(evd);
            org.ejml.data.DMatrixRMaj Dmat = org.ejml.dense.row.EigenOps_DDRM.createMatrixD(evd);
            
            // Extract eigenvalues as a vector
            int size = Dmat.getNumRows();
            E[] dData = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), size);
            for(int i=0; i<size; i++) dData[i] = fromDouble(Dmat.get(i, i));
            Vector<E> D = new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(
                new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(dData), this, field);
                
            return new EigenResult<E>(fromEjmlMatrix(org.ejml.simple.SimpleMatrix.wrap(Vmat)), D);
        }

        @Override
        @SuppressWarnings("unchecked")
        public LUResult<E> lu(Matrix<E> a) {
            org.ejml.simple.SimpleMatrix ma = toEjmlMatrix(a);
            
            var luDecomp = org.ejml.dense.row.factory.DecompositionFactory_DDRM.lu(ma.getNumRows(), ma.getNumCols());
            luDecomp.decompose(ma.getDDRM());
            
            org.ejml.data.DMatrixRMaj Lmat = new org.ejml.data.DMatrixRMaj(ma.getNumRows(), ma.getNumCols());
            org.ejml.data.DMatrixRMaj Umat = new org.ejml.data.DMatrixRMaj(ma.getNumRows(), ma.getNumCols());
            
            // Try direct getters first, then fallback to combined matrix
            try {
                // Some versions have these direct methods
                java.lang.reflect.Method getLower = luDecomp.getClass().getMethod("getLower", org.ejml.data.DMatrixRMaj.class);
                java.lang.reflect.Method getUpper = luDecomp.getClass().getMethod("getUpper", org.ejml.data.DMatrixRMaj.class);
                getLower.invoke(luDecomp, Lmat);
                getUpper.invoke(luDecomp, Umat);
            } catch (Exception e) {
                // Fallback to combined LU matrix if direct getters are missing
                try {
                     org.ejml.data.DMatrixRMaj combined = null;
                     try {
                         combined = (org.ejml.data.DMatrixRMaj) luDecomp.getClass().getMethod("getLU").invoke(luDecomp);
                     } catch (Exception e2) {
                         combined = (org.ejml.data.DMatrixRMaj) luDecomp.getClass().getMethod("getMatrix").invoke(luDecomp);
                     }
                     org.ejml.dense.row.SpecializedOps_DDRM.copyTriangle(combined, Lmat, true);
                     org.ejml.dense.row.SpecializedOps_DDRM.copyTriangle(combined, Umat, false);
                     for(int i=0; i<ma.getNumRows(); i++) Lmat.set(i, i, 1.0);
                } catch (Exception e3) {
                     throw new RuntimeException("Could not extract LU factors from EJML decomposition", e3);
                }
            }
            
            int[] pivotArr = luDecomp.getRowPivotV(null);
            E[] pData = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), pivotArr.length);
            for(int i=0; i<pivotArr.length; i++) pData[i] = fromDouble(pivotArr[i]);
            Vector<E> P = new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(
                new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(pData), this, field);
                
            return new LUResult<E>(fromEjmlMatrix(org.ejml.simple.SimpleMatrix.wrap(Lmat)), fromEjmlMatrix(org.ejml.simple.SimpleMatrix.wrap(Umat)), P);
        }

        @Override
        public CholeskyResult<E> cholesky(Matrix<E> a) {
            org.ejml.simple.SimpleMatrix ma = toEjmlMatrix(a);
            org.ejml.interfaces.decomposition.CholeskyDecomposition_F64<org.ejml.data.DMatrixRMaj> chol = 
                org.ejml.dense.row.factory.DecompositionFactory_DDRM.chol(ma.getNumRows(), true);
            chol.decompose(ma.getDDRM());
            org.ejml.data.DMatrixRMaj L = new org.ejml.data.DMatrixRMaj(ma.getNumRows(), ma.getNumRows());
            chol.getT(L);
            return new CholeskyResult<E>(fromEjmlMatrix(org.ejml.simple.SimpleMatrix.wrap(L)));
        }

        @Override
        public E trace(Matrix<E> a) { return fromDouble(toEjmlMatrix(a).trace()); }

        @Override public Matrix<E> exp(Matrix<E> a) { return fromEjmlMatrix(toEjmlMatrix(a).elementExp()); }
        @Override public Matrix<E> log(Matrix<E> a) { return fromEjmlMatrix(toEjmlMatrix(a).elementLog()); }
        @Override public Matrix<E> log10(Matrix<E> a) { 
            org.ejml.simple.SimpleMatrix ma = toEjmlMatrix(a);
            for(int i=0; i<ma.getNumElements(); i++) ma.set(i, Math.log10(ma.get(i)));
            return fromEjmlMatrix(ma);
        }
        @Override public Matrix<E> sin(Matrix<E> a) {
            org.ejml.simple.SimpleMatrix ma = toEjmlMatrix(a);
            for(int i=0; i<ma.getNumElements(); i++) ma.set(i, Math.sin(ma.get(i)));
            return fromEjmlMatrix(ma);
        }
        @Override public Matrix<E> cos(Matrix<E> a) {
            org.ejml.simple.SimpleMatrix ma = toEjmlMatrix(a);
            for(int i=0; i<ma.getNumElements(); i++) ma.set(i, Math.cos(ma.get(i)));
            return fromEjmlMatrix(ma);
        }
        @Override public Matrix<E> tan(Matrix<E> a) {
            org.ejml.simple.SimpleMatrix ma = toEjmlMatrix(a);
            for(int i=0; i<ma.getNumElements(); i++) ma.set(i, Math.tan(ma.get(i)));
            return fromEjmlMatrix(ma);
        }
        @Override public Matrix<E> asin(Matrix<E> a) {
            org.ejml.simple.SimpleMatrix ma = toEjmlMatrix(a);
            for(int i=0; i<ma.getNumElements(); i++) ma.set(i, Math.asin(ma.get(i)));
            return fromEjmlMatrix(ma);
        }
        @Override public Matrix<E> acos(Matrix<E> a) {
            org.ejml.simple.SimpleMatrix ma = toEjmlMatrix(a);
            for(int i=0; i<ma.getNumElements(); i++) ma.set(i, Math.acos(ma.get(i)));
            return fromEjmlMatrix(ma);
        }
        @Override public Matrix<E> atan(Matrix<E> a) {
            org.ejml.simple.SimpleMatrix ma = toEjmlMatrix(a);
            for(int i=0; i<ma.getNumElements(); i++) ma.set(i, Math.atan(ma.get(i)));
            return fromEjmlMatrix(ma);
        }
        @Override public Matrix<E> sinh(Matrix<E> a) {
            org.ejml.simple.SimpleMatrix ma = toEjmlMatrix(a);
            for(int i=0; i<ma.getNumElements(); i++) ma.set(i, Math.sinh(ma.get(i)));
            return fromEjmlMatrix(ma);
        }
        @Override public Matrix<E> cosh(Matrix<E> a) {
            org.ejml.simple.SimpleMatrix ma = toEjmlMatrix(a);
            for(int i=0; i<ma.getNumElements(); i++) ma.set(i, Math.cosh(ma.get(i)));
            return fromEjmlMatrix(ma);
        }
        @Override public Matrix<E> tanh(Matrix<E> a) {
            org.ejml.simple.SimpleMatrix ma = toEjmlMatrix(a);
            for(int i=0; i<ma.getNumElements(); i++) ma.set(i, Math.tanh(ma.get(i)));
            return fromEjmlMatrix(ma);
        }
        @Override public Matrix<E> asinh(Matrix<E> a) {
            org.ejml.simple.SimpleMatrix ma = toEjmlMatrix(a);
            for(int i=0; i<ma.getNumElements(); i++) {
                double x = ma.get(i);
                ma.set(i, Math.log(x + Math.sqrt(x * x + 1.0)));
            }
            return fromEjmlMatrix(ma);
        }
        @Override public Matrix<E> acosh(Matrix<E> a) {
            org.ejml.simple.SimpleMatrix ma = toEjmlMatrix(a);
            for(int i=0; i<ma.getNumElements(); i++) {
                double x = ma.get(i);
                ma.set(i, Math.log(x + Math.sqrt(x * x - 1.0)));
            }
            return fromEjmlMatrix(ma);
        }
        @Override public Matrix<E> atanh(Matrix<E> a) {
            org.ejml.simple.SimpleMatrix ma = toEjmlMatrix(a);
            for(int i=0; i<ma.getNumElements(); i++) {
                double x = ma.get(i);
                ma.set(i, 0.5 * Math.log((1.0 + x) / (1.0 - x)));
            }
            return fromEjmlMatrix(ma);
        }
        @Override public Matrix<E> sqrt(Matrix<E> a) {
            org.ejml.simple.SimpleMatrix ma = toEjmlMatrix(a);
            for(int i=0; i<ma.getNumElements(); i++) ma.set(i, Math.sqrt(ma.get(i)));
            return fromEjmlMatrix(ma);
        }
        @Override public Matrix<E> cbrt(Matrix<E> a) {
            org.ejml.simple.SimpleMatrix ma = toEjmlMatrix(a);
            for(int i=0; i<ma.getNumElements(); i++) ma.set(i, Math.cbrt(ma.get(i)));
            return fromEjmlMatrix(ma);
        }
        @Override public Matrix<E> pow(Matrix<E> a, E exponent) {
            return fromEjmlMatrix(toEjmlMatrix(a).elementPower(toDouble(exponent)));
        }

        @Override public Vector<E> normalize(Vector<E> v) {
            double n = toDouble(norm(v));
            if (n == 0) return v;
            return multiply(v, fromDouble(1.0 / n));
        }

        @Override public Vector<E> cross(Vector<E> a, Vector<E> b) {
            if (a.dimension() != 3 || b.dimension() != 3) throw new IllegalArgumentException("Cross product only supported for 3D vectors");
            double a1 = toDouble(a.get(0));
            double a2 = toDouble(a.get(1));
            double a3 = toDouble(a.get(2));
            double b1 = toDouble(b.get(0));
            double b2 = toDouble(b.get(1));
            double b3 = toDouble(b.get(2));
            
            org.ejml.simple.SimpleMatrix res = new org.ejml.simple.SimpleMatrix(3, 1);
            res.set(0, 0, a2 * b3 - a3 * b2);
            res.set(1, 0, a3 * b1 - a1 * b3);
            res.set(2, 0, a1 * b2 - a2 * b1);
            return fromEjmlVector(res);
        }

        @Override public Vector<E> projection(Vector<E> a, Vector<E> b) {
            double dotAB = toDouble(dot(a, b));
            double dotBB = toDouble(dot(b, b));
            if (dotBB == 0) return multiply(b, fromDouble(0.0));
            return multiply(b, fromDouble(dotAB / dotBB));
        }

        @Override
        public Vector<E> solveTriangular(Matrix<E> A, Vector<E> b, boolean upper, boolean transpose, boolean conjugate, boolean unit) {
            org.ejml.simple.SimpleMatrix ma = toEjmlMatrix(A);
            org.ejml.simple.SimpleMatrix mb = toEjmlVector(b);
            
            // EJML doesn't have a direct TRSM in SimpleMatrix for vectors easily exposed with unit diag
            // We use the parent's logic if it's generic enough, or implement it here.
            // Actually, EJML's solve() is efficient enough for small systems.
            // But for compliance, we should respect the triangular nature.
            
            // Forward/backward substitution is simple:
            int n = A.rows();
            double[] x = new double[n];
            double[] bData = new double[n];
            for (int i = 0; i < n; i++) bData[i] = toDouble(b.get(i));

            if (upper) {
                if (transpose) {
                    for (int i = 0; i < n; i++) {
                        double sum = 0;
                        for (int j = 0; j < i; j++) sum += ma.get(j, i) * x[j];
                        double val = bData[i] - sum;
                        if (!unit) val /= ma.get(i, i);
                        x[i] = val;
                    }
                } else {
                    for (int i = n - 1; i >= 0; i--) {
                        double sum = 0;
                        for (int j = i + 1; j < n; j++) sum += ma.get(i, j) * x[j];
                        double val = bData[i] - sum;
                        if (!unit) val /= ma.get(i, i);
                        x[i] = val;
                    }
                }
            } else {
                if (transpose) {
                    for (int i = n - 1; i >= 0; i--) {
                        double sum = 0;
                        for (int j = i + 1; j < n; j++) sum += ma.get(j, i) * x[j];
                        double val = bData[i] - sum;
                        if (!unit) val /= ma.get(i, i);
                        x[i] = val;
                    }
                } else {
                    for (int i = 0; i < n; i++) {
                        double sum = 0;
                        for (int j = 0; j < i; j++) sum += ma.get(i, j) * x[j];
                        double val = bData[i] - sum;
                        if (!unit) val /= ma.get(i, i);
                        x[i] = val;
                    }
                }
            }
            
            org.ejml.simple.SimpleMatrix res = new org.ejml.simple.SimpleMatrix(n, 1);
            for (int i = 0; i < n; i++) res.set(i, 0, x[i]);
            return fromEjmlVector(res);
        }
    }
}
