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
import org.jscience.mathematics.analysis.Function;

/**
 * Finite Difference Methods for solving Partial Differential Equations.
 * <p>
 * Discretizes PDEs on grid, approximates derivatives with differences.
 * Handles: heat equation, wave equation, Poisson equation.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FiniteDifference {

    /**
     * Solves 1D heat equation: Ã¢Ë†â€šu/Ã¢Ë†â€št = ÃŽÂ± Ã¢Ë†â€šÃ‚Â²u/Ã¢Ë†â€šxÃ‚Â²
     * <p>
     * Forward Euler (explicit) time stepping.
     * Boundary conditions: u(0,t) = u(L,t) = 0
     * </p>
     * 
     * @param initial initial condition u(x,0)
     * @param alpha   thermal diffusivity
     * @param L       domain length
     * @param T       final time
     * @param nx      number of spatial points
     * @param nt      number of time steps
     * @return solution u[time][space]
     */
    public static Real[][] heatEquation1D(Function<Real, Real> initial,
            Real alpha, Real L, Real T,
            int nx, int nt) {
        Real dx = L.divide(Real.of(nx - 1));
        Real dt = T.divide(Real.of(nt - 1));

        // Stability condition: ÃŽÂ±*dt/dxÃ‚Â² Ã¢â€°Â¤ 0.5
        Real stability = alpha.multiply(dt).divide(dx.multiply(dx));
        if (stability.compareTo(Real.of(0.5)) > 0) {
            throw new IllegalArgumentException("Unstable: reduce dt or increase dx");
        }

        Real[][] u = new Real[nt][nx];

        // Initial condition
        for (int i = 0; i < nx; i++) {
            Real x = Real.of(i).multiply(dx);
            u[0][i] = initial.evaluate(x);
        }

        // Boundary conditions
        for (int n = 0; n < nt; n++) {
            u[n][0] = Real.ZERO;
            u[n][nx - 1] = Real.ZERO;
        }

        // Time stepping
        for (int n = 0; n < nt - 1; n++) {
            for (int i = 1; i < nx - 1; i++) {
                // u_{i}^{n+1} = u_i^n + ÃŽÂ±*dt/dxÃ‚Â² * (u_{i+1}^n - 2u_i^n + u_{i-1}^n)
                Real laplacian = u[n][i + 1].add(u[n][i - 1]).subtract(u[n][i].multiply(Real.of(2)));
                u[n + 1][i] = u[n][i].add(stability.multiply(laplacian));
            }
        }

        return u;
    }

    /**
     * Solves 1D wave equation: Ã¢Ë†â€šÃ‚Â²u/Ã¢Ë†â€štÃ‚Â² = cÃ‚Â² Ã¢Ë†â€šÃ‚Â²u/Ã¢Ë†â€šxÃ‚Â²
     * <p>
     * Explicit central differences in space and time.
     * </p>
     * 
     * @param initial         initial displacement u(x,0)
     * @param initialVelocity initial velocity Ã¢Ë†â€šu/Ã¢Ë†â€št(x,0)
     * @param c               wave speed
     * @param L               domain length
     * @param T               final time
     * @param nx              spatial points
     * @param nt              time steps
     * @return solution u[time][space]
     */
    public static Real[][] waveEquation1D(Function<Real, Real> initial,
            Function<Real, Real> initialVelocity,
            Real c, Real L, Real T,
            int nx, int nt) {
        Real dx = L.divide(Real.of(nx - 1));
        Real dt = T.divide(Real.of(nt - 1));

        // CFL condition: c*dt/dx Ã¢â€°Â¤ 1
        Real cfl = c.multiply(dt).divide(dx);
        if (cfl.compareTo(Real.ONE) > 0) {
            throw new IllegalArgumentException("Unstable: CFL condition violated");
        }

        Real[][] u = new Real[nt][nx];

        // Initial condition u(x,0)
        for (int i = 0; i < nx; i++) {
            Real x = Real.of(i).multiply(dx);
            u[0][i] = initial.evaluate(x);
        }

        // First time step using initial velocity
        Real r = cfl.multiply(cfl); // (c*dt/dx)Ã‚Â²
        for (int i = 1; i < nx - 1; i++) {
            Real x = Real.of(i).multiply(dx);
            Real laplacian = u[0][i + 1].add(u[0][i - 1]).subtract(u[0][i].multiply(Real.of(2)));
            u[1][i] = u[0][i].add(dt.multiply(initialVelocity.evaluate(x)))
                    .add(r.multiply(laplacian).divide(Real.of(2)));
        }

        // Boundary conditions
        for (int n = 0; n < nt; n++) {
            u[n][0] = Real.ZERO;
            u[n][nx - 1] = Real.ZERO;
        }

        // Time stepping
        for (int n = 1; n < nt - 1; n++) {
            for (int i = 1; i < nx - 1; i++) {
                Real laplacian = u[n][i + 1].add(u[n][i - 1]).subtract(u[n][i].multiply(Real.of(2)));
                u[n + 1][i] = u[n][i].multiply(Real.of(2)).subtract(u[n - 1][i])
                        .add(r.multiply(laplacian));
            }
        }

        return u;
    }

    /**
     * Solves 2D Poisson equation: Ã¢Ë†â€¡Ã‚Â²u = f on rectangular domain.
     * <p>
     * Uses Jacobi iteration (simple iterative solver).
     * Ã¢Ë†â€šÃ‚Â²u/Ã¢Ë†â€šxÃ‚Â² + Ã¢Ë†â€šÃ‚Â²u/Ã¢Ë†â€šyÃ‚Â² = f
     * </p>
     * 
     * @param source    source term f(x,y)
     * @param nx,       ny grid points in x, y
     * @param lx,       ly domain size
     * @param maxIter   max iterations
     * @param tolerance convergence tolerance
     * @return solution u[x][y]
     */
    public static Real[][] poisson2D(Function<Real[], Real> source,
            int nx, int ny,
            Real lx, Real ly,
            int maxIter, Real tolerance) {
        Real dx = lx.divide(Real.of(nx - 1));
        Real dy = ly.divide(Real.of(ny - 1));

        Real[][] u = new Real[nx][ny];
        Real[][] uNew = new Real[nx][ny];

        // Initialize to zero
        for (int i = 0; i < nx; i++) {
            for (int j = 0; j < ny; j++) {
                u[i][j] = Real.ZERO;
                uNew[i][j] = Real.ZERO;
            }
        }

        // Source term
        Real[][] f = new Real[nx][ny];
        for (int i = 0; i < nx; i++) {
            for (int j = 0; j < ny; j++) {
                Real x = Real.of(i).multiply(dx);
                Real y = Real.of(j).multiply(dy);
                f[i][j] = source.evaluate(new Real[] { x, y });
            }
        }

        // Jacobi iteration
        Real dx2 = dx.multiply(dx);
        Real dy2 = dy.multiply(dy);
        Real denominator = Real.of(2).multiply(dx2.add(dy2));

        for (int iter = 0; iter < maxIter; iter++) {
            Real maxChange = Real.ZERO;

            for (int i = 1; i < nx - 1; i++) {
                for (int j = 1; j < ny - 1; j++) {
                    Real numerator = dx2.multiply(u[i][j + 1].add(u[i][j - 1]))
                            .add(dy2.multiply(u[i + 1][j].add(u[i - 1][j])))
                            .subtract(dx2.multiply(dy2).multiply(f[i][j]));

                    uNew[i][j] = numerator.divide(denominator);

                    Real change = uNew[i][j].subtract(u[i][j]).abs();
                    if (change.compareTo(maxChange) > 0) {
                        maxChange = change;
                    }
                }
            }

            // Copy new to old
            for (int i = 0; i < nx; i++) {
                System.arraycopy(uNew[i], 0, u[i], 0, ny);
            }

            // Check convergence
            if (maxChange.compareTo(tolerance) < 0) {
                break;
            }
        }

        return u;
    }
}


