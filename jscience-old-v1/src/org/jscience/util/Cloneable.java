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
 * Interface for cloneable classes. A cloneable class implements the standard
 * {@link java.lang.Cloneable} interface from J2SE and additionnaly overrides
 * the {@link Object#clone()} method with public access. For some reason lost
 * in the mists of time, the J2SE's {@link java.lang.Cloneable} interface
 * doesn't declare the <code>clone()</code> method, which make it hard to use.
 * This <code>Cloneable</code> interface add this missing method, which avoid
 * the need to cast an interface to its implementation in order to clone it.
 *
 * @see java.lang.Cloneable
 * @see <A
 *      HREF="http://developer.java.sun.com/developer/bugParade/bugs/4098033.html">Cloneable
 *      doesn't define <code>clone()</code></A> on Sun's bug parade
 */

//copied after Geotools (under LGPL)
public interface Cloneable extends java.lang.Cloneable {
    /**
     * Creates and returns a copy of this object. The precise meaning
     * of "copy" may depend on the class of the object.
     *
     * @return A clone of this instance.
     *
     * @see Object#clone
     */
    public Object clone();
}
