/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.core.mathematics.symbolic;

import java.util.Map;
import org.episteme.core.mathematics.structures.rings.Ring;

/**
 * Represents a symbolic mathematical expression.
 * <p>
 * Expressions support algebraic operations, differentiation, integration,
 * composition, simplification, and evaluation.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Expression<T extends Ring<T>> {

    /**
     * Adds this expression to another.
     * 
     * @param other the other expression
     * @return the sum expression
     */
    Expression<T> add(Expression<T> other);

    /**
     * Multiplies this expression by another.
     * 
     * @param other the other expression
     * @return the product expression
     */
    Expression<T> multiply(Expression<T> other);

    /**
     * Subtracts another expression from this.
     * 
     * @param other the subtrahend
     * @return the difference expression
     */
    Expression<T> subtract(Expression<T> other);

    /**
     * Negates this expression.
     * 
     * @return the negated expression
     */
    Expression<T> negate();

    /**
     * Divides this expression by another.
     * 
     * @param other the divisor
     * @return the quotient expression
     */
    Expression<T> divide(Expression<T> other);

    /**
     * Raises this expression to an integer power.
     * 
     * @param n the exponent
     * @return the power expression
     */
    default Expression<T> pow(int n) {
        if (n == 0) return ConstantExpression.ofConstant(getRing().one(), getRing());
        if (n == 1) return this;
        Expression<T> result = this;
        for (int i = 1; i < n; i++) {
            result = result.multiply(this);
        }
        return result;
    }

    /**
     * Returns the ring this expression belongs to.
     * 
     * @return the ring
     */
    Ring<T> getRing();

    /**
     * Differentiates this expression with respect to the given variable.
     * 
     * @param variable the variable to differentiate by
     * @return the derivative expression
     */
    Expression<T> differentiate(Variable<T> variable);

    /**
     * Integrates this expression with respect to the given variable.
     * 
     * @param variable the variable to integrate by
     * @return the integral expression
     */
    Expression<T> integrate(Variable<T> variable);

    /**
     * Composes this expression by substituting a variable with another expression.
     * 
     * @param variable     the variable to substitute
     * @param substitution the expression to substitute with
     * @return the composed expression
     */
    Expression<T> compose(Variable<T> variable, Expression<T> substitution);

    /**
     * Simplifies this expression using algebraic rules.
     * 
     * @return the simplified expression
     */
    Expression<T> simplify();

    /**
     * Converts this expression to LaTeX format.
     * 
     * @return the LaTeX string
     */
    String toLatex();

    /**
     * Evaluates this expression with the given variable assignments.
     * 
     * @param assignments the variable assignments
     * @return the evaluated value
     */
    T evaluate(Map<Variable<T>, T> assignments);
}


