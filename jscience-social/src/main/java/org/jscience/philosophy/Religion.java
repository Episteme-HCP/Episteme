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

import java.io.Serializable;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;

/**
 * A specialized type of philosophical belief directed towards supernatural 
 * entities, moral codes, and existential meaning.
 * 
 * <p> Religions include tracking of social impact such as the number 
 *     of believers.</p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 6.0, July 21, 2014
 */
@Persistent
public class Religion extends Belief implements Serializable {


    private static final long serialVersionUID = 1L;

    @Attribute
    private long numberOfBelievers;

    /**
     * Creates a new Religion.
     *
     * @param name     the name of the religion or faith system
     * @param comments descriptive details, origins, or core tenets
     */
    public Religion(String name, String comments) {
        super(name, comments);
        this.numberOfBelievers = 0;
    }

    /**
     * Returns the estimated number of individuals following this religion.
     * @return count of believers
     */
    public long getNumberOfBelievers() {
        return numberOfBelievers;
    }

    /**
     * Sets the number of believers.
     * @param believers count must be non-negative
     * @throws IllegalArgumentException if believers is negative
     */
    public void setNumberOfBelievers(long believers) {
        if (believers < 0) {
            throw new IllegalArgumentException("Number of believers cannot be negative.");
        }
        this.numberOfBelievers = believers;
    }

    @Override
    public String toString() {
        return super.getName() + " [" + numberOfBelievers + " followers]";
    }
}
