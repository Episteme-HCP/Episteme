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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jscience.mathematics.logic.multivalued;

import org.jscience.mathematics.logic.Logic;

/**
 * Łukasiewicz infinite-valued logic.
 * <p>
 * In Łukasiewicz logic, truth values are real numbers in [0,1].
 * Operations are defined as:
 * - Negation: ¬a = 1 - a
 * - Implication: a → b = min(1, 1 - a + b)
 * - Conjunction: a ∧ b = max(0, a + b - 1)
 * - Disjunction: a ∨ b = min(1, a + b)
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LukasiewiczLogic implements Logic<Double> {

    private static final LukasiewiczLogic INSTANCE = new LukasiewiczLogic();

    private LukasiewiczLogic() {
    }

    /**
     * Returns the singleton instance.
     * 
     * @return the instance
     */
    public static LukasiewiczLogic getInstance() {
        return INSTANCE;
    }

    @Override
    public Double trueValue() {
        return 1.0;
    }

    @Override
    public Double falseValue() {
        return 0.0;
    }

    @Override
    public Double and(Double a, Double b) {
        // Łukasiewicz conjunction: max(0, a + b - 1)
        return Math.max(0.0, a + b - 1.0);
    }

    @Override
    public Double or(Double a, Double b) {
        // Łukasiewicz disjunction: min(1, a + b)
        return Math.min(1.0, a + b);
    }

    @Override
    public Double not(Double a) {
        // Łukasiewicz negation: 1 - a
        return 1.0 - a;
    }

    @Override
    public Double implies(Double a, Double b) {
        // Łukasiewicz implication: min(1, 1 - a + b)
        return Math.min(1.0, 1.0 - a + b);
    }

    /**
     * Validates that a value is in [0,1].
     * 
     * @param value the value to validate
     * @return the validated value
     * @throws IllegalArgumentException if value is not in [0,1]
     */
    public Double validate(Double value) {
        if (value < 0.0 || value > 1.0) {
            throw new IllegalArgumentException("Value must be in [0,1]: " + value);
        }
        return value;
    }

    /**
     * Computes the Łukasiewicz distance between two truth values.
     * 
     * @param a first value
     * @param b second value
     * @return |a - b|
     */
    public double distance(Double a, Double b) {
        return Math.abs(a - b);
    }

    @Override
    public String toString() {
        return "Łukasiewicz Logic";
    }
}