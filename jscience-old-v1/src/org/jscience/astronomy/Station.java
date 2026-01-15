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
