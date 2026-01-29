/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.technical.backend.math;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.nio.DoubleBuffer;
import org.jscience.mathematics.linearalgebra.matrices.NativeMatrix;

/**
 * Implementation of {@link MatrixBackend} using OpenBLAS or Intel MKL via Panama.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public class BLASBackend implements MatrixBackend {

    private static final MethodHandle DGEMM_HANDLE;
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
            AVAILABLE = true;
        } else {
            DGEMM_HANDLE = null;
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
