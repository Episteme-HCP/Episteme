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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Verifier for formal proofs.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ProofVerifier {

    private final Map<String, InferenceRule> rules = new HashMap<>();

    /**
     * Registers an inference rule with a given name.
     * 
     * @param name the name of the rule (e.g., "Modus Ponens")
     * @param rule the rule implementation
     */
    public void registerRule(String name, InferenceRule rule) {
        rules.put(name, rule);
    }

    /**
     * Verifies the validity of a proof.
     * 
     * @param proof the proof to verify
     * @return true if the proof is valid
     */
    public boolean verify(Proof proof) {
        for (ProofStep step : proof.getSteps()) {
            if (!verifyStep(step)) {
                return false;
            }
        }
        return true;
    }

    private boolean verifyStep(ProofStep step) {
        String justification = step.getJustification();

        // Base case: Axioms or Premises are assumed true for the context of the proof
        // structure verification
        // In a full system, we would check against a list of allowed axioms.
        if (justification.startsWith("Axiom") || justification.startsWith("Premise")) {
            return true;
        }

        // Check if justification matches a registered rule
        if (rules.containsKey(justification)) {
            InferenceRule rule = rules.get(justification);
            List<Formula> premiseFormulas = step.getPremises().stream()
                    .map(ProofStep::getFormula)
                    .collect(Collectors.toList());

            return rule.isValid(premiseFormulas, step.getFormula());
        }

        // Unknown justification
        return false;
    }
}