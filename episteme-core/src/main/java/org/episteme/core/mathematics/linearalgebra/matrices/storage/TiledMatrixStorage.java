/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra.matrices.storage;

import org.episteme.core.mathematics.linearalgebra.Matrix;

/**
 * Storage implementation for tiled matrices.
 * <p>
 * This storage organizes matrix data into smaller tiles (blocks) for improved
 * cache locality and parallel processing efficiency.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public class TiledMatrixStorage<E> implements MatrixStorage<E> {

    private final Matrix<E>[][] tiles;
    private final int rows;
    private final int cols;
    private final int tileRows;
    private final int tileCols;

    /**
     * Creates a tiled matrix storage.
     *
     * @param tiles 2D array of matrix tiles
     * @param rows Total number of rows
     * @param cols Total number of columns
     * @param tileRows Rows per tile
     * @param tileCols Columns per tile
     */
    public TiledMatrixStorage(Matrix<E>[][] tiles, int rows, int cols, 
                             int tileRows, int tileCols) {
        this.tiles = tiles;
        this.rows = rows;
        this.cols = cols;
        this.tileRows = tileRows;
        this.tileCols = tileCols;
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
    public E get(int row, int col) {
        int tileI = row / tileRows;
        int tileJ = col / tileCols;
        int localI = row % tileRows;
        int localJ = col % tileCols;
        
        if (tiles[tileI][tileJ] == null) {
            return null; // Should be handled by caller or initialized
        }
        
        return tiles[tileI][tileJ].get(localI, localJ);
    }

    @Override
    public void set(int row, int col, E value) {
        throw new UnsupportedOperationException("TiledMatrixStorage does not support direct element mutation. Use setTile() instead.");
    }

    /**
     * Gets a tile at the specified position.
     *
     * @param tileRow Tile row index
     * @param tileCol Tile column index
     * @return The tile matrix
     */
    public Matrix<E> getTile(int tileRow, int tileCol) {
        return tiles[tileRow][tileCol];
    }

    /**
     * Sets a tile at the specified position.
     *
     * @param tileRow Tile row index
     * @param tileCol Tile column index
     * @param tile The tile matrix to set
     */
    public void setTile(int tileRow, int tileCol, Matrix<E> tile) {
        tiles[tileRow][tileCol] = tile;
    }

    @Override
    public MatrixStorage<E> clone() {
        @SuppressWarnings("unchecked")
        Matrix<E>[][] newTiles = (Matrix<E>[][]) new Matrix[tiles.length][tiles[0].length];
        
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] != null) {
                    // Shallow copy of tiles (matrices are typically immutable)
                    newTiles[i][j] = tiles[i][j];
                }
            }
        }
        
        return new TiledMatrixStorage<E>(newTiles, rows, cols, tileRows, tileCols);
    }
}

