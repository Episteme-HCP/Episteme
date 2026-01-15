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

package org.jscience.mathematics.analysis.pde;

import org.jscience.mathematics.numbers.real.Real;

/**
 * 1D Heat Equation Solver.
 * Supports Explicit and Implicit integration schemas.
 */
public class HeatEquationSolver {

    private final int nx; // Number of spatial points
    private final Real dx; // Spatial step
    private final Real alpha; // Thermal diffusivity

    private Real[] u; // Current temperature distribution
    private Real[] uNew; // Buffer for next timestep

    // Boundary conditions
    private Real leftBC = Real.ZERO;
    private Real rightBC = Real.ZERO;
    private BoundaryType leftType = BoundaryType.DIRICHLET;
    private BoundaryType rightType = BoundaryType.DIRICHLET;

    public HeatEquationSolver(int nx, Real length, Real alpha) {
        this.nx = nx;
        this.dx = length.divide(Real.of(nx - 1));
        this.alpha = alpha;
        this.u = new Real[nx];
        this.uNew = new Real[nx];
        for (int i = 0; i < nx; i++) {
            u[i] = Real.ZERO;
            uNew[i] = Real.ZERO;
        }
    }

    public HeatEquationSolver(int nx, double length, double alpha) {
        this(nx, Real.of(length), Real.of(alpha));
    }

    /**
     * Set initial condition.
     */
    public void setInitialCondition(org.jscience.mathematics.analysis.Function<Real, Real> f) {
        for (int i = 0; i < nx; i++) {
            u[i] = f.evaluate(dx.multiply(Real.of(i)));
        }
    }

    public void setLeftBoundary(BoundaryType type, Real value) {
        leftType = type;
        leftBC = value;
    }

    public void setRightBoundary(BoundaryType type, Real value) {
        rightType = type;
        rightBC = value;
    }

    /**
     * Explicit Euler time step (FTCS scheme).
     * Stable if dt <= dx^2/(2*alpha)
     */
    public void stepExplicit(Real dt) {
        Real r = alpha.multiply(dt).divide(dx.multiply(dx));

        // Interior points
        for (int i = 1; i < nx - 1; i++) {
            uNew[i] = u[i].add(r.multiply(u[i + 1].subtract(u[i].multiply(Real.of(2))).add(u[i - 1])));
        }

        // Boundary conditions
        applyBoundaryConditions();

        // Swap buffers
        Real[] temp = u;
        u = uNew;
        uNew = temp;
    }

    /**
     * Implicit Euler time step (unconditionally stable).
     * Uses Thomas algorithm for tridiagonal system.
     */
    public void stepImplicit(Real dt) {
        Real r = alpha.multiply(dt).divide(dx.multiply(dx));

        // Setup tridiagonal system: A * uNew = u
        Real[] a = new Real[nx]; // Lower diagonal
        Real[] b = new Real[nx]; // Main diagonal
        Real[] c = new Real[nx]; // Upper diagonal
        Real[] d = new Real[nx]; // RHS

        for (int i = 1; i < nx - 1; i++) {
            a[i] = r.negate();
            b[i] = Real.ONE.add(r.multiply(Real.of(2)));
            c[i] = r.negate();
            d[i] = u[i];
        }

        // Boundary conditions
        b[0] = Real.ONE;
        c[0] = Real.ZERO;
        a[nx - 1] = Real.ZERO;
        b[nx - 1] = Real.ONE;

        if (leftType == BoundaryType.DIRICHLET) {
            d[0] = leftBC;
        } else {
            b[0] = Real.ONE;
            c[0] = Real.of(-1);
            d[0] = leftBC.multiply(dx);
        }

        if (rightType == BoundaryType.DIRICHLET) {
            d[nx - 1] = rightBC;
        } else {
            a[nx - 1] = Real.of(-1);
            b[nx - 1] = Real.ONE;
            d[nx - 1] = rightBC.multiply(dx);
        }

        // Thomas algorithm
        thomasSolve(a, b, c, d, uNew);

        // Swap
        Real[] temp = u;
        u = uNew;
        uNew = temp;
    }

    private void thomasSolve(Real[] a, Real[] b, Real[] c, Real[] d, Real[] x) {
        int n = d.length;
        Real[] cp = new Real[n];
        Real[] dp = new Real[n];

        cp[0] = c[0].divide(b[0]);
        dp[0] = d[0].divide(b[0]);

        for (int i = 1; i < n; i++) {
            Real m = b[i].subtract(a[i].multiply(cp[i - 1]));
            cp[i] = c[i].divide(m);
            dp[i] = d[i].subtract(a[i].multiply(dp[i - 1])).divide(m);
        }

        x[n - 1] = dp[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            x[i] = dp[i].subtract(cp[i].multiply(x[i + 1]));
        }
    }

    private void applyBoundaryConditions() {
        if (leftType == BoundaryType.DIRICHLET) {
            uNew[0] = leftBC;
        } else {
            uNew[0] = uNew[1].subtract(leftBC.multiply(dx));
        }

        if (rightType == BoundaryType.DIRICHLET) {
            uNew[nx - 1] = rightBC;
        } else {
            uNew[nx - 1] = uNew[nx - 2].add(rightBC.multiply(dx));
        }
    }

    public Real[] getSolution() {
        return u;
    }
}
