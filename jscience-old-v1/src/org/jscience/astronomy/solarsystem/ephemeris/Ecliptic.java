package org.jscience.astronomy.solarsystem.ephemeris;


//this code is rebundled after the code from
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class Ecliptic {
/**
     * Creates a new Ecliptic object.
     */
    public Ecliptic() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double obliquity(double d) {
        double d1 = (d - 2451545D) / 36525D;

        return 0.40909280419999999D +
        (d1 * (-0.00022696552489999999D +
        (d1 * (-2.8604007189999999E-009D + (d1 * 8.7896720390000002E-009D)))));
    }
}
