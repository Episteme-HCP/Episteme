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

package org.jscience.biology.molecules.lipids;

import org.jscience.chemistry.*;


/**
 * A class representing the Citronellol lipid molecule.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//this is a Terpene
public class Citronellol extends Molecule {
    /** DOCUMENT ME! */
    private static Element carbonElement;

    /** DOCUMENT ME! */
    private static Element oxygenElement;

    /** DOCUMENT ME! */
    private static Element hydrogenElement;

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
    private static Atom carbonAtom6;

    /** DOCUMENT ME! */
    private static Atom carbonAtom7;

    /** DOCUMENT ME! */
    private static Atom carbonAtom8;

    /** DOCUMENT ME! */
    private static Atom carbonAtom9;

    /** DOCUMENT ME! */
    private static Atom carbonAtom10;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom;

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
    private static Atom hydrogenAtom6;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom7;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom8;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom9;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom10;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom11;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom12;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom13;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom14;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom15;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom16;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom17;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom18;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom19;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom20;

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

    /** DOCUMENT ME! */
    private static Bond bond18;

    /** DOCUMENT ME! */
    private static Bond bond19;

    /** DOCUMENT ME! */
    private static Bond bond20;

    /** DOCUMENT ME! */
    private static Bond bond21;

    /** DOCUMENT ME! */
    private static Bond bond22;

    /** DOCUMENT ME! */
    private static Bond bond23;

    /** DOCUMENT ME! */
    private static Bond bond24;

    /** DOCUMENT ME! */
    private static Bond bond25;

    /** DOCUMENT ME! */
    private static Bond bond26;

    /** DOCUMENT ME! */
    private static Bond bond27;

    /** DOCUMENT ME! */
    private static Bond bond28;

    /** DOCUMENT ME! */
    private static Bond bond29;

    /** DOCUMENT ME! */
    private static Bond bond30;

    static {
        carbonElement = PeriodicTable.getElement("Carbon");
        oxygenElement = PeriodicTable.getElement("Oxygen");
        hydrogenElement = PeriodicTable.getElement("Hydrogen");

        carbonAtom1 = new Atom(carbonElement);
        carbonAtom2 = new Atom(carbonElement);
        carbonAtom3 = new Atom(carbonElement);
        carbonAtom4 = new Atom(carbonElement);
        carbonAtom5 = new Atom(carbonElement);
        carbonAtom6 = new Atom(carbonElement);
        carbonAtom7 = new Atom(carbonElement);
        carbonAtom8 = new Atom(carbonElement);
        carbonAtom9 = new Atom(carbonElement);
        carbonAtom10 = new Atom(carbonElement);
        oxygenAtom = new Atom(oxygenElement);
        hydrogenAtom1 = new Atom(hydrogenElement);
        hydrogenAtom2 = new Atom(hydrogenElement);
        hydrogenAtom3 = new Atom(hydrogenElement);
        hydrogenAtom4 = new Atom(hydrogenElement);
        hydrogenAtom5 = new Atom(hydrogenElement);
        hydrogenAtom6 = new Atom(hydrogenElement);
        hydrogenAtom7 = new Atom(hydrogenElement);
        hydrogenAtom8 = new Atom(hydrogenElement);
        hydrogenAtom9 = new Atom(hydrogenElement);
        hydrogenAtom10 = new Atom(hydrogenElement);
        hydrogenAtom11 = new Atom(hydrogenElement);
        hydrogenAtom12 = new Atom(hydrogenElement);
        hydrogenAtom13 = new Atom(hydrogenElement);
        hydrogenAtom14 = new Atom(hydrogenElement);
        hydrogenAtom15 = new Atom(hydrogenElement);
        hydrogenAtom16 = new Atom(hydrogenElement);
        hydrogenAtom17 = new Atom(hydrogenElement);
        hydrogenAtom18 = new Atom(hydrogenElement);
        hydrogenAtom19 = new Atom(hydrogenElement);
        hydrogenAtom20 = new Atom(hydrogenElement);

        bond1 = new Bond(carbonAtom1, hydrogenAtom1);
        bond2 = new Bond(carbonAtom1, hydrogenAtom2);
        bond3 = new Bond(carbonAtom1, carbonAtom2, Bond.DOUBLE);
        bond4 = new Bond(carbonAtom2, carbonAtom3);
        bond5 = new Bond(carbonAtom3, hydrogenAtom3);
        bond6 = new Bond(carbonAtom3, hydrogenAtom4);
        bond7 = new Bond(carbonAtom3, hydrogenAtom5);
        bond8 = new Bond(carbonAtom2, carbonAtom4);
        bond9 = new Bond(carbonAtom4, hydrogenAtom6);
        bond10 = new Bond(carbonAtom4, hydrogenAtom7);
        bond11 = new Bond(carbonAtom4, carbonAtom5);
        bond12 = new Bond(carbonAtom5, hydrogenAtom8);
        bond13 = new Bond(carbonAtom5, hydrogenAtom9);
        bond14 = new Bond(carbonAtom5, carbonAtom6);
        bond15 = new Bond(carbonAtom6, hydrogenAtom10);
        bond16 = new Bond(carbonAtom6, hydrogenAtom11);
        bond17 = new Bond(carbonAtom6, carbonAtom7);
        bond18 = new Bond(carbonAtom7, hydrogenAtom12);
        bond19 = new Bond(carbonAtom7, carbonAtom8);
        bond20 = new Bond(carbonAtom8, hydrogenAtom13);
        bond21 = new Bond(carbonAtom8, hydrogenAtom14);
        bond22 = new Bond(carbonAtom8, hydrogenAtom15);
        bond23 = new Bond(carbonAtom7, carbonAtom9);
        bond24 = new Bond(carbonAtom9, hydrogenAtom16);
        bond25 = new Bond(carbonAtom9, hydrogenAtom17);
        bond26 = new Bond(carbonAtom9, carbonAtom10);
        bond27 = new Bond(carbonAtom10, hydrogenAtom18);
        bond28 = new Bond(carbonAtom10, hydrogenAtom19);
        bond29 = new Bond(carbonAtom10, oxygenAtom);
        bond30 = new Bond(oxygenAtom, hydrogenAtom20);
    }

/**
     * Creates a new Citronellol object.
     */
    public Citronellol() {
        super(carbonAtom1);
    }
}
