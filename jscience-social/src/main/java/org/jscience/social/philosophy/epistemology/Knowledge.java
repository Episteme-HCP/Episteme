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

package org.jscience.social.philosophy.epistemology;

import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.quantity.Dimensionless;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.SimpleIdentification;
import org.jscience.core.util.identity.ComprehensiveIdentification;
import org.jscience.social.philosophy.Belief;

import java.util.Objects;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents Knowledge according to the classical definition: Justified True Belief (JTB).
 * 
 * <p>Knowledge requires:
 * 1. A Proposition that is True.
 * 2. A Subject who has a Belief in that proposition.
 * 3. Evidence that provides Justification for the belief.</p>
 * Modernized to implement ComprehensiveIdentification and support dynamic traits and consistent identity.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Knowledge implements ComprehensiveIdentification {

    private static final long serialVersionUID = 2L;

    private final Identification id;
    private final Map<String, Object> traits = new HashMap<>();

    private final Proposition proposition;
    private final Belief belief;
    private final Evidence justification;

    /**
     * Creates a new Knowledge instance.
     * 
     * @param proposition   the true proposition
     * @param belief        the subject's belief in the proposition
     * @param justification the evidence justifying the belief
     * @throws IllegalArgumentException if the proposition is not true or metadata doesn't match
     */
    public Knowledge(Proposition proposition, Belief belief, Evidence justification) {
        this.id = new SimpleIdentification("Knowledge:" + UUID.randomUUID());
        this.proposition = Objects.requireNonNull(proposition, "Proposition cannot be null");
        this.belief = Objects.requireNonNull(belief, "Belief cannot be null");
        this.justification = Objects.requireNonNull(justification, "Justification cannot be null");

        if (!proposition.isTrue()) {
            throw new IllegalArgumentException("Knowledge requires a TRUE proposition.");
        }
        
        setName("Knowledge of " + proposition.getName());
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    /**
     * Returns the proposition that is known.
     * @return the proposition
     */
    public Proposition getProposition() {
        return proposition;
    }

    /**
     * Returns the belief associated with this knowledge.
     * @return the belief
     */
    public Belief getBelief() {
        return belief;
    }

    /**
     * Returns the justification for this knowledge.
     * @return the evidence
     */
    public Evidence getJustification() {
        return justification;
    }

    /**
     * Returns the epistemic strength of this knowledge based on evidence reliability.
     * @return confidence level (0.0 to 1.0)
     */
    public Quantity<Dimensionless> getCertainty() {
        return justification.getReliability();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Knowledge that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Knowledge: " + proposition.getContent() + " (Justified by " + justification.getName() + ")";
    }
}

