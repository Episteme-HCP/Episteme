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

package org.jscience.law;

import org.jscience.biology.Individual;

import org.jscience.sociology.Role;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;


/**
 * The people against whom the trial is done, may be a human, a group, a
 * country... (a Set of HumanGroups)
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Defendant extends Role {
    /** DOCUMENT ME! */
    private Set charges;

/**
     * Creates a new Defendant object.
     *
     * @param individual       DOCUMENT ME!
     * @param lawSuitSituation DOCUMENT ME!
     */
    public Defendant(Individual individual, LawSuitSituation lawSuitSituation) {
        super(individual, "Defendant", lawSuitSituation, Role.CLIENT);
        charges = Collections.EMPTY_SET;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getCharges() {
        return charges;
    }

    /**
     * DOCUMENT ME!
     *
     * @param charges DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setCharges(Set charges) {
        Iterator iterator;
        boolean valid;

        if (charges != null) {
            iterator = charges.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof String;
            }

            if (valid) {
                this.charges = charges;
            } else {
                throw new IllegalArgumentException(
                    "The Set of charges should contain only Strings.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of charges shouldn't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param charge DOCUMENT ME!
     */
    public void addCharge(String charge) {
        charges.add(charge);
    }

    /**
     * DOCUMENT ME!
     *
     * @param charge DOCUMENT ME!
     */
    public void removeCharge(String charge) {
        charges.remove(charge);
    }
}
