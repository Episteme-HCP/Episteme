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

package org.jscience.computing.ai.agents;

/**
 * A class representing a triangular flat cell.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class TriangularCell extends Cell {
    /** DOCUMENT ME! */
    private double side1Length;

    /** DOCUMENT ME! */
    private double side2Length;

    /** DOCUMENT ME! */
    private double side3Length;

/**
     * Creates a new TriangularCell object.
     *
     * @param environment DOCUMENT ME!
     * @param position    DOCUMENT ME!
     * @param side1Length DOCUMENT ME!
     * @param side2Length DOCUMENT ME!
     * @param side3Length DOCUMENT ME!
     */
    public TriangularCell(DiscreteEnvironment environment, int[] position,
        double side1Length, double side2Length, double side3Length) {
        super(environment, position);

        if ((side1Length > 0) && (side2Length > 0)) {
            this.side1Length = side1Length;
            this.side2Length = side2Length;
            this.side3Length = side3Length;
        } else {
            throw new IllegalArgumentException(
                "Side lengths must be strictly positive.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSide1Length() {
        return side1Length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSide2Length() {
        return side2Length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSide3Length() {
        return side3Length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSurface() {
        //using heron formula, also look at http://en.wikipedia.org/wiki/Triangle
        double s = (side1Length + side2Length + side3Length) / 2;

        return Math.sqrt(s * (s - side1Length) * (s - side2Length) * (s -
            side3Length));
    }
}
