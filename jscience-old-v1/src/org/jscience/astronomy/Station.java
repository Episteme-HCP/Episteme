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

package org.jscience.astronomy;

import org.jscience.util.Named;


/**
 * The Station class provides support for surface based observations.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
//we assume that the considered planet has defined the concepts of latitude and longitude around its axis
//as well as a zero for height
public class Station implements Named {

    /**
     * The name of the observatory.
     * <p/>
     * <p>This is not only for information, but the name given by the user is
     * also used to look up the observatory location in a file that contains a
     * list of observatories.
     */

    private String itsName;

    /**
     * Geographic longitude in rad.
     * <p/>
     * <p>Positive to the East.
     */
    private double itsLong;

    /**
     * Geodetic (geographic) latitude in rad.
     * <p/>
     * <p>This is the elevation of the North Celestial Pole.
     */
    private double itsLat;

    /**
     * Height above sea level in m.
     * <p/>
     * <p>This is the height above the ellipsoidal geoid.
     */
    private double itsHeight;

    public Station(String name, double longitude, double latitude, double height) {

        if (name != null) {
            itsName = name;
            itsLong = longitude;
            itsLat = latitude;
            itsHeight = height;
        } else throw new IllegalArgumentException("You can't set a null name.");

    }

    /**
     * Return the station name.
     */

    public final String getName() {

        return itsName;

    }

    /**
     * Sets the station name.
     */
    public final void setName(String name) {

        if (name != null) {
            itsName = name;
        } else throw new IllegalArgumentException("You can't set a null name.");

    }

    /**
     * Return the station geodetic (geographic) latitude (in radian).
     */
    public final double getLatitude() {

        return itsLat;

    }

    /**
     * Sets the station geodetic (geographic) latitude (in radian).
     */
    public final void setLatitude(double latitude) {

        itsLat = latitude;

    }

    /**
     * Return the station geographic longitude (in radian).
     */
    public final double getLongitude() {

        return itsLong;

    }

    /**
     * Sets the station geodetic (geographic) longitude (in radian).
     */
    public final void setLongitude(double longitude) {

        itsLong = longitude;

    }

    /**
     * Return the station elevation above sea level (in m).
     */
    public final double getHeight() {

        return itsHeight;

    }

    /**
     * Sets the station elevation above sea level (in m).
     */
    public final void setHeight(double height) {

        itsHeight = height;

    }

    /**
     * Display the position in various representations.
     */
    public String toString() {

        return new String("Name: " + itsName + ", Longitude: " + itsLong + ", Latitude: " + itsLat + ", Height: " + itsHeight);

    }

}
