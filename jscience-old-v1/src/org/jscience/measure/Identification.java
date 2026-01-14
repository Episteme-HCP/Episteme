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

package org.jscience.measure;

/**
 * A class representing an abstract way to identify something
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//You can spot that many classes require an identification, which is defined as String (see StringIdentificationFactory).
//We do not offer better support because... there isn't any.
//Usually each country or company provides its own internal schema to decide what is a valid or invalid identification. 
//Books (ISBN), other goods (ASIN), banks accounts, companies have some sort of international conventions but which have many exceptions.
//should be renamed Identifiable
public abstract class Identification extends Object implements java.io.Serializable {
    //a String that describes the common name for that identification scheme
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract String getDescription();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract String toString();

    /**
     * DOCUMENT ME!
     *
     * @param object DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract boolean equals(Object object);
}
