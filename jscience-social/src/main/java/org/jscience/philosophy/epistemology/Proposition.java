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
import java.util.Objects;
import org.jscience.util.Named;

/**
 * Represents a statement or assertion that expresses something that can be true or false.
 * In epistemology, propositions are the primary objects of belief and knowledge.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Proposition implements Named, Serializable {

    private static final long serialVersionUID = 1L;

    private final String content;
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
        this.content = Objects.requireNonNull(content, "Proposition content cannot be null");
        this.truthValue = truthValue;
    }

    @Override
    public String getName() {
        return content;
    }

    /**
     * Returns the content of the proposition.
     * @return the content
     */
    public String getContent() {
        return content;
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
        if (o == null || getClass() != o.getClass()) return false;
        Proposition that = (Proposition) o;
        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    @Override
    public String toString() {
        return "Proposition[" + content + " is " + (truthValue ? "True" : "False") + "]";
    }
}
