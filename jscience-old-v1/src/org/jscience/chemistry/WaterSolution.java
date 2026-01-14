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

import java.util.HashMap;


/**
 * The WaterSolution class defines the solution in which many biological
 * processes occur.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//the solution is already filled with water at pH7
public final class WaterSolution extends Solution {
    //one liter
    public WaterSolution() {
        this(1.0d);
    }

    /**
     * Creates a new WaterSolution object.
     *
     * @param mass DOCUMENT ME!
     */
    public WaterSolution(double mass) {
        Element hydrogenElement;
        Element oxygenElement;
        Atom hydrogenAtom1;
        Atom hydrogenAtom2;
        Atom oxygenAtom;
        Bond bond1;
        Bond bond2;

        HashMap contents;

        hydrogenElement = PeriodicTable.getElement("Hydrogen");
        oxygenElement = PeriodicTable.getElement("Oxygen");

        hydrogenAtom1 = new Atom(hydrogenElement);
        hydrogenAtom2 = new Atom(hydrogenElement);
        oxygenAtom = new Atom(oxygenElement);

        bond1 = new Bond(oxygenAtom, hydrogenAtom1);
        bond2 = new Bond(oxygenAtom, hydrogenAtom2);

        contents = new HashMap();
        contents.put(new Molecule(oxygenAtom), new Double(mass));
        super(contents, ChemicalConditions.getDefaultChemicalConditions());

        //pH, the logarithmic concentration of dissociated particles
        //made of the dissociation of H20 into H+ and OH-
        //how do I set it ?
        //and manage concentration so that there is a huge amount of water
    }
}
