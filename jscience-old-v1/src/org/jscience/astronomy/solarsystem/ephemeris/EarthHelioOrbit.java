package org.jscience.astronomy.solarsystem.ephemeris;


//this code is rebundled after the code from
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class EarthHelioOrbit extends HelioOrbit {
/**
     * Creates a new EarthHelioOrbit object.
     *
     * @param d  DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     * @param d2 DOCUMENT ME!
     * @param d3 DOCUMENT ME!
     * @param d4 DOCUMENT ME!
     * @param d5 DOCUMENT ME!
     * @param d6 DOCUMENT ME!
     * @param d7 DOCUMENT ME!
     * @param d8 DOCUMENT ME!
     */
    public EarthHelioOrbit(double d, double d1, double d2, double d3,
        double d4, double d5, double d6, double d7, double d8) {
        super(d, d1, d2, d3, d4, d5, d6, d7, d8);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector3 findHelioRectEquatCoords(double d) {
        double d1 = (d - 2451545D) / 365.25D;
        double d2 = Angle.reduce(3.804817769D + (8399.7111839999998D * d1));
        Vector3 vector3 = super.findHelioRectEquatCoords(d);
        vector3.set(vector3.x - (3.1199999999999999E-005D * Math.cos(d2)),
            vector3.y - (3.1199999999999999E-005D * Math.sin(d2)), vector3.z);

        return vector3;
    }
}
