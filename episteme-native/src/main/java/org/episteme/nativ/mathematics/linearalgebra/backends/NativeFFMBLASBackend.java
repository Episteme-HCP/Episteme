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
import org.episteme.core.technical.backend.nativ.NativeLibraryLoader;
import com.google.auto.service.AutoService;
import org.episteme.core.mathematics.linearalgebra.vectors.DenseVector;

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
@AutoService({Backend.class, LinearAlgebraProvider.class, CPUBackend.class, NativeBackend.class, AlgorithmProvider.class})
public class NativeFFMBLASBackend implements LinearAlgebraProvider<org.episteme.core.mathematics.numbers.real.Real>, CPUBackend, NativeBackend {
    
    private static final Logger logger = LoggerFactory.getLogger(NativeFFMBLASBackend.class);

    private static final SymbolLookup LOOKUP;
    private static final SymbolLookup LAPACK_LOOKUP;
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
    private static MethodHandle DPOTRF;
    private static MethodHandle DSYEV;
    private static MethodHandle DGELS;
    
    private static final int LAPACK_ROW_MAJOR = 101;

    private static Optional<MemorySegment> findLapackSymbol(String name) {
        Optional<MemorySegment> sym = NativeLibraryLoader.findSymbol(LOOKUP, name);
        if (sym.isEmpty() && LAPACK_LOOKUP != null) {
            sym = NativeLibraryLoader.findSymbol(LAPACK_LOOKUP, name);
        }
        return sym;
    }

    static {
        Arena arena = Arena.global();
        Optional<SymbolLookup> lib = NativeLibraryLoader.loadLibrary("openblas", arena);
        if (lib.isEmpty()) {
             lib = NativeLibraryLoader.loadLibrary("mkl_rt", arena);
        }
        
        if (lib.isPresent()) {
             logger.info("FFM: Successfully matched native library for FFM backend: {}", lib.get());
        } else {
             logger.info("FFM: No local BLAS/LAPACK library found in libs/. Attempting system lookup (CAUTION: possible ABI mismatch).");
             lib = NativeLibraryLoader.getSystemLookup();
        }
        
        LOOKUP = lib.orElse(null);
        
        Optional<SymbolLookup> lapackLib = NativeLibraryLoader.loadLibrary("lapacke", arena);
        if (lapackLib.isEmpty()) {
            lapackLib = NativeLibraryLoader.loadLibrary("lapack", arena);
        }
        LAPACK_LOOKUP = lapackLib.orElse(null);
        
        boolean available = false;

        if (LOOKUP != null) {
            try {
                // BLAS Handles - Use JAVA_INT as standard, but we'll check availability
                FunctionDescriptor dgemmDesc = FunctionDescriptor.ofVoid(
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                        ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                        ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                Optional<MemorySegment> dgemmSym = NativeLibraryLoader.findSymbol(LOOKUP, "cblas_dgemm", "dgemm_");
                if (dgemmSym.isPresent()) {
                    DGEMM = LINKER.downcallHandle(dgemmSym.get(), dgemmDesc);
                }

                FunctionDescriptor ddotDesc = FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE,
                        ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                DDOT = NativeLibraryLoader.findSymbol(LOOKUP, "cblas_ddot", "ddot_")
                    .map(s -> LINKER.downcallHandle(s, ddotDesc)).orElse(null);

                FunctionDescriptor dnrm2Desc = FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE,
                        ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                DNRM2 = NativeLibraryLoader.findSymbol(LOOKUP, "cblas_dnrm2", "dnrm2_")
                    .map(s -> LINKER.downcallHandle(s, dnrm2Desc)).orElse(null);

                FunctionDescriptor daxpyDesc = FunctionDescriptor.ofVoid(
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT, 
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                DAXPY = NativeLibraryLoader.findSymbol(LOOKUP, "cblas_daxpy", "daxpy_")
                    .map(s -> LINKER.downcallHandle(s, daxpyDesc)).orElse(null);
                
                FunctionDescriptor dscalDesc = FunctionDescriptor.ofVoid(
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                DSCAL = NativeLibraryLoader.findSymbol(LOOKUP, "cblas_dscal", "dscal_")
                    .map(s -> LINKER.downcallHandle(s, dscalDesc)).orElse(null);

                FunctionDescriptor dgemvDesc = FunctionDescriptor.ofVoid(
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                DGEMV = NativeLibraryLoader.findSymbol(LOOKUP, "cblas_dgemv", "dgemv_")
                    .map(s -> LINKER.downcallHandle(s, dgemvDesc)).orElse(null);

                FunctionDescriptor domatcopyDesc = FunctionDescriptor.ofVoid(
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_DOUBLE, AddressLayout.ADDRESS, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                DOMATCOPY = NativeLibraryLoader.findSymbol(LOOKUP, "cblas_domatcopy", "mkl_domatcopy", "domatcopy_")
                    .map(s -> LINKER.downcallHandle(s, domatcopyDesc)).orElse(null);

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

                // Eigen
                FunctionDescriptor dsyevDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_BYTE,
                        ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT,
                        AddressLayout.ADDRESS
                );
                DSYEV = findLapackSymbol("LAPACKE_dsyev")
                    .map(s -> LINKER.downcallHandle(s, dsyevDesc)).orElse(null);

                FunctionDescriptor dgelsDesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, AddressLayout.ADDRESS,
                        ValueLayout.JAVA_INT, AddressLayout.ADDRESS, ValueLayout.JAVA_INT
                );
                DGELS = findLapackSymbol("LAPACKE_dgels")
                    .map(s -> LINKER.downcallHandle(s, dgelsDesc)).orElse(null);

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
    public Vector<org.episteme.core.mathematics.numbers.real.Real> solve(Matrix<org.episteme.core.mathematics.numbers.real.Real> A, Vector<org.episteme.core.mathematics.numbers.real.Real> b) {
        if (!IS_AVAILABLE || DGESV == null) throw new UnsupportedOperationException(getName() + ": solve() not available");
        org.episteme.core.ComputeContext.checkCurrentCancelled();
        
        int m = A.rows();
        int n = A.cols();
        if (m == n) {
             if (n != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");
        } else {
             // For rectangular, DGELS handles least squares
             if (m != b.dimension()) throw new IllegalArgumentException("Dimension mismatch (m != b.dim)");
        }
        
        try (Arena arena = Arena.ofConfined()) {
            if (m == n) {
                // Square system via DGESV
                MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(A));
                MemorySegment segB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b));
                MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, n);
                
                int info = (int) DGESV.invokeExact(LAPACK_ROW_MAJOR, n, 1, segA, n, segIpiv, segB, 1);
                if (info != 0) throw new ArithmeticException("DGESV failed with info: " + info);
                
                double[] result = segB.toArray(ValueLayout.JAVA_DOUBLE);
                return createDenseVector(result, n, A);
            } else {
                // Rectangular system via DGELS (Least Squares)
                if (DGELS == null) throw new UnsupportedOperationException(getName() + ": dgels() (least squares) not available");
                
                MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(A));
                // B must be large enough to hold the result (max(m, n))
                int maxDim = Math.max(m, n);
                double[] bPad = new double[maxDim];
                for(int i=0; i<m; i++) bPad[i] = b.get(i).doubleValue();
                MemorySegment segB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, bPad);
                
                // dgels(layout, trans, m, n, nrhs, A, lda, B, ldb)
                int info = (int) DGELS.invokeExact(LAPACK_ROW_MAJOR, (byte) 'N', m, n, 1, segA, n, segB, 1);
                if (info != 0) throw new RuntimeException("DGELS failed with info: " + info);
                
                double[] result = new double[n];
                MemorySegment.copy(segB, ValueLayout.JAVA_DOUBLE, 0L, result, 0, n);
                return createDenseVector(result, n, A);
            }
        } catch (Throwable e) {
             throw new RuntimeException("FFM Solve failed", e);
        }
    }

    private Vector<org.episteme.core.mathematics.numbers.real.Real> createDenseVector(double[] data, int n, Matrix<org.episteme.core.mathematics.numbers.real.Real> ref) {
        List<org.episteme.core.mathematics.numbers.real.Real> list = new ArrayList<>(n);
        for (double v : data) list.add(org.episteme.core.mathematics.numbers.real.Real.of(v));
        return new DenseVector<>(list, (Ring<org.episteme.core.mathematics.numbers.real.Real>) ref.getScalarRing());
    }

    @Override
    public Matrix<org.episteme.core.mathematics.numbers.real.Real> transpose(Matrix<org.episteme.core.mathematics.numbers.real.Real> a) {
        if (!IS_AVAILABLE || DOMATCOPY == null) throw new UnsupportedOperationException(getName() + ": transpose() not available");
        
        int rows = a.rows();
        int cols = a.cols();
        
        org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix res = org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix.direct(cols, rows);
        
        try (Arena arena = Arena.ofConfined()) {
            double[] arrA = toDoubleArray(a);
            MemorySegment segA = arena.allocate(ValueLayout.JAVA_DOUBLE, arrA.length);
            MemorySegment.copy(arrA, 0, segA, ValueLayout.JAVA_DOUBLE, 0L, arrA.length);
            
            MemorySegment segC = MemorySegment.ofBuffer(res.getBuffer());
            
            // cblas_domatcopy(layout, trans, rows, cols, alpha, A, lda, B, ldb)
            // trans=CblasTrans (112)
            DOMATCOPY.invokeExact(CblasRowMajor, 112, rows, cols, 1.0, segA, cols, segC, rows);
            return res;
        } catch (Throwable t) {
            logger.error("FFM BLAS Transpose failed: {}", t.getMessage());
            throw new RuntimeException("FFM Transpose Operation Failed", t);
        }
    }
    
    @Override
    public Matrix<org.episteme.core.mathematics.numbers.real.Real> inverse(Matrix<org.episteme.core.mathematics.numbers.real.Real> A) {
         if (!IS_AVAILABLE || DGETRF == null || DGETRI == null) throw new UnsupportedOperationException(getName() + ": inverse() not available");
         int m = A.rows();
         int n = A.cols();
         if (m != n) {
              return pseudoInverse(A);
         }
         if (n <= 0) throw new IllegalArgumentException("Matrix dimension must be positive");
         if (n != A.cols()) throw new IllegalArgumentException("Matrix must be square");

         try (Arena arena = Arena.ofConfined()) {
             double[] arrA = toDoubleArray(A);
             if (arrA.length < (long)n * n) {
                 throw new IllegalArgumentException("Matrix data size mismatch: expected " + (n*n) + ", got " + arrA.length);
             }

             MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, arrA);
             MemorySegment segIpiv = arena.allocate(ValueLayout.JAVA_INT, n);
             
              // 1. LU Factorization
              int info;
              try {
                  info = (int) DGETRF.invokeExact(LAPACK_ROW_MAJOR, n, n, segA, n, segIpiv);
              } catch (Throwable t) {
                  throw new RuntimeException("DGETRF failed", t);
              }
              if (info != 0) {
                  if (info > 0) throw new ArithmeticException("Matrix is singular (U(" + info + "," + info + ") is exactly zero). Cannot compute inverse.");
                  throw new ArithmeticException("LU Factorization failed with illegal argument at position " + (-info));
              }
              
             // 2. Inverse using LU Factorization
             try {
                info = (int) DGETRI.invokeExact(LAPACK_ROW_MAJOR, n, segA, n, segIpiv);
             } catch (Throwable t) {
                 throw new RuntimeException("DGETRI failed", t);
             }
            if (info != 0) {
                if (info > 0) {
                    throw new ArithmeticException("Inversion failed: Matrix is singular (U(" + info + "," + info + ") is zero).");
                }
                throw new IllegalArgumentException("Inversion failed: Illegal value in parameter " + (-info));
            }
             
             double[] result = segA.toArray(ValueLayout.JAVA_DOUBLE);
             
             return createDenseMatrix(result, n, n, A);
         }
    }

    private Matrix<org.episteme.core.mathematics.numbers.real.Real> pseudoInverse(Matrix<org.episteme.core.mathematics.numbers.real.Real> a) {
        SVDResult<org.episteme.core.mathematics.numbers.real.Real> svd = svd(a);
        // A+ = V * S+ * UT
        int m = a.rows();
        int n = a.cols();
        int k = svd.S().dimension();
        
        double[][] sInvData = new double[n][m];
        for (int i = 0; i < k; i++) {
            double sVal = svd.S().get(i).doubleValue();
            if (sVal > 1e-12) { // Tolerance for non-zero singular value
                sInvData[i][i] = 1.0 / sVal;
            }
        }
        Matrix<org.episteme.core.mathematics.numbers.real.Real> sInv = new DenseMatrix<>(
            java.util.Arrays.stream(sInvData).map(row -> java.util.Arrays.stream(row).mapToObj(org.episteme.core.mathematics.numbers.real.Real::of).toArray(org.episteme.core.mathematics.numbers.real.Real[]::new)).toArray(org.episteme.core.mathematics.numbers.real.Real[][]::new),
            (Ring<org.episteme.core.mathematics.numbers.real.Real>) a.getScalarRing()
        );
        
        return svd.V().multiply(sInv).multiply(svd.U().transpose());
    }

    @Override
    public org.episteme.core.mathematics.numbers.real.Real determinant(Matrix<org.episteme.core.mathematics.numbers.real.Real> A) {
         if (!IS_AVAILABLE || DGETRF == null) throw new UnsupportedOperationException(getName() + ": determinant() not available");
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
             logger.warn("FFM Determinant failed: {}", e.getMessage());
             throw new RuntimeException("FFM Determinant Operation Failed", e);
         }
    }
    
    @Override
    public boolean isAvailable() {
        return IS_AVAILABLE;
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
    public QRResult<org.episteme.core.mathematics.numbers.real.Real> qr(Matrix<org.episteme.core.mathematics.numbers.real.Real> a) {
        if (!IS_AVAILABLE || DGEQRF == null || DORGQR == null) {
            throw new UnsupportedOperationException(getName() + ": qr() not available");
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
            throw new UnsupportedOperationException(getName() + ": svd() not available");
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
    public LUResult<org.episteme.core.mathematics.numbers.real.Real> lu(Matrix<org.episteme.core.mathematics.numbers.real.Real> a) {
        if (!IS_AVAILABLE || DGETRF == null) throw new UnsupportedOperationException(getName() + ": lu() not available");
        int m = a.rows();
        int n = a.cols();
        int k = Math.min(m, n);

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segA = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);
            double[] arrA = toDoubleArray(a);
            MemorySegment.copy(arrA, 0, segA, ValueLayout.JAVA_DOUBLE, 0L, arrA.length);
            
            MemorySegment ipiv = arena.allocate(ValueLayout.JAVA_INT, (long) k);
            
            int info = (int) DGETRF.invokeExact(LAPACK_ROW_MAJOR, m, n, segA, n, ipiv);
            if (info < 0) throw new IllegalArgumentException("DGETRF failed with info: " + info);

            double[] lData = new double[m * k];
            double[] uData = new double[k * n];

            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    double val = segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long) i * n + j);
                    if (i > j) {
                        lData[i * k + j] = val;
                        if (i < k) uData[i * n + j] = 0.0;
                    } else if (i == j) {
                        lData[i * k + j] = 1.0;
                        uData[i * n + j] = val;
                    } else {
                        if (j < k) lData[i * k + j] = 0.0;
                        uData[i * n + j] = val;
                    }
                }
            }

            double[] pData = new double[m];
            for (int i = 0; i < m; i++) pData[i] = i;

            for (int i = 0; i < k; i++) {
                int ip = ipiv.getAtIndex(ValueLayout.JAVA_INT, (long) i) - 1; // 1-indexed SWAP
                if (ip != i) {
                    double tmp = pData[i];
                    pData[i] = pData[ip];
                    pData[ip] = tmp;
                }
            }

            return new LUResult<>(
                createDenseMatrix(lData, m, k, a),
                createDenseMatrix(uData, k, n, a),
                new DenseVector<>(java.util.Arrays.stream(pData).mapToObj(org.episteme.core.mathematics.numbers.real.Real::of).toList(), (Ring<org.episteme.core.mathematics.numbers.real.Real>) a.getScalarRing())
            );
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public CholeskyResult<org.episteme.core.mathematics.numbers.real.Real> cholesky(Matrix<org.episteme.core.mathematics.numbers.real.Real> a) {
        if (!IS_AVAILABLE || DPOTRF == null) throw new UnsupportedOperationException(getName() + ": cholesky() not available");
        int n = a.rows();
        if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square");

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segA = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
            double[] arrA = toDoubleArray(a);
            MemorySegment.copy(arrA, 0, segA, ValueLayout.JAVA_DOUBLE, 0L, arrA.length);
            
            int info = (int) DPOTRF.invokeExact(LAPACK_ROW_MAJOR, (byte) 'L', n, segA, n);
            if (info != 0) throw new ArithmeticException("Matrix is not positive definite (DPOTRF info: " + info + ")");

            double[] lData = new double[n * n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (j <= i) lData[i * n + j] = segA.getAtIndex(ValueLayout.JAVA_DOUBLE, (long) i * n + j);
                    else lData[i * n + j] = 0.0;
                }
            }

            return new CholeskyResult<>(createDenseMatrix(lData, n, n, a));
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public EigenResult<org.episteme.core.mathematics.numbers.real.Real> eigen(Matrix<org.episteme.core.mathematics.numbers.real.Real> a) {
        if (!IS_AVAILABLE || DSYEV == null) throw new UnsupportedOperationException(getName() + ": eigen() not available");
        int n = a.rows();
        if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square");

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segA = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
            double[] arrA = toDoubleArray(a);
            MemorySegment.copy(arrA, 0, segA, ValueLayout.JAVA_DOUBLE, 0L, arrA.length);
            
            MemorySegment w = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n);
            
            int info = (int) DSYEV.invokeExact(LAPACK_ROW_MAJOR, (byte) 'V', (byte) 'U', n, segA, n, w);
            if (info != 0) throw new RuntimeException("DSYEV failed with info: " + info);

            double[] vData = new double[n * n];
            MemorySegment.copy(segA, ValueLayout.JAVA_DOUBLE, 0L, vData, 0, n * n);

            double[] wData = new double[n];
            MemorySegment.copy(w, ValueLayout.JAVA_DOUBLE, 0L, wData, 0, n);

            return new EigenResult<>(
                createDenseMatrix(vData, n, n, a),
                new DenseVector<>(java.util.Arrays.stream(wData).mapToObj(org.episteme.core.mathematics.numbers.real.Real::of).toList(), (Ring<org.episteme.core.mathematics.numbers.real.Real>) a.getScalarRing())
            );
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
    public void shutdown() {
        // No-op. Panama memory segments are managed via ScopedArena in operations.
    }

    @Override
    public Matrix<org.episteme.core.mathematics.numbers.real.Real> multiply(Matrix<org.episteme.core.mathematics.numbers.real.Real> A, Matrix<org.episteme.core.mathematics.numbers.real.Real> B) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": multiply() not available");
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
                logger.warn("FFM DGEMM failed: {}", e.getMessage());
                throw new RuntimeException("FFM Multiply Operation Failed", e);
            }

            double[] result = new double[m * n];
            MemorySegment.copy(segC, ValueLayout.JAVA_DOUBLE, 0L, result, 0, (int) ( (long) m * n ) );
            return createDenseMatrix(result, m, n, A);
        }
    }

    @Override
    public org.episteme.core.mathematics.numbers.real.Real dot(Vector<org.episteme.core.mathematics.numbers.real.Real> a, Vector<org.episteme.core.mathematics.numbers.real.Real> b) {
         if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": dot() not available");
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
         if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": norm() not available");
         int n = a.dimension();
         try (Arena arena = Arena.ofConfined()) {
             MemorySegment segX = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
             for(int i=0; i<n; i++) segX.setAtIndex(ValueLayout.JAVA_DOUBLE, i, a.get(i).doubleValue());
             try { return org.episteme.core.mathematics.numbers.real.Real.of((double) DNRM2.invokeExact(n, segX, 1)); } catch (Throwable e) { throw new RuntimeException(e); }
         }
    }

    @Override
    public Vector<org.episteme.core.mathematics.numbers.real.Real> add(Vector<org.episteme.core.mathematics.numbers.real.Real> a, Vector<org.episteme.core.mathematics.numbers.real.Real> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": Vector add() not available");
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
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": Vector subtract() not available");
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
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": Vector multiply() not available");
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
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": Matrix add() not available");
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
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": Matrix subtract() not available");
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

    private double[] toDoubleArray(Vector<org.episteme.core.mathematics.numbers.real.Real> vector) {
        if (vector instanceof org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector) {
            return ((org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector) vector).toDoubleArray();
        }
        int n = vector.dimension();
        double[] result = new double[n];
        for (int i = 0; i < n; i++) {
            result[i] = vector.get(i).doubleValue();
        }
        return result;
    }

    @Override
    public Matrix<org.episteme.core.mathematics.numbers.real.Real> scale(org.episteme.core.mathematics.numbers.real.Real scalar, Matrix<org.episteme.core.mathematics.numbers.real.Real> a) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": scale() not available");
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
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": Matrix-Vector multiply() not available");
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
