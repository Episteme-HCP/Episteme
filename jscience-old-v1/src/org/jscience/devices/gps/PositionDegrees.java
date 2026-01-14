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

package org.jscience.devices.gps;

/**
 * Class used to store degrees, usually latitude or longitude.
 */
public class PositionDegrees {
    /** DOCUMENT ME! */
    protected double value;

/**
     * Creates a new PositionDegrees object.
     *
     * @param v DOCUMENT ME!
     */
    public PositionDegrees(double v) {
        value = v;
    }

    /**
     * Returns the degrees part of this object, when converted to
     * coordinates.
     *
     * @return DOCUMENT ME!
     */
    public int getDegrees() {
        return (int) value;
    }

    /**
     * Converts the degrees to Radians.
     *
     * @return DOCUMENT ME!
     */
    public PositionRadians convertToRadians() {
        return new PositionRadians((value * Math.PI) / 180.0d);
    }

    /**
     * Returns the minutes part of this object, when converted to
     * coordinates.
     *
     * @return DOCUMENT ME!
     */
    public double getMinutes() {
        double v = value;
        v -= getDegrees();

        return 60 * v; // 60 minutes in one degree.
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return String.valueOf(getDegrees()) + "\' " +
        String.valueOf((int) getMinutes()) + "\"";
    }
}
