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
import org.junit.jupiter.api.Test;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for formal logic proof system.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ProofTest {

    @Test
    public void testModusPonensValidity() {
        ModusPonens mp = new ModusPonens();

        Formula p = new Formula("A");
        Formula imp = new Formula("A -> B");
        Formula q = new Formula("B");

        assertTrue(mp.isValid(Arrays.asList(p, imp), q), "Modus Ponens should be valid for A, A->B |- B");
        assertTrue(mp.isValid(Arrays.asList(imp, p), q),
                "Modus Ponens should be valid for A->B, A |- B (order shouldn't matter)");
    }

    @Test
    public void testModusPonensInvalidity() {
        ModusPonens mp = new ModusPonens();

        Formula p = new Formula("A");
        Formula imp = new Formula("A -> B");
        Formula wrongQ = new Formula("C");

        assertFalse(mp.isValid(Arrays.asList(p, imp), wrongQ), "Modus Ponens should be invalid for A, A->B |- C");

        Formula notImp = new Formula("B");
        assertFalse(mp.isValid(Arrays.asList(p, notImp), p), "Modus Ponens should be invalid without implication");
    }

    @Test
    public void testProofVerification() {
        ProofVerifier verifier = new ProofVerifier();
        verifier.registerRule("Modus Ponens", new ModusPonens());

        Proof proof = new Proof();

        // Step 1: A (Axiom)
        ProofStep step1 = new ProofStep(new Formula("A"), "Axiom");
        proof.addStep(step1);

        // Step 2: A -> B (Axiom)
        ProofStep step2 = new ProofStep(new Formula("A -> B"), "Axiom");
        proof.addStep(step2);

        // Step 3: B (Modus Ponens on 1, 2)
        ProofStep step3 = new ProofStep(new Formula("B"), "Modus Ponens", Arrays.asList(step1, step2));
        proof.addStep(step3);

        assertTrue(verifier.verify(proof), "Valid proof should be verified");
    }

    @Test
    public void testInvalidProofVerification() {
        ProofVerifier verifier = new ProofVerifier();
        verifier.registerRule("Modus Ponens", new ModusPonens());

        Proof proof = new Proof();

        // Step 1: A (Axiom)
        ProofStep step1 = new ProofStep(new Formula("A"), "Axiom");
        proof.addStep(step1);

        // Step 2: A -> B (Axiom)
        ProofStep step2 = new ProofStep(new Formula("A -> B"), "Axiom");
        proof.addStep(step2);

        // Step 3: C (Invalid Modus Ponens)
        ProofStep step3 = new ProofStep(new Formula("C"), "Modus Ponens", Arrays.asList(step1, step2));
        proof.addStep(step3);

        assertFalse(verifier.verify(proof), "Invalid proof should be rejected");
    }
}

