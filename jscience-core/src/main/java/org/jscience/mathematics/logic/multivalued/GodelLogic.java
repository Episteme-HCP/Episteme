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

package org.jscience.mathematics.logic.multivalued;

import org.jscience.mathematics.logic.Logic;

/**
 * GÃƒÂ¶del infinite-valued logic (also called GÃƒÂ¶del-Dummett logic).
 * <p>
 * In GÃƒÂ¶del logic, truth values are real numbers in [0,1].
 * Operations are defined as:
 * - Negation: Ã‚Â¬a = 1 if a = 0, else 0
 * - Implication: a Ã¢â€ â€™ b = 1 if a Ã¢â€°Â¤ b, else b
 * - Conjunction: a Ã¢Ë†Â§ b = min(a, b)
 * - Disjunction: a Ã¢Ë†Â¨ b = max(a, b)
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GodelLogic implements Logic<Double> {

    private static final GodelLogic INSTANCE = new GodelLogic();
    private static final double EPSILON = 1e-10;

    private GodelLogic() {
    }

    /**
     * Returns the singleton instance.
     * 
     * @return the instance
     */
    public static GodelLogic getInstance() {
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
        // GÃƒÂ¶del conjunction: min(a, b)
        return Math.min(a, b);
    }

    @Override
    public Double or(Double a, Double b) {
        // GÃƒÂ¶del disjunction: max(a, b)
        return Math.max(a, b);
    }

    @Override
    public Double not(Double a) {
        // GÃƒÂ¶del negation: 1 if a = 0, else 0
        return Math.abs(a) < EPSILON ? 1.0 : 0.0;
    }

    @Override
    public Double implies(Double a, Double b) {
        // GÃƒÂ¶del implication: 1 if a Ã¢â€°Â¤ b, else b
        return a <= b + EPSILON ? 1.0 : b;
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
     * Checks if two values are equal within epsilon.
     * 
     * @param a first value
     * @param b second value
     * @return true if approximately equal
     */
    public boolean approximatelyEqual(Double a, Double b) {
        return Math.abs(a - b) < EPSILON;
    }

    @Override
    public String toString() {
        return "GÃƒÂ¶del Logic";
    }
}

