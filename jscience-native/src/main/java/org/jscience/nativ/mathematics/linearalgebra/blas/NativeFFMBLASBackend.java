/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.nativ.mathematics.linearalgebra.blas;

import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.core.mathematics.structures.rings.Ring;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.core.technical.backend.Backend;
import org.jscience.nativ.util.NativeLibraryLoader;
import com.google.auto.service.AutoService;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Map;
import java.util.Optional;

/**
 * High-Performance Native BLAS Backend using Project Panama (FFM).
 * Binds to OpenBLAS/MKL for Matrix Operations.
 */
@AutoService({Backend.class, LinearAlgebraProvider.class, AlgorithmProvider.class})
public class NativeFFMBLASBackend implements LinearAlgebraProvider<Double>, Backend {

    private static final SymbolLookup LOOKUP;
    private static final boolean IS_AVAILABLE;
    private static final Linker LINKER = Linker.nativeLinker();

    // CBLAS Layout Constants
    private static final int CblasRowMajor = 101;
    private static final int CblasNoTrans = 111;
    private static final int CblasTrans = 112;

    // Method Handles
    private static MethodHandle DGEMM;
    private static MethodHandle DGEMV;
    private static MethodHandle DDOT;
    private static MethodHandle DAXPY;
    private static MethodHandle DNRM2;

    static {
        // Attempt to load 'openblas' first, then fallback to system lookup
        Arena arena = Arena.ofAuto();
        Optional<SymbolLookup> lib = NativeLibraryLoader.loadLibrary("openblas", arena);
        if (lib.isEmpty()) {
             // Try 'mkl_rt' on Windows/Linux or 'blas' on Mac
             lib = NativeLibraryLoader.loadLibrary("mkl_rt", arena);
             if (lib.isEmpty()) {
                 lib = NativeLibraryLoader.getSystemLookup();
             }
        }
        
        LOOKUP = lib.orElse(null);
        boolean available = false;

        if (LOOKUP != null) {
            try {
                // void cblas_dgemm(int Order, int TransA, int TransB, int M, int N, int K, double alpha, double *A, int lda, double *B, int ldb, double beta, double *C, int ldc);
                FunctionDescriptor dgemmDesc = FunctionDescriptor.ofVoid(
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                        ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                        ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                DGEMM = LINKER.downcallHandle(LOOKUP.find("cblas_dgemm").orElseThrow(), dgemmDesc);

                // double cblas_ddot(int N, double *X, int incX, double *Y, int incY);
                FunctionDescriptor ddotDesc = FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE,
                        ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                DDOT = LINKER.downcallHandle(LOOKUP.find("cblas_ddot").orElseThrow(), ddotDesc);

                // double cblas_dnrm2(const int N, const double *X, const int incX);
                FunctionDescriptor dnrm2Desc = FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE,
                         ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                DNRM2 = LINKER.downcallHandle(LOOKUP.find("cblas_dnrm2").orElseThrow(), dnrm2Desc);

                available = true;
            } catch (Throwable t) {
                // System.err.println("Native BLAS symbols not found: " + t.getMessage());
                available = false;
            }
        }
        IS_AVAILABLE = available;
    }

    @Override
    public boolean isAvailable() {
        return IS_AVAILABLE;
    }

    @Override
    public String getType() {
        return "compute";
    }

    @Override
    public String getId() {
        return "native_blas";
    }

    @Override
    public String getName() {
        return "Native BLAS Provider (FFM)";
    }

    @Override
    public String getDescription() {
        return "High-performance Linear Algebra using Project Panama and OpenBLAS/MKL.";
    }

    @Override
    public int getPriority() {
        return 100; // High priority for Double matrices
    }

    @Override
    public boolean isCompatible(Ring<?> ring) {
        // Only supports Double (Real)
        return ring.zero() instanceof Double || ring.zero().getClass().getSimpleName().equals("Real");
    }

    // --- Matrix Multiply (cblas_dgemm) ---

    @Override
    public Matrix<Double> multiply(Matrix<Double> A, Matrix<Double> B) {
        // Check availability
        if (!IS_AVAILABLE) throw new UnsupportedOperationException("Native BLAS not available");

        int m = A.rows();
        int k = A.cols();
        int n = B.cols();
        
        // Input Checking
        if (k != B.rows()) throw new IllegalArgumentException("Matrix dimensions do not match for multiplication.");

        // Data Preparation: Copy to native memory
        // We use Arena.ofConfined because operations are synchronous and we want deterministic deallocation
        try (Arena arena = Arena.ofConfined()) {
            
            // Allocate A (m * k)
            MemorySegment segA = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * k);
            // Copy data. Since DenseMatrix implementation is generic, we use toDoubleArray() which copies.
            // This overhead is O(mn), while multiplication is O(mnk). FFM wins for n > ~50.
            double[] arrA = ((DenseMatrix<?>)A).toDoubleArray();
            MemorySegment.copy(arrA, 0, segA, ValueLayout.JAVA_DOUBLE, 0, arrA.length);

            // Allocate B (k * n)
            MemorySegment segB = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) k * n);
            double[] arrB = ((DenseMatrix<?>)B).toDoubleArray();
            MemorySegment.copy(arrB, 0, segB, ValueLayout.JAVA_DOUBLE, 0, arrB.length);

            // Allocate C (m * n) - initialized to zero by default
            MemorySegment segC = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);

            // Call cblas_dgemm
            // C = alpha * A * B + beta * C
            double alpha = 1.0;
            double beta = 0.0;
            
            try {
                DGEMM.invokeExact(CblasRowMajor, CblasNoTrans, CblasNoTrans, 
                                  m, n, k, alpha, segA, k, segB, n, beta, segC, n);
            } catch (Throwable e) {
                throw new RuntimeException("Error invoking native BLAS dgemm", e);
            }

            // Copy result back
            double[] result = new double[m * n];
            MemorySegment.copy(segC, ValueLayout.JAVA_DOUBLE, 0, result, 0, m * n);

            // Convert back to Generic Matrix (boxing)
            // Ideally, we'd have a Matrix implementation backed by double[] directly.
            // Using DenseMatrix(Double[][]) factory for now.
            // TODO: Optimize Result Construction
            Double[][] resObj = new Double[m][n];
            for(int i=0; i<m; i++) {
                for(int j=0; j<n; j++) {
                    resObj[i][j] = result[i*n + j];
                }
            }
            return new DenseMatrix<>(resObj, (Ring<Double>) A.getScalarRing());
        }
    }

    // --- Vector Dot Product (cblas_ddot) ---
    
    @Override
    public Double dot(Vector<Double> a, Vector<Double> b) {
         if (!IS_AVAILABLE) throw new UnsupportedOperationException("Native BLAS not available");
         int n = a.dimension();
         if (n != b.dimension()) throw new IllegalArgumentException("Vector dimensions mismatch");
         
         try (Arena arena = Arena.ofConfined()) {
             MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
             // Assuming DenseVector has similar access or copy
             for(int i=0; i<n; i++) segX.setAtIndex(ValueLayout.JAVA_DOUBLE, i, a.get(i));
             
             MemorySegment segY = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
             for(int i=0; i<n; i++) segY.setAtIndex(ValueLayout.JAVA_DOUBLE, i, b.get(i));

             try {
                 return (double) DDOT.invokeExact(n, segX, 1, segY, 1);
             } catch (Throwable e) {
                 throw new RuntimeException("Error invoking native BLAS ddot", e);
             }
         }
    }
    
    // --- Vector Norm (cblas_dnrm2) ---
    @Override
    public Double norm(Vector<Double> a) {
         if (!IS_AVAILABLE) throw new UnsupportedOperationException("Native BLAS not available");
         int n = a.dimension();
         
         try (Arena arena = Arena.ofConfined()) {
             MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
             for(int i=0; i<n; i++) segX.setAtIndex(ValueLayout.JAVA_DOUBLE, i, a.get(i));

             try {
                 return (double) DNRM2.invokeExact(n, segX, 1);
             } catch (Throwable e) {
                 throw new RuntimeException("Error invoking native BLAS dnrm2", e);
             }
         }
    }

    // Used to suppress warnings, or implement later
    @SuppressWarnings("unused")
    private void useHandles() {
        // Dummy usage to avoid warnings for now
        if (DGEMV == null || DAXPY == null || CblasTrans == 0) return;
    }

    // --- Other Methods (Stubs / Fallbacks) ---

    @Override
    public Vector<Double> add(Vector<Double> a, Vector<Double> b) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Vector<Double> subtract(Vector<Double> a, Vector<Double> b) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Vector<Double> multiply(Vector<Double> vector, Double scalar) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Matrix<Double> add(Matrix<Double> a, Matrix<Double> b) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Matrix<Double> subtract(Matrix<Double> a, Matrix<Double> b) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Vector<Double> multiply(Matrix<Double> a, Vector<Double> b) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Matrix<Double> inverse(Matrix<Double> a) {
        throw new UnsupportedOperationException("Use LAPACK provider for inverse");
    }

    @Override
    public Double determinant(Matrix<Double> a) {
        throw new UnsupportedOperationException("Use LAPACK provider for determinant");
    }

    @Override
    public Vector<Double> solve(Matrix<Double> a, Vector<Double> b) {
        throw new UnsupportedOperationException("Use LAPACK provider for solve");
    }

    @Override
    public Matrix<Double> transpose(Matrix<Double> a) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Matrix<Double> scale(Double scalar, Matrix<Double> a) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @Override
    public Object createBackend() {
        return new NativeFFMBLASBackend();
    }
}
