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

package org.jscience.biology.cycles;

import org.jscience.biology.molecules.CO2;
import org.jscience.biology.molecules.H2O;
import org.jscience.biology.molecules.O2;
import org.jscience.biology.molecules.carbohydrates.Glucose;

import org.jscience.chemistry.ChemicalReaction;

import java.util.Collections;
import java.util.Set;


/**
 * A class representing the photosynthesis process that takes place into
 * chloroplastes. Photosynthesis needs light (also it does not appear here).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//6 CO2 + 12 H2O ----> Glucose + 6 O2 + 6 H2O
public class Photosynthesis extends ChemicalReaction {
    /** DOCUMENT ME! */
    static Set reactants;

    /** DOCUMENT ME! */
    static Set products;

    static {
        O2 dioxygen;
        CO2 carbonDioxide;
        H2O water;
        Glucose glucose;
        int i;

        reactants = Collections.EMPTY_SET;
        products = Collections.EMPTY_SET;

        for (i = 1; i < 13; i++) {
            water = new H2O();
            reactants.add(water);
        }

        for (i = 1; i < 7; i++) {
            carbonDioxide = new CO2();
            reactants.add(carbonDioxide);
        }

        glucose = new Glucose();
        products.add(glucose);

        for (i = 1; i < 7; i++) {
            dioxygen = new O2();
            products.add(dioxygen);
        }

        for (i = 1; i < 13; i++) {
            water = new H2O();
            products.add(water);
        }
    }

    /*
    * Constructs a Photosynthesis.
    */
    public Photosynthesis() {
        super(reactants, products);
    }
}
