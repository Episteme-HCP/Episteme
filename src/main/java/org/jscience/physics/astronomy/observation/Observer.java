package org.jscience.physics.astronomy.observation;

import org.jscience.physics.astronomy.coordinates.*;
import org.jscience.physics.astronomy.time.*;

/**
 * Observational astronomy calculations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class Observer {

    private final double latitude; // degrees, North positive
    private final double longitude; // degrees, East positive
    private final double altitude; // meters above sea level
    private final String name;

    public Observer(String name, double latitude, double longitude, double altitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    /**
     * Calculates local sidereal time for this observer.
     */
    public double getLocalSiderealTime(JulianDate jd) {
        return SiderealTime.lmstDegrees(jd, longitude);
    }

    /**
     * Converts equatorial coordinates to horizontal for this observer.
     */
    public HorizontalCoordinate toHorizontal(EquatorialCoordinate eq, JulianDate jd) {
        double lmst = getLocalSiderealTime(jd);
        return HorizontalCoordinate.fromEquatorial(eq, latitude, lmst);
    }

    /**
     * Calculates rise time for an object.
     * Returns null if object is circumpolar or never rises.
     * 
     * @param eq         Equatorial coordinates
     * @param jdMidnight Julian Date at local midnight
     * @return Julian Date of rise, or null
     */
    public JulianDate riseTime(EquatorialCoordinate eq, JulianDate jdMidnight) {
        double dec = Math.toRadians(eq.getDecDegrees());
        double lat = Math.toRadians(latitude);

        // Hour angle at rise: cos(H) = -tan(lat) * tan(dec)
        double cosH = -Math.tan(lat) * Math.tan(dec);

        if (cosH > 1)
            return null; // Never rises
        if (cosH < -1)
            return null; // Circumpolar

        double H = Math.toDegrees(Math.acos(cosH)); // Hour angle at rise

        // LST at rise = RA - H
        double lstRise = eq.getRaDegrees() - H;
        lstRise = (lstRise + 360) % 360;

        // Find time when LST = lstRise
        double gmst = SiderealTime.gmstDegrees(jdMidnight);
        double deltaLst = lstRise - gmst - longitude;
        if (deltaLst < 0)
            deltaLst += 360;

        // Convert sidereal time difference to solar time
        double solarHours = deltaLst / 15.0 * (24.0 / 24.06571);

        return jdMidnight.addDays(solarHours / 24.0);
    }

    /**
     * Calculates transit (meridian crossing) time.
     */
    public JulianDate transitTime(EquatorialCoordinate eq, JulianDate jdMidnight) {
        double lstTransit = eq.getRaDegrees();
        double gmst = SiderealTime.gmstDegrees(jdMidnight);

        double deltaLst = lstTransit - gmst - longitude;
        if (deltaLst < 0)
            deltaLst += 360;
        if (deltaLst > 360)
            deltaLst -= 360;

        double solarHours = deltaLst / 15.0 * (24.0 / 24.06571);

        return jdMidnight.addDays(solarHours / 24.0);
    }

    /**
     * Checks if an object is currently visible.
     */
    public boolean isVisible(EquatorialCoordinate eq, JulianDate jd) {
        HorizontalCoordinate hor = toHorizontal(eq, jd);
        return hor.getAltitude() > 0;
    }

    /**
     * Checks if object is circumpolar (never sets).
     */
    public boolean isCircumpolar(EquatorialCoordinate eq) {
        double dec = eq.getDecDegrees();
        return Math.abs(dec) > (90 - Math.abs(latitude));
    }

    /**
     * Returns maximum altitude an object can reach.
     * alt_max = 90 - |lat - dec|
     */
    public double maximumAltitude(EquatorialCoordinate eq) {
        return 90 - Math.abs(latitude - eq.getDecDegrees());
    }

    // Getters
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public String getName() {
        return name;
    }

    // --- Well-known observatories ---

    /** Mauna Kea Observatory, Hawaii */
    public static final Observer MAUNA_KEA = new Observer("Mauna Kea", 19.8207, -155.4681, 4205);

    /** Paranal Observatory (VLT), Chile */
    public static final Observer PARANAL = new Observer("Paranal", -24.6253, -70.4042, 2635);

    /** La Palma (Roque de los Muchachos), Spain */
    public static final Observer LA_PALMA = new Observer("La Palma", 28.7606, -17.8816, 2396);

    /** Greenwich Observatory, UK */
    public static final Observer GREENWICH = new Observer("Greenwich", 51.4769, -0.0005, 48);
}
