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
 * 1D Wave equation solver: Ã¢Ë†â€šÃ‚Â²u/Ã¢Ë†â€štÃ‚Â² = cÃ‚Â² Ã¢Ë†â€šÃ‚Â²u/Ã¢Ë†â€šxÃ‚Â²
 * <p>
 * Uses explicit finite difference (leapfrog) scheme.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class WaveEquationSolver {

    private final int nx;
    private final double dx;
    private final double c; // Wave speed

    private double[] uPrev; // u at t-dt
    private double[] uCurr; // u at t
    private double[] uNext; // u at t+dt
    private double time;

    public WaveEquationSolver(int nx, double length, double waveSpeed) {
        this.nx = nx;
        this.dx = length / (nx - 1);
        this.c = waveSpeed;
        this.uPrev = new double[nx];
        this.uCurr = new double[nx];
        this.uNext = new double[nx];
    }

    /**
     * Set initial displacement.
     */
    public void setInitialDisplacement(java.util.function.DoubleUnaryOperator f) {
        for (int i = 0; i < nx; i++) {
            uCurr[i] = f.applyAsDouble(i * dx);
            uPrev[i] = uCurr[i]; // Zero initial velocity
        }
    }

    /**
     * Set initial displacement and velocity.
     */
    public void setInitialConditions(java.util.function.DoubleUnaryOperator displacement,
            java.util.function.DoubleUnaryOperator velocity,
            double dt) {
        for (int i = 0; i < nx; i++) {
            double x = i * dx;
            uCurr[i] = displacement.applyAsDouble(x);
            // First step: u_prev Ã¢â€°Ë† u_curr - v*dt
            uPrev[i] = uCurr[i] - velocity.applyAsDouble(x) * dt;
        }
    }

    /**
     * Advance by one time step using leapfrog scheme.
     * CFL condition: dt <= dx/c for stability.
     */
    public void step(double dt) {
        double r2 = (c * dt / dx) * (c * dt / dx);

        // Interior points
        for (int i = 1; i < nx - 1; i++) {
            uNext[i] = 2 * uCurr[i] - uPrev[i]
                    + r2 * (uCurr[i + 1] - 2 * uCurr[i] + uCurr[i - 1]);
        }

        // Fixed boundary conditions (u = 0 at ends)
        uNext[0] = 0;
        uNext[nx - 1] = 0;

        // Shift arrays
        double[] temp = uPrev;
        uPrev = uCurr;
        uCurr = uNext;
        uNext = temp;

        time += dt;
    }

    /**
     * Step with free boundary conditions (Ã¢Ë†â€šu/Ã¢Ë†â€šx = 0).
     */
    public void stepFreeBoundary(double dt) {
        double r2 = (c * dt / dx) * (c * dt / dx);

        for (int i = 1; i < nx - 1; i++) {
            uNext[i] = 2 * uCurr[i] - uPrev[i]
                    + r2 * (uCurr[i + 1] - 2 * uCurr[i] + uCurr[i - 1]);
        }

        // Free boundary (du/dx = 0) using ghost cells
        uNext[0] = uNext[1];
        uNext[nx - 1] = uNext[nx - 2];

        double[] temp = uPrev;
        uPrev = uCurr;
        uCurr = uNext;
        uNext = temp;

        time += dt;
    }

    /**
     * Run for specified total time.
     */
    public void solve(double totalTime, double dt) {
        int steps = (int) (totalTime / dt);
        for (int i = 0; i < steps; i++) {
            step(dt);
        }
    }

    public double[] getSolution() {
        return uCurr.clone();
    }

    public double getValue(int i) {
        return uCurr[i];
    }

    public double getTime() {
        return time;
    }

    public int getSize() {
        return nx;
    }

    public double getX(int i) {
        return i * dx;
    }

    /**
     * Total energy (kinetic + potential) - should be conserved.
     */
    public double totalEnergy(double dt) {
        double ke = 0, pe = 0;
        for (int i = 1; i < nx - 1; i++) {
            // Kinetic: 0.5 * (du/dt)^2
            double dudt = (uCurr[i] - uPrev[i]) / dt;
            ke += 0.5 * dudt * dudt * dx;

            // Potential: 0.5 * c^2 * (du/dx)^2
            double dudx = (uCurr[i + 1] - uCurr[i - 1]) / (2 * dx);
            pe += 0.5 * c * c * dudx * dudx * dx;
        }
        return ke + pe;
    }

    // --- Factory methods ---

    /**
     * Plucked string: triangular initial displacement.
     */
    public static WaveEquationSolver pluckedString(int nx, double length, double waveSpeed,
            double pluckPosition, double pluckHeight) {
        WaveEquationSolver solver = new WaveEquationSolver(nx, length, waveSpeed);
        solver.setInitialDisplacement(x -> {
            if (x < pluckPosition) {
                return pluckHeight * x / pluckPosition;
            } else {
                return pluckHeight * (length - x) / (length - pluckPosition);
            }
        });
        return solver;
    }

    /**
     * Gaussian pulse.
     */
    public static WaveEquationSolver gaussianPulse(int nx, double length, double waveSpeed,
            double center, double width) {
        WaveEquationSolver solver = new WaveEquationSolver(nx, length, waveSpeed);
        solver.setInitialDisplacement(x -> Math.exp(-Math.pow((x - center) / width, 2)));
        return solver;
    }
}


