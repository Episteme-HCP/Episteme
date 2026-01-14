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
 * This is a class meant for containing positions.
 */
public class Position implements IPosition {
    /** DOCUMENT ME! */
    private PositionRadians lat;

    /** DOCUMENT ME! */
    private PositionRadians lon;

/**
     * Makes a new position. Initializes the latitude and the longitude to 0.
     */
    public Position() {
        this(0, 0);
    }

/**
     * Initializes the Position with la as the latitude and lo as the
     * longitude.
     *
     * @param la DOCUMENT ME!
     * @param lo DOCUMENT ME!
     */
    public Position(double la, double lo) {
        lat = new PositionRadians(la);
        lon = new PositionRadians(lo);
    }

/**
     * Creates a new Position object.
     *
     * @param la DOCUMENT ME!
     * @param lo DOCUMENT ME!
     */
    public Position(PositionRadians la, PositionRadians lo) {
        lat = la;
        lon = lo;
    }

/**
     * Initializes the position object from an IPosition reference.
     *
     * @param pos DOCUMENT ME!
     */
    public Position(IPosition pos) {
        lat = pos.getLatitude();
        lon = pos.getLongitude();
    }

    /**
     * Sets the latitude of this position.
     *
     * @param l DOCUMENT ME!
     */
    public void setLatitude(PositionRadians l) {
        lat = l;
    }

    /**
     * Sets the longitude of this position.
     *
     * @param l DOCUMENT ME!
     */
    public void setLongitude(PositionRadians l) {
        lon = l;
    }

    /**
     * Returns the latitude of this position.
     *
     * @return DOCUMENT ME!
     */
    public PositionRadians getLatitude() {
        return lat;
    }

    /**
     * Returns the longitude of this position.
     *
     * @return DOCUMENT ME!
     */
    public PositionRadians getLongitude() {
        return lon;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        if ((lat == null) || (lon == null)) {
            return "error printing position packet, long or lat is null";
        }

        return "position[" + String.valueOf(lat.getDegrees()) + "' " +
        String.valueOf((int) lat.getMinutes()) + "\" x " +
        String.valueOf(lon.getDegrees()) + "' " +
        String.valueOf((int) lon.getMinutes()) + "\"]";
    }
}
