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
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.nativ.technical.backend.nativ.NativeLibraryLoader;
import com.google.auto.service.AutoService;
import org.episteme.core.mathematics.linearalgebra.vectors.DenseVector;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.episteme.core.technical.algorithm.AlgorithmProvider;

/**
 * High-Performance Native BLAS Backend using Project Panama (FFM).
 * Binds to OpenBLAS/MKL for Matrix Operations.
 * Implements {@link CPUBackend}, {@link NativeBackend} and {@link AlgorithmProvider}.
 */
@AutoService({Backend.class, LinearAlgebraProvider.class, CPUBackend.class, NativeBackend.class, AlgorithmProvider.class})
public class NativeFFMBLASBackend implements LinearAlgebraProvider<org.episteme.core.mathematics.numbers.real.Real>, CPUBackend, NativeBackend {
    
    private static final Logger LOGGER = Logger.getLogger(NativeFFMBLASBackend.class.getName());

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
    private static MethodHandle DOMATCOPY;
    
    // LAPACK Method Handles
    private static MethodHandle DGESV;
    private static MethodHandle DGETRF;
    private static MethodHandle DGETRI;
    private static MethodHandle DGEQRF;
    private static MethodHandle DORGQR;
    private static MethodHandle DGESVD;
    
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
        
        if (lib.isPresent()) {
             LOGGER.fine("FFM: Successfully matched native library for FFM backend.");
        } else {
             LOGGER.info("FFM: No suitable BLAS/LAPACK library found for FFM backend (OpenBLAS or MKL).");
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

                Optional<MemorySegment> domatcopyAddr = LOOKUP.find("cblas_domatcopy");
                if (domatcopyAddr.isPresent()) {
                    FunctionDescriptor domatcopyDesc = FunctionDescriptor.ofVoid(
                            ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG,
                            ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_LONG,
                            AddressLayout.ADDRESS, ValueLayout.JAVA_LONG
                    );
                    DOMATCOPY = LINKER.downcallHandle(domatcopyAddr.get(), domatcopyDesc);
                }

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

                // QR Decomposition
                Optional<MemorySegment> dgeqrfAddr = LOOKUP.find("LAPACKE_dgeqrf");
                if (dgeqrfAddr.isPresent()) {
                    FunctionDescriptor dgeqrfDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                            ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                            AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS
                    );
                    DGEQRF = LINKER.downcallHandle(dgeqrfAddr.get(), dgeqrfDesc);
                }

                Optional<MemorySegment> dorgqrAddr = LOOKUP.find("LAPACKE_dorgqr");
                if (dorgqrAddr.isPresent()) {
                    FunctionDescriptor dorgqrDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                            ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                            AddressLayout.ADDRESS, ValueLayout.JAVA_INT, AddressLayout.ADDRESS
                    );
                    DORGQR = LINKER.downcallHandle(dorgqrAddr.get(), dorgqrDesc);
                }

                // Singular Value Decomposition
                Optional<MemorySegment> dgesvdAddr = LOOKUP.find("LAPACKE_dgesvd");
                if (dgesvdAddr.isPresent()) {
                    FunctionDescriptor dgesvdDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                            ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_BYTE, 
                            ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, AddressLayout.ADDRESS, 
                            ValueLayout.JAVA_INT, AddressLayout.ADDRESS, AddressLayout.ADDRESS, 
                            ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                            AddressLayout.ADDRESS
                    );
                    DGESVD = LINKER.downcallHandle(dgesvdAddr.get(), dgesvdDesc);
                }

                available = true;
                LOGGER.info("FFM: Backend initialized successfully. Handles: DGEMM=" + (DGEMM != null) + ", DGESV=" + (DGESV != null) + ", DGETRI=" + (DGETRI != null));
            } catch (Throwable t) {
                LOGGER.log(Level.WARNING, "FFM: Failed to link native symbols", t);
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
    public Vector<org.episteme.core.mathematics.numbers.real.Real> solve(Matrix<org.episteme.core.mathematics.numbers.real.Real> A, Vector<org.episteme.core.mathematics.numbers.real.Real> b) {
        if (!IS_AVAILABLE || DGESV == null) return LinearAlgebraProvider.super.solve(A, b);
        org.episteme.core.ComputeContext.checkCurrentCancelled();
        
        int n = A.rows();
        if (n != A.cols()) throw new IllegalArgumentException("Matrix must be square");
        if (n != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");
        
        try (Arena arena = Arena.ofConfined()) {
            int len = (int) ((long) n * n);
            MemorySegment segA = arena.allocate(ValueLayout.JAVA_DOUBLE, len);
            double[] arrA = toDoubleArray(A);
            MemorySegment.copy(arrA, 0, segA, ValueLayout.JAVA_DOUBLE, 0L, (int) Math.min(arrA.length, len));
            
            MemorySegment segB = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
            for(int i=0; i<n; i++) segB.setAtIndex(ValueLayout.JAVA_DOUBLE, (long) i, b.get(i).doubleValue());
            
            MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, n);
            
            int info = (int) DGESV.invokeExact(LAPACK_ROW_MAJOR, n, 1, segA, n, segIpiv, segB, 1);
            if (info != 0) throw new ArithmeticException("Linear solve failed (singular matrix). Info: " + info);
            
            double[] result = new double[n];
            MemorySegment.copy(segB, ValueLayout.JAVA_DOUBLE, 0L, result, 0, n);
            
            List<org.episteme.core.mathematics.numbers.real.Real> list = new ArrayList<>(n);
            for(double v : result) list.add(org.episteme.core.mathematics.numbers.real.Real.of(v));
            return new DenseVector<>(list, (Ring<org.episteme.core.mathematics.numbers.real.Real>) A.getScalarRing());
        } catch (Throwable e) {
             throw new RuntimeException(e);
        }
    }
    
    @Override
    public Matrix<org.episteme.core.mathematics.numbers.real.Real> inverse(Matrix<org.episteme.core.mathematics.numbers.real.Real> A) {
         if (!IS_AVAILABLE || DGETRF == null || DGETRI == null) return LinearAlgebraProvider.super.inverse(A);
         int n = A.rows();
         if (n != A.cols()) throw new IllegalArgumentException("Matrix must be square");

         try (Arena arena = Arena.ofConfined()) {
             int len = (int) ((long) n * n);
             MemorySegment segA = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) len);
             double[] arrA = toDoubleArray(A);
             MemorySegment.copy(arrA, 0, segA, ValueLayout.JAVA_DOUBLE, 0L, (int) Math.min(arrA.length, len));
             
             MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
             
             int info = (int) DGETRF.invokeExact(LAPACK_ROW_MAJOR, n, n, segA, n, segIpiv);
             if (info != 0) throw new ArithmeticException("LU Factorization failed. Info: " + info);
             
             info = (int) DGETRI.invokeExact(LAPACK_ROW_MAJOR, n, segA, n, segIpiv);
             if (info != 0) throw new ArithmeticException("Inverse failed. Info: " + info);
             
             double[] result = new double[n * n];
             MemorySegment.copy(segA, ValueLayout.JAVA_DOUBLE, 0L, result, 0, (int) ( (long) n * n ) );
             
             org.episteme.core.mathematics.numbers.real.Real[][] resObj = new org.episteme.core.mathematics.numbers.real.Real[n][n];
             for(int i=0; i<n; i++) {
                 for(int j=0; j<n; j++) {
                     resObj[i][j] = org.episteme.core.mathematics.numbers.real.Real.of(result[i*n + j]);
                 }
             }
             return new DenseMatrix<>(resObj, (Ring<org.episteme.core.mathematics.numbers.real.Real>) A.getScalarRing());
         } catch (Throwable e) {
             throw new RuntimeException(e);
         }
    }

    @Override
    public org.episteme.core.mathematics.numbers.real.Real determinant(Matrix<org.episteme.core.mathematics.numbers.real.Real> A) {
         if (!IS_AVAILABLE || DGETRF == null) return LinearAlgebraProvider.super.determinant(A);
         int n = A.rows();
         if (n != A.cols()) throw new IllegalArgumentException("Matrix must be square");

         try (Arena arena = Arena.ofConfined()) {
             int len = (int) ((long) n * n);
             MemorySegment segA = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) len);
             double[] arrA = toDoubleArray(A);
             MemorySegment.copy(arrA, 0, segA, ValueLayout.JAVA_DOUBLE, 0L, (int) Math.min(arrA.length, len));
             
             MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
             
             int info = (int) DGETRF.invokeExact(LAPACK_ROW_MAJOR, n, n, segA, n, segIpiv);
             if (info > 0) return org.episteme.core.mathematics.numbers.real.Real.ZERO; // Singular
             
             double det = 1.0;
             for(int i=0; i<n; i++) {
                 det *= segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long) i * n + i);
                 int pivot = segIpiv.getAtIndex(ValueLayout.JAVA_INT, (long) i);
                 if (pivot != i + 1) det = -det;
             }
             return org.episteme.core.mathematics.numbers.real.Real.of(det);
         } catch (Throwable e) {
             throw new RuntimeException(e);
         }
    }
    
    @Override
    public boolean isAvailable() {
        return IS_AVAILABLE;
    }

    @Override
    public String getEnvironmentInfo() {
        return IS_AVAILABLE ? "CPU (FFM/BLAS)" : "N/A";
    }

    @Override
    public String getName() {
        return "Native BLAS Provider (FFM)";
    }

    @Override
    public QRResult<org.episteme.core.mathematics.numbers.real.Real> qr(Matrix<org.episteme.core.mathematics.numbers.real.Real> a) {
        if (!IS_AVAILABLE || DGEQRF == null || DORGQR == null) {
            return LinearAlgebraProvider.super.qr(a);
        }
        int m = a.rows();
        int n = a.cols();
        int k = Math.min(m, n);

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segA = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);
            double[] arrA = toDoubleArray(a);
            MemorySegment.copy(arrA, 0, segA, ValueLayout.JAVA_DOUBLE, 0L, arrA.length);

            MemorySegment tau = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) k);

            // 1. Factorize
            int info = (int) DGEQRF.invokeExact(LAPACK_ROW_MAJOR, m, n, segA, n, tau);
            if (info != 0) throw new RuntimeException("DGEQRF failed with info: " + info);

            // 2. Extract R (upper triangular part)
            double[] rData = new double[k * n];
            for (int i = 0; i < k; i++) {
                for (int j = i; j < n; j++) {
                    rData[i * n + j] = segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long) i * n + j);
                }
            }
            Matrix<org.episteme.core.mathematics.numbers.real.Real> R = createDenseMatrix(rData, k, n, a);

            // 3. Extract Q (orthogonal matrix)
            // dorgqr overwrites the matrix with Q. We use k because we want the economy QR (m x k).
            info = (int) DORGQR.invokeExact(LAPACK_ROW_MAJOR, m, k, k, segA, n, tau);
            if (info != 0) throw new RuntimeException("DORGQR failed with info: " + info);

            double[] qData = new double[m * k];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < k; j++) {
                    qData[i * k + j] = segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long) i * n + j);
                }
            }
            Matrix<org.episteme.core.mathematics.numbers.real.Real> Q = createDenseMatrix(qData, m, k, a);

            return new QRResult<>(Q, R);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public SVDResult<org.episteme.core.mathematics.numbers.real.Real> svd(Matrix<org.episteme.core.mathematics.numbers.real.Real> a) {
        if (!IS_AVAILABLE || DGESVD == null) {
            return LinearAlgebraProvider.super.svd(a);
        }
        int m = a.rows();
        int n = a.cols();
        int k = Math.min(m, n);

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segA = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);
            double[] arrA = toDoubleArray(a);
            MemorySegment.copy(arrA, 0, segA, ValueLayout.JAVA_DOUBLE, 0L, arrA.length);

            MemorySegment s = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) k);
            MemorySegment u = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * m);
            MemorySegment vt = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
            MemorySegment superb = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) Math.max(1, k - 1));

            // jobu = 'A' (full U), jobvt = 'A' (full V^T)
            int info = (int) DGESVD.invokeExact(LAPACK_ROW_MAJOR, (byte) 'A', (byte) 'A', m, n, segA, n, s, u, m, vt, n, superb);
            if (info != 0) throw new RuntimeException("DGESVD failed with info: " + info);

            // Extract S as a vector
            double[] sData = new double[k];
            MemorySegment.copy(s, ValueLayout.JAVA_DOUBLE, 0L, sData, 0, k);
            List<org.episteme.core.mathematics.numbers.real.Real> sList = new ArrayList<>(k);
            for (double v : sData) sList.add(org.episteme.core.mathematics.numbers.real.Real.of(v));
            Vector<org.episteme.core.mathematics.numbers.real.Real> S = new DenseVector<>(sList, (Ring<org.episteme.core.mathematics.numbers.real.Real>) a.getScalarRing());

            // Extract U
            double[] uData = new double[m * m];
            MemorySegment.copy(u, ValueLayout.JAVA_DOUBLE, 0L, uData, 0, m * m);
            Matrix<org.episteme.core.mathematics.numbers.real.Real> U = createDenseMatrix(uData, m, m, a);

            // Extract V (input Vt is V transpose)
            double[] vtData = new double[n * n];
            MemorySegment.copy(vt, ValueLayout.JAVA_DOUBLE, 0L, vtData, 0, n * n);

            // We return V, so we transpose Vt (in row-major, VT[j*n + i] is V[i*n + j])
            org.episteme.core.mathematics.numbers.real.Real[][] vObj = new org.episteme.core.mathematics.numbers.real.Real[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    vObj[i][j] = org.episteme.core.mathematics.numbers.real.Real.of(vtData[j * n + i]);
                }
            }
            Matrix<org.episteme.core.mathematics.numbers.real.Real> V = new DenseMatrix<>(vObj, (Ring<org.episteme.core.mathematics.numbers.real.Real>) a.getScalarRing());

            return new SVDResult<>(U, S, V);
        } catch (Throwable t) {
            throw new RuntimeException(t);
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
        return ring.zero() instanceof org.episteme.core.mathematics.numbers.real.Real;
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
    public Matrix<org.episteme.core.mathematics.numbers.real.Real> multiply(Matrix<org.episteme.core.mathematics.numbers.real.Real> A, Matrix<org.episteme.core.mathematics.numbers.real.Real> B) {
        if (!IS_AVAILABLE) return LinearAlgebraProvider.super.multiply(A, B);
        int m = A.rows();
        int k = A.cols();
        int n = B.cols();
        if (k != B.rows()) throw new IllegalArgumentException("Matrix dimensions mismatch");

        try (Arena arena = Arena.ofConfined()) {
            int lenA = m * k;
            MemorySegment segA = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) lenA);
            double[] arrA = toDoubleArray(A);
            MemorySegment.copy(arrA, 0, segA, ValueLayout.JAVA_DOUBLE, 0L, (int) Math.min(arrA.length, lenA));

            int lenB = k * n;
            MemorySegment segB = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) lenB);
            double[] arrB = toDoubleArray(B);
            MemorySegment.copy(arrB, 0, segB, ValueLayout.JAVA_DOUBLE, 0L, (int) Math.min(arrB.length, lenB));

            MemorySegment segC = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);

            try {
                DGEMM.invokeExact(CblasRowMajor, CblasNoTrans, CblasNoTrans, 
                                  m, n, k, 1.0, segA, k, segB, n, 0.0, segC, n);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }

            double[] result = new double[m * n];
            MemorySegment.copy(segC, ValueLayout.JAVA_DOUBLE, 0L, result, 0, (int) ( (long) m * n ) );
            return createDenseMatrix(result, m, n, A);
        }
    }

    @Override
    public org.episteme.core.mathematics.numbers.real.Real dot(Vector<org.episteme.core.mathematics.numbers.real.Real> a, Vector<org.episteme.core.mathematics.numbers.real.Real> b) {
         if (!IS_AVAILABLE) return LinearAlgebraProvider.super.dot(a, b);
         int n = a.dimension();
         try (Arena arena = Arena.ofConfined()) {
             MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
             for(int i=0; i<n; i++) segX.setAtIndex(ValueLayout.JAVA_DOUBLE, i, a.get(i).doubleValue());
             MemorySegment segY = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
             for(int i=0; i<n; i++) segY.setAtIndex(ValueLayout.JAVA_DOUBLE, i, b.get(i).doubleValue());
             try { return org.episteme.core.mathematics.numbers.real.Real.of((double) DDOT.invokeExact(n, segX, 1, segY, 1)); } catch (Throwable e) { throw new RuntimeException(e); }
         }
    }
    
    @Override
    public org.episteme.core.mathematics.numbers.real.Real norm(Vector<org.episteme.core.mathematics.numbers.real.Real> a) {
         if (!IS_AVAILABLE) return LinearAlgebraProvider.super.norm(a);
         int n = a.dimension();
         try (Arena arena = Arena.ofConfined()) {
             MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
             for(int i=0; i<n; i++) segX.setAtIndex(ValueLayout.JAVA_DOUBLE, i, a.get(i).doubleValue());
             try { return org.episteme.core.mathematics.numbers.real.Real.of((double) DNRM2.invokeExact(n, segX, 1)); } catch (Throwable e) { throw new RuntimeException(e); }
         }
    }

    @Override
    public Vector<org.episteme.core.mathematics.numbers.real.Real> add(Vector<org.episteme.core.mathematics.numbers.real.Real> a, Vector<org.episteme.core.mathematics.numbers.real.Real> b) {
        if (!IS_AVAILABLE) return LinearAlgebraProvider.super.add(a, b);
        int n = a.dimension();
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
            MemorySegment segY = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
            for (int i = 0; i < n; i++) {
                segX.setAtIndex(ValueLayout.JAVA_DOUBLE, i, a.get(i).doubleValue());
                segY.setAtIndex(ValueLayout.JAVA_DOUBLE, i, b.get(i).doubleValue());
            }
            try { DAXPY.invokeExact(n, 1.0, segX, 1, segY, 1); } catch (Throwable e) { throw new RuntimeException(e); }
            org.episteme.core.mathematics.numbers.real.Real[] result = new org.episteme.core.mathematics.numbers.real.Real[n];
            for (int i = 0; i < n; i++) result[i] = org.episteme.core.mathematics.numbers.real.Real.of(segY.getAtIndex(ValueLayout.JAVA_DOUBLE, i));
            return DenseVector.of(java.util.Arrays.asList(result), (Ring<org.episteme.core.mathematics.numbers.real.Real>)a.getScalarRing());
        }
    }

    @Override
    public Vector<org.episteme.core.mathematics.numbers.real.Real> subtract(Vector<org.episteme.core.mathematics.numbers.real.Real> a, Vector<org.episteme.core.mathematics.numbers.real.Real> b) {
        if (!IS_AVAILABLE) return LinearAlgebraProvider.super.subtract(a, b);
        int n = a.dimension();
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
            MemorySegment segY = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
            for (int i = 0; i < n; i++) {
                segX.setAtIndex(ValueLayout.JAVA_DOUBLE, i, b.get(i).doubleValue());
                segY.setAtIndex(ValueLayout.JAVA_DOUBLE, i, a.get(i).doubleValue());
            }
            try { DAXPY.invokeExact(n, -1.0, segX, 1, segY, 1); } catch (Throwable e) { throw new RuntimeException(e); }
            org.episteme.core.mathematics.numbers.real.Real[] result = new org.episteme.core.mathematics.numbers.real.Real[n];
            for (int i = 0; i < n; i++) result[i] = org.episteme.core.mathematics.numbers.real.Real.of(segY.getAtIndex(ValueLayout.JAVA_DOUBLE, i));
            return DenseVector.of(java.util.Arrays.asList(result), (Ring<org.episteme.core.mathematics.numbers.real.Real>)a.getScalarRing());
        }
    }

    @Override
    public Vector<org.episteme.core.mathematics.numbers.real.Real> multiply(Vector<org.episteme.core.mathematics.numbers.real.Real> vector, org.episteme.core.mathematics.numbers.real.Real scalar) {
        if (!IS_AVAILABLE) return LinearAlgebraProvider.super.multiply(vector, scalar);
        int n = vector.dimension();
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
            for (int i = 0; i < n; i++) segX.setAtIndex(ValueLayout.JAVA_DOUBLE, i, vector.get(i).doubleValue());
            try { DSCAL.invokeExact(n, scalar.doubleValue(), segX, 1); } catch (Throwable e) { throw new RuntimeException(e); }
            org.episteme.core.mathematics.numbers.real.Real[] result = new org.episteme.core.mathematics.numbers.real.Real[n];
            for (int i = 0; i < n; i++) result[i] = org.episteme.core.mathematics.numbers.real.Real.of(segX.getAtIndex(ValueLayout.JAVA_DOUBLE, i));
            return DenseVector.of(java.util.Arrays.asList(result), (Ring<org.episteme.core.mathematics.numbers.real.Real>)vector.getScalarRing());
        }
    }

    @Override
    public Matrix<org.episteme.core.mathematics.numbers.real.Real> add(Matrix<org.episteme.core.mathematics.numbers.real.Real> a, Matrix<org.episteme.core.mathematics.numbers.real.Real> b) {
        if (!IS_AVAILABLE) return LinearAlgebraProvider.super.add(a, b);
        int m = a.rows(), n = a.cols();
        try (Arena arena = Arena.ofConfined()) {
            int len = m * n;
            MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, len);
            MemorySegment segY = arena.allocate(ValueLayout.JAVA_DOUBLE, len);
            double[] arrA = toDoubleArray(a);
            double[] arrB = toDoubleArray(b);
            MemorySegment.copy(arrA, 0, segX, ValueLayout.JAVA_DOUBLE, 0L, (int) Math.min(arrA.length, len));
            MemorySegment.copy(arrB, 0, segY, ValueLayout.JAVA_DOUBLE, 0L, (int) Math.min(arrB.length, len));
            try { DAXPY.invokeExact(len, 1.0, segX, 1, segY, 1); } catch (Throwable e) { throw new RuntimeException(e); }
            double[] result = new double[len];
            MemorySegment.copy(segY, ValueLayout.JAVA_DOUBLE, 0L, result, 0, len);
            return createDenseMatrix(result, m, n, a);
        }
    }

    @Override
    public Matrix<org.episteme.core.mathematics.numbers.real.Real> subtract(Matrix<org.episteme.core.mathematics.numbers.real.Real> a, Matrix<org.episteme.core.mathematics.numbers.real.Real> b) {
        if (!IS_AVAILABLE) return LinearAlgebraProvider.super.subtract(a, b);
        int m = a.rows(), n = a.cols();
        try (Arena arena = Arena.ofConfined()) {
            int len = m * n;
            MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, len);
            MemorySegment segY = arena.allocate(ValueLayout.JAVA_DOUBLE, len);
            double[] arrA = toDoubleArray(a);
            double[] arrB = toDoubleArray(b);
            MemorySegment.copy(arrB, 0, segX, ValueLayout.JAVA_DOUBLE, 0L, (int) Math.min(arrB.length, len));
            MemorySegment.copy(arrA, 0, segY, ValueLayout.JAVA_DOUBLE, 0L, (int) Math.min(arrA.length, len));
            try { DAXPY.invokeExact(len, -1.0, segX, 1, segY, 1); } catch (Throwable e) { throw new RuntimeException(e); }
            double[] result = new double[len];
            MemorySegment.copy(segY, ValueLayout.JAVA_DOUBLE, 0L, result, 0, len);
            return createDenseMatrix(result, m, n, a);
        }
    }

    @Override
    public Matrix<org.episteme.core.mathematics.numbers.real.Real> scale(org.episteme.core.mathematics.numbers.real.Real scalar, Matrix<org.episteme.core.mathematics.numbers.real.Real> a) {
        if (!IS_AVAILABLE) return LinearAlgebraProvider.super.scale(scalar, a);
        int m = a.rows(), n = a.cols();
        try (Arena arena = Arena.ofConfined()) {
            int len = m * n;
            MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, len);
            double[] arrA = toDoubleArray(a);
            MemorySegment.copy(arrA, 0, segX, ValueLayout.JAVA_DOUBLE, 0L, (int) Math.min(arrA.length, len));
            try { DSCAL.invokeExact(len, scalar.doubleValue(), segX, 1); } catch (Throwable e) { throw new RuntimeException(e); }
            double[] result = new double[len];
            MemorySegment.copy(segX, ValueLayout.JAVA_DOUBLE, 0L, result, 0, len);
            return createDenseMatrix(result, m, n, a);
        }
    }

    @Override
    public Matrix<org.episteme.core.mathematics.numbers.real.Real> transpose(Matrix<org.episteme.core.mathematics.numbers.real.Real> a) {
        if (IS_AVAILABLE && DOMATCOPY != null) {
            int m = a.rows(), n = a.cols();
            try (Arena arena = Arena.ofConfined()) {
                int len = m * n;
                MemorySegment segA = arena.allocate(ValueLayout.JAVA_DOUBLE, (long)len);
                double[] arrA = toDoubleArray(a);
                MemorySegment.copy(arrA, 0, segA, ValueLayout.JAVA_DOUBLE, 0L, (int)arrA.length);
                
                MemorySegment segB = arena.allocate(ValueLayout.JAVA_DOUBLE, len);
                
                // CblasTrans = 112
                try { DOMATCOPY.invokeExact(CblasRowMajor, 112, (long)m, (long)n, 1.0, segA, (long)n, segB, (long)m); } catch (Throwable e) { throw new RuntimeException(e); }
                
                double[] result = new double[len];
                MemorySegment.copy(segB, ValueLayout.JAVA_DOUBLE, 0L, result, 0, len);
                return createDenseMatrix(result, n, m, a);
            }
        }
        
        return LinearAlgebraProvider.super.transpose(a);
    }

    private Matrix<org.episteme.core.mathematics.numbers.real.Real> createDenseMatrix(double[] data, int rows, int cols, Matrix<org.episteme.core.mathematics.numbers.real.Real> reference) {
        org.episteme.core.mathematics.numbers.real.Real[][] resObj = new org.episteme.core.mathematics.numbers.real.Real[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                resObj[i][j] = org.episteme.core.mathematics.numbers.real.Real.of(data[i * cols + j]);
            }
        }
        return new DenseMatrix<>(resObj, (Ring<org.episteme.core.mathematics.numbers.real.Real>) reference.getScalarRing());
    }

    @Override
    public Vector<org.episteme.core.mathematics.numbers.real.Real> multiply(Matrix<org.episteme.core.mathematics.numbers.real.Real> a, Vector<org.episteme.core.mathematics.numbers.real.Real> b) {
        if (!IS_AVAILABLE) return LinearAlgebraProvider.super.multiply(a, b);
        int m = a.rows(), k = a.cols();
        try (Arena arena = Arena.ofConfined()) {
            int lenA = m * k;
            MemorySegment segA = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) lenA);
            double[] arrA = toDoubleArray(a);
            MemorySegment.copy(arrA, 0, segA, ValueLayout.JAVA_DOUBLE, 0L, (int) Math.min(arrA.length, lenA));
            
            MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) k);
            for (int i = 0; i < k; i++) segX.setAtIndex(ValueLayout.JAVA_DOUBLE, (long) i, b.get(i).doubleValue());
            MemorySegment segY = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m);
            try { DGEMV.invokeExact(CblasRowMajor, CblasNoTrans, m, k, 1.0, segA, k, segX, 1, 0.0, segY, 1); } catch (Throwable e) { throw new RuntimeException(e); }
            org.episteme.core.mathematics.numbers.real.Real[] result = new org.episteme.core.mathematics.numbers.real.Real[m];
            for (int i = 0; i < m; i++) result[i] = org.episteme.core.mathematics.numbers.real.Real.of(segY.getAtIndex(ValueLayout.JAVA_DOUBLE, (long) i));
            return DenseVector.of(java.util.Arrays.asList(result), (Ring<org.episteme.core.mathematics.numbers.real.Real>)b.getScalarRing());
        }
    }

    private double[] toDoubleArray(Matrix<org.episteme.core.mathematics.numbers.real.Real> matrix) {
        Object mObj = matrix;
        if (mObj instanceof org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix) {
            return ((org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix) mObj).toDoubleArray();
        } else if (matrix instanceof org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix) {
            return ((org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<?>) matrix).toDoubleArray();
        } else {
             int rows = matrix.rows();
             int cols = matrix.cols();
             double[] result = new double[rows * cols];
             for (int i = 0; i < rows; i++) {
                 for (int j = 0; j < cols; j++) {
                     result[i * cols + j] = matrix.get(i, j).doubleValue();
                 }
             }
             return result;
        }
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return null; 
    }
}
