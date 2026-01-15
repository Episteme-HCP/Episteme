package org.jscience.astronomy.solarsystem.ephemeris;


//this code is rebundled after the code from
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class Angle {
/**
     * Creates a new Angle object.
     */
    public Angle() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double hoursToRadians(double d) {
        return (d * 3.1415926535897931D) / 12D;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double reduce(double d) {
        for (; d < 0.0D; d += 6.2831853071795862D)
            ;

        for (; d > 6.2831853071795862D; d -= 6.2831853071795862D)
            ;

        return d;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toHMString(double d) {
        double d1 = Math.abs(toHours(d));
        double d2 = (d1 - Math.floor(d1)) * 60D;
        d1 = Math.floor(d1);

        if (d < 0.0D) {
            d1 = -d1;
        }

        return new String(Math.round(d1) + ":" + d2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double toDegrees(double d) {
        return (d * 180D) / 3.1415926535897931D;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toDMString(double d) {
        double d1 = Math.abs(toDegrees(d));
        double d2 = (d1 - Math.floor(d1)) * 60D;
        d1 = Math.floor(d1);

        if (d < 0.0D) {
            d1 = -d1;
        }

        return new String(Math.round(d1) + ":" + d2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double toRadians(double d) {
        return (d * 3.1415926535897931D) / 180D;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double toHours(double d) {
        return (d * 12D) / 3.1415926535897931D;
    }
}
