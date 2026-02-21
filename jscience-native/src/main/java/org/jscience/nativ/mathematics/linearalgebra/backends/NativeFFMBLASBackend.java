/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.nativ.mathematics.linearalgebra.backends;

import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.core.mathematics.structures.rings.Ring;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.core.technical.backend.Backend;
import org.jscience.core.technical.backend.cpu.CPUBackend;
import org.jscience.nativ.technical.backend.nativ.NativeBackend;
import org.jscience.nativ.technical.backend.nativ.NativeLibraryLoader;
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
 * Implements {@link CPUBackend} and {@link NativeBackend}.
 */
@AutoService({Backend.class, LinearAlgebraProvider.class, AlgorithmProvider.class, CPUBackend.class, NativeBackend.class})
public class NativeFFMBLASBackend implements LinearAlgebraProvider<Double>, CPUBackend, NativeBackend {

    private static final SymbolLookup LOOKUP;
    private static final boolean IS_AVAILABLE;
    private static final Linker LINKER = NativeLibraryLoader.getLinker();

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
    private static MethodHandle DSCAL;
    
    // LAPACK Method Handles
    private static MethodHandle DGESV;
    private static MethodHandle DGETRF;
    private static MethodHandle DGETRI;
    
    private static final int LAPACK_ROW_MAJOR = 101;

    static {
        Arena arena = Arena.global();
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
                // BLAS Handles
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

                FunctionDescriptor daxpyDesc = FunctionDescriptor.ofVoid(
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                DAXPY = LINKER.downcallHandle(LOOKUP.find("cblas_daxpy").orElseThrow(), daxpyDesc);
                
                FunctionDescriptor dscalDesc = FunctionDescriptor.ofVoid(
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                DSCAL = LINKER.downcallHandle(LOOKUP.find("cblas_dscal").orElseThrow(), dscalDesc);

                FunctionDescriptor dgemvDesc = FunctionDescriptor.ofVoid(
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                DGEMV = LINKER.downcallHandle(LOOKUP.find("cblas_dgemv").orElseThrow(), dgemvDesc);

                // LAPACK
                Optional<MemorySegment> dgesvAddr = LOOKUP.find("LAPACKE_dgesv");
                if (dgesvAddr.isPresent()) {
                    FunctionDescriptor dgesvDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                            ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                            AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS,
                            AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                    );
                    DGESV = LINKER.downcallHandle(dgesvAddr.get(), dgesvDesc);
                }

                Optional<MemorySegment> dgetrfAddr = LOOKUP.find("LAPACKE_dgetrf");
                if (dgetrfAddr.isPresent()) {
                    FunctionDescriptor dgetrfDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                            ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                            AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS
                    );
                    DGETRF = LINKER.downcallHandle(dgetrfAddr.get(), dgetrfDesc);
                }
                
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

    @Override
    public boolean isLoaded() {
        return IS_AVAILABLE;
    }

    @Override
    public String getNativeLibraryName() {
        return "openblas";
    }

    @Override
    public Vector<Double> solve(Matrix<Double> A, Vector<Double> b) {
        if (!IS_AVAILABLE || DGESV == null) throw new UnsupportedOperationException("Native LAPACK dgesv not available");
        
        int n = A.rows();
        if (n != A.cols()) throw new IllegalArgumentException("Matrix must be square");
        if (n != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");
        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segA = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
            double[] arrA = ((DenseMatrix<?>)A).toDoubleArray();
            MemorySegment.copy(arrA, 0, segA, ValueLayout.JAVA_DOUBLE, 0, arrA.length);
            
            MemorySegment segB = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
            for(int i=0; i<n; i++) segB.setAtIndex(ValueLayout.JAVA_DOUBLE, i, b.get(i));
            
            MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, n);
            
            int info = (int) DGESV.invokeExact(LAPACK_ROW_MAJOR, n, 1, segA, n, segIpiv, segB, 1);
            if (info != 0) throw new ArithmeticException("Linear solve failed (singular matrix). Info: " + info);
            
            double[] result = new double[n];
            MemorySegment.copy(segB, ValueLayout.JAVA_DOUBLE, 0, result, 0, n);
            
            List<Double> list = new ArrayList<>(n);
            for(double v : result) list.add(v);
            return new DenseVector<>(list, (Ring<Double>) A.getScalarRing());
        } catch (Throwable e) {
             throw new RuntimeException(e);
        }
    }
    
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
             
             int info = (int) DGETRF.invokeExact(LAPACK_ROW_MAJOR, n, n, segA, n, segIpiv);
             if (info != 0) throw new ArithmeticException("LU Factorization failed. Info: " + info);
             
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
             
             int info = (int) DGETRF.invokeExact(LAPACK_ROW_MAJOR, n, n, segA, n, segIpiv);
             if (info > 0) return 0.0; // Singular
             
             double det = 1.0;
             for(int i=0; i<n; i++) {
                 det *= segA.getAtIndex(ValueLayout.JAVA_DOUBLE, i * n + i);
                 int pivot = segIpiv.getAtIndex(ValueLayout.JAVA_INT, i);
                 if (pivot != i + 1) det = -det;
             }
             return det;
         } catch (Throwable e) {
             throw new RuntimeException(e);
         }
    }
    
    @Override
    public boolean isAvailable() {
        return IS_AVAILABLE;
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
        return 100;
    }

    @Override
    public boolean isCompatible(Ring<?> ring) {
        return ring.zero() instanceof Double || ring.zero().getClass().getSimpleName().equals("Real");
    }

    @Override
    public double score(OperationContext context) {
        if (!IS_AVAILABLE) return -1.0;
        double score = getPriority();
        if (context.hasHint(OperationContext.Hint.GPU_RESIDENT)) score -= 50.0;
        if (context.getDataSize() > 0 && context.getDataSize() < 256) score -= 20.0;
        if (!context.hasHint(OperationContext.Hint.FLOAT32_OK)) score += 10.0;
        return score;
    }

    @Override
    public Matrix<Double> multiply(Matrix<Double> A, Matrix<Double> B) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException("Native BLAS not available");
        int m = A.rows();
        int k = A.cols();
        int n = B.cols();
        if (k != B.rows()) throw new IllegalArgumentException("Matrix dimensions mismatch");

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segA = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * k);
            double[] arrA = ((DenseMatrix<?>)A).toDoubleArray();
            MemorySegment.copy(arrA, 0, segA, ValueLayout.JAVA_DOUBLE, 0, arrA.length);

            MemorySegment segB = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) k * n);
            double[] arrB = ((DenseMatrix<?>)B).toDoubleArray();
            MemorySegment.copy(arrB, 0, segB, ValueLayout.JAVA_DOUBLE, 0, arrB.length);

            MemorySegment segC = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);

            try {
                DGEMM.invokeExact(CblasRowMajor, CblasNoTrans, CblasNoTrans, 
                                  m, n, k, 1.0, segA, k, segB, n, 0.0, segC, n);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }

            double[] result = new double[m * n];
            MemorySegment.copy(segC, ValueLayout.JAVA_DOUBLE, 0, result, 0, m * n);
            return createDenseMatrix(result, m, n, A);
        }
    }

    @Override
    public Double dot(Vector<Double> a, Vector<Double> b) {
         if (!IS_AVAILABLE) throw new UnsupportedOperationException("Native BLAS not available");
         int n = a.dimension();
         try (Arena arena = Arena.ofConfined()) {
             MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
             for(int i=0; i<n; i++) segX.setAtIndex(ValueLayout.JAVA_DOUBLE, i, a.get(i));
             MemorySegment segY = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
             for(int i=0; i<n; i++) segY.setAtIndex(ValueLayout.JAVA_DOUBLE, i, b.get(i));
             try { return (double) DDOT.invokeExact(n, segX, 1, segY, 1); } catch (Throwable e) { throw new RuntimeException(e); }
         }
    }
    
    @Override
    public Double norm(Vector<Double> a) {
         if (!IS_AVAILABLE) throw new UnsupportedOperationException("Native BLAS not available");
         int n = a.dimension();
         try (Arena arena = Arena.ofConfined()) {
             MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
             for(int i=0; i<n; i++) segX.setAtIndex(ValueLayout.JAVA_DOUBLE, i, a.get(i));
             try { return (double) DNRM2.invokeExact(n, segX, 1); } catch (Throwable e) { throw new RuntimeException(e); }
         }
    }

    @Override
    public Vector<Double> add(Vector<Double> a, Vector<Double> b) {
        int n = a.dimension();
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
                return DenseVector.of(java.util.Arrays.asList(result), (Ring<Double>)a.getScalarRing());
            }
        }
        Double[] result = new Double[n];
        for (int i = 0; i < n; i++) result[i] = a.get(i) + b.get(i);
        return DenseVector.of(java.util.Arrays.asList(result), (Ring<Double>)a.getScalarRing());
    }

    @Override
    public Vector<Double> subtract(Vector<Double> a, Vector<Double> b) {
        int n = a.dimension();
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
                return DenseVector.of(java.util.Arrays.asList(result), (Ring<Double>)a.getScalarRing());
            }
        }
        Double[] result = new Double[n];
        for (int i = 0; i < n; i++) result[i] = a.get(i) - b.get(i);
        return DenseVector.of(java.util.Arrays.asList(result), (Ring<Double>)a.getScalarRing());
    }

    @Override
    public Vector<Double> multiply(Vector<Double> vector, Double scalar) {
        int n = vector.dimension();
        if (IS_AVAILABLE) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
                for (int i = 0; i < n; i++) segX.setAtIndex(ValueLayout.JAVA_DOUBLE, i, vector.get(i));
                try { DSCAL.invokeExact(n, scalar.doubleValue(), segX, 1); } catch (Throwable e) { throw new RuntimeException(e); }
                Double[] result = new Double[n];
                for (int i = 0; i < n; i++) result[i] = segX.getAtIndex(ValueLayout.JAVA_DOUBLE, i);
                return DenseVector.of(java.util.Arrays.asList(result), (Ring<Double>)vector.getScalarRing());
            }
        }
        Double[] result = new Double[n];
        for (int i = 0; i < n; i++) result[i] = vector.get(i) * scalar;
        return DenseVector.of(java.util.Arrays.asList(result), (Ring<Double>)vector.getScalarRing());
    }

    @Override
    public Matrix<Double> add(Matrix<Double> a, Matrix<Double> b) {
        int m = a.rows(), n = a.cols();
        if (IS_AVAILABLE) {
             try (Arena arena = Arena.ofConfined()) {
                 int len = m * n;
                 MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, len);
                 MemorySegment segY = arena.allocate(ValueLayout.JAVA_DOUBLE, len);
                 double[] arrA = ((DenseMatrix<?>)a).toDoubleArray();
                 double[] arrB = ((DenseMatrix<?>)b).toDoubleArray();
                 MemorySegment.copy(arrA, 0, segX, ValueLayout.JAVA_DOUBLE, 0, len);
                 MemorySegment.copy(arrB, 0, segY, ValueLayout.JAVA_DOUBLE, 0, len);
                 try { DAXPY.invokeExact(len, 1.0, segX, 1, segY, 1); } catch (Throwable e) { throw new RuntimeException(e); }
                 double[] result = new double[len];
                 MemorySegment.copy(segY, ValueLayout.JAVA_DOUBLE, 0, result, 0, len);
                 return createDenseMatrix(result, m, n, a);
             }
        }
        Double[][] result = new Double[m][n];
        for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) result[i][j] = a.get(i, j) + b.get(i, j);
        return new DenseMatrix<>(result, (Ring<Double>) a.getScalarRing());
    }

    @Override
    public Matrix<Double> subtract(Matrix<Double> a, Matrix<Double> b) {
        int m = a.rows(), n = a.cols();
        if (IS_AVAILABLE) {
             try (Arena arena = Arena.ofConfined()) {
                 int len = m * n;
                 MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, len);
                 MemorySegment segY = arena.allocate(ValueLayout.JAVA_DOUBLE, len);
                 double[] arrA = ((DenseMatrix<?>)a).toDoubleArray();
                 double[] arrB = ((DenseMatrix<?>)b).toDoubleArray();
                 MemorySegment.copy(arrB, 0, segX, ValueLayout.JAVA_DOUBLE, 0, len);
                 MemorySegment.copy(arrA, 0, segY, ValueLayout.JAVA_DOUBLE, 0, len);
                 try { DAXPY.invokeExact(len, -1.0, segX, 1, segY, 1); } catch (Throwable e) { throw new RuntimeException(e); }
                 double[] result = new double[len];
                 MemorySegment.copy(segY, ValueLayout.JAVA_DOUBLE, 0, result, 0, len);
                 return createDenseMatrix(result, m, n, a);
             }
        }
        Double[][] result = new Double[m][n];
        for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) result[i][j] = a.get(i, j) - b.get(i, j);
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
                try { DSCAL.invokeExact(len, scalar.doubleValue(), segX, 1); } catch (Throwable e) { throw new RuntimeException(e); }
                double[] result = new double[len];
                MemorySegment.copy(segX, ValueLayout.JAVA_DOUBLE, 0, result, 0, len);
                return createDenseMatrix(result, m, n, a);
            }
        }
        Double[][] result = new Double[m][n];
        for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) result[i][j] = a.get(i, j) * scalar;
        return new DenseMatrix<>(result, (Ring<Double>) a.getScalarRing());
    }

    @Override
    public Matrix<Double> transpose(Matrix<Double> a) {
        int m = a.rows(), n = a.cols();
        Double[][] result = new Double[n][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                result[j][i] = a.get(i, j);
            }
        }
        return new DenseMatrix<>(result, (Ring<Double>) a.getScalarRing());
    }

    private Matrix<Double> createDenseMatrix(double[] data, int rows, int cols, Matrix<Double> reference) {
        Double[][] resObj = new Double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                resObj[i][j] = data[i * cols + j];
            }
        }
        return new DenseMatrix<>(resObj, (Ring<Double>) reference.getScalarRing());
    }

    @Override
    public Vector<Double> multiply(Matrix<Double> a, Vector<Double> b) {
        int m = a.rows(), k = a.cols();
        if (IS_AVAILABLE) {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment segA = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * k);
                for (int i = 0; i < m; i++) for (int j = 0; j < k; j++) segA.setAtIndex(ValueLayout.JAVA_DOUBLE, (long) i * k + j, a.get(i, j));
                MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, k);
                for (int i = 0; i < k; i++) segX.setAtIndex(ValueLayout.JAVA_DOUBLE, i, b.get(i));
                MemorySegment segY = arena.allocate(ValueLayout.JAVA_DOUBLE, m);
                try { DGEMV.invokeExact(CblasRowMajor, CblasNoTrans, m, k, 1.0, segA, k, segX, 1, 0.0, segY, 1); } catch (Throwable e) { throw new RuntimeException(e); }
                Double[] result = new Double[m];
                for (int i = 0; i < m; i++) result[i] = segY.getAtIndex(ValueLayout.JAVA_DOUBLE, i);
                return DenseVector.of(java.util.Arrays.asList(result), (Ring<Double>)b.getScalarRing());
            }
        }
        Double[] result = new Double[m];
        for (int i = 0; i < m; i++) {
            double sum = 0;
            for (int j = 0; j < k; j++) sum += a.get(i, j) * b.get(j);
            result[i] = sum;
        }
        return DenseVector.of(java.util.Arrays.asList(result), (Ring<Double>)b.getScalarRing());
    }

    @Override
    public org.jscience.core.technical.backend.ExecutionContext createContext() {
        return null; 
    }
}
