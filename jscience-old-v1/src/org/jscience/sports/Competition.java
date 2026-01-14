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

package org.jscience.sports;

import org.jscience.util.Named;


/**
 * A class representing a competition (where various matchs in various
 * sports occur).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Competition extends Object implements Named {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private Match[] matches;

    /** DOCUMENT ME! */
    private String[] prizes; //from the first to the last one, description and money prize

/**
     * Creates a new Competition object.
     *
     * @param name    DOCUMENT ME!
     * @param prizes  DOCUMENT ME!
     * @param matches DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Competition(String name, String[] prizes, Match[] matches) {
        if ((name != null) && (prizes != null) && (matches != null)) {
            this.name = name;
            this.matches = matches;
            this.prizes = prizes;
        } else {
            throw new IllegalArgumentException(
                "The Competition constructor can't have null arguments.");
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
    public String[] getPrizes() {
        return prizes;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Match[] getMatches() {
        return matches;
    }
}
