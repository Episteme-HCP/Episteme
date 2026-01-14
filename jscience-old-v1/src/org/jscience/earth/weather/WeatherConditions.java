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
 * A class representing the main characteristics of a weather report
 * (METAR...).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//http://www.webalgorithm.com/ is a source of inspiration
//to learn a bit about weather
//http://www.doc.mmu.ac.uk/aric/eae/Weather/weather.html
public class WeatherConditions extends Object implements Positioned {
    /** DOCUMENT ME! */
    private Date date;

    /** DOCUMENT ME! */
    private String stationName;

    /** DOCUMENT ME! */
    private Place stationLocation;

    /** DOCUMENT ME! */
    private String description; //overall description and comments if any

    /** DOCUMENT ME! */
    private float temperature; //celcius degrees

    /** DOCUMENT ME! */
    private float feelsLikeTemperature; //celcius degrees

    /** DOCUMENT ME! */
    private float pressure; //pascal

    /** DOCUMENT ME! */
    private float humidity; //percent

    /** DOCUMENT ME! */
    private float dewPoint; //celcius degrees

    /** DOCUMENT ME! */
    private float windSpeed; //meters/second

    /** DOCUMENT ME! */
    private float windGust; //meters/second

    /** DOCUMENT ME! */
    private float windDirection; //degrees counter clockwise, 0 towards north

    /** DOCUMENT ME! */
    private float visibility; //meters

    /** DOCUMENT ME! */
    private float energy; //joules per square meter

    /** DOCUMENT ME! */
    private int UVLevel; //index from 5 (none) to 0 (dangerous)

    /** DOCUMENT ME! */
    private int airQuality; //index from 5 (good) to 0 (dangerous)

    /** DOCUMENT ME! */
    private float snowHeight; //in meters

    /** DOCUMENT ME! */
    private float seaHeight; //between low and high position in meters

/**
     * Creates a new WeatherConditions object.
     *
     * @param date            DOCUMENT ME!
     * @param stationName     DOCUMENT ME!
     * @param stationLocation DOCUMENT ME!
     * @param description     DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public WeatherConditions(Date date, String stationName,
        Place stationLocation, String description) {
        if ((date != null) && (stationName != null) &&
                (stationLocation != null) && (description != null)) {
            this.date = date;
            this.stationName = stationName;
            this.stationLocation = stationLocation;
            this.description = description;
            this.temperature = 0;
            this.feelsLikeTemperature = 0;
            this.pressure = 0;
            this.humidity = 0;
            this.dewPoint = 0;
            this.windSpeed = 0;
            this.windGust = 0;
            this.windDirection = 0;
            this.visibility = Float.MAX_VALUE;
            this.energy = 0;
            this.UVLevel = 5;
            this.airQuality = 5;
            this.snowHeight = 0;
            this.seaHeight = 0;
        } else {
            throw new IllegalArgumentException(
                "The WeatherConditions constructor doesn't accept null arguments.");
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
    public String getStationName() {
        return stationName;
    }

    //you can fill this field with getPlace.getName()
    /**
     * DOCUMENT ME!
     *
     * @param description DOCUMENT ME!
     */
    public void setStationName(String description) {
        if (stationName != null) {
            this.stationName = stationName;
        } else {
            throw new IllegalArgumentException(
                "You can't set a null stationName.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Place getPosition() {
        return stationLocation;
    }

    /**
     * DOCUMENT ME!
     *
     * @param stationLocation DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setPosition(Place stationLocation) {
        if (stationLocation != null) {
            this.stationLocation = stationLocation;
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

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getTemperature() {
        return temperature;
    }

    /**
     * DOCUMENT ME!
     *
     * @param temperature DOCUMENT ME!
     */
    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getFeelsLikeTemperature() {
        return feelsLikeTemperature;
    }

    /**
     * DOCUMENT ME!
     *
     * @param feelsLikeTemperature DOCUMENT ME!
     */
    public void setFeelsLikeTemperature(float feelsLikeTemperature) {
        this.feelsLikeTemperature = feelsLikeTemperature;
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
    public float getHumidity() {
        return humidity;
    }

    /**
     * DOCUMENT ME!
     *
     * @param humidity DOCUMENT ME!
     */
    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getDewPoint() {
        return dewPoint;
    }

    /**
     * DOCUMENT ME!
     *
     * @param dewPoint DOCUMENT ME!
     */
    public void setDewPoint(float dewPoint) {
        this.dewPoint = dewPoint;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getWindSpeed() {
        return windSpeed;
    }

    /**
     * DOCUMENT ME!
     *
     * @param windSpeed DOCUMENT ME!
     */
    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getWindGust() {
        return windGust;
    }

    /**
     * DOCUMENT ME!
     *
     * @param windGust DOCUMENT ME!
     */
    public void setWindGust(float windGust) {
        this.windGust = windGust;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getWindDirection() {
        return windDirection;
    }

    /**
     * DOCUMENT ME!
     *
     * @param windDirection DOCUMENT ME!
     */
    public void setWindDirection(float windDirection) {
        this.windDirection = windDirection;
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
    public float getEnergy() {
        return energy;
    }

    /**
     * DOCUMENT ME!
     *
     * @param energy DOCUMENT ME!
     */
    public void setEnergy(float energy) {
        this.energy = energy;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getUVLevel() {
        return UVLevel;
    }

    /**
     * DOCUMENT ME!
     *
     * @param UVLevel DOCUMENT ME!
     */
    public void setUVLevel(int UVLevel) {
        this.UVLevel = UVLevel;
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
}
