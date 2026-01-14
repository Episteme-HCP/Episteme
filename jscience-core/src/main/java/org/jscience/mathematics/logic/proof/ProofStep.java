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
import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a single step in a formal proof.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ProofStep {

    private final Formula formula;
    private final String justification;
    private final List<ProofStep> premises;

    /**
     * Creates a new proof step.
     * 
     * @param formula       the formula asserted at this step
     * @param justification description of the justification (e.g., "Axiom", "Modus
     *                      Ponens")
     * @param premises      list of previous steps used as premises for this step
     */
    public ProofStep(Formula formula, String justification, List<ProofStep> premises) {
        this.formula = formula;
        this.justification = justification;
        this.premises = new ArrayList<>(premises);
    }

    /**
     * Creates a new proof step with no premises (e.g., an axiom).
     * 
     * @param formula       the formula asserted
     * @param justification description of the justification
     */
    public ProofStep(Formula formula, String justification) {
        this(formula, justification, Collections.emptyList());
    }

    public Formula getFormula() {
        return formula;
    }

    public String getJustification() {
        return justification;
    }

    public List<ProofStep> getPremises() {
        return Collections.unmodifiableList(premises);
    }

    @Override
    public String toString() {
        return formula.toString() + " [" + justification + "]";
    }
}

