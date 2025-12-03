/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.vector.backend;

import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.Matrix;
import org.jscience.mathematics.vector.Vector;
import org.jscience.mathematics.vector.DenseMatrix;
import org.jscience.mathematics.vector.DenseVector;
import org.jscience.mathematics.vector.backend.CudaStorage;
import org.jscience.mathematics.vector.backend.CudaLinearAlgebraProvider;
import org.jscience.mathematics.sets.Reals;

import java.util.ArrayList;
import java.util.List;

import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.jcublas.JCublas;

/**
 * GPU-accelerated Matrix implementation using CUDA.
 * <p>
 * Data is stored in GPU memory. Operations are performed on the GPU.
 * Moving data between CPU and GPU is expensive, so use this class for
 * computationally intensive operations where data stays on the GPU.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class CudaMatrix implements Matrix<Real>, AutoCloseable {

    private final int rows;
    private final int cols;
    private final CudaStorage storage;
    private final CudaLinearAlgebraProvider<Real> provider;

    /**
     * Creates a new CudaMatrix from a 2D array of Reals.
     * 
     * @param data the data
     */
    public CudaMatrix(Real[][] data) {
        this.rows = data.length;
        this.cols = data[0].length;
        this.provider = new CudaLinearAlgebraProvider<>(Reals.getInstance());

        // Flatten data
        double[] flatData = new double[rows * cols];
        for (int j = 0; j < cols; j++) {
            for (int i = 0; i < rows; i++) {
                // Column-major order for CUBLAS
                flatData[j * rows + i] = data[i][j].doubleValue();
            }
        }

        this.storage = new CudaStorage(flatData.length);
        this.storage.upload(flatData);
    }

    /**
     * Creates a new CudaMatrix from an existing CudaStorage.
     * 
     * @param rows    number of rows
     * @param cols    number of columns
     * @param storage the GPU storage
     */
    public CudaMatrix(int rows, int cols, CudaStorage storage) {
        this.rows = rows;
        this.cols = cols;
        this.storage = storage;
        this.provider = new CudaLinearAlgebraProvider<>(Reals.getInstance());
    }

    public CudaStorage getStorage() {
        return storage;
    }

    @Override
    public int rows() {
        return rows;
    }

    @Override
    public int cols() {
        return cols;
    }

    @Override
    public Real get(int row, int col) {
        // Calculate index in column-major format
        int index = col * rows + row;

        double[] element = new double[1];
        // Fetch single element: pointer + index * sizeof(double)
        Pointer p = storage.getPointer().withByteOffset(index * Sizeof.DOUBLE);
        JCublas.cublasGetVector(1, Sizeof.DOUBLE, p, 1, Pointer.to(element), 1);

        return Real.of(element[0]);
    }

    @Override
    public Matrix<Real> add(Matrix<Real> other) {
        return provider.add(this, other);
    }

    @Override
    public Matrix<Real> subtract(Matrix<Real> other) {
        return provider.subtract(this, other);
    }

    @Override
    public Matrix<Real> multiply(Matrix<Real> other) {
        return provider.multiply(this, other);
    }

    @Override
    public Vector<Real> multiply(Vector<Real> vector) {
        return provider.multiply(this, vector);
    }

    @Override
    public Matrix<Real> inverse() {
        return provider.inverse(this);
    }

    @Override
    public Real determinant() {
        return provider.determinant(this);
    }

    @Override
    public Matrix<Real> negate() {
        // GPU-side negation: scale by -1.0
        return scale(Real.of(-1.0), this);
    }

    @Override
    public Matrix<Real> transpose() {
        int m = rows;
        int n = cols;
        // CudaStorage resultStorage = new CudaStorage(n * m); // Unused

        // cublasDgeam(handle, transa, transb, m, n, alpha, A, lda, beta, B, ldb, C,
        // ldc)
        // C = alpha*op(A) + beta*op(B)
        // To transpose A: C = 1.0 * A^T + 0.0 * A^T
        // transa = CUBLAS_OP_T

        // Note: JCublas.cublasDgeam is not available in all JCuda versions directly or
        // might have different signature.
        // Checking common JCublas2 signature:
        // cublasDgeam(cublasHandle handle, int transa, int transb, int m, int n,
        // Pointer alpha, Pointer A, int lda, Pointer beta, Pointer B, int ldb, Pointer
        // C, int ldc)

        // Since we are using JCublas (v1) in other parts (static methods), we check
        // availability.
        // JCublas v1 does not have cublasDgeam. We might need to use a custom kernel or
        // download/upload if v1 is strict.
        // However, standard BLAS has xGEMM. We can transpose using xGEMM: C = A^T * I
        // is not quite right.
        // Actually, for CudaMatrix (column-major), we can just treat it as row-major
        // with swapped dimensions?
        // No, that changes the interpretation of elements.

        // If cublasDgeam is missing in JCublas1, we can use a simple kernel.
        // Or we can use the provider which might have better access.
        // Let's delegate to provider for transpose if complex.
        // But wait, the plan said "Implement transpose() using cublasDgeam".
        // Assuming we can access it or upgrade to JCublas2.
        // Looking at CudaStorage, it uses `JCublas.cublasAlloc`, which is v1.
        // v1 is deprecated. We should ideally use v2.
        // But for now, let's stick to what compiles.
        // If Dgeam is not there, we fallback to provider or CPU.

        // Let's try to implement a simple transpose kernel string here or use provider.
        // Actually, the provider is better suited for this.
        return provider.transpose(this);
    }

    @Override
    public Matrix<Real> zero() {
        // Return zero matrix of same size
        CudaStorage zeroStorage = new CudaStorage(rows * cols);
        // cudaMemset is available in JCuda driver/runtime
        jcuda.runtime.JCuda.cudaMemset(zeroStorage.getPointer(), 0, rows * cols * jcuda.Sizeof.DOUBLE);
        return new CudaMatrix(rows, cols, zeroStorage);
    }

    @Override
    public Matrix<Real> scale(Real scalar, Matrix<Real> element) {
        if (!(element instanceof CudaMatrix)) {
            return element.scale(scalar, element);
        }
        CudaMatrix cudaMat = (CudaMatrix) element;
        int size = cudaMat.rows() * cudaMat.cols();
        CudaStorage resultStorage = new CudaStorage(size);

        // Copy original
        jcuda.jcublas.JCublas.cublasDcopy(size, cudaMat.getStorage().getPointer(), 1, resultStorage.getPointer(), 1);

        // Scale in place
        double alpha = scalar.doubleValue();
        jcuda.jcublas.JCublas.cublasDscal(size, alpha, resultStorage.getPointer(), 1);

        return new CudaMatrix(cudaMat.rows(), cudaMat.cols(), resultStorage);
    }

    @Override
    public Matrix<Real> getSubMatrix(int rowStart, int rowEnd, int colStart, int colEnd) {
        int newRows = rowEnd - rowStart;
        int newCols = colEnd - colStart;

        if (newRows <= 0 || newCols <= 0) {
            throw new IllegalArgumentException("Invalid submatrix dimensions");
        }

        CudaStorage newStorage = new CudaStorage(newRows * newCols);

        // Copy column by column
        for (int j = 0; j < newCols; j++) {
            int srcCol = colStart + j;
            int srcIndex = srcCol * rows + rowStart;
            int destIndex = j * newRows; // Dest is (0, j) in new matrix

            Pointer srcPtr = storage.getPointer().withByteOffset(srcIndex * Sizeof.DOUBLE);
            Pointer destPtr = newStorage.getPointer().withByteOffset(destIndex * Sizeof.DOUBLE);

            // Copy 'newRows' elements from src column to dest column
            JCublas.cublasDcopy(newRows, srcPtr, 1, destPtr, 1);
        }

        return new CudaMatrix(newRows, newCols, newStorage);
    }

    @Override
    public Vector<Real> getRow(int row) {
        double[] rowData = new double[cols];

        // Elements are at: row, row+rows, row+2*rows...
        // Stride is 'rows'
        Pointer srcPtr = storage.getPointer().withByteOffset(row * Sizeof.DOUBLE);
        JCublas.cublasGetVector(cols, Sizeof.DOUBLE, srcPtr, rows, Pointer.to(rowData), 1);

        List<Real> elements = new ArrayList<>(cols);
        for (double val : rowData) {
            elements.add(Real.of(val));
        }
        return new DenseVector<>(elements, Reals.getInstance());
    }

    @Override
    public Vector<Real> getColumn(int col) {
        double[] colData = new double[rows];

        // Elements are contiguous starting at col*rows
        Pointer srcPtr = storage.getPointer().withByteOffset(col * rows * Sizeof.DOUBLE);
        JCublas.cublasGetVector(rows, Sizeof.DOUBLE, srcPtr, 1, Pointer.to(colData), 1);

        List<Real> elements = new ArrayList<>(rows);
        for (double val : colData) {
            elements.add(Real.of(val));
        }
        return new DenseVector<>(elements, Reals.getInstance());
    }

    @Override
    public Real trace() {
        int n = Math.min(rows, cols);
        double[] diagonal = new double[n];

        // Diagonal elements are at 0, rows+1, 2*(rows+1)...
        // Stride is rows + 1
        JCublas.cublasGetVector(n, Sizeof.DOUBLE, storage.getPointer(), rows + 1, Pointer.to(diagonal), 1);

        double sum = 0;
        for (double val : diagonal) {
            sum += val;
        }
        return Real.of(sum);
    }

    @Override
    public Matrix<Real> one() {
        CudaStorage newStorage = new CudaStorage(rows * cols);

        // Set to zero
        jcuda.runtime.JCuda.cudaMemset(newStorage.getPointer(), 0, rows * cols * Sizeof.DOUBLE);

        // Set diagonal to 1
        int n = Math.min(rows, cols);
        double[] ones = new double[n];
        for (int i = 0; i < n; i++)
            ones[i] = 1.0;

        JCublas.cublasSetVector(n, Sizeof.DOUBLE, Pointer.to(ones), 1, newStorage.getPointer(), rows + 1);

        return new CudaMatrix(rows, cols, newStorage);
    }

    @Override
    public boolean isMultiplicationCommutative() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Matrix<Real> element) {
        return element == this;
    }

    @Override
    public String description() {
        return "CudaMatrix (" + rows + "x" + cols + ")";
    }

    @Override
    public void close() {
        storage.close();
    }

    /**
     * Converts this CudaMatrix to a CPU-based DenseMatrix.
     * 
     * @return the dense matrix
     */
    public DenseMatrix<Real> toDenseMatrix() {
        double[] flatData = storage.download();
        List<List<Real>> rowsList = new ArrayList<>(rows);

        for (int i = 0; i < rows; i++) {
            List<Real> row = new ArrayList<>(cols);
            for (int j = 0; j < cols; j++) {
                // Column-major to Row-major
                row.add(Real.of(flatData[j * rows + i]));
            }
            rowsList.add(row);
        }

        return new DenseMatrix<>(rowsList, Reals.getInstance());
    }

    // Helper to expose rows for DenseMatrix constructor if needed,
    // but DenseMatrix constructor takes List<List<E>>.
    // We can't easily implement 'getRows()' without downloading.
    @Override
    public org.jscience.mathematics.algebra.Ring<Real> getScalarRing() {
        return Reals.getInstance();
    }
}

