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
 * A class representing the Triacylglycerol lipid molecule.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//Saturated and unsaturated fats. (a) Palmitic acid, with no double bonds and, thus, a maximum number of hydrogen atoms bonded to the
//carbon chain, is a saturated fatty acid. Many animal triacylglycerols (fats) are saturated. Because their fatty acid chains can fit closely
//together, these triacylglycerols form immobile arrays called hard fat. (b) Linoleic acid, with three double bonds and, thus, fewer than the
//maximum number of hydrogen atoms bonded to the carbon chain, is an unsaturated fatty acid. Plant fats are typically unsaturated. The
//many kinks the double bonds introduce into the fatty acid chains prevent the triacylglycerols from closely aligning and produce oils, which
//are liquid at room temperature.
//From Raven Biology
public final class Triacylglycerol extends Molecule {
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
    private static Atom carbonAtom11;

    /** DOCUMENT ME! */
    private static Atom carbonAtom12;

    /** DOCUMENT ME! */
    private static Atom carbonAtom13;

    /** DOCUMENT ME! */
    private static Atom carbonAtom14;

    /** DOCUMENT ME! */
    private static Atom carbonAtom15;

    /** DOCUMENT ME! */
    private static Atom carbonAtom16;

    /** DOCUMENT ME! */
    private static Atom carbonAtom17;

    /** DOCUMENT ME! */
    private static Atom carbonAtom18;

    /** DOCUMENT ME! */
    private static Atom carbonAtom19;

    /** DOCUMENT ME! */
    private static Atom carbonAtom20;

    /** DOCUMENT ME! */
    private static Atom carbonAtom21;

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
    private static Atom hydrogenAtom21;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom22;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom23;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom24;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom25;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom26;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom27;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom28;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom29;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom30;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom31;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom32;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom33;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom34;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom35;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom36;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom37;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom38;

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

    /** DOCUMENT ME! */
    private static Bond bond31;

    /** DOCUMENT ME! */
    private static Bond bond32;

    /** DOCUMENT ME! */
    private static Bond bond33;

    /** DOCUMENT ME! */
    private static Bond bond34;

    /** DOCUMENT ME! */
    private static Bond bond35;

    /** DOCUMENT ME! */
    private static Bond bond36;

    /** DOCUMENT ME! */
    private static Bond bond37;

    /** DOCUMENT ME! */
    private static Bond bond38;

    /** DOCUMENT ME! */
    private static Bond bond39;

    /** DOCUMENT ME! */
    private static Bond bond40;

    /** DOCUMENT ME! */
    private static Bond bond41;

    /** DOCUMENT ME! */
    private static Bond bond42;

    /** DOCUMENT ME! */
    private static Bond bond43;

    /** DOCUMENT ME! */
    private static Bond bond44;

    /** DOCUMENT ME! */
    private static Bond bond45;

    /** DOCUMENT ME! */
    private static Bond bond46;

    /** DOCUMENT ME! */
    private static Bond bond47;

    /** DOCUMENT ME! */
    private static Bond bond48;

    /** DOCUMENT ME! */
    private static Bond bond49;

    /** DOCUMENT ME! */
    private static Bond bond50;

    /** DOCUMENT ME! */
    private static Bond bond51;

    /** DOCUMENT ME! */
    private static Bond bond52;

    /** DOCUMENT ME! */
    private static Bond bond53;

    /** DOCUMENT ME! */
    private static Bond bond54;

    /** DOCUMENT ME! */
    private static Bond bond55;

    /** DOCUMENT ME! */
    private static Bond bond56;

    /** DOCUMENT ME! */
    private static Bond bond57;

    /** DOCUMENT ME! */
    private static Bond bond58;

    /** DOCUMENT ME! */
    private static Bond bond59;

    /** DOCUMENT ME! */
    private static Bond bond60;

    /** DOCUMENT ME! */
    private static Bond bond61;

    /** DOCUMENT ME! */
    private static Bond bond62;

    /** DOCUMENT ME! */
    private static Bond bond63;

    /** DOCUMENT ME! */
    private static Bond bond64;

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
        carbonAtom11 = new Atom(carbonElement);
        carbonAtom12 = new Atom(carbonElement);
        carbonAtom13 = new Atom(carbonElement);
        carbonAtom14 = new Atom(carbonElement);
        carbonAtom15 = new Atom(carbonElement);
        carbonAtom16 = new Atom(carbonElement);
        carbonAtom17 = new Atom(carbonElement);
        carbonAtom18 = new Atom(carbonElement);
        carbonAtom19 = new Atom(carbonElement);
        carbonAtom20 = new Atom(carbonElement);
        carbonAtom21 = new Atom(carbonElement);
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
        hydrogenAtom13 = new Atom(hydrogenElement);
        hydrogenAtom14 = new Atom(hydrogenElement);
        hydrogenAtom15 = new Atom(hydrogenElement);
        hydrogenAtom16 = new Atom(hydrogenElement);
        hydrogenAtom17 = new Atom(hydrogenElement);
        hydrogenAtom18 = new Atom(hydrogenElement);
        hydrogenAtom19 = new Atom(hydrogenElement);
        hydrogenAtom20 = new Atom(hydrogenElement);
        hydrogenAtom21 = new Atom(hydrogenElement);
        hydrogenAtom22 = new Atom(hydrogenElement);
        hydrogenAtom23 = new Atom(hydrogenElement);
        hydrogenAtom24 = new Atom(hydrogenElement);
        hydrogenAtom25 = new Atom(hydrogenElement);
        hydrogenAtom26 = new Atom(hydrogenElement);
        hydrogenAtom27 = new Atom(hydrogenElement);
        hydrogenAtom28 = new Atom(hydrogenElement);
        hydrogenAtom29 = new Atom(hydrogenElement);
        hydrogenAtom30 = new Atom(hydrogenElement);
        hydrogenAtom31 = new Atom(hydrogenElement);
        hydrogenAtom32 = new Atom(hydrogenElement);
        hydrogenAtom33 = new Atom(hydrogenElement);
        hydrogenAtom34 = new Atom(hydrogenElement);
        hydrogenAtom35 = new Atom(hydrogenElement);
        hydrogenAtom36 = new Atom(hydrogenElement);
        hydrogenAtom37 = new Atom(hydrogenElement);
        hydrogenAtom38 = new Atom(hydrogenElement);

        //part1
        bond1 = new Bond(carbonAtom1, hydrogenAtom1);
        bond2 = new Bond(carbonAtom1, hydrogenAtom2);
        bond3 = new Bond(carbonAtom1, hydrogenAtom3);
        bond4 = new Bond(carbonAtom1, carbonAtom2);
        bond5 = new Bond(carbonAtom2, hydrogenAtom4);
        bond6 = new Bond(carbonAtom2, hydrogenAtom5);
        bond7 = new Bond(carbonAtom2, carbonAtom3);
        bond8 = new Bond(carbonAtom3, hydrogenAtom6);
        bond9 = new Bond(carbonAtom3, hydrogenAtom7);
        bond10 = new Bond(carbonAtom3, carbonAtom4);
        bond11 = new Bond(carbonAtom4, hydrogenAtom8);
        bond12 = new Bond(carbonAtom4, hydrogenAtom9);
        bond13 = new Bond(carbonAtom4, carbonAtom5);
        bond14 = new Bond(carbonAtom5, hydrogenAtom10);
        bond15 = new Bond(carbonAtom5, hydrogenAtom11);
        bond16 = new Bond(carbonAtom5, carbonAtom6);
        bond17 = new Bond(carbonAtom6, oxygenAtom1, Bond.DOUBLE);
        bond18 = new Bond(carbonAtom6, oxygenAtom2);
        bond19 = new Bond(oxygenAtom2, carbonAtom7);
        bond20 = new Bond(carbonAtom7, hydrogenAtom12);

        //part2
        bond21 = new Bond(carbonAtom8, hydrogenAtom13);
        bond22 = new Bond(carbonAtom8, hydrogenAtom14);
        bond23 = new Bond(carbonAtom8, hydrogenAtom15);
        bond24 = new Bond(carbonAtom8, carbonAtom9);
        bond25 = new Bond(carbonAtom9, hydrogenAtom16);
        bond26 = new Bond(carbonAtom9, hydrogenAtom17);
        bond27 = new Bond(carbonAtom9, carbonAtom10);
        bond28 = new Bond(carbonAtom10, hydrogenAtom18);
        bond29 = new Bond(carbonAtom10, hydrogenAtom19);
        bond30 = new Bond(carbonAtom10, carbonAtom11);
        bond31 = new Bond(carbonAtom11, hydrogenAtom20);
        bond32 = new Bond(carbonAtom11, hydrogenAtom21);
        bond33 = new Bond(carbonAtom11, carbonAtom12);
        bond34 = new Bond(carbonAtom12, hydrogenAtom22);
        bond35 = new Bond(carbonAtom12, hydrogenAtom23);
        bond36 = new Bond(carbonAtom12, carbonAtom13);
        bond37 = new Bond(carbonAtom13, oxygenAtom3, Bond.DOUBLE);
        bond38 = new Bond(carbonAtom13, oxygenAtom4);
        bond39 = new Bond(oxygenAtom4, carbonAtom14);
        bond40 = new Bond(carbonAtom14, hydrogenAtom24);

        //part3
        bond41 = new Bond(carbonAtom15, hydrogenAtom25);
        bond42 = new Bond(carbonAtom15, hydrogenAtom26);
        bond43 = new Bond(carbonAtom15, hydrogenAtom27);
        bond44 = new Bond(carbonAtom15, carbonAtom16);
        bond45 = new Bond(carbonAtom16, hydrogenAtom28);
        bond46 = new Bond(carbonAtom16, hydrogenAtom29);
        bond47 = new Bond(carbonAtom16, carbonAtom17);
        bond48 = new Bond(carbonAtom17, hydrogenAtom30);
        bond49 = new Bond(carbonAtom17, hydrogenAtom31);
        bond50 = new Bond(carbonAtom17, carbonAtom18);
        bond51 = new Bond(carbonAtom18, hydrogenAtom32);
        bond52 = new Bond(carbonAtom18, hydrogenAtom33);
        bond53 = new Bond(carbonAtom18, carbonAtom19);
        bond54 = new Bond(carbonAtom19, hydrogenAtom34);
        bond55 = new Bond(carbonAtom19, hydrogenAtom35);
        bond56 = new Bond(carbonAtom19, carbonAtom20);
        bond57 = new Bond(carbonAtom20, oxygenAtom5, Bond.DOUBLE);
        bond58 = new Bond(carbonAtom20, oxygenAtom6);
        bond59 = new Bond(oxygenAtom6, carbonAtom21);
        bond60 = new Bond(carbonAtom21, hydrogenAtom36);

        bond61 = new Bond(carbonAtom7, hydrogenAtom37);
        bond62 = new Bond(carbonAtom7, carbonAtom14);
        bond63 = new Bond(carbonAtom14, carbonAtom21);
        bond64 = new Bond(carbonAtom21, hydrogenAtom38);
    }

/**
     * Creates a new Triacylglycerol object.
     */
    public Triacylglycerol() {
        super(carbonAtom1);
    }
}
