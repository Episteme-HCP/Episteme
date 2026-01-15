package org.jscience.astronomy.solarsystem.ephemeris;


//this code is rebundled after the code from
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class Observer {
/**
     * Creates a new Observer object.
     */
    public Observer() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Vector3 geocentricEquatorialXYZ(double d, double d1) {
        double d2 = 6378.1400000000003D;
        double d3 = 6356.7550000000001D;
        double d4 = Math.atan((d3 / d2) * Math.tan(d1));
        double d5 = d3 * Math.sin(d4);
        double d6 = d2 * Math.cos(d4) * Math.cos(d);
        double d7 = d2 * Math.cos(d4) * Math.sin(d);
        Vector3 vector3 = new Vector3(d6, d7, d5);
        vector3.scale(6.6845871535470389E-009D);

        return vector3;
    }
}
