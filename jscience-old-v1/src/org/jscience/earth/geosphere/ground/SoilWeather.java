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
 */
import javax.vecmath.Vector3d;


//the basic class for the "weather of earth itself"
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class SoilWeather extends Object {
    /** DOCUMENT ME! */
    public final static int CLEAR_SKY = 0;

    /** DOCUMENT ME! */
    public final static int FOG = 1;

    /** DOCUMENT ME! */
    public final static int RAINFALL = 2;

    /** DOCUMENT ME! */
    public final static int SNOWFALL = 4;

    /** DOCUMENT ME! */
    public final static int ICE_FALL = 8;

    /** DOCUMENT ME! */
    public final static int CLOUDY = 16;

    /** DOCUMENT ME! */
    public final static int TORNADO = 32;

    /** DOCUMENT ME! */
    public final static int LIGHTNINGS = 64;

    /** DOCUMENT ME! */
    private final static int MAX_VALUE = 127;

    //we could also try to introduce the concept of polution level
    /** DOCUMENT ME! */
    private long time; //the time at which the weather was reported, unix time

    /** DOCUMENT ME! */
    private Vector3d position; //latitude, longitude, altitude at ground level

    /** DOCUMENT ME! */
    private float snowHeight; //in millimeters //negative values means unknown //between 0 and 30000 per year

    /** DOCUMENT ME! */
    private float temperature; //kelvin //negative values means unknown

    /** DOCUMENT ME! */
    private float groundTemperature; //kelvin //negative values means unknown

    /** DOCUMENT ME! */
    private float humidity; //percent //negative values means unknown

    /** DOCUMENT ME! */
    private float groundHumidity; //percent //negative values means unknown

    /** DOCUMENT ME! */
    private float pressure; //pascal //negative values means unknown

    /** DOCUMENT ME! */
    private float windForce; //kilometers per hour //negative values means unknown

    /** DOCUMENT ME! */
    private Vector3d windDirection; //east, north, sky

    /** DOCUMENT ME! */
    private float sunLight; //joules per square meter //negative values means unknown

    /** DOCUMENT ME! */
    private float precipitation; //millimeters per hour //negative values means unknown

    /** DOCUMENT ME! */
    private float visibility; //meters //negative values means unknown

    /** DOCUMENT ME! */
    private int description; //string description, optional parameter, basic user notification, not very accurate

    //automatically tries to set the description
    /**
     * Creates a new SoilWeather object.
     *
     * @param time DOCUMENT ME!
     * @param position DOCUMENT ME!
     * @param snowHeight DOCUMENT ME!
     * @param temperature DOCUMENT ME!
     * @param groundTemperature DOCUMENT ME!
     * @param humidity DOCUMENT ME!
     * @param groundHumidity DOCUMENT ME!
     * @param pressure DOCUMENT ME!
     * @param windForce DOCUMENT ME!
     * @param windDirection DOCUMENT ME!
     * @param sunLight DOCUMENT ME!
     * @param precipitation DOCUMENT ME!
     * @param visibility DOCUMENT ME!
     */
    public SoilWeather(long time, Vector3d position, float snowHeight,
        float temperature, float groundTemperature, float humidity,
        float groundHumidity, float pressure, float windForce,
        Vector3d windDirection, float sunLight, float precipitation,
        float visibility) {
        this.time = time;
        this.position = position;
        this.snowHeight = snowHeight;
        this.temperature = temperature;
        this.groundTemperature = groundTemperature;
        this.humidity = humidity;
        this.groundHumidity = groundHumidity;
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

/**
     * Creates a new SoilWeather object.
     *
     * @param time              DOCUMENT ME!
     * @param position          DOCUMENT ME!
     * @param snowHeight        DOCUMENT ME!
     * @param temperature       DOCUMENT ME!
     * @param groundTemperature DOCUMENT ME!
     * @param humidity          DOCUMENT ME!
     * @param groundHumidity    DOCUMENT ME!
     * @param pressure          DOCUMENT ME!
     * @param windForce         DOCUMENT ME!
     * @param windDirection     DOCUMENT ME!
     * @param sunLight          DOCUMENT ME!
     * @param precipitation     DOCUMENT ME!
     * @param visibility        DOCUMENT ME!
     * @param description       DOCUMENT ME!
     */
    public SoilWeather(long time, Vector3d position, float snowHeight,
        float temperature, float groundTemperature, float humidity,
        float groundHumidity, float pressure, float windForce,
        Vector3d windDirection, float sunLight, float precipitation,
        float visibility, int description) {
        this(time, position, snowHeight, temperature, groundTemperature,
            humidity, groundHumidity, pressure, windForce, windDirection,
            sunLight, precipitation, visibility);
        this.description = description;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public long getTime() {
        return time;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector3d getPosition() {
        return position;
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
     * @return DOCUMENT ME!
     */
    public float getTemperature() {
        return temperature;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getGroundTemperature() {
        return groundTemperature;
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
     * @return DOCUMENT ME!
     */
    public float getGroundHumidity() {
        return groundHumidity;
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
     * @return DOCUMENT ME!
     */
    public float getWindForce() {
        return windForce;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector3d getWindDirection() {
        return windDirection;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getSunLight() {
        return sunLight;
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
     * @return DOCUMENT ME!
     */
    public float getVisibility() {
        return visibility;
    }

    //the best available description for the overall weather using a combination of values
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDescription() {
        return description;
    }

    //retrieves a human readable value
    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
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
