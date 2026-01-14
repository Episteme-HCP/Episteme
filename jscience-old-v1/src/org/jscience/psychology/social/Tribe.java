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

package org.jscience.psychology.social;

import org.jscience.geography.Place;

import org.jscience.sociology.Culture;

import org.jscience.util.Named;


/**
 * A class representing the basic facts about an organized human group.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Tribe extends HumanGroup implements Named {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private Culture culture;

/**
     * Creates a new Tribe object.
     *
     * @param name    DOCUMENT ME!
     * @param culture DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Tribe(String name, Culture culture) {
        if ((name != null) && (name.length() > 0) && (culture != null)) {
            this.name = name;
            this.culture = culture;
        } else {
            throw new IllegalArgumentException(
                "The Tribe constructor can't have null or empty arguments.");
        }
    }

/**
     * Creates a new Tribe object.
     *
     * @param name            DOCUMENT ME!
     * @param formalTerritory DOCUMENT ME!
     * @param culture         DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Tribe(String name, Place formalTerritory, Culture culture) {
        super(formalTerritory);

        if ((name != null) && (name.length() > 0) && (culture != null)) {
            this.name = name;
            this.culture = culture;
        } else {
            throw new IllegalArgumentException(
                "The Tribe constructor can't have null or empty arguments.");
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
     * @param name DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setName(String name) {
        if ((name != null) && (name.length() > 0)) {
            this.name = name;
        } else {
            throw new IllegalArgumentException(
                "The name can't be null or empty.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Culture getCulture() {
        return culture;
    }

    /**
     * DOCUMENT ME!
     *
     * @param culture DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setCulture(Culture culture) {
        if ((culture != null)) {
            this.culture = culture;
        } else {
            throw new IllegalArgumentException("The culture must be non null.");
        }
    }
}
