/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.linearalgebra.matrices;

import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.linearalgebra.matrices.storage.TiledMatrixStorage;
import org.jscience.core.ComputeContext;
import org.jscience.core.mathematics.sets.Reals;


/**
 * A matrix decomposed into smaller tiles (blocks).
 * This structure is optimized for cache-local computations and distributed processing.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public class TiledMatrix extends GenericMatrix<Real> implements AutoCloseable {

    private final Matrix<Real>[][] tiles;
    private final int tileRows;
    private final int tileCols;
    private final int rows;
    private final int cols;
    private final int tileSize;

    /**
     * Creates a TiledMatrix from an existing matrix.
     */
    @SuppressWarnings("unchecked")
    public TiledMatrix(Matrix<Real> original, int tileRows, int tileCols) {
        super(new TiledMatrixStorage(new Matrix[(original.rows() + tileRows - 1) / tileRows][(original.cols() + tileCols - 1) / tileCols],
                original.rows(), original.cols(), tileRows, tileCols),
                ComputeContext.current().getDenseLinearAlgebraProvider(Reals.getInstance()),
                Reals.getInstance());
        
        this.rows = original.rows();
        this.cols = original.cols();
        this.tileRows = tileRows;
        this.tileCols = tileCols;
        this.tileSize = tileRows; // Assuming square tiles for simplicity

        int numTileRows = (rows + tileRows - 1) / tileRows;
        int numTileCols = (cols + tileCols - 1) / tileCols;
        this.tiles = new Matrix[numTileRows][numTileCols];

        for (int i = 0; i < numTileRows; i++) {
            for (int j = 0; j < numTileCols; j++) {
                int rStart = i * tileRows;
                int rEnd = Math.min(rStart + tileRows, rows);
                int cStart = j * tileCols;
                int cEnd = Math.min(cStart + tileCols, cols);
                tiles[i][j] = original.getSubMatrix(rStart, rEnd, cStart, cEnd);
            }
        }
        
        // Re-initialize storage with actual tiles (since we passed an empty array to super)
        this.storage = new TiledMatrixStorage(tiles, rows, cols, tileRows, tileCols);
    }

    /**
     * Creates an empty TiledMatrix with specified dimensions.
     */
    @SuppressWarnings("unchecked")
    public TiledMatrix(int rows, int cols, int tileSize) {
        super(new TiledMatrixStorage(new Matrix[(rows + tileSize - 1) / tileSize][(cols + tileSize - 1) / tileSize],
                rows, cols, tileSize, tileSize),
                ComputeContext.current().getDenseLinearAlgebraProvider(Reals.getInstance()),
                Reals.getInstance());
        
        this.rows = rows;
        this.cols = cols;
        this.tileRows = tileSize;
        this.tileCols = tileSize;
        this.tileSize = tileSize;

        int numTileRows = (rows + tileSize - 1) / tileSize;
        int numTileCols = (cols + tileSize - 1) / tileSize;
        this.tiles = new Matrix[numTileRows][numTileCols];

        // Initialize with zero matrices
        for (int i = 0; i < numTileRows; i++) {
            for (int j = 0; j < numTileCols; j++) {
                int rSize = Math.min(tileSize, rows - i * tileSize);
                int cSize = Math.min(tileSize, cols - j * tileSize);
                tiles[i][j] = GenericMatrix.of(new Real[rSize][cSize], Reals.getInstance());
            }
        }
        
        this.storage = new TiledMatrixStorage(tiles, rows, cols, tileSize, tileSize);
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
        int tileI = row / tileRows;
        int tileJ = col / tileCols;
        int localI = row % tileRows;
        int localJ = col % tileCols;
        return tiles[tileI][tileJ].get(localI, localJ);
    }

    public void set(int row, int col, Real value) {
        int tileI = row / tileRows;
        int tileJ = col / tileCols;
        int localI = row % tileRows;
        int localJ = col % tileCols;
        // GenericMatrix usually doesn't expose set? We need to cast or assume implementation.
        // Assuming tiles are GenericMatrix or MutableMatrix.
        // But Matrix interface doesn't have set() in JScience 5? 
        // Wait, MMapMatrix implemented it but it wasn't in interface (no @Override).
        // Let's implement it here properly.
        Matrix<Real> tile = tiles[tileI][tileJ];
        if (tile instanceof GenericMatrix) {
             ((GenericMatrix<Real>) tile).set(localI, localJ, value);
        } else {
             throw new UnsupportedOperationException("Tile is immutable or does not support set.");
        }
    }

    public Matrix<Real> getTile(int i, int j) {
        return tiles[i][j];
    }

    public int getNumTileRows() {
        return tiles.length;
    }

    public int getNumTileCols() {
        return tiles[0].length;
    }

    public int getTileSize() {
        return tileSize;
    }

    public void setTile(int i, int j, Matrix<Real> tile) {
        tiles[i][j] = tile;
    }

    /**
     * Internal constructor for sub-matrices.
     */
    private TiledMatrix(Matrix<Real>[][] tiles, int rows, int cols, int tileRows, int tileCols) {
        super(new TiledMatrixStorage(tiles, rows, cols, tileRows, tileCols),
                ComputeContext.current().getDenseLinearAlgebraProvider(Reals.getInstance()),
                Reals.getInstance());
        this.tiles = tiles;
        this.rows = rows;
        this.cols = cols;
        this.tileRows = tileRows;
        this.tileCols = tileCols;
        this.tileSize = tileRows;
    }

    /**
     * Returns a sub-tiled matrix representing a portion of this tiled matrix.
     * Dimensions are in terms of tiles.
     */
    @SuppressWarnings("unchecked")
    public TiledMatrix getSubTiledMatrix(int startTileRow, int endTileRow, int startTileCol, int endTileCol) {
        int numRows = endTileRow - startTileRow;
        int numCols = endTileCol - startTileCol;
        Matrix<Real>[][] subTiles = new Matrix[numRows][numCols];
        
        int totalRows = 0;
        int totalCols = 0;
        
        for (int i = 0; i < numRows; i++) {
            subTiles[i] = new Matrix[numCols];
            int currentRowRows = 0;
            for (int j = 0; j < numCols; j++) {
                subTiles[i][j] = tiles[startTileRow + i][startTileCol + j];
                if (i == 0) totalCols += subTiles[i][j].cols();
                currentRowRows = subTiles[i][j].rows();
            }
            totalRows += currentRowRows;
        }
        
        return new TiledMatrix(subTiles, totalRows, totalCols, tileRows, tileCols);
    }

    @Override
    public void close() {
        for (Matrix<Real>[] row : tiles) {
            for (Matrix<Real> tile : row) {
                if (tile instanceof AutoCloseable) {
                    try {
                        ((AutoCloseable) tile).close();
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }
        }
    }
}

