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
 * 1D Wave equation solver: ∂²u/∂t² = c² ∂²u/∂x²
 * <p>
 * Uses explicit finite difference (leapfrog) scheme.
 * </p>
 *
 * <p>
 * References:
 * <ul>
 * <li>Courant, R., Friedrichs, K., & Lewy, H. (1928). Über die partiellen
 * Differenzengleichungen der mathematischen Physik. Mathematische Annalen,
 * 100(1), 32-74.</li>
 * <li>Trefethen, L. N. (2000). Spectral Methods in MATLAB. SIAM.</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class WaveEquationSolver {

    private final int nx;
    private final Real dx;
    private final Real c; // Wave speed

    private Real[] uPrev; // u at t-dt
    private Real[] uCurr; // u at t
    private Real[] uNext; // u at t+dt
    private Real time = Real.ZERO;

    public WaveEquationSolver(int nx, Real length, Real waveSpeed) {
        this.nx = nx;
        this.dx = length.divide(Real.of(nx - 1));
        this.c = waveSpeed;
        this.uPrev = new Real[nx];
        this.uCurr = new Real[nx];
        this.uNext = new Real[nx];
        for (int i = 0; i < nx; i++) {
            uPrev[i] = Real.ZERO;
            uCurr[i] = Real.ZERO;
            uNext[i] = Real.ZERO;
        }
    }

    public WaveEquationSolver(int nx, double length, double waveSpeed) {
        this(nx, Real.of(length), Real.of(waveSpeed));
    }

    /**
     * Set initial displacement.
     */
    public void setInitialDisplacement(org.jscience.mathematics.analysis.Function<Real, Real> f) {
        for (int i = 0; i < nx; i++) {
            uCurr[i] = f.evaluate(dx.multiply(Real.of(i)));
            uPrev[i] = uCurr[i]; // Zero initial velocity
        }
    }

    /**
     * Set initial displacement and velocity.
     */
    public void setInitialConditions(org.jscience.mathematics.analysis.Function<Real, Real> displacement,
            org.jscience.mathematics.analysis.Function<Real, Real> velocity,
            Real dt) {
        for (int i = 0; i < nx; i++) {
            Real x = dx.multiply(Real.of(i));
            uCurr[i] = displacement.evaluate(x);
            // First step: u_prev Ã¢â€°Ë† u_curr - v*dt
            uPrev[i] = uCurr[i].subtract(velocity.evaluate(x).multiply(dt));
        }
    }

    /**
     * Advance by one time step using leapfrog scheme.
     * CFL condition: dt <= dx/c for stability.
     */
    public void step(Real dt) {
        Real r = c.multiply(dt).divide(dx);
        Real r2 = r.multiply(r);

        // Interior points
        for (int i = 1; i < nx - 1; i++) {
            uNext[i] = uCurr[i].multiply(Real.of(2)).subtract(uPrev[i])
                    .add(r2.multiply(uCurr[i + 1].subtract(uCurr[i].multiply(Real.of(2))).add(uCurr[i - 1])));
        }

        // Fixed boundary conditions (u = 0 at ends)
        uNext[0] = Real.ZERO;
        uNext[nx - 1] = Real.ZERO;

        // Shift arrays
        Real[] temp = uPrev;
        uPrev = uCurr;
        uCurr = uNext;
        uNext = temp;

        time = time.add(dt);
    }

    /**
     * Step with free boundary conditions (Ã¢Ë†â€šu/Ã¢Ë†â€šx = 0).
     */
    public void stepFreeBoundary(Real dt) {
        Real r = c.multiply(dt).divide(dx);
        Real r2 = r.multiply(r);

        for (int i = 1; i < nx - 1; i++) {
            uNext[i] = uCurr[i].multiply(Real.of(2)).subtract(uPrev[i])
                    .add(r2.multiply(uCurr[i + 1].subtract(uCurr[i].multiply(Real.of(2))).add(uCurr[i - 1])));
        }

        // Free boundary (du/dx = 0) using ghost cells
        uNext[0] = uNext[1];
        uNext[nx - 1] = uNext[nx - 2];

        Real[] temp = uPrev;
        uPrev = uCurr;
        uCurr = uNext;
        uNext = temp;

        time = time.add(dt);
    }

    /**
     * Run for specified total time.
     */
    public void solve(Real totalTime, Real dt) {
        int steps = (int) totalTime.divide(dt).doubleValue();
        for (int i = 0; i < steps; i++) {
            step(dt);
        }
    }

    public Real[] getSolution() {
        return uCurr.clone();
    }

    public Real getValue(int i) {
        return uCurr[i];
    }

    public Real getTime() {
        return time;
    }

    public int getSize() {
        return nx;
    }

    public Real getX(int i) {
        return dx.multiply(Real.of(i));
    }

    /**
     * Total energy (kinetic + potential) - should be conserved.
     */
    public Real totalEnergy(Real dt) {
        Real ke = Real.ZERO, pe = Real.ZERO;
        for (int i = 1; i < nx - 1; i++) {
            // Kinetic: 0.5 * (du/dt)^2
            Real dudt = uCurr[i].subtract(uPrev[i]).divide(dt);
            ke = ke.add(dudt.multiply(dudt).multiply(dx).multiply(Real.of(0.5)));

            // Potential: 0.5 * c^2 * (du/dx)^2
            Real dudx = uCurr[i + 1].subtract(uCurr[i - 1]).divide(dx.multiply(Real.of(2)));
            pe = pe.add(c.multiply(c).multiply(dudx).multiply(dudx).multiply(dx).multiply(Real.of(0.5)));
        }
        return ke.add(pe);
    }

    // --- Factory methods ---

    /**
     * Plucked string: triangular initial displacement.
     */
    public static WaveEquationSolver pluckedString(int nx, Real length, Real waveSpeed,
            Real pluckPosition, Real pluckHeight) {
        WaveEquationSolver solver = new WaveEquationSolver(nx, length, waveSpeed);
        solver.setInitialDisplacement(x -> {
            if (x.compareTo(pluckPosition) < 0) {
                return pluckHeight.multiply(x).divide(pluckPosition);
            } else {
                return pluckHeight.multiply(length.subtract(x)).divide(length.subtract(pluckPosition));
            }
        });
        return solver;
    }

    /**
     * Gaussian pulse.
     */
    public static WaveEquationSolver gaussianPulse(int nx, Real length, Real waveSpeed,
            Real center, Real width) {
        WaveEquationSolver solver = new WaveEquationSolver(nx, length, waveSpeed);
        solver.setInitialDisplacement(x -> x.subtract(center).divide(width).pow(2).negate().exp());
        return solver;
    }
}
