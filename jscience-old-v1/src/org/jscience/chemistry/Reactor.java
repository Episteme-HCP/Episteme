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
 * The Reactor class is the superclass for all reactions.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public abstract class Reactor extends Object {
    //this is the data you initially put in and where changed due to calls to react() or getEquilibrium()
    /**
     * DOCUMENT ME!
     */
    public abstract void getInitialConditions();

    //this is the simulator part which transforms part of the reactants into products
    /**
     * DOCUMENT ME!
     *
     * @param dt DOCUMENT ME!
     */
    public abstract void react(double dt);

    //proceeds to the end equilibrium by reacting and reacting again from the current parameters to the final equilibrium
    /**
     * DOCUMENT ME!
     */
    public abstract void getEquilibrium();

    //the complete energy produced in that reaction from the initial conditions to the equilibrium
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract double getSumEnergy();
}
