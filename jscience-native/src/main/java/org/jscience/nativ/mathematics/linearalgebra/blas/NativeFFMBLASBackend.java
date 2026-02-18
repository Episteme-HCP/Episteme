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
import org.jscience.core.technical.algorithm.OperationContext;
import com.google.auto.service.AutoService;
import org.jscience.core.mathematics.linearalgebra.vectors.DenseVector;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

/**
 * High-Performance Native BLAS Backend using Project Panama (FFM).
 * Binds to OpenBLAS/MKL for Matrix Operations.
 */
@SuppressWarnings("rawtypes") // LinearAlgebraProvider.class is a raw type reference (unavoidable in @AutoService)
@AutoService({Backend.class, LinearAlgebraProvider.class, AlgorithmProvider.class})
public class NativeFFMBLASBackend implements LinearAlgebraProvider<Double>, Backend {

    private static final SymbolLookup LOOKUP;
    private static final boolean IS_AVAILABLE;
    private static final Linker LINKER = Linker.nativeLinker();

    // CBLAS Layout Constants
    private static final int CblasRowMajor = 101;
    private static final int CblasNoTrans = 111;
    private static final int CblasTrans = 112;

    // BLAS Method Handles
    private static MethodHandle DGEMM;
    private static MethodHandle DGEMV;
    private static MethodHandle DDOT;
    private static MethodHandle DAXPY;
    private static MethodHandle DNRM2;
    private static MethodHandle DSCAL;
    
    // LAPACK Method Handles
    private static MethodHandle DGESV;
    private static MethodHandle DGETRF;
    private static MethodHandle DGETRI;
    
    // LAPACK Constants
    private static final int LAPACK_ROW_MAJOR = 101;

    static {
        // ... (Existing BLAS lookup code) ...
        Arena arena = Arena.ofAuto();
        Optional<SymbolLookup> lib = NativeLibraryLoader.loadLibrary("openblas", arena);
        if (lib.isEmpty()) {
             lib = NativeLibraryLoader.loadLibrary("mkl_rt", arena);
             if (lib.isEmpty()) {
                 lib = NativeLibraryLoader.getSystemLookup();
             }
        }
        
        LOOKUP = lib.orElse(null);
        boolean available = false;

        if (LOOKUP != null) {
            try {
                // BLAS Handles (Existing)
                FunctionDescriptor dgemmDesc = FunctionDescriptor.ofVoid(
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                        ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                        ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                DGEMM = LINKER.downcallHandle(LOOKUP.find("cblas_dgemm").orElseThrow(), dgemmDesc);

                FunctionDescriptor ddotDesc = FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE,
                        ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                DDOT = LINKER.downcallHandle(LOOKUP.find("cblas_ddot").orElseThrow(), ddotDesc);

                FunctionDescriptor dnrm2Desc = FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE,
                        ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                DNRM2 = LINKER.downcallHandle(LOOKUP.find("cblas_dnrm2").orElseThrow(), dnrm2Desc);

                // DAXPY: void cblas_daxpy(int n, double alpha, const double *x, int incx, double *y, int incy)
                FunctionDescriptor daxpyDesc = FunctionDescriptor.ofVoid(
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                DAXPY = LINKER.downcallHandle(LOOKUP.find("cblas_daxpy").orElseThrow(), daxpyDesc);
                
                // DSCAL: void cblas_dscal(int n, double alpha, double *x, int incx)
                FunctionDescriptor dscalDesc = FunctionDescriptor.ofVoid(
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                DSCAL = LINKER.downcallHandle(LOOKUP.find("cblas_dscal").orElseThrow(), dscalDesc);

                // DGEMV: void cblas_dgemv(int Layout, int TransA, int M, int N, double alpha, const double *A, int lda, const double *X, int incX, double beta, double *Y, int incY)
                FunctionDescriptor dgemvDesc = FunctionDescriptor.ofVoid(
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                DGEMV = LINKER.downcallHandle(LOOKUP.find("cblas_dgemv").orElseThrow(), dgemvDesc);

                // LAPACK Handles
                // Try LAPACKE (C Interface) first for Row-Major support
                // lapack_int LAPACKE_dgesv(int matrix_layout, lapack_int n, lapack_int nrhs, double* a, lapack_int lda, lapack_int* ipiv, double* b, lapack_int ldb);
                Optional<MemorySegment> dgesvAddr = LOOKUP.find("LAPACKE_dgesv");
                if (dgesvAddr.isPresent()) {
                    FunctionDescriptor dgesvDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                            ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                            AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS,
                            AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                    );
                    DGESV = LINKER.downcallHandle(dgesvAddr.get(), dgesvDesc);
                }

                // lapack_int LAPACKE_dgetrf(int matrix_layout, lapack_int m, lapack_int n, double* a, lapack_int lda, lapack_int* ipiv);
                Optional<MemorySegment> dgetrfAddr = LOOKUP.find("LAPACKE_dgetrf");
                if (dgetrfAddr.isPresent()) {
                    FunctionDescriptor dgetrfDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                            ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                            AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS
                    );
                    DGETRF = LINKER.downcallHandle(dgetrfAddr.get(), dgetrfDesc);
                }
                
                // lapack_int LAPACKE_dgetri(int matrix_layout, lapack_int n, double* a, lapack_int lda, const lapack_int* ipiv);
                Optional<MemorySegment> dgetriAddr = LOOKUP.find("LAPACKE_dgetri");
                if (dgetriAddr.isPresent()) {
                    FunctionDescriptor dgetriDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                            ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                            AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS
                    );
                    DGETRI = LINKER.downcallHandle(dgetriAddr.get(), dgetriDesc);
                }

                available = true;
            } catch (Throwable t) {
                available = false;
            }
        }
        IS_AVAILABLE = available;
    }
    
    // ... (Existing implementation methods) ...



    // ... (Existing imports) ...

    @Override
    public Vector<Double> solve(Matrix<Double> A, Vector<Double> b) {
        if (!IS_AVAILABLE || DGESV == null) throw new UnsupportedOperationException("Native LAPACK dgesv not available");
        
        int n = A.rows();
        if (n != A.cols()) throw new IllegalArgumentException("Matrix must be square");
        if (n != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");
        
        try (Arena arena = Arena.ofConfined()) {
            // Allocate A (copy)
            MemorySegment segA = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
            double[] arrA = ((DenseMatrix<?>)A).toDoubleArray();
            MemorySegment.copy(arrA, 0, segA, ValueLayout.JAVA_DOUBLE, 0, arrA.length);
            
            // Allocate b (copy) - will become x
            MemorySegment segB = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
            for(int i=0; i<n; i++) segB.setAtIndex(ValueLayout.JAVA_DOUBLE, i, b.get(i));
            
            // Allocate IPIV (pivots)
            MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, n);
            
            // Call LAPACKE_dgesv
            int info = (int) DGESV.invokeExact(LAPACK_ROW_MAJOR, n, 1, segA, n, segIpiv, segB, 1);
            
            if (info != 0) throw new ArithmeticException("Linear solve failed (singular matrix). Info: " + info);
            
            // Read result x from segB
            double[] result = new double[n];
            MemorySegment.copy(segB, ValueLayout.JAVA_DOUBLE, 0, result, 0, n);
            
            // Construct DenseVector result
            List<Double> list = new ArrayList<>(n);
            for(double v : result) list.add(v);
            
            return new DenseVector<>(list, (Ring<Double>) A.getScalarRing());
        } catch (Throwable e) {
             throw new RuntimeException(e);
        }
    }
    
    // Stub for now to allow partial update
    @Override
    public Matrix<Double> inverse(Matrix<Double> A) {
         if (!IS_AVAILABLE || DGETRF == null || DGETRI == null) throw new UnsupportedOperationException("Native LAPACK inverse not available");
         int n = A.rows();
         if (n != A.cols()) throw new IllegalArgumentException("Matrix must be square");

         try (Arena arena = Arena.ofConfined()) {
             MemorySegment segA = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
             double[] arrA = ((DenseMatrix<?>)A).toDoubleArray();
             MemorySegment.copy(arrA, 0, segA, ValueLayout.JAVA_DOUBLE, 0, arrA.length);
             
             MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, n);
             
             // LU Factorization
             int info = (int) DGETRF.invokeExact(LAPACK_ROW_MAJOR, n, n, segA, n, segIpiv);
             if (info != 0) throw new ArithmeticException("LU Factorization failed. Info: " + info);
             
             // Inverse
             info = (int) DGETRI.invokeExact(LAPACK_ROW_MAJOR, n, segA, n, segIpiv);
             if (info != 0) throw new ArithmeticException("Inverse failed. Info: " + info);
             
             double[] result = new double[n * n];
             MemorySegment.copy(segA, ValueLayout.JAVA_DOUBLE, 0, result, 0, n * n);
             
             Double[][] resObj = new Double[n][n];
             for(int i=0; i<n; i++) {
                 for(int j=0; j<n; j++) {
                     resObj[i][j] = result[i*n + j];
                 }
             }
             return new DenseMatrix<>(resObj, (Ring<Double>) A.getScalarRing());
         } catch (Throwable e) {
             throw new RuntimeException(e);
         }
    }

    @Override
    public Double determinant(Matrix<Double> A) {
         if (!IS_AVAILABLE || DGETRF == null) throw new UnsupportedOperationException("Native LAPACK determinant not available");
         int n = A.rows();
         if (n != A.cols()) throw new IllegalArgumentException("Matrix must be square");

         try (Arena arena = Arena.ofConfined()) {
             MemorySegment segA = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
             double[] arrA = ((DenseMatrix<?>)A).toDoubleArray();
             MemorySegment.copy(arrA, 0, segA, ValueLayout.JAVA_DOUBLE, 0, arrA.length);
             
             MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, n);
             
             // LU Factorization
             int info = (int) DGETRF.invokeExact(LAPACK_ROW_MAJOR, n, n, segA, n, segIpiv);
             if (info > 0) return 0.0; // Singular
             
             // Det = Product(diag(U)) * (-1)^swaps
             // U is in segA (upper triangle). L is in lower (unit diagonal).
             // Pivots in segIpiv.
             
             double det = 1.0;
             for(int i=0; i<n; i++) {
                 det *= segA.getAtIndex(ValueLayout.JAVA_DOUBLE, i * n + i);
                 int pivot = segIpiv.getAtIndex(ValueLayout.JAVA_INT, i);
                 if (pivot != i + 1) det = -det; // Swap changes sign (LAPACK pivots are 1-based)
             }
             return det;
         } catch (Throwable e) {
             throw new RuntimeException(e);
         }
    }
    
    // ... (Rest of existing methods) ...

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

    @Override
    public double score(OperationContext context) {
        if (!IS_AVAILABLE) return -1.0;

        double score = getPriority();

        // Penalty if GPU operations are preferred
        if (context.hasHint(OperationContext.Hint.GPU_RESIDENT)) {
            score -= 50.0;
        }

        // Small data penalty (FFM overhead)
        if (context.getDataSize() > 0 && context.getDataSize() < 256) {
           score -= 20.0;
        }

        // Double precision bonus (since this is a double provider)
        if (!context.hasHint(OperationContext.Hint.FLOAT32_OK)) {
            score += 10.0;
        }

        return score;
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

            // NOTE: Result construction boxes double[] → Double[][] for DenseMatrix compatibility.
            // A native-backed Matrix implementation (backed by MemorySegment directly) would
            // eliminate this copy. This is acceptable for now as the BLAS multiply itself
            // dominates execution time for large matrices.
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
        if (DGEMV == null || DAXPY == null || DSCAL == null || CblasTrans == 0) return;
    }

    // --- Other Methods (Stubs / Fallbacks) ---

    @Override
    public Vector<Double> add(Vector<Double> a, Vector<Double> b) {
        int n = a.dimension();
        if (n != b.dimension()) throw new IllegalArgumentException("Vector dimensions mismatch");
        // result = a + b  via DAXPY: y = alpha*x + y (alpha=1, x=a, y=copy-of-b)
        if (IS_AVAILABLE) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
                MemorySegment segY = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
                for (int i = 0; i < n; i++) {
                    segX.setAtIndex(ValueLayout.JAVA_DOUBLE, i, a.get(i));
                    segY.setAtIndex(ValueLayout.JAVA_DOUBLE, i, b.get(i));
                }
                try { DAXPY.invokeExact(n, 1.0, segX, 1, segY, 1); } catch (Throwable e) { throw new RuntimeException(e); }
                Double[] result = new Double[n];
                for (int i = 0; i < n; i++) result[i] = segY.getAtIndex(ValueLayout.JAVA_DOUBLE, i);
                return DenseVector.of(java.util.Arrays.asList(result), getRing(a));
            }
        }
        // Java fallback
        Double[] result = new Double[n];
        for (int i = 0; i < n; i++) result[i] = a.get(i) + b.get(i);
        return DenseVector.of(java.util.Arrays.asList(result), getRing(a));
    }

    @Override
    public Vector<Double> subtract(Vector<Double> a, Vector<Double> b) {
        int n = a.dimension();
        if (n != b.dimension()) throw new IllegalArgumentException("Vector dimensions mismatch");
        // result = a - b via DAXPY: y = -1*x + y where x=b, y=copy-of-a
        if (IS_AVAILABLE) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
                MemorySegment segY = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
                for (int i = 0; i < n; i++) {
                    segX.setAtIndex(ValueLayout.JAVA_DOUBLE, i, b.get(i));
                    segY.setAtIndex(ValueLayout.JAVA_DOUBLE, i, a.get(i));
                }
                try { DAXPY.invokeExact(n, -1.0, segX, 1, segY, 1); } catch (Throwable e) { throw new RuntimeException(e); }
                Double[] result = new Double[n];
                for (int i = 0; i < n; i++) result[i] = segY.getAtIndex(ValueLayout.JAVA_DOUBLE, i);
                return DenseVector.of(java.util.Arrays.asList(result), getRing(a));
            }
        }
        Double[] result = new Double[n];
        for (int i = 0; i < n; i++) result[i] = a.get(i) - b.get(i);
        return DenseVector.of(java.util.Arrays.asList(result), getRing(a));
    }

    @Override
    public Vector<Double> multiply(Vector<Double> vector, Double scalar) {
        int n = vector.dimension();
        if (IS_AVAILABLE) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
                for (int i = 0; i < n; i++) segX.setAtIndex(ValueLayout.JAVA_DOUBLE, i, vector.get(i));
                
                try {
                    // DSCAL(n, alpha, x, incx)
                    DSCAL.invokeExact(n, scalar.doubleValue(), segX, 1);
                } catch (Throwable e) { throw new RuntimeException(e); }
                
                Double[] result = new Double[n];
                for (int i = 0; i < n; i++) result[i] = segX.getAtIndex(ValueLayout.JAVA_DOUBLE, i);
                return DenseVector.of(java.util.Arrays.asList(result), getRing(vector));
            }
        }
        Double[] result = new Double[n];
        for (int i = 0; i < n; i++) result[i] = vector.get(i) * scalar;
        return DenseVector.of(java.util.Arrays.asList(result), getRing(vector));
    }

    @Override
    public Matrix<Double> add(Matrix<Double> a, Matrix<Double> b) {
        int m = a.rows(), n = a.cols();
        if (m != b.rows() || n != b.cols()) throw new IllegalArgumentException("Matrix dimensions mismatch");
        
        if (IS_AVAILABLE) {
             try (Arena arena = Arena.ofConfined()) {
                 int len = m * n;
                 MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, len);
                 MemorySegment segY = arena.allocate(ValueLayout.JAVA_DOUBLE, len);
                 
                 double[] arrA = ((DenseMatrix<?>)a).toDoubleArray(); // x
                 double[] arrB = ((DenseMatrix<?>)b).toDoubleArray(); // y
                 
                 MemorySegment.copy(arrA, 0, segX, ValueLayout.JAVA_DOUBLE, 0, len);
                 MemorySegment.copy(arrB, 0, segY, ValueLayout.JAVA_DOUBLE, 0, len);
                 
                 // Y = 1.0 * X + Y => B + A
                 try { DAXPY.invokeExact(len, 1.0, segX, 1, segY, 1); } catch (Throwable e) { throw new RuntimeException(e); }
                 
                 double[] result = new double[len];
                 MemorySegment.copy(segY, ValueLayout.JAVA_DOUBLE, 0, result, 0, len);
                 return createDenseMatrix(result, m, n, a);
             }
        }
        
        Double[][] result = new Double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                result[i][j] = a.get(i, j) + b.get(i, j);
        return new DenseMatrix<>(result, (Ring<Double>) a.getScalarRing());
    }

    @Override
    public Matrix<Double> subtract(Matrix<Double> a, Matrix<Double> b) {
        int m = a.rows(), n = a.cols();
        if (m != b.rows() || n != b.cols()) throw new IllegalArgumentException("Matrix dimensions mismatch");

        if (IS_AVAILABLE) {
             try (Arena arena = Arena.ofConfined()) {
                 int len = m * n;
                 MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, len);
                 MemorySegment segY = arena.allocate(ValueLayout.JAVA_DOUBLE, len);
                 
                 double[] arrA = ((DenseMatrix<?>)a).toDoubleArray(); // y (initial)
                 double[] arrB = ((DenseMatrix<?>)b).toDoubleArray(); // x
                 
                 // Y = -1.0 * X + Y => -B + A
                 MemorySegment.copy(arrB, 0, segX, ValueLayout.JAVA_DOUBLE, 0, len);
                 MemorySegment.copy(arrA, 0, segY, ValueLayout.JAVA_DOUBLE, 0, len);
                 
                 try { DAXPY.invokeExact(len, -1.0, segX, 1, segY, 1); } catch (Throwable e) { throw new RuntimeException(e); }
                 
                 double[] result = new double[len];
                 MemorySegment.copy(segY, ValueLayout.JAVA_DOUBLE, 0, result, 0, len);
                 return createDenseMatrix(result, m, n, a);
             }
        }

        Double[][] result = new Double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                result[i][j] = a.get(i, j) - b.get(i, j);
        return new DenseMatrix<>(result, (Ring<Double>) a.getScalarRing());
    }
    
    private Matrix<Double> createDenseMatrix(double[] data, int rows, int cols, Matrix<Double> proto) {
         Double[][] resObj = new Double[rows][cols];
         for(int i=0; i<rows; i++) {
             for(int j=0; j<cols; j++) {
                 resObj[i][j] = data[i*cols + j];
             }
         }
         return new DenseMatrix<>(resObj, (Ring<Double>) proto.getScalarRing());
    }

    @Override
    public Vector<Double> multiply(Matrix<Double> a, Vector<Double> b) {
        int m = a.rows(), k = a.cols();
        if (k != b.dimension()) throw new IllegalArgumentException("Matrix cols must equal vector dimension");
        // Use DGEMV if available: y = alpha*A*x + beta*y
        if (IS_AVAILABLE) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment segA = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * k);
                for (int i = 0; i < m; i++)
                    for (int j = 0; j < k; j++)
                        segA.setAtIndex(ValueLayout.JAVA_DOUBLE, (long) i * k + j, a.get(i, j));

                MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, k);
                for (int i = 0; i < k; i++) segX.setAtIndex(ValueLayout.JAVA_DOUBLE, i, b.get(i));

                MemorySegment segY = arena.allocate(ValueLayout.JAVA_DOUBLE, m);

                try {
                    DGEMV.invokeExact(CblasRowMajor, CblasNoTrans, m, k, 1.0, segA, k, segX, 1, 0.0, segY, 1);
                } catch (Throwable e) { throw new RuntimeException("DGEMV error", e); }

                Double[] result = new Double[m];
                for (int i = 0; i < m; i++) result[i] = segY.getAtIndex(ValueLayout.JAVA_DOUBLE, i);
                return DenseVector.of(java.util.Arrays.asList(result), getRing(b));
            }
        }
        // Java fallback
        Double[] result = new Double[m];
        for (int i = 0; i < m; i++) {
            double sum = 0;
            for (int j = 0; j < k; j++) sum += a.get(i, j) * b.get(j);
            result[i] = sum;
        }
        return DenseVector.of(java.util.Arrays.asList(result), getRing(b));
    }

    @Override
    public Matrix<Double> transpose(Matrix<Double> a) {
        int m = a.rows(), n = a.cols();
        Double[][] result = new Double[n][m];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                result[j][i] = a.get(i, j);
        return new DenseMatrix<>(result, (Ring<Double>) a.getScalarRing());
    }

    @Override
    public Matrix<Double> scale(Double scalar, Matrix<Double> a) {
        int m = a.rows(), n = a.cols();
        
        if (IS_AVAILABLE) {
             try (Arena arena = Arena.ofConfined()) {
                 int len = m * n;
                 MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, len);
                 double[] arrA = ((DenseMatrix<?>)a).toDoubleArray();
                 MemorySegment.copy(arrA, 0, segX, ValueLayout.JAVA_DOUBLE, 0, len);
                 
                 // DSCAL(n, alpha, x, incx)
                 try { DSCAL.invokeExact(len, scalar.doubleValue(), segX, 1); } catch (Throwable e) { throw new RuntimeException(e); }
                 
                 double[] result = new double[len];
                 MemorySegment.copy(segX, ValueLayout.JAVA_DOUBLE, 0, result, 0, len);
                 return createDenseMatrix(result, m, n, a);
             }
        }
        
        Double[][] result = new Double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                result[i][j] = scalar * a.get(i, j);
        return new DenseMatrix<>(result, (Ring<Double>) a.getScalarRing());
    }

    @SuppressWarnings("unchecked")
    private Ring<Double> getRing(Vector<Double> v) {
        if (v instanceof org.jscience.core.mathematics.linearalgebra.vectors.GenericVector) {
            return ((org.jscience.core.mathematics.linearalgebra.vectors.GenericVector<Double>) v).getScalarRing();
        }
        return DOUBLE_RING;
    }

    // Simple Ring<Double> for fallback construction
    @SuppressWarnings("unchecked")
    private static final Ring<Double> DOUBLE_RING = new Ring<Double>() {
        @Override public Double operate(Double a, Double b) { return a + b; }
        @Override public Double add(Double a, Double b) { return a + b; }
        @Override public Double zero() { return 0.0; }
        @Override public Double inverse(Double e) { return -e; }
        @Override public Double negate(Double e) { return -e; }
        @Override public Double multiply(Double a, Double b) { return a * b; }
        @Override public Double one() { return 1.0; }
        @Override public boolean isMultiplicationCommutative() { return true; }
        @Override public boolean contains(Double element) { return element != null; }
        @Override public boolean isEmpty() { return false; }
        @Override public String description() { return "ℝ (Double precision reals)"; }
    };
    
    @Override
    public Object createBackend() {
        return new NativeFFMBLASBackend();
    }
}
