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
 * A class representing the carbon dioxide (CO2).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class CO2 extends Molecule {
    /** DOCUMENT ME! */
    private static Element carbonElement;

    /** DOCUMENT ME! */
    private static Element oxygenElement;

    /** DOCUMENT ME! */
    private static Atom carbonAtom;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom1;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom2;

    /** DOCUMENT ME! */
    private static Bond bond1;

    /** DOCUMENT ME! */
    private static Bond bond2;

    static {
        carbonElement = PeriodicTable.getElement("Carbon");
        oxygenElement = PeriodicTable.getElement("Oxygen");

        carbonAtom = new Atom(carbonElement);
        oxygenAtom1 = new Atom(oxygenElement);
        oxygenAtom2 = new Atom(oxygenElement);

        bond1 = new Bond(carbonAtom, oxygenAtom1, Bond.DOUBLE);
        bond2 = new Bond(carbonAtom, oxygenAtom2, Bond.DOUBLE);
    }

/**
     * Creates a new CO2 object.
     */
    public CO2() {
        super(carbonAtom);
    }
}
