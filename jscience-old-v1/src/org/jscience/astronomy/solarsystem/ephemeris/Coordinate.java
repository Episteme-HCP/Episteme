package org.jscience.astronomy.solarsystem.ephemeris;


//this code is rebundled after the code from
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class Coordinate {
/**
     * Creates a new Coordinate object.
     */
    public Coordinate() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param heliocentriccoordinate DOCUMENT ME!
     * @param heliocentriccoordinate1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static GeocentricCoordinate helioToGeo(
        HeliocentricCoordinate heliocentriccoordinate,
        HeliocentricCoordinate heliocentriccoordinate1) {
        if (heliocentriccoordinate1.getDay() != heliocentriccoordinate.getDay()) {
            System.out.println(
                "Converting heliocentric to geocentric with non-aligned dates!\n");
        }

        double d = Math.sin(heliocentriccoordinate.l -
                heliocentriccoordinate.Omega) * Math.cos(heliocentriccoordinate.i);
        double d1 = Math.cos(heliocentriccoordinate.l -
                heliocentriccoordinate.Omega);
        double d2 = Angle.reduce(Math.atan2(d, d1) +
                heliocentriccoordinate.Omega);
        double d3 = heliocentriccoordinate.r * Math.cos(heliocentriccoordinate.psi);
        double d4;

        if (heliocentriccoordinate.a > 1.0D) {
            d = heliocentriccoordinate1.r * Math.sin(d2 -
                    heliocentriccoordinate1.l);
            d1 = d3 -
                (heliocentriccoordinate1.r * Math.cos(d2 -
                    heliocentriccoordinate1.l));
            d4 = Math.atan2(d, d1) + d2;
        } else {
            d = d3 * Math.sin(heliocentriccoordinate1.l - d2);
            d1 = heliocentriccoordinate1.r -
                (d3 * Math.cos(heliocentriccoordinate1.l - d2));
            d4 = 3.1415926535897931D + heliocentriccoordinate1.l +
                Math.atan2(d, d1);
        }

        d4 = Angle.reduce(d4);
        d = d3 * Math.tan(heliocentriccoordinate.psi) * Math.sin(d4 - d2);
        d1 = heliocentriccoordinate1.r * Math.sin(d2 -
                heliocentriccoordinate1.l);

        double d5 = Math.atan(d / d1);

        return new GeocentricCoordinate(d5, d4,
            heliocentriccoordinate1.getDay(), GeocentricCoordinate.ecliptic);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     * @param vector3 DOCUMENT ME!
     */
    public static void sphereToXYZ(double d, double d1, Vector3 vector3) {
        double d4 = Math.sin(d1);
        double d2 = Math.cos(d1) * Math.cos(d);
        double d3 = Math.cos(d1) * Math.sin(d);
        vector3.x = d2;
        vector3.y = d3;
        vector3.z = d4;
    }
}
