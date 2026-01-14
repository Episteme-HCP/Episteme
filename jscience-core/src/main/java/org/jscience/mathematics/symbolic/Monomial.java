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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.jscience.mathematics.structures.rings.Ring;

/**
 * Represents a monomial, which is a product of variables raised to non-negative
 * integer powers.
 * Example: x^2 * y^1
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Monomial<T extends Ring<T>> implements Comparable<Monomial<T>> {

    private final Map<Variable<T>, Integer> powers;

    public Monomial(Map<Variable<T>, Integer> powers) {
        // Filter out zero powers
        Map<Variable<T>, Integer> cleanPowers = new HashMap<>();
        powers.forEach((v, p) -> {
            if (p > 0) {
                cleanPowers.put(v, p);
            }
        });
        this.powers = Collections.unmodifiableMap(cleanPowers);
    }

    public static <T extends Ring<T>> Monomial<T> one() {
        return new Monomial<>(Collections.emptyMap());
    }

    public static <T extends Ring<T>> Monomial<T> of(Variable<T> v) {
        return new Monomial<>(Collections.singletonMap(v, 1));
    }

    /**
     * Returns the degree of this monomial (sum of exponents).
     */
    public int degree() {
        return powers.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Multiplies this monomial by another.
     */
    public Monomial<T> multiply(Monomial<T> other) {
        Map<Variable<T>, Integer> newPowers = new HashMap<>(this.powers);
        other.powers.forEach((v, exp) -> newPowers.merge(v, exp, (a, b) -> a + b));
        return new Monomial<>(newPowers);
    }

    /**
     * Returns the power of a specific variable.
     */
    public int getPower(Variable<T> v) {
        return powers.getOrDefault(v, 0);
    }

    public Map<Variable<T>, Integer> getPowers() {
        return powers;
    }

    @Override
    public String toString() {
        if (powers.isEmpty())
            return "1";

        // Sort by variable symbol for consistent output
        return powers.entrySet().stream()
                .sorted((e1, e2) -> e1.getKey().getSymbol().compareTo(e2.getKey().getSymbol()))
                .map(e -> {
                    if (e.getValue() == 1)
                        return e.getKey().toString();
                    return e.getKey().toString() + "^" + e.getValue();
                })
                .collect(Collectors.joining("*"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Monomial<?> monomial = (Monomial<?>) o;
        return Objects.equals(powers, monomial.powers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(powers);
    }

    @Override
    public int compareTo(Monomial<T> o) {
        // Graded lexicographic order
        int degDiff = Integer.compare(this.degree(), o.degree());
        if (degDiff != 0)
            return degDiff;

        // Lexicographic comparison of string representations as a fallback for
        // stability
        return this.toString().compareTo(o.toString());
    }
}

