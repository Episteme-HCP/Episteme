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

package org.jscience.chemistry;

/**
 * The Polymer class represent molecules that are repeating many times.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//please note that proteins are polymers
//but also note we do not treat them as such
//subclasses should have the name of the Molecule followed by factory
//for example NylonFactory
public abstract class Polymer extends Object {
    /** DOCUMENT ME! */
    private Molecule repeatingPart;

/**
     * Creates a new Polymer object.
     *
     * @param repeatingPart DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Polymer(Molecule repeatingPart) {
        if (repeatingPart != null) {
            this.repeatingPart = repeatingPart;
        } else {
            throw new IllegalArgumentException(
                "The Polymer constructor doesn't accept null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Molecule getRepeatingPart() {
        return repeatingPart;
    }

    //uses the repeating part and the polymerisation degree to produce any polymer.
    /**
     * DOCUMENT ME!
     *
     * @param polymerisationDegree DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Molecule getMolecule(int polymerisationDegree);
}
