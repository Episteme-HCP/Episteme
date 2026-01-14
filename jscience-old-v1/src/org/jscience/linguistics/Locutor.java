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

package org.jscience.linguistics;

import org.jscience.biology.Individual;

import org.jscience.sociology.Role;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing an individual in an talking situation (cocktail,
 * etc.).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Locutor extends Role {
    /** DOCUMENT ME! */
    private Set knownLanguages; //we don't specify the degree of knowledge here, this is mainly to have a broad idea of the locutor capacities

    //may be you will only want to set mother tongues
    /**
     * Creates a new Locutor object.
     *
     * @param individual DOCUMENT ME!
     * @param situation DOCUMENT ME!
     */
    public Locutor(Individual individual, ChatSituation situation) {
        super(individual, "Locutor", situation, Role.CLIENT);
        this.knownLanguages = Collections.EMPTY_SET;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getKnownLanguages() {
        return knownLanguages;
    }

    /**
     * DOCUMENT ME!
     *
     * @param language DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addLanguage(Language language) {
        if (knownLanguages != null) {
            knownLanguages.add(language);
        } else {
            throw new IllegalArgumentException("You can't add a null language.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param language DOCUMENT ME!
     */
    public void removeLanguage(Language language) {
        knownLanguages.remove(language);
    }

    /**
     * DOCUMENT ME!
     *
     * @param languages DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setLanguages(Set languages) {
        Iterator iterator;
        boolean valid;

        if (languages != null) {
            iterator = languages.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Language;
            }

            if (valid) {
                this.knownLanguages = languages;
            } else {
                throw new IllegalArgumentException(
                    "The Set of languages should contain only Languages.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of languages shouldn't be null.");
        }
    }
}
