package org.jscience.astronomy.solarsystem.ephemeris;


//this code is rebundled after the code from
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class HelioOrbit {
    /** DOCUMENT ME! */
    private double n;

    /** DOCUMENT ME! */
    private double M0;

    /** DOCUMENT ME! */
    private double omega;

    /** DOCUMENT ME! */
    private double e;

    /** DOCUMENT ME! */
    private double a;

    /** DOCUMENT ME! */
    private double i;

    /** DOCUMENT ME! */
    private double Omega;

    /** DOCUMENT ME! */
    protected double epoch;

    /** DOCUMENT ME! */
    protected double equinox;

/**
     * Creates a new HelioOrbit object.
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
    public HelioOrbit(double d, double d1, double d2, double d3, double d4,
        double d5, double d6, double d7, double d8) {
        i = (d * 3.1415926535897931D) / 180D;
        Omega = (d1 * 3.1415926535897931D) / 180D;
        omega = ((d2 - d1) * 3.1415926535897931D) / 180D;
        a = d3;
        n = (d4 * 3.1415926535897931D) / 180D;
        e = d5;
        M0 = ((d6 - d2) * 3.1415926535897931D) / 180D;
        epoch = d7;
        equinox = d8;
    }

/**
     * Creates a new HelioOrbit object.
     *
     * @param helioorbit DOCUMENT ME!
     */
    public HelioOrbit(HelioOrbit helioorbit) {
        copy(helioorbit);
    }

    /**
     * DOCUMENT ME!
     *
     * @param helioorbit DOCUMENT ME!
     */
    public void copy(HelioOrbit helioorbit) {
        n = helioorbit.n;
        M0 = helioorbit.M0;
        omega = helioorbit.omega;
        e = helioorbit.e;
        a = helioorbit.a;
        i = helioorbit.i;
        Omega = helioorbit.Omega;
        epoch = helioorbit.epoch;
        equinox = helioorbit.equinox;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector3 findHelioRectEquatCoords(double d) {
        double d1 = Ecliptic.obliquity(equinox);
        double d2 = Math.cos(Omega);
        double d3 = Math.sin(Omega) * Math.cos(d1);
        double d4 = Math.sin(Omega) * Math.sin(d1);
        double d5 = -Math.sin(Omega) * Math.cos(i);
        double d6 = (Math.cos(Omega) * Math.cos(i) * Math.cos(d1)) -
            (Math.sin(i) * Math.sin(d1));
        double d7 = (Math.cos(Omega) * Math.cos(i) * Math.sin(d1)) +
            (Math.sin(i) * Math.cos(d1));
        double d8 = Math.atan2(d2, d5);
        double d9 = Math.atan2(d3, d6);
        double d10 = Math.atan2(d4, d7);
        double d11 = Math.sqrt((d2 * d2) + (d5 * d5));
        double d12 = Math.sqrt((d3 * d3) + (d6 * d6));
        double d13 = Math.sqrt((d4 * d4) + (d7 * d7));
        double d14 = Angle.reduce(M0 + ((d - epoch) * n));
        double d15 = Orrery.root_Kepler(d14, e);
        double d16 = 2D * Math.atan(Math.sqrt((1.0D + e) / (1.0D - e)) * Math.tan(
                    d15 / 2D));
        double d17 = a * (1.0D - (e * Math.cos(d15)));

        return new Vector3(d17 * d11 * Math.sin(d8 + omega + d16),
            d17 * d12 * Math.sin(d9 + omega + d16),
            d17 * d13 * Math.sin(d10 + omega + d16));
    }
}
