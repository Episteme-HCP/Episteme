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

/**
 * The OxydoReduction class provides support for ionic reaction.
 * @version 1.0
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 */

import org.jscience.physics.electricity.circuits.Generator;

import java.util.Set;

public class OxidoReduction extends ChemicalReaction {

    private double potential;

    public OxidoReduction(Set reactants, Set products, double potential) {

        super(reactants, products);
        this.potential = potential;

    }

    //the electrons involved in this reaction
    public int getNumElectrons() {

        XXX
    }

    public double getStandardPotential() {

        return potential;

    }

    //build out a pile using this reaction to define actual current
    public Generator getGenerator(double period, int signal) {
        XXXXXXXXXX DC
        and potential
        return new Generator(getPotential(), period, signal);

    }

    //ions are exchanged in the solution
    modify pH

    public double getPotential() {

        http:
//fr.encarta.msn.com/encyclopedia_761591014/oxydor%C3%A9duction.html
        E0 + RT / nF
        ln(Ox / Red)

    }

}
