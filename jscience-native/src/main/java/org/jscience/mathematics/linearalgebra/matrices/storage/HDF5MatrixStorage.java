/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.mathematics.linearalgebra.matrices.storage;

import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.physics.loaders.hdf5.HDF5Reader;
import java.nio.file.Path;

/**
 * Matrix storage backed by HDF5 with hyperslab support for partial loading.
 * <p>
 * This storage allows working with matrices larger than available RAM by loading
 * only the required tiles from disk on-demand.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public class HDF5MatrixStorage implements MatrixStorage<Real> {

    private final Path hdf5Path;
    private final String datasetName;
    private final int rows;
    private final int cols;
    private HDF5Reader reader;

    /**
     * Creates HDF5-backed matrix storage.
     *
     * @param hdf5Path Path to HDF5 file
     * @param datasetName Name of dataset within file
     */
    public HDF5MatrixStorage(Path hdf5Path, String datasetName) {
        this.hdf5Path = hdf5Path;
        this.datasetName = datasetName;
        
        // Open reader to get dimensions
        try (HDF5Reader r = new HDF5Reader(hdf5Path)) {
            long[] dims = r.getDatasetDimensions(datasetName);
            if (dims == null || dims.length < 2) {
                throw new IllegalArgumentException("Dataset not found or invalid dimensions");
            }
            this.rows = (int) dims[0];
            this.cols = (int) dims[1];
        }
        
        // Keep persistent reader for tile access
        this.reader = new HDF5Reader(hdf5Path);
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
        // For single element access, load a 1x1 block
        org.jscience.mathematics.linearalgebra.matrices.NativeMatrix block = 
            new org.jscience.mathematics.linearalgebra.matrices.NativeMatrix(1, 1);
        reader.readBlock(datasetName, block, row, col, 1, 1);
        return org.jscience.mathematics.numbers.real.RealDouble.valueOf(block.get(0, 0).doubleValue());
    }

    @Override
    public void set(int row, int col, Real value) {
        throw new UnsupportedOperationException("HDF5MatrixStorage is read-only");
    }

    /**
     * Loads a rectangular block from the HDF5 file.
     *
     * @param startRow Starting row (0-indexed)
     * @param startCol Starting column (0-indexed)
     * @param rowCount Number of rows to load
     * @param colCount Number of columns to load
     * @return Matrix containing the loaded block
     */
    public Matrix<Real> loadBlock(int startRow, int startCol, int rowCount, int colCount) {
        org.jscience.mathematics.linearalgebra.matrices.NativeMatrix block = 
            new org.jscience.mathematics.linearalgebra.matrices.NativeMatrix(rowCount, colCount);
        reader.readBlock(datasetName, block, startRow, startCol, rowCount, colCount);
        return block;
    }

    @Override
    public void close() {
        if (reader != null) {
            reader.close();
            reader = null;
        }
    }

    @Override
    public MatrixStorage<Real> copy() {
        return new HDF5MatrixStorage(hdf5Path, datasetName);
    }
}
