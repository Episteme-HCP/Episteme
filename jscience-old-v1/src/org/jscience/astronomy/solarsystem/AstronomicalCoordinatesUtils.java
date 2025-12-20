package org.jscience.astronomy.solarsystem;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.*;


/**
 * The AstronomicalCoordinatesUtils class provides useful astronomical coordinates computation related methods.
 *
 * @author Jim Baer, Silvere Martin-Michiellot
 * @version 1.0
 */

//this code is a copy from CODES, CelestialBody.java and SolarSystemBody.java, with very minor editing:
//http://home.earthlink.net/~jimbaer1/
//by Jim Baert
//jimbaer1@earthlink.net
public final class AstronomicalCoordinatesUtils extends Object {

    /*  DECLARE CONSTANTS  */

    /*
         Value of epsilon (Obliquity of the Ecliptic at J2000.0) is that corresponding to the DE405 ephemeris, since that is the source of planetary positions used to calculate perturbations.  In radians.
     */
    private static final double epsilon = 0.4090926296894037;

    /*
         The value of mu depends on the equation and reference frame being used.  For two-body calculations performed in a reference frame relative to the Sun, a value of one plus the fractional body mass could be used.  However, for n-body calculations performed relative to the non-rotating barycenter of the solar system, individual values of mu for each planet are used; the mass of the body does not appear.  Therefore, for simplicity's sake, we will use mu = 1.
     */
    private static final double mu = 1;

    /*
       Value of Gaussian Gravitational Constant kappa (= GM) is that from the IAU (1976) System of Astronomical Constants
     */
    private static final double kappa = 0.01720209895;

    /*
       Speed of light, in A.U.s per day
     */
    private static final double speed_of_light = 173.144632685;

    /*
      Length of an A.U., in km
    */
    private static final double au = 149597870.691;

    /*
      Radius of the Earth, in meters
    */
    private static final double earth_radius = 6378140;

    /*
      Flattening factor for the Earth
    */
    private static final double flattening = 0.00335281;

    /*
   This utility routine is intended to apply Bessel's Interpolation to EOP-type data, in support of high-precision requirements for radar observations.
   We assume that time is a UTC Julian date.  The output will be the interpolated data corresponding to time.
   Algorithm adapted from P. Kenneth Seidelmann, "Explanatory Supplement to the Astronomical Almanac" (1992), pp. 546-547.
   */
    public static double getBesselInterpolation(double f_1, double f0, double fp1, double fp2, double time) {

         double delta_minus12 = 0, delta_12 = 0, delta_32 = 0, delta_20 = 0, delta_21 = 0, delta_312 = 0, p = 0, b2 = 0, b3 = 0, fp = 0;

        p = time - (Math.floor(time) + 0.5);

        delta_minus12 = f0 - f_1;
        delta_12 = fp1 - f0;
        delta_32 = fp2 - fp1;

        delta_20 = delta_12 - delta_minus12;
        delta_21 = delta_32 - delta_12;

        delta_312 = delta_21 - delta_20;

        b2 = p * (p - 1) / 4.0;
        b3 = p * (p - 1) * (p - 0.5) / 6.0;

        fp = f0 + p * delta_12 + b2 * (delta_20 + delta_21) + b3 * delta_312;

        return fp;

    }

    /*
     Procedure to calculate the matrix which will precess an equatorial rectangular coordinate vector from the equinox and equator of epoch J2000.0 to the equinox and equator of epoch jultime.
     Algorithm taken from P. Kenneth Seidelmann, "Explanatory Supplement to the Astronomical Almanac", 1992, pg. 103-104.

     Tested and verified 19 October 1999.
   */
   public static void precessionMatrix(double jultime, double matrix_P[][]) {

         double t = 0, xi = 0, z = 0, theta = 0;

        /* Calculate the accumulated precession angles at jultime */
        t = (jultime - 2451545.0) / 36525.0;
        xi = (2306.2181) * t + (.30188) * Math.pow(t, 2) + .017998 * Math.pow(t, 3);
        xi = ((xi / 3600.0) * Math.PI / 180.0);
        z = (2306.2181) * t + (1.09468) * Math.pow(t, 2) + .018203 * Math.pow(t, 3);
        z = ((z / 3600.0) * Math.PI / 180.0);
        theta = (2004.3109) * t - (.42665) * Math.pow(t, 2) - .041833 * Math.pow(t, 3);
        theta = ((theta / 3600.0) * Math.PI / 180.0);

        /* Calculate the entries of the precession matrix */
        matrix_P[1][1] = Math.cos(z) * Math.cos(theta) * Math.cos(xi) - Math.sin(z) * Math.sin(xi);
        matrix_P[1][2] = -Math.cos(z) * Math.cos(theta) * Math.sin(xi) - Math.sin(z) * Math.cos(xi);
        matrix_P[1][3] = -Math.cos(z) * Math.sin(theta);
        matrix_P[2][1] = Math.sin(z) * Math.cos(theta) * Math.cos(xi) + Math.cos(z) * Math.sin(xi);
        matrix_P[2][2] = -Math.sin(z) * Math.cos(theta) * Math.sin(xi) + Math.cos(z) * Math.cos(xi);
        matrix_P[2][3] = -Math.sin(z) * Math.sin(theta);
        matrix_P[3][1] = Math.sin(theta) * Math.cos(xi);
        matrix_P[3][2] = -Math.sin(theta) * Math.sin(xi);
        matrix_P[3][3] = Math.cos(theta);


    }

    /*
   Algorithm to calculate the IAU 1980 expressions for nutation in longitude and obliquity, as well as the mean obliquity, at jultime.  Precise values can be calculated by adding the differentials compiled by the IERS.
   The format of the nutation_vector is as follows:
       nutation_vector[1] = delta_psi (in radians);
       nutation_vector[2] = delta_epsilon (in radians);
       nutation_vector[3] = mean_obliquity (in radians).
   Algorithm taken from "Explanatory Supplement to the Astronomical Almanac", P. Kenneth Seidelmann, 1992, pp. 111-119.
   Tested and verified 10-18-99.
   */
    public static void getNutationIAU1980(double jultime, double nutation_vector[]) {

        double[] a = new double[107];
        double[] b = new double[107];
        double[] c = new double[107];
        double[] d = new double[107];
        double[] e = new double[107];
        double[] little_a = new double[86];
        double[] little_b = new double[86];
        double[] little_c = new double[86];
        double[] little_d = new double[86];
        double[] little_e = new double[86];
        double[] little_f = new double[86];
        double[] little_g = new double[86];
        double[] little_h = new double[86];
        double[] little_i = new double[86];
        double[] little_j = new double[86];
        double[] bigS = new double[107];
        double[] bigC = new double[107];
        double[] bigLS = new double[86];
        double[] bigLC = new double[86];
        double[] bigOC = new double[86];
        double[] bigOS = new double[86];

        double T = 0, mean_obliquity = 0, delta_psi = 0, delta_epsilon = 0, l = 0, lprime = 0, F = 0, bigD = 0, omega = 0, bigA = 0, bigQ = 0, bigM = 0, bigV = 0, bigJ = 0, bigE = 0, S = 0, delta_psi_c = 0, delta_epsilon_c = 0, delta_psi_p = 0, delta_epsilon_p = 0;

        int j = 0;

        /* Calculate the time variable, and the fundamental arguments */
        T = (jultime - 2451545.0) / 36525;
        l = (134.0 + 57.0 / 60.0 + 46.733 / 3600) + (1325 * 360 + (198 + 52.0 / 60.0 + 2.633 / 3600)) * T + (31.310 / 3600) * Math.pow(T, 2) + (0.064 / 3600) * Math.pow(T, 3);
        l = l * Math.PI / 180;
        lprime = (357.0 + 31.0 / 60.0 + 39.804 / 3600) + (99 * 360 + (359 + 3.0 / 60.0 + 1.224 / 3600)) * T - (0.577 / 3600) * Math.pow(T, 2) - (0.012 / 3600) * Math.pow(T, 3);
        lprime = lprime * Math.PI / 180;
        F = (93.0 + 16.0 / 60.0 + 18.877 / 3600) + (1342 * 360 + (82 + 1.0 / 60.0 + 3.137 / 3600)) * T - (13.257 / 3600) * Math.pow(T, 2) + (0.011 / 3600) * Math.pow(T, 3);
        F = F * Math.PI / 180;
        bigD = (297.0 + 51.0 / 60.0 + 1.307 / 3600) + (1236 * 360 + (307 + 6.0 / 60.0 + 41.328 / 3600)) * T - (6.891 / 3600) * Math.pow(T, 2) + (0.019 / 3600) * Math.pow(T, 3);
        bigD = bigD * Math.PI / 180;
        omega = (125.0 + 2.0 / 60.0 + 40.280 / 3600) - (5 * 360 + (134 + 8.0 / 60.0 + 10.539 / 3600)) * T + (7.455 / 3600) * Math.pow(T, 2) + (0.008 / 3600) * Math.pow(T, 3);
        omega = omega * Math.PI / 180;
        /* Note: NOT using the JPL DE405 value of mean obliquity at J2000 */
        mean_obliquity = (23.0 + 26.0 / 60.0 + 21.412 / 3600.0) - (46.8150 / 3600.0) * T - (0.00059 / 3600.0) * Math.pow(T, 2) + (0.001813 / 3600.0) * Math.pow(T, 3);
        mean_obliquity = mean_obliquity * Math.PI / 180;
        bigQ = 252.3 + 149472.7 * T;
        bigQ = bigQ * Math.PI / 180;
        bigM = 353.3 + 19140.3 * T;
        bigM = bigM * Math.PI / 180;
        bigV = 179.9 + 58517.8 * T;
        bigV = bigV * Math.PI / 180;
        bigJ = 32.3 + 3034.9 * T;
        bigJ = bigJ * Math.PI / 180;
        bigE = 98.4 + 35999.4 * T;
        bigE = bigE * Math.PI / 180;
        S = 48.0 + 1222.1 * T;
        S = S * Math.PI / 180;

        /* Initialize the IAU 1980 coefficients and argument multiples */
        /* Set 1 */
        a[1] = 0;
        b[1] = 0;
        c[1] = 0;
        d[1] = 0;
        e[1] = 1;
        bigS[1] = (-171996 - 174.2 * T) * 1E-04;
        bigC[1] = (92025 + 8.9 * T) * 1E-04;
        /* Set 2 */
        a[2] = 0;
        b[2] = 0;
        c[2] = 0;
        d[2] = 0;
        e[2] = 2;
        bigS[2] = (2062 + 0.2 * T) * 1E-04;
        bigC[2] = (-895 + 0.5 * T) * 1E-04;
        /* Set 3 */
        a[3] = -2;
        b[3] = 0;
        c[3] = 2;
        d[3] = 0;
        e[3] = 1;
        bigS[3] = (46) * 1E-04;
        bigC[3] = (-24) * 1E-04;
        /* Set 4 */
        a[4] = 2;
        b[4] = 0;
        c[4] = -2;
        d[4] = 0;
        e[4] = 0;
        bigS[4] = (11) * 1E-04;
        bigC[4] = (0) * 1E-04;
        /* Set 5 */
        a[5] = -2;
        b[5] = 0;
        c[5] = 2;
        d[5] = 0;
        e[5] = 2;
        bigS[5] = (-3) * 1E-04;
        bigC[5] = (1) * 1E-04;
        /* Set 6 */
        a[6] = 1;
        b[6] = -1;
        c[6] = 0;
        d[6] = -1;
        e[6] = 0;
        bigS[6] = (-3) * 1E-04;
        bigC[6] = (0) * 1E-04;
        /* Set 7 */

        a[7] = 0;
        b[7] = -2;
        c[7] = 2;
        d[7] = -2;
        e[7] = 1;
        bigS[7] = (-2) * 1E-04;
        bigC[7] = (1) * 1E-04;
        /* Set 8 */
        a[8] = 2;
        b[8] = 0;
        c[8] = -2;
        d[8] = 0;
        e[8] = 1;
        bigS[8] = (1) * 1E-04;
        bigC[8] = (0) * 1E-04;
        /* Set 9 */
        a[9] = 0;
        b[9] = 0;
        c[9] = 2;
        d[9] = -2;
        e[9] = 2;
        bigS[9] = (-13187 - 1.6 * T) * 1E-04;
        bigC[9] = (5736 - 3.1 * T) * 1E-04;
        /* Set 10 */
        a[10] = 0;
        b[10] = 1;
        c[10] = 0;
        d[10] = 0;
        e[10] = 0;
        bigS[10] = (1426 - 3.4 * T) * 1E-04;
        bigC[10] = (54 - 0.1 * T) * 1E-04;
        /* Set 11 */
        a[11] = 0;
        b[11] = 1;
        c[11] = 2;
        d[11] = -2;
        e[11] = 2;
        bigS[11] = (-517 + 1.2 * T) * 1E-04;
        bigC[11] = (224 - 0.6 * T) * 1E-04;
        /* Set 12 */
        a[12] = 0;
        b[12] = -1;
        c[12] = 2;
        d[12] = -2;
        e[12] = 2;
        bigS[12] = (217 - 0.5 * T) * 1E-04;
        bigC[12] = (-95 + 0.3 * T) * 1E-04;
        /* Set 13 */
        a[13] = 0;
        b[13] = 0;
        c[13] = 2;
        d[13] = -2;
        e[13] = 1;
        bigS[13] = (129 + 0.1 * T) * 1E-04;
        bigC[13] = (-70) * 1E-04;
        /* Set 14 */
        a[14] = 2;
        b[14] = 0;
        c[14] = 0;
        d[14] = -2;
        e[14] = 0;
        bigS[14] = (48) * 1E-04;
        bigC[14] = (1) * 1E-04;
        /* Set 15 */
        a[15] = 0;
        b[15] = 0;
        c[15] = 2;
        d[15] = -2;
        e[15] = 0;
        bigS[15] = (-22) * 1E-04;
        bigC[15] = (0) * 1E-04;
        /* Set 16 */
        a[16] = 0;
        b[16] = 2;
        c[16] = 0;
        d[16] = 0;
        e[16] = 0;
        bigS[16] = (17 - 0.1 * T) * 1E-04;
        bigC[16] = (0) * 1E-04;
        /* Set 17 */
        a[17] = 0;
        b[17] = 1;
        c[17] = 0;
        d[17] = 0;
        e[17] = 1;
        bigS[17] = (-15) * 1E-04;
        bigC[17] = (9) * 1E-04;
        /* Set 18 */
        a[18] = 0;
        b[18] = 2;
        c[18] = 2;
        d[18] = -2;
        e[18] = 2;
        bigS[18] = (-16 + 0.1 * T) * 1E-04;
        bigC[18] = (7) * 1E-04;
        /* Set 19 */
        a[19] = 0;
        b[19] = -1;
        c[19] = 0;
        d[19] = 0;
        e[19] = 1;
        bigS[19] = (-12) * 1E-04;
        bigC[19] = (6) * 1E-04;
        /* Set 20 */
        a[20] = -2;
        b[20] = 0;
        c[20] = 0;
        d[20] = 2;
        e[20] = 1;
        bigS[20] = (-6) * 1E-04;
        bigC[20] = (3) * 1E-04;
        /* Set 21 */
        a[21] = 0;
        b[21] = -1;
        c[21] = 2;
        d[21] = -2;
        e[21] = 1;
        bigS[21] = (-5) * 1E-04;
        bigC[21] = (3) * 1E-04;
        /* Set 22 */
        a[22] = 2;
        b[22] = 0;
        c[22] = 0;
        d[22] = -2;
        e[22] = 1;
        bigS[22] = (4) * 1E-04;
        bigC[22] = (-2) * 1E-04;
        /* Set 23 */
        a[23] = 0;
        b[23] = 1;
        c[23] = 2;
        d[23] = -2;
        e[23] = 1;
        bigS[23] = (4) * 1E-04;
        bigC[23] = (-2) * 1E-04;
        /* Set 24 */
        a[24] = 1;
        b[24] = 0;
        c[24] = 0;
        d[24] = -1;
        e[24] = 0;
        bigS[24] = (-4) * 1E-04;
        bigC[24] = (0) * 1E-04;
        /* Set 25 */
        a[25] = 2;
        b[25] = 1;
        c[25] = 0;
        d[25] = -2;
        e[25] = 0;
        bigS[25] = (1) * 1E-04;
        bigC[25] = (0) * 1E-04;
        /* Set 26 */
        a[26] = 0;
        b[26] = 0;
        c[26] = -2;
        d[26] = 2;
        e[26] = 1;
        bigS[26] = (1) * 1E-04;
        bigC[26] = (0) * 1E-04;
        /* Set 27 */
        a[27] = 0;
        b[27] = 1;
        c[27] = -2;
        d[27] = 2;
        e[27] = 0;
        bigS[27] = (-1) * 1E-04;
        bigC[27] = (0) * 1E-04;
        /* Set 28 */
        a[28] = 0;
        b[28] = 1;
        c[28] = 0;
        d[28] = 0;
        e[28] = 2;
        bigS[28] = (1) * 1E-04;
        bigC[28] = (0) * 1E-04;
        /* Set 29 */
        a[29] = -1;
        b[29] = 0;
        c[29] = 0;
        d[29] = 1;
        e[29] = 1;
        bigS[29] = (1) * 1E-04;
        bigC[29] = (0) * 1E-04;
        /* Set 30 */
        a[30] = 0;
        b[30] = 1;
        c[30] = 2;
        d[30] = -2;
        e[30] = 0;
        bigS[30] = (-1) * 1E-04;
        bigC[30] = (0) * 1E-04;
        /* Set 31 */
        a[31] = 0;
        b[31] = 0;
        c[31] = 2;
        d[31] = 0;
        e[31] = 2;
        bigS[31] = (-2274 - 0.2 * T) * 1E-04;
        bigC[31] = (977 - 0.5 * T) * 1E-04;
        /* Set 32 */
        a[32] = 1;
        b[32] = 0;
        c[32] = 0;
        d[32] = 0;
        e[32] = 0;
        bigS[32] = (712 + 0.1 * T) * 1E-04;
        bigC[32] = (-7) * 1E-04;
        /* Set 33 */
        a[33] = 0;
        b[33] = 0;
        c[33] = 2;
        d[33] = 0;
        e[33] = 1;
        bigS[33] = (-386 - 0.4 * T) * 1E-04;
        bigC[33] = (200) * 1E-04;
        /* Set 34 */
        a[34] = 1;
        b[34] = 0;
        c[34] = 2;
        d[34] = 0;
        e[34] = 2;
        bigS[34] = (-301) * 1E-04;
        bigC[34] = (129 - 0.1 * T) * 1E-04;
        /* Set 35 */
        a[35] = 1;
        b[35] = 0;
        c[35] = 0;
        d[35] = -2;
        e[35] = 0;
        bigS[35] = (-158) * 1E-04;
        bigC[35] = (-1) * 1E-04;
        /* Set 36 */
        a[36] = -1;
        b[36] = 0;
        c[36] = 2;
        d[36] = 0;
        e[36] = 2;
        bigS[36] = (123) * 1E-04;
        bigC[36] = (-53) * 1E-04;
        /* Set 37 */
        a[37] = 0;
        b[37] = 0;
        c[37] = 0;
        d[37] = 2;
        e[37] = 0;
        bigS[37] = (63) * 1E-04;
        bigC[37] = (-2) * 1E-04;
        /* Set 38 */
        a[38] = 1;
        b[38] = 0;
        c[38] = 0;
        d[38] = 0;
        e[38] = 1;
        bigS[38] = (63 + 0.1 * T) * 1E-04;
        bigC[38] = (-33) * 1E-04;
        /* Set 39 */
        a[39] = -1;
        b[39] = 0;
        c[39] = 0;
        d[39] = 0;
        e[39] = 1;
        bigS[39] = (-58 - 0.1 * T) * 1E-04;
        bigC[39] = (32) * 1E-04;
        /* Set 40 */
        a[40] = -1;
        b[40] = 0;
        c[40] = 2;
        d[40] = 2;
        e[40] = 2;
        bigS[40] = (-59) * 1E-04;
        bigC[40] = (26) * 1E-04;
        /* Set 41 */
        a[41] = 1;
        b[41] = 0;
        c[41] = 2;
        d[41] = 0;
        e[41] = 1;
        bigS[41] = (-51) * 1E-04;
        bigC[41] = (27) * 1E-04;
        /* Set 42 */
        a[42] = 0;
        b[42] = 0;
        c[42] = 2;
        d[42] = 2;
        e[42] = 2;
        bigS[42] = (-38) * 1E-04;
        bigC[42] = (16) * 1E-04;
        /* Set 43 */
        a[43] = 2;
        b[43] = 0;
        c[43] = 0;
        d[43] = 0;
        e[43] = 0;
        bigS[43] = (29) * 1E-04;
        bigC[43] = (-1) * 1E-04;
        /* Set 44 */
        a[44] = 1;
        b[44] = 0;
        c[44] = 2;
        d[44] = -2;
        e[44] = 2;
        bigS[44] = (29) * 1E-04;
        bigC[44] = (-12) * 1E-04;
        /* Set 45 */
        a[45] = 2;
        b[45] = 0;
        c[45] = 2;
        d[45] = 0;
        e[45] = 2;
        bigS[45] = (-31) * 1E-04;
        bigC[45] = (13) * 1E-04;
        /* Set 46 */
        a[46] = 0;
        b[46] = 0;
        c[46] = 2;
        d[46] = 0;
        e[46] = 0;
        bigS[46] = (26) * 1E-04;
        bigC[46] = (-1) * 1E-04;
        /* Set 47 */
        a[47] = -1;
        b[47] = 0;
        c[47] = 2;
        d[47] = 0;
        e[47] = 1;
        bigS[47] = (21) * 1E-04;
        bigC[47] = (-10) * 1E-04;
        /* Set 48 */
        a[48] = -1;
        b[48] = 0;
        c[48] = 0;
        d[48] = 2;
        e[48] = 1;
        bigS[48] = (16) * 1E-04;
        bigC[48] = (-8) * 1E-04;
        /* Set 49 */
        a[49] = 1;
        b[49] = 0;
        c[49] = 0;
        d[49] = -2;
        e[49] = 1;
        bigS[49] = (-13) * 1E-04;
        bigC[49] = (7) * 1E-04;
        /* Set 50 */
        a[50] = -1;
        b[50] = 0;
        c[50] = 2;
        d[50] = 2;
        e[50] = 1;
        bigS[50] = (-10) * 1E-04;
        bigC[50] = (5) * 1E-04;
        /* Set 51 */
        a[51] = 1;
        b[51] = 1;
        c[51] = 0;
        d[51] = -2;
        e[51] = 0;
        bigS[51] = (-7) * 1E-04;
        bigC[51] = (0) * 1E-04;
        /* Set 52 */
        a[52] = 0;
        b[52] = 1;
        c[52] = 2;
        d[52] = 0;
        e[52] = 2;
        bigS[52] = (7) * 1E-04;
        bigC[52] = (-3) * 1E-04;
        /* Set 53 */
        a[53] = 0;
        b[53] = -1;
        c[53] = 2;
        d[53] = 0;
        e[53] = 2;
        bigS[53] = (-7) * 1E-04;
        bigC[53] = (3) * 1E-04;
        /* Set 54 */
        a[54] = 1;
        b[54] = 0;
        c[54] = 2;
        d[54] = 2;
        e[54] = 2;
        bigS[54] = (-8) * 1E-04;
        bigC[54] = (3) * 1E-04;
        /* Set 55 */
        a[55] = 1;
        b[55] = 0;
        c[55] = 0;
        d[55] = 2;
        e[55] = 0;
        bigS[55] = (6) * 1E-04;
        bigC[55] = (0) * 1E-04;
        /* Set 56 */
        a[56] = 2;
        b[56] = 0;
        c[56] = 2;
        d[56] = -2;
        e[56] = 2;
        bigS[56] = (6) * 1E-04;
        bigC[56] = (-3) * 1E-04;
        /* Set 57 */
        a[57] = 0;
        b[57] = 0;
        c[57] = 0;
        d[57] = 2;
        e[57] = 1;
        bigS[57] = (-6) * 1E-04;
        bigC[57] = (3) * 1E-04;
        /* Set 58 */
        a[58] = 0;
        b[58] = 0;
        c[58] = 2;
        d[58] = 2;
        e[58] = 1;
        bigS[58] = (-7) * 1E-04;
        bigC[58] = (3) * 1E-04;
        /* Set 59 */
        a[59] = 1;
        b[59] = 0;
        c[59] = 2;
        d[59] = -2;
        e[59] = 1;
        bigS[59] = (6) * 1E-04;
        bigC[59] = (-3) * 1E-04;
        /* Set 60 */
        a[60] = 0;
        b[60] = 0;
        c[60] = 0;
        d[60] = -2;
        e[60] = 1;
        bigS[60] = (-5) * 1E-04;
        bigC[60] = (3) * 1E-04;
        /* Set 61 */
        a[61] = 1;
        b[61] = -1;
        c[61] = 0;
        d[61] = 0;
        e[61] = 0;
        bigS[61] = (5) * 1E-04;
        bigC[61] = (0) * 1E-04;
        /* Set 62 */
        a[62] = 2;
        b[62] = 0;
        c[62] = 2;
        d[62] = 0;
        e[62] = 1;
        bigS[62] = (-5) * 1E-04;
        bigC[62] = (3) * 1E-04;
        /* Set 63 */
        a[63] = 0;
        b[63] = 1;
        c[63] = 0;
        d[63] = -2;
        e[63] = 0;
        bigS[63] = (-4) * 1E-04;
        bigC[63] = (0) * 1E-04;
        /* Set 64 */
        a[64] = 1;
        b[64] = 0;
        c[64] = -2;
        d[64] = 0;
        e[64] = 0;
        bigS[64] = (4) * 1E-04;
        bigC[64] = (0) * 1E-04;
        /* Set 65 */
        a[65] = 0;
        b[65] = 0;
        c[65] = 0;
        d[65] = 1;
        e[65] = 0;
        bigS[65] = (-4) * 1E-04;
        bigC[65] = (0) * 1E-04;
        /* Set 66 */
        a[66] = 1;
        b[66] = 1;
        c[66] = 0;
        d[66] = 0;
        e[66] = 0;
        bigS[66] = (-3) * 1E-04;
        bigC[66] = (0) * 1E-04;
        /* Set 67 */
        a[67] = 1;
        b[67] = 0;
        c[67] = 2;
        d[67] = 0;
        e[67] = 0;
        bigS[67] = (3) * 1E-04;
        bigC[67] = (0) * 1E-04;
        /* Set 68 */
        a[68] = 1;
        b[68] = -1;
        c[68] = 2;
        d[68] = 0;
        e[68] = 2;
        bigS[68] = (-3) * 1E-04;
        bigC[68] = (1) * 1E-04;
        /* Set 69 */
        a[69] = -1;
        b[69] = -1;
        c[69] = 2;
        d[69] = 2;
        e[69] = 2;
        bigS[69] = (-3) * 1E-04;
        bigC[69] = (1) * 1E-04;
        /* Set 70 */
        a[70] = -2;
        b[70] = 0;
        c[70] = 0;
        d[70] = 0;
        e[70] = 1;
        bigS[70] = (-2) * 1E-04;
        bigC[70] = (1) * 1E-04;
        /* Set 71 */
        a[71] = 3;
        b[71] = 0;
        c[71] = 2;
        d[71] = 0;
        e[71] = 2;
        bigS[71] = (-3) * 1E-04;
        bigC[71] = (1) * 1E-04;
        /* Set 72 */
        a[72] = 0;
        b[72] = -1;
        c[72] = 2;
        d[72] = 2;
        e[72] = 2;
        bigS[72] = (-3) * 1E-04;
        bigC[72] = (1) * 1E-04;
        /* Set 73 */
        a[73] = 1;
        b[73] = 1;
        c[73] = 2;
        d[73] = 0;
        e[73] = 2;
        bigS[73] = (2) * 1E-04;
        bigC[73] = (-1) * 1E-04;
        /* Set 74 */
        a[74] = -1;
        b[74] = 0;
        c[74] = 2;
        d[74] = -2;
        e[74] = 1;
        bigS[74] = (-2) * 1E-04;
        bigC[74] = (1) * 1E-04;
        /* Set 75 */
        a[75] = 2;
        b[75] = 0;
        c[75] = 0;
        d[75] = 0;
        e[75] = 1;
        bigS[75] = (2) * 1E-04;
        bigC[75] = (-1) * 1E-04;
        /* Set 76 */
        a[76] = 1;
        b[76] = 0;
        c[76] = 0;
        d[76] = 0;
        e[76] = 2;
        bigS[76] = (-2) * 1E-04;
        bigC[76] = (1) * 1E-04;
        /* Set 77 */
        a[77] = 3;
        b[77] = 0;
        c[77] = 0;
        d[77] = 0;
        e[77] = 0;
        bigS[77] = (2) * 1E-04;
        bigC[77] = (0) * 1E-04;
        /* Set 78 */
        a[78] = 0;
        b[78] = 0;
        c[78] = 2;
        d[78] = 1;
        e[78] = 2;
        bigS[78] = (2) * 1E-04;
        bigC[78] = (-1) * 1E-04;
        /* Set 79 */
        a[79] = -1;
        b[79] = 0;
        c[79] = 0;
        d[79] = 0;
        e[79] = 2;
        bigS[79] = (1) * 1E-04;
        bigC[79] = (-1) * 1E-04;
        /* Set 80 */
        a[80] = 1;
        b[80] = 0;
        c[80] = 0;
        d[80] = -4;
        e[80] = 0;
        bigS[80] = (-1) * 1E-04;
        bigC[80] = (0) * 1E-04;
        /* Set 81 */
        a[81] = -2;
        b[81] = 0;
        c[81] = 2;
        d[81] = 2;
        e[81] = 2;
        bigS[81] = (1) * 1E-04;
        bigC[81] = (-1) * 1E-04;
        /* Set 82 */
        a[82] = -1;
        b[82] = 0;
        c[82] = 2;
        d[82] = 4;
        e[82] = 2;
        bigS[82] = (-2) * 1E-04;
        bigC[82] = (1) * 1E-04;
        /* Set 83 */
        a[83] = 2;
        b[83] = 0;
        c[83] = 0;
        d[83] = -4;
        e[83] = 0;
        bigS[83] = (-1) * 1E-04;
        bigC[83] = (0) * 1E-04;
        /* Set 84 */
        a[84] = 1;
        b[84] = 1;
        c[84] = 2;
        d[84] = -2;
        e[84] = 2;
        bigS[84] = (1) * 1E-04;
        bigC[84] = (-1) * 1E-04;
        /* Set 85 */
        a[85] = 1;
        b[85] = 0;
        c[85] = 2;
        d[85] = 2;
        e[85] = 1;
        bigS[85] = (-1) * 1E-04;
        bigC[85] = (1) * 1E-04;
        /* Set 86 */
        a[86] = -2;
        b[86] = 0;
        c[86] = 2;
        d[86] = 4;
        e[86] = 2;
        bigS[86] = (-1) * 1E-04;
        bigC[86] = (1) * 1E-04;
        /* Set 87 */
        a[87] = -1;
        b[87] = 0;
        c[87] = 4;
        d[87] = 0;
        e[87] = 2;
        bigS[87] = (1) * 1E-04;
        bigC[87] = (0) * 1E-04;
        /* Set 88 */
        a[88] = 1;
        b[88] = -1;
        c[88] = 0;
        d[88] = -2;
        e[88] = 0;
        bigS[88] = (1) * 1E-04;
        bigC[88] = (0) * 1E-04;
        /* Set 89 */
        a[89] = 2;
        b[89] = 0;
        c[89] = 2;
        d[89] = -2;
        e[89] = 1;
        bigS[89] = (1) * 1E-04;
        bigC[89] = (-1) * 1E-04;
        /* Set 90 */
        a[90] = 2;
        b[90] = 0;
        c[90] = 2;
        d[90] = 2;
        e[90] = 2;
        bigS[90] = (-1) * 1E-04;
        bigC[90] = (0) * 1E-04;
        /* Set 91 */
        a[91] = 1;
        b[91] = 0;
        c[91] = 0;
        d[91] = 2;
        e[91] = 1;
        bigS[91] = (-1) * 1E-04;
        bigC[91] = (0) * 1E-04;
        /* Set 92 */
        a[92] = 0;
        b[92] = 0;
        c[92] = 4;
        d[92] = -2;
        e[92] = 2;
        bigS[92] = (1) * 1E-04;
        bigC[92] = (0) * 1E-04;
        /* Set 93 */
        a[93] = 3;
        b[93] = 0;
        c[93] = 2;
        d[93] = -2;
        e[93] = 2;
        bigS[93] = (1) * 1E-04;
        bigC[93] = (0) * 1E-04;
        /* Set 94 */
        a[94] = 1;
        b[94] = 0;
        c[94] = 2;
        d[94] = -2;
        e[94] = 0;
        bigS[94] = (-1) * 1E-04;
        bigC[94] = (0) * 1E-04;
        /* Set 95 */
        a[95] = 0;
        b[95] = 1;
        c[95] = 2;
        d[95] = 0;
        e[95] = 1;
        bigS[95] = (1) * 1E-04;
        bigC[95] = (0) * 1E-04;
        /* Set 96 */
        a[96] = -1;
        b[96] = -1;
        c[96] = 0;
        d[96] = 2;
        e[96] = 1;
        bigS[96] = (1) * 1E-04;
        bigC[96] = (0) * 1E-04;
        /* Set 97 */
        a[97] = 0;
        b[97] = 0;
        c[97] = -2;
        d[97] = 0;
        e[97] = 1;
        bigS[97] = (-1) * 1E-04;
        bigC[97] = (0) * 1E-04;
        /* Set 98 */
        a[98] = 0;
        b[98] = 0;
        c[98] = 2;
        d[98] = -1;
        e[98] = 2;
        bigS[98] = (-1) * 1E-04;
        bigC[98] = (0) * 1E-04;
        /* Set 99 */
        a[99] = 0;
        b[99] = 1;
        c[99] = 0;
        d[99] = 2;
        e[99] = 0;
        bigS[99] = (-1) * 1E-04;
        bigC[99] = (0) * 1E-04;
        /* Set 100 */
        a[100] = 1;
        b[100] = 0;
        c[100] = -2;
        d[100] = -2;
        e[100] = 0;
        bigS[100] = (-1) * 1E-04;
        bigC[100] = (0) * 1E-04;
        /* Set 101 */
        a[101] = 0;
        b[101] = -1;
        c[101] = 2;
        d[101] = 0;
        e[101] = 1;
        bigS[101] = (-1) * 1E-04;
        bigC[101] = (0) * 1E-04;
        /* Set 102 */
        a[102] = 1;
        b[102] = 1;
        c[102] = 0;
        d[102] = -2;
        e[102] = 1;
        bigS[102] = (-1) * 1E-04;
        bigC[102] = (0) * 1E-04;
        /* Set 103 */
        a[103] = 1;
        b[103] = 0;
        c[103] = -2;
        d[103] = 2;
        e[103] = 0;
        bigS[103] = (-1) * 1E-04;
        bigC[103] = (0) * 1E-04;
        /* Set 104 */
        a[104] = 2;
        b[104] = 0;
        c[104] = 0;
        d[104] = 2;
        e[104] = 0;
        bigS[104] = (1) * 1E-04;
        bigC[104] = (0) * 1E-04;
        /* Set 105 */
        a[105] = 0;
        b[105] = 0;
        c[105] = 2;
        d[105] = 4;
        e[105] = 2;
        bigS[105] = (-1) * 1E-04;
        bigC[105] = (0) * 1E-04;
        /* Set 106 */
        a[106] = 0;
        b[106] = 1;
        c[106] = 0;
        d[106] = 1;
        e[106] = 0;
        bigS[106] = (1) * 1E-04;
        bigC[106] = (0) * 1E-04;

        /* Calculate the IAU 1980 nutation angles from the series */
        for (j = 1; j <= 106; j++) {
            bigA = a[j] * l + b[j] * lprime + c[j] * F + d[j] * bigD + e[j] * omega;
            delta_psi = delta_psi + (bigS[j] * Math.sin(bigA) / 3600) * Math.PI / 180;
            delta_epsilon = delta_epsilon + (bigC[j] * Math.cos(bigA) / 3600) * Math.PI / 180;
        }

        /* Store the nutations and mean obliquity in a vector for return */
        nutation_vector[1] = delta_psi;
        nutation_vector[2] = delta_epsilon;
        nutation_vector[3] = mean_obliquity;

    }

    /*
  Algorithm to calculate the nutation in longitude and obliquity, as well as the mean obliquity, at jultime.
  The format of the nutation_vector is as follows:
      nutation_vector[1] = delta_psi (in radians);
      nutation_vector[2] = delta_epsilon (in radians);
      nutation_vector[3] = mean_obliquity (in radians).
  This algorithm uses the IAU 1980 nutation series, plus four correction terms, and a planetary series.  The intent is to support both optical and radar observations.
  Algorithm taken from "Explanatory Supplement to the Astronomical Almanac", P. Kenneth Seidelmann, 1992, pp. 111-119.
  Tested and verified 10-18-99.
  */
    public static void getNutation(double jultime, double nutation_vector[]) {

        double[] initial_nutation_vector = new double[4];
        double[] a = new double[5];
        double[] b = new double[5];
        double[] c = new double[5];
        double[] d = new double[5];
        double[] e = new double[5];
        double[] little_a = new double[86];
        double[] little_b = new double[86];
        double[] little_c = new double[86];
        double[] little_d = new double[86];
        double[] little_e = new double[86];
        double[] little_f = new double[86];
        double[] little_g = new double[86];
        double[] little_h = new double[86];
        double[] little_i = new double[86];
        double[] little_j = new double[86];
        double[] bigLS = new double[86];
        double[] bigLC = new double[86];
        double[] bigOC = new double[86];
        double[] bigOS = new double[86];

        double T = 0, mean_obliquity = 0, delta_psi = 0, delta_epsilon = 0, l = 0, lprime = 0, F = 0, bigD = 0, omega = 0, bigA = 0, bigQ = 0, bigM = 0, bigV = 0, bigJ = 0, bigE = 0, S = 0, delta_psi_c = 0, delta_epsilon_c = 0, delta_psi_p = 0, delta_epsilon_p = 0;

        int j = 0;

        /* Calculate the time variable, and the fundamental arguments */
        T = (jultime - 2451545.0) / 36525;
        l = (134.0 + 57.0 / 60.0 + 46.733 / 3600) + (1325 * 360 + (198 + 52.0 / 60.0 + 2.633 / 3600)) * T + (31.310 / 3600) * Math.pow(T, 2) + (0.064 / 3600) * Math.pow(T, 3);
        l = l * Math.PI / 180;
        lprime = (357.0 + 31.0 / 60.0 + 39.804 / 3600) + (99 * 360 + (359 + 3.0 / 60.0 + 1.224 / 3600)) * T - (0.577 / 3600) * Math.pow(T, 2) - (0.012 / 3600) * Math.pow(T, 3);
        lprime = lprime * Math.PI / 180;
        F = (93.0 + 16.0 / 60.0 + 18.877 / 3600) + (1342 * 360 + (82 + 1.0 / 60.0 + 3.137 / 3600)) * T - (13.257 / 3600) * Math.pow(T, 2) + (0.011 / 3600) * Math.pow(T, 3);
        F = F * Math.PI / 180;
        bigD = (297.0 + 51.0 / 60.0 + 1.307 / 3600) + (1236 * 360 + (307 + 6.0 / 60.0 + 41.328 / 3600)) * T - (6.891 / 3600) * Math.pow(T, 2) + (0.019 / 3600) * Math.pow(T, 3);
        bigD = bigD * Math.PI / 180;
        omega = (125.0 + 2.0 / 60.0 + 40.280 / 3600) - (5 * 360 + (134 + 8.0 / 60.0 + 10.539 / 3600)) * T + (7.455 / 3600) * Math.pow(T, 2) + (0.008 / 3600) * Math.pow(T, 3);
        omega = omega * Math.PI / 180;
        bigQ = 252.3 + 149472.7 * T;
        bigQ = bigQ * Math.PI / 180;
        bigM = 353.3 + 19140.3 * T;
        bigM = bigM * Math.PI / 180;
        bigV = 179.9 + 58517.8 * T;
        bigV = bigV * Math.PI / 180;
        bigJ = 32.3 + 3034.9 * T;
        bigJ = bigJ * Math.PI / 180;
        bigE = 98.4 + 35999.4 * T;
        bigE = bigE * Math.PI / 180;
        S = 48.0 + 1222.1 * T;
        S = S * Math.PI / 180;

        /* Get the IAU 1980 nutation values */
        getNutationIAU1980(jultime, initial_nutation_vector);
        delta_psi = initial_nutation_vector[1];
        delta_epsilon = initial_nutation_vector[2];
        mean_obliquity = initial_nutation_vector[3];

        /* Initialize the correction arguments and coefficients */
        /* Set 1 */
        a[1] = 0;
        b[1] = 0;
        c[1] = 0;
        d[1] = 0;
        e[1] = 1;
        bigLS[1] = -725 * 1E-05;
        bigLC[1] = 417 * 1E-05;
        bigOC[1] = 213 * 1E-05;
        bigOS[1] = 224 * 1E-05;
        /* Set 2 */
        a[2] = 0;
        b[2] = 1;
        c[2] = 0;
        d[2] = 0;
        e[2] = 0;
        bigLS[2] = 523 * 1E-05;
        bigLC[2] = 61 * 1E-05;
        bigOC[2] = 208 * 1E-05;
        bigOS[2] = -24 * 1E-05;
        /* Set 3 */
        a[3] = 0;
        b[3] = 0;
        c[3] = 2;
        d[3] = -2;
        e[3] = 2;
        bigLS[3] = 102 * 1E-05;
        bigLC[3] = -118 * 1E-05;
        bigOC[3] = -41 * 1E-05;
        bigOS[3] = -47 * 1E-05;
        /* Set 4 */
        a[4] = 0;
        b[4] = 0;
        c[4] = 2;
        d[4] = 0;
        e[4] = 2;
        bigLS[4] = -81 * 1E-05;
        bigLC[4] = 0;
        bigOC[4] = 32 * 1E-05;
        bigOS[4] = 0;

        /* Calculate the corrections to the 1980 IAU nutation angles from the series */
        for (j = 1; j <= 4; j++) {
            bigA = a[j] * l + b[j] * lprime + c[j] * F + d[j] * bigD + e[j] * omega;
            delta_psi_c = delta_psi_c + (bigLS[j] * Math.sin(bigA) / 3600 + bigLC[j] * Math.cos(bigA) / 3600) * Math.PI / 180;
            delta_epsilon_c = delta_epsilon_c + (bigOC[j] * Math.cos(bigA) / 3600 + bigOS[j] * Math.sin(bigA) / 3600) * Math.PI / 180;
        }

        /* Initialize the planetary arguments and coefficients */
        /* Set 1 */
        little_a[1] = 0;
        little_b[1] = 0;
        little_c[1] = 0;
        little_d[1] = 0;
        little_e[1] = 0;
        little_f[1] = 0;
        little_g[1] = 2;
        little_h[1] = 0;
        little_i[1] = 0;
        little_j[1] = 0;
        bigLS[1] = -1;
        bigLC[1] = 0;
        bigOC[1] = 0;
        bigOS[1] = 0;
        /* Set 2 */
        little_a[2] = 1;
        little_b[2] = 0;
        little_c[2] = -2;
        little_d[2] = -1;
        little_e[2] = 3;
        little_f[2] = 0;
        little_g[2] = -1;
        little_h[2] = 0;
        little_i[2] = 0;
        little_j[2] = 0;
        bigLS[2] = 0;
        bigLC[2] = -1;
        bigOC[2] = 0;
        bigOS[2] = 0;
        /* Set 3 */
        little_a[3] = 0;
        little_b[3] = 0;
        little_c[3] = 0;
        little_d[3] = 0;
        little_e[3] = 0;
        little_f[3] = 1;
        little_g[3] = -2;
        little_h[3] = 0;
        little_i[3] = 0;
        little_j[3] = 0;
        bigLS[3] = 0;
        bigLC[3] = -1;
        bigOC[3] = 0;
        bigOS[3] = 0;
        /* Set 4 */
        little_a[4] = 0;
        little_b[4] = 0;
        little_c[4] = 0;
        little_d[4] = 0;
        little_e[4] = 0;
        little_f[4] = 1;
        little_g[4] = -1;
        little_h[4] = 0;
        little_i[4] = 0;
        little_j[4] = 0;
        bigLS[4] = 16;
        bigLC[4] = 0;
        bigOC[4] = 0;
        bigOS[4] = 0;
        /* Set 5 */
        little_a[5] = 0;
        little_b[5] = 0;
        little_c[5] = 0;
        little_d[5] = 0;
        little_e[5] = 0;
        little_f[5] = 1;
        little_g[5] = 1;
        little_h[5] = 0;
        little_i[5] = 0;
        little_j[5] = 0;
        bigLS[5] = -4;
        bigLC[5] = 0;
        bigOC[5] = 0;
        bigOS[5] = 1;
        /* Set 6 */
        little_a[6] = 0;
        little_b[6] = 0;
        little_c[6] = 0;
        little_d[6] = 0;
        little_e[6] = 0;
        little_f[6] = 1;
        little_g[6] = -3;
        little_h[6] = 0;
        little_i[6] = 0;
        little_j[6] = 0;
        bigLS[6] = -1;
        bigLC[6] = 0;
        bigOC[6] = 0;
        bigOS[6] = 0;
        /* Set 7 */
        little_a[7] = 0;
        little_b[7] = 0;
        little_c[7] = 0;
        little_d[7] = 0;
        little_e[7] = 0;
        little_f[7] = 2;
        little_g[7] = -4;
        little_h[7] = 0;
        little_i[7] = 0;
        little_j[7] = 0;
        bigLS[7] = 4;
        bigLC[7] = 0;
        bigOC[7] = 0;
        bigOS[7] = 3;
        /* Set 8 */
        little_a[8] = 0;
        little_b[8] = 0;
        little_c[8] = 0;
        little_d[8] = 0;
        little_e[8] = 0;
        little_f[8] = 2;
        little_g[8] = -3;
        little_h[8] = 0;
        little_i[8] = 0;
        little_j[8] = 0;
        bigLS[8] = 0;
        bigLC[8] = 7;
        bigOC[8] = 0;
        bigOS[8] = 0;
        /* Set 9 */
        little_a[9] = 0;
        little_b[9] = 0;
        little_c[9] = 0;
        little_d[9] = 0;
        little_e[9] = 0;
        little_f[9] = 2;
        little_g[9] = -5;
        little_h[9] = 0;
        little_i[9] = 0;
        little_j[9] = 0;
        bigLS[9] = 0;
        bigLC[9] = -1;
        bigOC[9] = 1;
        bigOS[9] = 0;
        /* Set 10 */
        little_a[10] = 0;
        little_b[10] = 0;
        little_c[10] = 0;
        little_d[10] = 0;
        little_e[10] = 0;
        little_f[10] = 2;
        little_g[10] = -2;
        little_h[10] = 0;
        little_i[10] = 0;
        little_j[10] = 0;
        bigLS[10] = -6;
        bigLC[10] = 1;
        bigOC[10] = 0;
        bigOS[10] = 0;
        /* Set 11 */
        little_a[11] = 0;
        little_b[11] = 0;
        little_c[11] = 0;
        little_d[11] = 0;
        little_e[11] = 0;
        little_f[11] = 2;
        little_g[11] = -1;
        little_h[11] = 0;
        little_i[11] = 0;
        little_j[11] = 0;
        bigLS[11] = 0;
        bigLC[11] = -2;
        bigOC[11] = -1;
        bigOS[11] = 0;
        /* Set 12 */
        little_a[12] = 0;
        little_b[12] = 0;
        little_c[12] = 0;
        little_d[12] = 0;
        little_e[12] = 0;
        little_f[12] = 2;
        little_g[12] = 0;
        little_h[12] = 0;
        little_i[12] = 0;
        little_j[12] = 0;
        bigLS[12] = 4;
        bigLC[12] = 0;
        bigOC[12] = 0;
        bigOS[12] = -2;
        /* Set 13 */
        little_a[13] = 0;
        little_b[13] = 0;
        little_c[13] = 0;
        little_d[13] = 0;
        little_e[13] = 0;
        little_f[13] = 3;
        little_g[13] = -7;
        little_h[13] = 0;
        little_i[13] = 0;
        little_j[13] = 0;
        bigLS[13] = 1;
        bigLC[13] = 0;
        bigOC[13] = 0;
        bigOS[13] = 0;
        /* Set 14 */
        little_a[14] = 0;
        little_b[14] = 0;
        little_c[14] = 0;
        little_d[14] = 0;
        little_e[14] = 0;
        little_f[14] = 3;
        little_g[14] = -6;
        little_h[14] = 0;
        little_i[14] = 0;
        little_j[14] = 0;
        bigLS[14] = 0;
        bigLC[14] = -1;
        bigOC[14] = 0;
        bigOS[14] = 0;
        /* Set 15 */
        little_a[15] = 0;
        little_b[15] = 0;
        little_c[15] = 0;
        little_d[15] = 0;
        little_e[15] = 0;
        little_f[15] = 3;
        little_g[15] = -5;
        little_h[15] = 0;
        little_i[15] = 0;
        little_j[15] = 0;
        bigLS[15] = 20;
        bigLC[15] = -4;
        bigOC[15] = 1;
        bigOS[15] = 7;
        /* Set 16 */
        little_a[16] = 0;
        little_b[16] = 0;
        little_c[16] = 0;
        little_d[16] = 1;
        little_e[16] = 0;
        little_f[16] = -3;
        little_g[16] = 5;
        little_h[16] = 0;
        little_i[16] = 0;
        little_j[16] = 0;
        bigLS[16] = 0;
        bigLC[16] = 1;
        bigOC[16] = 0;
        bigOS[16] = 0;
        /* Set 17 */
        little_a[17] = 0;
        little_b[17] = 0;
        little_c[17] = 0;
        little_d[17] = 0;
        little_e[17] = 0;
        little_f[17] = 3;
        little_g[17] = -4;
        little_h[17] = 0;
        little_i[17] = 0;
        little_j[17] = 0;
        bigLS[17] = 0;
        bigLC[17] = 4;
        bigOC[17] = 0;
        bigOS[17] = 0;
        /* Set 18 */
        little_a[18] = 0;
        little_b[18] = 0;
        little_c[18] = 0;
        little_d[18] = 0;
        little_e[18] = 0;
        little_f[18] = 3;
        little_g[18] = -3;
        little_h[18] = 0;
        little_i[18] = 0;
        little_j[18] = 0;
        bigLS[18] = 2;
        bigLC[18] = 0;
        bigOC[18] = 0;
        bigOS[18] = 0;
        /* Set 19 */
        little_a[19] = 0;
        little_b[19] = 2;
        little_c[19] = -2;
        little_d[19] = 1;
        little_e[19] = 0;
        little_f[19] = -3;
        little_g[19] = 3;
        little_h[19] = 0;
        little_i[19] = 0;
        little_j[19] = 0;
        bigLS[19] = 1;
        bigLC[19] = 0;
        bigOC[19] = 0;
        bigOS[19] = -1;
        /* Set 20 */
        little_a[20] = 2;
        little_b[20] = 0;
        little_c[20] = -2;
        little_d[20] = 0;
        little_e[20] = 0;
        little_f[20] = -3;
        little_g[20] = 3;
        little_h[20] = 0;
        little_i[20] = 0;
        little_j[20] = 0;
        bigLS[20] = 1;
        bigLC[20] = 0;
        bigOC[20] = 0;
        bigOS[20] = 0;
        /* Set 21 */
        little_a[21] = 0;
        little_b[21] = 0;
        little_c[21] = 0;
        little_d[21] = 0;
        little_e[21] = 0;
        little_f[21] = 3;
        little_g[21] = -2;
        little_h[21] = 0;
        little_i[21] = 0;
        little_j[21] = 0;
        bigLS[21] = 0;
        bigLC[21] = -1;
        bigOC[21] = -1;
        bigOS[21] = 0;
        /* Set 22 */
        little_a[22] = 0;
        little_b[22] = 0;
        little_c[22] = 0;
        little_d[22] = 0;
        little_e[22] = 0;
        little_f[22] = 4;
        little_g[22] = -7;
        little_h[22] = 0;
        little_i[22] = 0;
        little_j[22] = 0;
        bigLS[22] = 0;
        bigLC[22] = 1;
        bigOC[22] = 0;
        bigOS[22] = 0;
        /* Set 23 */
        little_a[23] = 0;
        little_b[23] = 0;
        little_c[23] = 0;
        little_d[23] = 0;
        little_e[23] = 0;
        little_f[23] = 4;
        little_g[23] = -6;
        little_h[23] = 0;
        little_i[23] = 0;
        little_j[23] = 0;
        bigLS[23] = -5;
        bigLC[23] = 1;
        bigOC[23] = -1;
        bigOS[23] = -2;
        /* Set 24 */
        little_a[24] = 0;
        little_b[24] = 0;
        little_c[24] = 0;
        little_d[24] = 0;
        little_e[24] = 0;
        little_f[24] = 4;
        little_g[24] = -4;
        little_h[24] = 0;
        little_i[24] = 0;
        little_j[24] = 0;
        bigLS[24] = 1;
        bigLC[24] = 0;
        bigOC[24] = 0;
        bigOS[24] = 0;
        /* Set 25 */
        little_a[25] = 0;
        little_b[25] = 0;
        little_c[25] = 0;
        little_d[25] = 0;
        little_e[25] = 0;
        little_f[25] = 5;
        little_g[25] = -8;
        little_h[25] = 0;
        little_i[25] = 0;
        little_j[25] = 0;
        bigLS[25] = 0;
        bigLC[25] = -3;
        bigOC[25] = 2;
        bigOS[25] = 0;
        /* Set 26 */
        little_a[26] = 0;
        little_b[26] = 0;
        little_c[26] = 0;
        little_d[26] = 0;
        little_e[26] = 0;
        little_f[26] = 5;
        little_g[26] = -7;
        little_h[26] = 0;
        little_i[26] = 0;
        little_j[26] = 0;
        bigLS[26] = 2;
        bigLC[26] = 0;
        bigOC[26] = 0;
        bigOS[26] = 1;
        /* Set 27 */
        little_a[27] = 0;
        little_b[27] = 2;
        little_c[27] = -2;
        little_d[27] = 1;
        little_e[27] = 0;
        little_f[27] = -5;
        little_g[27] = 6;
        little_h[27] = 0;
        little_i[27] = 0;
        little_j[27] = 0;
        bigLS[27] = 0;
        bigLC[27] = -4;
        bigOC[27] = -2;
        bigOS[27] = 0;
        /* Set 28 */
        little_a[28] = 0;
        little_b[28] = 2;
        little_c[28] = -2;
        little_d[28] = 0;
        little_e[28] = 0;
        little_f[28] = -5;
        little_g[28] = 6;
        little_h[28] = 0;
        little_i[28] = 0;
        little_j[28] = 0;
        bigLS[28] = 0;
        bigLC[28] = 2;
        bigOC[28] = 0;
        bigOS[28] = 0;
        /* Set 29 */
        little_a[29] = 0;
        little_b[29] = 0;
        little_c[29] = 0;
        little_d[29] = 0;
        little_e[29] = 0;
        little_f[29] = 5;
        little_g[29] = -5;
        little_h[29] = 0;
        little_i[29] = 0;
        little_j[29] = 0;
        bigLS[29] = -1;
        bigLC[29] = 0;
        bigOC[29] = 0;
        bigOS[29] = 0;
        /* Set 30 */
        little_a[30] = 0;
        little_b[30] = 0;
        little_c[30] = 0;
        little_d[30] = 0;
        little_e[30] = 0;
        little_f[30] = 6;
        little_g[30] = -9;
        little_h[30] = 0;
        little_i[30] = 0;
        little_j[30] = 0;
        bigLS[30] = 0;
        bigLC[30] = -1;
        bigOC[30] = 0;
        bigOS[30] = 0;
        /* Set 31 */
        little_a[31] = 0;
        little_b[31] = 0;
        little_c[31] = 0;
        little_d[31] = 0;
        little_e[31] = 0;
        little_f[31] = 6;
        little_g[31] = -8;
        little_h[31] = 0;
        little_i[31] = 0;
        little_j[31] = 0;
        bigLS[31] = -1;
        bigLC[31] = 0;
        bigOC[31] = 0;
        bigOS[31] = -1;
        /* Set 32 */
        little_a[32] = 2;
        little_b[32] = 0;
        little_c[32] = -2;
        little_d[32] = 0;
        little_e[32] = 0;
        little_f[32] = -6;
        little_g[32] = 8;
        little_h[32] = 0;
        little_i[32] = 0;
        little_j[32] = 0;
        bigLS[32] = -1;
        bigLC[32] = 0;
        bigOC[32] = 0;
        bigOS[32] = 0;
        /* Set 33 */
        little_a[33] = 0;
        little_b[33] = 0;
        little_c[33] = 0;
        little_d[33] = 0;
        little_e[33] = 0;
        little_f[33] = 7;
        little_g[33] = -9;
        little_h[33] = 0;
        little_i[33] = 0;
        little_j[33] = 0;
        bigLS[33] = 1;
        bigLC[33] = 0;
        bigOC[33] = 0;
        bigOS[33] = 0;
        /* Set 34 */
        little_a[34] = 0;
        little_b[34] = 0;
        little_c[34] = 0;
        little_d[34] = 0;
        little_e[34] = 0;
        little_f[34] = 8;
        little_g[34] = -10;
        little_h[34] = 0;
        little_i[34] = 0;
        little_j[34] = 0;
        bigLS[34] = -1;
        bigLC[34] = 0;
        bigOC[34] = 0;
        bigOS[34] = 0;
        /* Set 35 */
        little_a[35] = 0;
        little_b[35] = 0;
        little_c[35] = 0;
        little_d[35] = 0;
        little_e[35] = 0;
        little_f[35] = 8;
        little_g[35] = -15;
        little_h[35] = 0;
        little_i[35] = 0;
        little_j[35] = 0;
        bigLS[35] = -1;
        bigLC[35] = -1;
        bigOC[35] = 0;
        bigOS[35] = 0;
        /* Set 36 */
        little_a[36] = 0;
        little_b[36] = 0;
        little_c[36] = 0;
        little_d[36] = 0;
        little_e[36] = 0;
        little_f[36] = 8;
        little_g[36] = -13;
        little_h[36] = 0;
        little_i[36] = 0;
        little_j[36] = 0;
        bigLS[36] = 3;
        bigLC[36] = -4;
        bigOC[36] = 0;
        bigOS[36] = 0;
        /* Set 37 */
        little_a[37] = 0;
        little_b[37] = 0;
        little_c[37] = 0;
        little_d[37] = 1;
        little_e[37] = 0;
        little_f[37] = 8;
        little_g[37] = -13;
        little_h[37] = 0;
        little_i[37] = 0;
        little_j[37] = 0;
        bigLS[37] = 1;
        bigLC[37] = 1;
        bigOC[37] = 0;
        bigOS[37] = 0;
        /* Set 38 */
        little_a[38] = 0;
        little_b[38] = 0;
        little_c[38] = 0;
        little_d[38] = 1;
        little_e[38] = 0;
        little_f[38] = -8;
        little_g[38] = 13;
        little_h[38] = 0;
        little_i[38] = 0;
        little_j[38] = 0;
        bigLS[38] = -1;
        bigLC[38] = 1;
        bigOC[38] = 0;
        bigOS[38] = 0;
        /* Set 39 */
        little_a[39] = 0;
        little_b[39] = 0;
        little_c[39] = 0;
        little_d[39] = 0;
        little_e[39] = 0;
        little_f[39] = 8;
        little_g[39] = -11;
        little_h[39] = 0;
        little_i[39] = 0;
        little_j[39] = 0;
        bigLS[39] = -1;
        bigLC[39] = -1;
        bigOC[39] = 0;
        bigOS[39] = 0;
        /* Set 40 */
        little_a[40] = 1;
        little_b[40] = 0;
        little_c[40] = 0;
        little_d[40] = -1;
        little_e[40] = 0;
        little_f[40] = -10;
        little_g[40] = 3;
        little_h[40] = 0;
        little_i[40] = 0;
        little_j[40] = 0;
        bigLS[40] = 0;
        bigLC[40] = -1;
        bigOC[40] = 0;
        bigOS[40] = 0;
        /* Set 41 */
        little_a[41] = 1;
        little_b[41] = 0;
        little_c[41] = 0;
        little_d[41] = 1;
        little_e[41] = 0;
        little_f[41] = -10;
        little_g[41] = 3;
        little_h[41] = 0;
        little_i[41] = 0;
        little_j[41] = 0;
        bigLS[41] = 0;
        bigLC[41] = -1;
        bigOC[41] = 0;
        bigOS[41] = 0;
        /* Set 42 */
        little_a[42] = 1;
        little_b[42] = 0;
        little_c[42] = 0;
        little_d[42] = -1;
        little_e[42] = 0;
        little_f[42] = -18;
        little_g[42] = 16;
        little_h[42] = 0;
        little_i[42] = 0;
        little_j[42] = 0;
        bigLS[42] = 1;
        bigLC[42] = 0;
        bigOC[42] = 0;
        bigOS[42] = 0;
        /* Set 43 */
        little_a[43] = 1;
        little_b[43] = 0;
        little_c[43] = 0;
        little_d[43] = 1;
        little_e[43] = 0;
        little_f[43] = -18;
        little_g[43] = 16;
        little_h[43] = 0;
        little_i[43] = 0;
        little_j[43] = 0;
        bigLS[43] = 1;
        bigLC[43] = 0;
        bigOC[43] = 0;
        bigOS[43] = 0;
        /* Set 44 */
        little_a[44] = 1;
        little_b[44] = -2;
        little_c[44] = 0;
        little_d[44] = -2;
        little_e[44] = 0;
        little_f[44] = -18;
        little_g[44] = 16;
        little_h[44] = 0;
        little_i[44] = 0;
        little_j[44] = 0;
        bigLS[44] = 1;
        bigLC[44] = -1;
        bigOC[44] = 0;
        bigOS[44] = 1;
        /* Set 45 */
        little_a[45] = 1;
        little_b[45] = 2;
        little_c[45] = 0;
        little_d[45] = 2;
        little_e[45] = 0;
        little_f[45] = -18;
        little_g[45] = 16;
        little_h[45] = 0;
        little_i[45] = 0;
        little_j[45] = 0;
        bigLS[45] = 1;
        bigLC[45] = -1;
        bigOC[45] = 0;
        bigOS[45] = -1;
        /* Set 46 */
        little_a[46] = 1;
        little_b[46] = 0;
        little_c[46] = -2;
        little_d[46] = 1;
        little_e[46] = 0;
        little_f[46] = 20;
        little_g[46] = -21;
        little_h[46] = 0;
        little_i[46] = 0;
        little_j[46] = 0;
        bigLS[46] = 0;
        bigLC[46] = 1;
        bigOC[46] = 1;
        bigOS[46] = 0;
        /* Set 47 */
        little_a[47] = 0;
        little_b[47] = 0;
        little_c[47] = 0;
        little_d[47] = 0;
        little_e[47] = 0;
        little_f[47] = 0;
        little_g[47] = 1;
        little_h[47] = -1;
        little_i[47] = 0;
        little_j[47] = 0;
        bigLS[47] = -1;
        bigLC[47] = 0;
        bigOC[47] = 0;
        bigOS[47] = 0;
        /* Set 48 */
        little_a[48] = 0;
        little_b[48] = 0;
        little_c[48] = 0;
        little_d[48] = 0;
        little_e[48] = 0;
        little_f[48] = 0;
        little_g[48] = 1;
        little_h[48] = 2;
        little_i[48] = 0;
        little_j[48] = 0;
        bigLS[48] = -1;
        bigLC[48] = -1;
        bigOC[48] = 0;
        bigOS[48] = 0;
        /* Set 49 */
        little_a[49] = 0;
        little_b[49] = 0;
        little_c[49] = 0;
        little_d[49] = 0;
        little_e[49] = 0;
        little_f[49] = 0;
        little_g[49] = 1;
        little_h[49] = -2;
        little_i[49] = 0;
        little_j[49] = 0;
        bigLS[49] = -5;
        bigLC[49] = 3;
        bigOC[49] = 0;
        bigOS[49] = 0;
        /* Set 50 */
        little_a[50] = 0;
        little_b[50] = 0;
        little_c[50] = 0;
        little_d[50] = 1;
        little_e[50] = 0;
        little_f[50] = 0;
        little_g[50] = -1;
        little_h[50] = 2;
        little_i[50] = 0;
        little_j[50] = 0;
        bigLS[50] = -6;
        bigLC[50] = -6;
        bigOC[50] = -3;
        bigOS[50] = 3;
        /* Set 51 */
        little_a[51] = 0;
        little_b[51] = 0;
        little_c[51] = 0;
        little_d[51] = 0;
        little_e[51] = 0;
        little_f[51] = 0;
        little_g[51] = 0;
        little_h[51] = 2;
        little_i[51] = 0;
        little_j[51] = 0;
        bigLS[51] = -1;
        bigLC[51] = 0;
        bigOC[51] = 0;
        bigOS[51] = 0;
        /* Set 52 */
        little_a[52] = 0;
        little_b[52] = 0;
        little_c[52] = 0;
        little_d[52] = 0;
        little_e[52] = 0;
        little_f[52] = 0;
        little_g[52] = 2;
        little_h[52] = -2;
        little_i[52] = 0;
        little_j[52] = 0;
        bigLS[52] = -4;
        bigLC[52] = 0;
        bigOC[52] = 0;
        bigOS[52] = 0;
        /* Set 53 */
        little_a[53] = 0;
        little_b[53] = 0;
        little_c[53] = 0;
        little_d[53] = 0;
        little_e[53] = 0;
        little_f[53] = 0;
        little_g[53] = 3;
        little_h[53] = -2;
        little_i[53] = 0;
        little_j[53] = 0;
        bigLS[53] = 1;
        bigLC[53] = -1;
        bigOC[53] = 0;
        bigOS[53] = 0;
        /* Set 54 */
        little_a[54] = 0;
        little_b[54] = 0;
        little_c[54] = 0;
        little_d[54] = 0;
        little_e[54] = 0;
        little_f[54] = 0;
        little_g[54] = 4;
        little_h[54] = -2;
        little_i[54] = 0;
        little_j[54] = 0;
        bigLS[54] = 1;
        bigLC[54] = 0;
        bigOC[54] = 0;
        bigOS[54] = -1;
        /* Set 55 */
        little_a[55] = 0;
        little_b[55] = 0;
        little_c[55] = 0;
        little_d[55] = 0;
        little_e[55] = 0;
        little_f[55] = 0;
        little_g[55] = 2;
        little_h[55] = -3;
        little_i[55] = 0;
        little_j[55] = 0;
        bigLS[55] = -1;
        bigLC[55] = 0;
        bigOC[55] = 0;
        bigOS[55] = 0;
        /* Set 56 */
        little_a[56] = 0;
        little_b[56] = 0;
        little_c[56] = 0;
        little_d[56] = 0;
        little_e[56] = 0;
        little_f[56] = 0;
        little_g[56] = 2;
        little_h[56] = -4;
        little_i[56] = 0;
        little_j[56] = 0;
        bigLS[56] = 0;
        bigLC[56] = 1;
        bigOC[56] = 0;
        bigOS[56] = 0;
        /* Set 57 */
        little_a[57] = 0;
        little_b[57] = 0;
        little_c[57] = 0;
        little_d[57] = 0;
        little_e[57] = 0;
        little_f[57] = 0;
        little_g[57] = 3;
        little_h[57] = -4;
        little_i[57] = 0;
        little_j[57] = 0;
        bigLS[57] = -1;
        bigLC[57] = 1;
        bigOC[57] = 0;
        bigOS[57] = 0;
        /* Set 58 */
        little_a[58] = 0;
        little_b[58] = 0;
        little_c[58] = 0;
        little_d[58] = 0;
        little_e[58] = 0;
        little_f[58] = 0;
        little_g[58] = 2;
        little_h[58] = -8;
        little_i[58] = 3;
        little_j[58] = 0;
        bigLS[58] = 1;
        bigLC[58] = -4;
        bigOC[58] = 2;
        bigOS[58] = 0;
        /* Set 59 */
        little_a[59] = 0;
        little_b[59] = 0;
        little_c[59] = 0;
        little_d[59] = 1;
        little_e[59] = 0;
        little_f[59] = 0;
        little_g[59] = -4;
        little_h[59] = 8;
        little_i[59] = -3;
        little_j[59] = 0;
        bigLS[59] = 1;
        bigLC[59] = 4;
        bigOC[59] = 2;
        bigOS[59] = -1;
        /* Set 60 */
        little_a[60] = 0;
        little_b[60] = 0;
        little_c[60] = 0;
        little_d[60] = 1;
        little_e[60] = 0;
        little_f[60] = 0;
        little_g[60] = 4;
        little_h[60] = -8;
        little_i[60] = 3;
        little_j[60] = 0;
        bigLS[60] = -1;
        bigLC[60] = 3;
        bigOC[60] = 2;
        bigOS[60] = 1;
        /* Set 61 */
        little_a[61] = 0;
        little_b[61] = 0;
        little_c[61] = 0;
        little_d[61] = 0;
        little_e[61] = 0;
        little_f[61] = 0;
        little_g[61] = 6;
        little_h[61] = -8;
        little_i[61] = 3;
        little_j[61] = 0;
        bigLS[61] = 1;
        bigLC[61] = -4;
        bigOC[61] = -2;
        bigOS[61] = -1;
        /* Set 62 */
        little_a[62] = 0;
        little_b[62] = 0;
        little_c[62] = 0;
        little_d[62] = 0;
        little_e[62] = 0;
        little_f[62] = 0;
        little_g[62] = 1;
        little_h[62] = 0;
        little_i[62] = -3;
        little_j[62] = 0;
        bigLS[62] = 1;
        bigLC[62] = 0;
        bigOC[62] = 0;
        bigOS[62] = 0;
        /* Set 63 */
        little_a[63] = 0;
        little_b[63] = 0;
        little_c[63] = 0;
        little_d[63] = 0;
        little_e[63] = 0;
        little_f[63] = 0;
        little_g[63] = 2;
        little_h[63] = 0;
        little_i[63] = -3;
        little_j[63] = 0;
        bigLS[63] = 1;
        bigLC[63] = 0;
        bigOC[63] = 0;
        bigOS[63] = 0;
        /* Set 64 */
        little_a[64] = 2;
        little_b[64] = 0;
        little_c[64] = -2;
        little_d[64] = -1;
        little_e[64] = 0;
        little_f[64] = 0;
        little_g[64] = -2;
        little_h[64] = 0;
        little_i[64] = 3;
        little_j[64] = 0;
        bigLS[64] = 1;
        bigLC[64] = 0;
        bigOC[64] = 0;
        bigOS[64] = 1;
        /* Set 65 */
        little_a[65] = 2;
        little_b[65] = 0;
        little_c[65] = -2;
        little_d[65] = 1;
        little_e[65] = 0;
        little_f[65] = 0;
        little_g[65] = -2;
        little_h[65] = 0;
        little_i[65] = 3;
        little_j[65] = 0;
        bigLS[65] = 2;
        bigLC[65] = 0;
        bigOC[65] = 0;
        bigOS[65] = -1;
        /* Set 66 */
        little_a[66] = 2;
        little_b[66] = 0;
        little_c[66] = -2;
        little_d[66] = 0;
        little_e[66] = 0;
        little_f[66] = 0;
        little_g[66] = -2;
        little_h[66] = 0;
        little_i[66] = 3;
        little_j[66] = 0;
        bigLS[66] = 4;
        bigLC[66] = 0;
        bigOC[66] = 0;
        bigOS[66] = 0;
        /* Set 67 */
        little_a[67] = 0;
        little_b[67] = 0;
        little_c[67] = 0;
        little_d[67] = 0;
        little_e[67] = 0;
        little_f[67] = 0;
        little_g[67] = 1;
        little_h[67] = 0;
        little_i[67] = -2;
        little_j[67] = 0;
        bigLS[67] = -2;
        bigLC[67] = 3;
        bigOC[67] = 0;
        bigOS[67] = 0;
        /* Set 68 */
        little_a[68] = 0;
        little_b[68] = 0;
        little_c[68] = 0;
        little_d[68] = 0;
        little_e[68] = 0;
        little_f[68] = 0;
        little_g[68] = 2;
        little_h[68] = 0;
        little_i[68] = -2;
        little_j[68] = 0;
        bigLS[68] = 4;
        bigLC[68] = 0;
        bigOC[68] = 0;
        bigOS[68] = 0;
        /* Set 69 */
        little_a[69] = 2;
        little_b[69] = 0;
        little_c[69] = -2;
        little_d[69] = -1;
        little_e[69] = 0;
        little_f[69] = 0;
        little_g[69] = -2;
        little_h[69] = 0;
        little_i[69] = 2;
        little_j[69] = 0;
        bigLS[69] = -5;
        bigLC[69] = 0;
        bigOC[69] = 0;
        bigOS[69] = -3;
        /* Set 70 */
        little_a[70] = 2;
        little_b[70] = 0;
        little_c[70] = -2;
        little_d[70] = 1;
        little_e[70] = 0;
        little_f[70] = 0;
        little_g[70] = -2;
        little_h[70] = 0;
        little_i[70] = 2;
        little_j[70] = 0;
        bigLS[70] = 1;
        bigLC[70] = 0;
        bigOC[70] = 0;
        bigOS[70] = 0;
        /* Set 71 */
        little_a[71] = 0;
        little_b[71] = 2;
        little_c[71] = -2;
        little_d[71] = 1;
        little_e[71] = 0;
        little_f[71] = 0;
        little_g[71] = -2;
        little_h[71] = 0;
        little_i[71] = 2;
        little_j[71] = 0;
        bigLS[71] = 1;
        bigLC[71] = 0;
        bigOC[71] = 0;
        bigOS[71] = 0;
        /* Set 72 */
        little_a[72] = 2;
        little_b[72] = 0;
        little_c[72] = -2;
        little_d[72] = 0;
        little_e[72] = 0;
        little_f[72] = 0;
        little_g[72] = -2;
        little_h[72] = 0;
        little_i[72] = 2;
        little_j[72] = 0;
        bigLS[72] = 4;
        bigLC[72] = 0;
        bigOC[72] = 0;
        bigOS[72] = 0;
        /* Set 73 */
        little_a[73] = 0;
        little_b[73] = 0;
        little_c[73] = 0;
        little_d[73] = 0;
        little_e[73] = 0;
        little_f[73] = 0;
        little_g[73] = 3;
        little_h[73] = 0;
        little_i[73] = -2;
        little_j[73] = 0;
        bigLS[73] = 1;
        bigLC[73] = -1;
        bigOC[73] = 0;
        bigOS[73] = 0;
        /* Set 74 */
        little_a[74] = 0;
        little_b[74] = 0;
        little_c[74] = 0;
        little_d[74] = 0;
        little_e[74] = 0;
        little_f[74] = 0;
        little_g[74] = 4;
        little_h[74] = 0;
        little_i[74] = -2;
        little_j[74] = 0;
        bigLS[74] = -2;
        bigLC[74] = 0;
        bigOC[74] = 0;
        bigOS[74] = 1;
        /* Set 75 */
        little_a[75] = 0;
        little_b[75] = 0;
        little_c[75] = 0;
        little_d[75] = 0;
        little_e[75] = 0;
        little_f[75] = 0;
        little_g[75] = 1;
        little_h[75] = 0;
        little_i[75] = -1;
        little_j[75] = 0;
        bigLS[75] = -16;
        bigLC[75] = 0;
        bigOC[75] = 0;
        bigOS[75] = 0;
        /* Set 76 */
        little_a[76] = 0;
        little_b[76] = 0;
        little_c[76] = 0;
        little_d[76] = 0;
        little_e[76] = 0;
        little_f[76] = 0;
        little_g[76] = 2;
        little_h[76] = 0;
        little_i[76] = -1;
        little_j[76] = 0;
        bigLS[76] = -2;
        bigLC[76] = 0;
        bigOC[76] = 0;
        bigOS[76] = 1;
        /* Set 77 */
        little_a[77] = 0;
        little_b[77] = 0;
        little_c[77] = 0;
        little_d[77] = 0;
        little_e[77] = 0;
        little_f[77] = 0;
        little_g[77] = 3;
        little_h[77] = 0;
        little_i[77] = -1;
        little_j[77] = 0;
        bigLS[77] = 5;
        bigLC[77] = 1;
        bigOC[77] = 0;
        bigOS[77] = -2;
        /* Set 78 */
        little_a[78] = 0;
        little_b[78] = 0;
        little_c[78] = 0;
        little_d[78] = 0;
        little_e[78] = 0;
        little_f[78] = 0;
        little_g[78] = 0;
        little_h[78] = 0;
        little_i[78] = 1;
        little_j[78] = 0;
        bigLS[78] = -1;
        bigLC[78] = -1;
        bigOC[78] = -1;
        bigOS[78] = 0;
        /* Set 79 */
        little_a[79] = 0;
        little_b[79] = 0;
        little_c[79] = 0;
        little_d[79] = 1;
        little_e[79] = 0;
        little_f[79] = 0;
        little_g[79] = 0;
        little_h[79] = 0;
        little_i[79] = 1;
        little_j[79] = 0;
        bigLS[79] = 0;
        bigLC[79] = -1;
        bigOC[79] = 0;
        bigOS[79] = 0;
        /* Set 80 */
        little_a[80] = 0;
        little_b[80] = 0;
        little_c[80] = 0;
        little_d[80] = 0;
        little_e[80] = 0;
        little_f[80] = 0;
        little_g[80] = 1;
        little_h[80] = 0;
        little_i[80] = 1;
        little_j[80] = 0;
        bigLS[80] = -1;
        bigLC[80] = 0;
        bigOC[80] = 0;
        bigOS[80] = 1;
        /* Set 81 */
        little_a[81] = 0;
        little_b[81] = 0;
        little_c[81] = 0;
        little_d[81] = 0;
        little_e[81] = 0;
        little_f[81] = 0;
        little_g[81] = 2;
        little_h[81] = 0;
        little_i[81] = 1;
        little_j[81] = 0;
        bigLS[81] = 2;
        bigLC[81] = 0;
        bigOC[81] = 0;
        bigOS[81] = -1;
        /* Set 82 */
        little_a[82] = 0;
        little_b[82] = 0;
        little_c[82] = 0;
        little_d[82] = 0;
        little_e[82] = 0;
        little_f[82] = 0;
        little_g[82] = 0;
        little_h[82] = 0;
        little_i[82] = 2;
        little_j[82] = 0;
        bigLS[82] = -12;
        bigLC[82] = 2;
        bigOC[82] = 1;
        bigOS[82] = 4;
        /* Set 83 */
        little_a[83] = 0;
        little_b[83] = 0;
        little_c[83] = 0;
        little_d[83] = 0;
        little_e[83] = 0;
        little_f[83] = 0;
        little_g[83] = 1;
        little_h[83] = 0;
        little_i[83] = 2;
        little_j[83] = 0;
        bigLS[83] = 0;
        bigLC[83] = -1;
        bigOC[83] = 0;
        bigOS[83] = 0;
        /* Set 84 */
        little_a[84] = 0;
        little_b[84] = 0;
        little_c[84] = 0;
        little_d[84] = 0;
        little_e[84] = 0;
        little_f[84] = 0;
        little_g[84] = 0;
        little_h[84] = 0;
        little_i[84] = 3;
        little_j[84] = 0;
        bigLS[84] = -1;
        bigLC[84] = 0;
        bigOC[84] = 0;
        bigOS[84] = 0;
        /* Set 85 */
        little_a[85] = 0;
        little_b[85] = 0;
        little_c[85] = 0;
        little_d[85] = 0;
        little_e[85] = 0;
        little_f[85] = 0;
        little_g[85] = 1;
        little_h[85] = 0;
        little_i[85] = 0;
        little_j[85] = -1;
        bigLS[85] = -1;
        bigLC[85] = 0;
        bigOC[85] = 0;
        bigOS[85] = 0;

        /* Calculate the planetary terms in nutation from the series */
        for (j = 1; j <= 85; j++) {
            bigA = little_a[j] * l + little_b[j] * F + little_c[j] * bigD + little_d[j] * omega + little_e[j] * bigQ + little_f[j] * bigV + little_g[j] * bigE + little_h[j] * bigM + little_i[j] * bigJ + little_j[j] * S;
            delta_psi_p = delta_psi_p + (bigLS[j] * 1E-05 * Math.sin(bigA) / 3600 + bigLC[j] * 1E-05 * Math.cos(bigA) / 3600) * Math.PI / 180;
            delta_epsilon_p = delta_epsilon_p + (bigOC[j] * 1E-05 * Math.cos(bigA) / 3600 + bigOS[j] * 1E-05 * Math.sin(bigA) / 3600) * Math.PI / 180;
        }

        /* Calculate the nutations in longitude and obliquity */
        delta_psi = delta_psi + delta_psi_c + delta_psi_p;
        delta_epsilon = delta_epsilon + delta_epsilon_c + delta_epsilon_p;

        /* Store the nutations and mean obliquity in a vector for return */
        nutation_vector[1] = delta_psi;
        nutation_vector[2] = delta_epsilon;
        nutation_vector[3] = mean_obliquity;

    }

    /*
    Procedure to invert a dimension_n-by-dimension_n matrix (matrix_nxn[][]) using Gaussian elimination.
    Algorithm taken from Richard L. Burden and J. Douglas Faires, "Numerical Analysis", 3rd edition, p. 300.
    NOTE:  It is assumed that the matrix is invertible!!

    Tested and verified 17 Oct 2000.
  */
    private static void invertNxn(int dimension_n, double matrix_nxn[][]) {
        int k = 0, kk = 0, kkk = 0, p = 0;
        double[][] inter = new double[9][17];
        double hold = 0, normalizer = 0;

        for (k = 1; k <= dimension_n; k++) {
            for (kk = 1; kk <= dimension_n; kk++) {
                inter[k][kk] = matrix_nxn[k][kk];
                inter[k][kk + dimension_n] = 0;
            }
            inter[k][k + dimension_n] = 1;
        }

        for (k = 1; k <= (dimension_n - 1); k++) {
            p = 0;
            for (kk = k; kk <= dimension_n; kk++) {
                if ((p == 0) && (inter[kk][k] != 0))
                    p = kk;
            }
            if (p != k) {
                for (kk = 1; kk <= 2 * dimension_n; kk++) {
                    hold = inter[k][kk];
                    inter[k][kk] = inter[p][kk];
                    inter[p][kk] = hold;
                }
            }
            for (kk = k + 1; kk <= dimension_n; kk++) {
                hold = inter[kk][k] / inter[k][k];
                for (kkk = 1; kkk <= 2 * dimension_n; kkk++)
                    inter[kk][kkk] = inter[kk][kkk] - hold * inter[k][kkk];
            }
        }

        for (k = dimension_n; k >= 2; k--) {
            for (kk = k - 1; kk >= 1; kk--) {
                hold = inter[kk][k] / inter[k][k];
                for (kkk = 1; kkk <= 2 * dimension_n; kkk++)
                    inter[kk][kkk] = inter[kk][kkk] - hold * inter[k][kkk];
            }
        }

        for (k = 1; k <= dimension_n; k++) {
            normalizer = inter[k][k];
            for (kk = 1; kk <= 2 * dimension_n; kk++)
                inter[k][kk] = inter[k][kk] / normalizer;
        }

        for (k = 1; k <= dimension_n; k++) {
            for (kk = 1; kk <= dimension_n; kk++)
                matrix_nxn[k][kk] = inter[k][kk + dimension_n];
        }
    }

    /*
    Procedure to convert the mean coordinates (radec) of an object relative to a specified equinox (jdequinox_in) to the corresponding mean coordinates relative to another specified equinox (jdequinox_out).
    Procedure accomodates precession within either the FK4 or the FK5 system; however, it does not address converting between these systems.
    Note:  radec[] is an array, where radec[1] = right ascension, and radec[2] = declination.  Both ra and dec are in radians, the equinoxes are expressed as Julian dates, and FK5 is a Boolean.
    Algorithm taken from Jean Meeus, "Astronomical Algorithms", pp 126-130 and P. Kenneth Seidelmann, "Explanatory Supplement to the Astronomical Almanac", 1992, pg. 107.

    Tested and verified 25 May 1999.
  */
    public static void getPrecession(double radec[], double jdequinox_in, double jdequinox_out, boolean FK5) {

        double t0 = 0, t = 0, xi = 0, z = 0, theta = 0, t1 = 0, t2 = 0, tau = 0, a = 0, b = 0, c = 0, raz = 0;

        if (FK5 == true) {
            /*  Use FK5 precessional formulae from Meeus */
            t0 = (jdequinox_in - 2451545) / 36525;
            t = (jdequinox_out - jdequinox_in) / 36525;
            xi = (2306.2181 + 1.39656 * t0 - .000139 * Math.pow(t0, 2)) * t + (.30188 - .000344 * t0) * Math.pow(t, 2) + .017998 * Math.pow(t, 3);
            xi = ((xi / 3600) * Math.PI / 180);
            z = (2306.2181 + 1.39656 * t0 - .000139 * Math.pow(t0, 2)) * t + (1.09468 + .000066 * t0) * Math.pow(t, 2) + .018203 * Math.pow(t, 3);
            z = ((z / 3600) * Math.PI / 180);
            theta = (2004.3109 - .8533 * t0 - .000217 * Math.pow(t0, 2)) * t - (.42665 + .000217 * t0) * Math.pow(t, 2) - .041833 * Math.pow(t, 3);
            theta = ((theta / 3600) * Math.PI / 180);
        } else {
            /*  Use FK4 (Andoyer) precessional formula from Seidelmann */
            t1 = (jdequinox_in - 2396758.203) / 365242.198782;
            t2 = (jdequinox_out - 2396758.203) / 365242.198782;
            tau = t2 - t1;
            xi = (23035.545 + 139.720 * t1 + 0.060 * Math.pow(t1, 2)) * tau + (30.240 - 0.270 * t1) * Math.pow(tau, 2) + 17.995 * Math.pow(tau, 3);
            xi = ((xi / 3600) * Math.PI / 180);
            z = (23035.545 + 139.720 * t1 + 0.060 * Math.pow(t1, 2)) * tau + (109.480 + 0.390 * t1) * Math.pow(tau, 2) + 18.325 * Math.pow(tau, 3);
            z = ((z / 3600) * Math.PI / 180);
            theta = (20051.12 - 85.29 * t1 - 0.37 * Math.pow(t1, 2)) * tau + (-42.65 - 0.37 * t1) * Math.pow(tau, 2) - 41.80 * Math.pow(tau, 3);
            theta = ((theta / 3600) * Math.PI / 180);
        }

        a = Math.cos(radec[2]) * Math.sin(radec[1] + xi);
        b = Math.cos(theta) * Math.cos(radec[2]) * Math.cos(radec[1] + xi) - Math.sin(theta) * Math.sin(radec[2]);
        c = Math.sin(theta) * Math.cos(radec[2]) * Math.cos(radec[1] + xi) + Math.cos(theta) * Math.sin(radec[2]);
        raz = Math.atan2(a, b);
        radec[1] = (raz + z);

        if (radec[1] < 0)
            radec[1] = radec[1] + 2 * Math.PI;

        if (radec[1] >= 2 * Math.PI)
            radec[1] = radec[1] - 2 * Math.PI;

        radec[2] = Math.atan(c / Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2)));

    }

    /*
         Returns the mean sidereal time (in radians) corresponding to the Julian TDT date specified, at the specified east longitude (given in radians), and referred to the equinox of date.  Since sidereal time is related to Universal Time (UT1), the input TDT time is first converted to UT1 by subtracting delta_t, which is defined as TDT - UT1.
         Note that tdt_time is given as a Julian date, delta_t is given in units of days (each lasting 86,400 seconds), and longitude is given in radians, measured eastward from Greenwich.
         Algorithm taken from "Astronomical Almanac, 1993", p. B6, and Jean Meeus, "Astronomical Algorithms, pg. 83.
         Tested and verified 5-23-99.
       */
    public static double getMeanSiderealTime(double tdt_time, double delta_t, double longitude) {

        double ut_time = 0, t = 0, sid_time = 0;

        ut_time = tdt_time - delta_t;
        t = ((Math.floor(ut_time - 0.5) + 0.5) - 2451545) / 36525;
        sid_time = 24110.54841 + 8640184.812866 * t + 0.093104 * Math.pow(t, 2) - .0000062 * Math.pow(t, 3);
        sid_time = sid_time / 3600;
        sid_time = sid_time + 1.00273790935 * 24 * (ut_time - (Math.floor(ut_time - 0.5) + 0.5));
        sid_time = sid_time + (longitude * 180 / Math.PI) / 15;

        while (sid_time < 0)
            sid_time = sid_time + 24;

        while (sid_time >= 24)
            sid_time = sid_time - 24;

        sid_time = sid_time * 15 * Math.PI / 180;

        return sid_time;

    }

    /*
     Method returns the calendar date of a given Julian date (jultime).
     Algorithm taken from Jean Meeus, "Astronomical Algorithms", p 63.

     The date is returned in an array:
       date[1] = year
       date[2] = month
       date[3] = day
       date[4] = hour
       date[5] = minute
       date[6] = second

     Tested and certified 23 May 1999.
   */
    public static double[] getCalendarFields(double jultime) {

        double z = 0, f = 0, a = 0, alpha = 0, b = 0, c = 0, d = 0, e = 0;
        double[] date = new double[7];

        jultime = jultime + 0.5;
        z = Math.floor(jultime);
        f = jultime - z;

        if (z < 2299161)
            a = z;
        else {
            alpha = Math.floor((z - 1867216.25) / 36524.25);
            a = z + 1 + alpha - Math.floor(alpha / 4);
        }

        b = a + 1524;
        c = Math.floor((b - 122.1) / 365.25);
        d = Math.floor(365.25 * c);
        e = Math.floor((b - d) / 30.6001);

        if (e < 14)
            date[2] = e - 1;
        else
            date[2] = e - 13;

        if (date[2] > 2)
            date[1] = c - 4716;
        else
            date[1] = c - 4715;

        date[3] = b - d - Math.floor(30.6001 * e) + f;
        date[4] = (date[3] - Math.floor(date[3])) * 24;
        date[3] = Math.floor(date[3]);
        date[5] = (date[4] - Math.floor(date[4])) * 60;
        date[4] = Math.floor(date[4]);
        date[6] = (date[5] - Math.floor(date[5])) * 60;
        date[5] = Math.floor(date[5]);
        jultime = jultime - 0.5;

        return date;

    }

     //we are dropping the millisecond accuracy here.
    public static GregorianCalendar getCalendarFieldsAsDate(double jultime) {

        double[] date;

        date = getCalendarFields(jultime);

          return new GregorianCalendar((int)date[1], (int)date[2], (int)date[3], (int)date[4], (int)date[5], (int)date[6]);

    }

    /*
     Method returns the Julian date of a given Julian or Gregorian calendar date.
     Algorithm taken from Jean Meeus, "Astronomical Algorithms", pp. 60-61.

    Tested and certified 23 May 1999.
   */
    public static double getJulianDay(boolean Gregorian, int year, int month, int day, int hour, int minute, double second) {

        double a = 0, b = 0, d = 0, julianDate = 0;

        d = day + (hour + (minute + second / 60) / 60) / 24;

        if (month == 1 || month == 2) {
            year = year - 1;
            month = month + 12;
        }

        if (Gregorian) {
            a = Math.floor(year / 100);
            b = 2 - a + Math.floor(a / 4);
        } else
            b = 0;

        julianDate = Math.floor(365.25 * (year + 4716)) + Math.floor(30.6001 * (month + 1)) + d + b - 1524.5;

        /*  Correct adjustment made to year and month above  */
        if (month > 12) {
            month = month - 12;
            year = year + 1;
        }

        return julianDate;

    }

    public static double getJulianDay(GregorianCalendar calendar) {

        return getJulianDay(true, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND)+calendar.get(Calendar.MILLISECOND)/1000);

    }

    /*
    This procedure returns the Julian day (jdequinox) of the standard equinox (standard_equinox, assumed to be a year, e.g. 1950.0) to whose mean equinox and ecliptic a set of coordinates or orbital elements refers.
    The variable "Julian" is a boolean, and tells whether the equinox refers to Julian or Besselian years.
    Values taken from P. Kenneth Seidelmann, "Explanatory Supplement to the Astronomical Almanac", 1992, pp. 167, 699.
   Testing completed 0426 5-22-99
  */
    public static double getJulianEquinox(double standard_equinox, boolean julian) {

        double jdequinox = 0;

        if (julian)
            jdequinox = 2451545.0 + (standard_equinox - 2000) * 365.25;
        else
            jdequinox = 2433282.42345905 + (standard_equinox - 1950) * 365.242198782;

        return jdequinox;

    }

    /*
    This utility routine is intended as a look-up table to determine the number of leap seconds in effect at the UTC Julian date jultime.
    The intent will be to use this routine when observations are processed; if the number of leap seconds is not provided (e.g., mpec), this routine would be called to provide the data.
    Algorithm adapted from "Relationship between TAI and UTC", International Earth Rotation Service website http://hpiers.obspm.fr/webiers/general/earthor/utc/table1.html .
    */
    public static double getLeapSecond(double jultime) {

        double MJD = 0, answer = 0;

        MJD = jultime - 2400000.5;

        if ((jultime >= 2339049.5) && (jultime <= 2344893.5))  // 1692-1707
            answer = -32.184 + 9;

        else if ((jultime >= 2344893.5) && (jultime <= 2348181.5))  // 1708-16
            answer = -32.184 + 10;

        else if ((jultime >= 2348181.5) && (jultime <= 2354390.5))  // 1717-33
            answer = -32.184 + 11;

        else if ((jultime >= 2354390.5) && (jultime <= 2358042.5))  // 1734-43
            answer = -32.184 + 12;

        else if ((jultime >= 2358042.5) && (jultime <= 2360599.5))  // 1744-50
            answer = -32.184 + 13;

        else if ((jultime >= 2360599.5) && (jultime <= 2363156.5))  // 1751-57
            answer = -32.184 + 14;

        else if ((jultime >= 2363156.5) && (jultime <= 2365713.5))  // 1758-64
            answer = -32.184 + 15;

        else if ((jultime >= 2365713.5) && (jultime <= 2369365.5))  // 1765-74
            answer = -32.184 + 16;

        else if ((jultime >= 2369365.5) && (jultime <= 2375574.5))  // 1775-91
            answer = -32.184 + 17;

        else if ((jultime >= 2375574.5) && (jultime <= 2377035.5))  // 1792-5
            answer = -32.184 + 16;

        else if ((jultime >= 2377035.5) && (jultime <= 2377766.5))  // 1796-7
            answer = -32.184 + 15;

        else if ((jultime >= 2377766.5) && (jultime <= 2378496.5))  // 1798-9
            answer = -32.184 + 14;

        else if ((jultime >= 2378496.5) && (jultime <= 2378861.5))  // 1800
            answer = -32.184 + 13.7;

        else if ((jultime >= 2378861.5) && (jultime <= 2379226.5))  // 1801
            answer = -32.184 + 13.4;

        else if ((jultime >= 2379226.5) && (jultime <= 2379591.5))  // 1802
            answer = -32.184 + 13.1;

        else if ((jultime >= 2379591.5) && (jultime <= 2379956.5))  // 1803
            answer = -32.184 + 12.9;

        else if ((jultime >= 2379956.5) && (jultime <= 2380322.5))  // 1804
            answer = -32.184 + 12.7;

        else if ((jultime >= 2380322.5) && (jultime <= 2380687.5))  // 1805
            answer = -32.184 + 12.6;

        else if ((jultime >= 2380687.5) && (jultime <= 2384705.5))  // 1806-16
            answer = -32.184 + 12.5;

        else if ((jultime >= 2384705.5) && (jultime <= 2385070.5))  // 1817
            answer = -32.184 + 12.4;

        else if ((jultime >= 2385070.5) && (jultime <= 2385435.5))  // 1818
            answer = -32.184 + 12.3;

        else if ((jultime >= 2385435.5) && (jultime <= 2385800.5))  // 1819
            answer = -32.184 + 12.2;

        else if ((jultime >= 2385800.5) && (jultime <= 2386166.5))  // 1820
            answer = -32.184 + 12.0;

        else if ((jultime >= 2386166.5) && (jultime <= 2386531.5))  // 1821
            answer = -32.184 + 11.7;

        else if ((jultime >= 2386531.5) && (jultime <= 2386896.5))  // 1822
            answer = -32.184 + 11.4;

        else if ((jultime >= 2386896.5) && (jultime <= 2387261.5))  // 1823
            answer = -32.184 + 11.1;

        else if ((jultime >= 2387261.5) && (jultime <= 2387627.5))  // 1824
            answer = -32.184 + 10.6;

        else if ((jultime >= 2387627.5) && (jultime <= 2387992.5))  // 1825
            answer = -32.184 + 10.2;

        else if ((jultime >= 2387992.5) && (jultime <= 2388357.5))  // 1826
            answer = -32.184 + 9.6;

        else if ((jultime >= 2388357.5) && (jultime <= 2388722.5))  // 1827
            answer = -32.184 + 9.1;

        else if ((jultime >= 2388722.5) && (jultime <= 2389088.5))  // 1828
            answer = -32.184 + 8.6;

        else if ((jultime >= 2389088.5) && (jultime <= 2389453.5))  // 1829
            answer = -32.184 + 8.0;

        else if ((jultime >= 2389453.5) && (jultime <= 2389818.5))  // 1830
            answer = -32.184 + 7.5;

        else if ((jultime >= 2389818.5) && (jultime <= 2390183.5))  // 1831
            answer = -32.184 + 7.0;

        else if ((jultime >= 2390183.5) && (jultime <= 2390549.5))  // 1832
            answer = -32.184 + 6.6;

        else if ((jultime >= 2390549.5) && (jultime <= 2390914.5))  // 1833
            answer = -32.184 + 6.3;

        else if ((jultime >= 2390914.5) && (jultime <= 2391279.5))  // 1834
            answer = -32.184 + 6.0;

        else if ((jultime >= 2391279.5) && (jultime <= 2391644.5))  // 1835
            answer = -32.184 + 5.8;

        else if ((jultime >= 2391644.5) && (jultime <= 2392010.5))  // 1836
            answer = -32.184 + 5.7;

        else if ((jultime >= 2392010.5) && (jultime <= 2393105.5))  // 1837-9
            answer = -32.184 + 5.6;

        else if ((jultime >= 2393105.5) && (jultime <= 2393471.5))  // 1840
            answer = -32.184 + 5.7;

        else if ((jultime >= 2393471.5) && (jultime <= 2393836.5))  // 1841
            answer = -32.184 + 5.8;

        else if ((jultime >= 2393836.5) && (jultime <= 2394201.5))  // 1842
            answer = -32.184 + 5.9;

        else if ((jultime >= 2394201.5) && (jultime <= 2394566.5))  // 1843
            answer = -32.184 + 6.1;

        else if ((jultime >= 2394566.5) && (jultime <= 2394932.5))  // 1844
            answer = -32.184 + 6.2;

        else if ((jultime >= 2394932.5) && (jultime <= 2395297.5))  // 1845
            answer = -32.184 + 6.3;

        else if ((jultime >= 2395297.5) && (jultime <= 2395662.5))  // 1846
            answer = -32.184 + 6.5;

        else if ((jultime >= 2395662.5) && (jultime <= 2396027.5))  // 1847
            answer = -32.184 + 6.6;

        else if ((jultime >= 2396027.5) && (jultime <= 2396393.5))  // 1848
            answer = -32.184 + 6.8;

        else if ((jultime >= 2396393.5) && (jultime <= 2396758.5))  // 1849
            answer = -32.184 + 6.9;

        else if ((jultime >= 2396758.5) && (jultime <= 2397123.5))  // 1850
            answer = -32.184 + 7.1;

        else if ((jultime >= 2397123.5) && (jultime <= 2397488.5))  // 1851
            answer = -32.184 + 7.2;

        else if ((jultime >= 2397488.5) && (jultime <= 2397854.5))  // 1852
            answer = -32.184 + 7.3;

        else if ((jultime >= 2397854.5) && (jultime <= 2398219.5))  // 1853
            answer = -32.184 + 7.4;

        else if ((jultime >= 2398219.5) && (jultime <= 2398584.5))  // 1854
            answer = -32.184 + 7.5;

        else if ((jultime >= 2398584.5) && (jultime <= 2398949.5))  // 1855
            answer = -32.184 + 7.6;

        else if ((jultime >= 2398949.5) && (jultime <= 2399680.5))  // 1856-7
            answer = -32.184 + 7.7;

        else if ((jultime >= 2399680.5) && (jultime <= 2400410.5))  // 1858-9
            answer = -32.184 + 7.8;

        else if ((jultime >= 2400410.5) && (jultime <= 2400776.5))  // 1860
            answer = -32.184 + 7.88;

        else if ((jultime >= 2400776.5) && (jultime <= 2401141.5))  // 1861
            answer = -32.184 + 7.82;

        else if ((jultime >= 2401141.5) && (jultime <= 2401506.5))  // 1862
            answer = -32.184 + 7.54;

        else if ((jultime >= 2401506.5) && (jultime <= 2401871.5))  // 1863
            answer = -32.184 + 6.97;

        else if ((jultime >= 2401871.5) && (jultime <= 2402237.5))  // 1864
            answer = -32.184 + 6.40;

        else if ((jultime >= 2402237.5) && (jultime <= 2402602.5))  // 1865
            answer = -32.184 + 6.02;

        else if ((jultime >= 2402602.5) && (jultime <= 2402967.5))  // 1866
            answer = -32.184 + 5.41;

        else if ((jultime >= 2402967.5) && (jultime <= 2403332.5))  // 1867
            answer = -32.184 + 4.10;

        else if ((jultime >= 2403332.5) && (jultime <= 2403698.5))  // 1868
            answer = -32.184 + 2.92;

        else if ((jultime >= 2403698.5) && (jultime <= 2404063.5))  // 1869
            answer = -32.184 + 1.82;

        else if ((jultime >= 2404063.5) && (jultime <= 2404428.5))  // 1870
            answer = -32.184 + 1.61;

        else if ((jultime >= 2404428.5) && (jultime <= 2404793.5))  // 1871
            answer = -32.184 + 0.10;

        else if ((jultime >= 2404793.5) && (jultime <= 2405159.5))  // 1872
            answer = -32.184 - 1.02;

        else if ((jultime >= 2405159.5) && (jultime <= 2405524.5))  // 1873
            answer = -32.184 - 1.28;

        else if ((jultime >= 2405524.5) && (jultime <= 2405889.5))  // 1874
            answer = -32.184 - 2.69;

        else if ((jultime >= 2405889.5) && (jultime <= 2406254.5))  // 1875
            answer = -32.184 - 3.24;

        else if ((jultime >= 2406254.5) && (jultime <= 2406620.5))  // 1876
            answer = -32.184 - 3.64;

        else if ((jultime >= 2406620.5) && (jultime <= 2406985.5))  // 1877
            answer = -32.184 - 4.54;

        else if ((jultime >= 2406985.5) && (jultime <= 2407350.5))  // 1878
            answer = -32.184 - 4.71;

        else if ((jultime >= 2407350.5) && (jultime <= 2407715.5))  // 1879
            answer = -32.184 - 5.11;

        else if ((jultime >= 2407715.5) && (jultime <= 2408081.5))  // 1880
            answer = -32.184 - 5.40;

        else if ((jultime >= 2408081.5) && (jultime <= 2408446.5))  // 1881
            answer = -32.184 - 5.42;

        else if ((jultime >= 2408446.5) && (jultime <= 2408811.5))  // 1882
            answer = -32.184 - 5.20;

        else if ((jultime >= 2408811.5) && (jultime <= 2409542.5))  // 1883-4
            answer = -32.184 - 5.46;

        else if ((jultime >= 2409542.5) && (jultime <= 2409907.5))  // 1885
            answer = -32.184 - 5.79;

        else if ((jultime >= 2409907.5) && (jultime <= 2410272.5))  // 1886
            answer = -32.184 - 5.63;

        else if ((jultime >= 2410272.5) && (jultime <= 2410637.5))  // 1887
            answer = -32.184 - 5.64;

        else if ((jultime >= 2410637.5) && (jultime <= 2411003.5))  // 1888
            answer = -32.184 - 5.80;

        else if ((jultime >= 2411003.5) && (jultime <= 2411368.5))  // 1889
            answer = -32.184 - 5.66;

        else if ((jultime >= 2411368.5) && (jultime <= 2411733.5))  // 1890
            answer = -32.184 - 5.87;

        else if ((jultime >= 2411733.5) && (jultime <= 2412098.5))  // 1891
            answer = -32.184 - 6.01;

        else if ((jultime >= 2412098.5) && (jultime <= 2412464.5))  // 1892
            answer = -32.184 - 6.19;

        else if ((jultime >= 2412464.5) && (jultime <= 2412829.5))  // 1893
            answer = -32.184 - 6.64;

        else if ((jultime >= 2412829.5) && (jultime <= 2413194.5))  // 1894
            answer = -32.184 - 6.44;

        else if ((jultime >= 2413194.5) && (jultime <= 2413559.5))  // 1895
            answer = -32.184 - 6.47;

        else if ((jultime >= 2413559.5) && (jultime <= 2413925.5))  // 1896
            answer = -32.184 - 6.09;

        else if ((jultime >= 2413925.5) && (jultime <= 2414290.5))  // 1897
            answer = -32.184 - 5.76;

        else if ((jultime >= 2414290.5) && (jultime <= 2414655.5))  // 1898
            answer = -32.184 - 4.66;

        else if ((jultime >= 2414655.5) && (jultime <= 2415020.5))  // 1899
            answer = -32.184 - 3.74;

        else if ((jultime >= 2415020.5) && (jultime <= 2415385.5))  // 1900
            answer = -32.184 - 2.72;

        else if ((jultime >= 2415385.5) && (jultime <= 2415750.5))  // 1901
            answer = -32.184 - 1.54;

        else if ((jultime >= 2415750.5) && (jultime <= 2416115.5))  // 1902
            answer = -32.184 - 0.02;

        else if ((jultime >= 2416115.5) && (jultime <= 2416480.5))  // 1903
            answer = -32.184 + 1.24;

        else if ((jultime >= 2416480.5) && (jultime <= 2416846.5))  // 1904
            answer = -32.184 + 2.64;

        else if ((jultime >= 2416846.5) && (jultime <= 2417211.5))  // 1905
            answer = -32.184 + 3.86;

        else if ((jultime >= 2417211.5) && (jultime <= 2417576.5))  // 1906
            answer = -32.184 + 5.37;

        else if ((jultime >= 2417576.5) && (jultime <= 2417941.5))  // 1907
            answer = -32.184 + 6.14;

        else if ((jultime >= 2417941.5) && (jultime <= 2418307.5))  // 1908
            answer = -32.184 + 7.75;

        else if ((jultime >= 2418307.5) && (jultime <= 2418672.5))  // 1909
            answer = -32.184 + 9.13;

        else if ((jultime >= 2418672.5) && (jultime <= 2419037.5))  // 1910
            answer = -32.184 + 10.46;

        else if ((jultime >= 2419037.5) && (jultime <= 2419402.5))  // 1911
            answer = -32.184 + 11.53;

        else if ((jultime >= 2419402.5) && (jultime <= 2419768.5))  // 1912
            answer = -32.184 + 13.36;

        else if ((jultime >= 2419768.5) && (jultime <= 2420133.5))  // 1913
            answer = -32.184 + 14.65;

        else if ((jultime >= 2420133.5) && (jultime <= 2420498.5))  // 1914
            answer = -32.184 + 16.01;

        else if ((jultime >= 2420498.5) && (jultime <= 2420863.5))  // 1915
            answer = -32.184 + 17.20;

        else if ((jultime >= 2420863.5) && (jultime <= 2421229.5))  // 1916
            answer = -32.184 + 18.24;

        else if ((jultime >= 2421229.5) && (jultime <= 2421594.5))  // 1917
            answer = -32.184 + 19.06;

        else if ((jultime >= 2421594.5) && (jultime <= 2421959.5))  // 1918
            answer = -32.184 + 20.25;

        else if ((jultime >= 2421959.5) && (jultime <= 2422324.5))  // 1919
            answer = -32.184 + 20.95;

        else if ((jultime >= 2422324.5) && (jultime <= 2422690.5))  // 1920
            answer = -32.184 + 21.16;

        else if ((jultime >= 2422690.5) && (jultime <= 2423055.5))  // 1921
            answer = -32.184 + 22.25;

        else if ((jultime >= 2423055.5) && (jultime <= 2423420.5))  // 1922
            answer = -32.184 + 22.41;

        else if ((jultime >= 2423420.5) && (jultime <= 2423785.5))  // 1923
            answer = -32.184 + 23.03;

        else if ((jultime >= 2423785.5) && (jultime <= 2424151.5))  // 1924
            answer = -32.184 + 23.49;

        else if ((jultime >= 2424151.5) && (jultime <= 2424516.5))  // 1925
            answer = -32.184 + 23.62;

        else if ((jultime >= 2424516.5) && (jultime <= 2424881.5))  // 1926
            answer = -32.184 + 23.86;

        else if ((jultime >= 2424881.5) && (jultime <= 2425246.5))  // 1927
            answer = -32.184 + 24.49;

        else if ((jultime >= 2425246.5) && (jultime <= 2425612.5))  // 1928
            answer = -32.184 + 24.34;

        else if ((jultime >= 2425612.5) && (jultime <= 2425977.5))  // 1929
            answer = -32.184 + 24.08;

        else if ((jultime >= 2425977.5) && (jultime <= 2426342.5))  // 1930
            answer = -32.184 + 24.02;

        else if ((jultime >= 2426342.5) && (jultime <= 2426707.5))  // 1931
            answer = -32.184 + 24.00;

        else if ((jultime >= 2426707.5) && (jultime <= 2427073.5))  // 1932
            answer = -32.184 + 23.87;

        else if ((jultime >= 2427073.5) && (jultime <= 2427438.5))  // 1933
            answer = -32.184 + 23.95;

        else if ((jultime >= 2427438.5) && (jultime <= 2427803.5))  // 1934
            answer = -32.184 + 23.86;

        else if ((jultime >= 2427803.5) && (jultime <= 2428168.5))  // 1935
            answer = -32.184 + 23.93;

        else if ((jultime >= 2428168.5) && (jultime <= 2428534.5))  // 1936
            answer = -32.184 + 23.73;

        else if ((jultime >= 2428534.5) && (jultime <= 2428899.5))  // 1937
            answer = -32.184 + 23.92;

        else if ((jultime >= 2428899.5) && (jultime <= 2429264.5))  // 1938
            answer = -32.184 + 23.96;

        else if ((jultime >= 2429264.5) && (jultime <= 2429629.5))  // 1939
            answer = -32.184 + 24.02;

        else if ((jultime >= 2429629.5) && (jultime <= 2429995.5))  // 1940
            answer = -32.184 + 24.33;

        else if ((jultime >= 2429995.5) && (jultime <= 2430360.5))  // 1941
            answer = -32.184 + 24.83;

        else if ((jultime >= 2430360.5) && (jultime <= 2430725.5))  // 1942
            answer = -32.184 + 25.30;

        else if ((jultime >= 2430725.5) && (jultime <= 2431090.5))  // 1943
            answer = -32.184 + 25.70;

        else if ((jultime >= 2431090.5) && (jultime <= 2431456.5))  // 1944
            answer = -32.184 + 26.24;

        else if ((jultime >= 2431456.5) && (jultime <= 2431821.5))  // 1945
            answer = -32.184 + 26.77;

        else if ((jultime >= 2431821.5) && (jultime <= 2432186.5))  // 1946
            answer = -32.184 + 27.28;

        else if ((jultime >= 2432186.5) && (jultime <= 2432551.5))  // 1947
            answer = -32.184 + 27.78;

        else if ((jultime >= 2432551.5) && (jultime <= 2432917.5))  // 1948
            answer = -32.184 + 28.25;

        else if ((jultime >= 2432917.5) && (jultime <= 2433282.5))  // 1949
            answer = -32.184 + 28.71;

        else if ((jultime >= 2433282.5) && (jultime <= 2433647.5))  // 1950
            answer = -32.184 + 29.15;

        else if ((jultime >= 2433647.5) && (jultime <= 2434012.5))  // 1951
            answer = -32.184 + 29.57;

        else if ((jultime >= 2434012.5) && (jultime <= 2434378.5))  // 1952
            answer = -32.184 + 29.97;

        else if ((jultime >= 2434378.5) && (jultime <= 2434743.5))  // 1953
            answer = -32.184 + 30.36;

        else if ((jultime >= 2434743.5) && (jultime <= 2435108.5))  // 1954
            answer = -32.184 + 30.72;

        else if ((jultime >= 2435108.5) && (jultime <= 2435473.5))  // 1955
            answer = -32.184 + 31.07;

        else if ((jultime >= 2435473.5) && (jultime <= 2435839.5))  // 1956
            answer = -32.184 + 31.35;

        else if ((jultime >= 2435839.5) && (jultime <= 2436204.5))  // 1957
            answer = -32.184 + 31.68;

        else if ((jultime >= 2436204.5) && (jultime <= 2436569.5))  // 1958
            answer = -32.184 + 32.18;

        else if ((jultime >= 2436569.5) && (jultime <= 2436934.5))  // 1959
            answer = -32.184 + 32.68;

        else if ((jultime >= 2436934.5) && (jultime <= 2437300.5))  // 1960
            answer = -32.184 + 33.15;

        else if ((jultime >= 2437300.5) && (jultime <= 2437512.5))
            answer = 1.4228180 + (MJD - 37300.0) * 0.001296;

        else if ((jultime >= 2437512.5) && (jultime <= 2437665.5))
            answer = 1.3728180 + (MJD - 37300.0) * 0.001296;

        else if ((jultime >= 2437665.5) && (jultime <= 2438334.5))
            answer = 1.8458580 + (MJD - 37665.0) * 0.0011232;

        else if ((jultime >= 2438334.5) && (jultime <= 2438395.5))
            answer = 1.9458580 + (MJD - 37665.0) * 0.0011232;

        else if ((jultime >= 2438395.5) && (jultime <= 2438486.5))
            answer = 3.2401300 + (MJD - 38761.0) * 0.001296;

        else if ((jultime >= 2438486.5) && (jultime <= 2438639.5))
            answer = 3.3401300 + (MJD - 38761.0) * 0.001296;

        else if ((jultime >= 2438639.5) && (jultime <= 2438761.5))
            answer = 3.4401300 + (MJD - 38761.0) * 0.001296;

        else if ((jultime >= 2438761.5) && (jultime <= 2438820.5))
            answer = 3.5401300 + (MJD - 38761.0) * 0.001296;

        else if ((jultime >= 2438820.5) && (jultime <= 2438942.5))
            answer = 3.6401300 + (MJD - 38761.0) * 0.001296;

        else if ((jultime >= 2438942.5) && (jultime <= 2439004.5))
            answer = 3.7401300 + (MJD - 38761.0) * 0.001296;

        else if ((jultime >= 2439004.5) && (jultime <= 2439126.5))
            answer = 3.8401300 + (MJD - 38761.0) * 0.001296;

        else if ((jultime >= 2439126.5) && (jultime <= 2439887.5))
            answer = 4.3131700 + (MJD - 39126.0) * 0.002592;

        else if ((jultime >= 2439887.5) && (jultime <= 2441317.5))
            answer = 4.2131700 + (MJD - 39126.0) * 0.002592;

        else if ((jultime >= 2441317.5) && (jultime <= 2441499.5))
            answer = 10;

        else if ((jultime >= 2441499.5) && (jultime <= 2441683.5))
            answer = 11;

        else if ((jultime >= 2441683.5) && (jultime <= 2442048.5))
            answer = 12;

        else if ((jultime >= 2442048.5) && (jultime <= 2442413.5))
            answer = 13;

        else if ((jultime >= 2442413.5) && (jultime <= 2442778.5))
            answer = 14;

        else if ((jultime >= 2442778.5) && (jultime <= 2443144.5))
            answer = 15;

        else if ((jultime >= 2443144.5) && (jultime <= 2443509.5))
            answer = 16;

        else if ((jultime >= 2443509.5) && (jultime <= 2443874.5))
            answer = 17;

        else if ((jultime >= 2443874.5) && (jultime <= 2444239.5))
            answer = 18;

        else if ((jultime >= 2444239.5) && (jultime <= 2444786.5))
            answer = 19;

        else if ((jultime >= 2444786.5) && (jultime <= 2445151.5))
            answer = 20;

        else if ((jultime >= 2445151.5) && (jultime <= 2445516.5))
            answer = 21;

        else if ((jultime >= 2445516.5) && (jultime <= 2446247.5))
            answer = 22;

        else if ((jultime >= 2446247.5) && (jultime <= 2447161.5))
            answer = 23;

        else if ((jultime >= 2447161.5) && (jultime <= 2447892.5))
            answer = 24;

        else if ((jultime >= 2447892.5) && (jultime <= 2448257.5))
            answer = 25;

        else if ((jultime >= 2448257.5) && (jultime <= 2448804.5))
            answer = 26;

        else if ((jultime >= 2448804.5) && (jultime <= 2449169.5))
            answer = 27;

        else if ((jultime >= 2449169.5) && (jultime <= 2449534.5))
            answer = 28;

        else if ((jultime >= 2449534.5) && (jultime <= 2450083.5))
            answer = 29;

        else if ((jultime >= 2450083.5) && (jultime <= 2450630.5))
            answer = 30;

        else if ((jultime >= 2450630.5) && (jultime <= 2451179.5))
            answer = 31;

        else if ((jultime >= 2451179.5) && (jultime <= 2453736.5))
            answer = 32;

        else if ((jultime >= 2453736.5) && (jultime <= 3000000.5))
            answer = 33;


        return answer;

    }

    /*
      Procedure to convert the ecliptic Keplerian elements from B1950/FK4 to J2000/FK5.
      Algorithm taken from P. Kenneth Seidelmann, "Explanatory Supplement to the Astronomical Almanac", 1992, pp.314-315.
      Tested and verified 30 May 1999.
    */
    public static void convertEclipticElementsFK4toFK5(double I_w_omega[]) {

        double I = 0, w = 0, omega = 0, lprime = 0, l = 0, j = 0, iprime_sin = 0, iprime_cos = 0, iprime = 0, wdiff_sin = 0, wdiff_cos = 0, wdiff = 0, omegaprime_sin = 0, omegaprime_cos = 0, omegaprime = 0, wprime = 0;

        I = I_w_omega[0];
        w = I_w_omega[1];
        omega = I_w_omega[2];

        lprime = 4.50001688 * Math.PI / 180;
        l = 5.19856209 * Math.PI / 180;
        j = 0.00651966 * Math.PI / 180;

        /*
          Since the changes in I, w, and omega will likely only be a few degrees, I believe it is safe to assume that COS(wprime - w) is positive.  I'll use this to determine the sign of SIN(iprime).
        */
        iprime_sin = Math.sqrt(Math.pow(Math.sin(j) * Math.sin(l + omega), 2) + Math.pow((Math.sin(I) * Math.cos(j) + Math.cos(I) * Math.sin(j) * Math.cos(l + omega)), 2));
        if ((Math.sin(I) * Math.cos(j) + Math.cos(I) * Math.sin(j) * Math.cos(l + omega)) < 0)
            iprime_sin = -Math.abs(iprime_sin);
        iprime_cos = Math.cos(I) * Math.cos(j) - Math.sin(I) * Math.sin(j) * Math.cos(l + omega);
        iprime = Math.atan2(iprime_sin, iprime_cos);

        wdiff_sin = Math.sin(j) * Math.sin(l + omega) / iprime_sin;
        wdiff_cos = (Math.sin(I) * Math.cos(j) + Math.cos(I) * Math.sin(j) * Math.cos(l + omega)) / iprime_sin;
        wdiff = Math.atan2(wdiff_sin, wdiff_cos);

        omegaprime_sin = Math.sin(I) * Math.sin(l + omega) / iprime_sin;
        omegaprime_cos = (Math.cos(I) * Math.sin(j) + Math.sin(I) * Math.cos(j) * Math.cos(l + omega)) / iprime_sin;
        omegaprime = Math.atan2(omegaprime_sin, omegaprime_cos);
        omegaprime = omegaprime - lprime;

        wprime = w + wdiff;

        while (wprime < 0)
            wprime = wprime + 2 * Math.PI;
        while (wprime > 2 * Math.PI)
            wprime = wprime - 2 * Math.PI;

        while (omegaprime < 0)
            omegaprime = omegaprime + 2 * Math.PI;
        while (omegaprime > 2 * Math.PI)
            omegaprime = omegaprime - 2 * Math.PI;

        I_w_omega[0] = iprime;
        I_w_omega[1] = wprime;
        I_w_omega[2] = omegaprime;

    }

    /*
      Procedure to convert the ecliptic Keplerian elements from J2000/FK5 to B1950/FK4.
      Algorithm taken from P. Kenneth Seidelmann, "Explanatory Supplement to the Astronomical Almanac", 1992, pp.314-315.
      Tested and Verified 30 May 1999.
    */
    public static void convertEclipticElementsFK5toFK4(double I_w_omega[]) {

        double I = 0, w = 0, omega = 0, lprime = 0, l = 0, j = 0, i_sin = 0, i_cos = 0, Iprime = 0, wdiff_sin = 0, wdiff_cos = 0, wdiff = 0, omega_sin = 0, omega_cos = 0, omegaprime = 0, wprime = 0;

        Iprime = I_w_omega[0];
        wprime = I_w_omega[1];
        omegaprime = I_w_omega[2];

        lprime = 4.50001688 * Math.PI / 180;
        l = 5.19856209 * Math.PI / 180;
        j = 0.00651966 * Math.PI / 180;

        /*
          Since the changes in Iprime, wprime, and omegaprime will likely only be a few degrees, I believe it is safe to assume that COS(wprime - w) is positive.  I'll use this to determine the sign of SIN(i).
        */
        i_sin = Math.sqrt(Math.pow(Math.sin(j) * Math.sin(lprime + omegaprime), 2) + Math.pow((Math.sin(Iprime) * Math.cos(j) - Math.cos(Iprime) * Math.sin(j) * Math.cos(lprime + omegaprime)), 2));
        if ((Math.sin(Iprime) * Math.cos(j) - Math.cos(Iprime) * Math.sin(j) * Math.cos(lprime + omegaprime)) < 0)
            i_sin = -Math.abs(i_sin);
        i_cos = Math.cos(Iprime) * Math.cos(j) + Math.sin(Iprime) * Math.sin(j) * Math.cos(lprime + omegaprime);
        I = Math.atan2(i_sin, i_cos);

        wdiff_sin = Math.sin(j) * Math.sin(lprime + omegaprime) / i_sin;
        wdiff_cos = (Math.sin(Iprime) * Math.cos(j) - Math.cos(Iprime) * Math.sin(j) * Math.cos(lprime + omegaprime)) / i_sin;
        wdiff = Math.atan2(wdiff_sin, wdiff_cos);

        omega_sin = Math.sin(Iprime) * Math.sin(lprime + omegaprime) / i_sin;
        omega_cos = (-Math.cos(Iprime) * Math.sin(j) + Math.sin(Iprime) * Math.cos(j) * Math.cos(lprime + omegaprime)) / i_sin;
        omega = Math.atan2(omega_sin, omega_cos);
        omega = omega - l;

        w = wprime - wdiff;

        while (w < 0)
            w = w + 2 * Math.PI;
        while (w > 2 * Math.PI)
            w = w - 2 * Math.PI;

        while (omega < 0)
            omega = omega + 2 * Math.PI;
        while (omega > 2 * Math.PI)
            omega = omega - 2 * Math.PI;

        I_w_omega[0] = I;
        I_w_omega[1] = w;
        I_w_omega[2] = omega;

    }

    /*
      Procedure to convert TDB time (expressed as a Julian date) to TDT time (also expressed as a Julian date).  This is necessary because Earth-based observations are made in TDT time, but solar system orbital mechanics are calculated in TDB time.
      Note that this method is accurate to approximately 10^-6 seconds, and should be sufficient to handle either optical or radar observations.
      Both latitude and longitude are given in radians, while altitude is given in meters.
      The value of (UT1 - TAI) (in seconds) may be zero, in which case an approximate value will be calculated; or, it may be precisely known, in which case it will be used as is.
        Algorithm taken from P. Kenneth Seidelmann, "Explanatory Supplement to the Astronomical Almanac", 1992, pp. 42-44.  Also uses elements from Jean Meeus, "Astronomical Algorithms".
      Tested and verified 1999.
    */
    public static double convertTDBToTDT(double tdb_time, double latitude, double longitude, double altitude, double ut1_minus_tai) {

        double bigT = 0, mean = 0, e = 0, bigE = 0, diff = 0, bigD = 0, C = 0, u = 0, tdt_time = 0, g = 0, delta_t = 0, ut1_time = 0, bigL = 0, S = 0, v = 0, Ej = 0, Esa = 0, Lj = 0, Lsa = 0;

        bigT = (tdb_time - 2451545.0) / 36525.0;

        /* Begin computing the elements used to compute each term in the difference TDB - TDT */

        /* Find Earth's eccentric anomaly - Meeus p. 203 */
        mean = (100.466449 + 35999.3728519 * bigT - 0.00000568 * Math.pow(bigT, 2)) - (102.937348 + 0.3225557 * bigT + 0.00015026 * Math.pow(bigT, 2) + 0.000000478 * Math.pow(bigT, 3));
        mean = mean * Math.PI / 180.0;
        e = 0.01670862 - 0.000042037 * bigT - 0.0000001236 * Math.pow(bigT, 2) + (4E-11) * Math.pow(bigT, 3);
        bigE = mean + e * Math.sin(mean);
        diff = (1.658E-03) * Math.sin(bigE);

        /* Find mean elongation of the Moon from the Sun - Seidelmann p. 114 */
        bigD = (297.0 + 51.0 / 60.0 + 1.307 / 3600) + (1236 * 360 + (307 + 6.0 / 60.0 + 41.328 / 3600)) * bigT - (6.891 / 3600) * Math.pow(bigT, 2) + (0.019 / 3600) * Math.pow(bigT, 3);
        bigD = bigD * Math.PI / 180;
        diff = diff + (1.548E-06) * Math.sin(bigD);

        /* Find distance of observer from Eath's spin axis, in km. */
        C = 1.0 / Math.sqrt(Math.pow(Math.cos(latitude), 2) + Math.pow((1 - flattening), 2) * Math.pow(Math.sin(latitude), 2));
        u = Math.abs((earth_radius * C + altitude) * Math.cos(latitude)) / 1000.0;

        /* Find UT1 time */
        /* First need to find an approximate value of TDT time and delta T */
        g = 357.528 + 0.985600274 * (tdb_time - 2451545.0);
        g = g * Math.PI / 180;
        tdt_time = tdb_time - (0.001658 * Math.sin(g) + 0.000014 * Math.sin(2 * g)) / 86400.0;
        if (ut1_minus_tai == 0.0) {
            /* (UT1 - TAI) is unknown.  Use leap_second look-up table. */
            ut1_minus_tai = -getLeapSecond(tdb_time);
        }
        /* If (UT1 - TAI) is known, use it.  */
        delta_t = (32.184 - ut1_minus_tai) / 86400.0;
        ut1_time = (tdt_time - delta_t);
        ut1_time = (ut1_time - Math.floor(ut1_time)) * 2.0 * Math.PI;
        diff = diff + (3.17679E-10) * u * Math.sin(ut1_time + longitude);
        diff = diff + (5.312E-12) * u * Math.sin(ut1_time + longitude - mean);
        diff = diff + (1.0E-13) * u * Math.sin(ut1_time + longitude - 2.0 * mean);

        /* Find mean longitude of Sun wrt Earth-Moon barycenter, referred to mean equinox and ecliptic of date - Meeus p. 151. */
        bigL = (280.46645 + 36000.76983 * bigT + 0.0003032 * Math.pow(bigT, 2)) * Math.PI / 180.0;
        diff = diff - (1.3677E-11) * u * Math.sin(ut1_time + longitude + 2.0 * bigL);
        diff = diff - (2.29E-13) * u * Math.sin(ut1_time + longitude + 2.0 * bigL + mean);

        /* Find the mean elongation of the Moon from the Sun - Seidelmann p. 114 */
        bigD = (297.0 + 51.0 / 60.0 + 1.307 / 3600) + (1236 * 360 + (307 + 6.0 / 60.0 + 41.328 / 3600)) * bigT - (6.891 / 3600) * Math.pow(bigT, 2) + (0.019 / 3600) * Math.pow(bigT, 3);
        bigD = bigD * Math.PI / 180;
        diff = diff + (1.33E-13) * u * Math.sin(ut1_time + longitude - bigD);

        /* Find distance from Earth's equatorial plane, in km */
        S = Math.pow((1 - flattening), 2) * C;
        v = (earth_radius * S + altitude) * Math.sin(latitude) / 1000.0;
        diff = diff - (1.3184E-10) * v * Math.cos(bigL);

        /* Find the eccentric anomaly of Jupiter - Meeus p. 204 */
        Ej = ((34.351484 + 3034.9056746 * bigT - 0.00008501 * Math.pow(bigT, 2) + 0.000000004 * Math.pow(bigT, 3)) - (14.331309 + 0.2155525 * bigT + 0.00072252 * Math.pow(bigT, 2) - 0.000004590 * Math.pow(bigT, 3))) * Math.PI / 180.0;
        diff = diff + (5.21E-06) * Math.sin(Ej);

        /* Find the eccentric anomaly of Saturn - Meeus p. 204 */
        Esa = ((50.077471 + 1222.1137943 * bigT + 0.00021004 * Math.pow(bigT, 2) - 0.000000019 * Math.pow(bigT, 3)) - (93.056787 + 0.5665496 * bigT + 0.00052809 * Math.pow(bigT, 2) + 0.000004882 * Math.pow(bigT, 3))) * Math.PI / 180.0;
        diff = diff + (2.45E-06) * Math.sin(Esa);

        /* Find the mean longitude of Jupiter - Meeus p. 204 */
        Lj = (34.351484 + 3034.9056746 * bigT - 0.00008501 * Math.pow(bigT, 2) + 0.000000004 * Math.pow(bigT, 3)) * Math.PI / 180.0;
        diff = diff + (20.73E-06) * Math.sin(bigL - Lj);

        /* Find the mean longitude of Saturn - Meeus p. 204 */
        Lsa = (50.077471 + 1222.1137943 * bigT + 0.00021004 * Math.pow(bigT, 2) - 0.000000019 * Math.pow(bigT, 3)) * Math.PI / 180.0;
        diff = diff + (4.58E-06) * Math.sin(bigL - Lsa);
        diff = diff + (1.33E-13) * u * Math.sin(ut1_time + longitude + bigL - Lj);
        diff = diff + (2.9E-14) * u * Math.sin(ut1_time + longitude + bigL - Lsa);

        diff = (diff / 86400);

        tdt_time = tdb_time - diff;
        return tdt_time;

    }

    /*
      Procedure to convert TDT time (expressed as a Julian date) to TDB time (also expressed as a Julian date).  This is necessary because Earth-based observations are made in TDT time, but solar system orbital mechanics are calculated in TDB time.
      Note that this method is accurate to approximately 10^-6 seconds, and should be sufficient to handle either optical or radar observations.
      Both latitude and longitude are given in radians, while altitude is given in meters.
      The value of (UT1 - TAI) (in seconds) may be zero, in which case an approximate value will be calculated; or, it may be precisely known, in which case it will be used as is.
        Algorithm taken from P. Kenneth Seidelmann, "Explanatory Supplement to the Astronomical Almanac", 1992, pp. 42-44.  Also uses elements from Jean Meeus, "Astronomical Algorithms".
      Tested and verified 1999.
    */
    public static double convertTDTtoTDB(double tdt_time, double latitude, double longitude, double altitude, double ut1_minus_tai) {

        double bigT = 0, mean = 0, e = 0, bigE = 0, diff = 0, bigD = 0, C = 0, u = 0, tdb_time = 0, g = 0, delta_t = 0, ut1_time = 0, bigL = 0, S = 0, v = 0, Ej = 0, Esa = 0, Lj = 0, Lsa = 0;

        bigT = (tdt_time - 2451545.0) / 36525.0;

        /* Begin computing the elements used to compute each term in the difference TDB - TDT */

        /* Find Earth's eccentric anomaly - Meeus p. 203 */
        mean = (100.466449 + 35999.3728519 * bigT - 0.00000568 * Math.pow(bigT, 2)) - (102.937348 + 0.3225557 * bigT + 0.00015026 * Math.pow(bigT, 2) + 0.000000478 * Math.pow(bigT, 3));
        mean = mean * Math.PI / 180.0;
        e = 0.01670862 - 0.000042037 * bigT - 0.0000001236 * Math.pow(bigT, 2) + (4E-11) * Math.pow(bigT, 3);
        bigE = mean + e * Math.sin(mean);
        diff = (1.658E-03) * Math.sin(bigE);

        /* Find mean elongation of the Moon from the Sun - Seidelmann p. 114 */
        bigD = (297.0 + 51.0 / 60.0 + 1.307 / 3600) + (1236 * 360 + (307 + 6.0 / 60.0 + 41.328 / 3600)) * bigT - (6.891 / 3600) * Math.pow(bigT, 2) + (0.019 / 3600) * Math.pow(bigT, 3);
        bigD = bigD * Math.PI / 180;
        diff = diff + (1.548E-06) * Math.sin(bigD);

        /* Find distance of observer from Eath's spin axis, in km. */
        C = 1.0 / Math.sqrt(Math.pow(Math.cos(latitude), 2) + Math.pow((1 - flattening), 2) * Math.pow(Math.sin(latitude), 2));
        u = Math.abs((earth_radius * C + altitude) * Math.cos(latitude)) / 1000.0;

        /* Find UT1 time */
        /* First need to find an approximate value of delta T */
        if (ut1_minus_tai == 0.0) {
            /* (UT1 - TAI) is unknown.  Use leap_second look-up table. */
            ut1_minus_tai = -getLeapSecond(tdt_time);
        }
        /* If (UT1 - TAI) is known, use it.  */
        delta_t = (32.184 - ut1_minus_tai) / 86400.0;
        ut1_time = (tdt_time - delta_t);
        ut1_time = (ut1_time - Math.floor(ut1_time)) * 2.0 * Math.PI;
        diff = diff + (3.17679E-10) * u * Math.sin(ut1_time + longitude);
        diff = diff + (5.312E-12) * u * Math.sin(ut1_time + longitude - mean);
        diff = diff + (1.0E-13) * u * Math.sin(ut1_time + longitude - 2.0 * mean);

        /* Find mean longitude of Sun wrt Earth-Moon barycenter, referred to mean equinox and ecliptic of date - Meeus p. 151. */
        bigL = (280.46645 + 36000.76983 * bigT + 0.0003032 * Math.pow(bigT, 2)) * Math.PI / 180.0;
        diff = diff - (1.3677E-11) * u * Math.sin(ut1_time + longitude + 2.0 * bigL);
        diff = diff - (2.29E-13) * u * Math.sin(ut1_time + longitude + 2.0 * bigL + mean);

        /* Find the mean elongation of the Moon from the Sun - Seidelmann p. 114 */
        bigD = (297.0 + 51.0 / 60.0 + 1.307 / 3600) + (1236 * 360 + (307 + 6.0 / 60.0 + 41.328 / 3600)) * bigT - (6.891 / 3600) * Math.pow(bigT, 2) + (0.019 / 3600) * Math.pow(bigT, 3);
        bigD = bigD * Math.PI / 180;
        diff = diff + (1.33E-13) * u * Math.sin(ut1_time + longitude - bigD);

        /* Find distance from Earth's equatorial plane, in km */
        S = Math.pow((1 - flattening), 2) * C;
        v = (earth_radius * S + altitude) * Math.sin(latitude) / 1000.0;
        diff = diff - (1.3184E-10) * v * Math.cos(bigL);

        /* Find the eccentric anomaly of Jupiter - Meeus p. 204 */
        Ej = ((34.351484 + 3034.9056746 * bigT - 0.00008501 * Math.pow(bigT, 2) + 0.000000004 * Math.pow(bigT, 3)) - (14.331309 + 0.2155525 * bigT + 0.00072252 * Math.pow(bigT, 2) - 0.000004590 * Math.pow(bigT, 3))) * Math.PI / 180.0;
        diff = diff + (5.21E-06) * Math.sin(Ej);

        /* Find the eccentric anomaly of Saturn - Meeus p. 204 */
        Esa = ((50.077471 + 1222.1137943 * bigT + 0.00021004 * Math.pow(bigT, 2) - 0.000000019 * Math.pow(bigT, 3)) - (93.056787 + 0.5665496 * bigT + 0.00052809 * Math.pow(bigT, 2) + 0.000004882 * Math.pow(bigT, 3))) * Math.PI / 180.0;
        diff = diff + (2.45E-06) * Math.sin(Esa);

        /* Find the mean longitude of Jupiter - Meeus p. 204 */
        Lj = (34.351484 + 3034.9056746 * bigT - 0.00008501 * Math.pow(bigT, 2) + 0.000000004 * Math.pow(bigT, 3)) * Math.PI / 180.0;
        diff = diff + (20.73E-06) * Math.sin(bigL - Lj);

        /* Find the mean longitude of Saturn - Meeus p. 204 */
        Lsa = (50.077471 + 1222.1137943 * bigT + 0.00021004 * Math.pow(bigT, 2) - 0.000000019 * Math.pow(bigT, 3)) * Math.PI / 180.0;
        diff = diff + (4.58E-06) * Math.sin(bigL - Lsa);
        diff = diff + (1.33E-13) * u * Math.sin(ut1_time + longitude + bigL - Lj);
        diff = diff + (2.9E-14) * u * Math.sin(ut1_time + longitude + bigL - Lsa);

        diff = (diff / 86400);

        tdb_time = tdt_time + diff;
        return tdb_time;

    }

    /*
      Procedure to calculate the mean ecliptic longitude and latitude of an object referred to the mean equinox of J2000, given the ra and dec of the object.
      Algorithm taken from Meeus, "Astronomical Formulae for Calculators", p 40.

      Tested and verified 30 May 1999.
    */
    public static void convertRadectoEcliptic(double radec[], double latlong[]) {

        double longsin = 0, longcos = 0, latsin = 0, latcos = 0;

        longsin = Math.sin(radec[0]) * Math.cos(epsilon) + Math.tan(radec[1]) * Math.sin(epsilon);
        longcos = Math.cos(radec[0]);
        latlong[2] = Math.atan2(longsin, longcos);

        latsin = Math.sin(radec[1]) * Math.cos(epsilon) - Math.cos(radec[1]) * Math.sin(epsilon) * Math.sin(radec[0]);
        latcos = Math.sqrt(1 - Math.pow(latsin, 2));
        latlong[1] = Math.atan(latsin / latcos);

    }

    /*
      Procedure to calculate the ecliptic lat/long (in radians) of an object, given the heliocentric ecliptic position vector r() (3 entries, expressed in heliocentric A.U.s) and the heliocentric ecliptic position of the observing site (sitex, sitey, and sitez, expressed in heliocentric A.U.s)
      Algorithm taken from Pedro Ramon Escobal, "Methods of Orbit Determination", p 463.
      Tested and verified 30 May 1999.
    */
    public static void convertHeliocentricPositionVectorToEclipticLatLong(double r[], double site[], double latlong[]) {

        double rhox = 0, rhoy = 0, rhoz = 0, rhomag = 0, Lx = 0, Ly = 0, Lz = 0;

        rhox = r[0] + site[0];
        rhoy = r[1] + site[1];
        rhoz = r[2] + site[2];
        rhomag = Math.sqrt(Math.pow(rhox, 2) + Math.pow(rhoy, 2) + Math.pow(rhoz, 2));

        Lx = rhox / rhomag;
        Ly = rhoy / rhomag;
        Lz = rhoz / rhomag;

        latlong[0] = Math.atan(Lz / Math.sqrt(1 - Math.pow(Lz, 2)));
        latlong[1] = Math.atan2(Ly / Math.cos(latlong[1]), Lx / Math.cos(latlong[1]));

    }

    /*
      Procedure to calculate the ecliptic rectangular coordinates of a site referred to the mean equinox of J2000, given the equatorial rectangular coordinates.
      Site coordinates should be in A.U.s.
      Algorithm taken from Pedro Ramon Escobal, "Methods of Orbit Determination", p 409.
    Tested and verified 29 May 1999.
    */
    public static void convertEquatorialToEcliptic(double site[]) {

        double sitexrect = 0, siteyrect = 0, sitezrect = 0;

        sitexrect = site[0];
        siteyrect = site[1] * Math.cos(epsilon) + site[2] * Math.sin(epsilon);
        sitezrect = -site[1] * Math.sin(epsilon) + site[2] * Math.cos(epsilon);

        site[0] = sitexrect;
        site[1] = siteyrect;
        site[2] = sitezrect;

    }

    /*
      Procedure to calculate the equatorial rectangular coordinates of a site referred to the mean equinox of J2000, given the ecliptic rectangular coordinates.
      Site coordinates should be in A.U.s.
      Algorithm taken from Pedro Ramon Escobal, "Methods of Orbit Determination", p 409.
    Copied from equatorial_to_ecliptic 24 June 1999.
    */
    public static void convertEclipticToEquatorial(double site[]) {

        double sitexrect = 0, siteyrect = 0, sitezrect = 0;

        sitexrect = site[0];
        siteyrect = site[1] * Math.cos(epsilon) - site[2] * Math.sin(epsilon);
        sitezrect = site[1] * Math.sin(epsilon) + site[2] * Math.cos(epsilon);

        site[0] = sitexrect;
        site[1] = siteyrect;
        site[2] = sitezrect;

    }

    /*
      Procedure to calculate the equatorial ra and dec of an object referred to the mean equinox of J2000, given the ecliptic longitude and latitude of the object referred to the mean equinox of J2000.
      Algorithm taken from Meeus, "Astronomical Algorithms", p 89.
    Tested and verified 29 May 1999.
    */
    public static void convertEclipticToRadec(double latlong[], double radec[]) {

        double rasin = 0, racos = 0, decsin = 0, deccos = 0;

        rasin = Math.sin(latlong[1]) * Math.cos(epsilon) - Math.tan(latlong[0]) * Math.sin(epsilon);
        racos = Math.cos(latlong[1]);
        radec[0] = Math.atan2(rasin, racos);

        decsin = Math.sin(latlong[1]) * Math.cos(epsilon) + Math.cos(latlong[1]) * Math.sin(epsilon) * Math.sin(latlong[2]);
        deccos = Math.sqrt(1 - Math.pow(decsin, 2));
        radec[1] = Math.atan(decsin / deccos);

    }

    /*
        Procedure to convert an equatorial rectangular vector from J2000/FK5 to B1950/FK4.
      Algorithm taken from P. Kenneth Seidelmann, "Explanatory Supplement to the Astronomical Almanac", 1992, pg. 313.
      Tested and verified 29 May 1999.
    */
    public static void convertEquatorialRectangularJ2000ToB1950(double r[]) {

        double[][] x = new double[3][3];
        double[] r1 = new double[3];
        int i = 0, j = 0;

        x[0][0] = (0.9999256794956877);
        x[0][1] = (-0.0111814832204662);
        x[0][2] = (-0.0048590038153592);
        x[1][0] = (0.0111814832391717);
        x[1][1] = (0.9999374848933135);
        x[1][2] = (-0.0000271625947142);
        x[2][0] = (0.0048590037723143);
        x[2][1] = (-0.0000271702937440);
        x[2][2] = (0.9999881946023742);

        invertNxn(3, x);

        for (i = 0; i <= 2; i++) {
            r1[i] = 0;
            for (j = 0; j <= 2; j++)
                r1[i] = r1[i] + x[i][j] * r[j];
        }

        for (i = 0; i <= 2; i++)
            r[i] = r1[i];

    }

    /*
      Procedure to convert an equatorial rectangular vector from B1950/FK4 to J2000/FK5.
      Algorithm taken from P. Kenneth Seidelmann, "Explanatory Supplement to the Astronomical Almanac", 1992, pg. 313.
      Tested and verified 29 May 1999.
    */
    public static void convertEquatorialRectangularB1950toJ2000(double r[]) {

        double[][] x = new double[3][3];
        double[] r1 = new double[3];
        int i = 0, j = 0;

        x[0][0] = (0.9999256794956877);
        x[0][1] = (-0.0111814832204662);
        x[0][2] = (-0.0048590038153592);
        x[1][0] = (0.0111814832391717);
        x[1][1] = (0.9999374848933135);
        x[1][2] = (-0.0000271625947142);
        x[2][0] = (0.0048590037723143);
        x[2][1] = (-0.0000271702937440);
        x[2][2] = (0.9999881946023742);

        for (i = 0; i <= 2; i++) {
            r1[i] = 0;
            for (j = 0; j <= 2; j++)
                r1[i] = r1[i] + x[i][j] * r[j];
        }

        for (i = 0; i <= 2; i++)
            r[i] = r1[i];

    }

    /*
      Procedure to convert the astrometric right ascension and declination angles of a comet or asteroid from the Julian standard equinox 2000.0 referred to the FK5 catalog, to B1950/FK4.  The Julian day of the epoch of observation (jd_epoch) is required to correct for imprecise FK4 precession values, and for equinox drift between FK4 and FK5.
    Note that the right ascension and declination are stored as an array, with radec[0] = ra, and radec[1] = dec.
      Algorithm taken from P. Kenneth Seidelmann, "Explanatory Supplement to the Astronomical Almanac", 1992, pp. 310-315.
    Tested and verified 29 May 1999.
    */
    public static void convertRadecJ2000toB1950(double[] radec, double jd_epoch) {

        double[] r0 = new double[4];
        double[] r1 = new double[4];
        double[] r = new double[4];
        double[][] m = new double[4][4];
        int i = 0, j = 0;
        double t = 0, mag = 0, dot_product = 0;

        /*  Form r0() vector  */
        r0[1] = Math.cos(radec[0]) * Math.cos(radec[1]);
        r0[2] = Math.sin(radec[0]) * Math.cos(radec[1]);
        r0[3] = Math.sin(radec[1]);

        /*  Form the vector r()  */
        t = (jd_epoch - 2433282.423) / 36525;
        m[1][1] = (0.9999256794956877) + t * (-0.0026455262) * 1e-06;
        m[1][2] = (-0.0111814832204662) + t * (-1.1539918689) * 1e-06;
        m[1][3] = (-0.0048590038153592) + t * (2.1111346190) * 1e-06;
        m[2][1] = (0.0111814832391717) + t * (1.1540628161) * 1e-06;
        m[2][2] = (0.9999374848933135) + t * (-0.0129042997) * 1e-06;
        m[2][3] = (-0.0000271625947142) + t * (0.0236021478) * 1e-06;
        m[3][1] = (0.0048590037723143) + t * (-2.1112979048) * 1e-06;
        m[3][2] = (-0.0000271702937440) + t * (-0.0056024448) * 1e-06;
        m[3][3] = (0.9999881946023742) + t * (0.0102587734) * 1e-06;
        invertNxn(3, m);
        for (i = 1; i <= 3; i++) {
            r1[i] = 0;
            for (j = 1; j <= 3; j++)
                r1[i] = r1[i] + m[i][j] * r0[j];
        }

        /*  Include the effects of elliptical aberration  */
        dot_product = r1[1] * (-1.62557e-06) + r1[2] * (-0.31919e-06) + r1[3] * (-0.13843e-06);
        r[1] = r1[1] + (-1.62557e-06) - dot_product * r1[1];
        r[2] = r1[2] + (-0.31919e-06) - dot_product * r1[2];
        r[3] = r1[3] + (-0.13843e-06) - dot_product * r1[3];

        /*  Calculate B1950 ra/dec  */
        mag = Math.sqrt(Math.pow(r[1], 2) + Math.pow(r[2], 2) + Math.pow(r[3], 2));
        radec[0] = Math.atan2((r[2] / mag), (r[1] / mag));
        radec[1] = Math.atan2((r[3] / mag), ((r[2] / mag) / Math.sin(radec[0])));

    }

    /*
    Procedure to convert the astrometric right ascension and declination angles of a comet or asteroid from the Besselian standard equinox 1950.0 referred to the FK4 catalog, to J2000/FK5.  	The Julian day of the epoch of observation (jd_epoch) is required to correct for imprecise FK4 precession values, and for equinox drift between FK4 and FK5.
    Note that the right ascension and declination are stored as an array, with radec[0] = ra, and radec[1] = dec.
    Algorithm taken from P. Kenneth Seidelmann, "Explanatory Supplement to the Astronomical Almanac", 1992, pp. 310-315.
    Tested and verified 29 May 1999.
    */
    public static void convertRadecB1950toJ2000(double[] radec, double jd_epoch) {

        double[] r0 = new double[4];
        double[] r1 = new double[4];
        double[] r = new double[4];
        double[][] m = new double[4][4];
        int i = 0, j = 0;
        double t = 0, mag = 0, dot_product = 0;

        /*  Form r0() vector */
        r0[1] = Math.cos(radec[0]) * Math.cos(radec[1]);
        r0[2] = Math.sin(radec[0]) * Math.cos(radec[1]);
        r0[3] = Math.sin(radec[1]);

        /*  Remove the effects of elliptical aberration to form astrographic position vector r1() */
        dot_product = r0[1] * (-1.62557e-06) + r0[2] * (-0.31919e-06) + r0[3] * (-0.13843e-06);
        r1[1] = r0[1] - (-1.62557e-06) + dot_product * r0[1];
        r1[2] = r0[2] - (-0.31919e-06) + dot_product * r0[2];
        r1[3] = r0[3] - (-0.13843e-06) + dot_product * r0[3];

        /*  Form the J2000 vector r() */
        t = (jd_epoch - 2433282.423) / 36525;
        m[1][1] = (0.9999256794956877) + t * (-0.0026455262) * 1e-06;
        m[1][2] = (-0.0111814832204662) + t * (-1.1539918689) * 1e-06;
        m[1][3] = (-0.0048590038153592) + t * (2.1111346190) * 1e-06;
        m[2][1] = (0.0111814832391717) + t * (1.1540628161) * 1e-06;
        m[2][2] = (0.9999374848933135) + t * (-0.0129042997) * 1e-06;
        m[2][3] = (-0.0000271625947142) + t * (0.0236021478) * 1e-06;
        m[3][1] = (0.0048590037723143) + t * (-2.1112979048) * 1e-06;
        m[3][2] = (-0.0000271702937440) + t * (-0.0056024448) * 1e-06;
        m[3][3] = (0.9999881946023742) + t * (0.0102587734) * 1e-06;
        for (i = 1; i <= 3; i++) {
            r[i] = 0;
            for (j = 1; j <= 3; j++)
                r[i] = r[i] + m[i][j] * r1[j];
        }

        /*  Calculate J2000 ra/dec  */
        mag = Math.sqrt(Math.pow(r[1], 2) + Math.pow(r[2], 2) + Math.pow(r[3], 2));
        radec[0] = Math.atan2((r[2] / mag), (r[1] / mag));
        radec[1] = Math.atan2((r[3] / mag), ((r[2] / mag) / Math.sin(radec[0])));

    }

    /*
      Procedure to precess the equatorial rectangular coordinates of a site from mean equinox jultime1 to mean equinox jultime2.  Precession takes place with reference to the equinox defined by FK4 or FK5, depending on the value of the boolean FK5.

      Note that the site coordinates are given by the array site[],
        where site[0] = x-coord in A.U.s
              site[1] = y-coord in A.U.s
              site[2] = z-coord in A.U.s.
      Note also that jultime1 and jultime2 are given as Julian dates.

      Algorithm taken from Jean Meeus, "Astronomical Algorithms", pp 162-3 and P. Kenneth Seidelmann, "Explanatory Supplement to the Astronomical Almanac", 1992, p. 107.

      Tested and verified 25 May 1999
    */
    public static void getRectangularEquatorialPrecession(double site[], double jultime1, double jultime2, boolean FK5) {

        double t0 = 0, t = 0, xi = 0, z = 0, theta = 0, t1 = 0, t2 = 0, tau = 0, Xx = 0, Xy = 0, Xz = 0, Yx = 0, Yy = 0, Yz = 0, Zx = 0, Zy = 0, Zz = 0, x = 0, y = 0;

        if (FK5 == true) {
            /*  Use FK5 precessional formulae from Meeus */
            t0 = (jultime1 - 2451545) / 36525;
            t = (jultime2 - jultime1) / 36525;
            xi = (2306.2181 + 1.39656 * t0 - .000139 * Math.pow(t0, 2)) * t + (.30188 - .000344 * t0) * Math.pow(t, 2) + .017998 * Math.pow(t, 3);
            xi = ((xi / 3600) * Math.PI / 180);
            z = (2306.2181 + 1.39656 * t0 - .000139 * Math.pow(t0, 2)) * t + (1.09468 + .000066 * t0) * Math.pow(t, 2) + .018203 * Math.pow(t, 3);
            z = ((z / 3600) * Math.PI / 180);
            theta = (2004.3109 - .8533 * t0 - .000217 * Math.pow(t0, 2)) * t - (.42665 + .000217 * t0) * Math.pow(t, 2) - .041833 * Math.pow(t, 3);
            theta = ((theta / 3600) * Math.PI / 180);
        } else {
            /*  Use FK4 (Andoyer) precessional formula from Seidelmann */
            t1 = (jultime1 - 2396758.203) / 365242.198782;
            t2 = (jultime2 - 2396758.203) / 365242.198782;
            tau = t2 - t1;
            xi = (23035.545 + 139.720 * t1 + 0.060 * Math.pow(t1, 2)) * tau + (30.240 - 0.270 * t1) * Math.pow(tau, 2) + 17.995 * Math.pow(tau, 3);
            xi = ((xi / 3600) * Math.PI / 180);
            z = (23035.545 + 139.720 * t1 + 0.060 * Math.pow(t1, 2)) * tau + (109.480 + 0.390 * t1) * Math.pow(tau, 2) + 18.325 * Math.pow(tau, 3);
            z = ((z / 3600) * Math.PI / 180);
            theta = (20051.12 - 85.29 * t1 - 0.37 * Math.pow(t1, 2)) * tau + (-42.65 - 0.37 * t1) * Math.pow(tau, 2) - 41.80 * Math.pow(tau, 3);
            theta = ((theta / 3600) * Math.PI / 180);
        }

        Xx = Math.cos(xi) * Math.cos(z) * Math.cos(theta) - Math.sin(xi) * Math.sin(z);
        Xy = Math.sin(xi) * Math.cos(z) + Math.cos(xi) * Math.sin(z) * Math.cos(theta);
        Xz = Math.cos(xi) * Math.sin(theta);
        Yx = -Math.cos(xi) * Math.sin(z) - Math.sin(xi) * Math.cos(z) * Math.cos(theta);
        Yy = Math.cos(xi) * Math.cos(z) - Math.sin(xi) * Math.sin(z) * Math.cos(theta);
        Yz = -Math.sin(xi) * Math.sin(theta);
        Zx = -Math.cos(z) * Math.sin(theta);
        Zy = -Math.sin(z) * Math.sin(theta);
        Zz = Math.cos(theta);
        x = Xx * site[0] + Yx * site[1] + Zx * site[2];
        y = Xy * site[0] + Yy * site[1] + Zy * site[2];
        z = Xz * site[0] + Yz * site[1] + Zz * site[2];
        site[0] = x;
        site[1] = y;
        site[2] = z;

    }

    /*

      Procedure to calculate the position and velocity of a body at time2 given the position and velocity of the body at time1.  Uses the universal variables solution to the Gauss problem.  Reference is Roger Bate, Donald Mueller, and Jerry White, "Fundamentals of Astrodynamics", pp 191-210.
      Positions and velocities are given in canonical units, while times are Julian dates.
      Tested and verified 5 June 1999.

    */
   public static void twoBodyUpdate(double r1[],double r1prime[],double time1,double r2[],double r2prime[],double time2) {

    double r0 = 0, v0 = 0, rdotv = 0, alpha = 0, t = 0, x = 0, diff = 0, z = 0, c = 0, s = 0, tn = 0, dtdx = 0, f = 0, g = 0, r = 0, fdot = 0, gdot = 0;
    int k = 0, counter = 0;

    r0 = Math.sqrt(Math.pow(r1[1],2) + Math.pow(r1[2],2) + Math.pow(r1[3],2));
    v0 = Math.sqrt(Math.pow(r1prime[1],2) + Math.pow(r1prime[2],2) + Math.pow(r1prime[3],2));
    rdotv = r1[1]*r1prime[1] + r1[2]*r1prime[2] + r1[3]*r1prime[3];
    alpha = 2/r0 - Math.pow(v0,2)/mu;
    /* Tests indicate parabolic orbits do not give alpha = 0, but rather -1e-16.  This is a numerical artifact due to round-off error.  Force alpha = 0 in this case.  */
    if (Math.abs(alpha) < 1e-15) alpha = 0;
    t = kappa*(time2 - time1);

    if (Math.abs(t) > 0) {
        if (alpha >= 0) {
            x = Math.sqrt(mu)*alpha*t;
            }
        else {
            x = (t/Math.abs(t))*Math.sqrt(-1/alpha)*Math.log((-2*mu*t*alpha)/(rdotv + (t/Math.abs(t))*Math.sqrt(-mu/alpha)*(1 - alpha*r0)));
            }
        }
    else
        x = 0;

    diff = 1;

    while ((Math.abs(diff) > 1e-09) && (counter < 100)) {
        counter = counter + 1;
        z = alpha*Math.pow(x,2);
        if (Math.abs(z) < .01) {
            c = 0.5 - z/24 + Math.pow(z,2)/720 - Math.pow(z,3)/40320 + Math.pow(z,4)/3628800;
            s = 1.0/6.0 - z/120 + Math.pow(z,2)/5040 - Math.pow(z,3)/362880 + Math.pow(z,4)/39916800;
            }
        else if (z < 0) {
            c = (1 - (Math.exp(Math.sqrt(-z)) + Math.exp(-Math.sqrt(-z)))/2)/z;
            s = ((Math.exp(Math.sqrt(-z)) - Math.exp(-Math.sqrt(-z)))/2 - Math.sqrt(-z))/Math.pow(Math.sqrt(-z),3);
            }
        else {
            c = (1 - Math.cos(Math.sqrt(z)))/z;
            s = (Math.sqrt(z) - Math.sin(Math.sqrt(z)))/Math.pow(Math.sqrt(z),3);
            }
        tn = (rdotv*Math.pow(x,2)*c/Math.sqrt(mu) + (1 - alpha*r0)*Math.pow(x,3)*s + r0*x)/Math.sqrt(mu);
        diff = t - tn;
        dtdx = (Math.pow(x,2)*c + rdotv*x*(1 - z*s)/Math.sqrt(mu) + r0*(1 - z*c))/Math.sqrt(mu);
        x = x + diff/dtdx;
        }

        f = 1 - Math.pow(x,2)*c/r0;
        g = t - Math.pow(x,3)*s/Math.sqrt(mu);
        for (k = 1; k <= 3; k++)
            r2[k] = f*r1[k] + g*r1prime[k];

        r = Math.sqrt(Math.pow(r2[1],2) + Math.pow(r2[2],2) + Math.pow(r2[3],2));

        fdot = Math.sqrt(mu)*x*(z*s - 1)/(r0*r);
        gdot = 1 - Math.pow(x,2)*c/r;
        for (k = 1; k <= 3; k++)
            r2prime[k] = fdot* r1[k] + gdot*r1prime[k];

    }

    /*
      Procedure to precess the Keplerian elements from the Julian day of jd_equinox1 to jd_equinox2.  Elements referred to either FK4 or FK5 are precessed within this catalog, as indicated by the FK5 boolean.
      All angles are in radians
      Algorithm taken from Jean Meeus, "Astronomical Algorithms", pp 147-150, and "Astronomical Formulae for Calculators", p 71.

      Tested and verified 4 June 1999.

    */
    public void precessKeplerianElements(double[] I_w_omega, double jd_equinox1, double jd_equinox2, boolean FK5) {

    double oldi = 0, oldw = 0, oldomega = 0, bigt = 0, t = 0, eta = 0, bigpi = 0, p = 0, tau0 = 0, tau = 0;
    double psi = 0, icos = 0, a = 0, b = 0, omega_minus_psi = 0, omega = 0, isin = 0, I = 0, deltawsin = 0, deltawcos = 0, deltaw = 0, w = 0;

    oldi = I_w_omega[1];
    oldw = I_w_omega[2];
    oldomega = I_w_omega[3];

    if (FK5 == true) {
        /*  Precess within the FK5 catalog  */
        bigt = (jd_equinox1 - 2451545)/36525;
        t = (jd_equinox2 - jd_equinox1)/36525;
        eta = ((47.0029 - .06603*bigt + .000598*Math.pow(bigt,2))*t + (-.03302 + .000598*bigt)*Math.pow(t,2) + .00006*Math.pow(t,3))*(Math.PI/180)/3600;
        bigpi = (174.876384 + (3289.4789*bigt + .60622*Math.pow(bigt,2) - (869.8089 + .50491*bigt)*t + .03536*Math.pow(t,2))/3600)*(Math.PI/180);
        p = ((5029.0966 + 2.22226*bigt - .000042*Math.pow(bigt,2))*t + (1.11113 - .000042*bigt)*Math.pow(t,2) - .000006*Math.pow(t,3))*(Math.PI/180)/3600;
        }

    else {
        /*  Precess within the FK4 catalog  */
        tau0 = (jd_equinox1 - 2415020.313)/365242.1897;
        tau = (jd_equinox2 - 2415020.313)/365242.1897;
        t = tau - tau0;
        eta = ((471.07 - 6.75*tau0 + .57*Math.pow(tau0,2))*t + (-3.37 + 0.57*tau0)*Math.pow(t,2) + .05*Math.pow(t,3))*(Math.PI/180)/3600;
        bigpi = (173.950833 + (32869*tau0 + 56*Math.pow(tau0,2) - (8694 + 55*tau0)*t + 3*Math.pow(t,2))/3600)*(Math.PI/180);
        p = ((50256.41 + 222.29*tau0 + 0.26*Math.pow(tau0,2))*t + (111.15 + 0.26*tau0)*Math.pow(t,2) + 0.1*Math.pow(t,3))*(Math.PI/180)/3600;
        }

    psi = bigpi + p;
    icos = Math.cos(oldi)*Math.cos(eta) + Math.sin(oldi)*Math.sin(eta)*Math.cos(oldomega - bigpi);
    a = Math.sin(oldi)*Math.sin(oldomega - bigpi);
    b = -Math.sin(eta)*Math.cos(oldi) + Math.cos(eta)*Math.sin(oldi)*Math.cos(oldomega - bigpi);
    omega_minus_psi = Math.atan2(a,b);
    omega = omega_minus_psi + psi;

    while (omega < 0)
        omega = omega + 2*Math.PI;
    while (omega > 2*Math.PI)
        omega = omega - 2*Math.PI;

    isin = a/Math.sin(omega - psi);
    I = Math.atan2(isin, icos);
    deltawsin = -Math.sin(eta)*Math.sin(oldomega - bigpi)/Math.sin(I);
    deltawcos = (Math.sin(oldi)*Math.cos(eta) - Math.cos(oldi)*Math.sin(eta)*Math.cos(oldomega - bigpi))/Math.sin(I);
    deltaw = Math.atan2(deltawsin, deltawcos);
    w = oldw + deltaw;

    while (w < 0)
        w = w + 2*Math.PI;
    while (w > 2*Math.PI)
        w = w - 2*Math.PI;

    /*  Pack elements for return  */
    I_w_omega[1] = I;
    I_w_omega[2] = w;
    I_w_omega[3] = omega;

    }

    /*
       Procedure to convert a position/velocity state vector into Keplerian orbital elements.

       r[] is the position vector (3 entries)
       rprime[] is the velocity vector (3 entries)
       classical_elements[] contains the following:
         a is the semimajor-axis in A.U.s
         e is the eccentricity
         I is the inclination in radians
         w is the argument of perihelion in radians
         omega is the longitude of the ascending node in radians
         peritime is the Julian date of last perihelion before epoch
         mean is the mean anomaly at epoch in radians
         q is the perihelion distance in A.U.s
       jultime1 is the Julian date of epoch
       h is the angular momentum vector (3 entries)
       vectorn is the vector in the orbital plane from the center to the ascending node (3 entries)
       vectore is the vector in the orbital plane from the center toward periapsis, with magnitude e (3 entries)

       Formulas taken from Bate, Mueller, White, "Fundamentals of Astrodynamics", pp 61-63, pg 188; Pedro Ramon Escobal, "Methods of Orbit Determination"; and Jean Meeus, "Astronomical Algorithms", pp. 225-227.

       Tested and verified 4 June 1999.

       NOTE: For comparative output, we require that 0 <= w, omega, and mean <= 2*pi

     */
     public void convertStateVectorToKeplerianElements(double r[],double rprime[],double classical_elements[],double jultime1) {

    double[] h = new double[4];
    double[] n = new double[4];
    double[] vectore = new double[4];


    double magh = 0, magn = 0, rdotv = 0, magr = 0, magv = 0, p = 0, e = 0, a = 0, q = 0;
    double icos = 0, isin = 0, i = 0, omegacos = 0, omegasin = 0, omega = 0, wcos = 0, wsin = 0;
    double w = 0, nucos = 0, nusin = 0, nu = 0;
    double eccentriccos = 0, eccentricsin = 0, eccentric = 0;
    double mean_anomaly = 0, mean_motion = 0;

    int k = 0;

    h[1] = r[2]*rprime[3] - r[3]*rprime[2];
    h[2] = -(r[1]*rprime[3] - r[3]*rprime[1]);
    h[3] = r[1]*rprime[2] - r[2]*rprime[1];
    magh = Math.sqrt(Math.pow(h[1],2) + Math.pow(h[2],2) + Math.pow(h[3],2));

    n[1] = -h[2];
    n[2] = h[1];
    n[3] = 0;
    magn = Math.sqrt(Math.pow(n[1],2) + Math.pow(n[2],2) + Math.pow(n[3],2));

    rdotv = r[1]*rprime[1] + r[2]*rprime[2] + r[3]*rprime[3];
    magr = Math.sqrt(Math.pow(r[1],2) + Math.pow(r[2],2) + Math.pow(r[3],2));
    magv = Math.sqrt(Math.pow(rprime[1],2) + Math.pow(rprime[2],2) + Math.pow(rprime[3],2));

    for (k = 1; k <= 3; k++)
        vectore[k] = ((Math.pow(magv,2) - mu/magr)*r[k] - rdotv*rprime[k])/mu;

    p = Math.pow(magh,2)/mu;
    e = Math.sqrt(Math.pow(vectore[1],2) + Math.pow(vectore[2],2) + Math.pow(vectore[3],2));
    /*  Tests of parabolic orbits indicate that rounding errors can result in e not equal to 1.  Therefore, in such cases, I will force it to be 1.  */
    /* Similarly, tests of circular orbits indicate that rounding errors can result in e not equal to zero.  Therefore, in such cases, I will force it to be zero.  */
    if (Math.abs(e - 1.0) < 1e-15) e = 1.0;
    if (Math.abs(e - 0.0) < 1e-15) e = 0.0;

    if (e != 1) {
        /*  Escobal p. 74  */
        a = p/(1 - Math.pow(e,2));
        q = a*(1 - e);
        }
    else {
        /*  Escobal p. 91  */
        q = p/2;
        }

    icos = h[3]/magh;
    isin = Math.sqrt(1 - Math.pow(icos,2));
    i = Math.atan2(isin, icos);

    if (i != 0.0) {
        omegacos = n[1]/magn;
        omegasin = Math.sqrt(1 - Math.pow(omegacos,2));
        if (n[2] <= 0)
            omegasin = -omegasin;
        omega = Math.atan2(omegasin, omegacos);
        while (omega < 0.0)
            omega = omega + 2.0*Math.PI;
        }
    else
        omega = 0.0;

    if (e != 0.0) {
        if (i != 0.0) {
            /* w is angle between line of nodes and perihelion */
            wcos = (n[1]*vectore[1] + n[2]*vectore[2] + n[3]*vectore[3])/(magn*e);
            wsin = Math.sqrt(1 - Math.pow(wcos,2));
            if (vectore[3] <= 0)
                wsin = -wsin;
            w = Math.atan2(wsin, wcos);
            while (w < 0.0)
                w = w + 2.0*Math.PI;
            }
        else {
            /* w is angle between I and perihelion */
            wcos = vectore[1]/e;
            wsin = Math.sqrt(1 - Math.pow(wcos,2));
            if (vectore[3] <= 0)
                wsin = -wsin;
            w = Math.atan2(wsin, wcos);
            while (w < 0.0)
                w = w + 2.0*Math.PI;
            }
        }
    else
        /* w is undefined */
        w = 0.0;

    if (e != 0.0) {
        /* nu is angle from perihelion to r */
        nucos = (r[1]*vectore[1] + r[2]*vectore[2] + r[3]*vectore[3])/(magr*e);
        nusin = Math.sqrt(1 - Math.pow(nucos,2));
        if (rdotv <= 0)
            nusin = -nusin;
        nu = Math.atan2(nusin, nucos);
        }
    else if (i != 0) {
        /* nu is angle from line of nodes to r */
        nucos = (r[1]*n[1] + r[2]*n[2] + r[3]*n[3])/(magr*magn);
        nusin = Math.sqrt(1 - Math.pow(nucos,2));
        if (rdotv <= 0)
            nusin = -nusin;
        nu = Math.atan2(nusin, nucos);
        }
    else {
        /* nu is angle from I to r */
        nucos = r[1]/magr;
        nusin = r[2]/magr;
        nu = Math.atan2(nusin, nucos);
        }
    classical_elements[0] = nu;

    /*  Determine mean anomaly and time of perihelion  */
    if (e < 1) {
        /*  Escobal p. 86  */
        eccentriccos = (e + Math.cos(nu))/(1 + e*Math.cos(nu));
        eccentricsin = Math.sqrt(1 - Math.pow(eccentriccos,2));
        if (Math.sin(nu) < 0)
            eccentricsin = -eccentricsin;
        eccentric = Math.atan2(eccentricsin, eccentriccos);
        mean_anomaly = eccentric - e*Math.sin(eccentric);

        mean_motion = kappa*Math.sqrt(mu/Math.pow(a,3));
        /* Forced to reuse variables */
        nu = jultime1 - mean_anomaly/mean_motion;
        }
    else if (e == 1) {
        /*  Meeus pp. 225-227.  */
        /* Forced to reuse variables */
        eccentric = Math.tan(nu/2);
        eccentricsin = Math.pow(eccentric,3) + 3*eccentric;
        nu = jultime1 - eccentricsin*Math.pow(q,1.5)/(3*kappa/Math.sqrt(2));
        }
    else {
        /*  Escobal p. 92, BMW p. 188.  */
        /* Forced to reuse variables */
        eccentric = (e + Math.cos(nu))/(1 + e*Math.cos(nu));
        eccentriccos = Math.log(eccentric + Math.sqrt(Math.pow(eccentric,2) - 1));
        if (nu < 0.0)
            eccentriccos = -Math.abs(eccentriccos);
        else
            eccentriccos = Math.abs(eccentriccos);
        eccentricsin = (Math.exp(eccentriccos) - Math.exp(-eccentriccos))/2;
        mean_anomaly = e*eccentricsin - eccentriccos;
        mean_motion = kappa*Math.sqrt(mu/Math.pow((-a),3));
        /* Forced to reuse variables */
        nu = jultime1 - mean_anomaly/mean_motion;
        }
    while (mean_anomaly < 0.0)
        mean_anomaly = mean_anomaly + 2.0*Math.PI;

    /*  Pack classical elements into array  */
    classical_elements[1] = a;
    classical_elements[2] = e;
    classical_elements[3] = i;
    classical_elements[4] = w;
    classical_elements[5] = omega;
    classical_elements[6] = nu;
    classical_elements[7] = mean_anomaly;
    classical_elements[8] = q;

    }

    /*

       Procedure to calculate the position vector r[] and velocity vector rprime[] of a satellite, given the classical Keplerian elements.

       Algorithm taken from Bate, Mueller, and White, "Fundamentals of Astrodynamics", pp 72-73, 212-217, Pedro Ramon Escobal, "Methods of Orbit Determination", pp. 73-85, 90-92, 118-119, 433, and Jean Meeus, "Astronomical Algorithms", pp. 225-227.

       r[] is the position vector (3 entries)
       rprime[] is the velocity vector (3 entries)
       p[] is the unit vector in the perifocal system toward periapsis (3 entries)
       q[] is the unit vector in the perifocal system perpendicular to p[], with +q in the direction of revolution (3 entries)

       Tested and verified 4 June 1999.

     */
    public void convertKeplerianElementsToStateVectorForAsteroidEphemeris(double classical_elements[], double r[], double rprime[], double jultime1) {

     double a = 0, e = 0, I = 0, w = 0, omega = 0, peritime = 0, mean = 0, q_element = 0;
    double p_element = 0, eccentric = 0, rmag = 0, nucos = 0, nusin = 0, nu = 0;
    double big_w = 0, s = 0, f = 0, fcosh = 0, fsinh = 0, olds = 0, diff = 1;
    double[] p = new double[4];
    double[] q = new double[4];
    int k = 0;

    /* Unpack the classical element array */
    a = classical_elements[1];
    e = classical_elements[2];
    I = classical_elements[3];
    w = classical_elements[4];
    omega = classical_elements[5];
    peritime = classical_elements[6];
    mean = classical_elements[7];
    q_element = classical_elements[8];

    if (e != 1)
        /*  Escobal p. 74  */
        p_element = a*(1 - Math.pow(e,2));
    else
        /*  Escobal p. 91  */
        p_element = 2*q_element;

    /*  Escobal p. 79  */
    p[1] = Math.cos(w)*Math.cos(omega) - Math.sin(w)*Math.sin(omega)*Math.cos(I);
    p[2] = Math.cos(w)*Math.sin(omega) + Math.sin(w)*Math.cos(omega)*Math.cos(I);
    p[3] = Math.sin(w)*Math.sin(I);
    q[1] = -Math.sin(w)*Math.cos(omega) - Math.cos(w)*Math.sin(omega)*Math.cos(I);
    q[2] = -Math.sin(w)*Math.sin(omega) + Math.cos(w)*Math.cos(omega)*Math.cos(I);
    q[3] = Math.cos(w)*Math.sin(I);

    if (e < 1) {
        eccentric = keplerSolver(e, mean);
        /*  Escobal p. 85  */
        rmag = a*(1 - e*Math.cos(eccentric));
        /*  Escobal p. 85  */
        nucos = (e - Math.cos(eccentric))/(e*Math.cos(eccentric) - 1);
        /*  Escobal p. 85  */
        nusin = a*Math.sqrt(1 - Math.pow(e,2))*Math.sin(eccentric)/rmag;
        nu = Math.atan2(nusin, nucos);
        }
    else if (e == 1) {
        /*  Meeus pp. 225-227  */
        big_w = (3*kappa/Math.sqrt(2.0))*(jultime1 - peritime)/Math.pow(q_element,1.5);
        while (diff > 1e-12) {
            olds = s;
            s = (2*Math.pow(s,3) + big_w)/(3*(Math.pow(s,2) + 1));
            diff = Math.abs(s - olds);
            }
        rmag = q_element*(1 + Math.pow(s,2));
        nu = 2*Math.atan(s);
        }
    else if (e > 1) {
        f = hyperbolicKeplerSolver(e,mean);
        fcosh = (Math.exp(f) + Math.exp(-f))/2;
        fsinh = (Math.exp(f) - Math.exp(-f))/2;
        /*  Escobal p. 90  */
        rmag = a*(1 - e*fcosh);
        /*  Escobal p. 118  */
        nucos = (fcosh - e)/(1 - e*fcosh);
        /*  Escobal p. 118  */
        nusin = Math.sqrt(Math.pow(e,2) - 1)*fsinh / (e*fcosh - 1);
        nu = Math.atan2(nusin, nucos);
        }

    for (k = 1; k <= 3; k++) {
        /*  Escobal p. 119 and Bate, Mueller, and White pp. 72-73 */
        r[k] = rmag*Math.cos(nu)*p[k] + rmag*Math.sin(nu)*q[k];
        rprime[k] = Math.sqrt(mu/p_element)*(-Math.sin(nu)*p[k] + (e + Math.cos(nu))*q[k]);
        }

    }

    /*
      Program to solve Kepler's equation (hyperbolic case) for f, given the eccentricity and mean anomaly
      Algorithm from Bate, Mueller, and White, "Fundamentals of Astrodynamics", p. 221, and Pedro Ramon Escobal, "Methods of Orbit Determination", p. 92.

      Tested and Verified 31 May 1999.
    */
    public double hyperbolicKeplerSolver(double e, double mean) {

    double f = 0, oldf = 0, diff = 0;

    oldf = mean;
    f = oldf + (mean - (e*((Math.exp(oldf) - Math.exp(-oldf))/2) - oldf))/(e*((Math.exp(oldf) + Math.exp(-oldf))/2) - 1);
    diff = Math.abs(oldf - f);

    while (diff >= 1e-11) {
        oldf = f;
        f = oldf + (mean - (e*((Math.exp(oldf) - Math.exp(-oldf))/2) - oldf))/(e*((Math.exp(oldf) + Math.exp(-oldf))/2) - 1);
        diff = Math.abs(oldf - f);
        }

    return f;

    }

    /*
      Procedure to solve Kepler's equation (elliptic case) for the eccentric anomaly, given the eccentricity and mean anomaly

      Tested and Verified 31 May 1999.
    */
    public double keplerSolver(double e, double mean) {

    double oldeccentric = 0, diff = 0, eccentric = 0;

    oldeccentric = mean;
    eccentric = oldeccentric - (oldeccentric - e*Math.sin(oldeccentric) - mean)/(1 - e*Math.cos(oldeccentric));
    diff = Math.abs(oldeccentric - eccentric);

    while (diff >= 1e-11) {
        oldeccentric = eccentric;
        eccentric = oldeccentric - (oldeccentric - e*Math.sin(oldeccentric) - mean)/(1 - e*Math.cos(oldeccentric));
        diff = Math.abs(oldeccentric - eccentric);
        }

    return eccentric;

    }

 }
