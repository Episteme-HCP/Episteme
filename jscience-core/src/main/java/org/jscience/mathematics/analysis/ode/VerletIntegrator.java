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

    /**
     * Velocity Verlet integration step.
     * <p>
     * For systems: d²x/dt² = a(x), where a is acceleration.
     * </p>
     * 
     * @param positions    Current positions x[n]
     * @param velocities   Current velocities v[n]
     * @param acceleration Function computing acceleration from positions
     * @param dt           Time step
     * @return Updated [positions, velocities] after one step
     */
    public static double[][] step(
            double[] positions,
            double[] velocities,
            java.util.function.Function<double[], double[]> acceleration,
            double dt) {

        int n = positions.length;
        double[] a = acceleration.apply(positions);

        // Half-step velocity: v(t + dt/2) = v(t) + a(t) * dt/2
        double[] vHalf = new double[n];
        for (int i = 0; i < n; i++) {
            vHalf[i] = velocities[i] + 0.5 * a[i] * dt;
        }

        // Full-step position: x(t + dt) = x(t) + v(t + dt/2) * dt
        double[] xNew = new double[n];
        for (int i = 0; i < n; i++) {
            xNew[i] = positions[i] + vHalf[i] * dt;
        }

        // Compute new acceleration
        double[] aNew = acceleration.apply(xNew);

        // Full-step velocity: v(t + dt) = v(t + dt/2) + a(t + dt) * dt/2
        double[] vNew = new double[n];
        for (int i = 0; i < n; i++) {
            vNew[i] = vHalf[i] + 0.5 * aNew[i] * dt;
        }

        return new double[][] { xNew, vNew };
    }

    /**
     * Integrate over multiple time steps.
     * 
     * @param x0           Initial positions
     * @param v0           Initial velocities
     * @param acceleration Acceleration function
     * @param dt           Time step
     * @param steps        Number of steps
     * @return Final [positions, velocities]
     */
    public static double[][] integrate(
            double[] x0,
            double[] v0,
            java.util.function.Function<double[], double[]> acceleration,
            double dt,
            int steps) {

        double[] x = x0.clone();
        double[] v = v0.clone();

        for (int i = 0; i < steps; i++) {
            double[][] result = step(x, v, acceleration, dt);
            x = result[0];
            v = result[1];
        }

        return new double[][] { x, v };
    }

    /**
     * Integrate with trajectory recording.
     * 
     * @param x0           Initial positions
     * @param v0           Initial velocities
     * @param acceleration Acceleration function
     * @param dt           Time step
     * @param steps        Number of steps
     * @param recordEvery  Record every N steps
     * @return List of trajectories [time, positions, velocities]
     */
    public static java.util.List<double[][]> integrateWithTrajectory(
            double[] x0,
            double[] v0,
            java.util.function.Function<double[], double[]> acceleration,
            double dt,
            int steps,
            int recordEvery) {

        java.util.List<double[][]> trajectory = new java.util.ArrayList<>();
        double[] x = x0.clone();
        double[] v = v0.clone();
        double t = 0;

        trajectory.add(new double[][] { { t }, x.clone(), v.clone() });

        for (int i = 0; i < steps; i++) {
            double[][] result = step(x, v, acceleration, dt);
            x = result[0];
            v = result[1];
            t += dt;

            if ((i + 1) % recordEvery == 0) {
                trajectory.add(new double[][] { { t }, x.clone(), v.clone() });
            }
        }

        return trajectory;
    }

    /**
     * Leapfrog integration (alternative symplectic scheme).
     * <p>
     * Positions and velocities evaluated at staggered times.
     * </p>
     */
    public static double[][] leapfrogStep(
            double[] positions,
            double[] velocities,
            java.util.function.Function<double[], double[]> acceleration,
            double dt) {

        int n = positions.length;

        // v(t + dt/2) = v(t - dt/2) + a(t) * dt
        double[] a = acceleration.apply(positions);
        double[] vNew = new double[n];
        for (int i = 0; i < n; i++) {
            vNew[i] = velocities[i] + a[i] * dt;
        }

        // x(t + dt) = x(t) + v(t + dt/2) * dt
        double[] xNew = new double[n];
        for (int i = 0; i < n; i++) {
            xNew[i] = positions[i] + vNew[i] * dt;
        }

        return new double[][] { xNew, vNew };
    }
}
