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

package org.jscience.linguistics.kif;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A trivial structure to hold the elements of a proof step.
 */
public class ProofStep {
    /**
     * A String containing a valid KIF expression, that is the axiom
     * expressing the conclusion of this proof step.
     */
    public String axiom = null;

    /**
     * The number assigned to this proof step, initially by Vampire and
     * then normalized by ProofStep.normalizeProofStepNumbers()
     */
    public Integer number = new Integer(0);

    /**
     * An ArrayList of Integer(s), which reference prior proof steps
     * from which this axiom is derived. 0 signifies the axiom is directly
     * from the knowledge base, rather than deduced. Note that the numbering
     * is what the ProofProcessor assigns, not necessarily the proof numbers
     * returned directly from Vampire.
     */
    public ArrayList premises = new ArrayList();

    /**
     * Take an ArrayList of ProofSteps and renumber them consecutively
     * starting at 1.  Update the ArrayList of premises so that they reflect
     * the renumbering.
     *
     * @param proofSteps DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ArrayList normalizeProofStepNumbers(ArrayList proofSteps) {
        HashMap numberingMap = new HashMap();

        for (int i = 0; i < proofSteps.size(); i++) {
            ProofStep ps = (ProofStep) proofSteps.get(i);
            numberingMap.put(ps.number, new Integer(i + 1));
            ps.number = new Integer(i + 1);

            for (int j = 0; j < ps.premises.size(); j++) {
                ps.premises.set(j,
                    (Integer) numberingMap.get(ps.premises.get(j)));
            }
        }

        return proofSteps;
    }
}
