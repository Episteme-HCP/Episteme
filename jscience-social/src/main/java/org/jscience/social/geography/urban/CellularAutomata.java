/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.social.geography.urban;

/**
 * A generalized Cellular Automata model for simulating urban growth, land use change,
 * or traffic flow.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CellularAutomata {

    private final int width;
    private final int height;
    private int[][] grid;

    public CellularAutomata(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new int[width][height];
    }
    
    public void setState(int x, int y, int state) {
        if (isValid(x, y)) {
            grid[x][y] = state;
        }
    }
    
    public int getState(int x, int y) {
        if (isValid(x, y)) {
            return grid[x][y];
        }
        return 0; // Default boundary condition
    }
    
    public void step(Rule rule) {
        int[][] newGrid = new int[width][height];
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                newGrid[x][y] = rule.apply(x, y, this);
            }
        }
        
        this.grid = newGrid;
    }
    
    private boolean isValid(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
    
    /**
     * Functional interface for CA transition rules.
     */
    @FunctionalInterface
    public interface Rule {
        int apply(int x, int y, CellularAutomata ca);
    }
}
