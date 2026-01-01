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
 * Paraconsistent logic.
 * <p>
 * Paraconsistent logic tolerates contradictions without trivializing
 * (principle of explosion does NOT hold).
 * Uses 4 truth values: True, False, Both, Neither.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ParaconsistentLogic implements Logic<ParaconsistentLogic.TruthValue> {

    private static final ParaconsistentLogic INSTANCE = new ParaconsistentLogic();

    public enum TruthValue {
        TRUE, FALSE, BOTH, NEITHER
    }

    private ParaconsistentLogic() {
    }

    public static ParaconsistentLogic getInstance() {
        return INSTANCE;
    }

    @Override
    public TruthValue trueValue() {
        return TruthValue.TRUE;
    }

    @Override
    public TruthValue falseValue() {
        return TruthValue.FALSE;
    }

    @Override
    public TruthValue and(TruthValue a, TruthValue b) {
        if (a == TruthValue.FALSE || b == TruthValue.FALSE) {
            return TruthValue.FALSE;
        }
        if (a == TruthValue.TRUE && b == TruthValue.TRUE) {
            return TruthValue.TRUE;
        }
        if (a == TruthValue.BOTH || b == TruthValue.BOTH) {
            return TruthValue.BOTH;
        }
        return TruthValue.NEITHER;
    }

    @Override
    public TruthValue or(TruthValue a, TruthValue b) {
        if (a == TruthValue.TRUE || b == TruthValue.TRUE) {
            return TruthValue.TRUE;
        }
        if (a == TruthValue.FALSE && b == TruthValue.FALSE) {
            return TruthValue.FALSE;
        }
        if (a == TruthValue.BOTH || b == TruthValue.BOTH) {
            return TruthValue.BOTH;
        }
        return TruthValue.NEITHER;
    }

    @Override
    public TruthValue not(TruthValue a) {
        switch (a) {
            case TRUE:
                return TruthValue.FALSE;
            case FALSE:
                return TruthValue.TRUE;
            case BOTH:
                return TruthValue.BOTH; // Contradiction remains
            case NEITHER:
                return TruthValue.NEITHER;
            default:
                return TruthValue.NEITHER;
        }
    }

    @Override
    public TruthValue implies(TruthValue a, TruthValue b) {
        return or(not(a), b);
    }

    /**
     * Checks if a value represents a contradiction.
     * 
     * @param value the truth value
     * @return true if BOTH
     */
    public boolean isContradiction(TruthValue value) {
        return value == TruthValue.BOTH;
    }

    /**
     * Checks if a value is undetermined.
     * 
     * @param value the truth value
     * @return true if NEITHER
     */
    public boolean isUndetermined(TruthValue value) {
        return value == TruthValue.NEITHER;
    }

    @Override
    public String toString() {
        return "Paraconsistent Logic (4-valued)";
    }
}

