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

package org.jscience.demo.conciousness;

import org.jscience.mathematics.numbers.real.Real;

import org.jscience.mathematics.symbolic.Expression;
import org.jscience.mathematics.symbolic.Variable;
import org.jscience.mathematics.symbolic.PolynomialExpression;
import java.util.Map;
import java.util.HashMap;

/**
 * Symbolic representation of the Consciousness Field model.
 * <p>
 * Provides symbolic expressions for the potential function and its derivatives,
 * enabling symbolic manipulation, differentiation, and simplification.
 * </p>
 * <p>
 * <strong>Reference:</strong>
 * StrÃƒÂ¸mme, M. (2025). "Universal consciousness as foundational field: A
 * theoretical
 * bridge between quantum physics and non-dual philosophy." AIP Advances,
 * 15(11).
 * DOI: <a href="https://doi.org/10.1063/5.0290984">10.1063/5.0290984</a>
 * </p>
 * <p>
 * The field potential is:
 *
 * <pre>
 * V(ÃŽÂ¦) = ÃŽÂ»/4 (ÃŽÂ¦Ã‚Â² - ÃŽÂ¦Ã¢â€šâ‚¬Ã‚Â²)Ã‚Â²
 * </pre>
 *
 * where:
 * <ul>
 * <li>ÃŽÂ¦ is the consciousness field variable</li>
 * <li>ÃŽÂ¦Ã¢â€šâ‚¬ is a constant parameter (vacuum expectation value)</li>
 * <li>ÃŽÂ» is a constant parameter (self-coupling)</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SymbolicConsciousnessField {

    private final Variable<Real> phi;
    private final Expression<Real> potential;
    private final Expression<Real> derivative;
    private final Expression<Real> secondDerivative;
    private final Real lambda;
    private final Real phi0;

    /**
     * Creates a symbolic consciousness field model.
     * 
     * @param lambda self-coupling constant
     * @param phi0   vacuum expectation value
     */
    public SymbolicConsciousnessField(Real lambda, Real phi0) {
        if (lambda.sign() <= 0) {
            throw new IllegalArgumentException("Coupling constant lambda must be positive");
        }
        this.lambda = lambda;
        this.phi0 = phi0;

        // Create symbolic variable ÃŽÂ¦
        this.phi = new Variable<>("ÃŽÂ¦", Real.ZERO);

        // Build V(ÃŽÂ¦) = ÃŽÂ»/4 (ÃŽÂ¦Ã‚Â² - ÃŽÂ¦Ã¢â€šâ‚¬Ã‚Â²)Ã‚Â²
        // First: ÃŽÂ¦Ã‚Â²
        Expression<Real> phiSquared = PolynomialExpression.ofVariable(phi, Real.ZERO)
                .multiply(PolynomialExpression.ofVariable(phi, Real.ZERO));

        // ÃŽÂ¦Ã‚Â² - ÃŽÂ¦Ã¢â€šâ‚¬Ã‚Â²
        Expression<Real> diff = phiSquared.add(PolynomialExpression.ofConstant(phi0.pow(2).negate(), Real.ZERO));

        // (ÃŽÂ¦Ã‚Â² - ÃŽÂ¦Ã¢â€šâ‚¬Ã‚Â²)Ã‚Â²
        Expression<Real> diffSquared = diff.multiply(diff);

        // ÃŽÂ»/4 as constant
        Real coefficient = lambda.divide(Real.of(4));

        // V(ÃŽÂ¦) = ÃŽÂ»/4 (ÃŽÂ¦Ã‚Â² - ÃŽÂ¦Ã¢â€šâ‚¬Ã‚Â²)Ã‚Â²
        this.potential = PolynomialExpression.ofConstant(coefficient, Real.ZERO).multiply(diffSquared);

        // dV/dÃŽÂ¦
        this.derivative = potential.differentiate(phi);

        // dÃ‚Â²V/dÃŽÂ¦Ã‚Â²
        this.secondDerivative = derivative.differentiate(phi);
    }

    /**
     * Gets the symbolic field variable ÃŽÂ¦.
     * 
     * @return the field variable
     */
    public Variable<Real> getFieldVariable() {
        return phi;
    }

    /**
     * Gets the symbolic potential expression V(ÃŽÂ¦).
     * 
     * @return the potential expression
     */
    public Expression<Real> getPotential() {
        return potential;
    }

    /**
     * Gets the symbolic derivative dV/dÃŽÂ¦.
     * 
     * @return the derivative expression
     */
    public Expression<Real> getDerivative() {
        return derivative;
    }

    /**
     * Gets the symbolic second derivative dÃ‚Â²V/dÃŽÂ¦Ã‚Â².
     * 
     * @return the second derivative expression
     */
    public Expression<Real> getSecondDerivative() {
        return secondDerivative;
    }

    /**
     * Evaluates the potential at a specific field value.
     * 
     * @param phiValue the field value
     * @return V(ÃŽÂ¦) evaluated at the given value
     */
    public Real evaluatePotential(Real phiValue) {
        Map<Variable<Real>, Real> assignments = new HashMap<>();
        assignments.put(phi, phiValue);
        return potential.evaluate(assignments);
    }

    /**
     * Evaluates the derivative at a specific field value.
     * 
     * @param phiValue the field value
     * @return dV/dÃŽÂ¦ evaluated at the given value
     */
    public Real evaluateDerivative(Real phiValue) {
        Map<Variable<Real>, Real> assignments = new HashMap<>();
        assignments.put(phi, phiValue);
        return derivative.evaluate(assignments);
    }

    /**
     * Evaluates the second derivative at a specific field value.
     * 
     * @param phiValue the field value
     * @return dÃ‚Â²V/dÃŽÂ¦Ã‚Â² evaluated at the given value
     */
    public Real evaluateSecondDerivative(Real phiValue) {
        Map<Variable<Real>, Real> assignments = new HashMap<>();
        assignments.put(phi, phiValue);
        return secondDerivative.evaluate(assignments);
    }

    /**
     * Returns a string representation of the potential.
     * 
     * @return symbolic potential as string
     */
    public String potentialToString() {
        return "V(ÃŽÂ¦) = " + potential.toString();
    }

    /**
     * Returns a string representation of the derivative.
     * 
     * @return symbolic derivative as string
     */
    public String derivativeToString() {
        return "dV/dÃŽÂ¦ = " + derivative.toString();
    }

    /**
     * Returns a string representation of the second derivative.
     * 
     * @return symbolic second derivative as string
     */
    public String secondDerivativeToString() {
        return "dÃ‚Â²V/dÃŽÂ¦Ã‚Â² = " + secondDerivative.toString();
    }

    public Real getLambda() {
        return lambda;
    }

    public Real getPhi0() {
        return phi0;
    }
}


