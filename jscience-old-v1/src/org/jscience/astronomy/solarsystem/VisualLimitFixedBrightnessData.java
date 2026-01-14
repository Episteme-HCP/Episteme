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

/**
 * \ VisLimitFixedBrightnessData \
 */
package org.jscience.astronomy.solarsystem;

/**
 * A support class for VisualLimit.
 * <p/>
 * <p/>
 * Holds values which are constant at a given time
 * </p>
 */
public class VisualLimitFixedBrightnessData {
    /**
     * The lunar zenith angle
     */
    private double zenithAngMoon;

    /**
     * The solar zenith angle
     */
    private double zenithAngSun;

    /**
     * The lunar elongation
     */
    private double moonElongation;

    /**
     * Altitude (above sea level) in meters
     */
    private double htAboveSeaInMeters;

    /**
     * Latitude
     */
    private double latitude;

    /**
     * Temperature in degrees Centigrade
     */
    private double temperatureInC;

    /**
     * Relative humidity
     */
    private double relativeHumidity;

    /**
     * Year
     */
    private double year;

    /**
     * Month
     */
    private double month;

    /**
     * Creates a new VisualLimitFixedBrightnessData object.
     */
    public VisualLimitFixedBrightnessData() {
        zenithAngMoon = 0D;
        zenithAngSun = 0D;
        moonElongation = 0D;
        htAboveSeaInMeters = 0D;
        latitude = 0D;
        temperatureInC = 0D;
        relativeHumidity = 0D;
        year = 0D;
        month = 0D;
    }

    /**
     * Creates a new VisualLimitFixedBrightnessData object.
     *
     * @param zm  DOCUMENT ME!
     * @param zs  DOCUMENT ME!
     * @param me  DOCUMENT ME!
     * @param h   DOCUMENT ME!
     * @param lat DOCUMENT ME!
     * @param t   DOCUMENT ME!
     * @param rh  DOCUMENT ME!
     * @param y   DOCUMENT ME!
     * @param m   DOCUMENT ME!
     */
    public VisualLimitFixedBrightnessData(double zm, double zs, double me,
                                          double h, double lat, double t, double rh, double y, double m) {
        zenithAngMoon = zm;
        zenithAngSun = zs;
        moonElongation = me;
        htAboveSeaInMeters = h;
        latitude = lat;
        temperatureInC = t;
        relativeHumidity = rh;
        year = y;
        month = m;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMoonZenithAngle() {
        return zenithAngMoon;
    }

    /**
     * DOCUMENT ME!
     *
     * @param zenithAngMoon DOCUMENT ME!
     */
    public void setMoonZenithAngle(double zenithAngMoon) {
        this.zenithAngMoon = zenithAngMoon;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSunZenithAngle() {
        return zenithAngSun;
    }

    /**
     * DOCUMENT ME!
     *
     * @param zenithAngSun DOCUMENT ME!
     */
    public void setSunZenithAngle(double zenithAngSun) {
        this.zenithAngSun = zenithAngSun;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMoonElongation() {
        return moonElongation;
    }

    /**
     * DOCUMENT ME!
     *
     * @param moonElongation DOCUMENT ME!
     */
    public void setMoonElongation(double moonElongation) {
        this.moonElongation = moonElongation;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getHeightAboveSea() {
        return htAboveSeaInMeters;
    }

    /**
     * DOCUMENT ME!
     *
     * @param htAboveSeaInMeters DOCUMENT ME!
     */
    public void setHeightAboveSea(double htAboveSeaInMeters) {
        this.htAboveSeaInMeters = htAboveSeaInMeters;
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
     * @param latitude DOCUMENT ME!
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getTemperature() {
        return temperatureInC;
    }

    /**
     * DOCUMENT ME!
     *
     * @param temperatureInC DOCUMENT ME!
     */
    public void setTemperature(double temperatureInC) {
        this.temperatureInC = temperatureInC;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getRelativeHumidity() {
        return relativeHumidity;
    }

    /**
     * DOCUMENT ME!
     *
     * @param relativeHumidity DOCUMENT ME!
     */
    public void setRelativeHumidity(double relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getYear() {
        return year;
    }

    /**
     * DOCUMENT ME!
     *
     * @param year DOCUMENT ME!
     */
    public void setYear(double year) {
        this.year = year;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMonth() {
        return month;
    }

    /**
     * DOCUMENT ME!
     *
     * @param month DOCUMENT ME!
     */
    public void setMonth(double month) {
        this.month = month;
    }

}
