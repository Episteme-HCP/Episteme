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
 * A class representing the degradation process that takes place into
 * cells.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//Glucose + 6 O2  ----> 6 CO2 + 6 H2O
public class CellularRespiration extends ChemicalReaction {
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

        glucose = new Glucose();
        reactants.add(glucose);

        for (i = 1; i < 7; i++) {
            dioxygen = new O2();
            reactants.add(dioxygen);
        }

        for (i = 1; i < 7; i++) {
            water = new H2O();
            products.add(water);
        }

        for (i = 1; i < 7; i++) {
            carbonDioxide = new CO2();
            products.add(carbonDioxide);
        }
    }

/**
     * } Constructs a Cellular Respiration.
     */
    public CellularRespiration() {
        super(reactants, products);
    }
}
