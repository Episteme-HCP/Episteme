/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.mathematics.analysis.pde;

/**
 * Finite Difference solver for 1D heat equation.
 * <p>
 * Solves: Ã¢Ë†â€šu/Ã¢Ë†â€št = ÃŽÂ± Ã¢Ë†â€šÃ‚Â²u/Ã¢Ë†â€šxÃ‚Â²
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class HeatEquationSolver {

    private final int nx; // Number of spatial points
    private final double dx; // Spatial step
    private final double alpha; // Thermal diffusivity

    private double[] u; // Current temperature distribution
    private double[] uNew; // Buffer for next timestep

    // Boundary conditions
    private double leftBC = 0;
    private double rightBC = 0;
    private BoundaryType leftType = BoundaryType.DIRICHLET;
    private BoundaryType rightType = BoundaryType.DIRICHLET;

    public enum BoundaryType {
        DIRICHLET, // Fixed value
        NEUMANN // Fixed derivative (flux)
    }

    public HeatEquationSolver(int nx, double length, double alpha) {
        this.nx = nx;
        this.dx = length / (nx - 1);
        this.alpha = alpha;
        this.u = new double[nx];
        this.uNew = new double[nx];
    }

    /**
     * Set initial condition.
     */
    public void setInitialCondition(java.util.function.DoubleUnaryOperator f) {
        for (int i = 0; i < nx; i++) {
            u[i] = f.applyAsDouble(i * dx);
        }
    }

    public void setLeftBoundary(BoundaryType type, double value) {
        leftType = type;
        leftBC = value;
    }

    public void setRightBoundary(BoundaryType type, double value) {
        rightType = type;
        rightBC = value;
    }

    /**
     * Explicit Euler time step (FTCS scheme).
     * Stable if dt <= dxÃ‚Â²/(2ÃŽÂ±)
     */
    public void stepExplicit(double dt) {
        double r = alpha * dt / (dx * dx);

        // Interior points
        for (int i = 1; i < nx - 1; i++) {
            uNew[i] = u[i] + r * (u[i + 1] - 2 * u[i] + u[i - 1]);
        }

        // Boundary conditions
        applyBoundaryConditions();

        // Swap buffers
        double[] temp = u;
        u = uNew;
        uNew = temp;
    }

    /**
     * Implicit Euler time step (unconditionally stable).
     * Uses Thomas algorithm for tridiagonal system.
     */
    public void stepImplicit(double dt) {
        double r = alpha * dt / (dx * dx);

        // Setup tridiagonal system: A * uNew = u
        // -r * u_{i-1} + (1+2r) * u_i - r * u_{i+1} = u_i^old
        double[] a = new double[nx]; // Lower diagonal
        double[] b = new double[nx]; // Main diagonal
        double[] c = new double[nx]; // Upper diagonal
        double[] d = new double[nx]; // RHS

        for (int i = 1; i < nx - 1; i++) {
            a[i] = -r;
            b[i] = 1 + 2 * r;
            c[i] = -r;
            d[i] = u[i];
        }

        // Boundary conditions
        b[0] = 1;
        c[0] = 0;
        a[nx - 1] = 0;
        b[nx - 1] = 1;

        if (leftType == BoundaryType.DIRICHLET) {
            d[0] = leftBC;
        } else {
            b[0] = 1;
            c[0] = -1;
            d[0] = leftBC * dx;
        }

        if (rightType == BoundaryType.DIRICHLET) {
            d[nx - 1] = rightBC;
        } else {
            a[nx - 1] = -1;
            b[nx - 1] = 1;
            d[nx - 1] = rightBC * dx;
        }

        // Thomas algorithm
        thomasSolve(a, b, c, d, uNew);

        // Swap
        double[] temp = u;
        u = uNew;
        uNew = temp;
    }

    private void thomasSolve(double[] a, double[] b, double[] c, double[] d, double[] x) {
        int n = d.length;
        double[] cp = new double[n];
        double[] dp = new double[n];

        cp[0] = c[0] / b[0];
        dp[0] = d[0] / b[0];

        for (int i = 1; i < n; i++) {
            double m = b[i] - a[i] * cp[i - 1];
            cp[i] = c[i] / m;
            dp[i] = (d[i] - a[i] * dp[i - 1]) / m;
        }

        x[n - 1] = dp[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            x[i] = dp[i] - cp[i] * x[i + 1];
        }
    }

    private void applyBoundaryConditions() {
        if (leftType == BoundaryType.DIRICHLET) {
            uNew[0] = leftBC;
        } else {
            uNew[0] = uNew[1] - leftBC * dx;
        }

        if (rightType == BoundaryType.DIRICHLET) {
            uNew[nx - 1] = rightBC;
        } else {
            uNew[nx - 1] = uNew[nx - 2] + rightBC * dx;
        }
    }

    /**
     * Run simulation for given time.
     */
    public void solve(double totalTime, double dt, boolean implicit) {
        int steps = (int) (totalTime / dt);
        for (int i = 0; i < steps; i++) {
            if (implicit) {
                stepImplicit(dt);
            } else {
                stepExplicit(dt);
            }
        }
    }

    public double[] getSolution() {
        return u.clone();
    }

    public double getValue(int i) {
        return u[i];
    }

    public int getSize() {
        return nx;
    }

    public double getDx() {
        return dx;
    }

    /**
     * Get the spatial coordinate for index i.
     */
    public double getX(int i) {
        return i * dx;
    }
}


