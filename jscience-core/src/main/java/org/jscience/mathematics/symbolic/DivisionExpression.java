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

package org.jscience.mathematics.symbolic;

import java.util.Map;
import org.jscience.mathematics.structures.rings.Ring;

/**
 * Represents a division of two expressions.
 * <p>
 * Note: Division requires the denominator to be non-zero.
 * This implementation uses Ring operations where possible.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DivisionExpression<T extends Ring<T>> implements Expression<T> {

    private final Expression<T> numerator;
    private final Expression<T> denominator;
    private final Ring<T> ring;

    /**
     * Creates a division expression.
     * 
     * @param numerator   the numerator
     * @param denominator the denominator
     * @param ring        the ring structure
     */
    public DivisionExpression(Expression<T> numerator, Expression<T> denominator, Ring<T> ring) {
        this.numerator = numerator;
        this.denominator = denominator;
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
    public Expression<T> differentiate(Variable<T> variable) {
        // Quotient rule: (f/g)' = (f'g - fg') / gÃ‚Â²
        Expression<T> fPrime = numerator.differentiate(variable);
        Expression<T> gPrime = denominator.differentiate(variable);

        Expression<T> term1 = new ProductExpression<>(fPrime, denominator, ring);
        Expression<T> term2 = new ProductExpression<>(numerator, gPrime, ring);
        Expression<T> numeratorResult = new SumExpression<>(term1, negate(term2), ring);

        Expression<T> denominatorSquared = new ProductExpression<>(denominator, denominator, ring);

        return new DivisionExpression<>(numeratorResult, denominatorSquared, ring);
    }

    @Override
    public Expression<T> integrate(Variable<T> variable) {
        throw new UnsupportedOperationException("Integration of quotients not yet implemented");
    }

    @Override
    public Expression<T> compose(Variable<T> variable, Expression<T> substitution) {
        return new DivisionExpression<>(numerator.compose(variable, substitution),
                denominator.compose(variable, substitution), ring);
    }

    @Override
    public Expression<T> simplify() {
        Expression<T> n = numerator.simplify();
        Expression<T> d = denominator.simplify();

        // x / 1 = x
        if (d instanceof ConstantExpression) {
            ConstantExpression<T> dc = (ConstantExpression<T>) d;
            if (dc.getValue().equals(dc.getRing().one())) {
                return n;
            }
        }

        return new DivisionExpression<>(n, d, ring);
    }

    @Override
    public String toLatex() {
        return "\\frac{" + numerator.toLatex() + "}{" + denominator.toLatex() + "}";
    }

    @Override
    public T evaluate(Map<Variable<T>, T> assignments) {
        T num = numerator.evaluate(assignments);
        T den = denominator.evaluate(assignments);

        // For Real, we can use divide
        if (num instanceof org.jscience.mathematics.numbers.real.Real) {
            org.jscience.mathematics.numbers.real.Real realNum = (org.jscience.mathematics.numbers.real.Real) num;
            org.jscience.mathematics.numbers.real.Real realDen = (org.jscience.mathematics.numbers.real.Real) den;
            @SuppressWarnings("unchecked")
            T result = (T) realNum.divide(realDen);
            return result;
        }

        throw new UnsupportedOperationException("Division only supported for Real type");
    }

    /**
     * Returns the numerator.
     * 
     * @return the numerator
     */
    public Expression<T> getNumerator() {
        return numerator;
    }

    /**
     * Returns the denominator.
     * 
     * @return the denominator
     */
    public Expression<T> getDenominator() {
        return denominator;
    }

    /**
     * Returns the ring structure.
     * 
     * @return the ring
     */
    public Ring<T> getRing() {
        return ring;
    }

    @Override
    public String toString() {
        return "(" + numerator + " / " + denominator + ")";
    }

    /**
     * Helper to negate an expression.
     */
    private Expression<T> negate(Expression<T> expr) {
        ConstantExpression<T> minusOne = new ConstantExpression<>(ring.negate(ring.one()), ring);
        return new ProductExpression<>(minusOne, expr, ring);
    }
}

