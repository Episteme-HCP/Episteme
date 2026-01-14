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

package org.jscience.philosophy;

/**
 * A class representing a philosophy targetted towards supernatural beings,
 * the meaning of life, etc.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Religion extends Belief {
    /** DOCUMENT ME! */
    public int numberOfBelievers;

/**
     * Creates a new Religion object.
     *
     * @param name     DOCUMENT ME!
     * @param comments DOCUMENT ME!
     */
    public Religion(String name, String comments) {
        super(name, comments);
        numberOfBelievers = 0;
    }

    //number of people obeing to this religion/cult
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumberOfBelievers() {
        return numberOfBelievers;
    }

    //must be >=0;
    /**
     * DOCUMENT ME!
     *
     * @param believers DOCUMENT ME!
     */
    public void setNumberOfBelievers(int believers) {
        if (believers >= 0) {
            this.numberOfBelievers = believers;
        } else {
            throw new IllegalArgumentException(
                "Number of believers can't be negative.");
        }
    }

    //a religion can be quite complex:
    //chief
    //organization (hierarchy, public organization...)
    //rules
    //ceremonies
    //contracts of faith (wedding...)
    //awards (for example canonization...)
    //status (believer, priest, monk...)
    //artifacts (holy tools and remains)
    //supernatural beings (names, roles)
    //holy places, and others historical facts
    //rituals
}
