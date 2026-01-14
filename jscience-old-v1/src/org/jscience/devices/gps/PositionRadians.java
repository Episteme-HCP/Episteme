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
 * Class used to store radians, usually latitude or longitude. Contains
 * methods for converting to the format degress,minutes.
 */
public class PositionRadians {
    /** DOCUMENT ME! */
    private final double value;

/**
     * Initializes the PositionRadians-object. After the object is constructed,
     * it can't change is value.
     *
     * @param v DOCUMENT ME!
     */
    public PositionRadians(double v) {
        value = v;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getRadians() {
        return value;
    }

    /**
     * Returns the degrees part of this object, when converted to
     * coordinates.
     *
     * @return DOCUMENT ME!
     */
    public int getDegrees() {
        return (int) (value * (180.0d / Math.PI));
    }

    /**
     * Returns the minutes part of this object, when converted to
     * coordinates.
     *
     * @return DOCUMENT ME!
     */
    public double getMinutes() {
        double v = value * (180.0d / Math.PI);
        v -= getDegrees();

        return 60 * v; // 60 minutes in one degree.
    }

    /**
     * Tests if the two PositionRadians contains the same value.
     *
     * @param p DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(PositionRadians p) {
        if (value == p.value) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests if this PositionRadians is greater than p.
     *
     * @param p DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean greaterThan(PositionRadians p) {
        if (value > p.value) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests if this PositionRadians is smaller than p.
     *
     * @param p DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean smallerThan(PositionRadians p) {
        if (value < p.value) {
            return true;
        } else {
            return false;
        }
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
