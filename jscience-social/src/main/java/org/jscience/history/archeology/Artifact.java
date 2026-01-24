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

package org.jscience.history.archeology;

import java.io.Serializable;
import java.util.Objects;
import org.jscience.history.temporal.TemporalCoordinate;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents an archaeological artifact discovered at a site.
 * Tracks its descriptive identity and estimated age.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
@Persistent
public class Artifact implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute
    private final String name;

    @Attribute
    private final String description;

    /** Estimated age determined through dating methods (e.g., C-14). */
    @Relation(type = Relation.Type.ONE_TO_ONE)
    private final TemporalCoordinate dating;

    /**
     * Creates a new Artifact.
     * 
     * @param name common name of the object
     * @param description detailed description
     * @param dating estimated age range
     * @throws NullPointerException if any argument is null
     */
    public Artifact(String name, String description, TemporalCoordinate dating) {
        this.name = Objects.requireNonNull(name, "Artifact name cannot be null");
        this.description = Objects.requireNonNull(description, "Description cannot be null");
        this.dating = Objects.requireNonNull(dating, "Dating cannot be null");
    }

    /**
     * Returns the name of the artifact.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the description of the artifact.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the estimated dating of the artifact.
     *
     * @return the dating (uncertain)
     */
    public TemporalCoordinate getDating() {
        return dating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Artifact that)) return false;
        return Objects.equals(name, that.name) && 
               Objects.equals(description, that.description) && 
               Objects.equals(dating, that.dating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, dating);
    }

    @Override
    public String toString() {
        return String.format("Artifact: %s (%s)", name, dating);
    }
}
