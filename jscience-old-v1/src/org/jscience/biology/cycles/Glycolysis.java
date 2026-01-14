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

import org.jscience.chemistry.ChemicalReaction;

import java.util.Collections;
import java.util.Set;


/**
 * A class representing the glycolysis process that takes place into cells.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//Glucose + 2 ADP + 2 Pi + 2 NAD+ ----> 2 Pyruvate + 2 ATP + 2 NADH + 2 H+ + 2 H2O
public class Glycolysis extends ChemicalReaction {
    /** DOCUMENT ME! */
    static Set reactants;

    /** DOCUMENT ME! */
    static Set products;

    static {
        reactants = Collections.EMPTY_SET;
        products = Collections.EMPTY_SET;

        //throw new RuntimeException("Not yet implemented.");
        //TODO Glucose + 2 ADP + 2 Pi + 2 NAD+ ----> 2 Pyruvate + 2 ATP + 2 NADH + 2 H+ + 2 H2O
    }

/**
     * Constructs a Glycolysis.
     */
    public Glycolysis() {
        super(reactants, products);
    }
}
