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

package org.jscience.mathematics.logic.proof;

import org.jscience.mathematics.logic.predicate.Formula;
import java.util.List;

/**
 * A simple inference rule for equality substitution.
 * If a = b and P(a), then P(b).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class EqualityRule implements InferenceRule {

    @Override
    public boolean isValid(List<Formula> premises, Formula conclusion) {
        // Very basic string matching for demonstration
        if (premises.isEmpty())
            return false;

        String conclStr = conclusion.getExpression().trim();

        // Transitivity: a = b, b = c |- a = c
        if (premises.size() == 2) {
            String p1 = premises.get(0).getExpression().trim();
            String p2 = premises.get(1).getExpression().trim();

            String[] eq1 = p1.split("=");
            String[] eq2 = p2.split("=");

            if (eq1.length == 2 && eq2.length == 2) {
                String a = eq1[0].trim();
                String b1 = eq1[1].trim();
                String b2 = eq2[0].trim();
                String c = eq2[1].trim();

                if (b1.equals(b2)) {
                    return conclStr.equals(a + " = " + c);
                }
            }
        }

        if (premises.size() == 2) {
            // Check substitution P(a), a=b |- P(b)
            // Order might be mixed.
            String p1 = premises.get(0).getExpression().trim();
            String p2 = premises.get(1).getExpression().trim();

            String eq = null;
            String p = "";

            if (p1.contains("=") && !p2.contains("=")) {
                eq = p1;
                p = p2;
            } else if (p2.contains("=") && !p1.contains("=")) {
                eq = p2;
                p = p1;
            }

            if (eq != null) {
                String[] parts = eq.split("=");
                if (parts.length == 2) {
                    String a = parts[0].trim();
                    String b = parts[1].trim();

                    // Check if P(b) is result of replacing a with b in P
                    String expected = p.replace(a, b);
                    return conclStr.equals(expected);
                }
            }
        }

        return false; // Safest default
    }
}


