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

package org.jscience.chemistry.radicals;

import org.jscience.chemistry.Atom;
import org.jscience.chemistry.Bond;
import org.jscience.chemistry.Element;
import org.jscience.chemistry.PeriodicTable;

import java.util.HashSet;
import java.util.Set;


/**
 * A class storing the CarboxylicAcid radical. To ease building molecules.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class CarboxylicAcid extends Object implements Radical {
    /** DOCUMENT ME! */
    private Element carbonElement;

    /** DOCUMENT ME! */
    private Element oxygenElement;

    /** DOCUMENT ME! */
    private Element hydrogenElement;

    /** DOCUMENT ME! */
    private Atom carbonAtom;

    /** DOCUMENT ME! */
    private Atom oxygenAtom1;

    /** DOCUMENT ME! */
    private Atom oxygenAtom2;

    /** DOCUMENT ME! */
    private Atom hydrogenAtom;

    /** DOCUMENT ME! */
    private Bond bond1;

    /** DOCUMENT ME! */
    private Bond bond2;

    /** DOCUMENT ME! */
    private Bond bond3;

/**
     * Creates a new CarboxylicAcid object.
     */
    public CarboxylicAcid() {
        carbonElement = PeriodicTable.getElement("Carbon");
        oxygenElement = PeriodicTable.getElement("Oxygen");
        hydrogenElement = PeriodicTable.getElement("Hydrogen");

        carbonAtom = new Atom(carbonElement);
        oxygenAtom1 = new Atom(oxygenElement);
        oxygenAtom2 = new Atom(oxygenElement);
        hydrogenAtom = new Atom(hydrogenElement);

        bond1 = new Bond(carbonAtom, oxygenAtom1, Bond.DOUBLE);
        bond2 = new Bond(carbonAtom, oxygenAtom2);
        bond3 = new Bond(oxygenAtom2, hydrogenAtom);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Atom getRadical() {
        return carbonAtom;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getUnboundedAtoms() {
        Set result;

        result = new HashSet();
        result.add(carbonAtom);

        return result;
    }
}
