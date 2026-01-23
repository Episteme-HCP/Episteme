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
package org.jscience.philosophy;


import java.io.Serializable;
import java.util.Objects;
import org.jscience.util.Commented;
import org.jscience.util.Named;

/**
 * Represents a fundamental conviction or tenet within a philosophical system.
 * 
 * <p> Beliefs are the core units of conviction that form the basis of larger 
 *     philosophical frameworks and argumentations.</p>
 *
 * @author <a href="mailto:silvere.martin-michiellot@jscience.org">Silvere Martin-Michiellot</a>
 * @author Gemini AI (Google DeepMind)
 * @version 6.0, July 21, 2014
 */
public class Belief implements Named, Commented, Serializable {


    private static final long serialVersionUID = 1L;

    private final String name;
    private final String comments;

    /**
     * Creates a new Belief.
     *
     * @param name     the name or title of the belief
     * @param comments descriptive text or rationale
     * @throws IllegalArgumentException if null or empty arguments are provided
     */
    public Belief(String name, String comments) {
        if (name == null || name.isEmpty() || comments == null || comments.isEmpty()) {
            throw new IllegalArgumentException("Name and comments cannot be null or empty.");
        }
        this.name = name;
        this.comments = comments;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getComments() {
        return comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Belief belief = (Belief) o;
        return Objects.equals(name, belief.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name + ": " + comments;
    }
}
