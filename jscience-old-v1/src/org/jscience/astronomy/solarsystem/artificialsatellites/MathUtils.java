/*
 * Copyright (C) 2006 Matthew Funk
 * Licensed under the Academic Free License version 1.2
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Academic Free License version 1.2 for more details.
 */
package org.jscience.astronomy.solarsystem.artificialsatellites;

/**
 * 
 */
class MathUtils {
    /**
     * DOCUMENT ME!
     */
    private final static double TWOPI = 6.28318530717959;

/**
     * Deny the ability to construct an instance of this class.
     */
    private MathUtils() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param SINX DOCUMENT ME!
     * @param COSX DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double ACTAN(double SINX, double COSX) {
        double ACTAN = 0.0f;

        if (COSX != 0.) {
            if (COSX <= 0.) {
                ACTAN = C2.PI;
            } else {
                if (SINX == 0.) {
                    return ACTAN;
                }

                if (SINX <= 0.) {
                    ACTAN = C2.TWOPI;
                }
            }
        } else {
            if (SINX == 0.) {
                return ACTAN;
            }

            if (SINX <= 0.) {
                ACTAN = C2.X3PIO2;

                return ACTAN;
            } else {
                ACTAN = C2.PIO2;

                return ACTAN;
            }
        }

        double TEMP = SINX / COSX;
        ACTAN = ACTAN + Math.atan(TEMP);

        return ACTAN;
    }

    /**
     * DOCUMENT ME!
     *
     * @param X DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double FMOD2P(double X) {
        double FMOD2P = 0.0;
        FMOD2P = X;

        int I = (int) (FMOD2P / C2.TWOPI);
        FMOD2P = FMOD2P - (I * C2.TWOPI);

        if (FMOD2P < 0) {
            FMOD2P = FMOD2P + C2.TWOPI;
        }

        return FMOD2P;
    }

    /**
     * DOCUMENT ME!
     *
     * @param EPOCH DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double computeDS50(double EPOCH) {
        double YR = (EPOCH + 2.e-7) * 1.e-3;
        int JY = (int) YR;
        YR = JY;

        double D = EPOCH - (YR * 1.e3);

        if (JY < 10) {
            JY = JY + 80;
        }

        int N = (JY - 69) / 4;

        if (JY < 70) {
            N = (JY - 72) / 4;
        }

        return 7305. + (365. * (JY - 70)) + N + D;
    }

    /**
     * DOCUMENT ME!
     *
     * @param DS50 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double THETAG(double DS50) {
        double THETA = 1.72944494 + (6.3003880987 * DS50);
        double THETAG = THETA - (((int) (THETA / TWOPI)) * TWOPI);

        if (THETAG < 0.0) {
            THETAG = THETAG + TWOPI;
        }

        return THETAG;
    }
}
