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

package org.jscience.economics;

import org.jscience.biology.Individual;

import org.jscience.sociology.Situation;


/**
 * A class representing the interaction of people around resources.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//you may prefer this class to org.jscience.sociology.Situations.WORKING
public class EconomicSituation extends Situation {
    //use the organization name as the name, or a part of it if your organization is big
    /**
     * Creates a new EconomicSituation object.
     *
     * @param name DOCUMENT ME!
     * @param comments DOCUMENT ME!
     */
    public EconomicSituation(String name, String comments) {
        super(name, comments);
    }

    //builds out a worker
    /**
     * DOCUMENT ME!
     *
     * @param individual DOCUMENT ME!
     */
    public void addEconomicAgent(Individual individual) {
        super.addRole(new EconomicAgent(individual, this));
    }
}
