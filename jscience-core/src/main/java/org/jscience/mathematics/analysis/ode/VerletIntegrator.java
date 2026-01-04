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

package org.jscience.mathematics.analysis.ode;

/**
 * Verlet integration for molecular dynamics and N-body simulations.
 * <p>
 * Symplectic integrator: conserves energy over long simulations.
 * Better for Hamiltonian systems than Runge-Kutta.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class VerletIntegrator {

    // Double-based methods removed per architectural guidelines (Real only for Core
    // physics)

    /**
     * Integrate using arbitrary precision Real numbers over multiple steps.
     * 
     * @param x0           Initial positions
     * @param v0           Initial velocities
     * @param acceleration Acceleration function
     * @param dt           Time step
     * @param steps        Number of steps
     * @return Final [positions, velocities]
     */
    public static org.jscience.mathematics.numbers.real.Real[][] integrate(
            org.jscience.mathematics.numbers.real.Real[] x0,
            org.jscience.mathematics.numbers.real.Real[] v0,
            java.util.function.Function<org.jscience.mathematics.numbers.real.Real[], org.jscience.mathematics.numbers.real.Real[]> acceleration,
            org.jscience.mathematics.numbers.real.Real dt,
            int steps) {

        org.jscience.mathematics.numbers.real.Real[] x = x0.clone();
        org.jscience.mathematics.numbers.real.Real[] v = v0.clone();

        for (int i = 0; i < steps; i++) {
            org.jscience.mathematics.numbers.real.Real[][] result = step(x, v, acceleration, dt);
            x = result[0];
            v = result[1];
        }

        return new org.jscience.mathematics.numbers.real.Real[][] { x, v };
    }

    /**
     * Velocity Verlet integration step using arbitrary precision Real numbers.
     * 
     * @param positions    Current positions x[n]
     * @param velocities   Current velocities v[n]
     * @param acceleration Function computing acceleration from positions
     * @param dt           Time step
     * @return Updated [positions, velocities] after one step
     */
    public static org.jscience.mathematics.numbers.real.Real[][] step(
            org.jscience.mathematics.numbers.real.Real[] positions,
            org.jscience.mathematics.numbers.real.Real[] velocities,
            java.util.function.Function<org.jscience.mathematics.numbers.real.Real[], org.jscience.mathematics.numbers.real.Real[]> acceleration,
            org.jscience.mathematics.numbers.real.Real dt) {

        int n = positions.length;
        org.jscience.mathematics.numbers.real.Real[] a = acceleration.apply(positions);
        org.jscience.mathematics.numbers.real.Real half = org.jscience.mathematics.numbers.real.Real.of(0.5);

        // Half-step velocity: v(t + dt/2) = v(t) + a(t) * dt/2
        org.jscience.mathematics.numbers.real.Real[] vHalf = new org.jscience.mathematics.numbers.real.Real[n];
        for (int i = 0; i < n; i++) {
            vHalf[i] = velocities[i].add(a[i].multiply(dt).multiply(half));
        }

        // Full-step position: x(t + dt) = x(t) + v(t + dt/2) * dt
        org.jscience.mathematics.numbers.real.Real[] xNew = new org.jscience.mathematics.numbers.real.Real[n];
        for (int i = 0; i < n; i++) {
            xNew[i] = positions[i].add(vHalf[i].multiply(dt));
        }

        // Compute new acceleration
        org.jscience.mathematics.numbers.real.Real[] aNew = acceleration.apply(xNew);

        // Full-step velocity: v(t + dt) = v(t + dt/2) + a(t + dt) * dt/2
        org.jscience.mathematics.numbers.real.Real[] vNew = new org.jscience.mathematics.numbers.real.Real[n];
        for (int i = 0; i < n; i++) {
            vNew[i] = vHalf[i].add(aNew[i].multiply(dt).multiply(half));
        }

        return new org.jscience.mathematics.numbers.real.Real[][] { xNew, vNew };
    }
}
