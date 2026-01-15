package org.jscience.earth.geosphere.sky;

/**
 */
import javax.vecmath.Vector3d;


//Contains information about the current temperature, wind force and direction, humidity and maybe sunlight and rainfall at given position and altitude
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class SkyWeather extends Object {
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
    private Vector3d position; //latitude, longitude, altitude

    /** DOCUMENT ME! */
    private float temperature; //kelvin //negative values means unknown //between -90 and +60 celicius degrees on Earth

    /** DOCUMENT ME! */
    private float humidity; //percent //negative values means unknown

    /** DOCUMENT ME! */
    private float pressure; //pascal //negative values means unknown

    /** DOCUMENT ME! */
    private float windForce; //kilometers per hour //negative values means unknown

    /** DOCUMENT ME! */
    private Vector3d windDirection; //east, north, sky

    /** DOCUMENT ME! */
    private float sunLight; //joules per square meter //negative values means unknown

    /** DOCUMENT ME! */
    private float precipitation; //millimeters per hour //negative values means unknown //between 0 and 30000 per year

    /** DOCUMENT ME! */
    private float visibility; //meters //negative values means unknown

    /** DOCUMENT ME! */
    private int description; //string description, optional parameter, basic user notification, not very accurate

    //automatically tries to set the description
    /**
     * Creates a new SkyWeather object.
     *
     * @param time DOCUMENT ME!
     * @param position DOCUMENT ME!
     * @param temperature DOCUMENT ME!
     * @param humidity DOCUMENT ME!
     * @param pressure DOCUMENT ME!
     * @param windForce DOCUMENT ME!
     * @param windDirection DOCUMENT ME!
     * @param sunLight DOCUMENT ME!
     * @param precipitation DOCUMENT ME!
     * @param visibility DOCUMENT ME!
     */
    public SkyWeather(long time, Vector3d position, float temperature,
        float humidity, float pressure, float windForce,
        Vector3d windDirection, float sunLight, float precipitation,
        float visibility) {
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

/**
     * Creates a new SkyWeather object.
     *
     * @param time          DOCUMENT ME!
     * @param position      DOCUMENT ME!
     * @param temperature   DOCUMENT ME!
     * @param humidity      DOCUMENT ME!
     * @param pressure      DOCUMENT ME!
     * @param windForce     DOCUMENT ME!
     * @param windDirection DOCUMENT ME!
     * @param sunLight      DOCUMENT ME!
     * @param precipitation DOCUMENT ME!
     * @param visibility    DOCUMENT ME!
     * @param description   DOCUMENT ME!
     */
    public SkyWeather(long time, Vector3d position, float temperature,
        float humidity, float pressure, float windForce,
        Vector3d windDirection, float sunLight, float precipitation,
        float visibility, int description) {
        this(time, position, temperature, humidity, pressure, windForce,
            windDirection, sunLight, precipitation, visibility);
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
    public float getTemperature() {
        return temperature;
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

    //feels like temperature
    //beaufort scale
    //UV index
}
