/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.episteme.nativ.mathematics.linearalgebra.backends;

import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;
import org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix;
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
@SuppressWarnings("unchecked")
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
    private static MethodHandle DGEMM;
    private static MethodHandle DGEMV;
    private static MethodHandle DDOT;
    private static MethodHandle DAXPY;
    private static MethodHandle DNRM2;
    private static MethodHandle DZNRM2;
    private static MethodHandle DSCAL;
    private static MethodHandle DOMATCOPY;

    private static MethodHandle ZGEMM;
    private static MethodHandle ZGEMV;
    private static MethodHandle ZDOTC;
    private static MethodHandle ZAXPY;
    private static MethodHandle ZSCAL;
    
    // LAPACK Method Handles
    private static MethodHandle DGESV;
    private static MethodHandle DGETRF;
    private static MethodHandle DGETRI;
    private static MethodHandle DGETRS;
    private static MethodHandle DGEQRF;
    private static MethodHandle DORGQR;
    private static MethodHandle DGESVD;
    private static MethodHandle DPOTRF;
    private static MethodHandle DPOTRS;
    private static MethodHandle DSYEV;
    private static MethodHandle DGELS;

    private static MethodHandle ZGESV;
    private static MethodHandle ZGETRF;
    private static MethodHandle ZGETRI;
    private static MethodHandle ZGETRS;
    private static MethodHandle ZGEQRF;
    private static MethodHandle ZUNGQR;
    private static MethodHandle ZGESVD;
    private static MethodHandle ZPOTRF;
    private static MethodHandle ZPOTRS;
    private static MethodHandle ZHEEV;
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
                // BLAS Handles - Use JAVA_INT as standard, but we'll check availability
                FunctionDescriptor dgemmDesc = FunctionDescriptor.ofVoid(
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                        ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                        ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                Optional<MemorySegment> dgemmSym = NativeFFMLoader.findSymbol(LOOKUP, "cblas_dgemm");
                if (dgemmSym.isPresent()) {
                    DGEMM = LINKER.downcallHandle(dgemmSym.get(), dgemmDesc);
                }

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

                // Complex BLAS
                FunctionDescriptor complexGemmDesc = FunctionDescriptor.ofVoid(
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                        AddressLayout.ADDRESS, AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                        AddressLayout.ADDRESS, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                
                ZGEMM = NativeFFMLoader.findSymbol(LOOKUP, "cblas_zgemm")
                        .map(s -> LINKER.downcallHandle(s, complexGemmDesc)).orElse(null);

                FunctionDescriptor complexGemvDesc = FunctionDescriptor.ofVoid(
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS, AddressLayout.ADDRESS, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                ZGEMV = NativeFFMLoader.findSymbol(LOOKUP, "cblas_zgemv")
                        .map(s -> LINKER.downcallHandle(s, complexGemvDesc)).orElse(null);

                FunctionDescriptor zdotcDesc = FunctionDescriptor.ofVoid(
                        ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS
                );
                ZDOTC = NativeFFMLoader.findSymbol(LOOKUP, "cblas_zdotc_sub")
                        .map(s -> LINKER.downcallHandle(s, zdotcDesc)).orElse(null);

                FunctionDescriptor dznrm2Desc = FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE,
                        ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                DZNRM2 = NativeFFMLoader.findSymbol(LOOKUP, "cblas_dznrm2")
                        .map(s -> LINKER.downcallHandle(s, dznrm2Desc)).orElse(null);

                FunctionDescriptor zaxpyDesc = FunctionDescriptor.ofVoid(
                        ValueLayout.JAVA_INT, AddressLayout.ADDRESS, AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                ZAXPY = NativeFFMLoader.findSymbol(LOOKUP, "cblas_zaxpy")
                        .map(s -> LINKER.downcallHandle(s, zaxpyDesc)).orElse(null);

                FunctionDescriptor zscalDesc = FunctionDescriptor.ofVoid(
                        ValueLayout.JAVA_INT, AddressLayout.ADDRESS, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                ZSCAL = NativeFFMLoader.findSymbol(LOOKUP, "cblas_zscal")
                        .map(s -> LINKER.downcallHandle(s, zscalDesc)).orElse(null);

                // LAPACK
                FunctionDescriptor dgesvDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS,
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                DGESV = findLapackSymbol("LAPACKE_dgesv")
                    .map(s -> LINKER.downcallHandle(s, dgesvDesc)).orElse(null);

                FunctionDescriptor dgetrfDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS
                );
                DGETRF = findLapackSymbol("LAPACKE_dgetrf")
                    .map(s -> LINKER.downcallHandle(s, dgetrfDesc)).orElse(null);

                FunctionDescriptor dgetriDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS
                );
                DGETRI = findLapackSymbol("LAPACKE_dgetri")
                    .map(s -> LINKER.downcallHandle(s, dgetriDesc)).orElse(null);

                FunctionDescriptor dgetrsDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                DGETRS = findLapackSymbol("LAPACKE_dgetrs")
                    .map(s -> LINKER.downcallHandle(s, dgetrsDesc)).orElse(null);

                // QR Decomposition
                FunctionDescriptor dgeqrfDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS
                );
                DGEQRF = findLapackSymbol("LAPACKE_dgeqrf")
                    .map(s -> LINKER.downcallHandle(s, dgeqrfDesc)).orElse(null);

                FunctionDescriptor dorgqrDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS
                );
                DORGQR = findLapackSymbol("LAPACKE_dorgqr")
                    .map(s -> LINKER.downcallHandle(s, dorgqrDesc)).orElse(null);

                // Singular Value Decomposition
                FunctionDescriptor dgesvdDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_BYTE, 
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, AddressLayout.ADDRESS, 
                        ValueLayout.JAVA_INT, AddressLayout.ADDRESS, AddressLayout.ADDRESS, 
                        ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                        AddressLayout.ADDRESS
                );
                DGESVD = findLapackSymbol("LAPACKE_dgesvd")
                    .map(s -> LINKER.downcallHandle(s, dgesvdDesc)).orElse(null);

                // Cholesky
                FunctionDescriptor dpotrfDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                DPOTRF = findLapackSymbol("LAPACKE_dpotrf")
                    .map(s -> LINKER.downcallHandle(s, dpotrfDesc)).orElse(null);

                FunctionDescriptor dpotrsDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                DPOTRS = findLapackSymbol("LAPACKE_dpotrs")
                    .map(s -> LINKER.downcallHandle(s, dpotrsDesc)).orElse(null);

                // Eigen
                FunctionDescriptor dsyevDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_BYTE,
                        ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS
                );
                DSYEV = findLapackSymbol("LAPACKE_dsyev")
                    .map(s -> LINKER.downcallHandle(s, dsyevDesc)).orElse(null);

                FunctionDescriptor dgelsDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                DGELS = findLapackSymbol("LAPACKE_dgels")
                    .map(s -> LINKER.downcallHandle(s, dgelsDesc)).orElse(null);

                // Complex LAPACK
                FunctionDescriptor zgesvDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS,
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                ZGESV = findLapackSymbol("LAPACKE_zgesv")
                    .map(s -> LINKER.downcallHandle(s, zgesvDesc)).orElse(null);

                FunctionDescriptor zgetrfDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS
                );
                ZGETRF = findLapackSymbol("LAPACKE_zgetrf")
                    .map(s -> LINKER.downcallHandle(s, zgetrfDesc)).orElse(null);

                FunctionDescriptor zgetriDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS
                );
                ZGETRI = findLapackSymbol("LAPACKE_zgetri")
                    .map(s -> LINKER.downcallHandle(s, zgetriDesc)).orElse(null);

                FunctionDescriptor zgetrsDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                ZGETRS = findLapackSymbol("LAPACKE_zgetrs")
                    .map(s -> LINKER.downcallHandle(s, zgetrsDesc)).orElse(null);

                FunctionDescriptor zgeqrfDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS
                );
                ZGEQRF = findLapackSymbol("LAPACKE_zgeqrf")
                    .map(s -> LINKER.downcallHandle(s, zgeqrfDesc)).orElse(null);

                FunctionDescriptor zungqrDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS
                );
                ZUNGQR = findLapackSymbol("LAPACKE_zungqr")
                    .map(s -> LINKER.downcallHandle(s, zungqrDesc)).orElse(null);

                FunctionDescriptor zgesvdDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_BYTE, 
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, AddressLayout.ADDRESS, 
                        ValueLayout.JAVA_INT, AddressLayout.ADDRESS, AddressLayout.ADDRESS, 
                        ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                        AddressLayout.ADDRESS
                );
                ZGESVD = findLapackSymbol("LAPACKE_zgesvd")
                    .map(s -> LINKER.downcallHandle(s, zgesvdDesc)).orElse(null);

                ZPOTRF = findLapackSymbol("LAPACKE_zpotrf")
                    .map(s -> LINKER.downcallHandle(s, dpotrfDesc)).orElse(null);
                ZPOTRS = findLapackSymbol("LAPACKE_zpotrs")
                    .map(s -> LINKER.downcallHandle(s, dpotrsDesc)).orElse(null);

                FunctionDescriptor zheevDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS
                );
                ZHEEV = findLapackSymbol("LAPACKE_zheev")
                    .map(s -> LINKER.downcallHandle(s, zheevDesc)).orElse(null);

                FunctionDescriptor zgelsDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                ZGELS = findLapackSymbol("LAPACKE_zgels")
                    .map(s -> LINKER.downcallHandle(s, zgelsDesc)).orElse(null);

                FunctionDescriptor dgelsDescriptor = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, AddressLayout.ADDRESS,
                        ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                DGELS = findLapackSymbol("LAPACKE_dgels")
                    .map(s -> LINKER.downcallHandle(s, dgelsDescriptor)).orElse(null);

                available = (DGEMM != null && DGEMV != null && DDOT != null);
                if (available) {
                    logger.info("FFM: Backend initialized successfully. Handles: DGEMM={}, DGESV={}, DGETRI={}", (DGEMM != null), (DGESV != null), (DGETRI != null));
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
        
        int m = A.rows();
        int n = A.cols();
        boolean complex = isComplex(A);

        if (m == n) {
             if (n != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");
             if (complex) {
                 if (ZGESV == null) throw new UnsupportedOperationException(getName() + ": ZGESV not available");
                 try (Arena arena = Arena.ofConfined()) {
                     MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(A));
                     MemorySegment segB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(b));
                     MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, n);
                     int info = (int) ZGESV.invokeExact(LAPACK_ROW_MAJOR, n, 1, segA, n, segIpiv, segB, 1);
                     if (info != 0) throw new ArithmeticException("ZGESV failed: " + info);
                     double[] resData = segB.toArray(ValueLayout.JAVA_DOUBLE);
                     return createDenseVector(resData, n, A);
                 } catch (Throwable t) { throw new RuntimeException(t); }
             }
             if (DGESV == null) throw new UnsupportedOperationException(getName() + ": DGESV not available");
             try (Arena arena = Arena.ofConfined()) {
                 MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(A));
                 MemorySegment segB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b));
                 MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, n);
                 int info = (int) DGESV.invokeExact(LAPACK_ROW_MAJOR, n, 1, segA, n, segIpiv, segB, 1);
                 if (info != 0) throw new ArithmeticException("DGESV failed: " + info);
                 double[] result = segB.toArray(ValueLayout.JAVA_DOUBLE);
                 return createDenseVector(result, n, A);
             } catch (Throwable t) { throw new RuntimeException(t); }
        } else {
             // Least Squares
             if (complex) {
                 if (ZGELS == null) throw new UnsupportedOperationException(getName() + ": ZGELS not available");
                 try (Arena arena = Arena.ofConfined()) {
                     MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(A));
                     int maxDim = Math.max(m, n);
                     double[] bPad = new double[maxDim * 2];
                     double[] bOrig = toInterlacedDoubleArray(b);
                     System.arraycopy(bOrig, 0, bPad, 0, bOrig.length);
                     MemorySegment segB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, bPad);
                     int info = (int) ZGELS.invokeExact(LAPACK_ROW_MAJOR, (byte) 'N', m, n, 1, segA, n, segB, 1);
                     if (info != 0) throw new RuntimeException("ZGELS failed: " + info);
                     double[] resFull = segB.toArray(ValueLayout.JAVA_DOUBLE);
                     double[] resData = new double[n * 2];
                     System.arraycopy(resFull, 0, resData, 0, n * 2);
                     return createDenseVector(resData, n, A);
                 } catch (Throwable t) { throw new RuntimeException(t); }
             }
             if (DGELS == null) throw new UnsupportedOperationException(getName() + ": DGELS not available");
             try (Arena arena = Arena.ofConfined()) {
                 MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(A));
                 int maxDim = Math.max(m, n);
                 double[] bPad = new double[maxDim];
                 double[] bOrig = toDoubleArray(b);
                 System.arraycopy(bOrig, 0, bPad, 0, bOrig.length);
                 MemorySegment segB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, bPad);
                 int info = (int) DGELS.invokeExact(LAPACK_ROW_MAJOR, (byte) 'N', m, n, 1, segA, n, segB, 1);
                 if (info != 0) throw new RuntimeException("DGELS failed: " + info);
                 double[] result = new double[n];
                 MemorySegment.copy(segB, ValueLayout.JAVA_DOUBLE, 0L, result, 0, n);
                 return createDenseVector(result, n, A);
             } catch (Throwable t) { throw new RuntimeException(t); }
        }
    }

    private Vector<E> createDenseVector(double[] data, int n, Matrix<E> ref) {
        List<E> list = new ArrayList<>(n);
        Ring<E> ring = (Ring<E>) ref.getScalarRing();
        boolean complex = isComplex(ref);
        if (complex) {
            for (int i = 0; i < n; i++) {
                list.add((E)(Object)org.episteme.core.mathematics.numbers.complex.Complex.of(data[i * 2], data[i * 2 + 1]));
            }
        } else {
            for (int i = 0; i < n; i++) {
                list.add((E)(Object)Real.of(data[i]));
            }
        }
        return new DenseVector<>(list, ring);
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
                DOMATCOPY.invokeExact(CblasRowMajor, 112, m, n, 1.0, segA, n, segC, m);
                return (Matrix<E>) (Matrix<?>) res;
            } catch (Throwable t) {
                logger.error("FFM BLAS Transpose failed: {}", t.getMessage());
            }
        }

        // Fallback or if DOMATCOPY fails
        double[] data = toDoubleArray(a);
        double[] res = new double[data.length];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                res[j * m + i] = data[i * n + j];
            }
        }
        return (Matrix<E>) createDenseMatrix(res, n, m, a);
    }
    
    @Override
    public Matrix<E> inverse(Matrix<E> A) {
         if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": inverse() not available");
         int m = A.rows();
         int n = A.cols();
         if (m != n) return pseudoInverse(A);
         
         boolean complex = isComplex(A);
         if (complex) {
             if (ZGETRF == null || ZGETRI == null) throw new UnsupportedOperationException(getName() + ": complex inverse not available");
             try (Arena arena = Arena.ofConfined()) {
                 MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(A));
                 MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, n);
                 int info = (int) ZGETRF.invokeExact(LAPACK_ROW_MAJOR, n, n, segA, n, segIpiv);
                 if (info != 0) throw new ArithmeticException("ZGETRF failed: " + info);
                 info = (int) ZGETRI.invokeExact(LAPACK_ROW_MAJOR, n, segA, n, segIpiv);
                 if (info != 0) throw new ArithmeticException("ZGETRI failed: " + info);
                 double[] result = segA.toArray(ValueLayout.JAVA_DOUBLE);
                 return createDenseMatrix(result, n, n, A);
             } catch (Throwable t) { throw new RuntimeException(t); }
         }
         
         if (DGETRF == null || DGETRI == null) throw new UnsupportedOperationException(getName() + ": inverse() not available");
         try (Arena arena = Arena.ofConfined()) {
             MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(A));
             MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, n);
             int info = (int) DGETRF.invokeExact(LAPACK_ROW_MAJOR, n, n, segA, n, segIpiv);
             if (info != 0) throw new ArithmeticException("DGETRF failed: " + info);
             info = (int) DGETRI.invokeExact(LAPACK_ROW_MAJOR, n, segA, n, segIpiv);
             if (info != 0) throw new ArithmeticException("DGETRI failed: " + info);
             double[] result = segA.toArray(ValueLayout.JAVA_DOUBLE);
             return createDenseMatrix(result, n, n, A);
         } catch (Throwable t) { throw new RuntimeException(t); }
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
                double sVal = ((Real)(Object)svd.S().get(i)).doubleValue();
                if (sVal > 1e-12) sData[i][i] = org.episteme.core.mathematics.numbers.complex.Complex.of(1.0 / sVal);
            }
            sInv = new DenseMatrix<>((E[][]) sData, (Ring<E>) a.getScalarRing());
        } else {
            Real[][] sData = new Real[n][m];
            for(int i=0; i<n; i++) for(int j=0; j<m; j++) sData[i][j] = Real.ZERO;
            for(int i=0; i<k; i++) {
                double sVal = ((Real)(Object)svd.S().get(i)).doubleValue();
                if (sVal > 1e-12) sData[i][i] = Real.of(1.0 / sVal);
            }
            sInv = new DenseMatrix<>((E[][]) sData, (Ring<E>) a.getScalarRing());
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
         
         try (Arena arena = Arena.ofConfined()) {
             if (complex) {
                 if (ZGETRF == null) throw new UnsupportedOperationException("ZGETRF not available");
                 MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(A));
                 MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, n);
                 int info = (int) ZGETRF.invokeExact(LAPACK_ROW_MAJOR, n, n, segA, n, segIpiv);
                 if (info > 0) return (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.ZERO;
                 org.episteme.core.mathematics.numbers.complex.Complex det = org.episteme.core.mathematics.numbers.complex.Complex.of(1.0);
                 for(int i=0; i<n; i++) {
                     double r = segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long)(i*n + i)*2);
                     double im = segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long)(i*n + i)*2 + 1);
                     det = det.multiply(org.episteme.core.mathematics.numbers.complex.Complex.of(r, im));
                     if (segIpiv.getAtIndex(ValueLayout.JAVA_INT, (long)i) != i + 1) det = det.negate();
                 }
                 return (E) (Object) det;
             }
             if (DGETRF == null) throw new UnsupportedOperationException("DGETRF not available");
             MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(A));
             MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, n);
             int info = (int) DGETRF.invokeExact(LAPACK_ROW_MAJOR, n, n, segA, n, segIpiv);
             if (info > 0) return (E) (Object) Real.ZERO;
             double det = 1.0;
             for(int i=0; i<n; i++) {
                 det *= segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long) i * n + i);
                 if (segIpiv.getAtIndex(ValueLayout.JAVA_INT, (long) i) != i + 1) det = -det;
             }
             return (E) (Object) Real.of(det);
         } catch (Throwable e) { throw new RuntimeException(e); }
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

        try (Arena arena = Arena.ofConfined()) {
            if (complex) {
                if (ZGEQRF == null || ZUNGQR == null) throw new UnsupportedOperationException("ZGEQRF/ZUNGQR not available");
                MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                MemorySegment tau = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) k * 2);
                int info = (int) ZGEQRF.invokeExact(LAPACK_ROW_MAJOR, m, n, segA, n, tau);
                if (info != 0) throw new RuntimeException("ZGEQRF failed: " + info);

                double[] rData = new double[k * n * 2];
                for (int i = 0; i < k; i++) {
                    for (int j = i; j < n; j++) {
                        rData[(i * n + j) * 2] = segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long) (i * n + j) * 2);
                        rData[(i * n + j) * 2 + 1] = segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long) (i * n + j) * 2 + 1);
                    }
                }
                Matrix<E> R = createDenseMatrix(rData, k, n, a);

                info = (int) ZUNGQR.invokeExact(LAPACK_ROW_MAJOR, m, k, k, segA, n, tau);
                if (info != 0) throw new RuntimeException("ZUNGQR failed: " + info);

                double[] qData = new double[m * k * 2];
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < k; j++) {
                        qData[(i * k + j) * 2] = segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long) (i * n + j) * 2);
                        qData[(i * k + j) * 2 + 1] = segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long) (i * n + j) * 2 + 1);
                    }
                }
                Matrix<E> Q = createDenseMatrix(qData, m, k, a);
                return new QRResult<E>(Q, R);
            }

            if (DGEQRF == null || DORGQR == null) throw new UnsupportedOperationException("DGEQRF/DORGQR not available");
            MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
            MemorySegment tau = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) k);
            int info = (int) DGEQRF.invokeExact(LAPACK_ROW_MAJOR, m, n, segA, n, tau);
            if (info != 0) throw new RuntimeException("DGEQRF failed: " + info);

            double[] rData = new double[k * n];
            for (int i = 0; i < k; i++) {
                for (int j = i; j < n; j++) {
                    rData[i * n + j] = segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long) i * n + j);
                }
            }
            Matrix<E> R = createDenseMatrix(rData, k, n, a);

            info = (int) DORGQR.invokeExact(LAPACK_ROW_MAJOR, m, k, k, segA, n, tau);
            if (info != 0) throw new RuntimeException("DORGQR failed: " + info);

            double[] qData = new double[m * k];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < k; j++) {
                    qData[i * k + j] = segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long) i * n + j);
                }
            }
            Matrix<E> Q = createDenseMatrix(qData, m, k, a);
            return new QRResult<E>(Q, R);
        } catch (Throwable t) { throw new RuntimeException(t); }
    }

    @Override
    public SVDResult<E> svd(Matrix<E> a) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": svd() not available");
        int m = a.rows();
        int n = a.cols();
        int k = Math.min(m, n);
        boolean complex = isComplex(a);

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment s = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) k);
            MemorySegment u, vt, superb;
            int info;

            if (complex) {
                if (ZGESVD == null) throw new UnsupportedOperationException("ZGESVD not available");
                MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                u = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * m * 2);
                vt = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n * 2);
                superb = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) Math.max(1, k - 1));
                info = (int) ZGESVD.invokeExact(LAPACK_ROW_MAJOR, (byte) 'A', (byte) 'A', m, n, segA, n, s, u, m, vt, n, superb);
            } else {
                if (DGESVD == null) throw new UnsupportedOperationException("DGESVD not available");
                MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
                u = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * m);
                vt = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
                superb = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) Math.max(1, k - 1));
                info = (int) DGESVD.invokeExact(LAPACK_ROW_MAJOR, (byte) 'A', (byte) 'A', m, n, segA, n, s, u, m, vt, n, superb);
            }

            if (info != 0) throw new RuntimeException("SVD failed: " + info);

            double[] sData = s.toArray(ValueLayout.JAVA_DOUBLE);
            List<E> sList = new ArrayList<>(k);
            for (double v : sData) sList.add((E) (Object) Real.of(v));
            Vector<E> S = new DenseVector<>(sList, (Ring<E>) a.getScalarRing());

            Matrix<E> U = createDenseMatrix(u.toArray(ValueLayout.JAVA_DOUBLE), m, m, a);
            double[] vtDataArr = vt.toArray(ValueLayout.JAVA_DOUBLE);
            E[][] vObj = (E[][]) new Object[n][n];

            if (complex) {
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        double r = vtDataArr[(j * n + i) * 2];
                        double im = -vtDataArr[(j * n + i) * 2 + 1]; // Conjugate transpose for V
                        vObj[i][j] = (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(r, im);
                    }
                }
            } else {
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        vObj[i][j] = (E) (Object) Real.of(vtDataArr[j * n + i]);
                    }
                }
            }
            Matrix<E> V = new DenseMatrix<>(vObj, (Ring<E>) a.getScalarRing());
            return new SVDResult<E>(U, S, V);
        } catch (Throwable t) { throw new RuntimeException(t); }
    }

    @Override
    public EigenResult<E> eigen(Matrix<E> a) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": eigen() not available");
        int n = a.rows();
        boolean complex = isComplex(a);

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment w = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
            MemorySegment segA;
            int info;

            if (complex) {
                if (ZHEEV == null) throw new UnsupportedOperationException("ZHEEV not available");
                segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                info = (int) ZHEEV.invokeExact(LAPACK_ROW_MAJOR, (byte) 'V', (byte) 'U', n, segA, n, w);
            } else {
                if (DSYEV == null) throw new UnsupportedOperationException("DSYEV not available");
                segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
                info = (int) DSYEV.invokeExact(LAPACK_ROW_MAJOR, (byte) 'V', (byte) 'U', n, segA, n, w);
            }

            if (info != 0) throw new RuntimeException("Eigen failed: " + info);

            double[] eigenvalues = w.toArray(ValueLayout.JAVA_DOUBLE);
            double[] eigenvectorsVec = segA.toArray(ValueLayout.JAVA_DOUBLE);
            
            E[] evData = (E[]) new Object[n];
            for (int i = 0; i < n; i++) evData[i] = (E) (Object) Real.of(eigenvalues[i]);
            Vector<E> vW = Vector.of(java.util.Arrays.asList(evData), (Ring<E>) a.getScalarRing());

            return new EigenResult<E>(createDenseMatrix(eigenvectorsVec, n, n, a), vW);
        } catch (Throwable t) { throw new RuntimeException(t); }
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

        try (Arena arena = Arena.ofConfined()) {
            double[] arrA = toDoubleArray(A);
            double[] arrB = toDoubleArray(B);
            MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, arrA);
            MemorySegment segB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, arrB);
            MemorySegment segC = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);

            try {
                DGEMM.invokeExact(CblasRowMajor, CblasNoTrans, CblasNoTrans, 
                                  m, n, k, 1.0, segA, k, segB, n, 0.0, segC, n);
            } catch (Throwable e) {
                logger.warn("FFM DGEMM failed: {}", e.getMessage());
                throw new RuntimeException("FFM Multiply Operation Failed", e);
            }

            double[] result = segC.toArray(ValueLayout.JAVA_DOUBLE);
            return createDenseMatrix(result, m, n, A);
        }
    }

    private boolean isComplex(Matrix<E> m) {
        return m.getScalarRing() instanceof org.episteme.core.mathematics.sets.Complexes;
    }

    private boolean isComplex(Vector<E> v) {
        return v.getScalarRing() instanceof org.episteme.core.mathematics.sets.Complexes;
    }

    private double[] toDoubleArray(Matrix<E> m) {
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

    private Matrix<E> multiplyComplex(Matrix<E> A, Matrix<E> B) {
        int m = A.rows();
        int k = A.cols();
        int n = B.cols();
        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(A));
            MemorySegment segB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(B));
            MemorySegment segC = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n * 2);
            
            MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0, 0.0);
            MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0, 0.0);
            
            if (ZGEMM != null) {
                ZGEMM.invokeExact(CblasRowMajor, CblasNoTrans, CblasNoTrans, m, n, k, alpha, segA, k, segB, n, beta, segC, n);
            } else {
                throw new UnsupportedOperationException("ZGEMM not available");
            }
            
            double[] resData = segC.toArray(ValueLayout.JAVA_DOUBLE);
            E[] resultElements = (E[]) new Object[m * n];
            for (int i = 0; i < m * n; i++) {
                resultElements[i] = (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(resData[i * 2], resData[i * 2 + 1]);
            }
            return new DenseMatrix<>(resultElements, m, n, (Ring<E>) A.getScalarRing());
        } catch (Throwable t) {
            throw new RuntimeException("Complex FFM Multiply failed", t);
        }
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (isComplex(a)) return multiplyComplex(a, b);
        if (!IS_AVAILABLE || DGEMV == null) throw new UnsupportedOperationException(getName() + ": matrix-vector multiply() not available");
        int m = a.rows();
        int n = a.cols();
        if (n != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
            MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b));
            MemorySegment segY = arena.allocate(ValueLayout.JAVA_DOUBLE, m);
            
            DGEMV.invokeExact(CblasRowMajor, CblasNoTrans, m, n, 1.0, segA, n, segX, 1, 0.0, segY, 1);
            
            double[] result = segY.toArray(ValueLayout.JAVA_DOUBLE);
            return createDenseVector(result, m, a);
        } catch (Throwable t) {
            throw new RuntimeException("DGEMV failed", t);
        }
    }

    private Vector<E> multiplyComplex(Matrix<E> a, Vector<E> b) {
        if (!IS_AVAILABLE || ZGEMV == null) throw new UnsupportedOperationException(getName() + ": complex matrix-vector multiply() not available");
        int m = a.rows();
        int n = a.cols();
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
            MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(b));
            MemorySegment segY = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * 2);

            MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0, 0.0);
            MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0, 0.0);

            ZGEMV.invokeExact(CblasRowMajor, CblasNoTrans, m, n, alpha, segA, n, segX, 1, beta, segY, 1);

            double[] resData = segY.toArray(ValueLayout.JAVA_DOUBLE);
            E[] result = (E[]) new Object[m];
            for (int i = 0; i < m; i++) result[i] = (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(resData[i * 2], resData[i * 2 + 1]);
            return DenseVector.of(java.util.Arrays.asList(result), (Ring<E>) a.getScalarRing());
        } catch (Throwable t) {
            throw new RuntimeException("ZGEMV failed", t);
        }
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
         if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": dot() not available");
         int n = a.dimension();
         if (isComplex(a)) {
             try (Arena arena = Arena.ofConfined()) {
                 MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                 MemorySegment segY = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(b));
                 MemorySegment res = arena.allocate(ValueLayout.JAVA_DOUBLE, 2);
                 ZDOTC.invokeExact(n, segX, 1, segY, 1, res);
                 return (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(res.get(ValueLayout.JAVA_DOUBLE, 0), res.get(ValueLayout.JAVA_DOUBLE, 8));
             } catch (Throwable t) { throw new RuntimeException("ZDOTC failed", t); }
         }
         try (Arena arena = Arena.ofConfined()) {
             MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
             for(int i=0; i<n; i++) segX.setAtIndex(ValueLayout.JAVA_DOUBLE, i, ((Real)(Object)a.get(i)).doubleValue());
             MemorySegment segY = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
             for(int i=0; i<n; i++) segY.setAtIndex(ValueLayout.JAVA_DOUBLE, i, ((Real)(Object)b.get(i)).doubleValue());
             try { return (E) (Object) org.episteme.core.mathematics.numbers.real.Real.of((double) DDOT.invokeExact(n, segX, 1, segY, 1)); } catch (Throwable e) { throw new RuntimeException(e); }
         }
    }
    
    @Override
    public E norm(Vector<E> a) {
         if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": norm() not available");
         int n = a.dimension();
         if (isComplex(a)) {
             try (Arena arena = Arena.ofConfined()) {
                 MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                 return (E) (Object) Real.of((double) DZNRM2.invokeExact(n, segX, 1));
             } catch (Throwable t) { throw new RuntimeException("DZNRM2 failed", t); }
         }
         try (Arena arena = Arena.ofConfined()) {
             MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
             for(int i=0; i<n; i++) segX.setAtIndex(ValueLayout.JAVA_DOUBLE, i, ((Real)(Object)a.get(i)).doubleValue());
             try { return (E) (Object) org.episteme.core.mathematics.numbers.real.Real.of((double) DNRM2.invokeExact(n, segX, 1)); } catch (Throwable e) { throw new RuntimeException(e); }
         }
    }

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": Vector add() not available");
        int n = a.dimension();
        if (isComplex(a)) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                MemorySegment segY = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(b));
                MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0, 0.0);
                ZAXPY.invokeExact(n, alpha, segX, 1, segY, 1);
                double[] resData = segY.toArray(ValueLayout.JAVA_DOUBLE);
                E[] result = (E[]) new Object[n];
                for (int i = 0; i < n; i++) result[i] = (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(resData[i * 2], resData[i * 2 + 1]);
                return DenseVector.of(java.util.Arrays.asList(result), (Ring<E>) a.getScalarRing());
            } catch (Throwable t) { throw new RuntimeException("ZAXPY failed", t); }
        }
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
            MemorySegment segY = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
            for (int i = 0; i < n; i++) {
                segX.setAtIndex(ValueLayout.JAVA_DOUBLE, i, ((Real)(Object)a.get(i)).doubleValue());
                segY.setAtIndex(ValueLayout.JAVA_DOUBLE, i, ((Real)(Object)b.get(i)).doubleValue());
            }
            try { DAXPY.invokeExact(n, 1.0, segX, 1, segY, 1); } catch (Throwable e) { throw new RuntimeException(e); }
            E[] result = (E[]) new Object[n];
            for (int i = 0; i < n; i++) result[i] = (E) (Object) org.episteme.core.mathematics.numbers.real.Real.of(segY.getAtIndex(ValueLayout.JAVA_DOUBLE, i));
            return DenseVector.of(java.util.Arrays.asList(result), (Ring<E>)a.getScalarRing());
        }
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": Vector subtract() not available");
        int n = a.dimension();
        if (isComplex(a)) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(b));
                MemorySegment segY = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, -1.0, 0.0);
                ZAXPY.invokeExact(n, alpha, segX, 1, segY, 1);
                double[] resData = segY.toArray(ValueLayout.JAVA_DOUBLE);
                E[] result = (E[]) new Object[n];
                for (int i = 0; i < n; i++) result[i] = (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(resData[i * 2], resData[i * 2 + 1]);
                return DenseVector.of(java.util.Arrays.asList(result), (Ring<E>) a.getScalarRing());
            } catch (Throwable t) { throw new RuntimeException("ZAXPY complex failed", t); }
        }
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
            MemorySegment segY = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
            for (int i = 0; i < n; i++) {
                segX.setAtIndex(ValueLayout.JAVA_DOUBLE, i, ((Real)(Object)b.get(i)).doubleValue());
                segY.setAtIndex(ValueLayout.JAVA_DOUBLE, i, ((Real)(Object)a.get(i)).doubleValue());
            }
            try { DAXPY.invokeExact(n, -1.0, segX, 1, segY, 1); } catch (Throwable e) { throw new RuntimeException(e); }
            E[] result = (E[]) new Object[n];
            for (int i = 0; i < n; i++) result[i] = (E) (Object) org.episteme.core.mathematics.numbers.real.Real.of(segY.getAtIndex(ValueLayout.JAVA_DOUBLE, i));
            return DenseVector.of(java.util.Arrays.asList(result), (Ring<E>)a.getScalarRing());
        }
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": Vector multiply() not available");
        int n = vector.dimension();
        if (isComplex(vector)) {
            org.episteme.core.mathematics.numbers.complex.Complex cScalar = (org.episteme.core.mathematics.numbers.complex.Complex) (Object) scalar;
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(vector));
                MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, cScalar.real(), cScalar.imaginary());
                ZSCAL.invokeExact(n, alpha, segX, 1);
                double[] resData = segX.toArray(ValueLayout.JAVA_DOUBLE);
                E[] result = (E[]) new Object[n];
                for (int i = 0; i < n; i++) result[i] = (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(resData[i * 2], resData[i * 2 + 1]);
                return DenseVector.of(java.util.Arrays.asList(result), (Ring<E>) vector.getScalarRing());
            } catch (Throwable t) { throw new RuntimeException("ZSCAL failed", t); }
        }
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
            for (int i = 0; i < n; i++) segX.setAtIndex(ValueLayout.JAVA_DOUBLE, i, ((Real)(Object)vector.get(i)).doubleValue());
            try { DSCAL.invokeExact(n, ((Real)(Object)scalar).doubleValue(), segX, 1); } catch (Throwable e) { throw new RuntimeException(e); }
            E[] result = (E[]) new Object[n];
            for (int i = 0; i < n; i++) result[i] = (E) (Object) org.episteme.core.mathematics.numbers.real.Real.of(segX.getAtIndex(ValueLayout.JAVA_DOUBLE, i));
            return DenseVector.of(java.util.Arrays.asList(result), (Ring<E>)vector.getScalarRing());
        }
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": Matrix add() not available");
        int m = a.rows(), n = a.cols();
        if (isComplex(a)) {
             try (Arena arena = Arena.ofConfined()) {
                int len = m * n;
                MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                MemorySegment segY = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(b));
                MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0, 0.0);
                ZAXPY.invokeExact(len, alpha, segX, 1, segY, 1);
                double[] result = segY.toArray(ValueLayout.JAVA_DOUBLE);
                return createDenseMatrix(result, m, n, a);
            } catch (Throwable t) { throw new RuntimeException("ZAXPY matrix add failed", t); }
        }
        try (Arena arena = Arena.ofConfined()) {
            int len = m * n;
            MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, len);
            MemorySegment segY = arena.allocate(ValueLayout.JAVA_DOUBLE, len);
            double[] arrA = toDoubleArray(a);
            double[] arrB = toDoubleArray(b);
            MemorySegment.copy(arrA, 0, segX, ValueLayout.JAVA_DOUBLE, 0L, (int) Math.min(arrA.length, (long)len));
            MemorySegment.copy(arrB, 0, segY, ValueLayout.JAVA_DOUBLE, 0L, (int) Math.min(arrB.length, (long)len));
            try { DAXPY.invokeExact(len, 1.0, segX, 1, segY, 1); } catch (Throwable e) { throw new RuntimeException(e); }
            double[] result = segY.toArray(ValueLayout.JAVA_DOUBLE);
            return createDenseMatrix(result, m, n, a);
        }
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": Matrix subtract() not available");
        int m = a.rows(), n = a.cols();
        if (isComplex(a)) {
             try (Arena arena = Arena.ofConfined()) {
                int len = m * n;
                MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(b));
                MemorySegment segY = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, -1.0, 0.0);
                ZAXPY.invokeExact(len, alpha, segX, 1, segY, 1);
                double[] result = segY.toArray(ValueLayout.JAVA_DOUBLE);
                return createDenseMatrix(result, m, n, a);
            } catch (Throwable t) { throw new RuntimeException("ZAXPY matrix subtract failed", t); }
        }
        try (Arena arena = Arena.ofConfined()) {
            int len = m * n;
            MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
            MemorySegment segY = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b));
            try { DAXPY.invokeExact(len, -1.0, segY, 1, segX, 1); } catch (Throwable e) { throw new RuntimeException(e); }
            double[] resData = segX.toArray(ValueLayout.JAVA_DOUBLE);
            return createDenseMatrix(resData, m, n, a);
        }
    }


    @Override
    public CholeskyResult<E> cholesky(Matrix<E> a) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": cholesky() not available");
        int n = a.rows();
        if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        boolean complex = isComplex(a);

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, complex ? toInterlacedDoubleArray(a) : toDoubleArray(a));
            int info;
            if (complex) {
                if (ZPOTRF == null) throw new UnsupportedOperationException("ZPOTRF not available");
                info = (int) ZPOTRF.invokeExact(LAPACK_ROW_MAJOR, (byte) 'L', n, segA, n);
            } else {
                if (DPOTRF == null) throw new UnsupportedOperationException("DPOTRF not available");
                info = (int) DPOTRF.invokeExact(LAPACK_ROW_MAJOR, (byte) 'L', n, segA, n);
            }
            if (info != 0) throw new ArithmeticException("POTRF failed: " + info);
            
            double[] resData = segA.toArray(ValueLayout.JAVA_DOUBLE);
            // Zero out upper triangle for Cholesky factor L
            if (complex) {
                for (int i = 0; i < n; i++) {
                    for (int j = i + 1; j < n; j++) {
                        resData[(i * n + j) * 2] = 0;
                        resData[(i * n + j) * 2 + 1] = 0;
                    }
                }
            } else {
                for (int i = 0; i < n; i++) {
                    for (int j = i + 1; j < n; j++) {
                        resData[i * n + j] = 0;
                    }
                }
            }
            Matrix<E> L = createDenseMatrix(resData, n, n, a);
            return new CholeskyResult<>(L);
        } catch (Throwable t) { throw new RuntimeException(t); }
    }

    @Override
    public LUResult<E> lu(Matrix<E> a) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": lu() not available");
        int m = a.rows(), n = a.cols();
        boolean complex = isComplex(a);

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, complex ? toInterlacedDoubleArray(a) : toDoubleArray(a));
            MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, Math.min(m, n));
            int info;
            if (complex) {
                if (ZGETRF == null) throw new UnsupportedOperationException("ZGETRF not available");
                info = (int) ZGETRF.invokeExact(LAPACK_ROW_MAJOR, m, n, segA, n, segIpiv);
            } else {
                if (DGETRF == null) throw new UnsupportedOperationException("DGETRF not available");
                info = (int) DGETRF.invokeExact(LAPACK_ROW_MAJOR, m, n, segA, n, segIpiv);
            }
            if (info < 0) throw new IllegalArgumentException("GETRF failed: illegal argument " + (-info));
            
            double[] packedData = segA.toArray(ValueLayout.JAVA_DOUBLE);
            int[] ipiv = segIpiv.toArray(ValueLayout.JAVA_INT);
            
            // Reconstruct L, U, P
            int minMN = Math.min(m, n);
            double[] lData = complex ? new double[m * minMN * 2] : new double[m * minMN];
            double[] uData = complex ? new double[minMN * n * 2] : new double[minMN * n];
            
            if (complex) {
                // L: unit diagonal, lower triangle of packedData
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
                // U: upper triangle of packedData
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
            
            Matrix<E> L = createDenseMatrix(lData, m, minMN, a);
            Matrix<E> U = createDenseMatrix(uData, minMN, n, a);
            
            // Convert ipiv to permutation indices. 
            // LAPACK ipiv is a series of swaps.
            int[] pIndices = new int[m];
            for (int i = 0; i < m; i++) pIndices[i] = i;
            for (int i = 0; i < ipiv.length; i++) {
                int swapIdx = ipiv[i] - 1; // 1-indexed to 0-indexed
                if (swapIdx != i) {
                    int tmp = pIndices[i];
                    pIndices[i] = pIndices[swapIdx];
                    pIndices[swapIdx] = tmp;
                }
            }
            
            List<E> pList = new ArrayList<>(m);
            for (int idx : pIndices) {
                if (complex) pList.add((E)(Object)org.episteme.core.mathematics.numbers.complex.Complex.of(idx, 0));
                else pList.add((E)(Object)Real.of(idx));
            }
            Vector<E> P = Vector.of(pList, (Ring<E>)a.getScalarRing());
            
            return new LUResult<>(L, U, P);
        } catch (Throwable t) { throw new RuntimeException(t); }
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": Matrix scale() not available");
        int m = a.rows(), n = a.cols();
        if (isComplex(a)) {
            org.episteme.core.mathematics.numbers.complex.Complex sc = (org.episteme.core.mathematics.numbers.complex.Complex) (Object) scalar;
             try (Arena arena = Arena.ofConfined()) {
                MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toInterlacedDoubleArray(a));
                MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, sc.real(), sc.imaginary());
                ZSCAL.invokeExact(m * n, alpha, segX, 1);
                double[] resData = segX.toArray(ValueLayout.JAVA_DOUBLE);
                return createDenseMatrix(resData, m, n, a);
            } catch (Throwable t) { throw new RuntimeException("ZSCAL failed", t); }
        }
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segX = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
            try { DSCAL.invokeExact(m * n, ((Real)(Object)scalar).doubleValue(), segX, 1); } catch (Throwable e) { throw new RuntimeException(e); }
            double[] resData = segX.toArray(ValueLayout.JAVA_DOUBLE);
            return createDenseMatrix(resData, m, n, a);
        }
    }

    @Override
    public Vector<E> solve(LUResult<E> lu, Vector<E> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": solve(LUResult) not available");
        // For LU solve, we usually need the original IPiv and factorized matrix.
        // If we don't have them in the result object, we fallback to generic solve.
        return lu.solve(b);
    }
    
    @Override
    public Vector<E> solve(CholeskyResult<E> cholesky, Vector<E> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": solve(CholeskyResult) not available");
        int n = b.dimension();
        boolean complex = isComplex(cholesky.L());

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segL = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, complex ? toInterlacedDoubleArray(cholesky.L()) : toDoubleArray(cholesky.L()));
            MemorySegment segB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, complex ? toInterlacedDoubleArray(b) : toDoubleArray(b));
            
            int info;
            if (complex) {
                if (ZPOTRS == null) throw new UnsupportedOperationException("ZPOTRS not available");
                info = (int) ZPOTRS.invokeExact(LAPACK_ROW_MAJOR, (byte) 'L', n, 1, segL, n, segB, 1);
            } else {
                if (DPOTRS == null) throw new UnsupportedOperationException("DPOTRS not available");
                info = (int) DPOTRS.invokeExact(LAPACK_ROW_MAJOR, (byte) 'L', n, 1, segL, n, segB, 1);
            }
            if (info != 0) throw new ArithmeticException("POTRS failed: " + info);
            
            double[] resultArr = segB.toArray(ValueLayout.JAVA_DOUBLE);
            return createDenseVector(resultArr, n, cholesky.L());
        } catch (Throwable t) { throw new RuntimeException(t); }
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
            return new DenseMatrix<>((E[][]) arr, ring);
        } else {
            Real[][] arr = new Real[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    arr[i][j] = Real.of(data[i * cols + j]);
                }
            }
            return new DenseMatrix<>((E[][]) arr, ring);
        }
    }
}
