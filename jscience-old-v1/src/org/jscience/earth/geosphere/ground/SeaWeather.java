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

package org.jscience.earth.geosphere.ground;

/**
 *
 * @version 1.0
 * @author Silvere Martin-Michiellot
 */

import javax.vecmath.Vector3d;

//Contains information about the current wave currents and forces, temperature, wind force and direction, humidity and maybe sunlight and rainfall above a large water area at sea level

public class SeaWeather extends Object {

    //we could also try to introduce the concept of pollution level

    //sea temperature and deep currents

    //ice Cover

    XXXX
    //tides (depending on moons)

    private long time;//the time at which the weather was reported, unix time
    private Vector3d position;//latitude, longitude, altitude
    private float temperature;//kelvin //negative values means unknown
    private float humidity;//percent //negative values means unknown
    private float pressure;//pascal //negative values means unknown
    private float windForce;//kilometers per hour //negative values means unknown
    private Vector3d windDirection;//east, north, sky
    private float sunLight;//joules per square meter //negative values means unknown
    private float precipitation;//millimeters per hour //negative values means unknown
    private float visibility;//meters //negative values means unknown

    private int description;//string description, optional parameter, basic user notification, not very accurate

    public final static int CLEAR_SKY = 0;
    public final static int FOG = 1;
    public final static int RAINFALL = 2;
    public final static int SNOWFALL = 4;
    public final static int ICE_FALL = 8;
    public final static int CLOUDY = 16;
    public final static int TORNADO = 32;
    public final static int LIGHTNINGS = 64;
    private final static int MAX_VALUE = 127;

    //automatically tries to set the description
    public SeaWeather(long time, Vector3d position, float temperature, float humidity, float pressure, float windForce, Vector3d windDirection, float sunLight, float precipitation, float visibility) {

        this.time = time;
        this.position = position;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windForce = windForce;
        this.windDirection = windDirection;
        this.sunLight = sunLight;
        this.precipitation = precipitation;
        this.visibility = visibility;

        if (this.precipitation > 0) {
            if (this.temperature < 270) {
                description += SNOWFALL;
            } else {
                description += RAINFALL;
            }
            description += CLOUDY;
        }
        if (this.visibility < 10000) {
            description += FOG;
        }

    }

    public SeaWeather(long time, Vector3d position, float temperature, float humidity, float pressure, float windForce, Vector3d windDirection, float sunLight, float precipitation, float visibility, int description) {

        this(time, position, temperature, humidity, pressure, windForce, windDirection, sunLight, precipitation, visibility);
        this.description = description;

    }

    public long getTime() {

        return time;

    }

    public Vector3d getPosition() {

        return position;

    }

    public float getTemperature() {

        return temperature;

    }

    public float getHumidity() {

        return humidity;

    }

    public float getPressure() {

        return pressure;

    }

    public float getWindForce() {

        return windForce;

    }

    public Vector3d getWindDirection() {

        return windDirection;

    }

    public float getSunLight() {

        return sunLight;

    }

    public float getPrecipitation() {

        return precipitation;

    }

    public float getVisibility() {

        return visibility;

    }

    //the best available description for the overall weather using a combination of values
    public int getDescription() {

        return description;

    }

    //retrieves a human readable value
    public static String getDescriptionName(int name) {

        String result;
        int value;

        result = new String();
        value = name;
        if (value == 0) {
            result = "Clear sky";
        } else {
            if (value > MAX_VALUE) {
                result = null;
            } else {
                if ((value - LIGHTNINGS) > 0) {
                    result.concat("lightnings, ");
                    value = value - LIGHTNINGS;
                }
                if ((value - TORNADO) > 0) {
                    result.concat("tornado, ");
                    value = value - TORNADO;
                }
                if ((value - CLOUDY) > 0) {
                    result.concat("cloudy, ");
                    value = value - CLOUDY;
                }
                if ((value - ICE_FALL) > 0) {
                    result.concat("ice fall, ");
                    value = value - ICE_FALL;
                }
                if ((value - SNOWFALL) > 0) {
                    result.concat("snowfall, ");
                    value = value - SNOWFALL;
                }
                if ((value - RAINFALL) > 0) {
                    result.concat("rainfall, ");
                    value = value - RAINFALL;
                }
                if ((value - FOG) > 0) {
                    result.concat("fog, ");
                    value = value - FOG;
                }
                result.substring(0, result.length() - 1);
                result.concat(".");
            }
        }

        return result;

    }

}
