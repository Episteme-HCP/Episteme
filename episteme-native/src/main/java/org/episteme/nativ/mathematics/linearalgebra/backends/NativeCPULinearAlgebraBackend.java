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
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector;

import org.episteme.core.technical.algorithm.AutoTuningManager;
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.core.technical.backend.nativ.NativeLibraryLoader;

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
@com.google.auto.service.AutoService({org.episteme.core.technical.backend.cpu.CPUBackend.class, org.episteme.nativ.technical.backend.nativ.NativeBackend.class, org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider.class, org.episteme.core.technical.algorithm.AlgorithmProvider.class})
public class NativeCPULinearAlgebraBackend implements CPUBackend, NativeBackend, LinearAlgebraProvider<Real> {

    private static final System.Logger logger = System.getLogger(NativeCPULinearAlgebraBackend.class.getName());

    private static final MethodHandle DGEMM_HANDLE;
    private static final MethodHandle DGEMV_HANDLE;
    private static final MethodHandle DDOT_HANDLE;
    private static final MethodHandle DNRM2_HANDLE;
    private static final MethodHandle DAXPY_HANDLE;
    private static final MethodHandle DSCAL_HANDLE;
    
    // LAPACK (via LAPACKE interface)
    private static final MethodHandle DGESV_HANDLE;
    private static final MethodHandle DGETRF_HANDLE;
    private static final MethodHandle DGETRI_HANDLE;
    private static final MethodHandle DSYEV_HANDLE;
    private static final MethodHandle DPOTRF_HANDLE;
    private static final MethodHandle DGEQRF_HANDLE;
    private static final MethodHandle DORGQR_HANDLE;
    private static final MethodHandle DGESVD_HANDLE;
    private static final MethodHandle DGELS_HANDLE;
    
    private static final boolean AVAILABLE;

    public static final int CblasRowMajor = 101;
    public static final int CblasColMajor = 102;
    public static final int CblasNoTrans = 111;
    public static final int CblasTrans = 112;
    public static final int CblasConjTrans = 113;

    public static final int LAPACK_ROW_MAJOR = 101;
    public static final int LAPACK_COL_MAJOR = 102;

    static {
        MethodHandle dgemm = null;
        MethodHandle dgemv = null;
        MethodHandle ddot = null;
        MethodHandle dnrm2 = null;
        MethodHandle daxpy = null;
        MethodHandle dscal = null;
        
        MethodHandle dgesv = null;
        MethodHandle dgetrf = null;
        MethodHandle dgetri = null;
        MethodHandle dsyev = null;
        MethodHandle dpotrf = null;
        MethodHandle dgeqrf = null;
        MethodHandle dorgqr = null;
        MethodHandle dgesvd = null;
        MethodHandle dgels = null;
        
        boolean avail = false;

        try {
            Linker linker = NativeLibraryLoader.getLinker();
            java.util.List<SymbolLookup> lookups = new java.util.ArrayList<>();
            
            // Try common libraries
            String[] commonLibs = {"episteme-jni", "openblas", "lapacke", "lapack", "mkl_rt"};
            for (String lib : commonLibs) {
                NativeLibraryLoader.loadLibrary(lib, java.lang.foreign.Arena.global()).ifPresent(lookups::add);
            }
            
            // Add system lookup as fallback
            lookups.add(SymbolLookup.loaderLookup());

            SymbolLookup lookup = name -> {
                for (SymbolLookup l : lookups) {
                    Optional<MemorySegment> s = l.find(name);
                    if (s.isPresent()) return s;
                }
                return Optional.empty();
            };
            
            Optional<MemorySegment> dgemmTarget = lookup.find("cblas_dgemm");
            if (dgemmTarget.isPresent()) {
                dgemm = linker.downcallHandle(dgemmTarget.get(), FunctionDescriptor.ofVoid(
                    ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                    ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                    ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
                    ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE,
                    ValueLayout.ADDRESS, ValueLayout.JAVA_INT
                ));
                
                MemorySegment mvSymbol = lookup.find("cblas_dgemv").orElse(null);
                if (mvSymbol != null) {
                    dgemv = linker.downcallHandle(mvSymbol, FunctionDescriptor.ofVoid(
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS,
                        ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS, ValueLayout.JAVA_INT
                    ));
                }
                
                // Level 1 BLAS
                ddot = lookup.find("cblas_ddot").map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
                dnrm2 = lookup.find("cblas_dnrm2").map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
                daxpy = lookup.find("cblas_daxpy").map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
                dscal = lookup.find("cblas_dscal").map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);

                // LAPACKE (Standard C interface names)
                dgesv = lookup.find("LAPACKE_dgesv")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
                
                dgetrf = lookup.find("LAPACKE_dgetrf")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);
                
                dgetri = lookup.find("LAPACKE_dgetri")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);
                
                Optional<MemorySegment> s_dsyev = NativeLibraryLoader.findSymbol(lookup, "LAPACKE_dsyev", "lapacke_dsyev");
                if (s_dsyev.isPresent()) {
                    dsyev = linker.downcallHandle(s_dsyev.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                }

                dpotrf = lookup.find("LAPACKE_dpotrf")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);

                dgeqrf = lookup.find("LAPACKE_dgeqrf")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);

                dorgqr = lookup.find("LAPACKE_dorgqr")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);

                Optional<MemorySegment> s_dgesvd = NativeLibraryLoader.findSymbol(lookup, "LAPACKE_dgesvd", "lapacke_dgesvd");
                if (s_dgesvd.isPresent()) {
                    dgesvd = linker.downcallHandle(s_dgesvd.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                }

                dgels = lookup.find("LAPACKE_dgels")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);

                avail = (dgemm != null && dgemv != null);
            }
        } catch (Throwable t) {
            logger.log(System.Logger.Level.DEBUG, "Native CPU initialization failed: " + t.getMessage());
        }
        
        if (!avail) {
            logger.log(System.Logger.Level.DEBUG, "No valid native handles found.");
        }
        
        DGEMM_HANDLE = dgemm;
        DGEMV_HANDLE = dgemv;
        DDOT_HANDLE = ddot;
        DNRM2_HANDLE = dnrm2;
        DAXPY_HANDLE = daxpy;
        DSCAL_HANDLE = dscal;
        DGESV_HANDLE = dgesv;
        DGETRF_HANDLE = dgetrf;
        DGETRI_HANDLE = dgetri;
        DSYEV_HANDLE = dsyev;
        DPOTRF_HANDLE = dpotrf;
        DGEQRF_HANDLE = dgeqrf;
        DORGQR_HANDLE = dorgqr;
        DGESVD_HANDLE = dgesvd;
        DGELS_HANDLE = dgels;
        
        // A more robust check for core BLAS/LAPACK functionality
        AVAILABLE = avail && dgesv != null;

        if (AVAILABLE) {
            logger.log(System.Logger.Level.INFO, "Native CPU-BLAS Backend initialized. SVD support: {0}, Eigen support: {1}", 
                DGESVD_HANDLE != null, DSYEV_HANDLE != null);
        } else {
            logger.log(System.Logger.Level.WARNING, "Native CPU-BLAS Backend NOT fully initialized (missing core BLAS/LAPACK symbols)");
        }
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
    public String getStatusMessage() {
        if (AVAILABLE) return "Ready (Native CPU-BLAS)";
        return "Native library (episteme-jni, openblas, or mkl_rt) not found or CBLAS/LAPACK symbols missing";
    }

    @Override
    public String getEnvironmentInfo() {
        return AVAILABLE ? "CPU (Native/Panama)" : "N/A";
    }

    @Override
    public String getName() {
        return "Native CPU-BLAS Linear Algebra Backend";
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
            DGEMM_HANDLE.invoke(CblasRowMajor, CblasNoTrans, CblasNoTrans, m, n, k,
                alpha, MemorySegment.ofBuffer(A), lda, MemorySegment.ofBuffer(B), ldb, beta, MemorySegment.ofBuffer(C), ldc);
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException("CBLAS dgemm failed", t);
        }
    }

    public void dgemv(int m, int n, double alpha, DoubleBuffer A, int lda, DoubleBuffer x, int incx, double beta, DoubleBuffer y, int incy) {
        if (!AVAILABLE || DGEMV_HANDLE == null) throw new UnsupportedOperationException("CBLAS dgemv not available");
        try {
            DGEMV_HANDLE.invoke(CblasRowMajor, CblasNoTrans, m, n, alpha, MemorySegment.ofBuffer(A), lda, MemorySegment.ofBuffer(x), incx, beta, MemorySegment.ofBuffer(y), incy);
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException("CBLAS dgemv failed", t);
        }
    }

    public double ddot(int n, DoubleBuffer x, int incx, DoubleBuffer y, int incy) {
        if (!AVAILABLE || DDOT_HANDLE == null) throw new UnsupportedOperationException("CBLAS ddot not available");
        try {
            return (double) DDOT_HANDLE.invoke(n, MemorySegment.ofBuffer(x), incx, MemorySegment.ofBuffer(y), incy);
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException("CBLAS ddot failed", t);
        }
    }

    public double dnrm2(int n, DoubleBuffer x, int incx) {
        if (!AVAILABLE || DNRM2_HANDLE == null) throw new UnsupportedOperationException("CBLAS dnrm2 not available");
        try {
            return (double) DNRM2_HANDLE.invoke(n, MemorySegment.ofBuffer(x), incx);
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException("CBLAS dnrm2 failed", t);
        }
    }

    public void dscal(int n, double alpha, DoubleBuffer x, int incx) {
        if (!AVAILABLE || DSCAL_HANDLE == null) throw new UnsupportedOperationException("CBLAS dscal not available");
        try {
            DSCAL_HANDLE.invoke(n, alpha, MemorySegment.ofBuffer(x), incx);
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException("CBLAS dscal failed", t);
        }
    }

    public int dgetrf(int m, int n, DoubleBuffer A, int lda, java.nio.IntBuffer ipiv) {
        if (!AVAILABLE || DGETRF_HANDLE == null) throw new UnsupportedOperationException("LAPACK dgetrf not available");
        try {
            MemorySegment aSeg = MemorySegment.ofBuffer(A);
            MemorySegment ipivSeg = MemorySegment.ofBuffer(ipiv);
            return (int) DGETRF_HANDLE.invoke(LAPACK_ROW_MAJOR, m, n, aSeg, lda, ipivSeg);
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException("LAPACK dgetrf failed", t);
        }
    }

    public int dgetri(int n, DoubleBuffer A, int lda, java.nio.IntBuffer ipiv) {
        if (!AVAILABLE || DGETRI_HANDLE == null) throw new UnsupportedOperationException("LAPACK dgetri not available");
        try {
            return (int) DGETRI_HANDLE.invoke(LAPACK_ROW_MAJOR, n, MemorySegment.ofBuffer(A), lda, MemorySegment.ofBuffer(ipiv));
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException("LAPACK dgetri failed", t);
        }
    }

    public int dgesv(int n, int nrhs, DoubleBuffer A, int lda, java.nio.IntBuffer ipiv, DoubleBuffer B, int ldb) {
        if (!AVAILABLE || DGESV_HANDLE == null) throw new UnsupportedOperationException("LAPACK dgesv not available");
        try {
            return (int) DGESV_HANDLE.invoke(LAPACK_ROW_MAJOR, n, nrhs, MemorySegment.ofBuffer(A), lda, MemorySegment.ofBuffer(ipiv), MemorySegment.ofBuffer(B), ldb);
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException("LAPACK dgesv failed", t);
        }
    }

    public int dsyev(int n, DoubleBuffer A, int lda, DoubleBuffer W) {
        if (!AVAILABLE || DSYEV_HANDLE == null) throw new UnsupportedOperationException("LAPACK dsyev not available");
        try {
            MemorySegment aSeg = MemorySegment.ofBuffer(A);
            MemorySegment wSeg = MemorySegment.ofBuffer(W);
            return (int) DSYEV_HANDLE.invoke(LAPACK_ROW_MAJOR, (byte) 'V', (byte) 'U', n, aSeg, n, wSeg);
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException("LAPACK dsyev failed", t);
        }
    }

    public int dpotrf(int n, DoubleBuffer A, int lda) {
        if (!AVAILABLE || DPOTRF_HANDLE == null) throw new UnsupportedOperationException("LAPACK dpotrf not available");
        try {
            return (int) DPOTRF_HANDLE.invoke(LAPACK_ROW_MAJOR, (byte) 'L', n, MemorySegment.ofBuffer(A), lda);
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException("LAPACK dpotrf failed", t);
        }
    }

    public int dgeqrf(int m, int n, DoubleBuffer A, int lda, DoubleBuffer tau) {
        if (!AVAILABLE || DGEQRF_HANDLE == null) throw new UnsupportedOperationException("LAPACK dgeqrf not available");
        try {
            MemorySegment aSeg = MemorySegment.ofBuffer(A);
            MemorySegment tauSeg = MemorySegment.ofBuffer(tau);
            return (int) DGEQRF_HANDLE.invoke(LAPACK_ROW_MAJOR, m, n, aSeg, n, tauSeg);
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException("LAPACK dgeqrf failed", t);
        }
    }

    public int dorgqr(int m, int n, int k, DoubleBuffer A, int lda, DoubleBuffer tau) {
        if (!AVAILABLE || DORGQR_HANDLE == null) throw new UnsupportedOperationException("LAPACK dorgqr not available");
        try {
            return (int) DORGQR_HANDLE.invoke(LAPACK_ROW_MAJOR, m, n, k, MemorySegment.ofBuffer(A), lda, MemorySegment.ofBuffer(tau));
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException("LAPACK dorgqr failed", t);
        }
    }

    public int dgesvd(byte jobu, byte jobvt, int m, int n, DoubleBuffer A, int lda, DoubleBuffer S, DoubleBuffer U, int ldu, DoubleBuffer VT, int ldvt, DoubleBuffer superb) {
        if (!AVAILABLE || DGESVD_HANDLE == null) throw new UnsupportedOperationException("LAPACK dgesvd not available");
        try {
            return (int) DGESVD_HANDLE.invoke(LAPACK_ROW_MAJOR, jobu, jobvt, m, n, MemorySegment.ofBuffer(A), lda, MemorySegment.ofBuffer(S), MemorySegment.ofBuffer(U), ldu, MemorySegment.ofBuffer(VT), ldvt, MemorySegment.ofBuffer(superb));
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException("LAPACK dgesvd failed", t);
        }
    }

    public int dgels(char trans, int m, int n, int nrhs, DoubleBuffer A, int lda, DoubleBuffer B, int ldb) {
        if (!AVAILABLE || DGELS_HANDLE == null) throw new UnsupportedOperationException("LAPACK dgels not available");
        try {
            return (int) DGELS_HANDLE.invoke(LAPACK_ROW_MAJOR, (byte) trans, m, n, nrhs, MemorySegment.ofBuffer(A), lda, MemorySegment.ofBuffer(B), ldb);
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException("LAPACK dgels failed", t);
        }
    }

    // --- LinearAlgebraProvider Implementation (Merged logic) ---


    @Override
    public void shutdown() {
        // No-op for now. Memory segments are managed via ScopedArena in operations.
    }

    @Override
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        if (AVAILABLE) {
            RealDoubleMatrix adm = (a instanceof RealDoubleMatrix) ? (RealDoubleMatrix) a : RealDoubleMatrix.of(toDoubleArray(a), a.rows(), a.cols());
            RealDoubleMatrix bdm = (b instanceof RealDoubleMatrix) ? (RealDoubleMatrix) b : RealDoubleMatrix.of(toDoubleArray(b), b.rows(), b.cols());
            
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
        throw new UnsupportedOperationException(getName() + ": multiply() not available");
    }

    @Override
    public Matrix<Real> inverse(Matrix<Real> a) {
        if (!AVAILABLE) throw new UnsupportedOperationException(getName() + ": inverse() not available");
        int m = a.rows();
        int n = a.cols();
        if (m != n) {
             return pseudoInverse(a);
        }
        if (a instanceof RealDoubleMatrix && m == n) {
            n = a.rows();
            RealDoubleMatrix res = RealDoubleMatrix.direct(n, n);
            RealDoubleMatrix src = (RealDoubleMatrix) a;
            
            res.getBuffer().put(src.toDoubleArray());
            res.getBuffer().position(0);
            
            java.nio.IntBuffer ipiv = java.nio.ByteBuffer.allocateDirect(n * 4)
                .order(java.nio.ByteOrder.nativeOrder()).asIntBuffer();
            
            int info = dgetrf(n, n, res.getBuffer(), n, ipiv);
            if (info < 0) {
                throw new IllegalArgumentException("Illegal argument to dgetrf: " + info);
            }
            if (info > 0) {
                throw new ArithmeticException("Matrix is singular");
            }
            
            info = dgetri(n, res.getBuffer(), n, ipiv);
            if (info < 0) {
                throw new IllegalArgumentException("Illegal argument to dgetri: " + info);
            }
            if (info > 0) {
                throw new ArithmeticException("Matrix is singular");
            }
            
            return res;
        }
        throw new UnsupportedOperationException(getName() + ": inverse() not available for these matrix types");
    }

    private Matrix<Real> pseudoInverse(Matrix<Real> a) {
        org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<Real> svd = svd(a);
        int m = a.rows();
        int n = a.cols();
        int k = svd.S().dimension();
        
        RealDoubleMatrix sInv = RealDoubleMatrix.direct(n, m);
        for (int i = 0; i < k; i++) {
            double sVal = svd.S().get(i).doubleValue();
            if (sVal > 1e-12) {
                sInv.set(i, i, Real.of(1.0 / sVal));
            }
        }
        
        // Use local multiply and transpose to maintain autonomy
        Matrix<Real> vSInv = multiply(svd.V(), sInv);
        Matrix<Real> uT = transpose(svd.U());
        return multiply(vSInv, uT);
    }

    private DoubleBuffer ensureDirect(RealDoubleMatrix m) {
        if (m.getStorage() instanceof org.episteme.core.mathematics.linearalgebra.matrices.storage.DirectRealDoubleMatrixStorage) {
            return ((org.episteme.core.mathematics.linearalgebra.matrices.storage.DirectRealDoubleMatrixStorage) m.getStorage()).getBuffer().duplicate().position(0);
        }
        DoubleBuffer direct = java.nio.ByteBuffer.allocateDirect(m.rows() * m.cols() * 8)
            .order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();
        direct.put(m.toDoubleArray());
        direct.flip();
        return direct;
    }


    private DoubleBuffer ensureDirect(RealDoubleVector v) {
        if (v.getBuffer().isDirect()) return v.getBuffer().duplicate().position(0);
        DoubleBuffer direct = java.nio.ByteBuffer.allocateDirect(v.dimension() * 8)
            .order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();
        direct.put(v.toDoubleArray());
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
            if (info != 0) throw new ArithmeticException("dgesv failed: " + info);
            
            // Extract vector from result matrix
            double[] result = new double[n];
            x.getBuffer().position(0);
            x.getBuffer().get(result);
            return RealDoubleVector.of(result);
        } else if (AVAILABLE && a instanceof RealDoubleMatrix && b.dimension() == a.rows()) {
            // Rectangular solve (Least Squares)
            int m = a.rows();
            int n = a.cols();
            int maxDim = Math.max(m, n);
            
            RealDoubleMatrix x = RealDoubleMatrix.direct(maxDim, 1);
            for(int i=0; i<m; i++) x.set(i, 0, b.get(i));
            
            RealDoubleMatrix aCopy = RealDoubleMatrix.direct(m, n);
            aCopy.getBuffer().put(((RealDoubleMatrix) a).toDoubleArray());
            aCopy.getBuffer().position(0);
            
            int info = dgels('N', m, n, 1, aCopy.getBuffer(), n, x.getBuffer(), 1);
            if (info != 0) throw new RuntimeException("dgels failed: " + info);
            
            double[] result = new double[n];
            x.getBuffer().position(0);
            x.getBuffer().get(result);
            return RealDoubleVector.of(result);
        }
        throw new UnsupportedOperationException(getName() + ": solve() not available for these matrix types");
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
        throw new UnsupportedOperationException(getName() + ": Vector add() not available for these types");
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
        throw new UnsupportedOperationException(getName() + ": Vector subtract() not available for these types");
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
        throw new UnsupportedOperationException(getName() + ": Matrix add() not available for these types");
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
        throw new UnsupportedOperationException(getName() + ": Matrix subtract() not available for these types");
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
        throw new UnsupportedOperationException(getName() + ": scale() not available for these types");
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
        throw new UnsupportedOperationException(getName() + ": transpose() not available for these types");
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
        throw new UnsupportedOperationException(getName() + ": determinant() not available for these types");
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<Real> lu(Matrix<Real> a) {
        if (AVAILABLE && a instanceof RealDoubleMatrix && a.rows() == a.cols()) {
            int n = a.rows();
            RealDoubleMatrix luMat = RealDoubleMatrix.direct(n, n);
            luMat.getBuffer().put(((RealDoubleMatrix) a).toDoubleArray());
            luMat.getBuffer().position(0);

            java.nio.IntBuffer ipiv = java.nio.ByteBuffer.allocateDirect(n * 4)
                .order(java.nio.ByteOrder.nativeOrder()).asIntBuffer();

            int info = dgetrf(n, n, luMat.getBuffer(), n, ipiv);
            if (info < 0) throw new IllegalArgumentException("Illegal argument to dgetrf: " + info);

            double[] lData = new double[n * n];
            double[] uData = new double[n * n];
            double[] luArr = luMat.toDoubleArray();

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    double val = luArr[i * n + j];
                    if (i > j) {
                        lData[i * n + j] = val;
                        uData[i * n + j] = 0.0;
                    } else if (i == j) {
                        lData[i * n + j] = 1.0;
                        uData[i * n + j] = val;
                    } else {
                        lData[i * n + j] = 0.0;
                        uData[i * n + j] = val;
                    }
                }
            }

            double[] pData = new double[n];
            if (n > 0) {
                for (int i = 0; i < n; i++) pData[i] = i;
                for (int i = 0; i < n; i++) {
                    int ip = ipiv.get(i) - 1;
                    if (ip != i) {
                        double tmp = pData[i];
                        pData[i] = pData[ip];
                        pData[ip] = tmp;
                    }
                }
            }

            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<>(
                RealDoubleMatrix.of(lData, n, n),
                RealDoubleMatrix.of(uData, n, n),
                RealDoubleVector.of(pData)
            );
        }
        throw new UnsupportedOperationException(getName() + ": lu() not available for these types");
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<Real> eigen(Matrix<Real> a) {
        org.episteme.core.mathematics.linearalgebra.matrices.storage.RealDoubleMatrixStorage storage = extractStorage(a);
        if (AVAILABLE && storage != null && a.rows() == a.cols()) {
            int n = a.rows();
            
            DoubleBuffer aBuf = java.nio.ByteBuffer.allocateDirect(n * n * 8)
                .order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();
            aBuf.put(storage.toDoubleArray());
            aBuf.flip();

            DoubleBuffer wBuf = java.nio.ByteBuffer.allocateDirect(n * 8)
                .order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();

            double[] initialA = storage.toDoubleArray();
            logger.log(System.Logger.Level.INFO, "dsyev: n={0}, first 5 elements of A: {1}", 
                n, java.util.Arrays.toString(java.util.Arrays.copyOf(initialA, Math.min(5, initialA.length))));

            int info = dsyev(n, aBuf, n, wBuf);
            if (info != 0) {
                logger.log(System.Logger.Level.WARNING, "dsyev failed with info: {0}", info);
            }
            if (info < 0) throw new IllegalArgumentException("Illegal argument to dsyev: " + info);
            if (info > 0) throw new ArithmeticException("Eigenvalue decomposition failed to converge");

            double[] wData = new double[n];
            wBuf.position(0);
            wBuf.get(wData);

            double[] evData = new double[n * n];
            aBuf.position(0);
            aBuf.get(evData);

            // Based on residue check: resNoT=0, T=21.5. Eigenvectors are in COLUMNS (no transpose needed for JScience).
            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<>(
                RealDoubleMatrix.of(evData, n, n),
                RealDoubleVector.of(wData)
            );
        }
        throw new UnsupportedOperationException(getName() + ": eigen() not available for these types");
    }

    private org.episteme.core.mathematics.linearalgebra.matrices.storage.RealDoubleMatrixStorage extractStorage(Matrix<Real> a) {
        if (a instanceof RealDoubleMatrix) {
            return (org.episteme.core.mathematics.linearalgebra.matrices.storage.RealDoubleMatrixStorage) ((RealDoubleMatrix) a).getStorage();
        } else if (a instanceof org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix) {
            org.episteme.core.mathematics.linearalgebra.matrices.storage.MatrixStorage<Real> storage = ((org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<Real>) a).getStorage();
            if (storage instanceof org.episteme.core.mathematics.linearalgebra.matrices.storage.RealDoubleMatrixStorage) {
                return (org.episteme.core.mathematics.linearalgebra.matrices.storage.RealDoubleMatrixStorage) storage;
            } else {
                logger.log(System.Logger.Level.INFO, "extractStorage: GenericMatrix had incompatible storage type: {0}", storage != null ? storage.getClass().getName() : "null");
            }
        } else {
            logger.log(System.Logger.Level.INFO, "extractStorage: Matrix was not RealDoubleMatrix or GenericMatrix: {0}", a.getClass().getName());
        }
        return null;
    }

    private double[][] to2DArray(Matrix<Real> m) {
        double[][] d = new double[m.rows()][m.cols()];
        for (int i = 0; i < m.rows(); i++) {
            for (int j = 0; j < m.cols(); j++) d[i][j] = m.get(i, j).doubleValue();
        }
        return d;
    }

    private double[] toDoubleArray(Matrix<Real> m) {
        double[] d = new double[m.rows() * m.cols()];
        for (int i = 0; i < m.rows(); i++) {
            for (int j = 0; j < m.cols(); j++) d[i * m.cols() + j] = m.get(i, j).doubleValue();
        }
        return d;
    }

    private double[] toDoubleArray(Vector<Real> v) {
        double[] d = new double[v.dimension()];
        for (int i = 0; i < v.dimension(); i++) d[i] = v.get(i).doubleValue();
        return d;
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<Real> cholesky(Matrix<Real> a) {
        if (AVAILABLE && a instanceof RealDoubleMatrix && a.rows() == a.cols()) {
            int n = a.rows();
            RealDoubleMatrix lMat = RealDoubleMatrix.direct(n, n);
            lMat.getBuffer().put(((RealDoubleMatrix) a).toDoubleArray());
            lMat.getBuffer().position(0);

            int info = dpotrf(n, lMat.getBuffer(), n);
            if (info < 0) throw new IllegalArgumentException("Illegal argument to dpotrf: " + info);
            if (info > 0) throw new ArithmeticException("Matrix is not positive definite (info=" + info + ")");

            // Zero out upper part
            double[] data = lMat.toDoubleArray();
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    data[i * n + j] = 0.0;
                }
            }
            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<>(
                RealDoubleMatrix.of(data, n, n)
            );
        }
        throw new UnsupportedOperationException(getName() + ": cholesky() not available for these types");
    }
    @Override
    public Real dot(Vector<Real> a, Vector<Real> b) {
        if (AVAILABLE && DDOT_HANDLE != null && a instanceof RealDoubleVector && b instanceof RealDoubleVector) {
            RealDoubleVector av = (RealDoubleVector) a;
            RealDoubleVector bv = (RealDoubleVector) b;
            if (av.dimension() != bv.dimension()) throw new IllegalArgumentException("Dimension mismatch");
            try {
                // Ensure direct buffers for addressable segments
                DoubleBuffer ab = ensureDirect(av);
                DoubleBuffer bb = ensureDirect(bv);
                return Real.of(ddot(av.dimension(), ab, 1, bb, 1));
            } catch (Throwable t) {
                // Fallback to Java if native fails
                System.err.println("Note: Native ddot failed, using Java fallback: " + t.getMessage());
            }
        }
        if (AVAILABLE && a instanceof RealDoubleVector && b instanceof RealDoubleVector) {
            double[] ad = ((RealDoubleVector) a).toDoubleArray();
            double[] bd = ((RealDoubleVector) b).toDoubleArray();
            if (ad.length != bd.length) throw new IllegalArgumentException("Dimension mismatch");
            double sum = 0.0;
            for (int i = 0; i < ad.length; i++) sum += ad[i] * bd[i];
            return Real.of(sum);
        }
        throw new UnsupportedOperationException(getName() + ": dot() not available for these types");
    }

    @Override
    public Real norm(Vector<Real> a) {
        if (AVAILABLE && DNRM2_HANDLE != null && a instanceof RealDoubleVector) {
            RealDoubleVector av = (RealDoubleVector) a;
            try {
                DoubleBuffer ab = ensureDirect(av);
                return Real.of(dnrm2(av.dimension(), ab, 1));
            } catch (Throwable t) {
                System.err.println("Note: Native dnrm2 failed, using Java fallback: " + t.getMessage());
            }
        }
        if (AVAILABLE && a instanceof RealDoubleVector) {
            double[] ad = ((RealDoubleVector) a).toDoubleArray();
            double sumSq = 0.0;
            for (double d : ad) sumSq += d * d;
            return Real.of(Math.sqrt(sumSq));
        }
        throw new UnsupportedOperationException(getName() + ": norm() not available for these types");
    }

    @Override
    public Vector<Real> multiply(Matrix<Real> a, Vector<Real> b) {
        if (AVAILABLE && DGEMV_HANDLE != null && a instanceof RealDoubleMatrix && b instanceof RealDoubleVector) {
            RealDoubleMatrix adm = (RealDoubleMatrix) a;
            RealDoubleVector bdv = (RealDoubleVector) b;
            if (adm.cols() != bdv.dimension()) throw new IllegalArgumentException("Dimension mismatch");
            
            DoubleBuffer aBuf = ensureDirect(adm);
            DoubleBuffer bBuf = ensureDirect(bdv);
            RealDoubleVector res = RealDoubleVector.direct(adm.rows());
            
            dgemv(adm.rows(), adm.cols(), 1.0, aBuf, adm.cols(), bBuf, 1, 0.0, res.getBuffer(), 1);
            return res;
        }
        if (AVAILABLE) {
            RealDoubleMatrix adm = (a instanceof RealDoubleMatrix) ? (RealDoubleMatrix) a : RealDoubleMatrix.of(toDoubleArray(a), a.rows(), a.cols());
            RealDoubleVector bdv = (b instanceof RealDoubleVector) ? (RealDoubleVector) b : RealDoubleVector.of(toDoubleArray(b));
            
            if (adm.cols() != bdv.dimension()) throw new IllegalArgumentException("Dimension mismatch");
            
            double[] aData = adm.toDoubleArray();
            double[] bData = bdv.toDoubleArray();
            double[] result = new double[adm.rows()];
            for (int i = 0; i < adm.rows(); i++) {
                double sum = 0;
                for (int j = 0; j < adm.cols(); j++) {
                    sum += aData[i * adm.cols() + j] * bData[j];
                }
                result[i] = sum;
            }
            return RealDoubleVector.of(result);
        }
        throw new UnsupportedOperationException(getName() + ": Matrix-Vector multiply() not available");
    }

    @Override
    public Vector<Real> multiply(Vector<Real> vector, Real scalar) {
        if (AVAILABLE && vector instanceof RealDoubleVector) {
            RealDoubleVector v = (RealDoubleVector) vector;
            RealDoubleVector res = RealDoubleVector.direct(v.dimension());
            res.getBuffer().put(v.toDoubleArray());
            res.getBuffer().flip();
            dscal(v.dimension(), scalar.doubleValue(), res.getBuffer(), 1);
            return res;
        }
        throw new UnsupportedOperationException(getName() + ": Vector multiply(scalar) not available for these types");
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<Real> qr(Matrix<Real> a) {
        if (AVAILABLE && a instanceof RealDoubleMatrix) {
            int m = a.rows();
            int n = a.cols();
            int k = Math.min(m, n);

            RealDoubleMatrix A = (RealDoubleMatrix) a;
            RealDoubleMatrix qMat = RealDoubleMatrix.direct(m, n); // Used temporarily to hold A
            qMat.getBuffer().put(A.toDoubleArray());
            qMat.getBuffer().position(0);

            DoubleBuffer tau = java.nio.ByteBuffer.allocateDirect(k * 8)
                .order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();

            // 1. DGEQRF
            int info = dgeqrf(m, n, qMat.getBuffer(), n, tau);
            if (info != 0) throw new RuntimeException("dgeqrf failed with info: " + info);

            // 2. Extract R
            double[] rData = new double[k * n];
            double[] aFactored = qMat.toDoubleArray();
            for (int i = 0; i < k; i++) {
                for (int j = i; j < n; j++) {
                    rData[i * n + j] = aFactored[i * n + j];
                }
            }
            Matrix<Real> R = RealDoubleMatrix.of(rData, k, n);

            // 3. DORGQR (Economy Q: m x k)
            info = dorgqr(m, k, k, qMat.getBuffer(), n, tau);
            if (info != 0) throw new RuntimeException("dorgqr failed with info: " + info);

            double[] qDataFull = qMat.toDoubleArray();
            double[] qDataEconomy = new double[m * k];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < k; j++) {
                    qDataEconomy[i * k + j] = qDataFull[i * n + j];
                }
            }
            Matrix<Real> Q = RealDoubleMatrix.of(qDataEconomy, m, k);

            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<>(Q, R);
        }
        throw new UnsupportedOperationException(getName() + ": qr() not available for these types");
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<Real> svd(Matrix<Real> a) {
        logger.log(System.Logger.Level.INFO, "Entering svd() with matrix: {0}x{1}, type: {2}", a.rows(), a.cols(), a.getClass().getName());
        org.episteme.core.mathematics.linearalgebra.matrices.storage.RealDoubleMatrixStorage storage = extractStorage(a);
        
        if (AVAILABLE && storage != null) {
            int m = a.rows();
            int n = a.cols();
            if (DGESVD_HANDLE == null) {
                logger.log(System.Logger.Level.INFO, "DGESVD_HANDLE is null in svd()");
                throw new UnsupportedOperationException("LAPACKE dgesvd not available");
            }
            if (m < n) {
                logger.log(System.Logger.Level.INFO, "m < n ({0} < {1}) not supported yet in native SVD", m, n);
                throw new UnsupportedOperationException("SVD for m < n not yet implemented");
            }
            int k = Math.min(m, n);

            DoubleBuffer aBuf = java.nio.ByteBuffer.allocateDirect(m * n * 8)
                .order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();
            aBuf.put(storage.toDoubleArray());
            aBuf.flip();

            DoubleBuffer sBuf = java.nio.ByteBuffer.allocateDirect(k * 8)
                .order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();
            DoubleBuffer uBuf = java.nio.ByteBuffer.allocateDirect(m * m * 8)
                .order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();
            DoubleBuffer vtBuf = java.nio.ByteBuffer.allocateDirect(n * n * 8)
                .order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();
            DoubleBuffer superb = java.nio.ByteBuffer.allocateDirect(Math.max(1, k - 1) * 8)
                .order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();

            int info = dgesvd((byte)'A', (byte)'A', m, n, aBuf, n, sBuf, uBuf, m, vtBuf, n, superb);
            if (info != 0) throw new RuntimeException("dgesvd failed with info: " + info);

            double[] sArr = new double[k];
            sBuf.get(sArr);
            
            double[] uArr = new double[m * m];
            uBuf.get(uArr);
            
            double[] vtArr = new double[n * n];
            vtBuf.get(vtArr);
            
            // Transpose VT to get V
            double[] vArr = new double[n * n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    vArr[i * n + j] = vtArr[j * n + i];
                }
            }

            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<>(
                RealDoubleMatrix.of(uArr, m, m),
                RealDoubleVector.of(sArr),
                RealDoubleMatrix.of(vArr, n, n)
            );
        }
        logger.log(System.Logger.Level.INFO, "svd() falling through to UOE. AVAILABLE: {0}, storageFound: {1}", AVAILABLE, (storage != null));
        throw new UnsupportedOperationException(getName() + ": svd() not available for these types");
    }

}
