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
 * The Reaction class is the superclass for all chemical reactions and even
 * nuclear ones.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public abstract class Reaction extends Object {
    /** DOCUMENT ME! */
    private double energy; //the energy that is produced or consumed in the reaction

    //reaction is an equilibrium that leads products to reactants
    /**
     * Creates a new Reaction object.
     */
    public Reaction() {
        this.energy = 0;
    }

    //check that the reactants can actually produce the products (equation is balanced)
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract boolean isValid();

    //the energy that is consumed or produced by the atomistic transformation of reactants into products
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getEnergy() {
        return energy;
    }

    /**
     * DOCUMENT ME!
     *
     * @param energy DOCUMENT ME!
     */
    public void setEnergy(double energy) {
        this.energy = energy;
    }

    //tries to compute the energy out of the reactants and products
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract double computeEnergy();
}
