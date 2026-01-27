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

package org.jscience.philosophy.epistemology;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Dimensionless;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.SimpleIdentification;
import org.jscience.util.identity.ComprehensiveIdentification;

/**
 * Represents the justification or grounds for a belief or knowledge claim.
 * Evidence can take many forms: empirical observation, logical proof, testimonial weight, etc.
 * Modernized to implement ComprehensiveIdentification and use EvidenceKind.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public abstract class Evidence implements ComprehensiveIdentification {

    private static final long serialVersionUID = 2L;

    private final Identification id;
    private final Map<String, Object> traits = new HashMap<>();

    private final Quantity<Dimensionless> reliability; // 0.0 to 1.0
    private final EvidenceKind kind;

    /**
     * Creates new Evidence.
     *
     * @param source      where the evidence comes from
     * @param description what the evidence consists of
     * @param reliability the estimated reliability of the source (0.0 to 1.0)
     * @param kind        the kind of evidence
     */
    protected Evidence(String source, String description, Quantity<Dimensionless> reliability, EvidenceKind kind) {
        this.id = new SimpleIdentification("Evidence:" + UUID.randomUUID());
        setName(Objects.requireNonNull(source, "Source cannot be null"));
        setComments(description);
        this.reliability = Objects.requireNonNull(reliability, "Reliability cannot be null");
        this.kind = Objects.requireNonNull(kind, "Kind cannot be null");
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    public String getSource() {
        return getName();
    }

    public String getDescription() {
        return getComments();
    }

    /**
     * Returns the reliability of the evidence.
     * @return a value between 0.0 and 1.0 (Unit.ONE)
     */
    public Quantity<Dimensionless> getReliability() {
        return reliability;
    }

    /**
     * Returns the kind of evidence (Empirical, Rational, etc.).
     * @return the evidence kind
     */
    public EvidenceKind getKind() {
        return kind;
    }

    /**
     * Legacy method for the evidence type name.
     * @return the evidence type name
     */
    public String getEvidenceType() {
        return kind.name();
    }

    @Override
    public String toString() {
        return kind.name() + " Evidence from " + getName() + " (Reliability: " + reliability + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Evidence other)) return false;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
