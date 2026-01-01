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
 * Represents a product of two expressions.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ProductExpression<T extends Ring<T>> implements Expression<T> {

    private final Expression<T> left;
    private final Expression<T> right;
    private final Ring<T> ring;

    /**
     * Creates a product expression.
     * 
     * @param left  the left operand
     * @param right the right operand
     * @param ring  the ring structure
     */
    public ProductExpression(Expression<T> left, Expression<T> right, Ring<T> ring) {
        this.left = left;
        this.right = right;
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
        // Product rule: (f * g)' = f' * g + f * g'
        Expression<T> leftDeriv = left.differentiate(variable);
        Expression<T> rightDeriv = right.differentiate(variable);
        Expression<T> term1 = new ProductExpression<>(leftDeriv, right, ring);
        Expression<T> term2 = new ProductExpression<>(left, rightDeriv, ring);
        return new SumExpression<>(term1, term2, ring);
    }

    @Override
    public Expression<T> integrate(Variable<T> variable) {
        throw new UnsupportedOperationException("Integration of products not yet implemented");
    }

    @Override
    public Expression<T> compose(Variable<T> variable, Expression<T> substitution) {
        return new ProductExpression<>(left.compose(variable, substitution), right.compose(variable, substitution),
                ring);
    }

    @Override
    public Expression<T> simplify() {
        Expression<T> l = left.simplify();
        Expression<T> r = right.simplify();

        // 0 * x = 0
        if (l instanceof ConstantExpression) {
            ConstantExpression<T> lc = (ConstantExpression<T>) l;
            if (lc.getValue().equals(lc.getRing().zero())) {
                return new ConstantExpression<>(ring.zero(), ring);
            }
        }
        if (r instanceof ConstantExpression) {
            ConstantExpression<T> rc = (ConstantExpression<T>) r;
            if (rc.getValue().equals(rc.getRing().zero())) {
                return new ConstantExpression<>(ring.zero(), ring);
            }
        }

        // 1 * x = x
        if (l instanceof ConstantExpression) {
            ConstantExpression<T> lc = (ConstantExpression<T>) l;
            if (lc.getValue().equals(lc.getRing().one())) {
                return r;
            }
        }
        if (r instanceof ConstantExpression) {
            ConstantExpression<T> rc = (ConstantExpression<T>) r;
            if (rc.getValue().equals(rc.getRing().one())) {
                return l;
            }
        }

        // c1 * c2 = c3 (constant folding)
        if (l instanceof ConstantExpression && r instanceof ConstantExpression) {
            ConstantExpression<T> lc = (ConstantExpression<T>) l;
            ConstantExpression<T> rc = (ConstantExpression<T>) r;
            T product = ring.multiply(lc.getValue(), rc.getValue());
            return new ConstantExpression<>(product, ring);
        }

        return new ProductExpression<>(l, r, ring);
    }

    @Override
    public String toLatex() {
        return "(" + left.toLatex() + " \\cdot " + right.toLatex() + ")";
    }

    @Override
    public T evaluate(Map<Variable<T>, T> assignments) {
        return ring.multiply(left.evaluate(assignments), right.evaluate(assignments));
    }

    /**
     * Returns the left operand.
     * 
     * @return the left expression
     */
    public Expression<T> getLeft() {
        return left;
    }

    /**
     * Returns the right operand.
     * 
     * @return the right expression
     */
    public Expression<T> getRight() {
        return right;
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
        return "(" + left + " * " + right + ")";
    }
}

