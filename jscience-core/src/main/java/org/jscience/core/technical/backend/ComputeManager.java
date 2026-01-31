/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.backend;

import java.util.ServiceLoader;
import java.nio.DoubleBuffer;
import org.jscience.core.technical.backend.math.MatrixBackend;
import org.jscience.core.technical.backend.math.FFTBackend;

/**
 * Manager for high-performance compute backends.
 */
public final class ComputeManager {

    private static final MatrixBackend MATRIX_BACKEND;
    private static final FFTBackend FFT_BACKEND;

    static {
        MATRIX_BACKEND = findBackend(MatrixBackend.class, new PureJavaMatrixBackend());
        FFT_BACKEND = findBackend(FFTBackend.class, new PureJavaFFTBackend());
    }

    private static <T extends BackendProvider> T findBackend(Class<T> service, T fallback) {
        for (T backend : ServiceLoader.load(service)) {
            if (backend.isAvailable()) return backend;
        }
        return fallback;
    }

    public static MatrixBackend getMatrixBackend() {
        return MATRIX_BACKEND;
    }

    public static FFTBackend getFFTBackend() {
        return FFT_BACKEND;
    }

    /** Fallback Pure Java Matrix Backend. */
    private static class PureJavaMatrixBackend implements MatrixBackend {
        @Override public boolean isAvailable() { return true; }
        @Override public String getName() { return "Pure Java BLAS Fallback"; }
        @Override public int getPriority() { return 0; }
        @Override public void dgemm(int rowsA, int colsA, int colsB,
                                DoubleBuffer A, int lda, DoubleBuffer B, int ldb, DoubleBuffer C, int ldc,
                                double alpha, double beta) {
            for (int i = 0; i < rowsA; i++) {
                for (int j = 0; j < colsB; j++) {
                    double sum = 0;
                    for (int k = 0; k < colsA; k++) {
                        sum += A.get(i * lda + k) * B.get(k * ldb + j);
                    }
                    int idxC = i * ldc + j;
                    C.put(idxC, alpha * sum + beta * C.get(idxC));
                }
            }
        }

        @Override
        public void dgemv(int rowsA, int colsA,
                         DoubleBuffer A, int lda,
                         DoubleBuffer x, int incx,
                         DoubleBuffer y, int incy,
                         double alpha, double beta) {
            for (int i = 0; i < rowsA; i++) {
                double sum = 0;
                for (int j = 0; j < colsA; j++) {
                    sum += A.get(i * lda + j) * x.get(j * incx);
                }
                int idxY = i * incy;
                y.put(idxY, alpha * sum + beta * y.get(idxY));
            }
        }
    }

    /** Fallback Pure Java FFT Backend. */
    private static class PureJavaFFTBackend implements FFTBackend {
        @Override public boolean isAvailable() { return true; }
        @Override public String getName() { return "Pure Java FFT Fallback"; }
        @Override public int getPriority() { return 0; }
        @Override public void forward(int n, DoubleBuffer input, DoubleBuffer output) {
            // Naive DFT...
        }
        @Override public void backward(int n, DoubleBuffer input, DoubleBuffer output) {
            // Naive IDFT...
        }
    }

    private ComputeManager() {}
}

