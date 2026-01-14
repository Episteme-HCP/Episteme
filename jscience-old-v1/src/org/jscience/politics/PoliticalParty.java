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

package org.jscience.politics;

import org.jscience.economics.Organization;

import org.jscience.geography.BusinessPlace;

import org.jscience.measure.Identification;

import java.util.Set;


/**
 * An organization targetted towards managing a human group living.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//just a tagging class
public class PoliticalParty extends Organization {
/**
     * Creates a new PoliticalParty object.
     *
     * @param name           DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param owners         DOCUMENT ME!
     * @param place          DOCUMENT ME!
     * @param accounts       DOCUMENT ME!
     */
    public PoliticalParty(String name, Identification identification,
        Set owners, BusinessPlace place, Set accounts) {
        super(name, identification, owners, place, accounts);
    }
}
