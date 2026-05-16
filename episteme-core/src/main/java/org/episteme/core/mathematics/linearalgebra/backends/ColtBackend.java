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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage;
import org.episteme.core.mathematics.linearalgebra.vectors.GenericVector;
import org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.structures.rings.Field;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.BackendDiscovery;
import org.episteme.core.technical.backend.ExecutionContext;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.core.technical.backend.cpu.CPUExecutionContext;
import com.google.auto.service.AutoService;
import java.lang.reflect.Constructor;

/**
 * CPU compute backend for the Colt high-performance scientific computing library.
 * <p>
 * Colt provides open-source libraries for high-performance scientific and
 * technical computing. This backend wraps the Colt linear algebra provider
 * and integrates it into the Episteme backend discovery system.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService({Backend.class, CPUBackend.class, LinearAlgebraProvider.class})
public class ColtBackend<E> implements CPUBackend, LinearAlgebraProvider<E> {

    private static final Logger logger = LoggerFactory.getLogger(ColtBackend.class);

    private static boolean coltAvailable = false;
    private final Field<E> field;
    private LinearAlgebraProvider<E> coltImpl;

    static {
        try {
            Class.forName("cern.colt.matrix.DoubleMatrix2D");
            coltAvailable = true;
        } catch (Throwable t) {
            coltAvailable = false;
        }
    }

    public ColtBackend() {
        this(null);
    }

    @SuppressWarnings("unchecked")
    public ColtBackend(Field<E> field) {
        Field<E> defaultField = (Field<E>) org.episteme.core.mathematics.sets.Reals.getInstance();
        this.field = (field != null) ? field : defaultField;
        
        if (coltAvailable && canUseColt()) {
            try {
                String innerName = ColtBackend.class.getName() + "$ColtImpl";
                Class<?> clazz = Class.forName(innerName);
                Constructor<?> ctor = clazz.getDeclaredConstructor(ColtBackend.class, Field.class);
                ctor.setAccessible(true);
                LinearAlgebraProvider<E> impl = (LinearAlgebraProvider<E>) ctor.newInstance(this, this.field);
                this.coltImpl = impl;
            } catch (Throwable t) {
                logger.error("Failed to initialize Colt implementation: {}", t.getMessage(), t);
                this.coltImpl = null;
            }
        }
    }

    @Override
    public String getType() {
        return BackendDiscovery.TYPE_LINEAR_ALGEBRA;
    }

    @Override
    public String getId() {
        return "colt";
    }

    @Override
    public String getName() {
        return coltAvailable ? "Colt (Optimized)" : "Colt";
    }

    @Override
    public String getDescription() {
        return "Open Source Libraries for High Performance Scientific and Technical Computing.";
    }

    @Override
    public boolean isAvailable() {
        return coltAvailable && !org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision() && canUseColt();
    }

    @Override
    public int getPriority() {
        return coltAvailable ? 70 : 0;
    }

    @Override
    public double score(org.episteme.core.technical.algorithm.OperationContext context) {
        if (!coltAvailable || !canUseColt() || org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision()) return -1.0;
        if (context.hasHint(org.episteme.core.technical.algorithm.OperationContext.Hint.COMPLEX)) return -1.0;
        return getPriority();
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
        // Nothing to release for Colt
    }

    private boolean canUseColt() {
        if (field == null) return false;
        Object zero = field.zero();
        return (field instanceof org.episteme.core.mathematics.sets.Reals ||
                zero instanceof Real ||
                zero instanceof Double ||
                zero instanceof Float ||
                zero instanceof Integer);
    }

    @Override
    public boolean isCompatible(Ring<?> ring) {
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

    @Override public Vector<E> add(Vector<E> a, Vector<E> b) { checkColt(); return coltImpl.add(a, b); }
    @Override public Vector<E> subtract(Vector<E> a, Vector<E> b) { checkColt(); return coltImpl.subtract(a, b); }
    @Override public Vector<E> multiply(Vector<E> vector, E scalar) { checkColt(); return coltImpl.multiply(vector, scalar); }
    @Override public E dot(Vector<E> a, Vector<E> b) { checkColt(); return coltImpl.dot(a, b); }
    @Override public Matrix<E> add(Matrix<E> a, Matrix<E> b) { checkColt(); return coltImpl.add(a, b); }
    @Override public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) { checkColt(); return coltImpl.subtract(a, b); }
    @Override public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) { checkColt(); return coltImpl.multiply(a, b); }
    @Override public Vector<E> multiply(Matrix<E> a, Vector<E> b) { checkColt(); return coltImpl.multiply(a, b); }
    @Override public Matrix<E> inverse(Matrix<E> a) { checkColt(); return coltImpl.inverse(a); }
    @Override public E determinant(Matrix<E> a) { checkColt(); return coltImpl.determinant(a); }
    @Override public Vector<E> solve(Matrix<E> a, Vector<E> b) { checkColt(); return coltImpl.solve(a, b); }
    @Override public Matrix<E> transpose(Matrix<E> a) { checkColt(); return coltImpl.transpose(a); }
    @Override public Matrix<E> scale(E scalar, Matrix<E> a) { checkColt(); return coltImpl.scale(scalar, a); }
    @Override public E norm(Vector<E> a) { checkColt(); return coltImpl.norm(a); }
    @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<E> lu(Matrix<E> a) { checkColt(); return coltImpl.lu(a); }
    @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<E> eigen(Matrix<E> a) { checkColt(); return coltImpl.eigen(a); }
    @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<E> cholesky(Matrix<E> a) { checkColt(); return coltImpl.cholesky(a); }
    @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<E> qr(Matrix<E> a) { checkColt(); return coltImpl.qr(a); }
    @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<E> svd(Matrix<E> a) { checkColt(); return coltImpl.svd(a); }
    @Override public E trace(Matrix<E> a) { checkColt(); return coltImpl.trace(a); }
    @Override public Matrix<E> exp(Matrix<E> a) { checkColt(); return coltImpl.exp(a); }
    @Override public Matrix<E> log(Matrix<E> a) { checkColt(); return coltImpl.log(a); }
    @Override public Matrix<E> sin(Matrix<E> a) { checkColt(); return coltImpl.sin(a); }
    @Override public Matrix<E> cos(Matrix<E> a) { checkColt(); return coltImpl.cos(a); }
    @Override public Matrix<E> tan(Matrix<E> a) { checkColt(); return coltImpl.tan(a); }
    @Override public Matrix<E> asin(Matrix<E> a) { checkColt(); return coltImpl.asin(a); }
    @Override public Matrix<E> acos(Matrix<E> a) { checkColt(); return coltImpl.acos(a); }
    @Override public Matrix<E> atan(Matrix<E> a) { checkColt(); return coltImpl.atan(a); }
    @Override public Matrix<E> sinh(Matrix<E> a) { checkColt(); return coltImpl.sinh(a); }
    @Override public Matrix<E> cosh(Matrix<E> a) { checkColt(); return coltImpl.cosh(a); }
    @Override public Matrix<E> tanh(Matrix<E> a) { checkColt(); return coltImpl.tanh(a); }
    @Override public Matrix<E> asinh(Matrix<E> a) { checkColt(); return coltImpl.asinh(a); }
    @Override public Matrix<E> acosh(Matrix<E> a) { checkColt(); return coltImpl.acosh(a); }
    @Override public Matrix<E> atanh(Matrix<E> a) { checkColt(); return coltImpl.atanh(a); }
    @Override public Matrix<E> sqrt(Matrix<E> a) { checkColt(); return coltImpl.sqrt(a); }
    @Override public Matrix<E> cbrt(Matrix<E> a) { checkColt(); return coltImpl.cbrt(a); }
    @Override public Matrix<E> pow(Matrix<E> a, E exponent) { checkColt(); return coltImpl.pow(a, exponent); }
    @Override public Matrix<E> log10(Matrix<E> a) { checkColt(); return coltImpl.log10(a); }
    @Override public Vector<E> normalize(Vector<E> v) { checkColt(); return coltImpl.normalize(v); }
    @Override public Vector<E> cross(Vector<E> a, Vector<E> b) { checkColt(); return coltImpl.cross(a, b); }
    @Override public E angle(Vector<E> a, Vector<E> b) { checkColt(); return coltImpl.angle(a, b); }
    @Override public Vector<E> projection(Vector<E> a, Vector<E> b) { checkColt(); return coltImpl.projection(a, b); }

    private void checkColt() {
        if (coltImpl == null) {
            throw new UnsupportedOperationException("Colt implementation not available for this field or environment.");
        }
    }

    /**
     * Inner implementation class that touches Colt types.
     */
    @SuppressWarnings("unused")
    private static class ColtImpl<E> implements LinearAlgebraProvider<E> {
        private final ColtBackend<E> parent;
        private final Field<E> field;

        ColtImpl(ColtBackend<E> parent, Field<E> field) {
            this.parent = parent;
            this.field = field;
        }

        @Override public String getName() { return "Colt (Inner)"; }
        @Override public boolean isAvailable() { return true; }
        @Override public int getPriority() { return parent.getPriority(); }

        private cern.colt.matrix.DoubleMatrix2D toColtMatrix(Matrix<E> m) {
            cern.colt.matrix.impl.DenseDoubleMatrix2D colt = new cern.colt.matrix.impl.DenseDoubleMatrix2D(m.rows(), m.cols());
            for (int i = 0; i < m.rows(); i++) {
                for (int j = 0; j < m.cols(); j++) {
                    E val = m.get(i, j);
                    if (val instanceof Real) {
                        colt.set(i, j, ((Real) val).doubleValue());
                    } else if (val instanceof Number) {
                        colt.set(i, j, ((Number) val).doubleValue());
                    } else if (val != null) {
                        try {
                            colt.set(i, j, Double.parseDouble(val.toString()));
                        } catch (Exception e) {
                            throw new ClassCastException("ColtBackend cannot convert " + val.getClass().getName() + " to double: " + val);
                        }
                    }
                }
            }
            return colt;
        }

        private Matrix<E> fromColtMatrix(cern.colt.matrix.DoubleMatrix2D colt) {
            int rows = colt.rows();
            int cols = colt.columns();
            @SuppressWarnings("unchecked")
            E zero = (E) Real.ZERO;
            DenseMatrixStorage<E> storage = new DenseMatrixStorage<>(rows, cols, zero);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    @SuppressWarnings("unchecked")
                    E val = (E) Real.of(colt.get(i, j));
                    storage.set(i, j, val);
                }
            }
            return new GenericMatrix<>(storage, this, field);
        }

        private cern.colt.matrix.DoubleMatrix1D toColtVector(Vector<E> v) {
            cern.colt.matrix.impl.DenseDoubleMatrix1D colt = new cern.colt.matrix.impl.DenseDoubleMatrix1D(v.dimension());
            for (int i = 0; i < v.dimension(); i++) {
                E val = v.get(i);
                if (val instanceof Real) {
                    colt.set(i, ((Real) val).doubleValue());
                } else if (val instanceof Number) {
                    colt.set(i, ((Number) val).doubleValue());
                } else if (val != null) {
                    try {
                        colt.set(i, Double.parseDouble(val.toString()));
                    } catch (Exception e) {
                        throw new ClassCastException("ColtBackend cannot convert " + val.getClass().getName() + " to double: " + val);
                    }
                }
            }
            return colt;
        }

        private Vector<E> fromColtVector(cern.colt.matrix.DoubleMatrix1D colt) {
            int size = (int) colt.size();
            Class<?> componentType = field.zero().getClass();
            if (field.zero() instanceof Real) componentType = Real.class;
            else if (field.zero() instanceof org.episteme.core.mathematics.numbers.complex.Complex) componentType = org.episteme.core.mathematics.numbers.complex.Complex.class;
            
            @SuppressWarnings("unchecked")
            E[] data = (E[]) java.lang.reflect.Array.newInstance(componentType, size);
            for (int i = 0; i < size; i++) {
                @SuppressWarnings("unchecked")
                E val = (E) Real.of(colt.get(i));
                data[i] = val;
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
        @Override
        public E dot(Vector<E> a, Vector<E> b) {
            @SuppressWarnings("unchecked")
            E res = (E) Real.of(toColtVector(a).zDotProduct(toColtVector(b)));
            return res;
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
        @Override
        public E determinant(Matrix<E> a) {
            @SuppressWarnings("unchecked")
            E res = (E) Real.of(new cern.colt.matrix.linalg.Algebra().det(toColtMatrix(a)));
            return res;
        }
        @Override public Vector<E> solve(Matrix<E> a, Vector<E> b) {
            cern.colt.matrix.linalg.Algebra algebra = new cern.colt.matrix.linalg.Algebra();
            cern.colt.matrix.DoubleMatrix2D ma = toColtMatrix(a);
            cern.colt.matrix.DoubleMatrix2D bMatrix = new cern.colt.matrix.impl.DenseDoubleMatrix2D(b.dimension(), 1);
            for (int i = 0; i < b.dimension(); i++) bMatrix.set(i, 0, ((Real) b.get(i)).doubleValue());
            
            cern.colt.matrix.DoubleMatrix2D xMatrix;
            if (a.rows() == a.cols()) {
                xMatrix = algebra.solve(ma, bMatrix);
            } else {
                // QR solver in Colt handles M x N where M >= N
                if (a.rows() >= a.cols()) {
                    xMatrix = new cern.colt.matrix.linalg.QRDecomposition(ma).solve(bMatrix);
                } else {
                    // Underdetermined: Colt doesn't provide a direct SVD solver for M < N in this way.
                    // Fallback to Algebra.solve() which should attempt it or throw.
                    xMatrix = algebra.solve(ma, bMatrix);
                }
            }
            
            int n = a.cols();
            cern.colt.matrix.impl.DenseDoubleMatrix1D result = new cern.colt.matrix.impl.DenseDoubleMatrix1D(n);
            for (int i = 0; i < n; i++) result.set(i, xMatrix.get(i, 0));
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
        @Override
        public E norm(Vector<E> a) {
            @SuppressWarnings("unchecked")
            E res = (E) org.episteme.core.mathematics.numbers.real.Real.of(Math.sqrt(toColtVector(a).zDotProduct(toColtVector(a))));
            return res;
        }

        @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<E> lu(Matrix<E> a) {
            cern.colt.matrix.DoubleMatrix2D cmat = toColtMatrix(a);
            cern.colt.matrix.linalg.LUDecomposition lu = new cern.colt.matrix.linalg.LUDecomposition(cmat);
            int[] pivot = lu.getPivot();
            if (pivot == null) throw new ArithmeticException("Colt LU: Matrix is singular or decomposition failed");
            cern.colt.matrix.DoubleMatrix1D pv = new cern.colt.matrix.impl.DenseDoubleMatrix1D(pivot.length);
            for (int i = 0; i < pivot.length; i++) pv.set(i, pivot[i]);
            cern.colt.matrix.DoubleMatrix2D l = lu.getL();
            cern.colt.matrix.DoubleMatrix2D u = lu.getU();
            if (l == null || u == null) throw new ArithmeticException("Colt LU: Failed to extract factors");
            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<>(
                fromColtMatrix(l),
                fromColtMatrix(u),
                fromColtVector(pv)
            );
        }

        @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<E> eigen(Matrix<E> a) {
            cern.colt.matrix.linalg.EigenvalueDecomposition eigen = new cern.colt.matrix.linalg.EigenvalueDecomposition(toColtMatrix(a));
            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<>(
                fromColtMatrix(eigen.getV()),
                fromColtVector(eigen.getRealEigenvalues())
            );
        }

        @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<E> cholesky(Matrix<E> a) {
            cern.colt.matrix.linalg.CholeskyDecomposition cholesky = new cern.colt.matrix.linalg.CholeskyDecomposition(toColtMatrix(a));
            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<>(
                fromColtMatrix(cholesky.getL())
            );
        }

        @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<E> qr(Matrix<E> a) {
            cern.colt.matrix.DoubleMatrix2D cm = toColtMatrix(a);
            cern.colt.matrix.linalg.QRDecomposition qr = new cern.colt.matrix.linalg.QRDecomposition(cm);
            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<>(
                fromColtMatrix(qr.getQ()),
                fromColtMatrix(qr.getR())
            );
        }

        @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<E> svd(Matrix<E> a) {
            cern.colt.matrix.DoubleMatrix2D cm = toColtMatrix(a);
            cern.colt.matrix.linalg.SingularValueDecomposition svd = new cern.colt.matrix.linalg.SingularValueDecomposition(cm);
            double[] sValues = svd.getSingularValues();
            Vector<E> vecS = fromColtVector(new cern.colt.matrix.impl.DenseDoubleMatrix1D(sValues));
            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<>(
                fromColtMatrix(svd.getU()),
                vecS,
                fromColtMatrix(svd.getV())
            );
        }

        @Override
        public E trace(Matrix<E> a) {
            @SuppressWarnings("unchecked")
            E res = (E) Real.of(new cern.colt.matrix.linalg.Algebra().trace(toColtMatrix(a)));
            return res;
        }
        @Override public Matrix<E> exp(Matrix<E> a) { cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a); ca.assign(cern.jet.math.Functions.exp); return fromColtMatrix(ca); }
        @Override public Matrix<E> log(Matrix<E> a) { cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a); ca.assign(cern.jet.math.Functions.log); return fromColtMatrix(ca); }
        @Override public Matrix<E> sin(Matrix<E> a) { cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a); ca.assign(cern.jet.math.Functions.sin); return fromColtMatrix(ca); }
        @Override public Matrix<E> cos(Matrix<E> a) { cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a); ca.assign(cern.jet.math.Functions.cos); return fromColtMatrix(ca); }
        @Override public Matrix<E> tan(Matrix<E> a) { cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a); ca.assign(cern.jet.math.Functions.tan); return fromColtMatrix(ca); }
        @Override public Matrix<E> asin(Matrix<E> a) { cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a); ca.assign(cern.jet.math.Functions.asin); return fromColtMatrix(ca); }
        @Override public Matrix<E> acos(Matrix<E> a) { cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a); ca.assign(cern.jet.math.Functions.acos); return fromColtMatrix(ca); }
        @Override public Matrix<E> atan(Matrix<E> a) { cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a); ca.assign(cern.jet.math.Functions.atan); return fromColtMatrix(ca); }
        @Override public Matrix<E> sinh(Matrix<E> a) { cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a); ca.assign(Math::sinh); return fromColtMatrix(ca); }
        @Override public Matrix<E> cosh(Matrix<E> a) { cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a); ca.assign(Math::cosh); return fromColtMatrix(ca); }
        @Override public Matrix<E> tanh(Matrix<E> a) { cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a); ca.assign(Math::tanh); return fromColtMatrix(ca); }
        @Override public Matrix<E> asinh(Matrix<E> a) { cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a); ca.assign(x -> Math.log(x + Math.sqrt(x * x + 1.0))); return fromColtMatrix(ca); }
        @Override public Matrix<E> acosh(Matrix<E> a) { cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a); ca.assign(x -> Math.log(x + Math.sqrt(x * x - 1.0))); return fromColtMatrix(ca); }
        @Override public Matrix<E> atanh(Matrix<E> a) { cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a); ca.assign(x -> 0.5 * Math.log((1.0 + x) / (1.0 - x))); return fromColtMatrix(ca); }
        @Override public Matrix<E> sqrt(Matrix<E> a) { cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a); ca.assign(cern.jet.math.Functions.sqrt); return fromColtMatrix(ca); }
        @Override public Matrix<E> cbrt(Matrix<E> a) { cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a); ca.assign(Math::cbrt); return fromColtMatrix(ca); }
        @Override public Matrix<E> pow(Matrix<E> a, E exponent) {
            cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a);
            double p = ((Real) exponent).doubleValue();
            ca.assign(cern.jet.math.Functions.pow(p));
            return fromColtMatrix(ca);
        }
        @Override public Matrix<E> log10(Matrix<E> a) { cern.colt.matrix.DoubleMatrix2D ca = toColtMatrix(a); ca.assign(Math::log10); return fromColtMatrix(ca); }

        @Override public Vector<E> normalize(Vector<E> v) {
            E n = norm(v);
            if (((Real) n).doubleValue() == 0) return v;
            @SuppressWarnings("unchecked")
            E invNorm = (E) Real.of(1.0 / ((Real) n).doubleValue());
            return multiply(v, invNorm);
        }

        @Override public Vector<E> cross(Vector<E> a, Vector<E> b) {
            if (a.dimension() != 3 || b.dimension() != 3) throw new IllegalArgumentException("Cross product only supported for 3D vectors");
            double a1 = ((Real) a.get(0)).doubleValue();
            double a2 = ((Real) a.get(1)).doubleValue();
            double a3 = ((Real) a.get(2)).doubleValue();
            double b1 = ((Real) b.get(0)).doubleValue();
            double b2 = ((Real) b.get(1)).doubleValue();
            double b3 = ((Real) b.get(2)).doubleValue();
            return fromColtVector(new cern.colt.matrix.impl.DenseDoubleMatrix1D(new double[]{
                a2 * b3 - a3 * b2,
                a3 * b1 - a1 * b3,
                a1 * b2 - a2 * b1
            }));
        }

        @Override public E angle(Vector<E> a, Vector<E> b) {
            double dot = ((Real) dot(a, b)).doubleValue();
            double nA = ((Real) norm(a)).doubleValue();
            double nB = ((Real) norm(b)).doubleValue();
            if (nA == 0 || nB == 0) {
                @SuppressWarnings("unchecked")
                E zero = (E) Real.ZERO;
                return zero;
            }
            @SuppressWarnings("unchecked")
            E res = (E) Real.of(Math.acos(dot / (nA * nB)));
            return res;
        }

        @Override public Vector<E> projection(Vector<E> a, Vector<E> b) {
            double dotAB = ((Real) dot(a, b)).doubleValue();
            double dotBB = ((Real) dot(b, b)).doubleValue();
            if (dotBB == 0) {
                @SuppressWarnings("unchecked")
                E zero = (E) Real.ZERO;
                return multiply(b, zero);
            }
            @SuppressWarnings("unchecked")
            E scale = (E) Real.of(dotAB / dotBB);
            return multiply(b, scale);
        }
    }
}
