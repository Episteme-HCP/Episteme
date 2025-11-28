package org.jscience.mathematics.analysis.ode;

import org.jscience.mathematics.analysis.Function;
import org.jscience.mathematics.number.Real;

/**
 * Ordinary Differential Equation (ODE) solvers.
 * <p>
 * Solves initial value problems dy/dt = f(t, y) with y(t₀) = y₀.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class ODESolver {

    /**
     * Euler's method: yₙ₊₁ = yₙ + h*f(tₙ, yₙ)
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

            Real increment = h.divide(Real.of(6)).multiply(
                    k1.add(k2.multiply(Real.of(2))).add(k3.multiply(Real.of(2))).add(k4));

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
