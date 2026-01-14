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

package org.jscience.util;

/**
 * An interface to mark up something that contains an extra field parameter to support additional information storage without (sometimes inconveninet subclassing).
 *
 * @author Silvere Martin-Michiellot
 */

//perhaps should be renamed parametrized

//this interface should rather be avoided as it is rather bad object oriented programming practice
//you should instead use the decorator pattern around object which currently implement this interface
//see http://en.wikipedia.org/wiki/Decorator_pattern
//you can also consider using this interface as an entry for a visitor pattern 
//although this is not the intented design
//http://en.wikipedia.org/wiki/Visitor_pattern
public interface Parameterized {
    /**
     * Defines the parameter in an unspecified manner.
     *
     * @return DOCUMENT ME!
     */
    public Object getExtraParameter();

    /**
     * Defines the parameter in an unspecified manner.
     *
     * @param parameter DOCUMENT ME!
     */
    public void setExtraParameter(Object parameter);
}
