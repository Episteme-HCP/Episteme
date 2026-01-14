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

package org.jscience.biology;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing an ecosystem (interacting populations from
 * different species).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//interacting species (usually of predators/preys)
public class Ecosystem extends Object {
    /** DOCUMENT ME! */
    private Set populations;

/**
     * Creates a new Ecosystem object.
     */
    public Ecosystem() {
        populations = Collections.EMPTY_SET;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumPopulations() {
        return populations.size();
    }

    //iterates through all populations and returns the species
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getSpecies() {
        Iterator iterator;
        Set result;

        iterator = populations.iterator();
        result = Collections.EMPTY_SET;

        while (iterator.hasNext()) {
            result.add(((Population) iterator.next()).getSpecies());
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getPopulations() {
        return populations;
    }

    //all elements of the Set should be populations
    /**
     * DOCUMENT ME!
     *
     * @param populations DOCUMENT ME!
     */
    public void setPopulations(Set populations) {
        Iterator iterator;
        boolean valid;

        if (populations != null) {
            iterator = populations.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Population;
            }

            if (valid) {
                this.populations = populations;
            }
        } else {
            throw new IllegalArgumentException(
                "You can't set a null populations.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param population DOCUMENT ME!
     */
    public void addPopulation(Population population) {
        populations.add(population);
    }

    /**
     * DOCUMENT ME!
     *
     * @param population DOCUMENT ME!
     */
    public void removePopulation(Population population) {
        populations.remove(population);
    }
}
