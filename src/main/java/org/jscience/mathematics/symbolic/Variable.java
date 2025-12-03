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
import java.util.Objects;
import org.jscience.mathematics.algebra.Ring;

/**
 * Represents a symbolic variable (e.g., "x", "y", "theta").
 * 
 * @param <T> the type of number used in evaluation
 * 
 * @author Silvere Martin-Michiellot
 * @since 2.0
 */
public class Variable<T extends Ring<T>> implements Expression<T> {

    private final String symbol;
    private final Ring<T> ring;

    /**
     * Creates a new variable with the given symbol and ring.
     * 
     * @param symbol the variable symbol (e.g., "x")
     * @param ring   the ring this variable belongs to
     */
    public Variable(String symbol, Ring<T> ring) {
        if (symbol == null || symbol.isEmpty()) {
            throw new IllegalArgumentException("Symbol cannot be null or empty");
        }
        if (ring == null) {
            throw new IllegalArgumentException("Ring cannot be null");
        }
        this.symbol = symbol;
        this.ring = ring;
    }

    public String getSymbol() {
        return symbol;
    }

    public Ring<T> getRing() {
        return ring;
    }

    @Override
    public Expression<T> add(Expression<T> other) {
        return PolynomialExpression.ofVariable(this, ring).add(other);
    }

    @Override
    public Expression<T> multiply(Expression<T> other) {
        return PolynomialExpression.ofVariable(this, ring).multiply(other);
    }

    @Override
    public Expression<T> differentiate(Variable<T> variable) {
        if (this.equals(variable)) {
            return PolynomialExpression.ofConstant(ring.one(), ring);
        } else {
            return PolynomialExpression.ofConstant(ring.zero(), ring);
        }
    }

    @Override
    public Expression<T> integrate(Variable<T> variable) {
        if (this.equals(variable)) {
            // Integral of x dx = 1/2 * x^2
            // We need 1/2 in the ring. If ring is integer, this might fail or be integer
            // division.
            // Assuming ring supports division or we return a polynomial with coefficients
            // in the ring.
            // PolynomialExpression handles this.
            return PolynomialExpression.ofVariable(this, ring).integrate(variable);
        } else {
            // Integral of c dx = c*x
            return PolynomialExpression.ofVariable(this, ring).integrate(variable);
        }
    }

    @Override
    public Expression<T> compose(Variable<T> variable, Expression<T> substitution) {
        if (this.equals(variable)) {
            return substitution;
        }
        return this;
    }

    @Override
    public T evaluate(Map<Variable<T>, T> assignments) {
        if (assignments.containsKey(this)) {
            return assignments.get(this);
        }
        throw new IllegalArgumentException("No value assigned for variable: " + symbol);
    }

    @Override
    public Expression<T> simplify() {
        // Variables are already simplified
        return this;
    }

    @Override
    public String toLatex() {
        // Simple symbol conversion for common cases
        if (symbol.equals("theta")) {
            return "\\theta";
        } else if (symbol.equals("phi")) {
            return "\\phi";
        } else if (symbol.equals("alpha")) {
            return "\\alpha";
        } else if (symbol.equals("beta")) {
            return "\\beta";
        } else if (symbol.equals("gamma")) {
            return "\\gamma";
        } else if (symbol.equals("lambda")) {
            return "\\lambda";
        } else if (symbol.length() > 1) {
            // Multi-character symbols become mathrm
            return "\\mathrm{" + symbol + "}";
        }
        return symbol; // Single letter as-is
    }

    @Override
    public String toString() {
        return symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Variable<?> variable = (Variable<?>) o;
        return Objects.equals(symbol, variable.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }
}
