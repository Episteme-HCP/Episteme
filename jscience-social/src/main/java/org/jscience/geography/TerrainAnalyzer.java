package org.jscience.geography;

import java.util.*;

/**
 * Analyzes terrain from Digital Elevation Models (DEM).
 */
public final class TerrainAnalyzer {

    private TerrainAnalyzer() {}

    public record TerrainPoint(
        double x, double y,
        double elevation,
        double slope,           // degrees
        double aspect,          // degrees from north
        double curvature
    ) {}

    public record WatershedResult(
        int[][] basinIds,
        Map<Integer, Double> basinAreas,
        Map<Integer, double[]> pourPoints,  // Outlet points
        List<int[][]> streamNetwork
    ) {}

    /**
     * Calculates slope from elevation grid.
     * @param dem Digital Elevation Model (elevation values)
     * @param cellSize Cell size in same units as elevation
     * @return Slope in degrees for each cell
     */
    public static double[][] calculateSlope(double[][] dem, double cellSize) {
        int rows = dem.length;
        int cols = dem[0].length;
        double[][] slope = new double[rows][cols];
        
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                // 3x3 neighborhood gradient (Horn's method)
                double dzdx = ((dem[i-1][j+1] + 2*dem[i][j+1] + dem[i+1][j+1]) -
                              (dem[i-1][j-1] + 2*dem[i][j-1] + dem[i+1][j-1])) / (8 * cellSize);
                double dzdy = ((dem[i+1][j-1] + 2*dem[i+1][j] + dem[i+1][j+1]) -
                              (dem[i-1][j-1] + 2*dem[i-1][j] + dem[i-1][j+1])) / (8 * cellSize);
                
                double slopeRad = Math.atan(Math.sqrt(dzdx*dzdx + dzdy*dzdy));
                slope[i][j] = Math.toDegrees(slopeRad);
            }
        }
        
        return slope;
    }

    /**
     * Calculates aspect (direction of slope).
     * @return Aspect in degrees from north (0-360)
     */
    public static double[][] calculateAspect(double[][] dem, double cellSize) {
        int rows = dem.length;
        int cols = dem[0].length;
        double[][] aspect = new double[rows][cols];
        
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                double dzdx = ((dem[i-1][j+1] + 2*dem[i][j+1] + dem[i+1][j+1]) -
                              (dem[i-1][j-1] + 2*dem[i][j-1] + dem[i+1][j-1])) / (8 * cellSize);
                double dzdy = ((dem[i+1][j-1] + 2*dem[i+1][j] + dem[i+1][j+1]) -
                              (dem[i-1][j-1] + 2*dem[i-1][j] + dem[i-1][j+1])) / (8 * cellSize);
                
                double asp = Math.toDegrees(Math.atan2(dzdy, -dzdx));
                if (asp < 0) asp += 360;
                aspect[i][j] = asp;
            }
        }
        
        return aspect;
    }

    /**
     * Calculates profile curvature.
     */
    public static double[][] calculateCurvature(double[][] dem, double cellSize) {
        int rows = dem.length;
        int cols = dem[0].length;
        double[][] curvature = new double[rows][cols];
        
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                // Second derivatives
                double d2zdx2 = (dem[i][j+1] - 2*dem[i][j] + dem[i][j-1]) / (cellSize * cellSize);
                double d2zdy2 = (dem[i+1][j] - 2*dem[i][j] + dem[i-1][j]) / (cellSize * cellSize);
                
                curvature[i][j] = -100 * (d2zdx2 + d2zdy2);  // Scaled
            }
        }
        
        return curvature;
    }

    /**
     * Calculates flow direction using D8 algorithm.
     * Returns direction code: 1=E, 2=SE, 4=S, 8=SW, 16=W, 32=NW, 64=N, 128=NE
     */
    public static int[][] calculateFlowDirection(double[][] dem) {
        int rows = dem.length;
        int cols = dem[0].length;
        int[][] flowDir = new int[rows][cols];
        
        int[] di = {0, 1, 1, 1, 0, -1, -1, -1};
        int[] dj = {1, 1, 0, -1, -1, -1, 0, 1};
        int[] codes = {1, 2, 4, 8, 16, 32, 64, 128};
        double[] dist = {1, Math.sqrt(2), 1, Math.sqrt(2), 1, Math.sqrt(2), 1, Math.sqrt(2)};
        
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                double maxDrop = 0;
                int maxDir = 0;
                
                for (int d = 0; d < 8; d++) {
                    double drop = (dem[i][j] - dem[i + di[d]][j + dj[d]]) / dist[d];
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

    /**
     * Calculates flow accumulation.
     */
    public static int[][] calculateFlowAccumulation(int[][] flowDir) {
        int rows = flowDir.length;
        int cols = flowDir[0].length;
        int[][] accumulation = new int[rows][cols];
        
        // Initialize all cells to 1 (count themselves)
        for (int i = 0; i < rows; i++) {
            Arrays.fill(accumulation[i], 1);
        }
        
        // Direction lookup
        int[] di = {0, 1, 1, 1, 0, -1, -1, -1};
        int[] dj = {1, 1, 0, -1, -1, -1, 0, 1};
        int[] codes = {1, 2, 4, 8, 16, 32, 64, 128};
        
        // Multiple passes to propagate flow
        for (int pass = 0; pass < rows + cols; pass++) {
            for (int i = 1; i < rows - 1; i++) {
                for (int j = 1; j < cols - 1; j++) {
                    // Find cells that flow into this one
                    for (int d = 0; d < 8; d++) {
                        int ni = i - di[d];
                        int nj = j - dj[d];
                        if (ni >= 0 && ni < rows && nj >= 0 && nj < cols) {
                            if (flowDir[ni][nj] == codes[(d + 4) % 8]) {
                                accumulation[i][j] = Math.max(accumulation[i][j],
                                    accumulation[ni][nj] + 1);
                            }
                        }
                    }
                }
            }
        }
        
        return accumulation;
    }

    /**
     * Extracts stream network from flow accumulation.
     */
    public static boolean[][] extractStreams(int[][] accumulation, int threshold) {
        int rows = accumulation.length;
        int cols = accumulation[0].length;
        boolean[][] streams = new boolean[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                streams[i][j] = accumulation[i][j] >= threshold;
            }
        }
        
        return streams;
    }

    /**
     * Calculates viewshed from a point.
     */
    public static boolean[][] calculateViewshed(double[][] dem, int observerRow, int observerCol,
            double observerHeight) {
        
        int rows = dem.length;
        int cols = dem[0].length;
        boolean[][] visible = new boolean[rows][cols];
        double observerElev = dem[observerRow][observerCol] + observerHeight;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == observerRow && j == observerCol) {
                    visible[i][j] = true;
                    continue;
                }
                
                visible[i][j] = isVisible(dem, observerRow, observerCol, observerElev, i, j);
            }
        }
        
        return visible;
    }

    private static boolean isVisible(double[][] dem, int r1, int c1, double elev1, int r2, int c2) {
        double dx = c2 - c1;
        double dy = r2 - r1;
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        if (distance < 1) return true;
        
        double targetElev = dem[r2][c2];
        double targetAngle = Math.atan2(targetElev - elev1, distance);
        
        // Check intermediate points
        int steps = (int) Math.ceil(distance);
        for (int s = 1; s < steps; s++) {
            double fraction = (double) s / steps;
            int checkR = (int) Math.round(r1 + dy * fraction);
            int checkC = (int) Math.round(c1 + dx * fraction);
            
            if (checkR >= 0 && checkR < dem.length && checkC >= 0 && checkC < dem[0].length) {
                double checkDist = distance * fraction;
                double checkElev = dem[checkR][checkC];
                double checkAngle = Math.atan2(checkElev - elev1, checkDist);
                
                if (checkAngle > targetAngle) return false;
            }
        }
        
        return true;
    }
}
