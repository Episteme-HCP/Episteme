package org.jscience.mathematics.analysis;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a real-valued function of a real variable (R -> R).
 * <p>
 * Provides capabilities for numerical analysis, such as differentiation
 * and integration.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface RealFunction extends DifferentiableFunction<Real, Real>, IntegrableFunction<Real, Real> {

    /**
     * Returns the derivative of this function.
     * 
     * @return the derivative function f'
     */
    default RealFunction derivative() {
        // Default to numerical differentiation using Ridders' method
        return x -> Differentiation.derivativeAt(this, x);
    }

    @Override
    default Function<Real, Real> differentiate() {
        return derivative();
    }

    /**
     * Returns the integral of this function (antiderivative) with zero constant.
     * 
     * @return the antiderivative function F such that F' = f
     * @throws UnsupportedOperationException if symbolic integration is not
     *                                       supported
     */
    @Override
    default RealFunction integrate() {
        // Return numerical antiderivative F(x) = ∫[0,x] f(t) dt
        return x -> integrate(Real.ZERO, x);
    }

    /**
     * Integrates this function over a definite interval [a, b].
     * 
     * @param a the lower bound
     * @param b the upper bound
     * @return the definite integral value
     */
    @Override
    default Real integrate(Real a, Real b) {
        // Default to Adaptive Simpson's method
        return Integration.integrate(this, a, b);
    }

    static RealFunction identity() {
        return x -> x;
    }
}
