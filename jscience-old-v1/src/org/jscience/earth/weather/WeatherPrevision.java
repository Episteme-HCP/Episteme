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

package org.jscience.earth.weather;

import org.jscience.geography.Place;

import org.jscience.util.Positioned;

import java.util.Date;


/**
 * A class representing the main characteristics of a weather prediction.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//http://www.doc.mmu.ac.uk/aric/eae/Weather/weather.html
//see ftp://tgftp.nws.noaa.gov/data/observations/metar/decoded/ for metar obeservations
//also see http://www.ncdc.noaa.gov/oa/climate/stationlocator.html
//we could store extra fields for example eclipses
public class WeatherPrevision extends Object implements Positioned {
    /** DOCUMENT ME! */
    private Date date; //when

    /** DOCUMENT ME! */
    private Place place; //where is it

    /** DOCUMENT ME! */
    private String description; //what the weather is like in human readable format

    /** DOCUMENT ME! */
    private Date sunriseTime; // Sunrise time at the location

    /** DOCUMENT ME! */
    private Date sunsetTime; //Sunset time at the location

    /** DOCUMENT ME! */
    private float highTemperature; //High temperature for this day (expected)

    /** DOCUMENT ME! */
    private float lowTemperature; //Low temperature for this day (expected)

    /** DOCUMENT ME! */
    private float pressure; //pressure for this day (expected)

    /** DOCUMENT ME! */
    private float precipitation; //millimeters of rainfall/snowfall (expected)

    /** DOCUMENT ME! */
    private float cloudCover; //total received energy (joules/square meter) (expected)

    /** DOCUMENT ME! */
    private int airQuality; //overall quality (expected)

    /** DOCUMENT ME! */
    private int UVindex; //overall value (expected)

    /** DOCUMENT ME! */
    private float visibility; //worst value for the day (in meters)

    /** DOCUMENT ME! */
    private float snowHeight; //at location (actual value plus expected value) (in meters)

    /** DOCUMENT ME! */
    private float seaHeight; //at location (worst expected) (in meters)

    /** DOCUMENT ME! */
    private Date firstHighTideTime; // tide time at the location

    /** DOCUMENT ME! */
    private Date secondHighTideTime; //Sunset time at the location

    /** DOCUMENT ME! */
    private Date moonriseTime; // moonrise time at the location

    /** DOCUMENT ME! */
    private Date moonsetTime; //moonset time at the location

/**
     * Creates a new WeatherPrevision object.
     *
     * @param date        DOCUMENT ME!
     * @param place       DOCUMENT ME!
     * @param description DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public WeatherPrevision(Date date, Place place, String description) {
        if ((date != null) && (place != null) && (description != null)) {
            this.date = date;
            this.place = place;
            this.description = description;
            this.sunriseTime = null;
            this.sunsetTime = null;
            this.highTemperature = 0;
            this.lowTemperature = 0;
            this.pressure = 0;
            this.precipitation = 0;
            this.cloudCover = 0;
            this.airQuality = 0; //from 0 to 300 or more
            this.UVindex = 1; //for 1 to 11 or more
            this.visibility = Float.MAX_VALUE;
            this.snowHeight = 0;
            this.seaHeight = 0;
            this.firstHighTideTime = null;
            this.secondHighTideTime = null;
            this.moonriseTime = null;
            this.moonsetTime = null;
        } else {
            throw new IllegalArgumentException(
                "The WeatherPrevision constructor doesn't accept null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getDate() {
        return date;
    }

    /**
     * DOCUMENT ME!
     *
     * @param date DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setDate(Date date) {
        if (date != null) {
            this.date = date;
        } else {
            throw new IllegalArgumentException("You can't set a null date.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Place getPosition() {
        return place;
    }

    /**
     * DOCUMENT ME!
     *
     * @param place DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setPosition(Place place) {
        if (place != null) {
            this.place = place;
        } else {
            throw new IllegalArgumentException("You can't set a null place.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDescription() {
        return description;
    }

    /**
     * DOCUMENT ME!
     *
     * @param description DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setDescription(String description) {
        if (description != null) {
            this.description = description;
        } else {
            throw new IllegalArgumentException(
                "You can't set a null description.");
        }
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getSunriseTime() {
        return sunriseTime;
    }

    /**
     * DOCUMENT ME!
     *
     * @param sunriseTime DOCUMENT ME!
     */
    public void setSunriseTime(Date sunriseTime) {
        this.sunriseTime = sunriseTime;
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getSunsetTime() {
        return sunsetTime;
    }

    /**
     * DOCUMENT ME!
     *
     * @param sunsetTime DOCUMENT ME!
     */
    public void setSunsetTime(Date sunsetTime) {
        this.sunsetTime = sunsetTime;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getHighTemperature() {
        return highTemperature;
    }

    /**
     * DOCUMENT ME!
     *
     * @param highTemperature DOCUMENT ME!
     */
    public void setHighTemperature(float highTemperature) {
        this.highTemperature = highTemperature;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getLowTemperature() {
        return lowTemperature;
    }

    /**
     * DOCUMENT ME!
     *
     * @param lowTemperature DOCUMENT ME!
     */
    public void setLowTemperature(float lowTemperature) {
        this.lowTemperature = lowTemperature;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getPressure() {
        return pressure;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pressure DOCUMENT ME!
     */
    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getPrecipitation() {
        return precipitation;
    }

    /**
     * DOCUMENT ME!
     *
     * @param precipitation DOCUMENT ME!
     */
    public void setPrecipitation(float precipitation) {
        this.precipitation = precipitation;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getCloudCover() {
        return cloudCover;
    }

    /**
     * DOCUMENT ME!
     *
     * @param cloudCover DOCUMENT ME!
     */
    public void setCloudCover(float cloudCover) {
        this.cloudCover = cloudCover;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getAirQuality() {
        return airQuality;
    }

    /**
     * DOCUMENT ME!
     *
     * @param airQuality DOCUMENT ME!
     */
    public void setAirQuality(int airQuality) {
        this.airQuality = airQuality;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getUVindex() {
        return UVindex;
    }

    /**
     * DOCUMENT ME!
     *
     * @param UVindex DOCUMENT ME!
     */
    public void setUVindex(int UVindex) {
        this.UVindex = UVindex;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getVisibility() {
        return visibility;
    }

    /**
     * DOCUMENT ME!
     *
     * @param visibility DOCUMENT ME!
     */
    public void setVisibility(float visibility) {
        this.visibility = visibility;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getSnowHeight() {
        return snowHeight;
    }

    /**
     * DOCUMENT ME!
     *
     * @param snowHeight DOCUMENT ME!
     */
    public void setSnowHeight(float snowHeight) {
        this.snowHeight = snowHeight;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getSeaHeight() {
        return seaHeight;
    }

    /**
     * DOCUMENT ME!
     *
     * @param seaHeight DOCUMENT ME!
     */
    public void setSeaHeight(float seaHeight) {
        this.seaHeight = seaHeight;
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getFirstHighTideTime() {
        return firstHighTideTime;
    }

    /**
     * DOCUMENT ME!
     *
     * @param firstHighTideTime DOCUMENT ME!
     */
    public void setFirstHighTideTime(Date firstHighTideTime) {
        this.firstHighTideTime = firstHighTideTime;
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getSecondHighTideTime() {
        return secondHighTideTime;
    }

    /**
     * DOCUMENT ME!
     *
     * @param secondHighTideTime DOCUMENT ME!
     */
    public void setSecondHighTideTime(Date secondHighTideTime) {
        this.secondHighTideTime = secondHighTideTime;
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getMoonriseTime() {
        return moonriseTime;
    }

    /**
     * DOCUMENT ME!
     *
     * @param moonriseTime DOCUMENT ME!
     */
    public void setMoonriseTime(Date moonriseTime) {
        this.moonriseTime = moonriseTime;
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getMoonsetTime() {
        return moonsetTime;
    }

    /**
     * DOCUMENT ME!
     *
     * @param moonsetTime DOCUMENT ME!
     */
    public void setMoonsetTime(Date moonsetTime) {
        this.moonsetTime = moonsetTime;
    }
}
