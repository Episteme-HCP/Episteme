package org.jscience.physics.classical.matter.fluids;

/**
 * 2D Lattice Boltzmann Method (LBM) solver for incompressible fluid flow.
 * Uses D2Q9 lattice (2D, 9 velocity directions).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class LatticeBoltzmannSolver {

    // D2Q9 lattice velocities
    private static final int[][] VELOCITIES = {
            { 0, 0 }, // Rest
            { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 }, // Cardinal
            { 1, 1 }, { -1, 1 }, { -1, -1 }, { 1, -1 } // Diagonal
    };

    // D2Q9 weights
    private static final double[] WEIGHTS = {
            4.0 / 9.0, // Rest
            1.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0, // Cardinal
            1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0 // Diagonal
    };

    // Opposite direction indices for bounce-back
    private static final int[] OPPOSITE = { 0, 3, 4, 1, 2, 7, 8, 5, 6 };

    private final int width;
    private final int height;
    private final double omega; // Relaxation parameter = 1/tau

    // Distribution functions
    private double[][][] f; // f[x][y][direction]
    private double[][][] fEq; // Equilibrium distributions
    private double[][][] fTemp; // Temporary for streaming

    // Macroscopic quantities
    private double[][] rho; // Density
    private double[][] ux; // X velocity
    private double[][] uy; // Y velocity

    // Obstacle mask
    private boolean[][] obstacle;

    /**
     * Creates an LBM solver.
     * 
     * @param width     Grid width
     * @param height    Grid height
     * @param viscosity Kinematic viscosity (determines relaxation time)
     */
    public LatticeBoltzmannSolver(int width, int height, double viscosity) {
        this.width = width;
        this.height = height;

        // tau = 3*viscosity + 0.5 (lattice units)
        double tau = 3.0 * viscosity + 0.5;
        this.omega = 1.0 / tau;

        // Initialize arrays
        f = new double[width][height][9];
        fEq = new double[width][height][9];
        fTemp = new double[width][height][9];
        rho = new double[width][height];
        ux = new double[width][height];
        uy = new double[width][height];
        obstacle = new boolean[width][height];

        // Initialize to equilibrium at rest (rho=1, u=0)
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                rho[x][y] = 1.0;
                ux[x][y] = 0.0;
                uy[x][y] = 0.0;
                for (int i = 0; i < 9; i++) {
                    f[x][y][i] = WEIGHTS[i];
                }
            }
        }
    }

    /**
     * Sets an obstacle at a grid point.
     */
    public void setObstacle(int x, int y, boolean isObstacle) {
        obstacle[x][y] = isObstacle;
    }

    /**
     * Sets inlet velocity (left boundary).
     */
    public void setInletVelocity(double ux0, double uy0) {
        for (int y = 1; y < height - 1; y++) {
            ux[0][y] = ux0;
            uy[0][y] = uy0;
        }
    }

    /**
     * Performs one LBM time step.
     */
    public void step() {
        // 1. Collision step
        collision();

        // 2. Streaming step
        streaming();

        // 3. Boundary conditions
        boundaryConditions();

        // 4. Compute macroscopic quantities
        computeMacroscopic();
    }

    private void collision() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (obstacle[x][y])
                    continue;

                double localRho = rho[x][y];
                double localUx = ux[x][y];
                double localUy = uy[x][y];
                double u2 = localUx * localUx + localUy * localUy;

                for (int i = 0; i < 9; i++) {
                    double cu = VELOCITIES[i][0] * localUx + VELOCITIES[i][1] * localUy;

                    // Equilibrium distribution
                    fEq[x][y][i] = WEIGHTS[i] * localRho * (1.0 + 3.0 * cu + 4.5 * cu * cu - 1.5 * u2);

                    // BGK collision
                    f[x][y][i] = f[x][y][i] + omega * (fEq[x][y][i] - f[x][y][i]);
                }
            }
        }
    }

    private void streaming() {
        // Copy to temp first
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                System.arraycopy(f[x][y], 0, fTemp[x][y], 0, 9);
            }
        }

        // Stream
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int i = 0; i < 9; i++) {
                    int xn = (x + VELOCITIES[i][0] + width) % width;
                    int yn = (y + VELOCITIES[i][1] + height) % height;
                    f[xn][yn][i] = fTemp[x][y][i];
                }
            }
        }
    }

    private void boundaryConditions() {
        // Bounce-back for obstacles
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (obstacle[x][y]) {
                    for (int i = 0; i < 9; i++) {
                        f[x][y][i] = fTemp[x][y][OPPOSITE[i]];
                    }
                }
            }
        }

        // Top/bottom walls - bounce back
        for (int x = 0; x < width; x++) {
            for (int i = 0; i < 9; i++) {
                f[x][0][i] = fTemp[x][0][OPPOSITE[i]];
                f[x][height - 1][i] = fTemp[x][height - 1][OPPOSITE[i]];
            }
        }
    }

    private void computeMacroscopic() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (obstacle[x][y]) {
                    rho[x][y] = 0;
                    ux[x][y] = 0;
                    uy[x][y] = 0;
                    continue;
                }

                double localRho = 0;
                double localUx = 0;
                double localUy = 0;

                for (int i = 0; i < 9; i++) {
                    localRho += f[x][y][i];
                    localUx += f[x][y][i] * VELOCITIES[i][0];
                    localUy += f[x][y][i] * VELOCITIES[i][1];
                }

                rho[x][y] = localRho;
                ux[x][y] = localUx / localRho;
                uy[x][y] = localUy / localRho;
            }
        }
    }

    // Accessors
    public double[][] getDensity() {
        return rho;
    }

    public double[][] getVelocityX() {
        return ux;
    }

    public double[][] getVelocityY() {
        return uy;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
