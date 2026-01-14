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

package org.jscience.psychology.social;

import org.jscience.biology.Species;

import org.jscience.geography.Place;


/**
 * A class representing a family which is a group whose individual share
 * some specific bond (usually genetic).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//due to Java single inheritance scheme you have to subclass this class or the HumanGroup class to define a HumanFamily class.
//TIP: you can use this class with HistoricalIndividuals to define a FamilyGroup
//though we perfom here no check if all members are parents of one another.
//family can also be thought as a situation, with roles such has parent, child, etc 
public class Family extends Group {
/**
     * Creates a new Family object.
     *
     * @param species DOCUMENT ME!
     */
    public Family(Species species) {
        super(species);
    }

/**
     * Creates a new Family object.
     *
     * @param species         DOCUMENT ME!
     * @param formalTerritory DOCUMENT ME!
     */
    public Family(Species species, Place formalTerritory) {
        super(species, formalTerritory);
    }
}
