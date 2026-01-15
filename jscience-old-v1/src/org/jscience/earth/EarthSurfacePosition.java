/* Copyright 2000 Charles G. Wright
 * This software may be distributed under the terms of the
 * GNU General Public License.
 *
 * $Id: EarthSurfacePosition.java,v 1.2 2007-10-21 17:44:20 virtualcall Exp $
 */
package org.jscience.earth;

/**
 * <p/>
 * This class represents a location on the surface of planet Earth. Its class
 * variables define a position on Earth and its time zone. Methods for this
 * class deal with solar time and the position of the sun in the sky at this
 * location.
 * </p>
 */

//you should plug in data from org.jscience.geography.coordinates.Coord3D subclass
public class EarthSurfacePosition {
    // Constant:

    /**
     * DOCUMENT ME!
     */
    private static final double FEET_PER_METER = 3.2808389850;

    // locale

    /**
     * Latitude of the surface, in degrees.
     */
    private double latitude;

    /**
     * Longitude of the surface, in degrees.
     */
    private double longitude;

    /**
     * Longitude of the standard time zone in degrees.
     */
    private int time_zone;

    /**
     * Elevation at this location, in meters.
     */
    private double elevation;

    /**
     * Name of this location.
     *
     * @param latitude DOCUMENT ME!
     * @param longitude DOCUMENT ME!
     * @param time_zone DOCUMENT ME!
     * @param elevation DOCUMENT ME!
     */

    //public String name;

    /**
     * List of orientations associated with this location.
     *
     * @param latitude DOCUMENT ME!
     * @param longitude DOCUMENT ME!
     * @param time_zone DOCUMENT ME!
     * @param elevation DOCUMENT ME!
     */

    //public Vector orientations;
    //--------------------  Constructor  -----------------------------

    /**
     * <p/>
     * Create an EarthSurfacePosition object, which represents a location on
     * the surface of planet Earth. The parameters defining the location are
     * its:
     * </p>
     * <p/>
     * <ul>
     * <li>
     * latitude, in degrees - North is positive
     * </li>
     * <li>
     * longitude, in degrees - West is positive
     * </li>
     * <li>
     * standard time zone, in degrees - West is positive
     * </li>
     * <li>
     * elevation, in meters
     * </li>
     * </ul>
     *
     * @param latitude  DOCUMENT ME!
     * @param longitude DOCUMENT ME!
     * @param time_zone DOCUMENT ME!
     * @param elevation DOCUMENT ME!
     */
    public EarthSurfacePosition(double latitude, double longitude,
                                int time_zone, double elevation) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.time_zone = time_zone;
        this.elevation = elevation;

        //orientations = new Vector();
    }

    //-------------------  Methods  ------------------------------------
    //---- utility methods ------
    public static double toRadians(double angle) {
        return (angle * 0.0174532925);
    }

    /**
     * DOCUMENT ME!
     *
     * @param angle DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static double toDegrees(double angle) {
        return (angle / 0.0174532925);
    }

    //-----------------------------

    /**
     * Returns the latitude of the location station.
     *
     * @return DOCUMENT ME!
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Returns the longitude of the location.
     *
     * @return DOCUMENT ME!
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Returns the time zone of the data location, in degrees (longitude of
     * standard meridian).
     *
     * @return DOCUMENT ME!
     */
    public int getTimeZone() {
        return time_zone;
    }

    /**
     * Returns the elevation of the location.
     *
     * @return DOCUMENT ME!
     */
    public double getElevation() {
        return (double) elevation;
    }

    //public String getName(){
    //   return(name);
    //}
    //---------------------- Solar Declination ------------------------

    /**
     * Returns solar declination, in degrees (angle between ecliptic and
     * equator), given a day number.
     *
     * @param daynum DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public double getDeclination(int daynum) {
        return 23.45 * Math.sin(toRadians((360 * (284 + daynum)) / 365));
    }

    //------------------ Equation of time -----------------------------

    /**
     * Returns local solar time, in seconds, given the day number (in the year)
     * and the local standard time (in seconds).
     *
     * @param daynum        DOCUMENT ME!
     * @param standard_time DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public int getSolarTime(int daynum, int standard_time) {
        // convert daynum to radians
        int solar_time;
        double D = (2 * Math.PI * daynum) / 365;
        double eot = -1 * ((445 * Math.sin(D)) - (15.4 * Math.cos(D)) +
                (554 * Math.sin(2 * D)) + (217.0 * Math.cos(2 * D)));
        solar_time = (int) (standard_time + (240.0 * (time_zone - longitude)) +
                eot);

        //System.out.println("[getSolarTime]: standard_time = " + standard_time + "solar_time = " + solar_time);
        return solar_time;
    }

    //------------------------- HMS to Sec ----------------------------

    /**
     * Returns time of day in seconds. Inputs are the hour of the day (0-23),
     * the minute of the hour (0-59), and second of the minute (0-59).
     *
     * @param hour   DOCUMENT ME!
     * @param minute DOCUMENT ME!
     * @param second DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public int timeInSeconds(int hour, int minute, int second) {
        return (((hour) * 3600) + ((minute) * 60) + (second));
    }

    //------------------------ Hour Angle -----------------------------

    /**
     * Returns Hour Angle, in degrees. Input is the local solar time, in
     * seconds.
     *
     * @param solar_time DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public double getHourAngle(int solar_time) {
        return (15.0 * ((solar_time / 3600.0) - 12.0));
    }

    //--------------------------   Solar Altitude ---------------------

    /**
     * Returns solar altitude, in degrees, given solar time and declination.
     *
     * @param decl       DOCUMENT ME!
     * @param solar_time DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public double getSolarAltitude(double decl, int solar_time) {
        double hourAngle = getHourAngle(solar_time);

        // Calculate sine of altitude
        //    System.out.println("[getSolarAltitude]: decl="+decl+" hourAngle="+hourAngle);
        double sinAlt = (Math.cos(toRadians(latitude)) * Math.cos(toRadians(
                decl)) * Math.cos(toRadians(hourAngle))) +
                (Math.sin(toRadians(latitude)) * Math.sin(toRadians(decl)));

        double alt = Math.max(0.0, toDegrees(Math.asin(sinAlt)));

        return alt;
    }

    /**
     * Returns solar altitude, in degrees, given solar time and day number.
     *
     * @param daynum     DOCUMENT ME!
     * @param solar_time DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public double getSolarAltitude(int daynum, int solar_time) {
        double decl = getDeclination(daynum);

        return (getSolarAltitude(decl, solar_time));
    }

    //---------------------------- Solar Azimuth ------------------------

    /**
     * Returns solar azimuth, in degrees, given solar time and declination.
     *
     * @param decl       DOCUMENT ME!
     * @param solar_time DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public double getSolarAzimuth(double decl, int solar_time) {
        double alt = getSolarAltitude(decl, solar_time);

        // Calculate cosine of solar azimuth
        double cosAzi = ((Math.sin(toRadians(alt)) * Math.sin(toRadians(
                latitude))) - Math.sin(toRadians(decl))) / (Math.cos(toRadians(
                alt)) * Math.cos(toRadians(latitude)));
        double result = toDegrees(Math.acos(cosAzi));

        if (solar_time < 43200) {
            result *= -1.0;
        }

        //  System.out.println("[getSolarAzimuth]: decl="+decl+" alt="+alt+" latitude="+latitude+" azi="+result+" solar_time="+solar_time);
        return (result);
    }

    /**
     * Returns solar azimuth, in degrees, given solar time and day number.
     *
     * @param daynum     DOCUMENT ME!
     * @param solar_time DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public double getSolarAzimuth(int daynum, int solar_time) {
        double decl = getDeclination(daynum);

        return (getSolarAzimuth(decl, solar_time));
    }
}
