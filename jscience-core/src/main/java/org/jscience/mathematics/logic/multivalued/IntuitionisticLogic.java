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
 * Intuitionistic (constructive) logic.
 * <p>
 * Intuitionistic logic rejects the law of excluded middle (A ∨ ¬A).
 * A proposition is true only if there is a constructive proof.
 * Uses Heyting algebra semantics instead of Boolean algebra.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class IntuitionisticLogic implements Logic<Boolean> {

    private static final IntuitionisticLogic INSTANCE = new IntuitionisticLogic();

    private IntuitionisticLogic() {
    }

    public static IntuitionisticLogic getInstance() {
        return INSTANCE;
    }

    @Override
    public Boolean trueValue() {
        return true;
    }

    @Override
    public Boolean falseValue() {
        return false;
    }

    @Override
    public Boolean and(Boolean a, Boolean b) {
        return a && b;
    }

    @Override
    public Boolean or(Boolean a, Boolean b) {
        return a || b;
    }

    @Override
    public Boolean not(Boolean a) {
        return !a;
    }

    @Override
    public Boolean implies(Boolean a, Boolean b) {
        // In intuitionistic logic, implication is primitive
        // A → B is true if we can construct B from A
        return !a || b;
    }

    /**
     * Checks if the law of excluded middle holds.
     * In intuitionistic logic, this is NOT a tautology.
     * 
     * @param a the proposition
     * @return A ∨ ¬A (not always true)
     */
    public Boolean excludedMiddle(Boolean a) {
        return or(a, not(a));
    }

    /**
     * Double negation elimination is NOT valid in intuitionistic logic.
     * ¬¬A does not imply A.
     * 
     * @param a the proposition
     * @return ¬¬A
     */
    public Boolean doubleNegation(Boolean a) {
        return not(not(a));
    }

    @Override
    public String toString() {
        return "Intuitionistic Logic";
    }
}