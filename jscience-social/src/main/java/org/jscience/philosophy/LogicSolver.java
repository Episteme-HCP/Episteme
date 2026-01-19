package org.jscience.philosophy;

import java.util.*;

/**
 * Basic logic engine for propositional and categorical syllogisms.
 */
public final class LogicSolver {

    private LogicSolver() {}

    public record Proposition(String subject, String predicate, boolean isAffirmative, boolean isUniversal) {}

    /**
     * Checks if a syllogism is valid based on Aristotelian rules.
     * (Barbara, Celarent, etc.)
     */
    public static boolean isValidSyllogism(Proposition major, Proposition minor, Proposition conclusion) {
        // Simplified check: Barbara pattern (AAA-1)
        // All M are P. All S are M. Therefore, All S are P.
        if (major.isUniversal() && major.isAffirmative() &&
            minor.isUniversal() && minor.isAffirmative() &&
            conclusion.isUniversal() && conclusion.isAffirmative()) {
            
            return major.predicate().equals(conclusion.predicate()) &&
                   minor.subject().equals(conclusion.subject()) &&
                   major.subject().equals(minor.predicate());
        }
        
        // Celarent pattern (EAE-1)
        // No M are P. All S are M. Therefore, No S are P.
        if (!major.isAffirmative() && major.isUniversal() &&
            minor.isUniversal() && minor.isAffirmative() &&
            !conclusion.isAffirmative() && conclusion.isUniversal()) {
            
            return major.predicate().equals(conclusion.predicate()) &&
                   minor.subject().equals(conclusion.subject()) &&
                   major.subject().equals(minor.predicate());
        }
        
        return false;
    }

    /**
     * Calculates truth table for a 2-variable expression.
     */
    public static Map<String, Boolean> truthTableAnd(boolean p, boolean q) {
        return Map.of("p && q", p && q);
    }

    /**
     * Checks for logical contradiction in a list of beliefs.
     */
    public static boolean hasContradiction(List<Belief> beliefs) {
        for (int i = 0; i < beliefs.size(); i++) {
            for (int j = i + 1; j < beliefs.size(); j++) {
                if (beliefs.get(i).getStatement().equals("NOT " + beliefs.get(j).getStatement()) ||
                    beliefs.get(j).getStatement().equals("NOT " + beliefs.get(i).getStatement())) {
                    return true;
                }
            }
        }
        return false;
    }
}
