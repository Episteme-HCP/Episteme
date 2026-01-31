/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.technical.backend.math;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.nio.DoubleBuffer;
import org.jscience.core.technical.backend.math.MatrixBackend;
import org.jscience.nativ.mathematics.linearalgebra.matrices.NativeMatrix;

/**
 * Implementation of {@link MatrixBackend} using OpenBLAS or Intel MKL via Panama.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public class PanamaBlasBackend implements MatrixBackend {

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
    
    private static final boolean AVAILABLE;

    public static final int CblasRowMajor = 101;
    public static final int CblasColMajor = 102;
    public static final int CblasNoTrans = 111;
    public static final int CblasTrans = 112;
    public static final int CblasConjTrans = 113;

    static {
        Linker linker = Linker.nativeLinker();
        SymbolLookup lookup = SymbolLookup.libraryLookup("libopenblas", java.lang.foreign.Arena.global());
        
        if (lookup.find("cblas_dgemm").isEmpty()) {
            lookup = SymbolLookup.libraryLookup("mkl_rt", java.lang.foreign.Arena.global());
        }

        MemorySegment symbol = lookup.find("cblas_dgemm").orElse(null);
        if (symbol != null) {
            DGEMM_HANDLE = linker.downcallHandle(symbol, FunctionDescriptor.ofVoid(
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT
            ));
            
            MemorySegment mvSymbol = lookup.find("cblas_dgemv").orElse(null);
            if (mvSymbol != null) {
                DGEMV_HANDLE = linker.downcallHandle(mvSymbol, FunctionDescriptor.ofVoid(
                    ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                    ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS,
                    ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
                    ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS, ValueLayout.JAVA_INT
                ));
            } else {
                DGEMV_HANDLE = null;
            }
            
            // Level 1
            DDOT_HANDLE = lookup.find("cblas_ddot").map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
            DNRM2_HANDLE = lookup.find("cblas_dnrm2").map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
            DAXPY_HANDLE = lookup.find("cblas_daxpy").map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
            DSCAL_HANDLE = lookup.find("cblas_dscal").map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);

            // LAPACKE
            DGESV_HANDLE = lookup.find("LAPACKE_dgesv").map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
            DGETRF_HANDLE = lookup.find("LAPACKE_dgetrf").map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);
            DGETRI_HANDLE = lookup.find("LAPACKE_dgetri").map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);

            AVAILABLE = true;
        } else {
            DGEMM_HANDLE = null;
            DGEMV_HANDLE = null;
            DDOT_HANDLE = DNRM2_HANDLE = DAXPY_HANDLE = DSCAL_HANDLE = null;
            DGESV_HANDLE = DGETRF_HANDLE = DGETRI_HANDLE = null;
            AVAILABLE = false;
        }
    }

    @Override
    public boolean isAvailable() {
        return AVAILABLE;
    }

    @Override
    public String getName() {
        return "Native BLAS Backend";
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public org.jscience.core.technical.backend.ExecutionContext createContext() {
        return new org.jscience.core.technical.backend.ExecutionContext() {
            @Override
            public <T> T execute(org.jscience.core.technical.backend.Operation<T> operation) {
                return operation.compute(this);
            }

            @Override
            public void close() {
                // No-op
            }
        };
    }

    @Override
    public void dgemm(int rowsA, int colsA, int colsB,
                     DoubleBuffer A, int lda,
                     DoubleBuffer B, int ldb,
                     DoubleBuffer C, int ldc,
                     double alpha, double beta) {
        if (!AVAILABLE) throw new UnsupportedOperationException("BLAS native library not found");

        try {
            // MemorySegment.ofBuffer(A) requires direct buffer or heap array with offset
            MemorySegment segA = MemorySegment.ofBuffer(A);
            MemorySegment segB = MemorySegment.ofBuffer(B);
            MemorySegment segC = MemorySegment.ofBuffer(C);

            // CblasRowMajor = 101, CblasNoTrans = 111
            DGEMM_HANDLE.invokeExact(CblasRowMajor, CblasNoTrans, CblasNoTrans, rowsA, colsB, colsA,
                alpha, segA, lda, segB, ldb, beta, segC, ldc);
        } catch (Throwable t) {
            throw new RuntimeException("CBLAS call failed", t);
        }
    }

    @Override
    public void dgemv(int rowsA, int colsA,
                     DoubleBuffer A, int lda,
                     DoubleBuffer x, int incx,
                     DoubleBuffer y, int incy,
                     double alpha, double beta) {
        if (!AVAILABLE || DGEMV_HANDLE == null) throw new UnsupportedOperationException("BLAS dgemv not available");

        try {
            MemorySegment segA = MemorySegment.ofBuffer(A);
            MemorySegment segX = MemorySegment.ofBuffer(x);
            MemorySegment segY = MemorySegment.ofBuffer(y);

            // CblasRowMajor = 101, CblasNoTrans = 111
            DGEMV_HANDLE.invokeExact(CblasRowMajor, CblasNoTrans, rowsA, colsA,
                alpha, segA, lda, segX, incx, beta, segY, incy);
        } catch (Throwable t) {
            throw new RuntimeException("CBLAS dgemv call failed", t);
        }
    }

    public double ddot(int n, DoubleBuffer x, int incx, DoubleBuffer y, int incy) {
        if (!AVAILABLE || DDOT_HANDLE == null) throw new UnsupportedOperationException("BLAS ddot not available");
        try {
            return (double) DDOT_HANDLE.invokeExact(n, MemorySegment.ofBuffer(x), incx, MemorySegment.ofBuffer(y), incy);
        } catch (Throwable t) {
            throw new RuntimeException("CBLAS ddot failed", t);
        }
    }

    public double dnrm2(int n, DoubleBuffer x, int incx) {
        if (!AVAILABLE || DNRM2_HANDLE == null) throw new UnsupportedOperationException("BLAS dnrm2 not available");
        try {
            return (double) DNRM2_HANDLE.invokeExact(n, MemorySegment.ofBuffer(x), incx);
        } catch (Throwable t) {
            throw new RuntimeException("CBLAS dnrm2 failed", t);
        }
    }

    public void daxpy(int n, double alpha, DoubleBuffer x, int incx, DoubleBuffer y, int incy) {
        if (!AVAILABLE || DAXPY_HANDLE == null) throw new UnsupportedOperationException("BLAS daxpy not available");
        try {
            DAXPY_HANDLE.invokeExact(n, alpha, MemorySegment.ofBuffer(x), incx, MemorySegment.ofBuffer(y), incy);
        } catch (Throwable t) {
            throw new RuntimeException("CBLAS daxpy failed", t);
        }
    }

    public void dscal(int n, double alpha, DoubleBuffer x, int incx) {
        if (!AVAILABLE || DSCAL_HANDLE == null) throw new UnsupportedOperationException("BLAS dscal not available");
        try {
            DSCAL_HANDLE.invokeExact(n, alpha, MemorySegment.ofBuffer(x), incx);
        } catch (Throwable t) {
            throw new RuntimeException("CBLAS dscal failed", t);
        }
    }

    // LAPACK wrappers
    public int dgesv(int n, int nrhs, DoubleBuffer A, int lda, java.nio.IntBuffer ipiv, DoubleBuffer B, int ldb) {
        if (!AVAILABLE || DGESV_HANDLE == null) throw new UnsupportedOperationException("LAPACK dgesv not available");
        try {
            return (int) DGESV_HANDLE.invokeExact(CblasRowMajor, n, nrhs, MemorySegment.ofBuffer(A), lda, MemorySegment.ofBuffer(ipiv), MemorySegment.ofBuffer(B), ldb);
        } catch (Throwable t) {
            throw new RuntimeException("LAPACK dgesv failed", t);
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
    
    /** Optimized version for NativeMatrix. */
    public void multiply(NativeMatrix A, NativeMatrix B, NativeMatrix C, double alpha, double beta) {
        if (!AVAILABLE) throw new UnsupportedOperationException("BLAS native library not found");
        try {
            DGEMM_HANDLE.invokeExact(CblasRowMajor, CblasNoTrans, CblasNoTrans, A.rows(), B.cols(), A.cols(),
                alpha, A.segment(), A.cols(), B.segment(), B.cols(), beta, C.segment(), C.cols());
        } catch (Throwable t) {
            throw new RuntimeException("CBLAS call failed", t);
        }
    }
}






