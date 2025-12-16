package org.jscience.physics.astronomy.time;

/**
 * Sidereal time calculations.
 * 
 * Greenwich Mean Sidereal Time (GMST): Sidereal time at Greenwich meridian.
 * Local Mean Sidereal Time (LMST): GMST adjusted for observer longitude.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class SiderealTime {

    private SiderealTime() {
    } // Utility class

    /**
     * Computes Greenwich Mean Sidereal Time (GMST) in degrees.
     * 
     * @param jd Julian Date
     * @return GMST in degrees [0, 360)
     */
    public static double gmstDegrees(JulianDate jd) {
        // Algorithm from the Astronomical Almanac
        double T = jd.getJulianCenturies();

        // GMST at 0h UT in seconds
        double gmst = 24110.54841
                + 8640184.812866 * T
                + 0.093104 * T * T
                - 6.2e-6 * T * T * T;

        // Convert to degrees
        gmst = (gmst / 240.0) % 360.0; // 240 = seconds per degree
        if (gmst < 0)
            gmst += 360.0;

        return gmst;
    }

    /**
     * Computes Greenwich Mean Sidereal Time in hours.
     * 
     * @param jd Julian Date
     * @return GMST in hours [0, 24)
     */
    public static double gmstHours(JulianDate jd) {
        return gmstDegrees(jd) / 15.0;
    }

    /**
     * Computes Local Mean Sidereal Time in degrees.
     * 
     * @param jd        Julian Date
     * @param longitude Observer longitude in degrees (East positive)
     * @return LMST in degrees [0, 360)
     */
    public static double lmstDegrees(JulianDate jd, double longitude) {
        double gmst = gmstDegrees(jd);
        double lmst = (gmst + longitude) % 360.0;
        if (lmst < 0)
            lmst += 360.0;
        return lmst;
    }

    /**
     * Computes Local Mean Sidereal Time in hours.
     * 
     * @param jd        Julian Date
     * @param longitude Observer longitude in degrees (East positive)
     * @return LMST in hours [0, 24)
     */
    public static double lmstHours(JulianDate jd, double longitude) {
        return lmstDegrees(jd, longitude) / 15.0;
    }

    /**
     * Computes Hour Angle for an object.
     * HA = LMST - RA
     * 
     * @param lmstHours Local sidereal time in hours
     * @param raHours   Right ascension in hours
     * @return Hour angle in hours (-12 to +12)
     */
    public static double hourAngle(double lmstHours, double raHours) {
        double ha = lmstHours - raHours;
        if (ha < -12)
            ha += 24;
        if (ha > 12)
            ha -= 24;
        return ha;
    }

    /**
     * Converts RA hours to degrees.
     */
    public static double raHoursToDegrees(double hours) {
        return hours * 15.0;
    }

    /**
     * Converts degrees to RA hours.
     */
    public static double degreesToRaHours(double degrees) {
        return degrees / 15.0;
    }
}
