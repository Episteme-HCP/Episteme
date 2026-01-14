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

package org.jscience.media.pictures.filters.math;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class MathFunction1D implements Function1D {
    /** DOCUMENT ME! */
    public final static int SIN = 1;

    /** DOCUMENT ME! */
    public final static int COS = 2;

    /** DOCUMENT ME! */
    public final static int TAN = 3;

    /** DOCUMENT ME! */
    public final static int SQRT = 4;

    /** DOCUMENT ME! */
    public final static int ASIN = -1;

    /** DOCUMENT ME! */
    public final static int ACOS = -2;

    /** DOCUMENT ME! */
    public final static int ATAN = -3;

    /** DOCUMENT ME! */
    public final static int SQR = -4;

    /** DOCUMENT ME! */
    private int operation;

/**
     * Creates a new MathFunction1D object.
     *
     * @param operation DOCUMENT ME!
     */
    public MathFunction1D(int operation) {
        this.operation = operation;
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float evaluate(float v) {
        switch (operation) {
        case SIN:
            return (float) Math.sin(v);

        case COS:
            return (float) Math.cos(v);

        case TAN:
            return (float) Math.tan(v);

        case SQRT:
            return (float) Math.sqrt(v);

        case ASIN:
            return (float) Math.asin(v);

        case ACOS:
            return (float) Math.acos(v);

        case ATAN:
            return (float) Math.atan(v);

        case SQR:
            return v * v;
        }

        return v;
    }
}
