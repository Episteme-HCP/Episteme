/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.nio.DoubleBuffer;
import java.util.Optional;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import com.google.auto.service.AutoService;
import org.episteme.core.technical.algorithm.AlgorithmProvider;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector;

import org.episteme.core.technical.algorithm.AutoTuningManager;
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.nativ.technical.backend.nativ.NativeLibraryLoader;

/**
 * Standalone Native Linear Algebra backend using bundled episteme_native library.
 * <p>
 * Provides a subset of BLAS operations without requiring external high-performance libraries.
 * For optimized performance using external BLAS/LAPACK, use {@link NativeFFMBLASBackend}.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@AutoService({ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, AlgorithmProvider.class})
public class NativeCPULinearAlgebraBackend implements CPUBackend, NativeBackend, LinearAlgebraProvider<Real> {

    private static final MethodHandle DGEMM_HANDLE;
    // private static final MethodHandle DGEMV_HANDLE;
    // private static final MethodHandle DDOT_HANDLE;
    // private static final MethodHandle DNRM2_HANDLE;
    // private static final MethodHandle DAXPY_HANDLE;
    // private static final MethodHandle DSCAL_HANDLE;
    
    // LAPACK (via LAPACKE interface)
    private static final MethodHandle DGESV_HANDLE;
    private static final MethodHandle DGETRF_HANDLE;
    private static final MethodHandle DGETRI_HANDLE;
    
    private static final boolean AVAILABLE;

    public static final int CblasRowMajor = 101;
    public static final int CblasColMajor = 102;
    public static final int CblasNoTrans = 111;
    public static final int CblasTrans = 112;
    public static final int CblasConjTrans = 113;

    static {
        MethodHandle dgemm = null;
        MethodHandle dgesv = null;
        MethodHandle dgetrf = null;
        MethodHandle dgetri = null;
        
        boolean avail = false;

        try {
            Linker linker = NativeLibraryLoader.getLinker();
            Optional<SymbolLookup> lookupOpt = NativeLibraryLoader.loadLibrary("episteme-jni", java.lang.foreign.Arena.global());
            if (lookupOpt.isEmpty() || lookupOpt.get().find("cblas_dgemm").isEmpty()) {
                lookupOpt = NativeLibraryLoader.loadLibrary("openblas", java.lang.foreign.Arena.global());
                if (lookupOpt.isEmpty() || lookupOpt.get().find("cblas_dgemm").isEmpty()) {
                    lookupOpt = NativeLibraryLoader.loadLibrary("mkl_rt", java.lang.foreign.Arena.global());
                }
            }
            
            if (lookupOpt.isPresent()) {
                SymbolLookup lookup = lookupOpt.get();
                if (lookup.find("cblas_dgemm").isPresent()) {
                    MemorySegment symbol = lookup.find("cblas_dgemm").get();
                    dgemm = linker.downcallHandle(symbol, FunctionDescriptor.ofVoid(
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
                        ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE,
                        ValueLayout.ADDRESS, ValueLayout.JAVA_INT
                    ));
                    
                    /*
                    MemorySegment mvSymbol = lookup.find("cblas_dgemv").orElse(null);
                    if (mvSymbol != null) {
                        dgemv = linker.downcallHandle(mvSymbol, FunctionDescriptor.ofVoid(
                            ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                            ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS,
                            ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
                            ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS, ValueLayout.JAVA_INT
                        ));
                    }
                    
                    // Level 1
                    ddot = lookup.find("cblas_ddot").map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
                    dnrm2 = lookup.find("cblas_dnrm2").map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
                    daxpy = lookup.find("cblas_daxpy").map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
                    dscal = lookup.find("cblas_dscal").map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);

                    // LAPACKE
                    dgesv = lookup.find("LAPACKE_dgesv").map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
                    */
                    // LAPACKE
                    dgesv = lookup.find("LAPACKE_dgesv").map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
                    dgetrf = lookup.find("LAPACKE_dgetrf").map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);
                    dgetri = lookup.find("LAPACKE_dgetri").map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);

                    avail = true;
                }
            }
        } catch (Throwable t) {
            // Silently mark unavailable
        }
        
        DGEMM_HANDLE = dgemm;
        DGESV_HANDLE = dgesv;
        DGETRF_HANDLE = dgetrf;
        DGETRI_HANDLE = dgetri;
        
        AVAILABLE = avail;
    }

    @Override
    public boolean isLoaded() {
        return AVAILABLE;
    }

    @Override
    public String getNativeLibraryName() {
        return "episteme-jni";
    }

    @Override
    public boolean isAvailable() {
        return AVAILABLE;
    }

    @Override
    public String getEnvironmentInfo() {
        return AVAILABLE ? "CPU (Native/Panama)" : "N/A";
    }

    @Override
    public String getName() {
        return "Native CPU/BLAS Linear Algebra Backend";
    }

    @Override
    public double score(OperationContext context) {
        if (!AVAILABLE) return -1.0;
        return AutoTuningManager.getDynamicScore(getName(), context.getDimensionality(), getPriority());
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return new org.episteme.core.technical.backend.ExecutionContext() {
            @Override
            public <T> T execute(org.episteme.core.technical.backend.Operation<T> operation) {
                return operation.compute(this);
            }

            @Override
            public void close() {
                // No-op
            }
        };
    }

    // --- Low-level BLAS/LAPACK methods ---

    public void dgemm(int m, int n, int k,
                     DoubleBuffer A, int lda,
                     DoubleBuffer B, int ldb,
                     DoubleBuffer C, int ldc,
                     double alpha, double beta) {
        if (!AVAILABLE) throw new UnsupportedOperationException("BLAS native library not found");

        try {
            DGEMM_HANDLE.invokeExact(CblasRowMajor, CblasNoTrans, CblasNoTrans, m, n, k,
                alpha, MemorySegment.ofBuffer(A), lda, MemorySegment.ofBuffer(B), ldb, beta, MemorySegment.ofBuffer(C), ldc);
        } catch (Throwable t) {
            throw new RuntimeException("CBLAS call failed", t);
        }
    }

    public int dgetrf(int m, int n, DoubleBuffer A, int lda, java.nio.IntBuffer ipiv) {
        if (!AVAILABLE || DGETRF_HANDLE == null) throw new UnsupportedOperationException("LAPACK dgetrf not available");
        try {
            return (int) DGETRF_HANDLE.invokeExact(CblasRowMajor, m, n, MemorySegment.ofBuffer(A), lda, MemorySegment.ofBuffer(ipiv));
        } catch (Throwable t) {
            throw new RuntimeException("LAPACK dgetrf failed", t);
        }
    }

    public int dgetri(int n, DoubleBuffer A, int lda, java.nio.IntBuffer ipiv) {
        if (!AVAILABLE || DGETRI_HANDLE == null) throw new UnsupportedOperationException("LAPACK dgetri not available");
        try {
            return (int) DGETRI_HANDLE.invokeExact(CblasRowMajor, n, MemorySegment.ofBuffer(A), lda, MemorySegment.ofBuffer(ipiv));
        } catch (Throwable t) {
            throw new RuntimeException("LAPACK dgetri failed", t);
        }
    }

    public int dgesv(int n, int nrhs, DoubleBuffer A, int lda, java.nio.IntBuffer ipiv, DoubleBuffer B, int ldb) {
        if (!AVAILABLE || DGESV_HANDLE == null) throw new UnsupportedOperationException("LAPACK dgesv not available");
        try {
            return (int) DGESV_HANDLE.invokeExact(CblasRowMajor, n, nrhs, MemorySegment.ofBuffer(A), lda, MemorySegment.ofBuffer(ipiv), MemorySegment.ofBuffer(B), ldb);
        } catch (Throwable t) {
            throw new RuntimeException("LAPACK dgesv failed", t);
        }
    }

    // --- LinearAlgebraProvider Implementation (Merged logic) ---


    @Override
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        if (AVAILABLE && a instanceof RealDoubleMatrix && b instanceof RealDoubleMatrix) {
            RealDoubleMatrix adm = (RealDoubleMatrix) a;
            RealDoubleMatrix bdm = (RealDoubleMatrix) b;
            
            if (adm.cols() != bdm.rows()) {
                throw new IllegalArgumentException("Matrix dimension mismatch: " + adm.cols() + " != " + bdm.rows());
            }

            int m = adm.rows();
            int k = adm.cols();
            int n = bdm.cols();
            
            RealDoubleMatrix cdm = RealDoubleMatrix.direct(m, n);
            DoubleBuffer aBuf = ensureDirect(adm);
            DoubleBuffer bBuf = ensureDirect(bdm);
            
            dgemm(m, n, k, aBuf, k, bBuf, n, cdm.getBuffer(), n, 1.0, 0.0);
            return cdm;
        }
        return LinearAlgebraProvider.super.multiply(a, b);
    }

    @Override
    public Matrix<Real> inverse(Matrix<Real> a) {
        if (AVAILABLE && a instanceof RealDoubleMatrix && a.rows() == a.cols()) {
            int n = a.rows();
            RealDoubleMatrix res = RealDoubleMatrix.direct(n, n);
            RealDoubleMatrix src = (RealDoubleMatrix) a;
            
            res.getBuffer().put(src.toDoubleArray());
            res.getBuffer().position(0);
            
            java.nio.IntBuffer ipiv = java.nio.ByteBuffer.allocateDirect(n * 4)
                .order(java.nio.ByteOrder.nativeOrder()).asIntBuffer();
            
            int info = dgetrf(n, n, res.getBuffer(), n, ipiv);
            if (info < 0) throw new IllegalArgumentException("Illegal argument to dgetrf: " + info);
            if (info > 0) throw new ArithmeticException("Matrix is singular");
            
            info = dgetri(n, res.getBuffer(), n, ipiv);
            if (info < 0) throw new IllegalArgumentException("Illegal argument to dgetri: " + info);
            if (info > 0) throw new ArithmeticException("Matrix is singular");
            
            return res;
        }
        return LinearAlgebraProvider.super.inverse(a);
    }

    private DoubleBuffer ensureDirect(RealDoubleMatrix m) {
        if (m.getBuffer().isDirect()) return m.getBuffer();
        DoubleBuffer direct = java.nio.ByteBuffer.allocateDirect(m.rows() * m.cols() * 8)
            .order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();
        direct.put(m.toDoubleArray());
        direct.flip();
        return direct;
    }

    // Other methods default to UnsupportedOperationException
    @Override
    public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) {
        if (AVAILABLE && a instanceof RealDoubleMatrix && a.rows() == a.cols() && b.dimension() == a.rows()) {
            int n = a.rows();
            RealDoubleMatrix adm = (RealDoubleMatrix) a;
            
            // Result vector initialized with b
            RealDoubleMatrix x = RealDoubleMatrix.direct(n, 1);
            for(int i=0; i<n; i++) x.set(i, 0, b.get(i));
            
            // Intermediate matrix for decomposition (A will be overwritten by DGESV)
            RealDoubleMatrix aDecomp = RealDoubleMatrix.direct(n, n);
            aDecomp.getBuffer().put(adm.toDoubleArray());
            aDecomp.getBuffer().position(0);
            
            java.nio.IntBuffer ipiv = java.nio.ByteBuffer.allocateDirect(n * 4)
                .order(java.nio.ByteOrder.nativeOrder()).asIntBuffer();
            
            int info = dgesv(n, 1, aDecomp.getBuffer(), n, ipiv, x.getBuffer(), 1);
            if (info < 0) throw new IllegalArgumentException("Illegal argument to dgesv: " + info);
            if (info > 0) throw new ArithmeticException("Matrix is singular");
            
            // Extract vector from result matrix
            double[] result = new double[n];
            x.getBuffer().position(0);
            x.getBuffer().get(result);
            return RealDoubleVector.of(result);
        }
        return LinearAlgebraProvider.super.solve(a, b);
    }

    @Override
    public Vector<Real> add(Vector<Real> a, Vector<Real> b) {
        if (AVAILABLE && a instanceof RealDoubleVector && b instanceof RealDoubleVector) {
            double[] ad = ((RealDoubleVector) a).toDoubleArray();
            double[] bd = ((RealDoubleVector) b).toDoubleArray();
            double[] rd = new double[ad.length];
            for (int i = 0; i < ad.length; i++) rd[i] = ad[i] + bd[i];
            return RealDoubleVector.of(rd);
        }
        return LinearAlgebraProvider.super.add(a, b);
    }

    @Override
    public Vector<Real> subtract(Vector<Real> a, Vector<Real> b) {
        if (AVAILABLE && a instanceof RealDoubleVector && b instanceof RealDoubleVector) {
            double[] ad = ((RealDoubleVector) a).toDoubleArray();
            double[] bd = ((RealDoubleVector) b).toDoubleArray();
            double[] rd = new double[ad.length];
            for (int i = 0; i < ad.length; i++) rd[i] = ad[i] - bd[i];
            return RealDoubleVector.of(rd);
        }
        return LinearAlgebraProvider.super.subtract(a, b);
    }

    @Override
    public Matrix<Real> add(Matrix<Real> a, Matrix<Real> b) {
        if (AVAILABLE && a instanceof RealDoubleMatrix && b instanceof RealDoubleMatrix) {
            int rows = a.rows();
            int cols = a.cols();
            double[] ad = ((RealDoubleMatrix) a).toDoubleArray();
            double[] bd = ((RealDoubleMatrix) b).toDoubleArray();
            double[] rd = new double[ad.length];
            for (int i = 0; i < ad.length; i++) rd[i] = ad[i] + bd[i];
            return RealDoubleMatrix.of(rd, rows, cols);
        }
        return LinearAlgebraProvider.super.add(a, b);
    }

    @Override
    public Matrix<Real> subtract(Matrix<Real> a, Matrix<Real> b) {
        if (AVAILABLE && a instanceof RealDoubleMatrix && b instanceof RealDoubleMatrix) {
            int rows = a.rows();
            int cols = a.cols();
            double[] ad = ((RealDoubleMatrix) a).toDoubleArray();
            double[] bd = ((RealDoubleMatrix) b).toDoubleArray();
            double[] rd = new double[ad.length];
            for (int i = 0; i < ad.length; i++) rd[i] = ad[i] - bd[i];
            return RealDoubleMatrix.of(rd, rows, cols);
        }
        return LinearAlgebraProvider.super.subtract(a, b);
    }

    @Override
    public Matrix<Real> scale(Real scalar, Matrix<Real> a) {
        if (AVAILABLE && a instanceof RealDoubleMatrix) {
            int rows = a.rows();
            int cols = a.cols();
            double s = scalar.doubleValue();
            double[] ad = ((RealDoubleMatrix) a).toDoubleArray();
            double[] rd = new double[ad.length];
            for (int i = 0; i < ad.length; i++) rd[i] = ad[i] * s;
            return RealDoubleMatrix.of(rd, rows, cols);
        }
        return LinearAlgebraProvider.super.scale(scalar, a);
    }

    @Override
    public Matrix<Real> transpose(Matrix<Real> a) {
        if (AVAILABLE && a instanceof RealDoubleMatrix) {
            int rows = a.rows();
            int cols = a.cols();
            double[] ad = ((RealDoubleMatrix) a).toDoubleArray();
            double[] rd = new double[ad.length];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    rd[j * rows + i] = ad[i * cols + j];
                }
            }
            return RealDoubleMatrix.of(rd, cols, rows);
        }
        return LinearAlgebraProvider.super.transpose(a);
    }

    @Override
    public Real determinant(Matrix<Real> a) {
        if (AVAILABLE && a instanceof RealDoubleMatrix && a.rows() == a.cols()) {
            int n = a.rows();
            RealDoubleMatrix copy = RealDoubleMatrix.direct(n, n);
            copy.getBuffer().put(((RealDoubleMatrix) a).toDoubleArray());
            copy.getBuffer().position(0);

            java.nio.IntBuffer ipiv = java.nio.ByteBuffer.allocateDirect(n * 4)
                .order(java.nio.ByteOrder.nativeOrder()).asIntBuffer();
            
            int info = dgetrf(n, n, copy.getBuffer(), n, ipiv);
            if (info < 0) throw new IllegalArgumentException("Illegal argument to dgetrf: " + info);
            if (info > 0) return Real.ZERO; // Singular matrix

            double det = 1.0;
            int swaps = 0;
            for (int i = 0; i < n; i++) {
                det *= copy.get(i, i).doubleValue();
                if (ipiv.get(i) != (i + 1)) {
                    swaps++;
                }
            }
            if (swaps % 2 != 0) det = -det;
            return Real.of(det);
        }
        return LinearAlgebraProvider.super.determinant(a);
    }

    // Other methods default to UnsupportedOperationException
}
