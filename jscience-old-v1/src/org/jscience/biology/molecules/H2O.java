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

package org.jscience.biology.molecules;

import org.jscience.chemistry.*;


/**
 * A class representing the water molecule (H2O).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class H2O extends Molecule {
    /** DOCUMENT ME! */
    private static Element oxygenElement;

    /** DOCUMENT ME! */
    private static Element hydrogenElement;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom1;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom2;

    /** DOCUMENT ME! */
    private static Bond bond1;

    /** DOCUMENT ME! */
    private static Bond bond2;

    static {
        oxygenElement = PeriodicTable.getElement("Oxygen");
        hydrogenElement = PeriodicTable.getElement("Hydrogen");

        oxygenAtom = new Atom(oxygenElement);
        hydrogenAtom1 = new Atom(hydrogenElement);
        hydrogenAtom2 = new Atom(hydrogenElement);

        bond1 = new Bond(oxygenAtom, hydrogenAtom1);
        bond2 = new Bond(oxygenAtom, hydrogenAtom2);
    }

/**
     * Creates a new H20 object.
     */
    public H2O() {
        super(oxygenAtom);
    }
}
