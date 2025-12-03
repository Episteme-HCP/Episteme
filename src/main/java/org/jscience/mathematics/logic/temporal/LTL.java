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
package org.jscience.mathematics.logic.temporal;

import java.util.List;

/**
 * Linear Temporal Logic (LTL) implementation.
 * <p>
 * Provides semantics for evaluating LTL formulas over a linear sequence of
 * states.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class LTL {

    /**
     * Evaluates if a property holds in the next state.
     * 
     * @param trace        sequence of states (valuations)
     * @param currentIndex current time step
     * @param property     the property to check
     * @return true if property holds in the next state
     */
    public boolean next(List<Boolean> trace, int currentIndex, boolean property) {
        if (currentIndex + 1 >= trace.size()) {
            return false; // Finite trace semantics: false if no next state
        }
        // In a real implementation, 'property' would be evaluated against
        // trace.get(currentIndex + 1)
        // Here we simplify for the interface structure
        return trace.get(currentIndex + 1);
    }

    /**
     * Validates if a formula is an allowed axiom.
     * 
     * @param formula       the formula to check
     * @param allowedAxioms list of allowed axioms
     * @return true if allowed
     */
    public boolean validate(String formula, List<String> allowedAxioms) {
        // In a full system, we would check against a list of allowed axioms.
        return allowedAxioms.contains(formula);
    }

    /**
     * Evaluates a simple implication formula "P -> Q".
     * 
     * @param formula the formula string
     * @param p       truth value of antecedent P
     * @param q       truth value of consequent Q
     * @return truth value of P -> Q
     */
    public boolean evaluateImplication(String formula, boolean p, boolean q) {
        // Simple string parsing for "->"
        // This is a basic implementation and assumes the implication is in the format
        // "P -> Q"
        // It does not handle complex nesting or parentheses correctly without a full
        // parser.
        if (formula.contains("->")) {
            return !p || q;
        }
        throw new IllegalArgumentException("Formula is not an implication: " + formula);
    }
}

