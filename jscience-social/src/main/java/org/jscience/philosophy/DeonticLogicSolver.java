package org.jscience.philosophy;

import java.util.*;

/**
 * Logic of obligation and permission (Deontic Logic).
 */
public final class DeonticLogicSolver {

    private DeonticLogicSolver() {}

    public enum DeonticMode {
        OBLIGATORY, PERMISSIBLE, FORBIDDEN, OPTIONAL
    }

    /**
     * Basic Deontic Square relations.
     * O(p) -> It is obligatory that p.
     * P(p) -> It is permissible that p (¬O¬p).
     * F(p) -> It is forbidden that p (O¬p).
     */
    public static DeonticMode resolveMode(boolean obligatory, boolean forbidden) {
        if (obligatory && forbidden) throw new IllegalStateException("Contradiction: O(p) and F(p)");
        if (obligatory) return DeonticMode.OBLIGATORY;
        if (forbidden) return DeonticMode.FORBIDDEN;
        return DeonticMode.PERMISSIBLE;
    }

    /**
     * Checks if a set of norms is consistent.
     */
    public static boolean isNormativelyConsistent(Map<String, DeonticMode> norms) {
        for (var entry : norms.entrySet()) {
            String p = entry.getKey();
            DeonticMode m = entry.getValue();
            
            // If p is obligatory, ¬p must not be obligatory
            if (m == DeonticMode.OBLIGATORY) {
                if (norms.getOrDefault("NOT " + p, DeonticMode.OPTIONAL) == DeonticMode.OBLIGATORY) {
                    return false;
                }
            }
        }
        return true;
    }
}
