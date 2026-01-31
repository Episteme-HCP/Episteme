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

package org.jscience.social.philosophy;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.jscience.core.mathematics.logic.proof.Proof;

/**
 * A logic engine for evaluating formal logical structures within philosophical 
 * discourse.
 * 
 * <p> This class provides high-level Aristotelian logic utilities (categorical 
 *     syllogisms) and serves as a bridge to the more rigorous mathematical 
 *     proof systems in {@code org.jscience.core.mathematics.logic.proof}.</p>
 *
 * @author <a href="mailto:silvere.martin-michiellot@jscience.org">Silvere Martin-Michiellot</a>
 * @author Gemini AI (Google DeepMind)
 * @version 6.0, July 21, 2014
 */
public final class LogicSolver {


    private LogicSolver() {}

    /**
     * Represents a categorical proposition (e.g., "All humans are mortal").
     * 
     * @param subject       Subject term (S)
     * @param predicate     Predicate term (P)
     * @param isAffirmative True if quality is affirmative, false if negative
     * @param isUniversal   True if quantity is universal, false if particular
     */
    public record Proposition(
        String subject,
        String predicate,
        boolean isAffirmative,
        boolean isUniversal
    ) implements Serializable {}

    /**
     * Checks if a categorical syllogism is valid based on classical Aristotelian rules.
     * Currently supports standard forms like Barbara (AAA-1) and Celarent (EAE-1).
     * <p>
     * For more advanced formal proof analysis involving predicate or propositional logic, 
     * see the {@link org.jscience.core.mathematics.logic.proof.ProofVerifier} in the core mathematics module.
     * </p>
     * 
     * @param major      Major proposition (contains predicate of conclusion)
     * @param minor      Minor proposition (contains subject of conclusion)
     * @param conclusion The conclusion to test
     * @return true if the syllogism is formally valid
     */
    public static boolean isValidSyllogism(Proposition major, Proposition minor, Proposition conclusion) {
        if (major == null || minor == null || conclusion == null) return false;

        // Barbara pattern (AAA-1)
        if (major.isUniversal() && major.isAffirmative() &&
            minor.isUniversal() && minor.isAffirmative() &&
            conclusion.isUniversal() && conclusion.isAffirmative()) {
            
            return major.predicate().equals(conclusion.predicate()) && 
                   minor.subject().equals(conclusion.subject()) &&     
                   major.subject().equals(minor.predicate());          
        }
        
        // Celarent pattern (EAE-1)
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
     * Integrates with the mathematical proof system to verify a complex argument.
     * 
     * @param proof the formal mathematical proof to verify
     * @return true if the proof is valid according to mathematical inference rules
     */
    public static boolean verifyFormalProof(Proof proof) {
        // Bridge to org.jscience.core.mathematics.logic.proof.ProofVerifier
        return new org.jscience.core.mathematics.logic.proof.ProofVerifier().verify(proof);
    }

    /**
     * Calculates the truth table result for a logical AND operation.
     * 
     * @param p First belief variable
     * @param q Second belief variable
     * @return Map containing representation and result
     */
    public static Map<String, Boolean> truthTableAnd(boolean p, boolean q) {
        return Map.of("p AND q", p && q);
    }

    /**
     * Checks for explicit logical contradictions within a system of beliefs.
     * 
     * @param beliefs List of beliefs to check
     * @return true if a contradiction is found
     */
    public static boolean hasContradiction(List<Belief> beliefs) {
        if (beliefs == null || beliefs.size() < 2) return false;

        for (int i = 0; i < beliefs.size(); i++) {
            String s1 = beliefs.get(i).getName();
            for (int j = i + 1; j < beliefs.size(); j++) {
                String s2 = beliefs.get(j).getName();
                
                if (s1.equalsIgnoreCase("NOT " + s2) || s2.equalsIgnoreCase("NOT " + s1)) {
                    return true;
                }
            }
        }
        return false;
    }
}

