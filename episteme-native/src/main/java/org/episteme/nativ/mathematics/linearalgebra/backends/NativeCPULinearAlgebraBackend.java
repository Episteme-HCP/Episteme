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
import java.util.List;
import java.util.ArrayList;

import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.structures.rings.Field;
import org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage;

import org.episteme.core.technical.algorithm.AutoTuningManager;
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.core.technical.algorithm.AlgorithmProvider;
import org.episteme.nativ.technical.backend.nativ.NativeFFMLoader;
import org.episteme.nativ.technical.backend.nativ.NativeSafe;

/**
 * Standalone Native Linear Algebra backend using bundled episteme_native library.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@com.google.auto.service.AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class})
public class NativeCPULinearAlgebraBackend<E> implements LinearAlgebraProvider<E>, NativeBackend, CPUBackend {

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

    // Single Precision (S)
    private static final MethodHandle SGEMM_HANDLE;
    private static final MethodHandle SGEMV_HANDLE;
    private static final MethodHandle SDOT_HANDLE;
    private static final MethodHandle SNRM2_HANDLE;
    private static final MethodHandle SSCAL_HANDLE;

    // Complex Double (Z)
    private static final MethodHandle ZGEMM_HANDLE;
    private static final MethodHandle ZGEMV_HANDLE;
    private static final MethodHandle ZDOTU_HANDLE;
    private static final MethodHandle ZNRM2_HANDLE;
    private static final MethodHandle ZSCAL_HANDLE;

    // Complex Float (C)
    private static final MethodHandle CGEMM_HANDLE;
    private static final MethodHandle CGEMV_HANDLE;
    private static final MethodHandle CDOTU_HANDLE;
    private static final MethodHandle CNRM2_HANDLE;
    private static final MethodHandle CSCAL_HANDLE;
    
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

        MethodHandle sgemm = null, sgemv = null, sdot = null, snrm2 = null, sscal = null;
        MethodHandle zgemm = null, zgemv = null, zdotu = null, znrm2 = null, zscal = null;
        MethodHandle cgemm = null, cgemv = null, cdotu = null, cnrm2 = null, cscal = null;
        
        boolean avail = false;

        try {
            Linker linker = NativeFFMLoader.getLinker();
            List<SymbolLookup> lookups = new ArrayList<>();
            
            // Try common libraries
            String[] commonLibs = {"episteme-jni", "openblas", "lapacke", "lapack", "mkl_rt"};
            for (String lib : commonLibs) {
                NativeFFMLoader.loadLibrary(lib, Arena.global()).ifPresent(lookups::add);
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

                // --- Single Precision (S) ---
                sgemm = lookup.find("cblas_sgemm").map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(
                    ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                    ValueLayout.JAVA_FLOAT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_FLOAT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
                sgemv = lookup.find("cblas_sgemv").map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(
                    ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_FLOAT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_FLOAT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
                sdot = lookup.find("cblas_sdot").map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_FLOAT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
                snrm2 = lookup.find("cblas_snrm2").map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_FLOAT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
                sscal = lookup.find("cblas_sscal").map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_FLOAT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);

                // --- Complex Double (Z) ---
                zgemm = lookup.find("cblas_zgemm").map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(
                    ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                    ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
                zgemv = lookup.find("cblas_zgemv").map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(
                    ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
                zdotu = lookup.find("cblas_zdotu_sub").map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);
                znrm2 = lookup.find("cblas_dznrm2").map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
                zscal = lookup.find("cblas_zscal").map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);

                // --- Complex Float (C) ---
                cgemm = lookup.find("cblas_cgemm").map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(
                    ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                    ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
                cgemv = lookup.find("cblas_cgemv").map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(
                    ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
                cdotu = lookup.find("cblas_cdotu_sub").map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);
                cnrm2 = lookup.find("cblas_scnrm2").map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_FLOAT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
                cscal = lookup.find("cblas_cscal").map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);

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

                avail = (dgemm != null || dgemv != null);
                
                // LAPACKE S/Z/C variants
                NativeFFMLoader.findSymbol(lookup, "LAPACKE_sgesv", "sgesv", "sgesv_", "lapack_sgesv").ifPresent(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT)));
                NativeFFMLoader.findSymbol(lookup, "LAPACKE_zgesv", "zgesv", "zgesv_", "lapack_zgesv").ifPresent(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT)));
                NativeFFMLoader.findSymbol(lookup, "LAPACKE_cgesv", "cgesv", "cgesv_", "lapack_cgesv").ifPresent(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT)));
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

        SGEMM_HANDLE = sgemm;
        SGEMV_HANDLE = sgemv;
        SDOT_HANDLE = sdot;
        SNRM2_HANDLE = snrm2;
        SSCAL_HANDLE = sscal;

        ZGEMM_HANDLE = zgemm;
        ZGEMV_HANDLE = zgemv;
        ZDOTU_HANDLE = zdotu;
        ZNRM2_HANDLE = znrm2;
        ZSCAL_HANDLE = zscal;

        CGEMM_HANDLE = cgemm;
        CGEMV_HANDLE = cgemv;
        CDOTU_HANDLE = cdotu;
        CNRM2_HANDLE = cnrm2;
        CSCAL_HANDLE = cscal;
        
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
    public boolean isExplicitlyDisabled() {
        String id = getId();
        return id != null && (Boolean.getBoolean("episteme.backend.disable." + id) || Boolean.getBoolean("episteme.linearalgebra.disable." + id));
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
    public String getDescription() {
        return "High-performance native CPU linear algebra implementation using Panama FFM API.";
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public Object createBackend() {
        return this;
    }

    @Override
    public List<AlgorithmProvider> getAlgorithmProviders() {
        return List.of(this);
    }

    @Override
    public double score(OperationContext context) {
        if (!AVAILABLE) return -1.0;
        return AutoTuningManager.getDynamicScore(getName(), context.getDimensionality(), getPriority());
    }

    @Override
    public boolean isCompatible(Ring<?> ring) {
        if (ring == null) return false;
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) return true;
        if (ring instanceof org.episteme.core.mathematics.sets.Complexes) return true;
        Object zero = ring.zero();
        return zero instanceof org.episteme.core.mathematics.numbers.real.RealDouble ||
               zero instanceof org.episteme.core.mathematics.numbers.real.RealFloat ||
               zero instanceof org.episteme.core.mathematics.numbers.complex.Complex;
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
    @SuppressWarnings("unchecked")
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (!AVAILABLE) return LinearAlgebraProvider.super.multiply(a, b);
        
        Ring<E> ring = a.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            E zero = ring.zero();
            if (zero instanceof org.episteme.core.mathematics.numbers.real.RealFloat) {
                return (Vector<E>) multiplyRealFloat((Matrix<org.episteme.core.mathematics.numbers.real.Real>)(Object)a, (Vector<org.episteme.core.mathematics.numbers.real.Real>)(Object)b);
            }
            return (Vector<E>) (Object) multiplyReal((Matrix<Real>)(Object)a, (Vector<Real>)(Object)b);
        } else if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
            E zero = ring.zero();
            if (zero instanceof org.episteme.core.mathematics.numbers.complex.Complex && ((org.episteme.core.mathematics.numbers.complex.Complex)zero).getReal() instanceof org.episteme.core.mathematics.numbers.real.RealFloat) {
                return (Vector<E>) multiplyComplexFloat((Matrix<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)a, (Vector<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)b);
            }
            return (Vector<E>) (Object) multiplyComplex((Matrix<Complex>)(Object)a, (Vector<Complex>)(Object)b);
        }
        return LinearAlgebraProvider.super.multiply(a, b);
    }

    @SuppressWarnings("unchecked")
    private Vector<org.episteme.core.mathematics.numbers.real.Real> multiplyRealFloat(Matrix<org.episteme.core.mathematics.numbers.real.Real> a, Vector<org.episteme.core.mathematics.numbers.real.Real> b) {
        if (AVAILABLE && SGEMV_HANDLE != null) {
            int m = a.rows();
            int n = a.cols();
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(a));
                MemorySegment bSeg = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(b));
                MemorySegment rSeg = arena.allocate(ValueLayout.JAVA_FLOAT, (long) m);
                NativeSafe.invoke(SGEMV_HANDLE, CblasRowMajor, CblasNoTrans, m, n, 1.0f, aSeg, n, bSeg, 1, 0.0f, rSeg, 1);
                float[] result = rSeg.toArray(ValueLayout.JAVA_FLOAT);
                org.episteme.core.mathematics.numbers.real.Real[] res = new org.episteme.core.mathematics.numbers.real.Real[m];
                for (int i=0; i<m; i++) res[i] = org.episteme.core.mathematics.numbers.real.RealFloat.of(result[i]);
                return Vector.of(java.util.Arrays.asList(res), org.episteme.core.mathematics.sets.Reals.getInstance());
            }
        }
        return (Vector<org.episteme.core.mathematics.numbers.real.Real>) (Object) LinearAlgebraProvider.super.multiply((Matrix<E>) (Object) a, (Vector<E>) (Object) b);
    }

    @SuppressWarnings("unchecked")
    private Vector<org.episteme.core.mathematics.numbers.complex.Complex> multiplyComplexFloat(Matrix<org.episteme.core.mathematics.numbers.complex.Complex> a, Vector<org.episteme.core.mathematics.numbers.complex.Complex> b) {
        if (AVAILABLE && CGEMV_HANDLE != null) {
            int m = a.rows();
            int n = a.cols();
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toComplexFloatArray(a));
                MemorySegment bSeg = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toComplexFloatArray(b));
                MemorySegment rSeg = arena.allocate(ValueLayout.JAVA_FLOAT, (long) m * 2);
                MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_FLOAT, 1.0f, 0.0f);
                MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_FLOAT, 0.0f, 0.0f);
                NativeSafe.invoke(CGEMV_HANDLE, CblasRowMajor, CblasNoTrans, m, n, alpha, aSeg, n, bSeg, 1, beta, rSeg, 1);
                float[] result = rSeg.toArray(ValueLayout.JAVA_FLOAT);
                org.episteme.core.mathematics.numbers.complex.Complex[] res = new org.episteme.core.mathematics.numbers.complex.Complex[m];
                for (int i=0; i<m; i++) res[i] = org.episteme.core.mathematics.numbers.complex.Complex.of(org.episteme.core.mathematics.numbers.real.RealFloat.of(result[2*i]), org.episteme.core.mathematics.numbers.real.RealFloat.of(result[2*i+1]));
                return Vector.of(java.util.Arrays.asList(res), org.episteme.core.mathematics.sets.Complexes.getInstance());
            }
        }
        return (Vector<org.episteme.core.mathematics.numbers.complex.Complex>) (Object) LinearAlgebraProvider.super.multiply((Matrix<E>) (Object) a, (Vector<E>) (Object) b);
    }

    @SuppressWarnings("unchecked")
    private Vector<Real> multiplyReal(Matrix<Real> a, Vector<Real> b) {
        if (AVAILABLE && DGEMV_HANDLE != null) {
            int m = a.rows();
            int n = a.cols();
            
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
                MemorySegment bSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b));
                MemorySegment rSeg = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m);
                
                NativeSafe.invoke(DGEMV_HANDLE, CblasRowMajor, CblasNoTrans, m, n, 1.0, aSeg, n, bSeg, 1, 0.0, rSeg, 1);
                
                double[] result = rSeg.toArray(ValueLayout.JAVA_DOUBLE);
                return (Vector<Real>)(Object) org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(result);
            }
        }
        
        // Fallback
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
        return (Vector<Real>)(Object) org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(rd);
    }

    @SuppressWarnings("unchecked")
    private Vector<Complex> multiplyComplex(Matrix<Complex> a, Vector<Complex> b) {
        if (AVAILABLE && ZGEMV_HANDLE != null) {
            int m = a.rows();
            int n = a.cols();
            try (Arena arena = Arena.ofConfined()) {
                double[] aData = toComplexDoubleArray(a);
                double[] bData = toComplexDoubleArray(b);
                MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, aData);
                MemorySegment bSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, bData);
                MemorySegment rSeg = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * 2);
                
                MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0, 0.0);
                MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0, 0.0);
                
                NativeSafe.invoke(ZGEMV_HANDLE, CblasRowMajor, CblasNoTrans, m, n, alpha, aSeg, n, bSeg, 1, beta, rSeg, 1);
                
                double[] result = rSeg.toArray(ValueLayout.JAVA_DOUBLE);
                Complex[] complexRes = new Complex[m];
                for (int i=0; i<m; i++) complexRes[i] = Complex.of(result[2*i], result[2*i+1]);
                return Vector.of(java.util.Arrays.asList(complexRes), org.episteme.core.mathematics.sets.Complexes.getInstance());
            }
        }
        return (Vector<Complex>) (Object) LinearAlgebraProvider.super.multiply((Matrix<E>) (Object) a, (Vector<E>) (Object) b);
    }

    private double[] toDoubleArray(Vector<Real> v) {
        if (v instanceof org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector) return ((org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector) v).toDoubleArray();
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

    private double[] toComplexDoubleArray(Vector<Complex> v) {
        int n = v.dimension();
        double[] d = new double[2 * n];
        for (int i = 0; i < n; i++) {
            Complex c = v.get(i);
            d[2 * i] = c.real();
            d[2 * i + 1] = c.imaginary();
        }
        return d;
    }

    private double[] toComplexDoubleArray(Matrix<org.episteme.core.mathematics.numbers.complex.Complex> m) {
        int r = m.rows();
        int c = m.cols();
        double[] d = new double[2 * r * c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                org.episteme.core.mathematics.numbers.complex.Complex val = m.get(i, j);
                d[2 * (i * c + j)] = val.real();
                d[2 * (i * c + j) + 1] = val.imaginary();
            }
        }
        return d;
    }

    private float[] toFloatArray(Vector<org.episteme.core.mathematics.numbers.real.Real> v) {
        float[] d = new float[v.dimension()];
        for (int i = 0; i < v.dimension(); i++) d[i] = v.get(i).floatValue();
        return d;
    }

    private float[] toFloatArray(Matrix<org.episteme.core.mathematics.numbers.real.Real> m) {
        float[] d = new float[m.rows() * m.cols()];
        for (int i = 0; i < m.rows(); i++) {
            for (int j = 0; j < m.cols(); j++) d[i * m.cols() + j] = m.get(i, j).floatValue();
        }
        return d;
    }

    private float[] toComplexFloatArray(Vector<org.episteme.core.mathematics.numbers.complex.Complex> v) {
        int n = v.dimension();
        float[] d = new float[2 * n];
        for (int i = 0; i < n; i++) {
            org.episteme.core.mathematics.numbers.complex.Complex c = v.get(i);
            d[2 * i] = (float) c.real();
            d[2 * i + 1] = (float) c.imaginary();
        }
        return d;
    }

    private float[] toComplexFloatArray(Matrix<org.episteme.core.mathematics.numbers.complex.Complex> m) {
        int r = m.rows();
        int c = m.cols();
        float[] d = new float[2 * r * c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                org.episteme.core.mathematics.numbers.complex.Complex val = m.get(i, j);
                d[2 * (i * c + j)] = (float) val.real();
                d[2 * (i * c + j) + 1] = (float) val.imaginary();
            }
        }
        return d;
    }

    // --- LinearAlgebraProvider Implementation (Merged logic) ---


    @Override
    public void shutdown() {
        // No-op for now. Memory segments are managed via ScopedArena in operations.
    }






    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        Ring<E> ring = a.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            E zero = ring.zero();
            if (zero instanceof org.episteme.core.mathematics.numbers.real.RealFloat) {
                float[] ad = toFloatArray((Matrix<org.episteme.core.mathematics.numbers.real.Real>)(Object)a);
                float[] bd = toFloatArray((Matrix<org.episteme.core.mathematics.numbers.real.Real>)(Object)b);
                float[] rd = new float[ad.length];
                for (int i = 0; i < ad.length; i++) rd[i] = ad[i] + bd[i];
                org.episteme.core.mathematics.numbers.real.Real[] resData = new org.episteme.core.mathematics.numbers.real.Real[a.rows() * a.cols()];
                for (int i=0; i<resData.length; i++) resData[i] = org.episteme.core.mathematics.numbers.real.RealFloat.of(rd[i]);
                return new GenericMatrix<>(new DenseMatrixStorage<>(a.rows(), a.cols(), (E[])resData), this, ring);
            }
            double[] ad = toDoubleArray((Matrix<org.episteme.core.mathematics.numbers.real.Real>)(Object)a);
            double[] bd = toDoubleArray((Matrix<org.episteme.core.mathematics.numbers.real.Real>)(Object)b);
            double[] rd = new double[ad.length];
            for (int i = 0; i < ad.length; i++) rd[i] = ad[i] + bd[i];
            return (Matrix<E>)(Object) RealDoubleMatrix.of(rd, a.rows(), a.cols());
        } else if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
            E zero = ring.zero();
            if (zero instanceof org.episteme.core.mathematics.numbers.complex.Complex && ((org.episteme.core.mathematics.numbers.complex.Complex)zero).getReal() instanceof org.episteme.core.mathematics.numbers.real.RealFloat) {
                float[] ad = toComplexFloatArray((Matrix<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)a);
                float[] bd = toComplexFloatArray((Matrix<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)b);
                float[] rd = new float[ad.length];
                for (int i = 0; i < ad.length; i++) rd[i] = ad[i] + bd[i];
                org.episteme.core.mathematics.numbers.complex.Complex[] resData = new org.episteme.core.mathematics.numbers.complex.Complex[a.rows() * a.cols()];
                for (int i=0; i<resData.length; i++) resData[i] = org.episteme.core.mathematics.numbers.complex.Complex.of(org.episteme.core.mathematics.numbers.real.RealFloat.of(rd[2*i]), org.episteme.core.mathematics.numbers.real.RealFloat.of(rd[2*i+1]));
                return new GenericMatrix<>(new DenseMatrixStorage<>(a.rows(), a.cols(), (E[])resData), this, ring);
            }
            double[] ad = toComplexDoubleArray((Matrix<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)a);
            double[] bd = toComplexDoubleArray((Matrix<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)b);
            double[] rd = new double[ad.length];
            for (int i = 0; i < ad.length; i++) rd[i] = ad[i] + bd[i];
            org.episteme.core.mathematics.numbers.complex.Complex[] resData = new org.episteme.core.mathematics.numbers.complex.Complex[a.rows() * a.cols()];
            for (int i=0; i<resData.length; i++) resData[i] = org.episteme.core.mathematics.numbers.complex.Complex.of(rd[2*i], rd[2*i+1]);
            return new GenericMatrix<>(new DenseMatrixStorage<>(a.rows(), a.cols(), (E[])resData), this, ring);
        }
        return LinearAlgebraProvider.super.add(a, b);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        Ring<E> ring = a.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            int rows = a.rows();
            int cols = a.cols();
            double[] ad = toDoubleArray((Matrix<Real>)(Object)a);
            double[] bd = toDoubleArray((Matrix<Real>)(Object)b);
            double[] rd = new double[ad.length];
            for (int i = 0; i < ad.length; i++) rd[i] = ad[i] - bd[i];
            return (Matrix<E>)(Object) RealDoubleMatrix.of(rd, rows, cols);
        }
        return LinearAlgebraProvider.super.subtract(a, b);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        Ring<E> ring = a.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            E zero = ring.zero();
            if (zero instanceof org.episteme.core.mathematics.numbers.real.RealFloat) {
                float s = ((org.episteme.core.mathematics.numbers.real.Real)scalar).floatValue();
                float[] ad = toFloatArray((Matrix<org.episteme.core.mathematics.numbers.real.Real>)(Object)a);
                float[] rd = new float[ad.length];
                for (int i = 0; i < ad.length; i++) rd[i] = ad[i] * s;
                org.episteme.core.mathematics.numbers.real.Real[] resData = new org.episteme.core.mathematics.numbers.real.Real[a.rows() * a.cols()];
                for (int i=0; i<resData.length; i++) resData[i] = org.episteme.core.mathematics.numbers.real.RealFloat.of(rd[i]);
                return new GenericMatrix<>(new DenseMatrixStorage<>(a.rows(), a.cols(), (E[])resData), this, ring);
            }
            double s = ((org.episteme.core.mathematics.numbers.real.Real)scalar).doubleValue();
            double[] ad = toDoubleArray((Matrix<org.episteme.core.mathematics.numbers.real.Real>)(Object)a);
            double[] rd = new double[ad.length];
            for (int i = 0; i < ad.length; i++) rd[i] = ad[i] * s;
            return (Matrix<E>)(Object) RealDoubleMatrix.of(rd, a.rows(), a.cols());
        } else if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
            org.episteme.core.mathematics.numbers.complex.Complex s = (org.episteme.core.mathematics.numbers.complex.Complex)scalar;
            
            org.episteme.core.mathematics.numbers.complex.Complex[] res = new org.episteme.core.mathematics.numbers.complex.Complex[a.rows() * a.cols()];
            for (int i=0; i<a.rows(); i++) {
                for (int j=0; j<a.cols(); j++) res[i*a.cols()+j] = s.multiply(((Matrix<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)a).get(i, j));
            }
            return new GenericMatrix<>(new DenseMatrixStorage<>(a.rows(), a.cols(), (E[])res), this, ring);
        }
        return LinearAlgebraProvider.super.scale(scalar, a);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (a.cols() != b.rows()) throw new IllegalArgumentException("Incompatible dimensions: " + a.cols() + " != " + b.rows());
        
        Ring<E> ring = a.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            E zero = ring.zero();
            if (zero instanceof org.episteme.core.mathematics.numbers.real.RealFloat) {
                if (!AVAILABLE || SGEMM_HANDLE == null) return LinearAlgebraProvider.super.multiply(a, b);
                int m = a.rows();
                int k = a.cols();
                int n = b.cols();
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray((Matrix<org.episteme.core.mathematics.numbers.real.Real>)(Object)a));
                    MemorySegment bSeg = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray((Matrix<org.episteme.core.mathematics.numbers.real.Real>)(Object)b));
                    MemorySegment cSeg = arena.allocate(ValueLayout.JAVA_FLOAT, (long) m * n);
                    NativeSafe.invoke(SGEMM_HANDLE, CblasRowMajor, CblasNoTrans, CblasNoTrans, m, n, k, 1.0f, aSeg, k, bSeg, n, 0.0f, cSeg, n);
                    float[] cData = cSeg.toArray(ValueLayout.JAVA_FLOAT);
                    org.episteme.core.mathematics.numbers.real.Real[] resData = new org.episteme.core.mathematics.numbers.real.Real[m * n];
                    for (int i=0; i<m*n; i++) resData[i] = org.episteme.core.mathematics.numbers.real.RealFloat.of(cData[i]);
                    E[] castedRes = (E[]) resData;
                    return new GenericMatrix<>(new DenseMatrixStorage<>(m, n, castedRes), this, ring);
                }
            }
            if (!AVAILABLE || DGEMM_HANDLE == null) return LinearAlgebraProvider.super.multiply(a, b);
            int m = a.rows();
            int k = a.cols();
            int n = b.cols();
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray((Matrix<org.episteme.core.mathematics.numbers.real.Real>)(Object)a));
                MemorySegment bSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray((Matrix<org.episteme.core.mathematics.numbers.real.Real>)(Object)b));
                MemorySegment cSeg = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);
                NativeSafe.invoke(DGEMM_HANDLE, CblasRowMajor, CblasNoTrans, CblasNoTrans, m, n, k, 1.0, aSeg, k, bSeg, n, 0.0, cSeg, n);
                double[] cData = cSeg.toArray(ValueLayout.JAVA_DOUBLE);
                return (Matrix<E>)(Object) RealDoubleMatrix.of(cData, m, n);
            }
        } else if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
            E zero = ring.zero();
            if (zero instanceof org.episteme.core.mathematics.numbers.complex.Complex && ((org.episteme.core.mathematics.numbers.complex.Complex)zero).getReal() instanceof org.episteme.core.mathematics.numbers.real.RealFloat) {
                if (!AVAILABLE || CGEMM_HANDLE == null) return LinearAlgebraProvider.super.multiply(a, b);
                int m = a.rows();
                int k = a.cols();
                int n = b.cols();
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toComplexFloatArray((Matrix<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)a));
                    MemorySegment bSeg = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toComplexFloatArray((Matrix<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)b));
                    MemorySegment cSeg = arena.allocate(ValueLayout.JAVA_FLOAT, (long) m * n * 2);
                    MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_FLOAT, 1.0f, 0.0f);
                    MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_FLOAT, 0.0f, 0.0f);
                    NativeSafe.invoke(CGEMM_HANDLE, CblasRowMajor, CblasNoTrans, CblasNoTrans, m, n, k, alpha, aSeg, k, bSeg, n, beta, cSeg, n);
                    float[] result = cSeg.toArray(ValueLayout.JAVA_FLOAT);
                    org.episteme.core.mathematics.numbers.complex.Complex[] resData = new org.episteme.core.mathematics.numbers.complex.Complex[m * n];
                    for (int i=0; i<m*n; i++) resData[i] = org.episteme.core.mathematics.numbers.complex.Complex.of(org.episteme.core.mathematics.numbers.real.RealFloat.of(result[2*i]), org.episteme.core.mathematics.numbers.real.RealFloat.of(result[2*i+1]));
                    E[] castedRes = (E[]) resData;
                    return new GenericMatrix<>(new DenseMatrixStorage<>(m, n, castedRes), this, ring);
                }
            }
            if (!AVAILABLE || ZGEMM_HANDLE == null) return LinearAlgebraProvider.super.multiply(a, b);
            int m = a.rows();
            int k = a.cols();
            int n = b.cols();
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray((Matrix<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)a));
                MemorySegment bSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray((Matrix<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)b));
                MemorySegment cSeg = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n * 2);
                MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0, 0.0);
                MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0, 0.0);
                NativeSafe.invoke(ZGEMM_HANDLE, CblasRowMajor, CblasNoTrans, CblasNoTrans, m, n, k, alpha, aSeg, k, bSeg, n, beta, cSeg, n);
                double[] result = cSeg.toArray(ValueLayout.JAVA_DOUBLE);
                org.episteme.core.mathematics.numbers.complex.Complex[] resData = new org.episteme.core.mathematics.numbers.complex.Complex[m * n];
                for (int i=0; i<m*n; i++) resData[i] = org.episteme.core.mathematics.numbers.complex.Complex.of(result[2*i], result[2*i+1]);
                Matrix<E> typedRes = (Matrix<E>)(Object) new GenericMatrix<>(new DenseMatrixStorage<>(m, n, (org.episteme.core.mathematics.numbers.complex.Complex[])resData), (LinearAlgebraProvider<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)this, (Ring<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)org.episteme.core.mathematics.sets.Complexes.getInstance());
                return typedRes;
            }
        }
        return LinearAlgebraProvider.super.multiply(a, b);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> inverse(Matrix<E> a) {
        Ring<E> ring = a.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            if (!AVAILABLE) throw new UnsupportedOperationException(getName() + ": inverse() not available");
            int m = a.rows();
            int n = a.cols();
            if (m != n) {
                return pseudoInverse(a);
            }
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray((Matrix<Real>)(Object)a));
                MemorySegment ipiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
                
                int info = (int) NativeSafe.invoke(DGETRF_HANDLE, LAPACK_ROW_MAJOR, n, n, aSeg, n, ipiv);
                if (info != 0) throw new ArithmeticException("Matrix is singular or dgetrf failed: " + info);
                
                info = (int) NativeSafe.invoke(DGETRI_HANDLE, LAPACK_ROW_MAJOR, n, aSeg, n, ipiv);
                if (info != 0) throw new ArithmeticException("dgetri failed: " + info);
                
                double[] invData = aSeg.toArray(ValueLayout.JAVA_DOUBLE);
                return (Matrix<E>)(Object) RealDoubleMatrix.of(invData, n, n);
            }
        }
        return LinearAlgebraProvider.super.inverse(a);
    }

    @SuppressWarnings("unchecked")
    private Matrix<E> pseudoInverse(Matrix<E> a) {
        org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<E> svd = svd(a);
        int m = a.rows();
        int n = a.cols();
        int k = svd.S().dimension();
        
        RealDoubleMatrix sInv = RealDoubleMatrix.direct(n, m);
        for (int i = 0; i < k; i++) {
            double sVal = ((org.episteme.core.mathematics.numbers.real.Real)svd.S().get(i)).doubleValue();
            if (sVal > 1e-12) {
                sInv.set(i, i, Real.of(1.0 / sVal));
            }
        }
        
        Matrix<E> vSInv = multiply(svd.V(), (Matrix<E>)(Object)sInv);
        Matrix<E> uT = transpose(svd.U());
        return multiply(vSInv, uT);
    }





    // Other methods default to UnsupportedOperationException
    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> transpose(Matrix<E> a) {
        Ring<E> ring = a.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            int m = a.rows();
            int n = a.cols();
            double[] data = toDoubleArray((Matrix<Real>)(Object)a);
            double[] res = new double[n * m];
            
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
            return (Matrix<E>)(Object) RealDoubleMatrix.of(res, n, m);
        }
        return LinearAlgebraProvider.super.transpose(a);
    }


    @Override
    @SuppressWarnings("unchecked")
    public E determinant(Matrix<E> a) {
        Ring<E> ring = a.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            if (!AVAILABLE || DGETRF_HANDLE == null || a.rows() != a.cols()) throw new UnsupportedOperationException(getName() + ": determinant not available");

            int n = a.rows();
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray((Matrix<Real>)(Object)a));
                MemorySegment ipiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
                
                int info = (int) NativeSafe.invoke(DGETRF_HANDLE, LAPACK_ROW_MAJOR, n, n, aSeg, n, ipiv);
                if (info < 0) throw new IllegalArgumentException("Illegal argument to dgetrf: " + info);
                if (info > 0) return ring.zero(); // Singular matrix
                
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
                return (E) Real.of(det);
            }
        }
        return LinearAlgebraProvider.super.determinant(a);
    }

    @Override
    @SuppressWarnings("unchecked")
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<E> lu(Matrix<E> a) {
        Ring<E> ring = a.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            if (AVAILABLE && a.rows() == a.cols()) {
                int n = a.rows();
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray((Matrix<Real>)(Object)a));
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
                        (Matrix<E>)(Object) RealDoubleMatrix.of(lData, n, n),
                        (Matrix<E>)(Object) RealDoubleMatrix.of(uData, n, n),
                        (Vector<E>)(Object) RealDoubleVector.of(pData)
                    );
                }
            }
        }
        return LinearAlgebraProvider.super.lu(a);
    }

    @Override
    @SuppressWarnings("unchecked")
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<E> eigen(Matrix<E> a) {
        Ring<E> ring = a.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            if (AVAILABLE && a.rows() == a.cols()) {
                int n = a.rows();
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray((Matrix<Real>)(Object)a));
                    MemorySegment wSeg = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n);
                    
                    int info = (int) NativeSafe.invoke(DSYEV_HANDLE, LAPACK_ROW_MAJOR, (byte) 'V', (byte) 'U', n, aSeg, n, wSeg);
                    if (info < 0) throw new IllegalArgumentException("Illegal argument to dsyev: " + info);
                    if (info > 0) throw new ArithmeticException("Eigenvalue decomposition failed to converge");
                    
                    double[] wData = wSeg.toArray(ValueLayout.JAVA_DOUBLE);
                    double[] evData = aSeg.toArray(ValueLayout.JAVA_DOUBLE);
                    
                    return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<>(
                        (Matrix<E>)(Object) RealDoubleMatrix.of(evData, n, n),
                        (Vector<E>)(Object) RealDoubleVector.of(wData)
                    );
                }
            }
        }
        return LinearAlgebraProvider.super.eigen(a);
    }





    @Override
    @SuppressWarnings("unchecked")
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<E> cholesky(Matrix<E> a) {
        Ring<E> ring = a.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            if (AVAILABLE && a.rows() == a.cols()) {
                int n = a.rows();
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray((Matrix<Real>)(Object)a));
                    
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
                        (Matrix<E>)(Object) RealDoubleMatrix.of(data, n, n)
                    );
                }
            }
        }
        return LinearAlgebraProvider.super.cholesky(a);
    }
    @Override
    @SuppressWarnings("unchecked")
    public E dot(Vector<E> a, Vector<E> b) {
        if (!AVAILABLE) return LinearAlgebraProvider.super.dot(a, b);
        if (a.dimension() != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");
        
        Ring<E> ring = a.getScalarRing();
        int n = a.dimension();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            E zero = ring.zero();
            if (zero instanceof org.episteme.core.mathematics.numbers.real.RealFloat) {
                if (SDOT_HANDLE == null) return LinearAlgebraProvider.super.dot(a, b);
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray((Vector<org.episteme.core.mathematics.numbers.real.Real>)(Object)a));
                    MemorySegment bSeg = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray((Vector<org.episteme.core.mathematics.numbers.real.Real>)(Object)b));
                    return (E) org.episteme.core.mathematics.numbers.real.RealFloat.of((float) NativeSafe.invoke(SDOT_HANDLE, n, aSeg, 1, bSeg, 1));
                }
            }
            if (DDOT_HANDLE == null) return LinearAlgebraProvider.super.dot(a, b);
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray((Vector<org.episteme.core.mathematics.numbers.real.Real>)(Object)a));
                MemorySegment bSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray((Vector<org.episteme.core.mathematics.numbers.real.Real>)(Object)b));
                return (E) org.episteme.core.mathematics.numbers.real.Real.of((double) NativeSafe.invoke(DDOT_HANDLE, n, aSeg, 1, bSeg, 1));
            }
        } else if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
            E zero = ring.zero();
            if (zero instanceof org.episteme.core.mathematics.numbers.complex.Complex && ((org.episteme.core.mathematics.numbers.complex.Complex)zero).getReal() instanceof org.episteme.core.mathematics.numbers.real.RealFloat) {
                if (CDOTU_HANDLE == null) return LinearAlgebraProvider.super.dot(a, b);
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toComplexFloatArray((Vector<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)a));
                    MemorySegment bSeg = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toComplexFloatArray((Vector<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)b));
                    MemorySegment resSeg = arena.allocate(ValueLayout.JAVA_FLOAT, 2);
                    NativeSafe.invoke(CDOTU_HANDLE, resSeg, n, aSeg, 1, bSeg, 1);
                    float[] res = resSeg.toArray(ValueLayout.JAVA_FLOAT);
                    return (E) org.episteme.core.mathematics.numbers.complex.Complex.of(org.episteme.core.mathematics.numbers.real.RealFloat.of(res[0]), org.episteme.core.mathematics.numbers.real.RealFloat.of(res[1]));
                }
            }
            if (ZDOTU_HANDLE == null) return LinearAlgebraProvider.super.dot(a, b);
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray((Vector<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)a));
                MemorySegment bSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray((Vector<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)b));
                MemorySegment resSeg = arena.allocate(ValueLayout.JAVA_DOUBLE, 2);
                NativeSafe.invoke(ZDOTU_HANDLE, resSeg, n, aSeg, 1, bSeg, 1);
                double[] res = resSeg.toArray(ValueLayout.JAVA_DOUBLE);
                return (E) org.episteme.core.mathematics.numbers.complex.Complex.of(res[0], res[1]);
            }
        }
        return LinearAlgebraProvider.super.dot(a, b);
    }

    @Override
    @SuppressWarnings("unchecked")
    public E norm(Vector<E> a) {
        if (!AVAILABLE) return (E) LinearAlgebraProvider.super.norm(a);
        Ring<E> ring = a.getScalarRing();
        int n = a.dimension();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            E zero = ring.zero();
            if (zero instanceof org.episteme.core.mathematics.numbers.real.RealFloat) {
                if (SNRM2_HANDLE != null) {
                    try (Arena arena = Arena.ofConfined()) {
                        MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray((Vector<org.episteme.core.mathematics.numbers.real.Real>)(Object)a));
                        return (E) org.episteme.core.mathematics.numbers.real.RealFloat.of((float) NativeSafe.invoke(SNRM2_HANDLE, n, aSeg, 1));
                    }
                }
            }
            if (DNRM2_HANDLE != null) {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray((Vector<org.episteme.core.mathematics.numbers.real.Real>)(Object)a));
                    return (E) org.episteme.core.mathematics.numbers.real.Real.of((double) NativeSafe.invoke(DNRM2_HANDLE, n, aSeg, 1));
                }
            }
        } else if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
            E zero = ring.zero();
            if (zero instanceof org.episteme.core.mathematics.numbers.complex.Complex && ((org.episteme.core.mathematics.numbers.complex.Complex)zero).getReal() instanceof org.episteme.core.mathematics.numbers.real.RealFloat) {
                if (CNRM2_HANDLE != null) {
                    try (Arena arena = Arena.ofConfined()) {
                        MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toComplexFloatArray((Vector<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)a));
                        return (E) org.episteme.core.mathematics.numbers.real.RealFloat.of((float) NativeSafe.invoke(CNRM2_HANDLE, n, aSeg, 1));
                    }
                }
            }
            if (ZNRM2_HANDLE != null) {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray((Vector<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)a));
                    return (E) org.episteme.core.mathematics.numbers.real.Real.of((double) NativeSafe.invoke(ZNRM2_HANDLE, n, aSeg, 1));
                }
            }
        }
        
        // Manual Fallback
        double sumSq = 0.0;
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            for (int i=0; i<n; i++) { double d = ((org.episteme.core.mathematics.numbers.real.Real)a.get(i)).doubleValue(); sumSq += d * d; }
        } else {
            for (int i=0; i<n; i++) { Complex c = (Complex)a.get(i); sumSq += c.real()*c.real() + c.imaginary()*c.imaginary(); }
        }
        return (E) org.episteme.core.mathematics.numbers.real.Real.of(Math.sqrt(sumSq));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> normalize(Vector<E> a) {
        org.episteme.core.mathematics.numbers.real.Real n = (org.episteme.core.mathematics.numbers.real.Real) norm(a);
        if (n.isZero()) return a;
        return multiply(a, (E) n.inverse());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        if (!AVAILABLE) return LinearAlgebraProvider.super.multiply(vector, scalar);
        int dim = vector.dimension();
        Ring<E> ring = vector.getScalarRing();
        
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            E zero = ring.zero();
            org.episteme.core.mathematics.numbers.real.Real sVal = (org.episteme.core.mathematics.numbers.real.Real)scalar;
            float sFloat = sVal.floatValue();
            double sDouble = sVal.doubleValue();
            
            if (zero instanceof org.episteme.core.mathematics.numbers.real.RealFloat) {
                if (SSCAL_HANDLE != null) {
                    try (Arena arena = Arena.ofConfined()) {
                        MemorySegment vSeg = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray((Vector<org.episteme.core.mathematics.numbers.real.Real>)(Object)vector));
                        NativeSafe.invoke(SSCAL_HANDLE, dim, sFloat, vSeg, 1);
                        float[] res = vSeg.toArray(ValueLayout.JAVA_FLOAT);
                        org.episteme.core.mathematics.numbers.real.Real[] resData = new org.episteme.core.mathematics.numbers.real.Real[dim];
                        for (int i=0; i<dim; i++) resData[i] = org.episteme.core.mathematics.numbers.real.RealFloat.of(res[i]);
                        return (Vector<E>)(Object) Vector.of(java.util.Arrays.asList(resData), org.episteme.core.mathematics.sets.Reals.getInstance());
                    }
                }
            } else {
                if (DSCAL_HANDLE != null) {
                    try (Arena arena = Arena.ofConfined()) {
                        MemorySegment vSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray((Vector<org.episteme.core.mathematics.numbers.real.Real>)(Object)vector));
                        NativeSafe.invoke(DSCAL_HANDLE, dim, sDouble, vSeg, 1);
                        double[] res = vSeg.toArray(ValueLayout.JAVA_DOUBLE);
                        return (Vector<E>)(Object) RealDoubleVector.of(res);
                    }
                }
            }
        } else if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
             E zero = ring.zero();
             org.episteme.core.mathematics.numbers.complex.Complex s = (org.episteme.core.mathematics.numbers.complex.Complex)scalar;
             if (zero instanceof org.episteme.core.mathematics.numbers.complex.Complex && ((org.episteme.core.mathematics.numbers.complex.Complex)zero).getReal() instanceof org.episteme.core.mathematics.numbers.real.RealFloat) {
                 if (CSCAL_HANDLE != null) {
                     try (Arena arena = Arena.ofConfined()) {
                         MemorySegment vSeg = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toComplexFloatArray((Vector<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)vector));
                         MemorySegment sSeg = arena.allocateFrom(ValueLayout.JAVA_FLOAT, (float) s.real(), (float) s.imaginary());
                         NativeSafe.invoke(CSCAL_HANDLE, dim, sSeg, vSeg, 1);
                         float[] res = vSeg.toArray(ValueLayout.JAVA_FLOAT);
                         org.episteme.core.mathematics.numbers.complex.Complex[] resData = new org.episteme.core.mathematics.numbers.complex.Complex[dim];
                         for (int i=0; i<dim; i++) resData[i] = org.episteme.core.mathematics.numbers.complex.Complex.of(org.episteme.core.mathematics.numbers.real.RealFloat.of(res[2*i]), org.episteme.core.mathematics.numbers.real.RealFloat.of(res[2*i+1]));
                         return (Vector<E>)(Object) Vector.of(java.util.Arrays.asList(resData), org.episteme.core.mathematics.sets.Complexes.getInstance());
                     }
                 }
             } else {
                 if (ZSCAL_HANDLE != null) {
                     try (Arena arena = Arena.ofConfined()) {
                         MemorySegment vSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray((Vector<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)vector));
                         MemorySegment sSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, s.real(), s.imaginary());
                         NativeSafe.invoke(ZSCAL_HANDLE, dim, sSeg, vSeg, 1);
                         double[] res = vSeg.toArray(ValueLayout.JAVA_DOUBLE);
                         org.episteme.core.mathematics.numbers.complex.Complex[] resData = new org.episteme.core.mathematics.numbers.complex.Complex[dim];
                         for (int i=0; i<dim; i++) resData[i] = org.episteme.core.mathematics.numbers.complex.Complex.of(res[2*i], res[2*i+1]);
                         return (Vector<E>)(Object) Vector.of(java.util.Arrays.asList(resData), org.episteme.core.mathematics.sets.Complexes.getInstance());
                     }
                 }
             }
        }
        return LinearAlgebraProvider.super.multiply(vector, scalar);
    }

    @Override
    @SuppressWarnings("unchecked")
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<E> qr(Matrix<E> a) {
        Ring<E> ring = a.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            if (!AVAILABLE || DGEQRF_HANDLE == null) throw new UnsupportedOperationException(getName() + ": QR not available");
            int m = a.rows();
            int n = a.cols();
            int k = Math.min(m, n);

            RealDoubleMatrix qMat = RealDoubleMatrix.direct(m, n); // Used temporarily to hold A
            qMat.getBuffer().put(toDoubleArray((Matrix<Real>)(Object)a));
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
                Matrix<E> R = (Matrix<E>)(Object) RealDoubleMatrix.of(rData, k, n);

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
                    Matrix<E> Q = (Matrix<E>)(Object) RealDoubleMatrix.of(qDataEconomy, m, k);

                    return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<>(Q, R);
                }
            }
            throw new ArithmeticException("Native QR failed with info: " + info);
        }
        return LinearAlgebraProvider.super.qr(a);
    }

    @Override
    @SuppressWarnings("unchecked")
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<E> svd(Matrix<E> a) {
        Ring<E> ring = a.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            if (AVAILABLE) {
                int m = a.rows();
                int n = a.cols();
                if (DGESVD_HANDLE == null) throw new UnsupportedOperationException("LAPACKE dgesvd not available");
                
                boolean transposed = false;
                Matrix<Real> workA = (Matrix<Real>)(Object)a;
                if (m < n) {
                    transposed = true;
                    workA = workA.transpose();
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

                Matrix<E> U = (Matrix<E>)(Object) RealDoubleMatrix.of(uArr, m, m);
                Vector<E> S = (Vector<E>)(Object) RealDoubleVector.of(sArr);
                Matrix<E> V = (Matrix<E>)(Object) RealDoubleMatrix.of(vArr, n, n);

                if (transposed) {
                    return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<>(V, S, U);
                }
                return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<>(U, S, V);
            }
            throw new UnsupportedOperationException(getName() + ": svd() not available");
        }
        return LinearAlgebraProvider.super.svd(a);
    }

    @Override
    public Vector<E> solve(LUResult<E> lu, Vector<E> b) {
        return org.episteme.core.mathematics.linearalgebra.matrices.solvers.GenericLU.solve(lu, b, (Field<E>)b.getScalarRing(), this);
    }

    @Override
    public Vector<E> solve(QRResult<E> qr, Vector<E> b) {
        return org.episteme.core.mathematics.linearalgebra.matrices.solvers.GenericQR.solve(qr, b, (Field<E>)b.getScalarRing(), this);
    }

    @Override
    public Vector<E> solve(CholeskyResult<E> cholesky, Vector<E> b) {
        return org.episteme.core.mathematics.linearalgebra.matrices.solvers.GenericCholesky.solve(cholesky, b, (Field<E>)b.getScalarRing(), this);
    }

}

