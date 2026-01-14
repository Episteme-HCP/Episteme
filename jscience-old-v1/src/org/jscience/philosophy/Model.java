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

import org.jscience.util.Named;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing a logical organization of Concepts.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Model extends Object implements Named {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private Set concepts;

/**
     * Creates a new Model object.
     *
     * @param name DOCUMENT ME!
     */
    public Model(String name) {
        if ((name != null) && (name.length() > 0)) {
            this.name = name;
            concepts = new HashSet();
        } else {
            throw new IllegalArgumentException(
                "The Model constructor doesn't accept null or empty arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getConcepts() {
        return concepts;
    }

    //all members of the Set should be concepts
    /**
     * DOCUMENT ME!
     *
     * @param concepts DOCUMENT ME!
     */
    public void setConcepts(Set concepts) {
        Iterator iterator;
        boolean valid;

        if (concepts != null) {
            iterator = concepts.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Concept;
            }

            if (valid) {
                this.concepts = concepts;
            } else {
                throw new IllegalArgumentException(
                    "The Set of concepts should contain only Concepts.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of concepts shouldn't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param concept DOCUMENT ME!
     */
    public void addConcept(Concept concept) {
        concepts.add(concept);
    }

    /**
     * DOCUMENT ME!
     *
     * @param concept DOCUMENT ME!
     */
    public void removeConcept(Concept concept) {
        concepts.remove(concept);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getAllConcepts() {
        Set result;
        Iterator iterator;

        result = new HashSet();
        iterator = concepts.iterator();

        while (iterator.hasNext()) {
            result.addAll(((Concept) iterator.next()).getAllConcepts());
        }

        return result;
    }
}
