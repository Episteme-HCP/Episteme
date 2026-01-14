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

package org.jscience.biology.bases;

import org.jscience.biology.Base;

import org.jscience.chemistry.Atom;
import org.jscience.chemistry.Bond;
import org.jscience.chemistry.Element;
import org.jscience.chemistry.PeriodicTable;


/**
 * A class representing the Guanine Base.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//bonds to Cytosine
public final class Guanine extends Base {
    /** DOCUMENT ME! */
    private static Element carbonElement;

    /** DOCUMENT ME! */
    private static Element nitrogenElement;

    /** DOCUMENT ME! */
    private static Element hydrogenElement;

    /** DOCUMENT ME! */
    private static Element oxygenElement;

    /** DOCUMENT ME! */
    private static Atom carbonAtom1;

    /** DOCUMENT ME! */
    private static Atom carbonAtom2;

    /** DOCUMENT ME! */
    private static Atom carbonAtom3;

    /** DOCUMENT ME! */
    private static Atom carbonAtom4;

    /** DOCUMENT ME! */
    private static Atom carbonAtom5;

    /** DOCUMENT ME! */
    private static Atom nitrogenAtom1;

    /** DOCUMENT ME! */
    private static Atom nitrogenAtom2;

    /** DOCUMENT ME! */
    private static Atom nitrogenAtom3;

    /** DOCUMENT ME! */
    private static Atom nitrogenAtom4;

    /** DOCUMENT ME! */
    private static Atom nitrogenAtom5;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom1;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom2;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom3;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom4;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom5;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom;

    /** DOCUMENT ME! */
    private static Bond bond1;

    /** DOCUMENT ME! */
    private static Bond bond2;

    /** DOCUMENT ME! */
    private static Bond bond3;

    /** DOCUMENT ME! */
    private static Bond bond4;

    /** DOCUMENT ME! */
    private static Bond bond5;

    /** DOCUMENT ME! */
    private static Bond bond6;

    /** DOCUMENT ME! */
    private static Bond bond7;

    /** DOCUMENT ME! */
    private static Bond bond8;

    /** DOCUMENT ME! */
    private static Bond bond9;

    /** DOCUMENT ME! */
    private static Bond bond10;

    /** DOCUMENT ME! */
    private static Bond bond11;

    /** DOCUMENT ME! */
    private static Bond bond12;

    /** DOCUMENT ME! */
    private static Bond bond13;

    /** DOCUMENT ME! */
    private static Bond bond14;

    /** DOCUMENT ME! */
    private static Bond bond15;

    /** DOCUMENT ME! */
    private static Bond bond16;

    /** DOCUMENT ME! */
    private static Bond bond17;

    static {
        carbonElement = PeriodicTable.getElement("Carbon");
        nitrogenElement = PeriodicTable.getElement("Nitrogen");
        hydrogenElement = PeriodicTable.getElement("Hydrogen");
        oxygenElement = PeriodicTable.getElement("Oxygen");

        carbonAtom1 = new Atom(carbonElement);
        carbonAtom2 = new Atom(carbonElement);
        carbonAtom3 = new Atom(carbonElement);
        carbonAtom4 = new Atom(carbonElement);
        carbonAtom5 = new Atom(carbonElement);
        nitrogenAtom1 = new Atom(nitrogenElement);
        nitrogenAtom2 = new Atom(nitrogenElement);
        nitrogenAtom3 = new Atom(nitrogenElement);
        nitrogenAtom4 = new Atom(nitrogenElement);
        nitrogenAtom5 = new Atom(nitrogenElement);
        hydrogenAtom1 = new Atom(hydrogenElement);
        hydrogenAtom2 = new Atom(hydrogenElement);
        hydrogenAtom3 = new Atom(hydrogenElement);
        hydrogenAtom4 = new Atom(hydrogenElement);
        hydrogenAtom5 = new Atom(hydrogenElement);
        oxygenAtom = new Atom(oxygenElement);

        bond1 = new Bond(carbonAtom1, hydrogenAtom1);
        bond2 = new Bond(carbonAtom1, nitrogenAtom1);
        bond3 = new Bond(nitrogenAtom1, hydrogenAtom2);
        bond4 = new Bond(nitrogenAtom1, carbonAtom2);
        bond5 = new Bond(carbonAtom2, carbonAtom3, Bond.DOUBLE);
        bond6 = new Bond(carbonAtom2, nitrogenAtom2);
        bond7 = new Bond(nitrogenAtom2, carbonAtom4, Bond.DOUBLE);
        bond8 = new Bond(carbonAtom4, nitrogenAtom3);
        bond9 = new Bond(nitrogenAtom3, hydrogenAtom3);
        bond10 = new Bond(nitrogenAtom3, hydrogenAtom4);
        bond11 = new Bond(carbonAtom4, nitrogenAtom4);
        bond12 = new Bond(nitrogenAtom4, hydrogenAtom5);
        bond13 = new Bond(nitrogenAtom4, carbonAtom5);
        bond14 = new Bond(carbonAtom5, oxygenAtom);
        bond15 = new Bond(carbonAtom5, carbonAtom3);
        bond16 = new Bond(carbonAtom3, nitrogenAtom5);
        bond17 = new Bond(nitrogenAtom5, carbonAtom1, Bond.DOUBLE);
    }

/**
     * Constructs an Guanine molecule.
     */
    public Guanine() {
        super(carbonAtom1);
    }

    /**
     * Determines whether this is a purine or pyrimidine molecule.
     *
     * @return DOCUMENT ME!
     */
    public boolean isPurine() {
        return true;
    }
}
