package org.jscience.mathematics.analysis.integration;

import org.jscience.mathematics.analysis.RealFunction;
import org.jscience.mathematics.number.Real;

/**
 * Interface for numerical integration strategies.
 * <p>
 * Allows pluggable integration algorithms for computing definite integrals.
 * </p>
 * <p>
 * <b>Usage Example:</b>
 * 
 * <pre>{@code
 * Integrator integrator = new AdaptiveSimpsonIntegrator();
 * RealFunction f = x -> x.multiply(x); // f(x) = x²
 * Real result = integrator.integrate(f, Real.ZERO, Real.ONE); // ∫₀¹ x² dx
 * }</pre>
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface Integrator {

    /**
     * Computes the definite integral of a function over [a, b].
     *
     * @param f the function to integrate
     * @param a the lower bound
     * @param b the upper bound
     * @return the approximate value of the integral
     */
    Real integrate(RealFunction f, Real a, Real b);

    /**
     * Computes the definite integral with specified tolerance.
     *
     * @param f         the function to integrate
     * @param a         the lower bound
     * @param b         the upper bound
     * @param tolerance the desired accuracy
     * @return the approximate value of the integral
     */
    Real integrate(RealFunction f, Real a, Real b, Real tolerance);
}
