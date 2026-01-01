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

package org.jscience.mathematics.analysis.chaos;

import org.jscience.mathematics.numbers.real.Real;

/**
 * The Gingerbread Man Map - a chaotic discrete dynamical system.
 * <p>
 * Defined by the recurrence relations:
 *
 * <pre>
 * x(n+1) = 1 - y(n) + |x(n)|
 * y(n+1) = x(n)
 * </pre>
 *
 * This map produces a distinctive gingerbread-man-shaped fractal attractor.
 * </p>
 *
 * <h2>Properties</h2>
 * <ul>
 * <li>Strange attractor with fractal structure</li>
 * <li>Basin of attraction includes most of the plane</li>
 * <li>Exhibits sensitive dependence on initial conditions</li>
 * </ul>
 *
 * <h2>References</h2>
 * <ul>
 * <li>Devaney, R. L. (1989). "An Introduction to Chaotic Dynamical
 * Systems"</li>
 * </ul>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GingerbreadManMap implements DiscreteMap<Real[]> {

    @Override
    public Real[] evaluate(Real[] state) {
        if (state == null || state.length != 2) {
            throw new IllegalArgumentException("State must be a 2D vector [x, y]");
        }

        Real x = state[0];
        Real y = state[1];

        // x(n+1) = 1 - y(n) + |x(n)|
        Real xNext = Real.ONE.subtract(y).add(x.abs());

        // y(n+1) = x(n)
        Real yNext = x;

        return new Real[] { xNext, yNext };
    }

    public int getDimension() {
        return 2;
    }

    /**
     * Generates a trajectory starting from initial conditions.
     * 
     * @param x0         initial x coordinate
     * @param y0         initial y coordinate
     * @param iterations number of iterations
     * @return array of points [x, y] forming the trajectory
     */
    public Real[][] generateTrajectory(double x0, double y0, int iterations) {
        Real[][] trajectory = new Real[iterations][2];
        Real[] current = new Real[] { Real.of(x0), Real.of(y0) };

        for (int i = 0; i < iterations; i++) {
            trajectory[i] = current;
            current = evaluate(current);
        }

        return trajectory;
    }

    /**
     * Typical initial conditions for visualization.
     * 
     * @return array [x0, y0]
     */
    public static double[] typicalInitialConditions() {
        return new double[] { 0.0, 0.0 };
    }

    /**
     * Suggested plot bounds for the attractor.
     * 
     * @return array [xMin, xMax, yMin, yMax]
     */
    public static double[] plotBounds() {
        return new double[] { -5.0, 5.0, -5.0, 5.0 };
    }

    @Override
    public String toString() {
        return "GingerbreadManMap: x' = 1 - y + |x|, y' = x";
    }
}

