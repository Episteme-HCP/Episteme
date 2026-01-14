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

import java.util.Collections;
import java.util.Set;


/**
 * A class representing the past competitions and matches results.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//may be we should provide a way to read and write an XML file to store that
public class Almanac extends Object {
    /** DOCUMENT ME! */
    private Set results;

/**
     * Creates a new Almanac object.
     */
    public Almanac() {
        results = Collections.EMPTY_SET;
    }

    //don't add matches as competitions or the opposite if you don't want to have them twice
    /**
     * DOCUMENT ME!
     *
     * @param match DOCUMENT ME!
     */
    public void addResult(Match match) {
        results.add(match);
    }

    /**
     * DOCUMENT ME!
     *
     * @param match DOCUMENT ME!
     */
    public void removeResult(Match match) {
        results.remove(match);
    }

    //don't add matches as competitions or the opposite if you don't want to have them twice
    /**
     * DOCUMENT ME!
     *
     * @param competition DOCUMENT ME!
     */
    public void addResult(Competition competition) {
        results.add(competition);
    }

    /**
     * DOCUMENT ME!
     *
     * @param competition DOCUMENT ME!
     */
    public void removeResult(Competition competition) {
        results.remove(competition);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getResults() {
        return results;
    }
}
