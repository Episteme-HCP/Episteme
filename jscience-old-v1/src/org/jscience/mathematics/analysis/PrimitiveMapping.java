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

package org.jscience.mathematics.analysis;

/**
 * This interface defines a map or function. It is used to pass user-defined
 * functions to some of the other maths classes.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 * @see org.jscience.mathematics.analysis.NumericalMathUtils
 * @see AbstractMapping
 */

//you should implement map(double) and provide converters for the other methods
//OR you implement a mapping for, say, int, and throw errors for the others methods meaning that this is a map only defined on ints.
//this class is a wrapping class to provide support for mappings with minimal object initialization
public interface PrimitiveMapping extends AbstractMapping {
    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double map(int x);

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double map(long x);

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double map(float x);

    /**
     * A user-defined function. We do not state anything about the
     * domain on which this function applies.
     *
     * @see See org.jscience.mathematics.analysis.IntervalList.
     */
    public double map(double x);
}
