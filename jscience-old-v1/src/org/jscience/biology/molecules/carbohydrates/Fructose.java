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

package org.jscience.biology.molecules.carbohydrates;

import org.jscience.chemistry.*;


/**
 * A class representing the Fructose carbohydrate molecule.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//as a ring shape, in solution
public final class Fructose extends Molecule {
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
    private static Atom oxygenAtom1;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom2;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom3;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom4;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom5;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom6;

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
        oxygenAtom1 = new Atom(oxygenElement);
        oxygenAtom2 = new Atom(oxygenElement);
        oxygenAtom3 = new Atom(oxygenElement);
        oxygenAtom4 = new Atom(oxygenElement);
        oxygenAtom5 = new Atom(oxygenElement);
        oxygenAtom6 = new Atom(oxygenElement);
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

        bond1 = new Bond(carbonAtom1, hydrogenAtom1);
        bond2 = new Bond(carbonAtom1, hydrogenAtom2);
        bond3 = new Bond(carbonAtom1, oxygenAtom1);
        bond4 = new Bond(oxygenAtom1, hydrogenAtom3);
        bond5 = new Bond(carbonAtom1, carbonAtom2);
        bond6 = new Bond(carbonAtom2, hydrogenAtom4);
        bond7 = new Bond(carbonAtom2, carbonAtom3);
        bond8 = new Bond(carbonAtom3, hydrogenAtom5);
        bond9 = new Bond(carbonAtom3, oxygenAtom2);
        bond10 = new Bond(oxygenAtom2, hydrogenAtom6);
        bond11 = new Bond(carbonAtom3, carbonAtom4);
        bond12 = new Bond(carbonAtom4, hydrogenAtom7);
        bond13 = new Bond(carbonAtom4, oxygenAtom3);
        bond14 = new Bond(oxygenAtom3, hydrogenAtom8);
        bond15 = new Bond(carbonAtom4, carbonAtom5);
        bond16 = new Bond(carbonAtom5, carbonAtom6);
        bond17 = new Bond(carbonAtom6, hydrogenAtom9);
        bond18 = new Bond(carbonAtom6, hydrogenAtom10);
        bond19 = new Bond(carbonAtom6, oxygenAtom4);
        bond20 = new Bond(oxygenAtom4, hydrogenAtom11);
        bond21 = new Bond(carbonAtom5, oxygenAtom5);
        bond22 = new Bond(oxygenAtom5, hydrogenAtom12);
        bond23 = new Bond(carbonAtom5, oxygenAtom6);
        bond24 = new Bond(oxygenAtom6, carbonAtom2);
    }

/**
     * Creates a new Fructose object.
     */
    public Fructose() {
        super(carbonAtom1);
    }
}
