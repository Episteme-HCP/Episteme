/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativelib.linearalgebra;

import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.linearalgebra.matrices.GenericMatrix;
import org.jscience.core.mathematics.linearalgebra.matrices.MatrixFactory;
import org.jscience.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage;
import org.jscience.core.mathematics.linearalgebra.matrices.storage.HeapRealDoubleMatrixStorage;
import org.jscience.core.mathematics.linearalgebra.matrices.storage.MatrixStorage;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.technical.backend.ComputeBackend;
import org.jscience.core.technical.backend.ComputeBackend.ComputeCapability;
import org.jscience.core.technical.backend.ComputeContext;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Collections;
import java.util.Set;

/**
 * Native BLAS implementation of Linear Algebra Provider.
 * <p>
 * Connects to native libraries (OpenBLAS, MKL) via Project Panama (FFM API)
 * to perform high-performance matrix operations.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class NativeLinearAlgebraProvider implements LinearAlgebraProvider<Real> {

    private static final SymbolLookup linker = Linker.nativeLinker().defaultLookup();
    private static final Linker nativeLinker = Linker.nativeLinker();
    
    private MethodHandle dgemm;
    private MethodHandle dscal;
    private MethodHandle daxpy;
    private MethodHandle dgetrf;
    private MethodHandle dgetri;
    private MethodHandle dgesv;
    private boolean available = false;

    public NativeLinearAlgebraProvider() {
        try {
            // Attempt to look for 'cblas_dgemm' or 'dgemm_'
            MemorySegment dgemmAddress = linker.find("cblas_dgemm")
                                            .orElse(linker.find("dgemm_").orElse(null));
                                            
            if (dgemmAddress != null) {
                // Define the signature of dgemm: (int, int, int, int, int, int, double, ptr, int, ptr, int, double, ptr, int) -> void
                dgemm = nativeLinker.downcallHandle(
                    dgemmAddress, 
                    FunctionDescriptor.ofVoid(
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
                        ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE,
                        ValueLayout.ADDRESS, ValueLayout.JAVA_INT
                    )
                );

                MemorySegment dscalAddress = linker.find("cblas_dscal").orElse(null);
                if (dscalAddress != null) {
                    dscal = nativeLinker.downcallHandle(
                        dscalAddress,
                        FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS, ValueLayout.JAVA_INT)
                    );
                }

                MemorySegment daxpyAddress = linker.find("cblas_daxpy").orElse(null);
                if (daxpyAddress != null) {
                    daxpy = nativeLinker.downcallHandle(
                        daxpyAddress,
                        FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT)
                    );
                }

                MemorySegment dgetrfAddress = linker.find("LAPACKE_dgetrf").orElse(null);
                if (dgetrfAddress != null) {
                    dgetrf = nativeLinker.downcallHandle(dgetrfAddress, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                }

                MemorySegment dgetriAddress = linker.find("LAPACKE_dgetri").orElse(null);
                if (dgetriAddress != null) {
                    dgetri = nativeLinker.downcallHandle(dgetriAddress, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                }

                MemorySegment dgesvAddress = linker.find("LAPACKE_dgesv").orElse(null);
                if (dgesvAddress != null) {
                    dgesv = nativeLinker.downcallHandle(dgesvAddress, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                }
                
                available = true;
                System.out.println("Native BLAS/LAPACK routines initialized.");
            } else {
                System.out.println("Native BLAS not found in default path.");
            }
            
        } catch (Throwable t) {
            System.err.println("Failed to initialize Native BLAS: " + t.getMessage());
        }
    }

    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getName() {
        return "NativeBLAS";
    }

    @Override
    public ComputeContext createContext() {
        // Return a context with Native capabilities
        // Simplified for now
        return ComputeContext.current(); 
    }

    // --- Matrix Operations ---

    @Override
    public Matrix<Real> multiply(Matrix<Real> A, Matrix<Real> B) {
        if (!available || dgemm == null) throw new UnsupportedOperationException("Native BLAS not available");
        
        int m = A.rows();
        int n = B.cols();
        int k = A.cols();
        if (k != B.rows()) throw new IllegalArgumentException("Dimension mismatch");
        
        double[] aData = flatten(A);
        double[] bData = flatten(B);
        double[] cData = new double[m * n];
        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment addrA = arena.allocate(m * k * 8L);
            MemorySegment addrB = arena.allocate(k * n * 8L);
            MemorySegment addrC = arena.allocate(m * n * 8L);

            MemorySegment.copy(MemorySegment.ofArray(aData), 0, addrA, 0, aData.length * 8L);
            MemorySegment.copy(MemorySegment.ofArray(bData), 0, addrB, 0, bData.length * 8L);
            
            // cblas_dgemm(CblasRowMajor=101, NoTrans=111, NoTrans=111, ...)
            dgemm.invokeExact(
                101, 111, 111, 
                m, n, k, 
                1.0, addrA, k, 
                addrB, n,      
                0.0, addrC, n 
            );
            
            MemorySegment.copy(addrC, 0, MemorySegment.ofArray(cData), 0, cData.length * 8L);
            
            return new GenericMatrix<Real>(
                new HeapRealDoubleMatrixStorage(cData, m, n),
                this, 
                org.jscience.core.mathematics.sets.Reals.getInstance()
            );
            
        } catch (Throwable e) {
             throw new RuntimeException("Native Mul Failed", e);
        }
    }
    
    private double[] flatten(Matrix<Real> M) {
        int rows = M.rows();
        int cols = M.cols();
        double[] data = new double[rows * cols];
         for (int i = 0; i < rows; i++) 
            for (int j = 0; j < cols; j++) 
                data[i * cols + j] = M.get(i, j).doubleValue();
        return data; 
    }

    @Override
    public Matrix<Real> add(Matrix<Real> A, Matrix<Real> B) {
        // Fallback or explicit loop (BLAS might only help if using DAXPY row by row)
        // For simplicity, naive generic implementation or pure Java optimized loop
        return new GenericMatrix<>(getAsDefaultStorage(A), this, org.jscience.core.mathematics.sets.Reals.getInstance())
               .add(B); 
    }

    private MatrixStorage<Real> getAsDefaultStorage(Matrix<Real> m) {
        if (m instanceof GenericMatrix) {
            return ((GenericMatrix<Real>) m).getStorage();
        }
        // Fallback: create fresh storage
        // This is inefficient but functional for the fallback path
        DenseMatrixStorage<Real> s = new DenseMatrixStorage<>(m.rows(), m.cols(), org.jscience.core.mathematics.sets.Reals.getInstance().zero());
        for(int i=0; i<m.rows(); i++)
            for(int j=0; j<m.cols(); j++)
                s.set(i, j, m.get(i,j));
        return s;
    }

    @Override
    public Matrix<Real> subtract(Matrix<Real> A, Matrix<Real> B) {
         return new GenericMatrix<>(getAsDefaultStorage(A), this, org.jscience.core.mathematics.sets.Reals.getInstance())
               .subtract(B);
    }

    @Override
    public Matrix<Real> inverse(Matrix<Real> matrix) {
        if (!available || dgetrf == null || dgetri == null) {
            throw new UnsupportedOperationException("Native LAPACK (LU) not available for inverse");
        }

        // We assume GenericMatrix with HeapRealDoubleMatrixStorage for native ops
        if (!(matrix instanceof GenericMatrix)) {
             throw new UnsupportedOperationException("Native inverse only supported for GenericMatrix");
        }
        
        MatrixStorage<Real> storage = ((GenericMatrix<Real>) matrix).getStorage();
        if (!(storage instanceof HeapRealDoubleMatrixStorage)) {
            throw new UnsupportedOperationException("Native inverse only supported for HeapRealDoubleMatrixStorage");
        }

        double[] data = ((HeapRealDoubleMatrixStorage) storage).getData().clone();
        int n = storage.rows();

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment matSeg = arena.allocate(data.length * 8L);
            MemorySegment.copy(MemorySegment.ofArray(data), 0, matSeg, 0, data.length * 8L);
            MemorySegment ipivSeg = arena.allocate(n * 4L); // int pivots

            // 1. LU Decomposition
            // dgetrf(matrix_layout, m, n, a, lda, ipiv)
            // 101 = LAPACK_ROW_MAJOR
            int res1 = (int) dgetrf.invokeExact(101, n, n, matSeg, n, ipivSeg);
            if (res1 != 0) throw new RuntimeException("LAPACK dgetrf failed with code " + res1);

            // 2. Inversion from LU
            // dgetri(matrix_layout, n, a, lda, ipiv)
            int res2 = (int) dgetri.invokeExact(101, n, matSeg, n, ipivSeg);
            if (res2 != 0) throw new RuntimeException("LAPACK dgetri failed with code " + res2);

            double[] resultData = new double[data.length];
            MemorySegment.copy(matSeg, 0, MemorySegment.ofArray(resultData), 0, data.length * 8L);

            return new GenericMatrix<Real>(
                (MatrixStorage<Real>) new HeapRealDoubleMatrixStorage(resultData, n, n),
                (org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider<Real>) this,
                org.jscience.core.mathematics.sets.Reals.getInstance()
            );
        } catch (Throwable t) {
            throw new RuntimeException("Native inverse failed", t);
        }
    }

    @Override
    public Real determinant(Matrix<Real> matrix) {
        throw new UnsupportedOperationException("Native Determinant not yet implemented");
    }

    @Override
    public Vector<Real> solve(Matrix<Real> A, Vector<Real> B) {
        // Wrap vector B into a matrix with 1 column
        double[] dataB = new double[B.getDimension()];
        for(int i=0; i<dataB.length; i++) dataB[i] = B.get(i).doubleValue();
        
        Matrix<Real> matB = new GenericMatrix<Real>(
            new HeapRealDoubleMatrixStorage(dataB, dataB.length, 1),
            this,
            org.jscience.core.mathematics.sets.Reals.getInstance()
        );
        
        Matrix<Real> solution = solve(A, matB);
        
        // Extract result column as vector
        double[] solData = new double[A.cols()];
        MatrixStorage<Real> solStorage = ((GenericMatrix<Real>) solution).getStorage();
        double[] rawSol = ((HeapRealDoubleMatrixStorage) solStorage).getData();
        System.arraycopy(rawSol, 0, solData, 0, solData.length);
        
        return new org.jscience.core.mathematics.linearalgebra.vectors.DenseVector<Real>(
            new org.jscience.core.mathematics.linearalgebra.vectors.storage.HeapRealDoubleVectorStorage(solData)
        );
    }

    @Override
    public Matrix<Real> solve(Matrix<Real> A, Matrix<Real> B) {
        if (!available || dgesv == null) {
            throw new UnsupportedOperationException("Native LAPACK (dgesv) not available for solve");
        }

        if (!(A instanceof GenericMatrix) || !(B instanceof GenericMatrix)) {
            throw new UnsupportedOperationException("Native solve only supported for GenericMatrix");
        }

        MatrixStorage<Real> storageA = ((GenericMatrix<Real>) A).getStorage();
        MatrixStorage<Real> storageB = ((GenericMatrix<Real>) B).getStorage();

        if (!(storageA instanceof HeapRealDoubleMatrixStorage) || !(storageB instanceof HeapRealDoubleMatrixStorage)) {
            throw new UnsupportedOperationException("Native solve only supported for HeapRealDoubleMatrixStorage");
        }

        double[] dataA = ((HeapRealDoubleMatrixStorage) storageA).getData().clone();
        double[] dataB = ((HeapRealDoubleMatrixStorage) storageB).getData().clone();
        
        int n = storageA.rows();
        int nrhs = storageB.cols();

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment segA = arena.allocate(dataA.length * 8L);
            MemorySegment.copy(MemorySegment.ofArray(dataA), 0, segA, 0, dataA.length * 8L);
            
            MemorySegment segB = arena.allocate(dataB.length * 8L);
            MemorySegment.copy(MemorySegment.ofArray(dataB), 0, segB, 0, dataB.length * 8L);

            MemorySegment ipivSeg = arena.allocate(n * 4L);

            // dgesv(matrix_layout, n, nrhs, a, lda, ipiv, b, ldb)
            // 101 = LAPACK_ROW_MAJOR
            int res = (int) dgesv.invokeExact(101, n, nrhs, segA, n, ipivSeg, segB, nrhs);
            
            if (res != 0) throw new RuntimeException("LAPACK dgesv failed with code " + res);

            double[] resultData = new double[dataB.length];
            MemorySegment.copy(segB, 0, MemorySegment.ofArray(resultData), 0, dataB.length * 8L);

            return new GenericMatrix<Real>(
                (org.jscience.core.mathematics.linearalgebra.matrices.storage.MatrixStorage<Real>) new HeapRealDoubleMatrixStorage(resultData, n, nrhs),
                (org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider<Real>) this,
                org.jscience.core.mathematics.sets.Reals.getInstance()
            );
        } catch (Throwable t) {
            throw new RuntimeException("Native solve failed", t);
        }
    }
    
    @Override
    public Matrix<Real> transpose(Matrix<Real> matrix) {
        // Naive transpose for now or specific memory copy
        int rows = matrix.rows();
        int cols = matrix.cols();
        double[] tData = new double[rows*cols];
        for(int i=0; i<rows; i++) {
            for(int j=0; j<cols; j++) {
                tData[j*rows + i] = matrix.get(i,j).doubleValue();
            }
        }
        return new GenericMatrix<Real>(
                new HeapRealDoubleMatrixStorage(tData, cols, rows),
                this, 
                org.jscience.core.mathematics.sets.Reals.getInstance()
            );
    }

    // --- Vector Operations ---

    @Override
    public Vector<Real> multiply(Matrix<Real> matrix, Vector<Real> vector) {
        // DGEMV could be used here
        // fallback
        return new GenericMatrix<Real>(
            (org.jscience.core.mathematics.linearalgebra.matrices.storage.MatrixStorage<Real>) getAsDefaultStorage(matrix), 
            (org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider<Real>) this, 
            org.jscience.core.mathematics.sets.Reals.getInstance()
        ).multiply(vector);
    }
    
    @Override
    public Vector<Real> multiply(Vector<Real> vector, Real scalar) {
         return vector.scale(scalar); 
    }

    @Override
    public Real dot(Vector<Real> v1, Vector<Real> v2) {
        // DDOT
        double sum = 0;
        int n = v1.getDimension();
        for(int i=0; i<n; i++) sum += v1.get(i).doubleValue() * v2.get(i).doubleValue();
        return Real.of(sum);
    }

    @Override
    public Vector<Real> add(Vector<Real> v1, Vector<Real> v2) {
        throw new UnsupportedOperationException("Native Vector Add not implemented");
    }

    @Override
    public Vector<Real> subtract(Vector<Real> v1, Vector<Real> v2) {
        throw new UnsupportedOperationException("Native Vector Sub not implemented");
    }

    @Override
    public Matrix<Real> scale(Real scalar, GenericMatrix<Real> element) {
        if (!available || dscal == null) return manualScale(scalar, element);
        
        MatrixStorage<Real> storage = element.getStorage();
        if (storage instanceof HeapRealDoubleMatrixStorage) {
            double[] data = ((HeapRealDoubleMatrixStorage) storage).getData();
            double factor = scalar.doubleValue();
            
            try {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment seg = arena.allocate(data.length * 8L);
                    MemorySegment.copy(MemorySegment.ofArray(data), 0, seg, 0, data.length * 8L);
                    
                    // dscal(N, alpha, X, incX)
                    dscal.invokeExact(data.length, factor, seg, 1);
                    
                    double[] resultData = new double[data.length];
                    MemorySegment.copy(seg, 0, MemorySegment.ofArray(resultData), 0, data.length * 8L);
                    
                    return new GenericMatrix<Real>(
                        (org.jscience.core.mathematics.linearalgebra.matrices.storage.MatrixStorage<Real>) new HeapRealDoubleMatrixStorage(resultData, storage.rows(), storage.cols()),
                        (org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider<Real>) this,
                        org.jscience.core.mathematics.sets.Reals.getInstance()
                    );
                }
            } catch (Throwable t) {
                return (Matrix<Real>) manualScale(scalar, element);
            }
        }
        return (Matrix<Real>) manualScale(scalar, element);
    }
    
    private Matrix<Real> manualScale(Real scalar, GenericMatrix<Real> element) {
         MatrixStorage<Real> st = element.getStorage();
         DenseMatrixStorage<Real> newSt = new DenseMatrixStorage<>(st.rows(), st.cols(), org.jscience.core.mathematics.sets.Reals.getInstance().zero());
          for(int i=0; i<st.rows(); i++)
            for(int j=0; j<st.cols(); j++)
                newSt.set(i, j, st.get(i,j).multiply(scalar));
          return new GenericMatrix<>(newSt, this, org.jscience.core.mathematics.sets.Reals.getInstance());
    }

    // Required by interface for generic usage
    public Matrix<Real> scale(Real scalar, Matrix<Real> element) {
        if (element instanceof GenericMatrix) {
            return scale(scalar, (GenericMatrix<Real>) element);
        }
        // fallback
        return null; // Should assume GenericMatrix is the main impl
    }
}
