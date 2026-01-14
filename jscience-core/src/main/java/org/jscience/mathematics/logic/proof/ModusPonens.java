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

package org.jscience.mathematics.logic.proof;

import org.jscience.mathematics.logic.predicate.Formula;
import java.util.List;

/**
 * Implementation of the Modus Ponens inference rule.
 * <p>
 * From P and P -> Q, infer Q.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ModusPonens implements InferenceRule {

    @Override
    public boolean isValid(List<Formula> premises, Formula conclusion) {
        if (premises.size() != 2) {
            return false;
        }

        String p1 = premises.get(0).getExpression().trim();
        String p2 = premises.get(1).getExpression().trim();
        String q = conclusion.getExpression().trim();

        // Check if p1 is P and p2 is P -> Q
        if (checkImplication(p1, p2, q)) {
            return true;
        }

        // Check if p2 is P and p1 is P -> Q
        if (checkImplication(p2, p1, q)) {
            return true;
        }

        return false;
    }

    private boolean checkImplication(String antecedent, String implication, String consequent) {
        // Simple string parsing for "->"
        // This is a basic implementation and assumes the implication is in the format
        // "P -> Q"
        // It does not handle complex nesting or parentheses correctly without a full
        // parser.

        String arrow = "->";
        int arrowIndex = implication.indexOf(arrow);

        if (arrowIndex == -1) {
            return false;
        }

        String implAntecedent = implication.substring(0, arrowIndex).trim();
        String implConsequent = implication.substring(arrowIndex + arrow.length()).trim();

        // Remove outer parentheses if present (basic handling)
        if (implAntecedent.startsWith("(") && implAntecedent.endsWith(")")) {
            implAntecedent = implAntecedent.substring(1, implAntecedent.length() - 1).trim();
        }
        if (implConsequent.startsWith("(") && implConsequent.endsWith(")")) {
            implConsequent = implConsequent.substring(1, implConsequent.length() - 1).trim();
        }

        if (antecedent.startsWith("(") && antecedent.endsWith(")")) {
            antecedent = antecedent.substring(1, antecedent.length() - 1).trim();
        }
        if (consequent.startsWith("(") && consequent.endsWith(")")) {
            consequent = consequent.substring(1, consequent.length() - 1).trim();
        }

        return implAntecedent.equals(antecedent) && implConsequent.equals(consequent);
    }
}

