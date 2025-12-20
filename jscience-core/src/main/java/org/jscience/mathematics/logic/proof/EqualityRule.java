package org.jscience.mathematics.logic.proof;

import org.jscience.mathematics.logic.predicate.Formula;
import java.util.List;

/**
 * A simple inference rule for equality substitution.
 * If a = b and P(a), then P(b).
 * Also supports transitivity: a = b, b = c -> a = c.
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
