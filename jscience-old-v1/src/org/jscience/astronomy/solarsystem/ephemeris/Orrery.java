package org.jscience.astronomy.solarsystem.ephemeris;


//this code is rebundled after the code from
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class Orrery {
    /** DOCUMENT ME! */
    public static final int Mercury = 0;

    /** DOCUMENT ME! */
    public static final int Venus = 1;

    /** DOCUMENT ME! */
    public static final int Earth = 2;

    /** DOCUMENT ME! */
    public static final int Mars = 3;

    /** DOCUMENT ME! */
    public static final int Jupiter = 4;

    /** DOCUMENT ME! */
    public static final int Saturn = 5;

    /** DOCUMENT ME! */
    public static final int Uranus = 6;

    /** DOCUMENT ME! */
    public static final int Neptune = 7;

    /** DOCUMENT ME! */
    public static final int Pluto = 8;

    /** DOCUMENT ME! */
    private HelioOrbit[] planet;

/**
     * Creates a new Orrery object.
     */
    public Orrery() {
        planet = new HelioOrbit[9];

        double d = 2451040.5D;
        double d1 = 2451545D;
        planet[0] = new HelioOrbit(7.0050299999999996D, 48.332099999999997D,
                77.453900000000004D, 0.38709919999999998D, 4.0923309999999997D,
                0.20561889999999999D, 347.66730999999999D, d, d1);
        planet[1] = new HelioOrbit(3.3946000000000001D, 76.684299999999993D,
                131.392D, 0.72332669999999999D, 1.602149D,
                0.0067692999999999998D, 93.705179999999999D, d, d1);
        planet[2] = new EarthHelioOrbit(0.00025000000000000001D,
                6.7000000000000002D, 102.9757D, 1.0000036999999999D,
                0.98560369999999997D, 0.0167092D, 323.22633000000002D, d, d1);
        planet[3] = new HelioOrbit(1.84992D, 49.562100000000001D,
                336.00020000000001D, 1.5236223D, 0.5240686D,
                0.093368199999999998D, 91.081469999999996D, d, d1);
        planet[4] = new HelioOrbit(1.3046599999999999D, 100.473D,
                15.664300000000001D, 5.2030620000000001D,
                0.083085030000000004D, 0.048553300000000001D, 352.47627D, d, d1);
        planet[5] = new HelioOrbit(2.4852500000000002D, 113.6407D,
                88.533299999999997D, 9.5794599999999992D,
                0.033247159999999998D, 0.054200600000000002D,
                33.025419999999997D, d, d1);
        planet[6] = new HelioOrbit(0.77312000000000003D, 74.062399999999997D,
                173.6687D, 19.281220000000001D, 0.01164157D,
                0.042824599999999997D, 307.50367999999997D, d, d1);
        planet[7] = new HelioOrbit(1.7674799999999999D, 131.79679999999999D,
                17.765999999999998D, 30.216270000000002D, 0.005934089D,
                0.010903599999999999D, 302.13157999999999D, d, d1);
        planet[8] = new HelioOrbit(17.1311D, 110.3496D, 224.58690000000001D,
                39.424460000000003D, 0.0039815800000000002D, 0.2472724D,
                237.1867D, d, d1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static double root_Kepler(double d, double d1) {
        double d2 = 0.0D;
        double d4 = d;
        int i = 0;

        do {
            if (++i > 100) {
                System.out.println(
                    "Solution to Kepler's equation not converging.\n");
            }

            double d3 = d4 -
                ((d4 - (d1 * Math.sin(d4)) - d) / (1.0D - (d1 * Math.cos(d4))));

            if (Math.abs(d3 - d4) > 1E-013D) {
                d4 = d3;
            } else {
                return d3;
            }
        } while (true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public GeocentricCoordinate geocentricCoord(int i, double d) {
        Vector3 vector3 = planet[i].findHelioRectEquatCoords(d);
        Vector3 vector3_1 = planet[2].findHelioRectEquatCoords(d);
        double d1 = vector3.x - vector3_1.x;
        double d2 = vector3.y - vector3_1.y;
        double d3 = vector3.z - vector3_1.z;
        double d4 = Angle.reduce(Math.atan2(d2, d1));
        double d5 = Math.sqrt((d1 * d1) + (d2 * d2) + (d3 * d3));
        double d6 = Math.asin(d3 / d5);

        return new GeocentricCoordinate(d6, d4, d);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public GeocentricCoordinate helos(double d) {
        Vector3 vector3 = planet[2].findHelioRectEquatCoords(d);
        double d1 = -vector3.x;
        double d2 = -vector3.y;
        double d3 = -vector3.z;
        double d4 = Angle.reduce(Math.atan2(d2, d1));
        double d5 = Math.sqrt((d1 * d1) + (d2 * d2) + (d3 * d3));
        double d6 = Math.asin(d3 / d5);

        return new GeocentricCoordinate(d6, d4, d);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector3 findHREq(int i, double d) {
        return planet[i].findHelioRectEquatCoords(d);
    }
}
