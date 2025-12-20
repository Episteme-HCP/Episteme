package org.jscience.astronomy.solarsystem;

import java.io.*;


/**
 * The EphemerisUtils class provides several utilities related to astronomy in the solar system.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */


public class EphemerisUtils {

    /*
        Method to calculate the barycentric equatorial rectangular position and velocity (in AU and AU/day, referred to the mean equator and equinox of J2000.0) of an observer on Earth at a specified julian TDB date.
        The inputs:
          geodetic latitude and east longitude are in radians,
          altitude is in meters,
          EOP is a vector containing the Earth Orientation Parameters xp, yp, (UT1 - TAI), delta psi, delta epsilon, mean obliquity, and Earth angular velocity;
        and the outputs:
          observer_r and observer_rprime are barycentric rectangular, referred to the mean equator and equinox of J2000.
        Note: This method is intended to support the accuracy requirements of both optical and radar observations.
        Source is P. Kenneth Seidelmann, "Explanatory Supplement to the Astronomical Almanac", pp.161-164.
        Tested and verified 12-5-99.
      */
     public void getObserverPosition(double latitude, double longitude, double altitude, double jultime, double EOP[], double observer_r[], double observer_rprime[]) {

    double delta_t = 0, ut1_time = 0, C = 0, S = 0, delta_psi = 0, mean_obliquity = 0, true_obliquity = 0, mean_sid_time = 0, apparent_sid_time = 0, bigT = 0, delta_time = 0;

    double[] nutation_vector = new double[4];
    double[][] nutation_matrix = new double[4][4];
    double[][] matrix_P = new double[4][4];
    double[] dummy_r = new double[4];
    double[][] rotation_matrix = new double[4][4];
    double[] rotated_vector = new double[4];
    double[] g = new double[4];
    double[] gprime = new double[4];

    int i = 0, j = 0, k = 0;


    /* Note: This method will mirror the algorithm in section 3.35 of Explanatory Supplement to the Astronomical Almanac */
    /* Note: This method has been provided values of the Earth Orientation Parameters (EOP).  They may be precise values entered by the user; in this case, we don't want to recalculate them.  On the other hand, they may be set to zero, in which case we do need to calculate them or use defaults.  We will test each in turn.  */
    /* If xp and yp are unknown, they will be set to zero, in which case we would default to zero.  If they are known, we would use them as is, so no test is needed for these parameters. */
    if (EOP[3] == 0.0) {
        /* (UT1 - TAI) is unknown.  Use leap_second look-up table. */
        EOP[3] = -AstronomicalCoordinatesUtils.getLeapSecond(jultime);
        }
    /* If (UT1 - TAI) is known, use it.  */
    delta_t = 32.184 - EOP[3];

    /* Section 3.351 Step a: Determine the UT1 time of observation */
    ut1_time = AstronomicalCoordinatesUtils.convertTDBToTDT(jultime,latitude,longitude,altitude,EOP[3]) - delta_t/86400.0;

    /* Section 3.351 Step b: Obtain the position vector of the observer in an Earth-fixed, geocentric coordinate system */
    C = 1.0/Math.sqrt(Math.pow(Math.cos(latitude),2) + Math.pow((1.0 - flattening),2)*Math.pow(Math.sin(latitude),2));
    S = Math.pow((1.0 - flattening),2)*C;
    observer_r[1] = (earth_radius*C + altitude)*Math.cos(latitude)*Math.cos(longitude);
    observer_r[2] = (earth_radius*C + altitude)*Math.cos(latitude)*Math.sin(longitude);
    observer_r[3] = (earth_radius*S + altitude)*Math.sin(latitude);

    /* Section 3.352 Step c: Obtain the nutation angles, and the mean and true obliquity; calculate the nutation matrix. */
    /* Note: These values may already have been sent in the EOP argument.  If any are zero, we will estimate them; if not, we will use the values as given.  */
    if ((EOP[4] == 0.0) || (EOP[5] == 0.0) || (EOP[6] == 0.0)) {
        /* Use the nutation method to estimate the missing parameters.  */
        AstronomicalCoordinatesUtils.getNutation(jultime,nutation_vector);
        if (EOP[4] == 0.0)
            EOP[4] = nutation_vector[1];
        if (EOP[5] == 0.0)
            EOP[5] = nutation_vector[2];
        if (EOP[6] == 0.0)
            EOP[6] = nutation_vector[3];
        }
    /* Calculate the entries in the nutation matrix, which represents a sequence of three rotations, that transform equatorial coordinates from the mean equinox and equator of date to the true equinox and equator of date.  */
    delta_psi = EOP[4];
    mean_obliquity = EOP[6];
    true_obliquity = EOP[5] + EOP[6];
    nutation_matrix[1][1] = Math.cos(delta_psi);
    nutation_matrix[1][2] = -Math.sin(delta_psi)*Math.cos(mean_obliquity);
    nutation_matrix[1][3] = -Math.sin(delta_psi)*Math.sin(mean_obliquity);
    nutation_matrix[2][1] = Math.sin(delta_psi)*Math.cos(true_obliquity);
    nutation_matrix[2][2] = Math.cos(delta_psi)*Math.cos(true_obliquity)*Math.cos(mean_obliquity) + Math.sin(true_obliquity)*Math.sin(mean_obliquity);
    nutation_matrix[2][3] = Math.cos(delta_psi)*Math.cos(true_obliquity)*Math.sin(mean_obliquity) - Math.sin(true_obliquity)*Math.cos(mean_obliquity);
    nutation_matrix[3][1] = Math.sin(delta_psi)*Math.sin(true_obliquity);
    nutation_matrix[3][2] = Math.cos(delta_psi)*Math.sin(true_obliquity)*Math.cos(mean_obliquity) - Math.cos(true_obliquity)*Math.sin(mean_obliquity);
    nutation_matrix[3][3] = Math.cos(delta_psi)*Math.sin(true_obliquity)*Math.sin(mean_obliquity) + Math.cos(true_obliquity)*Math.cos(mean_obliquity);

    /* Section 3.352 Step d: Compute the Greenwich mean and apparent sidereal time at ut1_time */
    bigT = (ut1_time - 2451545.0)/36525;
    mean_sid_time = 67310.54841 + (876600.0*3600.0 + 8640184.812866)*bigT + 0.093104*Math.pow(bigT,2) - 6.2E-06*Math.pow(bigT,3);
    mean_sid_time = (mean_sid_time/3600)*Math.PI/12;
    apparent_sid_time = mean_sid_time + EOP[4]*Math.cos(EOP[5] + EOP[6]);

    /* Section 3.353 Step e: Compute the geocentric position and velocity vectors of the observer, with respect to the true equator and equinox of date */
    /* Test to see if the Earth's angular velocity has been provided.  If not, use an estimate derived from Meeus p. 73. and "Explanatory Supplement, p. 51 */
    if (EOP[7] == 0.0) {
        /* delta_time is the rate of change of (UT1 - TAI) in seconds per day */
        delta_time = -(123.5 + 2.0*32.5*bigT)/36525;
        EOP[7] = (86400.0/(86400.0 - delta_time))*72.921151467E-06;
        }
    /* Rotate the observer vector xp radians through y-axis */
    rotation_matrix[1][1] = Math.cos(EOP[1]);
    rotation_matrix[1][2] = 0;
    rotation_matrix[1][3] = -Math.sin(EOP[1]);
    rotation_matrix[2][1] = 0;
    rotation_matrix[2][2] = 1;
    rotation_matrix[2][3] = 0;
    rotation_matrix[3][1] = Math.sin(EOP[1]);
    rotation_matrix[3][2] = 0;
    rotation_matrix[3][3] = Math.cos(EOP[1]);
    for (i=1;i<=3;i++) {
        rotated_vector[i] = 0;
        for (j=1;j<=3;j++)
            rotated_vector[i] = rotated_vector[i] + rotation_matrix[i][j]*observer_r[j];
        }
    for (i=1;i<=3;i++)
        g[i] = rotated_vector[i];
    /* Rotate the g vector yp radians through x-axis */
    rotation_matrix[1][1] = 1;
    rotation_matrix[1][2] = 0;
    rotation_matrix[1][3] = 0;
    rotation_matrix[2][1] = 0;
    rotation_matrix[2][2] = Math.cos(EOP[2]);
    rotation_matrix[2][3] = Math.sin(EOP[2]);
    rotation_matrix[3][1] = 0;
    rotation_matrix[3][2] = -Math.sin(EOP[2]);
    rotation_matrix[3][3] = Math.cos(EOP[2]);
    for (i=1;i<=3;i++) {
        rotated_vector[i] = 0;
        for (j=1;j<=3;j++)
            rotated_vector[i] = rotated_vector[i] + rotation_matrix[i][j]*g[j];
        }
    for (i=1;i<=3;i++)
        g[i] = rotated_vector[i];
    /* Compute the observer velocity vector */
    rotation_matrix[1][1] = -Math.sin(apparent_sid_time);
    rotation_matrix[1][2] = -Math.cos(apparent_sid_time);
    rotation_matrix[1][3] = 0;
    rotation_matrix[2][1] = Math.cos(apparent_sid_time);
    rotation_matrix[2][2] = -Math.sin(apparent_sid_time);
    rotation_matrix[2][3] = 0;
    rotation_matrix[3][1] = 0;
    rotation_matrix[3][2] = 0;
    rotation_matrix[3][3] = 0;
    for (i=1;i<=3;i++) {
        rotated_vector[i] = 0;
        for (j=1;j<=3;j++)
            rotated_vector[i] = rotated_vector[i] + rotation_matrix[i][j]*g[j];
        }
    /* Step f: Convert to AU/day */
    for (i=1;i<=3;i++)
        gprime[i] = EOP[7]*rotated_vector[i]*86400.0/(au*1000.0);
    /* Rotate the g vector (-apparent_sid_time) radians through z-axis */
    rotation_matrix[1][1] = Math.cos(-apparent_sid_time);
    rotation_matrix[1][2] = Math.sin(-apparent_sid_time);
    rotation_matrix[1][3] = 0;
    rotation_matrix[2][1] = -Math.sin(-apparent_sid_time);
    rotation_matrix[2][2] = Math.cos(-apparent_sid_time);
    rotation_matrix[2][3] = 0;
    rotation_matrix[3][1] = 0;
    rotation_matrix[3][2] = 0;
    rotation_matrix[3][3] = 1;
    for (i=1;i<=3;i++) {
        rotated_vector[i] = 0;
        for (j=1;j<=3;j++)
            rotated_vector[i] = rotated_vector[i] + rotation_matrix[i][j]*g[j];
        }
    /* Step f: Convert to AUs */
    for (i=1;i<=3;i++)
        g[i] = rotated_vector[i]/(au*1000.0);

    /* Section 3.354 Step g: Transform observer position and velocity vectors to mean equator and equinox of J2000.0 */
    /* Multiply g by inverse (transpose) of nutation matrix */
    for (i=1;i<=3;i++) {
        rotated_vector[i] = 0;
        for (j=1;j<=3;j++)
            rotated_vector[i] = rotated_vector[i] + nutation_matrix[j][i]*g[j];
        }
    for (i=1;i<=3;i++)
        g[i] = rotated_vector[i];
    /* Multiply g by inverse (transpose) of precession matrix */
    AstronomicalCoordinatesUtils.precessionMatrix(jultime,matrix_P);
    for (i=1;i<=3;i++) {
        rotated_vector[i] = 0;
        for (j=1;j<=3;j++)
            rotated_vector[i] = rotated_vector[i] + matrix_P[j][i]*g[j];
        }
    for (i=1;i<=3;i++)
        g[i] = rotated_vector[i];
    /* Multiply gprime by inverse (transpose) of nutation matrix */
    for (i=1;i<=3;i++) {
        rotated_vector[i] = 0;
        for (j=1;j<=3;j++)
            rotated_vector[i] = rotated_vector[i] + nutation_matrix[j][i]*gprime[j];
        }
    for (i=1;i<=3;i++)
        gprime[i] = rotated_vector[i];
    /* Multiply gprime by inverse (transpose) of precession matrix */
    for (i=1;i<=3;i++) {
        rotated_vector[i] = 0;
        for (j=1;j<=3;j++)
            rotated_vector[i] = rotated_vector[i] + matrix_P[j][i]*gprime[j];
        }
    for (i=1;i<=3;i++)
        gprime[i] = rotated_vector[i];

    /* Section 3.355 Step h: Find the Earth's barycentric rectangular position and velocity at TDB jultime, add that to the observer's position and velocity, and get the barycentric rectangular position and velocity of the observer at jultime */
    planetary_ephemeris(jultime,dummy_r,0);
    for (j=1;j<=3;j++) {
        observer_r[j] = g[j] + planet_r[3][j];
        observer_rprime[j] = gprime[j] + planet_rprime[3][j];
        }

    }

    /*
       Method to calculate the barycentric equatorial positions and velocities of the 300 most important asteroids at jultime.
       The output barycentric equatorial pos/vel are placed in the planet_r and planet_rprime arrays beginning in slot 11; they can then be used by get_acceleration to calculate gravitational perturbations.
       The input orbital elements are wrt the mean ecliptic and equinox of J2000; they were calculated by the author.
     */
   public static void asteroidEphemeris(double jultime) {

    double[] asteroid_r = new double[4];
    double[] asteroid_rprime = new double[4];
    double[] classical_elements = new double[7];
    double[] sun_r = new double[4];
    double[] sun_rprime = new double[4];
    double[] p = new double[4];
    double[] q = new double[4];

    double mean_motion = 0, asteroid_epoch_time = 0;
    double sitexrect = 0, siteyrect = 0, sitezrect = 0;
    double p_element = 0, eccentric = 0, rmag = 0, nucos = 0, nusin = 0, nu = 0;
    double oldeccentric = 0, diff = 0;

    int j = 0, k = 0, index = 0;

    /* Determine the index corresponding to jultime */
    if (jultime >= 2453775.0)
        index = (int)(Math.floor((jultime - 2378495.0 - 0.00001)/40.0) + 1);
    else
        index = (int)(Math.floor((jultime - 2378495.0)/40.0) + 2);
    if (index < 1) index = 1;
    if (index > 3654) index = 3654;

    asteroid_epoch_time = (index - 1)*40.0 + 2378495.0;

    /* Calculate the position and velocity of the Sun at jultime, for use below */
    get_planet_posvel(jultime,11,sun_r,sun_rprime);

    /* For each asteroid, calculate pos/vel at jultime */
    for (j=1;j<=(number_of_planets-10);j++) {

        /*  Obtain the orbital elements of asteroid j at jultime */
        classical_elements[1] = asteroid_ephemeris[index][j][1];
        classical_elements[2] = asteroid_ephemeris[index][j][2];
        classical_elements[3] = asteroid_ephemeris[index][j][3];
        classical_elements[4] = asteroid_ephemeris[index][j][4];
        classical_elements[5] = asteroid_ephemeris[index][j][5];
        classical_elements[6] = asteroid_ephemeris[index][j][6];

        /* Propogate the mean anomaly from asteroid_epoch_time to jultime */
        mean_motion = kappa*Math.sqrt(mu)/Math.pow(classical_elements[1],1.5);
        classical_elements[6] = classical_elements[6] + mean_motion*(jultime - asteroid_epoch_time);

        /* The elements must be converted into a state vector and rotated into heliocentric equatorial space */
        /* ae_convert_Keplerian_elements_to_state_vector(classical_elements,asteroid_r,asteroid_rprime,jultime); */
        p_element = classical_elements[1]*(1.0 - Math.pow(classical_elements[2],2));
        p[1] = Math.cos(classical_elements[4])*Math.cos(classical_elements[5]) - Math.sin(classical_elements[4])*Math.sin(classical_elements[5])*Math.cos(classical_elements[3]);
        p[2] = Math.cos(classical_elements[4])*Math.sin(classical_elements[5]) + Math.sin(classical_elements[4])*Math.cos(classical_elements[5])*Math.cos(classical_elements[3]);
        p[3] = Math.sin(classical_elements[4])*Math.sin(classical_elements[3]);
        q[1] = -Math.sin(classical_elements[4])*Math.cos(classical_elements[5]) - Math.cos(classical_elements[4])*Math.sin(classical_elements[5])*Math.cos(classical_elements[3]);
        q[2] = -Math.sin(classical_elements[4])*Math.sin(classical_elements[5]) + Math.cos(classical_elements[4])*Math.cos(classical_elements[5])*Math.cos(classical_elements[3]);
        q[3] = Math.cos(classical_elements[4])*Math.sin(classical_elements[3]);
        /* eccentric = Kepler_solver(classical_elements[2], classical_elements[6]);  */
        oldeccentric = classical_elements[6];
        eccentric = oldeccentric - (oldeccentric - classical_elements[2]*Math.sin(oldeccentric) - classical_elements[6])/(1 - classical_elements[2]*Math.cos(oldeccentric));
        diff = Math.abs(oldeccentric - eccentric);
        while (diff >= 1e-11) {
            oldeccentric = eccentric;
            eccentric = oldeccentric - (oldeccentric - classical_elements[2]*Math.sin(oldeccentric) - classical_elements[6])/(1 - classical_elements[2]*Math.cos(oldeccentric));
            diff = Math.abs(oldeccentric - eccentric);
            }
        /*  Escobal p. 85  */
        rmag = classical_elements[1]*(1 - classical_elements[2]*Math.cos(eccentric));
        /*  Escobal p. 85  */
        nucos = (classical_elements[2] - Math.cos(eccentric))/(classical_elements[2]*Math.cos(eccentric) - 1);
        /*  Escobal p. 85  */
        nusin = classical_elements[1]*Math.sqrt(1 - Math.pow(classical_elements[2],2))*Math.sin(eccentric)/rmag;
        nu = Math.atan2(nusin, nucos);
        for (k = 1; k <= 3; k++) {
            /*  Escobal p. 119 and Bate, Mueller, and White pp. 72-73 */
            asteroid_r[k] = rmag*Math.cos(nu)*p[k] + rmag*Math.sin(nu)*q[k];
            asteroid_rprime[k] = Math.sqrt(mu/p_element)*(-Math.sin(nu)*p[k] + (classical_elements[2] + Math.cos(nu))*q[k]);
            }
        /* ecliptic_to_equatorial(asteroid_r);  */
        sitexrect = asteroid_r[1];
        siteyrect = asteroid_r[2]*Math.cos(epsilon) - asteroid_r[3]*Math.sin(epsilon);
        sitezrect = asteroid_r[2]*Math.sin(epsilon) + asteroid_r[3]*Math.cos(epsilon);
        asteroid_r[1] = sitexrect;
        asteroid_r[2] = siteyrect;
        asteroid_r[3] = sitezrect;
        /* ecliptic_to_equatorial(asteroid_rprime);  */
        sitexrect = asteroid_rprime[1];
        siteyrect = asteroid_rprime[2]*Math.cos(epsilon) - asteroid_rprime[3]*Math.sin(epsilon);
        sitezrect = asteroid_rprime[2]*Math.sin(epsilon) + asteroid_rprime[3]*Math.cos(epsilon);
        asteroid_rprime[1] = sitexrect;
        asteroid_rprime[2] = siteyrect;
        asteroid_rprime[3] = sitezrect;

        /* Two-body pos/vel is heliocentric; convert to barycentric.  Set the Newtonian acceleration of each asteroid to zero. */
        for (k=1;k<=3;k++) {
            planet_r[10+j][k] = asteroid_r[k] + sun_r[k];
            planet_rprime[10+j][k] = kappa*asteroid_rprime[k] + sun_rprime[k];
            planet_r2prime[10+j][k] = 0.0;
            }

        }

    }

    /*
     Procedure to read from disk (file "asteroid_ephemeris.txt") an array of the orbital elements of the 300 DE405 minor planets.
     */
     public void readAsteroidEphemeris() {

     int i = 0, j = 0, k = 0;

     String line = " ";

     try {
         /* Read asteroid_ephemeris file from disk */
         FileReader file1 = new FileReader("asteroid_ephemeris.txt");
         BufferedReader buff1 = new BufferedReader(file1);

     /*	for (i = 1; i <= 1883; i++) {		*/
         for (i = 1; i <= 3653; i++) {
             for (j = 1; j <= 300; j++) {
                 for (k = 1; k <= 6; k++) {
                     line = buff1.readLine();
                     asteroid_ephemeris[i][j][k] = Double.valueOf(line).doubleValue();
                     }
                 }
             }

         buff1.close();
         } catch (FileNotFoundException exceptFNF) {
             System.out.println("Error -- " + exceptFNF.toString());
         } catch (NullPointerException exceptNP) {
             System.out.println("Error -- " + exceptNP.toString());
         } catch (EOFException exceptEOF) {
             System.out.println("Error -- " + exceptEOF.toString());
         } catch (IOException exceptIO) {
             System.out.println("Error -- " + exceptIO.toString());
             }

         }
   

}
