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
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.core.mathematics.sets.Complexes;
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
public abstract class AbstractNativeCPULinearAlgebraBackend<E> implements LinearAlgebraProvider<E>, NativeBackend, CPUBackend {

    private static final System.Logger logger = System.getLogger(AbstractNativeCPULinearAlgebraBackend.class.getName());

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
    private static final MethodHandle SGESV_HANDLE;
    private static final MethodHandle SGETRF_HANDLE;

    // Complex Double (Z)
    private static final MethodHandle ZGEMM_HANDLE;
    private static final MethodHandle ZGEMV_HANDLE;
    private static final MethodHandle ZDOTC_HANDLE;
    private static final MethodHandle ZNRM2_HANDLE;
    private static final MethodHandle ZSCAL_HANDLE;
    private static final MethodHandle ZGESV_HANDLE;
    private static final MethodHandle ZGETRF_HANDLE;
    private static final MethodHandle ZGETRI_HANDLE;
    private static final MethodHandle ZHEEV_HANDLE;
    private static final MethodHandle ZPOTRF_HANDLE;
    private static final MethodHandle ZGEQRF_HANDLE;
    private static final MethodHandle ZUNGQR_HANDLE;
    private static final MethodHandle ZGESVD_HANDLE;

    // Complex Float (C)
    private static final MethodHandle CGEMM_HANDLE;
    private static final MethodHandle CGEMV_HANDLE;
    private static final MethodHandle CDOTC_HANDLE;
    private static final MethodHandle CNRM2_HANDLE;
    private static final MethodHandle CSCAL_HANDLE;
    private static final MethodHandle CGESV_HANDLE;
    private static final MethodHandle CGETRF_HANDLE;
    private static final MethodHandle CGETRI_HANDLE;
    private static final MethodHandle CHEEV_HANDLE;
    private static final MethodHandle CPOTRF_HANDLE;
    private static final MethodHandle CGEQRF_HANDLE;
    private static final MethodHandle CUNGQR_HANDLE;
    private static final MethodHandle CGESVD_HANDLE;
    
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

        MethodHandle sgemm = null, sgemv = null, sdot = null, snrm2 = null, sscal = null, sgesv = null, sgetrf = null;
        MethodHandle zgemm = null, zgemv = null, zdotc = null, znrm2 = null, zscal = null, zgesv = null, zgetrf = null, zgetri = null, zheev = null, zpotrf = null, zgeqrf = null, zungqr = null, zgesvd = null;
        MethodHandle cgemm = null, cgemv = null, cdotc = null, cnrm2 = null, cscal = null, cgesv = null, cgetrf = null, cgetri = null, cheev = null, cpotrf = null, cgeqrf = null, cungqr = null, cgesvd = null;
        
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
                zdotc = lookup.find("cblas_zdotc_sub").map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);
                znrm2 = lookup.find("cblas_dznrm2").map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
                zscal = lookup.find("cblas_zscal").map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);

                // --- Complex Float (C) ---
                cgemm = lookup.find("cblas_cgemm").map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(
                    ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                    ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
                cgemv = lookup.find("cblas_cgemv").map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(
                    ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
                cdotc = lookup.find("cblas_cdotc_sub").map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);
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
                
                // --- Single Precision (S) LAPACK ---
                sgesv = NativeFFMLoader.findSymbol(lookup, "LAPACKE_sgesv", "sgesv", "sgesv_", "lapack_sgesv")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
                sgetrf = NativeFFMLoader.findSymbol(lookup, "LAPACKE_sgetrf", "sgetrf", "sgetrf_", "lapack_sgetrf")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);

                // --- Complex Double (Z) LAPACK ---
                zgesv = NativeFFMLoader.findSymbol(lookup, "LAPACKE_zgesv", "zgesv", "zgesv_", "lapack_zgesv")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
                zgetrf = NativeFFMLoader.findSymbol(lookup, "LAPACKE_zgetrf", "zgetrf", "zgetrf_", "lapack_zgetrf")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);
                zgetri = NativeFFMLoader.findSymbol(lookup, "LAPACKE_zgetri", "zgetri", "zgetri_", "lapack_zgetri")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);
                zheev = NativeFFMLoader.findSymbol(lookup, "LAPACKE_zheev", "zheev", "zheev_", "lapack_zheev")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);
                zpotrf = NativeFFMLoader.findSymbol(lookup, "LAPACKE_zpotrf", "zpotrf", "zpotrf_", "lapack_zpotrf")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
                zgeqrf = NativeFFMLoader.findSymbol(lookup, "LAPACKE_zgeqrf", "zgeqrf", "zgeqrf_", "lapack_zgeqrf")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);
                zungqr = NativeFFMLoader.findSymbol(lookup, "LAPACKE_zungqr", "zungqr", "zungqr_", "lapack_zungqr")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);
                zgesvd = NativeFFMLoader.findSymbol(lookup, "LAPACKE_zgesvd", "zgesvd", "zgesvd_", "lapack_zgesvd")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);

                // --- Complex Float (C) LAPACK ---
                cgesv = NativeFFMLoader.findSymbol(lookup, "LAPACKE_cgesv", "cgesv", "cgesv_", "lapack_cgesv")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
                cgetrf = NativeFFMLoader.findSymbol(lookup, "LAPACKE_cgetrf", "cgetrf", "cgetrf_", "lapack_cgetrf")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);
                cgetri = NativeFFMLoader.findSymbol(lookup, "LAPACKE_cgetri", "cgetri", "cgetri_", "lapack_cgetri")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);
                cheev = NativeFFMLoader.findSymbol(lookup, "LAPACKE_cheev", "cheev", "cheev_", "lapack_cheev")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);
                cpotrf = NativeFFMLoader.findSymbol(lookup, "LAPACKE_cpotrf", "cpotrf", "cpotrf_", "lapack_cpotrf")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
                cgeqrf = NativeFFMLoader.findSymbol(lookup, "LAPACKE_cgeqrf", "cgeqrf", "cgeqrf_", "lapack_cgeqrf")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);
                cungqr = NativeFFMLoader.findSymbol(lookup, "LAPACKE_cungqr", "cungqr", "cungqr_", "lapack_cungqr")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);
                cgesvd = NativeFFMLoader.findSymbol(lookup, "LAPACKE_cgesvd", "cgesvd", "cgesvd_", "lapack_cgesvd")
                    .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);
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

        SGESV_HANDLE = sgesv;
        SGETRF_HANDLE = sgetrf;

        ZGEMM_HANDLE = zgemm;
        ZGEMV_HANDLE = zgemv;
        ZDOTC_HANDLE = zdotc;
        ZNRM2_HANDLE = znrm2;
        ZSCAL_HANDLE = zscal;
        ZGESV_HANDLE = zgesv;
        ZGETRF_HANDLE = zgetrf;
        ZGETRI_HANDLE = zgetri;
        ZHEEV_HANDLE = zheev;
        ZPOTRF_HANDLE = zpotrf;
        ZGEQRF_HANDLE = zgeqrf;
        ZUNGQR_HANDLE = zungqr;
        ZGESVD_HANDLE = zgesvd;

        CGEMM_HANDLE = cgemm;
        CGEMV_HANDLE = cgemv;
        CDOTC_HANDLE = cdotc;
        CNRM2_HANDLE = cnrm2;
        CSCAL_HANDLE = cscal;
        CGESV_HANDLE = cgesv;
        CGETRF_HANDLE = cgetrf;
        CGETRI_HANDLE = cgetri;
        CHEEV_HANDLE = cheev;
        CPOTRF_HANDLE = cpotrf;
        CGEQRF_HANDLE = cgeqrf;
        CUNGQR_HANDLE = cungqr;
        CGESVD_HANDLE = cgesvd;
        
        // Broadened availability check
        AVAILABLE = avail;

        if (AVAILABLE) {
            logger.log(System.Logger.Level.INFO, "Native CPU-BLAS Backend initialized. SVD support: {0}, Eigen support: {1}, LAPACK support: {2}", 
                DGESVD_HANDLE != null, DSYEV_HANDLE != null, DGESV_HANDLE != null);
        } else {
            logger.log(System.Logger.Level.WARNING, "Native CPU-BLAS Backend NOT initialized (missing core BLAS symbols)");
        }
    }

    protected final Ring<E> ring;

    protected AbstractNativeCPULinearAlgebraBackend(Ring<E> ring) {
        this.ring = ring;
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
        return "native-cpu-" + (ring instanceof Complexes ? "complex" : "real");
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
        return "Native CPU-BLAS (" + (ring instanceof Complexes ? "Complex" : "Real") + ")";
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
        
        boolean res = false;
        // Strict domain matching
        if (this.ring instanceof Reals) {
            res = ring instanceof Reals || ring.zero() instanceof org.episteme.core.mathematics.numbers.real.Real;
        } else if (this.ring instanceof Complexes) {
            res = ring instanceof Complexes || ring.zero() instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        }
        
        System.out.println(String.format("[DEBUG] Backend %s isCompatible(%s)? %b (this.ring=%s)", 
            getName(), ring.getClass().getSimpleName(), res, this.ring.getClass().getSimpleName()));
        return res;
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

    public int zgeqrf(int m, int n, DoubleBuffer A, int lda, DoubleBuffer tau) {
        if (!AVAILABLE || ZGEQRF_HANDLE == null) throw new UnsupportedOperationException("LAPACK zgeqrf not available");
        return (int) NativeSafe.invoke(ZGEQRF_HANDLE, LAPACK_ROW_MAJOR, m, n, MemorySegment.ofBuffer(A), n, MemorySegment.ofBuffer(tau));
    }

    public int zungqr(int m, int n, int k, DoubleBuffer A, int lda, DoubleBuffer tau) {
        if (!AVAILABLE || ZUNGQR_HANDLE == null) throw new UnsupportedOperationException("LAPACK zungqr not available");
        return (int) NativeSafe.invoke(ZUNGQR_HANDLE, LAPACK_ROW_MAJOR, m, n, k, MemorySegment.ofBuffer(A), lda, MemorySegment.ofBuffer(tau));
    }

    public int zgesvd(byte jobu, byte jobvt, int m, int n, DoubleBuffer A, int lda, DoubleBuffer S, DoubleBuffer U, int ldu, DoubleBuffer VT, int ldvt, DoubleBuffer superb) {
        if (!AVAILABLE || ZGESVD_HANDLE == null) throw new UnsupportedOperationException("LAPACK zgesvd not available");
        return (int) NativeSafe.invoke(ZGESVD_HANDLE, LAPACK_ROW_MAJOR, jobu, jobvt, m, n, MemorySegment.ofBuffer(A), lda, MemorySegment.ofBuffer(S), MemorySegment.ofBuffer(U), ldu, MemorySegment.ofBuffer(VT), ldvt, MemorySegment.ofBuffer(superb));
    }

    public int dgels(char trans, int m, int n, int nrhs, DoubleBuffer A, int lda, DoubleBuffer B, int ldb) {
        if (!AVAILABLE || DGELS_HANDLE == null) throw new UnsupportedOperationException("LAPACK dgels not available");
        return (int) NativeSafe.invoke(DGELS_HANDLE, LAPACK_ROW_MAJOR, (byte) trans, m, n, nrhs, MemorySegment.ofBuffer(A), lda, MemorySegment.ofBuffer(B), ldb);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (!AVAILABLE) throw new UnsupportedOperationException("Native library not available");
        
        Ring<E> ring = a.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            E zero = ring.zero();
            if (zero instanceof org.episteme.core.mathematics.numbers.real.RealFloat) {
                @SuppressWarnings("unchecked")
                Vector<E> res = (Vector<E>) multiplyRealFloat((Matrix<org.episteme.core.mathematics.numbers.real.Real>)(Object)a, (Vector<org.episteme.core.mathematics.numbers.real.Real>)(Object)b);
                return res;
            }
            @SuppressWarnings("unchecked")
            Vector<E> res = (Vector<E>) (Object) multiplyReal((Matrix<Real>)(Object)a, (Vector<Real>)(Object)b);
            return res;
        } else if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
            E zero = ring.zero();
            if (zero instanceof org.episteme.core.mathematics.numbers.complex.Complex && ((org.episteme.core.mathematics.numbers.complex.Complex)zero).getReal() instanceof org.episteme.core.mathematics.numbers.real.RealFloat) {
                @SuppressWarnings("unchecked")
                Vector<E> res = (Vector<E>) multiplyComplexFloat((Matrix<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)a, (Vector<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)b);
                return res;
            }
            @SuppressWarnings("unchecked")
            Vector<E> res = (Vector<E>) (Object) multiplyComplex((Matrix<Complex>)(Object)a, (Vector<Complex>)(Object)b);
            return res;
        }
        throw new UnsupportedOperationException("Unsupported ring type for NativeCPU multiply: " + ring.getClass().getName());
    }

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
        throw new UnsupportedOperationException("NativeCPU multiplyRealFloat failed or not available");
    }

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
        throw new UnsupportedOperationException("NativeCPU multiplyComplexFloat failed or not available");
    }

    private Vector<Real> multiplyReal(Matrix<Real> a, Vector<Real> b) {
        if (AVAILABLE && DGEMV_HANDLE != null) {
            int m = a.rows();
            int n = a.cols();
            
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment aSeg = getMemorySegment(a, arena);
                MemorySegment bSeg = getMemorySegment(b, arena);
                MemorySegment rSeg = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m);
                
                NativeSafe.invoke(DGEMV_HANDLE, CblasRowMajor, CblasNoTrans, m, n, 1.0, aSeg, n, bSeg, 1, 0.0, rSeg, 1);
                
                double[] result = rSeg.toArray(ValueLayout.JAVA_DOUBLE);
                @SuppressWarnings("unchecked")
                Vector<Real> res = (Vector<Real>)(Object) org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(result);
                return res;
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
        @SuppressWarnings("unchecked")
        Vector<Real> res = (Vector<Real>)(Object) org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(rd);
        return res;
    }

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
                return (org.episteme.core.mathematics.linearalgebra.Vector<E>)(Object) org.episteme.core.mathematics.linearalgebra.Vector.<org.episteme.core.mathematics.numbers.complex.Complex>of(java.util.Arrays.asList(complexRes), (org.episteme.core.mathematics.structures.rings.Ring<org.episteme.core.mathematics.numbers.complex.Complex>)(Object) org.episteme.core.mathematics.sets.Complexes.getInstance());
            }
        }
        throw new UnsupportedOperationException("NativeCPU multiplyComplex failed or not available");
    }

    private double[] toDoubleArray(Vector<Real> v) {
        if (v instanceof org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector) {
            return ((org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector) v).toDoubleArray();
        }
        double[] d = new double[v.dimension()];
        for (int i = 0; i < v.dimension(); i++) d[i] = v.get(i).doubleValue();
        return d;
    }

    private double[] toDoubleArray(Matrix<Real> m) {
        if (m instanceof RealDoubleMatrix) {
            RealDoubleMatrix rdm = (RealDoubleMatrix) m;
            double[] data = rdm.getDoubleStorage().getData();
            if (data != null) return data; // Direct access to underlying array
            return rdm.toDoubleArray();
        }
        double[] d = new double[m.rows() * m.cols()];
        for (int i = 0; i < m.rows(); i++) {
            for (int j = 0; j < m.cols(); j++) d[i * m.cols() + j] = m.get(i, j).doubleValue();
        }
        return d;
    }

    private MemorySegment getMemorySegment(Matrix<Real> m, Arena arena) {
        if (m instanceof RealDoubleMatrix) {
            RealDoubleMatrix rdm = (RealDoubleMatrix) m;
            if (rdm.isDirect()) {
                return MemorySegment.ofBuffer(rdm.getBuffer());
            }
            double[] data = rdm.getDoubleStorage().getData();
            if (data != null) {
                return arena.allocateFrom(ValueLayout.JAVA_DOUBLE, data);
            }
        }
        return arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(m));
    }

    private MemorySegment getMemorySegment(Vector<Real> v, Arena arena) {
        if (v instanceof org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector) {
            return arena.allocateFrom(ValueLayout.JAVA_DOUBLE, ((org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector) v).toDoubleArray());
        }
        return arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(v));
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
                E[] resData = (E[]) java.lang.reflect.Array.newInstance(zero.getClass(), a.rows() * a.cols());
                for (int i=0; i<resData.length; i++) resData[i] = (E) org.episteme.core.mathematics.numbers.real.RealFloat.of(rd[i]);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> result = new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<E>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<E>(a.rows(), a.cols(), resData), this, ring);
                return result;
            }
            double[] ad = toDoubleArray((Matrix<org.episteme.core.mathematics.numbers.real.Real>)(Object)a);
            double[] bd = toDoubleArray((Matrix<org.episteme.core.mathematics.numbers.real.Real>)(Object)b);
            double[] rd = new double[ad.length];
            for (int i = 0; i < ad.length; i++) rd[i] = ad[i] + bd[i];
            @SuppressWarnings("unchecked")
            Matrix<E> result = (Matrix<E>)(Object) RealDoubleMatrix.of(rd, a.rows(), a.cols());
            return result;
        } else if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
            E zero = ring.zero();
            if (zero instanceof org.episteme.core.mathematics.numbers.complex.Complex && ((org.episteme.core.mathematics.numbers.complex.Complex)zero).getReal() instanceof org.episteme.core.mathematics.numbers.real.RealFloat) {
                float[] ad = toComplexFloatArray((Matrix<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)a);
                float[] bd = toComplexFloatArray((Matrix<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)b);
                float[] rd = new float[ad.length];
                for (int i = 0; i < ad.length; i++) rd[i] = ad[i] + bd[i];
                org.episteme.core.mathematics.numbers.complex.Complex[] resData = new org.episteme.core.mathematics.numbers.complex.Complex[a.rows() * a.cols()];
                for (int i=0; i<resData.length; i++) resData[i] = org.episteme.core.mathematics.numbers.complex.Complex.of(org.episteme.core.mathematics.numbers.real.RealFloat.of(rd[2*i]), org.episteme.core.mathematics.numbers.real.RealFloat.of(rd[2*i+1]));
                org.episteme.core.mathematics.linearalgebra.Matrix<E> result = new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<E>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<E>(a.rows(), a.cols(), (E[])resData), this, ring);
                return result;
            }
            double[] ad = toComplexDoubleArray((Matrix<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)a);
            double[] bd = toComplexDoubleArray((Matrix<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)b);
            double[] rd = new double[ad.length];
            for (int i = 0; i < ad.length; i++) rd[i] = ad[i] + bd[i];
            E[] resData = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), a.rows() * a.cols());
            for (int i=0; i<resData.length; i++) resData[i] = (E) org.episteme.core.mathematics.numbers.complex.Complex.of(rd[2*i], rd[2*i+1]);
            org.episteme.core.mathematics.linearalgebra.Matrix<E> result = new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<E>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<E>(a.rows(), a.cols(), resData), this, ring);
            return result;
        }
        throw new UnsupportedOperationException("Unsupported ring type for NativeCPU add: " + ring.getClass().getName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        Ring<E> ring = a.getScalarRing();
        int rows = a.rows();
        int cols = a.cols();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            double[] ad = toDoubleArray((Matrix<Real>)(Object)a);
            double[] bd = toDoubleArray((Matrix<Real>)(Object)b);
            double[] rd = new double[ad.length];
            for (int i = 0; i < ad.length; i++) rd[i] = ad[i] - bd[i];
            @SuppressWarnings("unchecked")
            Matrix<E> result = (Matrix<E>)(Object) RealDoubleMatrix.of(rd, rows, cols);
            return result;
        } else if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
            double[] ad = toComplexDoubleArray((Matrix<Complex>)(Object)a);
            double[] bd = toComplexDoubleArray((Matrix<Complex>)(Object)b);
            double[] rd = new double[ad.length];
            for (int i = 0; i < ad.length; i++) rd[i] = ad[i] - bd[i];
            E[] resData = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), rows * cols);
            for (int i=0; i<rows*cols; i++) resData[i] = (E) Complex.of(rd[2*i], rd[2*i+1]);
            return new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<E>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<E>(rows, cols, resData), this, ring);
        }
        throw new UnsupportedOperationException("Unsupported ring type for NativeCPU subtract: " + ring.getClass().getName());
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
                @SuppressWarnings("unchecked")
                org.episteme.core.mathematics.linearalgebra.Matrix<E> result = new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<E>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<E>(a.rows(), a.cols(), (E[])resData), this, ring);
                return result;
            }
            double s = ((org.episteme.core.mathematics.numbers.real.Real)scalar).doubleValue();
            double[] ad = toDoubleArray((Matrix<org.episteme.core.mathematics.numbers.real.Real>)(Object)a);
            double[] rd = new double[ad.length];
            for (int i = 0; i < ad.length; i++) rd[i] = ad[i] * s;
            @SuppressWarnings("unchecked")
            Matrix<E> result = (Matrix<E>)(Object) RealDoubleMatrix.of(rd, a.rows(), a.cols());
            return result;
        } else if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
            org.episteme.core.mathematics.numbers.complex.Complex s;
            if (scalar instanceof org.episteme.core.mathematics.numbers.complex.Complex) {
                s = (org.episteme.core.mathematics.numbers.complex.Complex)scalar;
            } else {
                s = org.episteme.core.mathematics.numbers.complex.Complex.of(getRealValue(scalar));
            }
            
            E[] res = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), a.rows() * a.cols());
            for (int i=0; i<a.rows(); i++) {
                for (int j=0; j<a.cols(); j++) res[i*a.cols()+j] = (E) s.multiply(((Matrix<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)a).get(i, j));
            }
            org.episteme.core.mathematics.linearalgebra.Matrix<E> result = new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<E>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<E>(a.rows(), a.cols(), res), this, ring);
            return result;
        }
        throw new UnsupportedOperationException("Unsupported ring type for NativeCPU scale: " + ring.getClass().getName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (a.cols() != b.rows()) throw new IllegalArgumentException("Incompatible dimensions: " + a.cols() + " != " + b.rows());
        
        Ring<E> ring = a.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            E zero = ring.zero();
            if (zero instanceof org.episteme.core.mathematics.numbers.real.RealFloat) {
                if (!AVAILABLE || SGEMM_HANDLE == null) throw new UnsupportedOperationException("SGEMM not available");
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
                    @SuppressWarnings("unchecked")
                    org.episteme.core.mathematics.linearalgebra.Matrix<E> result = new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<E>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<E>(m, n, castedRes), this, ring);
                    return result;
                }
            }
            if (!AVAILABLE || DGEMM_HANDLE == null) throw new UnsupportedOperationException("DGEMM not available");
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
                if (!AVAILABLE || CGEMM_HANDLE == null) throw new UnsupportedOperationException("CGEMM not available");
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
                    return new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<E>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<E>(m, n, castedRes), this, ring);
                }
            }
            if (!AVAILABLE || ZGEMM_HANDLE == null) throw new UnsupportedOperationException("ZGEMM not available");
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
                org.episteme.core.mathematics.linearalgebra.Matrix<E> typedRes = (org.episteme.core.mathematics.linearalgebra.Matrix<E>)(Object) new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<org.episteme.core.mathematics.numbers.complex.Complex>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<org.episteme.core.mathematics.numbers.complex.Complex>(m, n, (org.episteme.core.mathematics.numbers.complex.Complex[])resData), (org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)this, (org.episteme.core.mathematics.structures.rings.Ring<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)org.episteme.core.mathematics.sets.Complexes.getInstance());
                return typedRes;
            }
        }
        throw new UnsupportedOperationException("Unsupported ring type for NativeCPU multiply: " + ring.getClass().getName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> inverse(Matrix<E> a) {
        Ring<E> ring = a.getScalarRing();
        int n = a.rows();
        int m = a.cols();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            if (!AVAILABLE) throw new UnsupportedOperationException(getName() + ": inverse() not available");
            if (n != m) return pseudoInverse(a);
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
        } else if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
            if (ZGETRF_HANDLE == null || ZGETRI_HANDLE == null) throw new UnsupportedOperationException("ZGETRF/ZGETRI not available");
            if (n != m) return pseudoInverse(a);
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray((Matrix<Complex>)(Object)a));
                MemorySegment ipiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
                
                int info = (int) NativeSafe.invoke(ZGETRF_HANDLE, LAPACK_ROW_MAJOR, n, n, aSeg, n, ipiv);
                if (info != 0) throw new ArithmeticException("Matrix is singular or zgetrf failed: " + info);
                
                info = (int) NativeSafe.invoke(ZGETRI_HANDLE, LAPACK_ROW_MAJOR, n, aSeg, n, ipiv);
                if (info != 0) throw new ArithmeticException("zgetri failed: " + info);
                
                double[] invData = aSeg.toArray(ValueLayout.JAVA_DOUBLE);
                Complex[] complexRes = new Complex[n * n];
                for (int i=0; i<n*n; i++) complexRes[i] = Complex.of(invData[2*i], invData[2*i+1]);
                return (org.episteme.core.mathematics.linearalgebra.Matrix<E>)(Object) new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<org.episteme.core.mathematics.numbers.complex.Complex>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<org.episteme.core.mathematics.numbers.complex.Complex>(n, n, complexRes), (org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)this, (org.episteme.core.mathematics.structures.rings.Ring<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)ring);
            }
        }
        throw new UnsupportedOperationException("NativeCPU inverse failed or not available");
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (a.rows() != a.cols()) throw new IllegalArgumentException("Matrix must be square for solve()");
        if (a.rows() != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");

        Ring<E> ring = a.getScalarRing();
        int n = a.rows();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            if (DGESV_HANDLE == null) throw new UnsupportedOperationException("DGESV not available");
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray((Matrix<Real>)(Object)a));
                MemorySegment bSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray((Vector<Real>)(Object)b));
                MemorySegment ipiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
                
                int info = (int) NativeSafe.invoke(DGESV_HANDLE, LAPACK_ROW_MAJOR, n, 1, aSeg, n, ipiv, bSeg, 1);
                if (info < 0) throw new IllegalArgumentException("DGESV: Illegal value at " + (-info));
                if (info > 0) throw new ArithmeticException("DGESV: Matrix is singular (U(" + info + "," + info + ") is zero)");
                
                double[] res = bSeg.toArray(ValueLayout.JAVA_DOUBLE);
                return (Vector<E>)(Object) RealDoubleVector.of(res);
            }
        } else if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
            if (ZGESV_HANDLE == null) throw new UnsupportedOperationException("ZGESV not available");
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray((Matrix<Complex>)(Object)a));
                MemorySegment bSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray((Vector<Complex>)(Object)b));
                MemorySegment ipiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
                
                int info = (int) NativeSafe.invoke(ZGESV_HANDLE, LAPACK_ROW_MAJOR, n, 1, aSeg, n, ipiv, bSeg, 1);
                if (info < 0) throw new IllegalArgumentException("ZGESV: Illegal value at " + (-info));
                if (info > 0) throw new ArithmeticException("ZGESV: Matrix is singular");
                
                double[] res = bSeg.toArray(ValueLayout.JAVA_DOUBLE);
                Complex[] complexRes = new Complex[n];
                for (int i=0; i<n; i++) complexRes[i] = Complex.of(res[2*i], res[2*i+1]);
                return (org.episteme.core.mathematics.linearalgebra.Vector<E>)(Object) org.episteme.core.mathematics.linearalgebra.Vector.<org.episteme.core.mathematics.numbers.complex.Complex>of(java.util.Arrays.asList(complexRes), (org.episteme.core.mathematics.structures.rings.Ring<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)ring);
            }
        }
        throw new UnsupportedOperationException("Unsupported ring for NativeCPU solve: " + ring.getClass().getName());
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
        int m = a.rows();
        int n = a.cols();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
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
        } else if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
            double[] data = toComplexDoubleArray((Matrix<Complex>)(Object)a);
            double[] resData = new double[2 * n * m];
            for (int i=0; i<m; i++) {
                for (int j=0; j<n; j++) {
                    resData[2*(j*m + i)] = data[2*(i*n + j)];
                    resData[2*(j*m + i) + 1] = data[2*(i*n + j) + 1];
                }
            }
            Complex[] complexRes = new Complex[n * m];
            for (int i=0; i<n*m; i++) complexRes[i] = Complex.of(resData[2*i], resData[2*i+1]);
            return (org.episteme.core.mathematics.linearalgebra.Matrix<E>)(Object) new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<org.episteme.core.mathematics.numbers.complex.Complex>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<org.episteme.core.mathematics.numbers.complex.Complex>(n, m, complexRes), (org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)this, (org.episteme.core.mathematics.structures.rings.Ring<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)ring);
        }
        throw new UnsupportedOperationException("NativeCPU transpose failed or not available for ring " + ring.getClass().getName());
    }


    @Override
    @SuppressWarnings("unchecked")
    public E determinant(Matrix<E> a) {
        Ring<E> ring = a.getScalarRing();
        int n = a.rows();
        if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray((Matrix<Real>)(Object)a));
                MemorySegment ipiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
                int info = (int) NativeSafe.invoke(DGETRF_HANDLE, LAPACK_ROW_MAJOR, n, n, aSeg, n, ipiv);
                if (info < 0) throw new IllegalArgumentException("DGETRF: Illegal value at " + (-info));
                if (info > 0) return ring.zero();
                
                double det = 1.0;
                double[] luData = aSeg.toArray(ValueLayout.JAVA_DOUBLE);
                for (int i = 0; i < n; i++) {
                    det *= luData[i * n + i];
                    if (ipiv.getAtIndex(ValueLayout.JAVA_INT, (long) i) != (i + 1)) det = -det;
                }
                return (E) Real.of(det);
            }
        } else if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
            if (ZGETRF_HANDLE == null) throw new UnsupportedOperationException("ZGETRF not available");
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray((Matrix<Complex>)(Object)a));
                MemorySegment ipiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
                int info = (int) NativeSafe.invoke(ZGETRF_HANDLE, LAPACK_ROW_MAJOR, n, n, aSeg, n, ipiv);
                if (info < 0) throw new IllegalArgumentException("ZGETRF: Illegal value at " + (-info));
                if (info > 0) return ring.zero();
                
                Complex det = Complex.ONE;
                double[] luData = aSeg.toArray(ValueLayout.JAVA_DOUBLE);
                for (int i = 0; i < n; i++) {
                    Complex diag = Complex.of(luData[2*(i * n + i)], luData[2*(i * n + i) + 1]);
                    det = det.multiply(diag);
                    if (ipiv.getAtIndex(ValueLayout.JAVA_INT, (long) i) != (i + 1)) det = det.negate();
                }
                return (E) det;
            }
        }
        throw new UnsupportedOperationException("NativeCPU determinant failed or not available");
    }

    @Override
    @SuppressWarnings("unchecked")
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<E> lu(Matrix<E> a) {
        Ring<E> ring = a.getScalarRing();
        int n = a.rows();
        int m = a.cols();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray((Matrix<Real>)(Object)a));
                MemorySegment ipiv = arena.allocate(ValueLayout.JAVA_INT, (long) Math.min(n, m));
                int info = (int) NativeSafe.invoke(DGETRF_HANDLE, LAPACK_ROW_MAJOR, n, m, aSeg, Math.max(n, m), ipiv);
                if (info < 0) throw new IllegalArgumentException("DGETRF failed: " + info);
                
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
                for (int i = 0; i < n; i++) pData[i] = i;
                for (int i = 0; i < Math.min(n, m); i++) {
                    int ip = ipiv.getAtIndex(ValueLayout.JAVA_INT, (long) i) - 1;
                    if (ip != i) {
                        double tmp = pData[i];
                        pData[i] = pData[ip];
                        pData[ip] = tmp;
                    }
                }
                
                return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<>(
                    (Matrix<E>)(Object) RealDoubleMatrix.of(lData, n, n),
                    (Matrix<E>)(Object) RealDoubleMatrix.of(uData, n, n),
                    (Vector<E>)(Object) RealDoubleVector.of(pData)
                );
            }
        } else if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
            if (ZGETRF_HANDLE == null) throw new UnsupportedOperationException("ZGETRF not available");
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray((Matrix<Complex>)(Object)a));
                MemorySegment ipiv = arena.allocate(ValueLayout.JAVA_INT, (long) Math.min(n, m));
                int info = (int) NativeSafe.invoke(ZGETRF_HANDLE, LAPACK_ROW_MAJOR, n, m, aSeg, Math.max(n, m), ipiv);
                if (info < 0) throw new IllegalArgumentException("ZGETRF failed: " + info);
                
                double[] luArr = aSeg.toArray(ValueLayout.JAVA_DOUBLE);
                Complex[] lData = new Complex[n * n];
                Complex[] uData = new Complex[n * n];
                
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        Complex val = Complex.of(luArr[2*(i * n + j)], luArr[2*(i * n + j) + 1]);
                        if (i > j) {
                            lData[i * n + j] = val;
                            uData[i * n + j] = Complex.ZERO;
                        } else if (i == j) {
                            lData[i * n + j] = Complex.ONE;
                            uData[i * n + j] = val;
                        } else {
                            lData[i * n + j] = Complex.ZERO;
                            uData[i * n + j] = val;
                        }
                    }
                }
                
                Complex[] pData = new Complex[n];
                for (int i = 0; i < n; i++) pData[i] = Complex.of(i);
                for (int i = 0; i < Math.min(n, m); i++) {
                    int ip = ipiv.getAtIndex(ValueLayout.JAVA_INT, (long) i) - 1;
                    if (ip != i) {
                        Complex tmp = pData[i];
                        pData[i] = pData[ip];
                        pData[ip] = tmp;
                    }
                }
                
                org.episteme.core.mathematics.linearalgebra.Matrix<E> L = (org.episteme.core.mathematics.linearalgebra.Matrix<E>)(Object) new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<org.episteme.core.mathematics.numbers.complex.Complex>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<org.episteme.core.mathematics.numbers.complex.Complex>(n, n, lData), (org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)this, (org.episteme.core.mathematics.structures.rings.Ring<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)ring);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> U = (org.episteme.core.mathematics.linearalgebra.Matrix<E>)(Object) new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<org.episteme.core.mathematics.numbers.complex.Complex>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<org.episteme.core.mathematics.numbers.complex.Complex>(n, n, uData), (org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)this, (org.episteme.core.mathematics.structures.rings.Ring<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)ring);
                org.episteme.core.mathematics.linearalgebra.Vector<E> P = (org.episteme.core.mathematics.linearalgebra.Vector<E>)(Object) org.episteme.core.mathematics.linearalgebra.Vector.<org.episteme.core.mathematics.numbers.complex.Complex>of(java.util.Arrays.asList(pData), (org.episteme.core.mathematics.structures.rings.Ring<org.episteme.core.mathematics.numbers.complex.Complex>)(Object) ring);

                return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<>(L, U, P);
            }
        }
        throw new UnsupportedOperationException("NativeCPU LU failed or not available");
    }

    @Override
    @SuppressWarnings("unchecked")
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<E> eigen(Matrix<E> a) {
        Ring<E> ring = a.getScalarRing();
        int n = a.rows();
        if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square");

        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray((Matrix<Real>)(Object)a));
                MemorySegment wSeg = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n);
                
                int info = (int) NativeSafe.invoke(DSYEV_HANDLE, LAPACK_ROW_MAJOR, (byte) 'V', (byte) 'U', n, aSeg, n, wSeg);
                if (info < 0) throw new IllegalArgumentException("DSYEV failed: " + info);
                if (info > 0) throw new ArithmeticException("Eigenvalue decomposition failed to converge");
                
                double[] wData = wSeg.toArray(ValueLayout.JAVA_DOUBLE);
                double[] vData = aSeg.toArray(ValueLayout.JAVA_DOUBLE);
                
                return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<>(
                    (Matrix<E>)(Object) RealDoubleMatrix.of(vData, n, n),
                    (Vector<E>)(Object) RealDoubleVector.of(wData)
                );
            }
        } else if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
            if (ZHEEV_HANDLE == null) throw new UnsupportedOperationException("ZHEEV not available");
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray((Matrix<Complex>)(Object)a));
                MemorySegment wSeg = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n);
                
                int info = (int) NativeSafe.invoke(ZHEEV_HANDLE, LAPACK_ROW_MAJOR, (byte) 'V', (byte) 'U', n, aSeg, n, wSeg);
                if (info < 0) throw new IllegalArgumentException("ZHEEV failed: " + info);
                if (info > 0) throw new ArithmeticException("ZHEEV failed to converge");
                
                double[] wData = wSeg.toArray(ValueLayout.JAVA_DOUBLE);
                double[] vArr = aSeg.toArray(ValueLayout.JAVA_DOUBLE);
                
                Complex[] complexV = new Complex[n * n];
                for (int i=0; i<n*n; i++) complexV[i] = Complex.of(vArr[2*i], vArr[2*i+1]);
                
                Complex[] complexW = new Complex[n];
                for (int i=0; i<n; i++) complexW[i] = Complex.of(wData[i]); // Eigenvalues of Hermitian are real
                
                org.episteme.core.mathematics.linearalgebra.Matrix<E> V = (org.episteme.core.mathematics.linearalgebra.Matrix<E>)(Object) new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<org.episteme.core.mathematics.numbers.complex.Complex>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<org.episteme.core.mathematics.numbers.complex.Complex>(n, n, complexV), (org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)this, (org.episteme.core.mathematics.structures.rings.Ring<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)ring);
                org.episteme.core.mathematics.linearalgebra.Vector<E> W = (org.episteme.core.mathematics.linearalgebra.Vector<E>)(Object) org.episteme.core.mathematics.linearalgebra.Vector.<org.episteme.core.mathematics.numbers.complex.Complex>of(java.util.Arrays.asList(complexW), (org.episteme.core.mathematics.structures.rings.Ring<org.episteme.core.mathematics.numbers.complex.Complex>)(Object) ring);

                return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<>(V, W);
            }
        }
        throw new UnsupportedOperationException("NativeCPU eigen failed or not available");
    }





    @Override
    @SuppressWarnings("unchecked")
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<E> cholesky(Matrix<E> a) {
        Ring<E> ring = a.getScalarRing();
        int n = a.rows();
        if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray((Matrix<Real>)(Object)a));
                int info = (int) NativeSafe.invoke(DPOTRF_HANDLE, LAPACK_ROW_MAJOR, (byte) 'L', n, aSeg, n);
                if (info < 0) throw new IllegalArgumentException("DPOTRF failed: " + info);
                if (info > 0) throw new ArithmeticException("Matrix is not positive definite (info=" + info + ")");
                
                double[] lArr = aSeg.toArray(ValueLayout.JAVA_DOUBLE);
                RealDoubleMatrix L = RealDoubleMatrix.direct(n, n);
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j <= i; j++) {
                        L.set(i, j, Real.of(lArr[i * n + j]));
                    }
                }
                return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<>((Matrix<E>)(Object)L);
            }
        } else if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
            if (ZPOTRF_HANDLE == null) throw new UnsupportedOperationException("ZPOTRF not available");
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray((Matrix<Complex>)(Object)a));
                int info = (int) NativeSafe.invoke(ZPOTRF_HANDLE, LAPACK_ROW_MAJOR, (byte) 'L', n, aSeg, n);
                if (info < 0) throw new IllegalArgumentException("ZPOTRF failed: " + info);
                if (info > 0) throw new ArithmeticException("Matrix is not positive definite");
                
                double[] lArr = aSeg.toArray(ValueLayout.JAVA_DOUBLE);
                Complex[] complexL = new Complex[n * n];
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j <= i; j++) {
                        complexL[i * n + j] = Complex.of(lArr[2*(i * n + j)], lArr[2*(i * n + j) + 1]);
                    }
                    for (int j = i + 1; j < n; j++) {
                        complexL[i * n + j] = Complex.ZERO;
                    }
                }
                org.episteme.core.mathematics.linearalgebra.Matrix<E> L = (org.episteme.core.mathematics.linearalgebra.Matrix<E>)(Object) new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<org.episteme.core.mathematics.numbers.complex.Complex>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<org.episteme.core.mathematics.numbers.complex.Complex>(n, n, complexL), (org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)this, (org.episteme.core.mathematics.structures.rings.Ring<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)ring);
                return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<>(L);
            }
        }
        throw new UnsupportedOperationException("NativeCPU cholesky failed or not available");
    }
    @Override
    @SuppressWarnings("unchecked")
    public E dot(Vector<E> a, Vector<E> b) {
        if (!AVAILABLE) throw new UnsupportedOperationException("Native library not available");
        if (a.dimension() != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");
        
        Ring<E> ring = a.getScalarRing();
        int n = a.dimension();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            E zero = ring.zero();
            if (zero instanceof org.episteme.core.mathematics.numbers.real.RealFloat) {
                if (SDOT_HANDLE == null) throw new UnsupportedOperationException("SDOT not available");
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray((Vector<org.episteme.core.mathematics.numbers.real.Real>)(Object)a));
                    MemorySegment bSeg = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray((Vector<org.episteme.core.mathematics.numbers.real.Real>)(Object)b));
                    return (E) org.episteme.core.mathematics.numbers.real.RealFloat.of((float) NativeSafe.invoke(SDOT_HANDLE, n, aSeg, 1, bSeg, 1));
                }
            }
            if (DDOT_HANDLE == null) throw new UnsupportedOperationException("DDOT not available");
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray((Vector<org.episteme.core.mathematics.numbers.real.Real>)(Object)a));
                MemorySegment bSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray((Vector<org.episteme.core.mathematics.numbers.real.Real>)(Object)b));
                return (E) org.episteme.core.mathematics.numbers.real.Real.of((double) NativeSafe.invoke(DDOT_HANDLE, n, aSeg, 1, bSeg, 1));
            }
        } else if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
            E zero = ring.zero();
            if (zero instanceof org.episteme.core.mathematics.numbers.complex.Complex && ((org.episteme.core.mathematics.numbers.complex.Complex)zero).getReal() instanceof org.episteme.core.mathematics.numbers.real.RealFloat) {
                if (CDOTC_HANDLE == null) throw new UnsupportedOperationException("CDOTC not available");
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toComplexFloatArray((Vector<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)a));
                    MemorySegment bSeg = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toComplexFloatArray((Vector<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)b));
                    MemorySegment resSeg = arena.allocate(ValueLayout.JAVA_FLOAT, 2);
                    NativeSafe.invoke(CDOTC_HANDLE, n, aSeg, 1, bSeg, 1, resSeg);
                    float[] res = resSeg.toArray(ValueLayout.JAVA_FLOAT);
                    return castScalar(org.episteme.core.mathematics.numbers.complex.Complex.of(org.episteme.core.mathematics.numbers.real.RealFloat.of(res[0]), org.episteme.core.mathematics.numbers.real.RealFloat.of(res[1])), ring);
                }
            }
            if (ZDOTC_HANDLE == null) throw new UnsupportedOperationException("ZDOTC not available");
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray((Vector<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)a));
                MemorySegment bSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray((Vector<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)b));
                MemorySegment resSeg = arena.allocate(ValueLayout.JAVA_DOUBLE, 2);
                NativeSafe.invoke(ZDOTC_HANDLE, n, aSeg, 1, bSeg, 1, resSeg);
                double[] res = resSeg.toArray(ValueLayout.JAVA_DOUBLE);
                return castScalar(org.episteme.core.mathematics.numbers.complex.Complex.of(res[0], res[1]), ring);
            }
        }
        throw new UnsupportedOperationException("Unsupported ring type for NativeCPU dot: " + ring.getClass().getName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public E norm(Vector<E> a) {
        if (!AVAILABLE) throw new UnsupportedOperationException("Native library not available");
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
                        return (E) org.episteme.core.mathematics.numbers.complex.Complex.of(org.episteme.core.mathematics.numbers.real.RealFloat.of((float) NativeSafe.invoke(CNRM2_HANDLE, n, aSeg, 1)), org.episteme.core.mathematics.numbers.real.RealFloat.ZERO);
                    }
                }
            }
            if (ZNRM2_HANDLE != null) {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment aSeg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray((Vector<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)a));
                    return (E) org.episteme.core.mathematics.numbers.complex.Complex.of((double) NativeSafe.invoke(ZNRM2_HANDLE, n, aSeg, 1), 0.0);
                }
            }
        }
        
        // Manual Fallback
        double sumSq = 0;
        for (int i = 0; i < n; i++) {
            E val = a.get(i);
            if (val instanceof Real r) sumSq += r.doubleValue() * r.doubleValue();
            else if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex c) sumSq += c.real()*c.real() + c.imaginary()*c.imaginary();
        }
        return createScalar(Math.sqrt(sumSq), a);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> normalize(Vector<E> v) {
        E n = norm(v);
        if (n == null) return v;
        
        Ring<E> ring = v.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.structures.rings.Field<E> field) {
            try {
                if (isZero(n, ring)) return v;
                return multiply(v, field.inverse(n));
            } catch (Exception e) {
                return v;
            }
        }
        
        double nv = getRealValue(n);
        if (nv == 0) return v;
        return multiply(v, createScalar(1.0 / nv, v));
    }

    @Override
    public Vector<E> cross(Vector<E> a, Vector<E> b) {
        if (a.dimension() != 3 || b.dimension() != 3) throw new IllegalArgumentException("Cross product only supported for 3D vectors");
        Ring<E> ring = a.getScalarRing();
        E a1 = a.get(0); E a2 = a.get(1); E a3 = a.get(2);
        E b1 = b.get(0); E b2 = b.get(1); E b3 = b.get(2);
        E c1 = ring.subtract(ring.multiply(a2, b3), ring.multiply(a3, b2));
        E c2 = ring.subtract(ring.multiply(a3, b1), ring.multiply(a1, b3));
        E c3 = ring.subtract(ring.multiply(a1, b2), ring.multiply(a2, b1));
        return (org.episteme.core.mathematics.linearalgebra.Vector<E>)(Object) org.episteme.core.mathematics.linearalgebra.Vector.<E>of(java.util.List.of(c1, c2, c3), ring);
    }

    @Override
    public E angle(Vector<E> a, Vector<E> b) {
        E d = dot(a, b);
        E nA = norm(a);
        E nB = norm(b);
        Ring<E> ring = a.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.structures.rings.Field<E> field) {
            try {
                E denom = field.multiply(nA, nB);
                if (isZero(denom, ring)) return ring.zero();
                E cosTheta = field.divide(d, denom);
                double cosVal = getRealValue(cosTheta);
                return createScalar(Math.acos(Math.max(-1.0, Math.min(1.0, cosVal))), a);
            } catch (Exception e) {
                return ring.zero();
            }
        }
        double dotVal = getRealValue(d);
        double nAVal = getRealValue(nA);
        double nBVal = getRealValue(nB);
        if (nAVal == 0 || nBVal == 0) return ring.zero();
        return createScalar(Math.acos(Math.max(-1.0, Math.min(1.0, dotVal / (nAVal * nBVal)))), a);
    }

    @Override
    public Vector<E> projection(Vector<E> a, Vector<E> b) {
        E dAB = dot(a, b);
        E dBB = dot(b, b);
        Ring<E> ring = a.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.structures.rings.Field<E> field) {
            try {
                if (isZero(dBB, ring)) return a;
                E factor = field.divide(dAB, dBB);
                return multiply(b, factor);
            } catch (Exception e) {
                return a;
            }
        }
        double dotAB = getRealValue(dAB);
        double dotBB = getRealValue(dBB);
        if (dotBB == 0) return a;
        return multiply(b, createScalar(dotAB / dotBB, a));
    }

    private E createScalar(double real, Object ref) {
        return createScalar(real, 0.0, ref);
    }

    private E createScalar(double real, double imag, Object ref) {
        if (isComplex(ref)) return (E) org.episteme.core.mathematics.numbers.complex.Complex.of(real, imag);
        if (isFloat(ref)) return (E) org.episteme.core.mathematics.numbers.real.RealFloat.of((float)real);
        return (E) Real.of(real);
    }

    private boolean isComplex(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Matrix<?> m) return isComplexRing(m.getScalarRing());
        if (obj instanceof Vector<?> v) return isComplexRing(v.getScalarRing());
        return obj.getClass().getName().equals("org.episteme.core.mathematics.numbers.complex.Complex");
    }

    private boolean isComplexRing(Ring<?> ring) {
        if (ring == null) return false;
        String name = ring.getClass().getName();
        return name.equals("org.episteme.core.mathematics.sets.Complexes") || name.endsWith(".Complexes");
    }

    private boolean isFloat(Object obj) {
        if (obj == null) return false;
        if (obj instanceof org.episteme.core.mathematics.numbers.real.RealFloat) return true;
        if (obj instanceof org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix) return false;
        if (obj instanceof org.episteme.nativ.mathematics.linearalgebra.matrices.NativeRealDoubleMatrix) return false;
        if (obj instanceof org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector) return false;

        if (obj instanceof Matrix<?> m) {
            if (m.rows() > 0 && m.cols() > 0) {
                try {
                    Object first = m.get(0, 0);
                    if (first instanceof org.episteme.core.mathematics.numbers.real.RealFloat) return true;
                    if (first instanceof org.episteme.core.mathematics.numbers.real.RealDouble) return false;
                    if (first instanceof org.episteme.core.mathematics.numbers.complex.Complex c) {
                        if (c.getReal() instanceof org.episteme.core.mathematics.numbers.real.RealFloat) return true;
                        if (c.getReal() instanceof org.episteme.core.mathematics.numbers.real.RealDouble) return false;
                    }
                } catch (Exception e) {}
            }
            return isFloatRing(m.getScalarRing());
        }
        if (obj instanceof Vector<?> v) {
            if (v.dimension() > 0) {
                try {
                    Object first = v.get(0);
                    if (first instanceof org.episteme.core.mathematics.numbers.real.RealFloat) return true;
                    if (first instanceof org.episteme.core.mathematics.numbers.real.RealDouble) return false;
                    if (first instanceof org.episteme.core.mathematics.numbers.complex.Complex c) {
                        if (c.getReal() instanceof org.episteme.core.mathematics.numbers.real.RealFloat) return true;
                        if (c.getReal() instanceof org.episteme.core.mathematics.numbers.real.RealDouble) return false;
                    }
                } catch (Exception e) {}
            }
            return isFloatRing(v.getScalarRing());
        }
        return false;
    }

    private boolean isFloatRing(Ring<?> ring) {
        if (ring == null) return false;
        Object zero = ring.zero();
        if (zero instanceof org.episteme.core.mathematics.numbers.real.RealFloat) return true;
        if (zero instanceof org.episteme.core.mathematics.numbers.real.RealDouble) return false;
        if (zero instanceof org.episteme.core.mathematics.numbers.complex.Complex c) {
            if (c.getReal() instanceof org.episteme.core.mathematics.numbers.real.RealFloat) return true;
            if (c.getReal() instanceof org.episteme.core.mathematics.numbers.real.RealDouble) return false;
        }
        
        // Fallback to MathContext for generic 'Reals' ring which doesn't specify precision in its zero()
        if (ring instanceof org.episteme.core.mathematics.sets.Reals || ring.getClass().getName().contains("Reals")) {
            return org.episteme.core.mathematics.context.MathContext.getCurrent().getRealPrecision() == org.episteme.core.mathematics.context.MathContext.RealPrecision.FAST;
        }
        if (ring instanceof org.episteme.core.mathematics.sets.Complexes || ring.getClass().getName().contains("Complexes")) {
            return org.episteme.core.mathematics.context.MathContext.getCurrent().getRealPrecision() == org.episteme.core.mathematics.context.MathContext.RealPrecision.FAST;
        }
        
        return false;
    }

    @SuppressWarnings("unchecked")
    private E castScalar(Object val, Ring<E> ring) {
        if (val == null) return ring.zero();
        String ringName = ring.getClass().getName();
        boolean isComplexRing = ringName.contains("Complexes");
        boolean isRealRing = ringName.contains("Reals");
        
        if (isComplexRing) {
            if (val instanceof Complex) return (E) val;
            if (val instanceof Real r) return (E) Complex.of(r);
            if (val instanceof Number n) return (E) Complex.of(n.doubleValue());
        }
        if (isRealRing) {
            if (val instanceof Real) return (E) val;
            if (val instanceof Complex c) return (E) c.getReal();
            if (val instanceof Number n) return (E) Real.of(n.doubleValue());
        }
        
        if (val instanceof Real r) {
            if (ring.zero() instanceof Double) return (E) Double.valueOf(r.doubleValue());
            if (ring.zero() instanceof Float) return (E) Float.valueOf(r.floatValue());
            return (E) r;
        }
        if (val instanceof Complex c) {
            if (ring.zero() instanceof Double) return (E) Double.valueOf(c.real());
            if (ring.zero() instanceof Float) return (E) Float.valueOf((float)c.real());
            return (E) c;
        }
        if (val instanceof Number n) {
            if (ring.zero() instanceof Double) return (E) Double.valueOf(n.doubleValue());
            if (ring.zero() instanceof Float) return (E) Float.valueOf(n.floatValue());
        }
        return (E) val;
    }

    private boolean isZero(Object obj, Ring<?> ring) {
        if (obj == null) return true;
        if (ring != null && ring.zero().equals(obj)) return true;
        
        if (obj instanceof org.episteme.core.mathematics.numbers.complex.Complex c) {
            return isZero(c.getReal(), null) && isZero(c.getImaginary(), null);
        }
        if (obj instanceof org.episteme.core.mathematics.numbers.real.Real r) {
            return r.isZero();
        }
        if (obj instanceof Number n) {
            return n.doubleValue() == 0.0;
        }
        return false;
    }

    private double getRealValue(Object obj) {
        if (obj == null) return 0.0;
        if (obj instanceof Number n) return n.doubleValue();
        if (obj instanceof org.episteme.core.mathematics.numbers.complex.Complex c) {
            return getRealValue(c.getReal());
        }
        if (obj instanceof org.episteme.core.mathematics.numbers.real.Real r) {
            return r.doubleValue();
        }
        return 0.0;
    }

    private Real getRealPart(Object val) {
        if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex c) return (Real) c.getReal();
        if (val instanceof Real r) return r;
        return Real.of(val.toString());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        if (!AVAILABLE) throw new UnsupportedOperationException("Native library not available");
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
                        return (org.episteme.core.mathematics.linearalgebra.Vector<E>)(Object) org.episteme.core.mathematics.linearalgebra.Vector.<org.episteme.core.mathematics.numbers.real.Real>of(java.util.Arrays.asList(resData), (org.episteme.core.mathematics.structures.rings.Ring<org.episteme.core.mathematics.numbers.real.Real>) org.episteme.core.mathematics.sets.Reals.getInstance());
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
             org.episteme.core.mathematics.numbers.complex.Complex s = (scalar instanceof org.episteme.core.mathematics.numbers.complex.Complex c) ? c : org.episteme.core.mathematics.numbers.complex.Complex.of(((Real)scalar).doubleValue());
             if (zero instanceof org.episteme.core.mathematics.numbers.complex.Complex && ((org.episteme.core.mathematics.numbers.complex.Complex)zero).getReal() instanceof org.episteme.core.mathematics.numbers.real.RealFloat) {
                 if (CSCAL_HANDLE != null) {
                     try (Arena arena = Arena.ofConfined()) {
                         MemorySegment vSeg = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toComplexFloatArray((Vector<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)vector));
                         MemorySegment sSeg = arena.allocateFrom(ValueLayout.JAVA_FLOAT, (float) s.real(), (float) s.imaginary());
                         NativeSafe.invoke(CSCAL_HANDLE, dim, sSeg, vSeg, 1);
                         float[] res = vSeg.toArray(ValueLayout.JAVA_FLOAT);
                         org.episteme.core.mathematics.numbers.complex.Complex[] resData = new org.episteme.core.mathematics.numbers.complex.Complex[dim];
                         for (int i=0; i<dim; i++) resData[i] = org.episteme.core.mathematics.numbers.complex.Complex.of(org.episteme.core.mathematics.numbers.real.RealFloat.of(res[2*i]), org.episteme.core.mathematics.numbers.real.RealFloat.of(res[2*i+1]));
                         return (org.episteme.core.mathematics.linearalgebra.Vector<E>)(Object) org.episteme.core.mathematics.linearalgebra.Vector.<org.episteme.core.mathematics.numbers.complex.Complex>of(java.util.Arrays.asList(resData), (org.episteme.core.mathematics.structures.rings.Ring<org.episteme.core.mathematics.numbers.complex.Complex>) org.episteme.core.mathematics.sets.Complexes.getInstance());
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
                         return (org.episteme.core.mathematics.linearalgebra.Vector<E>)(Object) org.episteme.core.mathematics.linearalgebra.Vector.<org.episteme.core.mathematics.numbers.complex.Complex>of(java.util.Arrays.asList(resData), (org.episteme.core.mathematics.structures.rings.Ring<org.episteme.core.mathematics.numbers.complex.Complex>) org.episteme.core.mathematics.sets.Complexes.getInstance());
                     }
                 }
             }
        }
        throw new UnsupportedOperationException("Unsupported ring type for NativeCPU scale: " + ring.getClass().getName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<E> qr(Matrix<E> a) {
        Ring<E> ring = a.getScalarRing();
        int m = a.rows();
        int n = a.cols();
        int k = Math.min(m, n);

        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            if (!AVAILABLE || DGEQRF_HANDLE == null) throw new UnsupportedOperationException(getName() + ": QR not available");

            RealDoubleMatrix qMat = RealDoubleMatrix.direct(m, n); 
            qMat.getBuffer().put(toDoubleArray((Matrix<Real>)(Object)a));
            qMat.getBuffer().position(0);

            DoubleBuffer tau = java.nio.ByteBuffer.allocateDirect(k * 8)
                .order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();

            int info = dgeqrf(m, n, qMat.getBuffer(), n, tau);
            if (info == 0) {
                double[] rData = new double[k * n];
                double[] aFactored = qMat.toDoubleArray();
                for (int i = 0; i < k; i++) {
                    for (int j = i; j < n; j++) {
                        rData[i * n + j] = aFactored[i * n + j];
                    }
                }
                Matrix<E> R = (Matrix<E>)(Object) RealDoubleMatrix.of(rData, k, n);

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
        } else if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
            if (ZGEQRF_HANDLE == null || ZUNGQR_HANDLE == null) throw new UnsupportedOperationException("ZGEQRF/ZUNGQR not available");
            
            double[] aData = toComplexDoubleArray((Matrix<Complex>)(Object)a);
            DoubleBuffer aBuf = java.nio.ByteBuffer.allocateDirect(m * n * 16).order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();
            aBuf.put(aData); aBuf.flip();

            DoubleBuffer tau = java.nio.ByteBuffer.allocateDirect(k * 16).order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();

            int info = zgeqrf(m, n, aBuf, n, tau);
            if (info == 0) {
                double[] rDataArr = new double[2 * k * n];
                aBuf.get(rDataArr);
                aBuf.flip();
                
                Complex[] complexR = new Complex[k * n];
                for (int i = 0; i < k; i++) {
                    for (int j = 0; j < n; j++) {
                        if (j >= i) {
                            complexR[i * n + j] = Complex.of(rDataArr[2*(i * n + j)], rDataArr[2*(i * n + j) + 1]);
                        } else {
                            complexR[i * n + j] = Complex.ZERO;
                        }
                    }
                }
                org.episteme.core.mathematics.linearalgebra.Matrix<E> R = (org.episteme.core.mathematics.linearalgebra.Matrix<E>)(Object) new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<org.episteme.core.mathematics.numbers.complex.Complex>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<org.episteme.core.mathematics.numbers.complex.Complex>(k, n, complexR), (org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)this, (org.episteme.core.mathematics.structures.rings.Ring<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)ring);

                info = zungqr(m, k, k, aBuf, n, tau);
                if (info == 0) {
                    double[] qDataArr = new double[2 * m * n];
                    aBuf.get(qDataArr);
                    
                    Complex[] complexQ = new Complex[m * k];
                    for (int i = 0; i < m; i++) {
                        for (int j = 0; j < k; j++) {
                            complexQ[i * k + j] = Complex.of(qDataArr[2*(i * n + j)], qDataArr[2*(i * n + j) + 1]);
                        }
                    }
                    org.episteme.core.mathematics.linearalgebra.Matrix<E> Q = (org.episteme.core.mathematics.linearalgebra.Matrix<E>)(Object) new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<org.episteme.core.mathematics.numbers.complex.Complex>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<org.episteme.core.mathematics.numbers.complex.Complex>(m, k, complexQ), (org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)this, (org.episteme.core.mathematics.structures.rings.Ring<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)ring);
                    return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<>(Q, R);
                }
            }
            throw new ArithmeticException("Native ZQR failed with info: " + info);
        }
        throw new UnsupportedOperationException("NativeCPU QR failed or not available");
    }

    @Override
    @SuppressWarnings("unchecked")
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<E> svd(Matrix<E> a) {
        Ring<E> ring = a.getScalarRing();
        int m = a.rows();
        int n = a.cols();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            if (AVAILABLE) {
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
        } else if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
            if (ZGESVD_HANDLE == null) throw new UnsupportedOperationException("ZGESVD not available");
            
            boolean transposed = false;
            Matrix<Complex> workA = (Matrix<Complex>)(Object)a;
            if (m < n) {
                transposed = true;
                workA = workA.transpose();
                int tmp = m; m = n; n = tmp;
            }
            int k = Math.min(m, n);
            double[] aData = toComplexDoubleArray(workA);
            
            DoubleBuffer aBuf = java.nio.ByteBuffer.allocateDirect(m * n * 16).order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();
            aBuf.put(aData); aBuf.flip();
            
            DoubleBuffer sBuf = java.nio.ByteBuffer.allocateDirect(k * 8).order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();
            DoubleBuffer uBuf = java.nio.ByteBuffer.allocateDirect(m * m * 16).order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();
            DoubleBuffer vtBuf = java.nio.ByteBuffer.allocateDirect(n * n * 16).order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();
            DoubleBuffer superb = java.nio.ByteBuffer.allocateDirect(Math.max(1, k - 1) * 8).order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();
            
            int info = zgesvd((byte)'A', (byte)'A', m, n, aBuf, n, sBuf, uBuf, m, vtBuf, n, superb);
            if (info != 0) throw new RuntimeException("zgesvd failed with info: " + info);
            
            double[] sArr = new double[k];
            sBuf.get(sArr);
            double[] uArr = new double[2 * m * m];
            uBuf.get(uArr);
            double[] vtArr = new double[2 * n * n];
            vtBuf.get(vtArr);
            
            Complex[] complexU = new Complex[m * m];
            for (int i=0; i<m*m; i++) complexU[i] = Complex.of(uArr[2*i], uArr[2*i+1]);
            
            Complex[] complexS = new Complex[k];
            for (int i=0; i<k; i++) complexS[i] = Complex.of(sArr[i]);
            
            // VT is ConjTransposed? LAPACK ZGESVD returns VT. V = VT^H
            Complex[] complexV = new Complex[n * n];
            for (int i=0; i<n; i++) {
                for (int j=0; j<n; j++) {
                    // V(i,j) = VT(j,i).conjugate()
                    complexV[i * n + j] = Complex.of(vtArr[2*(j * n + i)], -vtArr[2*(j * n + i) + 1]);
                }
            }
            
            org.episteme.core.mathematics.linearalgebra.Matrix<E> U = (org.episteme.core.mathematics.linearalgebra.Matrix<E>)(Object) new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<org.episteme.core.mathematics.numbers.complex.Complex>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<org.episteme.core.mathematics.numbers.complex.Complex>(m, m, complexU), (org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)this, (org.episteme.core.mathematics.structures.rings.Ring<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)ring);
            org.episteme.core.mathematics.linearalgebra.Vector<E> S = (org.episteme.core.mathematics.linearalgebra.Vector<E>)(Object) org.episteme.core.mathematics.linearalgebra.Vector.<org.episteme.core.mathematics.numbers.complex.Complex>of(java.util.Arrays.asList(complexS), (org.episteme.core.mathematics.structures.rings.Ring<org.episteme.core.mathematics.numbers.complex.Complex>)(Object) ring);
            org.episteme.core.mathematics.linearalgebra.Matrix<E> V = (org.episteme.core.mathematics.linearalgebra.Matrix<E>)(Object) new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<org.episteme.core.mathematics.numbers.complex.Complex>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<org.episteme.core.mathematics.numbers.complex.Complex>(n, n, complexV), (org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)this, (org.episteme.core.mathematics.structures.rings.Ring<org.episteme.core.mathematics.numbers.complex.Complex>)(Object)ring);

            if (transposed) {
                return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<>(V, S, U);
            }
            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<>(U, S, V);
        }
        throw new UnsupportedOperationException("NativeCPU SVD failed or not available");
    }

    @Override
    @SuppressWarnings("unchecked")
    public E trace(Matrix<E> a) {
        if (a.rows() != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        Ring<E> ring = a.getScalarRing();
        int n = a.rows();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            double sum = 0;
            for (int i = 0; i < n; i++) sum += ((Real) a.get(i, i)).doubleValue();
            return createScalar(sum, a);
        } else if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
            org.episteme.core.mathematics.numbers.complex.Complex sum = org.episteme.core.mathematics.numbers.complex.Complex.ZERO;
            for (int i = 0; i < n; i++) sum = sum.add((org.episteme.core.mathematics.numbers.complex.Complex) a.get(i, i));
            return createScalar(sum.real(), sum.imaginary(), (Object) a);
        }
        throw new UnsupportedOperationException("Unsupported ring type for NativeCPU trace: " + ring.getClass().getName());
    }

    // --- Transcendental Operations ---

    @Override public Matrix<E> exp(Matrix<E> m) { return applyFunc(m, Math::exp, org.episteme.core.mathematics.numbers.complex.Complex::exp); }
    @Override public Matrix<E> log(Matrix<E> m) { return applyFunc(m, Math::log, org.episteme.core.mathematics.numbers.complex.Complex::log); }
    @Override public Matrix<E> log10(Matrix<E> m) { return applyFunc(m, Math::log10, org.episteme.core.mathematics.numbers.complex.Complex::log10); }
    @Override public Matrix<E> sin(Matrix<E> m) { return applyFunc(m, Math::sin, org.episteme.core.mathematics.numbers.complex.Complex::sin); }
    @Override public Matrix<E> cos(Matrix<E> m) { return applyFunc(m, Math::cos, org.episteme.core.mathematics.numbers.complex.Complex::cos); }
    @Override public Matrix<E> tan(Matrix<E> m) { return applyFunc(m, Math::tan, org.episteme.core.mathematics.numbers.complex.Complex::tan); }
    @Override public Matrix<E> asin(Matrix<E> m) { return applyFunc(m, Math::asin, c -> { throw new UnsupportedOperationException("asin not supported for Complex"); }); }
    @Override public Matrix<E> acos(Matrix<E> m) { return applyFunc(m, Math::acos, c -> { throw new UnsupportedOperationException("acos not supported for Complex"); }); }
    @Override public Matrix<E> atan(Matrix<E> m) { return applyFunc(m, Math::atan, c -> { throw new UnsupportedOperationException("atan not supported for Complex"); }); }
    @Override public Matrix<E> sinh(Matrix<E> m) { return applyFunc(m, Math::sinh, org.episteme.core.mathematics.numbers.complex.Complex::sinh); }
    @Override public Matrix<E> cosh(Matrix<E> m) { return applyFunc(m, Math::cosh, org.episteme.core.mathematics.numbers.complex.Complex::cosh); }
    @Override public Matrix<E> tanh(Matrix<E> m) { return applyFunc(m, Math::tanh, org.episteme.core.mathematics.numbers.complex.Complex::tanh); }
    @Override public Matrix<E> asinh(Matrix<E> m) { return applyFunc(m, x -> Math.log(x + Math.sqrt(x * x + 1.0)), c -> { throw new UnsupportedOperationException("asinh not supported for Complex"); }); }
    @Override public Matrix<E> acosh(Matrix<E> m) { return applyFunc(m, x -> Math.log(x + Math.sqrt(x * x - 1.0)), c -> { throw new UnsupportedOperationException("acosh not supported for Complex"); }); }
    @Override public Matrix<E> atanh(Matrix<E> m) { return applyFunc(m, x -> 0.5 * Math.log((1.0 + x) / (1.0 - x)), c -> { throw new UnsupportedOperationException("atanh not supported for Complex"); }); }
    @Override public Matrix<E> sqrt(Matrix<E> m) { return applyFunc(m, Math::sqrt, org.episteme.core.mathematics.numbers.complex.Complex::sqrt); }
    @Override public Matrix<E> cbrt(Matrix<E> m) { return applyFunc(m, Math::cbrt, org.episteme.core.mathematics.numbers.complex.Complex::cbrt); }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> pow(Matrix<E> m, E exponent) {
        int rows = m.rows();
        int cols = m.cols();
        Ring<E> ring = m.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
            org.episteme.core.mathematics.numbers.complex.Complex exp = (org.episteme.core.mathematics.numbers.complex.Complex) exponent;
            org.episteme.core.mathematics.numbers.complex.Complex[][] res = new org.episteme.core.mathematics.numbers.complex.Complex[rows][cols];
            for (int i = 0; i < rows; i++) for (int j = 0; j < cols; j++) res[i][j] = ((org.episteme.core.mathematics.numbers.complex.Complex) m.get(i, j)).pow(exp);
            return (Matrix<E>) Matrix.of(res, (org.episteme.core.mathematics.structures.rings.Ring<org.episteme.core.mathematics.numbers.complex.Complex>) ring);
        } else {
            double exp = ((Real) exponent).doubleValue();
            double[] data = toDoubleArray((Matrix<Real>) m);
            for (int i = 0; i < data.length; i++) data[i] = Math.pow(data[i], exp);
            return (Matrix<E>) RealDoubleMatrix.of(data, rows, cols);
        }
    }

    @SuppressWarnings("unchecked")
    private Matrix<E> applyFunc(Matrix<E> m, java.util.function.DoubleUnaryOperator realOp, java.util.function.UnaryOperator<org.episteme.core.mathematics.numbers.complex.Complex> complexOp) {
        int rows = m.rows();
        int cols = m.cols();
        Ring<E> ring = m.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
            org.episteme.core.mathematics.numbers.complex.Complex[][] res = new org.episteme.core.mathematics.numbers.complex.Complex[rows][cols];
            for (int i = 0; i < rows; i++) for (int j = 0; j < cols; j++) res[i][j] = complexOp.apply((org.episteme.core.mathematics.numbers.complex.Complex) m.get(i, j));
            return (Matrix<E>) Matrix.of(res, (org.episteme.core.mathematics.structures.rings.Ring<org.episteme.core.mathematics.numbers.complex.Complex>) ring);
        } else {
            double[] data = toDoubleArray((Matrix<Real>) m);
            for (int i = 0; i < data.length; i++) data[i] = realOp.applyAsDouble(data[i]);
            return (Matrix<E>) RealDoubleMatrix.of(data, rows, cols);
        }
    }
}
