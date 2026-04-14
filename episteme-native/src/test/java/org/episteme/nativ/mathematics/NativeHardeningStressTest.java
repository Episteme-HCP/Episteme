/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics;

import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.nativ.mathematics.analysis.fft.backends.NativeFFTBackend;
import org.episteme.nativ.mathematics.analysis.fft.backends.NativeOpenCLFFTBackend;
import org.episteme.nativ.mathematics.linearalgebra.backends.NativeOpenCLSparseLinearAlgebraBackend;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Extreme stress test (1,000+ iterations) for Native Math Stack to detect memory leaks
 * in FFTW3, OpenCL FFT, and GMRES/Sparse solvers.
 */
public class NativeHardeningStressTest {

    private static final Logger logger = LoggerFactory.getLogger(NativeHardeningStressTest.class);
    private static final int ITERATIONS = 1000;
    private static final int DIM = 256;

    @Test
    public void testFFTW3Stress() {
        logger.info("Starting FFTW3 Stress Test ({} iterations)...", ITERATIONS);
        NativeFFTBackend backend = new NativeFFTBackend();
        if (!backend.isAvailable()) {
            logger.warn("FFTW3 not available, skipping stress test.");
            return;
        }

        double[] data = new double[DIM];
        double[] imag = new double[DIM];
        Random rnd = new Random();
        for (int i = 0; i < data.length; i++) {
            data[i] = rnd.nextDouble();
            imag[i] = 0.0;
        }

        for (int i = 0; i < ITERATIONS; i++) {
            if (i % 100 == 0) logger.info("FFTW3 Iteration {}...", i);
            double[][] result = backend.transform(data, imag);
            assertNotNull(result);
        }
        logger.info("FFTW3 Stress Test completed successfully.");
    }

    @Test
    public void testOpenCLFFTStress() {
        logger.info("Starting OpenCL FFT Stress Test ({} iterations)...", ITERATIONS);
        NativeOpenCLFFTBackend backend = new NativeOpenCLFFTBackend();
        if (!backend.isAvailable()) {
            logger.warn("OpenCL FFT not available, skipping stress test.");
            return;
        }

        double[] data = new double[DIM];
        double[] imag = new double[DIM];
        Random rnd = new Random();
        for (int i = 0; i < data.length; i++) {
            data[i] = rnd.nextDouble();
            imag[i] = 0.0;
        }

        for (int i = 0; i < ITERATIONS; i++) {
            if (i % 100 == 0) logger.info("OpenCL FFT Iteration {}...", i);
            double[][] result = backend.transform(data, imag);
            assertNotNull(result);
        }
        logger.info("OpenCL FFT Stress Test completed successfully.");
    }

    @Test
    public void testOpenCLGMRESStress() {
        logger.info("Starting OpenCL GMRES Stress Test ({} iterations)...", ITERATIONS);
        NativeOpenCLSparseLinearAlgebraBackend backend = new NativeOpenCLSparseLinearAlgebraBackend();
        if (!backend.isAvailable()) {
            logger.warn("OpenCL Sparse Backend not available, skipping stress test.");
            return;
        }

        // Create a simple diagonally dominant sparse matrix for stability
        int n = 100;
        int nnz = n + (n - 1) * 2;
        int[] rowPtr = new int[n + 1];
        int[] colIdx = new int[nnz];
        double[] values = new double[nnz];
        
        int ptr = 0;
        for (int i = 0; i < n; i++) {
            rowPtr[i] = ptr;
            if (i > 0) {
                colIdx[ptr] = i - 1;
                values[ptr] = -1.0;
                ptr++;
            }
            colIdx[ptr] = i;
            values[ptr] = 4.0;
            ptr++;
            if (i < n - 1) {
                colIdx[ptr] = i + 1;
                values[ptr] = -1.0;
                ptr++;
            }
        }
        rowPtr[n] = ptr;

        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<Real> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<>(n, n, Real.ZERO);
        for (int i = 0; i < n; i++) {
            for (int k = rowPtr[i]; k < rowPtr[i+1]; k++) {
                storage.set(i, colIdx[k], Real.of(values[k]));
            }
        }
        SparseMatrix<Real> A = new SparseMatrix<>(storage, Reals.getInstance());
        
        double[] bData = new double[n];
        for (int i = 0; i < n; i++) bData[i] = 1.0;
        Vector<Real> b = createVector(bData);

        for (int i = 0; i < ITERATIONS; i++) {
            if (i % 100 == 0) logger.info("GMRES Iteration {}...", i);
            Vector<Real> x = backend.gmres(A, b, null, Real.of(1e-10), 100, 30);
            assertNotNull(x);
        }
        logger.info("OpenCL GMRES Stress Test completed successfully.");
    }

    private Vector<Real> createVector(double[] data) {
        int n = data.length;
        org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<Real> storage = 
            new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(n);
        for (int i = 0; i < n; i++) storage.set(i, Real.of(data[i]));
        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<Real>(storage, null, Reals.getInstance());
    }
}
