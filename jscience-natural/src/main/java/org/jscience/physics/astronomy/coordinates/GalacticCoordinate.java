package org.jscience.physics.astronomy.coordinates;

/**
 * Galactic coordinate system.
 * 
 * l: Galactic longitude (0° toward Galactic center, increases counterclockwise)
 * b: Galactic latitude (0° = Galactic plane, +90° = North Galactic Pole)
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class GalacticCoordinate {

    // Galactic North Pole in J2000 equatorial coordinates
    private static final double NGP_RA = 192.85948; // degrees
    private static final double NGP_DEC = 27.12825; // degrees
    private static final double GALACTIC_LON_NCP = 122.93192; // l of NCP

    private final double l; // Galactic longitude [0, 360)
    private final double b; // Galactic latitude [-90, +90]

    public GalacticCoordinate(double l, double b) {
        this.l = normalizeAngle(l);
        this.b = Math.max(-90, Math.min(90, b));
    }

    /**
     * Converts equatorial coordinates to galactic.
     */
    public static GalacticCoordinate fromEquatorial(EquatorialCoordinate eq) {
        double ra = Math.toRadians(eq.getRaDegrees());
        double dec = Math.toRadians(eq.getDecDegrees());
        double raNGP = Math.toRadians(NGP_RA);
        double decNGP = Math.toRadians(NGP_DEC);
        double lNCP = Math.toRadians(GALACTIC_LON_NCP);

        // Galactic latitude
        double sinB = Math.sin(dec) * Math.sin(decNGP)
                + Math.cos(dec) * Math.cos(decNGP) * Math.cos(ra - raNGP);
        double b = Math.asin(sinB);

        // Galactic longitude
        double y = Math.cos(dec) * Math.sin(ra - raNGP);
        double x = Math.sin(dec) * Math.cos(decNGP)
                - Math.cos(dec) * Math.sin(decNGP) * Math.cos(ra - raNGP);
        double l = lNCP - Math.atan2(y, x);

        return new GalacticCoordinate(Math.toDegrees(l), Math.toDegrees(b));
    }

    /**
     * Converts galactic coordinates to equatorial.
     */
    public EquatorialCoordinate toEquatorial() {
        double lR = Math.toRadians(l);
        double bR = Math.toRadians(b);
        double raNGP = Math.toRadians(NGP_RA);
        double decNGP = Math.toRadians(NGP_DEC);
        double lNCP = Math.toRadians(GALACTIC_LON_NCP);

        // Declination
        double sinDec = Math.sin(bR) * Math.sin(decNGP)
                + Math.cos(bR) * Math.cos(decNGP) * Math.sin(lNCP - lR);
        double dec = Math.asin(sinDec);

        // Right Ascension
        double y = Math.cos(bR) * Math.cos(lNCP - lR);
        double x = Math.sin(bR) * Math.cos(decNGP)
                - Math.cos(bR) * Math.sin(decNGP) * Math.sin(lNCP - lR);
        double ra = raNGP + Math.atan2(y, x);

        return new EquatorialCoordinate(Math.toDegrees(ra), Math.toDegrees(dec));
    }

    public double getL() {
        return l;
    }

    public double getB() {
        return b;
    }

    /**
     * Checks if coordinate is in the Galactic plane (|b| < 10°).
     */
    public boolean isInGalacticPlane() {
        return Math.abs(b) < 10;
    }

    /**
     * Checks if coordinate is toward the Galactic center (|l| < 30°).
     */
    public boolean isTowardCenter() {
        return l < 30 || l > 330;
    }

    private static double normalizeAngle(double deg) {
        deg = deg % 360.0;
        return deg < 0 ? deg + 360.0 : deg;
    }

    @Override
    public String toString() {
        return String.format("l=%.2f°, b=%.2f°", l, b);
    }

    // --- Notable galactic coordinates ---

    /** Galactic center (Sgr A*) */
    public static final GalacticCoordinate GALACTIC_CENTER = new GalacticCoordinate(0.0, 0.0);

    /** Galactic anticenter */
    public static final GalacticCoordinate GALACTIC_ANTICENTER = new GalacticCoordinate(180.0, 0.0);

    /** North Galactic Pole */
    public static final GalacticCoordinate NORTH_GALACTIC_POLE = new GalacticCoordinate(0.0, 90.0);
}
