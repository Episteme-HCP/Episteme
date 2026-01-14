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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing a well defined group of ideas.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class PhilosophicalCurrent extends Belief {
    /** DOCUMENT ME! */
    private Set models;

/**
     * Creates a new PhilosophicalCurrent object.
     *
     * @param name     DOCUMENT ME!
     * @param comments DOCUMENT ME!
     */
    public PhilosophicalCurrent(String name, String comments) {
        super(name, comments);
        models = new HashSet();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getModels() {
        return models;
    }

    //all members of the Set should be models
    /**
     * DOCUMENT ME!
     *
     * @param models DOCUMENT ME!
     */
    public void setModels(Set models) {
        Iterator iterator;
        boolean valid;

        if (models != null) {
            iterator = models.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Model;
            }

            if (valid) {
                this.models = models;
            } else {
                throw new IllegalArgumentException(
                    "The Set of models should contain only Models.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of models shouldn't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param model DOCUMENT ME!
     */
    public void addModel(Model model) {
        models.add(model);
    }

    /**
     * DOCUMENT ME!
     *
     * @param model DOCUMENT ME!
     */
    public void removeModel(Model model) {
        models.remove(model);
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
        iterator = models.iterator();

        while (iterator.hasNext()) {
            result.addAll(((Model) iterator.next()).getAllConcepts());
        }

        return result;
    }
}
