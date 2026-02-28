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

package org.episteme.social.philosophy.epistemology;

import java.util.Objects;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.episteme.core.util.identity.Identification;
import org.episteme.core.util.identity.SimpleIdentification;
import org.episteme.core.util.identity.ComprehensiveIdentification;

/**
 * Represents a statement or assertion that expresses something that can be true or false.
 * In epistemology, propositions are the primary objects of belief and knowledge.
 * Modernized to implement ComprehensiveIdentification and support dynamic traits and consistent identity.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Proposition implements ComprehensiveIdentification {

    private static final long serialVersionUID = 2L;

    private final Identification id;
    private final Map<String, Object> traits = new HashMap<>();

    private boolean truthValue;

    /**
     * Creates a new Proposition with a specific content.
     * By default, the truth value is false (unassigned/unverified).
     *
     * @param content the statement content
     */
    public Proposition(String content) {
        this(content, false);
    }

    /**
     * Creates a new Proposition with content and a truth value.
     *
     * @param content    the statement content
     * @param truthValue the truth value
     */
    public Proposition(String content, boolean truthValue) {
        this.id = new SimpleIdentification("Proposition:" + UUID.randomUUID());
        setName(Objects.requireNonNull(content, "Proposition content cannot be null"));
        this.truthValue = truthValue;
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
     * Returns the content of the proposition.
     * @return the content
     */
    public String getContent() {
        return getName();
    }

    /**
     * Returns the truth value of the proposition.
     * @return true if the proposition is true, false otherwise
     */
    public boolean isTrue() {
        return truthValue;
    }

    /**
     * Sets the truth value of the proposition.
     * @param truthValue the new truth value
     */
    public void setTruthValue(boolean truthValue) {
        this.truthValue = truthValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Proposition that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Proposition[" + getName() + " is " + (truthValue ? "True" : "False") + "]";
    }
}

