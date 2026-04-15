/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.nio.DoubleBuffer;
import java.util.Optional;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult;

import org.episteme.core.technical.algorithm.AutoTuningManager;
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.nativ.technical.backend.nativ.NativeFFMLoader;
import org.episteme.nativ.technical.backend.nativ.NativeSafe;

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
@com.google.auto.service.AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, CPUBackend.class})
public class NativeCPULinearAlgebraBackend implements LinearAlgebraProvider<Real>, NativeBackend, CPUBackend {

    private static final System.Logger logger = System.getLogger(NativeCPULinearAlgebraBackend.class.getName());

    private static final MethodHandle DGEMM_HANDLE;
    private static final MethodHandle DGEMV_HANDLE;
    private static final MethodHandle DDOT_HANDLE;
    private static final MethodHandle DNRM2_HANDLE;
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
            Linker linker = NativeFFMLoader.getLinker();
            java.util.List<SymbolLookup> lookups = new java.util.ArrayList<>();
            
            // Try common libraries
            String[] commonLibs = {"episteme-jni", "openblas", "lapacke", "lapack", "mkl_rt"};
            for (String lib : commonLibs) {
                NativeFFMLoader.loadLibrary(lib, java.lang.foreign.Arena.global()).ifPresent(lookups::add);
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

                dscal = lookup.find("cblas_dscal").map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);

                // LAPACKE (Standard C interface names and variants)
                // Windows OpenBLAS often uses dgesv_ or lapack_dgesv
                dgesv = NativeFFMLoader.findSymbol(lookup, "LAPACKE_dgesv", "dgesv", "dgesv_", "lapack_dgesv")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
                
                dgetrf = NativeFFMLoader.findSymbol(lookup, "LAPACKE_dgetrf", "dgetrf", "dgetrf_", "lapack_dgetrf")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);
                
                dgetri = NativeFFMLoader.findSymbol(lookup, "LAPACKE_dgetri", "dgetri", "dgetri_", "lapack_dgetri")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);
                
                Optional<MemorySegment> s_dsyev = NativeFFMLoader.findSymbol(lookup, "LAPACKE_dsyev", "dsyev", "dsyev_", "lapack_dsyev");
                if (s_dsyev.isPresent()) {
                    FunctionDescriptor dsyevDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_BYTE, 
                        ValueLayout.JAVA_INT, java.lang.foreign.AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                        java.lang.foreign.AddressLayout.ADDRESS);
                    dsyev = linker.downcallHandle(s_dsyev.get(), dsyevDesc);
                }

                dpotrf = NativeFFMLoader.findSymbol(lookup, "LAPACKE_dpotrf", "dpotrf", "dpotrf_", "lapack_dpotrf")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);

                dgeqrf = NativeFFMLoader.findSymbol(lookup, "LAPACKE_dgeqrf", "dgeqrf", "dgeqrf_", "lapack_dgeqrf")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);

                dorgqr = NativeFFMLoader.findSymbol(lookup, "LAPACKE_dorgqr", "dorgqr", "dorgqr_", "lapack_dorgqr")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);

                Optional<MemorySegment> s_dgesvd = NativeFFMLoader.findSymbol(lookup, "LAPACKE_dgesvd", "dgesvd", "dgesvd_", "lapack_dgesvd");
                if (s_dgesvd.isPresent()) {
                    dgesvd = linker.downcallHandle(s_dgesvd.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                }

                dgels = NativeFFMLoader.findSymbol(lookup, "LAPACKE_dgels", "dgels", "dgels_", "lapack_dgels")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);

                avail = (dgemm != null || dgemv != null);
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
        
        // Broadened availability check
        AVAILABLE = avail;

        if (AVAILABLE) {
            logger.log(System.Logger.Level.INFO, "Native CPU-BLAS Backend initialized. SVD support: {0}, Eigen support: {1}, LAPACK support: {2}", 
                DGESVD_HANDLE != null, DSYEV_HANDLE != null, DGESV_HANDLE != null);
        } else {
            logger.log(System.Logger.Level.WARNING, "Native CPU-BLAS Backend NOT initialized (missing core BLAS symbols)");
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
        return AVAILABLE && !isExplicitlyDisabled();
    }

    @Override
    public String getId() {
        return "native-cpu";
    }

    @Override
    public String getType() {
        return "math";
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
    public boolean isCompatible(org.episteme.core.mathematics.structures.rings.Ring<?> ring) {
        if (ring == null) return false;
        // Native CPU backend is strictly for double-precision Real numbers.
        // It does not support Complex numbers or Native high-precision types.
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) return true;
        Object zero = ring.zero();
        return zero instanceof org.episteme.core.mathematics.numbers.real.RealDouble;
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
        if (!AVAILABLE || DGEMM_HANDLE == null) throw new UnsupportedOperationException("BLAS native library not found");
        NativeSafe.invoke(DGEMM_HANDLE, CblasRowMajor, CblasNoTrans, CblasNoTrans, m, n, k,
                alpha, MemorySegment.ofBuffer(A), lda, MemorySegment.ofBuffer(B), ldb, beta, MemorySegment.ofBuffer(C), ldc);
    }

    public void dgemv(int m, int n, double alpha, DoubleBuffer A, int lda, DoubleBuffer x, int incx, double beta, DoubleBuffer y, int incy) {
        if (!AVAILABLE || DGEMV_HANDLE == null) throw new UnsupportedOperationException("CBLAS dgemv not available");
        NativeSafe.invoke(DGEMV_HANDLE, CblasRowMajor, CblasNoTrans, m, n, alpha, MemorySegment.ofBuffer(A), lda, MemorySegment.ofBuffer(x), incx, beta, MemorySegment.ofBuffer(y), incy);
    }

    public double ddot(int n, DoubleBuffer x, int incx, DoubleBuffer y, int incy) {
        if (!AVAILABLE || DDOT_HANDLE == null) throw new UnsupportedOperationException("CBLAS ddot not available");
        return (double) NativeSafe.invoke(DDOT_HANDLE, n, MemorySegment.ofBuffer(x), incx, MemorySegment.ofBuffer(y), incy);
    }

    public double dnrm2(int n, DoubleBuffer x, int incx) {
        if (!AVAILABLE || DNRM2_HANDLE == null) throw new UnsupportedOperationException("CBLAS dnrm2 not available");
        return (double) NativeSafe.invoke(DNRM2_HANDLE, n, MemorySegment.ofBuffer(x), incx);
    }

    public void dscal(int n, double alpha, DoubleBuffer x, int incx) {
        if (!AVAILABLE || DSCAL_HANDLE == null) throw new UnsupportedOperationException("CBLAS dscal not available");
        NativeSafe.invoke(DSCAL_HANDLE, n, alpha, MemorySegment.ofBuffer(x), incx);
    }

    public int dgetrf(int m, int n, DoubleBuffer A, int lda, java.nio.IntBuffer ipiv) {
        if (!AVAILABLE || DGETRF_HANDLE == null) throw new UnsupportedOperationException("LAPACK dgetrf not available");
        return (int) NativeSafe.invoke(DGETRF_HANDLE, LAPACK_ROW_MAJOR, m, n, MemorySegment.ofBuffer(A), lda, MemorySegment.ofBuffer(ipiv));
    }

    public int dgetri(int n, DoubleBuffer A, int lda, java.nio.IntBuffer ipiv) {
        if (!AVAILABLE || DGETRI_HANDLE == null) throw new UnsupportedOperationException("LAPACK dgetri not available");
        return (int) NativeSafe.invoke(DGETRI_HANDLE, LAPACK_ROW_MAJOR, n, MemorySegment.ofBuffer(A), lda, MemorySegment.ofBuffer(ipiv));
    }

    public int dgesv(int n, int nrhs, DoubleBuffer A, int lda, java.nio.IntBuffer ipiv, DoubleBuffer B, int ldb) {
        if (!AVAILABLE || DGESV_HANDLE == null) throw new UnsupportedOperationException("LAPACK dgesv not available");
        return (int) NativeSafe.invoke(DGESV_HANDLE, LAPACK_ROW_MAJOR, n, nrhs, MemorySegment.ofBuffer(A), lda, MemorySegment.ofBuffer(ipiv), MemorySegment.ofBuffer(B), ldb);
    }

    public int dsyev(int n, DoubleBuffer A, int lda, DoubleBuffer W) {
        if (!AVAILABLE || DSYEV_HANDLE == null) throw new UnsupportedOperationException("LAPACK dsyev not available");
        return (int) NativeSafe.invoke(DSYEV_HANDLE, LAPACK_ROW_MAJOR, (byte) 'V', (byte) 'U', n, MemorySegment.ofBuffer(A), n, MemorySegment.ofBuffer(W));
    }

    public int dpotrf(int n, DoubleBuffer A, int lda) {
        if (!AVAILABLE || DPOTRF_HANDLE == null) throw new UnsupportedOperationException("LAPACK dpotrf not available");
        return (int) NativeSafe.invoke(DPOTRF_HANDLE, LAPACK_ROW_MAJOR, (byte) 'L', n, MemorySegment.ofBuffer(A), lda);
    }

    public int dgeqrf(int m, int n, DoubleBuffer A, int lda, DoubleBuffer tau) {
        if (!AVAILABLE || DGEQRF_HANDLE == null) throw new UnsupportedOperationException("LAPACK dgeqrf not available");
        return (int) NativeSafe.invoke(DGEQRF_HANDLE, LAPACK_ROW_MAJOR, m, n, MemorySegment.ofBuffer(A), n, MemorySegment.ofBuffer(tau));
    }

    public int dorgqr(int m, int n, int k, DoubleBuffer A, int lda, DoubleBuffer tau) {
        if (!AVAILABLE || DORGQR_HANDLE == null) throw new UnsupportedOperationException("LAPACK dorgqr not available");
        return (int) NativeSafe.invoke(DORGQR_HANDLE, LAPACK_ROW_MAJOR, m, n, k, MemorySegment.ofBuffer(A), lda, MemorySegment.ofBuffer(tau));
    }

    public int dgesvd(byte jobu, byte jobvt, int m, int n, DoubleBuffer A, int lda, DoubleBuffer S, DoubleBuffer U, int ldu, DoubleBuffer VT, int ldvt, DoubleBuffer superb) {
        if (!AVAILABLE || DGESVD_HANDLE == null) throw new UnsupportedOperationException("LAPACK dgesvd not available");
        return (int) NativeSafe.invoke(DGESVD_HANDLE, LAPACK_ROW_MAJOR, jobu, jobvt, m, n, MemorySegment.ofBuffer(A), lda, MemorySegment.ofBuffer(S), MemorySegment.ofBuffer(U), ldu, MemorySegment.ofBuffer(VT), ldvt, MemorySegment.ofBuffer(superb));
    }

    public int dgels(char trans, int m, int n, int nrhs, DoubleBuffer A, int lda, DoubleBuffer B, int ldb) {
        if (!AVAILABLE || DGELS_HANDLE == null) throw new UnsupportedOperationException("LAPACK dgels not available");
        return (int) NativeSafe.invoke(DGELS_HANDLE, LAPACK_ROW_MAJOR, (byte) trans, m, n, nrhs, MemorySegment.ofBuffer(A), lda, MemorySegment.ofBuffer(B), ldb);
    }

    @Override
    public Vector<Real> multiply(Matrix<Real> a, Vector<Real> b) {
        if (AVAILABLE && a instanceof RealDoubleMatrix && b instanceof RealDoubleVector) {
            RealDoubleMatrix adm = (RealDoubleMatrix) a;
            RealDoubleVector bdv = (RealDoubleVector) b;
            
            if (adm.cols() != bdv.dimension()) {
                throw new IllegalArgumentException("Matrix-Vector dimension mismatch: " + adm.cols() + " != " + bdv.dimension());
            }

            int m = adm.rows();
            int n = adm.cols();
            
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, adm.toDoubleArray());
                MemorySegment bSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, bdv.toDoubleArray());
                MemorySegment rSeg = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m);
                
                NativeSafe.invoke(DGEMV_HANDLE, CblasRowMajor, CblasNoTrans, m, n, 1.0, aSeg, n, bSeg, 1, 0.0, rSeg, 1);
                
                double[] result = rSeg.toArray(ValueLayout.JAVA_DOUBLE);
                return RealDoubleVector.of(result);
            }
        }
        // Fallback for non-RealDouble types or if not available
        double[] ad = toDoubleArray(a);
        double[] bd = toDoubleArray(b);
        double[] rd = new double[a.rows()];
        for (int i = 0; i < a.rows(); i++) {
            double sum = 0;
            for (int k = 0; k < a.cols(); k++) {
                sum += ad[i * a.cols() + k] * bd[k];
            }
            rd[i] = sum;
        }
        return RealDoubleVector.of(rd);
    }

    private double[] toDoubleArray(Vector<Real> v) {
        if (v instanceof RealDoubleVector) return ((RealDoubleVector) v).toDoubleArray();
        double[] d = new double[v.dimension()];
        for (int i = 0; i < v.dimension(); i++) d[i] = v.get(i).doubleValue();
        return d;
    }

    private double[] toDoubleArray(Matrix<Real> m) {
        if (m instanceof RealDoubleMatrix) return ((RealDoubleMatrix) m).toDoubleArray();
        double[] d = new double[m.rows() * m.cols()];
        for (int i = 0; i < m.rows(); i++) {
            for (int j = 0; j < m.cols(); j++) d[i * m.cols() + j] = m.get(i, j).doubleValue();
        }
        return d;
    }

    // --- LinearAlgebraProvider Implementation (Merged logic) ---


    @Override
    public void shutdown() {
        // No-op for now. Memory segments are managed via ScopedArena in operations.
    }






    @Override
    public Matrix<Real> add(Matrix<Real> a, Matrix<Real> b) {
        int rows = a.rows();
        int cols = a.cols();
        double[] ad = toDoubleArray(a);
        double[] bd = toDoubleArray(b);
        double[] rd = new double[ad.length];
        for (int i = 0; i < ad.length; i++) rd[i] = ad[i] + bd[i];
        return RealDoubleMatrix.of(rd, rows, cols);
    }

    @Override
    public Matrix<Real> subtract(Matrix<Real> a, Matrix<Real> b) {
        int rows = a.rows();
        int cols = a.cols();
        double[] ad = toDoubleArray(a);
        double[] bd = toDoubleArray(b);
        double[] rd = new double[ad.length];
        for (int i = 0; i < ad.length; i++) rd[i] = ad[i] - bd[i];
        return RealDoubleMatrix.of(rd, rows, cols);
    }

    @Override
    public Matrix<Real> scale(Real scalar, Matrix<Real> a) {
        int rows = a.rows();
        int cols = a.cols();
        double s = scalar.doubleValue();
        double[] ad = toDoubleArray(a);
        double[] rd = new double[ad.length];
        for (int i = 0; i < ad.length; i++) rd[i] = ad[i] * s;
        return RealDoubleMatrix.of(rd, rows, cols);
    }

    @Override
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        if (a.cols() != b.rows()) throw new IllegalArgumentException("Incompatible dimensions: " + a.cols() + " != " + b.rows());
        if (!AVAILABLE || DGEMM_HANDLE == null) throw new UnsupportedOperationException(getName() + ": DGEMM not available");
        
        int m = a.rows();
        int k = a.cols();
        int n = b.cols();
        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
            MemorySegment bSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b));
            MemorySegment cSeg = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);
            
            NativeSafe.invoke(DGEMM_HANDLE, CblasRowMajor, CblasNoTrans, CblasNoTrans, m, n, k, 1.0, aSeg, k, bSeg, n, 0.0, cSeg, n);
            
            double[] cData = cSeg.toArray(ValueLayout.JAVA_DOUBLE);
            return RealDoubleMatrix.of(cData, m, n);
        }
    }

    @Override
    public Matrix<Real> inverse(Matrix<Real> a) {
        if (!AVAILABLE) throw new UnsupportedOperationException(getName() + ": inverse() not available");
        int m = a.rows();
        int n = a.cols();
        if (m != n) {
             return pseudoInverse(a);
        }
        if (m == n) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
                MemorySegment ipiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
                
                int info = (int) NativeSafe.invoke(DGETRF_HANDLE, LAPACK_ROW_MAJOR, n, n, aSeg, n, ipiv);
                if (info != 0) throw new ArithmeticException("Matrix is singular or dgetrf failed: " + info);
                
                info = (int) NativeSafe.invoke(DGETRI_HANDLE, LAPACK_ROW_MAJOR, n, aSeg, n, ipiv);
                if (info != 0) throw new ArithmeticException("dgetri failed: " + info);
                
                double[] invData = aSeg.toArray(ValueLayout.JAVA_DOUBLE);
                return RealDoubleMatrix.of(invData, n, n);
            }
        }
        throw new UnsupportedOperationException(getName() + ": inverse() failed");
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





    // Other methods default to UnsupportedOperationException
    @Override
    public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) {
        int m = a.rows();
        int n = a.cols();
        
        if (!AVAILABLE || DGELS_HANDLE == null) throw new UnsupportedOperationException(getName() + ": DGELS not available");


        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
            int maxDim = Math.max(m, n);
            double[] bPad = new double[maxDim];
            double[] bOrig = toDoubleArray(b);
            System.arraycopy(bOrig, 0, bPad, 0, bOrig.length);
            MemorySegment xSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, bPad);
            
            int info = (int) NativeSafe.invoke(DGELS_HANDLE, LAPACK_ROW_MAJOR, (byte) 'N', m, n, 1, aSeg, n, xSeg, 1);
            if (info == 0) {
                double[] result = new double[n];
                MemorySegment.copy(xSeg, ValueLayout.JAVA_DOUBLE, 0L, result, 0, n);
                return RealDoubleVector.of(result);
            }
            throw new ArithmeticException("Native dgels failed with info: " + info);
        }
    }

    @Override
    public Matrix<Real> transpose(Matrix<Real> a) {
        int m = a.rows();
        int n = a.cols();
        double[] data = toDoubleArray(a);
        double[] res = new double[n * m];
        
        // Tiled Transpose for better cache locality (March 24 Optimization)
        int tileSize = 64;
        for (int i = 0; i < m; i += tileSize) {
            for (int j = 0; j < n; j += tileSize) {
                for (int ii = i; ii < Math.min(i + tileSize, m); ii++) {
                    for (int jj = j; jj < Math.min(j + tileSize, n); jj++) {
                        res[jj * m + ii] = data[ii * n + jj];
                    }
                }
            }
        }
        return RealDoubleMatrix.of(res, n, m);
    }


    @Override
    public Real determinant(Matrix<Real> a) {
        if (!AVAILABLE || DGESV_HANDLE == null || a.rows() != a.cols()) throw new UnsupportedOperationException(getName() + ": determinant not available");

        int n = a.rows();
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
            MemorySegment ipiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
            
            int info = (int) NativeSafe.invoke(DGETRF_HANDLE, LAPACK_ROW_MAJOR, n, n, aSeg, n, ipiv);
            if (info < 0) throw new IllegalArgumentException("Illegal argument to dgetrf: " + info);
            if (info > 0) return Real.ZERO; // Singular matrix
            
            double[] luData = aSeg.toArray(ValueLayout.JAVA_DOUBLE);
            double det = 1.0;
            for (int i = 0; i < n; i++) {
                det *= luData[i * n + i];
            }
            
            int swaps = 0;
            for (int i = 0; i < n; i++) {
                if (ipiv.getAtIndex(ValueLayout.JAVA_INT, (long) i) != (i + 1)) {
                    swaps++;
                }
            }
            if (swaps % 2 != 0) det = -det;
            return Real.of(det);
        }
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<Real> lu(Matrix<Real> a) {
        if (AVAILABLE && a.rows() == a.cols()) {
            int n = a.rows();
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
                MemorySegment ipiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
                
                int info = (int) NativeSafe.invoke(DGETRF_HANDLE, LAPACK_ROW_MAJOR, n, n, aSeg, n, ipiv);
                if (info < 0) throw new IllegalArgumentException("Illegal argument to dgetrf: " + info);
                
                double[] luArr = aSeg.toArray(ValueLayout.JAVA_DOUBLE);
                double[] lData = new double[n * n];
                double[] uData = new double[n * n];
                
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
                        int ip = ipiv.getAtIndex(ValueLayout.JAVA_INT, (long) i) - 1;
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
        }
        throw new UnsupportedOperationException(getName() + ": lu() not available for these types");
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<Real> eigen(Matrix<Real> a) {
        if (AVAILABLE && a.rows() == a.cols()) {
            int n = a.rows();
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
                MemorySegment wSeg = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n);
                
                int info = (int) NativeSafe.invoke(DSYEV_HANDLE, LAPACK_ROW_MAJOR, (byte) 'V', (byte) 'U', n, aSeg, n, wSeg);
                if (info < 0) throw new IllegalArgumentException("Illegal argument to dsyev: " + info);
                if (info > 0) throw new ArithmeticException("Eigenvalue decomposition failed to converge");
                
                double[] wData = wSeg.toArray(ValueLayout.JAVA_DOUBLE);
                double[] evData = aSeg.toArray(ValueLayout.JAVA_DOUBLE);
                
                return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<>(
                    RealDoubleMatrix.of(evData, n, n),
                    RealDoubleVector.of(wData)
                );
            }
        }
        throw new UnsupportedOperationException(getName() + ": eigen() not available for these types");
    }





    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<Real> cholesky(Matrix<Real> a) {
        if (AVAILABLE && a.rows() == a.cols()) {
            int n = a.rows();
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
                
                int info = (int) NativeSafe.invoke(DPOTRF_HANDLE, LAPACK_ROW_MAJOR, (byte) 'L', n, aSeg, n);
                if (info < 0) throw new IllegalArgumentException("Illegal argument to dpotrf: " + info);
                if (info > 0) throw new ArithmeticException("Matrix is not positive definite (info=" + info + ")");
                
                double[] data = aSeg.toArray(ValueLayout.JAVA_DOUBLE);
                // Zero out upper part
                for (int i = 0; i < n; i++) {
                    for (int j = i + 1; j < n; j++) {
                        data[i * n + j] = 0.0;
                    }
                }
                return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<>(
                    RealDoubleMatrix.of(data, n, n)
                );
            }
        }
        throw new UnsupportedOperationException(getName() + ": cholesky() not available for these types");
    }
    @Override
    public Real dot(Vector<Real> a, Vector<Real> b) {
        if (!AVAILABLE || DDOT_HANDLE == null) throw new UnsupportedOperationException(getName() + ": dot not available");
        if (a.dimension() != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
            MemorySegment bSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b));
            double result = (double) NativeSafe.invoke(DDOT_HANDLE, a.dimension(), aSeg, 1, bSeg, 1);
            return Real.of(result);
        }
    }

    @Override
    public Real norm(Vector<Real> a) {
        double[] ad = toDoubleArray(a);
        double sumSq = 0.0;
        for (double d : ad) sumSq += d * d;
        return Real.of(Math.sqrt(sumSq));
    }

    @Override
    public Vector<Real> multiply(Vector<Real> vector, Real scalar) {
        if (AVAILABLE) {
            int dim = vector.dimension();
            RealDoubleVector res = RealDoubleVector.direct(dim);
            res.getBuffer().put(toDoubleArray(vector));
            res.getBuffer().flip();
            dscal(dim, scalar.doubleValue(), res.getBuffer(), 1);
            return res;
        }
        throw new UnsupportedOperationException(getName() + ": Vector multiply(scalar) not available for these types");
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<Real> qr(Matrix<Real> a) {
        if (!AVAILABLE || DGEQRF_HANDLE == null) throw new UnsupportedOperationException(getName() + ": QR not available");
        int m = a.rows();
        int n = a.cols();
        int k = Math.min(m, n);

        RealDoubleMatrix qMat = RealDoubleMatrix.direct(m, n); // Used temporarily to hold A
        qMat.getBuffer().put(toDoubleArray(a));
        qMat.getBuffer().position(0);

        DoubleBuffer tau = java.nio.ByteBuffer.allocateDirect(k * 8)
            .order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();

        // 1. DGEQRF
        int info = dgeqrf(m, n, qMat.getBuffer(), n, tau);
        if (info == 0) {
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
            if (info == 0) {
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
        }
        throw new ArithmeticException("Native QR failed with info: " + info);
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<Real> svd(Matrix<Real> a) {
        if (AVAILABLE) {
            int m = a.rows();
            int n = a.cols();
            if (DGESVD_HANDLE == null) throw new UnsupportedOperationException("LAPACKE dgesvd not available");
            
            boolean transposed = false;
            Matrix<Real> workA = a;
            if (m < n) {
                transposed = true;
                workA = a.transpose();
                int tmp = m; m = n; n = tmp;
            }

            int k = Math.min(m, n);
            double[] aData = toDoubleArray(workA);

            DoubleBuffer aBuf = java.nio.ByteBuffer.allocateDirect(m * n * 8).order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();
            aBuf.put(aData); aBuf.flip();

            DoubleBuffer sBuf = java.nio.ByteBuffer.allocateDirect(k * 8).order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();
            DoubleBuffer uBuf = java.nio.ByteBuffer.allocateDirect(m * m * 8).order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();
            DoubleBuffer vtBuf = java.nio.ByteBuffer.allocateDirect(n * n * 8).order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();
            DoubleBuffer superb = java.nio.ByteBuffer.allocateDirect(Math.max(1, k - 1) * 8).order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();

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

            Matrix<Real> U = RealDoubleMatrix.of(uArr, m, m);
            Vector<Real> S = RealDoubleVector.of(sArr);
            Matrix<Real> V = RealDoubleMatrix.of(vArr, n, n);

            if (transposed) {
                return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<>(V, S, U);
            }
            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<>(U, S, V);
        }
        throw new UnsupportedOperationException(getName() + ": svd() not available");
    }

    @Override
    public Vector<Real> solve(LUResult<Real> lu, Vector<Real> b) {
        return org.episteme.core.mathematics.linearalgebra.matrices.solvers.GenericLU.solve(lu, b, (org.episteme.core.mathematics.structures.rings.Field<Real>)b.getScalarRing(), this);
    }

    @Override
    public Vector<Real> solve(QRResult<Real> qr, Vector<Real> b) {
        return org.episteme.core.mathematics.linearalgebra.matrices.solvers.GenericQR.solve(qr, b, (org.episteme.core.mathematics.structures.rings.Field<Real>)b.getScalarRing(), this);
    }

    @Override
    public Vector<Real> solve(CholeskyResult<Real> cholesky, Vector<Real> b) {
        return org.episteme.core.mathematics.linearalgebra.matrices.solvers.GenericCholesky.solve(cholesky, b, (org.episteme.core.mathematics.structures.rings.Field<Real>)b.getScalarRing(), this);
    }

}

