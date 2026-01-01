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
 * Represents a constant value as a symbolic expression.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ConstantExpression<T extends Ring<T>> implements Expression<T> {

    private final T value;
    private final Ring<T> ring;

    /**
     * Creates a constant expression.
     * 
     * @param value the constant value
     * @param ring  the ring structure
     */
    public ConstantExpression(T value, Ring<T> ring) {
        this.value = value;
        this.ring = ring;
    }

    @Override
    public Expression<T> add(Expression<T> other) {
        if (other instanceof ConstantExpression) {
            ConstantExpression<T> otherConst = (ConstantExpression<T>) other;
            return new ConstantExpression<>(ring.add(value, otherConst.value), ring);
        }
        return new SumExpression<>(this, other, ring);
    }

    @Override
    public Expression<T> multiply(Expression<T> other) {
        if (other instanceof ConstantExpression) {
            ConstantExpression<T> otherConst = (ConstantExpression<T>) other;
            return new ConstantExpression<>(ring.multiply(value, otherConst.value), ring);
        }
        return new ProductExpression<>(this, other, ring);
    }

    @Override
    public Expression<T> differentiate(Variable<T> variable) {
        // Derivative of a constant is zero
        return new ConstantExpression<>(ring.zero(), ring);
    }

    @Override
    public Expression<T> integrate(Variable<T> variable) {
        // Integral of constant c is c*x
        return new ProductExpression<>(this, variable, ring);
    }

    @Override
    public Expression<T> compose(Variable<T> variable, Expression<T> substitution) {
        // Constants don't change with substitution
        return this;
    }

    @Override
    public Expression<T> simplify() {
        // Constants are already simplified
        return this;
    }

    @Override
    public String toLatex() {
        return value.toString();
    }

    @Override
    public T evaluate(Map<Variable<T>, T> assignments) {
        return value;
    }

    /**
     * Returns the constant value.
     * 
     * @return the value
     */
    public T getValue() {
        return value;
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
        return value.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof ConstantExpression))
            return false;
        ConstantExpression<?> other = (ConstantExpression<?>) obj;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}

