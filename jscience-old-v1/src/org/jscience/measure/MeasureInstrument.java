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

package org.jscience.measure;

import org.jscience.geography.Place;

import org.jscience.util.Named;
import org.jscience.util.Positioned;


/**
 * The MeasureInstrument class is the base class for physical instruments.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class MeasureInstrument implements Named, Identified, Positioned {
    /** DOCUMENT ME! */
    private String name; //name of test

    /** DOCUMENT ME! */
    private Identification identification; //unique identification value

    /** DOCUMENT ME! */
    private Place place; //where this instrument is located

/**
     * Creates a new MeasureInstrument object.
     *
     * @param name           DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param place          DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public MeasureInstrument(String name, Identification identification,
        Place place) {
        if ((name != null) && (name.length() > 0) && (identification != null) &&
                (place != null)) {
            this.name = name;
            this.identification = identification;
            this.place = place;
        } else {
            throw new IllegalArgumentException(
                "The MeasureInstrument constructor can't have null arguments (and name can't be empty).");
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
    public Identification getIdentification() {
        return identification;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Place getPosition() {
        return place;
    }
}
