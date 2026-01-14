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
 * The people who have seen and come to testimony.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Witness extends Role {
    /** DOCUMENT ME! */
    private Set parties; //who you are helping

/**
     * Creates a new Witness object.
     *
     * @param individual       DOCUMENT ME!
     * @param lawSuitSituation DOCUMENT ME!
     */
    public Witness(Individual individual, LawSuitSituation lawSuitSituation) {
        super(individual, "Witness", lawSuitSituation, Role.SERVER);
        parties = Collections.EMPTY_SET;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getClients() {
        return parties;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parties DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setClients(Set parties) {
        Iterator iterator;
        boolean valid;
        Object value;

        if (parties != null) {
            iterator = parties.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                value = iterator.next();
                valid = (value instanceof Plaintiff) ||
                    (value instanceof Defendant);
            }

            if (valid) {
                this.parties = parties;
            } else {
                throw new IllegalArgumentException(
                    "The Set of parties should contain only Plaintiffs and Defendants.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of parties shouldn't be null.");
        }
    }
}
