/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.symbolic;

import java.util.Map;
import org.jscience.mathematics.algebra.Ring;

/**
 * Generic interface for symbolic mathematical expressions.
 * <p>
 * This interface supports algebraic manipulation of symbolic expressions
 * over any Ring type T. Expressions can be added, multiplied, differentiated,
 * integrated, composed, and evaluated.
 * </p>
 * 
 * @param <T> the ring type for expression values
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface Expression<T extends Ring<T>> {

    /**
     * Adds this expression to another.
     *
     * @param other the expression to add
     * @return the sum expression
     */
    Expression<T> add(Expression<T> other);

    /**
     * Multiplies this expression by another.
     *
     * @param other the expression to multiply
     * @return the product expression
     */
    Expression<T> multiply(Expression<T> other);

    /**
     * Differentiates this expression with respect to a variable.
     *
     * @param variable the variable to differentiate with respect to
     * @return the derivative expression
     */
    Expression<T> differentiate(Variable<T> variable);

    /**
     * Integrates this expression with respect to a variable.
     *
     * @param variable the variable to integrate with respect to
     * @return the integral expression
     */
    Expression<T> integrate(Variable<T> variable);

    /**
     * Composes this expression by substituting a variable.
     *
     * @param variable     the variable to substitute
     * @param substitution the expression to substitute for the variable
     * @return the composed expression
     */
    Expression<T> compose(Variable<T> variable, Expression<T> substitution);

    /**
     * Evaluates this expression with given variable assignments.
     *
     * @param assignments map from variables to their values
     * @return the evaluated result
     */
    T evaluate(Map<Variable<T>, T> assignments);

    /**
     * Simplifies this expression algebraically.
     * <p>
     * Applies algebraic rules to reduce the expression to a simpler form, such as:
     * <ul>
     * <li>0 + x = x</li>
     * <li>1 * x = x</li>
     * <li>x^0 = 1</li>
     * <li>x^1 = x</li>
     * <li>Constant folding (e.g., 2 + 3 = 5)</li>
     * </ul>
     * </p>
     *
     * @return the simplified expression
     */
    Expression<T> simplify();

    /**
     * Returns the LaTeX representation of this expression.
     * <p>
     * Useful for rendering mathematical notation in documents, web pages,
     * or scientific publications.
     * </p>
     *
     * @return the LaTeX string representation
     */
    String toLatex();
}
