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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.jscience.mathematics.structures.rings.Ring;
import org.jscience.mathematics.structures.rings.Field;

/**
 * Represents a multivariate polynomial expression.
 * Sum of terms, where each term is a coefficient * Monomial.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PolynomialExpression<T extends Ring<T>> implements Expression<T> {

    private final Map<Monomial<T>, T> terms;
    private final Ring<T> ring;

    /**
     * Creates a polynomial expression.
     * 
     * @param terms map of monomial to coefficient
     * @param ring  the ring for coefficient arithmetic
     */
    public PolynomialExpression(Map<Monomial<T>, T> terms, Ring<T> ring) {
        this.ring = ring;
        // Filter out zero coefficients
        Map<Monomial<T>, T> cleanTerms = new HashMap<>();
        terms.forEach((m, c) -> {
            if (!c.equals(ring.zero())) {
                cleanTerms.put(m, c);
            }
        });
        this.terms = Collections.unmodifiableMap(cleanTerms);
    }

    public static <T extends Ring<T>> PolynomialExpression<T> ofConstant(T value, Ring<T> ring) {
        Map<Monomial<T>, T> terms = new HashMap<>();
        terms.put(Monomial.<T>one(), value);
        return new PolynomialExpression<>(terms, ring);
    }

    public static <T extends Ring<T>> PolynomialExpression<T> ofVariable(Variable<T> v, Ring<T> ring) {
        Map<Monomial<T>, T> terms = new HashMap<>();
        terms.put(Monomial.of(v), ring.one());
        return new PolynomialExpression<>(terms, ring);
    }

    @Override
    public Expression<T> add(Expression<T> other) {
        if (other instanceof PolynomialExpression) {
            return addPoly((PolynomialExpression<T>) other);
        } else if (other instanceof Variable) {
            return addPoly(PolynomialExpression.ofVariable((Variable<T>) other, ring));
        }
        // Fallback or error? For now assume everything converts to Poly
        throw new IllegalArgumentException("Cannot add " + other.getClass().getSimpleName());
    }

    private PolynomialExpression<T> addPoly(PolynomialExpression<T> other) {
        Map<Monomial<T>, T> newTerms = new HashMap<>(this.terms);
        other.terms.forEach((m, c) -> newTerms.merge(m, c, ring::add));
        return new PolynomialExpression<>(newTerms, ring);
    }

    @Override
    public Expression<T> multiply(Expression<T> other) {
        if (other instanceof PolynomialExpression) {
            return multiplyPoly((PolynomialExpression<T>) other);
        } else if (other instanceof Variable) {
            return multiplyPoly(PolynomialExpression.ofVariable((Variable<T>) other, ring));
        }
        throw new IllegalArgumentException("Cannot multiply " + other.getClass().getSimpleName());
    }

    private PolynomialExpression<T> multiplyPoly(PolynomialExpression<T> other) {
        Map<Monomial<T>, T> newTerms = new HashMap<>();

        for (Map.Entry<Monomial<T>, T> entry1 : this.terms.entrySet()) {
            for (Map.Entry<Monomial<T>, T> entry2 : other.terms.entrySet()) {
                Monomial<T> newMono = entry1.getKey().multiply(entry2.getKey());
                T newCoeff = ring.multiply(entry1.getValue(), entry2.getValue());
                newTerms.merge(newMono, newCoeff, ring::add);
            }
        }

        return new PolynomialExpression<>(newTerms, ring);
    }

    @Override
    public Expression<T> differentiate(Variable<T> variable) {
        Map<Monomial<T>, T> newTerms = new HashMap<>();

        for (Map.Entry<Monomial<T>, T> entry : terms.entrySet()) {
            Monomial<T> m = entry.getKey();
            T c = entry.getValue();

            int power = m.getPower(variable);
            if (power > 0) {
                // d/dx(c * x^n * y^m) = c * n * x^(n-1) * y^m

                // Create new monomial with power - 1
                Map<Variable<T>, Integer> newPowers = new HashMap<>(m.getPowers());
                if (power == 1) {
                    newPowers.remove(variable);
                } else {
                    newPowers.put(variable, power - 1);
                }
                Monomial<T> newMono = new Monomial<>(newPowers);

                // Calculate new coefficient: c * power
                T newCoeff = c;
                for (int i = 1; i < power; i++) {
                    newCoeff = ring.add(newCoeff, c);
                }

                newTerms.merge(newMono, newCoeff, ring::add);
            }
        }
        return new PolynomialExpression<>(newTerms, ring);
    }

    @Override
    public Expression<T> integrate(Variable<T> variable) {
        if (!(ring instanceof Field)) {
            throw new UnsupportedOperationException("Integration requires coefficients to be a Field");
        }
        Field<T> field = (Field<T>) ring;

        Map<Monomial<T>, T> newTerms = new HashMap<>();

        for (Map.Entry<Monomial<T>, T> entry : terms.entrySet()) {
            Monomial<T> m = entry.getKey();
            T c = entry.getValue();

            int power = m.getPower(variable);
            // int x^n dx = x^(n+1) / (n+1)

            int newPower = power + 1;

            // Create new monomial
            Map<Variable<T>, Integer> newPowers = new HashMap<>(m.getPowers());
            newPowers.put(variable, newPower);
            Monomial<T> newMono = new Monomial<>(newPowers);

            // Calculate new coefficient: c / (n+1)
            T divisor = field.one();
            for (int i = 1; i < newPower; i++) {
                divisor = field.add(divisor, field.one());
            }
            T inverse = field.inverse(divisor);
            T newCoeff = field.multiply(c, inverse);

            newTerms.put(newMono, newCoeff);
        }
        return new PolynomialExpression<>(newTerms, ring);
    }

    @Override
    public Expression<T> compose(Variable<T> variable, Expression<T> substitution) {
        Expression<T> result = PolynomialExpression.ofConstant(ring.zero(), ring);

        for (Map.Entry<Monomial<T>, T> entry : terms.entrySet()) {
            Monomial<T> m = entry.getKey();
            T c = entry.getValue();

            int power = m.getPower(variable);

            // Term without the variable
            Map<Variable<T>, Integer> remainingPowers = new HashMap<>(m.getPowers());
            remainingPowers.remove(variable);
            Monomial<T> remainingMono = new Monomial<>(remainingPowers);
            Expression<T> term = new PolynomialExpression<>(Collections.singletonMap(remainingMono, c), ring);

            // Multiply by substitution^power
            Expression<T> subPow = PolynomialExpression.ofConstant(ring.one(), ring);
            for (int i = 0; i < power; i++) {
                subPow = subPow.multiply(substitution);
            }

            result = result.add(term.multiply(subPow));
        }
        return result;
    }

    @Override
    public T evaluate(Map<Variable<T>, T> assignments) {
        T result = ring.zero();

        for (Map.Entry<Monomial<T>, T> entry : terms.entrySet()) {
            Monomial<T> m = entry.getKey();
            T c = entry.getValue();

            T termValue = c;
            for (Map.Entry<Variable<T>, Integer> powerEntry : m.getPowers().entrySet()) {
                Variable<T> v = powerEntry.getKey();
                int power = powerEntry.getValue();

                if (!assignments.containsKey(v)) {
                    throw new IllegalArgumentException("No value assigned for variable: " + v);
                }
                T val = assignments.get(v);

                for (int i = 0; i < power; i++) {
                    termValue = ring.multiply(termValue, val);
                }
            }

            result = ring.add(result, termValue);
        }

        return result;
    }

    @Override
    public String toString() {
        if (terms.isEmpty())
            return "0";

        // Sort terms by monomial order
        return terms.entrySet().stream()
                .sorted((e1, e2) -> e2.getKey().compareTo(e1.getKey())) // Descending order
                .map(e -> {
                    String coeffStr = e.getValue().toString();
                    String monoStr = e.getKey().toString();

                    if (monoStr.equals("1"))
                        return coeffStr;
                    if (e.getValue().equals(ring.one()))
                        return monoStr;
                    return coeffStr + "*" + monoStr;
                })
                .collect(Collectors.joining(" +"));
    }

    @Override
    public Expression<T> simplify() {
        // Already simplified by removing zero coefficients in constructor
        return this;
    }

    @Override
    public String toLatex() {
        if (terms.isEmpty())
            return "0";

        return terms.entrySet().stream()
                .sorted((e1, e2) -> e2.getKey().compareTo(e1.getKey()))
                .map(e -> {
                    String coeffStr = e.getValue().toString();
                    String monoStr = e.getKey().toString(); // Would use .toLatex() if Monomial had it

                    if (monoStr.equals("1"))
                        return coeffStr;
                    if (e.getValue().equals(ring.one()))
                        return monoStr;
                    return coeffStr + monoStr;
                })
                .collect(Collectors.joining(" + "));
    }
}

