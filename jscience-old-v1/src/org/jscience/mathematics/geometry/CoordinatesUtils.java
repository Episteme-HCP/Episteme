package org.jscience.mathematics.geometry;

/**
 * The coordinate transformation math library. This class is duplicate with
 * the rest of this package. It is useful for a quick conversion without the
 * full burden from AbstractPoint allocation. Provides common coordinate
 * tranformations. This class cannot be subclassed or instantiated because all
 * methods are static.
 *
 * @author Mark Hale, Silvere Martin-Michiellot
 * @version 1.2
 */
public final class CoordinatesUtils extends Object {
/**
     * Creates a new CoordinatesUtils object.
     */
    private CoordinatesUtils() {
    }

    /**
     * Converts cartesian coordinates to polar coordinates.
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return an array with [0] containing the radius and [1] containing the
     *         angle.
     */
    public static double[] cartesianToPolar(double x, double y) {
        final double[] rtheta = new double[2];
        final double xAbs = Math.abs(x);
        final double yAbs = Math.abs(y);

        if ((xAbs == 0.0) && (yAbs == 0.0)) {
            rtheta[0] = 0.0;
        } else if (xAbs < yAbs) {
            rtheta[0] = yAbs * Math.sqrt(1.0 + ((x / y) * (x / y)));
        } else {
            rtheta[0] = xAbs * Math.sqrt(1.0 + ((y / x) * (y / x)));
        }

        rtheta[1] = Math.atan2(y, x);

        return rtheta;
    }

    /**
     * Converts polar coordinates to cartesian coordinates.
     *
     * @param r DOCUMENT ME!
     * @param theta DOCUMENT ME!
     *
     * @return an array with [0] containing the x-coordinate and [1] containing
     *         the y-coordinate.
     */
    public static double[] polarToCartesian(double r, double theta) {
        final double[] xy = new double[2];
        xy[0] = r * Math.cos(theta);
        xy[1] = r * Math.sin(theta);

        return xy;
    }

    /**
     * Converts 3D cartesian coordinates to spherical polar
     * coordinates.
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param z DOCUMENT ME!
     *
     * @return an array with [0] containing the radius, [1] containing the
     *         theta angle and [2] containing the phi angle.
     */
    public static double[] cartesianToSpherical(double x, double y, double z) {
        final double[] rthetaphi = new double[3];
        rthetaphi[0] = Math.sqrt((x * x) + (y * y) + (z * z));
        rthetaphi[1] = Math.acos(z / rthetaphi[0]);
        rthetaphi[2] = Math.atan2(y, x);

        return rthetaphi;
    }

    /**
     * Converts spherical polar coordinates to 3D cartesian
     * coordinates.
     *
     * @param r DOCUMENT ME!
     * @param theta DOCUMENT ME!
     * @param phi DOCUMENT ME!
     *
     * @return an array with [0] containing the x-coordinate, [1] containing
     *         the y-coordinate and [2] containing the z-coordinate.
     */
    public static double[] sphericalToCartesian(double r, double theta,
        double phi) {
        final double[] xyz = new double[3];
        xyz[0] = r * Math.sin(theta) * Math.cos(phi);
        xyz[1] = r * Math.sin(theta) * Math.sin(phi);
        xyz[2] = r * Math.cos(theta);

        return xyz;
    }

    /**
     * Converts 3D cartesian coordinates to cylindrical coordinates.
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param z DOCUMENT ME!
     *
     * @return an array with [0] containing the radius, [1] containing the
     *         angle and [2] containing the height.
     */
    public static double[] cartesianToCylindrical(double x, double y, double z) {
        final double[] rphih = new double[3];
        final double[] rphi = cartesianToPolar(x, y);
        rphih[0] = rphi[0];
        rphih[1] = rphi[1];
        rphih[2] = z;

        return rphih;
    }

    /**
     * Converts cylindrical coordinates to cartesian coordinates.
     *
     * @param r DOCUMENT ME!
     * @param phi DOCUMENT ME!
     * @param h DOCUMENT ME!
     *
     * @return an array with [0] containing the x-coordinate, [1] containing
     *         the y-coordinate and [2] containing the z-coordinate.
     */
    public static double[] cylindricalToCartesian(double r, double phi, double h) {
        final double[] xyz = new double[3];
        final double[] xy = polarToCartesian(r, phi);
        xyz[0] = xy[0];
        xyz[1] = xy[1];
        xyz[2] = h;

        return xyz;
    }

    /**
     * Converts cylindrical coordinates to spherical polar coordinates.
     *
     * @param r DOCUMENT ME!
     * @param phi DOCUMENT ME!
     * @param h DOCUMENT ME!
     *
     * @return an array with [0] containing the radius, [1] containing the
     *         theta angle and [2] containing the phi angle.
     */
    public static double[] cylindricalToSpherical(double r, double phi, double h) {
        final double[] rthetaphi = new double[3];
        final double[] rtheta = cartesianToPolar(h, r);
        rthetaphi[0] = rtheta[0];
        rthetaphi[1] = rtheta[1];
        rthetaphi[2] = phi;

        return rthetaphi;
    }

    /**
     * Converts spherical coordinates to cylindrical coordinates.
     *
     * @param r DOCUMENT ME!
     * @param theta DOCUMENT ME!
     * @param phi DOCUMENT ME!
     *
     * @return an array with [0] containing the radius, [1] containing the
     *         angle and [2] containing the height.
     */
    public static double[] sphericalToCylindrical(double r, double theta,
        double phi) {
        final double[] rphih = new double[3];
        final double[] hr = polarToCartesian(r, theta);
        rphih[0] = hr[1];
        rphih[1] = phi;
        rphih[2] = hr[0];

        return rphih;
    }
}
