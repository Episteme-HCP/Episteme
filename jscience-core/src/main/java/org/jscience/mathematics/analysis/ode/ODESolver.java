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

package org.jscience.mathematics.analysis.ode;

import org.jscience.mathematics.analysis.Function;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Ordinary Differential Equation (ODE) solvers.
 * <p>
 * Solves initial value problems dy/dt = f(t, y) with y(t₀) = y₀.
 * </p>
 *
 * <p>
 * References:
 * <ul>
 * <li>Hairer, E., Nørsett, S. P., & Wanner, G. (1993). Solving Ordinary
 * Differential Equations I: Nonstiff Problems. Springer.</li>
 * <li>Butcher, J. C. (2008). Numerical Methods for Ordinary Differential
 * Equations. Wiley.</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ODESolver {
    /**
     * Euler's method: y_{n+1} = y_n + h*f(t_n, y_n)
     * <p>
     * First-order method. Simple but low accuracy O(h).
     * </p>
     */
    public static Real[] euler(Function<Real[], Real> f, Real t0, Real y0, Real tEnd, Real h) {
        int steps = (int) Math.ceil(tEnd.subtract(t0).divide(h).doubleValue());
        Real[] solution = new Real[steps + 1];

        solution[0] = y0;
        Real t = t0;

        for (int i = 0; i < steps; i++) {
            Real k = f.evaluate(new Real[] { t, solution[i] });
            solution[i + 1] = solution[i].add(h.multiply(k));
            t = t.add(h);
        }

        return solution;
    }

    /**
     * Runge-Kutta 4th order (RK4): Classical method.
     * <p>
     * Fourth-order accuracy O(h⁴). Industry standard for ODE solving.
     * </p>
     */
    public static Real[] rungeKutta4(Function<Real[], Real> f, Real t0, Real y0, Real tEnd, Real h) {
        int steps = (int) Math.ceil(tEnd.subtract(t0).divide(h).doubleValue());
        Real[] solution = new Real[steps + 1];

        solution[0] = y0;
        Real t = t0;

        for (int i = 0; i < steps; i++) {
            Real y = solution[i];

            Real k1 = f.evaluate(new Real[] { t, y });
            Real k2 = f.evaluate(new Real[] { t.add(h.divide(Real.of(2))), y.add(h.multiply(k1).divide(Real.of(2))) });
            Real k3 = f.evaluate(new Real[] { t.add(h.divide(Real.of(2))), y.add(h.multiply(k2).divide(Real.of(2))) });
            Real k4 = f.evaluate(new Real[] { t.add(h), y.add(h.multiply(k3)) });

            Real increment = h.divide(Real.of(6))
                    .multiply(k1.add(k2.multiply(Real.of(2))).add(k3.multiply(Real.of(2))).add(k4));

            solution[i + 1] = y.add(increment);
            t = t.add(h);
        }

        return solution;
    }

    /**
     * Midpoint method (2nd order Runge-Kutta).
     */
    public static Real[] midpoint(Function<Real[], Real> f, Real t0, Real y0, Real tEnd, Real h) {
        int steps = (int) Math.ceil(tEnd.subtract(t0).divide(h).doubleValue());
        Real[] solution = new Real[steps + 1];

        solution[0] = y0;
        Real t = t0;

        for (int i = 0; i < steps; i++) {
            Real y = solution[i];
            Real k1 = f.evaluate(new Real[] { t, y });
            Real k2 = f.evaluate(new Real[] { t.add(h.divide(Real.of(2))), y.add(h.multiply(k1).divide(Real.of(2))) });

            solution[i + 1] = y.add(h.multiply(k2));
            t = t.add(h);
        }

        return solution;
    }
}
