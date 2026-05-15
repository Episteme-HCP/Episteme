/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.episteme.nativ.mathematics.linearalgebra.backends;

import org.episteme.nativ.technical.backend.nativ.NativeSafe;

import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;
import org.episteme.core.mathematics.linearalgebra.vectors.DenseVector;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.technical.algorithm.AutoTuningManager;
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.nativ.technical.backend.nativ.NativeFFMLoader;
import com.google.auto.service.AutoService;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.nativ.technical.backend.nativ.ResourceTracker;
import org.episteme.nativ.technical.backend.nativ.NativeSegmentProxy;
import org.episteme.core.mathematics.sets.Complexes;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.nativ.mathematics.linearalgebra.matrices.storage.NativeRealDoubleMatrixStorage;
import org.episteme.nativ.mathematics.linearalgebra.vectors.storage.NativeRealDoubleVectorStorage;
import org.episteme.nativ.mathematics.linearalgebra.matrices.NativeRealDoubleMatrix;
import org.episteme.nativ.mathematics.linearalgebra.vectors.NativeRealDoubleVector;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.episteme.core.technical.algorithm.AlgorithmProvider;

/**
 * Abstract base for High-Performance Native BLAS Backend using Project Panama (FFM).
 * Binds to OpenBLAS/MKL for Matrix Operations.
 * 
 * @param <E> the element type
 */
public abstract class AbstractNativeFFMBLASBackend<E> implements LinearAlgebraProvider<E>, NativeBackend, CPUBackend {
    
    private static final Logger logger = LoggerFactory.getLogger(AbstractNativeFFMBLASBackend.class);

    private static SymbolLookup LOOKUP;
    private static SymbolLookup LAPACK_LOOKUP;
    private static boolean IS_AVAILABLE = false;
    private static boolean initAttempted = false;

    private static synchronized void ensureInitialized() {
        if (initAttempted) return;
        initAttempted = true;
        
        if (Boolean.getBoolean("episteme.native.disable") || 
            Boolean.getBoolean("episteme.native.skip.openblas") || 
            Boolean.getBoolean("episteme.backend.ffm-blas.disabled")) {
            logger.info("FFM: Skipping BLAS/LAPACK initialization as requested by system property.");
            return;
        }

        try {
            Arena arena = Arena.global();
            Optional<SymbolLookup> lib = NativeFFMLoader.loadLibrary("openblas", arena);
            if (lib.isEmpty()) {
                lib = NativeFFMLoader.loadLibrary("mkl_rt", arena);
            }
            if (lib.isEmpty()) {
                lib = NativeFFMLoader.loadLibrary("blas", arena);
            }
            
            if (lib.isPresent()) {
                logger.info("FFM: Successfully matched native library for FFM backend: {}", lib.get());
            } else {
                logger.info("FFM: No local BLAS/LAPACK library found. Attempting system lookup.");
                lib = NativeFFMLoader.getSystemLookup();
            }
            
            LOOKUP = lib.orElse(null);
            
            Optional<SymbolLookup> lapackLib = NativeFFMLoader.loadLibrary("lapacke", arena);
            if (lapackLib.isEmpty()) {
                lapackLib = NativeFFMLoader.loadLibrary("lapack", arena);
            }
            LAPACK_LOOKUP = lapackLib.orElse(null);
            
            if (LOOKUP == null) {
                logger.warn("FFM: BLAS symbol lookup unavailable. Backend disabled.");
                return;
            }

            // --- VALIDATE LIBRARY INTEGRITY ---
            // Try to find a very basic symbol to ensure the library is actually functional
            if (NativeFFMLoader.findSymbol(LOOKUP, "cblas_sgemm", "sgemm_", "sgemm").isEmpty()) {
                logger.warn("FFM: Library found but essential BLAS symbols (sgemm) are missing. Probable ABI mismatch or corrupt library.");
                return;
            }

            bindSymbols();

            // Verify essential handles
            IS_AVAILABLE = (DGEMM != null && DGEMV != null && DDOT != null);

            if (IS_AVAILABLE) {
                logger.info("FFM: BLAS/LAPACK Backend initialized successfully.");
            } else {
                logger.warn("FFM: Native library linked but essential handles are missing.");
            }
        } catch (Throwable t) {
            logger.error("FFM: Critical failure during BLAS/LAPACK initialization: {}", t.getMessage());
            IS_AVAILABLE = false;
        }
    }

    private static void bindSymbols() {
        // --- SINGLE PRECISION (FLOAT) ---
        FunctionDescriptor sgemmDesc = FunctionDescriptor.ofVoid(
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                ValueLayout.JAVA_FLOAT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                ValueLayout.JAVA_FLOAT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
        );
        SGEMM = NativeFFMLoader.findSymbol(LOOKUP, "cblas_sgemm")
            .map(s -> LINKER.downcallHandle(s, sgemmDesc)).orElse(null);

        FunctionDescriptor sgemvDesc = FunctionDescriptor.ofVoid(
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                ValueLayout.JAVA_FLOAT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT,
                AddressLayout.ADDRESS, ValueLayout.JAVA_INT,
                ValueLayout.JAVA_FLOAT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
        );
        SGEMV = NativeFFMLoader.findSymbol(LOOKUP, "cblas_sgemv")
            .map(s -> LINKER.downcallHandle(s, sgemvDesc)).orElse(null);

        FunctionDescriptor sdotDesc = FunctionDescriptor.of(ValueLayout.JAVA_FLOAT,
                ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                AddressLayout.ADDRESS, ValueLayout.JAVA_INT
        );
        SDOT = NativeFFMLoader.findSymbol(LOOKUP, "cblas_sdot")
            .map(s -> LINKER.downcallHandle(s, sdotDesc)).orElse(null);

        FunctionDescriptor snrm2Desc = FunctionDescriptor.of(ValueLayout.JAVA_FLOAT,
                ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
        );
        SNRM2 = NativeFFMLoader.findSymbol(LOOKUP, "cblas_snrm2")
            .map(s -> LINKER.downcallHandle(s, snrm2Desc)).orElse(null);

        FunctionDescriptor saxpyDesc = FunctionDescriptor.ofVoid(
                ValueLayout.JAVA_INT, ValueLayout.JAVA_FLOAT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                AddressLayout.ADDRESS, ValueLayout.JAVA_INT
        );
        SAXPY = NativeFFMLoader.findSymbol(LOOKUP, "cblas_saxpy")
            .map(s -> LINKER.downcallHandle(s, saxpyDesc)).orElse(null);

        FunctionDescriptor sscalDesc = FunctionDescriptor.ofVoid(
                ValueLayout.JAVA_INT, ValueLayout.JAVA_FLOAT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
        );
        SSCAL = NativeFFMLoader.findSymbol(LOOKUP, "cblas_sscal")
            .map(s -> LINKER.downcallHandle(s, sscalDesc)).orElse(null);


        // --- DOUBLE PRECISION (Standard) ---
        FunctionDescriptor dgemmDesc = FunctionDescriptor.ofVoid(
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
        );
        DGEMM = NativeFFMLoader.findSymbol(LOOKUP, "cblas_dgemm")
                .map(s -> LINKER.downcallHandle(s, dgemmDesc)).orElse(null);

        FunctionDescriptor ddotDesc = FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE,
                ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                AddressLayout.ADDRESS, ValueLayout.JAVA_INT
        );
        DDOT = NativeFFMLoader.findSymbol(LOOKUP, "cblas_ddot")
            .map(s -> LINKER.downcallHandle(s, ddotDesc)).orElse(null);

        FunctionDescriptor dnrm2Desc = FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE,
                ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
        );
        DNRM2 = NativeFFMLoader.findSymbol(LOOKUP, "cblas_dnrm2")
            .map(s -> LINKER.downcallHandle(s, dnrm2Desc)).orElse(null);

        FunctionDescriptor daxpyDesc = FunctionDescriptor.ofVoid(
                ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                AddressLayout.ADDRESS, ValueLayout.JAVA_INT
        );
        DAXPY = NativeFFMLoader.findSymbol(LOOKUP, "cblas_daxpy")
            .map(s -> LINKER.downcallHandle(s, daxpyDesc)).orElse(null);
        
        FunctionDescriptor dscalDesc = FunctionDescriptor.ofVoid(
                ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
        );
        DSCAL = NativeFFMLoader.findSymbol(LOOKUP, "cblas_dscal")
            .map(s -> LINKER.downcallHandle(s, dscalDesc)).orElse(null);


        FunctionDescriptor dgemvDesc = FunctionDescriptor.ofVoid(
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT,
                AddressLayout.ADDRESS, ValueLayout.JAVA_INT,
                ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
        );
        DGEMV = NativeFFMLoader.findSymbol(LOOKUP, "cblas_dgemv")
            .map(s -> LINKER.downcallHandle(s, dgemvDesc)).orElse(null);

        // --- COMPLEX BLAS (FLOAT & DOUBLE) ---
        FunctionDescriptor cgemmDesc = FunctionDescriptor.ofVoid(
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                AddressLayout.ADDRESS, AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                AddressLayout.ADDRESS, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
        );
        CGEMM = NativeFFMLoader.findSymbol(LOOKUP, "cblas_cgemm")
            .map(s -> LINKER.downcallHandle(s, cgemmDesc)).orElse(null);
        ZGEMM = NativeFFMLoader.findSymbol(LOOKUP, "cblas_zgemm")
                .map(s -> LINKER.downcallHandle(s, cgemmDesc)).orElse(null);

        FunctionDescriptor cgemvDesc = FunctionDescriptor.ofVoid(
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                AddressLayout.ADDRESS, AddressLayout.ADDRESS, ValueLayout.JAVA_INT,
                AddressLayout.ADDRESS, ValueLayout.JAVA_INT,
                AddressLayout.ADDRESS, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
        );
        CGEMV = NativeFFMLoader.findSymbol(LOOKUP, "cblas_cgemv")
            .map(s -> LINKER.downcallHandle(s, cgemvDesc)).orElse(null);
        ZGEMV = NativeFFMLoader.findSymbol(LOOKUP, "cblas_zgemv")
            .map(s -> LINKER.downcallHandle(s, cgemvDesc)).orElse(null);

        FunctionDescriptor cdotcDesc = FunctionDescriptor.ofVoid(
                ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS
        );
        CDOTC = NativeFFMLoader.findSymbol(LOOKUP, "cblas_cdotc_sub", "cblas_cdots")
            .map(s -> LINKER.downcallHandle(s, cdotcDesc)).orElse(null);
        ZDOTC = NativeFFMLoader.findSymbol(LOOKUP, "cblas_zdotc_sub", "cblas_zdotc")
            .map(s -> LINKER.downcallHandle(s, cdotcDesc)).orElse(null);

        FunctionDescriptor scnrm2Desc = FunctionDescriptor.of(ValueLayout.JAVA_FLOAT,
                ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
        );
        SCNRM2 = NativeFFMLoader.findSymbol(LOOKUP, "cblas_scnrm2")
            .map(s -> LINKER.downcallHandle(s, scnrm2Desc)).orElse(null);
        
        FunctionDescriptor dznrm2Desc = FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE,
                ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
        );
        DZNRM2 = NativeFFMLoader.findSymbol(LOOKUP, "cblas_dznrm2")
            .map(s -> LINKER.downcallHandle(s, dznrm2Desc)).orElse(null);

        FunctionDescriptor caxpyDesc = FunctionDescriptor.ofVoid(
                ValueLayout.JAVA_INT, AddressLayout.ADDRESS, AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                AddressLayout.ADDRESS, ValueLayout.JAVA_INT
        );
        CAXPY = NativeFFMLoader.findSymbol(LOOKUP, "cblas_caxpy")
            .map(s -> LINKER.downcallHandle(s, caxpyDesc)).orElse(null);
        ZAXPY = NativeFFMLoader.findSymbol(LOOKUP, "cblas_zaxpy")
            .map(s -> LINKER.downcallHandle(s, caxpyDesc)).orElse(null);

        FunctionDescriptor cscalDesc = FunctionDescriptor.ofVoid(
                ValueLayout.JAVA_INT, AddressLayout.ADDRESS, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
        );
        CSCAL = NativeFFMLoader.findSymbol(LOOKUP, "cblas_cscal")
            .map(s -> LINKER.downcallHandle(s, cscalDesc)).orElse(null);
        ZSCAL = NativeFFMLoader.findSymbol(LOOKUP, "cblas_zscal")
            .map(s -> LINKER.downcallHandle(s, cscalDesc)).orElse(null);


        // LAPACK
        FunctionDescriptor gesvDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS,
                AddressLayout.ADDRESS, ValueLayout.JAVA_INT
        );
        SGESV = findLapackSymbol("LAPACKE_sgesv").map(s -> LINKER.downcallHandle(s, gesvDesc)).orElse(null);
        DGESV = findLapackSymbol("LAPACKE_dgesv").map(s -> LINKER.downcallHandle(s, gesvDesc)).orElse(null);
        CGESV = findLapackSymbol("LAPACKE_cgesv").map(s -> LINKER.downcallHandle(s, gesvDesc)).orElse(null);
        ZGESV = findLapackSymbol("LAPACKE_zgesv").map(s -> LINKER.downcallHandle(s, gesvDesc)).orElse(null);
        
        FunctionDescriptor getrfDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS
        );
        SGETRF = findLapackSymbol("LAPACKE_sgetrf").map(s -> LINKER.downcallHandle(s, getrfDesc)).orElse(null);
        DGETRF = findLapackSymbol("LAPACKE_dgetrf").map(s -> LINKER.downcallHandle(s, getrfDesc)).orElse(null);
        CGETRF = findLapackSymbol("LAPACKE_cgetrf").map(s -> LINKER.downcallHandle(s, getrfDesc)).orElse(null);
        ZGETRF = findLapackSymbol("LAPACKE_zgetrf").map(s -> LINKER.downcallHandle(s, getrfDesc)).orElse(null);

        FunctionDescriptor getriDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS
        );
        SGETRI = findLapackSymbol("LAPACKE_sgetri", "lapacke_sgetri", "sgetri").map(s -> LINKER.downcallHandle(s, getriDesc)).orElse(null);
        DGETRI = findLapackSymbol("LAPACKE_dgetri", "lapacke_dgetri", "dgetri").map(s -> LINKER.downcallHandle(s, getriDesc)).orElse(null);
        CGETRI = findLapackSymbol("LAPACKE_cgetri", "lapacke_cgetri", "cgetri").map(s -> LINKER.downcallHandle(s, getriDesc)).orElse(null);
        ZGETRI = findLapackSymbol("LAPACKE_zgetri", "lapacke_zgetri", "zgetri").map(s -> LINKER.downcallHandle(s, getriDesc)).orElse(null);

        FunctionDescriptor getrsDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
        );
        SGETRS = findLapackSymbol("LAPACKE_sgetrs").map(s -> LINKER.downcallHandle(s, getrsDesc)).orElse(null);
        DGETRS = findLapackSymbol("LAPACKE_dgetrs").map(s -> LINKER.downcallHandle(s, getrsDesc)).orElse(null);
        CGETRS = findLapackSymbol("LAPACKE_cgetrs").map(s -> LINKER.downcallHandle(s, getrsDesc)).orElse(null);
        ZGETRS = findLapackSymbol("LAPACKE_zgetrs").map(s -> LINKER.downcallHandle(s, getrsDesc)).orElse(null);

        // QR Decomposition
        FunctionDescriptor trtrsDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_BYTE,
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT,
                AddressLayout.ADDRESS, ValueLayout.JAVA_INT
        );
        STRTRS = findLapackSymbol("LAPACKE_strtrs").map(s -> LINKER.downcallHandle(s, trtrsDesc)).orElse(null);
        DTRTRS = findLapackSymbol("LAPACKE_dtrtrs").map(s -> LINKER.downcallHandle(s, trtrsDesc)).orElse(null);
        CTRTRS = findLapackSymbol("LAPACKE_ctrtrs").map(s -> LINKER.downcallHandle(s, trtrsDesc)).orElse(null);
        ZTRTRS = findLapackSymbol("LAPACKE_ztrtrs").map(s -> LINKER.downcallHandle(s, trtrsDesc)).orElse(null);

        // QR Decomposition
        FunctionDescriptor geqrfDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS
        );
        SGEQRF = findLapackSymbol("LAPACKE_sgeqrf", "lapacke_sgeqrf", "sgeqrf").map(s -> LINKER.downcallHandle(s, geqrfDesc)).orElse(null);
        DGEQRF = findLapackSymbol("LAPACKE_dgeqrf", "lapacke_dgeqrf", "dgeqrf").map(s -> LINKER.downcallHandle(s, geqrfDesc)).orElse(null);
        CGEQRF = findLapackSymbol("LAPACKE_cgeqrf", "lapacke_cgeqrf", "cgeqrf").map(s -> LINKER.downcallHandle(s, geqrfDesc)).orElse(null);
        ZGEQRF = findLapackSymbol("LAPACKE_zgeqrf", "lapacke_zgeqrf", "zgeqrf").map(s -> LINKER.downcallHandle(s, geqrfDesc)).orElse(null);

        FunctionDescriptor orgqrDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS
        );
        SORGQR = findLapackSymbol("LAPACKE_sorgqr").map(s -> LINKER.downcallHandle(s, orgqrDesc)).orElse(null);
        DORGQR = findLapackSymbol("LAPACKE_dorgqr").map(s -> LINKER.downcallHandle(s, orgqrDesc)).orElse(null);
        CUNGQR = findLapackSymbol("LAPACKE_cungqr").map(s -> LINKER.downcallHandle(s, orgqrDesc)).orElse(null);
        ZUNGQR = findLapackSymbol("LAPACKE_zungqr").map(s -> LINKER.downcallHandle(s, orgqrDesc)).orElse(null);

        // Singular Value Decomposition
        FunctionDescriptor gesvdDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_BYTE, 
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, AddressLayout.ADDRESS, 
                ValueLayout.JAVA_INT, AddressLayout.ADDRESS, AddressLayout.ADDRESS, 
                ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                AddressLayout.ADDRESS
        );
        SGESVD = findLapackSymbol("LAPACKE_sgesvd").map(s -> LINKER.downcallHandle(s, gesvdDesc)).orElse(null);
        DGESVD = findLapackSymbol("LAPACKE_dgesvd").map(s -> LINKER.downcallHandle(s, gesvdDesc)).orElse(null);
        CGESVD = findLapackSymbol("LAPACKE_cgesvd").map(s -> LINKER.downcallHandle(s, gesvdDesc)).orElse(null);
        ZGESVD = findLapackSymbol("LAPACKE_zgesvd").map(s -> LINKER.downcallHandle(s, gesvdDesc)).orElse(null);

        // Cholesky
        FunctionDescriptor potrfDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT,
                AddressLayout.ADDRESS, ValueLayout.JAVA_INT
        );
        SPOTRF = findLapackSymbol("LAPACKE_spotrf", "lapacke_spotrf", "spotrf").map(s -> LINKER.downcallHandle(s, potrfDesc)).orElse(null);
        DPOTRF = findLapackSymbol("LAPACKE_dpotrf", "lapacke_dpotrf", "dpotrf").map(s -> LINKER.downcallHandle(s, potrfDesc)).orElse(null);
        CPOTRF = findLapackSymbol("LAPACKE_cpotrf", "lapacke_cpotrf", "cpotrf").map(s -> LINKER.downcallHandle(s, potrfDesc)).orElse(null);
        ZPOTRF = findLapackSymbol("LAPACKE_zpotrf", "lapacke_zpotrf", "zpotrf").map(s -> LINKER.downcallHandle(s, potrfDesc)).orElse(null);

        FunctionDescriptor potrsDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
        );
        SPOTRS = findLapackSymbol("LAPACKE_spotrs").map(s -> LINKER.downcallHandle(s, potrsDesc)).orElse(null);
        DPOTRS = findLapackSymbol("LAPACKE_dpotrs").map(s -> LINKER.downcallHandle(s, potrsDesc)).orElse(null);
        CPOTRS = findLapackSymbol("LAPACKE_cpotrs").map(s -> LINKER.downcallHandle(s, potrsDesc)).orElse(null);
        ZPOTRS = findLapackSymbol("LAPACKE_zpotrs").map(s -> LINKER.downcallHandle(s, potrsDesc)).orElse(null);

        // Eigen
        FunctionDescriptor syevDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_BYTE,
                ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT,
                AddressLayout.ADDRESS
        );
        SSYEV = findLapackSymbol("LAPACKE_ssyev").map(s -> LINKER.downcallHandle(s, syevDesc)).orElse(null);
        DSYEV = findLapackSymbol("LAPACKE_dsyev").map(s -> LINKER.downcallHandle(s, syevDesc)).orElse(null);

        FunctionDescriptor heevDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT,
                AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS
        );
        CHEEV = findLapackSymbol("LAPACKE_cheev").map(s -> LINKER.downcallHandle(s, heevDesc)).orElse(null);
        ZHEEV = findLapackSymbol("LAPACKE_zheev").map(s -> LINKER.downcallHandle(s, heevDesc)).orElse(null);

        FunctionDescriptor gelsDescriptor = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, AddressLayout.ADDRESS,
                ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
        );
        SGELS = findLapackSymbol("LAPACKE_sgels").map(s -> LINKER.downcallHandle(s, gelsDescriptor)).orElse(null);
        DGELS = findLapackSymbol("LAPACKE_dgels").map(s -> LINKER.downcallHandle(s, gelsDescriptor)).orElse(null);
        CGELS = findLapackSymbol("LAPACKE_cgels").map(s -> LINKER.downcallHandle(s, gelsDescriptor)).orElse(null);
        ZGELS = findLapackSymbol("LAPACKE_zgels").map(s -> LINKER.downcallHandle(s, gelsDescriptor)).orElse(null);

        // --- TRSM (Triangular Solve) ---
        FunctionDescriptor ctrsmDesc = FunctionDescriptor.ofVoid(
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                AddressLayout.ADDRESS, AddressLayout.ADDRESS, ValueLayout.JAVA_INT,
                AddressLayout.ADDRESS, ValueLayout.JAVA_INT
        );
        CTRSM = NativeFFMLoader.findSymbol(LOOKUP, "cblas_ctrsm")
                .map(s -> LINKER.downcallHandle(s, ctrsmDesc)).orElse(null);
        ZTRSM = NativeFFMLoader.findSymbol(LOOKUP, "cblas_ztrsm")
                .map(s -> LINKER.downcallHandle(s, ctrsmDesc)).orElse(null);

        FunctionDescriptor dtrsmDesc = FunctionDescriptor.ofVoid(
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT,
                AddressLayout.ADDRESS, ValueLayout.JAVA_INT
        );
        DTRSM = NativeFFMLoader.findSymbol(LOOKUP, "cblas_dtrsm")
                .map(s -> LINKER.downcallHandle(s, dtrsmDesc)).orElse(null);

        FunctionDescriptor strsmDesc = FunctionDescriptor.ofVoid(
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                ValueLayout.JAVA_FLOAT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT,
                AddressLayout.ADDRESS, ValueLayout.JAVA_INT
        );
        STRSM = NativeFFMLoader.findSymbol(LOOKUP, "cblas_strsm")
                .map(s -> LINKER.downcallHandle(s, strsmDesc)).orElse(null);
    }

    private static final Linker LINKER = NativeFFMLoader.getLinker();


    // CBLAS Layout Constants
    private static final int CblasRowMajor = 101;
    private static final int CblasNoTrans = 111;

    // BLAS Method Handles
    private static MethodHandle SGEMM;
    private static MethodHandle DGEMM;
    private static MethodHandle CGEMM;
    private static MethodHandle ZGEMM;
    
    private static MethodHandle SGEMV;
    private static MethodHandle DGEMV;
    private static MethodHandle CGEMV;
    private static MethodHandle ZGEMV;

    private static MethodHandle SDOT;
    private static MethodHandle DDOT;
    private static MethodHandle CDOTC;
    private static MethodHandle ZDOTC;

    private static MethodHandle SAXPY;
    private static MethodHandle DAXPY;
    private static MethodHandle CAXPY;
    private static MethodHandle ZAXPY;

    private static MethodHandle SNRM2;
    private static MethodHandle DNRM2;
    private static MethodHandle SCNRM2;
    private static MethodHandle DZNRM2;

    private static MethodHandle SSCAL;
    private static MethodHandle DSCAL;
    private static MethodHandle CSCAL;
    private static MethodHandle ZSCAL;

    private static MethodHandle STRSM;
    private static MethodHandle DTRSM;
    private static MethodHandle CTRSM;
    private static MethodHandle ZTRSM;
    
    private static MethodHandle STRTRS;
    private static MethodHandle DTRTRS;
    private static MethodHandle CTRTRS;
    private static MethodHandle ZTRTRS;
    
    // LAPACK Method Handles
    private static MethodHandle SGESV;
    private static MethodHandle DGESV;
    private static MethodHandle CGESV;
    private static MethodHandle ZGESV;

    private static MethodHandle SGETRF;
    private static MethodHandle DGETRF;
    private static MethodHandle CGETRF;
    private static MethodHandle ZGETRF;

    private static MethodHandle SGETRI;
    private static MethodHandle DGETRI;
    private static MethodHandle CGETRI;
    private static MethodHandle ZGETRI;

    private static MethodHandle SGETRS;
    private static MethodHandle DGETRS;
    private static MethodHandle CGETRS;
    private static MethodHandle ZGETRS;

    private static MethodHandle SGEQRF;
    private static MethodHandle DGEQRF;
    private static MethodHandle CGEQRF;
    private static MethodHandle ZGEQRF;

    private static MethodHandle SORGQR;
    private static MethodHandle DORGQR;
    private static MethodHandle CUNGQR;
    private static MethodHandle ZUNGQR;

    private static MethodHandle SGESVD;
    private static MethodHandle DGESVD;
    private static MethodHandle CGESVD;
    private static MethodHandle ZGESVD;

    private static MethodHandle SPOTRF;
    private static MethodHandle DPOTRF;
    private static MethodHandle CPOTRF;
    private static MethodHandle ZPOTRF;

    private static MethodHandle SPOTRS;
    private static MethodHandle DPOTRS;
    private static MethodHandle CPOTRS;
    private static MethodHandle ZPOTRS;

    private static MethodHandle SSYEV;
    private static MethodHandle DSYEV;
    private static MethodHandle CHEEV;
    private static MethodHandle ZHEEV;

    private static MethodHandle SGELS;
    private static MethodHandle DGELS;
    private static MethodHandle CGELS;
    private static MethodHandle ZGELS;
    
    private static final int LAPACK_ROW_MAJOR = 101;

    private static Optional<MemorySegment> findLapackSymbol(String... names) {
        Optional<MemorySegment> sym = NativeFFMLoader.findSymbol(LOOKUP, names);
        if (sym.isEmpty() && LAPACK_LOOKUP != null) {
            sym = NativeFFMLoader.findSymbol(LAPACK_LOOKUP, names);
        }
        return sym;
    }

    @Override
    public boolean isLoaded() {
        ensureInitialized();
        return IS_AVAILABLE;
    }

    @Override
    public boolean isAvailable() {
        if (isExplicitlyDisabled()) return false;
        ensureInitialized();
        return IS_AVAILABLE;
    }

    @Override
    public boolean isExplicitlyDisabled() {
        return Boolean.getBoolean("episteme.backend.ffm-blas.disabled") || 
               Boolean.getBoolean("episteme.native.skip.openblas") ||
               (getId() != null && Boolean.getBoolean("episteme.backend." + getId() + ".disabled"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getId() {
        return "ffm-blas";
    }

    @Override
    public String getNativeLibraryName() {
        return "openblas";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> solveTriangular(Matrix<E> A, Vector<E> b, boolean upper, boolean transpose, boolean conjugate, boolean unit) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": solveTriangular() not available");
        int n = A.rows();
        if (n != A.cols()) throw new IllegalArgumentException("Matrix must be square");
        if (n != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");

        boolean single = isFloat(A);
        boolean complex = isComplex(A);

        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment segA = getSegment(A, arena, tracker);
            // We must copy b to segB because TRSM/GESV are in-place on B
            MemorySegment segB;
                if (single) {
                    if (complex) segB = NativeSafe.allocateFromArray(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(b));
                    else segB = NativeSafe.allocateFromArray(arena, ValueLayout.JAVA_FLOAT, toFloatArray(b));
                } else {
                    if (complex) segB = NativeSafe.allocateFromArray(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(b));
                    else segB = NativeSafe.allocateFromArray(arena, ValueLayout.JAVA_DOUBLE, toDoubleArray(b));
                }
            
            // CBLAS Constants
            int side = 141; // CblasLeft
            int uplo = upper ? 121 : 122; // CblasUpper : CblasLower
            int trans;
            if (transpose) {
                trans = conjugate ? 113 : 112; // CblasConjTrans : CblasTrans
            } else {
                trans = 111; // CblasNoTrans
            }
            int diag = unit ? 131 : 132; // CblasUnit : CblasNonUnit

            if (complex) {
                MemorySegment alpha;
                if (single) {
                    alpha = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, 1.0f, 0.0f);
                    NativeSafe.invoke(CTRSM, CblasRowMajor, side, uplo, trans, diag, n, 1, alpha, segA, n, segB, 1);
                    @SuppressWarnings("unchecked")
                    Vector<E> result = (Vector<E>) createDenseVector(segB.toArray(ValueLayout.JAVA_FLOAT), n, A);
                    return result;
                } else {
                    alpha = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, 1.0, 0.0);
                    NativeSafe.invoke(ZTRSM, CblasRowMajor, side, uplo, trans, diag, n, 1, alpha, segA, n, segB, 1);
                    @SuppressWarnings("unchecked")
                    Vector<E> result = (Vector<E>) createDenseVector(segB.toArray(ValueLayout.JAVA_DOUBLE), n, A);
                    return result;
                }
            } else {
                if (single) {
                    NativeSafe.invoke(STRSM, CblasRowMajor, side, uplo, trans, diag, n, 1, 1.0f, segA, n, segB, 1);
                    @SuppressWarnings("unchecked")
                    Vector<E> result = (Vector<E>) createDenseVector(segB.toArray(ValueLayout.JAVA_FLOAT), n, A);
                    return result;
                } else {
                    NativeSafe.invoke(DTRSM, CblasRowMajor, side, uplo, trans, diag, n, 1, 1.0, segA, n, segB, 1);
                    @SuppressWarnings("unchecked")
                    Vector<E> result = (Vector<E>) createDenseVector(segB.toArray(ValueLayout.JAVA_DOUBLE), n, A);
                    return result;
                }
            }
        } catch (Throwable t) { 
            throw new RuntimeException("TRSM failed", t); 
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> solve(Matrix<E> A, Vector<E> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": solve() not available");
        org.episteme.core.mathematics.context.MathContext.checkCancelled();
        
        int m = A.rows(), n = A.cols();
        boolean complex = isComplex(A);
        boolean single = isFloat(A);

        if (m == n) {
             if (n != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");
             try (ResourceTracker tracker = new ResourceTracker()) {
                 Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
                 MemorySegment segA;
                 MemorySegment segB;
                if (single) {
                    if (complex) {
                        segA = NativeSafe.allocateFromArray(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(A));
                        segB = NativeSafe.allocateFromArray(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(b));
                    } else {
                        segA = NativeSafe.allocateFromArray(arena, ValueLayout.JAVA_FLOAT, toFloatArray(A));
                        segB = NativeSafe.allocateFromArray(arena, ValueLayout.JAVA_FLOAT, toFloatArray(b));
                    }
                } else {
                    if (complex) {
                        segA = NativeSafe.allocateFromArray(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(A));
                        segB = NativeSafe.allocateFromArray(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(b));
                    } else {
                        segA = NativeSafe.allocateFromArray(arena, ValueLayout.JAVA_DOUBLE, toDoubleArray(A));
                        segB = NativeSafe.allocateFromArray(arena, ValueLayout.JAVA_DOUBLE, toDoubleArray(b));
                    }
                }
                MemorySegment segIpiv = NativeSafe.allocate(arena, ValueLayout.JAVA_INT, (long) n);

                if (complex) {
                    if (single) {
                        if (logger.isDebugEnabled()) logger.debug("[FFM-BLAS] CGESV: n={}, lda={}", n, n);
                        int info = (int) NativeSafe.invoke(CGESV, LAPACK_ROW_MAJOR, n, 1, segA, n, segIpiv, segB, 1);
                        if (info != 0) throw new ArithmeticException("CGESV failed: " + info);
                        @SuppressWarnings("unchecked")
                        Vector<E> result = (Vector<E>) createDenseVector(segB.toArray(ValueLayout.JAVA_FLOAT), n, A);
                        return result;
                    } else {
                        if (logger.isDebugEnabled()) logger.debug("[FFM-BLAS] ZGESV: n={}, lda={}", n, n);
                        int info = (int) NativeSafe.invoke(ZGESV, LAPACK_ROW_MAJOR, n, 1, segA, n, segIpiv, segB, 1);
                        if (info != 0) throw new ArithmeticException("ZGESV failed: " + info);
                        @SuppressWarnings("unchecked")
                        Vector<E> result = (Vector<E>) createDenseVector(segB.toArray(ValueLayout.JAVA_DOUBLE), n, A);
                        return result;
                    }
                } else {
                    if (single) {
                        if (logger.isDebugEnabled()) logger.debug("[FFM-BLAS] SGESV: n={}, lda={}", n, n);
                        int info = (int) NativeSafe.invoke(SGESV, LAPACK_ROW_MAJOR, n, 1, segA, n, segIpiv, segB, 1);
                        if (info != 0) throw new ArithmeticException("SGESV failed: " + info);
                        @SuppressWarnings("unchecked")
                        Vector<E> result = (Vector<E>) createDenseVector(segB.toArray(ValueLayout.JAVA_FLOAT), n, A);
                        return result;
                    } else {
                        if (logger.isDebugEnabled()) logger.debug("[FFM-BLAS] DGESV: n={}, lda={}", n, n);
                        int info = (int) NativeSafe.invoke(DGESV, LAPACK_ROW_MAJOR, n, 1, segA, n, segIpiv, segB, 1);
                        if (info != 0) throw new ArithmeticException("DGESV failed: " + info);
                        @SuppressWarnings("unchecked")
                        Vector<E> result = (Vector<E>) createDenseVector(segB.toArray(ValueLayout.JAVA_DOUBLE), n, A);
                        return result;
                    }
                }
            }
        } else {
             // Least Squares
             try (ResourceTracker tracker = new ResourceTracker()) {
                 Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
                 int maxDim = Math.max(m, n);

                 if (complex) {
                     MemorySegment segA = getSegment(A, arena, tracker);
                     if (single) {
                        float[] bPad = new float[maxDim * 2];
                        float[] bOrig = toInterlacedFloatArray(b);
                        System.arraycopy(bOrig, 0, bPad, 0, bOrig.length);
                        MemorySegment segB = NativeSafe.allocateFromArray(arena, ValueLayout.JAVA_FLOAT, bPad);
                        int info = (int) NativeSafe.invoke(CGELS, LAPACK_ROW_MAJOR, (byte) 'N', m, n, 1, segA, n, segB, 1);
                         if (info != 0) throw new RuntimeException("CGELS failed: " + info);
                         float[] resFull = segB.toArray(ValueLayout.JAVA_FLOAT);
                         float[] resData = new float[n * 2];
                         System.arraycopy(resFull, 0, resData, 0, n * 2);
                         @SuppressWarnings("unchecked")
                         Vector<E> result = (Vector<E>) createDenseVector(resData, n, A);
                         return result;
                     } else {
                         MemorySegment segA_D = getSegment(A, arena, tracker);
                        double[] bPad = new double[maxDim * 2];
                        double[] bOrig = toInterlacedDoubleArray(b);
                        System.arraycopy(bOrig, 0, bPad, 0, bOrig.length);
                        MemorySegment segB = NativeSafe.allocateFromArray(arena, ValueLayout.JAVA_DOUBLE, bPad);
                        int info = (int) NativeSafe.invoke(ZGELS, LAPACK_ROW_MAJOR, (byte) 'N', m, n, 1, segA_D, n, segB, 1);
                         if (info != 0) throw new RuntimeException("ZGELS failed: " + info);
                         double[] resFull = segB.toArray(ValueLayout.JAVA_DOUBLE);
                         double[] resData = new double[n * 2];
                         System.arraycopy(resFull, 0, resData, 0, n * 2);
                         @SuppressWarnings("unchecked")
                         Vector<E> result = (Vector<E>) createDenseVector(resData, n, A);
                         return result;
                     }
                 } else {
                     MemorySegment segA = getSegment(A, arena, tracker);
                     if (single) {
                         float[] bPad = new float[maxDim];
                         float[] bOrig = toFloatArray(b);
                         System.arraycopy(bOrig, 0, bPad, 0, bOrig.length);
                         MemorySegment segB = NativeSafe.allocateFromArray(arena, ValueLayout.JAVA_FLOAT, bPad);
                         int info = (int) NativeSafe.invoke(SGELS, LAPACK_ROW_MAJOR, (byte) 'N', m, n, 1, segA, n, segB, 1);
                         if (info != 0) throw new RuntimeException("SGELS failed: " + info);
                         float[] result = new float[n];
                         MemorySegment.copy(segB, ValueLayout.JAVA_FLOAT, 0L, result, 0, n);
                         @SuppressWarnings("unchecked")
                         Vector<E> resVec = (Vector<E>) createDenseVector(result, n, A);
                         return resVec;
                     } else {
                        double[] bPad = new double[maxDim];
                        double[] bOrig = toDoubleArray(b);
                        System.arraycopy(bOrig, 0, bPad, 0, bOrig.length);
                        MemorySegment segB = NativeSafe.allocateFromArray(arena, ValueLayout.JAVA_DOUBLE, bPad);
                        int info = (int) NativeSafe.invoke(DGELS, LAPACK_ROW_MAJOR, (byte) 'N', m, n, 1, segA, n, segB, 1);
                         if (info != 0) throw new RuntimeException("DGELS failed: " + info);
                         double[] result = new double[n];
                         MemorySegment.copy(segB, ValueLayout.JAVA_DOUBLE, 0L, result, 0, n);
                         @SuppressWarnings("unchecked")
                         Vector<E> resVec = (Vector<E>) createDenseVector(result, n, A);
                         return resVec;
                     }
                 }
             }
        }
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
    @SuppressWarnings("unchecked")
    public Vector<E> cross(Vector<E> a, Vector<E> b) {
        if (a.dimension() != 3 || b.dimension() != 3) throw new IllegalArgumentException("Cross product only supported for 3D vectors");
        
        if (isComplex(a)) {
            Ring<E> ring = a.getScalarRing();
            List<E> res = java.util.Arrays.asList(
                castScalar(getComplexValue(a.get(1)).multiply(getComplexValue(b.get(2))).subtract(getComplexValue(a.get(2)).multiply(getComplexValue(b.get(1)))), ring),
                castScalar(getComplexValue(a.get(2)).multiply(getComplexValue(b.get(0))).subtract(getComplexValue(a.get(0)).multiply(getComplexValue(b.get(2)))), ring),
                castScalar(getComplexValue(a.get(0)).multiply(getComplexValue(b.get(1))).subtract(getComplexValue(a.get(1)).multiply(getComplexValue(b.get(0)))), ring)
            );
            return org.episteme.core.mathematics.linearalgebra.Vector.of(res, ring);
        }
        
        Ring<E> ring = a.getScalarRing();
        List<E> res = java.util.Arrays.asList(
            castScalar(getReal(a.get(1)).multiply(getReal(b.get(2))).subtract(getReal(a.get(2)).multiply(getReal(b.get(1)))), ring),
            castScalar(getReal(a.get(2)).multiply(getReal(b.get(0))).subtract(getReal(a.get(0)).multiply(getReal(b.get(2)))), ring),
            castScalar(getReal(a.get(0)).multiply(getReal(b.get(1))).subtract(getReal(a.get(1)).multiply(getReal(b.get(0)))), ring)
        );
        
        return org.episteme.core.mathematics.linearalgebra.Vector.of(res, ring);
    }

    @Override
    @SuppressWarnings("unchecked")
    public E angle(Vector<E> a, Vector<E> b) {
        E d = dot(a, b);
        E nA = norm(a);
        E nB = norm(b);
        
        Ring<E> ring = a.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.structures.rings.Field<E> field) {
            try {
                E denom = field.multiply(nA, nB);
                if (isZero(denom, ring)) return createScalar(0.0, (Object)a);
                
                E cosTheta = field.divide(d, denom);
                Real cosReal = getReal(cosTheta);
                // Use Real.acos() if possible for better precision
                Real angleReal = cosReal.max(Real.of(-1.0)).min(Real.of(1.0)).acos();
                return castScalar(angleReal, ring);
            } catch (Exception e) {
                return createScalar(0.0, (Object)a);
            }
        }

        double dotVal = getRealValue(d);
        double nAVal = getRealValue(nA);
        double nBVal = getRealValue(nB);
        
        if (nAVal == 0 || nBVal == 0) return createScalar(0.0, (Object)a);
        return castScalar(Math.acos(Math.max(-1.0, Math.min(1.0, dotVal / (nAVal * nBVal)))), ring);
    }

    @Override
    @SuppressWarnings("unchecked")
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
        return multiply(b, createScalar(dotAB / dotBB, b));
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": transpose() not available");
        int m = a.rows(), n = a.cols();
        if (isComplex(a)) {
            double[] data = toInterlacedDoubleArray(a);
            double[] res = new double[data.length];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    res[(j * m + i) * 2] = data[(i * n + j) * 2];
                    res[(j * m + i) * 2 + 1] = data[(i * n + j) * 2 + 1];
                }
            }
            return (Matrix<E>) createDenseMatrix(res, n, m, a);
        }
        double[] data = toDoubleArray(a);
        double[] res = new double[m * n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                res[j * m + i] = data[i * n + j];
            }
        }
        return (Matrix<E>) createDenseMatrix(res, n, m, a);
    }

    
    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> inverse(Matrix<E> A) {
         if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": inverse() not available");
         int m = A.rows();
         int n = A.cols();
         if (m != n) return pseudoInverse(A);
         
         boolean complex = isComplex(A);
         boolean single = isFloat(A);

         if (complex) {
             if (single) {
                 try (Arena arena = Arena.ofConfined()) {
                     MemorySegment segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(A));
                     MemorySegment segIpiv = NativeSafe.allocate(arena, ValueLayout.JAVA_INT, (long) n);
                     int info = (int) NativeSafe.invoke(CGETRF, LAPACK_ROW_MAJOR, n, n, segA, n, segIpiv);
                     if (info != 0) throw new ArithmeticException("CGETRF failed: " + info);
                     info = (int) NativeSafe.invoke(CGETRI, LAPACK_ROW_MAJOR, n, segA, n, segIpiv);
                     if (info != 0) throw new ArithmeticException("CGETRI failed: " + info);
                     float[] result = segA.toArray(ValueLayout.JAVA_FLOAT);
                     return (Matrix<E>) createDenseMatrix(result, n, n, A);
                 }
             } else {
                 try (Arena arena = Arena.ofConfined()) {
                     MemorySegment segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(A));
                     MemorySegment segIpiv = NativeSafe.allocate(arena, ValueLayout.JAVA_INT, (long) n);
                     int info = (int) NativeSafe.invoke(ZGETRF, LAPACK_ROW_MAJOR, n, n, segA, n, segIpiv);
                     if (info != 0) throw new ArithmeticException("ZGETRF failed: " + info);
                     info = (int) NativeSafe.invoke(ZGETRI, LAPACK_ROW_MAJOR, n, segA, n, segIpiv);
                     if (info != 0) throw new ArithmeticException("ZGETRI failed: " + info);
                     double[] result = segA.toArray(ValueLayout.JAVA_DOUBLE);
                     return (Matrix<E>) createDenseMatrix(result, n, n, A);
                 }
             }
         }
         
         if (single) {
             try (ResourceTracker tracker = new ResourceTracker()) {
                 Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
                 MemorySegment segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toFloatArray(A));
                 MemorySegment segIpiv = NativeSafe.allocate(arena, ValueLayout.JAVA_INT, (long) n);
                 int info = (int) NativeSafe.invoke(SGETRF, LAPACK_ROW_MAJOR, n, n, segA, n, segIpiv);
                 if (info != 0) throw new ArithmeticException("SGETRF failed: " + info);
                 info = (int) NativeSafe.invoke(SGETRI, LAPACK_ROW_MAJOR, n, segA, n, segIpiv);
                 if (info != 0) throw new ArithmeticException("SGETRI failed: " + info);
                 float[] result = segA.toArray(ValueLayout.JAVA_FLOAT);
                 return (Matrix<E>) createDenseMatrix(result, n, n, A);
             }
         }
 
         try (ResourceTracker tracker = new ResourceTracker()) {
             Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
             MemorySegment segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toDoubleArray(A));
             MemorySegment segIpiv = NativeSafe.allocate(arena, ValueLayout.JAVA_INT, (long) n);
             int info = (int) NativeSafe.invoke(DGETRF, LAPACK_ROW_MAJOR, n, m, segA, n, segIpiv);
             if (info != 0) throw new ArithmeticException("DGETRF failed: " + info);
             info = (int) NativeSafe.invoke(DGETRI, LAPACK_ROW_MAJOR, n, segA, n, segIpiv);
             if (info != 0) throw new ArithmeticException("DGETRI failed: " + info);
             double[] result = segA.toArray(ValueLayout.JAVA_DOUBLE);
             return (Matrix<E>) createDenseMatrix(result, n, n, A);
         }
    }

    private Matrix<E> pseudoInverse(Matrix<E> a) {
        SVDResult<E> svd = svd(a);
        int m = a.rows();
        int n = a.cols();
        int k = svd.S().dimension();
        
        Matrix<E> sInv;
        if (isComplex(a)) {
            org.episteme.core.mathematics.numbers.complex.Complex[][] sData = new org.episteme.core.mathematics.numbers.complex.Complex[n][m];
            for(int i=0; i<n; i++) for(int j=0; j<m; j++) sData[i][j] = org.episteme.core.mathematics.numbers.complex.Complex.ZERO;
            for(int i=0; i<k; i++) {
                double sVal = ((org.episteme.core.mathematics.numbers.real.Real)(Object)svd.S().get(i)).doubleValue();
                if (sVal > 1e-12) sData[i][i] = org.episteme.core.mathematics.numbers.complex.Complex.of(1.0 / sVal);
            }

            E[][] castedArr = (E[][]) sData;
            sInv = new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(castedArr, (Ring<E>) a.getScalarRing());
        } else {
            Real[][] sData = new Real[n][m];
            for(int i=0; i<n; i++) for(int j=0; j<m; j++) sData[i][j] = Real.ZERO;
            for(int i=0; i<k; i++) {
                double sVal = ((Real)(Object)svd.S().get(i)).doubleValue();
                if (sVal > 1e-12) sData[i][i] = Real.of(1.0 / sVal);
            }

            E[][] castedArr = (E[][]) sData;
            sInv = new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(castedArr, (Ring<E>) a.getScalarRing());
        }
        return svd.V().multiply(sInv).multiply(conjugateTranspose(svd.U()));
    }

    public Matrix<E> conjugateTranspose(Matrix<E> m) {
        Matrix<E> mt = m.transpose();
        if (isComplex(m)) {
            return mt.map(val -> {

                org.episteme.core.mathematics.numbers.complex.Complex c = (org.episteme.core.mathematics.numbers.complex.Complex) (Object) val;
                E casted = (E) (Object) c.conjugate();
                return casted;
            });
        }
        return mt;
    }

    @Override
    public E determinant(Matrix<E> A) {
         if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": determinant() not available");
         int n = A.rows();
         if (n != A.cols()) throw new IllegalArgumentException("Matrix must be square");
         boolean complex = isComplex(A);
         boolean single = isFloat(A);
         
         if (complex) {
             if (single) {
                 if (CGETRF != null) {
                     try (ResourceTracker tracker = new ResourceTracker()) {
                         Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
                         MemorySegment segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(A));
                         MemorySegment segIpiv = NativeSafe.allocate(arena, ValueLayout.JAVA_INT, n);
                         int info = (int) NativeSafe.invoke(CGETRF, LAPACK_ROW_MAJOR, n, n, segA, n, segIpiv);
                         if (info < 0) throw new IllegalArgumentException("CGETRF failed: illegal argument " + (-info));
                         if (info > 0) return (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.ZERO;
                         org.episteme.core.mathematics.numbers.complex.Complex det = org.episteme.core.mathematics.numbers.complex.Complex.of(1.0);
                         int swaps = 0;
                         for(int i=0; i<n; i++) {
                             float r = segA.getAtIndex(ValueLayout.JAVA_FLOAT, (long)(i*n + i)*2);
                             float im = segA.getAtIndex(ValueLayout.JAVA_FLOAT, (long)(i*n + i)*2 + 1);
                             det = det.multiply(org.episteme.core.mathematics.numbers.complex.Complex.of(r, im));
                             int p = segIpiv.getAtIndex(ValueLayout.JAVA_INT, (long)i);
                             if (p != (i + 1)) swaps++;
                         }
                         if (swaps % 2 != 0) det = det.negate();
                         return (E) (Object) det;
                     } catch (Throwable e) { 
                         throw new RuntimeException("Native complex float determinant failed", e);
                     }
                 }
             } else {
                 if (ZGETRF != null) {
                     try (ResourceTracker tracker = new ResourceTracker()) {
                         Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
                         MemorySegment segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(A));
                         MemorySegment segIpiv = NativeSafe.allocate(arena, ValueLayout.JAVA_INT, n);
                         int info = (int) NativeSafe.invoke(ZGETRF, LAPACK_ROW_MAJOR, n, n, segA, n, segIpiv);
                         if (info < 0) throw new IllegalArgumentException("ZGETRF failed: illegal argument " + (-info));
                         if (info > 0) return (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.ZERO;
                         org.episteme.core.mathematics.numbers.complex.Complex det = org.episteme.core.mathematics.numbers.complex.Complex.of(1.0);
                         int swaps = 0;
                         for(int i=0; i<n; i++) {
                             double r = segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long)(i*n + i)*2);
                             double im = segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long)(i*n + i)*2 + 1);
                             det = det.multiply(org.episteme.core.mathematics.numbers.complex.Complex.of(r, im));
                             int p = segIpiv.getAtIndex(ValueLayout.JAVA_INT, (long)i);
                             if (p != (i + 1)) swaps++;
                         }
                         if (swaps % 2 != 0) det = det.negate();
                         return (E) (Object) det;
                     } catch (Throwable e) { 
                         throw new RuntimeException("Native complex double determinant failed", e);
                     }
                 }
             }
         } else {
             if (single) {
                 if (SGETRF != null) {
                     try (Arena arena = Arena.ofConfined()) {
                         MemorySegment segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toFloatArray(A));
                         MemorySegment segIpiv = NativeSafe.allocate(arena, ValueLayout.JAVA_INT, n);
                         int info = (int) NativeSafe.invoke(SGETRF, LAPACK_ROW_MAJOR, n, n, segA, n, segIpiv);
                         if (info > 0) return createScalar(0.0, A);
                         float det = 1.0f;
                         for(int i=0; i<n; i++) {
                             det *= segA.getAtIndex(ValueLayout.JAVA_FLOAT, (long) i * n + i);
                             if (segIpiv.getAtIndex(ValueLayout.JAVA_INT, (long) i) != i + 1) det = -det;
                         }
                         return createScalar(det, A);
                     } catch (Throwable e) { 
                         throw new RuntimeException("Native real float determinant failed", e);
                     }
                 }
             } else {
                 if (DGETRF != null) {
                     try (Arena arena = Arena.ofConfined()) {
                         MemorySegment segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toDoubleArray(A));
                         MemorySegment segIpiv = NativeSafe.allocate(arena, ValueLayout.JAVA_INT, n);
                         int info = (int) NativeSafe.invoke(DGETRF, LAPACK_ROW_MAJOR, n, n, segA, n, segIpiv);
                         if (info > 0) return createScalar(0.0, A);
                         double det = 1.0;
                         for(int i=0; i<n; i++) {
                             det *= segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long) i * n + i);
                             if (segIpiv.getAtIndex(ValueLayout.JAVA_INT, (long) i) != i + 1) det = -det;
                         }
                         return createScalar(det, A);
                     } catch (Throwable e) { 
                         throw new RuntimeException("Native real double determinant failed", e);
                     }
                 }
             }
         }
         
         throw new UnsupportedOperationException(getName() + " does not support determinant for this configuration");
    }
    
    @Override
    public E trace(Matrix<E> a) {
        if (a.rows() != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        ensureAvailable();
        int n = a.rows();
        Ring<E> ring = a.getScalarRing();
        
        if (ring.zero() instanceof org.episteme.core.mathematics.numbers.real.Real || ring.getClass().getName().contains("Reals")) {
            E sum = ring.zero();
            for (int i = 0; i < n; i++) {
                sum = ring.add(sum, a.get(i, i));
            }
            return sum;
        } else if (ring.zero() instanceof org.episteme.core.mathematics.numbers.complex.Complex || ring.getClass().getName().contains("Complexes")) {
            Complex sum = Complex.ZERO;
            for (int i = 0; i < n; i++) {
                Object val = a.get(i, i);
                if (val instanceof Complex c) sum = sum.add(c);
                else sum = sum.add(Complex.of(getRealValue(val)));
            }
            return (E) sum;
        }
        throw new UnsupportedOperationException("Unsupported ring type for FFMBLAS trace: " + ring.getClass().getName());
    }

    private void ensureAvailable() {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": not available");
    }


    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public boolean isCompatible(Ring<?> ring) {
        if (ring == null) return false;
        Object zero = ring.zero();
        return ring instanceof org.episteme.core.mathematics.sets.Reals || 
               ring instanceof org.episteme.core.mathematics.sets.Complexes ||
               zero instanceof org.episteme.core.mathematics.numbers.real.Real ||
               zero instanceof org.episteme.core.mathematics.numbers.complex.Complex;
    }

    @Override
    public double score(OperationContext context) {
        if (!IS_AVAILABLE) return -1.0;
        double score = AutoTuningManager.getDynamicScore(getName(), context.getDimensionality(), getPriority());
        if (context.hasHint(OperationContext.Hint.GPU_RESIDENT)) score -= 50.0;
        if (context.getDataSize() > 0 && context.getDataSize() < 256) score -= 20.0;
        if (!context.hasHint(OperationContext.Hint.FLOAT32_OK)) score += 10.0;
        
        // Granular operation hints
        if (context.hasHint(OperationContext.Hint.MAT_MUL)) score += 5.0;
        if (context.hasHint(OperationContext.Hint.MAT_INV)) score += 5.0;
        if (context.hasHint(OperationContext.Hint.MAT_DET)) score += 5.0;
        if (context.hasHint(OperationContext.Hint.MAT_SOLVE)) score += 5.0;
        
        return score;
    }


    @Override
    public String getType() {
        return "linear-algebra";
    }

    @Override
    public String getEnvironmentInfo() {
        return IS_AVAILABLE ? "CPU (FFM-BLAS)" : "N/A";
    }

    @Override
    public String getName() {
        return "Native FFM BLAS Linear Algebra Backend";
    }

    @Override
    public String description() {
        return "High-performance Linear Algebra using Project Panama and OpenBLAS/MKL.";
    }

    @Override
    public QRResult<E> qr(Matrix<E> a) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": qr() not available");
        int m = a.rows();
        int n = a.cols();
        int k = Math.min(m, n);
        boolean complex = isComplex(a);
        boolean single = isFloat(a);

        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            if (complex) {
                if (single) {
                    MemorySegment segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(a));
                    MemorySegment tau = NativeSafe.allocate(arena, ValueLayout.JAVA_FLOAT, (long) k * 2);
                    if (logger.isDebugEnabled()) logger.debug("[FFM-BLAS] CGEQRF: m={}, n={}, lda={}", m, n, n);
                    int info = (int) NativeSafe.invoke(CGEQRF, LAPACK_ROW_MAJOR, m, n, segA, n, tau);
                    if (info != 0) throw new RuntimeException("CGEQRF failed with info: " + info);
                    float[] rData = new float[k * n * 2];
                    for (int i = 0; i < k; i++) for (int j = i; j < n; j++) {
                        rData[(i * n + j) * 2] = segA.getAtIndex(ValueLayout.JAVA_FLOAT, (long) (i * n + j) * 2);
                        rData[(i * n + j) * 2 + 1] = segA.getAtIndex(ValueLayout.JAVA_FLOAT, (long) (i * n + j) * 2 + 1);
                    }
                    Matrix<E> R = createDenseMatrix(rData, k, n, a);
                    if (logger.isDebugEnabled()) logger.debug("[FFM-BLAS] CUNGQR: m={}, k={}, lda={}", m, k, n);
                    info = (int) NativeSafe.invoke(CUNGQR, LAPACK_ROW_MAJOR, m, k, k, segA, n, tau);
                    if (info != 0) throw new RuntimeException("CUNGQR failed with info: " + info);
                    float[] qData = new float[m * k * 2];
                    for (int i = 0; i < m; i++) for (int j = 0; j < k; j++) {
                        qData[(i * k + j) * 2] = segA.getAtIndex(ValueLayout.JAVA_FLOAT, (long) (i * n + j) * 2);
                        qData[(i * k + j) * 2 + 1] = segA.getAtIndex(ValueLayout.JAVA_FLOAT, (long) (i * n + j) * 2 + 1);
                    }
                    return new QRResult<E>(createDenseMatrix(qData, m, k, a), R);
                } else {
                    MemorySegment segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                    MemorySegment tau = NativeSafe.allocate(arena, ValueLayout.JAVA_DOUBLE, (long) k * 2);
                    if (logger.isDebugEnabled()) logger.debug("[FFM-BLAS] ZGEQRF: m={}, n={}, lda={}", m, n, n);
                    int info = (int) NativeSafe.invoke(ZGEQRF, LAPACK_ROW_MAJOR, m, n, segA, n, tau);
                    if (info != 0) throw new RuntimeException("ZGEQRF failed: " + info);
                    double[] rData = new double[k * n * 2];
                    for (int i = 0; i < k; i++) for (int j = i; j < n; j++) {
                        rData[(i * n + j) * 2] = segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long) (i * n + j) * 2);
                        rData[(i * n + j) * 2 + 1] = segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long) (i * n + j) * 2 + 1);
                    }
                    Matrix<E> R = createDenseMatrix(rData, k, n, a);
                    if (logger.isDebugEnabled()) logger.debug("[FFM-BLAS] ZUNGQR: m={}, k={}, lda={}", m, k, n);
                    info = (int) NativeSafe.invoke(ZUNGQR, LAPACK_ROW_MAJOR, m, k, k, segA, n, tau);
                    if (info != 0) throw new RuntimeException("ZUNGQR failed: " + info);
                    double[] qData = new double[m * k * 2];
                    for (int i = 0; i < m; i++) for (int j = 0; j < k; j++) {
                        qData[(i * k + j) * 2] = segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long) (i * n + j) * 2);
                        qData[(i * k + j) * 2 + 1] = segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long) (i * n + j) * 2 + 1);
                    }
                    return new QRResult<E>(createDenseMatrix(qData, m, k, a), R);
                }
            } else {
                if (single) {
                    MemorySegment segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toFloatArray(a));
                    MemorySegment tau = NativeSafe.allocate(arena, ValueLayout.JAVA_FLOAT, (long) k);
                    if (logger.isDebugEnabled()) logger.debug("[FFM-BLAS] SGEQRF: m={}, n={}, lda={}", m, n, n);
                    int info = (int) NativeSafe.invoke(SGEQRF, LAPACK_ROW_MAJOR, m, n, segA, n, tau);
                    if (info != 0) throw new RuntimeException("SGEQRF failed: " + info);
                    float[] rData = new float[k * n];
                    for (int i = 0; i < k; i++) for (int j = i; j < n; j++) rData[i * n + j] = segA.getAtIndex(ValueLayout.JAVA_FLOAT, (long) i * n + j);
                    Matrix<E> R = createDenseMatrix(rData, k, n, a);
                    if (logger.isDebugEnabled()) logger.debug("[FFM-BLAS] SORGQR: m={}, k={}, lda={}", m, k, n);
                    info = (int) NativeSafe.invoke(SORGQR, LAPACK_ROW_MAJOR, m, k, k, segA, n, tau);
                    if (info != 0) throw new RuntimeException("SORGQR failed: " + info);
                    float[] qData = new float[m * k];
                    for (int i = 0; i < m; i++) for (int j = 0; j < k; j++) qData[i * k + j] = segA.getAtIndex(ValueLayout.JAVA_FLOAT, (long) i * n + j);
                    return new QRResult<E>(createDenseMatrix(qData, m, k, a), R);
                } else {
                    MemorySegment segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
                    MemorySegment tau = NativeSafe.allocate(arena, ValueLayout.JAVA_DOUBLE, (long) k);
                    if (logger.isDebugEnabled()) logger.debug("[FFM-BLAS] DGEQRF: m={}, n={}, lda={}", m, n, n);
                    int info = (int) NativeSafe.invoke(DGEQRF, LAPACK_ROW_MAJOR, m, n, segA, n, tau);
                    if (info != 0) throw new RuntimeException("DGEQRF failed: " + info);
                    double[] rData = new double[k * n];
                    for (int i = 0; i < k; i++) for (int j = i; j < n; j++) rData[i * n + j] = segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long) i * n + j);
                    Matrix<E> R = createDenseMatrix(rData, k, n, a);
                    if (logger.isDebugEnabled()) logger.debug("[FFM-BLAS] DORGQR: m={}, k={}, lda={}", m, k, n);
                    info = (int) NativeSafe.invoke(DORGQR, LAPACK_ROW_MAJOR, m, k, k, segA, n, tau);
                    if (info != 0) throw new RuntimeException("DORGQR failed: " + info);
                    double[] qData = new double[m * k];
                    for (int i = 0; i < m; i++) for (int j = 0; j < k; j++) qData[i * k + j] = segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long) i * n + j);
                    return new QRResult<E>(createDenseMatrix(qData, m, k, a), R);
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException("FFM BLAS QR failed", t);
        }

    }

    @Override

    public SVDResult<E> svd(Matrix<E> a) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": svd() not available");
        int m = a.rows();
        int n = a.cols();
        int k = Math.min(m, n);
        boolean complex = isComplex(a);
        boolean single = isFloat(a);

        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment u, vt, superb;
            int info;

            if (complex) {
                if (single) {
                    MemorySegment segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(a));
                    MemorySegment s = NativeSafe.allocate(arena, ValueLayout.JAVA_FLOAT, (long) k);
                    u = NativeSafe.allocate(arena, ValueLayout.JAVA_FLOAT, (long) m * m * 2);
                    vt = NativeSafe.allocate(arena, ValueLayout.JAVA_FLOAT, (long) n * n * 2);
                    superb = NativeSafe.allocate(arena, ValueLayout.JAVA_FLOAT, (long) Math.max(1, k - 1));
                    info = (int) NativeSafe.invoke(CGESVD, LAPACK_ROW_MAJOR, (byte) 'A', (byte) 'A', m, n, segA, n, s, u, m, vt, n, superb);
                    if (info != 0) throw new RuntimeException("CGESVD failed with info: " + info);
                    
                    float[] sArr = s.toArray(ValueLayout.JAVA_FLOAT);
                    List<E> sList = new ArrayList<>(k);
                    for (float v : sArr) sList.add(castScalar(v, (Ring<E>) a.getScalarRing()));
                    Vector<E> S = new DenseVector<>(sList, (Ring<E>) a.getScalarRing());
                    Matrix<E> U = createDenseMatrix(u.toArray(ValueLayout.JAVA_FLOAT), m, m, a);
                    float[] vtArr = vt.toArray(ValueLayout.JAVA_FLOAT);
                    float[] vArr = new float[n * n * 2];
                    for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) {
                        vArr[(j * n + i) * 2] = vtArr[(i * n + j) * 2];
                        vArr[(j * n + i) * 2 + 1] = vtArr[(i * n + j) * 2 + 1];
                    }
                    Matrix<E> V = createDenseMatrix(vArr, n, n, a);
                    return new SVDResult<>(U, S, V);
                } else {
                    MemorySegment segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                    MemorySegment s = NativeSafe.allocate(arena, ValueLayout.JAVA_DOUBLE, (long) k);
                    u = NativeSafe.allocate(arena, ValueLayout.JAVA_DOUBLE, (long) m * m * 2);
                    vt = NativeSafe.allocate(arena, ValueLayout.JAVA_DOUBLE, (long) n * n * 2);
                    superb = NativeSafe.allocate(arena, ValueLayout.JAVA_DOUBLE, (long) Math.max(1, k - 1));
                    info = (int) NativeSafe.invoke(ZGESVD, LAPACK_ROW_MAJOR, (byte) 'A', (byte) 'A', m, n, segA, n, s, u, m, vt, n, superb);
                    if (info != 0) throw new RuntimeException("ZGESVD failed with info: " + info);
                    
                    double[] sArr = s.toArray(ValueLayout.JAVA_DOUBLE);
                    List<E> sList = new ArrayList<>(k);
                    Ring<E> ring = (Ring<E>) a.getScalarRing();
                    for (double v : sArr) sList.add(castScalar(v, ring));
                    Vector<E> S = Vector.of(sList, ring);
                    Matrix<E> U = createDenseMatrix(u.toArray(ValueLayout.JAVA_DOUBLE), m, m, a);
                    double[] vtArr = vt.toArray(ValueLayout.JAVA_DOUBLE);
                    // To get V from VT (conjugate transpose), we must transpose and conjugate
                    // Since it's already VT, V = (VT)* = conj(transpose(VT))
                    for (int i = 0; i < n; i++) {
                        for (int j = i; j < n; j++) {
                            int idx1 = (i * n + j) * 2;
                            int idx2 = (j * n + i) * 2;
                            
                            // Conjugate both elements (even if i=j)
                            vtArr[idx1 + 1] = -vtArr[idx1 + 1];
                            if (i != j) {
                                vtArr[idx2 + 1] = -vtArr[idx2 + 1];
                                
                                // Swap
                                double tmpR = vtArr[idx1];
                                double tmpI = vtArr[idx1 + 1];
                                vtArr[idx1] = vtArr[idx2];
                                vtArr[idx1 + 1] = vtArr[idx2 + 1];
                                vtArr[idx2] = tmpR;
                                vtArr[idx2 + 1] = tmpI;
                            }
                        }
                    }
                    Matrix<E> V = createDenseMatrix(vtArr, n, n, a);
                    return new SVDResult<>(U, S, V);
                }
            } else {
                if (single) {
                    MemorySegment segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toFloatArray(a));
                    MemorySegment s = NativeSafe.allocate(arena, ValueLayout.JAVA_FLOAT, (long) k);
                    u = NativeSafe.allocate(arena, ValueLayout.JAVA_FLOAT, (long) m * m);
                    vt = NativeSafe.allocate(arena, ValueLayout.JAVA_FLOAT, (long) n * n);
                    superb = NativeSafe.allocate(arena, ValueLayout.JAVA_FLOAT, (long) Math.max(1, k - 1));
                    info = (int) NativeSafe.invoke(SGESVD, LAPACK_ROW_MAJOR, (byte) 'A', (byte) 'A', m, n, segA, n, s, u, m, vt, n, superb);
                    if (info != 0) throw new RuntimeException("SGESVD failed: " + info);
                    
                    float[] sArr = s.toArray(ValueLayout.JAVA_FLOAT);
                    List<E> sList = new ArrayList<>(k);
                    for (float v : sArr) sList.add(castScalar(v, (Ring<E>) a.getScalarRing()));
                    Vector<E> S = new DenseVector<>(sList, (Ring<E>) a.getScalarRing());
                    Matrix<E> U = createDenseMatrix(u.toArray(ValueLayout.JAVA_FLOAT), m, m, a);
                    float[] vtArr = vt.toArray(ValueLayout.JAVA_FLOAT);
                    float[] vArr = new float[n * n];
                    for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) vArr[j * n + i] = vtArr[i * n + j];
                    Matrix<E> V = createDenseMatrix(vArr, n, n, a);
                    return new SVDResult<>(U, S, V);
                } else {
                    MemorySegment segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
                    MemorySegment s = NativeSafe.allocate(arena, ValueLayout.JAVA_DOUBLE, (long) k);
                    u = NativeSafe.allocate(arena, ValueLayout.JAVA_DOUBLE, (long) m * m);
                    vt = NativeSafe.allocate(arena, ValueLayout.JAVA_DOUBLE, (long) n * n);
                    superb = NativeSafe.allocate(arena, ValueLayout.JAVA_DOUBLE, (long) Math.max(1, k - 1));
                    info = (int) NativeSafe.invoke(DGESVD, LAPACK_ROW_MAJOR, (byte) 'A', (byte) 'A', m, n, segA, n, s, u, m, vt, n, superb);
                    if (info != 0) throw new RuntimeException("DGESVD failed: " + info);
                    
                    double[] sArr = s.toArray(ValueLayout.JAVA_DOUBLE);
                    List<E> sList = new ArrayList<>(k);
                    for (double v : sArr) sList.add(castScalar(v, (Ring<E>) a.getScalarRing()));
                    Vector<E> S = new DenseVector<>(sList, (Ring<E>) a.getScalarRing());
                    Matrix<E> U = createDenseMatrix(u.toArray(ValueLayout.JAVA_DOUBLE), m, m, a);
                    double[] vtArr = vt.toArray(ValueLayout.JAVA_DOUBLE);
                    double[] vArr = new double[n * n];
                    for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) vArr[j * n + i] = vtArr[i * n + j];
                    Matrix<E> V = createDenseMatrix(vArr, n, n, a);
                    return new SVDResult<>(U, S, V);
                }
            }
        } catch (Throwable t) { 
            throw new RuntimeException("FFM BLAS SVD failed", t);
        }

    }

    @Override

    public EigenResult<E> eigen(Matrix<E> a) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": eigen() not available");
        int n = a.rows();
        boolean complex = isComplex(a);
        boolean single = isFloat(a);

        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment w, segA;
            int info;

            if (complex) {
                if (single) {
                    w = NativeSafe.allocate(arena, ValueLayout.JAVA_FLOAT, n);
                    segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(a));
                    info = (int) NativeSafe.invoke(CHEEV, LAPACK_ROW_MAJOR, (byte) 'V', (byte) 'U', n, segA, n, w);
                    if (info != 0) throw new RuntimeException("CHEEV failed: " + info);
                    float[] eigenvalues = w.toArray(ValueLayout.JAVA_FLOAT);
                    float[] eigenvectorsVec = segA.toArray(ValueLayout.JAVA_FLOAT);
                    
                    Integer[] idx = new Integer[n];
                    for (int i = 0; i < n; i++) idx[i] = i;
                    java.util.Arrays.sort(idx, (i1, i2) -> Float.compare(Math.abs(eigenvalues[i2]), Math.abs(eigenvalues[i1])));
                    
                    float[] sortedW = new float[n];
                    float[] sortedV = new float[n * n * 2];
                    for (int i = 0; i < n; i++) {
                        sortedW[i] = eigenvalues[idx[i]];
                        for (int j = 0; j < n; j++) {
                            sortedV[(j * n + i) * 2] = eigenvectorsVec[(j * n + idx[i]) * 2];
                            sortedV[(j * n + i) * 2 + 1] = eigenvectorsVec[(j * n + idx[i]) * 2 + 1];
                        }
                    }
                    
                    E[] evData = (E[]) new Object[n];
                    for (int i = 0; i < n; i++) evData[i] = castScalar(sortedW[i], (Ring<E>) a.getScalarRing());
                    Vector<E> vW = Vector.of(java.util.Arrays.asList(evData), (Ring<E>) a.getScalarRing());
                    return new EigenResult<E>(createDenseMatrix(sortedV, n, n, a), vW);
                } else {
                    w = NativeSafe.allocate(arena, ValueLayout.JAVA_DOUBLE, n);
                    segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                    info = (int) NativeSafe.invoke(ZHEEV, LAPACK_ROW_MAJOR, (byte) 'V', (byte) 'U', n, segA, n, w);
                    if (info != 0) throw new RuntimeException("ZHEEV failed: " + info);
                    double[] eigenvalues = w.toArray(ValueLayout.JAVA_DOUBLE);
                    double[] eigenvectorsVec = segA.toArray(ValueLayout.JAVA_DOUBLE);
                    
                    Integer[] idx = new Integer[n];
                    for (int i = 0; i < n; i++) idx[i] = i;
                    java.util.Arrays.sort(idx, (i1, i2) -> Double.compare(Math.abs(eigenvalues[i2]), Math.abs(eigenvalues[i1])));
                    
                    double[] sortedW = new double[n];
                    double[] sortedV = new double[n * n * 2];
                    for (int i = 0; i < n; i++) {
                        sortedW[i] = eigenvalues[idx[i]];
                        for (int j = 0; j < n; j++) {
                            sortedV[(j * n + i) * 2] = eigenvectorsVec[(j * n + idx[i]) * 2];
                            sortedV[(j * n + i) * 2 + 1] = eigenvectorsVec[(j * n + idx[i]) * 2 + 1];
                        }
                    }
                    
                    E[] evData = (E[]) new Object[n];
                    for (int i = 0; i < n; i++) evData[i] = castScalar(sortedW[i], (Ring<E>) a.getScalarRing());
                    Vector<E> vW = Vector.of(java.util.Arrays.asList(evData), (Ring<E>) a.getScalarRing());
                    return new EigenResult<E>(createDenseMatrix(sortedV, n, n, a), vW);
                }
            } else {
                if (single) {
                    w = NativeSafe.allocate(arena, ValueLayout.JAVA_FLOAT, n);
                    segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toFloatArray(a));
                    info = (int) NativeSafe.invoke(SSYEV, LAPACK_ROW_MAJOR, (byte) 'V', (byte) 'U', n, segA, n, w);
                    if (info != 0) throw new RuntimeException("SSYEV failed: " + info);
                    float[] eigenvalues = w.toArray(ValueLayout.JAVA_FLOAT);
                    float[] eigenvectorsVec = segA.toArray(ValueLayout.JAVA_FLOAT);
                    
                    Integer[] idx = new Integer[n];
                    for (int i = 0; i < n; i++) idx[i] = i;
                    java.util.Arrays.sort(idx, (i1, i2) -> Float.compare(Math.abs(eigenvalues[i2]), Math.abs(eigenvalues[i1])));
                    
                    float[] sortedW = new float[n];
                    float[] sortedV = new float[n * n];
                    for (int i = 0; i < n; i++) {
                        sortedW[i] = eigenvalues[idx[i]];
                        for (int j = 0; j < n; j++) sortedV[j * n + i] = eigenvectorsVec[j * n + idx[i]];
                    }
                    
                    E[] evData = (E[]) new Object[n];
                    for (int i = 0; i < n; i++) evData[i] = castScalar(sortedW[i], (Ring<E>) a.getScalarRing());
                    Vector<E> vW = Vector.of(java.util.Arrays.asList(evData), (Ring<E>) a.getScalarRing());
                    return new EigenResult<E>(createDenseMatrix(sortedV, n, n, a), vW);
                } else {
                    w = NativeSafe.allocate(arena, ValueLayout.JAVA_DOUBLE, n);
                    segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
                    info = (int) NativeSafe.invoke(DSYEV, LAPACK_ROW_MAJOR, (byte) 'V', (byte) 'U', n, segA, n, w);
                    if (info != 0) throw new RuntimeException("DSYEV failed: " + info);
                    double[] eigenvalues = w.toArray(ValueLayout.JAVA_DOUBLE);
                    double[] eigenvectorsVec = segA.toArray(ValueLayout.JAVA_DOUBLE);
                    
                    Integer[] idx = new Integer[n];
                    for (int i = 0; i < n; i++) idx[i] = i;
                    java.util.Arrays.sort(idx, (i1, i2) -> Double.compare(Math.abs(eigenvalues[i2]), Math.abs(eigenvalues[i1])));
                    
                    double[] sortedW = new double[n];
                    double[] sortedV = new double[n * n];
                    for (int i = 0; i < n; i++) {
                        sortedW[i] = eigenvalues[idx[i]];
                        for (int j = 0; j < n; j++) sortedV[j * n + i] = eigenvectorsVec[j * n + idx[i]];
                    }
                    
                    E[] evData = (E[]) new Object[n];
                    for (int i = 0; i < n; i++) evData[i] = castScalar(sortedW[i], (Ring<E>) a.getScalarRing());
                    Vector<E> vW = Vector.of(java.util.Arrays.asList(evData), (Ring<E>) a.getScalarRing());
                    return new EigenResult<E>(createDenseMatrix(sortedV, n, n, a), vW);
                }
            }
        } catch (Throwable t) { 
            throw new RuntimeException("FFM BLAS Eigen failed", t);
        }

    }


    @Override
    public CholeskyResult<E> cholesky(Matrix<E> a) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": cholesky() not available");
        int n = a.rows();
        if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        boolean complex = isComplex(a);
        boolean single = isFloat(a);

        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment segA;
            int info;
            if (complex) {
                if (single) {
                    segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(a));
                    info = (int) NativeSafe.invoke(CPOTRF, LAPACK_ROW_MAJOR, (byte) 'L', n, segA, n);
                    if (info != 0) throw new RuntimeException("CPOTRF failed: " + info);
                    float[] data = segA.toArray(ValueLayout.JAVA_FLOAT);
                    // Zero out upper part
                    for (int i = 0; i < n; i++) {
                        for (int j = i + 1; j < n; j++) {
                            data[(i * n + j) * 2] = 0;
                            data[(i * n + j) * 2 + 1] = 0;
                        }
                    }
                    return new CholeskyResult<>(createDenseMatrix(data, n, n, a));
                } else {
                    segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                    info = (int) NativeSafe.invoke(ZPOTRF, LAPACK_ROW_MAJOR, (byte) 'L', n, segA, n);
                    if (info != 0) throw new RuntimeException("ZPOTRF failed: " + info);
                    double[] data = segA.toArray(ValueLayout.JAVA_DOUBLE);
                    for (int i = 0; i < n; i++) {
                        for (int j = i + 1; j < n; j++) {
                            data[(i * n + j) * 2] = 0;
                            data[(i * n + j) * 2 + 1] = 0;
                        }
                    }
                    return new CholeskyResult<>(createDenseMatrix(data, n, n, a));
                }
            } else {
                if (single) {
                    segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toFloatArray(a));
                    info = (int) NativeSafe.invoke(SPOTRF, LAPACK_ROW_MAJOR, (byte) 'L', n, segA, n);
                    if (info != 0) throw new RuntimeException("SPOTRF failed: " + info);
                    float[] data = segA.toArray(ValueLayout.JAVA_FLOAT);
                    for (int i = 0; i < n; i++) {
                        for (int j = i + 1; j < n; j++) data[i * n + j] = 0;
                    }
                    return new CholeskyResult<>(createDenseMatrix(data, n, n, a));
                } else {
                    segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
                    info = (int) NativeSafe.invoke(DPOTRF, LAPACK_ROW_MAJOR, (byte) 'L', n, segA, n);
                    if (info != 0) throw new RuntimeException("DPOTRF failed: " + info);
                    double[] data = segA.toArray(ValueLayout.JAVA_DOUBLE);
                    for (int i = 0; i < n; i++) {
                        for (int j = i + 1; j < n; j++) data[i * n + j] = 0;
                    }
                    return new CholeskyResult<>(createDenseMatrix(data, n, n, a));
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException("POTRF failed", t);
        }
    }

    @Override
    public int rank(Matrix<E> a) {
        SVDResult<E> svdResult = svd(a);
        Vector<E> s = svdResult.S();
        int rank = 0;
        double tol = isFloat(a) ? 1e-6 : 1e-12;
        for (int i = 0; i < s.dimension(); i++) {
            if (getRealValue(s.get(i)) > tol) rank++;
        }
        return rank;
    }

    @Override
    public E conditionNumber(Matrix<E> a) {
        SVDResult<E> svdResult = svd(a);
        Vector<E> s = svdResult.S();
        if (s.dimension() == 0) return null;
        double max = getRealValue(s.get(0));
        double min = getRealValue(s.get(s.dimension() - 1));
        if (min < 1e-18) min = 1e-18;
        return castScalar(max / min, (Ring<E>) a.getScalarRing());
    }




        


    private Matrix<E> createDenseMatrixGeneric(E[][] data, Ring<E> ring) {
        int rows = data.length;
        int cols = data[0].length;
        E[] flatData = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), rows * cols);
        for (int i = 0; i < rows; i++) {
            System.arraycopy(data[i], 0, flatData, i * cols, cols);
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<>(
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<E>(rows, cols, flatData),
            this, ring);
    }

    @Override
    public void shutdown() {
        // No-op for FFM BLAS.
    }




    private boolean isComplex(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Matrix<?> m) return isComplexRing(m.getScalarRing());
        if (obj instanceof Vector<?> v) return isComplexRing(v.getScalarRing());
        return obj instanceof org.episteme.core.mathematics.numbers.complex.Complex;
    }

    private boolean isComplexRing(Ring<?> ring) {
        if (ring == null) return false;
        return ring instanceof org.episteme.core.mathematics.sets.Complexes || ring.zero() instanceof org.episteme.core.mathematics.numbers.complex.Complex;
    }

    private boolean isRealRing(Ring<?> ring) {
        return ring instanceof org.episteme.core.mathematics.sets.Reals;
    }

    private boolean isFloat(Object obj) {
        if (obj == null) return false;
        if (obj instanceof org.episteme.core.mathematics.numbers.real.RealFloat) return true;
        if (obj instanceof org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix) return false;
        if (obj instanceof org.episteme.nativ.mathematics.linearalgebra.matrices.NativeRealDoubleMatrix) return false;
        if (obj instanceof org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector) return false;

        boolean result = false;
        if (obj instanceof Matrix<?> m) {
            // Check elements if possible
            if (m.rows() > 0 && m.cols() > 0) {
                try {
                    Object first = m.get(0, 0);
                    if (first instanceof org.episteme.core.mathematics.numbers.real.RealFloat) result = true;
                    else if (first instanceof org.episteme.core.mathematics.numbers.real.RealDouble) result = false;
                    else if (first instanceof org.episteme.core.mathematics.numbers.complex.Complex c) {
                        if (c.getReal() instanceof org.episteme.core.mathematics.numbers.real.RealFloat) result = true;
                        else if (c.getReal() instanceof org.episteme.core.mathematics.numbers.real.RealDouble) result = false;
                        else result = isFloatRing(m.getScalarRing());
                    }
                    else result = isFloatRing(m.getScalarRing());
                } catch (Exception e) {
                    result = isFloatRing(m.getScalarRing());
                }
            } else {
                result = isFloatRing(m.getScalarRing());
            }
        } else if (obj instanceof Vector<?> v) {
            if (v.dimension() > 0) {
                try {
                    Object first = v.get(0);
                    if (first instanceof org.episteme.core.mathematics.numbers.real.RealFloat) result = true;
                    else if (first instanceof org.episteme.core.mathematics.numbers.real.RealDouble) result = false;
                    else if (first instanceof org.episteme.core.mathematics.numbers.complex.Complex c) {
                        if (c.getReal() instanceof org.episteme.core.mathematics.numbers.real.RealFloat) result = true;
                        else if (c.getReal() instanceof org.episteme.core.mathematics.numbers.real.RealDouble) result = false;
                        else result = isFloatRing(v.getScalarRing());
                    }
                    else result = isFloatRing(v.getScalarRing());
                } catch (Exception e) {
                    result = isFloatRing(v.getScalarRing());
                }
            } else {
                result = isFloatRing(v.getScalarRing());
            }
        }
        
        return result;
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
            boolean isFast = org.episteme.core.mathematics.context.MathContext.getCurrent().getRealPrecision() == org.episteme.core.mathematics.context.MathContext.RealPrecision.FAST;
            return isFast;
        }
        if (ring instanceof org.episteme.core.mathematics.sets.Complexes || ring.getClass().getName().contains("Complexes")) {
            boolean isFast = org.episteme.core.mathematics.context.MathContext.getCurrent().getRealPrecision() == org.episteme.core.mathematics.context.MathContext.RealPrecision.FAST;
            return isFast;
        }
        
        return false;
    }

    @SuppressWarnings("unchecked")
    private E castScalar(Object val, Ring<E> ring) {
        if (val == null) return ring.zero();
        
        if (isComplexRing(ring)) {
            if (val instanceof Complex) return (E) val;
            if (val instanceof Real r) return (E) Complex.of(r);
            if (val instanceof Number n) return (E) Complex.of(n.doubleValue());
        }
        if (isRealRing(ring)) {
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
        try {
            String name = obj.getClass().getName();
            if (name.equals("org.episteme.core.mathematics.numbers.complex.Complex")) {
                Object re = obj.getClass().getMethod("real").invoke(obj);
                Object im = obj.getClass().getMethod("imaginary").invoke(obj);
                return isZero(re, null) && isZero(im, null);
            }
            if (name.contains("Real")) {
                try {
                    java.math.BigDecimal bd = (java.math.BigDecimal) obj.getClass().getMethod("bigDecimalValue").invoke(obj);
                    return bd.signum() == 0;
                } catch (Exception e) {
                    double val = (double) obj.getClass().getMethod("doubleValue").invoke(obj);
                    return val == 0;
                }
            }
        } catch (Exception e) {}
        return false;
    }

    private double getRealValue(Object obj) {
        if (obj == null) return 0.0;
        if (obj instanceof Number n) return n.doubleValue();
        try {
            String name = obj.getClass().getName();
            if (name.equals("org.episteme.core.mathematics.numbers.complex.Complex")) {
                return getRealValue(obj.getClass().getMethod("real").invoke(obj));
            }
            return (double) obj.getClass().getMethod("doubleValue").invoke(obj);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private Real getReal(Object obj) {
        if (obj == null) return Real.ZERO;
        if (obj instanceof Real r) return r;
        if (obj instanceof Complex c) return c.getReal();
        return Real.of(getRealValue(obj));
    }

    private Complex getComplexValue(Object obj) {
        if (obj == null) return Complex.ZERO;
        if (obj instanceof Complex c) return c;
        return Complex.of(getRealValue(obj));
    }

    private boolean isFloat(Vector<E> v) {
        if (v.dimension() > 0) {
            E val = v.get(0);
            if (val != null && val.getClass().getSimpleName().equals("RealFloat")) return true;
            if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) {
                org.episteme.core.mathematics.numbers.complex.Complex c = (org.episteme.core.mathematics.numbers.complex.Complex) val;
                return c.getReal() != null && c.getReal().getClass().getSimpleName().equals("RealFloat");
            }
        }
        return false;
    }

    private double[] toDoubleArray(Matrix<E> m) {
        if (m instanceof org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix) {
            return ((org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix) m).toDoubleArray();
        }
        int rows = m.rows();
        int cols = m.cols();
        double[] data = new double[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i * cols + j] = ((Real) (Object) m.get(i, j)).doubleValue();
            }
        }
        return data;
    }

    private double[] toDoubleArray(Vector<E> v) {
        if (v instanceof org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector) {
            return ((org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector) v).toDoubleArray();
        }
        int n = v.dimension();
        double[] data = new double[n];
        for (int i = 0; i < n; i++) {
            data[i] = getRealValue(v.get(i));
        }
        return data;
    }

    private double[] toInterlacedDoubleArray(Matrix<E> m) {
        int rows = m.rows();
        int cols = m.cols();
        double[] data = new double[rows * cols * 2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Complex c = getComplexValue(m.get(i, j));
                data[(i * cols + j) * 2] = c.real();
                data[(i * cols + j) * 2 + 1] = c.imaginary();
            }
        }
        return data;
    }

    private double[] toInterlacedDoubleArray(Vector<E> v) {
        int n = v.dimension();
        double[] data = new double[n * 2];
        for (int i = 0; i < n; i++) {
            Complex c = getComplexValue(v.get(i));
            data[i * 2] = c.real();
            data[i * 2 + 1] = c.imaginary();
        }
        return data;
    }


    private float[] toFloatArray(Matrix<E> m) {
        int rows = m.rows();
        int cols = m.cols();
        float[] data = new float[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i * cols + j] = (float) ((Real) (Object) m.get(i, j)).doubleValue();
            }
        }
        return data;
    }

    private float[] toFloatArray(Vector<E> v) {
        int n = v.dimension();
        float[] data = new float[n];
        for (int i = 0; i < n; i++) {
            data[i] = (float) getRealValue(v.get(i));
        }
        return data;
    }

    private float[] toInterlacedFloatArray(Matrix<E> m) {
        int rows = m.rows();
        int cols = m.cols();
        float[] data = new float[rows * cols * 2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Complex c = getComplexValue(m.get(i, j));
                data[(i * cols + j) * 2] = (float) c.real();
                data[(i * cols + j) * 2 + 1] = (float) c.imaginary();
            }
        }
        return data;
    }

    private float[] toInterlacedFloatArray(Vector<E> v) {
        int n = v.dimension();
        float[] data = new float[n * 2];
        for (int i = 0; i < n; i++) {
            Complex c = getComplexValue(v.get(i));
            data[i * 2] = (float) c.real();
            data[i * 2 + 1] = (float) c.imaginary();
        }
        return data;
    }


    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": Matrix multiply() not available");
        int m = a.rows();
        int k = a.cols();
        int n = b.cols();
        if (k != b.rows()) throw new IllegalArgumentException("Dimension mismatch: " + k + " != " + b.rows());

        boolean complex = isComplex(a);
        boolean single = isFloat(a);

        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment segA = getSegment(a, arena, tracker);
            MemorySegment segB = getSegment(b, arena, tracker);
            
            if (complex) {
                if (single) {
                    MemorySegment segC = NativeSafe.allocate(arena, ValueLayout.JAVA_FLOAT, (long) m * n * 2);
                    MemorySegment alpha = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, 1.0f, 0.0f);
                    MemorySegment beta = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, 0.0f, 0.0f);
                    NativeSafe.invoke(CGEMM, CblasRowMajor, CblasNoTrans, CblasNoTrans, m, n, k, alpha, segA, k, segB, n, beta, segC, n);
                    return createDenseMatrix(segC.toArray(ValueLayout.JAVA_FLOAT), m, n, a);
                } else {
                    MemorySegment segC = NativeSafe.allocate(arena, ValueLayout.JAVA_DOUBLE, (long) m * n * 2);
                    MemorySegment alpha = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, 1.0, 0.0);
                    MemorySegment beta = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, 0.0, 0.0);
                    NativeSafe.invoke(ZGEMM, CblasRowMajor, CblasNoTrans, CblasNoTrans, m, n, k, alpha, segA, k, segB, n, beta, segC, n);
                    return createDenseMatrix(segC.toArray(ValueLayout.JAVA_DOUBLE), m, n, a);
                }
            } else {
                if (single) {
                    MemorySegment segC = NativeSafe.allocate(arena, ValueLayout.JAVA_FLOAT, (long) m * n);
                    if (logger.isDebugEnabled()) logger.debug("[FFM-BLAS] SGEMM: m={}, n={}, k={}, lda={}, ldb={}, ldc={}", m, n, k, k, n, n);
                    NativeSafe.invoke(SGEMM, CblasRowMajor, CblasNoTrans, CblasNoTrans, m, n, k, 1.0f, segA, k, segB, n, 0.0f, segC, n);
                    return createDenseMatrix(segC.toArray(ValueLayout.JAVA_FLOAT), m, n, a);
                } else {
                    Arena resultArena = Arena.ofAuto();
                    MemorySegment segC = NativeSafe.allocate(resultArena, ValueLayout.JAVA_DOUBLE, (long) m * n);
                    if (logger.isDebugEnabled()) logger.debug("[FFM-BLAS] DGEMM: m={}, n={}, k={}, lda={}, ldb={}, ldc={}", m, n, k, k, n, n);
                    NativeSafe.invoke(DGEMM, CblasRowMajor, CblasNoTrans, CblasNoTrans, m, n, k, 1.0, segA, k, segB, n, 0.0, segC, n);
                    return createZeroCopyDoubleMatrix(segC, m, n, resultArena, a);
                }
            }
        } catch (Throwable t) { 
            throw new RuntimeException("GEMM failed", t); 
        }
    }

    private E createScalar(double val, Object ref) {
        return createScalar(val, 0.0, ref);
    }

    private E createScalar(double real, double imag, Object ref) {
        if (isComplex(ref)) return (E) Complex.of(real, imag);
        if (isFloat(ref)) return (E) org.episteme.core.mathematics.numbers.real.RealFloat.of((float)real);
        return (E) Real.of(real);
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": matrix-vector multiply() not available");
        if (isComplex(a)) return multiplyComplex(a, b);
        
        int m = a.rows();
        int n = a.cols();
        if (n != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");

        boolean single = isFloat(a);

        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment segA = getSegment(a, arena, tracker);
            MemorySegment segX = getSegment(b, arena, tracker);
            
            if (single) {
                MemorySegment segY = NativeSafe.allocate(arena, ValueLayout.JAVA_FLOAT, (long) m);
                if (logger.isDebugEnabled()) logger.debug("[FFM-BLAS] SGEMV: m={}, n={}, lda={}", m, n, n);
                NativeSafe.invoke(SGEMV, CblasRowMajor, CblasNoTrans, m, n, 1.0f, segA, n, segX, 1, 0.0f, segY, 1);
                return createDenseVector(segY.toArray(ValueLayout.JAVA_FLOAT), m, a);
            } else {
                Arena resultArena = Arena.ofAuto();
                MemorySegment segY = NativeSafe.allocate(resultArena, ValueLayout.JAVA_DOUBLE, (long) m);
                if (logger.isDebugEnabled()) logger.debug("[FFM-BLAS] DGEMV: m={}, n={}, lda={}", m, n, n);
                NativeSafe.invoke(DGEMV, CblasRowMajor, CblasNoTrans, m, n, 1.0, segA, n, segX, 1, 0.0, segY, 1);
                return createZeroCopyDoubleVector(segY, m, resultArena, a);
            }
        } catch (Throwable t) { 
            throw new RuntimeException((single ? "SGEMV" : "DGEMV") + " failed", t); 
        }
    }

    private Vector<E> multiplyComplex(Matrix<E> a, Vector<E> b) {
        int m = a.rows();
        int n = a.cols();

        if (isFloat(a)) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(a));
                MemorySegment segX = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(b));
                MemorySegment segY = NativeSafe.allocate(arena, ValueLayout.JAVA_FLOAT, (long) m * 2);
                MemorySegment alpha = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, 1.0f, 0.0f);
                MemorySegment beta = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, 0.0f, 0.0f);
                NativeSafe.invoke(CGEMV, CblasRowMajor, CblasNoTrans, m, n, alpha, segA, n, segX, 1, beta, segY, 1);
                float[] resData = segY.toArray(ValueLayout.JAVA_FLOAT);
                return createDenseVector(resData, m, a);
            } catch (Throwable t) { throw new RuntimeException("CGEMV failed", t); }
        }

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
            MemorySegment segX = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(b));
            MemorySegment segY = NativeSafe.allocate(arena, ValueLayout.JAVA_DOUBLE, (long) m * 2);

            MemorySegment alpha = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, 1.0, 0.0);
            MemorySegment beta = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, 0.0, 0.0);

            if (ZGEMV != null) {
                NativeSafe.invoke(ZGEMV, CblasRowMajor, CblasNoTrans, m, n, alpha, segA, n, segX, 1, beta, segY, 1);
            } else {
                throw new UnsupportedOperationException("ZGEMV not available");
            }

            double[] resData = segY.toArray(ValueLayout.JAVA_DOUBLE);
            return createDenseVector(resData, m, a);
        } catch (Throwable t) {
            throw new RuntimeException("ZGEMV failed", t);
        }
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": dot() not available");
        int n = a.dimension();
        boolean complex = isComplex(a);
        boolean single = isFloat(a);

        if (complex) {
            if (single) {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(a));
                    MemorySegment segB = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(b));
                    MemorySegment res = NativeSafe.allocate(arena, ValueLayout.JAVA_FLOAT, 2);
                    // Single precision complex dot product (conjugated)
                    NativeSafe.invoke(CDOTC, n, segB, 1, segA, 1, res);
                    return createScalar(res.get(ValueLayout.JAVA_FLOAT, 0), res.get(ValueLayout.JAVA_FLOAT, 4), a);
                } catch (Throwable t) { throw new RuntimeException("CDOTC failed", t); }
            } else {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                    MemorySegment segB = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(b));
                    MemorySegment res = NativeSafe.allocate(arena, ValueLayout.JAVA_DOUBLE, 2);
                    NativeSafe.invoke(ZDOTC, n, segB, 1, segA, 1, res);
                    return createScalar(res.get(ValueLayout.JAVA_DOUBLE, 0), res.get(ValueLayout.JAVA_DOUBLE, 8), a);
                } catch (Throwable t) { throw new RuntimeException("ZDOTC failed", t); }
            }
        }
        
        if (single) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment segX = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toFloatArray(a));
                MemorySegment segY = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toFloatArray(b));
                return createScalar((double)(float) NativeSafe.invoke(SDOT, n, segX, 1, segY, 1), a);
            } catch (Throwable t) { throw new RuntimeException("SDOT failed", t); }
        }

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segX = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
            MemorySegment segY = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toDoubleArray(b));
            return createScalar((double) NativeSafe.invoke(DDOT, n, segX, 1, segY, 1), a);
        } catch (Throwable t) { throw new RuntimeException("DDOT failed", t); }
    }
    
    @Override

    public E norm(Vector<E> a) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": norm() not available");
        int n = a.dimension();
        boolean complex = isComplex(a);
        boolean single = isFloat(a);

        if (complex) {
            if (single) {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment segX = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(a));
                    return createScalar((double)(float) NativeSafe.invoke(SCNRM2, n, segX, 1), a);
                } catch (Throwable t) { throw new RuntimeException("SCNRM2 failed", t); }
            } else {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment segX = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                    return createScalar((double) NativeSafe.invoke(DZNRM2, n, segX, 1), a);
                } catch (Throwable t) { throw new RuntimeException("DZNRM2 failed", t); }
            }
        }
        
        if (single) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment segX = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toFloatArray(a));
                return createScalar((double)(float) NativeSafe.invoke(SNRM2, n, segX, 1), a);
            } catch (Throwable t) { throw new RuntimeException("SNRM2 failed", t); }
        }

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segX = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
            return createScalar((double) NativeSafe.invoke(DNRM2, n, segX, 1), a);
        } catch (Throwable t) { throw new RuntimeException("DNRM2 failed", t); }
    }

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": Vector add() not available");
        int n = a.dimension();
        boolean complex = isComplex(a);
        boolean single = isFloat(a);

        if (complex) {
            if (single) {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment segX = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(a));
                    MemorySegment segY = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(b));
                    MemorySegment alpha = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, 1.0f, 0.0f);
                    NativeSafe.invoke(CAXPY, n, alpha, segX, 1, segY, 1);
                    float[] resData = segY.toArray(ValueLayout.JAVA_FLOAT);
                    return createDenseVector(resData, n, a);
                } catch (Throwable t) { throw new RuntimeException("CAXPY failed", t); }
            } else {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment segX = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                    MemorySegment segY = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(b));
                    MemorySegment alpha = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, 1.0, 0.0);
                     NativeSafe.invoke(ZAXPY, n, alpha, segX, 1, segY, 1);
                    double[] resData = segY.toArray(ValueLayout.JAVA_DOUBLE);
                    return createDenseVector(resData, n, a);
                } catch (Throwable t) { throw new RuntimeException("ZAXPY failed", t); }
            }
        }
        
        if (single) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment segX = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toFloatArray(a));
                MemorySegment segY = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toFloatArray(b));
                NativeSafe.invoke(SAXPY, n, 1.0f, segX, 1, segY, 1);
                float[] result = segY.toArray(ValueLayout.JAVA_FLOAT);
                return createDenseVector(result, n, a);
            } catch (Throwable t) { throw new RuntimeException("SAXPY failed", t); }
        }

        MemorySegment segY;
        Arena resultArena = Arena.ofAuto();
        try (Arena tempArena = Arena.ofConfined()) {
            MemorySegment segX = NativeSafe.allocateFrom(tempArena, ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
            segY = NativeSafe.allocateFrom(resultArena, ValueLayout.JAVA_DOUBLE, toDoubleArray(b));
            NativeSafe.invoke(DAXPY, n, 1.0, segX, 1, segY, 1);
        } catch (Throwable t) { throw new RuntimeException("DAXPY failed", t); }
        return createZeroCopyDoubleVector(segY, n, resultArena, a);
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": Vector subtract() not available");
        int n = a.dimension();
        boolean complex = isComplex(a);
        boolean single = isFloat(a);

        if (complex) {
            if (single) {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment segX = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(b));
                    MemorySegment segY = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(a));
                    MemorySegment alpha = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, -1.0f, 0.0f);
                    NativeSafe.invoke(CAXPY, n, alpha, segX, 1, segY, 1);
                    float[] resData = segY.toArray(ValueLayout.JAVA_FLOAT);
                    return createDenseVector(resData, n, a);
                } catch (Throwable t) { throw new RuntimeException("CAXPY failed", t); }
            } else {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment segX = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(b));
                    MemorySegment segY = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                    MemorySegment alpha = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, -1.0, 0.0);
                    NativeSafe.invoke(ZAXPY, n, alpha, segX, 1, segY, 1);
                    double[] resData = segY.toArray(ValueLayout.JAVA_DOUBLE);
                    return createDenseVector(resData, n, a);
                } catch (Throwable t) { throw new RuntimeException("ZAXPY complex failed", t); }
            }
        }
        
        if (single) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment segX = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toFloatArray(b));
                MemorySegment segY = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toFloatArray(a));
                NativeSafe.invoke(SAXPY, n, -1.0f, segX, 1, segY, 1);
                float[] result = segY.toArray(ValueLayout.JAVA_FLOAT);
                return createDenseVector(result, n, a);
            } catch (Throwable t) { throw new RuntimeException("SAXPY failed", t); }
        }

        MemorySegment segY;
        Arena resultArena = Arena.ofAuto();
        try (Arena tempArena = Arena.ofConfined()) {
            MemorySegment segX = NativeSafe.allocateFrom(tempArena, ValueLayout.JAVA_DOUBLE, toDoubleArray(b));
            segY = NativeSafe.allocateFrom(resultArena, ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
            NativeSafe.invoke(DAXPY, n, -1.0, segX, 1, segY, 1);
        } catch (Throwable t) { throw new RuntimeException("DAXPY failed", t); }
        return createZeroCopyDoubleVector(segY, n, resultArena, a);
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": Vector multiply() not available");
        int n = vector.dimension();
        boolean complex = isComplex(vector);
        boolean single = isFloat(vector);
        
        if (complex) {
            org.episteme.core.mathematics.numbers.complex.Complex cScalar = (scalar instanceof org.episteme.core.mathematics.numbers.complex.Complex c) ? c : org.episteme.core.mathematics.numbers.complex.Complex.of(getRealValue(scalar));
            if (single) {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment segX = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(vector));
                    MemorySegment alpha = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, (float) cScalar.real(), (float) cScalar.imaginary());
                    NativeSafe.invoke(CSCAL, n, alpha, segX, 1);
                    float[] resData = segX.toArray(ValueLayout.JAVA_FLOAT);
                    return createDenseVector(resData, n, vector);
                } catch (Throwable t) { throw new RuntimeException("CSCAL failed", t); }
            } else {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment segX = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(vector));
                    MemorySegment alpha = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, cScalar.real(), cScalar.imaginary());
                    NativeSafe.invoke(ZSCAL, n, alpha, segX, 1);
                    double[] resData = segX.toArray(ValueLayout.JAVA_DOUBLE);
                    return createDenseVector(resData, n, vector);
                } catch (Throwable t) { throw new RuntimeException("ZSCAL failed", t); }
            }
        }

        if (scalar == null) return vector;
        double s = getRealValue(scalar);
        if (single) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment segX = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toFloatArray(vector));
                NativeSafe.invoke(SSCAL, n, (float) s, segX, 1);
                float[] resData = segX.toArray(ValueLayout.JAVA_FLOAT);
                return createDenseVector(resData, n, vector);
            } catch (Throwable t) { throw new RuntimeException("SSCAL failed", t); }
        }

        MemorySegment segX;
        Arena resultArena = Arena.ofAuto();
        try {
            segX = NativeSafe.allocateFrom(resultArena, ValueLayout.JAVA_DOUBLE, toDoubleArray(vector));
            NativeSafe.invoke(DSCAL, n, s, segX, 1);
        } catch (Throwable t) { throw new RuntimeException("DSCAL failed", t); }
        return createZeroCopyDoubleVector(segX, n, resultArena, vector);
    }



    public Vector<E> divide(Vector<E> v, E scalar) {
        if (isComplex(v)) {
            try {
                Object sc = scalar;
                if (scalar.getClass().getName().equals("org.episteme.core.mathematics.numbers.complex.Complex")) {
                    return multiply(v, (E) scalar.getClass().getMethod("reciprocal").invoke(scalar));
                }
                double val = getRealValue(scalar);
                return multiply(v, createScalar(1.0 / val, v));
            } catch (Exception e) {
                return multiply(v, createScalar(0.0, v));
            }
        }
        double val = getRealValue(scalar);
        return multiply(v, createScalar(1.0 / val, v));
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": Matrix add() not available");
        int m = a.rows(), n = a.cols();
        boolean complex = isComplex(a);
        boolean single = isFloat(a);

        if (complex) {
            if (single) {
                try (Arena arena = Arena.ofConfined()) {
                    int len = m * n;
                    MemorySegment segX = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(a));
                    MemorySegment segY = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(b));
                    MemorySegment alpha = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, 1.0f, 0.0f);
                    NativeSafe.invoke(CAXPY, len, alpha, segX, 1, segY, 1);
                    float[] result = segY.toArray(ValueLayout.JAVA_FLOAT);
                    return createDenseMatrix(result, m, n, a);
                } catch (Throwable t) { throw new RuntimeException("CAXPY matrix add failed", t); }
            } else {
                try (Arena arena = Arena.ofConfined()) {
                    int len = m * n;
                    MemorySegment segX = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                    MemorySegment segY = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(b));
                    MemorySegment alpha = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, 1.0, 0.0);
                    NativeSafe.invoke(ZAXPY, len, alpha, segX, 1, segY, 1);
                    double[] result = segY.toArray(ValueLayout.JAVA_DOUBLE);
                    return createDenseMatrix(result, m, n, a);
                } catch (Throwable t) { throw new RuntimeException("ZAXPY matrix add failed", t); }
            }
        }

        if (single) {
            try (Arena arena = Arena.ofConfined()) {
                int len = m * n;
                MemorySegment segX = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toFloatArray(a));
                MemorySegment segY = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toFloatArray(b));
                NativeSafe.invoke(SAXPY, len, 1.0f, segX, 1, segY, 1);
                float[] result = segY.toArray(ValueLayout.JAVA_FLOAT);
                return createDenseMatrix(result, m, n, a);
            } catch (Throwable t) { throw new RuntimeException("SAXPY matrix add failed", t); }
        }

        MemorySegment segY;
        Arena resultArena = Arena.ofAuto();
        try (Arena tempArena = Arena.ofConfined()) {
            int len = m * n;
            MemorySegment segX = NativeSafe.allocateFrom(tempArena, ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
            segY = NativeSafe.allocateFrom(resultArena, ValueLayout.JAVA_DOUBLE, toDoubleArray(b));
            NativeSafe.invoke(DAXPY, len, 1.0, segX, 1, segY, 1);
        } catch (Throwable t) { throw new RuntimeException("DAXPY matrix add failed", t); }
        return createZeroCopyDoubleMatrix(segY, m, n, resultArena, a);
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": Matrix subtract() not available");
        int m = a.rows(), n = a.cols();
        boolean complex = isComplex(a);
        boolean single = isFloat(a);

        if (complex) {
            if (single) {
                try (Arena arena = Arena.ofConfined()) {
                    int len = m * n;
                    MemorySegment segX = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(b));
                    MemorySegment segY = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(a));
                    MemorySegment alpha = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, -1.0f, 0.0f);
                    NativeSafe.invoke(CAXPY, len, alpha, segX, 1, segY, 1);
                    float[] result = segY.toArray(ValueLayout.JAVA_FLOAT);
                    return createDenseMatrix(result, m, n, a);
                } catch (Throwable t) { throw new RuntimeException("CAXPY matrix subtract failed", t); }
            } else {
                try (Arena arena = Arena.ofConfined()) {
                    int len = m * n;
                    MemorySegment segX = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(b));
                    MemorySegment segY = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                    MemorySegment alpha = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, -1.0, 0.0);
                    NativeSafe.invoke(ZAXPY, len, alpha, segX, 1, segY, 1);
                    double[] result = segY.toArray(ValueLayout.JAVA_DOUBLE);
                    return createDenseMatrix(result, m, n, a);
                } catch (Throwable t) { throw new RuntimeException("ZAXPY matrix subtract failed", t); }
            }
        }

        if (single) {
            try (Arena arena = Arena.ofConfined()) {
                int len = m * n;
                MemorySegment segX = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toFloatArray(b));
                MemorySegment segY = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toFloatArray(a));
                NativeSafe.invoke(SAXPY, len, -1.0f, segX, 1, segY, 1);
                float[] result = segY.toArray(ValueLayout.JAVA_FLOAT);
                return createDenseMatrix(result, m, n, a);
            } catch (Throwable t) { throw new RuntimeException("SAXPY matrix subtract failed", t); }
        }

        MemorySegment segY;
        Arena resultArena = Arena.ofAuto();
        try (Arena tempArena = Arena.ofConfined()) {
            int len = m * n;
            MemorySegment segX = NativeSafe.allocateFrom(tempArena, ValueLayout.JAVA_DOUBLE, toDoubleArray(b));
            segY = NativeSafe.allocateFrom(resultArena, ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
            NativeSafe.invoke(DAXPY, len, -1.0, segX, 1, segY, 1);
        } catch (Throwable t) { throw new RuntimeException("DAXPY matrix subtract failed", t); }
        return createZeroCopyDoubleMatrix(segY, m, n, resultArena, a);
    }




    @Override
    public LUResult<E> lu(Matrix<E> a) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": lu() not available");
        int m = a.rows(), n = a.cols();
        boolean complex = isComplex(a);
        boolean single = isFloat(a);

        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment pFace = NativeSafe.allocate(arena, ValueLayout.JAVA_INT, Math.min(m, n));
            
            if (complex) {
                if (single) {
                    MemorySegment segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(a));
                    int info = (int) NativeSafe.invoke(CGETRF, LAPACK_ROW_MAJOR, m, n, segA, n, pFace);
                    if (info < 0) throw new ArithmeticException("CGETRF failed: " + info);
                    float[] resA = segA.toArray(ValueLayout.JAVA_FLOAT);
                    int[] ipiv = pFace.toArray(ValueLayout.JAVA_INT);
                    return reconstructLU(resA, ipiv, m, n, a, true);
                } else {
                    MemorySegment segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                    int info = (int) NativeSafe.invoke(ZGETRF, LAPACK_ROW_MAJOR, m, n, segA, n, pFace);
                    if (info < 0) throw new ArithmeticException("ZGETRF failed: " + info);
                    double[] resA = segA.toArray(ValueLayout.JAVA_DOUBLE);
                    int[] ipiv = pFace.toArray(ValueLayout.JAVA_INT);
                    return reconstructLU(resA, ipiv, m, n, a, true);
                }
            } else {
                if (single) {
                    MemorySegment segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toFloatArray(a));
                    int info = (int) NativeSafe.invoke(SGETRF, LAPACK_ROW_MAJOR, m, n, segA, n, pFace);
                    if (info < 0) throw new ArithmeticException("SGETRF failed: " + info);
                    float[] resA = segA.toArray(ValueLayout.JAVA_FLOAT);
                    int[] ipiv = pFace.toArray(ValueLayout.JAVA_INT);
                    return reconstructLU(resA, ipiv, m, n, a, false);
                } else {
                    MemorySegment segA = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
                    int info = (int) NativeSafe.invoke(DGETRF, LAPACK_ROW_MAJOR, m, n, segA, n, pFace);
                    if (info < 0) throw new ArithmeticException("DGETRF failed: " + info);
                    double[] resA = segA.toArray(ValueLayout.JAVA_DOUBLE);
                    int[] ipiv = pFace.toArray(ValueLayout.JAVA_INT);
                    return reconstructLU(resA, ipiv, m, n, a, false);
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException("FFM BLAS LU failed", t);
        }

    }

    private LUResult<E> reconstructLU(double[] packedData, int[] ipiv, int m, int n, Matrix<E> a, boolean complex) {
        int minMN = Math.min(m, n);
        double[] lData = complex ? new double[m * minMN * 2] : new double[m * minMN];
        double[] uData = complex ? new double[minMN * n * 2] : new double[minMN * n];

        if (complex) {
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < Math.min(i, n); j++) {
                    lData[(i * minMN + j) * 2] = packedData[(i * n + j) * 2];
                    lData[(i * minMN + j) * 2 + 1] = packedData[(i * n + j) * 2 + 1];
                }
                if (i < minMN) {
                    lData[(i * minMN + i) * 2] = 1.0;
                    lData[(i * minMN + i) * 2 + 1] = 0.0;
                }
            }
            for (int i = 0; i < minMN; i++) {
                for (int j = i; j < n; j++) {
                    uData[(i * n + j) * 2] = packedData[(i * n + j) * 2];
                    uData[(i * n + j) * 2 + 1] = packedData[(i * n + j) * 2 + 1];
                }
            }
        } else {
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < Math.min(i, n); j++) {
                    lData[i * minMN + j] = packedData[i * n + j];
                }
                if (i < minMN) lData[i * minMN + i] = 1.0;
            }
            for (int i = 0; i < minMN; i++) {
                for (int j = i; j < n; j++) {
                    uData[i * n + j] = packedData[i * n + j];
                }
            }
        }
        return buildLUResultObject(lData, uData, ipiv, m, minMN, n, a, complex);
    }

    private LUResult<E> reconstructLU(float[] packedData, int[] ipiv, int m, int n, Matrix<E> a, boolean complex) {
        int minMN = Math.min(m, n);
        float[] lData = complex ? new float[m * minMN * 2] : new float[m * minMN];
        float[] uData = complex ? new float[minMN * n * 2] : new float[minMN * n];

        if (complex) {
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < Math.min(i, n); j++) {
                    lData[(i * minMN + j) * 2] = packedData[(i * n + j) * 2];
                    lData[(i * minMN + j) * 2 + 1] = packedData[(i * n + j) * 2 + 1];
                }
                if (i < minMN) {
                    lData[(i * minMN + i) * 2] = 1.0f;
                    lData[(i * minMN + i) * 2 + 1] = 0.0f;
                }
            }
            for (int i = 0; i < minMN; i++) {
                for (int j = i; j < n; j++) {
                    uData[(i * n + j) * 2] = packedData[(i * n + j) * 2];
                    uData[(i * n + j) * 2 + 1] = packedData[(i * n + j) * 2 + 1];
                }
            }
        } else {
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < Math.min(i, n); j++) {
                    lData[i * minMN + j] = packedData[i * n + j];
                }
                if (i < minMN) lData[i * minMN + i] = 1.0f;
            }
            for (int i = 0; i < minMN; i++) {
                for (int j = i; j < n; j++) {
                    uData[i * n + j] = packedData[i * n + j];
                }
            }
        }
        return buildLUResultObject(lData, uData, ipiv, m, minMN, n, a, complex);
    }

    private LUResult<E> buildLUResultObject(Object lData, Object uData, int[] ipiv, int m, int minMN, int n, Matrix<E> a, boolean complex) {
        Matrix<E> L = (lData instanceof double[]) ? createDenseMatrix((double[])lData, m, minMN, a) : createDenseMatrix((float[])lData, m, minMN, a);
        Matrix<E> U = (uData instanceof double[]) ? createDenseMatrix((double[])uData, minMN, n, a) : createDenseMatrix((float[])uData, minMN, n, a);

        int[] pIndices = new int[m];
        for (int i = 0; i < m; i++) pIndices[i] = i;
        for (int i = 0; i < ipiv.length; i++) {
            int swapIdx = ipiv[i] - 1;
            if (swapIdx != i && swapIdx >= 0 && swapIdx < m) {
                int tmp = pIndices[i];
                pIndices[i] = pIndices[swapIdx];
                pIndices[swapIdx] = tmp;
            }
        }

        List<E> pList = new java.util.ArrayList<>(m);
        Ring<E> ring = (Ring<E>) a.getScalarRing();
        for (int idx : pIndices) {
            if (complex) pList.add((E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(idx, 0));
            else pList.add(createScalar((double)idx, a));
        }
        Vector<E> P = org.episteme.core.mathematics.linearalgebra.Vector.of(pList, ring);
        return new LUResult<>(L, U, P);
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": Matrix scale() not available");
        int m = a.rows(), n = a.cols();
        boolean complex = isComplex(a);
        boolean single = isFloat(a);

        if (complex) {
            org.episteme.core.mathematics.numbers.complex.Complex sc = (org.episteme.core.mathematics.numbers.complex.Complex) (Object) scalar;
            if (single) {
                 try (Arena arena = Arena.ofConfined()) {
                    MemorySegment segX = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(a));
                    MemorySegment alpha = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, (float) sc.real(), (float) sc.imaginary());
                    NativeSafe.invoke(CSCAL, m * n, alpha, segX, 1);
                    float[] resData = segX.toArray(ValueLayout.JAVA_FLOAT);
                    return createDenseMatrix(resData, m, n, a);
                } catch (Throwable t) { throw new RuntimeException("CSCAL failed", t); }
            } else {
                 try (Arena arena = Arena.ofConfined()) {
                    MemorySegment segX = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                    MemorySegment alpha = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, sc.real(), sc.imaginary());
                    NativeSafe.invoke(ZSCAL, m * n, alpha, segX, 1);
                    double[] resData = segX.toArray(ValueLayout.JAVA_DOUBLE);
                    return createDenseMatrix(resData, m, n, a);
                } catch (Throwable t) { throw new RuntimeException("ZSCAL failed", t); }
            }
        }

        double s = ((Real)(Object)scalar).doubleValue();
        if (single) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment segX = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toFloatArray(a));
                NativeSafe.invoke(SSCAL, m * n, (float) s, segX, 1);
                float[] resData = segX.toArray(ValueLayout.JAVA_FLOAT);
                return createDenseMatrix(resData, m, n, a);
            } catch (Throwable t) { throw new RuntimeException("SSCAL failed", t); }
        }

        MemorySegment segX;
        Arena resultArena = Arena.ofAuto();
        try {
            segX = NativeSafe.allocateFrom(resultArena, ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
            NativeSafe.invoke(DSCAL, m * n, s, segX, 1);
        } catch (Throwable t) { throw new RuntimeException("DSCAL failed", t); }
        return createZeroCopyDoubleMatrix(segX, m, n, resultArena, a);
    }

    @Override
    public Vector<E> solve(LUResult<E> lu, Vector<E> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": solve(LUResult) not available");
        int m = lu.L().rows();
        int n = lu.U().cols();
        if (m != n || m != b.dimension()) return lu.solve(b);

        boolean complex = isComplex(lu.L());
        int[] ipiv = new int[n];
        int[] currentP = new int[n];
        for(int i=0; i<n; i++) currentP[i] = i;
        for(int i=0; i<n; i++) {
            int target;
            if (complex) target = (int) ((org.episteme.core.mathematics.numbers.complex.Complex)(Object)lu.P().get(i)).real();
            else target = (int) ((Real)(Object)lu.P().get(i)).doubleValue();
            int swapIdx = i;
            for(int j=i; j<n; j++) if(currentP[j] == target) { swapIdx = j; break; }
            ipiv[i] = swapIdx + 1;
            int tmp = currentP[i];
            currentP[i] = currentP[swapIdx];
            currentP[swapIdx] = tmp;
        }

        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            boolean single = isFloat(lu.L());
            MemorySegment segLU = NativeSafe.allocate(arena, single ? ValueLayout.JAVA_FLOAT : ValueLayout.JAVA_DOUBLE, complex ? (long) n * n * 2 : (long) n * n);
            
            if (complex) {
                if (single) {
                    float[] lData = toInterlacedFloatArray(lu.L());
                    float[] uData = toInterlacedFloatArray(lu.U());
                    for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) {
                        if (i <= j) {
                            segLU.setAtIndex(ValueLayout.JAVA_FLOAT, (i * n + j) * 2, uData[(i * n + j) * 2]);
                            segLU.setAtIndex(ValueLayout.JAVA_FLOAT, (i * n + j) * 2 + 1, uData[(i * n + j) * 2 + 1]);
                        } else {
                            segLU.setAtIndex(ValueLayout.JAVA_FLOAT, (i * n + j) * 2, lData[(i * n + j) * 2]);
                            segLU.setAtIndex(ValueLayout.JAVA_FLOAT, (i * n + j) * 2 + 1, lData[(i * n + j) * 2 + 1]);
                        }
                    }
                } else {
                    double[] lData = toInterlacedDoubleArray(lu.L());
                    double[] uData = toInterlacedDoubleArray(lu.U());
                    for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) {
                        if (i <= j) {
                            segLU.setAtIndex(ValueLayout.JAVA_DOUBLE, (i * n + j) * 2, uData[(i * n + j) * 2]);
                            segLU.setAtIndex(ValueLayout.JAVA_DOUBLE, (i * n + j) * 2 + 1, uData[(i * n + j) * 2 + 1]);
                        } else {
                            segLU.setAtIndex(ValueLayout.JAVA_DOUBLE, (i * n + j) * 2, lData[(i * n + j) * 2]);
                            segLU.setAtIndex(ValueLayout.JAVA_DOUBLE, (i * n + j) * 2 + 1, lData[(i * n + j) * 2 + 1]);
                        }
                    }
                }
            } else {
                if (single) {
                    float[] lData = toFloatArray(lu.L());
                    float[] uData = toFloatArray(lu.U());
                    for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) {
                        if (i <= j) segLU.setAtIndex(ValueLayout.JAVA_FLOAT, i * n + j, uData[i * n + j]);
                        else segLU.setAtIndex(ValueLayout.JAVA_FLOAT, i * n + j, lData[i * n + j]);
                    }
                } else {
                    double[] lData = toDoubleArray(lu.L());
                    double[] uData = toDoubleArray(lu.U());
                    for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) {
                        if (i <= j) segLU.setAtIndex(ValueLayout.JAVA_DOUBLE, i * n + j, uData[i * n + j]);
                        else segLU.setAtIndex(ValueLayout.JAVA_DOUBLE, i * n + j, lData[i * n + j]);
                    }
                }
            }
            MemorySegment segIpiv = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_INT, ipiv);
            MemorySegment segB;
            if (complex) {
                if (single) segB = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(b));
                else segB = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(b));
            } else {
                if (single) segB = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toFloatArray(b));
                else segB = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toDoubleArray(b));
            }
            
            int info;
            if (complex) {
                if (single) {
                    if (CGETRS == null) throw new UnsupportedOperationException("CGETRS not available");
                    info = (int) NativeSafe.invoke(CGETRS, LAPACK_ROW_MAJOR, (byte) 'N', n, 1, segLU, n, segIpiv, segB, 1);
                } else {
                    if (ZGETRS == null) throw new UnsupportedOperationException("ZGETRS not available");
                    info = (int) NativeSafe.invoke(ZGETRS, LAPACK_ROW_MAJOR, (byte) 'N', n, 1, segLU, n, segIpiv, segB, 1);
                }
            } else {
                if (single) {
                    if (SGETRS == null) throw new UnsupportedOperationException("SGETRS not available");
                    info = (int) NativeSafe.invoke(SGETRS, LAPACK_ROW_MAJOR, (byte) 'N', n, 1, segLU, n, segIpiv, segB, 1);
                } else {
                    if (DGETRS == null) throw new UnsupportedOperationException("DGETRS not available");
                    info = (int) NativeSafe.invoke(DGETRS, LAPACK_ROW_MAJOR, (byte) 'N', n, 1, segLU, n, segIpiv, segB, 1);
                }
            }
            if (info != 0) throw new ArithmeticException("GETRS failed: " + info);
            
            if (single) return createDenseVector(segB.toArray(ValueLayout.JAVA_FLOAT), n, lu.L());
            else return createDenseVector(segB.toArray(ValueLayout.JAVA_DOUBLE), n, lu.L());
        } catch (Throwable t) { throw new RuntimeException("FFM BLAS LU solve failed", t); }
    }
    
    @Override
    public Vector<E> solve(CholeskyResult<E> cholesky, Vector<E> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": solve(CholeskyResult) not available");
        int n = b.dimension();
        boolean complex = isComplex(cholesky.L());

        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            boolean single = isFloat(cholesky.L());
            MemorySegment segL;
            MemorySegment segB;
            
            if (complex) {
                if (single) {
                    segL = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(cholesky.L()));
                    segB = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(b));
                } else {
                    segL = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(cholesky.L()));
                    segB = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(b));
                }
            } else {
                if (single) {
                    segL = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toFloatArray(cholesky.L()));
                    segB = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toFloatArray(b));
                } else {
                    segL = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toDoubleArray(cholesky.L()));
                    segB = NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toDoubleArray(b));
                }
            }
            
            int info;
            if (complex) {
                if (single) {
                    if (CPOTRS == null) throw new UnsupportedOperationException("CPOTRS not available");
                    info = (int) NativeSafe.invoke(CPOTRS, LAPACK_ROW_MAJOR, (byte) 'L', n, 1, segL, n, segB, 1);
                } else {
                    if (ZPOTRS == null) throw new UnsupportedOperationException("ZPOTRS not available");
                    info = (int) NativeSafe.invoke(ZPOTRS, LAPACK_ROW_MAJOR, (byte) 'L', n, 1, segL, n, segB, 1);
                }
            } else {
                if (single) {
                    if (SPOTRS == null) throw new UnsupportedOperationException("SPOTRS not available");
                    info = (int) NativeSafe.invoke(SPOTRS, LAPACK_ROW_MAJOR, (byte) 'L', n, 1, segL, n, segB, 1);
                } else {
                    if (DPOTRS == null) throw new UnsupportedOperationException("DPOTRS not available");
                    info = (int) NativeSafe.invoke(DPOTRS, LAPACK_ROW_MAJOR, (byte) 'L', n, 1, segL, n, segB, 1);
                }
            }
            if (info != 0) throw new ArithmeticException("POTRS failed: " + info);
            
            if (single) return createDenseVector(segB.toArray(ValueLayout.JAVA_FLOAT), n, cholesky.L());
            else return createDenseVector(segB.toArray(ValueLayout.JAVA_DOUBLE), n, cholesky.L());
        } catch (Throwable t) { throw new RuntimeException("FFM BLAS Cholesky solve failed", t); }
    }

    @Override
    public Vector<E> solve(org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<E> qr, Vector<E> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": solve(QRResult) not available");
        boolean complex = isComplex(qr.Q());
        Vector<E> qtb = multiply(complex ? conjugateTranspose(qr.Q()) : transpose(qr.Q()), b);
        
        Matrix<E> r = qr.getR();
        int n = r.cols();
        if (r.rows() != n) {
            // Rectangular R: solve square part
            Matrix<E> rSquare = r.getSubMatrix(0, n - 1, 0, n - 1);
            java.util.List<E> d1List = new java.util.ArrayList<>(n);
            for (int i = 0; i < n; i++) d1List.add(qtb.get(i));
            Vector<E> d1 = Vector.of(d1List, (Ring<E>)b.getScalarRing());
            return solveTriangular(rSquare, d1, true, false, false, false);
        }
        
        return solveTriangular(r, qtb, true, false, false, false);
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return null;
    }


    private Matrix<E> createDenseMatrix(double[] data, int rows, int cols, Matrix<E> ref) {
        if (isComplex(ref)) {
              // Interlaced double[] to Complex[][]
              Ring<E> ring = (Ring<E>) ref.getScalarRing();
              org.episteme.core.mathematics.numbers.complex.Complex[][] arr = new org.episteme.core.mathematics.numbers.complex.Complex[rows][cols];
              for (int i = 0; i < rows; i++) {
                  for (int j = 0; j < cols; j++) {
                      arr[i][j] = org.episteme.core.mathematics.numbers.complex.Complex.of(data[(i * cols + j) * 2], data[(i * cols + j) * 2 + 1]);
                  }
              }
              return new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>((E[][]) arr, ring);
        }
        Arena arena = Arena.ofConfined();
        org.episteme.nativ.mathematics.linearalgebra.matrices.storage.NativeRealDoubleMatrixStorage storage = new org.episteme.nativ.mathematics.linearalgebra.matrices.storage.NativeRealDoubleMatrixStorage(rows, cols, arena);
        storage.setAll(data);
        @SuppressWarnings("unchecked")
        LinearAlgebraProvider<Real> realProvider = (this instanceof LinearAlgebraProvider<?> lp && isRealRing(ref.getScalarRing())) ? (LinearAlgebraProvider<Real>) lp : (LinearAlgebraProvider<Real>) (LinearAlgebraProvider<?>) this;
        return (Matrix<E>) (Matrix<?>) new org.episteme.nativ.mathematics.linearalgebra.matrices.NativeRealDoubleMatrix(storage, realProvider);
    }

    private Matrix<E> createDenseMatrix(float[] data, int rows, int cols, Matrix<E> ref) {
        Ring<E> ring = (Ring<E>) ref.getScalarRing();
        if (isComplex(ref)) {
            org.episteme.core.mathematics.numbers.complex.Complex[][] arr = new org.episteme.core.mathematics.numbers.complex.Complex[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    arr[i][j] = org.episteme.core.mathematics.numbers.complex.Complex.of(data[(i * cols + j) * 2], data[(i * cols + j) * 2 + 1]);
                }
            }
            return new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>((E[][]) arr, ring);
        } else {
            org.episteme.core.mathematics.numbers.real.Real[][] arr = new org.episteme.core.mathematics.numbers.real.Real[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    arr[i][j] = org.episteme.core.mathematics.numbers.real.Real.of(data[i * cols + j]);
                }
            }
            return new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>((E[][]) arr, ring);
        }
    }


    private Matrix<E> createZeroCopyDoubleMatrix(MemorySegment data, int rows, int cols, Arena arena, Matrix<E> ref) {
        NativeRealDoubleMatrixStorage storage = new NativeRealDoubleMatrixStorage(data, rows, cols, arena);
        return (Matrix<E>) (Matrix<?>) new NativeRealDoubleMatrix(storage, (LinearAlgebraProvider<Real>) (Object) this);
    }


    private Vector<E> createZeroCopyDoubleVector(MemorySegment data, int dim, Arena arena, Matrix<E> ref) {
        NativeRealDoubleVectorStorage storage = new NativeRealDoubleVectorStorage(data, dim, arena);
        return (Vector<E>) (Vector<?>) new NativeRealDoubleVector(storage, (LinearAlgebraProvider<Real>) (Object) this);
    }


    private Vector<E> createZeroCopyDoubleVector(MemorySegment data, int dim, Arena arena, Vector<E> ref) {
        NativeRealDoubleVectorStorage storage = new NativeRealDoubleVectorStorage(data, dim, arena);
        return (Vector<E>) (Vector<?>) new NativeRealDoubleVector(storage, (LinearAlgebraProvider<Real>) (Object) this);
    }

    private Vector<E> createDenseVector(double[] data, int dimension, Matrix<E> ref) {
        Ring<E> ring = (Ring<E>) ref.getScalarRing();
        if (isComplex(ref)) {
            org.episteme.core.mathematics.numbers.complex.Complex[] arr = new org.episteme.core.mathematics.numbers.complex.Complex[dimension];
            for (int i = 0; i < dimension; i++) {
                arr[i] = org.episteme.core.mathematics.numbers.complex.Complex.of(data[i * 2], data[i * 2 + 1]);
            }
            return (Vector<E>) org.episteme.core.mathematics.linearalgebra.Vector.of(java.util.Arrays.asList(arr), (Ring) ring);
        } else {
            org.episteme.core.mathematics.numbers.real.Real[] arr = new org.episteme.core.mathematics.numbers.real.Real[dimension];
            for (int i = 0; i < dimension; i++) {
                arr[i] = org.episteme.core.mathematics.numbers.real.Real.of(data[i]);
            }
            return (Vector<E>) org.episteme.core.mathematics.linearalgebra.Vector.of(java.util.Arrays.asList(arr), (Ring) ring);
        }
    }

    private Vector<E> createDenseVector(float[] data, int dimension, Matrix<E> ref) {
        Ring<E> ring = (Ring<E>) ref.getScalarRing();
        if (isComplex(ref)) {
            org.episteme.core.mathematics.numbers.complex.Complex[] arr = new org.episteme.core.mathematics.numbers.complex.Complex[dimension];
            for (int i = 0; i < dimension; i++) {
                arr[i] = org.episteme.core.mathematics.numbers.complex.Complex.of(data[i * 2], data[i * 2 + 1]);
            }
            return (Vector<E>) org.episteme.core.mathematics.linearalgebra.Vector.of(java.util.Arrays.asList(arr), (Ring) ring);
        } else {
            org.episteme.core.mathematics.numbers.real.Real[] arr = new org.episteme.core.mathematics.numbers.real.Real[dimension];
            for (int i = 0; i < dimension; i++) {
                arr[i] = org.episteme.core.mathematics.numbers.real.Real.of(data[i]);
            }
            return (Vector<E>) org.episteme.core.mathematics.linearalgebra.Vector.of(java.util.Arrays.asList(arr), (Ring) ring);
        }
    }

    private Vector<E> createDenseVector(double[] data, int dimension, Vector<E> ref) {
        Ring<E> ring = (Ring<E>) ref.getScalarRing();
        if (isComplex(ref)) {
            org.episteme.core.mathematics.numbers.complex.Complex[] arr = new org.episteme.core.mathematics.numbers.complex.Complex[dimension];
            for (int i = 0; i < dimension; i++) {
                arr[i] = org.episteme.core.mathematics.numbers.complex.Complex.of(data[i * 2], data[i * 2 + 1]);
            }
            return (Vector<E>) org.episteme.core.mathematics.linearalgebra.Vector.of(java.util.Arrays.asList(arr), (Ring) ring);
        } else {
            org.episteme.core.mathematics.numbers.real.Real[] arr = new org.episteme.core.mathematics.numbers.real.Real[dimension];
            for (int i = 0; i < dimension; i++) {
                arr[i] = org.episteme.core.mathematics.numbers.real.Real.of(data[i]);
            }
            return (Vector<E>) org.episteme.core.mathematics.linearalgebra.Vector.of(java.util.Arrays.asList(arr), (Ring) ring);
        }
    }

    private Vector<E> createDenseVector(float[] data, int dimension, Vector<E> ref) {
        Ring<E> ring = (Ring<E>) ref.getScalarRing();
        if (isComplex(ref)) {
            org.episteme.core.mathematics.numbers.complex.Complex[] arr = new org.episteme.core.mathematics.numbers.complex.Complex[dimension];
            for (int i = 0; i < dimension; i++) {
                arr[i] = org.episteme.core.mathematics.numbers.complex.Complex.of(data[i * 2], data[i * 2 + 1]);
            }
            return (Vector<E>) org.episteme.core.mathematics.linearalgebra.Vector.of(java.util.Arrays.asList(arr), (Ring) ring);
        } else {
            org.episteme.core.mathematics.numbers.real.Real[] arr = new org.episteme.core.mathematics.numbers.real.Real[dimension];
            for (int i = 0; i < dimension; i++) {
                arr[i] = org.episteme.core.mathematics.numbers.real.Real.of(data[i]);
            }
            return (Vector<E>) org.episteme.core.mathematics.linearalgebra.Vector.of(java.util.Arrays.asList(arr), (Ring) ring);
        }
    }



    // --- Func Category (Matrix Transcendentals) ---
    // For 1x1 matrices used in compliance audit


    @Override public Matrix<E> exp(Matrix<E> m) { return applyFunc(m, Math::exp, Complex::exp); }
    @Override public Matrix<E> log(Matrix<E> m) { return applyFunc(m, Math::log, Complex::log); }
    @Override public Matrix<E> log10(Matrix<E> m) { return applyFunc(m, Math::log10, org.episteme.core.mathematics.numbers.complex.Complex::log10); }
    @Override public Matrix<E> sin(Matrix<E> m) { return applyFunc(m, Math::sin, Complex::sin); }
    @Override public Matrix<E> cos(Matrix<E> m) { return applyFunc(m, Math::cos, Complex::cos); }
    @Override public Matrix<E> tan(Matrix<E> m) { return applyFunc(m, Math::tan, Complex::tan); }
    @Override public Matrix<E> asin(Matrix<E> m) { return applyFunc(m, Math::asin, c -> { throw new UnsupportedOperationException("asin not supported for Complex"); }); }
    @Override public Matrix<E> acos(Matrix<E> m) { return applyFunc(m, Math::acos, c -> { throw new UnsupportedOperationException("acos not supported for Complex"); }); }
    @Override public Matrix<E> atan(Matrix<E> m) { return applyFunc(m, Math::atan, c -> { throw new UnsupportedOperationException("atan not supported for Complex"); }); }
    @Override public Matrix<E> sinh(Matrix<E> m) { return applyFunc(m, Math::sinh, Complex::sinh); }
    @Override public Matrix<E> cosh(Matrix<E> m) { return applyFunc(m, Math::cosh, Complex::cosh); }
    @Override public Matrix<E> tanh(Matrix<E> m) { return applyFunc(m, Math::tanh, Complex::tanh); }
    @Override public Matrix<E> asinh(Matrix<E> m) { return applyFunc(m, x -> Math.log(x + Math.sqrt(x * x + 1.0)), c -> { throw new UnsupportedOperationException("asinh not supported for Complex"); }); }
    @Override public Matrix<E> acosh(Matrix<E> m) { return applyFunc(m, x -> Math.log(x + Math.sqrt(x * x - 1.0)), c -> { throw new UnsupportedOperationException("acosh not supported for Complex"); }); }
    @Override public Matrix<E> atanh(Matrix<E> m) { return applyFunc(m, x -> 0.5 * Math.log((1.0 + x) / (1.0 - x)), c -> { throw new UnsupportedOperationException("atanh not supported for Complex"); }); }
    @Override public Matrix<E> sqrt(Matrix<E> m) { return applyFunc(m, Math::sqrt, Complex::sqrt); }
    @Override public Matrix<E> cbrt(Matrix<E> m) { return applyFunc(m, Math::cbrt, Complex::cbrt); }

    @Override
    public Matrix<E> pow(Matrix<E> m, E exponent) {
        int rows = m.rows();
        int cols = m.cols();
        boolean complex = isComplex(m);
        boolean single = isFloat(m);
        
        if (complex) {
            Complex exp = (Complex) (Object) exponent;
            Complex[][] res = new Complex[rows][cols];
            for (int i = 0; i < rows; i++) for (int j = 0; j < cols; j++) res[i][j] = ((Complex) (Object) m.get(i, j)).pow(exp);
            return (Matrix<E>) Matrix.of(res, (Ring<Complex>) (Object) m.getScalarRing());
        } else {
            double exp = ((Real) (Object) exponent).doubleValue();
            if (single) {
                float[] data = toFloatArray(m);
                for (int i = 0; i < data.length; i++) data[i] = (float) Math.pow(data[i], exp);
                return createDenseMatrix(data, rows, cols, m);
            } else {
                double[] data = toDoubleArray(m);
                for (int i = 0; i < data.length; i++) data[i] = Math.pow(data[i], exp);
                return createDenseMatrix(data, rows, cols, m);
            }
        }
    }

    private Matrix<E> applyFunc(Matrix<E> m, java.util.function.DoubleUnaryOperator realOp, java.util.function.UnaryOperator<Complex> complexOp) {
        int rows = m.rows();
        int cols = m.cols();
        boolean complex = isComplex(m);
        boolean single = isFloat(m);
        
        if (complex) {
            Complex[][] res = new Complex[rows][cols];
            for (int i = 0; i < rows; i++) for (int j = 0; j < cols; j++) res[i][j] = complexOp.apply((Complex) (Object) m.get(i, j));
            return (Matrix<E>) Matrix.of(res, (Ring<Complex>) (Object) m.getScalarRing());
        } else {
            if (single) {
                float[] data = toFloatArray(m);
                for (int i = 0; i < data.length; i++) data[i] = (float) realOp.applyAsDouble(data[i]);
                return createDenseMatrix(data, rows, cols, m);
            } else {
                double[] data = toDoubleArray(m);
                for (int i = 0; i < data.length; i++) data[i] = realOp.applyAsDouble(data[i]);
                return createDenseMatrix(data, rows, cols, m);
            }
        }
    }


    @Override
    public void close() {
        // No-op.
    }

    private MemorySegment getSegment(Object obj, Arena arena, ResourceTracker tracker) {
        if (obj instanceof NativeSegmentProxy proxy) {
            if (proxy.isAlive()) return proxy.segment();
            logger.warn("NativeSegmentProxy scope is not alive, falling back to copy");
        }
        
        // Zero-copy optimization for GenericMatrix wrapping Native storage
        if (obj instanceof Matrix<?> m) {
            try {
                Object storage = m.getStorage();
                if (storage instanceof NativeSegmentProxy proxy) {
                    if (proxy.isAlive()) return proxy.segment();
                }
            } catch (Exception e) {
                // Ignore and fall back to copy
            }
        }
        
        if (obj instanceof Matrix<?> m) {
            boolean single = isFloat(m);
            boolean complex = isComplex(m);
            if (complex) {
                if (single) return NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray((Matrix<E>)m));
                return NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray((Matrix<E>)m));
            } else {
                if (single) return NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toFloatArray((Matrix<E>)m));
                return NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toDoubleArray((Matrix<E>)m));
            }
        } else if (obj instanceof Vector<?> v) {
            boolean single = isFloat(v);
            boolean complex = isComplex(v);
             if (complex) {
                if (single) return NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toInterlacedFloatArray((Vector<E>)v));
                return NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray((Vector<E>)v));
            } else {
                if (single) return NativeSafe.allocateFrom(arena, ValueLayout.JAVA_FLOAT, toFloatArray((Vector<E>)v));
                return NativeSafe.allocateFrom(arena, ValueLayout.JAVA_DOUBLE, toDoubleArray((Vector<E>)v));
            }
        }
        throw new IllegalArgumentException("Unsupported object for FFM conversion: " + (obj != null ? obj.getClass() : "null"));
    }

}

