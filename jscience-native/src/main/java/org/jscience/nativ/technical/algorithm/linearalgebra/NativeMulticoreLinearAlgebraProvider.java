/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.technical.algorithm.linearalgebra;

import com.google.auto.service.AutoService;
import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.nativ.mathematics.linearalgebra.matrices.backends.NativeCPULinearAlgebraBackend;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.jscience.core.mathematics.linearalgebra.providers.CPUDenseLinearAlgebraProvider;

/**
 * Native multicore linear algebra provider using BLAS/LAPACK.
 * <p>
 * Delegates to native BLAS for {@link RealDoubleMatrix} operations and
 * falls back to {@link CPUDenseLinearAlgebraProvider} for generic operations.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@SuppressWarnings("rawtypes") // LinearAlgebraProvider.class is a raw type reference (unavoidable in @AutoService)
@AutoService({LinearAlgebraProvider.class, AlgorithmProvider.class})
public class NativeMulticoreLinearAlgebraProvider implements LinearAlgebraProvider<Real> {

    private final NativeCPULinearAlgebraBackend backend = new NativeCPULinearAlgebraBackend();
    private final CPUDenseLinearAlgebraProvider<Real> fallback = new CPUDenseLinearAlgebraProvider<>();

    public String getName() {
        return "Native JScience (OpenBLAS)";
    }

    @Override
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        if (a instanceof RealDoubleMatrix && b instanceof RealDoubleMatrix) {
            RealDoubleMatrix adm = (RealDoubleMatrix) a;
            RealDoubleMatrix bdm = (RealDoubleMatrix) b;
            
            // Validate dimensions
            if (adm.cols() != bdm.rows()) {
                throw new IllegalArgumentException("Matrix dimension mismatch: " + adm.cols() + " != " + bdm.rows());
            }

            int m = adm.rows(); // Rows of A
            int k = adm.cols(); // Cols of A / Rows of B
            int n = bdm.cols(); // Cols of B
            
            // Result C must be Direct for native interop (M x N)
            RealDoubleMatrix cdm = RealDoubleMatrix.direct(m, n);
            
            // Ensure A and B are accessible by native code (Direct Buffers)
            java.nio.DoubleBuffer aBuf = ensureDirect(adm);
            java.nio.DoubleBuffer bBuf = ensureDirect(bdm);
            
            // Backend dgemm expects: rowsA (M), colsB (N), colsA (K)
            // cblas_dgemm(CblasRowMajor, CblasNoTrans, CblasNoTrans, M, N, K, alpha, A, lda, B, ldb, beta, C, ldc)
            backend.dgemm(m, n, k, 
                aBuf, k,   // lda = k (RowMajor)
                bBuf, n,   // ldb = n (RowMajor)
                cdm.getBuffer(), n, // ldc = n
                1.0, 0.0);
            
            return cdm;
        }
        return fallback.multiply(a, b);
    }

    private java.nio.DoubleBuffer ensureDirect(RealDoubleMatrix m) {
        if (m.getBuffer().isDirect()) return m.getBuffer();
        java.nio.DoubleBuffer direct = java.nio.ByteBuffer.allocateDirect(m.rows() * m.cols() * 8)
            .order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();
        direct.put(m.toDoubleArray());
        direct.flip();
        return direct;
    }

    @Override
    public Vector<Real> add(Vector<Real> a, Vector<Real> b) { return fallback.add(a, b); }
    @Override
    public Vector<Real> subtract(Vector<Real> a, Vector<Real> b) { return fallback.subtract(a, b); }
    @Override
    public Vector<Real> multiply(Vector<Real> vector, Real scalar) { return fallback.multiply(vector, scalar); }
    @Override
    public Real dot(Vector<Real> a, Vector<Real> b) { return fallback.dot(a, b); }
    @Override
    public Real norm(Vector<Real> a) { return fallback.norm(a); }
    @Override
    public Matrix<Real> add(Matrix<Real> a, Matrix<Real> b) { return fallback.add(a, b); }
    @Override
    public Matrix<Real> subtract(Matrix<Real> a, Matrix<Real> b) { return fallback.subtract(a, b); }
    @Override
    public Vector<Real> multiply(Matrix<Real> a, Vector<Real> b) { return fallback.multiply(a, b); }
    @Override
    public Matrix<Real> inverse(Matrix<Real> a) {
        if (a instanceof RealDoubleMatrix && a.rows() == a.cols()) {
            int n = a.rows();
            
            // Create direct matrix for result to ensure efficient native interop
            RealDoubleMatrix res = RealDoubleMatrix.direct(n, n);
            RealDoubleMatrix src = (RealDoubleMatrix) a;
            
            // Copy data. We use toDoubleArray() which is reasonably fast (System.arraycopy for heap).
            // For critical path, zero-allocation copy would be better but this is O(N^2) vs O(N^3) compute.
            res.getBuffer().put(src.toDoubleArray());
            res.getBuffer().position(0);
            
            // Prepare pivot buffer
            java.nio.IntBuffer ipiv = java.nio.ByteBuffer.allocateDirect(n * 4)
                .order(java.nio.ByteOrder.nativeOrder()).asIntBuffer();
            
            // LU Factorization (dgetrf)
            int info = backend.dgetrf(n, n, res.getBuffer(), n, ipiv);
            if (info < 0) throw new IllegalArgumentException("Illegal argument to dgetrf: " + info);
            if (info > 0) throw new ArithmeticException("Matrix is singular");
            
            // Inverse (dgetri)
            info = backend.dgetri(n, res.getBuffer(), n, ipiv);
            if (info < 0) throw new IllegalArgumentException("Illegal argument to dgetri: " + info);
            if (info > 0) throw new ArithmeticException("Matrix is singular");
            
            return res;
        }
        return fallback.inverse(a);
    }
    @Override
    public Real determinant(Matrix<Real> a) { return fallback.determinant(a); }
    @Override
    public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) { return fallback.solve(a, b); }
    @Override
    public Matrix<Real> transpose(Matrix<Real> a) { return fallback.transpose(a); }
    @Override
    public Matrix<Real> scale(Real scalar, Matrix<Real> a) { return fallback.scale(scalar, a); }

    @Override
    public boolean isAvailable() {
        return backend != null && backend.isAvailable();
    }
}
