/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.philosophy;

import java.util.Map;

/**
 * Provides logic algorithms for norms, obligations, and permissions (Deontic Logic).
 * 
 * <p> Deontic logic is the field of philosophical logic that is concerned with 
 *     obligation, permission, and related concepts. This solver supports the 
 *     analysis of moral codes and normative consistency through the standard 
 *     Deontic Square.</p>
 * */
public final class DeonticLogicSolver {


    private DeonticLogicSolver() {}

    /**
     * Categories of normative status.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
    public enum DeonticMode {
        /** Required by norm (O). */
        OBLIGATORY,
        /** Allowed by norm (P). Allowed to be or not to be. */
        PERMISSIBLE,
        /** Not allowed by norm (F). */
        FORBIDDEN,
        /** Neither obligatory nor forbidden. */
        OPTIONAL
    }

    /**
     * Resolves the deontic status of an action based on its requirement and prohibition status.
     * 
     * @param obligatory true if the action is required
     * @param forbidden  true if the action is prohibited
     * @return the resulting DeonticMode
     * @throws IllegalStateException if a contradiction (both obligatory and forbidden) is detected
     */
    public static DeonticMode resolveMode(boolean obligatory, boolean forbidden) {
        if (obligatory && forbidden) {
            throw new IllegalStateException("Normative Contradiction: Action is both Obligatory and Forbidden.");
        }
        if (obligatory) return DeonticMode.OBLIGATORY;
        if (forbidden) return DeonticMode.FORBIDDEN;
        return DeonticMode.PERMISSIBLE;
    }

    /**
     * Verifies if a set of norms is logically consistent.
     * Checks if any action is both obligatory and has an obligatory negation.
     * 
     * @param norms Map of action descriptions to their deontic status
     * @return true if no internal contradictions are found
     */
    public static boolean isNormativelyConsistent(Map<String, DeonticMode> norms) {
        if (norms == null) return true;
        
        for (Map.Entry<String, DeonticMode> entry : norms.entrySet()) {
            String action = entry.getKey();
            DeonticMode mode = entry.getValue();
            
            if (mode == DeonticMode.OBLIGATORY) {
                // Check if NOT action is also obligatory
                if (norms.getOrDefault("NOT " + action, DeonticMode.OPTIONAL) == DeonticMode.OBLIGATORY) {
                    return false;
                }
            }
        }
        return true;
    }
}

