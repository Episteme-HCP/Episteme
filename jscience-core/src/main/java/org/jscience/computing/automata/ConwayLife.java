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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jscience.computing.automata;

/**
 * Conway's Game of Life Implementation.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ConwayLife implements CellularAutomaton<Boolean> {

    private final int width;
    private final int height;
    private boolean[][] grid;
    private boolean[][] nextGrid;

    public ConwayLife(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new boolean[width][height];
        this.nextGrid = new boolean[width][height];
    }

    @Override
    public void nextGeneration() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int neighbors = countNeighbors(x, y);
                if (grid[x][y]) {
                    nextGrid[x][y] = (neighbors == 2 || neighbors == 3);
                } else {
                    nextGrid[x][y] = (neighbors == 3);
                }
            }
        }
        // Swap grids
        boolean[][] temp = grid;
        grid = nextGrid;
        nextGrid = temp;
    }

    private int countNeighbors(int x, int y) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0)
                    continue;
                int nx = (x + i + width) % width; // Wrap around
                int ny = (y + j + height) % height;
                if (grid[nx][ny])
                    count++;
            }
        }
        return count;
    }

    @Override
    public Boolean getState(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return false;
        return grid[x][y];
    }

    @Override
    public void setState(int x, int y, Boolean state) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            grid[x][y] = state;
        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
