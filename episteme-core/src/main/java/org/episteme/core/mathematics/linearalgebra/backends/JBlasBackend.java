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

    private static final Logger logger = LoggerFactory.getLogger(JBlasBackend.class);

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
                Constructor<?> ctor = clazz.getDeclaredConstructor(JBlasBackend.class, Field.class);
                ctor.setAccessible(true);
                this.jblasImpl = (LinearAlgebraProvider<E>) ctor.newInstance(this, this.field);
            } catch (Throwable t) {
                logger.error("Failed to initialize JBlas implementation: {}", t.getMessage(), t);
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
        return jblasAvailable && !org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision() && canUseJBlas();
    }

    @Override
    public int getPriority() {
        return jblasAvailable ? 90 : 0;
    }

    @Override
    public double score(org.episteme.core.technical.algorithm.OperationContext context) {
        if (!jblasAvailable || !canUseJBlas() || org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision()) return -1.0;
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

    private boolean canUseJBlas() {
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

    @Override
    public void shutdown() {
        // No explicit native resources to release in the wrapper.
        // JBlas handles its own native library lifecycle.
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
    @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<E> lu(Matrix<E> a) { checkJBlas(); return jblasImpl.lu(a); }
    @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<E> eigen(Matrix<E> a) { checkJBlas(); return jblasImpl.eigen(a); }
    @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<E> qr(Matrix<E> a) { checkJBlas(); return jblasImpl.qr(a); }
    @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<E> svd(Matrix<E> a) { checkJBlas(); return jblasImpl.svd(a); }
    @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<E> cholesky(Matrix<E> a) { checkJBlas(); return jblasImpl.cholesky(a); }
    @Override public E trace(Matrix<E> a) { checkJBlas(); return jblasImpl.trace(a); }
    @Override public Matrix<E> exp(Matrix<E> a) { checkJBlas(); return jblasImpl.exp(a); }
    @Override public Matrix<E> log(Matrix<E> a) { checkJBlas(); return jblasImpl.log(a); }
    @Override public Matrix<E> sin(Matrix<E> a) { checkJBlas(); return jblasImpl.sin(a); }
    @Override public Matrix<E> cos(Matrix<E> a) { checkJBlas(); return jblasImpl.cos(a); }
    @Override public Matrix<E> tan(Matrix<E> a) { checkJBlas(); return jblasImpl.tan(a); }
    @Override public Matrix<E> asin(Matrix<E> a) { checkJBlas(); return jblasImpl.asin(a); }
    @Override public Matrix<E> acos(Matrix<E> a) { checkJBlas(); return jblasImpl.acos(a); }
    @Override public Matrix<E> atan(Matrix<E> a) { checkJBlas(); return jblasImpl.atan(a); }
    @Override public Matrix<E> sinh(Matrix<E> a) { checkJBlas(); return jblasImpl.sinh(a); }
    @Override public Matrix<E> cosh(Matrix<E> a) { checkJBlas(); return jblasImpl.cosh(a); }
    @Override public Matrix<E> tanh(Matrix<E> a) { checkJBlas(); return jblasImpl.tanh(a); }
    @Override public Matrix<E> asinh(Matrix<E> a) { checkJBlas(); return jblasImpl.asinh(a); }
    @Override public Matrix<E> acosh(Matrix<E> a) { checkJBlas(); return jblasImpl.acosh(a); }
    @Override public Matrix<E> atanh(Matrix<E> a) { checkJBlas(); return jblasImpl.atanh(a); }
    @Override public Matrix<E> sqrt(Matrix<E> a) { checkJBlas(); return jblasImpl.sqrt(a); }
    @Override public Matrix<E> cbrt(Matrix<E> a) { checkJBlas(); return jblasImpl.cbrt(a); }
    @Override public Matrix<E> log10(Matrix<E> a) { checkJBlas(); return jblasImpl.log10(a); }
    @Override public Matrix<E> pow(Matrix<E> a, E exponent) { checkJBlas(); return jblasImpl.pow(a, exponent); }
    @Override public Vector<E> normalize(Vector<E> v) { checkJBlas(); return jblasImpl.normalize(v); }
    @Override public Vector<E> cross(Vector<E> a, Vector<E> b) { checkJBlas(); return jblasImpl.cross(a, b); }
    @Override public Vector<E> projection(Vector<E> a, Vector<E> b) { checkJBlas(); return jblasImpl.projection(a, b); }
    @Override public Vector<E> solveTriangular(Matrix<E> A, Vector<E> b, boolean upper, boolean transpose, boolean conjugate, boolean unit) { checkJBlas(); return jblasImpl.solveTriangular(A, b, upper, transpose, conjugate, unit); }
    @Override public E angle(Vector<E> a, Vector<E> b) { checkJBlas(); return jblasImpl.angle(a, b); }

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
        private final JBlasBackend<E> parent;
        private final Field<E> field;

        JBlasImpl(JBlasBackend<E> parent, Field<E> field) {
            this.parent = parent;
            this.field = field;
        }

        @Override public String getName() { return "JBlas (Inner)"; }
        @Override public boolean isAvailable() { return true; }
        @Override public int getPriority() { return parent.getPriority(); }

        private org.jblas.DoubleMatrix toJBlasMatrix(Matrix<E> m) {
            int rows = m.rows();
            int cols = m.cols();
            org.jblas.DoubleMatrix jm = new org.jblas.DoubleMatrix(rows, cols);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    E val = m.get(i, j);
                    if (val instanceof Real) {
                        jm.put(i, j, ((Real) val).doubleValue());
                    } else if (val instanceof Number) {
                        jm.put(i, j, ((Number) val).doubleValue());
                    } else if (val != null) {
                        try {
                            jm.put(i, j, Double.parseDouble(val.toString()));
                        } catch (Exception e) {
                            throw new ClassCastException("JBlasBackend cannot convert " + val.getClass().getName() + " to double: " + val);
                        }
                    }
                }
            }
            return jm;
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

        private Matrix<E> fromJBlasMatrix(org.jblas.DoubleMatrix jm) {
            int rows = jm.rows;
            int cols = jm.columns;
            DenseMatrixStorage<E> storage = new DenseMatrixStorage<>(rows, cols, field.zero());
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    storage.set(i, j, fromDouble(jm.get(i, j)));
                }
            }
            return new GenericMatrix<>(storage, this, field);
        }

        private org.jblas.DoubleMatrix toJBlasVector(Vector<E> v) {
            double[] data = new double[v.dimension()];
            for (int i = 0; i < v.dimension(); i++) {
                E val = v.get(i);
                if (val instanceof Real) {
                    data[i] = ((Real) val).doubleValue();
                } else if (val instanceof Number) {
                    data[i] = ((Number) val).doubleValue();
                } else if (val != null) {
                    try {
                        data[i] = Double.parseDouble(val.toString());
                    } catch (Exception e) {
                        throw new ClassCastException("JBlasBackend cannot convert " + val.getClass().getName() + " to double: " + val);
                    }
                }
            }
            return new org.jblas.DoubleMatrix(data);
        }

        @SuppressWarnings("unchecked")
        private Vector<E> fromJBlasVector(org.jblas.DoubleMatrix jv) {
            int size = jv.length;
            E[] data = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), size);
            for (int i = 0; i < size; i++)
                data[i] = fromDouble(jv.get(i));
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
        @Override public Matrix<E> inverse(Matrix<E> a) {
            org.jblas.DoubleMatrix ja = toJBlasMatrix(a);
            if (ja.rows == ja.columns) {
                return fromJBlasMatrix(org.jblas.Solve.solve(ja, org.jblas.DoubleMatrix.eye(ja.rows)));
            }
            return fromJBlasMatrix(org.jblas.Solve.pinv(ja));
        }
        @Override @SuppressWarnings("unchecked")
        public E determinant(Matrix<E> a) {
            org.jblas.DoubleMatrix javaA = toJBlasMatrix(a);
            org.jblas.Decompose.LUDecomposition<org.jblas.DoubleMatrix> lu = org.jblas.Decompose.lu(javaA);
            double det = 1.0;
            for (int i = 0; i < lu.u.rows; i++) det *= lu.u.get(i, i);
            
            // Account for permutation sign (parity)
            // JBlas returns p such that a = p * l * u
            // det(a) = det(p) * det(l) * det(u) = det(p) * 1 * det(u)
            // Determinant of a permutation matrix is +1 or -1 based on parity.
            int n = lu.p.rows;
            int[] perm = new int[n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (lu.p.get(i, j) > 0.5) {
                        perm[i] = j;
                        break;
                    }
                }
            }
            
            int swaps = 0;
            boolean[] visited = new boolean[n];
            for (int i = 0; i < n; i++) {
                if (!visited[i]) {
                    int j = i;
                    int cycleSize = 0;
                    while (!visited[j]) {
                        visited[j] = true;
                        j = perm[j];
                        cycleSize++;
                    }
                    if (cycleSize > 1) {
                        swaps += (cycleSize - 1);
                    }
                }
            }
            
            if (swaps % 2 != 0) det = -det;
            return (E) Real.of(det);
        }
        @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<E> lu(Matrix<E> a) {
            org.jblas.Decompose.LUDecomposition<org.jblas.DoubleMatrix> luResult = org.jblas.Decompose.lu(toJBlasMatrix(a));
            // JBlas LU returns L, U, P such that A = P * L * U
            // ComplianceTest expects PA = LU (where P applied to A swaps rows)
            // If A = PLU, then P^T A = LU. So our result p should represent P^T.
            // p[i] = j if (P^T)[i,j] = 1, i.e., P[j,i] = 1.
            int n = luResult.p.rows;
            double[] pData = new double[n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (luResult.p.get(j, i) > 0.5) {
                        pData[i] = j;
                        break;
                    }
                }
            }

            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<>(
                fromJBlasMatrix(luResult.l), 
                fromJBlasMatrix(luResult.u), 
                fromJBlasVector(new org.jblas.DoubleMatrix(pData))
            );
        }
        @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<E> eigen(Matrix<E> a) {
            // eigenvectors() returns [V, D] where D is a diagonal matrix of eigenvalues
            org.jblas.ComplexDoubleMatrix[] eigenResult = org.jblas.Eigen.eigenvectors(toJBlasMatrix(a)); 
            org.jblas.DoubleMatrix V = eigenResult[0].getReal();
            org.jblas.DoubleMatrix D = eigenResult[1].getReal();
            
            // Extract diagonal of D as a vector
            double[] eigenvalues = new double[D.rows];
            for (int i = 0; i < D.rows; i++) {
                eigenvalues[i] = D.get(i, i);
            }
            
            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<>(
                fromJBlasMatrix(V),
                fromJBlasVector(new org.jblas.DoubleMatrix(eigenvalues))
            );
        }
        @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<E> qr(Matrix<E> a) {
            org.jblas.Decompose.QRDecomposition<org.jblas.DoubleMatrix> qr = org.jblas.Decompose.qr(toJBlasMatrix(a));
            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<>(
                fromJBlasMatrix(qr.q), fromJBlasMatrix(qr.r)
            );
        }
        @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<E> svd(Matrix<E> a) {
            org.jblas.DoubleMatrix[] svd = org.jblas.Singular.fullSVD(toJBlasMatrix(a));
            // JBlas returns [U, S (diagonal), V]. 
            // Compliance test expects V such that A = U * S * V.transpose().
            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<>(
                fromJBlasMatrix(svd[0]), fromJBlasVector(svd[1]), fromJBlasMatrix(svd[2])
            );
        }
        @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<E> cholesky(Matrix<E> a) {
        org.jblas.DoubleMatrix res = org.jblas.Decompose.cholesky(toJBlasMatrix(a));
        // JBlas returns U such that A = U' U. Episteme expects L such that A = L L'.
        // L = U'.
        org.jblas.DoubleMatrix l = res.transpose();
        return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<>(fromJBlasMatrix(l));
    }
        @Override public Vector<E> solve(Matrix<E> a, Vector<E> b) {
            org.jblas.DoubleMatrix ja = toJBlasMatrix(a);
            org.jblas.DoubleMatrix jb = toJBlasVector(b);
            if (ja.rows == ja.columns) {
                return fromJBlasVector(org.jblas.Solve.solve(ja, jb));
            } else {
                return fromJBlasVector(org.jblas.Solve.solveLeastSquares(ja, jb));
            }
        }
        @Override public Matrix<E> transpose(Matrix<E> a) { return fromJBlasMatrix(toJBlasMatrix(a).transpose()); }
        @Override public Matrix<E> scale(E s, Matrix<E> a) { return fromJBlasMatrix(toJBlasMatrix(a).mul(((Real) s).doubleValue())); }
        @Override @SuppressWarnings("unchecked")
        public E norm(Vector<E> a) { return (E) org.episteme.core.mathematics.numbers.real.Real.of(toJBlasVector(a).norm2()); }

        @Override @SuppressWarnings("unchecked")
        public E trace(Matrix<E> a) {
            org.jblas.DoubleMatrix jm = toJBlasMatrix(a);
            double sum = 0.0;
            for (int i = 0; i < Math.min(jm.rows, jm.columns); i++) sum += jm.get(i, i);
            return (E) org.episteme.core.mathematics.numbers.real.Real.of(sum);
        }

        @Override public Matrix<E> exp(Matrix<E> a) { return fromJBlasMatrix(org.jblas.MatrixFunctions.exp(toJBlasMatrix(a))); }
        @Override public Matrix<E> log(Matrix<E> a) { return fromJBlasMatrix(org.jblas.MatrixFunctions.log(toJBlasMatrix(a))); }
        @Override public Matrix<E> sin(Matrix<E> a) { return fromJBlasMatrix(org.jblas.MatrixFunctions.sin(toJBlasMatrix(a))); }
        @Override public Matrix<E> cos(Matrix<E> a) { return fromJBlasMatrix(org.jblas.MatrixFunctions.cos(toJBlasMatrix(a))); }
        @Override public Matrix<E> tan(Matrix<E> a) { return fromJBlasMatrix(org.jblas.MatrixFunctions.tan(toJBlasMatrix(a))); }
        @Override public Matrix<E> asin(Matrix<E> a) { return fromJBlasMatrix(org.jblas.MatrixFunctions.asin(toJBlasMatrix(a))); }
        @Override public Matrix<E> acos(Matrix<E> a) { return fromJBlasMatrix(org.jblas.MatrixFunctions.acos(toJBlasMatrix(a))); }
        @Override public Matrix<E> atan(Matrix<E> a) { return fromJBlasMatrix(org.jblas.MatrixFunctions.atan(toJBlasMatrix(a))); }
        @Override public Matrix<E> sqrt(Matrix<E> a) { return fromJBlasMatrix(org.jblas.MatrixFunctions.sqrt(toJBlasMatrix(a))); }
        @Override public Matrix<E> pow(Matrix<E> a, E exponent) {
            double p = ((Real) exponent).doubleValue();
            return fromJBlasMatrix(org.jblas.MatrixFunctions.pow(toJBlasMatrix(a), p));
        }

        @Override public Matrix<E> sinh(Matrix<E> a) { return fromJBlasMatrix(org.jblas.MatrixFunctions.sinh(toJBlasMatrix(a))); }
        @Override public Matrix<E> cosh(Matrix<E> a) { return fromJBlasMatrix(org.jblas.MatrixFunctions.cosh(toJBlasMatrix(a))); }
        @Override public Matrix<E> tanh(Matrix<E> a) { return fromJBlasMatrix(org.jblas.MatrixFunctions.tanh(toJBlasMatrix(a))); }
        @Override public Matrix<E> asinh(Matrix<E> a) {
            org.jblas.DoubleMatrix jm = toJBlasMatrix(a);
            org.jblas.DoubleMatrix res = new org.jblas.DoubleMatrix(jm.rows, jm.columns);
            for (int i = 0; i < jm.length; i++) {
                double x = jm.get(i);
                res.put(i, Math.log(x + Math.sqrt(x * x + 1.0)));
            }
            return fromJBlasMatrix(res);
        }
        @Override public Matrix<E> acosh(Matrix<E> a) {
            org.jblas.DoubleMatrix jm = toJBlasMatrix(a);
            org.jblas.DoubleMatrix res = new org.jblas.DoubleMatrix(jm.rows, jm.columns);
            for (int i = 0; i < jm.length; i++) {
                double x = jm.get(i);
                res.put(i, Math.log(x + Math.sqrt(x * x - 1.0)));
            }
            return fromJBlasMatrix(res);
        }
        @Override public Matrix<E> atanh(Matrix<E> a) {
            org.jblas.DoubleMatrix jm = toJBlasMatrix(a);
            org.jblas.DoubleMatrix res = new org.jblas.DoubleMatrix(jm.rows, jm.columns);
            for (int i = 0; i < jm.length; i++) {
                double x = jm.get(i);
                res.put(i, 0.5 * Math.log((1.0 + x) / (1.0 - x)));
            }
            return fromJBlasMatrix(res);
        }
        @Override public Matrix<E> cbrt(Matrix<E> a) { return fromJBlasMatrix(org.jblas.MatrixFunctions.cbrt(toJBlasMatrix(a))); }
        @Override public Matrix<E> log10(Matrix<E> a) { return fromJBlasMatrix(org.jblas.MatrixFunctions.log10(toJBlasMatrix(a))); }

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
            
            double[] res = new double[3];
            res[0] = a2 * b3 - a3 * b2;
            res[1] = a3 * b1 - a1 * b3;
            res[2] = a1 * b2 - a2 * b1;
            return fromJBlasVector(new org.jblas.DoubleMatrix(res));
        }

        @Override public Vector<E> projection(Vector<E> a, Vector<E> b) {
            double dotAB = toDouble(dot(a, b));
            double dotBB = toDouble(dot(b, b));
            if (dotBB == 0) return multiply(b, fromDouble(0.0));
            return multiply(b, fromDouble(dotAB / dotBB));
        }

        @Override public E angle(Vector<E> a, Vector<E> b) {
            double dot = toDouble(dot(a, b));
            double nA = toDouble(norm(a));
            double nB = toDouble(norm(b));
            if (nA == 0 || nB == 0) return ring().zero();
            double cosTheta = dot / (nA * nB);
            return fromDouble(Math.acos(Math.max(-1.0, Math.min(1.0, cosTheta))));
        }

        private org.episteme.core.mathematics.structures.rings.Ring<E> ring() { return field; }

        private double toDouble(E val) {
            if (val instanceof Real) return ((Real) val).doubleValue();
            if (val instanceof Number) return ((Number) val).doubleValue();
            return Double.parseDouble(val.toString());
        }

        @Override
        public Vector<E> solveTriangular(Matrix<E> A, Vector<E> b, boolean upper, boolean transpose, boolean conjugate, boolean unit) {
            org.jblas.DoubleMatrix ma = toJBlasMatrix(A);
            org.jblas.DoubleMatrix mb = toJBlasVector(b);
            int n = A.rows();
            double[] x = new double[n];
            double[] bData = mb.toArray();

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
            return fromJBlasVector(new org.jblas.DoubleMatrix(x));
        }
    }
}
