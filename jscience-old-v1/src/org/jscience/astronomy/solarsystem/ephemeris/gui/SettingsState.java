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

//this code is rebundled after the code from
//Peter Csapo at http://www.pcsapo.com/csphere/csphere.html
//mailto:peter@pcsapo.com
//website:http://www.pcsapo.com/csphere/csphere.html
//the author agreed we reuse his code under GPL
package org.jscience.astronomy.solarsystem.ephemeris.gui;

import org.jscience.astronomy.solarsystem.ephemeris.Angle;
import org.jscience.astronomy.solarsystem.ephemeris.CalendarDate;
import org.jscience.astronomy.solarsystem.ephemeris.JulianDay;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class SettingsState {
    /** DOCUMENT ME! */
    static final double TwoPI = 6.2831853071795862D;

    /** DOCUMENT ME! */
    public boolean stars;

    /** DOCUMENT ME! */
    public boolean graticule;

    /** DOCUMENT ME! */
    public boolean constellations;

    /** DOCUMENT ME! */
    public boolean names;

    /** DOCUMENT ME! */
    public boolean boundaries;

    /** DOCUMENT ME! */
    public boolean colour;

    /** DOCUMENT ME! */
    public boolean animate;

    /** DOCUMENT ME! */
    public boolean planets;

    /** DOCUMENT ME! */
    public boolean info;

    /** DOCUMENT ME! */
    public boolean messiers;

    /** DOCUMENT ME! */
    public boolean clip_horizon;

    /** DOCUMENT ME! */
    public boolean brighter;

    /** DOCUMENT ME! */
    private double zoom;

    /** DOCUMENT ME! */
    private double JD;

    /** DOCUMENT ME! */
    private double JD_offset;

    /** DOCUMENT ME! */
    private double latitude;

    /** DOCUMENT ME! */
    private double longitude;

    /** DOCUMENT ME! */
    private double azimuth;

    /** DOCUMENT ME! */
    private double altitude;

/**
     * Creates a new SettingsState object.
     */
    SettingsState() {
        stars = true;
        graticule = true;
        constellations = true;
        names = true;
        boundaries = true;
        colour = false;
        animate = true;
        planets = true;
        info = false;
        messiers = true;
        clip_horizon = true;
        brighter = false;
        zoom = 1.5D;
        JD = JulianDay.now();
        JD_offset = 0.0D;
        altitude = 1.5707963267948966D;
    }

/**
     * Creates a new SettingsState object.
     *
     * @param settingsstate DOCUMENT ME!
     */
    SettingsState(SettingsState settingsstate) {
        stars = true;
        graticule = true;
        constellations = true;
        names = true;
        boundaries = true;
        colour = false;
        animate = true;
        planets = true;
        info = false;
        messiers = true;
        clip_horizon = true;
        brighter = false;
        zoom = 1.5D;
        JD = JulianDay.now();
        JD_offset = 0.0D;
        altitude = 1.5707963267948966D;
        set(settingsstate);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getZoom() {
        return zoom;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     */
    public void setZoom(double d) {
        zoom = d;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     */
    public void multZoom(double d) {
        zoom *= d;

        if (zoom < 1.0D) {
            zoom = 1.0D;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     */
    public void incAz(double d) {
        azimuth = Angle.reduce(azimuth + d);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     */
    public void decAz(double d) {
        azimuth = Angle.reduce(azimuth - d);
    }

    /**
     * DOCUMENT ME!
     *
     * @param settingsstate DOCUMENT ME!
     */
    public void set(SettingsState settingsstate) {
        brighter = settingsstate.brighter;
        clip_horizon = settingsstate.clip_horizon;
        stars = settingsstate.stars;
        graticule = settingsstate.graticule;
        constellations = settingsstate.constellations;
        names = settingsstate.names;
        boundaries = settingsstate.boundaries;
        colour = settingsstate.colour;
        animate = settingsstate.animate;
        planets = settingsstate.planets;
        info = settingsstate.info;
        latitude = settingsstate.latitude;
        longitude = settingsstate.longitude;
        JD = settingsstate.JD;
        JD_offset = settingsstate.JD_offset;
        zoom = settingsstate.zoom;
        messiers = settingsstate.messiers;
        azimuth = settingsstate.azimuth;
        altitude = settingsstate.altitude;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     */
    public void incTimeOffset(double d) {
        JD_offset += d;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     */
    public void setLatitude(double d) {
        latitude = d;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CalendarDate getDate() {
        return JulianDay.getGregorian(JD + JD_offset);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     */
    public void decTimeOffset(double d) {
        JD_offset -= d;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     */
    public void incLongitude(double d) {
        longitude = Angle.reduce(longitude + d);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     */
    public void decLongitude(double d) {
        longitude = Angle.reduce(longitude - d);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     */
    public void decLatitude(double d) {
        latitude = limitRange(latitude - d);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSiderealTime() {
        return JulianDay.LST(JD + JD_offset, longitude);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     */
    public void setJulianDay(double d) {
        JD = d;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double limitRange(double d) {
        if (d > 1.5707963267948966D) {
            d = 1.5707963267948966D;
        }

        if (d < -1.5707963267948966D) {
            d = -1.5707963267948966D;
        }

        return d;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getTimeOffset() {
        return JD_offset;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     */
    public void setTimeOffset(double d) {
        JD_offset = d;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     */
    public void setLongitude(double d) {
        longitude = d;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getAltitude() {
        return altitude;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     */
    public void setAltitude(double d) {
        altitude = d;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     */
    public void incAlt(double d) {
        altitude = limitRange(altitude + d);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getAzimuth() {
        return azimuth;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     */
    public void setAzimuth(double d) {
        azimuth = d;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     */
    public void incLatitude(double d) {
        latitude = limitRange(latitude + d);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getJulianEphemerisDay() {
        return JulianDay.inDynamicalTime(JD + JD_offset);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     */
    public void decAlt(double d) {
        altitude = limitRange(altitude - d);

        if (altitude < 0.0D) {
            altitude = 0.0D;
        }
    }
}
