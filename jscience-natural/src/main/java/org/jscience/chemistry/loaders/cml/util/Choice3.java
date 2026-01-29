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

package org.jscience.chemistry.loaders.cml.util;

/**
 * offers the choice of 0, 1 or 2; useful to restrict argument values
 * (rather like enum). The user cannot access the constructor so has to use
 * Choice3.Y, etc.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Choice3 {
    /** Constant for choice X (value 0). */
    public static final Choice3 X = new Choice3(0);

    /** Constant for choice Y (value 1). */
    public static final Choice3 Y = new Choice3(1);

    /** Constant for choice Z (value 2). */
    public static final Choice3 Z = new Choice3(2);

    /** Constant for choice zero (value 0). */
    public static final Choice3 zero = new Choice3(0);

    /** Constant for choice one (value 1). */
    public static final Choice3 one = new Choice3(1);

    /** Constant for choice two (value 2). */
    public static final Choice3 two = new Choice3(2);

    /** The integer value represented by this choice. */
    int value;

    /**
     * Creates a new Choice3 object.
     *
     * @param i the integer value for this choice
     */
    Choice3(int i) {
        this.value = i;
    }

    /**
     * Returns the integer value of this choice.
     *
     * @return the integer value
     */
    public int getValue() {
        return value;
    }
}
