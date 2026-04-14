/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.episteme.nativ.mathematics.linearalgebra.backends;

import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.technical.algorithm.AutoTuningManager;
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.nativ.technical.backend.nativ.NativeFFMLoader;
import com.google.auto.service.AutoService;
import org.episteme.core.mathematics.linearalgebra.vectors.DenseVector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.nativ.technical.backend.nativ.NativeSafe;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.episteme.core.technical.algorithm.AlgorithmProvider;

/**
 * High-Performance Native BLAS Backend using Project Panama (FFM).
 * Binds to OpenBLAS/MKL for Matrix Operations.
 * Implements {@link CPUBackend}, {@link NativeBackend} and {@link AlgorithmProvider}.
 */
@AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, CPUBackend.class})
public class NativeFFMBLASBackend<E> implements LinearAlgebraProvider<E>, NativeBackend, CPUBackend {
    
    private static final Logger logger = LoggerFactory.getLogger(NativeFFMBLASBackend.class);

    private static final SymbolLookup LOOKUP;
    private static final SymbolLookup LAPACK_LOOKUP;
    private static final boolean IS_AVAILABLE;
    private static final Linker LINKER = NativeFFMLoader.getLinker();

    // CBLAS Layout Constants
    private static final int CblasRowMajor = 101;
    private static final int CblasNoTrans = 111;
    // private static final int CblasTrans = 112;

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

    private static MethodHandle DOMATCOPY;
    
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

    private static Optional<MemorySegment> findLapackSymbol(String name) {
        Optional<MemorySegment> sym = NativeFFMLoader.findSymbol(LOOKUP, name);
        if (sym.isEmpty() && LAPACK_LOOKUP != null) {
            sym = NativeFFMLoader.findSymbol(LAPACK_LOOKUP, name);
        }
        return sym;
    }

    static {
        Arena arena = Arena.global();
        Optional<SymbolLookup> lib = NativeFFMLoader.loadLibrary("openblas", arena);
        if (lib.isEmpty()) {
             lib = NativeFFMLoader.loadLibrary("mkl_rt", arena);
        }
        
        if (lib.isPresent()) {
             logger.info("FFM: Successfully matched native library for FFM backend: {}", lib.get());
        } else {
             logger.info("FFM: No local BLAS/LAPACK library found in libs/. Attempting system lookup (CAUTION: possible ABI mismatch).");
             lib = NativeFFMLoader.getSystemLookup();
        }
        
        LOOKUP = lib.orElse(null);
        
        Optional<SymbolLookup> lapackLib = NativeFFMLoader.loadLibrary("lapacke", arena);
        if (lapackLib.isEmpty()) {
            lapackLib = NativeFFMLoader.loadLibrary("lapack", arena);
        }
        LAPACK_LOOKUP = lapackLib.orElse(null);
        
        boolean available = false;

        if (LOOKUP != null && !Boolean.getBoolean("episteme.backend.disable.ffm-blas")) {
            try {
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

                FunctionDescriptor domatcopyDesc = FunctionDescriptor.ofVoid(
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                DOMATCOPY = NativeFFMLoader.findSymbol(LOOKUP, "cblas_domatcopy", "mkl_domatcopy")
                    .map(s -> LINKER.downcallHandle(s, domatcopyDesc)).orElse(null);

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
                SGETRI = findLapackSymbol("LAPACKE_sgetri").map(s -> LINKER.downcallHandle(s, getriDesc)).orElse(null);
                DGETRI = findLapackSymbol("LAPACKE_dgetri").map(s -> LINKER.downcallHandle(s, getriDesc)).orElse(null);
                CGETRI = findLapackSymbol("LAPACKE_cgetri").map(s -> LINKER.downcallHandle(s, getriDesc)).orElse(null);
                ZGETRI = findLapackSymbol("LAPACKE_zgetri").map(s -> LINKER.downcallHandle(s, getriDesc)).orElse(null);

                FunctionDescriptor getrsDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                SGETRS = findLapackSymbol("LAPACKE_sgetrs").map(s -> LINKER.downcallHandle(s, getrsDesc)).orElse(null);
                DGETRS = findLapackSymbol("LAPACKE_dgetrs").map(s -> LINKER.downcallHandle(s, getrsDesc)).orElse(null);
                CGETRS = findLapackSymbol("LAPACKE_cgetrs").map(s -> LINKER.downcallHandle(s, getrsDesc)).orElse(null);
                ZGETRS = findLapackSymbol("LAPACKE_zgetrs").map(s -> LINKER.downcallHandle(s, getrsDesc)).orElse(null);

                // QR Decomposition
                FunctionDescriptor geqrfDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS
                );
                SGEQRF = findLapackSymbol("LAPACKE_sgeqrf").map(s -> LINKER.downcallHandle(s, geqrfDesc)).orElse(null);
                DGEQRF = findLapackSymbol("LAPACKE_dgeqrf").map(s -> LINKER.downcallHandle(s, geqrfDesc)).orElse(null);
                CGEQRF = findLapackSymbol("LAPACKE_cgeqrf").map(s -> LINKER.downcallHandle(s, geqrfDesc)).orElse(null);
                ZGEQRF = findLapackSymbol("LAPACKE_zgeqrf").map(s -> LINKER.downcallHandle(s, geqrfDesc)).orElse(null);

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
                SPOTRF = findLapackSymbol("LAPACKE_spotrf").map(s -> LINKER.downcallHandle(s, potrfDesc)).orElse(null);
                DPOTRF = findLapackSymbol("LAPACKE_dpotrf").map(s -> LINKER.downcallHandle(s, potrfDesc)).orElse(null);
                CPOTRF = findLapackSymbol("LAPACKE_cpotrf").map(s -> LINKER.downcallHandle(s, potrfDesc)).orElse(null);
                ZPOTRF = findLapackSymbol("LAPACKE_zpotrf").map(s -> LINKER.downcallHandle(s, potrfDesc)).orElse(null);

                FunctionDescriptor potrsDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                SPOTRS = findLapackSymbol("LAPACKE_spotrs").map(s -> LINKER.downcallHandle(s, potrsDesc)).orElse(null);
                DPOTRS = findLapackSymbol("LAPACKE_dpotrs").map(s -> LINKER.downcallHandle(s, potrsDesc)).orElse(null);
                CPOTRS = findLapackSymbol("LAPACKE_cpotrf").map(s -> LINKER.downcallHandle(s, potrsDesc)).orElse(null);
                ZPOTRS = findLapackSymbol("LAPACKE_zpotrs").map(s -> LINKER.downcallHandle(s, potrsDesc)).orElse(null);

                // Eigen
                FunctionDescriptor dsyevDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_BYTE,
                        ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS
                );
                DSYEV = findLapackSymbol("LAPACKE_dsyev")
                    .map(s -> LINKER.downcallHandle(s, dsyevDesc)).orElse(null);

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

                available = (DGEMM != null && DGEMV != null && DDOT != null);

                if (available) {
                    logger.info("FFM: Backend initialized successfully. Multi-precision handles linked.");
                } else {
                    logger.warn("FFM: Native library found but essential BLAS handles (DGEMM, DGEMV) are missing.");
                }
            } catch (Throwable t) {
                logger.warn("FFM: Failed to link native symbols: {}", t.getMessage());
                available = false;
            }
        }
        IS_AVAILABLE = available;
    }

    @Override
    public boolean isLoaded() {
        return IS_AVAILABLE;
    }

    @Override
    public String getNativeLibraryName() {
        return "openblas";
    }

    @Override
    public Vector<E> solve(Matrix<E> A, Vector<E> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": solve() not available");
        org.episteme.core.mathematics.context.MathContext.checkCancelled();
        
        int m = A.rows(), n = A.cols();
        boolean complex = isComplex(A);
        boolean single = isFloat(A);

        if (m == n) {
             if (n != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");
             if (complex) {
                 if (single) {
                     try (Arena arena = Arena.ofConfined()) {
                         MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(A));
                         MemorySegment segB = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(b));
                         MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
                         int info = (int) NativeSafe.invoke(CGESV, LAPACK_ROW_MAJOR, n, 1, segA, n, segIpiv, segB, 1);
                         if (info != 0) throw new ArithmeticException("CGESV failed: " + info);
                         float[] resData = segB.toArray(ValueLayout.JAVA_FLOAT);
                         return createDenseVector(resData, n, A);
                     }
                 } else {
                     try (Arena arena = Arena.ofConfined()) {
                         MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(A));
                         MemorySegment segB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(b));
                         MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
                         int info = (int) NativeSafe.invoke(ZGESV, LAPACK_ROW_MAJOR, n, 1, segA, n, segIpiv, segB, 1);
                         if (info != 0) throw new ArithmeticException("ZGESV failed: " + info);
                         double[] resData = segB.toArray(ValueLayout.JAVA_DOUBLE);
                         return createDenseVector(resData, n, A);
                     }
                 }
             } else {
                 if (single) {
                     try (Arena arena = Arena.ofConfined()) {
                         MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(A));
                         MemorySegment segB = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(b));
                         MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
                         int info = (int) NativeSafe.invoke(SGESV, LAPACK_ROW_MAJOR, n, 1, segA, n, segIpiv, segB, 1);
                         if (info != 0) throw new ArithmeticException("SGESV failed: " + info);
                         float[] resData = segB.toArray(ValueLayout.JAVA_FLOAT);
                         return createDenseVector(resData, n, A);
                     }
                 } else {
                     try (Arena arena = Arena.ofConfined()) {
                         MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(A));
                         MemorySegment segB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b));
                         MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
                         int info = (int) NativeSafe.invoke(DGESV, LAPACK_ROW_MAJOR, n, 1, segA, n, segIpiv, segB, 1);
                         if (info != 0) throw new ArithmeticException("DGESV failed: " + info);
                         double[] resultSize = segB.toArray(ValueLayout.JAVA_DOUBLE);
                         return createDenseVector(resultSize, n, A);
                     }
                 }
             }
        } else {
             // Least Squares
             if (complex) {
                 if (single) {
                     try (Arena arena = Arena.ofConfined()) {
                         MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(A));
                         int maxDim = Math.max(m, n);
                         float[] bPad = new float[maxDim * 2];
                         float[] bOrig = toInterlacedFloatArray(b);
                         System.arraycopy(bOrig, 0, bPad, 0, bOrig.length);
                         MemorySegment segB = arena.allocateFrom(ValueLayout.JAVA_FLOAT, bPad);
                         int info = (int) NativeSafe.invoke(CGELS, LAPACK_ROW_MAJOR, (byte) 'N', m, n, 1, segA, n, segB, 1);
                         if (info != 0) throw new RuntimeException("CGELS failed: " + info);
                         float[] resFull = segB.toArray(ValueLayout.JAVA_FLOAT);
                         float[] resData = new float[n * 2];
                         System.arraycopy(resFull, 0, resData, 0, n * 2);
                         return createDenseVector(resData, n, A);
                     }
                 } else {
                     try (Arena arena = Arena.ofConfined()) {
                         MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(A));
                         int maxDim = Math.max(m, n);
                         double[] bPad = new double[maxDim * 2];
                         double[] bOrig = toInterlacedDoubleArray(b);
                         System.arraycopy(bOrig, 0, bPad, 0, bOrig.length);
                         MemorySegment segB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, bPad);
                         int info = (int) NativeSafe.invoke(ZGELS, LAPACK_ROW_MAJOR, (byte) 'N', m, n, 1, segA, n, segB, 1);
                         if (info != 0) throw new RuntimeException("ZGELS failed: " + info);
                         double[] resFull = segB.toArray(ValueLayout.JAVA_DOUBLE);
                         double[] resData = new double[n * 2];
                         System.arraycopy(resFull, 0, resData, 0, n * 2);
                         return createDenseVector(resData, n, A);
                     }
                 }
             } else {
                 if (single) {
                     try (Arena arena = Arena.ofConfined()) {
                         MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(A));
                         int maxDim = Math.max(m, n);
                         float[] bPad = new float[maxDim];
                         float[] bOrig = toFloatArray(b);
                         System.arraycopy(bOrig, 0, bPad, 0, bOrig.length);
                         MemorySegment segB = arena.allocateFrom(ValueLayout.JAVA_FLOAT, bPad);
                         int info = (int) NativeSafe.invoke(SGELS, LAPACK_ROW_MAJOR, (byte) 'N', m, n, 1, segA, n, segB, 1);
                         if (info != 0) throw new RuntimeException("SGELS failed: " + info);
                         float[] result = new float[n];
                         MemorySegment.copy(segB, ValueLayout.JAVA_FLOAT, 0L, result, 0, n);
                         return createDenseVector(result, n, A);
                     }
                 } else {
                     try (Arena arena = Arena.ofConfined()) {
                         MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(A));
                         int maxDim = Math.max(m, n);
                         double[] bPad = new double[maxDim];
                         double[] bOrig = toDoubleArray(b);
                         System.arraycopy(bOrig, 0, bPad, 0, bOrig.length);
                         MemorySegment segB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, bPad);
                         int info = (int) NativeSafe.invoke(DGELS, LAPACK_ROW_MAJOR, (byte) 'N', m, n, 1, segA, n, segB, 1);
                         if (info != 0) throw new RuntimeException("DGELS failed: " + info);
                         double[] result = new double[n];
                         MemorySegment.copy(segB, ValueLayout.JAVA_DOUBLE, 0L, result, 0, n);
                         return createDenseVector(result, n, A);
                     }
                 }
             }
        }
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
        
        if (DOMATCOPY != null) {
            org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix res = org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix.direct(n, m);
            try (Arena arena = Arena.ofConfined()) {
                double[] arrA = toDoubleArray(a);
                MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, arrA);
                MemorySegment segC = MemorySegment.ofBuffer(res.getBuffer());
                NativeSafe.invoke(DOMATCOPY, CblasRowMajor, 112, m, n, 1.0, segA, n, segC, m);
                return (Matrix<E>) (Matrix<?>) res;
            }
        }

        throw new UnsupportedOperationException(getName() + ": transpose() failed or not available");
    }
    
    @Override
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
                     MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(A));
                     MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
                     int info = (int) NativeSafe.invoke(CGETRF, LAPACK_ROW_MAJOR, n, n, segA, n, segIpiv);
                     if (info != 0) throw new ArithmeticException("CGETRF failed: " + info);
                     info = (int) NativeSafe.invoke(CGETRI, LAPACK_ROW_MAJOR, n, segA, n, segIpiv);
                     if (info != 0) throw new ArithmeticException("CGETRI failed: " + info);
                     float[] result = segA.toArray(ValueLayout.JAVA_FLOAT);
                     return createDenseMatrix(result, n, n, A);
                 }
             } else {
                 try (Arena arena = Arena.ofConfined()) {
                     MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(A));
                     MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
                     int info = (int) NativeSafe.invoke(ZGETRF, LAPACK_ROW_MAJOR, n, n, segA, n, segIpiv);
                     if (info != 0) throw new ArithmeticException("ZGETRF failed: " + info);
                     info = (int) NativeSafe.invoke(ZGETRI, LAPACK_ROW_MAJOR, n, segA, n, segIpiv);
                     if (info != 0) throw new ArithmeticException("ZGETRI failed: " + info);
                     double[] result = segA.toArray(ValueLayout.JAVA_DOUBLE);
                     return createDenseMatrix(result, n, n, A);
                 }
             }
         }
         
         if (single) {
             try (Arena arena = Arena.ofConfined()) {
                 MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(A));
                 MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
                 int info = (int) NativeSafe.invoke(SGETRF, LAPACK_ROW_MAJOR, n, n, segA, n, segIpiv);
                 if (info != 0) throw new ArithmeticException("SGETRF failed: " + info);
                 info = (int) NativeSafe.invoke(SGETRI, LAPACK_ROW_MAJOR, n, segA, n, segIpiv);
                 if (info != 0) throw new ArithmeticException("SGETRI failed: " + info);
                 float[] result = segA.toArray(ValueLayout.JAVA_FLOAT);
                 return createDenseMatrix(result, n, n, A);
             }
         }

         try (Arena arena = Arena.ofConfined()) {
             MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(A));
             MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
             int info = (int) NativeSafe.invoke(DGETRF, LAPACK_ROW_MAJOR, n, m, segA, n, segIpiv);
             if (info != 0) throw new ArithmeticException("DGETRF failed: " + info);
             info = (int) NativeSafe.invoke(DGETRI, LAPACK_ROW_MAJOR, n, segA, n, segIpiv);
             if (info != 0) throw new ArithmeticException("DGETRI failed: " + info);
             double[] result = segA.toArray(ValueLayout.JAVA_DOUBLE);
             return createDenseMatrix(result, n, n, A);
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
            sInv = new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>((E[][]) sData, (Ring<E>) a.getScalarRing());
        } else {
            Real[][] sData = new Real[n][m];
            for(int i=0; i<n; i++) for(int j=0; j<m; j++) sData[i][j] = Real.ZERO;
            for(int i=0; i<k; i++) {
                double sVal = ((Real)(Object)svd.S().get(i)).doubleValue();
                if (sVal > 1e-12) sData[i][i] = Real.of(1.0 / sVal);
            }
            sInv = new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>((E[][]) sData, (Ring<E>) a.getScalarRing());
        }
        return svd.V().multiply(sInv).multiply(conjugateTranspose(svd.U()));
    }

    private Matrix<E> conjugateTranspose(Matrix<E> m) {
        Matrix<E> mt = m.transpose();
        if (isComplex(m)) {
            return mt.map(val -> {
                org.episteme.core.mathematics.numbers.complex.Complex c = (org.episteme.core.mathematics.numbers.complex.Complex) (Object) val;
                return (E) (Object) c.conjugate();
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
         
         if (complex) {
             if (ZGETRF != null) {
                 try (Arena arena = Arena.ofConfined()) {
                     MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(A));
                     MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, n);
                     int info = (int) NativeSafe.invoke(ZGETRF, LAPACK_ROW_MAJOR, n, n, segA, n, segIpiv);
                     if (info < 0) throw new IllegalArgumentException("ZGETRF failed: illegal argument " + (-info));
                     if (info > 0) return (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.ZERO;
                     org.episteme.core.mathematics.numbers.complex.Complex det = org.episteme.core.mathematics.numbers.complex.Complex.of(1.0);
                     for(int i=0; i<n; i++) {
                         double r = segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long)(i*n + i)*2);
                         double im = segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long)(i*n + i)*2 + 1);
                         det = det.multiply(org.episteme.core.mathematics.numbers.complex.Complex.of(r, im));
                         if (segIpiv.getAtIndex(ValueLayout.JAVA_INT, (long)i) != (i + 1)) det = det.negate();
                     }
                     return (E) (Object) det;
                 } catch (Throwable e) { logger.debug("Native determinant failed, falling back: {}", e.getMessage()); }
             }
             return org.episteme.core.mathematics.linearalgebra.matrices.solvers.GenericLU.determinant(A, (org.episteme.core.mathematics.structures.rings.Field<E>)A.getScalarRing(), this);
         }
         
         if (DGETRF != null) {
             try (Arena arena = Arena.ofConfined()) {
                 MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(A));
                 MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, n);
                 int info = (int) NativeSafe.invoke(DGETRF, LAPACK_ROW_MAJOR, n, n, segA, n, segIpiv);
                 if (info > 0) return (E) (Object) Real.ZERO;
                 double det = 1.0;
                 for(int i=0; i<n; i++) {
                     det *= segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long) i * n + i);
                     if (segIpiv.getAtIndex(ValueLayout.JAVA_INT, (long) i) != i + 1) det = -det;
                 }
                 return (E) (Object) Real.of(det);
             } catch (Throwable e) { logger.debug("Native determinant failed, falling back: {}", e.getMessage()); }
         }
         return (E) (Object) org.episteme.core.mathematics.linearalgebra.matrices.solvers.GenericLU.determinant((Matrix<Real>)A, (org.episteme.core.mathematics.structures.rings.Field<Real>)A.getScalarRing(), (org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<Real>)(Object)this);
    }
    
    @Override
    public boolean isAvailable() {
        return IS_AVAILABLE && !isExplicitlyDisabled();
    }

    @Override
    public String getId() {
        return "blas";
    }

    @Override
    public String getType() {
        return "math";
    }

    @Override
    public String getEnvironmentInfo() {
        return IS_AVAILABLE ? "CPU (FFM-BLAS)" : "N/A";
    }

    @Override
    public String getName() {
        return "Native BLAS Provider FFM";
    }

    @Override
    public QRResult<E> qr(Matrix<E> a) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": qr() not available");
        int m = a.rows();
        int n = a.cols();
        int k = Math.min(m, n);
        boolean complex = isComplex(a);
        boolean single = isFloat(a);

        try (Arena arena = Arena.ofConfined()) {
            if (complex) {
                if (single) {
                    MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(a));
                    MemorySegment tau = arena.allocate(ValueLayout.JAVA_FLOAT, (long) k * 2);
                    int info = (int) NativeSafe.invoke(CGEQRF, LAPACK_ROW_MAJOR, m, n, segA, n, tau);
                    if (info != 0) throw new RuntimeException("CGEQRF failed: " + info);
                    float[] rData = new float[k * n * 2];
                    for (int i = 0; i < k; i++) {
                        for (int j = i; j < n; j++) {
                            rData[(i * n + j) * 2] = segA.getAtIndex(ValueLayout.JAVA_FLOAT, (long) (i * n + j) * 2);
                            rData[(i * n + j) * 2 + 1] = segA.getAtIndex(ValueLayout.JAVA_FLOAT, (long) (i * n + j) * 2 + 1);
                        }
                    }
                    Matrix<E> R = createDenseMatrix(rData, k, n, a);
                    info = (int) NativeSafe.invoke(CUNGQR, LAPACK_ROW_MAJOR, m, k, k, segA, n, tau);
                    if (info != 0) throw new RuntimeException("CUNGQR failed: " + info);
                    float[] qData = new float[m * k * 2];
                    for (int i = 0; i < m; i++) {
                        for (int j = 0; j < k; j++) {
                            qData[(i * k + j) * 2] = segA.getAtIndex(ValueLayout.JAVA_FLOAT, (long) (i * n + j) * 2);
                            qData[(i * k + j) * 2 + 1] = segA.getAtIndex(ValueLayout.JAVA_FLOAT, (long) (i * n + j) * 2 + 1);
                        }
                    }
                    return new QRResult<E>(createDenseMatrix(qData, m, k, a), R);
                } else {
                    MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                    MemorySegment tau = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) k * 2);
                    int info = (int) NativeSafe.invoke(ZGEQRF, LAPACK_ROW_MAJOR, m, n, segA, n, tau);
                    if (info != 0) throw new RuntimeException("ZGEQRF failed: " + info);
                    double[] rData = new double[k * n * 2];
                    for (int i = 0; i < k; i++) {
                        for (int j = i; j < n; j++) {
                            rData[(i * n + j) * 2] = segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long) (i * n + j) * 2);
                            rData[(i * n + j) * 2 + 1] = segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long) (i * n + j) * 2 + 1);
                        }
                    }
                    Matrix<E> R = createDenseMatrix(rData, k, n, a);
                    info = (int) NativeSafe.invoke(ZUNGQR, LAPACK_ROW_MAJOR, m, k, k, segA, n, tau);
                    if (info != 0) throw new RuntimeException("ZUNGQR failed: " + info);
                    double[] qData = new double[m * k * 2];
                    for (int i = 0; i < m; i++) {
                        for (int j = 0; j < k; j++) {
                            qData[(i * k + j) * 2] = segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long) (i * n + j) * 2);
                            qData[(i * k + j) * 2 + 1] = segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long) (i * n + j) * 2 + 1);
                        }
                    }
                    return new QRResult<E>(createDenseMatrix(qData, m, k, a), R);
                }
            } else {
                if (single) {
                    MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(a));
                    MemorySegment tau = arena.allocate(ValueLayout.JAVA_FLOAT, (long) k);
                    int info = (int) NativeSafe.invoke(SGEQRF, LAPACK_ROW_MAJOR, m, n, segA, n, tau);
                    if (info != 0) throw new RuntimeException("SGEQRF failed: " + info);
                    float[] rData = new float[k * n];
                    for (int i = 0; i < k; i++) {
                        for (int j = i; j < n; j++) {
                            rData[i * n + j] = segA.getAtIndex(ValueLayout.JAVA_FLOAT, (long) i * n + j);
                        }
                    }
                    Matrix<E> R = createDenseMatrix(rData, k, n, a);
                    info = (int) NativeSafe.invoke(SORGQR, LAPACK_ROW_MAJOR, m, k, k, segA, n, tau);
                    if (info != 0) throw new RuntimeException("SORGQR failed: " + info);
                    float[] qData = new float[m * k];
                    for (int i = 0; i < m; i++) {
                        for (int j = 0; j < k; j++) {
                            qData[i * k + j] = segA.getAtIndex(ValueLayout.JAVA_FLOAT, (long) i * n + j);
                        }
                    }
                    return new QRResult<E>(createDenseMatrix(qData, m, k, a), R);
                } else {
                    MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
                    MemorySegment tau = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) k);
                    int info = (int) NativeSafe.invoke(DGEQRF, LAPACK_ROW_MAJOR, m, n, segA, n, tau);
                    if (info != 0) throw new RuntimeException("DGEQRF failed: " + info);
                    double[] rData = new double[k * n];
                    for (int i = 0; i < k; i++) {
                        for (int j = i; j < n; j++) {
                            rData[i * n + j] = segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long) i * n + j);
                        }
                    }
                    Matrix<E> R = createDenseMatrix(rData, k, n, a);
                    info = (int) NativeSafe.invoke(DORGQR, LAPACK_ROW_MAJOR, m, k, k, segA, n, tau);
                    if (info != 0) throw new RuntimeException("DORGQR failed: " + info);
                    double[] qData = new double[m * k];
                    for (int i = 0; i < m; i++) {
                        for (int j = 0; j < k; j++) {
                            qData[i * k + j] = segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long) i * n + j);
                        }
                    }
                    return new QRResult<E>(createDenseMatrix(qData, m, k, a), R);
                }
            }
        } catch (Throwable t) {
            logger.debug("Native QR failed, falling back: {}", t.getMessage());
            return org.episteme.core.mathematics.linearalgebra.matrices.solvers.GenericQR.decompose(a, (org.episteme.core.mathematics.structures.rings.Field<E>)a.getScalarRing(), this);
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

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment u, vt, superb;
            int info;

            if (complex) {
                if (single) {
                    MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(a));
                    MemorySegment s = arena.allocate(ValueLayout.JAVA_FLOAT, (long) k);
                    u = arena.allocate(ValueLayout.JAVA_FLOAT, (long) m * m * 2);
                    vt = arena.allocate(ValueLayout.JAVA_FLOAT, (long) n * n * 2);
                    superb = arena.allocate(ValueLayout.JAVA_FLOAT, (long) Math.max(1, k - 1));
                    info = (int) NativeSafe.invoke(CGESVD, LAPACK_ROW_MAJOR, (byte) 'A', (byte) 'A', m, n, segA, n, s, u, m, vt, n, superb);
                    if (info != 0) throw new RuntimeException("CGESVD failed: " + info);
                    
                    float[] sArr = s.toArray(ValueLayout.JAVA_FLOAT);
                    List<E> sList = new ArrayList<>(k);
                    for (float v : sArr) sList.add((E) (Object) Real.of((double)v));
                    Vector<E> S = new DenseVector<>(sList, (Ring<E>) a.getScalarRing());
                    Matrix<E> U = createDenseMatrix(u.toArray(ValueLayout.JAVA_FLOAT), m, m, a);
                    float[] vtArr = vt.toArray(ValueLayout.JAVA_FLOAT);
                    float[] vArr = new float[n * n * 2];
                    for (int i = 0; i < n; i++)
                        for (int j = 0; j < n; j++) {
                            vArr[(j * n + i) * 2] = vtArr[(i * n + j) * 2];
                            vArr[(j * n + i) * 2 + 1] = vtArr[(i * n + j) * 2 + 1];
                        }
                    Matrix<E> V = createDenseMatrix(vArr, n, n, a);
                    return new SVDResult<>(U, S, V);
                } else {
                    MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                    MemorySegment s = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) k);
                    u = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * m * 2);
                    vt = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n * 2);
                    superb = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) Math.max(1, k - 1));
                    info = (int) NativeSafe.invoke(ZGESVD, LAPACK_ROW_MAJOR, (byte) 'A', (byte) 'A', m, n, segA, n, s, u, m, vt, n, superb);
                    if (info != 0) throw new RuntimeException("ZGESVD failed: " + info);
                    
                    double[] sArr = s.toArray(ValueLayout.JAVA_DOUBLE);
                    List<E> sList = new ArrayList<>(k);
                    for (double v : sArr) sList.add((E) (Object) Real.of(v));
                    Vector<E> S = new DenseVector<>(sList, (Ring<E>) a.getScalarRing());
                    Matrix<E> U = createDenseMatrix(u.toArray(ValueLayout.JAVA_DOUBLE), m, m, a);
                    double[] vtArr = vt.toArray(ValueLayout.JAVA_DOUBLE);
                    double[] vArr = new double[n * n * 2];
                    for (int i = 0; i < n; i++)
                        for (int j = 0; j < n; j++) {
                            vArr[(j * n + i) * 2] = vtArr[(i * n + j) * 2];
                            vArr[(j * n + i) * 2 + 1] = vtArr[(i * n + j) * 2 + 1];
                        }
                    Matrix<E> V = createDenseMatrix(vArr, n, n, a);
                    return new SVDResult<>(U, S, V);
                }
            } else {
                if (single) {
                    MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(a));
                    MemorySegment s = arena.allocate(ValueLayout.JAVA_FLOAT, (long) k);
                    u = arena.allocate(ValueLayout.JAVA_FLOAT, (long) m * m);
                    vt = arena.allocate(ValueLayout.JAVA_FLOAT, (long) n * n);
                    superb = arena.allocate(ValueLayout.JAVA_FLOAT, (long) Math.max(1, k - 1));
                    info = (int) NativeSafe.invoke(SGESVD, LAPACK_ROW_MAJOR, (byte) 'A', (byte) 'A', m, n, segA, n, s, u, m, vt, n, superb);
                    if (info != 0) throw new RuntimeException("SGESVD failed: " + info);
                    
                    float[] sArr = s.toArray(ValueLayout.JAVA_FLOAT);
                    List<E> sList = new ArrayList<>(k);
                    for (float v : sArr) sList.add((E) (Object) Real.of((double)v));
                    Vector<E> S = new DenseVector<>(sList, (Ring<E>) a.getScalarRing());
                    Matrix<E> U = createDenseMatrix(u.toArray(ValueLayout.JAVA_FLOAT), m, m, a);
                    float[] vtArr = vt.toArray(ValueLayout.JAVA_FLOAT);
                    float[] vArr = new float[n * n];
                    for (int i = 0; i < n; i++)
                        for (int j = 0; j < n; j++)
                            vArr[j * n + i] = vtArr[i * n + j];
                    Matrix<E> V = createDenseMatrix(vArr, n, n, a);
                    return new SVDResult<>(U, S, V);
                } else {
                    MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
                    MemorySegment s = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) k);
                    u = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * m);
                    vt = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
                    superb = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) Math.max(1, k - 1));
                    info = (int) NativeSafe.invoke(DGESVD, LAPACK_ROW_MAJOR, (byte) 'A', (byte) 'A', m, n, segA, n, s, u, m, vt, n, superb);
                    if (info != 0) throw new RuntimeException("DGESVD failed: " + info);
                    
                    double[] sArr = s.toArray(ValueLayout.JAVA_DOUBLE);
                    List<E> sList = new ArrayList<>(k);
                    for (double v : sArr) sList.add((E) (Object) Real.of(v));
                    Vector<E> S = new DenseVector<>(sList, (Ring<E>) a.getScalarRing());
                    Matrix<E> U = createDenseMatrix(u.toArray(ValueLayout.JAVA_DOUBLE), m, m, a);
                    double[] vtArr = vt.toArray(ValueLayout.JAVA_DOUBLE);
                    double[] vArr = new double[n * n];
                    for (int i = 0; i < n; i++)
                        for (int j = 0; j < n; j++)
                            vArr[j * n + i] = vtArr[i * n + j];
                    Matrix<E> V = createDenseMatrix(vArr, n, n, a);
                    return new SVDResult<>(U, S, V);
                }
            }
        } catch (Throwable t) { 
            logger.debug("Native SVD failed, falling back: {}", t.getMessage());
            return org.episteme.core.mathematics.linearalgebra.matrices.solvers.GenericSVD.decompose(a, (org.episteme.core.mathematics.structures.rings.Field<E>)a.getScalarRing(), this);
        }
    }

    @Override
    public EigenResult<E> eigen(Matrix<E> a) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": eigen() not available");
        int n = a.rows();
        boolean complex = isComplex(a);
        boolean single = isFloat(a);

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment w, segA;
            int info;

            if (complex) {
                if (single) {
                    w = arena.allocate(ValueLayout.JAVA_FLOAT, n);
                    segA = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(a));
                    info = (int) NativeSafe.invoke(CHEEV, LAPACK_ROW_MAJOR, (byte) 'V', (byte) 'U', n, segA, n, w);
                    if (info != 0) throw new RuntimeException("CHEEV failed: " + info);
                    float[] eigenvalues = w.toArray(ValueLayout.JAVA_FLOAT);
                    float[] eigenvectorsVec = segA.toArray(ValueLayout.JAVA_FLOAT);
                    E[] evData = (E[]) new Object[n];
                    for (int i = 0; i < n; i++) evData[i] = (E) (Object) Real.of((double)eigenvalues[i]);
                    Vector<E> vW = Vector.of(java.util.Arrays.asList(evData), (Ring<E>) a.getScalarRing());
                    return new EigenResult<E>(createDenseMatrix(eigenvectorsVec, n, n, a), vW);
                } else {
                    w = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
                    segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                    info = (int) NativeSafe.invoke(ZHEEV, LAPACK_ROW_MAJOR, (byte) 'V', (byte) 'U', n, segA, n, w);
                    if (info != 0) throw new RuntimeException("ZHEEV failed: " + info);
                    double[] eigenvalues = w.toArray(ValueLayout.JAVA_DOUBLE);
                    double[] eigenvectorsVec = segA.toArray(ValueLayout.JAVA_DOUBLE);
                    E[] evData = (E[]) new Object[n];
                    for (int i = 0; i < n; i++) evData[i] = (E) (Object) Real.of(eigenvalues[i]);
                    Vector<E> vW = Vector.of(java.util.Arrays.asList(evData), (Ring<E>) a.getScalarRing());
                    return new EigenResult<E>(createDenseMatrix(eigenvectorsVec, n, n, a), vW);
                }
            } else {
                if (single) {
                    w = arena.allocate(ValueLayout.JAVA_FLOAT, n);
                    segA = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(a));
                    info = (int) NativeSafe.invoke(SSYEV, LAPACK_ROW_MAJOR, (byte) 'V', (byte) 'U', n, segA, n, w);
                    if (info != 0) throw new RuntimeException("SSYEV failed: " + info);
                    float[] eigenvalues = w.toArray(ValueLayout.JAVA_FLOAT);
                    float[] eigenvectorsVec = segA.toArray(ValueLayout.JAVA_FLOAT);
                    E[] evData = (E[]) new Object[n];
                    for (int i = 0; i < n; i++) evData[i] = (E) (Object) Real.of((double)eigenvalues[i]);
                    Vector<E> vW = Vector.of(java.util.Arrays.asList(evData), (Ring<E>) a.getScalarRing());
                    return new EigenResult<E>(createDenseMatrix(eigenvectorsVec, n, n, a), vW);
                } else {
                    w = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
                    segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
                    info = (int) NativeSafe.invoke(DSYEV, LAPACK_ROW_MAJOR, (byte) 'V', (byte) 'U', n, segA, n, w);
                    if (info != 0) throw new RuntimeException("DSYEV failed: " + info);
                    double[] eigenvalues = w.toArray(ValueLayout.JAVA_DOUBLE);
                    double[] eigenvectorsVec = segA.toArray(ValueLayout.JAVA_DOUBLE);
                    E[] evData = (E[]) new Object[n];
                    for (int i = 0; i < n; i++) evData[i] = (E) (Object) Real.of(eigenvalues[i]);
                    Vector<E> vW = Vector.of(java.util.Arrays.asList(evData), (Ring<E>) a.getScalarRing());
                    return new EigenResult<E>(createDenseMatrix(eigenvectorsVec, n, n, a), vW);
                }
            }
        } catch (Throwable t) { 
            logger.debug("Native Eigen failed, falling back: {}", t.getMessage());
            try {
                return org.episteme.core.mathematics.linearalgebra.matrices.solvers.GenericEigen.decompose(a, (org.episteme.core.mathematics.structures.rings.Field<E>)a.getScalarRing(), this);
            } catch (Throwable t2) {
                throw new RuntimeException("Native Eigen and Generic Eigen both failed", t2);
            }
        }
    }






    @Override
    public String getDescription() {
        return "High-performance Linear Algebra using Project Panama and OpenBLAS/MKL.";
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public boolean isCompatible(Ring<?> ring) {
        return ring instanceof org.episteme.core.mathematics.sets.Reals || 
               ring instanceof org.episteme.core.mathematics.sets.Complexes;
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
    public void shutdown() {
        // No-op. Panama memory segments are managed via ScopedArena in operations.
    }

    @Override
    public Matrix<E> multiply(Matrix<E> A, Matrix<E> B) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": multiply() not available");
        
        if (isComplex(A)) {
            return multiplyComplex(A, B);
        }
        
        int m = A.rows();
        int k = A.cols();
        int n = B.cols();
        if (k != B.rows()) throw new IllegalArgumentException("Matrix dimensions mismatch");

        if (isFloat(A)) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(A));
                MemorySegment segB = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(B));
                MemorySegment segC = arena.allocate(ValueLayout.JAVA_FLOAT, (long) m * n);
                NativeSafe.invoke(SGEMM, CblasRowMajor, CblasNoTrans, CblasNoTrans, m, n, k, 1.0f, segA, k, segB, n, 0.0f, segC, n);
                float[] result = segC.toArray(ValueLayout.JAVA_FLOAT);
                return createDenseMatrix(result, m, n, A);
            } catch (Throwable t) { throw new RuntimeException("SGEMM failed", t); }
        }

        MemorySegment segC;
        Arena resultArena = Arena.ofAuto();
        segC = resultArena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);

        try (Arena tempArena = Arena.ofConfined()) {
            MemorySegment segA = tempArena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(A));
            MemorySegment segB = tempArena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(B));

            try {
                NativeSafe.invoke(DGEMM, CblasRowMajor, CblasNoTrans, CblasNoTrans, 
                                  m, n, k, 1.0, segA, k, segB, n, 0.0, segC, n);
            } catch (Throwable e) {
                logger.warn("FFM DGEMM failed: {}", e.getMessage());
                throw new RuntimeException("FFM Multiply Operation Failed", e);
            }
        }
        return createZeroCopyDoubleMatrix(segC, m, n, resultArena, A);
    }

    private boolean isComplex(Matrix<E> m) {
        return m.getScalarRing() instanceof org.episteme.core.mathematics.sets.Complexes;
    }

    private boolean isComplex(Vector<E> v) {
        return v.getScalarRing() instanceof org.episteme.core.mathematics.sets.Complexes;
    }

    private boolean isFloat(Matrix<E> m) {
        if (m.rows() > 0 && m.cols() > 0) {
            E val = m.get(0, 0);
            if (val != null && val.getClass().getSimpleName().equals("RealFloat")) return true;
            if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) {
                org.episteme.core.mathematics.numbers.complex.Complex c = (org.episteme.core.mathematics.numbers.complex.Complex) val;
                return c.getReal() != null && c.getReal().getClass().getSimpleName().equals("RealFloat");
            }
        }
        return false;
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
            data[i] = ((Real) (Object) v.get(i)).doubleValue();
        }
        return data;
    }

    private double[] toInterlacedDoubleArray(Matrix<E> m) {
        int rows = m.rows();
        int cols = m.cols();
        double[] data = new double[rows * cols * 2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                org.episteme.core.mathematics.numbers.complex.Complex c = (org.episteme.core.mathematics.numbers.complex.Complex) (Object) m.get(i, j);
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
            org.episteme.core.mathematics.numbers.complex.Complex c = (org.episteme.core.mathematics.numbers.complex.Complex) (Object) v.get(i);
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
            data[i] = (float) ((Real) (Object) v.get(i)).doubleValue();
        }
        return data;
    }

    private float[] toInterlacedFloatArray(Matrix<E> m) {
        int rows = m.rows();
        int cols = m.cols();
        float[] data = new float[rows * cols * 2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                org.episteme.core.mathematics.numbers.complex.Complex c = (org.episteme.core.mathematics.numbers.complex.Complex) (Object) m.get(i, j);
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
            org.episteme.core.mathematics.numbers.complex.Complex c = (org.episteme.core.mathematics.numbers.complex.Complex) (Object) v.get(i);
            data[i * 2] = (float) c.real();
            data[i * 2 + 1] = (float) c.imaginary();
        }
        return data;
    }


    private Matrix<E> multiplyComplex(Matrix<E> A, Matrix<E> B) {
        int m = A.rows();
        int k = A.cols();
        int n = B.cols();
        
        if (isFloat(A)) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(A));
                MemorySegment segB = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(B));
                MemorySegment segC = arena.allocate(ValueLayout.JAVA_FLOAT, (long) m * n * 2);
                MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_FLOAT, 1.0f, 0.0f);
                MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_FLOAT, 0.0f, 0.0f);
                NativeSafe.invoke(CGEMM, CblasRowMajor, CblasNoTrans, CblasNoTrans, m, n, k, alpha, segA, k, segB, n, beta, segC, n);
                float[] resData = segC.toArray(ValueLayout.JAVA_FLOAT);
                return createDenseMatrix(resData, m, n, A);
            } catch (Throwable t) { throw new RuntimeException("CGEMM failed", t); }
        }

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(A));
            MemorySegment segB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(B));
            MemorySegment segC = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n * 2);
            
            MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0, 0.0);
            MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0, 0.0);
            
            if (ZGEMM != null) {
                NativeSafe.invoke(ZGEMM, CblasRowMajor, CblasNoTrans, CblasNoTrans, m, n, k, alpha, segA, k, segB, n, beta, segC, n);
            } else {
                throw new UnsupportedOperationException("ZGEMM not available");
            }
            
            double[] resData = segC.toArray(ValueLayout.JAVA_DOUBLE);
            return createDenseMatrix(resData, m, n, A);
        } catch (Throwable t) {
            throw new RuntimeException("Complex FFM Multiply failed", t);
        }
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": matrix-vector multiply() not available");
        if (isComplex(a)) return multiplyComplex(a, b);
        
        int m = a.rows();
        int n = a.cols();
        if (n != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");

        if (isFloat(a)) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(a));
                MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(b));
                MemorySegment segY = arena.allocate(ValueLayout.JAVA_FLOAT, (long) m);
                NativeSafe.invoke(SGEMV, CblasRowMajor, CblasNoTrans, m, n, 1.0f, segA, n, segX, 1, 0.0f, segY, 1);
                float[] result = segY.toArray(ValueLayout.JAVA_FLOAT);
                return createDenseVector(result, m, a);
            } catch (Throwable t) { throw new RuntimeException("SGEMV failed", t); }
        }

        MemorySegment segY;
        Arena resultArena = Arena.ofAuto();
        segY = resultArena.allocate(ValueLayout.JAVA_DOUBLE, m);

        try (Arena tempArena = Arena.ofConfined()) {
            MemorySegment segA = tempArena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
            MemorySegment segX = tempArena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b));
            
            try {
                NativeSafe.invoke(DGEMV, CblasRowMajor, CblasNoTrans, m, n, 1.0, segA, n, segX, 1, 0.0, segY, 1);
            } catch (Throwable t) {
                logger.warn("FFM DGEMV failed: {}", t.getMessage());
                throw new RuntimeException("DGEMV failed", t);
            }
        }
        return createZeroCopyDoubleVector(segY, m, resultArena, a);
    }

    private Vector<E> multiplyComplex(Matrix<E> a, Vector<E> b) {
        int m = a.rows();
        int n = a.cols();

        if (isFloat(a)) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(a));
                MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(b));
                MemorySegment segY = arena.allocate(ValueLayout.JAVA_FLOAT, (long) m * 2);
                MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_FLOAT, 1.0f, 0.0f);
                MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_FLOAT, 0.0f, 0.0f);
                NativeSafe.invoke(CGEMV, CblasRowMajor, CblasNoTrans, m, n, alpha, segA, n, segX, 1, beta, segY, 1);
                float[] resData = segY.toArray(ValueLayout.JAVA_FLOAT);
                return createDenseVector(resData, m, a);
            } catch (Throwable t) { throw new RuntimeException("CGEMV failed", t); }
        }

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
            MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(b));
            MemorySegment segY = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * 2);

            MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0, 0.0);
            MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0, 0.0);

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
                    MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(a));
                    MemorySegment segB = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(b));
                    MemorySegment res = arena.allocate(ValueLayout.JAVA_FLOAT, 2);
                    // Single precision complex dot product (conjugated)
                    NativeSafe.invoke(CDOTC, n, segB, 1, segA, 1, res);
                    return (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(res.get(ValueLayout.JAVA_FLOAT, 0), res.get(ValueLayout.JAVA_FLOAT, 4));
                } catch (Throwable t) { throw new RuntimeException("CDOTC failed", t); }
            } else {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                    MemorySegment segB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(b));
                    MemorySegment res = arena.allocate(ValueLayout.JAVA_DOUBLE, 2);
                    NativeSafe.invoke(ZDOTC, n, segB, 1, segA, 1, res);
                    return (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(res.get(ValueLayout.JAVA_DOUBLE, 0), res.get(ValueLayout.JAVA_DOUBLE, 8));
                } catch (Throwable t) { throw new RuntimeException("ZDOTC failed", t); }
            }
        }
        
        if (single) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(a));
                MemorySegment segY = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(b));
                return (E) (Object) org.episteme.core.mathematics.numbers.real.Real.of((double)(float) NativeSafe.invoke(SDOT, n, segX, 1, segY, 1));
            } catch (Throwable t) { throw new RuntimeException("SDOT failed", t); }
        }

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
            MemorySegment segY = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b));
            return (E) (Object) org.episteme.core.mathematics.numbers.real.Real.of((double) NativeSafe.invoke(DDOT, n, segX, 1, segY, 1));
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
                    MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(a));
                    return (E) (Object) Real.of((double)(float) NativeSafe.invoke(SCNRM2, n, segX, 1));
                } catch (Throwable t) { throw new RuntimeException("SCNRM2 failed", t); }
            } else {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                    return (E) (Object) Real.of((double) NativeSafe.invoke(DZNRM2, n, segX, 1));
                } catch (Throwable t) { throw new RuntimeException("DZNRM2 failed", t); }
            }
        }
        
        if (single) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(a));
                return (E) (Object) org.episteme.core.mathematics.numbers.real.Real.of((double)(float) NativeSafe.invoke(SNRM2, n, segX, 1));
            } catch (Throwable t) { throw new RuntimeException("SNRM2 failed", t); }
        }

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
            return (E) (Object) org.episteme.core.mathematics.numbers.real.Real.of((double) NativeSafe.invoke(DNRM2, n, segX, 1));
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
                    MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(a));
                    MemorySegment segY = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(b));
                    MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_FLOAT, 1.0f, 0.0f);
                    NativeSafe.invoke(CAXPY, n, alpha, segX, 1, segY, 1);
                    float[] resData = segY.toArray(ValueLayout.JAVA_FLOAT);
                    return createDenseVector(resData, n, a);
                } catch (Throwable t) { throw new RuntimeException("CAXPY failed", t); }
            } else {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                    MemorySegment segY = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(b));
                    MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0, 0.0);
                     NativeSafe.invoke(ZAXPY, n, alpha, segX, 1, segY, 1);
                    double[] resData = segY.toArray(ValueLayout.JAVA_DOUBLE);
                    return createDenseVector(resData, n, a);
                } catch (Throwable t) { throw new RuntimeException("ZAXPY failed", t); }
            }
        }
        
        if (single) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(a));
                MemorySegment segY = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(b));
                NativeSafe.invoke(SAXPY, n, 1.0f, segX, 1, segY, 1);
                float[] result = segY.toArray(ValueLayout.JAVA_FLOAT);
                return createDenseVector(result, n, a);
            } catch (Throwable t) { throw new RuntimeException("SAXPY failed", t); }
        }

        MemorySegment segY;
        Arena resultArena = Arena.ofAuto();
        try (Arena tempArena = Arena.ofConfined()) {
            MemorySegment segX = tempArena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
            segY = resultArena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b));
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
                    MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(b));
                    MemorySegment segY = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(a));
                    MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_FLOAT, -1.0f, 0.0f);
                    NativeSafe.invoke(CAXPY, n, alpha, segX, 1, segY, 1);
                    float[] resData = segY.toArray(ValueLayout.JAVA_FLOAT);
                    return createDenseVector(resData, n, a);
                } catch (Throwable t) { throw new RuntimeException("CAXPY failed", t); }
            } else {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(b));
                    MemorySegment segY = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                    MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, -1.0, 0.0);
                    NativeSafe.invoke(ZAXPY, n, alpha, segX, 1, segY, 1);
                    double[] resData = segY.toArray(ValueLayout.JAVA_DOUBLE);
                    return createDenseVector(resData, n, a);
                } catch (Throwable t) { throw new RuntimeException("ZAXPY complex failed", t); }
            }
        }
        
        if (single) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(b));
                MemorySegment segY = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(a));
                NativeSafe.invoke(SAXPY, n, -1.0f, segX, 1, segY, 1);
                float[] result = segY.toArray(ValueLayout.JAVA_FLOAT);
                return createDenseVector(result, n, a);
            } catch (Throwable t) { throw new RuntimeException("SAXPY failed", t); }
        }

        MemorySegment segY;
        Arena resultArena = Arena.ofAuto();
        try (Arena tempArena = Arena.ofConfined()) {
            MemorySegment segX = tempArena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b));
            segY = resultArena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
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
            org.episteme.core.mathematics.numbers.complex.Complex cScalar = (org.episteme.core.mathematics.numbers.complex.Complex) (Object) scalar;
            if (single) {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(vector));
                    MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_FLOAT, (float) cScalar.real(), (float) cScalar.imaginary());
                    NativeSafe.invoke(CSCAL, n, alpha, segX, 1);
                    float[] resData = segX.toArray(ValueLayout.JAVA_FLOAT);
                    return createDenseVector(resData, n, vector);
                } catch (Throwable t) { throw new RuntimeException("CSCAL failed", t); }
            } else {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(vector));
                    MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, cScalar.real(), cScalar.imaginary());
                    NativeSafe.invoke(ZSCAL, n, alpha, segX, 1);
                    double[] resData = segX.toArray(ValueLayout.JAVA_DOUBLE);
                    return createDenseVector(resData, n, vector);
                } catch (Throwable t) { throw new RuntimeException("ZSCAL failed", t); }
            }
        }

        double s = ((Real)(Object)scalar).doubleValue();
        if (single) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(vector));
                NativeSafe.invoke(SSCAL, n, (float) s, segX, 1);
                float[] resData = segX.toArray(ValueLayout.JAVA_FLOAT);
                return createDenseVector(resData, n, vector);
            } catch (Throwable t) { throw new RuntimeException("SSCAL failed", t); }
        }

        MemorySegment segX;
        Arena resultArena = Arena.ofAuto();
        try (Arena tempArena = Arena.ofConfined()) {
            segX = resultArena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(vector));
            NativeSafe.invoke(DSCAL, n, s, segX, 1);
        } catch (Throwable t) { throw new RuntimeException("DSCAL failed", t); }
        return createZeroCopyDoubleVector(segX, n, resultArena, vector);
    }


    public Vector<E> divide(Vector<E> v, E scalar) {
        if (isComplex(v)) {
            org.episteme.core.mathematics.numbers.complex.Complex sc = (org.episteme.core.mathematics.numbers.complex.Complex) (Object) scalar;
            return multiply(v, (E) (Object) sc.reciprocal());
        }
        Real sc = (Real) (Object) scalar;
        return multiply(v, (E) (Object) Real.of(1.0 / sc.doubleValue()));
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
                    MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(a));
                    MemorySegment segY = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(b));
                    MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_FLOAT, 1.0f, 0.0f);
                    NativeSafe.invoke(CAXPY, len, alpha, segX, 1, segY, 1);
                    float[] result = segY.toArray(ValueLayout.JAVA_FLOAT);
                    return createDenseMatrix(result, m, n, a);
                } catch (Throwable t) { throw new RuntimeException("CAXPY matrix add failed", t); }
            } else {
                try (Arena arena = Arena.ofConfined()) {
                    int len = m * n;
                    MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                    MemorySegment segY = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(b));
                    MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0, 0.0);
                    NativeSafe.invoke(ZAXPY, len, alpha, segX, 1, segY, 1);
                    double[] result = segY.toArray(ValueLayout.JAVA_DOUBLE);
                    return createDenseMatrix(result, m, n, a);
                } catch (Throwable t) { throw new RuntimeException("ZAXPY matrix add failed", t); }
            }
        }

        if (single) {
            try (Arena arena = Arena.ofConfined()) {
                int len = m * n;
                MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(a));
                MemorySegment segY = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(b));
                NativeSafe.invoke(SAXPY, len, 1.0f, segX, 1, segY, 1);
                float[] result = segY.toArray(ValueLayout.JAVA_FLOAT);
                return createDenseMatrix(result, m, n, a);
            } catch (Throwable t) { throw new RuntimeException("SAXPY matrix add failed", t); }
        }

        MemorySegment segY;
        Arena resultArena = Arena.ofAuto();
        try (Arena tempArena = Arena.ofConfined()) {
            int len = m * n;
            MemorySegment segX = tempArena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
            segY = resultArena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b));
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
                    MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(b));
                    MemorySegment segY = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(a));
                    MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_FLOAT, -1.0f, 0.0f);
                    NativeSafe.invoke(CAXPY, len, alpha, segX, 1, segY, 1);
                    float[] result = segY.toArray(ValueLayout.JAVA_FLOAT);
                    return createDenseMatrix(result, m, n, a);
                } catch (Throwable t) { throw new RuntimeException("CAXPY matrix subtract failed", t); }
            } else {
                try (Arena arena = Arena.ofConfined()) {
                    int len = m * n;
                    MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(b));
                    MemorySegment segY = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                    MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, -1.0, 0.0);
                    NativeSafe.invoke(ZAXPY, len, alpha, segX, 1, segY, 1);
                    double[] result = segY.toArray(ValueLayout.JAVA_DOUBLE);
                    return createDenseMatrix(result, m, n, a);
                } catch (Throwable t) { throw new RuntimeException("ZAXPY matrix subtract failed", t); }
            }
        }

        if (single) {
            try (Arena arena = Arena.ofConfined()) {
                int len = m * n;
                MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(b));
                MemorySegment segY = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(a));
                NativeSafe.invoke(SAXPY, len, -1.0f, segX, 1, segY, 1);
                float[] result = segY.toArray(ValueLayout.JAVA_FLOAT);
                return createDenseMatrix(result, m, n, a);
            } catch (Throwable t) { throw new RuntimeException("SAXPY matrix subtract failed", t); }
        }

        MemorySegment segY;
        Arena resultArena = Arena.ofAuto();
        try (Arena tempArena = Arena.ofConfined()) {
            int len = m * n;
            MemorySegment segX = tempArena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b));
            segY = resultArena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
            NativeSafe.invoke(DAXPY, len, -1.0, segX, 1, segY, 1);
        } catch (Throwable t) { throw new RuntimeException("DAXPY matrix subtract failed", t); }
        return createZeroCopyDoubleMatrix(segY, m, n, resultArena, a);
    }


    @Override
    public CholeskyResult<E> cholesky(Matrix<E> a) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": cholesky() not available");
        int n = a.rows();
        if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        boolean complex = isComplex(a);
        boolean single = isFloat(a);

        if (complex) {
            if (single) {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(a));
                    int info = (int) NativeSafe.invoke(CPOTRF, LAPACK_ROW_MAJOR, (byte) 'L', n, segA, n);
                    if (info != 0) throw new ArithmeticException("CPOTRF failed: " + info);
                    float[] resData = segA.toArray(ValueLayout.JAVA_FLOAT);
                    for (int i = 0; i < n; i++) {
                        for (int j = i + 1; j < n; j++) {
                            resData[(i * n + j) * 2] = 0;
                            resData[(i * n + j) * 2 + 1] = 0;
                        }
                    }
                    return new CholeskyResult<>(createDenseMatrix(resData, n, n, a));
                } catch (Throwable t) { throw new RuntimeException(t); }
            } else {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                    int info = (int) NativeSafe.invoke(ZPOTRF, LAPACK_ROW_MAJOR, (byte) 'L', n, segA, n);
                    if (info != 0) throw new ArithmeticException("ZPOTRF failed: " + info);
                    double[] resData = segA.toArray(ValueLayout.JAVA_DOUBLE);
                    for (int i = 0; i < n; i++) {
                        for (int j = i + 1; j < n; j++) {
                            resData[(i * n + j) * 2] = 0;
                            resData[(i * n + j) * 2 + 1] = 0;
                        }
                    }
                    return new CholeskyResult<>(createDenseMatrix(resData, n, n, a));
                } catch (Throwable t) { throw new RuntimeException(t); }
            }
        } else {
            if (single) {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(a));
                    int info = (int) NativeSafe.invoke(SPOTRF, LAPACK_ROW_MAJOR, (byte) 'L', n, segA, n);
                    if (info != 0) throw new ArithmeticException("SPOTRF failed: " + info);
                    float[] resData = segA.toArray(ValueLayout.JAVA_FLOAT);
                    for (int i = 0; i < n; i++) {
                        for (int j = i + 1; j < n; j++) {
                            resData[i * n + j] = 0;
                        }
                    }
                    return new CholeskyResult<>(createDenseMatrix(resData, n, n, a));
                } catch (Throwable t) { throw new RuntimeException(t); }
            } else {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
                    int info = (int) NativeSafe.invoke(DPOTRF, LAPACK_ROW_MAJOR, (byte) 'L', n, segA, n);
                    if (info != 0) throw new ArithmeticException("DPOTRF failed: " + info);
                    double[] resData = segA.toArray(ValueLayout.JAVA_DOUBLE);
                    for (int i = 0; i < n; i++) {
                        for (int j = i + 1; j < n; j++) {
                            resData[i * n + j] = 0;
                        }
                    }
                    return new CholeskyResult<>(createDenseMatrix(resData, n, n, a));
                } catch (Throwable t) { throw new RuntimeException(t); }
            }
        }
    }

    @Override
    public LUResult<E> lu(Matrix<E> a) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": lu() not available");
        int m = a.rows(), n = a.cols();
        boolean complex = isComplex(a);
        boolean single = isFloat(a);

        if (complex) {
            if (single) {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(a));
                    MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, (long) Math.min(m, n));
                    int info = (int) NativeSafe.invoke(CGETRF, LAPACK_ROW_MAJOR, m, n, segA, n, segIpiv);
                    if (info >= 0) {
                        float[] packedData = segA.toArray(ValueLayout.JAVA_FLOAT);
                        int[] ipiv = segIpiv.toArray(ValueLayout.JAVA_INT);
                        return reconstructLU(packedData, ipiv, m, n, a, true);
                    }
                } catch (Throwable t) { logger.debug("Native Single Complex LU failed, falling back: {}", t.getMessage()); }
            } else {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                    MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, (long) Math.min(m, n));
                    int info = (int) NativeSafe.invoke(ZGETRF, LAPACK_ROW_MAJOR, m, n, segA, n, segIpiv);
                    if (info >= 0) {
                        double[] packedData = segA.toArray(ValueLayout.JAVA_DOUBLE);
                        int[] ipiv = segIpiv.toArray(ValueLayout.JAVA_INT);
                        return reconstructLU(packedData, ipiv, m, n, a, true);
                    }
                } catch (Throwable t) { logger.debug("Native Complex LU failed, falling back: {}", t.getMessage()); }
            }
            return org.episteme.core.mathematics.linearalgebra.matrices.solvers.GenericLU.decompose(a, (org.episteme.core.mathematics.structures.rings.Field<E>)a.getScalarRing(), this);
        } else {
            if (single) {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(a));
                    MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, (long) Math.min(m, n));
                    int info = (int) NativeSafe.invoke(SGETRF, LAPACK_ROW_MAJOR, m, n, segA, n, segIpiv);
                    if (info >= 0) {
                        float[] packedData = segA.toArray(ValueLayout.JAVA_FLOAT);
                        int[] ipiv = segIpiv.toArray(ValueLayout.JAVA_INT);
                        return reconstructLU(packedData, ipiv, m, n, a, false);
                    }
                } catch (Throwable t) { logger.debug("Native Single Real LU failed, falling back: {}", t.getMessage()); }
            } else {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
                    MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, (long) Math.min(m, n));
                    int info = (int) NativeSafe.invoke(DGETRF, LAPACK_ROW_MAJOR, m, n, segA, n, segIpiv);
                    if (info >= 0) {
                        double[] packedData = segA.toArray(ValueLayout.JAVA_DOUBLE);
                        int[] ipiv = segIpiv.toArray(ValueLayout.JAVA_INT);
                        return reconstructLU(packedData, ipiv, m, n, a, false);
                    }
                } catch (Throwable t) { logger.debug("Native Real LU failed, falling back: {}", t.getMessage()); }
            }
            return (LUResult<E>) (Object) org.episteme.core.mathematics.linearalgebra.matrices.solvers.GenericLU.decompose((Matrix<Real>) a, (org.episteme.core.mathematics.structures.rings.Field<Real>)a.getScalarRing(), (org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider<Real>)(Object)this);
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
            else pList.add((E) (Object) Real.of(idx));
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
                    MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(a));
                    MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_FLOAT, (float) sc.real(), (float) sc.imaginary());
                    NativeSafe.invoke(CSCAL, m * n, alpha, segX, 1);
                    float[] resData = segX.toArray(ValueLayout.JAVA_FLOAT);
                    return createDenseMatrix(resData, m, n, a);
                } catch (Throwable t) { throw new RuntimeException("CSCAL failed", t); }
            } else {
                 try (Arena arena = Arena.ofConfined()) {
                    MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                    MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, sc.real(), sc.imaginary());
                    NativeSafe.invoke(ZSCAL, m * n, alpha, segX, 1);
                    double[] resData = segX.toArray(ValueLayout.JAVA_DOUBLE);
                    return createDenseMatrix(resData, m, n, a);
                } catch (Throwable t) { throw new RuntimeException("ZSCAL failed", t); }
            }
        }

        double s = ((Real)(Object)scalar).doubleValue();
        if (single) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(a));
                NativeSafe.invoke(SSCAL, m * n, (float) s, segX, 1);
                float[] resData = segX.toArray(ValueLayout.JAVA_FLOAT);
                return createDenseMatrix(resData, m, n, a);
            } catch (Throwable t) { throw new RuntimeException("SSCAL failed", t); }
        }

        MemorySegment segX;
        Arena resultArena = Arena.ofAuto();
        try (Arena tempArena = Arena.ofConfined()) {
            segX = resultArena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
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
            if (complex) {
                target = (int) ((org.episteme.core.mathematics.numbers.complex.Complex)(Object)lu.P().get(i)).real();
            } else {
                target = (int) ((Real)(Object)lu.P().get(i)).doubleValue();
            }
            int swapIdx = i;
            for(int j=i; j<n; j++) {
                if(currentP[j] == target) {
                    swapIdx = j;
                    break;
                }
            }
            ipiv[i] = swapIdx + 1;
            int tmp = currentP[i];
            currentP[i] = currentP[swapIdx];
            currentP[swapIdx] = tmp;
        }

        try (Arena arena = Arena.ofConfined()) {
            boolean single = isFloat(lu.L());
            int typeSize = single ? 4 : 8;
            MemorySegment segLU = arena.allocate(single ? ValueLayout.JAVA_FLOAT : ValueLayout.JAVA_DOUBLE, complex ? (long) n * n * 2 : (long) n * n);
            
            if (complex) {
                if (single) {
                    float[] lData = toInterlacedFloatArray(lu.L());
                    float[] uData = toInterlacedFloatArray(lu.U());
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            if (i <= j) {
                                segLU.setAtIndex(ValueLayout.JAVA_FLOAT, (i * n + j) * 2, uData[(i * n + j) * 2]);
                                segLU.setAtIndex(ValueLayout.JAVA_FLOAT, (i * n + j) * 2 + 1, uData[(i * n + j) * 2 + 1]);
                            } else {
                                segLU.setAtIndex(ValueLayout.JAVA_FLOAT, (i * n + j) * 2, lData[(i * n + j) * 2]);
                                segLU.setAtIndex(ValueLayout.JAVA_FLOAT, (i * n + j) * 2 + 1, lData[(i * n + j) * 2 + 1]);
                            }
                        }
                    }
                } else {
                    double[] lData = toInterlacedDoubleArray(lu.L());
                    double[] uData = toInterlacedDoubleArray(lu.U());
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            if (i <= j) {
                                segLU.setAtIndex(ValueLayout.JAVA_DOUBLE, (i * n + j) * 2, uData[(i * n + j) * 2]);
                                segLU.setAtIndex(ValueLayout.JAVA_DOUBLE, (i * n + j) * 2 + 1, uData[(i * n + j) * 2 + 1]);
                            } else {
                                segLU.setAtIndex(ValueLayout.JAVA_DOUBLE, (i * n + j) * 2, lData[(i * n + j) * 2]);
                                segLU.setAtIndex(ValueLayout.JAVA_DOUBLE, (i * n + j) * 2 + 1, lData[(i * n + j) * 2 + 1]);
                            }
                        }
                    }
                }
            } else {
                if (single) {
                    float[] lData = toFloatArray(lu.L());
                    float[] uData = toFloatArray(lu.U());
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            if (i <= j) segLU.setAtIndex(ValueLayout.JAVA_FLOAT, i * n + j, uData[i * n + j]);
                            else segLU.setAtIndex(ValueLayout.JAVA_FLOAT, i * n + j, lData[i * n + j]);
                        }
                    }
                } else {
                    double[] lData = toDoubleArray(lu.L());
                    double[] uData = toDoubleArray(lu.U());
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            if (i <= j) segLU.setAtIndex(ValueLayout.JAVA_DOUBLE, i * n + j, uData[i * n + j]);
                            else segLU.setAtIndex(ValueLayout.JAVA_DOUBLE, i * n + j, lData[i * n + j]);
                        }
                    }
                }
            }
            MemorySegment segIpiv = arena.allocateFrom(ValueLayout.JAVA_INT, ipiv);
            MemorySegment segB;
            if (complex) {
                if (single) segB = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(b));
                else segB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(b));
            } else {
                if (single) segB = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(b));
                else segB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b));
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
            
            if (single) {
                float[] resultArr = segB.toArray(ValueLayout.JAVA_FLOAT);
                return createDenseVector(resultArr, n, lu.L());
            } else {
                double[] resultArr = segB.toArray(ValueLayout.JAVA_DOUBLE);
                return createDenseVector(resultArr, n, lu.L());
            }
        } catch (Throwable t) { throw new RuntimeException(t); }
    }
    
    @Override
    public Vector<E> solve(CholeskyResult<E> cholesky, Vector<E> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": solve(CholeskyResult) not available");
        int n = b.dimension();
        boolean complex = isComplex(cholesky.L());

        try (Arena arena = Arena.ofConfined()) {
            boolean single = isFloat(cholesky.L());
            MemorySegment segL;
            MemorySegment segB;
            
            if (complex) {
                if (single) {
                    segL = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(cholesky.L()));
                    segB = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toInterlacedFloatArray(b));
                } else {
                    segL = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(cholesky.L()));
                    segB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(b));
                }
            } else {
                if (single) {
                    segL = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(cholesky.L()));
                    segB = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(b));
                } else {
                    segL = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(cholesky.L()));
                    segB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b));
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
            
            if (single) {
                float[] resultArr = segB.toArray(ValueLayout.JAVA_FLOAT);
                return createDenseVector(resultArr, n, cholesky.L());
            } else {
                double[] resultArr = segB.toArray(ValueLayout.JAVA_DOUBLE);
                return createDenseVector(resultArr, n, cholesky.L());
            }
        } catch (Throwable t) { throw new RuntimeException(t); }
    }

    @Override
    public Vector<E> solve(org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<E> qr, Vector<E> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": solve(QRResult) not available");
        Vector<E> qtb = multiply(transpose(qr.Q()), b);
        return solve(qr.R(), qtb); // Evaluates LU(R) quickly and natively solves using our optimized solve(LU) via GETRS.
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return null;
    }

    private Matrix<E> createDenseMatrix(double[] data, int rows, int cols, Matrix<E> ref) {
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

    @SuppressWarnings("unchecked")
    private Matrix<E> createZeroCopyDoubleMatrix(MemorySegment data, int rows, int cols, Arena arena, Matrix<E> ref) {
        org.episteme.nativ.mathematics.linearalgebra.matrices.storage.NativeDoubleMatrixStorage storage = new org.episteme.nativ.mathematics.linearalgebra.matrices.storage.NativeDoubleMatrixStorage(data, rows, cols, arena);
        return (Matrix<E>) new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<>((org.episteme.core.mathematics.linearalgebra.matrices.storage.MatrixStorage<E>)(Object) storage, this, ref.getScalarRing());
    }

    @SuppressWarnings("unchecked")
    private Vector<E> createZeroCopyDoubleVector(MemorySegment data, int dim, Arena arena, Matrix<E> ref) {
        org.episteme.nativ.mathematics.linearalgebra.vectors.storage.NativeDoubleVectorStorage storage = new org.episteme.nativ.mathematics.linearalgebra.vectors.storage.NativeDoubleVectorStorage(data, dim, arena);
        return (Vector<E>) new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>((org.episteme.core.mathematics.linearalgebra.vectors.storage.VectorStorage<E>)(Object) storage, this, ref.getScalarRing());
    }

    @SuppressWarnings("unchecked")
    private Vector<E> createZeroCopyDoubleVector(MemorySegment data, int dim, Arena arena, Vector<E> ref) {
        org.episteme.nativ.mathematics.linearalgebra.vectors.storage.NativeDoubleVectorStorage storage = new org.episteme.nativ.mathematics.linearalgebra.vectors.storage.NativeDoubleVectorStorage(data, dim, arena);
        return (Vector<E>) new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>((org.episteme.core.mathematics.linearalgebra.vectors.storage.VectorStorage<E>)(Object) storage, this, ref.getScalarRing());
    }

    private Vector<E> createDenseVector(double[] data, int dimension, Matrix<E> ref) {
        Ring<E> ring = (Ring<E>) ref.getScalarRing();
        if (isComplex(ref)) {
            org.episteme.core.mathematics.numbers.complex.Complex[] arr = new org.episteme.core.mathematics.numbers.complex.Complex[dimension];
            for (int i = 0; i < dimension; i++) {
                arr[i] = org.episteme.core.mathematics.numbers.complex.Complex.of(data[i * 2], data[i * 2 + 1]);
            }
            return org.episteme.core.mathematics.linearalgebra.Vector.of(java.util.Arrays.asList((E[]) arr), ring);
        } else {
            org.episteme.core.mathematics.numbers.real.Real[] arr = new org.episteme.core.mathematics.numbers.real.Real[dimension];
            for (int i = 0; i < dimension; i++) {
                arr[i] = org.episteme.core.mathematics.numbers.real.Real.of(data[i]);
            }
            return org.episteme.core.mathematics.linearalgebra.Vector.of(java.util.Arrays.asList((E[]) arr), ring);
        }
    }

    private Vector<E> createDenseVector(float[] data, int dimension, Matrix<E> ref) {
        Ring<E> ring = (Ring<E>) ref.getScalarRing();
        if (isComplex(ref)) {
            org.episteme.core.mathematics.numbers.complex.Complex[] arr = new org.episteme.core.mathematics.numbers.complex.Complex[dimension];
            for (int i = 0; i < dimension; i++) {
                arr[i] = org.episteme.core.mathematics.numbers.complex.Complex.of(data[i * 2], data[i * 2 + 1]);
            }
            return org.episteme.core.mathematics.linearalgebra.Vector.of(java.util.Arrays.asList((E[]) arr), ring);
        } else {
            org.episteme.core.mathematics.numbers.real.Real[] arr = new org.episteme.core.mathematics.numbers.real.Real[dimension];
            for (int i = 0; i < dimension; i++) {
                arr[i] = org.episteme.core.mathematics.numbers.real.Real.of(data[i]);
            }
            return org.episteme.core.mathematics.linearalgebra.Vector.of(java.util.Arrays.asList((E[]) arr), ring);
        }
    }

    private Vector<E> createDenseVector(double[] data, int dimension, Vector<E> ref) {
        Ring<E> ring = (Ring<E>) ref.getScalarRing();
        if (isComplex(ref)) {
            org.episteme.core.mathematics.numbers.complex.Complex[] arr = new org.episteme.core.mathematics.numbers.complex.Complex[dimension];
            for (int i = 0; i < dimension; i++) {
                arr[i] = org.episteme.core.mathematics.numbers.complex.Complex.of(data[i * 2], data[i * 2 + 1]);
            }
            return org.episteme.core.mathematics.linearalgebra.Vector.of(java.util.Arrays.asList((E[]) arr), ring);
        } else {
            org.episteme.core.mathematics.numbers.real.Real[] arr = new org.episteme.core.mathematics.numbers.real.Real[dimension];
            for (int i = 0; i < dimension; i++) {
                arr[i] = org.episteme.core.mathematics.numbers.real.Real.of(data[i]);
            }
            return org.episteme.core.mathematics.linearalgebra.Vector.of(java.util.Arrays.asList((E[]) arr), ring);
        }
    }

    private Vector<E> createDenseVector(float[] data, int dimension, Vector<E> ref) {
        Ring<E> ring = (Ring<E>) ref.getScalarRing();
        if (isComplex(ref)) {
            org.episteme.core.mathematics.numbers.complex.Complex[] arr = new org.episteme.core.mathematics.numbers.complex.Complex[dimension];
            for (int i = 0; i < dimension; i++) {
                arr[i] = org.episteme.core.mathematics.numbers.complex.Complex.of(data[i * 2], data[i * 2 + 1]);
            }
            return org.episteme.core.mathematics.linearalgebra.Vector.of(java.util.Arrays.asList((E[]) arr), ring);
        } else {
            org.episteme.core.mathematics.numbers.real.Real[] arr = new org.episteme.core.mathematics.numbers.real.Real[dimension];
            for (int i = 0; i < dimension; i++) {
                arr[i] = org.episteme.core.mathematics.numbers.real.Real.of(data[i]);
            }
            return org.episteme.core.mathematics.linearalgebra.Vector.of(java.util.Arrays.asList((E[]) arr), ring);
        }
    }

    @Override
    public void close() {
        // Arena is currently global or per-call, no-op for now.
    }
}
