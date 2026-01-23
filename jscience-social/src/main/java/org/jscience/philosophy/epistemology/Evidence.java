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

import java.io.Serializable;
import org.jscience.util.Commented;
import org.jscience.util.Named;

/**
 * Represents the justification or grounds for a belief or knowledge claim.
 * Evidence can take many forms: empirical observation, logical proof, testimonial weight, etc.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public abstract class Evidence implements Named, Commented, Serializable {

    private static final long serialVersionUID = 1L;

    private final String source;
    private final String description;
    private final double reliability; // 0.0 to 1.0

    /**
     * Creates new Evidence.
     *
     * @param source      where the evidence comes from
     * @param description what the evidence consists of
     * @param reliability the estimated reliability of the source (0.0 to 1.0)
     */
    protected Evidence(String source, String description, double reliability) {
        this.source = source;
        this.description = description;
        this.reliability = Math.max(0.0, Math.min(1.0, reliability));
    }

    @Override
    public String getName() {
        return source;
    }

    @Override
    public String getComments() {
        return description;
    }

    /**
     * Returns the reliability of the evidence.
     * @return a value between 0.0 and 1.0
     */
    public double getReliability() {
        return reliability;
    }

    /**
     * Returns the type of evidence (Empirical, Rational, etc.).
     * @return the evidence type name
     */
    public abstract String getEvidenceType();

    @Override
    public String toString() {
        return getEvidenceType() + " Evidence from " + source + " (Reliability: " + reliability + ")";
    }
}
