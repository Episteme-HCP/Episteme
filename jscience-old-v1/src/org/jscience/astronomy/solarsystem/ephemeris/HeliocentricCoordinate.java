package org.jscience.astronomy.solarsystem.ephemeris;


//this code is rebundled after the code from
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class HeliocentricCoordinate {
    /** DOCUMENT ME! */
    protected double l;

    /** DOCUMENT ME! */
    protected double psi;

    /** DOCUMENT ME! */
    protected double r;

    /** DOCUMENT ME! */
    protected double a;

    /** DOCUMENT ME! */
    protected double i;

    /** DOCUMENT ME! */
    protected double Omega;

    /** DOCUMENT ME! */
    protected double JD;

/**
     * Creates a new HeliocentricCoordinate object.
     */
    public HeliocentricCoordinate() {
        r = 0.0D;
        a = 0.0D;
        i = 0.0D;
    }

/**
     * Creates a new HeliocentricCoordinate object.
     *
     * @param d  DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     * @param d2 DOCUMENT ME!
     * @param d3 DOCUMENT ME!
     * @param d4 DOCUMENT ME!
     * @param d5 DOCUMENT ME!
     * @param d6 DOCUMENT ME!
     */
    public HeliocentricCoordinate(double d, double d1, double d2, double d3,
        double d4, double d5, double d6) {
        l = d;
        psi = d1;
        r = d2;
        a = d3;
        i = d4;
        Omega = d5;
        JD = d6;
    }

/**
     * Creates a new HeliocentricCoordinate object.
     *
     * @param heliocentriccoordinate DOCUMENT ME!
     */
    public HeliocentricCoordinate(HeliocentricCoordinate heliocentriccoordinate) {
        copy(heliocentriccoordinate);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getDay() {
        return JD;
    }

    /**
     * DOCUMENT ME!
     *
     * @param heliocentriccoordinate DOCUMENT ME!
     */
    public void copy(HeliocentricCoordinate heliocentriccoordinate) {
        l = heliocentriccoordinate.l;
        psi = heliocentriccoordinate.psi;
        r = heliocentriccoordinate.r;
        a = heliocentriccoordinate.a;
        i = heliocentriccoordinate.i;
        Omega = heliocentriccoordinate.Omega;
        JD = heliocentriccoordinate.JD;
    }
}
