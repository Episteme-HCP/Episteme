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

package org.jscience.mathematics.symbolic;

import java.util.Map;
import org.jscience.mathematics.structures.rings.Ring;

/**
 * Represents a symbolic integral.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class IntegralExpression<T extends Ring<T>> implements Expression<T> {

    private final Expression<T> integrand;
    private final Variable<T> variable;
    private final Ring<T> ring;

    /**
     * Creates an integral expression.
     * 
     * @param integrand the expression to integrate
     * @param variable  the integration variable
     * @param ring      the ring structure
     */
    public IntegralExpression(Expression<T> integrand, Variable<T> variable, Ring<T> ring) {
        this.integrand = integrand;
        this.variable = variable;
        this.ring = ring;
    }

    @Override
    public Expression<T> add(Expression<T> other) {
        return new SumExpression<>(this, other, ring);
    }

    @Override
    public Expression<T> multiply(Expression<T> other) {
        return new ProductExpression<>(this, other, ring);
    }

    @Override
    public Expression<T> differentiate(Variable<T> v) {
        // Fundamental Theorem of Calculus: d/dx ∫ f(t) dt
        if (v.equals(this.variable)) {
            return integrand;
        }
        // If differentiating by another variable, assume independence or mixed
        // derivative
        // d/dy ∫ f(x) dx = ∫ (d/dy f(x)) dx
        return new IntegralExpression<>(integrand.differentiate(v), variable, ring);
    }

    @Override
    public Expression<T> integrate(Variable<T> v) {
        return new IntegralExpression<>(this, v, ring);
    }

    @Override
    public Expression<T> compose(Variable<T> v, Expression<T> substitution) {
        // Composition in integral is complex (change of variables).
        // For now, just symbolic substitution if variable matches.
        return new IntegralExpression<>(integrand.compose(v, substitution), variable, ring);
    }

    @Override
    public Expression<T> simplify() {
        return new IntegralExpression<>(integrand.simplify(), variable, ring);
    }

    @Override
    public String toLatex() {
        return "\\int " + integrand.toLatex() + " \\, d" + variable.toLatex();
    }

    @Override
    public T evaluate(Map<Variable<T>, T> assignments) {
        throw new UnsupportedOperationException("Numeric evaluation of symbolic integrals not supported");
    }

    @Override
    public String toString() {
        return "∫(" + integrand + ") d" + variable;
    }
}
