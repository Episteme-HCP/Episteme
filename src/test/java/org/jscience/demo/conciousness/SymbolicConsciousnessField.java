package org.jscience.demo.conciousness;

import org.jscience.mathematics.number.Real;
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
 * The field potential is:
 * 
 * <pre>
 * V(Φ) = λ/4 (Φ² - Φ₀²)²
 * </pre>
 * 
 * where:
 * <ul>
 * <li>Φ is the consciousness field variable</li>
 * <li>Φ₀ is a constant parameter (vacuum expectation value)</li>
 * <li>λ is a constant parameter (self-coupling)</li>
 * </ul>
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
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

        // Create symbolic variable Φ
        this.phi = new Variable<>("Φ", Real.ZERO);

        // Build V(Φ) = λ/4 (Φ² - Φ₀²)²
        // First: Φ²
        Expression<Real> phiSquared = PolynomialExpression.ofVariable(phi, Real.ZERO)
                .multiply(PolynomialExpression.ofVariable(phi, Real.ZERO));

        // Φ² - Φ₀²
        Expression<Real> diff = phiSquared.add(PolynomialExpression.ofConstant(phi0.pow(2).negate(), Real.ZERO));

        // (Φ² - Φ₀²)²
        Expression<Real> diffSquared = diff.multiply(diff);

        // λ/4 as constant
        Real coefficient = lambda.divide(Real.of(4));

        // V(Φ) = λ/4 (Φ² - Φ₀²)²
        this.potential = PolynomialExpression.ofConstant(coefficient, Real.ZERO).multiply(diffSquared);

        // dV/dΦ
        this.derivative = potential.differentiate(phi);

        // d²V/dΦ²
        this.secondDerivative = derivative.differentiate(phi);
    }

    /**
     * Gets the symbolic field variable Φ.
     * 
     * @return the field variable
     */
    public Variable<Real> getFieldVariable() {
        return phi;
    }

    /**
     * Gets the symbolic potential expression V(Φ).
     * 
     * @return the potential expression
     */
    public Expression<Real> getPotential() {
        return potential;
    }

    /**
     * Gets the symbolic derivative dV/dΦ.
     * 
     * @return the derivative expression
     */
    public Expression<Real> getDerivative() {
        return derivative;
    }

    /**
     * Gets the symbolic second derivative d²V/dΦ².
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
     * @return V(Φ) evaluated at the given value
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
     * @return dV/dΦ evaluated at the given value
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
     * @return d²V/dΦ² evaluated at the given value
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
        return "V(Φ) = " + potential.toString();
    }

    /**
     * Returns a string representation of the derivative.
     * 
     * @return symbolic derivative as string
     */
    public String derivativeToString() {
        return "dV/dΦ = " + derivative.toString();
    }

    /**
     * Returns a string representation of the second derivative.
     * 
     * @return symbolic second derivative as string
     */
    public String secondDerivativeToString() {
        return "d²V/dΦ² = " + secondDerivative.toString();
    }

    public Real getLambda() {
        return lambda;
    }

    public Real getPhi0() {
        return phi0;
    }
}
