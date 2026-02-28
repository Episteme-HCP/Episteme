/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.geography;

import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.Units;
import org.episteme.core.measure.quantity.Length;


/**
 * Utility class for terrain analysis using Digital Elevation Models (DEM).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class TerrainAnalyzer {

    private TerrainAnalyzer() {}

    /**
     * Calculates slope grid from elevation grid using Horn's method.
     * 
     * @param dem Digital Elevation Model (2D elevation array)
     * @param cellSize width/height of a grid cell
     * @return grid of slope values in degrees
     */
    public static double[][] calculateSlope(double[][] dem, Quantity<Length> cellSize) {
        int rows = dem.length;
        int cols = dem[0].length;
        double[][] slope = new double[rows][cols];
        double size = cellSize.to(Units.METER).getValue().doubleValue();
        
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                double dzdx = ((dem[i-1][j+1] + 2*dem[i][j+1] + dem[i+1][j+1]) -
                              (dem[i-1][j-1] + 2*dem[i][j-1] + dem[i+1][j-1])) / (8 * size);
                double dzdy = ((dem[i+1][j-1] + 2*dem[i+1][j] + dem[i+1][j+1]) -
                              (dem[i-1][j-1] + 2*dem[i-1][j] + dem[i-1][j+1])) / (8 * size);
                
                slope[i][j] = Math.toDegrees(Math.atan(Math.sqrt(dzdx*dzdx + dzdy*dzdy)));
            }
        }
        return slope;
    }

    /**
     * Calculates aspect (slope direction) grid.
     * 
     * @param dem elevation grid
     * @param cellSize cell dimension
     * @return aspect in degrees (0-360) from North
     */
    public static double[][] calculateAspect(double[][] dem, Quantity<Length> cellSize) {
        int rows = dem.length;
        int cols = dem[0].length;
        double[][] aspect = new double[rows][cols];
        double size = cellSize.to(Units.METER).getValue().doubleValue();
        
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                double dzdx = ((dem[i-1][j+1] + 2*dem[i][j+1] + dem[i+1][j+1]) -
                              (dem[i-1][j-1] + 2*dem[i][j-1] + dem[i+1][j-1])) / (8 * size);
                double dzdy = ((dem[i+1][j-1] + 2*dem[i+1][j] + dem[i+1][j+1]) -
                              (dem[i-1][j-1] + 2*dem[i-1][j] + dem[i-1][j+1])) / (8 * size);
                
                double asp = Math.toDegrees(Math.atan2(dzdy, -dzdx));
                if (asp < 0) asp += 360;
                aspect[i][j] = asp;
            }
        }
        return aspect;
    }
    
    /**
     * Simplified flow direction (D8 algorithm).
     */
    public static int[][] calculateFlowDirection(double[][] dem) {
        int rows = dem.length;
        int cols = dem[0].length;
        int[][] flowDir = new int[rows][cols];
        
        int[] di = {0, 1, 1, 1, 0, -1, -1, -1};
        int[] dj = {1, 1, 0, -1, -1, -1, 0, 1};
        int[] codes = {1, 2, 4, 8, 16, 32, 64, 128};
        
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                double maxDrop = 0;
                int maxDir = 0;
                for (int d = 0; d < 8; d++) {
                    double drop = dem[i][j] - dem[i + di[d]][j + dj[d]];
                    if (drop > maxDrop) {
                        maxDrop = drop;
                        maxDir = codes[d];
                    }
                }
                flowDir[i][j] = maxDir;
            }
        }
        return flowDir;
    }
}

