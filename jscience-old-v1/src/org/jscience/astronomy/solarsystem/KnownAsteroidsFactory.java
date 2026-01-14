/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.astronomy.solarsystem;

import org.jscience.astronomy.TimeUtils;

import java.io.*;

/**
 * The KnownAsteroidsFactory class provides support for the asteroids from our
 * star sytstem.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//data:
//http://pdssbn.astro.umd.edu/nodehtml/sbdb.html
//http://pdssbn.astro.umd.edu/sbnhtml/index.html
//ftp://ftp.lowell.edu/pub/elgb/astorb.html
//http://ssd.jpl.nasa.gov/horizons.html
//http://www.imcce.fr/ephem/meteor/
//models:
//http://astronomy.swin.edu.au/~pbourke/modelling/asteroid2/
//http://www.eecs.wsu.edu/~hudson/Research/Asteroids/models.html

//code taken after CODES from Jim Baer
public class KnownAsteroidsFactory extends Object {
    //ftp://ftp.lowell.edu/pub/elgb/astorb.html
    public void getAsteroids() {

        XXXXX

    }

    //CelestialBody variables

    /*  DECLARE CLASS CONSTANTS  */

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

    //SolarSystemBody variables

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

    /*  Declare Class Variables  */

    /*
       Ratio of mass of Earth to mass of Moon
     */
    static double emrat = 81.30056;

    /*
       Chebyshev coefficients for the DE405 ephemeris are contained in the files "ASCPxxxx.txt".  These files are broken into intervals of length "interval_duration", in days.
     */
    static int interval_duration = 32;

    /*
       Each interval contains an interval number, length, start and end jultimes, and Chebyshev coefficients.  We keep only the coefficients.
     */
    static int numbers_per_interval = 816;

    /*
       For each planet (and the Moon makes 10, and the Sun makes 11), each interval contains several complete sets of coefficients, each covering a fraction of the interval duration
     */
    static int number_of_coef_sets_1 = 4;
    static int number_of_coef_sets_2 = 2;
    static int number_of_coef_sets_3 = 2;
    static int number_of_coef_sets_4 = 1;
    static int number_of_coef_sets_5 = 1;
    static int number_of_coef_sets_6 = 1;
    static int number_of_coef_sets_7 = 1;
    static int number_of_coef_sets_8 = 1;
    static int number_of_coef_sets_9 = 1;
    static int number_of_coef_sets_10 = 8;
    static int number_of_coef_sets_11 = 2;

    /*
       Each planet (and the Moon makes 10, and the Sun makes 11) has a different number of Chebyshev coefficients used to calculate each component of position and velocity.
     */
    static int number_of_coefs_1 = 14;
    static int number_of_coefs_2 = 10;
    static int number_of_coefs_3 = 13;
    static int number_of_coefs_4 = 11;
    static int number_of_coefs_5 = 8;
    static int number_of_coefs_6 = 7;
    static int number_of_coefs_7 = 6;
    static int number_of_coefs_8 = 6;
    static int number_of_coefs_9 = 6;
    static int number_of_coefs_10 = 13;
    static int number_of_coefs_11 = 11;

    /*
       Values of mu for the planets, given in (AU^3)/(day^2)
       Note: planets are indexed as Sun = 0, Mercury = 1, ... , Pluto = 9, Moon = 10.
       Note: Value of kappa is used explicitly here; reference to the class constant failed
     */
    static double mu_planet_0 = Math.pow(0.01720209895, 2);
    static double mu_planet_1 = Math.pow(0.01720209895, 2) / 6023600;
    static double mu_planet_2 = Math.pow(0.01720209895, 2) / 408523.71;
    static double mu_planet_3 = Math.pow(0.01720209895, 2) / 332946.050895;
    static double mu_planet_4 = Math.pow(0.01720209895, 2) / 3098708;
    static double mu_planet_5 = Math.pow(0.01720209895, 2) / 1047.3486;
    static double mu_planet_6 = Math.pow(0.01720209895, 2) / 3497.898;
    static double mu_planet_7 = Math.pow(0.01720209895, 2) / 22902.98;
    static double mu_planet_8 = Math.pow(0.01720209895, 2) / 19412.24;
    static double mu_planet_9 = Math.pow(0.01720209895, 2) / 135200000;
    static double mu_planet_10 = Math.pow(0.01720209895, 2) / 27068700.387534;

    /*
       The equatorial radii of the nine planets (and the Sun in slot 0, and the Moon in slot 10), in A.U.s
     */
    static double planet_radius_0 = 696000 / au;
    static double planet_radius_1 = 2439 / au;
    static double planet_radius_2 = 6052 / au;
    static double planet_radius_3 = 6378.140 / au;
    static double planet_radius_4 = 3397.2 / au;
    static double planet_radius_5 = 71398 / au;
    static double planet_radius_6 = 60000 / au;
    static double planet_radius_7 = 25400 / au;
    static double planet_radius_8 = 24300 / au;
    static double planet_radius_9 = 2500 / au;
    static double planet_radius_10 = 1738 / au;

    /*  DEFINE INSTANCE VARIABLES  */

    /*  Define ephemeris-related arrays as instance variables  */
    double[] ephemeris_coefficients = new double[187681];
    double[] ephemeris_dates = new double[3];
    int[] number_of_coef_sets = new int[12];
    int[] number_of_coefs = new int[12];
    double[] planet_radius = new double[11];

    /*  Define the positions, velocities, and Newtonian accelerations of the major planets as instance variables, as well as the gravitational constants of every perturber  */
    public double[][] planet_r = new double[312][4];
    public double[][] planet_rprime = new double[312][4];
    double[][] planet_r2prime = new double[312][4];
    double[] mu_planet = new double[312];

    /*
     The following arrays contain the mean orbital elements of the 300 most important perturbing asteroids.  They are used in method asteroid_ephemeris at each time step to determine the perturbing forces on a minor planet.
     */
    double[] asteroid_a = new double[301];
    double[] asteroid_e1 = new double[301];
    double[] asteroid_e2 = new double[301];
    double[] asteroid_i1 = new double[301];
    double[] asteroid_i2 = new double[301];
    double[] asteroid_omega1 = new double[301];
    double[] asteroid_omega2 = new double[301];
    double[] asteroid_w1 = new double[301];
    double[] asteroid_w2 = new double[301];
    double[] asteroid_mean1 = new double[301];
    double[] asteroid_mean2 = new double[301];
    double[] asteroid_meanmotion = new double[301];

    /*
       Number of major and minor planets used to calculate perturbations.
       Note: Since Sun = 0 is not a perturber, this parameter is the number of planets, plus the Moon, plus minor planets (if any); in other words, 10 + (number of asteroids) .
       10 = planets plus Moon
       13 = planets, Moon, plus Ceres, Pallas, and Vesta
      310 = planets, Moon, and all available asteroids
     */
    public int number_of_planets = 13;

    /* Note: Strictly speaking, I should declare
incremental_state_vector_epoch,
optical_time,
observation_geocentric,
observation_latitude,
observation_longitude,
observation_altitude,
observation_EOP,
observation_r,
and observation_rprime
as instance variables of SolarSystemBody, since they are generically attributes of a Solar System Body class member.  However, I have chosen to group all of the instance variables relating to a minor planet together at the top of MinorPlanet.  I believe it makes things easier to keep them together.  Also, these specific arrays relate to a minor planet; a Solar System Body would use generic instance variables.  */

//MinorPlanet variables

    /*  DECLARE INSTANCE VARIABLES  */

    /* Auxiliary Variables:  */

    /* A list (mp_list) is maintained of the minor planets for whom a data file (observations and orbit) exists. */
    public int number_of_minor_planets = 0;
    public String[] mp_list = new String[1001];

    /* The current minor planet on that list */
    public String current_minor_planet = " ";


    /* In method "probability_of_collision", it is necessary to determine the state transition matrix, which essentially represents the sensitivity of the current state vector to small changes in the epoch state vector.  This requires integrating forward from epoch along eight different variant paths; rather than start from scratch for each collision/near miss event, these instance variables will be used to keep a "running count" of the variant trajectories.  */
    public double[][] collision_sensitivity_r_variant = new double[9][4];
    public double[][] collision_sensitivity_rprime_variant = new double[9][4];
    public double collision_sensitivity_time = 0;
    public double[][][] collision_state_matrix = new double[2000][9][9];
    public double[][] collision_nominal_state = new double[2000][9];


    /*
     nominal_observable is an array containing the nominal computed value of each ra, dec, delay, or doppler observation, as determined by method get_residuals.  Note that the observables treat a ra and a dec as separate, so the index will not correspond to the observation number.
     */
    public double[] nominal_observable = new double[16001];


    /*
     In response to feedback from Dr. Gerhard Hahn of DLR, I am adding a GUI-selectable non-database variable representing the threshold approach distance defining a near-miss.  The default value of 0.05 AU matches the MPC definition.
     */
    public double near_miss_threshold = 0.05;

    /*
     In searching for virtual impactors, users have encountered collisions on variant trajectories; to capitalize on these fortuitous accidents, I'm adding three flags that will control whether variant trajectories are checked for collisions.  "VIflag1" will indicate that a search for VIs is underway.  "VIflag2" is set by detect_collision to prevent the variant trajectories from being tested for collision; the absence of this flag is interpreted to mean that variant trajectories should be tested for collision.  "VIflag3" is set for variant trajectories by probability_of_collision to prevent a recursive call to probability_of_collision from within detect_collision.  "VIflag4" allows detect_collision while suppressing calls to probability_of_collision, thus avoiding the use of the six variant trajectories.  "VIflag5" is used within detect_collision to prevent bisection calls to update from resetting the VI_trajectory_r and _rprime vectors.
     */
    public boolean VIflag1 = false, VIflag2 = false, VIflag3 = false, VIflag4 = false, VIflag5 = false;

    /*
     In searching for virtual impactors, we often want the variant trajectories to be centered on a non-nominal trajectory.  These arrays will be used to store the state vector passed to update by the VI search algorithm; if the VIflag1 flag is set, probability_of_collision will center its variant trajectories around this state vector, rather than the nominal epoch state vector.
     */
    public double[] VI_trajectory_r = new double[4];
    public double[] VI_trajectory_rprime = new double[4];

    /* In method "least_squares_solution", the user can exclude observations from the final solution on the basis of excessive chi or residuals.  The "testing" flags and thresholds are set in the GUI (although default values are defined here). */
    public double chi_threshold = 2.8;
    public double optical_residual_threshold = 2.3 * Math.PI / (180.0 * 3600.0);
    public double delay_residual_threshold = 30.0E-6;
    public double doppler_residual_threshold = 30.0;
    public boolean chi_testing = true;
    public boolean residual_testing = false;
    public int[] excluded_observations = new int[8001];

    /* The GUI provides the user with the ability to evaluate collision or near-miss events with all bodies, or Earth-only.  To save processing time, the earth_only boolean will tell detect_collision which planets to test. */
    boolean earth_only = true;

    /*
     Variant collision analysis processing time is unreasonably long when the highest integration accuracy is used.  To speed processing, I will allow the user to select between high (6e-9) and medium (1e-5) integration accuracy; this selection will also change the precision with which near-miss and collision events are localized.
     */
    public double integration_error = 6e-9;
    public double event_localization = 1e-5;

    /* Instance Variables Comprising Database for Each Minor Planet */

    /* comet_identifier is a boolean that indicates whether the minor planet is a comet */
    public boolean comet_identifier = false;

    /* The following variables and arrays contain the orbital solution (state vector and covariances at epoch) for the minor planet */
    /* epoch_time is the TDB jultime to which the calculated or a'priori minor planet state vector applies */
    public double epoch_time = 0;

    /* epoch_r and epoch_rprime are the calculated or a'priori minor planet state vector */
    public double[] epoch_r = new double[4];
    public double[] epoch_rprime = new double[4];

    /*
         A1_A2_DT is an array containing parameters that model a comet's outgassing thrust.
         A1 and A2 are the radial and transverse components respectively, and are on the order of 10^(-9) AU/(day)^2.
         DT represents an offset from perihelion (in days, < 0 if before, > 0 if after) in the peak of a comet's light (and water-vaporization) curve.  It is small, on the order of 0-80 days, and is not always required.
     */
    public double[] A1_A2_DT = new double[4];

    /* big_p is the epoch error covariance matrix, either calculated using least squares, or constructed using assumed state vector covariances.  Diagonal entries 1-3 represent squared covariances in epoch position, entries 4-6 represent squared covariances in epoch velocity, and entries 7 and 8 represent squared covariances in A1 and A2.  */
    public double[][] big_p = new double[9][9];

    /* absolute_magnitude_H is the mean absolute visual magnitude of the asteroid.  */
    public double absolute_magnitude_H = 0;

    /* slope_parameter_G is used to calculate the visual magnitude of the asteroid. */
    public double slope_parameter_G = 0;

    /* comet_absolute_magnitude is the mean absolute visual magnitude of the comet */
    public double comet_absolute_magnitude = 0;

    /* comet_slope_parameter is used to calculate the visual magnitude of the comet */
    public double comet_slope_parameter = 0;

    /* The following variables and arrays contain the actual optical, radar delay and radar doppler observations of the minor planet */

    /* Note that one optical observation consists of both a ra and a dec */
    public int number_of_optical_observations = 0;
    public int number_of_delay_observations = 0;
    public int number_of_doppler_observations = 0;

    /*
     observation type is an integer array containing the type of each observation:
         = 1 for optical;
         = 2 for delay;
         = 3 for doppler;
     */
    public int[] observation_type = new int[8001];

    /*
     incremental_state_vector_epoch is an array containing the TDB times of each observation:
         = TDB time of optical observation, or
         = TDB receiver time for radar observations.
     Note: incremental_state_vector_epoch[0] = epoch_time;
     */
    public double[] incremental_state_vector_epoch = new double[8001];

    /* In method "least_squares_solution", a call to "get_residuals" requires calculation of the nominal state vector and observables at the TDB time of each observation.  These state vectors are retained for use in "least_squares" and elsewhere, so as to avoid recalculation.  */
    /*  incremental_state_vector_r is an array containing the state vector positions at the TDB times of each observation */
    public double[][] incremental_state_vector_r = new double[8001][4];
    /*  incremental_state_vector_rprime is an array containing the state vector velocities at the TDB times of each observation */
    public double[][] incremental_state_vector_rprime = new double[8001][4];

    /* OPTICAL OBSERVATIONS  */

    /* ra and dec are in radians */
    public double[] optical_ra = new double[8001];
    public double[] ra_dev = new double[8001];
    public double[] optical_dec = new double[8001];
    public double[] dec_dev = new double[8001];

    /* TDT times of optical observations */
    public double[] optical_time = new double[8001];

    /* visual_magnitude[i] is the observed visual-range brightness of the minor planet, made as part of optical observation i.  A value of "-99" means that no visual-range brightness measurement was made.  (Note that we will only solve for brightness parameters in the case of non-thrusting minor planets [asteroids]; thus, "-99" magnitude is unlikely to occur naturally.)  An uncertainty of 0.1 will be assumed for all magnitude observations. */
    public double[] visual_magnitude = new double[8001];
    public double[] visual_magnitude_dev = new double[8001];

    /* observation_geocentric is a boolean array; entry i = true iff optical observation i was geocentric */
    public boolean[] observation_geocentric = new boolean[8001];

    /* observation_latitude contains the geodetic latitude in radians from which each optical observation was made */
    public double[] observation_latitude = new double[8001];

    /* observation_longitude contains the east longitude in radians from which each optical observation was made */
    public double[] observation_longitude = new double[8001];

    /* observation_altitude contains the altitude in meters from which each optical observation was made */
    public double[] observation_altitude = new double[8001];

    /* observation_EOP[i][j] contains the Earth Orientation Parameters for optical observation i:
             - observation_EOP[i][1] contains the value of xp in radians at the time of observation i;
             - observation_EOP[i][2] contains the value of yp in radians at the time of observation i;
             - observation_EOP[i][3] contains the value of (UT1 - TAI) in seconds at the time of observation i;
             - observation_EOP[i][4] contains the value of delta psi in radians at the time of observation i;
             - observation_EOP[i][5] contains the value of delta epsilon in radians at the time of observation i;
             - observation_EOP[i][6] contains the value of mean obliquity in radians at the time of observation i;
             - observation_EOP[i][7] contains the value of Earth's angular velocity in radians per second at the time of observation i;
     */
    public double[][] observation_EOP = new double[8001][8];

    /* observation_r contains the barycentric rectangular equatorial position (in AUs) of the site from which each optical observation was made */
    public double[][] observation_r = new double[8001][4];

    /* observation_rprime contains the barycentric rectangular equatorial velocity (in AU/day) of the site from which each optical observation was made */
    public double[][] observation_rprime = new double[8001][4];

    /* DELAY OBSERVATIONS  */

    /* delay is in seconds */
    public double[] radar_delay = new double[8001];
    public double[] delay_dev = new double[8001];

    /* UTC Julian dates of radar delay receiver observations */
    public double[] radar_delay_receiver_time = new double[8001];

    /* radar_delay_receiver_latitude contains the geodetic latitude in radians at which each radar delay receiver is located */
    public double[] radar_delay_receiver_latitude = new double[8001];

    /* radar_delay_receiver_longitude contains the east longitude in radians at which each radar delay receiver is located */
    public double[] radar_delay_receiver_longitude = new double[8001];

    /* radar_delay_receiver_altitude contains the altitude in meters at which each radar delay receiver is located */
    public double[] radar_delay_receiver_altitude = new double[8001];

    /* radar_delay_receiver_EOP[i][j] contains the Earth Orientation Parameters for the receiver in radar delay observation i, in format identical to observation_EOP */
    public double[][] radar_delay_receiver_EOP = new double[8001][8];

    /* radar_delay_receiver_tdb_minus_utc contains the value of (TDB-UTC) in seconds for the receiver from which each radar delay observation was made */
    public double[] radar_delay_receiver_tdb_minus_utc = new double[8001];

    /* radar_delay_receiver_r contains the barycentric rectangular equatorial position (in AUs) of the receiver site from which each radar delay observation was made */
    public double[][] radar_delay_receiver_r = new double[8001][4];

    /* radar_delay_receiver_rprime contains the barycentric rectangular equatorial velocity (in AU/day) of the receiver site from which each radar delay observation was made */
    public double[][] radar_delay_receiver_rprime = new double[8001][4];

    /* radar_delay_transmitter_latitude contains the geodetic latitude in radians at which each radar delay transmitter is located */
    public double[] radar_delay_transmitter_latitude = new double[8001];

    /* radar_delay_transmitter_longitude contains the east longitude in radians at which each radar delay transmitter is located */
    public double[] radar_delay_transmitter_longitude = new double[8001];

    /* radar_delay_transmitter_altitude contains the altitude in meters at which each radar delay transmitter is located */
    public double[] radar_delay_transmitter_altitude = new double[8001];

    /* radar_delay_transmitter_EOP[i][j] contains the Earth Orientation Parameters for the transmitter in radar delay observation i, in format identical to observation_EOP */
    public double[][] radar_delay_transmitter_EOP = new double[8001][8];

    /* radar_delay_transmitter_tdb_minus_utc contains the value of (TDB-UTC) in seconds for the transmitter from which each radar delay observation was made */
    public double[] radar_delay_transmitter_tdb_minus_utc = new double[8001];

    /* radar_delay_transmitter_frequency contains the frequency in Hertz for the transmitter from which each radar delay observation was made */
    public double[] radar_delay_transmitter_frequency = new double[8001];

    /* DOPPLER OBSERVATIONS  */

    /* doppler is in Hz */
    public double[] radar_doppler = new double[8001];
    public double[] doppler_dev = new double[8001];

    /* UTC Julian dates of radar doppler receiver observations */
    public double[] radar_doppler_receiver_time = new double[8001];

    /* radar_doppler_receiver_latitude contains the geodetic latitude in radians at which each radar doppler receiver is located */
    public double[] radar_doppler_receiver_latitude = new double[8001];

    /* radar_doppler_receiver_longitude contains the east longitude in radians at which each radar doppler receiver is located */
    public double[] radar_doppler_receiver_longitude = new double[8001];

    /* radar_doppler_receiver_altitude contains the altitude in meters at which each radar doppler receiver is located */
    public double[] radar_doppler_receiver_altitude = new double[8001];

    /* radar_doppler_receiver_EOP[i][j] contains the Earth Orientation Parameters for the receiver in radar doppler observation i, in format identical to observation_EOP */
    public double[][] radar_doppler_receiver_EOP = new double[8001][8];

    /* radar_doppler_receiver_tdb_minus_utc contains the value of (TDB-UTC) in seconds for the receiver from which each radar doppler observation was made */
    public double[] radar_doppler_receiver_tdb_minus_utc = new double[8001];

    /* radar_doppler_receiver_r contains the barycentric rectangular equatorial position (in AUs) of the receiver site from which each radar doppler observation was made */
    public double[][] radar_doppler_receiver_r = new double[8001][4];

    /* radar_doppler_receiver_rprime contains the barycentric rectangular equatorial velocity (in AU/day) of the receiver site from which each radar doppler observation was made */
    public double[][] radar_doppler_receiver_rprime = new double[8001][4];

    /* radar_doppler_transmitter_latitude contains the geodetic latitude in radians at which each radar doppler transmitter is located */
    public double[] radar_doppler_transmitter_latitude = new double[8001];

    /* radar_doppler_transmitter_longitude contains the east longitude in radians at which each radar doppler transmitter is located */
    public double[] radar_doppler_transmitter_longitude = new double[8001];

    /* radar_doppler_transmitter_altitude contains the altitude in meters at which each radar doppler transmitter is located */
    public double[] radar_doppler_transmitter_altitude = new double[8001];

    /* radar_doppler_transmitter_EOP[i][j] contains the Earth Orientation Parameters for the transmitter in radar doppler observation i, in format identical to observation_EOP */
    public double[][] radar_doppler_transmitter_EOP = new double[8001][8];

    /* radar_doppler_transmitter_tdb_minus_utc contains the value of (TDB-UTC) in seconds for the transmitter from which each radar doppler observation was made */
    public double[] radar_doppler_transmitter_tdb_minus_utc = new double[8001];

    /* radar_doppler_transmitter_frequency contains the frequency in Hertz for the transmitter from which each radar doppler observation was made */
    public double[] radar_doppler_transmitter_frequency = new double[8001];

    /* minor_planet_radius is an estimate of the radius of the minor planet, in AU; it is used in radar calculations to estimate the bounce point */
    public double minor_planet_radius = 0;

    //CelestialBody methods

    private static void invertNxn(int dimension_n, double matrix_nxn[][]) {

        /*
        Procedure to invert a dimension_n-by-dimension_n matrix (matrix_nxn[][]) using Gaussian elimination.
        Algorithm taken from Richard L. Burden and J. Douglas Faires, "Numerical Analysis", 3rd edition, p. 300.
        NOTE:  It is assumed that the matrix is invertible!!

        Tested and verified 17 Oct 2000.
      */

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

    void precessionMatrix(double jultime, double matrix_P[][]) {

        /*
        Procedure to calculate the matrix which will precess an equatorial rectangular coordinate vector from the equinox and equator of epoch J2000.0 to the equinox and equator of epoch jultime.
        Algorithm taken from P. Kenneth Seidelmann, "Explanatory Supplement to the Astronomical Almanac", 1992, pg. 103-104.

        Tested and verified 19 October 1999.
      */

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

//SolarSystemBody methods

    public void planetaryEphemeris(double jultime, double r[], int light_flight) {

        /*
            Procedure to calculate the position, velocity, and (if light_flight == 1) Newtonian acceleration at TDB jultime of the major planets, plus Earth's Moon.
            The results will be used:
              - to evaluate gravitational perturbations in procedure get_acceleration;
              - in procedure detect_collision; and
              - in calculation of ra/dec.
            These ephemerides can be corrected for light time-of-flight relative to position r[], depending on the value of the int light_flight.  Note that these light-time corrections are high-accuracy, based on an iterative algorithm which includes relativistic delays (see P. Kenneth Seidelmann, Explanatory Supplement to the Astronomical Almanac, 1992, pp.147-149).  Iterative light-time corrections are also applied in calculating Newtonian accelerations, though relativistic corrections are not included.
            Calculated values are output to instance variables planet_r[][], planet_rprime[][], and planet_r2prime[][].
            Note that the planets are initially enumerated as follows:  Mercury = 1, Venus = 2, Earth-Moon barycenter = 3, Mars = 4, ... , Pluto = 9, Geocentric Moon = 10, Sun = 11.  However, the output order will be: Sun = 0, Mercury = 1, Venus = 2, Earth = 3, Mars = 4, ... , Pluto = 9, Moon = 10.
            Tested and verified 11-04-00
          */

        int i = 0, j = 0, k = 0;

        double dist = 0, newlighttime = 0, oldlighttime = 0, sublighttime = 0, u = 0, e = 0, q = 0;

        double[] ephemeris_r = new double[4];
        double[] ephemeris_rprime = new double[4];
        double[] earth_r = new double[4];
        double[] earth_rprime = new double[4];
        double[] moon_r = new double[4];
        double[] moon_rprime = new double[4];
        double[] vector_ub = new double[4];
        double[] vector_sb = new double[4];
        double[] vector_eh = new double[4];
        double[] vector_u = new double[4];
        double[] vector_q = new double[4];
        double[] lighttime = new double[12];

        /*  Get the ephemeris positions and velocities of each major planet  */
        for (i = 1; i <= 11; i++) {
            getPlanetPosVel(jultime, i, ephemeris_r, ephemeris_rprime);
            /* Initialize lighttime array for later use */
            lighttime[i] = jultime;
            /* Store output in instance variables */
            for (j = 1; j <= 3; j++) {
                planet_r[i][j] = ephemeris_r[j];
                planet_rprime[i][j] = ephemeris_rprime[j];
                planet_r2prime[i][j] = 0;
            }
        }

        /*  The positions and velocities of the Earth and Moon are found indirectly.  We already have the pos/vel of the Earth-Moon barycenter (i = 3).  We have also calculated planet_r(10,j), a geocentric vector from the Earth to the Moon.  Using the ratio of masses, we get vectors from the Earth-Moon barycenter to the Moon and to the Earth.  */
        for (j = 1; j <= 3; j++) {
            planet_r[3][j] = planet_r[3][j] - planet_r[10][j] / (1 + emrat);
            planet_r[10][j] = planet_r[3][j] + planet_r[10][j];
            planet_rprime[3][j] = planet_rprime[3][j] - planet_rprime[10][j] / (1 + emrat);
            planet_rprime[10][j] = planet_rprime[3][j] + planet_rprime[10][j];
        }

        /*  If necessary, recalculate the positions and velocities of each major planet, accounting for light time-of-flight relative to position r[] */
        if (light_flight == 1) {
            /* Calculate the vector from Sun to position r[] at jultime */
            for (j = 1; j <= 3; j++)
                vector_eh[j] = r[j] - planet_r[11][j];
            e = Math.sqrt(Math.pow(vector_eh[1], 2) + Math.pow(vector_eh[2], 2) + Math.pow(vector_eh[3], 2));
            /* Begin recalculation for planet i */
            for (i = 1; i <= 11; i++) {
                /* Make the initial estimate of lighttime */
                dist = Math.sqrt(Math.pow((r[1] - planet_r[i][1]), 2) + Math.pow((r[2] - planet_r[i][2]), 2) + Math.pow((r[3] - planet_r[i][3]), 2));
                oldlighttime = jultime;
                lighttime[i] = jultime - dist / speed_of_light;
                while (Math.abs(lighttime[i] - oldlighttime) > 1.0E-08) {
                    /* Recalculate planetary positions at lighttime */
                    if (i == 3) {
                        getPlanetPosVel(lighttime[i], 3, ephemeris_r, ephemeris_rprime);
                        for (j = 1; j <= 3; j++) {
                            earth_r[j] = ephemeris_r[j];
                            earth_rprime[j] = ephemeris_rprime[j];
                        }
                        getPlanetPosVel(lighttime[i], 10, ephemeris_r, ephemeris_rprime);
                        for (j = 1; j <= 3; j++) {
                            moon_r[j] = ephemeris_r[j];
                            moon_rprime[j] = ephemeris_rprime[j];
                        }
                        for (j = 1; j <= 3; j++) {
                            planet_r[3][j] = earth_r[j] - moon_r[j] / (1 + emrat);
                            planet_rprime[3][j] = earth_rprime[j] - moon_rprime[j] / (1 + emrat);
                        }
                    } else if (i == 10) {
                        getPlanetPosVel(lighttime[i], 3, ephemeris_r, ephemeris_rprime);
                        for (j = 1; j <= 3; j++) {
                            earth_r[j] = ephemeris_r[j];
                            earth_rprime[j] = ephemeris_rprime[j];
                        }
                        getPlanetPosVel(lighttime[i], 10, ephemeris_r, ephemeris_rprime);
                        for (j = 1; j <= 3; j++) {
                            moon_r[j] = ephemeris_r[j];
                            moon_rprime[j] = ephemeris_rprime[j];
                        }
                        for (j = 1; j <= 3; j++) {
                            planet_r[10][j] = earth_r[j] - moon_r[j] / (1 + emrat) + moon_r[j];
                            planet_rprime[10][j] = earth_rprime[j] - moon_rprime[j] / (1 + emrat) + moon_rprime[j];
                        }
                    } else {
                        getPlanetPosVel(lighttime[i], i, ephemeris_r, ephemeris_rprime);
                        for (j = 1; j <= 3; j++) {
                            planet_r[i][j] = ephemeris_r[j];
                            planet_rprime[i][j] = ephemeris_rprime[j];
                        }
                    }
                    /* Get the position of the Sun at lighttime, and form the required vectors */
                    getPlanetPosVel(lighttime[i], 11, vector_sb, ephemeris_rprime);
                    for (j = 1; j <= 3; j++) {
                        vector_ub[j] = planet_r[i][j];
                        vector_u[j] = vector_ub[j] - r[j];
                        vector_q[j] = vector_ub[j] - vector_sb[j];
                    }
                    u = Math.sqrt(Math.pow(vector_u[1], 2) + Math.pow(vector_u[2], 2) + Math.pow(vector_u[3], 2));
                    q = Math.sqrt(Math.pow(vector_q[1], 2) + Math.pow(vector_q[2], 2) + Math.pow(vector_q[3], 2));
                    /* recalculate lighttime, based on improved positions */
                    oldlighttime = lighttime[i];
                    if (i != 11)
                        lighttime[i] = jultime - (u / speed_of_light + (2 * Math.pow(kappa, 2) / Math.pow(speed_of_light, 3)) * Math.log((e + u + q) / (e - u + q)));
                    else
                        lighttime[i] = jultime - u / speed_of_light;
                }
            }
        }

        /*  Put Sun into slot 0 for later convenience  */
        lighttime[0] = lighttime[11];
        for (j = 1; j <= 3; j++) {
            planet_r[0][j] = planet_r[11][j];
            planet_rprime[0][j] = planet_rprime[11][j];
        }

        /* Note: Use of planet_r2prime in the 1/c-squared terms of the force model shows no impact whatsoever in a 200 year ephemeris test; moreover, calculating these terms consumes over 80 percent of total integration CPU cycles.  Therefore, these terms will be zeroed.  */
        /* SS: For solar sailing, the acceleration term for the Sun might be significant at r = 0.25 AU; implement this only for the Sun */
        for (k = 1; k <= 3; k++)
            planet_r2prime[0][k] = 0;
        for (k = 1; k <= 10; k++) {
            /* Perturbations due to planet k */
            dist = Math.sqrt(Math.pow((planet_r[0][1] - planet_r[k][1]), 2) + Math.pow((planet_r[0][2] - planet_r[k][2]), 2) + Math.pow((planet_r[0][3] - planet_r[k][3]), 2));
            for (i = 1; i <= 3; i++)
                planet_r2prime[0][i] = planet_r2prime[0][i] + mu_planet[k] * (planet_r[k][i] - planet_r[0][i]) / Math.pow(dist, 3);
        }


    }

    void initializeArrays() {
        /*
            Routine to initialize the ephemeris- and integration-related arrays "number_of_coefs", "number_of_coef_sets", and "mu_planet", as well as the arrays containing the mean orbital elements of the 300 most important perturbing asteroids.
          */
        int j = 0, k = 0;

        /*
            Initialize arrays
          */
        number_of_coefs[1] = number_of_coefs_1;
        number_of_coefs[2] = number_of_coefs_2;
        number_of_coefs[3] = number_of_coefs_3;
        number_of_coefs[4] = number_of_coefs_4;
        number_of_coefs[5] = number_of_coefs_5;
        number_of_coefs[6] = number_of_coefs_6;
        number_of_coefs[7] = number_of_coefs_7;
        number_of_coefs[8] = number_of_coefs_8;
        number_of_coefs[9] = number_of_coefs_9;
        number_of_coefs[10] = number_of_coefs_10;
        number_of_coefs[11] = number_of_coefs_11;
        number_of_coef_sets[1] = number_of_coef_sets_1;
        number_of_coef_sets[2] = number_of_coef_sets_2;
        number_of_coef_sets[3] = number_of_coef_sets_3;
        number_of_coef_sets[4] = number_of_coef_sets_4;
        number_of_coef_sets[5] = number_of_coef_sets_5;
        number_of_coef_sets[6] = number_of_coef_sets_6;
        number_of_coef_sets[7] = number_of_coef_sets_7;
        number_of_coef_sets[8] = number_of_coef_sets_8;
        number_of_coef_sets[9] = number_of_coef_sets_9;
        number_of_coef_sets[10] = number_of_coef_sets_10;
        number_of_coef_sets[11] = number_of_coef_sets_11;
        mu_planet[0] = mu_planet_0;
        mu_planet[1] = mu_planet_1;
        mu_planet[2] = mu_planet_2;
        mu_planet[3] = mu_planet_3;
        mu_planet[4] = mu_planet_4;
        mu_planet[5] = mu_planet_5;
        mu_planet[6] = mu_planet_6;
        mu_planet[7] = mu_planet_7;
        mu_planet[8] = mu_planet_8;
        mu_planet[9] = mu_planet_9;
        mu_planet[10] = mu_planet_10;
        planet_radius[0] = planet_radius_0;
        planet_radius[1] = planet_radius_1;
        planet_radius[2] = planet_radius_2;
        planet_radius[3] = planet_radius_3;
        planet_radius[4] = planet_radius_4;
        planet_radius[5] = planet_radius_5;
        planet_radius[6] = planet_radius_6;
        planet_radius[7] = planet_radius_7;
        planet_radius[8] = planet_radius_8;
        planet_radius[9] = planet_radius_9;
        planet_radius[10] = planet_radius_10;

        /*  Asteroid 1  */
        /*
        1a G? 933.0 21 10 2 -0.5493E-10 7
        1b G 2.767099 0.077838 10.5964 80.0461 72.1949 286.2132 0.003736445 47800.
        1c G 2.767099 0.077879 10.5981 80.1970 71.8685 192.8751 0.003736445 44000.
        */
        mu_planet[11] = (4.7e-10) * Math.pow(0.01720209895, 2);
        asteroid_a[1] = 2.767099;
        asteroid_e1[1] = 0.077838;
        asteroid_e2[1] = 0.077879;
        asteroid_i1[1] = 10.5964;
        asteroid_i2[1] = 10.5981;
        asteroid_omega1[1] = 80.0461;
        asteroid_omega2[1] = 80.1970;
        asteroid_w1[1] = 72.1949;
        asteroid_w2[1] = 71.8685;
        asteroid_mean1[1] = 286.2132;
        asteroid_mean2[1] = 192.8751;
        asteroid_meanmotion[1] = 0.003736445;

        /*  Asteroid 2  */
        /*
        2a m 525.0 21 10 2 -0.1836E-09 10
        2b m 2.771103 0.232943 34.8393 172.6166 310.0759 272.9434 0.003728786 47800.
        2c m 2.771103 0.233488 34.8255 172.7186 309.9927 181.0786 0.003728786 44000.
        */
        mu_planet[12] = (1.11e-10) * Math.pow(0.01720209895, 2);
        asteroid_a[2] = 2.771103;
        asteroid_e1[2] = 0.232943;
        asteroid_e2[2] = 0.233488;
        asteroid_i1[2] = 34.8393;
        asteroid_i2[2] = 34.8255;
        asteroid_omega1[2] = 172.6166;
        asteroid_omega2[2] = 172.7186;
        asteroid_w1[2] = 310.0759;
        asteroid_w2[2] = 309.9927;
        asteroid_mean1[2] = 272.9434;
        asteroid_mean2[2] = 181.0786;
        asteroid_meanmotion[2] = 0.003728786;

        /*  Asteroid 4  */
        /*
        4a r 510.0 17 8 2 -0.3419E-11 5
        4b r 2.361558 0.089331 7.1391 103.4034 150.0770 43.3824 0.004739505 47800.
        4c r 2.361558 0.089299 7.1385 103.4931 149.8668 91.5990 0.004739505 44000.
        */
        mu_planet[13] = 1.36e-10 * Math.pow(0.01720209895, 2);
        asteroid_a[3] = 2.361558;
        asteroid_e1[3] = 0.089331;
        asteroid_e2[3] = 0.089299;
        asteroid_i1[3] = 7.1391;
        asteroid_i2[3] = 7.1385;
        asteroid_omega1[3] = 103.4034;
        asteroid_omega2[3] = 103.4931;
        asteroid_w1[3] = 150.0770;
        asteroid_w2[3] = 149.8668;
        asteroid_mean1[3] = 43.3824;
        asteroid_mean2[3] = 91.5990;
        asteroid_meanmotion[3] = 0.004739505;

        /*  Asteroid 29  */
        /*
        29a S 219.0 19 9 2 0.9016E-10 5
        29b S 2.554328 0.073536 6.0882 355.9270 62.7517 197.6035 0.004213097 47800.
        29c S 2.554328 0.073590 6.0912 356.0654 62.5250 0.3995 0.004213097 44000.
        */
        mu_planet[14] = (4 * Math.PI / 3) * Math.pow((212.22 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[4] = 2.554328;
        asteroid_e1[4] = 0.073536;
        asteroid_e2[4] = 0.073590;
        asteroid_i1[4] = 6.0882;
        asteroid_i2[4] = 6.0912;
        asteroid_omega1[4] = 355.9270;
        asteroid_omega2[4] = 356.0654;
        asteroid_w1[4] = 62.7517;
        asteroid_w2[4] = 62.5250;
        asteroid_mean1[4] = 197.6035;
        asteroid_mean2[4] = 0.3995;
        asteroid_meanmotion[4] = 0.004213097;

        /*  Asteroid 16  */
        /*
        16a M 264.0 25 12 3 0.1465E-11 7
        16b M 2.922186 0.136883 3.0961 149.9157 227.5733 318.6324 0.003442758 47800.
        16c M 2.922186 0.136889 3.0931 150.0613 227.2647 289.2245 0.003442758 44000.
        */
        mu_planet[15] = (4 * Math.PI / 3) * Math.pow((253.16 / 2), 3) * 5.3E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[5] = 2.922186;
        asteroid_e1[5] = 0.136883;
        asteroid_e2[5] = 0.136889;
        asteroid_i1[5] = 3.0961;
        asteroid_i2[5] = 3.0931;
        asteroid_omega1[5] = 149.9157;
        asteroid_omega2[5] = 150.0613;
        asteroid_w1[5] = 227.5733;
        asteroid_w2[5] = 227.2647;
        asteroid_mean1[5] = 318.6324;
        asteroid_mean2[5] = 289.2245;
        asteroid_meanmotion[5] = 0.003442758;

        /*  Asteroid 15  */
        /*
        15a S 272.0 23 11 2 0.2315E-10 5
        15b S 2.643717 0.186752 11.7475 292.8734 97.3149 328.0986 0.004001206 47800.
        15c S 2.643717 0.186798 11.7471 293.0490 97.0663 177.0130 0.004001206 44000.
        */
        mu_planet[16] = (4 * Math.PI / 3) * Math.pow((255.34 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[6] = 2.643717;
        asteroid_e1[6] = 0.186752;
        asteroid_e2[6] = 0.186798;
        asteroid_i1[6] = 11.7475;
        asteroid_i2[6] = 11.7471;
        asteroid_omega1[6] = 292.8734;
        asteroid_omega2[6] = 293.0490;
        asteroid_w1[6] = 97.3149;
        asteroid_w2[6] = 97.0663;
        asteroid_mean1[6] = 328.0986;
        asteroid_mean2[6] = 177.0130;
        asteroid_meanmotion[6] = 0.004001206;

        /*  Asteroid 10  */
        /*
        10a C 429.0 25 12 3 0.9252E-10 11
        10b C 3.142300 0.112211 3.8337 283.2154 315.2664 34.5092 0.003087160 47800.
        10c C 3.142300 0.112105 3.8312 283.6001 314.4323 82.8100 0.003087160 44000.
        */
        mu_planet[17] = (4 * Math.PI / 3) * Math.pow((407.12 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[7] = 3.142300;
        asteroid_e1[7] = 0.112211;
        asteroid_e2[7] = 0.112105;
        asteroid_i1[7] = 3.8337;
        asteroid_i2[7] = 3.8312;
        asteroid_omega1[7] = 283.2154;
        asteroid_omega2[7] = 283.6001;
        asteroid_w1[7] = 315.2664;
        asteroid_w2[7] = 314.4323;
        asteroid_mean1[7] = 34.5092;
        asteroid_mean2[7] = 82.8100;
        asteroid_meanmotion[7] = 0.003087160;

        /*  Asteroid 7  */
        /*
        7a S 203.0 17 8 2 -0.1886E-10 6
        7b S 2.386053 0.230150 5.5140 259.2639 144.8214 132.5142 0.004666663 47800.
        7c S 2.386053 0.230197 5.5107 259.4088 144.5828 196.5635 0.004666663 44000.
        */
        mu_planet[18] = (4 * Math.PI / 3) * Math.pow((199.83 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[8] = 2.386053;
        asteroid_e1[8] = 0.230150;
        asteroid_e2[8] = 0.230197;
        asteroid_i1[8] = 5.5140;
        asteroid_i2[8] = 5.5107;
        asteroid_omega1[8] = 259.2639;
        asteroid_omega2[8] = 259.4088;
        asteroid_w1[8] = 144.8214;
        asteroid_w2[8] = 144.5828;
        asteroid_mean1[8] = 132.5142;
        asteroid_mean2[8] = 196.5635;
        asteroid_meanmotion[8] = 0.004666663;

        /*  Asteroid 6  */
        /*
        6a S 192.0 19 9 2 0.1379E-11 4
        6b S 2.425208 0.202556 14.7662 138.3633 238.7807 148.8952 0.004554149 47800.
        6c S 2.425208 0.202486 14.7678 138.4768 238.6001 237.4149 0.004554149 44000.
        */
        mu_planet[19] = (4 * Math.PI / 3) * Math.pow((185.18 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[9] = 2.425208;
        asteroid_e1[9] = 0.202556;
        asteroid_e2[9] = 0.202486;
        asteroid_i1[9] = 14.7662;
        asteroid_i2[9] = 14.7678;
        asteroid_omega1[9] = 138.3633;
        asteroid_omega2[9] = 138.4768;
        asteroid_w1[9] = 238.7807;
        asteroid_w2[9] = 238.6001;
        asteroid_mean1[9] = 148.8952;
        asteroid_mean2[9] = 237.4149;
        asteroid_meanmotion[9] = 0.004554149;

        /*  Asteroid 3  */
        /*
        3a S 267.0 23 11 2 -0.9984E-10 8
        3b S 2.669252 0.257030 12.9912 169.7284 247.3068 114.8961 0.003943857 47800.
        3c S 2.669252 0.256972 12.9938 169.9168 247.0266 336.3156 0.003943857 44000.
        */
        mu_planet[20] = (4 * Math.PI / 3) * Math.pow((233.92 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[10] = 2.669252;
        asteroid_e1[10] = 0.257030;
        asteroid_e2[10] = 0.256972;
        asteroid_i1[10] = 12.9912;
        asteroid_i2[10] = 12.9938;
        asteroid_omega1[10] = 169.7284;
        asteroid_omega2[10] = 169.9168;
        asteroid_w1[10] = 247.3068;
        asteroid_w2[10] = 247.0266;
        asteroid_mean1[10] = 114.8961;
        asteroid_mean2[10] = 336.3156;
        asteroid_meanmotion[10] = 0.003943857;

        /*  Asteroid 345  */
        /*
        345a C 100.0 21 10 2 0.1384E-11 4
        345b C 2.325284 0.061331 9.7471 212.2284 229.9536 84.9951 0.004850905 47800.
        345c C 2.325284 0.061355 9.7452 212.3336 229.7734 108.9117 0.004850905 44000.
        */
        mu_planet[21] = (4 * Math.PI / 3) * Math.pow((94.12 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[11] = 2.325284;
        asteroid_e1[11] = 0.061331;
        asteroid_e2[11] = 0.061355;
        asteroid_i1[11] = 9.7471;
        asteroid_i2[11] = 9.7452;
        asteroid_omega1[11] = 212.2284;
        asteroid_omega2[11] = 212.3336;
        asteroid_w1[11] = 229.9536;
        asteroid_w2[11] = 229.7734;
        asteroid_mean1[11] = 84.9951;
        asteroid_mean2[11] = 108.9117;
        asteroid_meanmotion[11] = 0.004850905;

        /*  Asteroid 12  */
        /*
        12a S 117.0 21 10 2 -0.5393E-11 10
        12b S 2.334286 0.219970 8.3728 235.0636 69.1588 28.9884 0.004822821 47800.
        12c S 2.334286 0.219896 8.3735 235.2061 68.9325 59.0285 0.004822821 44000.
        */
        mu_planet[22] = (4 * Math.PI / 3) * Math.pow((112.77 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[12] = 2.334286;
        asteroid_e1[12] = 0.219970;
        asteroid_e2[12] = 0.219896;
        asteroid_i1[12] = 8.3728;
        asteroid_i2[12] = 8.3735;
        asteroid_omega1[12] = 235.0636;
        asteroid_omega2[12] = 235.2061;
        asteroid_w1[12] = 69.1588;
        asteroid_w2[12] = 68.9325;
        asteroid_mean1[12] = 28.9884;
        asteroid_mean2[12] = 59.0285;
        asteroid_meanmotion[12] = 0.004822821;

        /*  Asteroid 442  */
        /*
        442a C 67.5 21 10 2 -0.1201E-12 5
        442b C 2.345304 0.071120 6.0675 134.5010 84.5714 17.1750 0.004788873 47800.
        442c C 2.345304 0.071097 6.0662 134.5926 84.3463 54.6562 0.004788873 44000.
        */
        mu_planet[23] = (4 * Math.PI / 3) * Math.pow((65.73 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[13] = 2.345304;
        asteroid_e1[13] = 0.071120;
        asteroid_e2[13] = 0.071097;
        asteroid_i1[13] = 6.0675;
        asteroid_i2[13] = 6.0662;
        asteroid_omega1[13] = 134.5010;
        asteroid_omega2[13] = 134.5926;
        asteroid_w1[13] = 84.5714;
        asteroid_w2[13] = 84.3463;
        asteroid_mean1[13] = 17.1750;
        asteroid_mean2[13] = 54.6562;
        asteroid_meanmotion[13] = 0.004788873;

        /*  Asteroid 27  */
        /*
        27a S 131.0 21 10 2 -0.3305E-11 6
        27b S 2.347047 0.172120 1.5854 94.3669 356.0217 255.1812 0.004783508 47800.
        27c S 2.347047 0.172168 1.5857 94.3860 355.9002 293.7992 0.004783508 44000.
        */
        mu_planet[24] = (4 * Math.PI / 3) * Math.pow((131.0 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[14] = 2.347047;
        asteroid_e1[14] = 0.172120;
        asteroid_e2[14] = 0.172168;
        asteroid_i1[14] = 1.5854;
        asteroid_i2[14] = 1.5857;
        asteroid_omega1[14] = 94.3669;
        asteroid_omega2[14] = 94.3860;
        asteroid_w1[14] = 356.0217;
        asteroid_w2[14] = 355.9002;
        asteroid_mean1[14] = 255.1812;
        asteroid_mean2[14] = 293.7992;
        asteroid_meanmotion[14] = 0.004783508;

        /*  Asteroid 287  */
        /*
        287a S 70.1 21 10 2 -0.2771E-11 4
        287b S 2.352964 0.023526 10.0298 141.9306 119.8885 330.8004 0.004765532 47800.
        287c S 2.352964 0.023489 10.0282 142.0241 119.6757 13.3490 0.004765532 44000.
        */
        mu_planet[25] = (4 * Math.PI / 3) * Math.pow((67.6 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[15] = 2.352964;
        asteroid_e1[15] = 0.023526;
        asteroid_e2[15] = 0.023489;
        asteroid_i1[15] = 10.0298;
        asteroid_i2[15] = 10.0282;
        asteroid_omega1[15] = 141.9306;
        asteroid_omega2[15] = 142.0241;
        asteroid_w1[15] = 119.8885;
        asteroid_w2[15] = 119.6757;
        asteroid_mean1[15] = 330.8004;
        asteroid_mean2[15] = 13.3490;
        asteroid_meanmotion[15] = 0.004765532;

        /*  Asteroid 43  */
        /*
        43a S 65.3 16 7 2 0.1810E-10 7
        43b S 2.203292 0.168099 3.4682 264.3729 15.6999 123.7651 0.005259349 47800.
        43c S 2.203292 0.168057 3.4678 264.4960 15.4843 58.7712 0.005259349 44000.
        */
        mu_planet[26] = (4 * Math.PI / 3) * Math.pow((65.88 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[16] = 2.203292;
        asteroid_e1[16] = 0.168099;
        asteroid_e2[16] = 0.168057;
        asteroid_i1[16] = 3.4682;
        asteroid_i2[16] = 3.4678;
        asteroid_omega1[16] = 264.3729;
        asteroid_omega2[16] = 264.4960;
        asteroid_w1[16] = 15.6999;
        asteroid_w2[16] = 15.4843;
        asteroid_mean1[16] = 123.7651;
        asteroid_mean2[16] = 58.7712;
        asteroid_meanmotion[16] = 0.005259349;

        /*  Asteroid 84  */
        /*
        84a G  83.0 17 8 2 0.1775E-10 6
        84b G 2.362162 0.236544 9.3313 327.1782 14.4598 55.8328 0.004737678 47800.
        84c G 2.362162 0.236490 9.3352 327.2904 14.2536 104.4208 0.004737678 44000.
        */
        mu_planet[27] = (4 * Math.PI / 3) * Math.pow((79.16 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[17] = 2.362162;
        asteroid_e1[17] = 0.236544;
        asteroid_e2[17] = 0.236490;
        asteroid_i1[17] = 9.3313;
        asteroid_i2[17] = 9.3352;
        asteroid_omega1[17] = 327.1782;
        asteroid_omega2[17] = 327.2904;
        asteroid_w1[17] = 14.4598;
        asteroid_w2[17] = 14.2536;
        asteroid_mean1[17] = 55.8328;
        asteroid_mean2[17] = 104.4208;
        asteroid_meanmotion[17] = 0.004737678;

        /*  Asteroid 30  */
        /*
        30a S 104.0 17 8 2 0.3660E-10 4
        30b S 2.365654 0.127177 2.0940 307.2747 86.3119 344.4716 0.004727195 47800.
        30c S 2.365654 0.127194 2.0951 307.4554 86.0474 35.3318 0.004727195 44000.
        */
        mu_planet[28] = (4 * Math.PI / 3) * Math.pow((99.66 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[18] = 2.365654;
        asteroid_e1[18] = 0.127177;
        asteroid_e2[18] = 0.127194;
        asteroid_i1[18] = 2.0940;
        asteroid_i2[18] = 2.0951;
        asteroid_omega1[18] = 307.2747;
        asteroid_omega2[18] = 307.4554;
        asteroid_w1[18] = 86.3119;
        asteroid_w2[18] = 86.0474;
        asteroid_mean1[18] = 344.4716;
        asteroid_mean2[18] = 35.3318;
        asteroid_meanmotion[18] = 0.004727195;

        /*  Asteroid 536  */
        /*
        536a X 158.0 25 12 3 -0.3865E-09 11
        536b C 3.499692 0.088933 19.4049 59.1570 299.9115 263.7905 0.002626488 47800.
        536c C 3.499692 0.089099 19.4071 59.4182 299.6008 51.9905 0.002626488 44000.
        */
        mu_planet[29] = (4 * Math.PI / 3) * Math.pow((151.42 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[19] = 3.499692;
        asteroid_e1[19] = 0.088933;
        asteroid_e2[19] = 0.089099;
        asteroid_i1[19] = 19.4049;
        asteroid_i2[19] = 19.4071;
        asteroid_omega1[19] = 59.1570;
        asteroid_omega2[19] = 59.4182;
        asteroid_w1[19] = 299.9115;
        asteroid_w2[19] = 299.6008;
        asteroid_mean1[19] = 263.7905;
        asteroid_mean2[19] = 51.9905;
        asteroid_meanmotion[19] = 0.002626488;

        /*  Asteroid 163  */
        /*
        163a C 76.5 17 8 2 -0.4279E-10 6
        163b C 2.367144 0.191508 4.8080 159.7955 297.5080 148.8406 0.004722701 47800.
        163c C 2.367144 0.191563 4.8046 159.9115 297.2894 200.6980 0.004722701 44000.
        */
        mu_planet[30] = (4 * Math.PI / 3) * Math.pow((72.63 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[20] = 2.367144;
        asteroid_e1[20] = 0.191508;
        asteroid_e2[20] = 0.191563;
        asteroid_i1[20] = 4.8080;
        asteroid_i2[20] = 4.8046;
        asteroid_omega1[20] = 159.7955;
        asteroid_omega2[20] = 159.9115;
        asteroid_w1[20] = 297.5080;
        asteroid_w2[20] = 297.2894;
        asteroid_mean1[20] = 148.8406;
        asteroid_mean2[20] = 200.6980;
        asteroid_meanmotion[20] = 0.004722701;

        /*  Asteroid 105  */
        /*
        105a C 123.0 17 8 2 0.1442E-10 7
        105b C 2.373276 0.176968 21.4736 187.8674 56.2756 136.0189 0.004704565 47800.
        105c C 2.373276 0.176792 21.4758 187.9693 56.1210 191.7751 0.004704565 44000.
        */
        mu_planet[31] = (4 * Math.PI / 3) * Math.pow((119.08 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[21] = 2.373276;
        asteroid_e1[21] = 0.176968;
        asteroid_e2[21] = 0.176792;
        asteroid_i1[21] = 21.4736;
        asteroid_i2[21] = 21.4758;
        asteroid_omega1[21] = 187.8674;
        asteroid_omega2[21] = 187.9693;
        asteroid_w1[21] = 56.2756;
        asteroid_w2[21] = 56.1210;
        asteroid_mean1[21] = 136.0189;
        asteroid_mean2[21] = 191.7751;
        asteroid_meanmotion[21] = 0.004704565;

        /*  Asteroid 554  */
        /*
        554a C 98.5 17 8 2 0.2092E-10 4
        554b C 2.374633 0.153182 2.9352 295.0977 127.0093 96.3782 0.004700393 47800.
        554c C 2.374633 0.153224 2.9353 295.2629 126.7505 153.0837 0.004700393 44000.
        */
        mu_planet[32] = (4 * Math.PI / 3) * Math.pow((95.87 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[22] = 2.374633;
        asteroid_e1[22] = 0.153182;
        asteroid_e2[22] = 0.153224;
        asteroid_i1[22] = 2.9352;
        asteroid_i2[22] = 2.9353;
        asteroid_omega1[22] = 295.0977;
        asteroid_omega2[22] = 295.2629;
        asteroid_w1[22] = 127.0093;
        asteroid_w2[22] = 126.7505;
        asteroid_mean1[22] = 96.3782;
        asteroid_mean2[22] = 153.0837;
        asteroid_meanmotion[22] = 0.004700393;

        /*  Asteroid 313  */
        /*
        313a C 101.0 17 8 2 -0.2561E-10 6
        313b C 2.375800 0.180606 11.6350 176.2788 315.2294 145.4009 0.004696948 47800.
        313c C 2.375800 0.180699 11.6293 176.3933 315.0173 202.8603 0.004696948 44000.
        */
        mu_planet[33] = (4 * Math.PI / 3) * Math.pow((96.34 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[23] = 2.375800;
        asteroid_e1[23] = 0.180606;
        asteroid_e2[23] = 0.180699;
        asteroid_i1[23] = 11.6350;
        asteroid_i2[23] = 11.6293;
        asteroid_omega1[23] = 176.2788;
        asteroid_omega2[23] = 176.3933;
        asteroid_w1[23] = 315.2294;
        asteroid_w2[23] = 315.0173;
        asteroid_mean1[23] = 145.4009;
        asteroid_mean2[23] = 202.8603;
        asteroid_meanmotion[23] = 0.004696948;

        /*  Asteroid 115  */
        /*
        115a S 83.5 17 8 2 -0.4138E-12 4
        115b S 2.379913 0.192490 11.5874 308.5545 96.2159 358.2184 0.004684811 47800.
        115c S 2.379913 0.192532 11.5876 308.6897 96.0207 58.2829 0.004684811 44000.
        */
        mu_planet[34] = (4 * Math.PI / 3) * Math.pow((79.83 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[24] = 2.379913;
        asteroid_e1[24] = 0.192490;
        asteroid_e2[24] = 0.192532;
        asteroid_i1[24] = 11.5874;
        asteroid_i2[24] = 11.5876;
        asteroid_omega1[24] = 308.5545;
        asteroid_omega2[24] = 308.6897;
        asteroid_w1[24] = 96.2159;
        asteroid_w2[24] = 96.0207;
        asteroid_mean1[24] = 358.2184;
        asteroid_mean2[24] = 58.2829;
        asteroid_meanmotion[24] = 0.004684811;

        /*  Asteroid 230  */
        /*
        230a S 115.0 17 8 2 0.1050E-10 4
        230b S 2.382352 0.061417 9.4446 239.4153 139.1477 9.4088 0.004677615 47800.
        230c S 2.382352 0.061431 9.4429 239.5302 138.9751 71.0378 0.004677615 44000.
        */
        mu_planet[35] = (4 * Math.PI / 3) * Math.pow((108.99 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[25] = 2.382352;
        asteroid_e1[25] = 0.061417;
        asteroid_e2[25] = 0.061431;
        asteroid_i1[25] = 9.4446;
        asteroid_i2[25] = 9.4429;
        asteroid_omega1[25] = 239.4153;
        asteroid_omega2[25] = 239.5302;
        asteroid_w1[25] = 139.1477;
        asteroid_w2[25] = 138.9751;
        asteroid_mean1[25] = 9.4088;
        asteroid_mean2[25] = 71.0378;
        asteroid_meanmotion[25] = 0.004677615;

        /*  Asteroid 337  */
        /*
        337a M 63.2 17 8 2 -0.1168E-11 6
        337b M 2.383025 0.137784 7.8546 355.0486 98.4397 64.2461 0.004675600 47800.
        337c M 2.383025 0.137843 7.8563 355.1737 98.2240 126.3466 0.004675600 44000.
        */
        mu_planet[36] = (4 * Math.PI / 3) * Math.pow((59.11 / 2), 3) * 5.3E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[26] = 2.383025;
        asteroid_e1[26] = 0.137784;
        asteroid_e2[26] = 0.137843;
        asteroid_i1[26] = 7.8546;
        asteroid_i2[26] = 7.8563;
        asteroid_omega1[26] = 355.0486;
        asteroid_omega2[26] = 355.1737;
        asteroid_w1[26] = 98.4397;
        asteroid_w2[26] = 98.2240;
        asteroid_mean1[26] = 64.2461;
        asteroid_mean2[26] = 126.3466;
        asteroid_meanmotion[26] = 0.004675600;

        /*  Asteroid 80  */
        /*
        80a S 81.7 16 7 2 -0.7143E-11 4
        80b S 2.295874 0.200323 8.6636 218.2988 138.9470 278.0724 0.004944407 47800.
        80c S 2.295874 0.200344 8.6593 218.4137 138.7554 281.6334 0.004944407 44000.
        */
        mu_planet[37] = (4 * Math.PI / 3) * Math.pow((78.39 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[27] = 2.295874;
        asteroid_e1[27] = 0.200323;
        asteroid_e2[27] = 0.200344;
        asteroid_i1[27] = 8.6636;
        asteroid_i2[27] = 8.6593;
        asteroid_omega1[27] = 218.2988;
        asteroid_omega2[27] = 218.4137;
        asteroid_w1[27] = 138.9470;
        asteroid_w2[27] = 138.7554;
        asteroid_mean1[27] = 278.0724;
        asteroid_mean2[27] = 281.6334;
        asteroid_meanmotion[27] = 0.004944407;

        /*  Asteroid 9  */
        /*
        9a S 170.0 17 8 2 0.3822E-10 4
        9b S 2.386460 0.122484 5.5808 68.5052 5.2856 270.7163 0.004665491 47800.
        9c S 2.386460 0.122522 5.5824 68.5928 5.1004 335.0247 0.004665491 44000.
        */
        mu_planet[38] = (4 * Math.PI / 3) * Math.pow((154.67 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[28] = 2.386460;
        asteroid_e1[28] = 0.122484;
        asteroid_e2[28] = 0.122522;
        asteroid_i1[28] = 5.5808;
        asteroid_i2[28] = 5.5824;
        asteroid_omega1[28] = 68.5052;
        asteroid_omega2[28] = 68.5928;
        asteroid_w1[28] = 5.2856;
        asteroid_w2[28] = 5.1004;
        asteroid_mean1[28] = 270.7163;
        asteroid_mean2[28] = 335.0247;
        asteroid_meanmotion[28] = 0.004665491;

        /*  Asteroid 63  */
        /*
        63a S 108.0 17 8 2 0.2196E-10 6
        63b S 2.395306 0.126366 5.7777 337.5011 294.8988 149.2969 0.004639667 47800.
        63c S 2.395306 0.126331 5.7793 337.6388 294.6537 219.2377 0.004639667 44000.
        */
        mu_planet[39] = (4 * Math.PI / 3) * Math.pow((103.14 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[29] = 2.395306;
        asteroid_e1[29] = 0.126366;
        asteroid_e2[29] = 0.126331;
        asteroid_i1[29] = 5.7777;
        asteroid_i2[29] = 5.7793;
        asteroid_omega1[29] = 337.5011;
        asteroid_omega2[29] = 337.6388;
        asteroid_w1[29] = 294.8988;
        asteroid_w2[29] = 294.6537;
        asteroid_mean1[29] = 149.2969;
        asteroid_mean2[29] = 219.2377;
        asteroid_meanmotion[29] = 0.004639667;

        /*  Asteroid 25  */
        /*
        25a S 78.2 17 8 2 0.2616E-10 6
        25b S 2.400355 0.254788 21.5950 213.7373 90.1516 190.3494 0.004625199 47800.
        25c S 2.400355 0.254735 21.5943 213.8650 90.0218 263.3348 0.004625199 44000.
        */
        mu_planet[40] = (4 * Math.PI / 3) * Math.pow((75.13 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[30] = 2.400355;
        asteroid_e1[30] = 0.254788;
        asteroid_e2[30] = 0.254735;
        asteroid_i1[30] = 21.5950;
        asteroid_i2[30] = 21.5943;
        asteroid_omega1[30] = 213.7373;
        asteroid_omega2[30] = 213.8650;
        asteroid_w1[30] = 90.1516;
        asteroid_w2[30] = 90.0218;
        asteroid_mean1[30] = 190.3494;
        asteroid_mean2[30] = 263.3348;
        asteroid_meanmotion[30] = 0.004625199;

        /*  Asteroid 192  */
        /*
        192a S 107.0 17 8 2 0.2044E-11 6
        192b S 2.402915 0.246456 6.8140 342.8978 29.8319 21.0057 0.004617616 47800.
        192c S 2.402915 0.246427 6.8196 343.0291 29.6083 95.7325 0.004617616 44000.
        */
        mu_planet[41] = (4 * Math.PI / 3) * Math.pow((103.26 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[31] = 2.402915;
        asteroid_e1[31] = 0.246456;
        asteroid_e2[31] = 0.246427;
        asteroid_i1[31] = 6.8140;
        asteroid_i2[31] = 6.8196;
        asteroid_omega1[31] = 342.8978;
        asteroid_omega2[31] = 343.0291;
        asteroid_w1[31] = 29.8319;
        asteroid_w2[31] = 29.6083;
        asteroid_mean1[31] = 21.0057;
        asteroid_mean2[31] = 95.7325;
        asteroid_meanmotion[31] = 0.004617616;

        /*  Asteroid 304  */
        /*
        304a C 68.5 17 8 2 0.1921E-10 6
        304b C 2.403722 0.221006 15.8229 158.6651 171.6872 352.7967 0.004615352 47800.
        304c C 2.403722 0.221030 15.8183 158.7616 171.4914 68.0232 0.004615352 44000.
        */
        mu_planet[42] = (4 * Math.PI / 3) * Math.pow((67.86 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[32] = 2.403722;
        asteroid_e1[32] = 0.221006;
        asteroid_e2[32] = 0.221030;
        asteroid_i1[32] = 15.8229;
        asteroid_i2[32] = 15.8183;
        asteroid_omega1[32] = 158.6651;
        asteroid_omega2[32] = 158.7616;
        asteroid_w1[32] = 171.6872;
        asteroid_w2[32] = 171.4914;
        asteroid_mean1[32] = 352.7967;
        asteroid_mean2[32] = 68.0232;
        asteroid_meanmotion[32] = 0.004615352;

        /*  Asteroid 20  */
        /*
        20a S 151.0 17 8 2 -0.3069E-10 6
        20b S 2.408617 0.143860 0.7065 205.8703 255.8354 151.1877 0.004601223 47800.
        20c S 2.408617 0.143912 0.7041 206.0670 255.5258 229.5040 0.004601223 44000.
        */
        mu_planet[43] = (4 * Math.PI / 3) * Math.pow((145.50 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[33] = 2.408617;
        asteroid_e1[33] = 0.143860;
        asteroid_e2[33] = 0.143912;
        asteroid_i1[33] = 0.7065;
        asteroid_i2[33] = 0.7041;
        asteroid_omega1[33] = 205.8703;
        asteroid_omega2[33] = 206.0670;
        asteroid_w1[33] = 255.8354;
        asteroid_w2[33] = 255.5258;
        asteroid_mean1[33] = 151.1877;
        asteroid_mean2[33] = 229.5040;
        asteroid_meanmotion[33] = 0.004601223;

        /*  Asteroid 566  */
        /*
        566a C 175.0 25 12 4 0.7476E-10 10
        566b C 3.386618 0.104959 4.9147 79.8442 302.2921 67.3890 0.002758573 47800.
        566c C 3.386618 0.105037 4.9166 80.1475 301.5814 187.1890 0.002758573 44000.
        */
        mu_planet[44] = (4 * Math.PI / 3) * Math.pow((168.16 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[34] = 3.386618;
        asteroid_e1[34] = 0.104959;
        asteroid_e2[34] = 0.105037;
        asteroid_i1[34] = 4.9147;
        asteroid_i2[34] = 4.9166;
        asteroid_omega1[34] = 79.8442;
        asteroid_omega2[34] = 80.1475;
        asteroid_w1[34] = 302.2921;
        asteroid_w2[34] = 301.5814;
        asteroid_mean1[34] = 67.3890;
        asteroid_mean2[34] = 187.1890;
        asteroid_meanmotion[34] = 0.002758573;

        /*  Asteroid 654  */
        /*
        654a C 132.0 25 12 2 -0.1854E-11 10
        654b C 2.297002 0.231097 18.1304 278.0231 213.6793 157.5063 0.004940827 47800.
        654c C 2.297002 0.230969 18.1367 278.1284 213.4948 161.8492 0.004940827 44000.
        */
        mu_planet[45] = (4 * Math.PI / 3) * Math.pow((127.4 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[35] = 2.297002;
        asteroid_e1[35] = 0.231097;
        asteroid_e2[35] = 0.230969;
        asteroid_i1[35] = 18.1304;
        asteroid_i2[35] = 18.1367;
        asteroid_omega1[35] = 278.0231;
        asteroid_omega2[35] = 278.1284;
        asteroid_w1[35] = 213.6793;
        asteroid_w2[35] = 213.4948;
        asteroid_mean1[35] = 157.5063;
        asteroid_mean2[35] = 161.8492;
        asteroid_meanmotion[35] = 0.004940827;

        /*  Asteroid 135  */
        /*
        135a M 82.0 19 9 2 0.1574E-10 6
        135b M 2.428512 0.205832 2.2993 343.3409 339.2143 46.8941 0.004544767 47800.
        135c M 2.428512 0.205788 2.3014 343.4836 338.9662 137.4949 0.004544767 44000.
        */
        mu_planet[46] = (4 * Math.PI / 3) * Math.pow((79.24 / 2), 3) * 5.3E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[36] = 2.428512;
        asteroid_e1[36] = 0.205832;
        asteroid_e2[36] = 0.205788;
        asteroid_i1[36] = 2.2993;
        asteroid_i2[36] = 2.3014;
        asteroid_omega1[36] = 343.3409;
        asteroid_omega2[36] = 343.4836;
        asteroid_w1[36] = 339.2143;
        asteroid_w2[36] = 338.9662;
        asteroid_mean1[36] = 46.8941;
        asteroid_mean2[36] = 137.4949;
        asteroid_meanmotion[36] = 0.004544767;

        /*  Asteroid 618  */
        /*
        618a C 124.0 25 12 3 -0.1922E-09 8
        618b C 3.188924 0.076291 17.0217 110.6385 236.9940 233.5584 0.003019988 47800.
        618c C 3.188924 0.076129 17.0223 110.8222 236.5542 296.2907 0.003019988 44000.
        */
        mu_planet[47] = (4 * Math.PI / 3) * Math.pow((120.29 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[37] = 3.188924;
        asteroid_e1[37] = 0.076291;
        asteroid_e2[37] = 0.076129;
        asteroid_i1[37] = 17.0217;
        asteroid_i2[37] = 17.0223;
        asteroid_omega1[37] = 110.6385;
        asteroid_omega2[37] = 110.8222;
        asteroid_w1[37] = 236.9940;
        asteroid_w2[37] = 236.5542;
        asteroid_mean1[37] = 233.5584;
        asteroid_mean2[37] = 296.2907;
        asteroid_meanmotion[37] = 0.003019988;

        /*  Asteroid 635  */
        /*
        635a C 100.0 25 12 3 0.1691E-09 8
        635b C 3.138979 0.085587 11.0271 183.0238 223.6227 144.1806 0.003092251 47800.
        635c C 3.138979 0.085597 11.0236 183.2395 223.1897 191.1409 0.003092251 44000.
        */
        mu_planet[48] = (4 * Math.PI / 3) * Math.pow((98.24 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[38] = 3.138979;
        asteroid_e1[38] = 0.085587;
        asteroid_e2[38] = 0.085597;
        asteroid_i1[38] = 11.0271;
        asteroid_i2[38] = 11.0236;
        asteroid_omega1[38] = 183.0238;
        asteroid_omega2[38] = 183.2395;
        asteroid_w1[38] = 223.6227;
        asteroid_w2[38] = 223.1897;
        asteroid_mean1[38] = 144.1806;
        asteroid_mean2[38] = 191.1409;
        asteroid_meanmotion[38] = 0.003092251;

        /*  Asteroid 21  */
        /*
        21a M 99.5 19 9 2 0.3429E-11 6
        21b M 2.435281 0.162555 3.0695 80.4949 249.2953 64.4146 0.004525844 47800.
        21c M 2.435281 0.162518 3.0707 80.5753 249.1152 159.1298 0.004525844 44000.
        */
        mu_planet[49] = (4 * Math.PI / 3) * Math.pow((95.76 / 2), 3) * 5.3E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[39] = 2.435281;
        asteroid_e1[39] = 0.162555;
        asteroid_e2[39] = 0.162518;
        asteroid_i1[39] = 3.0695;
        asteroid_i2[39] = 3.0707;
        asteroid_omega1[39] = 80.4949;
        asteroid_omega2[39] = 80.5753;
        asteroid_w1[39] = 249.2953;
        asteroid_w2[39] = 249.1152;
        asteroid_mean1[39] = 64.4146;
        asteroid_mean2[39] = 159.1298;
        asteroid_meanmotion[39] = 0.004525844;

        /*  Asteroid 42  */
        /*
        42a S 107.0 19 9 2 -0.3921E-11 6
        42b S 2.441213 0.224748 8.5255 84.0128 236.3866 292.8485 0.004509347 47800.
        42c S 2.441213 0.224676 8.5291 84.1331 236.1682 31.1538 0.004509347 44000.
        */
        mu_planet[50] = (4 * Math.PI / 3) * Math.pow((100.20 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[40] = 2.441213;
        asteroid_e1[40] = 0.224748;
        asteroid_e2[40] = 0.224676;
        asteroid_i1[40] = 8.5255;
        asteroid_i2[40] = 8.5291;
        asteroid_omega1[40] = 84.0128;
        asteroid_omega2[40] = 84.1331;
        asteroid_w1[40] = 236.3866;
        asteroid_w2[40] = 236.1682;
        asteroid_mean1[40] = 292.8485;
        asteroid_mean2[40] = 31.1538;
        asteroid_meanmotion[40] = 0.004509347;

        /*  Asteroid 19  */
        /*
        19a G 200.0 19 9 2 0.1038E-10 5
        19b C 2.442064 0.157741 1.5699 210.8678 181.9084 287.7448 0.004507001 47800.
        19c C 2.442064 0.157760 1.5672 211.0153 181.6658 26.5577 0.004507001 44000.
        */
        mu_planet[51] = (4 * Math.PI / 3) * Math.pow((200.0 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[41] = 2.442064;
        asteroid_e1[41] = 0.157741;
        asteroid_e2[41] = 0.157760;
        asteroid_i1[41] = 1.5699;
        asteroid_i2[41] = 1.5672;
        asteroid_omega1[41] = 210.8678;
        asteroid_omega2[41] = 211.0153;
        asteroid_w1[41] = 181.9084;
        asteroid_w2[41] = 181.6658;
        asteroid_mean1[41] = 287.7448;
        asteroid_mean2[41] = 26.5577;
        asteroid_meanmotion[41] = 0.004507001;

        /*  Asteroid 11  */
        /*
        11a S 162.0 19 9 2 -0.1552E-10 5
        11b S 2.452280 0.100960 4.6338 125.0624 194.6403 28.0972 0.004478869 47800.
        11c S 2.452280 0.100921 4.6326 125.1492 194.4545 133.0390 0.004478869 44000.
        */
        mu_planet[52] = (4 * Math.PI / 3) * Math.pow((153.33 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[42] = 2.452280;
        asteroid_e1[42] = 0.100960;
        asteroid_e2[42] = 0.100921;
        asteroid_i1[42] = 4.6338;
        asteroid_i2[42] = 4.6326;
        asteroid_omega1[42] = 125.0624;
        asteroid_omega2[42] = 125.1492;
        asteroid_w1[42] = 194.6403;
        asteroid_w2[42] = 194.4545;
        asteroid_mean1[42] = 28.0972;
        asteroid_mean2[42] = 133.0390;
        asteroid_meanmotion[42] = 0.004478869;

        /*  Asteroid 168  */
        /*
        168a C 154.0 25 12 4 0.2403E-09 10
        168b C 3.378470 0.059153 4.6351 206.6178 181.0930 50.5109 0.002768631 47800.
        168c C 3.378470 0.059247 4.6271 207.0170 180.4051 168.0024 0.002768631 44000.
        */
        mu_planet[53] = (4 * Math.PI / 3) * Math.pow((148.39 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[43] = 3.378470;
        asteroid_e1[43] = 0.059153;
        asteroid_e2[43] = 0.059247;
        asteroid_i1[43] = 4.6351;
        asteroid_i2[43] = 4.6271;
        asteroid_omega1[43] = 206.6178;
        asteroid_omega2[43] = 207.0170;
        asteroid_w1[43] = 181.0930;
        asteroid_w2[43] = 180.4051;
        asteroid_mean1[43] = 50.5109;
        asteroid_mean2[43] = 168.0024;
        asteroid_meanmotion[43] = 0.002768631;

        /*  Asteroid 17  */
        /*
        17a S 93.2 19 9 3 0.2791E-10 6
        17b S 2.471179 0.133234 5.6072 125.0429 137.5489 77.1749 0.004427558 47800.
        17c S 2.471179 0.133188 5.6049 125.1502 137.2997 193.3312 0.004427558 44000.
        */
        mu_planet[54] = (4 * Math.PI / 3) * Math.pow((90.04 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[44] = 2.471179;
        asteroid_e1[44] = 0.133234;
        asteroid_e2[44] = 0.133188;
        asteroid_i1[44] = 5.6072;
        asteroid_i2[44] = 5.6049;
        asteroid_omega1[44] = 125.0429;
        asteroid_omega2[44] = 125.1502;
        asteroid_w1[44] = 137.5489;
        asteroid_w2[44] = 137.2997;
        asteroid_mean1[44] = 77.1749;
        asteroid_mean2[44] = 193.3312;
        asteroid_meanmotion[44] = 0.004427558;

        /*  Asteroid 1467  */
        /*
        1467a C 112.0 25 12 4 -0.8949E-10 11 amp 0.10 radians
        1467b C 3.381960 0.131636 21.9256 326.5530 342.5212 79.7924 0.002764988 47800.
        1467c C 3.381960 0.131893 21.9223 326.7868 341.8940 198.1818 0.002764988 44000.
        */
        mu_planet[55] = (4 * Math.PI / 3) * Math.pow((112.0 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[45] = 3.381960;
        asteroid_e1[45] = 0.131636;
        asteroid_e2[45] = 0.131893;
        asteroid_i1[45] = 21.9256;
        asteroid_i2[45] = 21.9223;
        asteroid_omega1[45] = 326.5530;
        asteroid_omega2[45] = 326.7868;
        asteroid_w1[45] = 342.5212;
        asteroid_w2[45] = 341.8940;
        asteroid_mean1[45] = 79.7924;
        asteroid_mean2[45] = 198.1818;
        asteroid_meanmotion[45] = 0.002764988;

        /*  Asteroid 329  */
        /*
        329a C 80.5 19 9 3 -0.1111E-10 6
        329b C 2.475555 0.026487 15.9533 178.1370 48.1762 330.9775 0.004415934 47800.
        329c C 2.475555 0.026439 15.9513 178.2414 47.9115 89.6832 0.004415934 44000.
        */
        mu_planet[56] = (4 * Math.PI / 3) * Math.pow((77.80 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[46] = 2.475555;
        asteroid_e1[46] = 0.026487;
        asteroid_e2[46] = 0.026439;
        asteroid_i1[46] = 15.9533;
        asteroid_i2[46] = 15.9513;
        asteroid_omega1[46] = 178.1370;
        asteroid_omega2[46] = 178.2414;
        asteroid_w1[46] = 48.1762;
        asteroid_w2[46] = 47.9115;
        asteroid_mean1[46] = 330.9775;
        asteroid_mean2[46] = 89.6832;
        asteroid_meanmotion[46] = 0.004415934;

        /*  Asteroid 46  */
        /*
        46a P 131.0 19 9 3 -0.4986E-10 6
        46b C 2.525687 0.168864 2.3327 180.9298 175.8911 297.8130 0.004284944 47800.
        46c C 2.525687 0.168830 2.3292 181.0461 175.6563 84.9964 0.004284944 44000.
        */
        mu_planet[57] = (4 * Math.PI / 3) * Math.pow((124.14 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[47] = 2.525687;
        asteroid_e1[47] = 0.168864;
        asteroid_e2[47] = 0.168830;
        asteroid_i1[47] = 2.3327;
        asteroid_i2[47] = 2.3292;
        asteroid_omega1[47] = 180.9298;
        asteroid_omega2[47] = 181.0461;
        asteroid_w1[47] = 175.8911;
        asteroid_w2[47] = 175.6563;
        asteroid_mean1[47] = 297.8130;
        asteroid_mean2[47] = 84.9964;
        asteroid_meanmotion[47] = 0.004284944;

        /*  Asteroid 751  */
        /*
        751a C 115.0 19 9 2 0.2826E-10 6
        751b C 2.551232 0.153214 15.5758 78.4339 301.8997 104.5080 0.004220832 47800.
        751c C 2.551232 0.153293 15.5748 78.5510 301.7085 265.6060 0.004220832 44000.
        */
        mu_planet[58] = (4 * Math.PI / 3) * Math.pow((110.50 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[48] = 2.551232;
        asteroid_e1[48] = 0.153214;
        asteroid_e2[48] = 0.153293;
        asteroid_i1[48] = 15.5758;
        asteroid_i2[48] = 15.5748;
        asteroid_omega1[48] = 78.4339;
        asteroid_omega2[48] = 78.5510;
        asteroid_w1[48] = 301.8997;
        asteroid_w2[48] = 301.7085;
        asteroid_mean1[48] = 104.5080;
        asteroid_mean2[48] = 265.6060;
        asteroid_meanmotion[48] = 0.004220832;

        /*  Asteroid 89  */
        /*
        89a S 159.0 19 9 3 -0.1654E-10 6
        89b S 2.551633 0.182562 16.0981 311.1525 45.0590 45.8613 0.004219864 47800.
        89c S 2.551633 0.182418 16.1034 311.2820 44.8523 207.1732 0.004219864 44000.
        */
        mu_planet[59] = (4 * Math.PI / 3) * Math.pow((151.46 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[49] = 2.551633;
        asteroid_e1[49] = 0.182562;
        asteroid_e2[49] = 0.182418;
        asteroid_i1[49] = 16.0981;
        asteroid_i2[49] = 16.1034;
        asteroid_omega1[49] = 311.1525;
        asteroid_omega2[49] = 311.2820;
        asteroid_w1[49] = 45.0590;
        asteroid_w2[49] = 44.8523;
        asteroid_mean1[49] = 45.8613;
        asteroid_mean2[49] = 207.1732;
        asteroid_meanmotion[49] = 0.004219864;

        /*  Asteroid 449  */
        /*
        449a C 88.6 19 9 3 -0.2779E-10 6
        449b C 2.553406 0.169305 3.0877 85.4515 47.4394 286.1480 0.004215286 47800.
        449c C 2.553406 0.169389 3.0892 85.5506 47.1666 88.5529 0.004215286 44000.
        */
        mu_planet[60] = (4 * Math.PI / 3) * Math.pow((85.59 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[50] = 2.553406;
        asteroid_e1[50] = 0.169305;
        asteroid_e2[50] = 0.169389;
        asteroid_i1[50] = 3.0877;
        asteroid_i2[50] = 3.0892;
        asteroid_omega1[50] = 85.4515;
        asteroid_omega2[50] = 85.5506;
        asteroid_w1[50] = 47.4394;
        asteroid_w2[50] = 47.1666;
        asteroid_mean1[50] = 286.1480;
        asteroid_mean2[50] = 88.5529;
        asteroid_meanmotion[50] = 0.004215286;

        /*  Asteroid 334  */
        /*
        334a C 170.0 25 12 8 0.2157E-07 12 amp 0.16 radians
        334b C 3.892623 0.039922 4.6696 130.0728 167.5634 265.3288 0.002237845 47800.
        334c C 3.892623 0.037555 4.6599 130.6298 165.1775 139.9254 0.002237845 44000.
        */
        mu_planet[61] = (4 * Math.PI / 3) * Math.pow((155.82 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[51] = 3.892623;
        asteroid_e1[51] = 0.039922;
        asteroid_e2[51] = 0.037555;
        asteroid_i1[51] = 4.6696;
        asteroid_i2[51] = 4.6599;
        asteroid_omega1[51] = 130.0728;
        asteroid_omega2[51] = 130.6298;
        asteroid_w1[51] = 167.5634;
        asteroid_w2[51] = 165.1775;
        asteroid_mean1[51] = 265.3288;
        asteroid_mean2[51] = 139.9254;
        asteroid_meanmotion[51] = 0.002237845;

        /*  Asteroid 134  */
        /*
        134a C 107.0 19 9 2 -0.2230E-10 6
        134b C 2.564043 0.115255 11.6037 345.7651 83.7938 38.2323 0.004189209 47800.
        134c C 2.564043 0.115317 11.6061 345.9040 83.5769 206.2191 0.004189209 44000.
        */
        mu_planet[62] = (4 * Math.PI / 3) * Math.pow((123.27 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[52] = 2.564043;
        asteroid_e1[52] = 0.115255;
        asteroid_e2[52] = 0.115317;
        asteroid_i1[52] = 11.6037;
        asteroid_i2[52] = 11.6061;
        asteroid_omega1[52] = 345.7651;
        asteroid_omega2[52] = 345.9040;
        asteroid_w1[52] = 83.7938;
        asteroid_w2[52] = 83.5769;
        asteroid_mean1[52] = 38.2323;
        asteroid_mean2[52] = 206.2191;
        asteroid_meanmotion[52] = 0.004189209;

        /*  Asteroid 535  */
        /*
        535a C  77.0 19 9 2 0.4574E-10 5
        535b C 2.569192 0.025570 6.7738 84.3894 68.5377 335.9014 0.004176565 47800.
        535c C 2.569192 0.025621 6.7746 84.4986 68.1642 146.8275 0.004176565 44000.
        */
        mu_planet[63] = (4 * Math.PI / 3) * Math.pow((74.49 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[53] = 2.569192;
        asteroid_e1[53] = 0.025570;
        asteroid_e2[53] = 0.025621;
        asteroid_i1[53] = 6.7738;
        asteroid_i2[53] = 6.7746;
        asteroid_omega1[53] = 84.3894;
        asteroid_omega2[53] = 84.4986;
        asteroid_w1[53] = 68.5377;
        asteroid_w2[53] = 68.1642;
        asteroid_mean1[53] = 335.9014;
        asteroid_mean2[53] = 146.8275;
        asteroid_meanmotion[53] = 0.004176565;

        /*  Asteroid 626  */
        /*
        626a C 104.0 19 9 2 0.5101E-10 6
        626b C 2.573802 0.242805 25.3951 341.3175 43.1499 98.7815 0.004165574 47800.
        626c C 2.573802 0.242496 25.4061 341.4293 42.9969 271.8775 0.004165574 44000.
        */
        mu_planet[64] = (4 * Math.PI / 3) * Math.pow((100.73 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[54] = 2.573802;
        asteroid_e1[54] = 0.242805;
        asteroid_e2[54] = 0.242496;
        asteroid_i1[54] = 25.3951;
        asteroid_i2[54] = 25.4061;
        asteroid_omega1[54] = 341.3175;
        asteroid_omega2[54] = 341.4293;
        asteroid_w1[54] = 43.1499;
        asteroid_w2[54] = 42.9969;
        asteroid_mean1[54] = 98.7815;
        asteroid_mean2[54] = 271.8775;
        asteroid_meanmotion[54] = 0.004165574;

        /*  Asteroid 5  */
        /*
        5a S 125.0 19 9 3 -0.1544E-11 9
        5b S 2.576167 0.189000 5.3562 141.1788 357.1611 228.3737 0.004159513 47800.
        5c S 2.576167 0.189081 5.3534 141.2915 356.8777 42.9188 0.004159513 44000.
        */
        mu_planet[65] = (4 * Math.PI / 3) * Math.pow((119.07 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[55] = 2.576167;
        asteroid_e1[55] = 0.189000;
        asteroid_e2[55] = 0.189081;
        asteroid_i1[55] = 5.3562;
        asteroid_i2[55] = 5.3534;
        asteroid_omega1[55] = 141.1788;
        asteroid_omega2[55] = 141.2915;
        asteroid_w1[55] = 357.1611;
        asteroid_w2[55] = 356.8777;
        asteroid_mean1[55] = 228.3737;
        asteroid_mean2[55] = 42.9188;
        asteroid_meanmotion[55] = 0.004159513;

        /*  Asteroid 712  */
        /*
        712a C 132.0 19 9 2 0.2201E-11 6
        712b C 2.575181 0.187523 12.7844 230.4393 180.6102 42.1922 0.004162030 47800.
        712c C 2.575181 0.187569 12.7824 230.5671 180.3566 216.1444 0.004162030 44000.
        */
        mu_planet[66] = (4 * Math.PI / 3) * Math.pow((127.57 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[56] = 2.575181;
        asteroid_e1[56] = 0.187523;
        asteroid_e2[56] = 0.187569;
        asteroid_i1[56] = 12.7844;
        asteroid_i2[56] = 12.7824;
        asteroid_omega1[56] = 230.4393;
        asteroid_omega2[56] = 230.5671;
        asteroid_w1[56] = 180.6102;
        asteroid_w2[56] = 180.3566;
        asteroid_mean1[56] = 42.1922;
        asteroid_mean2[56] = 216.1444;
        asteroid_meanmotion[56] = 0.004162030;

        /*  Asteroid 107  */
        /*
        107a C 237.0 25 12 4 0.9322E-09 12
        107b C 3.486048 0.074166 9.9763 173.2376 299.8894 136.7929 0.002641464 47800.
        107c C 3.486048 0.074572 9.9639 173.6167 299.1223 282.0710 0.002641464 44000.
        */
        mu_planet[67] = (4 * Math.PI / 3) * Math.pow((222.62 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[57] = 3.486048;
        asteroid_e1[57] = 0.074166;
        asteroid_e2[57] = 0.074572;
        asteroid_i1[57] = 9.9763;
        asteroid_i2[57] = 9.9639;
        asteroid_omega1[57] = 173.2376;
        asteroid_omega2[57] = 173.6167;
        asteroid_w1[57] = 299.8894;
        asteroid_w2[57] = 299.1223;
        asteroid_mean1[57] = 136.7929;
        asteroid_mean2[57] = 282.0710;
        asteroid_meanmotion[57] = 0.002641464;

        /*  Asteroid 409  */
        /*
        409a CX 168.0 19 9 2 -0.3340E-10 6
        409b C 2.576267 0.071288 11.2590 241.9180 354.0479 84.3954 0.004159412 47800.
        409c C 2.576267 0.071264 11.2566 242.0550 353.7225 258.9802 0.004159412 44000.
        */
        mu_planet[68] = (4 * Math.PI / 3) * Math.pow((161.60 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[58] = 2.576267;
        asteroid_e1[58] = 0.071288;
        asteroid_e2[58] = 0.071264;
        asteroid_i1[58] = 11.2590;
        asteroid_i2[58] = 11.2566;
        asteroid_omega1[58] = 241.9180;
        asteroid_omega2[58] = 242.0550;
        asteroid_w1[58] = 354.0479;
        asteroid_w2[58] = 353.7225;
        asteroid_mean1[58] = 84.3954;
        asteroid_mean2[58] = 258.9802;
        asteroid_meanmotion[58] = 0.004159412;

        /*  Asteroid 362  */
        /*
        362a CX 98.0 19 9 2 0.5557E-10 5
        362b C 2.578861 0.044585 8.0579 26.9665 30.5670 229.2487 0.004153109 47800.
        362c C 2.578861 0.044631 8.0608 27.0910 30.3785 45.0812 0.004153109 44000.
        */
        mu_planet[69] = (4 * Math.PI / 3) * Math.pow((98.0 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[59] = 2.578861;
        asteroid_e1[59] = 0.044585;
        asteroid_e2[59] = 0.044631;
        asteroid_i1[59] = 8.0579;
        asteroid_i2[59] = 8.0608;
        asteroid_omega1[59] = 26.9665;
        asteroid_omega2[59] = 27.0910;
        asteroid_w1[59] = 30.5670;
        asteroid_w2[59] = 30.3785;
        asteroid_mean1[59] = 229.2487;
        asteroid_mean2[59] = 45.0812;
        asteroid_meanmotion[59] = 0.004153109;

        /*  Asteroid 405  */
        /*
        405a C 129.0 25 12 3 -0.3905E-11 11
        405b C 2.582540 0.246574 11.9033 255.0296 307.7592 320.4962 0.004144178 47800.
        405c C 2.582540 0.246705 11.8918 255.2339 307.4306 138.3336 0.004144178 44000.
        */
        mu_planet[70] = (4 * Math.PI / 3) * Math.pow((124.9 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[60] = 2.582540;
        asteroid_e1[60] = 0.246574;
        asteroid_e2[60] = 0.246705;
        asteroid_i1[60] = 11.9033;
        asteroid_i2[60] = 11.8918;
        asteroid_omega1[60] = 255.0296;
        asteroid_omega2[60] = 255.2339;
        asteroid_w1[60] = 307.7592;
        asteroid_w2[60] = 307.4306;
        asteroid_mean1[60] = 320.4962;
        asteroid_mean2[60] = 138.3336;
        asteroid_meanmotion[60] = 0.004144178;

        /*  Asteroid 32  */
        /*
        32a S 82.6 19 9 2 0.9739E-10 6
        32b S 2.586961 0.082775 5.5200 220.0440 337.3163 355.9204 0.004133575 47800.
        32c S 2.586961 0.082784 5.5166 220.1989 336.9597 176.1438 0.004133575 44000.
        */
        mu_planet[71] = (4 * Math.PI / 3) * Math.pow((80.76 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[61] = 2.586961;
        asteroid_e1[61] = 0.082775;
        asteroid_e2[61] = 0.082784;
        asteroid_i1[61] = 5.5200;
        asteroid_i2[61] = 5.5166;
        asteroid_omega1[61] = 220.0440;
        asteroid_omega2[61] = 220.1989;
        asteroid_w1[61] = 337.3163;
        asteroid_w2[61] = 336.9597;
        asteroid_mean1[61] = 355.9204;
        asteroid_mean2[61] = 176.1438;
        asteroid_meanmotion[61] = 0.004133575;

        /*  Asteroid 14  */
        /*
        14a S 167.0 19 9 2 -0.2117E-09 8
        14b S 2.587576 0.164500 9.1204 86.0763 95.9441 128.3571 0.004132067 47800.
        14c S 2.587576 0.164528 9.1204 86.2314 95.6343 308.8619 0.004132067 44000.
        */
        mu_planet[72] = (4 * Math.PI / 3) * Math.pow((167.0 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[62] = 2.587576;
        asteroid_e1[62] = 0.164500;
        asteroid_e2[62] = 0.164528;
        asteroid_i1[62] = 9.1204;
        asteroid_i2[62] = 9.1204;
        asteroid_omega1[62] = 86.0763;
        asteroid_omega2[62] = 86.2314;
        asteroid_w1[62] = 95.9441;
        asteroid_w2[62] = 95.6343;
        asteroid_mean1[62] = 128.3571;
        asteroid_mean2[62] = 308.8619;
        asteroid_meanmotion[62] = 0.004132067;

        /*  Asteroid 91  */
        /*
        91a CP 114.0 19 9 2 0.6259E-10 6
        91b C 2.589914 0.106221 2.1115 10.3234 73.1837 134.2688 0.004126498 47800.
        91c C 2.589914 0.106300 2.1147 10.4721 72.9044 315.9619 0.004126498 44000.
        */
        mu_planet[73] = (4 * Math.PI / 3) * Math.pow((109.82 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[63] = 2.589914;
        asteroid_e1[63] = 0.106221;
        asteroid_e2[63] = 0.106300;
        asteroid_i1[63] = 2.1115;
        asteroid_i2[63] = 2.1147;
        asteroid_omega1[63] = 10.3234;
        asteroid_omega2[63] = 10.4721;
        asteroid_w1[63] = 73.1837;
        asteroid_w2[63] = 72.9044;
        asteroid_mean1[63] = 134.2688;
        asteroid_mean2[63] = 315.9619;
        asteroid_meanmotion[63] = 0.004126498;

        /*  Asteroid 111  */
        /*
        111a C 139.0 19 9 2 -0.1889E-10 6
        111b C 2.594079 0.101937 4.9218 305.4631 166.6295 38.8111 0.004116568 47800.
        111c C 2.594079 0.102016 4.9232 305.6305 166.3060 222.6917 0.004116568 44000.
        */
        mu_planet[74] = (4 * Math.PI / 3) * Math.pow((134.56 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[64] = 2.594079;
        asteroid_e1[64] = 0.101937;
        asteroid_e2[64] = 0.102016;
        asteroid_i1[64] = 4.9218;
        asteroid_i2[64] = 4.9232;
        asteroid_omega1[64] = 305.4631;
        asteroid_omega2[64] = 305.6305;
        asteroid_w1[64] = 166.6295;
        asteroid_w2[64] = 166.3060;
        asteroid_mean1[64] = 38.8111;
        asteroid_mean2[64] = 222.6917;
        asteroid_meanmotion[64] = 0.004116568;

        /*  Asteroid 1015  */
        /*
        1015a C 101.0 25 12 4 0.1237E-09 9 amp 0.13 radians
        1015b C 3.201628 0.096624 9.4346 120.5583 275.1401 259.4105 0.003001864 47800.
        1015c C 3.201628 0.096709 9.4322 120.7752 274.5161 326.2397 0.003001864 44000.
        */
        mu_planet[75] = (4 * Math.PI / 3) * Math.pow((96.94 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[65] = 3.201628;
        asteroid_e1[65] = 0.096624;
        asteroid_e2[65] = 0.096709;
        asteroid_i1[65] = 9.4346;
        asteroid_i2[65] = 9.4322;
        asteroid_omega1[65] = 120.5583;
        asteroid_omega2[65] = 120.7752;
        asteroid_w1[65] = 275.1401;
        asteroid_w2[65] = 274.5161;
        asteroid_mean1[65] = 259.4105;
        asteroid_mean2[65] = 326.2397;
        asteroid_meanmotion[65] = 0.003001864;

        /*  Asteroid 404  */
        /*
        404a C 101.0 19 9 3 -0.7543E-11 8
        404b C 2.592159 0.201712 14.0993 92.2237 120.3618 247.7171 0.004121154 47800.
        404c C 2.592159 0.201792 14.0940 92.3811 120.0835 70.5640 0.004121154 44000.
        */
        mu_planet[76] = (4 * Math.PI / 3) * Math.pow((97.71 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[66] = 2.592159;
        asteroid_e1[66] = 0.201712;
        asteroid_e2[66] = 0.201792;
        asteroid_i1[66] = 14.0993;
        asteroid_i2[66] = 14.0940;
        asteroid_omega1[66] = 92.2237;
        asteroid_omega2[66] = 92.3811;
        asteroid_w1[66] = 120.3618;
        asteroid_w2[66] = 120.0835;
        asteroid_mean1[66] = 247.7171;
        asteroid_mean2[66] = 70.5640;
        asteroid_meanmotion[66] = 0.004121154;

        /*  Asteroid 344  */
        /*
        344a C  138.0 23 11 2 0.8719E-10 11
        344b C 2.594330 0.314747 18.4278 48.0302 236.2860 149.5414 0.004116036 47800.
        344c C 2.594330 0.314453 18.4470 48.2063 236.0456 333.4460 0.004116036 44000.
        */
        mu_planet[77] = (4 * Math.PI / 3) * Math.pow((132.25 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[67] = 2.594330;
        asteroid_e1[67] = 0.314747;
        asteroid_e2[67] = 0.314453;
        asteroid_i1[67] = 18.4278;
        asteroid_i2[67] = 18.4470;
        asteroid_omega1[67] = 48.0302;
        asteroid_omega2[67] = 48.2063;
        asteroid_w1[67] = 236.2860;
        asteroid_w2[67] = 236.0456;
        asteroid_mean1[67] = 149.5414;
        asteroid_mean2[67] = 333.4460;
        asteroid_meanmotion[67] = 0.004116036;

        /*  Asteroid 849  */
        /*
        849a M 98.0 25 12 4 0.1438E-09 12
        849b M 3.155161 0.191152 19.5558 228.5008 60.6011 159.4822 0.003068677 47800.
        849c M 3.155161 0.190631 19.5659 228.7279 60.2276 211.5041 0.003068677 44000.
        */
        mu_planet[78] = (4 * Math.PI / 3) * Math.pow((61.82 / 2), 3) * 5.3E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[68] = 3.155161;
        asteroid_e1[68] = 0.191152;
        asteroid_e2[68] = 0.190631;
        asteroid_i1[68] = 19.5558;
        asteroid_i2[68] = 19.5659;
        asteroid_omega1[68] = 228.5008;
        asteroid_omega2[68] = 228.7279;
        asteroid_w1[68] = 60.6011;
        asteroid_w2[68] = 60.2276;
        asteroid_mean1[68] = 159.4822;
        asteroid_mean2[68] = 211.5041;
        asteroid_meanmotion[68] = 0.003068677;

        /*  Asteroid 389  */
        /*
        389a S 81.6 23 11 2 -0.2017E-10 6
        389b S 2.608501 0.065608 8.1320 281.9759 265.0845 216.1440 0.004082497 47800.
        389c S 2.608501 0.065613 8.1322 282.1411 264.7456 47.4603 0.004082497 44000.
        */
        mu_planet[79] = (4 * Math.PI / 3) * Math.pow((78.98 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[69] = 2.608501;
        asteroid_e1[69] = 0.065608;
        asteroid_e2[69] = 0.065613;
        asteroid_i1[69] = 8.1320;
        asteroid_i2[69] = 8.1322;
        asteroid_omega1[69] = 281.9759;
        asteroid_omega2[69] = 282.1411;
        asteroid_w1[69] = 265.0845;
        asteroid_w2[69] = 264.7456;
        asteroid_mean1[69] = 216.1440;
        asteroid_mean2[69] = 47.4603;
        asteroid_meanmotion[69] = 0.004082497;

        /*  Asteroid 347  */
        /*
        347a M 54.1 23 11 2 -0.7008E-10 8
        347b M 2.613008 0.163798 11.6982 85.2867 85.4629 242.0698 0.004071903 47800.
        347c M 2.613008 0.163823 11.6994 85.4445 85.1758 75.6481 0.004071903 44000.
        */
        mu_planet[80] = (4 * Math.PI / 3) * Math.pow((51.28 / 2), 3) * 5.3E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[70] = 2.613008;
        asteroid_e1[70] = 0.163798;
        asteroid_e2[70] = 0.163823;
        asteroid_i1[70] = 11.6982;
        asteroid_i2[70] = 11.6994;
        asteroid_omega1[70] = 85.2867;
        asteroid_omega2[70] = 85.4445;
        asteroid_w1[70] = 85.4629;
        asteroid_w2[70] = 85.1758;
        asteroid_mean1[70] = 242.0698;
        asteroid_mean2[70] = 75.6481;
        asteroid_meanmotion[70] = 0.004071903;

        /*  Asteroid 70  */
        /*
        70a C 127.0 23 11 2 -0.5624E-11 6
        70b C 2.614748 0.182667 11.5946 47.5006 254.7629 73.5509 0.004067860 47800.
        70c C 2.614748 0.182574 11.5994 47.6537 254.5105 267.9798 0.004067860 44000.
        */
        mu_planet[81] = (4 * Math.PI / 3) * Math.pow((122.17 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[71] = 2.614748;
        asteroid_e1[71] = 0.182667;
        asteroid_e2[71] = 0.182574;
        asteroid_i1[71] = 11.5946;
        asteroid_i2[71] = 11.5994;
        asteroid_omega1[71] = 47.5006;
        asteroid_omega2[71] = 47.6537;
        asteroid_w1[71] = 254.7629;
        asteroid_w2[71] = 254.5105;
        asteroid_mean1[71] = 73.5509;
        asteroid_mean2[71] = 267.9798;
        asteroid_meanmotion[71] = 0.004067860;

        /*  Asteroid 194  */
        /*
        194a C 174.0 23 11 2 0.4203E-10 8
        194b C 2.616362 0.237733 18.5076 158.9650 162.8388 280.8321 0.004064149 47800.
        194c C 2.616362 0.237836 18.4986 159.0850 162.5989 116.0896 0.004064149 44000.
        */
        mu_planet[82] = (4 * Math.PI / 3) * Math.pow((168.42 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[72] = 2.616362;
        asteroid_e1[72] = 0.237733;
        asteroid_e2[72] = 0.237836;
        asteroid_i1[72] = 18.5076;
        asteroid_i2[72] = 18.4986;
        asteroid_omega1[72] = 158.9650;
        asteroid_omega2[72] = 159.0850;
        asteroid_w1[72] = 162.8388;
        asteroid_w2[72] = 162.5989;
        asteroid_mean1[72] = 280.8321;
        asteroid_mean2[72] = 116.0896;
        asteroid_meanmotion[72] = 0.004064149;

        /*  Asteroid 53  */
        /*
        53a C 119.0 23 11 2 -0.1037E-09 8
        53b C 2.618937 0.203674 5.1607 143.3293 312.4746 73.2664 0.004058009 47800.
        53c C 2.618937 0.203772 5.1560 143.4704 312.1894 269.8847 0.004058009 44000.
        */
        mu_planet[83] = (4 * Math.PI / 3) * Math.pow((115.38 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[73] = 2.618937;
        asteroid_e1[73] = 0.203674;
        asteroid_e2[73] = 0.203772;
        asteroid_i1[73] = 5.1607;
        asteroid_i2[73] = 5.1560;
        asteroid_omega1[73] = 143.3293;
        asteroid_omega2[73] = 143.4704;
        asteroid_w1[73] = 312.4746;
        asteroid_w2[73] = 312.1894;
        asteroid_mean1[73] = 73.2664;
        asteroid_mean2[73] = 269.8847;
        asteroid_meanmotion[73] = 0.004058009;

        /*  Asteroid 78  */
        /*
        78a C 125.0 23 11 2 -0.1922E-09 11
        78b C 2.621218 0.205752 8.6763 333.1378 151.5187 330.6226 0.004052728 47800.
        78c C 2.621218 0.205870 8.6761 333.2967 151.2008 168.4056 0.004052728 44000.
        */
        mu_planet[84] = (4 * Math.PI / 3) * Math.pow((120.60 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[74] = 2.621218;
        asteroid_e1[74] = 0.205752;
        asteroid_e2[74] = 0.205870;
        asteroid_i1[74] = 8.6763;
        asteroid_i2[74] = 8.6761;
        asteroid_omega1[74] = 333.1378;
        asteroid_omega2[74] = 333.2967;
        asteroid_w1[74] = 151.5187;
        asteroid_w2[74] = 151.2008;
        asteroid_mean1[74] = 330.6226;
        asteroid_mean2[74] = 168.4056;
        asteroid_meanmotion[74] = 0.004052728;

        /*  Asteroid 407  */
        /*
        407a C 97.6 23 11 2 0.1630E-11 6
        407b C 2.624757 0.069946 7.5301 294.3029 80.9935 44.6471 0.004044627 47800.
        407c C 2.624757 0.069946 7.5310 294.4626 80.7682 244.1005 0.004044627 44000.
        */
        mu_planet[85] = (4 * Math.PI / 3) * Math.pow((95.07 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[75] = 2.624757;
        asteroid_e1[75] = 0.069946;
        asteroid_e2[75] = 0.069946;
        asteroid_i1[75] = 7.5301;
        asteroid_i2[75] = 7.5310;
        asteroid_omega1[75] = 294.3029;
        asteroid_omega2[75] = 294.4626;
        asteroid_w1[75] = 80.9935;
        asteroid_w2[75] = 80.7682;
        asteroid_mean1[75] = 44.6471;
        asteroid_mean2[75] = 244.1005;
        asteroid_meanmotion[75] = 0.004044627;

        /*  Asteroid 454  */
        /*
        454a CB 84.5 23 11 2 0.8672E-10 11
        454b C 2.627103 0.111825 6.3045 31.9949 176.4423 350.6781 0.004039139 47800.
        454c C 2.627103 0.111801 6.3077 32.1265 176.1132 191.4584 0.004039139 44000.
        */
        mu_planet[86] = (4 * Math.PI / 3) * Math.pow((81.57 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[76] = 2.627103;
        asteroid_e1[76] = 0.111825;
        asteroid_e2[76] = 0.111801;
        asteroid_i1[76] = 6.3045;
        asteroid_i2[76] = 6.3077;
        asteroid_omega1[76] = 31.9949;
        asteroid_omega2[76] = 32.1265;
        asteroid_w1[76] = 176.4423;
        asteroid_w2[76] = 176.1132;
        asteroid_mean1[76] = 350.6781;
        asteroid_mean2[76] = 191.4584;
        asteroid_meanmotion[76] = 0.004039139;

        /*  Asteroid 23  */
        /*
        23a S 111.0 23 11 2 -0.1550E-09 11
        23b S 2.627841 0.232397 10.1423 66.6696 59.6202 357.4652 0.004037406 47800.
        23c S 2.627841 0.232417 10.1495 66.8535 59.3045 198.5570 0.004037406 44000.
        */
        mu_planet[87] = (4 * Math.PI / 3) * Math.pow((107.53 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[77] = 2.627841;
        asteroid_e1[77] = 0.232397;
        asteroid_e2[77] = 0.232417;
        asteroid_i1[77] = 10.1423;
        asteroid_i2[77] = 10.1495;
        asteroid_omega1[77] = 66.6696;
        asteroid_omega2[77] = 66.8535;
        asteroid_w1[77] = 59.6202;
        asteroid_w2[77] = 59.3045;
        asteroid_mean1[77] = 357.4652;
        asteroid_mean2[77] = 198.5570;
        asteroid_meanmotion[77] = 0.004037406;

        /*  Asteroid 124  */
        /*
        124a S 79.5 23 11 2 0.5395E-10 6
        124b S 2.629850 0.077494 2.9553 187.7334 61.1144 239.2842 0.004032820 47800.
        124c S 2.629850 0.077423 2.9522 187.8784 60.7806 81.4316 0.004032820 44000.
        */
        mu_planet[88] = (4 * Math.PI / 3) * Math.pow((76.36 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[78] = 2.629850;
        asteroid_e1[78] = 0.077494;
        asteroid_e2[78] = 0.077423;
        asteroid_i1[78] = 2.9553;
        asteroid_i2[78] = 2.9522;
        asteroid_omega1[78] = 187.7334;
        asteroid_omega2[78] = 187.8784;
        asteroid_w1[78] = 61.1144;
        asteroid_w2[78] = 60.7806;
        asteroid_mean1[78] = 239.2842;
        asteroid_mean2[78] = 81.4316;
        asteroid_meanmotion[78] = 0.004032820;

        /*  Asteroid 164  */
        /*
        164a C 110.0 23 11 2 0.3617E-10 8
        164b C 2.632976 0.345525 24.4670 76.6791 283.5211 119.2852 0.004025907 47800.
        164c C 2.632976 0.345709 24.4595 76.8487 283.3631 322.7372 0.004025907 44000.
        */
        mu_planet[89] = (4 * Math.PI / 3) * Math.pow((104.92 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[79] = 2.632976;
        asteroid_e1[79] = 0.345525;
        asteroid_e2[79] = 0.345709;
        asteroid_i1[79] = 24.4670;
        asteroid_i2[79] = 24.4595;
        asteroid_omega1[79] = 76.6791;
        asteroid_omega2[79] = 76.8487;
        asteroid_w1[79] = 283.5211;
        asteroid_w2[79] = 283.3631;
        asteroid_mean1[79] = 119.2852;
        asteroid_mean2[79] = 322.7372;
        asteroid_meanmotion[79] = 0.004025907;

        /*  Asteroid 37  */
        /*
        37a S 112.0 23 11 2 -0.5182E-10 6
        37b S 2.642133 0.175964 3.0734 7.1254 61.8511 23.9283 0.004004695 47800.
        37c S 2.642133 0.176034 3.0775 7.3096 61.5334 232.1438 0.004004695 44000.
        */
        mu_planet[90] = (4 * Math.PI / 3) * Math.pow((108.35 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[80] = 2.642133;
        asteroid_e1[80] = 0.175964;
        asteroid_e2[80] = 0.176034;
        asteroid_i1[80] = 3.0734;
        asteroid_i2[80] = 3.0775;
        asteroid_omega1[80] = 7.1254;
        asteroid_omega2[80] = 7.3096;
        asteroid_w1[80] = 61.8511;
        asteroid_w2[80] = 61.5334;
        asteroid_mean1[80] = 23.9283;
        asteroid_mean2[80] = 232.1438;
        asteroid_meanmotion[80] = 0.004004695;

        /*  Asteroid 72  */
        /*
        72a TD 89.3 16 7 2 -0.3984E-10 7
        72b C 2.266232 0.120618 5.4169 207.5817 102.2742 127.2158 0.005041752 47800.
        72c C 2.266232 0.120581 5.4147 207.6940 102.0810 109.5865 0.005041752 44000.
        */
        mu_planet[91] = (4 * Math.PI / 3) * Math.pow((86.11 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[81] = 2.266232;
        asteroid_e1[81] = 0.120618;
        asteroid_e2[81] = 0.120581;
        asteroid_i1[81] = 5.4169;
        asteroid_i2[81] = 5.4147;
        asteroid_omega1[81] = 207.5817;
        asteroid_omega2[81] = 207.6940;
        asteroid_w1[81] = 102.2742;
        asteroid_w2[81] = 102.0810;
        asteroid_mean1[81] = 127.2158;
        asteroid_mean2[81] = 109.5865;
        asteroid_meanmotion[81] = 0.005041752;

        /*  Asteroid 224  */
        /*
        224a M 70.0 23 11 2 0.1583E-10 6
        224b M 2.645168 0.044457 5.8424 352.6042 281.7162 278.8845 0.003997858 47800.
        224c M 2.645168 0.044382 5.8456 352.7588 281.4072 128.6094 0.003997858 44000.
        */
        mu_planet[92] = (4 * Math.PI / 3) * Math.pow((61.82 / 2), 3) * 5.3E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[82] = 2.645168;
        asteroid_e1[82] = 0.044457;
        asteroid_e2[82] = 0.044382;
        asteroid_i1[82] = 5.8424;
        asteroid_i2[82] = 5.8456;
        asteroid_omega1[82] = 352.6042;
        asteroid_omega2[82] = 352.7588;
        asteroid_w1[82] = 281.7162;
        asteroid_w2[82] = 281.4072;
        asteroid_mean1[82] = 278.8845;
        asteroid_mean2[82] = 128.6094;
        asteroid_meanmotion[82] = 0.003997858;

        /*  Asteroid 369  */
        /*
        369a M 62.2 23 11 2 -0.2480E-11 11
        369b M 2.648952 0.097543 12.7207 93.9365 268.4800 33.8649 0.003989342 47800.
        369c M 2.648952 0.097527 12.7212 94.0622 268.2860 245.3580 0.003989342 44000.
        */
        mu_planet[93] = (4 * Math.PI / 3) * Math.pow((60.0 / 2), 3) * 5.3E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[83] = 2.648952;
        asteroid_e1[83] = 0.097543;
        asteroid_e2[83] = 0.097527;
        asteroid_i1[83] = 12.7207;
        asteroid_i2[83] = 12.7212;
        asteroid_omega1[83] = 93.9365;
        asteroid_omega2[83] = 94.0622;
        asteroid_w1[83] = 268.4800;
        asteroid_w2[83] = 268.2860;
        asteroid_mean1[83] = 33.8649;
        asteroid_mean2[83] = 245.3580;
        asteroid_meanmotion[83] = 0.003989342;

        /*  Asteroid 50  */
        /*
        50a CX 88.0 21 10 2 -0.6997E-10 8 11:4 resonance?
        50b C 2.650030 0.286240 2.8366 173.2049 199.5121 211.8493 0.003986719 47800.
        50c C 2.650030 0.286241 2.8329 173.3240 199.2570 63.9811 0.003986719 44000.
        */
        mu_planet[94] = (4 * Math.PI / 3) * Math.pow((99.82 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[84] = 2.650030;
        asteroid_e1[84] = 0.286240;
        asteroid_e2[84] = 0.286241;
        asteroid_i1[84] = 2.8366;
        asteroid_i2[84] = 2.8329;
        asteroid_omega1[84] = 173.2049;
        asteroid_omega2[84] = 173.3240;
        asteroid_w1[84] = 199.5121;
        asteroid_w2[84] = 199.2570;
        asteroid_mean1[84] = 211.8493;
        asteroid_mean2[84] = 63.9811;
        asteroid_meanmotion[84] = 0.003986719;

        /*  Asteroid 476  */
        /*
        476a P 121.0 21 10 2 0.1863E-10 6 11:4 resonance?
        476b C 2.649753 0.074091 10.9341 286.0123 359.8770 198.3446 0.003987534 47800.
        476c C 2.649753 0.074035 10.9341 286.1618 359.5763 50.3141 0.003987534 44000.
        */
        mu_planet[95] = (4 * Math.PI / 3) * Math.pow((116.76 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[85] = 2.649753;
        asteroid_e1[85] = 0.074091;
        asteroid_e2[85] = 0.074035;
        asteroid_i1[85] = 10.9341;
        asteroid_i2[85] = 10.9341;
        asteroid_omega1[85] = 286.0123;
        asteroid_omega2[85] = 286.1618;
        asteroid_w1[85] = 359.8770;
        asteroid_w2[85] = 359.5763;
        asteroid_mean1[85] = 198.3446;
        asteroid_mean2[85] = 50.3141;
        asteroid_meanmotion[85] = 0.003987534;

        /*  Asteroid 713  */
        /*
        713a C 109.0 25 12 4 -0.8273E-09 10
        713b C 3.400747 0.156039 10.2371 218.5255 130.7074 34.4703 0.002741601 47800.
        713c C 3.400747 0.156147 10.2212 218.9424 129.9827 157.8658 0.002741601 44000.
        */
        mu_planet[96] = (4 * Math.PI / 3) * Math.pow((105.52 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[86] = 3.400747;
        asteroid_e1[86] = 0.156039;
        asteroid_e2[86] = 0.156147;
        asteroid_i1[86] = 10.2371;
        asteroid_i2[86] = 10.2212;
        asteroid_omega1[86] = 218.5255;
        asteroid_omega2[86] = 218.9424;
        asteroid_w1[86] = 130.7074;
        asteroid_w2[86] = 129.9827;
        asteroid_mean1[86] = 34.4703;
        asteroid_mean2[86] = 157.8658;
        asteroid_meanmotion[86] = 0.002741601;

        /*  Asteroid 85  */
        /*
        85a C 157.0 23 11 2 0.2084E-10 11
        85b C 2.653648 0.192698 11.9575 202.9063 122.3212 278.9259 0.003978736 47800.
        85c C 2.653648 0.192716 11.9508 203.0734 122.0628 132.7509 0.003978736 44000.
        */
        mu_planet[97] = (4 * Math.PI / 3) * Math.pow((154.79 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[87] = 2.653648;
        asteroid_e1[87] = 0.192698;
        asteroid_e2[87] = 0.192716;
        asteroid_i1[87] = 11.9575;
        asteroid_i2[87] = 11.9508;
        asteroid_omega1[87] = 202.9063;
        asteroid_omega2[87] = 203.0734;
        asteroid_w1[87] = 122.3212;
        asteroid_w2[87] = 122.0628;
        asteroid_mean1[87] = 278.9259;
        asteroid_mean2[87] = 132.7509;
        asteroid_meanmotion[87] = 0.003978736;

        /*  Asteroid 26  */
        /*
        26a S 98.7 23 11 2 0.5832E-11 11
        26b S 2.655641 0.088442 3.5638 45.4924 193.5300 165.9477 0.003974179 47800.
        26c S 2.655641 0.088378 3.5669 45.6095 193.2157 20.8710 0.003974179 44000.
        */
        mu_planet[98] = (4 * Math.PI / 3) * Math.pow((95.07 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[88] = 2.655641;
        asteroid_e1[88] = 0.088442;
        asteroid_e2[88] = 0.088378;
        asteroid_i1[88] = 3.5638;
        asteroid_i2[88] = 3.5669;
        asteroid_omega1[88] = 45.4924;
        asteroid_omega2[88] = 45.6095;
        asteroid_w1[88] = 193.5300;
        asteroid_w2[88] = 193.2157;
        asteroid_mean1[88] = 165.9477;
        asteroid_mean2[88] = 20.8710;
        asteroid_meanmotion[88] = 0.003974179;

        /*  Asteroid 455  */
        /*
        455a CP 87.5 23 11 2 0.5107E-10 11
        455b C 2.656852 0.293056 12.0247 76.2090 272.0876 150.7179 0.003971470 47800.
        455c C 2.656852 0.293043 12.0258 76.4176 271.7885 6.1241 0.003971470 44000.
        */
        mu_planet[99] = (4 * Math.PI / 3) * Math.pow((84.41 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[89] = 2.656852;
        asteroid_e1[89] = 0.293056;
        asteroid_e2[89] = 0.293043;
        asteroid_i1[89] = 12.0247;
        asteroid_i2[89] = 12.0258;
        asteroid_omega1[89] = 76.2090;
        asteroid_omega2[89] = 76.4176;
        asteroid_w1[89] = 272.0876;
        asteroid_w2[89] = 271.7885;
        asteroid_mean1[89] = 150.7179;
        asteroid_mean2[89] = 6.1241;
        asteroid_meanmotion[89] = 0.003971470;

        /*  Asteroid 144  */
        /*
        144a C 146.0 23 11 2 0.6859E-10 11
        144b C 2.655068 0.234353 4.8110 76.1215 293.1378 68.3518 0.003975427 47800.
        144c C 2.655068 0.234356 4.8110 76.2681 292.8626 282.9349 0.003975427 44000.
        */
        mu_planet[100] = (4 * Math.PI / 3) * Math.pow((141.76 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[90] = 2.655068;
        asteroid_e1[90] = 0.234353;
        asteroid_e2[90] = 0.234356;
        asteroid_i1[90] = 4.8110;
        asteroid_i2[90] = 4.8110;
        asteroid_omega1[90] = 76.1215;
        asteroid_omega2[90] = 76.2681;
        asteroid_w1[90] = 293.1378;
        asteroid_w2[90] = 292.8626;
        asteroid_mean1[90] = 68.3518;
        asteroid_mean2[90] = 282.9349;
        asteroid_meanmotion[90] = 0.003975427;

        /*  Asteroid 488  */
        /*
        488a C 158.0 25 12 4 -0.2184E-10 11 amp 0.11 radians
        488b C 3.154513 0.171640 11.4955 84.0747 72.4489 269.5038 0.003069287 47800.
        488c C 3.154513 0.171672 11.5028 84.3972 71.7742 321.5988 0.003069287 44000.
        */
        mu_planet[101] = (4 * Math.PI / 3) * Math.pow((150.12 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[91] = 3.154513;
        asteroid_e1[91] = 0.171640;
        asteroid_e2[91] = 0.171672;
        asteroid_i1[91] = 11.4955;
        asteroid_i2[91] = 11.5028;
        asteroid_omega1[91] = 84.0747;
        asteroid_omega2[91] = 84.3972;
        asteroid_w1[91] = 72.4489;
        asteroid_w2[91] = 71.7742;
        asteroid_mean1[91] = 269.5038;
        asteroid_mean2[91] = 321.5988;
        asteroid_meanmotion[91] = 0.003069287;

        /*  Asteroid 466  */
        /*
        466a C 121.0 25 12 5 -0.6697E-09 10
        466b C 3.364702 0.076880 19.0773 290.3599 255.9728 71.0988 0.002786267 47800.
        466c C 3.364702 0.076716 19.0814 290.6409 255.5207 184.6328 0.002786267 44000.
        */
        mu_planet[102] = (4 * Math.PI / 3) * Math.pow((115.53 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[92] = 3.364702;
        asteroid_e1[92] = 0.076880;
        asteroid_e2[92] = 0.076716;
        asteroid_i1[92] = 19.0773;
        asteroid_i2[92] = 19.0814;
        asteroid_omega1[92] = 290.3599;
        asteroid_omega2[92] = 290.6409;
        asteroid_w1[92] = 255.9728;
        asteroid_w2[92] = 255.5207;
        asteroid_mean1[92] = 71.0988;
        asteroid_mean2[92] = 184.6328;
        asteroid_meanmotion[92] = 0.002786267;

        /*  Asteroid 511  */
        /*
        511a C 337.0 25 12 4 0.1345E-09 10 amp 0.16 radians
        511b C 3.175197 0.179238 15.8820 107.5846 334.8766 256.6288 0.003039413 47800.
        511c C 3.175197 0.179619 15.8740 107.7911 334.2695 315.2764 0.003039413 44000.
        */
        mu_planet[103] = (4 * Math.PI / 3) * Math.pow((326.07 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[93] = 3.175197;
        asteroid_e1[93] = 0.179238;
        asteroid_e2[93] = 0.179619;
        asteroid_i1[93] = 15.8820;
        asteroid_i2[93] = 15.8740;
        asteroid_omega1[93] = 107.5846;
        asteroid_omega2[93] = 107.7911;
        asteroid_w1[93] = 334.8766;
        asteroid_w2[93] = 334.2695;
        asteroid_mean1[93] = 256.6288;
        asteroid_mean2[93] = 315.2764;
        asteroid_meanmotion[93] = 0.003039413;

        /*  Asteroid 240  */
        /*
        240a C 108.0 23 11 2 0.7414E-10 8
        240b C 2.664629 0.206954 2.1053 114.7871 299.9819 0.2058 0.003954040 47800.
        240c C 2.664629 0.207015 2.1040 114.8649 299.7660 219.4546 0.003954040 44000.
        */
        mu_planet[104] = (4 * Math.PI / 3) * Math.pow((103.90 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[94] = 2.664629;
        asteroid_e1[94] = 0.206954;
        asteroid_e2[94] = 0.207015;
        asteroid_i1[94] = 2.1053;
        asteroid_i2[94] = 2.1040;
        asteroid_omega1[94] = 114.7871;
        asteroid_omega2[94] = 114.8649;
        asteroid_w1[94] = 299.9819;
        asteroid_w2[94] = 299.7660;
        asteroid_mean1[94] = 0.2058;
        asteroid_mean2[94] = 219.4546;
        asteroid_meanmotion[94] = 0.003954040;

        /*  Asteroid 1036  */
        /*
        1036a S 41.0 23 11 2 -0.9658E-10 11
        1036b S 2.663001 0.536795 26.5319 215.4023 131.9277 344.1902 0.003958040 47800.
        1036c S 2.663001 0.537505 26.4691 215.6222 131.7159 202.4218 0.003958040 44000.
        */
        mu_planet[105] = (4 * Math.PI / 3) * Math.pow((31.66 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[95] = 2.663001;
        asteroid_e1[95] = 0.536795;
        asteroid_e2[95] = 0.537505;
        asteroid_i1[95] = 26.5319;
        asteroid_i2[95] = 26.4691;
        asteroid_omega1[95] = 215.4023;
        asteroid_omega2[95] = 215.6222;
        asteroid_w1[95] = 131.9277;
        asteroid_w2[95] = 131.7159;
        asteroid_mean1[95] = 344.1902;
        asteroid_mean2[95] = 202.4218;
        asteroid_meanmotion[95] = 0.003958040;

        /*  Asteroid 141  */
        /*
        141a C 135.0 23 11 2 -0.4496E-10 5
        141b C 2.665891 0.214254 11.8995 318.3098 57.3290 234.1963 0.003951355 47800.
        141c C 2.665891 0.214168 11.9063 318.4870 57.0648 93.9787 0.003951355 44000.
        */
        mu_planet[106] = (4 * Math.PI / 3) * Math.pow((131.03 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[96] = 2.665891;
        asteroid_e1[96] = 0.214254;
        asteroid_e2[96] = 0.214168;
        asteroid_i1[96] = 11.8995;
        asteroid_i2[96] = 11.9063;
        asteroid_omega1[96] = 318.3098;
        asteroid_omega2[96] = 318.4870;
        asteroid_w1[96] = 57.3290;
        asteroid_w2[96] = 57.0648;
        asteroid_mean1[96] = 234.1963;
        asteroid_mean2[96] = 93.9787;
        asteroid_meanmotion[96] = 0.003951355;

        /*  Asteroid 97  */
        /*
        97a M 87.1 23 11 2 -0.4945E-10 8
        97b M 2.668844 0.257017 11.7746 159.4752 267.9650 30.5145 0.003944740 47800.
        97c M 2.668844 0.257050 11.7717 159.6779 267.6684 251.7439 0.003944740 44000.
        */
        mu_planet[107] = (4 * Math.PI / 3) * Math.pow((82.83 / 2), 3) * 5.3E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[97] = 2.668844;
        asteroid_e1[97] = 0.257017;
        asteroid_e2[97] = 0.257050;
        asteroid_i1[97] = 11.7746;
        asteroid_i2[97] = 11.7717;
        asteroid_omega1[97] = 159.4752;
        asteroid_omega2[97] = 159.6779;
        asteroid_w1[97] = 267.9650;
        asteroid_w2[97] = 267.6684;
        asteroid_mean1[97] = 30.5145;
        asteroid_mean2[97] = 251.7439;
        asteroid_meanmotion[97] = 0.003944740;

        /*  Asteroid 326  */
        /*
        326a C 100.0 21 10 2 -0.4145E-12 10
        326b C 2.317595 0.189907 23.7293 31.7857 238.3181 168.6036 0.004875185 47800.
        326c C 2.317595 0.189739 23.7356 31.8788 238.1999 187.1843 0.004875185 44000.
        */
        mu_planet[108] = (4 * Math.PI / 3) * Math.pow((93.0 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[98] = 2.317595;
        asteroid_e1[98] = 0.189907;
        asteroid_e2[98] = 0.189739;
        asteroid_i1[98] = 23.7293;
        asteroid_i2[98] = 23.7356;
        asteroid_omega1[98] = 31.7857;
        asteroid_omega2[98] = 31.8788;
        asteroid_w1[98] = 238.3181;
        asteroid_w2[98] = 238.1999;
        asteroid_mean1[98] = 168.6036;
        asteroid_mean2[98] = 187.1843;
        asteroid_meanmotion[98] = 0.004875185;

        /*  Asteroid 77  */
        /*
        77a MU 71.0 23 11 2 0.9747E-11 6
        77b M 2.668735 0.132340 2.4299 0.9771 60.6274 3.0821 0.003944961 47800.
        77c M 2.668735 0.132405 2.4336 1.1581 60.3172 224.2986 0.003944961 44000.
        */
        mu_planet[109] = (4 * Math.PI / 3) * Math.pow((69.25 / 2), 3) * 5.3E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[99] = 2.668735;
        asteroid_e1[99] = 0.132340;
        asteroid_e2[99] = 0.132405;
        asteroid_i1[99] = 2.4299;
        asteroid_i2[99] = 2.4336;
        asteroid_omega1[99] = 0.9771;
        asteroid_omega2[99] = 1.1581;
        asteroid_w1[99] = 60.6274;
        asteroid_w2[99] = 60.3172;
        asteroid_mean1[99] = 3.0821;
        asteroid_mean2[99] = 224.2986;
        asteroid_meanmotion[99] = 0.003944961;

        /*  Asteroid 694  */
        /*
        694a CP 92.7 23 11 2 0.2165E-10 11
        694b C 2.670891 0.323199 15.8384 230.1171 110.5136 148.4939 0.003940309 47800.
        694c C 2.670891 0.323307 15.8264 230.3428 110.2415 10.6405 0.003940309 44000.
        */
        mu_planet[110] = (4 * Math.PI / 3) * Math.pow((90.78 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[100] = 2.670891;
        asteroid_e1[100] = 0.323199;
        asteroid_e2[100] = 0.323307;
        asteroid_i1[100] = 15.8384;
        asteroid_i2[100] = 15.8264;
        asteroid_omega1[100] = 230.1171;
        asteroid_omega2[100] = 230.3428;
        asteroid_w1[100] = 110.5136;
        asteroid_w2[100] = 110.2415;
        asteroid_mean1[100] = 148.4939;
        asteroid_mean2[100] = 10.6405;
        asteroid_meanmotion[100] = 0.003940309;

        /*  Asteroid 145  */
        /*
        145a C 155.0 23 11 2 0.5999E-10 8
        145b C 2.672747 0.145348 12.6356 77.0950 44.0851 183.0749 0.003936128 47800.
        145c C 2.672747 0.145347 12.6401 77.2380 43.8083 46.2194 0.003936128 44000.
        */
        mu_planet[111] = (4 * Math.PI / 3) * Math.pow((151.14 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[101] = 2.672747;
        asteroid_e1[101] = 0.145348;
        asteroid_e2[101] = 0.145347;
        asteroid_i1[101] = 12.6356;
        asteroid_i2[101] = 12.6401;
        asteroid_omega1[101] = 77.0950;
        asteroid_omega2[101] = 77.2380;
        asteroid_w1[101] = 44.0851;
        asteroid_w2[101] = 43.8083;
        asteroid_mean1[101] = 183.0749;
        asteroid_mean2[101] = 46.2194;
        asteroid_meanmotion[101] = 0.003936128;

        /*  Asteroid 75  */
        /*
        75a M 58.3 23 11 2 -0.1131E-09 11
        75b M 2.672213 0.305856 4.9999 359.0906 338.5962 45.5875 0.003937128 47800.
        75c M 2.672213 0.305812 5.0012 359.2540 338.2884 268.5247 0.003937128 44000.
        */
        mu_planet[112] = (4 * Math.PI / 3) * Math.pow((55.66 / 2), 3) * 5.3E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[102] = 2.672213;
        asteroid_e1[102] = 0.305856;
        asteroid_e2[102] = 0.305812;
        asteroid_i1[102] = 4.9999;
        asteroid_i2[102] = 5.0012;
        asteroid_omega1[102] = 359.0906;
        asteroid_omega2[102] = 359.2540;
        asteroid_w1[102] = 338.5962;
        asteroid_w2[102] = 338.2884;
        asteroid_mean1[102] = 45.5875;
        asteroid_mean2[102] = 268.5247;
        asteroid_meanmotion[102] = 0.003937128;

        /*  Asteroid 225  */
        /*
        225a F 124.0 25 12 5 0.3377E-08 11 amp 0.21 radians
        225b C 3.375859 0.274177 20.8205 197.7361 101.7818 37.0415 0.002772656 47800.
        225c C 3.375859 0.274317 20.8066 198.1288 101.3894 153.3675 0.002772656 44000.
        */
        mu_planet[113] = (4 * Math.PI / 3) * Math.pow((120.49 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[103] = 3.375859;
        asteroid_e1[103] = 0.274177;
        asteroid_e2[103] = 0.274317;
        asteroid_i1[103] = 20.8205;
        asteroid_i2[103] = 20.8066;
        asteroid_omega1[103] = 197.7361;
        asteroid_omega2[103] = 198.1288;
        asteroid_w1[103] = 101.7818;
        asteroid_w2[103] = 101.3894;
        asteroid_mean1[103] = 37.0415;
        asteroid_mean2[103] = 153.3675;
        asteroid_meanmotion[103] = 0.002772656;

        /*  Asteroid 201  */
        /*
        201a M 70.5 23 11 2 -0.4802E-10 8
        201b M 2.678250 0.180758 5.7577 156.6426 180.3430 36.1628 0.003923934 47800.
        201c M 2.678250 0.180715 5.7540 156.7709 180.0793 261.9638 0.003923934 44000.
        */
        mu_planet[114] = (4 * Math.PI / 3) * Math.pow((68.39 / 2), 3) * 5.3E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[104] = 2.678250;
        asteroid_e1[104] = 0.180758;
        asteroid_e2[104] = 0.180715;
        asteroid_i1[104] = 5.7577;
        asteroid_i2[104] = 5.7540;
        asteroid_omega1[104] = 156.6426;
        asteroid_omega2[104] = 156.7709;
        asteroid_w1[104] = 180.3430;
        asteroid_w2[104] = 180.0793;
        asteroid_mean1[104] = 36.1628;
        asteroid_mean2[104] = 261.9638;
        asteroid_meanmotion[104] = 0.003923934;

        /*  Asteroid 324  */
        /*
        324a C 228.0 23 11 2 0.2198E-10 11
        324b C 2.683458 0.338440 11.1314 327.7251 43.5152 189.3530 0.003912488 47800.
        324c C 2.683458 0.338280 11.1503 327.9469 43.1859 57.6182 0.003912488 44000.
        */
        mu_planet[115] = (4 * Math.PI / 3) * Math.pow((229.43 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[105] = 2.683458;
        asteroid_e1[105] = 0.338440;
        asteroid_e2[105] = 0.338280;
        asteroid_i1[105] = 11.1314;
        asteroid_i2[105] = 11.1503;
        asteroid_omega1[105] = 327.7251;
        asteroid_omega2[105] = 327.9469;
        asteroid_w1[105] = 43.5152;
        asteroid_w2[105] = 43.1859;
        asteroid_mean1[105] = 189.3530;
        asteroid_mean2[105] = 57.6182;
        asteroid_meanmotion[105] = 0.003912488;

        /*  Asteroid 34  */
        /*
        34a C 118.0 23 11 2 -0.5323E-10 8
        34b C 2.686809 0.106829 5.4927 184.0955 329.1368 231.4040 0.003905198 47800.
        34c C 2.686809 0.106901 5.4880 184.2495 328.7895 101.3419 0.003905198 44000.
        */
        mu_planet[116] = (4 * Math.PI / 3) * Math.pow((113.54 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[106] = 2.686809;
        asteroid_e1[106] = 0.106829;
        asteroid_e2[106] = 0.106901;
        asteroid_i1[106] = 5.4927;
        asteroid_i2[106] = 5.4880;
        asteroid_omega1[106] = 184.0955;
        asteroid_omega2[106] = 184.2495;
        asteroid_w1[106] = 329.1368;
        asteroid_w2[106] = 328.7895;
        asteroid_mean1[106] = 231.4040;
        asteroid_mean2[106] = 101.3419;
        asteroid_meanmotion[106] = 0.003905198;

        /*  Asteroid 98  */
        /*
        98a C 109.0 23 11 2 -0.1241E-09 11
        98b C 2.686903 0.188139 15.5805 353.6606 157.5166 230.4771 0.003905057 47800.
        98c C 2.686903 0.188278 15.5800 353.8037 157.2022 100.4241 0.003905057 44000.
        */
        mu_planet[117] = (4 * Math.PI / 3) * Math.pow((104.45 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[107] = 2.686903;
        asteroid_e1[107] = 0.188139;
        asteroid_e2[107] = 0.188278;
        asteroid_i1[107] = 15.5805;
        asteroid_i2[107] = 15.5800;
        asteroid_omega1[107] = 353.6606;
        asteroid_omega2[107] = 353.8037;
        asteroid_w1[107] = 157.5166;
        asteroid_w2[107] = 157.2022;
        asteroid_mean1[107] = 230.4771;
        asteroid_mean2[107] = 100.4241;
        asteroid_meanmotion[107] = 0.003905057;

        /*  Asteroid 31  */
        /*
        31a C 248.0 25 12 3 -0.1523E-09 8
        31b C 3.155017 0.221400 26.3051 30.5744 63.1016 340.9340 0.003069121 47800.
        31c C 3.155017 0.220994 26.3199 30.7587 62.9170 32.7131 0.003069121 44000.
        */
        mu_planet[118] = (4 * Math.PI / 3) * Math.pow((255.90 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[108] = 3.155017;
        asteroid_e1[108] = 0.221400;
        asteroid_e2[108] = 0.220994;
        asteroid_i1[108] = 26.3051;
        asteroid_i2[108] = 26.3199;
        asteroid_omega1[108] = 30.5744;
        asteroid_omega2[108] = 30.7587;
        asteroid_w1[108] = 63.1016;
        asteroid_w2[108] = 62.9170;
        asteroid_mean1[108] = 340.9340;
        asteroid_mean2[108] = 32.7131;
        asteroid_meanmotion[108] = 0.003069121;

        /*  Asteroid 175  */
        /*
        175a C 107.0 25 12 5 -0.3750E-08 11 amp 0.49 radians
        175b C 3.211836 0.210848 3.1981 21.9847 317.2893 159.8714 0.002987173 47800.
        175c C 3.211836 0.210642 3.2001 22.3052 316.1708 230.2903 0.002987173 44000.
        */
        mu_planet[119] = (4 * Math.PI / 3) * Math.pow((100.92 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[109] = 3.211836;
        asteroid_e1[109] = 0.210848;
        asteroid_e2[109] = 0.210642;
        asteroid_i1[109] = 3.1981;
        asteroid_i2[109] = 3.2001;
        asteroid_omega1[109] = 21.9847;
        asteroid_omega2[109] = 22.3052;
        asteroid_w1[109] = 317.2893;
        asteroid_w2[109] = 316.1708;
        asteroid_mean1[109] = 159.8714;
        asteroid_mean2[109] = 230.2903;
        asteroid_meanmotion[109] = 0.002987173;

        /*  Asteroid 109  */
        /*
        109a C 91.6 23 11 3 0.1267E-11 8
        109b C 2.696231 0.297703 7.8957 2.9719 55.9104 10.2299 0.003884672 47800.
        109c C 2.696231 0.297711 7.9078 3.2207 55.5381 244.5674 0.003884672 44000.
        */
        mu_planet[120] = (4 * Math.PI / 3) * Math.pow((89.44 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[110] = 2.696231;
        asteroid_e1[110] = 0.297703;
        asteroid_e2[110] = 0.297711;
        asteroid_i1[110] = 7.8957;
        asteroid_i2[110] = 7.9078;
        asteroid_omega1[110] = 2.9719;
        asteroid_omega2[110] = 3.2207;
        asteroid_w1[110] = 55.9104;
        asteroid_w2[110] = 55.5381;
        asteroid_mean1[110] = 10.2299;
        asteroid_mean2[110] = 244.5674;
        asteroid_meanmotion[110] = 0.003884672;

        /*  Asteroid 58  */
        /*
        58a C 97.7 23 11 2 0.2937E-10 8
        58b C 2.700003 0.044176 5.0622 160.8233 31.3413 58.7200 0.003876616 47800.
        58c C 2.700003 0.044176 5.0594 160.9580 30.9270 294.9674 0.003876616 44000.
        */
        mu_planet[121] = (4 * Math.PI / 3) * Math.pow((93.43 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[111] = 2.700003;
        asteroid_e1[111] = 0.044176;
        asteroid_e2[111] = 0.044176;
        asteroid_i1[111] = 5.0622;
        asteroid_i2[111] = 5.0594;
        asteroid_omega1[111] = 160.8233;
        asteroid_omega2[111] = 160.9580;
        asteroid_w1[111] = 31.3413;
        asteroid_w2[111] = 30.9270;
        asteroid_mean1[111] = 58.7200;
        asteroid_mean2[111] = 294.9674;
        asteroid_meanmotion[111] = 0.003876616;

        /*  Asteroid 103  */
        /*
        103a S 95.2 23 11 2 -0.7887E-11 8
        103b S 2.702109 0.080423 5.4216 135.8026 188.6147 118.4913 0.003872091 47800.
        103c S 2.702109 0.080358 5.4194 135.9241 188.3713 355.5663 0.003872091 44000.
        */
        mu_planet[122] = (4 * Math.PI / 3) * Math.pow((91.2 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[112] = 2.702109;
        asteroid_e1[112] = 0.080423;
        asteroid_e2[112] = 0.080358;
        asteroid_i1[112] = 5.4216;
        asteroid_i2[112] = 5.4194;
        asteroid_omega1[112] = 135.8026;
        asteroid_omega2[112] = 135.9241;
        asteroid_w1[112] = 188.6147;
        asteroid_w2[112] = 188.3713;
        asteroid_mean1[112] = 118.4913;
        asteroid_mean2[112] = 355.5663;
        asteroid_meanmotion[112] = 0.003872091;

        /*  Asteroid 59  */
        /*
        59a C 173.0 23 11 2 -0.2485E-11 8
        59b C 2.713407 0.118089 8.6430 169.7513 210.5244 297.9861 0.003847945 47800.
        59c C 2.713407 0.118081 8.6406 169.8930 210.2648 180.3139 0.003847945 44000.
        */
        mu_planet[123] = (4 * Math.PI / 3) * Math.pow((164.80 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[113] = 2.713407;
        asteroid_e1[113] = 0.118089;
        asteroid_e2[113] = 0.118081;
        asteroid_i1[113] = 8.6430;
        asteroid_i2[113] = 8.6406;
        asteroid_omega1[113] = 169.7513;
        asteroid_omega2[113] = 169.8930;
        asteroid_w1[113] = 210.5244;
        asteroid_w2[113] = 210.2648;
        asteroid_mean1[113] = 297.9861;
        asteroid_mean2[113] = 180.3139;
        asteroid_meanmotion[113] = 0.003847945;

        /*  Asteroid 54  */
        /*
        54a C 171.0 23 11 3 0.7324E-11 10
        54b C 2.710603 0.198281 11.8115 313.0017 344.8635 154.2294 0.003853912 47800.
        54c C 2.710603 0.198270 11.8098 313.1619 344.5494 35.2944 0.003853912 44000.
        */
        mu_planet[124] = (4 * Math.PI / 3) * Math.pow((165.75 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[114] = 2.710603;
        asteroid_e1[114] = 0.198281;
        asteroid_e2[114] = 0.198270;
        asteroid_i1[114] = 11.8115;
        asteroid_i2[114] = 11.8098;
        asteroid_omega1[114] = 313.0017;
        asteroid_omega2[114] = 313.1619;
        asteroid_w1[114] = 344.8635;
        asteroid_w2[114] = 344.5494;
        asteroid_mean1[114] = 154.2294;
        asteroid_mean2[114] = 35.2944;
        asteroid_meanmotion[114] = 0.003853912;

        /*  Asteroid 146  */
        /*
        146a C 137.0 21 10 2 -0.1168E-10 8
        146b C 2.718763 0.065490 13.0869 83.7152 144.5992 205.9312 0.003836609 47800.
        146c C 2.718763 0.065491 13.0869 83.8500 144.2687 90.8051 0.003836609 44000.
        */
        mu_planet[125] = (4 * Math.PI / 3) * Math.pow((132.20 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[115] = 2.718763;
        asteroid_e1[115] = 0.065490;
        asteroid_e2[115] = 0.065491;
        asteroid_i1[115] = 13.0869;
        asteroid_i2[115] = 13.0869;
        asteroid_omega1[115] = 83.7152;
        asteroid_omega2[115] = 83.8500;
        asteroid_w1[115] = 144.5992;
        asteroid_w2[115] = 144.2687;
        asteroid_mean1[115] = 205.9312;
        asteroid_mean2[115] = 90.8051;
        asteroid_meanmotion[115] = 0.003836609;

        /*  Asteroid 45  */
        /*
        45a C 214.0 21 10 2 -0.1135E-10 8
        45b C 2.720764 0.082343 6.6082 147.4348 86.3388 188.2756 0.003832304 47800.
        45c C 2.720764 0.082279 6.6059 147.5822 85.9915 74.0912 0.003832304 44000.
        */
        mu_planet[126] = (4 * Math.PI / 3) * Math.pow((214.63 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[116] = 2.720764;
        asteroid_e1[116] = 0.082343;
        asteroid_e2[116] = 0.082279;
        asteroid_i1[116] = 6.6082;
        asteroid_i2[116] = 6.6059;
        asteroid_omega1[116] = 147.4348;
        asteroid_omega2[116] = 147.5822;
        asteroid_w1[116] = 86.3388;
        asteroid_w2[116] = 85.9915;
        asteroid_mean1[116] = 188.2756;
        asteroid_mean2[116] = 74.0912;
        asteroid_meanmotion[116] = 0.003832304;

        /*  Asteroid 209  */
        /*
        209a C  149.0 25 12 3 -0.7546E-10 9
        209b C 3.147524 0.058344 7.1813 0.4346 256.9117 11.6991 0.003079583 47800.
        209c C 3.147524 0.058177 7.1876 0.6863 256.2702 61.5900 0.003079583 44000.
        */
        mu_planet[127] = (4 * Math.PI / 3) * Math.pow((159.94 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[117] = 3.147524;
        asteroid_e1[117] = 0.058344;
        asteroid_e2[117] = 0.058177;
        asteroid_i1[117] = 7.1813;
        asteroid_i2[117] = 7.1876;
        asteroid_omega1[117] = 0.4346;
        asteroid_omega2[117] = 0.6863;
        asteroid_w1[117] = 256.9117;
        asteroid_w2[117] = 256.2702;
        asteroid_mean1[117] = 11.6991;
        asteroid_mean2[117] = 61.5900;
        asteroid_meanmotion[117] = 0.003079583;

        /*  Asteroid 410  */
        /*
        410a C 128.0 25 12 3 0.7509E-12 11
        410b C 2.726435 0.239104 10.9408 96.7242 171.8349 142.1510 0.003820258 47800.
        410c C 2.726435 0.239050 10.9373 96.8657 171.5146 30.5680 0.003820258 44000.
        */
        mu_planet[128] = (4 * Math.PI / 3) * Math.pow((123.51 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[118] = 2.726435;
        asteroid_e1[118] = 0.239104;
        asteroid_e2[118] = 0.239050;
        asteroid_i1[118] = 10.9408;
        asteroid_i2[118] = 10.9373;
        asteroid_omega1[118] = 96.7242;
        asteroid_omega2[118] = 96.8657;
        asteroid_w1[118] = 171.8349;
        asteroid_w2[118] = 171.5146;
        asteroid_mean1[118] = 142.1510;
        asteroid_mean2[118] = 30.5680;
        asteroid_meanmotion[118] = 0.003820258;

        /*  Asteroid 160  */
        /*
        160a CX 85.0 21 10 2 0.5971E-10 6
        160b C 2.727640 0.066241 3.8255 8.4094 50.6229 162.6641 0.003817829 47800.
        160c C 2.727640 0.066303 3.8292 8.5702 50.3559 51.5375 0.003817829 44000.
        */
        mu_planet[129] = (4 * Math.PI / 3) * Math.pow((81.24 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[119] = 2.727640;
        asteroid_e1[119] = 0.066241;
        asteroid_e2[119] = 0.066303;
        asteroid_i1[119] = 3.8255;
        asteroid_i2[119] = 3.8292;
        asteroid_omega1[119] = 8.4094;
        asteroid_omega2[119] = 8.5702;
        asteroid_w1[119] = 50.6229;
        asteroid_w2[119] = 50.3559;
        asteroid_mean1[119] = 162.6641;
        asteroid_mean2[119] = 51.5375;
        asteroid_meanmotion[119] = 0.003817829;

        /*  Asteroid 140  */
        /*
        140a P  114.0 21 10 2 0.7963E-11 8
        140b C 2.732353 0.216152 3.1903 106.8696 195.9980 222.7782 0.003807843 47800.
        140c C 2.732353 0.216058 3.1901 106.9659 195.7385 113.8829 0.003807843 44000.
        */
        mu_planet[130] = (4 * Math.PI / 3) * Math.pow((109.79 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[120] = 2.732353;
        asteroid_e1[120] = 0.216152;
        asteroid_e2[120] = 0.216058;
        asteroid_i1[120] = 3.1903;
        asteroid_i2[120] = 3.1901;
        asteroid_omega1[120] = 106.8696;
        asteroid_omega2[120] = 106.9659;
        asteroid_w1[120] = 195.9980;
        asteroid_w2[120] = 195.7385;
        asteroid_mean1[120] = 222.7782;
        asteroid_mean2[120] = 113.8829;
        asteroid_meanmotion[120] = 0.003807843;

        /*  Asteroid 156  */
        /*
        156a C 126.0 25 12 2 0.4021E-09 11
        156b C 2.730398 0.224604 9.7394 241.6918 337.3871 282.8325 0.003811957 47800.
        156c C 2.730398 0.224671 9.7288 241.8867 337.0060 173.0643 0.003811957 44000.
        */
        mu_planet[131] = (4 * Math.PI / 3) * Math.pow((120.99 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[121] = 2.730398;
        asteroid_e1[121] = 0.224604;
        asteroid_e2[121] = 0.224671;
        asteroid_i1[121] = 9.7394;
        asteroid_i2[121] = 9.7288;
        asteroid_omega1[121] = 241.6918;
        asteroid_omega2[121] = 241.8867;
        asteroid_w1[121] = 337.3871;
        asteroid_w2[121] = 337.0060;
        asteroid_mean1[121] = 282.8325;
        asteroid_mean2[121] = 173.0643;
        asteroid_meanmotion[121] = 0.003811957;

        /*  Asteroid 110  */
        /*
        110a M 89.1 21 10 2 -0.7923E-10 6
        110b M 2.732891 0.079481 5.9701 56.5853 282.1435 11.7803 0.003806830 47800.
        110c M 2.732891 0.079434 5.9725 56.7205 281.9029 263.0475 0.003806830 44000.
        */
        mu_planet[132] = (4 * Math.PI / 3) * Math.pow((86.09 / 2), 3) * 5.3E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[122] = 2.732891;
        asteroid_e1[122] = 0.079481;
        asteroid_e2[122] = 0.079434;
        asteroid_i1[122] = 5.9701;
        asteroid_i2[122] = 5.9725;
        asteroid_omega1[122] = 56.5853;
        asteroid_omega2[122] = 56.7205;
        asteroid_w1[122] = 282.1435;
        asteroid_w2[122] = 281.9029;
        asteroid_mean1[122] = 11.7803;
        asteroid_mean2[122] = 263.0475;
        asteroid_meanmotion[122] = 0.003806830;

        /*  Asteroid 187  */
        /*
        187a C 135.0 25 12 3 0.1185E-10 11
        187b C 2.730538 0.238175 10.6047 21.4790 195.3490 239.3163 0.003811626 47800.
        187c C 2.730538 0.238053 10.6156 21.6438 194.9892 129.6291 0.003811626 44000.
        */
        mu_planet[133] = (4 * Math.PI / 3) * Math.pow((131.25 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[123] = 2.730538;
        asteroid_e1[123] = 0.238175;
        asteroid_e2[123] = 0.238053;
        asteroid_i1[123] = 10.6047;
        asteroid_i2[123] = 10.6156;
        asteroid_omega1[123] = 21.4790;
        asteroid_omega2[123] = 21.6438;
        asteroid_w1[123] = 195.3490;
        asteroid_w2[123] = 194.9892;
        asteroid_mean1[123] = 239.3163;
        asteroid_mean2[123] = 129.6291;
        asteroid_meanmotion[123] = 0.003811626;

        /*  Asteroid 18  */
        /*
        18a S 148.0 16 7 2 -0.5826E-12 7
        18b S 2.295629 0.218121 10.1365 150.0206 227.5080 87.3051 0.004945191 47800.
        18c S 2.295629 0.218089 10.1371 150.1206 227.3289 90.6976 0.004945191 44000.
        */
        mu_planet[134] = (4 * Math.PI / 3) * Math.pow((140.57 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[124] = 2.295629;
        asteroid_e1[124] = 0.218121;
        asteroid_e2[124] = 0.218089;
        asteroid_i1[124] = 10.1365;
        asteroid_i2[124] = 10.1371;
        asteroid_omega1[124] = 150.0206;
        asteroid_omega2[124] = 150.1206;
        asteroid_w1[124] = 227.5080;
        asteroid_w2[124] = 227.3289;
        asteroid_mean1[124] = 87.3051;
        asteroid_mean2[124] = 90.6976;
        asteroid_meanmotion[124] = 0.004945191;

        /*  Asteroid 387  */
        /*
        387a S 106.0 21 10 2 -0.6363E-10 10
        387b S 2.739105 0.237258 18.0944 127.9195 156.5153 274.0635 0.003793927 47800.
        387c S 2.739105 0.237398 18.0828 128.0628 156.2326 168.1742 0.003793927 44000.
        */
        mu_planet[135] = (4 * Math.PI / 3) * Math.pow((100.51 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[125] = 2.739105;
        asteroid_e1[125] = 0.237258;
        asteroid_e2[125] = 0.237398;
        asteroid_i1[125] = 18.0944;
        asteroid_i2[125] = 18.0828;
        asteroid_omega1[125] = 127.9195;
        asteroid_omega2[125] = 128.0628;
        asteroid_w1[125] = 156.5153;
        asteroid_w2[125] = 156.2326;
        asteroid_mean1[125] = 274.0635;
        asteroid_mean2[125] = 168.1742;
        asteroid_meanmotion[125] = 0.003793927;

        /*  Asteroid 200  */
        /*
        200a C 132.0 21 10 2 0.3330E-10 6
        200b C 2.737536 0.133426 6.9006 324.2733 85.1950 37.7548 0.003797153 47800.
        200c C 2.737536 0.133480 6.9030 324.4677 84.8885 291.1359 0.003797153 44000.
        */
        mu_planet[136] = (4 * Math.PI / 3) * Math.pow((128.36 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[126] = 2.737536;
        asteroid_e1[126] = 0.133426;
        asteroid_e2[126] = 0.133480;
        asteroid_i1[126] = 6.9006;
        asteroid_i2[126] = 6.9030;
        asteroid_omega1[126] = 324.2733;
        asteroid_omega2[126] = 324.4677;
        asteroid_w1[126] = 85.1950;
        asteroid_w2[126] = 84.8885;
        asteroid_mean1[126] = 37.7548;
        asteroid_mean2[126] = 291.1359;
        asteroid_meanmotion[126] = 0.003797153;

        /*  Asteroid 185  */
        /*
        185a C 165.0 21 10 2 0.2001E-10 5
        185b C 2.738596 0.127946 23.2475 153.4771 223.5609 356.5558 0.003795170 47800.
        185c C 2.738596 0.127795 23.2476 153.5951 223.3734 250.3260 0.003795170 44000.
        */
        mu_planet[137] = (4 * Math.PI / 3) * Math.pow((157.51 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[127] = 2.738596;
        asteroid_e1[127] = 0.127946;
        asteroid_e2[127] = 0.127795;
        asteroid_i1[127] = 23.2475;
        asteroid_i2[127] = 23.2476;
        asteroid_omega1[127] = 153.4771;
        asteroid_omega2[127] = 153.5951;
        asteroid_w1[127] = 223.5609;
        asteroid_w2[127] = 223.3734;
        asteroid_mean1[127] = 356.5558;
        asteroid_mean2[127] = 250.3260;
        asteroid_meanmotion[127] = 0.003795170;

        /*  Asteroid 57  */
        /*
        57a S 116.0 25 12 3 0.1529E-09 8
        57b S 3.154033 0.110621 15.1981 198.8084 214.6433 35.7243 0.003070223 47800.
        57c S 3.154033 0.110544 15.1967 199.0134 214.1904 87.5111 0.003070223 44000.
        */
        mu_planet[138] = (4 * Math.PI / 3) * Math.pow((112.59 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[128] = 3.154033;
        asteroid_e1[128] = 0.110621;
        asteroid_e2[128] = 0.110544;
        asteroid_i1[128] = 15.1981;
        asteroid_i2[128] = 15.1967;
        asteroid_omega1[128] = 198.8084;
        asteroid_omega2[128] = 199.0134;
        asteroid_w1[128] = 214.6433;
        asteroid_w2[128] = 214.1904;
        asteroid_mean1[128] = 35.7243;
        asteroid_mean2[128] = 87.5111;
        asteroid_meanmotion[128] = 0.003070223;

        /*  Asteroid 247  */
        /*
        247a CP 137.0 21 10 2 -0.3800E-10 8
        247b C 2.740978 0.243561 25.0010 359.7949 54.7276 107.7274 0.003790287 47800.
        247c C 2.740978 0.243252 25.0135 359.9325 54.5814 2.4995 0.003790287 44000.
        */
        mu_planet[139] = (4 * Math.PI / 3) * Math.pow((134.43 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[129] = 2.740978;
        asteroid_e1[129] = 0.243561;
        asteroid_e2[129] = 0.243252;
        asteroid_i1[129] = 25.0010;
        asteroid_i2[129] = 25.0135;
        asteroid_omega1[129] = 359.7949;
        asteroid_omega2[129] = 359.9325;
        asteroid_w1[129] = 54.7276;
        asteroid_w2[129] = 54.5814;
        asteroid_mean1[129] = 107.7274;
        asteroid_mean2[129] = 2.4995;
        asteroid_meanmotion[129] = 0.003790287;

        /*  Asteroid 372  */
        /*
        372a C 195.0 25 12 3 0.6067E-09 12 amp 0.11 radians
        372b C 3.153684 0.256962 23.8300 326.8914 116.5682 343.3084 0.003070989 47800.
        372c C 3.153684 0.257621 23.8122 327.1119 116.3036 34.7245 0.003070989 44000.
        */
        mu_planet[140] = (4 * Math.PI / 3) * Math.pow((188.62 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[130] = 3.153684;
        asteroid_e1[130] = 0.256962;
        asteroid_e2[130] = 0.257621;
        asteroid_i1[130] = 23.8300;
        asteroid_i2[130] = 23.8122;
        asteroid_omega1[130] = 326.8914;
        asteroid_omega2[130] = 327.1119;
        asteroid_w1[130] = 116.5682;
        asteroid_w2[130] = 116.3036;
        asteroid_mean1[130] = 343.3084;
        asteroid_mean2[130] = 34.7245;
        asteroid_meanmotion[130] = 0.003070989;

        /*  Asteroid 980  */
        /*
        980a S 89.0 21 10 2 -0.1864E-10 8
        980b S 2.741106 0.201286 15.9039 285.5627 69.4796 349.8664 0.003789865 47800.
        980c S 2.741106 0.201155 15.9085 285.7372 69.2534 244.7737 0.003789865 44000.
        */
        mu_planet[141] = (4 * Math.PI / 3) * Math.pow((86.19 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[131] = 2.741106;
        asteroid_e1[131] = 0.201286;
        asteroid_e2[131] = 0.201155;
        asteroid_i1[131] = 15.9039;
        asteroid_i2[131] = 15.9085;
        asteroid_omega1[131] = 285.5627;
        asteroid_omega2[131] = 285.7372;
        asteroid_w1[131] = 69.4796;
        asteroid_w2[131] = 69.2534;
        asteroid_mean1[131] = 349.8664;
        asteroid_mean2[131] = 244.7737;
        asteroid_meanmotion[131] = 0.003789865;

        /*  Asteroid 481  */
        /*
        481a C 116.0 21 10 2 -0.4172E-10 8
        481b C 2.740519 0.157034 9.8504 66.5290 348.5576 173.4509 0.003790939 47800.
        481c C 2.740519 0.157095 9.8522 66.6635 348.2796 68.2162 0.003790939 44000.
        */
        mu_planet[142] = (4 * Math.PI / 3) * Math.pow((113.23 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[132] = 2.740519;
        asteroid_e1[132] = 0.157034;
        asteroid_e2[132] = 0.157095;
        asteroid_i1[132] = 9.8504;
        asteroid_i2[132] = 9.8522;
        asteroid_omega1[132] = 66.5290;
        asteroid_omega2[132] = 66.6635;
        asteroid_w1[132] = 348.5576;
        asteroid_w2[132] = 348.2796;
        asteroid_mean1[132] = 173.4509;
        asteroid_mean2[132] = 68.2162;
        asteroid_meanmotion[132] = 0.003790939;

        /*  Asteroid 521  */
        /*

        521a C 121.0 21 10 2 0.1654E-09 10
        521b C 2.741823 0.280919 10.5643 89.4887 315.1147 341.8269 0.003788150 47800.
        521c C 2.741823 0.281055 10.5562 89.6643 314.8058 237.1893 0.003788150 44000.
        */
        mu_planet[143] = (4 * Math.PI / 3) * Math.pow((115.65 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[133] = 2.741823;
        asteroid_e1[133] = 0.280919;
        asteroid_e2[133] = 0.281055;
        asteroid_i1[133] = 10.5643;
        asteroid_i2[133] = 10.5562;
        asteroid_omega1[133] = 89.4887;
        asteroid_omega2[133] = 89.6643;
        asteroid_w1[133] = 315.1147;
        asteroid_w2[133] = 314.8058;
        asteroid_mean1[133] = 341.8269;
        asteroid_mean2[133] = 237.1893;
        asteroid_meanmotion[133] = 0.003788150;

        /*  Asteroid 206  */
        /*
        206a C 113.0 21 10 2 0.1644E-10 6
        206b C 2.740708 0.039466 3.7792 144.8114 300.5244 19.4246 0.003790537 47800.
        206c C 2.740708 0.039555 3.7766 144.9322 300.2783 274.2594 0.003790537 44000.
        */
        mu_planet[144] = (4 * Math.PI / 3) * Math.pow((113.0 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[134] = 2.740708;
        asteroid_e1[134] = 0.039466;
        asteroid_e2[134] = 0.039555;
        asteroid_i1[134] = 3.7792;
        asteroid_i2[134] = 3.7766;
        asteroid_omega1[134] = 144.8114;
        asteroid_omega2[134] = 144.9322;
        asteroid_w1[134] = 300.5244;
        asteroid_w2[134] = 300.2783;
        asteroid_mean1[134] = 19.4246;
        asteroid_mean2[134] = 274.2594;
        asteroid_meanmotion[134] = 0.003790537;

        /*  Asteroid 38  */
        /*
        38a C 120.0 21 10 2 0.6714E-10 8
        38b C 2.740878 0.153316 6.9601 295.4464 168.5022 179.5650 0.003790167 47800.
        38c C 2.740878 0.153412 6.9610 295.6300 168.1468 74.5267 0.003790167 44000.
        */
        mu_planet[145] = (4 * Math.PI / 3) * Math.pow((115.93 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[135] = 2.740878;
        asteroid_e1[135] = 0.153316;
        asteroid_e2[135] = 0.153412;
        asteroid_i1[135] = 6.9601;
        asteroid_i2[135] = 6.9610;
        asteroid_omega1[135] = 295.4464;
        asteroid_omega2[135] = 295.6300;
        asteroid_w1[135] = 168.5022;
        asteroid_w2[135] = 168.1468;
        asteroid_mean1[135] = 179.5650;
        asteroid_mean2[135] = 74.5267;
        asteroid_meanmotion[135] = 0.003790167;

        /*  Asteroid 173  */
        /*
        173a C 159.0 21 10 2 -0.4672E-10 8
        173b C 2.743129 0.207643 14.2161 147.9476 227.6639 212.6950 0.003785576 47800.
        173c C 2.743129 0.207525 14.2191 148.1017 227.4011 108.5932 0.003785576 44000.
        */
        mu_planet[146] = (4 * Math.PI / 3) * Math.pow((154.10 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[136] = 2.743129;
        asteroid_e1[136] = 0.207643;
        asteroid_e2[136] = 0.207525;
        asteroid_i1[136] = 14.2161;
        asteroid_i2[136] = 14.2191;
        asteroid_omega1[136] = 147.9476;
        asteroid_omega2[136] = 148.1017;
        asteroid_w1[136] = 227.6639;
        asteroid_w2[136] = 227.4011;
        asteroid_mean1[136] = 212.6950;
        asteroid_mean2[136] = 108.5932;
        asteroid_meanmotion[136] = 0.003785576;

        /*  Asteroid 36  */
        /*
        36a C 109.0 21 10 2 -0.6933E-10 8
        36b C 2.747348 0.303291 18.4525 358.1286 46.6827 123.7804 0.003776934 47800.
        36c C 2.747348 0.303007 18.4730 358.3106 46.4335 21.5186 0.003776934 44000.
        */
        mu_planet[147] = (4 * Math.PI / 3) * Math.pow((105.61 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[137] = 2.747348;
        asteroid_e1[137] = 0.303291;
        asteroid_e2[137] = 0.303007;
        asteroid_i1[137] = 18.4525;
        asteroid_i2[137] = 18.4730;
        asteroid_omega1[137] = 358.1286;
        asteroid_omega2[137] = 358.3106;
        asteroid_w1[137] = 46.6827;
        asteroid_w2[137] = 46.4335;
        asteroid_mean1[137] = 123.7804;
        asteroid_mean2[137] = 21.5186;
        asteroid_meanmotion[137] = 0.003776934;

        /*  Asteroid 171  */
        /*
        171a C 121.0 25 12 3 -0.1116E-10 11
        171b C 3.136095 0.125020 2.5498 100.3829 53.2793 77.0534 0.003096263 47800.
        171c C 3.136095 0.125164 2.5506 100.5390 52.6558 123.3902 0.003096263 44000.
        */
        mu_planet[148] = (4 * Math.PI / 3) * Math.pow((116.69 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[138] = 3.136095;
        asteroid_e1[138] = 0.125020;
        asteroid_e2[138] = 0.125164;
        asteroid_i1[138] = 2.5498;
        asteroid_i2[138] = 2.5506;
        asteroid_omega1[138] = 100.3829;
        asteroid_omega2[138] = 100.5390;
        asteroid_w1[138] = 53.2793;
        asteroid_w2[138] = 52.6558;
        asteroid_mean1[138] = 77.0534;
        asteroid_mean2[138] = 123.3902;
        asteroid_meanmotion[138] = 0.003096263;

        /*  Asteroid 490  */
        /*
        490a C 121.0 25 12 3 0.6381E-10 9
        490b C 3.174544 0.088295 9.2599 178.0058 199.9759 114.6627 0.003040378 47800.
        490c C 3.174544 0.088288 9.2555 178.2222 199.4447 173.0143 0.003040378 44000.
        */
        mu_planet[149] = (4 * Math.PI / 3) * Math.pow((155.55 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[139] = 3.174544;
        asteroid_e1[139] = 0.088295;
        asteroid_e2[139] = 0.088288;
        asteroid_i1[139] = 9.2599;
        asteroid_i2[139] = 9.2555;
        asteroid_omega1[139] = 178.0058;
        asteroid_omega2[139] = 178.2222;
        asteroid_w1[139] = 199.9759;
        asteroid_w2[139] = 199.4447;
        asteroid_mean1[139] = 114.6627;
        asteroid_mean2[139] = 173.0143;
        asteroid_meanmotion[139] = 0.003040378;

        /*  Asteroid 381  */
        /*
        381a C 124.0 25 12 5 0.4210E-09 9 amp 0.24 radians
        381b C 3.209721 0.110919 12.6192 124.5250 144.2460 104.8051 0.002990525 47800.
        381c C 3.209721 0.110882 12.6100 124.7599 143.3305 174.3768 0.002990525 44000.
        */
        mu_planet[150] = (4 * Math.PI / 3) * Math.pow((120.58 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[140] = 3.209721;
        asteroid_e1[140] = 0.110919;
        asteroid_e2[140] = 0.110882;
        asteroid_i1[140] = 12.6192;
        asteroid_i2[140] = 12.6100;
        asteroid_omega1[140] = 124.5250;
        asteroid_omega2[140] = 124.7599;
        asteroid_w1[140] = 144.2460;
        asteroid_w2[140] = 143.3305;
        asteroid_mean1[140] = 104.8051;
        asteroid_mean2[140] = 174.3768;
        asteroid_meanmotion[140] = 0.002990525;

        /*  Asteroid 128  */
        /*
        128a C 194.0 21 10 2 -0.2084E-10 6
        128b C 2.750105 0.126480 6.2502 76.0237 302.4453 259.3318 0.003771116 47800.
        128c C 2.750105 0.126498 6.2510 76.1594 302.1873 158.3918 0.003771116 44000.
        */
        mu_planet[151] = (4 * Math.PI / 3) * Math.pow((188.16 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[141] = 2.750105;
        asteroid_e1[141] = 0.126480;
        asteroid_e2[141] = 0.126498;
        asteroid_i1[141] = 6.2502;
        asteroid_i2[141] = 6.2510;
        asteroid_omega1[141] = 76.0237;
        asteroid_omega2[141] = 76.1594;
        asteroid_w1[141] = 302.4453;
        asteroid_w2[141] = 302.1873;
        asteroid_mean1[141] = 259.3318;
        asteroid_mean2[141] = 158.3918;
        asteroid_meanmotion[141] = 0.003771116;

        /*  Asteroid 93  */
        /*
        93a CU 171.0 21 10 2 0.3329E-10 8
        93b C 2.754684 0.141078 8.5564 3.8947 274.1428 309.5847 0.003761723 47800.
        93c C 2.754684 0.141009 8.5603 4.0858 273.8126 210.7066 0.003761723 44000.
        */
        mu_planet[152] = (4 * Math.PI / 3) * Math.pow((140.97 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[142] = 2.754684;
        asteroid_e1[142] = 0.141078;
        asteroid_e2[142] = 0.141009;
        asteroid_i1[142] = 8.5564;
        asteroid_i2[142] = 8.5603;
        asteroid_omega1[142] = 3.8947;
        asteroid_omega2[142] = 4.0858;
        asteroid_w1[142] = 274.1428;
        asteroid_w2[142] = 273.8126;
        asteroid_mean1[142] = 309.5847;
        asteroid_mean2[142] = 210.7066;
        asteroid_meanmotion[142] = 0.003761723;

        /*  Asteroid 71  */
        /*
        71a S 87.3 21 10 2 -0.3422E-10 8
        71b S 2.755177 0.175146 23.2653 315.7319 266.6282 96.7772 0.003761021 47800.
        71c S 2.755177 0.175107 23.2683 315.8803 266.5135 357.8791 0.003761021 44000.
        */
        mu_planet[153] = (4 * Math.PI / 3) * Math.pow((83.42 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[143] = 2.755177;
        asteroid_e1[143] = 0.175146;
        asteroid_e2[143] = 0.175107;
        asteroid_i1[143] = 23.2653;
        asteroid_i2[143] = 23.2683;
        asteroid_omega1[143] = 315.7319;
        asteroid_omega2[143] = 315.8803;
        asteroid_w1[143] = 266.6282;
        asteroid_w2[143] = 266.5135;
        asteroid_mean1[143] = 96.7772;
        asteroid_mean2[143] = 357.8791;
        asteroid_meanmotion[143] = 0.003761021;

        /*  Asteroid 356  */
        /*
        356a C 135.0 21 10 2 -0.1756E-09 8
        356b C 2.756762 0.239461 8.2265 354.6755 77.8445 79.5829 0.003757423 47800.
        356c C 2.756762 0.239530 8.2318 354.9242 77.4715 341.6262 0.003757423 44000.
        */
        mu_planet[154] = (4 * Math.PI / 3) * Math.pow((131.31 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[144] = 2.756762;
        asteroid_e1[144] = 0.239461;
        asteroid_e2[144] = 0.239530;
        asteroid_i1[144] = 8.2265;
        asteroid_i2[144] = 8.2318;
        asteroid_omega1[144] = 354.6755;
        asteroid_omega2[144] = 354.9242;
        asteroid_w1[144] = 77.8445;
        asteroid_w2[144] = 77.4715;
        asteroid_mean1[144] = 79.5829;
        asteroid_mean2[144] = 341.6262;
        asteroid_meanmotion[144] = 0.003757423;

        /*  Asteroid 127  */
        /*
        127a CX 122.0 21 10 2 0.7337E-10 7
        127b C 2.755935 0.065340 8.2442 30.9786 92.4348 118.7115 0.003759174 47800.
        127c C 2.755935 0.065434 8.2473 31.1339 92.1095 20.4192 0.003759174 44000.
        */
        mu_planet[155] = (4 * Math.PI / 3) * Math.pow((123.33 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[145] = 2.755935;
        asteroid_e1[145] = 0.065340;
        asteroid_e2[145] = 0.065434;
        asteroid_i1[145] = 8.2442;
        asteroid_i2[145] = 8.2473;
        asteroid_omega1[145] = 30.9786;
        asteroid_omega2[145] = 31.1339;
        asteroid_w1[145] = 92.4348;
        asteroid_w2[145] = 92.1095;
        asteroid_mean1[145] = 118.7115;
        asteroid_mean2[145] = 20.4192;
        asteroid_meanmotion[145] = 0.003759174;

        /*  Asteroid 41  */
        /*
        41a C 182.0 25 12 3 0.3821E-11 12
        41b C 2.764841 0.271628 15.7950 177.7809 45.6181 337.8434 0.003740971 47800.
        41c C 2.764841 0.271299 15.8092 177.9836 45.2689 243.4909 0.003740971 44000.
        */
        mu_planet[156] = (4 * Math.PI / 3) * Math.pow((174.0 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[146] = 2.764841;
        asteroid_e1[146] = 0.271628;
        asteroid_e2[146] = 0.271299;
        asteroid_i1[146] = 15.7950;
        asteroid_i2[146] = 15.8092;
        asteroid_omega1[146] = 177.7809;
        asteroid_omega2[146] = 177.9836;
        asteroid_w1[146] = 45.6181;
        asteroid_w2[146] = 45.2689;
        asteroid_mean1[146] = 337.8434;
        asteroid_mean2[146] = 243.4909;
        asteroid_meanmotion[146] = 0.003740971;

        /*  Asteroid 143  */
        /*
        143a C  92.8 21 10 2 0.4710E-10 8
        143b C 2.761482 0.072141 11.4642 332.8088 251.3433 282.5727 0.003747900 47800.
        143c C 2.761482 0.072074 11.4680 332.9789 251.0208 186.7174 0.003747900 44000.
        */
        mu_planet[157] = (4 * Math.PI / 3) * Math.pow((89.93 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[147] = 2.761482;
        asteroid_e1[147] = 0.072141;
        asteroid_e2[147] = 0.072074;
        asteroid_i1[147] = 11.4642;
        asteroid_i2[147] = 11.4680;
        asteroid_omega1[147] = 332.8088;
        asteroid_omega2[147] = 332.9789;
        asteroid_w1[147] = 251.3433;
        asteroid_w2[147] = 251.0208;
        asteroid_mean1[147] = 282.5727;
        asteroid_mean2[147] = 186.7174;
        asteroid_meanmotion[147] = 0.003747900;

        /*  Asteroid 94  */
        /*
        94a C 212.0 25 12 3 0.3520E-09 8
        94b C 3.158030 0.092380 7.9981 2.6993 52.8172 222.4032 0.003064246 47800.
        94c C 3.158030 0.092456 8.0045 2.9447 52.3127 275.5025 0.003064246 44000.
        */
        mu_planet[158] = (4 * Math.PI / 3) * Math.pow((204.89 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[148] = 3.158030;
        asteroid_e1[148] = 0.092380;
        asteroid_e2[148] = 0.092456;
        asteroid_i1[148] = 7.9981;
        asteroid_i2[148] = 8.0045;
        asteroid_omega1[148] = 2.6993;
        asteroid_omega2[148] = 2.9447;
        asteroid_w1[148] = 52.8172;
        asteroid_w2[148] = 52.3127;
        asteroid_mean1[148] = 222.4032;
        asteroid_mean2[148] = 275.5025;
        asteroid_meanmotion[148] = 0.003064246;

        /*  Asteroid 39  */
        /*
        39a S 159.0 21 10 2 -0.1542E-10 6
        39b S 2.768676 0.113202 10.3786 156.7302 208.3789 151.7931 0.003733265 47800.
        39c S 2.768676 0.113167 10.3764 156.8742 208.1116 59.0951 0.003733265 44000.
        */
        mu_planet[159] = (4 * Math.PI / 3) * Math.pow((149.52 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[149] = 2.768676;
        asteroid_e1[149] = 0.113202;
        asteroid_e2[149] = 0.113167;
        asteroid_i1[149] = 10.3786;
        asteroid_i2[149] = 10.3764;
        asteroid_omega1[149] = 156.7302;
        asteroid_omega2[149] = 156.8742;
        asteroid_w1[149] = 208.3789;
        asteroid_w2[149] = 208.1116;
        asteroid_mean1[149] = 151.7931;
        asteroid_mean2[149] = 59.0951;
        asteroid_meanmotion[149] = 0.003733265;

        /*  Asteroid 88  */
        /*
        88a C 232.0 21 10 2 0.4606E-10 8
        88b C 2.768094 0.163521 5.2232 276.3513 35.2973 258.8249 0.003734371 47800.
        88c C 2.768094 0.163415 5.2248 276.5624 34.9313 165.9176 0.003734371 44000.
        */
        mu_planet[160] = (4 * Math.PI / 3) * Math.pow((200.57 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[150] = 2.768094;
        asteroid_e1[150] = 0.163521;
        asteroid_e2[150] = 0.163415;
        asteroid_i1[150] = 5.2232;
        asteroid_i2[150] = 5.2248;
        asteroid_omega1[150] = 276.3513;
        asteroid_omega2[150] = 276.5624;
        asteroid_w1[150] = 35.2973;
        asteroid_w2[150] = 34.9313;
        asteroid_mean1[150] = 258.8249;
        asteroid_mean2[150] = 165.9176;
        asteroid_meanmotion[150] = 0.003734371;

        /*  Asteroid 433  */
        /*
        433a S 20.0 16 7 2 -0.6217E-14 3
        433b S 1.458307 0.222908 10.8264 303.7460 178.5679 122.0633 0.009767819 47800.
        433c S 1.458307 0.222916 10.8269 303.7874 178.4861 155.4154 0.009767819 44000.
        */
        mu_planet[161] = (4 * Math.PI / 3) * Math.pow((20.0 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[151] = 1.458307;
        asteroid_e1[151] = 0.222908;
        asteroid_e2[151] = 0.222916;
        asteroid_i1[151] = 10.8264;
        asteroid_i2[151] = 10.8269;
        asteroid_omega1[151] = 303.7460;
        asteroid_omega2[151] = 303.7874;
        asteroid_w1[151] = 178.5679;
        asteroid_w2[151] = 178.4861;
        asteroid_mean1[151] = 122.0633;
        asteroid_mean2[151] = 155.4154;
        asteroid_meanmotion[151] = 0.009767819;

        /*  Asteroid 8  */
        /*
        8a S 141.0 16 7 2 0.1867E-10 4
        8b S 2.201363 0.156492 5.8892 110.4812 284.9066 176.2135 0.005266283 47800.
        8c S 2.201363 0.156508 5.8885 110.5602 284.7547 109.6907 0.005266283 44000.
        */
        mu_planet[162] = (4 * Math.PI / 3) * Math.pow((135.89 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[152] = 2.201363;
        asteroid_e1[152] = 0.156492;
        asteroid_e2[152] = 0.156508;
        asteroid_i1[152] = 5.8892;
        asteroid_i2[152] = 5.8885;
        asteroid_omega1[152] = 110.4812;
        asteroid_omega2[152] = 110.5602;
        asteroid_w1[152] = 284.9066;
        asteroid_w2[152] = 284.7547;
        asteroid_mean1[152] = 176.2135;
        asteroid_mean2[152] = 109.6907;
        asteroid_meanmotion[152] = 0.005266283;

        /*  Asteroid 532  */
        /*
        532a S 225.0 21 10 2 -0.6296E-10 10
        532b S 2.771894 0.177153 16.3305 107.3814 75.4158 198.9324 0.003726812 47800.
        532c S 2.771894 0.177079 16.3336 107.5612 75.1447 107.6074 0.003726812 44000.
        */
        mu_planet[163] = (4 * Math.PI / 3) * Math.pow((222.19 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[153] = 2.771894;
        asteroid_e1[153] = 0.177153;
        asteroid_e2[153] = 0.177079;
        asteroid_i1[153] = 16.3305;
        asteroid_i2[153] = 16.3336;
        asteroid_omega1[153] = 107.3814;
        asteroid_omega2[153] = 107.5612;
        asteroid_w1[153] = 75.4158;
        asteroid_w2[153] = 75.1447;
        asteroid_mean1[153] = 198.9324;
        asteroid_mean2[153] = 107.6074;
        asteroid_meanmotion[153] = 0.003726812;

        /*  Asteroid 92  */
        /*
        92a X 132.0 25 12 5 -0.3011E-09 9 amp 0.11 radians
        92b M 3.194622 0.088469 9.9179 101.5770 234.1633 123.1092 0.003011745 47800.
        92c M 3.194622 0.088301 9.9194 101.7814 233.5626 187.7764 0.003011745 44000.
        */
        mu_planet[164] = (4 * Math.PI / 3) * Math.pow((126.42 / 2), 3) * 5.3E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[154] = 3.194622;
        asteroid_e1[154] = 0.088469;
        asteroid_e2[154] = 0.088301;
        asteroid_i1[154] = 9.9179;
        asteroid_i2[154] = 9.9194;
        asteroid_omega1[154] = 101.5770;
        asteroid_omega2[154] = 101.7814;
        asteroid_w1[154] = 234.1633;
        asteroid_w2[154] = 233.5626;
        asteroid_mean1[154] = 123.1092;
        asteroid_mean2[154] = 187.7764;
        asteroid_meanmotion[154] = 0.003011745;

        /*  Asteroid 275  */
        /*
        275a X 115.0 21 10 2 0.1273E-09 10
        275b C 2.771424 0.163045 4.7740 133.8888 37.6205 10.8161 0.003727565 47800.
        275c C 2.771424 0.163076 4.7734 134.0380 37.2515 279.4556 0.003727565 44000.
        */
        mu_planet[165] = (4 * Math.PI / 3) * Math.pow((115.0 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[155] = 2.771424;
        asteroid_e1[155] = 0.163045;
        asteroid_e2[155] = 0.163076;
        asteroid_i1[155] = 4.7740;
        asteroid_i2[155] = 4.7734;
        asteroid_omega1[155] = 133.8888;
        asteroid_omega2[155] = 134.0380;
        asteroid_w1[155] = 37.6205;
        asteroid_w2[155] = 37.2515;
        asteroid_mean1[155] = 10.8161;
        asteroid_mean2[155] = 279.4556;
        asteroid_meanmotion[155] = 0.003727565;

        /*  Asteroid 65  */
        /*
        65a P 240.0 25 12 4 0.4478E-09 12
        65b C 3.425753 0.115082 3.5502 155.4054 107.3485 21.7770 0.002711191 47800.
        65c C 3.425753 0.114678 3.5416 155.8693 106.2187 152.1518 0.002711191 44000.
        */
        mu_planet[166] = (4 * Math.PI / 3) * Math.pow((237.26 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[156] = 3.425753;
        asteroid_e1[156] = 0.115082;
        asteroid_e2[156] = 0.114678;
        asteroid_i1[156] = 3.5502;
        asteroid_i2[156] = 3.5416;
        asteroid_omega1[156] = 155.4054;
        asteroid_omega2[156] = 155.8693;
        asteroid_w1[156] = 107.3485;
        asteroid_w2[156] = 106.2187;
        asteroid_mean1[156] = 21.7770;
        asteroid_mean2[156] = 152.1518;
        asteroid_meanmotion[156] = 0.002711191;

        /*  Asteroid 444  */
        /*
        444a C 170.0 21 10 2 0.4690E-10 6
        444b C 2.770732 0.174788 10.2706 195.4137 154.3219 78.2081 0.003729092 47800.
        444c C 2.770732 0.174820 10.2637 195.5770 154.0277 346.4262 0.003729092 44000.
        */
        mu_planet[167] = (4 * Math.PI / 3) * Math.pow((159.57 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[157] = 2.770732;
        asteroid_e1[157] = 0.174788;
        asteroid_e2[157] = 0.174820;
        asteroid_i1[157] = 10.2706;
        asteroid_i2[157] = 10.2637;
        asteroid_omega1[157] = 195.4137;
        asteroid_omega2[157] = 195.5770;
        asteroid_w1[157] = 154.3219;
        asteroid_w2[157] = 154.0277;
        asteroid_mean1[157] = 78.2081;
        asteroid_mean2[157] = 346.4262;
        asteroid_meanmotion[157] = 0.003729092;

        /*  Asteroid 393  */
        /*
        393a C  106.0 21 10 3 0.5243E-10 10
        393b C 2.776743 0.332851 14.8644 212.5739 89.8161 246.4329 0.003717042 47800.
        393c C 2.776743 0.332737 14.8643 212.8698 89.4646 157.1996 0.003717042 44000.
        */
        mu_planet[168] = (4 * Math.PI / 3) * Math.pow((96.89 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[158] = 2.776743;
        asteroid_e1[158] = 0.332851;
        asteroid_e2[158] = 0.332737;
        asteroid_i1[158] = 14.8644;
        asteroid_i2[158] = 14.8643;
        asteroid_omega1[158] = 212.5739;
        asteroid_omega2[158] = 212.8698;
        asteroid_w1[158] = 89.8161;
        asteroid_w2[158] = 89.4646;
        asteroid_mean1[158] = 246.4329;
        asteroid_mean2[158] = 157.1996;
        asteroid_meanmotion[158] = 0.003717042;

        /*  Asteroid 147  */
        /*
        147a C 137.0 25 12 3 -0.4265E-11 8
        147b C 3.136446 0.026951 1.9287 248.5353 119.1530 134.8779 0.003095898 47800.
        147c C 3.136446 0.026933 1.9259 248.9056 118.8013 180.8082 0.003095898 44000.
        */
        mu_planet[169] = (4 * Math.PI / 3) * Math.pow((132.93 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[159] = 3.136446;
        asteroid_e1[159] = 0.026951;
        asteroid_e2[159] = 0.026933;
        asteroid_i1[159] = 1.9287;
        asteroid_i2[159] = 1.9259;
        asteroid_omega1[159] = 248.5353;
        asteroid_omega2[159] = 248.9056;
        asteroid_w1[159] = 119.1530;
        asteroid_w2[159] = 118.8013;
        asteroid_mean1[159] = 134.8779;
        asteroid_mean2[159] = 180.8082;
        asteroid_meanmotion[159] = 0.003095898;

        /*  Asteroid 205  */
        /*
        205a C  83.5 21 10 2 0.3100E-10 6
        205b C 2.777409 0.035306 10.6937 211.5629 173.5829 275.2530 0.003715697 47800.
        205c C 2.777409 0.035322 10.6905 211.7218 173.3883 186.2924 0.003715697 44000.
        */
        mu_planet[170] = (4 * Math.PI / 3) * Math.pow((80.94 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[160] = 2.777409;
        asteroid_e1[160] = 0.035306;
        asteroid_e2[160] = 0.035322;
        asteroid_i1[160] = 10.6937;
        asteroid_i2[160] = 10.6905;
        asteroid_omega1[160] = 211.5629;
        asteroid_omega2[160] = 211.7218;
        asteroid_w1[160] = 173.5829;
        asteroid_w2[160] = 173.3883;
        asteroid_mean1[160] = 275.2530;
        asteroid_mean2[160] = 186.2924;
        asteroid_meanmotion[160] = 0.003715697;

        /*  Asteroid 28  */
        /*
        28a S 126.0 21 10 2 0.1495E-09 10
        28b S 2.777495 0.150471 9.4117 144.0003 342.5609 135.7421 0.003715417 47800.
        28c S 2.777495 0.150592 9.4074 144.1491 342.2211 46.9978 0.003715417 44000.
        */
        mu_planet[171] = (4 * Math.PI / 3) * Math.pow((120.90 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[161] = 2.777495;
        asteroid_e1[161] = 0.150471;
        asteroid_e2[161] = 0.150592;
        asteroid_i1[161] = 9.4117;
        asteroid_i2[161] = 9.4074;
        asteroid_omega1[161] = 144.0003;
        asteroid_omega2[161] = 144.1491;
        asteroid_w1[161] = 342.5609;
        asteroid_w2[161] = 342.2211;
        asteroid_mean1[161] = 135.7421;
        asteroid_mean2[161] = 46.9978;
        asteroid_meanmotion[161] = 0.003715417;

        /*  Asteroid 139  */
        /*
        139a C 162.0 21 10 2 -0.1988E-10 10
        139b C 2.782923 0.173553 10.9035 1.6168 165.7282 137.7681 0.003704538 47800.
        139c C 2.782923 0.173620 10.9062 1.7826 165.3419 51.4219 0.003704538 44000.
        */
        mu_planet[172] = (4 * Math.PI / 3) * Math.pow((156.60 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[162] = 2.782923;
        asteroid_e1[162] = 0.173553;
        asteroid_e2[162] = 0.173620;
        asteroid_i1[162] = 10.9035;
        asteroid_i2[162] = 10.9062;
        asteroid_omega1[162] = 1.6168;
        asteroid_omega2[162] = 1.7826;
        asteroid_w1[162] = 165.7282;
        asteroid_w2[162] = 165.3419;
        asteroid_mean1[162] = 137.7681;
        asteroid_mean2[162] = 51.4219;
        asteroid_meanmotion[162] = 0.003704538;

        /*  Asteroid 68  */
        /*
        68a S 127.0 25 12 2 0.3116E-10 6
        68b S 2.782070 0.186459 7.9618 43.8678 304.1362 127.8548 0.003706263 47800.
        68c S 2.782070 0.186458 7.9624 44.0401 303.8283 41.0481 0.003706263 44000.
        */
        mu_planet[173] = (4 * Math.PI / 3) * Math.pow((122.57 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[163] = 2.782070;
        asteroid_e1[163] = 0.186459;
        asteroid_e2[163] = 0.186458;
        asteroid_i1[163] = 7.9618;
        asteroid_i2[163] = 7.9624;
        asteroid_omega1[163] = 43.8678;
        asteroid_omega2[163] = 44.0401;
        asteroid_w1[163] = 304.1362;
        asteroid_w2[163] = 303.8283;
        asteroid_mean1[163] = 127.8548;
        asteroid_mean2[163] = 41.0481;
        asteroid_meanmotion[163] = 0.003706263;

        /*  Asteroid 322  */
        /*
        322a X 73.8 25 12 2 0.7470E-10 7
        322b M 2.782560 0.246186 8.0247 252.2657 114.1332 62.7768 0.003705282 47800.
        322c M 2.782560 0.246227 8.0181 252.5229 113.7578 336.1664 0.003705282 44000.
        */
        mu_planet[174] = (4 * Math.PI / 3) * Math.pow((70.84 / 2), 3) * 5.3E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[164] = 2.782560;
        asteroid_e1[164] = 0.246186;
        asteroid_e2[164] = 0.246227;
        asteroid_i1[164] = 8.0247;
        asteroid_i2[164] = 8.0181;
        asteroid_omega1[164] = 252.2657;
        asteroid_omega2[164] = 252.5229;
        asteroid_w1[164] = 114.1332;
        asteroid_w2[164] = 113.7578;
        asteroid_mean1[164] = 62.7768;
        asteroid_mean2[164] = 336.1664;
        asteroid_meanmotion[164] = 0.003705282;

        /*  Asteroid 416  */
        /*
        416a S 89.5 25 12 3 0.2106E-10 10
        416b S 2.788749 0.220927 12.9043 57.8818 197.8022 352.9522 0.003692902 47800.
        416c S 2.788749 0.220740 12.9127 58.0364 197.4483 269.1182 0.003692902 44000.
        */
        mu_planet[175] = (4 * Math.PI / 3) * Math.pow((85.47 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[165] = 2.788749;
        asteroid_e1[165] = 0.220927;
        asteroid_e2[165] = 0.220740;
        asteroid_i1[165] = 12.9043;
        asteroid_i2[165] = 12.9127;
        asteroid_omega1[165] = 57.8818;
        asteroid_omega2[165] = 58.0364;
        asteroid_w1[165] = 197.8022;
        asteroid_w2[165] = 197.4483;
        asteroid_mean1[165] = 352.9522;
        asteroid_mean2[165] = 269.1182;
        asteroid_meanmotion[165] = 0.003692902;

        /*  Asteroid 216  */
        /*
        216a M 140.0 25 12 2 -0.4016E-11 10
        216b M 2.794451 0.250665 13.1144 215.1684 179.3781 311.5628 0.003681665 47800.
        216c M 2.794451 0.250723 13.1101 215.3232 179.0648 230.1346 0.003681665 44000.
        */
        mu_planet[176] = (4 * Math.PI / 3) * Math.pow((135.07 / 2), 3) * 5.3E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[166] = 2.794451;
        asteroid_e1[166] = 0.250665;
        asteroid_e2[166] = 0.250723;
        asteroid_i1[166] = 13.1144;
        asteroid_i2[166] = 13.1101;
        asteroid_omega1[166] = 215.1684;
        asteroid_omega2[166] = 215.3232;
        asteroid_w1[166] = 179.3781;
        asteroid_w2[166] = 179.0648;
        asteroid_mean1[166] = 311.5628;
        asteroid_mean2[166] = 230.1346;
        asteroid_meanmotion[166] = 0.003681665;

        /*  Asteroid 354  */
        /*
        354a S 162.0 25 12 3 0.6325E-12 10
        354b S 2.798081 0.114446 18.3993 140.0835 6.3709 217.4112 0.003674628 47800.
        354c S 2.798081 0.114458 18.3986 140.2192 6.0247 137.5670 0.003674628 44000.
        */
        mu_planet[177] = (4 * Math.PI / 3) * Math.pow((155.17 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[167] = 2.798081;
        asteroid_e1[167] = 0.114446;
        asteroid_e2[167] = 0.114458;
        asteroid_i1[167] = 18.3993;
        asteroid_i2[167] = 18.3986;
        asteroid_omega1[167] = 140.0835;
        asteroid_omega2[167] = 140.2192;
        asteroid_w1[167] = 6.3709;
        asteroid_w2[167] = 6.0247;
        asteroid_mean1[167] = 217.4112;
        asteroid_mean2[167] = 137.5670;
        asteroid_meanmotion[167] = 0.003674628;

        /*  Asteroid 346  */
        /*
        346a S 110.0 25 12 2 -0.7525E-11 6
        346b S 2.796271 0.101422 8.7588 91.7397 290.3729 284.7452 0.003678093 47800.
        346c S 2.796271 0.101450 8.7588 91.8846 290.1212 204.0429 0.003678093 44000.
        */
        mu_planet[178] = (4 * Math.PI / 3) * Math.pow((106.52 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[168] = 2.796271;
        asteroid_e1[168] = 0.101422;
        asteroid_e2[168] = 0.101450;
        asteroid_i1[168] = 8.7588;
        asteroid_i2[168] = 8.7588;
        asteroid_omega1[168] = 91.7397;
        asteroid_omega2[168] = 91.8846;
        asteroid_w1[168] = 290.3729;
        asteroid_w2[168] = 290.1212;
        asteroid_mean1[168] = 284.7452;
        asteroid_mean2[168] = 204.0429;
        asteroid_meanmotion[168] = 0.003678093;

        /*  Asteroid 236  */
        /*
        236a S 90.5 25 12 2 0.3273E-10 6
        236b S 2.799759 0.188707 7.6798 185.7046 173.8506 33.5426 0.003671174 47800.
        236c S 2.799759 0.188710 7.6742 185.8658 173.5407 314.3887 0.003671174 44000.
        */
        mu_planet[179] = (4 * Math.PI / 3) * Math.pow((86.20 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[169] = 2.799759;
        asteroid_e1[169] = 0.188707;
        asteroid_e2[169] = 0.188710;
        asteroid_i1[169] = 7.6798;
        asteroid_i2[169] = 7.6742;
        asteroid_omega1[169] = 185.7046;
        asteroid_omega2[169] = 185.8658;
        asteroid_w1[169] = 173.8506;
        asteroid_w2[169] = 173.5407;
        asteroid_mean1[169] = 33.5426;
        asteroid_mean2[169] = 314.3887;
        asteroid_meanmotion[169] = 0.003671174;

        /*  Asteroid 365  */
        /*
        365a X 110.0 25 12 2 0.6234E-10 6
        365b C 2.802719 0.156307 12.7987 185.0101 215.3360 336.1294 0.003665447 47800.
        365c C 2.802719 0.156262 12.7981 185.1692 215.0468 258.2039 0.003665447 44000.
        */
        mu_planet[180] = (4 * Math.PI / 3) * Math.pow((105.92 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[170] = 2.802719;
        asteroid_e1[170] = 0.156307;
        asteroid_e2[170] = 0.156262;
        asteroid_i1[170] = 12.7987;
        asteroid_i2[170] = 12.7981;
        asteroid_omega1[170] = 185.0101;
        asteroid_omega2[170] = 185.1692;
        asteroid_w1[170] = 215.3360;
        asteroid_w2[170] = 215.0468;
        asteroid_mean1[170] = 336.1294;
        asteroid_mean2[170] = 258.2039;
        asteroid_meanmotion[170] = 0.003665447;

        /*  Asteroid 266  */
        /*
        266a C 113.0 25 12 3 -0.4085E-11 6
        266b C 2.804280 0.156701 13.3989 235.6137 150.7015 155.6327 0.003662415 47800.
        266c C 2.804280 0.156814 13.3935 235.7772 150.4147 78.3606 0.003662415 44000.
        */
        mu_planet[181] = (4 * Math.PI / 3) * Math.pow((109.09 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[171] = 2.804280;
        asteroid_e1[171] = 0.156701;
        asteroid_e2[171] = 0.156814;
        asteroid_i1[171] = 13.3989;
        asteroid_i2[171] = 13.3935;
        asteroid_omega1[171] = 235.6137;
        asteroid_omega2[171] = 235.7772;
        asteroid_w1[171] = 150.7015;
        asteroid_w2[171] = 150.4147;
        asteroid_mean1[171] = 155.6327;
        asteroid_mean2[171] = 78.3606;
        asteroid_meanmotion[171] = 0.003662415;

        /*  Asteroid 804  */
        /*
        804a C 161.0 25 12 3 -0.6401E-11 10
        804b C 2.839117 0.139431 15.3609 347.4469 342.9366 60.8995 0.003595206 47800.
        804c C 2.839117 0.139451 15.3620 347.6027 342.6271 358.2908 0.003595206 44000.
        */
        mu_planet[182] = (4 * Math.PI / 3) * Math.pow((157.25 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[172] = 2.839117;
        asteroid_e1[172] = 0.139431;
        asteroid_e2[172] = 0.139451;
        asteroid_i1[172] = 15.3609;
        asteroid_i2[172] = 15.3620;
        asteroid_omega1[172] = 347.4469;
        asteroid_omega2[172] = 347.6027;
        asteroid_w1[172] = 342.9366;
        asteroid_w2[172] = 342.6271;
        asteroid_mean1[172] = 60.8995;
        asteroid_mean2[172] = 358.2908;
        asteroid_meanmotion[172] = 0.003595206;

        /*  Asteroid 385  */
        /*
        385a S 94.1 25 12 3 0.4170E-10 10
        385b S 2.846520 0.127218 13.6002 344.8633 187.7806 317.0992 0.003581107 47800.
        385c S 2.846520 0.127185 13.6064 345.0321 187.3522 257.6661 0.003581107 44000.
        */
        mu_planet[183] = (4 * Math.PI / 3) * Math.pow((91.53 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[173] = 2.846520;
        asteroid_e1[173] = 0.127218;
        asteroid_e2[173] = 0.127185;
        asteroid_i1[173] = 13.6002;
        asteroid_i2[173] = 13.6064;
        asteroid_omega1[173] = 344.8633;
        asteroid_omega2[173] = 345.0321;
        asteroid_w1[173] = 187.7806;
        asteroid_w2[173] = 187.3522;
        asteroid_mean1[173] = 317.0992;
        asteroid_mean2[173] = 257.6661;
        asteroid_meanmotion[173] = 0.003581107;

        /*  Asteroid 81  */
        /*
        81a C 124.0 25 12 3 0.6592E-12 7
        81b C 2.854180 0.210375 7.8285 1.0730 49.8977 301.0549 0.003566612 47800.
        81c C 2.854180 0.210386 7.8378 1.2995 49.5248 244.6644 0.003566612 44000.
        */
        mu_planet[184] = (4 * Math.PI / 3) * Math.pow((119.08 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[174] = 2.854180;
        asteroid_e1[174] = 0.210375;
        asteroid_e2[174] = 0.210386;
        asteroid_i1[174] = 7.8285;
        asteroid_i2[174] = 7.8378;
        asteroid_omega1[174] = 1.0730;
        asteroid_omega2[174] = 1.2995;
        asteroid_w1[174] = 49.8977;
        asteroid_w2[174] = 49.5248;
        asteroid_mean1[174] = 301.0549;
        asteroid_mean2[174] = 244.6644;
        asteroid_meanmotion[174] = 0.003566612;

        /*  Asteroid 909  */
        /*
        909a X 120.0 25 12 3 0.2425E-09 11
        909b C 3.541873 0.097011 18.7895 146.4173 231.8754 325.1436 0.002579648 47800.
        909c C 3.541873 0.096770 18.7882 146.6933 231.4957 123.5961 0.002579648 44000.
        */
        mu_planet[185] = (4 * Math.PI / 3) * Math.pow((116.44 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[175] = 3.541873;
        asteroid_e1[175] = 0.097011;
        asteroid_e2[175] = 0.096770;
        asteroid_i1[175] = 18.7895;
        asteroid_i2[175] = 18.7882;
        asteroid_omega1[175] = 146.4173;
        asteroid_omega2[175] = 146.6933;
        asteroid_w1[175] = 231.8754;
        asteroid_w2[175] = 231.4957;
        asteroid_mean1[175] = 325.1436;
        asteroid_mean2[175] = 123.5961;
        asteroid_meanmotion[175] = 0.002579648;

        /*  Asteroid 129  */
        /*
        129a M 125.0 25 12 3 0.1453E-10 12
        129b M 2.869898 0.211024 12.2390 136.0131 108.5475 272.4024 0.003537346 47800.
        129c M 2.869898 0.211006 12.2318 136.2583 108.1512 222.3884 0.003537346 44000.
        */
        mu_planet[186] = (4 * Math.PI / 3) * Math.pow((125.0 / 2), 3) * 5.3E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[176] = 2.869898;
        asteroid_e1[176] = 0.211024;
        asteroid_e2[176] = 0.211006;
        asteroid_i1[176] = 12.2390;
        asteroid_i2[176] = 12.2318;
        asteroid_omega1[176] = 136.0131;
        asteroid_omega2[176] = 136.2583;
        asteroid_w1[176] = 108.5475;
        asteroid_w2[176] = 108.1512;
        asteroid_mean1[176] = 272.4024;
        asteroid_mean2[176] = 222.3884;
        asteroid_meanmotion[176] = 0.003537346;

        /*  Asteroid 508  */
        /*
        508a C 147.0 25 12 3 -0.2355E-10 9
        508b C 3.161464 0.018392 13.3427 44.1240 213.1726 87.7985 0.003059349 47800.
        508c C 3.161464 0.018243 13.3467 44.3220 212.3657 142.3138 0.003059349 44000.
        */
        mu_planet[187] = (4 * Math.PI / 3) * Math.pow((142.35 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[177] = 3.161464;
        asteroid_e1[177] = 0.018392;
        asteroid_e2[177] = 0.018243;
        asteroid_i1[177] = 13.3427;
        asteroid_i2[177] = 13.3467;
        asteroid_omega1[177] = 44.1240;
        asteroid_omega2[177] = 44.3220;
        asteroid_w1[177] = 213.1726;
        asteroid_w2[177] = 212.3657;
        asteroid_mean1[177] = 87.7985;
        asteroid_mean2[177] = 142.3138;
        asteroid_meanmotion[177] = 0.003059349;

        /*  Asteroid 195  */
        /*
        195a C 89.7 25 12 3 0.6820E-11 7
        195b C 2.878011 0.042026 6.9676 6.7928 118.7878 295.4335 0.003522427 47800.
        195c C 2.878011 0.042138 6.9715 6.9755 118.3598 248.7620 0.003522427 44000.
        */
        mu_planet[188] = (4 * Math.PI / 3) * Math.pow((85.71 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[178] = 2.878011;
        asteroid_e1[178] = 0.042026;
        asteroid_e2[178] = 0.042138;
        asteroid_i1[178] = 6.9676;
        asteroid_i2[178] = 6.9715;
        asteroid_omega1[178] = 6.7928;
        asteroid_omega2[178] = 6.9755;
        asteroid_w1[178] = 118.7878;
        asteroid_w2[178] = 118.3598;
        asteroid_mean1[178] = 295.4335;
        asteroid_mean2[178] = 248.7620;
        asteroid_meanmotion[178] = 0.003522427;

        /*  Asteroid 47  */
        /*
        47a C 137.0 25 12 2 -0.1164E-09 7
        47b C 2.880112 0.132978 4.9819 2.8707 313.9493 35.2195 0.003518529 47800.
        47c C 2.880112 0.132902 4.9849 3.0749 313.5736 349.3230 0.003518529 44000.
        */
        mu_planet[189] = (4 * Math.PI / 3) * Math.pow((126.96 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[179] = 2.880112;
        asteroid_e1[179] = 0.132978;
        asteroid_e2[179] = 0.132902;
        asteroid_i1[179] = 4.9819;
        asteroid_i2[179] = 4.9849;
        asteroid_omega1[179] = 2.8707;
        asteroid_omega2[179] = 3.0749;
        asteroid_w1[179] = 313.9493;
        asteroid_w2[179] = 313.5736;
        asteroid_mean1[179] = 35.2195;
        asteroid_mean2[179] = 349.3230;
        asteroid_meanmotion[179] = 0.003518529;

        /*  Asteroid 104  */
        /*
        104a C 127.0 25 12 3 0.2727E-09 9
        104b C 3.147951 0.159396 2.8179 41.8992 26.5064 182.3483 0.003078819 47800.
        104c C 3.147951 0.159558 2.8252 42.1179 25.9424 232.3610 0.003078819 44000.
        */
        mu_planet[190] = (4 * Math.PI / 3) * Math.pow((123.68 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[180] = 3.147951;
        asteroid_e1[180] = 0.159396;
        asteroid_e2[180] = 0.159558;
        asteroid_i1[180] = 2.8179;
        asteroid_i2[180] = 2.8252;
        asteroid_omega1[180] = 41.8992;
        asteroid_omega2[180] = 42.1179;
        asteroid_w1[180] = 26.5064;
        asteroid_w2[180] = 25.9424;
        asteroid_mean1[180] = 182.3483;
        asteroid_mean2[180] = 232.3610;
        asteroid_meanmotion[180] = 0.003078819;

        /*  Asteroid 426  */
        /*
        426a F 134.0 25 12 2 0.1377E-09 12
        426b C 2.888439 0.104268 19.5199 311.1736 220.0288 82.1975 0.003503583 47800.
        426c C 2.888439 0.104089 19.5255 311.3323 219.7396 39.5141 0.003503583 44000.
        */
        mu_planet[191] = (4 * Math.PI / 3) * Math.pow((127.10 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[181] = 2.888439;
        asteroid_e1[181] = 0.104268;
        asteroid_e2[181] = 0.104089;
        asteroid_i1[181] = 19.5199;
        asteroid_i2[181] = 19.5255;
        asteroid_omega1[181] = 311.1736;
        asteroid_omega2[181] = 311.3323;
        asteroid_w1[181] = 220.0288;
        asteroid_w2[181] = 219.7396;
        asteroid_mean1[181] = 82.1975;
        asteroid_mean2[181] = 39.5141;
        asteroid_meanmotion[181] = 0.003503583;

        /*  Asteroid 471  */
        /*
        471a S 135.0 25 12 2 -0.2258E-09 12
        471b S 2.888379 0.231703 14.9698 83.7371 314.0030 238.5353 0.003503496 47800.
        471c S 2.888379 0.231917 14.9616 83.9174 313.6935 195.8696 0.003503496 44000.
        */
        mu_planet[192] = (4 * Math.PI / 3) * Math.pow((134.19 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[182] = 2.888379;
        asteroid_e1[182] = 0.231703;
        asteroid_e2[182] = 0.231917;
        asteroid_i1[182] = 14.9698;
        asteroid_i2[182] = 14.9616;
        asteroid_omega1[182] = 83.7371;
        asteroid_omega2[182] = 83.9174;
        asteroid_w1[182] = 314.0030;
        asteroid_w2[182] = 313.6935;
        asteroid_mean1[182] = 238.5353;
        asteroid_mean2[182] = 195.8696;
        asteroid_meanmotion[182] = 0.003503496;

        /*  Asteroid 431  */
        /*
        431a B 97.7 25 12 3 -0.1483E-09 9
        431b C 3.130174 0.180334 1.8302 116.9451 214.5571 288.0399 0.003105074 47800.
        431c C 3.130174 0.180206 1.8285 117.0207 214.1682 332.3040 0.003105074 44000.
        */
        mu_planet[193] = (4 * Math.PI / 3) * Math.pow((95.03 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[183] = 3.130174;
        asteroid_e1[183] = 0.180334;
        asteroid_e2[183] = 0.180206;
        asteroid_i1[183] = 1.8302;
        asteroid_i2[183] = 1.8285;
        asteroid_omega1[183] = 116.9451;
        asteroid_omega2[183] = 117.0207;
        asteroid_w1[183] = 214.5571;
        asteroid_w2[183] = 214.1682;
        asteroid_mean1[183] = 288.0399;
        asteroid_mean2[183] = 332.3040;
        asteroid_meanmotion[183] = 0.003105074;

        /*  Asteroid 386  */
        /*
        386a C 173.0 25 12 2 -0.7104E-10 12
        386b C 2.896301 0.170753 20.2587 166.4924 219.5834 266.5655 0.003489293 47800.
        386c C 2.896301 0.170550 20.2610 166.6417 219.3259 226.9709 0.003489293 44000.
        */
        mu_planet[194] = (4 * Math.PI / 3) * Math.pow((165.01 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[184] = 2.896301;
        asteroid_e1[184] = 0.170753;
        asteroid_e2[184] = 0.170550;
        asteroid_i1[184] = 20.2587;
        asteroid_i2[184] = 20.2610;
        asteroid_omega1[184] = 166.4924;
        asteroid_omega2[184] = 166.6417;
        asteroid_w1[184] = 219.5834;
        asteroid_w2[184] = 219.3259;
        asteroid_mean1[184] = 266.5655;
        asteroid_mean2[184] = 226.9709;
        asteroid_meanmotion[184] = 0.003489293;

        /*  Asteroid 238  */
        /*
        238a C 156.0 25 12 2 0.8987E-10 7
        238b C 2.907039 0.089023 12.4026 183.6728 208.3903 23.8570 0.003469850 47800.
        238c C 2.907039 0.089013 12.3996 183.8414 208.0924 348.5169 0.003469850 44000.
        */
        mu_planet[195] = (4 * Math.PI / 3) * Math.pow((148.49 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[185] = 2.907039;
        asteroid_e1[185] = 0.089023;
        asteroid_e2[185] = 0.089013;
        asteroid_i1[185] = 12.4026;
        asteroid_i2[185] = 12.3996;
        asteroid_omega1[185] = 183.6728;
        asteroid_omega2[185] = 183.8414;
        asteroid_w1[185] = 208.3903;
        asteroid_w2[185] = 208.0924;
        asteroid_mean1[185] = 23.8570;
        asteroid_mean2[185] = 348.5169;
        asteroid_meanmotion[185] = 0.003469850;

        /*  Asteroid 338  */
        /*
        338a M 62.1 25 12 2 0.2901E-10 7
        338b M 2.912515 0.019874 6.0413 287.2951 115.6802 154.0818 0.003459998 47800.
        338c M 2.912515 0.019927 6.0419 287.5179 115.5252 120.6895 0.003459998 44000.
        */
        mu_planet[196] = (4 * Math.PI / 3) * Math.pow((63.11 / 2), 3) * 5.3E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[186] = 2.912515;
        asteroid_e1[186] = 0.019874;
        asteroid_e2[186] = 0.019927;
        asteroid_i1[186] = 6.0413;
        asteroid_i2[186] = 6.0419;
        asteroid_omega1[186] = 287.2951;
        asteroid_omega2[186] = 287.5179;
        asteroid_w1[186] = 115.6802;
        asteroid_w2[186] = 115.5252;
        asteroid_mean1[186] = 154.0818;
        asteroid_mean2[186] = 120.6895;
        asteroid_meanmotion[186] = 0.003459998;

        /*  Asteroid 22  */
        /*
        22a M 187.0 25 12 2 0.4339E-10 7
        22b M 2.909881 0.100610 13.7149 65.8165 355.6649 212.4406 0.003464758 47800.
        22c M 2.909881 0.100665 13.7177 65.9725 355.3441 178.2443 0.003464758 44000.
        */
        mu_planet[197] = (4 * Math.PI / 3) * Math.pow((181.0 / 2), 3) * 5.3E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[187] = 2.909881;
        asteroid_e1[187] = 0.100610;
        asteroid_e2[187] = 0.100665;
        asteroid_i1[187] = 13.7149;
        asteroid_i2[187] = 13.7177;
        asteroid_omega1[187] = 65.8165;
        asteroid_omega2[187] = 65.9725;
        asteroid_w1[187] = 355.6649;
        asteroid_w2[187] = 355.3441;
        asteroid_mean1[187] = 212.4406;
        asteroid_mean2[187] = 178.2443;
        asteroid_meanmotion[187] = 0.003464758;

        /*  Asteroid 24  */
        /*
        24a C 198.0 25 12 3 -0.3958E-10 11
        24b C 3.134382 0.127265 0.7568 35.7971 110.0350 230.2781 0.003098805 47800.
        24c C 3.134382 0.127431 0.7623 35.8231 109.5560 276.0469 0.003098805 44000.
        */
        mu_planet[198] = (4 * Math.PI / 3) * Math.pow((198.0 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[188] = 3.134382;
        asteroid_e1[188] = 0.127265;
        asteroid_e2[188] = 0.127431;
        asteroid_i1[188] = 0.7568;
        asteroid_i2[188] = 0.7623;
        asteroid_omega1[188] = 35.7971;
        asteroid_omega2[188] = 35.8231;
        asteroid_w1[188] = 110.0350;
        asteroid_w2[188] = 109.5560;
        asteroid_mean1[188] = 230.2781;
        asteroid_mean2[188] = 276.0469;
        asteroid_meanmotion[188] = 0.003098805;

        /*  Asteroid 674  */
        /*
        674a S 101.0 25 12 3 0.4696E-11 12
        674b S 2.923769 0.193749 13.5241 58.0179 41.0923 16.2561 0.003440032 47800.
        674c S 2.923769 0.193685 13.5355 58.2169 40.7366 347.4353 0.003440032 44000.
        */
        mu_planet[199] = (4 * Math.PI / 3) * Math.pow((97.35 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[189] = 2.923769;
        asteroid_e1[189] = 0.193749;
        asteroid_e2[189] = 0.193685;
        asteroid_i1[189] = 13.5241;
        asteroid_i2[189] = 13.5355;
        asteroid_omega1[189] = 58.0179;
        asteroid_omega2[189] = 58.2169;
        asteroid_w1[189] = 41.0923;
        asteroid_w2[189] = 40.7366;
        asteroid_mean1[189] = 16.2561;
        asteroid_mean2[189] = 347.4353;
        asteroid_meanmotion[189] = 0.003440032;

        /*  Asteroid 702  */
        /*
        702a C 202.0 25 12 4 -0.2758E-10 10
        702b C 3.194364 0.021815 20.5683 289.5855 26.2266 312.4575 0.003012446 47800.
        702c C 3.194364 0.021755 20.5689 289.7589 25.7075 16.9215 0.003012446 44000.
        */
        mu_planet[200] = (4 * Math.PI / 3) * Math.pow((194.73 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[190] = 3.194364;
        asteroid_e1[190] = 0.021815;
        asteroid_e2[190] = 0.021755;
        asteroid_i1[190] = 20.5683;
        asteroid_i2[190] = 20.5689;
        asteroid_omega1[190] = 289.5855;
        asteroid_omega2[190] = 289.7589;
        asteroid_w1[190] = 26.2266;
        asteroid_w2[190] = 25.7075;
        asteroid_mean1[190] = 312.4575;
        asteroid_mean2[190] = 16.9215;
        asteroid_meanmotion[190] = 0.003012446;

        /*  Asteroid 40  */
        /*
        40a S 111.0 16 7 2 -0.1255E-10 4
        40b S 2.267249 0.046690 4.2581 93.7886 268.9371 230.3254 0.005038361 47800.
        40c S 2.267249 0.046682 4.2584 93.8561 268.8245 213.3984 0.005038361 44000.
        */
        mu_planet[201] = (4 * Math.PI / 3) * Math.pow((107.62 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[191] = 2.267249;
        asteroid_e1[191] = 0.046690;
        asteroid_e2[191] = 0.046682;
        asteroid_i1[191] = 4.2581;
        asteroid_i2[191] = 4.2584;
        asteroid_omega1[191] = 93.7886;
        asteroid_omega2[191] = 93.8561;
        asteroid_w1[191] = 268.9371;
        asteroid_w2[191] = 268.8245;
        asteroid_mean1[191] = 230.3254;
        asteroid_mean2[191] = 213.3984;
        asteroid_meanmotion[191] = 0.005038361;

        /*  Asteroid 705  */
        /*
        705a X 139.0 25 12 2 0.4754E-10 12
        705b C 2.923353 0.051384 25.0219 2.5312 99.7485 217.9751 0.003441097 47800.
        705c C 2.923353 0.051492 25.0242 2.6614 99.6357 188.7484 0.003441097 44000.
        */
        mu_planet[202] = (4 * Math.PI / 3) * Math.pow((134.22 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[192] = 2.923353;
        asteroid_e1[192] = 0.051384;
        asteroid_e2[192] = 0.051492;
        asteroid_i1[192] = 25.0219;
        asteroid_i2[192] = 25.0242;
        asteroid_omega1[192] = 2.5312;
        asteroid_omega2[192] = 2.6614;
        asteroid_w1[192] = 99.7485;
        asteroid_w2[192] = 99.6357;
        asteroid_mean1[192] = 217.9751;
        asteroid_mean2[192] = 188.7484;
        asteroid_meanmotion[192] = 0.003441097;

        /*  Asteroid 776  */
        /*
        776a C 177.0 25 12 2 -0.5455E-10 7
        776b C 2.932555 0.163654 18.2217 79.5556 305.6896 114.1546 0.003424713 47800.
        776c C 2.932555 0.163840 18.2184 79.7186 305.4359 88.6031 0.003424713 44000.
        */
        mu_planet[203] = (4 * Math.PI / 3) * Math.pow((151.17 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[193] = 2.932555;
        asteroid_e1[193] = 0.163654;
        asteroid_e2[193] = 0.163840;
        asteroid_i1[193] = 18.2217;
        asteroid_i2[193] = 18.2184;
        asteroid_omega1[193] = 79.5556;
        asteroid_omega2[193] = 79.7186;
        asteroid_w1[193] = 305.6896;
        asteroid_w2[193] = 305.4359;
        asteroid_mean1[193] = 114.1546;
        asteroid_mean2[193] = 88.6031;
        asteroid_meanmotion[193] = 0.003424713;

        /*  Asteroid 375  */
        /*
        375a C 216.0 25 12 3 -0.3837E-10 8
        375b C 3.128300 0.100609 15.9216 336.3279 343.7054 169.1980 0.003108224 47800.
        375c C 3.128300 0.100628 15.9233 336.5253 343.2570 212.7142 0.003108224 44000.
        */
        mu_planet[204] = (4 * Math.PI / 3) * Math.pow((216.0 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[194] = 3.128300;
        asteroid_e1[194] = 0.100609;
        asteroid_e2[194] = 0.100628;
        asteroid_i1[194] = 15.9216;
        asteroid_i2[194] = 15.9233;
        asteroid_omega1[194] = 336.3279;
        asteroid_omega2[194] = 336.5253;
        asteroid_w1[194] = 343.7054;
        asteroid_w2[194] = 343.2570;
        asteroid_mean1[194] = 169.1980;
        asteroid_mean2[194] = 212.7142;
        asteroid_meanmotion[194] = 0.003108224;

        /*  Asteroid 69  */
        /*
        69a M 143.0 25 12 3 0.2094E-10 9
        69b M 2.979471 0.167405 8.5701 185.0488 287.8018 32.3419 0.003343888 47800.
        69c M 2.979471 0.167565 8.5613 185.3070 287.3483 24.4926 0.003343888 44000.
        */
        mu_planet[205] = (4 * Math.PI / 3) * Math.pow((138.13 / 2), 3) * 5.3E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[195] = 2.979471;
        asteroid_e1[195] = 0.167405;
        asteroid_e2[195] = 0.167565;
        asteroid_i1[195] = 8.5701;
        asteroid_i2[195] = 8.5613;
        asteroid_omega1[195] = 185.0488;
        asteroid_omega2[195] = 185.3070;
        asteroid_w1[195] = 287.8018;
        asteroid_w2[195] = 287.3483;
        asteroid_mean1[195] = 32.3419;
        asteroid_mean2[195] = 24.4926;
        asteroid_meanmotion[195] = 0.003343888;

        /*  Asteroid 150  */
        /*
        150a C 157.0 25 12 2 0.2607E-09 7
        150b C 2.982180 0.127499 2.1901 206.0279 152.2786 61.3635 0.003339332 47800.
        150c C 2.982180 0.127472 2.1847 206.2788 151.8535 54.4851 0.003339332 44000.
        */
        mu_planet[206] = (4 * Math.PI / 3) * Math.pow((151.14 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[196] = 2.982180;
        asteroid_e1[196] = 0.127499;
        asteroid_e2[196] = 0.127472;
        asteroid_i1[196] = 2.1901;
        asteroid_i2[196] = 2.1847;
        asteroid_omega1[196] = 206.0279;
        asteroid_omega2[196] = 206.2788;
        asteroid_w1[196] = 152.2786;
        asteroid_w2[196] = 151.8535;
        asteroid_mean1[196] = 61.3635;
        asteroid_mean2[196] = 54.4851;
        asteroid_meanmotion[196] = 0.003339332;

        /*  Asteroid 117  */
        /*
        117a C 154.0 25 12 3 -0.2114E-11 9
        117b C 2.991326 0.025555 14.9189 348.5667 57.8821 244.8554 0.003324229 47800.
        117c C 2.991326 0.025598 14.9226 348.7446 57.7329 241.0626 0.003324229 44000.
        */
        mu_planet[207] = (4 * Math.PI / 3) * Math.pow((148.71 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[197] = 2.991326;
        asteroid_e1[197] = 0.025555;
        asteroid_e2[197] = 0.025598;
        asteroid_i1[197] = 14.9189;
        asteroid_i2[197] = 14.9226;
        asteroid_omega1[197] = 348.5667;
        asteroid_omega2[197] = 348.7446;
        asteroid_w1[197] = 57.8821;
        asteroid_w2[197] = 57.7329;
        asteroid_mean1[197] = 244.8554;
        asteroid_mean2[197] = 241.0626;
        asteroid_meanmotion[197] = 0.003324229;

        /*  Asteroid 35  */
        /*
        35a C 108.0 25 12 4 0.7369E-10 12
        35b C 2.995638 0.223697 7.9745 353.6383 212.5858 320.8487 0.003316647 47800.
        35c C 2.995638 0.223522 7.9950 353.9365 211.9933 319.0295 0.003316647 44000.
        */
        mu_planet[208] = (4 * Math.PI / 3) * Math.pow((103.11 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[198] = 2.995638;
        asteroid_e1[198] = 0.223697;
        asteroid_e2[198] = 0.223522;
        asteroid_i1[198] = 7.9745;
        asteroid_i2[198] = 7.9950;
        asteroid_omega1[198] = 353.6383;
        asteroid_omega2[198] = 353.9365;
        asteroid_w1[198] = 212.5858;
        asteroid_w2[198] = 211.9933;
        asteroid_mean1[198] = 320.8487;
        asteroid_mean2[198] = 319.0295;
        asteroid_meanmotion[198] = 0.003316647;

        /*  Asteroid 747  */
        /*
        747a C 178.0 25 12 3 0.1559E-10 9
        747b C 2.996949 0.342454 18.1738 129.7645 275.5315 356.4726 0.003314903 47800.
        747c C 2.996949 0.342541 18.1653 130.0755 275.1874 354.7720 0.003314903 44000.
        */
        mu_planet[209] = (4 * Math.PI / 3) * Math.pow((171.70 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[199] = 2.996949;
        asteroid_e1[199] = 0.342454;
        asteroid_e2[199] = 0.342541;
        asteroid_i1[199] = 18.1738;
        asteroid_i2[199] = 18.1653;
        asteroid_omega1[199] = 129.7645;
        asteroid_omega2[199] = 130.0755;
        asteroid_w1[199] = 275.5315;
        asteroid_w2[199] = 275.1874;
        asteroid_mean1[199] = 356.4726;
        asteroid_mean2[199] = 354.7720;
        asteroid_meanmotion[199] = 0.003314903;

        /*  Asteroid 772  */
        /*
        772a C 123.0 25 12 2 -0.1245E-09 11
        772b C 3.000579 0.094463 28.8190 63.5271 143.3874 108.5847 0.003309114 47800.
        772c C 3.000579 0.094729 28.8174 63.6531 143.1349 108.2378 0.003309114 44000.
        */
        mu_planet[210] = (4 * Math.PI / 3) * Math.pow((117.66 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[200] = 3.000579;
        asteroid_e1[200] = 0.094463;
        asteroid_e2[200] = 0.094729;
        asteroid_i1[200] = 28.8190;
        asteroid_i2[200] = 28.8174;
        asteroid_omega1[200] = 63.5271;
        asteroid_omega2[200] = 63.6531;
        asteroid_w1[200] = 143.3874;
        asteroid_w2[200] = 143.1349;
        asteroid_mean1[200] = 108.5847;
        asteroid_mean2[200] = 108.2378;
        asteroid_meanmotion[200] = 0.003309114;

        /*  Asteroid 360  */
        /*
        360a C 121.0 25 12 3 -0.2600E-10 9
        360b C 3.001585 0.178769 11.7065 132.2791 288.4192 291.5880 0.003307047 47800.
        360c C 3.001585 0.178911 11.7001 132.4975 288.0572 291.7082 0.003307047 44000.
        */
        mu_planet[211] = (4 * Math.PI / 3) * Math.pow((115.76 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[201] = 3.001585;
        asteroid_e1[201] = 0.178769;
        asteroid_e2[201] = 0.178911;
        asteroid_i1[201] = 11.7065;
        asteroid_i2[201] = 11.7001;
        asteroid_omega1[201] = 132.2791;
        asteroid_omega2[201] = 132.4975;
        asteroid_w1[201] = 288.4192;
        asteroid_w2[201] = 288.0572;
        asteroid_mean1[201] = 291.5880;
        asteroid_mean2[201] = 291.7082;
        asteroid_meanmotion[201] = 0.003307047;

        /*  Asteroid 388  */
        /*
        388a C 120.0 25 12 3 -0.5904E-10 7
        388b C 3.005682 0.061638 6.4532 354.2551 329.2369 321.2222 0.003300273 47800.
        388c C 3.005682 0.061557 6.4573 354.4712 328.8621 322.8326 0.003300273 44000.
        */
        mu_planet[212] = (4 * Math.PI / 3) * Math.pow((114.17 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[202] = 3.005682;
        asteroid_e1[202] = 0.061638;
        asteroid_e2[202] = 0.061557;
        asteroid_i1[202] = 6.4532;
        asteroid_i2[202] = 6.4573;
        asteroid_omega1[202] = 354.2551;
        asteroid_omega2[202] = 354.4712;
        asteroid_w1[202] = 329.2369;
        asteroid_w2[202] = 328.8621;
        asteroid_mean1[202] = 321.2222;
        asteroid_mean2[202] = 322.8326;
        asteroid_meanmotion[202] = 0.003300273;

        /*  Asteroid 250  */
        /*
        250a M 85.5 25 12 3 0.3063E-10 9
        250b M 3.145932 0.132788 12.8527 24.1046 71.1879 134.3914 0.003082016 47800.
        250c M 3.145932 0.132873 12.8592 24.3528 70.7307 183.5716 0.003082016 44000.
        */
        mu_planet[213] = (4 * Math.PI / 3) * Math.pow((79.75 / 2), 3) * 5.3E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[203] = 3.145932;
        asteroid_e1[203] = 0.132788;
        asteroid_e2[203] = 0.132873;
        asteroid_i1[203] = 12.8527;
        asteroid_i2[203] = 12.8592;
        asteroid_omega1[203] = 24.1046;
        asteroid_omega2[203] = 24.3528;
        asteroid_w1[203] = 71.1879;
        asteroid_w2[203] = 70.7307;
        asteroid_mean1[203] = 134.3914;
        asteroid_mean2[203] = 183.5716;
        asteroid_meanmotion[203] = 0.003082016;

        /*  Asteroid 176  */
        /*
        176a G 125.0 25 12 3 0.6417E-09 10 amp 0.14 radians
        176b C 3.178085 0.173852 22.6786 200.3957 183.1575 272.2931 0.003035538 47800.
        176c C 3.178085 0.173861 22.6754 200.5614 182.6398 331.7356 0.003035538 44000.
        */
        mu_planet[214] = (4 * Math.PI / 3) * Math.pow((121.04 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[204] = 3.178085;
        asteroid_e1[204] = 0.173852;
        asteroid_e2[204] = 0.173861;
        asteroid_i1[204] = 22.6786;
        asteroid_i2[204] = 22.6754;
        asteroid_omega1[204] = 200.3957;
        asteroid_omega2[204] = 200.5614;
        asteroid_w1[204] = 183.1575;
        asteroid_w2[204] = 182.6398;
        asteroid_mean1[204] = 272.2931;
        asteroid_mean2[204] = 331.7356;
        asteroid_meanmotion[204] = 0.003035538;

        /*  Asteroid 162  */
        /*
        162a ST 105.0 25 12 3 -0.4986E-10 12
        162b S 3.020561 0.178706 6.0819 36.0276 112.3147 266.6452 0.003275710 47800.
        162c S 3.020561 0.178881 6.0823 36.3092 111.7309 273.7469 0.003275710 44000.
        */
        mu_planet[215] = (4 * Math.PI / 3) * Math.pow((99.10 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[205] = 3.020561;
        asteroid_e1[205] = 0.178706;
        asteroid_e2[205] = 0.178881;
        asteroid_i1[205] = 6.0819;
        asteroid_i2[205] = 6.0823;
        asteroid_omega1[205] = 36.0276;
        asteroid_omega2[205] = 36.3092;
        asteroid_w1[205] = 112.3147;
        asteroid_w2[205] = 111.7309;
        asteroid_mean1[205] = 266.6452;
        asteroid_mean2[205] = 273.7469;
        asteroid_meanmotion[205] = 0.003275710;

        /*  Asteroid 350  */
        /*
        350a C 123.0 25 12 3 -0.2242E-10 11
        350b C 3.114107 0.155860 24.8640 89.8995 336.1151 222.7331 0.003129632 47800.
        350c C 3.114107 0.156145 24.8607 90.0496 335.7478 261.5544 0.003129632 44000.
        */
        mu_planet[216] = (4 * Math.PI / 3) * Math.pow((118.35 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[206] = 3.114107;
        asteroid_e1[206] = 0.155860;
        asteroid_e2[206] = 0.156145;
        asteroid_i1[206] = 24.8640;
        asteroid_i2[206] = 24.8607;
        asteroid_omega1[206] = 89.8995;
        asteroid_omega2[206] = 90.0496;
        asteroid_w1[206] = 336.1151;
        asteroid_w2[206] = 335.7478;
        asteroid_mean1[206] = 222.7331;
        asteroid_mean2[206] = 261.5544;
        asteroid_meanmotion[206] = 0.003129632;

        /*  Asteroid 506  */
        /*
        506a C 109.0 25 12 3 -0.2666E-10 11
        506b C 3.043413 0.144592 16.9722 312.7098 145.6896 158.7354 0.003239216 47800.
        506c C 3.043413 0.144874 16.9699 312.9010 145.2974 173.6815 0.003239216 44000.
        */
        mu_planet[217] = (4 * Math.PI / 3) * Math.pow((105.94 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[207] = 3.043413;
        asteroid_e1[207] = 0.144592;
        asteroid_e2[207] = 0.144874;
        asteroid_i1[207] = 16.9722;
        asteroid_i2[207] = 16.9699;
        asteroid_omega1[207] = 312.7098;
        asteroid_omega2[207] = 312.9010;
        asteroid_w1[207] = 145.6896;
        asteroid_w2[207] = 145.2974;
        asteroid_mean1[207] = 158.7354;
        asteroid_mean2[207] = 173.6815;
        asteroid_meanmotion[207] = 0.003239216;

        /*  Asteroid 211  */
        /*
        211a C 148.0 25 12 3 -0.4977E-10 11
        211b C 3.044384 0.158415 3.8759 263.4626 173.7280 240.3064 0.003237400 47800.
        211c C 3.044384 0.158557 3.8744 263.7516 173.1922 255.6936 0.003237400 44000.
        */
        mu_planet[218] = (4 * Math.PI / 3) * Math.pow((143.19 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[208] = 3.044384;
        asteroid_e1[208] = 0.158415;
        asteroid_e2[208] = 0.158557;
        asteroid_i1[208] = 3.8759;
        asteroid_i2[208] = 3.8744;
        asteroid_omega1[208] = 263.4626;
        asteroid_omega2[208] = 263.7516;
        asteroid_w1[208] = 173.7280;
        asteroid_w2[208] = 173.1922;
        asteroid_mean1[208] = 240.3064;
        asteroid_mean2[208] = 255.6936;
        asteroid_meanmotion[208] = 0.003237400;

        /*  Asteroid 514  */
        /*
        514a C 110.0 25 12 3 0.1725E-10 8
        514b C 3.046654 0.041362 3.8777 268.6188 114.2148 28.7892 0.003233868 47800.
        514c C 3.046654 0.041387 3.8768 268.9034 113.8680 44.7608 0.003233868 44000.
        */
        mu_planet[219] = (4 * Math.PI / 3) * Math.pow((106.17 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[209] = 3.046654;
        asteroid_e1[209] = 0.041362;
        asteroid_e2[209] = 0.041387;
        asteroid_i1[209] = 3.8777;
        asteroid_i2[209] = 3.8768;
        asteroid_omega1[209] = 268.6188;
        asteroid_omega2[209] = 268.9034;
        asteroid_w1[209] = 114.2148;
        asteroid_w2[209] = 113.8680;
        asteroid_mean1[209] = 28.7892;
        asteroid_mean2[209] = 44.7608;
        asteroid_meanmotion[209] = 0.003233868;

        /*  Asteroid 241  */
        /*
        241a C 169.0 25 12 3 -0.7697E-10 8
        241b C 3.049705 0.101247 5.5105 270.4573 76.3814 269.2369 0.003229020 47800.
        241c C 3.049705 0.101171 5.5104 270.7334 75.9380 286.3692 0.003229020 44000.
        */
        mu_planet[220] = (4 * Math.PI / 3) * Math.pow((168.90 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[210] = 3.049705;
        asteroid_e1[210] = 0.101247;
        asteroid_e2[210] = 0.101171;
        asteroid_i1[210] = 5.5105;
        asteroid_i2[210] = 5.5104;
        asteroid_omega1[210] = 270.4573;
        asteroid_omega2[210] = 270.7334;
        asteroid_w1[210] = 76.3814;
        asteroid_w2[210] = 75.9380;
        asteroid_mean1[210] = 269.2369;
        asteroid_mean2[210] = 286.3692;
        asteroid_meanmotion[210] = 0.003229020;

        /*  Asteroid 740  */
        /*
        740a C 94.5 25 12 3 -0.5491E-11 11
        740b C 3.051723 0.111375 10.8449 115.8038 49.6214 119.0035 0.003225799 47800.
        740c C 3.051723 0.111353 10.8474 116.0178 49.1175 136.9596 0.003225799 44000.
        */
        mu_planet[221] = (4 * Math.PI / 3) * Math.pow((90.90 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[211] = 3.051723;
        asteroid_e1[211] = 0.111375;
        asteroid_e2[211] = 0.111353;
        asteroid_i1[211] = 10.8449;
        asteroid_i2[211] = 10.8474;
        asteroid_omega1[211] = 115.8038;
        asteroid_omega2[211] = 116.0178;
        asteroid_w1[211] = 49.6214;
        asteroid_w2[211] = 49.1175;
        asteroid_mean1[211] = 119.0035;
        asteroid_mean2[211] = 136.9596;
        asteroid_meanmotion[211] = 0.003225799;

        /*  Asteroid 96  */
        /*
        96a T  174.0 25 12 3 0.2106E-11 11
        96b C 3.052132 0.138279 15.9974 321.5518 205.6362 272.0573 0.003225300 47800.
        96c C 3.052132 0.138089 16.0078 321.7586 205.1774 290.0842 0.003225300 44000.
        */
        mu_planet[222] = (4 * Math.PI / 3) * Math.pow((169.91 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[212] = 3.052132;
        asteroid_e1[212] = 0.138279;
        asteroid_e2[212] = 0.138089;
        asteroid_i1[212] = 15.9974;
        asteroid_i2[212] = 16.0078;
        asteroid_omega1[212] = 321.5518;
        asteroid_omega2[212] = 321.7586;
        asteroid_w1[212] = 205.6362;
        asteroid_w2[212] = 205.1774;
        asteroid_mean1[212] = 272.0573;
        asteroid_mean2[212] = 290.0842;
        asteroid_meanmotion[212] = 0.003225300;

        /*  Asteroid 663  */
        /*
        663a X 104.0 25 12 3 0.1181E-10 11
        663b C 3.062929 0.152184 17.8399 232.5419 311.5065 132.9657 0.003208328 47800.
        663c C 3.062929 0.152519 17.8269 232.7548 311.1459 154.5835 0.003208328 44000.
        */
        mu_planet[223] = (4 * Math.PI / 3) * Math.pow((100.88 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[213] = 3.062929;
        asteroid_e1[213] = 0.152184;
        asteroid_e2[213] = 0.152519;
        asteroid_i1[213] = 17.8399;
        asteroid_i2[213] = 17.8269;
        asteroid_omega1[213] = 232.5419;
        asteroid_omega2[213] = 232.7548;
        asteroid_w1[213] = 311.5065;
        asteroid_w2[213] = 311.1459;
        asteroid_mean1[213] = 132.9657;
        asteroid_mean2[213] = 154.5835;
        asteroid_meanmotion[213] = 0.003208328;

        /*  Asteroid 451  */
        /*
        451a C 230.0 25 12 3 0.3069E-10 11
        451b C 3.062349 0.072755 15.2255 88.9902 339.5369 273.7444 0.003209156 47800.
        451c C 3.062349 0.072862 15.2263 89.1640 339.1607 295.2369 0.003209156 44000.
        */
        mu_planet[224] = (4 * Math.PI / 3) * Math.pow((224.96 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[214] = 3.062349;
        asteroid_e1[214] = 0.072755;
        asteroid_e2[214] = 0.072862;
        asteroid_i1[214] = 15.2255;
        asteroid_i2[214] = 15.2263;
        asteroid_omega1[214] = 88.9902;
        asteroid_omega2[214] = 89.1640;
        asteroid_w1[214] = 339.5369;
        asteroid_w2[214] = 339.1607;
        asteroid_mean1[214] = 273.7444;
        asteroid_mean2[214] = 295.2369;
        asteroid_meanmotion[214] = 0.003209156;

        /*  Asteroid 895  */
        /*
        895a C 147.0 25 12 4 0.2894E-09 8 amp 0.30 radians
        895b C 3.214618 0.134295 25.9713 264.1401 181.7619 350.5177 0.002984054 47800.
        895c C 3.214618 0.134273 25.9733 264.2946 180.9848 61.4402 0.002984054 44000.
        */
        mu_planet[225] = (4 * Math.PI / 3) * Math.pow((141.90 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[215] = 3.214618;
        asteroid_e1[215] = 0.134295;
        asteroid_e2[215] = 0.134273;
        asteroid_i1[215] = 25.9713;
        asteroid_i2[215] = 25.9733;
        asteroid_omega1[215] = 264.1401;
        asteroid_omega2[215] = 264.2946;
        asteroid_w1[215] = 181.7619;
        asteroid_w2[215] = 180.9848;
        asteroid_mean1[215] = 350.5177;
        asteroid_mean2[215] = 61.4402;
        asteroid_meanmotion[215] = 0.002984054;

        /*  Asteroid 423  */
        /*
        423a C 217.0 25 12 3 -0.4595E-11 11
        423b C 3.068285 0.035136 11.2386 69.2317 205.8506 234.2793 0.003199768 47800.
        423c C 3.068285 0.035000 11.2410 69.4177 205.3552 257.9227 0.003199768 44000.
        */
        mu_planet[226] = (4 * Math.PI / 3) * Math.pow((208.77 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[216] = 3.068285;
        asteroid_e1[216] = 0.035136;
        asteroid_e2[216] = 0.035000;
        asteroid_i1[216] = 11.2386;
        asteroid_i2[216] = 11.2410;
        asteroid_omega1[216] = 69.2317;
        asteroid_omega2[216] = 69.4177;
        asteroid_w1[216] = 205.8506;
        asteroid_w2[216] = 205.3552;
        asteroid_mean1[216] = 234.2793;
        asteroid_mean2[216] = 257.9227;
        asteroid_meanmotion[216] = 0.003199768;

        /*  Asteroid 95  */
        /*
        95a C 145.0 25 12 3 0.2481E-10 11
        95b C 3.068931 0.148629 12.9709 242.9407 152.3204 255.8614 0.003198809 47800.
        95c C 3.068931 0.148795 12.9646 243.1519 151.9185 279.5948 0.003198809 44000.
        */
        mu_planet[227] = (4 * Math.PI / 3) * Math.pow((136.04 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[217] = 3.068931;
        asteroid_e1[217] = 0.148629;
        asteroid_e2[217] = 0.148795;
        asteroid_i1[217] = 12.9709;
        asteroid_i2[217] = 12.9646;
        asteroid_omega1[217] = 242.9407;
        asteroid_omega2[217] = 243.1519;
        asteroid_w1[217] = 152.3204;
        asteroid_w2[217] = 151.9185;
        asteroid_mean1[217] = 255.8614;
        asteroid_mean2[217] = 279.5948;
        asteroid_meanmotion[217] = 0.003198809;

        /*  Asteroid 49  */
        /*
        49a C 154.0 25 12 3 -0.1382E-12 11
        49b C 3.089143 0.230652 3.1789 285.6985 110.8977 79.6040 0.003167167 47800.
        49c C 3.089143 0.230747 3.1756 286.1889 110.1557 110.2874 0.003167167 44000.
        */
        mu_planet[228] = (4 * Math.PI / 3) * Math.pow((149.80 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[218] = 3.089143;
        asteroid_e1[218] = 0.230652;
        asteroid_e2[218] = 0.230747;
        asteroid_i1[218] = 3.1789;
        asteroid_i2[218] = 3.1756;
        asteroid_omega1[218] = 285.6985;
        asteroid_omega2[218] = 286.1889;
        asteroid_w1[218] = 110.8977;
        asteroid_w2[218] = 110.1557;
        asteroid_mean1[218] = 79.6040;
        asteroid_mean2[218] = 110.2874;
        asteroid_meanmotion[218] = 0.003167167;

        /*  Asteroid 602  */
        /*
        602a C 130.0 25 12 3 -0.6455E-11 11
        602b C 3.088113 0.245811 15.1707 331.6532 43.6364 260.8502 0.003169041 47800.
        602c C 3.088113 0.245479 15.1911 331.9004 43.2377 291.0255 0.003169041 44000.
        */
        mu_planet[229] = (4 * Math.PI / 3) * Math.pow((124.72 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[219] = 3.088113;
        asteroid_e1[219] = 0.245811;
        asteroid_e2[219] = 0.245479;
        asteroid_i1[219] = 15.1707;
        asteroid_i2[219] = 15.1911;
        asteroid_omega1[219] = 331.6532;
        asteroid_omega2[219] = 331.9004;
        asteroid_w1[219] = 43.6364;
        asteroid_w2[219] = 43.2377;
        asteroid_mean1[219] = 260.8502;
        asteroid_mean2[219] = 291.0255;
        asteroid_meanmotion[219] = 0.003169041;

        /*  Asteroid 52  */
        /*
        52a C 312.0 25 12 3 0.2194E-11 11
        52b C 3.096648 0.107285 7.4677 128.6235 339.4319 89.2242 0.003155772 47800.
        52c C 3.096648 0.107471 7.4643 128.8179 338.9247 122.4497 0.003155772 44000.
        */
        mu_planet[230] = (4 * Math.PI / 3) * Math.pow((302.51 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[220] = 3.096648;
        asteroid_e1[220] = 0.107285;
        asteroid_e2[220] = 0.107471;
        asteroid_i1[220] = 7.4677;
        asteroid_i2[220] = 7.4643;
        asteroid_omega1[220] = 128.6235;
        asteroid_omega2[220] = 128.8179;
        asteroid_w1[220] = 339.4319;
        asteroid_w2[220] = 338.9247;
        asteroid_mean1[220] = 89.2242;
        asteroid_mean2[220] = 122.4497;
        asteroid_meanmotion[220] = 0.003155772;

        /*  Asteroid 159  */
        /*
        159a C 131.0 25 12 3 0.7081E-11 11
        159b C 3.104572 0.105110 6.1228 133.8914 335.8886 243.8673 0.003143675 47800.
        159c C 3.104572 0.105299 6.1188 134.0855 335.3687 279.7399 0.003143675 44000.
        */
        mu_planet[231] = (4 * Math.PI / 3) * Math.pow((124.96 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[221] = 3.104572;
        asteroid_e1[221] = 0.105110;
        asteroid_e2[221] = 0.105299;
        asteroid_i1[221] = 6.1228;
        asteroid_i2[221] = 6.1188;
        asteroid_omega1[221] = 133.8914;
        asteroid_omega2[221] = 134.0855;
        asteroid_w1[221] = 335.8886;
        asteroid_w2[221] = 335.3687;
        asteroid_mean1[221] = 243.8673;
        asteroid_mean2[221] = 279.7399;
        asteroid_meanmotion[221] = 0.003143675;

        /*  Asteroid 357  */
        /*
        357a CX 110.0 25 12 3 0.8572E-10 8
        357b C 3.148149 0.082424 15.0756 137.7173 249.4430 220.6609 0.003078821 47800.
        357c C 3.148149 0.082391 15.0736 137.9098 249.1050 270.4734 0.003078821 44000.
        */
        mu_planet[232] = (4 * Math.PI / 3) * Math.pow((106.10 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[222] = 3.148149;
        asteroid_e1[222] = 0.082424;
        asteroid_e2[222] = 0.082391;
        asteroid_i1[222] = 15.0756;
        asteroid_i2[222] = 15.0736;
        asteroid_omega1[222] = 137.7173;
        asteroid_omega2[222] = 137.9098;
        asteroid_w1[222] = 249.4430;
        asteroid_w2[222] = 249.1050;
        asteroid_mean1[222] = 220.6609;
        asteroid_mean2[222] = 270.4734;
        asteroid_meanmotion[222] = 0.003078821;

        /*  Asteroid 545  */
        /*
        545a CD 115.0 25 12 4 -0.4463E-09 10 amp 0.20 radians
        545b C 3.184437 0.185381 11.2022 333.5047 326.3417 52.9440 0.003026131 47800.
        545c C 3.184437 0.185433 11.1944 333.7977 325.5960 114.5357 0.003026131 44000.
        */
        mu_planet[233] = (4 * Math.PI / 3) * Math.pow((111.30 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[223] = 3.184437;
        asteroid_e1[223] = 0.185381;
        asteroid_e2[223] = 0.185433;
        asteroid_i1[223] = 11.2022;
        asteroid_i2[223] = 11.1944;
        asteroid_omega1[223] = 333.5047;
        asteroid_omega2[223] = 333.7977;
        asteroid_w1[223] = 326.3417;
        asteroid_w2[223] = 325.5960;
        asteroid_mean1[223] = 52.9440;
        asteroid_mean2[223] = 114.5357;
        asteroid_meanmotion[223] = 0.003026131;

        /*  Asteroid 469  */
        /*
        469a X 129.0 25 12 4 -0.5602E-12 12 amp 0.14 radians
        469b C 3.162871 0.173380 11.6522 333.6415 209.6105 232.1978 0.003057102 47800.
        469c C 3.162871 0.173129 11.6732 333.9399 208.8392 287.0664 0.003057102 44000.
        */
        mu_planet[234] = (4 * Math.PI / 3) * Math.pow((125.57 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[224] = 3.162871;
        asteroid_e1[224] = 0.173380;
        asteroid_e2[224] = 0.173129;
        asteroid_i1[224] = 11.6522;
        asteroid_i2[224] = 11.6732;
        asteroid_omega1[224] = 333.6415;
        asteroid_omega2[224] = 333.9399;
        asteroid_w1[224] = 209.6105;
        asteroid_w2[224] = 208.8392;
        asteroid_mean1[224] = 232.1978;
        asteroid_mean2[224] = 287.0664;
        asteroid_meanmotion[224] = 0.003057102;

        /*  Asteroid 373  */
        /*
        373a C 99.6 25 12 3 -0.3202E-10 8
        373b C 3.116634 0.143301 15.4214 3.5486 349.5320 167.7913 0.003125644 47800.
        373c C 3.116634 0.143316 15.4243 3.7414 349.0914 207.5115 0.003125644 44000.
        */
        mu_planet[235] = (4 * Math.PI / 3) * Math.pow((95.77 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[225] = 3.116634;
        asteroid_e1[225] = 0.143301;
        asteroid_e2[225] = 0.143316;
        asteroid_i1[225] = 15.4214;
        asteroid_i2[225] = 15.4243;
        asteroid_omega1[225] = 3.5486;
        asteroid_omega2[225] = 3.7414;
        asteroid_w1[225] = 349.5320;
        asteroid_w2[225] = 349.0914;
        asteroid_mean1[225] = 167.7913;
        asteroid_mean2[225] = 207.5115;
        asteroid_meanmotion[225] = 0.003125644;

        /*  Asteroid 48  */
        /*
        48a C 225.0 25 12 3 0.1648E-09 8
        48b C 3.112389 0.067899 6.5472 183.3872 256.9388 285.6652 0.003131901 47800.
        48c C 3.112389 0.068031 6.5420 183.6181 256.4776 324.0056 0.003131901 44000.
        */
        mu_planet[236] = (4 * Math.PI / 3) * Math.pow((221.81 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[226] = 3.112389;
        asteroid_e1[226] = 0.067899;
        asteroid_e2[226] = 0.068031;
        asteroid_i1[226] = 6.5472;
        asteroid_i2[226] = 6.5420;
        asteroid_omega1[226] = 183.3872;
        asteroid_omega2[226] = 183.6181;
        asteroid_w1[226] = 256.9388;
        asteroid_w2[226] = 256.4776;
        asteroid_mean1[226] = 285.6652;
        asteroid_mean2[226] = 324.0056;
        asteroid_meanmotion[226] = 0.003131901;

        /*  Asteroid 137  */
        /*
        137a C 150.0 25 12 3 0.6464E-10 9
        137b C 3.118674 0.217235 13.4292 201.9622 108.0016 262.8672 0.003122536 47800.
        137c C 3.118674 0.217231 13.4190 202.2689 107.5481 303.1630 0.003122536 44000.
        */
        mu_planet[237] = (4 * Math.PI / 3) * Math.pow((145.42 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[227] = 3.118674;
        asteroid_e1[227] = 0.217235;
        asteroid_e2[227] = 0.217231;
        asteroid_i1[227] = 13.4292;
        asteroid_i2[227] = 13.4190;
        asteroid_omega1[227] = 201.9622;
        asteroid_omega2[227] = 202.2689;
        asteroid_w1[227] = 108.0016;
        asteroid_w2[227] = 107.5481;
        asteroid_mean1[227] = 262.8672;
        asteroid_mean2[227] = 303.1630;
        asteroid_meanmotion[227] = 0.003122536;

        /*  Asteroid 86  */
        /*
        86a C 127.0 25 12 3 -0.3868E-10 11
        86b C 3.107710 0.214117 4.8078 86.4493 305.2056 215.9485 0.003138812 47800.
        86c C 3.107710 0.214196 4.8056 86.6760 304.7068 252.8259 0.003138812 44000.
        */
        mu_planet[238] = (4 * Math.PI / 3) * Math.pow((120.56 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[228] = 3.107710;
        asteroid_e1[228] = 0.214117;
        asteroid_e2[228] = 0.214196;
        asteroid_i1[228] = 4.8078;
        asteroid_i2[228] = 4.8056;
        asteroid_omega1[228] = 86.4493;
        asteroid_omega2[228] = 86.6760;
        asteroid_w1[228] = 305.2056;
        asteroid_w2[228] = 304.7068;
        asteroid_mean1[228] = 215.9485;
        asteroid_mean2[228] = 252.8259;
        asteroid_meanmotion[228] = 0.003138812;

        /*  Asteroid 121  */
        /*
        121a C 217.0 25 12 3 -0.5323E-10 10
        121b C 3.449865 0.133506 7.5763 73.6571 291.3994 1.4626 0.002683058 47800.
        121c C 3.449865 0.133523 7.5777 74.0297 290.7175 137.6059 0.002683058 44000.
        */
        mu_planet[239] = (4 * Math.PI / 3) * Math.pow((208.99 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[229] = 3.449865;
        asteroid_e1[229] = 0.133506;
        asteroid_e2[229] = 0.133523;
        asteroid_i1[229] = 7.5763;
        asteroid_i2[229] = 7.5777;
        asteroid_omega1[229] = 73.6571;
        asteroid_omega2[229] = 74.0297;
        asteroid_w1[229] = 291.3994;
        asteroid_w2[229] = 290.7175;
        asteroid_mean1[229] = 1.4626;
        asteroid_mean2[229] = 137.6059;
        asteroid_meanmotion[229] = 0.002683058;

        /*  Asteroid 196  */
        /*
        196a S 146.0 25 12 3 -0.9027E-11 8
        196b S 3.114069 0.019683 7.2605 72.2262 218.1675 296.0197 0.003129365 47800.
        196c S 3.114069 0.019523 7.2628 72.4167 217.7039 334.9550 0.003129365 44000.
        */
        mu_planet[240] = (4 * Math.PI / 3) * Math.pow((136.34 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[230] = 3.114069;
        asteroid_e1[230] = 0.019683;
        asteroid_e2[230] = 0.019523;
        asteroid_i1[230] = 7.2605;
        asteroid_i2[230] = 7.2628;
        asteroid_omega1[230] = 72.2262;
        asteroid_omega2[230] = 72.4167;
        asteroid_w1[230] = 218.1675;
        asteroid_w2[230] = 217.7039;
        asteroid_mean1[230] = 296.0197;
        asteroid_mean2[230] = 334.9550;
        asteroid_meanmotion[230] = 0.003129365;

        /*  Asteroid 62  */
        /*
        62a BU 99.3 25 12 3 -0.3485E-11 8
        62b C 3.121660 0.178037 2.2282 125.1674 275.3792 110.2598 0.003117816 47800.
        62c C 3.121660 0.178121 2.2257 125.3220 274.9381 151.7232 0.003117816 44000.
        */
        mu_planet[241] = (4 * Math.PI / 3) * Math.pow((95.39 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[231] = 3.121660;
        asteroid_e1[231] = 0.178037;
        asteroid_e2[231] = 0.178121;
        asteroid_i1[231] = 2.2282;
        asteroid_i2[231] = 2.2257;
        asteroid_omega1[231] = 125.1674;
        asteroid_omega2[231] = 125.3220;
        asteroid_w1[231] = 275.3792;
        asteroid_w2[231] = 274.9381;
        asteroid_mean1[231] = 110.2598;
        asteroid_mean2[231] = 151.7232;
        asteroid_meanmotion[231] = 0.003117816;

        /*  Asteroid 90  */
        /*
        90a C 125.0 25 12 3 -0.2001E-09 10
        90b C 3.146137 0.166899 2.2272 70.2004 238.9490 48.7116 0.003081450 47800.
        90c C 3.146137 0.166710 2.2314 70.3662 238.4198 98.1695 0.003081450 44000.
        */
        mu_planet[242] = (4 * Math.PI / 3) * Math.pow((120.07 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[232] = 3.146137;
        asteroid_e1[232] = 0.166899;
        asteroid_e2[232] = 0.166710;
        asteroid_i1[232] = 2.2272;
        asteroid_i2[232] = 2.2314;
        asteroid_omega1[232] = 70.2004;
        asteroid_omega2[232] = 70.3662;
        asteroid_w1[232] = 238.9490;
        asteroid_w2[232] = 238.4198;
        asteroid_mean1[232] = 48.7116;
        asteroid_mean2[232] = 98.1695;
        asteroid_meanmotion[232] = 0.003081450;

        /*  Asteroid 791  */
        /*
        791a C 107.0 25 12 3 -0.1908E-10 11
        791b C 3.122448 0.194068 16.3689 129.6132 202.5099 187.8423 0.003116831 47800.
        791c C 3.122448 0.193832 16.3722 129.8038 202.0510 229.5017 0.003116831 44000.
        */
        mu_planet[243] = (4 * Math.PI / 3) * Math.pow((103.52 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[233] = 3.122448;
        asteroid_e1[233] = 0.194068;
        asteroid_e2[233] = 0.193832;
        asteroid_i1[233] = 16.3689;
        asteroid_i2[233] = 16.3722;
        asteroid_omega1[233] = 129.6132;
        asteroid_omega2[233] = 129.8038;
        asteroid_w1[233] = 202.5099;
        asteroid_w2[233] = 202.0510;
        asteroid_mean1[233] = 187.8423;
        asteroid_mean2[233] = 229.5017;
        asteroid_meanmotion[233] = 0.003116831;

        /*  Asteroid 120  */
        /*
        120a C 178.0 25 12 3 -0.3101E-10 9
        120b C 3.117128 0.058077 6.9566 341.0185 238.2833 94.3829 0.003124754 47800.
        120c C 3.117128 0.057978 6.9626 341.2803 237.6014 134.4692 0.003124754 44000.
        */
        mu_planet[244] = (4 * Math.PI / 3) * Math.pow((174.10 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[234] = 3.117128;
        asteroid_e1[234] = 0.058077;
        asteroid_e2[234] = 0.057978;
        asteroid_i1[234] = 6.9566;
        asteroid_i2[234] = 6.9626;
        asteroid_omega1[234] = 341.0185;
        asteroid_omega2[234] = 341.2803;
        asteroid_w1[234] = 238.2833;
        asteroid_w2[234] = 237.6014;
        asteroid_mean1[234] = 94.3829;
        asteroid_mean2[234] = 134.4692;
        asteroid_meanmotion[234] = 0.003124754;

        /*  Asteroid 683  */
        /*
        683a C? 116.0 25 12 3 -0.1554E-10 11
        683b C 3.116131 0.053949 18.5185 259.5934 276.8505 330.6127 0.003126558 47800.
        683c C 3.116131 0.054011 18.5165 259.7839 276.5825 9.9636 0.003126558 44000.
        */
        mu_planet[245] = (4 * Math.PI / 3) * Math.pow((81.98 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[235] = 3.116131;
        asteroid_e1[235] = 0.053949;
        asteroid_e2[235] = 0.054011;
        asteroid_i1[235] = 18.5185;
        asteroid_i2[235] = 18.5165;
        asteroid_omega1[235] = 259.5934;
        asteroid_omega2[235] = 259.7839;
        asteroid_w1[235] = 276.8505;
        asteroid_w2[235] = 276.5825;
        asteroid_mean1[235] = 330.6127;
        asteroid_mean2[235] = 9.9636;
        asteroid_meanmotion[235] = 0.003126558;

        /*  Asteroid 212  */
        /*
        212a CPD 140.0 25 12 3 0.1343E-09 8
        212b C 3.113027 0.111386 4.2709 313.2166 103.0462 66.5646 0.003130907 47800.
        212c C 3.113027 0.111513 4.2729 313.5291 102.4959 105.1291 0.003130907 44000.
        */
        mu_planet[246] = (4 * Math.PI / 3) * Math.pow((136.12 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[236] = 3.113027;
        asteroid_e1[236] = 0.111386;
        asteroid_e2[236] = 0.111513;
        asteroid_i1[236] = 4.2709;
        asteroid_i2[236] = 4.2729;
        asteroid_omega1[236] = 313.2166;
        asteroid_omega2[236] = 313.5291;
        asteroid_w1[236] = 103.0462;
        asteroid_w2[236] = 102.4959;
        asteroid_mean1[236] = 66.5646;
        asteroid_mean2[236] = 105.1291;
        asteroid_meanmotion[236] = 0.003130907;

        /*  Asteroid 283  */
        /*
        283a XPD 150.0 25 12 3 -0.5592E-11 9
        283b C 3.046071 0.149287 7.9926 304.0580 54.5980 108.2840 0.003234818 47800.
        283c C 3.046071 0.149192 7.9978 304.3183 54.1670 124.1571 0.003234818 44000.
        */
        mu_planet[247] = (4 * Math.PI / 3) * Math.pow((148.06 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[237] = 3.046071;
        asteroid_e1[237] = 0.149287;
        asteroid_e2[237] = 0.149192;
        asteroid_i1[237] = 7.9926;
        asteroid_i2[237] = 7.9978;
        asteroid_omega1[237] = 304.0580;
        asteroid_omega2[237] = 304.3183;
        asteroid_w1[237] = 54.5980;
        asteroid_w2[237] = 54.1670;
        asteroid_mean1[237] = 108.2840;
        asteroid_mean2[237] = 124.1571;
        asteroid_meanmotion[237] = 0.003234818;

        /*  Asteroid 691  */
        /*
        691a CPD 92.6 25 12 3 0.2129E-10 9
        691b C 3.011700 0.122093 13.0114 87.9391 301.6994 144.7895 0.003290445 47800.
        691c C 3.011700 0.122196 13.0103 88.1205 301.3859 148.5129 0.003290445 44000.
        */
        mu_planet[248] = (4 * Math.PI / 3) * Math.pow((87.68 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[238] = 3.011700;
        asteroid_e1[238] = 0.122093;
        asteroid_e2[238] = 0.122196;
        asteroid_i1[238] = 13.0114;
        asteroid_i2[238] = 13.0103;
        asteroid_omega1[238] = 87.9391;
        asteroid_omega2[238] = 88.1205;
        asteroid_w1[238] = 301.6994;
        asteroid_w2[238] = 301.3859;
        asteroid_mean1[238] = 144.7895;
        asteroid_mean2[238] = 148.5129;
        asteroid_meanmotion[238] = 0.003290445;

        /*  Asteroid 596  */
        /*
        596a CFP 117.0 25 12 3 0.6414E-11 12
        596b C 2.929826 0.162680 14.6497 70.4185 175.5512 174.7248 0.003429340 47800.
        596c C 2.929826 0.162630 14.6498 70.5855 175.1355 148.3241 0.003429340 44000.
        */
        mu_planet[249] = (4 * Math.PI / 3) * Math.pow((113.34 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[239] = 2.929826;
        asteroid_e1[239] = 0.162680;
        asteroid_e2[239] = 0.162630;
        asteroid_i1[239] = 14.6497;
        asteroid_i2[239] = 14.6498;
        asteroid_omega1[239] = 70.4185;
        asteroid_omega2[239] = 70.5855;
        asteroid_w1[239] = 175.5512;
        asteroid_w2[239] = 175.1355;
        asteroid_mean1[239] = 174.7248;
        asteroid_mean2[239] = 148.3241;
        asteroid_meanmotion[239] = 0.003429340;

        /*  Asteroid 709  */
        /*
        709a XPD 99.6 25 12 2 0.3438E-10 7
        709b C 2.914387 0.113939 16.2846 324.1604 17.2445 75.4139 0.003456811 47800.
        709c C 2.914387 0.113837 16.2881 324.3239 16.9305 42.9339 0.003456811 44000.
        */
        mu_planet[250] = (4 * Math.PI / 3) * Math.pow((96.56 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[240] = 2.914387;
        asteroid_e1[240] = 0.113939;
        asteroid_e2[240] = 0.113837;
        asteroid_i1[240] = 16.2846;
        asteroid_i2[240] = 16.2881;
        asteroid_omega1[240] = 324.1604;
        asteroid_omega2[240] = 324.3239;
        asteroid_w1[240] = 17.2445;
        asteroid_w2[240] = 16.9305;
        asteroid_mean1[240] = 75.4139;
        asteroid_mean2[240] = 42.9339;
        asteroid_meanmotion[240] = 0.003456811;

        /*  Asteroid 191  */
        /*
        191a CFPD 105.0 25 12 2 -0.3966E-10 7
        191b C 2.895442 0.088754 11.5093 159.0558 226.8289 165.9683 0.003490705 47800.
        191c C 2.895442 0.088734 11.5067 159.2205 226.5514 126.0710 0.003490705 44000.
        */
        mu_planet[251] = (4 * Math.PI / 3) * Math.pow((101.03 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[241] = 2.895442;
        asteroid_e1[241] = 0.088754;
        asteroid_e2[241] = 0.088734;
        asteroid_i1[241] = 11.5093;
        asteroid_i2[241] = 11.5067;
        asteroid_omega1[241] = 159.0558;
        asteroid_omega2[241] = 159.2205;
        asteroid_w1[241] = 226.8289;
        asteroid_w2[241] = 226.5514;
        asteroid_mean1[241] = 165.9683;
        asteroid_mean2[241] = 126.0710;
        asteroid_meanmotion[241] = 0.003490705;

        /*  Asteroid 74  */
        /*
        74a CPD 123.0 21 10 2 0.7299E-10 8
        74b C 2.779467 0.237999 4.0603 196.9659 173.6056 138.2477 0.003711404 47800.
        74c C 2.779467 0.238009 4.0541 197.1389 173.2810 50.3377 0.003711404 44000.
        */
        mu_planet[252] = (4 * Math.PI / 3) * Math.pow((118.71 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[242] = 2.779467;
        asteroid_e1[242] = 0.237999;
        asteroid_e2[242] = 0.238009;
        asteroid_i1[242] = 4.0603;
        asteroid_i2[242] = 4.0541;
        asteroid_omega1[242] = 196.9659;
        asteroid_omega2[242] = 197.1389;
        asteroid_w1[242] = 173.6056;
        asteroid_w2[242] = 173.2810;
        asteroid_mean1[242] = 138.2477;
        asteroid_mean2[242] = 50.3377;
        asteroid_meanmotion[242] = 0.003711404;

        /*  Asteroid 203  */
        /*
        203a CPD 120.0 21 10 2 0.3500E-10 6
        203b C 2.737144 0.059746 3.1806 347.6162 58.0045 73.7021 0.003797955 47800.
        203c C 2.737144 0.059793 3.1840 347.8005 57.7305 326.8860 0.003797955 44000.
        */
        mu_planet[253] = (4 * Math.PI / 3) * Math.pow((116.26 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[243] = 2.737144;
        asteroid_e1[243] = 0.059746;
        asteroid_e2[243] = 0.059793;
        asteroid_i1[243] = 3.1806;
        asteroid_i2[243] = 3.1840;
        asteroid_omega1[243] = 347.6162;
        asteroid_omega2[243] = 347.8005;
        asteroid_w1[243] = 58.0045;
        asteroid_w2[243] = 57.7305;
        asteroid_mean1[243] = 73.7021;
        asteroid_mean2[243] = 326.8860;
        asteroid_meanmotion[243] = 0.003797955;

        /*  Asteroid 377  */
        /*
        377a CPD 94.5 23 11 2 0.2321E-10 6
        377b C 2.690509 0.076829 6.6785 209.7827 195.5605 245.0076 0.003897193 47800.
        377c C 2.690509 0.076867 6.6756 209.9385 195.2989 116.6011 0.003897193 44000.
        */
        mu_planet[254] = (4 * Math.PI / 3) * Math.pow((91.05 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[244] = 2.690509;
        asteroid_e1[244] = 0.076829;
        asteroid_e2[244] = 0.076867;
        asteroid_i1[244] = 6.6785;
        asteroid_i2[244] = 6.6756;
        asteroid_omega1[244] = 209.7827;
        asteroid_omega2[244] = 209.9385;
        asteroid_w1[244] = 195.5605;
        asteroid_w2[244] = 195.2989;
        asteroid_mean1[244] = 245.0076;
        asteroid_mean2[244] = 116.6011;
        asteroid_meanmotion[244] = 0.003897193;

        /*  Asteroid 112  */
        /*
        112a CPD 75.5 19 9 2 -0.2383E-10 5
        112b C 2.433929 0.127996 2.5995 323.2581 16.6135 146.0648 0.004529632 47800.
        112c C 2.433929 0.127966 2.6016 323.4145 16.3635 239.9490 0.004529632 44000.
        */
        mu_planet[255] = (4 * Math.PI / 3) * Math.pow((72.18 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[245] = 2.433929;
        asteroid_e1[245] = 0.127996;
        asteroid_e2[245] = 0.127966;
        asteroid_i1[245] = 2.5995;
        asteroid_i2[245] = 2.6016;
        asteroid_omega1[245] = 323.2581;
        asteroid_omega2[245] = 323.4145;
        asteroid_w1[245] = 16.6135;
        asteroid_w2[245] = 16.3635;
        asteroid_mean1[245] = 146.0648;
        asteroid_mean2[245] = 239.9490;
        asteroid_meanmotion[245] = 0.004529632;

        /*  Asteroid 165  */
        /*
        165a CT 160.0 25 12 3 -0.4311E-10 9
        165b C 3.128930 0.077366 11.2206 302.5033 345.2240 183.0546 0.003107185 47800.
        165c C 3.128930 0.077302 11.2209 302.7374 344.6836 226.8523 0.003107185 44000.
        */
        mu_planet[256] = (4 * Math.PI / 3) * Math.pow((155.17 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[246] = 3.128930;
        asteroid_e1[246] = 0.077366;
        asteroid_e2[246] = 0.077302;
        asteroid_i1[246] = 11.2206;
        asteroid_i2[246] = 11.2209;
        asteroid_omega1[246] = 302.5033;
        asteroid_omega2[246] = 302.7374;
        asteroid_w1[246] = 345.2240;
        asteroid_w2[246] = 344.6836;
        asteroid_mean1[246] = 183.0546;
        asteroid_mean2[246] = 226.8523;
        asteroid_meanmotion[246] = 0.003107185;

        /*  Asteroid 363  */
        /*
        363a CT 97.0 21 10 2 -0.4253E-10 6
        363b C 2.747409 0.071513 5.9515 64.5383 294.1070 234.2365 0.003776685 47800.
        363c C 2.747409 0.071494 5.9535 64.6702 293.8822 132.0548 0.003776685 44000.
        */
        mu_planet[257] = (4 * Math.PI / 3) * Math.pow((72.44 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[247] = 2.747409;
        asteroid_e1[247] = 0.071513;
        asteroid_e2[247] = 0.071494;
        asteroid_i1[247] = 5.9515;
        asteroid_i2[247] = 5.9535;
        asteroid_omega1[247] = 64.5383;
        asteroid_omega2[247] = 64.6702;
        asteroid_w1[247] = 294.1070;
        asteroid_w2[247] = 293.8822;
        asteroid_mean1[247] = 234.2365;
        asteroid_mean2[247] = 132.0548;
        asteroid_meanmotion[247] = 0.003776685;

        /*  Asteroid 276  */
        /*
        276a XC 127.0 25 12 3 0.3197E-10 11
        276b P 3.116210 0.067117 21.6366 210.8236 270.1665 242.9081 0.003126501 47800.
        276c P 3.116210 0.067190 21.6325 210.9929 269.9935 282.1977 0.003126501 44000.
        */
        mu_planet[258] = (4 * Math.PI / 3) * Math.pow((121.60 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[248] = 3.116210;
        asteroid_e1[248] = 0.067117;
        asteroid_e2[248] = 0.067190;
        asteroid_i1[248] = 21.6366;
        asteroid_i2[248] = 21.6325;
        asteroid_omega1[248] = 210.8236;
        asteroid_omega2[248] = 210.9929;
        asteroid_w1[248] = 270.1665;
        asteroid_w2[248] = 269.9935;
        asteroid_mean1[248] = 242.9081;
        asteroid_mean2[248] = 282.1977;
        asteroid_meanmotion[248] = 0.003126501;

        /*  Asteroid 739  */
        /*
        739a XP 110.0 21 10 2 0.1632E-11 10
        739b C 2.737449 0.142602 20.6987 136.3223 43.8228 305.7495 0.003797479 47800.
        739c C 2.737449 0.142414 20.7021 136.4574 43.5719 199.0632 0.003797479 44000.
        */
        mu_planet[259] = (4 * Math.PI / 3) * Math.pow((107.38 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[249] = 2.737449;
        asteroid_e1[249] = 0.142602;
        asteroid_e2[249] = 0.142414;
        asteroid_i1[249] = 20.6987;
        asteroid_i2[249] = 20.7021;
        asteroid_omega1[249] = 136.3223;
        asteroid_omega2[249] = 136.4574;
        asteroid_w1[249] = 43.8228;
        asteroid_w2[249] = 43.5719;
        asteroid_mean1[249] = 305.7495;
        asteroid_mean2[249] = 199.0632;
        asteroid_meanmotion[249] = 0.003797479;

        /*  Asteroid 1093  */
        /*
        1093a CPD 120.0 25 12 4 -0.1898E-10 11
        1093b C 3.139578 0.263891 25.2350 55.5244 251.0008 168.0964 0.003091751 47800.
        1093c C 3.139578 0.263475 25.2535 55.7355 250.8006 214.9372 0.003091751 44000.
        */
        mu_planet[260] = (4 * Math.PI / 3) * Math.pow((116.73 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[250] = 3.139578;
        asteroid_e1[250] = 0.263891;
        asteroid_e2[250] = 0.263475;
        asteroid_i1[250] = 25.2350;
        asteroid_i2[250] = 25.2535;
        asteroid_omega1[250] = 55.5244;
        asteroid_omega2[250] = 55.7355;
        asteroid_w1[250] = 251.0008;
        asteroid_w2[250] = 250.8006;
        asteroid_mean1[250] = 168.0964;
        asteroid_mean2[250] = 214.9372;
        asteroid_meanmotion[250] = 0.003091751;

        /*  Asteroid 268  */
        /*
        268a FC 142.0 25 12 3 -0.5509E-10 11
        268b F 3.096797 0.131563 2.4391 120.7699 66.3792 334.1989 0.003155409 47800.
        268c F 3.096797 0.131586 2.4377 120.9277 65.7985 7.6136 0.003155409 44000.
        Note: Use C density.
        */
        mu_planet[261] = (4 * Math.PI / 3) * Math.pow((139.89 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[251] = 3.096797;
        asteroid_e1[251] = 0.131563;
        asteroid_e2[251] = 0.131586;
        asteroid_i1[251] = 2.4391;
        asteroid_i2[251] = 2.4377;
        asteroid_omega1[251] = 120.7699;
        asteroid_omega2[251] = 120.9277;
        asteroid_w1[251] = 66.3792;
        asteroid_w2[251] = 65.7985;
        asteroid_mean1[251] = 334.1989;
        asteroid_mean2[251] = 7.6136;
        asteroid_meanmotion[251] = 0.003155409;

        /*  Asteroid 1021  */
        /*
        1021a FC 103.0 21 10 2 -0.2476E-11 8
        1021b F 2.737909 0.285783 15.8346 115.3231 286.1067 268.8210 0.003796424 47800.
        1021c F 2.737909 0.285910 15.8265 115.5251 285.8380 162.3152 0.003796424 44000.
        Note: Use C density.
        */
        mu_planet[262] = (4 * Math.PI / 3) * Math.pow((99.39 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[252] = 2.737909;
        asteroid_e1[252] = 0.285783;
        asteroid_e2[252] = 0.285910;
        asteroid_i1[252] = 15.8346;
        asteroid_i2[252] = 15.8265;
        asteroid_omega1[252] = 115.3231;
        asteroid_omega2[252] = 115.5251;
        asteroid_w1[252] = 286.1067;
        asteroid_w2[252] = 285.8380;
        asteroid_mean1[252] = 268.8210;
        asteroid_mean2[252] = 162.3152;
        asteroid_meanmotion[252] = 0.003796424;

        /*  Asteroid 210  */
        /*
        210a CF 90.0 21 10 2 0.8208E-12 6
        210b C 2.721987 0.123108 5.2638 32.3811 13.6663 164.1026 0.003829726 47800.
        210c C 2.721987 0.123147 5.2678 32.5221 13.3974 50.4073 0.003829726 44000.
        */
        mu_planet[263] = (4 * Math.PI / 3) * Math.pow((86.65 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[253] = 2.721987;
        asteroid_e1[253] = 0.123108;
        asteroid_e2[253] = 0.123147;
        asteroid_i1[253] = 5.2638;
        asteroid_i2[253] = 5.2678;
        asteroid_omega1[253] = 32.3811;
        asteroid_omega2[253] = 32.5221;
        asteroid_w1[253] = 13.6663;
        asteroid_w2[253] = 13.3974;
        asteroid_mean1[253] = 164.1026;
        asteroid_mean2[253] = 50.4073;
        asteroid_meanmotion[253] = 0.003829726;

        /*  Asteroid 505  */
        /*
        505a FC 115.0 23 11 2 -0.3211E-10 8
        505b F 2.685186 0.245246 9.8243 90.5418 336.0811 185.4545 0.003908700 47800.
        505c F 2.685186 0.245355 9.8213 90.6754 335.8040 54.5804 0.003908700 44000.
        Note: Use C density.
        */
        mu_planet[264] = (4 * Math.PI / 3) * Math.pow((115.0 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[254] = 2.685186;
        asteroid_e1[254] = 0.245246;
        asteroid_e2[254] = 0.245355;
        asteroid_i1[254] = 9.8243;
        asteroid_i2[254] = 9.8213;
        asteroid_omega1[254] = 90.5418;
        asteroid_omega2[254] = 90.6754;
        asteroid_w1[254] = 336.0811;
        asteroid_w2[254] = 335.8040;
        asteroid_mean1[254] = 185.4545;
        asteroid_mean2[254] = 54.5804;
        asteroid_meanmotion[254] = 0.003908700;

        /*  Asteroid 99  */
        /*
        99a CM 79.0 23 11 2 0.8969E-10 11
        99b C 2.663563 0.197014 13.8783 41.2501 194.7605 316.9394 0.003956469 47800.
        99c C 2.663563 0.196890 13.8849 41.3857 194.4544 175.6918 0.003956469 44000.
        */
        mu_planet[265] = (4 * Math.PI / 3) * Math.pow((71.93 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[255] = 2.663563;
        asteroid_e1[255] = 0.197014;
        asteroid_e2[255] = 0.196890;
        asteroid_i1[255] = 13.8783;
        asteroid_i2[255] = 13.8849;
        asteroid_omega1[255] = 41.2501;
        asteroid_omega2[255] = 41.3857;
        asteroid_w1[255] = 194.7605;
        asteroid_w2[255] = 194.4544;
        asteroid_mean1[255] = 316.9394;
        asteroid_mean2[255] = 175.6918;
        asteroid_meanmotion[255] = 0.003956469;

        /*  Asteroid 690  */
        /*
        690a CM 145.0 25 12 3 0.1294E-09 8
        690b C 3.149322 0.177363 11.2641 252.9468 116.2401 119.1817 0.003077025 47800.
        690c C 3.149322 0.177486 11.2561 253.2330 115.7506 169.4429 0.003077025 44000.
        */
        mu_planet[266] = (4 * Math.PI / 3) * Math.pow((135.04 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[256] = 3.149322;
        asteroid_e1[256] = 0.177363;
        asteroid_e2[256] = 0.177486;
        asteroid_i1[256] = 11.2641;
        asteroid_i2[256] = 11.2561;
        asteroid_omega1[256] = 252.9468;
        asteroid_omega2[256] = 253.2330;
        asteroid_w1[256] = 116.2401;
        asteroid_w2[256] = 115.7506;
        asteroid_mean1[256] = 119.1817;
        asteroid_mean2[256] = 169.4429;
        asteroid_meanmotion[256] = 0.003077025;

        /*  Asteroid 366  */
        /*
        366a l 98.1 25 12 3 -0.1989E-09 8
        366b l 3.142346 0.063881 10.5780 346.5419 326.2294 356.0078 0.003087288 47800.
        366c l 3.142346 0.063817 10.5817 346.7699 325.7552 44.0774 0.003087288 44000.
        Note: Use C density.
        */
        mu_planet[267] = (4 * Math.PI / 3) * Math.pow((93.75 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[257] = 3.142346;
        asteroid_e1[257] = 0.063881;
        asteroid_e2[257] = 0.063817;
        asteroid_i1[257] = 10.5780;
        asteroid_i2[257] = 10.5817;
        asteroid_omega1[257] = 346.5419;
        asteroid_omega2[257] = 346.7699;
        asteroid_w1[257] = 326.2294;
        asteroid_w2[257] = 325.7552;
        asteroid_mean1[257] = 356.0078;
        asteroid_mean2[257] = 44.0774;
        asteroid_meanmotion[257] = 0.003087288;

        /*  Asteroid 814  */
        /*
        814a CPD 116.0 25 12 3 0.1085E-08 10 amp 0.17 radians
        814b C 3.164730 0.301896 21.7774 88.5264 296.8893 77.2676 0.003054781 47800.
        814c C 3.164730 0.302473 21.7520 88.7973 296.5208 132.2661 0.003054781 44000.
        */
        mu_planet[268] = (4 * Math.PI / 3) * Math.pow((109.55 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[258] = 3.164730;
        asteroid_e1[258] = 0.301896;
        asteroid_e2[258] = 0.302473;
        asteroid_i1[258] = 21.7774;
        asteroid_i2[258] = 21.7520;
        asteroid_omega1[258] = 88.5264;
        asteroid_omega2[258] = 88.7973;
        asteroid_w1[258] = 296.8893;
        asteroid_w2[258] = 296.5208;
        asteroid_mean1[258] = 77.2676;
        asteroid_mean2[258] = 132.2661;
        asteroid_meanmotion[258] = 0.003054781;

        /*  Asteroid 788  */
        /*
        788a l 109.0 25 12 3 0.2545E-09 12
        788b l 3.127415 0.129783 14.3598 177.9362 43.3018 234.1390 0.003109408 47800.
        788c l 3.127415 0.129509 14.3621 178.1636 42.7402 277.4805 0.003109408 44000.
        Note: Use C density.
        */
        mu_planet[269] = (4 * Math.PI / 3) * Math.pow((103.68 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[259] = 3.127415;
        asteroid_e1[259] = 0.129783;
        asteroid_e2[259] = 0.129509;
        asteroid_i1[259] = 14.3598;
        asteroid_i2[259] = 14.3621;
        asteroid_omega1[259] = 177.9362;
        asteroid_omega2[259] = 178.1636;
        asteroid_w1[259] = 43.3018;
        asteroid_w2[259] = 42.7402;
        asteroid_mean1[259] = 234.1390;
        asteroid_mean2[259] = 277.4805;
        asteroid_meanmotion[259] = 0.003109408;

        /*  Asteroid 303  */
        /*
        303a l 103.0 25 12 3 0.1282E-09 8
        303b l 3.122070 0.063566 6.8830 343.7622 70.8708 38.5386 0.003117362 47800.
        303c l 3.122070 0.063664 6.8877 344.0095 70.4458 79.9920 0.003117362 44000.
        Note: Use C density.
        */
        mu_planet[270] = (4 * Math.PI / 3) * Math.pow((99.29 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[260] = 3.122070;
        asteroid_e1[260] = 0.063566;
        asteroid_e2[260] = 0.063664;
        asteroid_i1[260] = 6.8830;
        asteroid_i2[260] = 6.8877;
        asteroid_omega1[260] = 343.7622;
        asteroid_omega2[260] = 344.0095;
        asteroid_w1[260] = 70.8708;
        asteroid_w2[260] = 70.4458;
        asteroid_mean1[260] = 38.5386;
        asteroid_mean2[260] = 79.9920;
        asteroid_meanmotion[260] = 0.003117362;

        /*  Asteroid 780  */
        /*
        780a l 97.1 25 12 3 -0.3979E-10 8
        780b l 3.117006 0.090327 19.0755 144.6572 212.5651 33.2585 0.003125184 47800.
        780c l 3.117006 0.090200 19.0740 144.8270 212.2114 73.0151 0.003125184 44000.
        Note: Use C density.
        */
        mu_planet[271] = (4 * Math.PI / 3) * Math.pow((94.40 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[261] = 3.117006;
        asteroid_e1[261] = 0.090327;
        asteroid_e2[261] = 0.090200;
        asteroid_i1[261] = 19.0755;
        asteroid_i2[261] = 19.0740;
        asteroid_omega1[261] = 144.6572;
        asteroid_omega2[261] = 144.8270;
        asteroid_w1[261] = 212.5651;
        asteroid_w2[261] = 212.2114;
        asteroid_mean1[261] = 33.2585;
        asteroid_mean2[261] = 73.0151;
        asteroid_meanmotion[261] = 0.003125184;

        /*  Asteroid 259  */
        /*
        259a CPDF 185.0 25 12 3 0.4646E-10 11
        259b C 3.138490 0.122403 10.7885 87.0217 162.7980 213.2272 0.003092850 47800.
        259c C 3.138490 0.122333 10.7858 87.2359 162.1732 260.2502 0.003092850 44000.
        */
        mu_planet[272] = (4 * Math.PI / 3) * Math.pow((178.60 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[262] = 3.138490;
        asteroid_e1[262] = 0.122403;
        asteroid_e2[262] = 0.122333;
        asteroid_i1[262] = 10.7885;
        asteroid_i2[262] = 10.7858;
        asteroid_omega1[262] = 87.0217;
        asteroid_omega2[262] = 87.2359;
        asteroid_w1[262] = 162.7980;
        asteroid_w2[262] = 162.1732;
        asteroid_mean1[262] = 213.2272;
        asteroid_mean2[262] = 260.2502;
        asteroid_meanmotion[262] = 0.003092850;

        /*  Asteroid 489  */
        /*
        489a CPD 144.0 25 12 3 0.8039E-11 10
        489b C 3.153354 0.043179 12.9711 166.8202 357.2150 13.8240 0.003071140 47800.
        489c C 3.153354 0.043259 12.9669 167.0243 356.4453 65.7288 0.003071140 44000.
        */
        mu_planet[273] = (4 * Math.PI / 3) * Math.pow((139.40 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[263] = 3.153354;
        asteroid_e1[263] = 0.043179;
        asteroid_e2[263] = 0.043259;
        asteroid_i1[263] = 12.9711;
        asteroid_i2[263] = 12.9669;
        asteroid_omega1[263] = 166.8202;
        asteroid_omega2[263] = 167.0243;
        asteroid_w1[263] = 357.2150;
        asteroid_w2[263] = 356.4453;
        asteroid_mean1[263] = 13.8240;
        asteroid_mean2[263] = 65.7288;
        asteroid_meanmotion[263] = 0.003071140;

        /*  Asteroid 568  */
        /*
        568a l 89.7 25 12 2 0.5762E-10 12
        568b l 2.883618 0.166647 18.3729 249.5310 172.8795 353.3725 0.003512317 47800.
        568c l 2.883618 0.166740 18.3706 249.6837 172.5555 308.8284 0.003512317 44000.
        Note: Use C density.
        */
        mu_planet[274] = (4 * Math.PI / 3) * Math.pow((86.99 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[264] = 2.883618;
        asteroid_e1[264] = 0.166647;
        asteroid_e2[264] = 0.166740;
        asteroid_i1[264] = 18.3729;
        asteroid_i2[264] = 18.3706;
        asteroid_omega1[264] = 249.5310;
        asteroid_omega2[264] = 249.6837;
        asteroid_w1[264] = 172.8795;
        asteroid_w2[264] = 172.5555;
        asteroid_mean1[264] = 353.3725;
        asteroid_mean2[264] = 308.8284;
        asteroid_meanmotion[264] = 0.003512317;

        /*  Asteroid 358  */
        /*
        358a l 91.8 25 12 2 0.4924E-10 7
        358b l 2.877883 0.150961 3.5490 171.9701 251.9961 2.5216 0.003522597 47800.
        358c l 2.877883 0.151043 3.5454 172.1635 251.6307 315.7398 0.003522597 44000.
        Note: Use C density.
        */
        mu_planet[275] = (4 * Math.PI / 3) * Math.pow((89.45 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[265] = 2.877883;
        asteroid_e1[265] = 0.150961;
        asteroid_e2[265] = 0.151043;
        asteroid_i1[265] = 3.5490;
        asteroid_i2[265] = 3.5454;
        asteroid_omega1[265] = 171.9701;
        asteroid_omega2[265] = 172.1635;
        asteroid_w1[265] = 251.9961;
        asteroid_w2[265] = 251.6307;
        asteroid_mean1[265] = 2.5216;
        asteroid_mean2[265] = 315.7398;
        asteroid_meanmotion[265] = 0.003522597;

        /*  Asteroid 424  */
        /*
        424a l 90.5 21 10 2 -0.2687E-10 6
        424b l 2.773875 0.109414 8.2139 98.9305 331.9615 59.4581 0.003722730 47800.
        424c l 2.773875 0.109505 8.2134 99.0664 331.6797 329.0765 0.003722730 44000.
        Note: Use C density.
        */
        mu_planet[276] = (4 * Math.PI / 3) * Math.pow((87.20 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[266] = 2.773875;
        asteroid_e1[266] = 0.109414;
        asteroid_e2[266] = 0.109505;
        asteroid_i1[266] = 8.2139;
        asteroid_i2[266] = 8.2134;
        asteroid_omega1[266] = 98.9305;
        asteroid_omega2[266] = 99.0664;
        asteroid_w1[266] = 331.9615;
        asteroid_w2[266] = 331.6797;
        asteroid_mean1[266] = 59.4581;
        asteroid_mean2[266] = 329.0765;
        asteroid_meanmotion[266] = 0.003722730;

        /*  Asteroid 412  */
        /*
        412a l 93.3 21 10 2 0.2387E-10 6
        412b l 2.762968 0.042338 13.7752 106.1238 93.3657 56.2492 0.003744884 47800.
        412c l 2.762968 0.042333 13.7749 106.2638 93.0434 321.0804 0.003744884 44000.
        Note: Use C density.
        */
        mu_planet[277] = (4 * Math.PI / 3) * Math.pow((90.96 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[267] = 2.762968;
        asteroid_e1[267] = 0.042338;
        asteroid_e2[267] = 0.042333;
        asteroid_i1[267] = 13.7752;
        asteroid_i2[267] = 13.7749;
        asteroid_omega1[267] = 106.1238;
        asteroid_omega2[267] = 106.2638;
        asteroid_w1[267] = 93.3657;
        asteroid_w2[267] = 93.0434;
        asteroid_mean1[267] = 56.2492;
        asteroid_mean2[267] = 321.0804;
        asteroid_meanmotion[267] = 0.003744884;

        /*  Asteroid 762  */
        /*
        762a CF 142.0 25 12 3 -0.1753E-10 10
        762b C 3.156277 0.103428 13.0891 305.7326 182.0057 77.0854 0.003066877 47800.
        762c C 3.156277 0.103494 13.0938 305.9617 181.3572 129.7722 0.003066877 44000.
        */
        mu_planet[278] = (4 * Math.PI / 3) * Math.pow((137.09 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[268] = 3.156277;
        asteroid_e1[268] = 0.103428;
        asteroid_e2[268] = 0.103494;
        asteroid_i1[268] = 13.0891;
        asteroid_i2[268] = 13.0938;
        asteroid_omega1[268] = 305.7326;
        asteroid_omega2[268] = 305.9617;
        asteroid_w1[268] = 182.0057;
        asteroid_w2[268] = 181.3572;
        asteroid_mean1[268] = 77.0854;
        asteroid_mean2[268] = 129.7722;
        asteroid_meanmotion[268] = 0.003066877;

        /*  Asteroid 308  */
        /*
        308a T 148.0 21 10 2 -0.6373E-10 6
        308b T 2.749575 0.038406 4.3601 181.4466 112.5316 114.8946 0.003772213 47800.
        308c T 2.749575 0.038314 4.3565 181.6015 112.2379 13.7324 0.003772213 44000.
        Note: Use C density.
        */
        mu_planet[279] = (4 * Math.PI / 3) * Math.pow((140.69 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[269] = 2.749575;
        asteroid_e1[269] = 0.038406;
        asteroid_e2[269] = 0.038314;
        asteroid_i1[269] = 4.3601;
        asteroid_i2[269] = 4.3565;
        asteroid_omega1[269] = 181.4466;
        asteroid_omega2[269] = 181.6015;
        asteroid_w1[269] = 112.5316;
        asteroid_w2[269] = 112.2379;
        asteroid_mean1[269] = 114.8946;
        asteroid_mean2[269] = 13.7324;
        asteroid_meanmotion[269] = 0.003772213;

        /*  Asteroid 102  */
        /*
        102a P 86.0 23 11 2 -0.9824E-10 11
        102b P 2.661464 0.253054 5.1605 210.5598 146.4651 319.9977 0.003961094 47800.
        102c P 2.661464 0.253058 5.1527 210.7509 146.1451 177.7016 0.003961094 44000.
        Note: Use C density.
        */
        mu_planet[280] = (4 * Math.PI / 3) * Math.pow((83.0 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[270] = 2.661464;
        asteroid_e1[270] = 0.253054;
        asteroid_e2[270] = 0.253058;
        asteroid_i1[270] = 5.1605;
        asteroid_i2[270] = 5.1527;
        asteroid_omega1[270] = 210.5598;
        asteroid_omega2[270] = 210.7509;
        asteroid_w1[270] = 146.4651;
        asteroid_w2[270] = 146.1451;
        asteroid_mean1[270] = 319.9977;
        asteroid_mean2[270] = 177.7016;
        asteroid_meanmotion[270] = 0.003961094;

        /*  Asteroid 56  */
        /*
        56a P 117.0 23 11 2 -0.9466E-10 8
        56b P 2.597899 0.235154 8.0795 193.0193 103.5192 225.2050 0.004107447 47800.
        56c P 2.597899 0.235081 8.0750 193.2235 103.1969 51.0334 0.004107447 44000.
        Note: Use C density.
        */
        mu_planet[281] = (4 * Math.PI / 3) * Math.pow((113.24 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[271] = 2.597899;
        asteroid_e1[271] = 0.235154;
        asteroid_e2[271] = 0.235081;
        asteroid_i1[271] = 8.0795;
        asteroid_i2[271] = 8.0750;
        asteroid_omega1[271] = 193.0193;
        asteroid_omega2[271] = 193.2235;
        asteroid_w1[271] = 103.5192;
        asteroid_w2[271] = 103.1969;
        asteroid_mean1[271] = 225.2050;
        asteroid_mean2[271] = 51.0334;
        asteroid_meanmotion[271] = 0.004107447;

        /*  Asteroid 154  */
        /*
        154a l 192.0 25 12 4 0.1379E-10 12 amp 0.10 radians
        154b l 3.184101 0.090906 21.0905 36.4312 161.1034 221.5593 0.003026917 47800.
        154c l 3.184101 0.091075 21.0912 36.6095 160.4010 283.0511 0.003026917 44000.
        Note: Use C density.
        */
        mu_planet[282] = (4 * Math.PI / 3) * Math.pow((184.93 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[272] = 3.184101;
        asteroid_e1[272] = 0.090906;
        asteroid_e2[272] = 0.091075;
        asteroid_i1[272] = 21.0905;
        asteroid_i2[272] = 21.0912;
        asteroid_omega1[272] = 36.4312;
        asteroid_omega2[272] = 36.6095;
        asteroid_w1[272] = 161.1034;
        asteroid_w2[272] = 160.4010;
        asteroid_mean1[272] = 221.5593;
        asteroid_mean2[272] = 283.0511;
        asteroid_meanmotion[272] = 0.003026917;

        /*  Asteroid 83  */
        /*
        83a X 84.2 19 9 2 0.4494E-11 6
        83b l 2.431448 0.083399 4.9738 27.3206 167.4002 303.7211 0.004536566 47800.
        83c l 2.431448 0.083398 4.9764 27.4266 167.1411 36.1549 0.004536566 44000.
        Note: Use M density.
        */
        mu_planet[283] = (4 * Math.PI / 3) * Math.pow((81.37 / 2), 3) * 5.3E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[273] = 2.431448;
        asteroid_e1[273] = 0.083399;
        asteroid_e2[273] = 0.083398;
        asteroid_i1[273] = 4.9738;
        asteroid_i2[273] = 4.9764;
        asteroid_omega1[273] = 27.3206;
        asteroid_omega2[273] = 27.4266;
        asteroid_w1[273] = 167.4002;
        asteroid_w2[273] = 167.1411;
        asteroid_mean1[273] = 303.7211;
        asteroid_mean2[273] = 36.1549;
        asteroid_meanmotion[273] = 0.004536566;

        /*  Asteroid 328  */
        /*
        328a S 47.0 25 12 3 0.2114E-10 11
        328b S 3.106729 0.114078 16.1064 352.0612 104.1349 1.0798 0.003140666 47800.
        328c S 3.106729 0.114302 16.1075 352.2713 103.8022 37.4040 0.003140666 44000.
        */
        mu_planet[284] = (4 * Math.PI / 3) * Math.pow((122.92 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[274] = 3.106729;
        asteroid_e1[274] = 0.114078;
        asteroid_e2[274] = 0.114302;
        asteroid_i1[274] = 16.1064;
        asteroid_i2[274] = 16.1075;
        asteroid_omega1[274] = 352.0612;
        asteroid_omega2[274] = 352.2713;
        asteroid_w1[274] = 104.1349;
        asteroid_w2[274] = 103.8022;
        asteroid_mean1[274] = 1.0798;
        asteroid_mean2[274] = 37.4040;
        asteroid_meanmotion[274] = 0.003140666;

        /*  Asteroid 769  */
        /*
        769a l 102.0 25 12 5 -0.1514E-09 10 amp 0.19 radians
        769b l 3.182290 0.172720 7.4090 39.0941 249.3828 199.8215 0.003029090 47800.
        769c l 3.182290 0.172426 7.4213 39.4255 248.5799 260.7875 0.003029090 44000.
        Note: Use C density.
        */
        mu_planet[285] = (4 * Math.PI / 3) * Math.pow((106.44 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[275] = 3.182290;
        asteroid_e1[275] = 0.172720;
        asteroid_e2[275] = 0.172426;
        asteroid_i1[275] = 7.4090;
        asteroid_i2[275] = 7.4213;
        asteroid_omega1[275] = 39.0941;
        asteroid_omega2[275] = 39.4255;
        asteroid_w1[275] = 249.3828;
        asteroid_w2[275] = 248.5799;
        asteroid_mean1[275] = 199.8215;
        asteroid_mean2[275] = 260.7875;
        asteroid_meanmotion[275] = 0.003029090;

        /*  Asteroid 181  */
        /*
        181a S 107.0 25 12 3 0.3986E-09 12
        181b S 3.127583 0.208255 18.7892 143.2954 315.5179 92.9843 0.003109238 47800.
        181c S 3.127583 0.208777 18.7697 143.5116 315.1086 136.2219 0.003109238 44000.
        Note: Use S density.
        */
        mu_planet[286] = (4 * Math.PI / 3) * Math.pow((106.0 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[276] = 3.127583;
        asteroid_e1[276] = 0.208255;
        asteroid_e2[276] = 0.208777;
        asteroid_i1[276] = 18.7892;
        asteroid_i2[276] = 18.7697;
        asteroid_omega1[276] = 143.2954;
        asteroid_omega2[276] = 143.5116;
        asteroid_w1[276] = 315.5179;
        asteroid_w2[276] = 315.1086;
        asteroid_mean1[276] = 92.9843;
        asteroid_mean2[276] = 136.2219;
        asteroid_meanmotion[276] = 0.003109238;

        /*  Asteroid 221  */
        /*
        221a S 110.0 25 12 3 -0.3992E-10 9
        221b S 3.012371 0.100804 10.8775 141.6156 193.3278 19.1768 0.003289309 47800.
        221c S 3.012371 0.100728 10.8745 141.7929 192.9640 23.2018 0.003289309 44000.
        Note: Use S density.
        */
        mu_planet[287] = (4 * Math.PI / 3) * Math.pow((103.87 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[277] = 3.012371;
        asteroid_e1[277] = 0.100804;
        asteroid_e2[277] = 0.100728;
        asteroid_i1[277] = 10.8775;
        asteroid_i2[277] = 10.8745;
        asteroid_omega1[277] = 141.6156;
        asteroid_omega2[277] = 141.7929;
        asteroid_w1[277] = 193.3278;
        asteroid_w2[277] = 192.9640;
        asteroid_mean1[277] = 19.1768;
        asteroid_mean2[277] = 23.2018;
        asteroid_meanmotion[277] = 0.003289309;

        /*  Asteroid 491  */
        /*
        491a l 101.0 25 12 4 0.1374E-09 8
        491b l 3.194166 0.076199 18.8761 175.0889 237.7399 36.0534 0.003012637 47800.
        491c l 3.194166 0.076120 18.8735 175.2715 237.3360 100.3515 0.003012637 44000.
        Note: Use C density.
        */
        mu_planet[288] = (4 * Math.PI / 3) * Math.pow((97.29 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[278] = 3.194166;
        asteroid_e1[278] = 0.076199;
        asteroid_e2[278] = 0.076120;
        asteroid_i1[278] = 18.8761;
        asteroid_i2[278] = 18.8735;
        asteroid_omega1[278] = 175.0889;
        asteroid_omega2[278] = 175.2715;
        asteroid_w1[278] = 237.7399;
        asteroid_w2[278] = 237.3360;
        asteroid_mean1[278] = 36.0534;
        asteroid_mean2[278] = 100.3515;
        asteroid_meanmotion[278] = 0.003012637;

        /*  Asteroid 349  */
        /*
        349a R 143.0 25 12 2 -0.6220E-10 7
        349b R 2.924746 0.088765 8.2529 32.1497 345.1530 176.9808 0.003438303 47800.
        349c R 2.924746 0.088776 8.2567 32.3218 344.8422 148.5186 0.003438303 44000.
        Note: Use S density.
        */
        mu_planet[289] = (4 * Math.PI / 3) * Math.pow((139.77 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[279] = 2.924746;
        asteroid_e1[279] = 0.088765;
        asteroid_e2[279] = 0.088776;
        asteroid_i1[279] = 8.2529;
        asteroid_i2[279] = 8.2567;
        asteroid_omega1[279] = 32.1497;
        asteroid_omega2[279] = 32.3218;
        asteroid_w1[279] = 345.1530;
        asteroid_w2[279] = 344.8422;
        asteroid_mean1[279] = 176.9808;
        asteroid_mean2[279] = 148.5186;
        asteroid_meanmotion[279] = 0.003438303;

        /*  Asteroid 148  */
        /*
        148a G 104.0 21 10 2 -0.3709E-10 5
        148b G 2.771288 0.186139 25.3112 144.7082 252.1076 217.5770 0.003728246 47800.
        148c G 2.771288 0.185982 25.3127 144.8378 251.9889 125.8376 0.003728246 44000.
        Note: Use C density.
        */
        mu_planet[290] = (4 * Math.PI / 3) * Math.pow((97.73 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[280] = 2.771288;
        asteroid_e1[280] = 0.186139;
        asteroid_e2[280] = 0.185982;
        asteroid_i1[280] = 25.3112;
        asteroid_i2[280] = 25.3127;
        asteroid_omega1[280] = 144.7082;
        asteroid_omega2[280] = 144.8378;
        asteroid_w1[280] = 252.1076;
        asteroid_w2[280] = 251.9889;
        asteroid_mean1[280] = 217.5770;
        asteroid_mean2[280] = 125.8376;
        asteroid_meanmotion[280] = 0.003728246;

        /*  Asteroid 704  */
        /*
        704a F 333.0 25 12 3 -0.2354E-11 9
        704b F 3.060959 0.150914 17.3017 280.1986 93.5841 274.7367 0.003211475 47800.
        704c F 3.060959 0.150942 17.3012 280.4040 93.3429 295.5576 0.003211475 44000.
        Note: Use C density.
        */
        mu_planet[291] = (4 * Math.PI / 3) * Math.pow((316.62 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[281] = 3.060959;
        asteroid_e1[281] = 0.150914;
        asteroid_e2[281] = 0.150942;
        asteroid_i1[281] = 17.3017;
        asteroid_i2[281] = 17.3012;
        asteroid_omega1[281] = 280.1986;
        asteroid_omega2[281] = 280.4040;
        asteroid_w1[281] = 93.5841;
        asteroid_w2[281] = 93.3429;
        asteroid_mean1[281] = 274.7367;
        asteroid_mean2[281] = 295.5576;
        asteroid_meanmotion[281] = 0.003211475;

        /*  Asteroid 595  */
        /*
        595a l 114.0 25 12 4 -0.1097E-09 10
        595b l 3.203090 0.070314 17.8624 24.0383 264.7368 127.6318 0.003000032 47800.
        595c l 3.203090 0.070137 17.8681 24.2317 264.2845 194.7120 0.003000032 44000.
        Note: Use C density.
        */
        mu_planet[292] = (4 * Math.PI / 3) * Math.pow((109.07 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[282] = 3.203090;
        asteroid_e1[282] = 0.070314;
        asteroid_e2[282] = 0.070137;
        asteroid_i1[282] = 17.8624;
        asteroid_i2[282] = 17.8681;
        asteroid_omega1[282] = 24.0383;
        asteroid_omega2[282] = 24.2317;
        asteroid_w1[282] = 264.7368;
        asteroid_w2[282] = 264.2845;
        asteroid_mean1[282] = 127.6318;
        asteroid_mean2[282] = 194.7120;
        asteroid_meanmotion[282] = 0.003000032;

        /*  Asteroid 213  */
        /*
        213a F 84.6 21 10 2 0.8755E-10 8
        213b F 2.752808 0.145687 6.8050 121.7564 162.1539 244.8247 0.003765526 47800.
        213c F 2.752808 0.145610 6.8020 121.8954 161.8436 145.1507 0.003765526 44000.
        Note: Use C density.
        */
        mu_planet[293] = (4 * Math.PI / 3) * Math.pow((83.01 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[283] = 2.752808;
        asteroid_e1[283] = 0.145687;
        asteroid_e2[283] = 0.145610;
        asteroid_i1[283] = 6.8050;
        asteroid_i2[283] = 6.8020;
        asteroid_omega1[283] = 121.7564;
        asteroid_omega2[283] = 121.8954;
        asteroid_w1[283] = 162.1539;
        asteroid_w2[283] = 161.8436;
        asteroid_mean1[283] = 244.8247;
        asteroid_mean2[283] = 145.1507;
        asteroid_meanmotion[283] = 0.003765526;

        /*  Asteroid 419  */
        /*
        419a F 133.0 25 12 3 0.1625E-11 11
        419b F 2.594105 0.253961 3.9400 229.0779 43.1822 146.6486 0.004116377 47800.
        419c F 2.594105 0.253852 3.9404 229.2821 42.8205 330.5722 0.004116377 44000.
        Note: Use C density.
        */
        mu_planet[294] = (4 * Math.PI / 3) * Math.pow((129.01 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[284] = 2.594105;
        asteroid_e1[284] = 0.253961;
        asteroid_e2[284] = 0.253852;
        asteroid_i1[284] = 3.9400;
        asteroid_i2[284] = 3.9404;
        asteroid_omega1[284] = 229.0779;
        asteroid_omega2[284] = 229.2821;
        asteroid_w1[284] = 43.1822;
        asteroid_w2[284] = 42.8205;
        asteroid_mean1[284] = 146.6486;
        asteroid_mean2[284] = 330.5722;
        asteroid_meanmotion[284] = 0.004116377;

        /*  Asteroid 335  */
        /*
        335a F 93.6 19 9 3 0.3196E-10 9 amp 0.14 radians
        335b F 2.475089 0.175210 5.1143 147.8270 141.1683 26.2078 0.004417055 47800.
        335c F 2.475089 0.175143 5.1104 147.9467 140.9108 144.6469 0.004417055 44000.
        Note: Use C density.
        */
        mu_planet[295] = (4 * Math.PI / 3) * Math.pow((89.07 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[285] = 2.475089;
        asteroid_e1[285] = 0.175210;
        asteroid_e2[285] = 0.175143;
        asteroid_i1[285] = 5.1143;
        asteroid_i2[285] = 5.1104;
        asteroid_omega1[285] = 147.8270;
        asteroid_omega2[285] = 147.9467;
        asteroid_w1[285] = 141.1683;
        asteroid_w2[285] = 140.9108;
        asteroid_mean1[285] = 26.2078;
        asteroid_mean2[285] = 144.6469;
        asteroid_meanmotion[285] = 0.004417055;

        /*  Asteroid 114  */
        /*
        114a T 103.0 23 11 2 -0.8488E-11 11
        114b T 2.676692 0.137633 4.9381 163.8909 351.7829 151.7673 0.003927339 47800.
        114c T 2.676692 0.137698 4.9341 164.0267 351.4548 16.8836 0.003927339 44000.
        Note: Use C density.
        */
        mu_planet[296] = (4 * Math.PI / 3) * Math.pow((99.64 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[286] = 2.676692;
        asteroid_e1[286] = 0.137633;
        asteroid_e2[286] = 0.137698;
        asteroid_i1[286] = 4.9381;
        asteroid_i2[286] = 4.9341;
        asteroid_omega1[286] = 163.8909;
        asteroid_omega2[286] = 164.0267;
        asteroid_w1[286] = 351.7829;
        asteroid_w2[286] = 351.4548;
        asteroid_mean1[286] = 151.7673;
        asteroid_mean2[286] = 16.8836;
        asteroid_meanmotion[286] = 0.003927339;

        /*  Asteroid 233  */
        /*
        233a T 108.0 23 11 2 -0.6559E-11 6
        233b T 2.660136 0.100533 7.6762 221.7329 125.0060 72.4481 0.003964168 47800.
        233c T 2.660136 0.100515 7.6730 221.8923 124.7526 289.4478 0.003964168 44000.
        Note: Use C density.
        */
        mu_planet[297] = (4 * Math.PI / 3) * Math.pow((102.78 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[287] = 2.660136;
        asteroid_e1[287] = 0.100533;
        asteroid_e2[287] = 0.100515;
        asteroid_i1[287] = 7.6762;
        asteroid_i2[287] = 7.6730;
        asteroid_omega1[287] = 221.7329;
        asteroid_omega2[287] = 221.8923;
        asteroid_w1[287] = 125.0060;
        asteroid_w2[287] = 124.7526;
        asteroid_mean1[287] = 72.4481;
        asteroid_mean2[287] = 289.4478;
        asteroid_meanmotion[287] = 0.003964168;

        /*  Asteroid 498  */
        /*
        498a M 84.8 23 11 3 0.4882E-11 11
        498b M 2.650721 0.223635 9.5195 97.1072 240.4030 100.9035 0.003985255 47800.
        498c M 2.650721 0.223547 9.5235 97.2616 240.1326 313.3340 0.003985255 44000.
        */
        mu_planet[298] = (4 * Math.PI / 3) * Math.pow((82.75 / 2), 3) * 5.3E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[288] = 2.650721;
        asteroid_e1[288] = 0.223635;
        asteroid_e2[288] = 0.223547;
        asteroid_i1[288] = 9.5195;
        asteroid_i2[288] = 9.5235;
        asteroid_omega1[288] = 97.1072;
        asteroid_omega2[288] = 97.2616;
        asteroid_w1[288] = 240.4030;
        asteroid_w2[288] = 240.1326;
        asteroid_mean1[288] = 100.9035;
        asteroid_mean2[288] = 313.3340;
        asteroid_meanmotion[288] = 0.003985255;

        /*  Asteroid 914  */
        /*
        914a C 79.0 19 9 3 0.1460E-10 9
        914b C 2.455616 0.214613 25.3016 255.4301 47.9549 83.4793 0.004469953 47800.
        914c C 2.455616 0.214317 25.3077 255.5305 47.8096 190.3084 0.004469953 44000.
        */
        mu_planet[299] = (4 * Math.PI / 3) * Math.pow((76.61 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[289] = 2.455616;
        asteroid_e1[289] = 0.214613;
        asteroid_e2[289] = 0.214317;
        asteroid_i1[289] = 25.3016;
        asteroid_i2[289] = 25.3077;
        asteroid_omega1[289] = 255.4301;
        asteroid_omega2[289] = 255.5305;
        asteroid_w1[289] = 47.9549;
        asteroid_w2[289] = 47.8096;
        asteroid_mean1[289] = 83.4793;
        asteroid_mean2[289] = 190.3084;
        asteroid_meanmotion[289] = 0.004469953;

        /*  Asteroid 44  */
        /*
        44a E 73.3 17 8 2 -0.5794E-10 6
        44b E 2.422790 0.150144 3.7102 131.1001 342.6200 141.3373 0.004560892 47800.
        44c E 2.422790 0.150199 3.7084 131.1814 342.4189 228.4417 0.004560892 44000.
        Note: Use S density.
        */
        mu_planet[300] = (4 * Math.PI / 3) * Math.pow((70.64 / 2), 3) * 2.7E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[290] = 2.422790;
        asteroid_e1[290] = 0.150144;
        asteroid_e2[290] = 0.150199;
        asteroid_i1[290] = 3.7102;
        asteroid_i2[290] = 3.7084;
        asteroid_omega1[290] = 131.1001;
        asteroid_omega2[290] = 131.1814;
        asteroid_w1[290] = 342.6200;
        asteroid_w2[290] = 342.4189;
        asteroid_mean1[290] = 141.3373;
        asteroid_mean2[290] = 228.4417;
        asteroid_meanmotion[290] = 0.004560892;

        /*  Asteroid 790  */
        /*
        790a PC 176.0 25 12 4 0.8889E-09 12
        790b P 3.399097 0.159855 20.5953 251.7767 39.9603 348.3076 0.002744007 47800.
        790c P 3.399097 0.159101 20.6065 252.0452 39.3935 111.1700 0.002744007 44000.
        */
        mu_planet[301] = (4 * Math.PI / 3) * Math.pow((170.37 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[291] = 3.399097;
        asteroid_e1[291] = 0.159855;
        asteroid_e2[291] = 0.159101;
        asteroid_i1[291] = 20.5953;
        asteroid_i2[291] = 20.6065;
        asteroid_omega1[291] = 251.7767;
        asteroid_omega2[291] = 252.0452;
        asteroid_w1[291] = 39.9603;
        asteroid_w2[291] = 39.3935;
        asteroid_mean1[291] = 348.3076;
        asteroid_mean2[291] = 111.1700;
        asteroid_meanmotion[291] = 0.002744007;

        /*  Asteroid 76  */
        /*
        76a CP 190.0 25 12 5 0.3676E-09 12 amp 0.14 radians
        76b C 3.398566 0.178583 2.1068 205.8920 250.7092 29.3305 0.002743563 47800.
        76c C 3.398566 0.179085 2.0992 206.7637 249.1486 152.6800 0.002743563 44000.
        */
        mu_planet[302] = (4 * Math.PI / 3) * Math.pow((183.66 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[292] = 3.398566;
        asteroid_e1[292] = 0.178583;
        asteroid_e2[292] = 0.179085;
        asteroid_i1[292] = 2.1068;
        asteroid_i2[292] = 2.0992;
        asteroid_omega1[292] = 205.8920;
        asteroid_omega2[292] = 206.7637;
        asteroid_w1[292] = 250.7092;
        asteroid_w2[292] = 249.1486;
        asteroid_mean1[292] = 29.3305;
        asteroid_mean2[292] = 152.6800;
        asteroid_meanmotion[292] = 0.002743563;

        /*  Asteroid 420  */
        /*
        420a P 146.0 25 12 4 0.5456E-09 11
        420b P 3.418054 0.033180 6.6788 243.4625 206.1543 159.7230 0.002720722 47800.
        420c P 3.418054 0.033436 6.6744 243.8922 205.3746 287.7063 0.002720722 44000.
        Note: Use C density.
        */
        mu_planet[303] = (4 * Math.PI / 3) * Math.pow((141.25 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[293] = 3.418054;
        asteroid_e1[293] = 0.033180;
        asteroid_e2[293] = 0.033436;
        asteroid_i1[293] = 6.6788;
        asteroid_i2[293] = 6.6744;
        asteroid_omega1[293] = 243.4625;
        asteroid_omega2[293] = 243.8922;
        asteroid_w1[293] = 206.1543;
        asteroid_w2[293] = 205.3746;
        asteroid_mean1[293] = 159.7230;
        asteroid_mean2[293] = 287.7063;
        asteroid_meanmotion[293] = 0.002720722;

        /*  Asteroid 106  */
        /*
        106a G 147.0 25 12 4 0.3574E-09 9 amp 0.14 radians
        106b G 3.171169 0.170549 4.6078 61.9103 332.8615 131.1147 0.003045058 47800.
        106c G 3.171169 0.170638 4.6103 62.1053 332.2824 188.5169 0.003045058 44000.
        Note: Use C density.
        */
        mu_planet[304] = (4 * Math.PI / 3) * Math.pow((146.59 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[294] = 3.171169;
        asteroid_e1[294] = 0.170549;
        asteroid_e2[294] = 0.170638;
        asteroid_i1[294] = 4.6078;
        asteroid_i2[294] = 4.6103;
        asteroid_omega1[294] = 61.9103;
        asteroid_omega2[294] = 62.1053;
        asteroid_w1[294] = 332.8615;
        asteroid_w2[294] = 332.2824;
        asteroid_mean1[294] = 131.1147;
        asteroid_mean2[294] = 188.5169;
        asteroid_meanmotion[294] = 0.003045058;

        /*  Asteroid 130  */
        /*
        130a G 189.0 25 12 3 -0.6656E-10 11
        130b G 3.117815 0.214473 22.8648 145.1217 235.8506 173.9108 0.003124055 47800.
        130c G 3.117815 0.214056 22.8741 145.3082 235.5820 213.8113 0.003124055 44000.
        Note: Use C density.
        */
        mu_planet[305] = (4 * Math.PI / 3) * Math.pow((182.27 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[295] = 3.117815;
        asteroid_e1[295] = 0.214473;
        asteroid_e2[295] = 0.214056;
        asteroid_i1[295] = 22.8648;
        asteroid_i2[295] = 22.8741;
        asteroid_omega1[295] = 145.1217;
        asteroid_omega2[295] = 145.3082;
        asteroid_w1[295] = 235.8506;
        asteroid_w2[295] = 235.5820;
        asteroid_mean1[295] = 173.9108;
        asteroid_mean2[295] = 213.8113;
        asteroid_meanmotion[295] = 0.003124055;

        /*  Asteroid 13  */
        /*
        13a G 215.0 19 9 2 0.6090E-10 6
        13b G 2.576332 0.086443 16.5167 42.8718 79.6666 134.1173 0.004159304 47800.
        13c G 2.576332 0.086493 16.5189 42.9927 79.4748 308.6080 0.004159304 44000.
        Note: Use C density.
        */
        mu_planet[306] = (4 * Math.PI / 3) * Math.pow((207.64 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[296] = 2.576332;
        asteroid_e1[296] = 0.086443;
        asteroid_e2[296] = 0.086493;
        asteroid_i1[296] = 16.5167;
        asteroid_i2[296] = 16.5189;
        asteroid_omega1[296] = 42.8718;
        asteroid_omega2[296] = 42.9927;
        asteroid_w1[296] = 79.6666;
        asteroid_w2[296] = 79.4748;
        asteroid_mean1[296] = 134.1173;
        asteroid_mean2[296] = 308.6080;
        asteroid_meanmotion[296] = 0.004159304;

        /*  Asteroid 87  */
        /*
        87a PC 271.0 25 12 3 -0.1507E-08 11
        87b P 3.485221 0.088342 10.8699 73.1223 268.5010 252.9191 0.002642460 47800.
        87c P 3.485221 0.088192 10.8748 73.4633 267.9848 37.7676 0.002642460 44000.
        */
        mu_planet[307] = (4 * Math.PI / 3) * Math.pow((260.94 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[297] = 3.485221;
        asteroid_e1[297] = 0.088342;
        asteroid_e2[297] = 0.088192;
        asteroid_i1[297] = 10.8699;
        asteroid_i2[297] = 10.8748;
        asteroid_omega1[297] = 73.1223;
        asteroid_omega2[297] = 73.4633;
        asteroid_w1[297] = 268.5010;
        asteroid_w2[297] = 267.9848;
        asteroid_mean1[297] = 252.9191;
        asteroid_mean2[297] = 37.7676;
        asteroid_meanmotion[297] = 0.002642460;

        /*  Asteroid 51  */
        /*
        51a C 153.0 17 8 2 0.1395E-12 6
        51b C 2.365692 0.066664 9.9722 175.5970 1.9177 51.2332 0.004727110 47800.
        51c C 2.365692 0.066679 9.9699 175.6975 1.6704 102.1747 0.004727110 44000.
        */
        mu_planet[308] = (4 * Math.PI / 3) * Math.pow((147.86 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[298] = 2.365692;
        asteroid_e1[298] = 0.066664;
        asteroid_e2[298] = 0.066679;
        asteroid_i1[298] = 9.9722;
        asteroid_i2[298] = 9.9699;
        asteroid_omega1[298] = 175.5970;
        asteroid_omega2[298] = 175.6975;
        asteroid_w1[298] = 1.9177;
        asteroid_w2[298] = 1.6704;
        asteroid_mean1[298] = 51.2332;
        asteroid_mean2[298] = 102.1747;
        asteroid_meanmotion[298] = 0.004727110;

        /*  Asteroid 773  */
        /*
        773a D 99.1 25 12 2 -0.4249E-10 7
        773b D 2.857904 0.080319 16.6858 322.0854 332.7508 20.0526 0.003559856 47800.
        773c D 2.857904 0.080346 16.6868 322.2433 332.4523 325.1275 0.003559856 44000.
        Note: Use C density.
        */
        mu_planet[309] = (4 * Math.PI / 3) * Math.pow((95.88 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[299] = 2.857904;
        asteroid_e1[299] = 0.080319;
        asteroid_e2[299] = 0.080346;
        asteroid_i1[299] = 16.6858;
        asteroid_i2[299] = 16.6868;
        asteroid_omega1[299] = 322.0854;
        asteroid_omega2[299] = 322.2433;
        asteroid_w1[299] = 332.7508;
        asteroid_w2[299] = 332.4523;
        asteroid_mean1[299] = 20.0526;
        asteroid_mean2[299] = 325.1275;
        asteroid_meanmotion[299] = 0.003559856;

        /*  Asteroid 336  */
        /*
        336a D  72.0 16 7 2 -0.1111E-10 7
        336b D 2.251821 0.095178 5.6488 234.6182 30.7736 346.0310 0.005090242 47800.
        336c D 2.251821 0.095135 5.6475 234.7283 30.5598 317.8669 0.005090242 44000.
        Note: Use C density.
        */
        mu_planet[310] = (4 * Math.PI / 3) * Math.pow((69.30 / 2), 3) * 1.34E+12 * Math.pow(0.01720209895, 2) / 1.9891E+30;
        asteroid_a[300] = 2.251821;
        asteroid_e1[300] = 0.095178;
        asteroid_e2[300] = 0.095135;
        asteroid_i1[300] = 5.6488;
        asteroid_i2[300] = 5.6475;
        asteroid_omega1[300] = 234.6182;
        asteroid_omega2[300] = 234.7283;
        asteroid_w1[300] = 30.7736;
        asteroid_w2[300] = 30.5598;
        asteroid_mean1[300] = 346.0310;
        asteroid_mean2[300] = 317.8669;
        asteroid_meanmotion[300] = 0.005090242;


    }

    void getPlanetPosVel(double jultime, int i, double ephemeris_r[], double ephemeris_rprime[]) {

        /*
            Procedure to calculate the position and velocity of planet i, subject to the JPL DE405 ephemeris.  The positions and velocities are calculated using Chebyshev polynomials, the coefficients of which are stored in the files "ASCPxxxx.txt".
            The general idea is as follows:  First, check to be sure the proper ephemeris coefficients (corresponding to jultime) are available.  Then read the coefficients corresponding to jultime, and calculate the positions and velocities of the planet.
            Tested and verified 7-27-99
          */

        int interval = 0, numbers_to_skip = 0, pointer = 0, j = 0, k = 0, subinterval = 0, light_pointer = 0;

        double interval_start_time = 0, subinterval_duration = 0, chebyshev_time = 0;

        double[] position_poly = new double[20];
        double[][] coef = new double[4][20];
        double[] velocity_poly = new double[20];

        /*
          If this is the first time the ephemeris routines are being called, initialize the arrays "number_of_coefs", "number_of_coef_sets", and "mu_planet".
        */
        if ((ephemeris_dates[1] == 0.0) && (ephemeris_dates[2] == 0.0))
            initializeArrays();

        /*
            Begin by determining whether the current ephemeris coefficients are appropriate for jultime, or if we need to load a new set.
          */
        if ((jultime < ephemeris_dates[1]) || (jultime > ephemeris_dates[2]))
            getEphemerisCoefficients(jultime);

        interval = (int) (Math.floor((jultime - ephemeris_dates[1]) / interval_duration) + 1);
        interval_start_time = (interval - 1) * interval_duration + ephemeris_dates[1];
        subinterval_duration = interval_duration / number_of_coef_sets[i];
        subinterval = (int) (Math.floor((jultime - interval_start_time) / subinterval_duration) + 1);
        numbers_to_skip = (interval - 1) * numbers_per_interval;

        /*
            Starting at the beginning of the coefficient array, skip the first "numbers_to_skip" coefficients.  This puts the pointer on the first piece of data in the correct interval.
        */
        pointer = numbers_to_skip + 1;

        /*  Skip the coefficients for the first (i-1) planets  */
        for (j = 1; j <= (i - 1); j++)
            pointer = pointer + 3 * number_of_coef_sets[j] * number_of_coefs[j];

        /*  Skip the next (subinterval - 1)*3*number_of_coefs(i) coefficients  */
        pointer = pointer + (subinterval - 1) * 3 * number_of_coefs[i];

        for (j = 1; j <= 3; j++) {
            for (k = 1; k <= number_of_coefs[i]; k++) {
                /*  Read the pointer'th coefficient as the array entry coef[j][k]  */
                coef[j][k] = ephemeris_coefficients[pointer];
                pointer = pointer + 1;
            }
        }

        /*  Calculate the chebyshev time within the subinterval, between -1 and +1  */
        chebyshev_time = 2 * (jultime - ((subinterval - 1) * subinterval_duration + interval_start_time)) / subinterval_duration - 1;

        /*  Calculate the Chebyshev position polynomials   */
        position_poly[1] = 1;
        position_poly[2] = chebyshev_time;
        for (j = 3; j <= number_of_coefs[i]; j++) {
            position_poly[j] = 2 * chebyshev_time * position_poly[j - 1] - position_poly[j - 2];
        }

        /*  Calculate the position of the i'th planet at jultime  */
        for (j = 1; j <= 3; j++) {
            ephemeris_r[j] = 0;
            for (k = 1; k <= number_of_coefs[i]; k++)
                ephemeris_r[j] = ephemeris_r[j] + coef[j][k] * position_poly[k];

            /*  Convert from km to A.U.  */
            ephemeris_r[j] = ephemeris_r[j] / au;
        }

        /*  Calculate the Chebyshev velocity polynomials  */
        velocity_poly[1] = 0;
        velocity_poly[2] = 1;
        velocity_poly[3] = 4 * chebyshev_time;
        for (j = 4; j <= number_of_coefs[i]; j++) {
            velocity_poly[j] = 2 * chebyshev_time * velocity_poly[j - 1] + 2 * position_poly[j - 1] - velocity_poly[j - 2];
        }

        /*  Calculate the velocity of the i'th planet  */
        for (j = 1; j <= 3; j++) {
            ephemeris_rprime[j] = 0;
            for (k = 1; k <= number_of_coefs[i]; k++)
                ephemeris_rprime[j] = ephemeris_rprime[j] + coef[j][k] * velocity_poly[k];
            /*  The next line accounts for differentiation of the iterative formula with respect to chebyshev time.  Essentially, if dx/dt = (dx/dct) times (dct/dt), the next line includes the factor (dct/dt) so that the units are km/day  */
            ephemeris_rprime[j] = ephemeris_rprime[j] * (2.0 * number_of_coef_sets[i] / interval_duration);

            /*  Convert from km to A.U.  */
            ephemeris_rprime[j] = ephemeris_rprime[j] / au;

        }

    }

    void getEphemerisCoefficients(double jultime) {

        /*
            Procedure to read the DE405 ephemeris file corresponding to jultime.  The start and end dates of the ephemeris file are returned, as are the Chebyshev coefficients for Mercury, Venus, Earth-Moon, Mars, Jupiter, Saturn, Uranus, Neptune, Pluto, Geocentric Moon, and Sun.

            Note that the DE405 ephemeris files should be in the "c:\codes" folder.

            Tested and verified 7-16-99.
          */

        int mantissa = 0, mantissa1 = 0, mantissa2 = 0, exponent = 0, i = 0, records = 0, j = 0;

        String s, filename = " ", line = " ";

        try {

            /*  Select the proper ephemeris file  */
            if ((jultime >= 2341968.5) && (jultime < 2349264.5)) {
                ephemeris_dates[1] = 2341968.5;
                ephemeris_dates[2] = 2349264.5;
                filename = "ASCP1700.txt";
                records = 229;
            } else if ((jultime >= 2349264.5) && (jultime < 2356560.5)) {
                ephemeris_dates[1] = 2349264.5;
                ephemeris_dates[2] = 2356560.5;
                filename = "ASCP1720.txt";
                records = 229;
            } else if ((jultime >= 2356560.5) && (jultime < 2363856.5)) {
                ephemeris_dates[1] = 2356560.5;
                ephemeris_dates[2] = 2363856.5;
                filename = "ASCP1740.txt";
                records = 229;
            } else if ((jultime >= 2363856.5) && (jultime < 2371184.5)) {
                ephemeris_dates[1] = 2363856.5;
                ephemeris_dates[2] = 2371184.5;
                filename = "ASCP1760.txt";
                records = 230;
            } else if ((jultime >= 2371184.5) && (jultime < 2378480.5)) {
                ephemeris_dates[1] = 2371184.5;
                ephemeris_dates[2] = 2378480.5;
                filename = "ASCP1780.txt";
                records = 229;
            } else if ((jultime >= 2378480.5) && (jultime < 2385776.5)) {
                ephemeris_dates[1] = 2378480.5;
                ephemeris_dates[2] = 2385776.5;
                filename = "ASCP1800.txt";
                records = 229;
            } else if ((jultime >= 2385776.5) && (jultime < 2393104.5)) {
                ephemeris_dates[1] = 2385776.5;
                ephemeris_dates[2] = 2393104.5;
                filename = "ASCP1820.txt";
                records = 230;
            } else if ((jultime >= 2393104.5) && (jultime < 2400400.5)) {
                ephemeris_dates[1] = 2393104.5;
                ephemeris_dates[2] = 2400400.5;
                filename = "ASCP1840.txt";
                records = 229;
            } else if ((jultime >= 2400400.5) && (jultime < 2407696.5)) {
                ephemeris_dates[1] = 2400400.5;
                ephemeris_dates[2] = 2407696.5;
                filename = "ASCP1860.txt";
                records = 229;
            } else if ((jultime >= 2407696.5) && (jultime < 2414992.5)) {
                ephemeris_dates[1] = 2407696.5;
                ephemeris_dates[2] = 2414992.5;
                filename = "ASCP1880.txt";
                records = 229;
            } else if ((jultime >= 2414992.5) && (jultime < 2422320.5)) {
                ephemeris_dates[1] = 2414992.5;
                ephemeris_dates[2] = 2422320.5;
                filename = "ASCP1900.txt";
                records = 230;
            } else if ((jultime >= 2422320.5) && (jultime < 2429616.5)) {
                ephemeris_dates[1] = 2422320.5;
                ephemeris_dates[2] = 2429616.5;
                filename = "ASCP1920.txt";
                records = 229;
            } else if ((jultime >= 2429616.5) && (jultime < 2436912.5)) {
                ephemeris_dates[1] = 2429616.5;
                ephemeris_dates[2] = 2436912.5;
                filename = "ASCP1940.txt";
                records = 229;
            } else if ((jultime >= 2436912.5) && (jultime < 2444208.5)) {
                ephemeris_dates[1] = 2436912.5;
                ephemeris_dates[2] = 2444208.5;
                filename = "ASCP1960.txt";
                records = 229;
            } else if ((jultime >= 2444208.5) && (jultime < 2451536.5)) {
                ephemeris_dates[1] = 2444208.5;
                ephemeris_dates[2] = 2451536.5;
                filename = "ASCP1980.txt";
                records = 230;
            } else if ((jultime >= 2451536.5) && (jultime < 2458832.5)) {
                ephemeris_dates[1] = 2451536.5;
                ephemeris_dates[2] = 2458832.5;
                filename = "ASCP2000.txt";
                records = 229;
            } else if ((jultime >= 2458832.5) && (jultime < 2466128.5)) {
                ephemeris_dates[1] = 2458832.5;
                ephemeris_dates[2] = 2466128.5;
                filename = "ASCP2020.txt";
                records = 229;
            } else if ((jultime >= 2466128.5) && (jultime < 2473456.5)) {
                ephemeris_dates[1] = 2466128.5;
                ephemeris_dates[2] = 2473456.5;
                filename = "ASCP2040.txt";
                records = 230;
            } else if ((jultime >= 2473456.5) && (jultime < 2480752.5)) {
                ephemeris_dates[1] = 2473456.5;
                ephemeris_dates[2] = 2480752.5;
                filename = "ASCP2060.txt";
                records = 229;
            } else if ((jultime >= 2480752.5) && (jultime < 2488048.5)) {
                ephemeris_dates[1] = 2480752.5;
                ephemeris_dates[2] = 2488048.5;
                filename = "ASCP2080.txt";
                records = 229;
            } else if ((jultime >= 2488048.5) && (jultime < 2495344.5)) {
                ephemeris_dates[1] = 2488048.5;
                ephemeris_dates[2] = 2495344.5;
                filename = "ASCP2100.txt";
                records = 229;
            } else if ((jultime >= 2495344.5) && (jultime < 2502672.5)) {
                ephemeris_dates[1] = 2495344.5;
                ephemeris_dates[2] = 2502672.5;
                filename = "ASCP2120.txt";
                records = 230;
            } else if ((jultime >= 2502672.5) && (jultime < 2509968.5)) {
                ephemeris_dates[1] = 2502672.5;
                ephemeris_dates[2] = 2509968.5;
                filename = "ASCP2140.txt";
                records = 229;
            } else if ((jultime >= 2509968.5) && (jultime < 2517264.5)) {
                ephemeris_dates[1] = 2509968.5;
                ephemeris_dates[2] = 2517264.5;
                filename = "ASCP2160.txt";
                records = 229;
            } else if ((jultime >= 2517264.5) && (jultime < 2524624.5)) {
                ephemeris_dates[1] = 2517264.5;
                ephemeris_dates[2] = 2524624.5;
                filename = "ASCP2180.txt";
                records = 230;
            }

            FileReader file = new FileReader(filename);
            BufferedReader buff = new BufferedReader(file);

            /* Read each record in the file */
            for (j = 1; j <= records; j++) {

                /*  read line 1 and ignore  */
                line = buff.readLine();

                /* read lines 2 through 274 and parse as appropriate */
                for (i = 2; i <= 274; i++) {
                    line = buff.readLine();
                    if (i > 2) {
                        /*  parse first entry  */
                        mantissa1 = Integer.parseInt(line.substring(4, 13));
                        mantissa2 = Integer.parseInt(line.substring(13, 22));
                        exponent = Integer.parseInt(line.substring(24, 26));
                        if (line.substring(23, 24).equals("+"))
                            ephemeris_coefficients[(j - 1) * 816 + (3 * (i - 2) - 1)] = mantissa1 * Math.pow(10, (exponent - 9)) + mantissa2 * Math.pow(10, (exponent - 18));
                        else
                            ephemeris_coefficients[(j - 1) * 816 + (3 * (i - 2) - 1)] = mantissa1 * Math.pow(10, -(exponent + 9)) + mantissa2 * Math.pow(10, -(exponent + 18));
                        if (line.substring(1, 2).equals("-"))
                            ephemeris_coefficients[(j - 1) * 816 + (3 * (i - 2) - 1)] = -ephemeris_coefficients[(j - 1) * 816 + (3 * (i - 2) - 1)];
                    }
                    if (i > 2) {
                        /*  parse second entry  */
                        mantissa1 = Integer.parseInt(line.substring(30, 39));
                        mantissa2 = Integer.parseInt(line.substring(39, 48));
                        exponent = Integer.parseInt(line.substring(50, 52));
                        if (line.substring(49, 50).equals("+"))
                            ephemeris_coefficients[(j - 1) * 816 + 3 * (i - 2)] = mantissa1 * Math.pow(10, (exponent - 9)) + mantissa2 * Math.pow(10, (exponent - 18));
                        else
                            ephemeris_coefficients[(j - 1) * 816 + 3 * (i - 2)] = mantissa1 * Math.pow(10, -(exponent + 9)) + mantissa2 * Math.pow(10, -(exponent + 18));
                        if (line.substring(27, 28).equals("-"))
                            ephemeris_coefficients[(j - 1) * 816 + 3 * (i - 2)] = -ephemeris_coefficients[(j - 1) * 816 + 3 * (i - 2)];
                    }
                    if (i < 274) {
                        /*  parse third entry  */
                        mantissa1 = Integer.parseInt(line.substring(56, 65));
                        mantissa2 = Integer.parseInt(line.substring(65, 74));
                        exponent = Integer.parseInt(line.substring(76, 78));
                        if (line.substring(75, 76).equals("+"))
                            ephemeris_coefficients[(j - 1) * 816 + (3 * (i - 2) + 1)] = mantissa1 * Math.pow(10, (exponent - 9)) + mantissa2 * Math.pow(10, (exponent - 18));
                        else
                            ephemeris_coefficients[(j - 1) * 816 + (3 * (i - 2) + 1)] = mantissa1 * Math.pow(10, -(exponent + 9)) + mantissa2 * Math.pow(10, -(exponent + 18));
                        if (line.substring(53, 54).equals("-"))
                            ephemeris_coefficients[(j - 1) * 816 + (3 * (i - 2) + 1)] = -ephemeris_coefficients[(j - 1) * 816 + (3 * (i - 2) + 1)];
                    }
                }

                /* read lines 275 through 341 and ignore */
                for (i = 275; i <= 341; i++)
                    line = buff.readLine();

            }

            buff.close();

        } catch (IOException e) {
            System.out.println("Error = " + e.toString());
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("String index out of bounds at i = " + i);
        }

    }


    void getObserverPosition(double latitude, double longitude, double altitude, double jultime, double EOP[], double observer_r[], double observer_rprime[]) {

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
            EOP[3] = -TimeUtils.getLeapSecond(jultime);
        }
        /* If (UT1 - TAI) is known, use it.  */
        delta_t = 32.184 - EOP[3];

        /* Section 3.351 Step a: Determine the UT1 time of observation */
        ut1_time = TimeUtils.convertTDBToTDT(jultime, latitude, longitude, altitude, EOP[3]) - delta_t / 86400.0;

        /* Section 3.351 Step b: Obtain the position vector of the observer in an Earth-fixed, geocentric coordinate system */
        C = 1.0 / Math.sqrt(Math.pow(Math.cos(latitude), 2) + Math.pow((1.0 - flattening), 2) * Math.pow(Math.sin(latitude), 2));
        S = Math.pow((1.0 - flattening), 2) * C;
        observer_r[1] = (earth_radius * C + altitude) * Math.cos(latitude) * Math.cos(longitude);
        observer_r[2] = (earth_radius * C + altitude) * Math.cos(latitude) * Math.sin(longitude);
        observer_r[3] = (earth_radius * S + altitude) * Math.sin(latitude);

        /* Section 3.352 Step c: Obtain the nutation angles, and the mean and true obliquity; calculate the nutation matrix. */
        /* Note: These values may already have been sent in the EOP argument.  If any are zero, we will estimate them; if not, we will use the values as given.  */
        if ((EOP[4] == 0.0) || (EOP[5] == 0.0) || (EOP[6] == 0.0)) {
            /* Use the nutation method to estimate the missing parameters.  */
            TimeUtils.getNutation(jultime, nutation_vector);
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
        nutation_matrix[1][2] = -Math.sin(delta_psi) * Math.cos(mean_obliquity);
        nutation_matrix[1][3] = -Math.sin(delta_psi) * Math.sin(mean_obliquity);
        nutation_matrix[2][1] = Math.sin(delta_psi) * Math.cos(true_obliquity);
        nutation_matrix[2][2] = Math.cos(delta_psi) * Math.cos(true_obliquity) * Math.cos(mean_obliquity) + Math.sin(true_obliquity) * Math.sin(mean_obliquity);
        nutation_matrix[2][3] = Math.cos(delta_psi) * Math.cos(true_obliquity) * Math.sin(mean_obliquity) - Math.sin(true_obliquity) * Math.cos(mean_obliquity);
        nutation_matrix[3][1] = Math.sin(delta_psi) * Math.sin(true_obliquity);
        nutation_matrix[3][2] = Math.cos(delta_psi) * Math.sin(true_obliquity) * Math.cos(mean_obliquity) - Math.cos(true_obliquity) * Math.sin(mean_obliquity);
        nutation_matrix[3][3] = Math.cos(delta_psi) * Math.sin(true_obliquity) * Math.sin(mean_obliquity) + Math.cos(true_obliquity) * Math.cos(mean_obliquity);

        /* Section 3.352 Step d: Compute the Greenwich mean and apparent sidereal time at ut1_time */
        bigT = (ut1_time - 2451545.0) / 36525;
        mean_sid_time = 67310.54841 + (876600.0 * 3600.0 + 8640184.812866) * bigT + 0.093104 * Math.pow(bigT, 2) - 6.2E-06 * Math.pow(bigT, 3);
        mean_sid_time = (mean_sid_time / 3600) * Math.PI / 12;
        apparent_sid_time = mean_sid_time + EOP[4] * Math.cos(EOP[5] + EOP[6]);

        /* Section 3.353 Step e: Compute the geocentric position and velocity vectors of the observer, with respect to the true equator and equinox of date */
        /* Test to see if the Earth's angular velocity has been provided.  If not, use an estimate derived from Meeus p. 73. and "Explanatory Supplement, p. 51 */
        if (EOP[7] == 0.0) {
            /* delta_time is the rate of change of (UT1 - TAI) in seconds per day */
            delta_time = -(123.5 + 2.0 * 32.5 * bigT) / 36525;
            EOP[7] = (86400.0 / (86400.0 - delta_time)) * 72.921151467E-06;
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
        for (i = 1; i <= 3; i++) {
            rotated_vector[i] = 0;
            for (j = 1; j <= 3; j++)
                rotated_vector[i] = rotated_vector[i] + rotation_matrix[i][j] * observer_r[j];
        }
        for (i = 1; i <= 3; i++)
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
        for (i = 1; i <= 3; i++) {
            rotated_vector[i] = 0;
            for (j = 1; j <= 3; j++)
                rotated_vector[i] = rotated_vector[i] + rotation_matrix[i][j] * g[j];
        }
        for (i = 1; i <= 3; i++)
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
        for (i = 1; i <= 3; i++) {
            rotated_vector[i] = 0;
            for (j = 1; j <= 3; j++)
                rotated_vector[i] = rotated_vector[i] + rotation_matrix[i][j] * g[j];
        }
        /* Step f: Convert to AU/day */
        for (i = 1; i <= 3; i++)
            gprime[i] = EOP[7] * rotated_vector[i] * 86400.0 / (au * 1000.0);
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
        for (i = 1; i <= 3; i++) {
            rotated_vector[i] = 0;
            for (j = 1; j <= 3; j++)
                rotated_vector[i] = rotated_vector[i] + rotation_matrix[i][j] * g[j];
        }
        /* Step f: Convert to AUs */
        for (i = 1; i <= 3; i++)
            g[i] = rotated_vector[i] / (au * 1000.0);

        /* Section 3.354 Step g: Transform observer position and velocity vectors to mean equator and equinox of J2000.0 */
        /* Multiply g by inverse (transpose) of nutation matrix */
        for (i = 1; i <= 3; i++) {
            rotated_vector[i] = 0;
            for (j = 1; j <= 3; j++)
                rotated_vector[i] = rotated_vector[i] + nutation_matrix[j][i] * g[j];
        }
        for (i = 1; i <= 3; i++)
            g[i] = rotated_vector[i];
        /* Multiply g by inverse (transpose) of precession matrix */
        precessionMatrix(jultime, matrix_P);
        for (i = 1; i <= 3; i++) {
            rotated_vector[i] = 0;
            for (j = 1; j <= 3; j++)
                rotated_vector[i] = rotated_vector[i] + matrix_P[j][i] * g[j];
        }
        for (i = 1; i <= 3; i++)
            g[i] = rotated_vector[i];
        /* Multiply gprime by inverse (transpose) of nutation matrix */
        for (i = 1; i <= 3; i++) {
            rotated_vector[i] = 0;
            for (j = 1; j <= 3; j++)
                rotated_vector[i] = rotated_vector[i] + nutation_matrix[j][i] * gprime[j];
        }
        for (i = 1; i <= 3; i++)
            gprime[i] = rotated_vector[i];
        /* Multiply gprime by inverse (transpose) of precession matrix */
        for (i = 1; i <= 3; i++) {
            rotated_vector[i] = 0;
            for (j = 1; j <= 3; j++)
                rotated_vector[i] = rotated_vector[i] + matrix_P[j][i] * gprime[j];
        }
        for (i = 1; i <= 3; i++)
            gprime[i] = rotated_vector[i];

        /* Section 3.355 Step h: Find the Earth's barycentric rectangular position and velocity at TDB jultime, add that to the observer's position and velocity, and get the barycentric rectangular position and velocity of the observer at jultime */
        planetaryEphemeris(jultime, dummy_r, 0);
        for (j = 1; j <= 3; j++) {
            observer_r[j] = g[j] + planet_r[3][j];
            observer_rprime[j] = gprime[j] + planet_rprime[3][j];
        }

    }

    void asteroidEphemeris(double jultime, int light_flight, double r[]) {
        /*
          Method to calculate the barycentric equatorial positions, velocities, and net Newtonian accelerations of the 300 most important asteroids.  The data is computed either at jultime, or so as to account for light time-of-flight relative to barycentric equatorial position r.  Valid values of light_flight are as follows:
            = 0 - apply no light time-of-flight correction
            = 1 - apply light time-of-flight correction
            = 2 - apply light time-of-flight correction, but don't bother to calculate net Newtonian acceleration
          IMPORTANT: This algorithm presumes that planetary ephemeris has already been called, and that planetary positions are available in the planet_r array.
          The output barycentric equatorial pos/vel/acc are placed in the planet_r, planet_rprime and planet_r2prime arrays beginning in slot 11; they can then be used by get_acceleration to calculate gravitational perturbations.
          The input mean orbital elements are wrt the mean ecliptic and equinox of B1950; they were provided by Dr. James Williams of JPL in a private communication.
        */

        double[] asteroid_r = new double[4];
        double[] asteroid_rprime = new double[4];
        double[] r_final = new double[4];
        double[] rprime_final = new double[4];
        double[] I_w_omega = new double[4];
        double[] classical_elements = new double[9];
        double[] sun_r = new double[4];
        double[] sun_rprime = new double[4];

        double dist = 0, lighttime = 0, mass = 0;

        double a = 0, e = 0, i = 0, omega = 0, w = 0, T1 = 0, T2 = 0, revs = 0, peritime = 0, mean = 0, q = 0;

        int j = 0, k = 0, l = 0;

        /* Calculate the position and velocity of the Sun at jultime, for use below */
        getPlanetPosVel(jultime, 11, sun_r, sun_rprime);

        /* For each asteroid, calculate pos/vel at jultime, and if necessary, compensate for light time-of-flight */
        for (j = 1; j <= (number_of_planets - 10); j++) {

            /*  Calculate the mean orbital elements of asteroid j at jultime */
            /*
              The mean elements of each asteroid were provided in two sets: The first set (with suffix ending in "1") corresponds to JD 2444000.5, while the second set (with suffix ending in "2") corresponds to JD 2447800.5.  This code therefore calculates the mean elements at jultime using a linear variation model.  This worked well for JPL in ephemeris DE403, according to Williams.
            */
            a = asteroid_a[j];
            e = asteroid_e2[j] + ((asteroid_e1[j] - asteroid_e2[j]) / 3800) * (jultime - 2444000.5);
            i = (asteroid_i2[j] + ((asteroid_i1[j] - asteroid_i2[j]) / 3800) * (jultime - 2444000.5)) * Math.PI / 180;
            omega = (asteroid_omega2[j] + ((asteroid_omega1[j] - asteroid_omega2[j]) / 3800) * (jultime - 2444000.5)) * Math.PI / 180;
            w = (asteroid_w2[j] + ((asteroid_w1[j] - asteroid_w2[j]) / 3800) * (jultime - 2444000.5)) * Math.PI / 180;
            T1 = 2447800.5 - (asteroid_mean1[j] * Math.PI / 180) / asteroid_meanmotion[j];
            T2 = 2444000.5 - (asteroid_mean2[j] * Math.PI / 180) / asteroid_meanmotion[j];
            revs = Math.floor((T1 - T2) / (2 * Math.PI / asteroid_meanmotion[j]));
            peritime = T2 + (T1 - T2 - revs * 2 * Math.PI / asteroid_meanmotion[j]) * (jultime - 2444000.5) / 3800;
            mean = asteroid_meanmotion[j] * (jultime - peritime);
            q = a * (1 - e);

            classical_elements[1] = a;
            classical_elements[2] = e;
            classical_elements[3] = i;
            classical_elements[4] = w;
            classical_elements[5] = omega;
            classical_elements[6] = peritime;
            classical_elements[7] = mean;
            classical_elements[8] = q;

            /* These elements are wrt the mean ecliptic and equinox of B1950/FK4.  They must be precessed and converted to J2000/FK5, and the resulting state vector must be rotated into heliocentric equatorial space */
            I_w_omega[1] = i;
            I_w_omega[2] = w;
            I_w_omega[3] = omega;
            TimeUtils.convertEclipticElementsFK4toFK5(I_w_omega);
            classical_elements[3] = I_w_omega[1];
            classical_elements[4] = I_w_omega[2];
            classical_elements[5] = I_w_omega[3];
            convertKeplerianElementsToStateVector(classical_elements, asteroid_r, asteroid_rprime, jultime);
            TimeUtils.convertEclipticToEquatorial(asteroid_r);
            TimeUtils.convertEclipticToEquatorial(asteroid_rprime);

            /* Two-body rprime is in units of au/(kappa*day); convert to au/day */
            for (k = 1; k <= 3; k++)
                rprime_final[k] = kappa * asteroid_rprime[k];

            /* Two-body pos/vel is heliocentric; convert to barycentric */
            for (k = 1; k <= 3; k++) {
                r_final[k] = asteroid_r[k] + sun_r[k];
                rprime_final[k] = rprime_final[k] + sun_rprime[k];
            }

            /* If necessary, correct for light-flight */
            /*
            Note: Since we are dealing with the mean elements of these asteroids, I do not believe it is appropriate to use a high-accuracy iterative process to precisely account for the light time-of-flight, including relativistic effects.  In the future, if better asteroid ephemerides become available, I will revisit this algorithm.  However, using only mean elements, a high-precision algorithm would yield no real increase in accuracy, despite a significant increase in processing time.
            */
            if ((light_flight == 1) || (light_flight == 2)) {
                dist = Math.sqrt(Math.pow((r[1] - r_final[1]), 2) + Math.pow((r[2] - r_final[2]), 2) + Math.pow((r[3] - r_final[3]), 2));
                lighttime = jultime - dist / speed_of_light;
                twoBodyUpdate(asteroid_r, asteroid_rprime, jultime, r_final, rprime_final, lighttime);
                getPlanetPosVel(lighttime, 11, sun_r, sun_rprime);
                for (k = 1; k <= 3; k++) {
                    r_final[k] = r_final[k] + sun_r[k];
                    rprime_final[k] = kappa * rprime_final[k] + sun_rprime[k];
                }
            }

            /* Put posvel into instance variables */
            for (k = 1; k <= 3; k++) {
                planet_r[10 + j][k] = r_final[k];
                planet_rprime[10 + j][k] = rprime_final[k];
            }

        }

        /*
          Finally, if needed, we must calculate the Newtonian acceleration of each asteroid, due to the gravitational effects of the other solar system bodies.
        */
        /*
        Note: Since we are dealing with the mean elements of these asteroids, I do not believe it is appropriate to use a high-accuracy iterative process to precisely account for the light time-of-flight, including relativistic effects.  Indeed, this would require updating the positions of the planets so as to account for light time-of-flight.  In the future, if better asteroid ephemerides become available, I will revisit this algorithm.  However, using only mean elements, a high-precision algorithm would yield no real increase in accuracy, despite a significant increase in processing time.
        Note: Since the Newtonian acceleration of each asteroid due to the other asteroids would be on the order of 10^-24 smaller than the Sun's gravitational force on a minor planet, I will neglect these terms, and only consider Newtonian acceleration due to the planets, Sun, and Earth's Moon.
        */
        if ((light_flight == 0) || (light_flight == 1)) {
            for (j = 11; j <= number_of_planets; j++) {
                for (k = 1; k <= 3; k++)
                    planet_r2prime[j][k] = 0;
/* TESTING ONLY !!!
			for (k=0;k<=10;k++) {
				if (j != k) {
					dist = Math.sqrt(Math.pow((planet_r[j][1] - planet_r[k][1]),2) + Math.pow((planet_r[j][2] - planet_r[k][2]),2) + Math.pow((planet_r[j][3] - planet_r[k][3]),2));
					for (l=1;l<=3;l++) {
						planet_r2prime[j][l] = planet_r2prime[j][l] + mu_planet[k]*(planet_r[k][l] - planet_r[j][l])/Math.pow(dist,3);
						}
					}
				}
TESTING ONLY !!!  */
            }
        }

    }

    public void twoBodyUpdate(double r1[], double r1prime[], double time1, double r2[], double r2prime[], double time2) {

        /*

          Procedure to calculate the position and velocity of a body at time2 given the position and velocity of the body at time1.  Uses the universal variables solution to the Gauss problem.  Reference is Roger Bate, Donald Mueller, and Jerry White, "Fundamentals of Astrodynamics", pp 191-210.

          Positions and velocities are given in canonical units, while times are Julian dates.

          Tested and verified 5 June 1999.

        */

        double r0 = 0, v0 = 0, rdotv = 0, alpha = 0, t = 0, x = 0, diff = 0, z = 0, c = 0, s = 0, tn = 0, dtdx = 0, f = 0, g = 0, r = 0, fdot = 0, gdot = 0;
        int k = 0, counter = 0;

        r0 = Math.sqrt(Math.pow(r1[1], 2) + Math.pow(r1[2], 2) + Math.pow(r1[3], 2));
        v0 = Math.sqrt(Math.pow(r1prime[1], 2) + Math.pow(r1prime[2], 2) + Math.pow(r1prime[3], 2));
        rdotv = r1[1] * r1prime[1] + r1[2] * r1prime[2] + r1[3] * r1prime[3];
        alpha = 2 / r0 - Math.pow(v0, 2) / mu;
        /* Tests indicate parabolic orbits do not give alpha = 0, but rather -1e-16.  This is a numerical artifact due to round-off error.  Force alpha = 0 in this case.  */
        if (Math.abs(alpha) < 1e-15) alpha = 0;
        t = kappa * (time2 - time1);

        if (Math.abs(t) > 0) {
            if (alpha >= 0) {
                x = Math.sqrt(mu) * alpha * t;
            } else {
                x = (t / Math.abs(t)) * Math.sqrt(-1 / alpha) * Math.log((-2 * mu * t * alpha) / (rdotv + (t / Math.abs(t)) * Math.sqrt(-mu / alpha) * (1 - alpha * r0)));
            }
        } else
            x = 0;

        diff = 1;

        while ((Math.abs(diff) > 1e-09) && (counter < 100)) {
            counter = counter + 1;
            z = alpha * Math.pow(x, 2);
            if (Math.abs(z) < .01) {
                c = 0.5 - z / 24 + Math.pow(z, 2) / 720 - Math.pow(z, 3) / 40320 + Math.pow(z, 4) / 3628800;
                s = 1.0 / 6.0 - z / 120 + Math.pow(z, 2) / 5040 - Math.pow(z, 3) / 362880 + Math.pow(z, 4) / 39916800;
            } else if (z < 0) {
                c = (1 - (Math.exp(Math.sqrt(-z)) + Math.exp(-Math.sqrt(-z))) / 2) / z;
                s = ((Math.exp(Math.sqrt(-z)) - Math.exp(-Math.sqrt(-z))) / 2 - Math.sqrt(-z)) / Math.pow(Math.sqrt(-z), 3);
            } else {
                c = (1 - Math.cos(Math.sqrt(z))) / z;
                s = (Math.sqrt(z) - Math.sin(Math.sqrt(z))) / Math.pow(Math.sqrt(z), 3);
            }
            tn = (rdotv * Math.pow(x, 2) * c / Math.sqrt(mu) + (1 - alpha * r0) * Math.pow(x, 3) * s + r0 * x) / Math.sqrt(mu);
            diff = t - tn;
            dtdx = (Math.pow(x, 2) * c + rdotv * x * (1 - z * s) / Math.sqrt(mu) + r0 * (1 - z * c)) / Math.sqrt(mu);
            x = x + diff / dtdx;
        }

        f = 1 - Math.pow(x, 2) * c / r0;
        g = t - Math.pow(x, 3) * s / Math.sqrt(mu);
        for (k = 1; k <= 3; k++)
            r2[k] = f * r1[k] + g * r1prime[k];

        r = Math.sqrt(Math.pow(r2[1], 2) + Math.pow(r2[2], 2) + Math.pow(r2[3], 2));

        fdot = Math.sqrt(mu) * x * (z * s - 1) / (r0 * r);
        gdot = 1 - Math.pow(x, 2) * c / r;
        for (k = 1; k <= 3; k++)
            r2prime[k] = fdot * r1[k] + gdot * r1prime[k];

    }

    void precessKeplerianElements(double[] I_w_omega, double jd_equinox1, double jd_equinox2, boolean FK5) {

        /*
          Procedure to precess the Keplerian elements from the Julian day of jd_equinox1 to jd_equinox2.  Elements referred to either FK4 or FK5 are precessed within this catalog, as indicated by the FK5 boolean.
          All angles are in radians
          Algorithm taken from Jean Meeus, "Astronomical Algorithms", pp 147-150, and "Astronomical Formulae for Calculators", p 71.

          Tested and verified 4 June 1999.

        */

        double oldi = 0, oldw = 0, oldomega = 0, bigt = 0, t = 0, eta = 0, bigpi = 0, p = 0, tau0 = 0, tau = 0;
        double psi = 0, icos = 0, a = 0, b = 0, omega_minus_psi = 0, omega = 0, isin = 0, I = 0, deltawsin = 0, deltawcos = 0, deltaw = 0, w = 0;

        oldi = I_w_omega[1];
        oldw = I_w_omega[2];
        oldomega = I_w_omega[3];

        if (FK5 == true) {
            /*  Precess within the FK5 catalog  */
            bigt = (jd_equinox1 - 2451545) / 36525;
            t = (jd_equinox2 - jd_equinox1) / 36525;
            eta = ((47.0029 - .06603 * bigt + .000598 * Math.pow(bigt, 2)) * t + (-.03302 + .000598 * bigt) * Math.pow(t, 2) + .00006 * Math.pow(t, 3)) * (Math.PI / 180) / 3600;
            bigpi = (174.876384 + (3289.4789 * bigt + .60622 * Math.pow(bigt, 2) - (869.8089 + .50491 * bigt) * t + .03536 * Math.pow(t, 2)) / 3600) * (Math.PI / 180);
            p = ((5029.0966 + 2.22226 * bigt - .000042 * Math.pow(bigt, 2)) * t + (1.11113 - .000042 * bigt) * Math.pow(t, 2) - .000006 * Math.pow(t, 3)) * (Math.PI / 180) / 3600;
        } else {
            /*  Precess within the FK4 catalog  */
            tau0 = (jd_equinox1 - 2415020.313) / 365242.1897;
            tau = (jd_equinox2 - 2415020.313) / 365242.1897;
            t = tau - tau0;
            eta = ((471.07 - 6.75 * tau0 + .57 * Math.pow(tau0, 2)) * t + (-3.37 + 0.57 * tau0) * Math.pow(t, 2) + .05 * Math.pow(t, 3)) * (Math.PI / 180) / 3600;
            bigpi = (173.950833 + (32869 * tau0 + 56 * Math.pow(tau0, 2) - (8694 + 55 * tau0) * t + 3 * Math.pow(t, 2)) / 3600) * (Math.PI / 180);
            p = ((50256.41 + 222.29 * tau0 + 0.26 * Math.pow(tau0, 2)) * t + (111.15 + 0.26 * tau0) * Math.pow(t, 2) + 0.1 * Math.pow(t, 3)) * (Math.PI / 180) / 3600;
        }

        psi = bigpi + p;
        icos = Math.cos(oldi) * Math.cos(eta) + Math.sin(oldi) * Math.sin(eta) * Math.cos(oldomega - bigpi);
        a = Math.sin(oldi) * Math.sin(oldomega - bigpi);
        b = -Math.sin(eta) * Math.cos(oldi) + Math.cos(eta) * Math.sin(oldi) * Math.cos(oldomega - bigpi);
        omega_minus_psi = Math.atan2(a, b);
        omega = omega_minus_psi + psi;

        while (omega < 0)
            omega = omega + 2 * Math.PI;
        while (omega > 2 * Math.PI)
            omega = omega - 2 * Math.PI;

        isin = a / Math.sin(omega - psi);
        I = Math.atan2(isin, icos);
        deltawsin = -Math.sin(eta) * Math.sin(oldomega - bigpi) / Math.sin(I);
        deltawcos = (Math.sin(oldi) * Math.cos(eta) - Math.cos(oldi) * Math.sin(eta) * Math.cos(oldomega - bigpi)) / Math.sin(I);
        deltaw = Math.atan2(deltawsin, deltawcos);
        w = oldw + deltaw;

        while (w < 0)
            w = w + 2 * Math.PI;
        while (w > 2 * Math.PI)
            w = w - 2 * Math.PI;

        /*  Pack elements for return  */
        I_w_omega[1] = I;
        I_w_omega[2] = w;
        I_w_omega[3] = omega;

    }


    public void convertStateVectorToKeplerianElements(double r[], double rprime[], double classical_elements[], double jultime1) {

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

        double[] h = new double[4];
        double[] n = new double[4];
        double[] vectore = new double[4];


        double magh = 0, magn = 0, rdotv = 0, magr = 0, magv = 0, p = 0, e = 0, a = 0, q = 0;
        double icos = 0, isin = 0, i = 0, omegacos = 0, omegasin = 0, omega = 0, wcos = 0, wsin = 0;
        double w = 0, nucos = 0, nusin = 0, nu = 0;
        double eccentriccos = 0, eccentricsin = 0, eccentric = 0;
        double mean_anomaly = 0, mean_motion = 0;

        int k = 0;

        h[1] = r[2] * rprime[3] - r[3] * rprime[2];
        h[2] = -(r[1] * rprime[3] - r[3] * rprime[1]);
        h[3] = r[1] * rprime[2] - r[2] * rprime[1];
        magh = Math.sqrt(Math.pow(h[1], 2) + Math.pow(h[2], 2) + Math.pow(h[3], 2));

        n[1] = -h[2];
        n[2] = h[1];
        n[3] = 0;
        magn = Math.sqrt(Math.pow(n[1], 2) + Math.pow(n[2], 2) + Math.pow(n[3], 2));

        rdotv = r[1] * rprime[1] + r[2] * rprime[2] + r[3] * rprime[3];
        magr = Math.sqrt(Math.pow(r[1], 2) + Math.pow(r[2], 2) + Math.pow(r[3], 2));
        magv = Math.sqrt(Math.pow(rprime[1], 2) + Math.pow(rprime[2], 2) + Math.pow(rprime[3], 2));

        for (k = 1; k <= 3; k++)
            vectore[k] = ((Math.pow(magv, 2) - mu / magr) * r[k] - rdotv * rprime[k]) / mu;

        p = Math.pow(magh, 2) / mu;
        e = Math.sqrt(Math.pow(vectore[1], 2) + Math.pow(vectore[2], 2) + Math.pow(vectore[3], 2));
        /*  Tests of parabolic orbits indicate that rounding errors can result in e not equal to 1.  Therefore, in such cases, I will force it to be 1.  */
        /* Similarly, tests of circular orbits indicate that rounding errors can result in e not equal to zero.  Therefore, in such cases, I will force it to be zero.  */
        if (Math.abs(e - 1.0) < 1e-15) e = 1.0;
        if (Math.abs(e - 0.0) < 1e-15) e = 0.0;

        if (e != 1) {
            /*  Escobal p. 74  */
            a = p / (1 - Math.pow(e, 2));
            q = a * (1 - e);
        } else {
            /*  Escobal p. 91  */
            q = p / 2;
        }

        icos = h[3] / magh;
        isin = Math.sqrt(1 - Math.pow(icos, 2));
        i = Math.atan2(isin, icos);

        if (i != 0.0) {
            omegacos = n[1] / magn;
            omegasin = Math.sqrt(1 - Math.pow(omegacos, 2));
            if (n[2] <= 0)
                omegasin = -omegasin;
            omega = Math.atan2(omegasin, omegacos);
            while (omega < 0.0)
                omega = omega + 2.0 * Math.PI;
        } else
            omega = 0.0;

        if (e != 0.0) {
            if (i != 0.0) {
                /* w is angle between line of nodes and perihelion */
                wcos = (n[1] * vectore[1] + n[2] * vectore[2] + n[3] * vectore[3]) / (magn * e);
                wsin = Math.sqrt(1 - Math.pow(wcos, 2));
                if (vectore[3] <= 0)
                    wsin = -wsin;
                w = Math.atan2(wsin, wcos);
                while (w < 0.0)
                    w = w + 2.0 * Math.PI;
            } else {
                /* w is angle between I and perihelion */
                wcos = vectore[1] / e;
                wsin = Math.sqrt(1 - Math.pow(wcos, 2));
                if (vectore[3] <= 0)
                    wsin = -wsin;
                w = Math.atan2(wsin, wcos);
                while (w < 0.0)
                    w = w + 2.0 * Math.PI;
            }
        } else
            /* w is undefined */
            w = 0.0;

        if (e != 0.0) {
            /* nu is angle from perihelion to r */
            nucos = (r[1] * vectore[1] + r[2] * vectore[2] + r[3] * vectore[3]) / (magr * e);
            nusin = Math.sqrt(1 - Math.pow(nucos, 2));
            if (rdotv <= 0)
                nusin = -nusin;
            nu = Math.atan2(nusin, nucos);
        } else if (i != 0) {
            /* nu is angle from line of nodes to r */
            nucos = (r[1] * n[1] + r[2] * n[2] + r[3] * n[3]) / (magr * magn);
            nusin = Math.sqrt(1 - Math.pow(nucos, 2));
            if (rdotv <= 0)
                nusin = -nusin;
            nu = Math.atan2(nusin, nucos);
        } else {
            /* nu is angle from I to r */
            nucos = r[1] / magr;
            nusin = r[2] / magr;
            nu = Math.atan2(nusin, nucos);
        }
        classical_elements[0] = nu;

        /*  Determine mean anomaly and time of perihelion  */
        if (e < 1) {
            /*  Escobal p. 86  */
            eccentriccos = (e + Math.cos(nu)) / (1 + e * Math.cos(nu));
            eccentricsin = Math.sqrt(1 - Math.pow(eccentriccos, 2));
            if (Math.sin(nu) < 0)
                eccentricsin = -eccentricsin;
            eccentric = Math.atan2(eccentricsin, eccentriccos);
            mean_anomaly = eccentric - e * Math.sin(eccentric);

            mean_motion = kappa * Math.sqrt(mu / Math.pow(a, 3));
            /* Forced to reuse variables */
            nu = jultime1 - mean_anomaly / mean_motion;
        } else if (e == 1) {
            /*  Meeus pp. 225-227.  */
            /* Forced to reuse variables */
            eccentric = Math.tan(nu / 2);
            eccentricsin = Math.pow(eccentric, 3) + 3 * eccentric;
            nu = jultime1 - eccentricsin * Math.pow(q, 1.5) / (3 * kappa / Math.sqrt(2));
        } else {
            /*  Escobal p. 92, BMW p. 188.  */
            /* Forced to reuse variables */
            eccentric = (e + Math.cos(nu)) / (1 + e * Math.cos(nu));
            eccentriccos = Math.log(eccentric + Math.sqrt(Math.pow(eccentric, 2) - 1));
            if (nu < 0.0)
                eccentriccos = -Math.abs(eccentriccos);
            else
                eccentriccos = Math.abs(eccentriccos);
            eccentricsin = (Math.exp(eccentriccos) - Math.exp(-eccentriccos)) / 2;
            mean_anomaly = e * eccentricsin - eccentriccos;
            mean_motion = kappa * Math.sqrt(mu / Math.pow((-a), 3));
            /* Forced to reuse variables */
            nu = jultime1 - mean_anomaly / mean_motion;
        }
        while (mean_anomaly < 0.0)
            mean_anomaly = mean_anomaly + 2.0 * Math.PI;

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

    public void convertKeplerianElementsToStateVector(double classical_elements[], double r[], double rprime[], double jultime1) {

        /*

          Procedure to calculate the position vector r[] and velocity vector rprime[] of a satellite, given the classical Keplerian elements.

          Algorithm taken from Bate, Mueller, and White, "Fundamentals of Astrodynamics", pp 72-73, 212-217, Pedro Ramon Escobal, "Methods of Orbit Determination", pp. 73-85, 90-92, 118-119, 433, and Jean Meeus, "Astronomical Algorithms", pp. 225-227.

          r[] is the position vector (3 entries)
          rprime[] is the velocity vector (3 entries)
          p[] is the unit vector in the perifocal system toward periapsis (3 entries)
          q[] is the unit vector in the perifocal system perpendicular to p[], with +q in the direction of revolution (3 entries)

          Tested and verified 4 June 1999.

        */

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
            p_element = a * (1 - Math.pow(e, 2));
        else
            /*  Escobal p. 91  */
            p_element = 2 * q_element;

        /*  Escobal p. 79  */
        p[1] = Math.cos(w) * Math.cos(omega) - Math.sin(w) * Math.sin(omega) * Math.cos(I);
        p[2] = Math.cos(w) * Math.sin(omega) + Math.sin(w) * Math.cos(omega) * Math.cos(I);
        p[3] = Math.sin(w) * Math.sin(I);
        q[1] = -Math.sin(w) * Math.cos(omega) - Math.cos(w) * Math.sin(omega) * Math.cos(I);
        q[2] = -Math.sin(w) * Math.sin(omega) + Math.cos(w) * Math.cos(omega) * Math.cos(I);
        q[3] = Math.cos(w) * Math.sin(I);

        if (e < 1) {
            eccentric = keplerSolver(e, mean);
            /*  Escobal p. 85  */
            rmag = a * (1 - e * Math.cos(eccentric));
            /*  Escobal p. 85  */
            nucos = (e - Math.cos(eccentric)) / (e * Math.cos(eccentric) - 1);
            /*  Escobal p. 85  */
            nusin = a * Math.sqrt(1 - Math.pow(e, 2)) * Math.sin(eccentric) / rmag;
            nu = Math.atan2(nusin, nucos);
        } else if (e == 1) {
            /*  Meeus pp. 225-227  */
            big_w = (3 * kappa / Math.sqrt(2.0)) * (jultime1 - peritime) / Math.pow(q_element, 1.5);
            while (diff > 1e-12) {
                olds = s;
                s = (2 * Math.pow(s, 3) + big_w) / (3 * (Math.pow(s, 2) + 1));
                diff = Math.abs(s - olds);
            }
            rmag = q_element * (1 + Math.pow(s, 2));
            nu = 2 * Math.atan(s);
        } else if (e > 1) {
            f = hyperbolicKeplerSolver(e, mean);
            fcosh = (Math.exp(f) + Math.exp(-f)) / 2;
            fsinh = (Math.exp(f) - Math.exp(-f)) / 2;
            /*  Escobal p. 90  */
            rmag = a * (1 - e * fcosh);
            /*  Escobal p. 118  */
            nucos = (fcosh - e) / (1 - e * fcosh);
            /*  Escobal p. 118  */
            nusin = Math.sqrt(Math.pow(e, 2) - 1) * fsinh / (e * fcosh - 1);
            nu = Math.atan2(nusin, nucos);
        }

        for (k = 1; k <= 3; k++) {
            /*  Escobal p. 119 and Bate, Mueller, and White pp. 72-73 */
            r[k] = rmag * Math.cos(nu) * p[k] + rmag * Math.sin(nu) * q[k];
            rprime[k] = Math.sqrt(mu / p_element) * (-Math.sin(nu) * p[k] + (e + Math.cos(nu)) * q[k]);
        }

    }


    double hyperbolicKeplerSolver(double e, double mean) {

        /*
          Program to solve Kepler's equation (hyperbolic case) for f, given the eccentricity and mean anomaly
          Algorithm from Bate, Mueller, and White, "Fundamentals of Astrodynamics", p. 221, and Pedro Ramon Escobal, "Methods of Orbit Determination", p. 92.

          Tested and Verified 31 May 1999.
        */

        double f = 0, oldf = 0, diff = 0;

        oldf = mean;
        f = oldf + (mean - (e * ((Math.exp(oldf) - Math.exp(-oldf)) / 2) - oldf)) / (e * ((Math.exp(oldf) + Math.exp(-oldf)) / 2) - 1);
        diff = Math.abs(oldf - f);

        while (diff >= 1e-11) {
            oldf = f;
            f = oldf + (mean - (e * ((Math.exp(oldf) - Math.exp(-oldf)) / 2) - oldf)) / (e * ((Math.exp(oldf) + Math.exp(-oldf)) / 2) - 1);
            diff = Math.abs(oldf - f);
        }

        return f;

    }

    double keplerSolver(double e, double mean) {

        /*
          Procedure to solve Kepler's equation (elliptic case) for the eccentric anomaly, given the eccentricity and mean anomaly

          Tested and Verified 31 May 1999.
        */

        double oldeccentric = 0, diff = 0, eccentric = 0;

        oldeccentric = mean;
        eccentric = oldeccentric - (oldeccentric - e * Math.sin(oldeccentric) - mean) / (1 - e * Math.cos(oldeccentric));
        diff = Math.abs(oldeccentric - eccentric);

        while (diff >= 1e-11) {
            oldeccentric = eccentric;
            eccentric = oldeccentric - (oldeccentric - e * Math.sin(oldeccentric) - mean) / (1 - e * Math.cos(oldeccentric));
            diff = Math.abs(oldeccentric - eccentric);
        }

        return eccentric;

    }

    //MinorPlanet methods

    public void leastSquaresSolution(double[] residuals, double[] rms_residuals) {

        /*
            Method to calculate the least squares best-fit orbit of a minor planet, given optical, radar delay, and or radar doppler observations.  The process is repeated until the magnitude of the corrections to the epoch state vector is less than some percentage of their corresponding covariances.
            The method presumes that an a priori state vector has been selected, and resides in epoch_r, epoch_rprime, and epoch_time.  The method also assumes that the observations have been ordered from earliest to most recent.
            The output will be the epoch state vector, and the epoch error covariance matrix (all of which are instance variables of the MinorPlanet class).
            We will assume a maximum of 2000 optical observations, 100 radar delay observations, and 100 radar doppler observations.
            Provision is made for excluding observations whose chi or residuals exceed user-defined thresholds; although these observations will not be deleted, they will not be used in the final iterations of the algorithm.
            Adapted from: D.K. Yeomans, S.J. Ostro, and P.W. Chodas (1987). "Radar Astrometry of Near-Earth Asteroids". Astronomical Journal 94, 189-200.  Modified to the "Square Root Information Filter" algorithm from Gerald J. Bierman (1974), "Sequential Square Root Filtering and Smoothing of Discrete Linear Systems", Automatica 10, 147-158.
            NOTE: In case of divergence, epoch_r[0] will be returned as 6.0.
          */

        int i = 0, j = 0, k = 0, index = 0, retained_index = 0;

        boolean convergence = false, divergence = false, edit = false;

        double rms_optical = 0, rms_delay = 0, rms_doppler = 0;
        double old_rms_optical = 0, old_rms_delay = 0, old_rms_doppler = 0;
        double convergence_factor = 0.10;
        double delta = 0, delay = 0, doppler = 0;
        double observation_residual = 0, observation_chi = 0;

        int number_of_observations = number_of_optical_observations + number_of_delay_observations + number_of_doppler_observations;

        double[] nominal_r = new double[4];
        double[] nominal_rprime = new double[4];
        double[] initial_r = new double[4];
        double[] initial_rprime = new double[4];
        double[][] r_variant = new double[7][4];
        double[][] rprime_variant = new double[7][4];
        double[] this_r = new double[4];
        double[] this_rprime = new double[4];
        double[] next_r = new double[4];
        double[] next_rprime = new double[4];
        double[] variant_delta = new double[7];
        double[] dev = new double[16002];
        double[][] matrix_a = new double[16002][7];
        double[][] collision_variant = new double[2000][9];
        double[][] phi = new double[7][7];
        double[] inter = new double[7];
        double[] sensitivity_ra = new double[7];
        double[] sensitivity_dec = new double[7];
        double[] sensitivity_delay = new double[7];
        double[] sensitivity_doppler = new double[7];
        double[] radec = new double[3];
        double[] row = new double[7];
        double[] corrections = new double[7];
        double[] observer_r = new double[4];
        double[] observer_rprime = new double[4];
        double[] delay_doppler = new double[3];
        double[] receiver_r = new double[4];
        double[] receiver_rprime = new double[4];
        double[] EOP = new double[8];
        double[][] matrix_R = new double[7][7];
        double[] matrix_z = new double[7];
        double[][] matrix_T = new double[6002][6002];
        double[][] matrix_temp = new double[16002][7];
        double[] retained_residuals = new double[16002];
        double[] initial_residuals = new double[16002];
        double[] initial_rms_residuals = new double[4];
        double[][] initial_big_p = new double[9][9];

        /* Initialize all observations as non-excluded for get_residuals */
        for (i = 1; i <= 8000; i++)
            excluded_observations[i] = 0;

        /* Using the a priori state vector, determine initial residuals, nominal observables, and the state vector at the tdb time of each observation.  Save the a priori state vector in case of divergence. */
        getResiduals(residuals, rms_residuals);
        rms_optical = rms_residuals[1];
        rms_delay = rms_residuals[2];
        rms_doppler = rms_residuals[3];
        for (i = 1; i <= 3; i++) {
            initial_r[i] = epoch_r[i];
            initial_rprime[i] = epoch_rprime[i];
            initial_rms_residuals[i] = rms_residuals[i];
        }
        for (i = 1; i <= 8; i++) {
            for (j = 1; j <= 8; j++)
                initial_big_p[i][j] = big_p[i][j];
        }
        index = 2 * number_of_optical_observations + number_of_delay_observations + number_of_doppler_observations;
        for (i = 1; i <= index; i++) {
            initial_residuals[i] = residuals[i];
        }

        /*
          The convergence condition is: The RMS residuals on the current loop must differ by no more than some percentage from the RMS residuals on the preceding loop.
          (Note that this condition was not my original choice.  I tried requiring that the corrections to the epoch state vector must be less than some percentage of their corresponding covariances.  However, real-world data is not sufficiently smooth.  This new condition uses the observed fact that the RMS residuals decrease with successive loops until the next loop results in almost no change.  At this point, the solution has converged.)
          Clearly, this condition will not be satisfied initially, so we will go through the loop at least once.
          NOTE:  MUST ACCOUNT FOR DIVERGENCE, AS MEASURED BY INCREASING RMS RESIDUALS.  If each of the RMS residuals increase from one loop to the next, the divergence flag will be set, and the original solution returned.
          */
        while ((convergence == false) && (divergence == false)) {

            /* Clear the counters */
            index = 0;
            retained_index = 0;

            /* Use the observations to generate rows of matrix_a */
            for (i = 1; i <= number_of_observations; i++) {

                /* Assume that the observation will be excluded; if not, the flag will be reset below. */
                excluded_observations[i] = 1;

                /* Generate the state transition matrix corresponding to this observation time */
                for (j = 1; j <= 3; j++) {
                    nominal_r[j] = incremental_state_vector_r[i][j];
                    nominal_rprime[j] = incremental_state_vector_rprime[i][j];
                }
                /* Initialize the variant trajectories */
                if (i == 1) {
                    for (j = 1; j <= 6; j++) {
                        for (k = 1; k <= 3; k++) {
                            r_variant[j][k] = epoch_r[k];
                            rprime_variant[j][k] = epoch_rprime[k];
                        }
                    }
                    for (j = 1; j <= 3; j++) {
                        variant_delta[j] = 0.000001 * epoch_r[j];
                        r_variant[j][j] = r_variant[j][j] + variant_delta[j];
                        variant_delta[j + 3] = 0.000001 * epoch_rprime[j];
                        rprime_variant[j + 3][j] = rprime_variant[j + 3][j] + variant_delta[j + 3];
                    }
                }
                /* Propogate the variant trajectories, and calculate the transition matrix entries */
                for (j = 1; j <= 6; j++) {
                    for (k = 1; k <= 3; k++) {
                        this_r[k] = r_variant[j][k];
                        this_rprime[k] = rprime_variant[j][k];
                    }
                    update(this_r, this_rprime, incremental_state_vector_epoch[i - 1], next_r, next_rprime, incremental_state_vector_epoch[i], false, collision_variant);
                    for (k = 1; k <= 3; k++) {
                        r_variant[j][k] = next_r[k];
                        rprime_variant[j][k] = next_rprime[k];
                    }
                    for (k = 1; k <= 3; k++) {
                        phi[k][j] = (r_variant[j][k] - nominal_r[k]) / variant_delta[j];
                        phi[k + 3][j] = (rprime_variant[j][k] - nominal_rprime[k]) / variant_delta[j];
                    }
                }

                /* Generate the sensitivity matrix for this observation */
                if (observation_type[i] == 1) {
                    /* Optical observation */
                    for (j = 1; j <= 3; j++) {
                        observer_r[j] = observation_r[i][j];
                        observer_rprime[j] = observation_rprime[i][j];
                    }
                    for (j = 1; j <= 3; j++) {
                        delta = 0.000001 * nominal_r[j];
                        nominal_r[j] = nominal_r[j] + delta;
                        getObservedRadec(nominal_r, nominal_rprime, incremental_state_vector_epoch[i], observation_geocentric[i], observation_latitude[i], observation_longitude[i], observation_altitude[i], observation_EOP[i][3], observer_r, observer_rprime, optical_time[i], radec);
                        sensitivity_ra[j] = radec[1] - nominal_observable[index + 1];
                        while (Math.abs(sensitivity_ra[j]) > Math.abs(sensitivity_ra[j] - 2.0 * Math.PI))
                            sensitivity_ra[j] = sensitivity_ra[j] - 2.0 * Math.PI;
                        while (Math.abs(sensitivity_ra[j]) > Math.abs(sensitivity_ra[j] + 2.0 * Math.PI))
                            sensitivity_ra[j] = sensitivity_ra[j] + 2.0 * Math.PI;
                        sensitivity_ra[j] = sensitivity_ra[j] / delta;
                        sensitivity_dec[j] = (radec[2] - nominal_observable[index + 2]) / delta;
                        nominal_r[j] = nominal_r[j] - delta;
                        delta = 0.000001 * nominal_rprime[j];
                        nominal_rprime[j] = nominal_rprime[j] + delta;
                        getObservedRadec(nominal_r, nominal_rprime, incremental_state_vector_epoch[i], observation_geocentric[i], observation_latitude[i], observation_longitude[i], observation_altitude[i], observation_EOP[i][3], observer_r, observer_rprime, optical_time[i], radec);
                        sensitivity_ra[j + 3] = radec[1] - nominal_observable[index + 1];
                        while (Math.abs(sensitivity_ra[j + 3]) > Math.abs(sensitivity_ra[j + 3] - 2.0 * Math.PI))
                            sensitivity_ra[j + 3] = sensitivity_ra[j + 3] - 2.0 * Math.PI;
                        while (Math.abs(sensitivity_ra[j + 3]) > Math.abs(sensitivity_ra[j + 3] + 2.0 * Math.PI))
                            sensitivity_ra[j + 3] = sensitivity_ra[j + 3] + 2.0 * Math.PI;
                        sensitivity_ra[j + 3] = sensitivity_ra[j + 3] / delta;
                        sensitivity_dec[j + 3] = (radec[2] - nominal_observable[index + 2]) / delta;
                        nominal_rprime[j] = nominal_rprime[j] - delta;
                    }

                    /* Calculate the residuals and chi; if observations meet the applicable thresholds, calculate the resulting rows of matrix_a */
                    observation_residual = Math.acos(Math.sin(nominal_observable[index + 2]) * Math.sin(optical_dec[i]) + Math.cos(nominal_observable[index + 2]) * Math.cos(optical_dec[i]) * Math.cos(nominal_observable[index + 1] - optical_ra[i]));
                    if ((ra_dev[i] > 0.0) && (dec_dev[i] > 0.0))
                        observation_chi = Math.sqrt(Math.pow(residuals[index + 1] * Math.cos(optical_dec[i]) / ra_dev[i], 2.0) + Math.pow(residuals[index + 2] / dec_dev[i], 2.0));
                    else
                        observation_chi = 0.0;
                    if (((edit) && (chi_testing) && (observation_chi < chi_threshold) && (ra_dev[i] < 5.0 * Math.PI / (3600.0 * 180.0)) && (dec_dev[i] < 5.0 * Math.PI / (3600.0 * 180.0))) || ((edit) && (residual_testing) && (Math.abs(observation_residual) < optical_residual_threshold) && (ra_dev[i] < 5.0 * Math.PI / (3600.0 * 180.0)) && (dec_dev[i] < 5.0 * Math.PI / (3600.0 * 180.0))) || (!edit)) {

                        /* Observation is not excluded; reset flag */
                        excluded_observations[i] = 0;

                        for (j = 1; j <= 6; j++) {
                            row[j] = 0.0;
                            for (k = 1; k <= 6; k++)
                                row[j] = row[j] + sensitivity_ra[k] * phi[k][j];
                            matrix_a[retained_index + 1][j] = row[j];
                        }
                        for (j = 1; j <= 6; j++) {
                            row[j] = 0.0;
                            for (k = 1; k <= 6; k++)
                                row[j] = row[j] + sensitivity_dec[k] * phi[k][j];
                            matrix_a[retained_index + 2][j] = row[j];
                        }

                        /* Fill the dev array with standard deviations */
                        dev[retained_index + 1] = ra_dev[i];
                        dev[retained_index + 2] = dec_dev[i];

                        /* Add these residuals to the list of those being retained */
                        retained_residuals[retained_index + 1] = residuals[index + 1];
                        retained_residuals[retained_index + 2] = residuals[index + 2];

                        /* Increment the retained observation counter */
                        retained_index = retained_index + 2;

                    }

                    /* Increment the index */
                    index = index + 2;

                } else if (observation_type[i] == 2) {
                    /* Delay observation */
                    for (j = 1; j <= 3; j++) {
                        receiver_r[j] = radar_delay_receiver_r[i][j];
                        receiver_rprime[j] = radar_delay_receiver_rprime[i][j];
                    }
                    for (j = 1; j <= 7; j++)
                        EOP[j] = radar_delay_transmitter_EOP[i][j];

                    for (j = 1; j <= 3; j++) {
                        delta = 0.000001 * nominal_r[j];
                        nominal_r[j] = nominal_r[j] + delta;
                        getObservedRadarDelayDoppler(radar_delay_receiver_latitude[i], radar_delay_receiver_longitude[i], radar_delay_receiver_altitude[i], receiver_r, receiver_rprime, radar_delay_receiver_time[i], radar_delay_transmitter_latitude[i], radar_delay_transmitter_longitude[i], radar_delay_transmitter_altitude[i], EOP, radar_delay_transmitter_tdb_minus_utc[i], radar_delay_receiver_tdb_minus_utc[i], radar_delay_transmitter_frequency[i], minor_planet_radius, nominal_r, nominal_rprime, incremental_state_vector_epoch[i], delay_doppler);
                        delay = delay_doppler[1];
                        sensitivity_delay[j] = (delay - nominal_observable[index + 1]) / delta;
                        nominal_r[j] = nominal_r[j] - delta;
                        delta = 0.000001 * nominal_rprime[j];
                        nominal_rprime[j] = nominal_rprime[j] + delta;
                        getObservedRadarDelayDoppler(radar_delay_receiver_latitude[i], radar_delay_receiver_longitude[i], radar_delay_receiver_altitude[i], receiver_r, receiver_rprime, radar_delay_receiver_time[i], radar_delay_transmitter_latitude[i], radar_delay_transmitter_longitude[i], radar_delay_transmitter_altitude[i], EOP, radar_delay_transmitter_tdb_minus_utc[i], radar_delay_receiver_tdb_minus_utc[i], radar_delay_transmitter_frequency[i], minor_planet_radius, nominal_r, nominal_rprime, incremental_state_vector_epoch[i], delay_doppler);
                        delay = delay_doppler[1];
                        sensitivity_delay[j + 3] = (delay - nominal_observable[index + 1]) / delta;
                        nominal_rprime[j] = nominal_rprime[j] - delta;
                    }

                    /* Calculate the residual and chi; if observations meet the applicable threshold, calculate the resulting row of matrix_a */
                    observation_residual = Math.abs(residuals[index + 1]);
                    if (delay_dev[i] != 0.0)
                        observation_chi = Math.abs(residuals[index + 1] / delay_dev[i]);
                    else
                        observation_chi = 0.0;
                    if (((edit) && (chi_testing) && (observation_chi < chi_threshold)) || ((edit) && (residual_testing) && (Math.abs(observation_residual) < delay_residual_threshold)) || (!edit)) {

                        /* Observation is not excluded; reset flag */
                        excluded_observations[i] = 0;

                        for (j = 1; j <= 6; j++) {
                            row[j] = 0.0;
                            for (k = 1; k <= 6; k++)
                                row[j] = row[j] + sensitivity_delay[k] * phi[k][j];
                            matrix_a[retained_index + 1][j] = row[j];
                        }

                        /* Fill the dev array with standard deviations */
                        dev[retained_index + 1] = delay_dev[i];

                        /* Add this residual to the list of those being retained */
                        retained_residuals[retained_index + 1] = residuals[index + 1];

                        /* Increment the retained observation counter */
                        retained_index = retained_index + 1;

                    }

                    /* Increment the index */
                    index = index + 1;

                } else if (observation_type[i] == 3) {
                    /* Doppler observation */
                    for (j = 1; j <= 3; j++) {
                        receiver_r[j] = radar_doppler_receiver_r[i][j];
                        receiver_rprime[j] = radar_doppler_receiver_rprime[i][j];
                    }
                    for (j = 1; j <= 7; j++) {
                        EOP[j] = radar_doppler_transmitter_EOP[i][j];
                    }

                    for (j = 1; j <= 3; j++) {
                        delta = 0.000001 * nominal_r[j];
                        nominal_r[j] = nominal_r[j] + delta;
                        getObservedRadarDelayDoppler(radar_doppler_receiver_latitude[i], radar_doppler_receiver_longitude[i], radar_doppler_receiver_altitude[i], receiver_r, receiver_rprime, radar_doppler_receiver_time[i], radar_doppler_transmitter_latitude[i], radar_doppler_transmitter_longitude[i], radar_doppler_transmitter_altitude[i], EOP, radar_doppler_transmitter_tdb_minus_utc[i], radar_doppler_receiver_tdb_minus_utc[i], radar_doppler_transmitter_frequency[i], minor_planet_radius, nominal_r, nominal_rprime, incremental_state_vector_epoch[i], delay_doppler);
                        doppler = delay_doppler[2];
                        sensitivity_doppler[j] = (doppler - nominal_observable[index + 1]) / delta;
                        nominal_r[j] = nominal_r[j] - delta;
                        delta = 0.000001 * nominal_rprime[j];
                        nominal_rprime[j] = nominal_rprime[j] + delta;
                        getObservedRadarDelayDoppler(radar_doppler_receiver_latitude[i], radar_doppler_receiver_longitude[i], radar_doppler_receiver_altitude[i], receiver_r, receiver_rprime, radar_doppler_receiver_time[i], radar_doppler_transmitter_latitude[i], radar_doppler_transmitter_longitude[i], radar_doppler_transmitter_altitude[i], EOP, radar_doppler_transmitter_tdb_minus_utc[i], radar_doppler_receiver_tdb_minus_utc[i], radar_doppler_transmitter_frequency[i], minor_planet_radius, nominal_r, nominal_rprime, incremental_state_vector_epoch[i], delay_doppler);
                        doppler = delay_doppler[2];
                        sensitivity_doppler[j + 3] = (doppler - nominal_observable[index + 1]) / delta;
                        nominal_rprime[j] = nominal_rprime[j] - delta;
                    }

                    /* Calculate the residual and chi; if observations meet the applicable thresholds, calculate the resulting row of matrix_a */
                    observation_residual = Math.abs(residuals[index + 1]);

                    if (doppler_dev[i] != 0.0)
                        observation_chi = Math.abs(residuals[index + 1] / doppler_dev[i]);
                    else
                        observation_chi = 0.0;
                    if (((edit) && (chi_testing) && (observation_chi < chi_threshold)) || ((edit) && (residual_testing) && (Math.abs(observation_residual) < doppler_residual_threshold)) || (!edit)) {

                        /* Observation is not excluded; reset flag */
                        excluded_observations[i] = 0;

                        for (j = 1; j <= 6; j++) {
                            row[j] = 0.0;
                            for (k = 1; k <= 6; k++)
                                row[j] = row[j] + sensitivity_doppler[k] * phi[k][j];
                            matrix_a[retained_index + 1][j] = row[j];
                        }

                        /* Fill the dev array with standard deviations */
                        dev[retained_index + 1] = doppler_dev[i];

                        /* Add this residual to the list of those being retained */
                        retained_residuals[retained_index + 1] = residuals[index + 1];

                        /* Increment the retained observation counter */
                        retained_index = retained_index + 1;

                    }

                    /* Increment the index */
                    index = index + 1;

                }


            }

            /* At this point, matrix A is complete.  We proceed to convert to a unity covariance form */
            /* Should be commented out */
            for (i = 1; i <= retained_index; i++) {
                for (j = 1; j <= 6; j++)
                    matrix_temp[i][j] = matrix_a[i][j] / dev[i];
                retained_residuals[i] = retained_residuals[i] / dev[i];
            }
            for (i = 1; i <= retained_index; i++) {
                for (j = 1; j <= 6; j++)
                    matrix_a[i][j] = matrix_temp[i][j];
            }

            /* Construct an orthogonal transform T using Givens rotations, and left multiply.  matrix_a will now be upper triangular; isolate the upper 6x6 as matrix_R, and isolate the top 6x1 of T x residuals as matrix_z. */
            givensQr(retained_index, 6, matrix_a, matrix_T);

            for (i = 1; i <= 6; i++) {
                matrix_z[i] = 0;
                for (j = 1; j <= retained_index; j++)
                    matrix_z[i] = matrix_z[i] + matrix_T[i][j] * retained_residuals[j];
            }

            for (i = 1; i <= 6; i++) {
                for (j = 1; j <= 6; j++)
                    matrix_R[i][j] = matrix_a[i][j];
            }

            /* Calculate the state vector corrections and covariances. */
            invertNxn(6, matrix_R);

            for (i = 1; i <= 6; i++) {
                corrections[i] = 0;
                for (k = 1; k <= 6; k++)
                    /* Square Root Information Filtering assumes that we are in unit covariance form.  Since we're often unable to invert matrix_R in that form, we must make the conversion to unit covariance form now, so the results are valid.  In this particular calculation, we would multiply matrix_R[i][k] by dev[k] (since matrix_a has been inverted), and we would divide matrix_z by dev[k]; since these precisely cancel, no action is really needed here. */
                    corrections[i] = corrections[i] + matrix_R[i][k] * matrix_z[k];
            }

            for (i = 1; i <= 6; i++) {
                for (j = 1; j <= 6; j++) {
                    big_p[i][j] = 0;
                    for (k = 1; k <= 6; k++)
                        /* Square Root Information Filtering assumes that we are in unit covariance form.  Since we're often unable to invert matrix_R in that form, we must make the conversion to unit covariance form now, so the results are valid.  In this particular calculation, we would multiply matrix_R[i][k] by dev[k] (since matrix_a has been inverted), and we would multiply matrix_R[j][k] by dev[k] (for the same reason).  */
/*						big_p[i][j] = big_p[i][j] + matrix_R[i][k]*matrix_R[j][k]*(dev[k]*dev[k]);   */
                        big_p[i][j] = big_p[i][j] + matrix_R[i][k] * matrix_R[j][k];
                }
            }

            /* Update the epoch state vector */
            for (i = 1; i <= 3; i++) {
                epoch_r[i] = epoch_r[i] + corrections[i];
                epoch_rprime[i] = epoch_rprime[i] + corrections[i + 3];
            }

            /* Recalculate the residuals using the updated epoch state vector */
            getResiduals(residuals, rms_residuals);
            old_rms_optical = rms_optical;
            old_rms_delay = rms_delay;
            old_rms_doppler = rms_doppler;
            rms_optical = rms_residuals[1];
            rms_delay = rms_residuals[2];
            rms_doppler = rms_residuals[3];

            System.out.println("old_rms_optical = " + old_rms_optical);
            System.out.println("old_rms_delay = " + old_rms_delay);
            System.out.println("old_rms_doppler = " + old_rms_doppler);

            /* Calculate the asteroid's absolute magnitude, slope parameter, and radius.  This last is especially important when radar observations are used, to account for the "bounce point".  */
            solveForAbsoluteMagnitude();

            /* Based on the RMS residuals, evaluate the convergence and divergence conditions */
            convergence = true;
            if (Math.abs(old_rms_optical - rms_optical) > (convergence_factor * old_rms_optical))
                convergence = false;
            if (Math.abs(old_rms_delay - rms_delay) > (convergence_factor * old_rms_delay))
                convergence = false;
            if (Math.abs(old_rms_doppler - rms_doppler) > (convergence_factor * old_rms_doppler))
                convergence = false;
            if ((rms_optical >= 1.2 * old_rms_optical) && (rms_delay >= 1.2 * old_rms_delay) && (rms_doppler >= 1.2 * old_rms_doppler))
                divergence = true;
            if ((convergence) && (!edit) && ((chi_testing) || (residual_testing))) {
                edit = true;
                convergence = false;
            }
            /* We may not have yet converged.  However, if the residuals have been reduced, shouldn't we keep this state vector and covariance matrix as the initial data in case the next iteration diverges? */
            if ((rms_optical <= old_rms_optical) && (rms_delay <= old_rms_delay) && (rms_doppler <= old_rms_doppler)) {
                for (i = 1; i <= 3; i++) {
                    initial_r[i] = epoch_r[i];
                    initial_rprime[i] = epoch_rprime[i];
                    initial_rms_residuals[i] = rms_residuals[i];
                }
                for (i = 1; i <= 8; i++) {
                    for (j = 1; j <= 8; j++)
                        initial_big_p[i][j] = big_p[i][j];
                }
                index = 2 * number_of_optical_observations + number_of_delay_observations + number_of_doppler_observations;
                for (i = 1; i <= index; i++)
                    initial_residuals[i] = residuals[i];
            }

            System.out.println("convergence = " + convergence);
            System.out.println("divergence = " + divergence);
            /* The loop is complete.  If the convergence or divergence condition is satisfied, we will exit; if not, we will go around again */

        }

        if (divergence == true) {
            /* Return initial orbit and residuals, and set epoch_r[0] = 6.0. */
            for (i = 1; i <= 3; i++) {
                epoch_r[i] = initial_r[i];
                epoch_rprime[i] = initial_rprime[i];
                rms_residuals[i] = initial_rms_residuals[i];
            }
            epoch_r[0] = 6.0;
            for (i = 1; i <= 6; i++) {
                for (j = 1; j <= 6; j++)
                    big_p[i][j] = initial_big_p[i][j];
            }
            index = 2 * number_of_optical_observations + number_of_delay_observations + number_of_doppler_observations;
            for (i = 1; i <= index; i++)
                residuals[i] = initial_residuals[i];
        } else if (divergence == false)
            epoch_r[0] = 0.0;

    }


    public void getResiduals(double residual[], double rms_residual[]) {

        /*
            Method to calculate the (observed - computed) and RMS residuals in the optical, delay, and doppler observations of a minor planet, given the state vector (an instance variable in the MinorPlanet class).
            Note that, to improve efficiency, it is assumed that the observations are ordered from earliest to most recent.  The algorithm will therefore integrate the epoch state vector successively from observation i to i+1, saving the state vector at observation i+1 in the instance variables "incremental_state_vector_r[i+1][j]" and "incremental_state_vector_rprime[i+1][j]".  This incremental state vector can then be used in method "least_squares" to represent the nominal state vector at the TDB time of observation i+1 (incremental_state_vector_epoch[i+1]).
          */

        double delay = 0, doppler = 0, rms_optical = 0, rms_delay = 0, rms_doppler = 0;

        double[] radec = new double[3];
        double[] observer_r = new double[4];
        double[] observer_rprime = new double[4];
        double[] delay_doppler = new double[3];
        double[] receiver_r = new double[4];
        double[] receiver_rprime = new double[4];
        double[] EOP = new double[8];
        double[] state_vector_r = new double[4];
        double[] state_vector_rprime = new double[4];
        double[] new_state_vector_r = new double[4];
        double[] new_state_vector_rprime = new double[4];
        double[][] collision_variant = new double[2000][9];

        int i = 0, j = 0, k = 0, index = 0, number_of_excluded_optical_observations = 0, number_of_excluded_delay_observations = 0, number_of_excluded_doppler_observations = 0;

        int number_of_observations = number_of_optical_observations + number_of_delay_observations + number_of_doppler_observations;

        /* Initialize the required variables */
        for (i = 1; i <= 3; i++) {
            incremental_state_vector_r[0][i] = epoch_r[i];
            incremental_state_vector_rprime[0][i] = epoch_rprime[i];
            state_vector_r[i] = epoch_r[i];
            state_vector_rprime[i] = epoch_rprime[i];
        }
        incremental_state_vector_epoch[0] = epoch_time;
        rms_optical = 0.0;
        rms_delay = 0.0;
        rms_doppler = 0.0;
        for (i = 1; i <= number_of_observations; i++) {
            /* For each observation, determine the nominal state vector */
            update(state_vector_r, state_vector_rprime, incremental_state_vector_epoch[i - 1], new_state_vector_r, new_state_vector_rprime, incremental_state_vector_epoch[i], false, collision_variant);
            for (j = 1; j <= 3; j++) {
                incremental_state_vector_r[i][j] = new_state_vector_r[j];
                incremental_state_vector_rprime[i][j] = new_state_vector_rprime[j];
                state_vector_r[j] = new_state_vector_r[j];
                state_vector_rprime[j] = new_state_vector_rprime[j];
            }
            /* Depending on the observation type, determine the nominal observables, and the residuals */
            if (observation_type[i] == 1) {
                /* Optical observation */
                /* Note that method get_observed_radec is being sent the observer posvel vectors that were determined when the observation was entered.  That method will see these non-zero vectors (possibly created using precise EOP values), and use them instead of calling method get_observer (which would use estimates of the EOPs to calculate observer posvel). */
                for (j = 1; j <= 3; j++) {
                    observer_r[j] = observation_r[i][j];
                    observer_rprime[j] = observation_rprime[i][j];
                }
                getObservedRadec(new_state_vector_r, new_state_vector_rprime, incremental_state_vector_epoch[i], observation_geocentric[i], observation_latitude[i], observation_longitude[i], observation_altitude[i], observation_EOP[i][3], observer_r, observer_rprime, optical_time[i], radec);
                nominal_observable[index + 1] = radec[1];
                nominal_observable[index + 2] = radec[2];
                residual[index + 1] = optical_ra[i] - radec[1];
                while (Math.abs(residual[index + 1]) > Math.abs(residual[index + 1] - 2.0 * Math.PI))
                    residual[index + 1] = residual[index + 1] - 2.0 * Math.PI;
                while (Math.abs(residual[index + 1]) > Math.abs(residual[index + 1] + 2.0 * Math.PI))
                    residual[index + 1] = residual[index + 1] + 2.0 * Math.PI;
/* System.out.println("residual[" + (index+1) + "] = " + residual[index+1]); */
                residual[index + 2] = optical_dec[i] - radec[2];
/* System.out.println("residual[" + (index+2) + "] = " + residual[index+2]); */
                /* Don't add these residuals to the rms total if the observation is excluded */
                if (excluded_observations[i] == 0) {
                    rms_optical = rms_optical + Math.pow(residual[index + 1], 2);
                    rms_optical = rms_optical + Math.pow(residual[index + 2], 2);
                } else {
                    number_of_excluded_optical_observations = number_of_excluded_optical_observations + 1;
                }
                /* Increment index to reflect two observables (ra and dec) */
                index = index + 2;
            } else if (observation_type[i] == 2) {
                /* Delay observation */
                for (j = 1; j <= 3; j++) {
                    receiver_r[j] = radar_delay_receiver_r[i][j];
                    receiver_rprime[j] = radar_delay_receiver_rprime[i][j];
                }
                for (j = 1; j <= 7; j++)
                    EOP[j] = radar_delay_transmitter_EOP[i][j];
                getObservedRadarDelayDoppler(radar_delay_receiver_latitude[i], radar_delay_receiver_longitude[i], radar_delay_receiver_altitude[i], receiver_r, receiver_rprime, radar_delay_receiver_time[i], radar_delay_transmitter_latitude[i], radar_delay_transmitter_longitude[i], radar_delay_transmitter_altitude[i], EOP, radar_delay_transmitter_tdb_minus_utc[i], radar_delay_receiver_tdb_minus_utc[i], radar_delay_transmitter_frequency[i], minor_planet_radius, new_state_vector_r, new_state_vector_rprime, incremental_state_vector_epoch[i], delay_doppler);
                delay = delay_doppler[1];
                nominal_observable[index + 1] = delay;
                residual[index + 1] = radar_delay[i] - delay;
/* System.out.println("residual[" + (index+1) + "] = " + residual[index+1]); */
                /* Don't add this residual to the rms total if the observation is excluded */
                if (excluded_observations[i] == 0) {
                    rms_delay = rms_delay + Math.pow(residual[index + 1], 2);
                } else {
                    number_of_excluded_delay_observations = number_of_excluded_delay_observations + 1;
                }
                /* Increment index to relect one observable (delay) */
                index = index + 1;
            } else if (observation_type[i] == 3) {
                /* Doppler observation */
                for (j = 1; j <= 3; j++) {
                    receiver_r[j] = radar_doppler_receiver_r[i][j];
                    receiver_rprime[j] = radar_doppler_receiver_rprime[i][j];
                }
                for (j = 1; j <= 7; j++)
                    EOP[j] = radar_doppler_transmitter_EOP[i][j];
                getObservedRadarDelayDoppler(radar_doppler_receiver_latitude[i], radar_doppler_receiver_longitude[i], radar_doppler_receiver_altitude[i], receiver_r, receiver_rprime, radar_doppler_receiver_time[i], radar_doppler_transmitter_latitude[i], radar_doppler_transmitter_longitude[i], radar_doppler_transmitter_altitude[i], EOP, radar_doppler_transmitter_tdb_minus_utc[i], radar_doppler_receiver_tdb_minus_utc[i], radar_doppler_transmitter_frequency[i], minor_planet_radius, new_state_vector_r, new_state_vector_rprime, incremental_state_vector_epoch[i], delay_doppler);
                doppler = delay_doppler[2];
                nominal_observable[index + 1] = doppler;
                residual[index + 1] = radar_doppler[i] - doppler;
/* System.out.println("residual[" + (index+1) + "] = " + residual[index+1]); */
                /* Don't add this residual to the rms total if the observation is excluded */
                if (excluded_observations[i] == 0) {
                    rms_doppler = rms_doppler + Math.pow(residual[index + 1], 2);
                } else {
                    number_of_excluded_doppler_observations = number_of_excluded_doppler_observations + 1;
                }
                /* Increment index to relect one observable (doppler) */
                index = index + 1;
            }
        }
        if ((number_of_optical_observations - number_of_excluded_optical_observations) > 0)
            rms_optical = Math.sqrt(rms_optical / (2.0 * (number_of_optical_observations - number_of_excluded_optical_observations)));
        else
            rms_optical = 0;
        System.out.println("rms_optical = " + rms_optical);
        rms_residual[1] = rms_optical;

        if ((number_of_delay_observations - number_of_excluded_delay_observations) > 0)
            rms_delay = Math.sqrt(rms_delay / (number_of_delay_observations - number_of_excluded_delay_observations));
        else
            rms_delay = 0;
        System.out.println("rms_delay  = " + rms_delay);
        rms_residual[2] = rms_delay;

        if ((number_of_doppler_observations - number_of_excluded_doppler_observations) > 0)
            rms_doppler = Math.sqrt(rms_doppler / (number_of_doppler_observations - number_of_excluded_doppler_observations));
        else
            rms_doppler = 0;
        System.out.println("rms_doppler  = " + rms_doppler);
        rms_residual[3] = rms_doppler;

    }


    void getObservedRadarDelayDoppler(double receiver_latitude, double receiver_longitude, double receiver_altitude, double receiver_r[], double receiver_rprime[], double receiver_time, double transmitter_latitude, double transmitter_longitude, double transmitter_altitude, double transmitter_EOP[], double transmitter_tdb_minus_utc, double receiver_tdb_minus_utc, double transmitter_frequency, double asteroid_radius, double asteroid_r[], double asteroid_rprime[], double asteroid_epoch_time, double delay_doppler[]) {

        /*
          Algorithm to determine the expected radar delay time and doppler shift for an astrometric radar observation.

          Inputs:
            - receiver_latitude, transmitter_latitude are in radians;
            - receiver_longitude, transmitter_longitude, measured east, are in radians;
            - receiver_altitude, transmitter_altitude are in meters;
            - receiver_r[] is a barycentric equatorial position in AU
            - receiver_rprime[] is a barycentric equatorial velocity in AU/day;
            - receiver_time is a UTC Julian date;
            - transmitter_EOP[] are the Earth Observation Parameters at transmitter_time;
            - transmitter_tdb_minus_utc, receiver_tdb_minus_utc are in seconds;
            - transmitter_frequency is in Hz;
            - asteroid_radius is in AU;
            - asteroid_r, asteroid_rprime, asteroid_epoch_time is the barycentric equatorial state vector of the asteroid, given in AUs, AUs/day, and TDB Julian date respectively.
          Output:
            - delay_doppler[1] = the predicted UTC delay from transmitter to asteroid to receiver, in seconds;
            - delay_doppler[2] = the predicted Doppler frequency shift, in Hz;

          This algorithm follows the outline provided in "Asteroid and Comet Orbits Using Radar Data", Yeomans, Chodas, Keesey, and Ostro, in The Astronomical Journal, Vol. 103, number 1 (January 1992), pp. 303-317.

        */

        double[] EOP = new double[8];
        double[][] collision = new double[2000][9];
        double[] r_at_bounce = new double[4];
        double[] rprime_at_bounce = new double[4];
        double[] rho_receiver = new double[4];
        double[] rho_dot_receiver = new double[4];
        double[] rho_transmitter = new double[4];
        double[] rho_dot_transmitter = new double[4];
        double[] dummy_rho = new double[4];
        double[] sun_r = new double[4];
        double[] sun_rprime = new double[4];
        double[] transmitter_r = new double[4];
        double[] transmitter_rprime = new double[4];

        double receiver_tdb_time = 0, bounce_time = 0, tau_receiver_delay = 0, tau_transmitter_delay = 0, dist_receiver = 0, old_tau_delay = 0, tau_diff = 1, transmitter_time = 0, dist_transmitter = 0, dot_one = 0, dot_two = 0, heliodist1 = 0, heliodist2 = 0, speed_transmitter = 0, speed_receiver = 0, rho_dot_t = 0, rho_dot_r = 0, bigT = 0;

        int i = 0;

        /* Note: I feel obligated to provide default values for TDB-UTC.  However, since the difference TDB-TDT is being calculated so cavalierly, I SINCERELY HOPE THIS IS NEVER USED!!!  Nevertheless, it is more accurate than a default of zero. */
        if (receiver_tdb_minus_utc == 0.0) {
            receiver_tdb_minus_utc = (TimeUtils.convertTDTtoTDB(receiver_time, receiver_latitude, receiver_longitude, receiver_altitude, transmitter_EOP[3]) - receiver_time) + (TimeUtils.getLeapSecond(receiver_time) + 32.184);
        }
        if (transmitter_tdb_minus_utc == 0.0) {
            transmitter_tdb_minus_utc = (TimeUtils.convertTDTtoTDB(receiver_time, receiver_latitude, receiver_longitude, receiver_altitude, transmitter_EOP[3]) - receiver_time) + (TimeUtils.getLeapSecond(receiver_time) + 32.184);
        }

        /*  First, get the barycentric position and velocity of the receiver at receiver_time.  */
        /* The receiver coordinates determined when the observation was entered may have been passed as an argument; these may have used precise values of the EOPs, so we don't want to recalculate.  On the other hand, the receiver coordinates may be zero; this method may be creating an ephemeris, so precise values of the EOPs may not be available.  We'll test and act accordingly.  */
        /* Convert the UTC receiver time to TDB  */
        receiver_tdb_time = receiver_time + receiver_tdb_minus_utc / 86400.0;
        if (receiver_r[1] == 0.0) {
            /* Receiver coordinates are unavailable; calculate them. */
            /* Set EOPs to zero so that method get_radar_position will use estimates and defaults. */
            for (i = 1; i <= 7; i++)
                EOP[i] = 0.0;
            /* get receiver barycentric equatorial position and velocity at TDB receiver_time */
            getObserverPosition(receiver_latitude, receiver_longitude, receiver_altitude, receiver_tdb_time, EOP, receiver_r, receiver_rprime);
        }
        /* If receiver_r[1] is not equal to 0.0, receiver coordinates are available; use them. */

        /* The downleg iteration - determine bounce_time, and the asteroid's position */
        /* As a first approximation, determine the asteroid's position at receiver_time, and estimate bounce_time */
        update(asteroid_r, asteroid_rprime, asteroid_epoch_time, r_at_bounce, rprime_at_bounce, receiver_tdb_time, false, collision);
        dist_receiver = Math.sqrt(Math.pow((r_at_bounce[1] - receiver_r[1]), 2) + Math.pow((r_at_bounce[2] - receiver_r[2]), 2) + Math.pow((r_at_bounce[3] - receiver_r[3]), 2));
        tau_receiver_delay = (dist_receiver - asteroid_radius) / (speed_of_light / 86400.0);

        /* Iteratively improve the estimate of bounce_time; repeat until successive values of tau_delay differ by no more than 0.05 microseconds. */
        while (tau_diff > 5E-08) {
            old_tau_delay = tau_receiver_delay;
            bounce_time = receiver_tdb_time - tau_receiver_delay / 86400.0;
            update(asteroid_r, asteroid_rprime, asteroid_epoch_time, r_at_bounce, rprime_at_bounce, bounce_time, false, collision);
            for (i = 1; i <= 3; i++) {
                rho_receiver[i] = r_at_bounce[i] - receiver_r[i];
                rho_dot_receiver[i] = rprime_at_bounce[i] - receiver_rprime[i];
            }
            dist_receiver = Math.sqrt(Math.pow(rho_receiver[1], 2) + Math.pow(rho_receiver[2], 2) + Math.pow(rho_receiver[3], 2));
            tau_receiver_delay = (dist_receiver - asteroid_radius) / (speed_of_light / 86400.0) + radarCorrections(receiver_tdb_time, r_at_bounce, receiver_r, transmitter_frequency);
            rho_dot_r = (rho_receiver[1] * rho_dot_receiver[1] + rho_receiver[2] * rho_dot_receiver[2] + rho_receiver[3] * rho_dot_receiver[3]) / dist_receiver;
            tau_diff = Math.abs(tau_receiver_delay - old_tau_delay);
        }

        /* The up-leg iteration - determine transmitter_time, */
        /* Using an initial approximation of transmitter_time and transmitter_r and _rprime, find the distance from the transmitter at transmitter_time to the asteroid at bounce_time, and iteratively refine transmitter_time */
        transmitter_time = bounce_time - tau_receiver_delay / 86400.0;
        /* get transmitter barycentric equatorial position and velocity at TDB transmitter_time */
        getObserverPosition(transmitter_latitude, transmitter_longitude, transmitter_altitude, transmitter_time, transmitter_EOP, transmitter_r, transmitter_rprime);
        dist_transmitter = Math.sqrt(Math.pow((r_at_bounce[1] - transmitter_r[1]), 2) + Math.pow((r_at_bounce[2] - transmitter_r[2]), 2) + Math.pow((r_at_bounce[3] - transmitter_r[3]), 2));
        tau_transmitter_delay = (dist_transmitter - asteroid_radius) / (speed_of_light / 86400.0);

        /* Iteratively improve the estimate of transmitter_time; repeat until successive values of tau_delay differ by no more than 0.05 microseconds. */
        tau_diff = 1.0;
        while (tau_diff > 5E-08) {
            old_tau_delay = tau_transmitter_delay;
            transmitter_time = bounce_time - tau_transmitter_delay / 86400.0;
            getObserverPosition(transmitter_latitude, transmitter_longitude, transmitter_altitude, transmitter_time, transmitter_EOP, transmitter_r, transmitter_rprime);
            for (i = 1; i <= 3; i++) {
                rho_transmitter[i] = r_at_bounce[i] - transmitter_r[i];
                rho_dot_transmitter[i] = rprime_at_bounce[i] - transmitter_rprime[i];
            }
            dist_transmitter = Math.sqrt(Math.pow(rho_transmitter[1], 2) + Math.pow(rho_transmitter[2], 2) + Math.pow(rho_transmitter[3], 2));
            tau_transmitter_delay = (dist_transmitter - asteroid_radius) / (speed_of_light / 86400.0) + radarCorrections(transmitter_time, r_at_bounce, transmitter_r, transmitter_frequency);
            rho_dot_t = (rho_transmitter[1] * rho_dot_transmitter[1] + rho_transmitter[2] * rho_dot_transmitter[2] + rho_transmitter[3] * rho_dot_transmitter[3]) / dist_transmitter;
            tau_diff = Math.abs(tau_transmitter_delay - old_tau_delay);
        }

        /* Calculate the predicted delay time, and Doppler shift */
        delay_doppler[1] = (tau_receiver_delay + tau_transmitter_delay) + transmitter_tdb_minus_utc - receiver_tdb_minus_utc;
        dot_one = rho_transmitter[1] * transmitter_rprime[1] + rho_transmitter[2] * transmitter_rprime[2] + rho_transmitter[3] * transmitter_rprime[3];
        dot_two = rho_receiver[1] * rprime_at_bounce[1] + rho_receiver[2] * rprime_at_bounce[2] + rho_receiver[3] * rprime_at_bounce[3];
        getPlanetPosVel(transmitter_time, 11, sun_r, sun_rprime);
        heliodist1 = Math.sqrt(Math.pow((transmitter_r[1] - sun_r[1]), 2) + Math.pow((transmitter_r[2] - sun_r[2]), 2) + Math.pow((transmitter_r[3] - sun_r[3]), 2));
        getPlanetPosVel(receiver_tdb_time, 11, sun_r, sun_rprime);
        heliodist2 = Math.sqrt(Math.pow((receiver_r[1] - sun_r[1]), 2) + Math.pow((receiver_r[2] - sun_r[2]), 2) + Math.pow((receiver_r[3] - sun_r[3]), 2));
        speed_transmitter = Math.sqrt(Math.pow(transmitter_rprime[1], 2) + Math.pow(transmitter_rprime[2], 2) + Math.pow(transmitter_rprime[3], 2));
        speed_receiver = Math.sqrt(Math.pow(receiver_rprime[1], 2) + Math.pow(receiver_rprime[2], 2) + Math.pow(receiver_rprime[3], 2));
        delay_doppler[2] = -transmitter_frequency * (rho_dot_t + rho_dot_r) / speed_of_light - (transmitter_frequency / Math.pow(speed_of_light, 2)) * (rho_dot_t * dot_one / dist_transmitter - rho_dot_r * dot_two / dist_receiver - rho_dot_t * rho_dot_r + Math.pow(kappa, 2) * (1.0 / heliodist1 - 1.0 / heliodist2) + 0.5 * (Math.pow(speed_transmitter, 2) - Math.pow(speed_receiver, 2)));

    }

    double radarCorrections(double jultime, double asteroid_r[], double site_r[], double frequency) {
        /*
          This method computes corrections to radar delay times due to general relativity, the solar corona, and the Earth's troposphere and ionosphere.
          Inputs:
            - jultime is a TDB Julian date corresponding to the epoch of site_r[];
            - asteroid_r[] and site_r[] are barycentric equatorial positions in AU; asteroid_r[] is evaluated at bounce_time, while site_r[] is evaluated at jultime (either transmitter_time or receiver_time);
            - frequency is in Hz;
          Outputs:
            - delay in seconds.
          Algorithm based on P. Kenneth Seidelmann, "Explanatory Supplement to the Astronomical Almanac", 1992, pp. 294-296.
        */

        double e = 0, p = 0, q = 0, delay1 = 0, delay2 = 0, delay3 = 0, delay = 0, s = 0, density = 0, bigA = 1.22E+08, r = 0, a = 0.44E+06, b = 0.44E+06, beta = 0, z = 0, mag_zenith = 0, mag_rho = 0;

        double[] rho = new double[4];
        double[] zenith = new double[4];

        int i = 0, j = 0;

        planetaryEphemeris(jultime, rho, 0);

        /* The time delay due to general relativity */
        e = Math.sqrt(Math.pow((site_r[1] - planet_r[0][1]), 2) + Math.pow((site_r[2] - planet_r[0][2]), 2) + Math.pow((site_r[3] - planet_r[0][3]), 2));
        p = Math.sqrt(Math.pow((asteroid_r[1] - planet_r[0][1]), 2) + Math.pow((asteroid_r[2] - planet_r[0][2]), 2) + Math.pow((asteroid_r[3] - planet_r[0][3]), 2));
        q = Math.sqrt(Math.pow((asteroid_r[1] - site_r[1]), 2) + Math.pow((asteroid_r[2] - site_r[2]), 2) + Math.pow((asteroid_r[3] - site_r[3]), 2));
        delay1 = 86400.0 * (2.0 * Math.pow(kappa, 2) / Math.pow(speed_of_light, 3)) * Math.log(Math.abs((e + p + q) / (e + p - q)));

        /* The time delay due to the solar corona */
        /* We will break this integral up into 100 slices from site to asteroid, and evaluate the density at each. */
        /* slice length s is in cm */
        s = (q / 100.0) * au * 100000.0;
        for (i = 1; i <= 100; i++) {
            for (j = 1; j <= 3; j++)
                rho[j] = (i / 100.0) * (asteroid_r[j] - site_r[j]) + (site_r[j] - planet_r[0][j]);
            beta = Math.asin(rho[3] / Math.sqrt(Math.pow(rho[1], 2) + Math.pow(rho[2], 2) + Math.pow(rho[3], 2)));
            r = Math.sqrt(Math.pow(rho[1], 2) + Math.pow(rho[2], 2) + Math.pow(rho[3], 2)) / planet_radius_0;
            density = bigA / Math.pow(r, 6) + (a * b / Math.sqrt(Math.pow((a * Math.sin(beta)), 2) + Math.pow((b * Math.cos(beta)), 2))) / Math.pow(r, 2);
            delay2 = delay2 + (40.3 / ((speed_of_light * au * 100000.0 / 86400.0) * Math.pow(frequency, 2))) * density * s;
        }

        /* The time delay due to the Earth's troposphere and ionosphere */
        for (i = 1; i <= 3; i++) {
            zenith[i] = site_r[i] - planet_r[3][i];
            rho[i] = asteroid_r[i] - site_r[i];
        }
        mag_zenith = Math.sqrt(Math.pow(zenith[1], 2) + Math.pow(zenith[2], 2) + Math.pow(zenith[3], 2));
        mag_rho = Math.sqrt(Math.pow(rho[1], 2) + Math.pow(rho[2], 2) + Math.pow(rho[3], 2));
        z = Math.acos((zenith[1] * rho[1] + zenith[2] * rho[2] + zenith[3] * rho[3]) / (mag_zenith * mag_rho));
        delay3 = 7E-09 / (Math.cos(z) + 0.0014 / (0.045 + 1.0 / Math.tan(z)));

        /* The output is the sum of the three delays */
        delay = delay1 + delay2 + delay3;

        return delay;

    }

    void getStateTransitionMatrix(double rnominal[], double rprimenominal[], double time, double phi[][]) {

        /*
            Method to calculate the state transition matrix at time, given the epoch state vector (available as an instance variable of the MinorPlanet object class) and the nominal state vector at time.
          */

        double[] epoch_rvariant = new double[4];
        double[] epoch_rprimevariant = new double[4];
        double[] rvariant = new double[4];
        double[] rprimevariant = new double[4];
        double[][] collision_variant = new double[2000][9];

        double delta = 0;

        int i = 0, j = 0;

        /* Initialize the variant state vector */
        for (i = 1; i <= 3; i++) {
            epoch_rvariant[i] = epoch_r[i];
            epoch_rprimevariant[i] = epoch_rprime[i];
        }

        /* Calculate the state transition matrix phi at time */
        for (i = 1; i <= 3; i++) {
            /* Vary epoch_r[i], and determine the change in state vector at time */
            delta = 0.000001 * epoch_r[i];
            epoch_rvariant[i] = epoch_r[i] + delta;
            update(epoch_rvariant, epoch_rprimevariant, epoch_time, rvariant, rprimevariant, time, false, collision_variant);
            for (j = 1; j <= 3; j++) {
                phi[j][i] = (rvariant[j] - rnominal[j]) / delta;
                phi[j + 3][i] = (rprimevariant[j] - rprimenominal[j]) / delta;
            }
            epoch_rvariant[i] = epoch_r[i];
            /* Vary epoch_rprime[i], and determine the change in state vector at time */
            delta = 0.000001 * epoch_rprime[i];
            epoch_rprimevariant[i] = epoch_rprime[i] + delta;
            update(epoch_rvariant, epoch_rprimevariant, epoch_time, rvariant, rprimevariant, time, false, collision_variant);
            for (j = 1; j <= 3; j++) {
                phi[j][i + 3] = (rvariant[j] - rnominal[j]) / delta;
                phi[j + 3][i + 3] = (rprimevariant[j] - rprimenominal[j]) / delta;
            }
            epoch_rprimevariant[i] = epoch_rprime[i];
            /* If non-gravitational thrust parameters A1_A2_DT are being utilized, vary A1_A2_DT[i], and determine the change in the state vector at time */
            if ((Math.abs(A1_A2_DT[i]) > 0.0) && (i < 3)) {
                delta = 0.1 * A1_A2_DT[i];
                A1_A2_DT[i] = A1_A2_DT[i] + delta;
                update(epoch_rvariant, epoch_rprimevariant, epoch_time, rvariant, rprimevariant, time, false, collision_variant);
                for (j = 1; j <= 3; j++) {
                    phi[j][i + 6] = (rvariant[j] - rnominal[j]) / delta;
                    phi[j + 3][i + 6] = (rprimevariant[j] - rprimenominal[j]) / delta;
                }
                phi[i + 6][i + 6] = 1.0;
                A1_A2_DT[i] = A1_A2_DT[i] - delta;
            }
        }

    }


    public void advanceEpoch(double newtime) {
        /*
            Method to advance the epoch state vector and error covariance matrix (accessible as instance variables of the class MinorPlanet) from epoch_time to another specified date (newtime).
            Note that the epoch state vector and error covariance matrix are actually changed here; thus, they are not sent back as arguments.
          */

        int i = 0, j = 0, k = 0;

        double[] newr = new double[4];
        double[] newrprime = new double[4];
        double[][] new_big_p = new double[9][9];
        double[][] phi = new double[9][9];
        double[][] inter = new double[9][9];
        double[][] collision_variant = new double[2000][9];

        /* Advance the epoch state vector */
        update(epoch_r, epoch_rprime, epoch_time, newr, newrprime, newtime, false, collision_variant);

        /* Advance the epoch error covariance matrix */
        getStateTransitionMatrix(newr, newrprime, newtime, phi);
        for (i = 1; i <= 8; i++) {
            for (j = 1; j <= 8; j++) {
                inter[i][j] = 0;
                for (k = 1; k <= 8; k++)
                    inter[i][j] = inter[i][j] + big_p[i][k] * phi[j][k];
            }
        }
        for (i = 1; i <= 8; i++) {
            for (j = 1; j <= 8; j++) {
                new_big_p[i][j] = 0;
                for (k = 1; k <= 8; k++)
                    new_big_p[i][j] = new_big_p[i][j] + phi[i][k] * inter[k][j];
            }
        }

        /* Update the state vector and error covariance matrix */
        epoch_time = newtime;
        for (i = 1; i <= 3; i++) {
            epoch_r[i] = newr[i];
            epoch_rprime[i] = newrprime[i];
        }
        for (i = 1; i <= 8; i++) {
            for (j = 1; j <= 8; j++)
                big_p[i][j] = new_big_p[i][j];
        }

    }


    double probabilityOfCollision(int planet, double r_at_time[], double rprime_at_time[], double time, double covariance[][], int collision_counter) {

        /*
            Method to determine the probability of collision between an object and a planet, given the posvel of the body at the time of closest approach.
            The method also uses the epoch time and posvel of the body, and its epoch error covariance matrix, which are assumed to be instance variables.  The method also permits use of the non-gravitational thrust parameters.
            Inputs:
              time = jultime of closest approach
              collision_counter = event counter for this call to update
              r_at_time = object barycentric equatorial position at time of closest approach
              rprime_at_time = object barycentric equatorial velocity at time of closest approach
              planet = number of the planet with which the object may collide
                     (Sun=0,Mercury=1,...,Pluto=9,Moon=10)
            Outputs:
              probability = probability of collision
              covariance = propogated covariance matrix at time

            References:
              D.K. Yeomans, S.J. Ostro, and P.W. Chodas (1987). "Radar Astrometry of Near-Earth Asteroids". Astronomical Journal 94, 189-200.
              Donald K. Yeomans and Paul W. Chodas (1995). "Near Earth Objects: Orbit Determination and Impact Prediction". JPL Technical Report 95-0032.
              Howard Anton (1984). "Elementary Linear Algebra". pp. 204-205.
              Mary L. Boas (1983). "Mathematical Methods in the Physical Sciences". pp. 723-726.
          */


        double[] rnominal = new double[4];
        double[] rprimenominal = new double[4];
        double[] rvariant = new double[4];
        double[] rprimevariant = new double[4];
        double[] epoch_rvariant = new double[4];
        double[] epoch_rprimevariant = new double[4];
        double[][] collision_variant = new double[2000][9];
        double[][] phi = new double[9][9];
        double[][] inter = new double[9][9];
        double[][] transition_matrix = new double[4][4];
        double[][] abc_covariance = new double[4][4];
        double[] rho = new double[4];
        double[] normal = new double[4];
        double[] d = new double[4];
        double[] r_project = new double[4];
        double[] vector_a = new double[4];
        double[] vector_b = new double[4];
        double[] vector_c = new double[4];

        double distance = 0, delta = 0, angle_cos = 0, angle_sin = 0, angle = 0, mag_normal = 0, mag_d = 0, mag_a = 0, probability = 0, probability1 = 0, probability2 = 0, probability3 = 0, start_time = 0;

        int i = 0, j = 0, k = 0;

        /* Note: epoch_time, epoch_r, and epoch_rprime are attributes of object (instance variables), hence available to all methods */
        /* IMPORTANT: update must have a boolean that controls whether we test for a collision.  Otherwise, we risk infinite regression */

        /* Initialize variant arrays and nominal state vector */
        for (i = 1; i <= 3; i++) {
            if (!VIflag1) {
                /* This is a standard search; variant trajectories should be centered on epoch state vector */
                epoch_rvariant[i] = epoch_r[i];
                epoch_rprimevariant[i] = epoch_rprime[i];
            } else {
                /* This is a VI search; variant trajectories should be centered on Monte Carlo state vector input to update */
                epoch_rvariant[i] = VI_trajectory_r[i];
                epoch_rprimevariant[i] = VI_trajectory_rprime[i];
            }
            rnominal[i] = r_at_time[i];
            rprimenominal[i] = rprime_at_time[i];
            collision_nominal_state[collision_counter][i] = rnominal[i];
            collision_nominal_state[collision_counter][i + 3] = rprimenominal[i];
        }

        /* Calculate the position of the target planet at time, for use in determining delta and the target plane */
        planetaryEphemeris(time, rho, 0);
        distance = Math.sqrt(Math.pow((r_at_time[1] - planet_r[planet][1]), 2.0) + Math.pow((r_at_time[2] - planet_r[planet][2]), 2.0) + Math.pow((r_at_time[3] - planet_r[planet][3]), 2.0));

        /* Calculate the state transition matrix phi at time */
        for (i = 1; i <= 3; i++) {
            /* Skip if a VI search has resulted in a variant collision */
            if (probability != -999.9) {
                /* If there is a previous collision/near miss event, we can start integrating where the variant trajectories left off.  Otherwise, we will establish new variant trajectories */
/*				delta = 0.000001*epoch_r[i];		*/
/*				delta = 0.000001*(distance/0.05);	*/
                delta = 0.0000001;
                if (collision_sensitivity_time == 0.0) {
                    if (!VIflag1) {
                        /* This is a standard search; variant trajectories should be centered on epoch state vector */
                        epoch_rvariant[i] = epoch_r[i] + delta;
                        start_time = epoch_time;
                    } else {
                        /* This is a VI search; variant trajectories should be centered on Monte Carlo state vector input to update */
                        epoch_rvariant[i] = VI_trajectory_r[i] + delta;
                        start_time = epoch_time;
                    }
                } else {
                    for (j = 1; j <= 3; j++) {
                        epoch_rvariant[j] = collision_sensitivity_r_variant[2 * i - 1][j];
                        epoch_rprimevariant[j] = collision_sensitivity_rprime_variant[2 * i - 1][j];
                    }
                    start_time = collision_sensitivity_time;
                }
                if (VIflag2) {
                    /* This is either a standard search, or a VI search in which the nominal trajectory is a VI; suppress collision detection for variant trajectories. */
                    update(epoch_rvariant, epoch_rprimevariant, start_time, rvariant, rprimevariant, time, false, collision_variant);
                } else {
                    /* This is a VI search in which the nominal trajectory was a near-miss; allow collision detection of the variant trajectories.  Set VIflag3 temporarily to prevent a recursive call back to probability_of_collision. */
                    VIflag3 = true;
                    update(epoch_rvariant, epoch_rprimevariant, start_time, rvariant, rprimevariant, time, true, collision_variant);
                    VIflag3 = false;
                    /* If the variant trajectory results in a collision, overwrite the current Monte Carlo trajectory with this variant VI and return probability = -999.9 (which will result in no further processing in this method).  After this method ends, will return to detect_collision which will set collision[1][1] to 1.0 and immediately end, returning finally to update.  Update will see the collision[1][1] and probability = -999.9, overwrite the current Monte Carlo trajectory with this variant VI, and end, returning to the VI search algorithm. */
                    if (collision_variant[(int) collision_variant[0][0]][1] == 1.0) {
                        /* Virtual Impactor */
                        probability = -999.9;
                        VI_trajectory_r[i] = VI_trajectory_r[i] + delta;
                    }
                }
                for (j = 1; j <= 3; j++) {
                    phi[j][i] = (rvariant[j] - rnominal[j]) / delta;
                    phi[j + 3][i] = (rprimevariant[j] - rprimenominal[j]) / delta;
                    collision_sensitivity_r_variant[2 * i - 1][j] = rvariant[j];
                    collision_sensitivity_rprime_variant[2 * i - 1][j] = rprimevariant[j];
                    collision_state_matrix[collision_counter][i][j] = (rvariant[j] - rnominal[j]) / delta;
                    collision_state_matrix[collision_counter][i][j + 3] = (rprimevariant[j] - rprimenominal[j]) / delta;
                }
                if (collision_sensitivity_time == 0.0) {
                    if (!VIflag1)
                        /* This is a standard search; variant trajectories should be centered on epoch state vector */
                        epoch_rvariant[i] = epoch_r[i];
                    else
                        /* This is a VI search; variant trajectories should be centered on Monte Carlo state vector input to update */
                        epoch_rvariant[i] = VI_trajectory_r[i];
                } else {
                    /* No action required; the arrays will be switched to a new set before the next calculation */
                }
            }
            /* Skip if a VI search has resulted in a variant collision */
            if (probability != -999.9) {
/*				delta = 0.000001*epoch_rprime[i];		*/
/*				delta = 0.0000001*(distance/0.05);		*/
                delta = 0.000000001;
                if (collision_sensitivity_time == 0.0) {
                    if (!VIflag1) {
                        /* This is a standard search; variant trajectories should be centered on epoch state vector */
                        epoch_rprimevariant[i] = epoch_rprime[i] + delta;
                    } else {
                        /* This is a VI search; variant trajectories should be centered on Monte Carlo state vector input to update */
                        epoch_rprimevariant[i] = VI_trajectory_rprime[i] + delta;
                    }
                } else {
                    for (j = 1; j <= 3; j++) {
                        epoch_rvariant[j] = collision_sensitivity_r_variant[2 * i][j];
                        epoch_rprimevariant[j] = collision_sensitivity_rprime_variant[2 * i][j];
                    }
                }
                if (VIflag2) {
                    /* This is either a standard search, or a VI search in which the nominal trajectory is a VI; suppress collision detection for variant trajectories. */
                    update(epoch_rvariant, epoch_rprimevariant, start_time, rvariant, rprimevariant, time, false, collision_variant);
                } else {
                    /* This is a VI search in which the nominal trajectory was a near-miss; allow collision detection of the variant trajectories.  Set VIflag3 temporarily to prevent a recursive call back to probability_of_collision. */
                    VIflag3 = true;
                    update(epoch_rvariant, epoch_rprimevariant, start_time, rvariant, rprimevariant, time, true, collision_variant);
                    VIflag3 = false;
                    /* If the variant trajectory results in a collision, overwrite the current Monte Carlo trajectory with this variant VI and return probability = -999.9 (which will result in no further processing in this method).  After this method ends, will return to detect_collision which will set collision[1][1] to 1.0 and immediately end, returning finally to update.  Update will see the collision[1][1] and probability = -999.9, overwrite the current Monte Carlo trajectory with this variant VI, and end, returning to the VI search algorithm. */
                    if (collision_variant[(int) collision_variant[0][0]][1] == 1.0) {
                        /* Virtual Impactor */
                        probability = -999.9;
                        VI_trajectory_rprime[i] = VI_trajectory_rprime[i] + delta;
                    }
                }
                for (j = 1; j <= 3; j++) {
                    phi[j][i + 3] = (rvariant[j] - rnominal[j]) / delta;
                    phi[j + 3][i + 3] = (rprimevariant[j] - rprimenominal[j]) / delta;
                    collision_sensitivity_r_variant[2 * i][j] = rvariant[j];
                    collision_sensitivity_rprime_variant[2 * i][j] = rprimevariant[j];
                    collision_state_matrix[collision_counter][i + 3][j] = (rvariant[j] - rnominal[j]) / delta;
                    collision_state_matrix[collision_counter][i + 3][j + 3] = (rprimevariant[j] - rprimenominal[j]) / delta;
                }
                if (collision_sensitivity_time == 0.0) {
                    if (!VIflag1)
                        /* This is a standard search; variant trajectories should be centered on epoch state vector */
                        epoch_rprimevariant[i] = epoch_rprime[i];
                    else
                        /* This is a VI search; variant trajectories should be centered on Monte Carlo state vector input to update */
                        epoch_rprimevariant[i] = VI_trajectory_rprime[i];
                } else {
                    /* No action required; the arrays will be switched to a new set before the next calculation */
                }
            }
            /* Skip if a VI search has resulted in a variant collision */
            if (probability != -999.9) {
                /* If non-gravitational thrust parameters A1_A2_DT are being utilized, vary A1_A2_DT[i], and determine the change in the state vector at time */
                if ((Math.abs(A1_A2_DT[i]) > 0.0) && (i < 3)) {
                    delta = 0.1 * A1_A2_DT[i];
                    A1_A2_DT[i] = A1_A2_DT[i] + delta;
                    if (collision_sensitivity_time > 0.0) {
                        for (j = 1; j <= 3; j++) {
                            epoch_rvariant[j] = collision_sensitivity_r_variant[i + 6][j];
                            epoch_rprimevariant[j] = collision_sensitivity_rprime_variant[i + 6][j];
                        }
                    }
                    if (VIflag2) {
                        /* This is either a standard search, or a VI search in which the nominal trajectory is a VI; suppress collision detection for variant trajectories. */
                        update(epoch_rvariant, epoch_rprimevariant, start_time, rvariant, rprimevariant, time, false, collision_variant);
                    } else {
                        /* This is a VI search in which the nominal trajectory was a near-miss; allow collision detection of the variant trajectories.  Set VIflag3 temporarily to prevent a recursive call back to probability_of_collision. */
                        VIflag3 = true;
                        update(epoch_rvariant, epoch_rprimevariant, start_time, rvariant, rprimevariant, time, true, collision_variant);
                        VIflag3 = false;
                    }
                    for (j = 1; j <= 3; j++) {
                        phi[j][i + 6] = (rvariant[j] - rnominal[j]) / delta;
                        phi[j + 3][i + 6] = (rprimevariant[j] - rprimenominal[j]) / delta;
                        collision_sensitivity_r_variant[i + 6][j] = rvariant[j];
                        collision_sensitivity_rprime_variant[i + 6][j] = rprimevariant[j];
                    }
                    phi[i + 6][i + 6] = 1.0;
                    A1_A2_DT[i] = A1_A2_DT[i] - delta;
                }
            }
        }

        /* Skip if a VI search has resulted in a variant collision */
        if (probability != -999.9) {

            /* Set the collision_sensitivity_time to the end time of integration, so we can pick up there if there is another event. */
            collision_sensitivity_time = time;

            /* Determine error covariance at time using epoch error covariance matrix big_p */
            /* Note that big_p is an attribute of object (instance variable), hence available to all methods */
            for (i = 1; i <= 8; i++) {
                for (j = 1; j <= 8; j++) {
                    inter[i][j] = 0;
                    for (k = 1; k <= 8; k++)
                        inter[i][j] = inter[i][j] + big_p[i][k] * phi[j][k];
                }
            }
            for (i = 1; i <= 6; i++) {
                for (j = 1; j <= 6; j++) {
                    covariance[i][j] = 0;
                    for (k = 1; k <= 8; k++)
                        covariance[i][j] = covariance[i][j] + phi[i][k] * inter[k][j];
                }
            }

            /* Define the "impact plane", i.e., the plane perpendicular to the planet's relative velocity vector which contains the planet's position at time. */
            for (i = 1; i <= 3; i++)
                normal[i] = rprime_at_time[i] - planet_rprime[planet][i];
            mag_normal = Math.sqrt(Math.pow(normal[1], 2) + Math.pow(normal[2], 2) + Math.pow(normal[3], 2));
            for (i = 1; i <= 3; i++) {
                normal[i] = normal[i] / mag_normal;
            }

            /* Find the vector d from the planet to the object, and the angle between d and the normal to the impact plane */
            for (i = 1; i <= 3; i++)
                d[i] = r_at_time[i] - planet_r[planet][i];
            mag_d = Math.sqrt(Math.pow(d[1], 2) + Math.pow(d[2], 2) + Math.pow(d[3], 2));
            for (i = 1; i <= 3; i++)
                d[i] = d[i] / mag_d;
            angle_cos = d[1] * normal[1] + d[2] * normal[2] + d[3] * normal[3];
            angle_sin = Math.sqrt(Math.pow((d[2] * normal[3] - d[3] * normal[2]), 2) + Math.pow((d[1] * normal[3] - d[3] * normal[1]), 2) + Math.pow((d[1] * normal[2] - d[2] * normal[1]), 2));
            angle = Math.atan2(angle_sin, angle_cos);

            /* Project the object position at time onto the impact plane */
            for (i = 1; i <= 3; i++) {
                r_project[i] = r_at_time[i] - mag_d * angle_cos * normal[i];
            }

            /* Create a new "a-b-c" coordinate system.  Basis vector a will be in the impact plane, with its direction defined by the vector from the projected object position to the planet.  Basis vector c will be the normal to the impact plane.  Basis vector b will be in the impact plane, defined so as to complete the right-handed system. */
            for (i = 1; i <= 3; i++) {
                vector_a[i] = planet_r[planet][i] - r_project[i];
                vector_c[i] = normal[i];
            }
            mag_a = Math.sqrt(Math.pow(vector_a[1], 2) + Math.pow(vector_a[2], 2) + Math.pow(vector_a[3], 2));
            for (i = 1; i <= 3; i++)
                vector_a[i] = vector_a[i] / mag_a;
            vector_b[1] = -(vector_a[2] * vector_c[3] - vector_a[3] * vector_c[2]);
            vector_b[2] = (vector_a[1] * vector_c[3] - vector_a[3] * vector_c[1]);
            vector_b[3] = -(vector_a[1] * vector_c[2] - vector_a[2] * vector_c[1]);

            /* Create the transition matrix, and transform the (position) error covariance matrix at time from "i-j-k" to "a-b-c".  Note that we will only transform the upper 3x3 of the covariance matrix; this is all we need for our purposes. */
            for (i = 1; i <= 3; i++) {
                transition_matrix[1][i] = vector_a[i];
                transition_matrix[2][i] = vector_b[i];
                transition_matrix[3][i] = vector_c[i];
            }
            for (i = 1; i <= 3; i++) {
                for (j = 1; j <= 3; j++) {
                    inter[i][j] = 0;
                    for (k = 1; k <= 3; k++)
                        inter[i][j] = inter[i][j] + covariance[i][k] * transition_matrix[j][k];
                }
            }
            for (i = 1; i <= 3; i++) {
                for (j = 1; j <= 3; j++) {
                    abc_covariance[i][j] = 0;
                    for (k = 1; k <= 3; k++)
                        abc_covariance[i][j] = abc_covariance[i][j] + transition_matrix[i][k] * inter[k][j];
                }
            }

            /*
                 Use the transformed covariances to determine the probability that the planet and the object physically overlap on the impact plane.  Note that this requires integrating the normal density function over the planet's disk.  However, we are making a pretty huge simplification already by projecting onto the "impact plane", in order to predict the probability of collision before or after the time of nominal closest approach.  Therefore, I feel justified in approximating this integral.
                 The integral would be over all a and b that cover the planet's disk.  I will approximate this numerically three different ways.  First, by evaluating the ndf at the planet's center and multiplying by the area of the planet's disk.  Second, by evaluating the ndf at four points along the a and b axes, and multiplying by area/4.  Third, by evaluating the ndf at four points along the a-b diagonals, and multiplying by area/4.  And finally, I will average these approximations to arrive at a final result.
                 You have to stop somewhere, and I feel that this covers the disk fairly well.
               */

            probability1 = Math.pow(planet_radius[planet], 2) * Math.exp(-Math.pow(mag_a, 2) / (2 * Math.abs(abc_covariance[1][1]))) / (2 * Math.sqrt(Math.abs(abc_covariance[1][1])) * Math.sqrt(Math.abs(abc_covariance[2][2])));
            probability2 = Math.pow(planet_radius[planet], 2) * (2 * Math.exp(-Math.pow(mag_a, 2) / (2 * Math.abs(abc_covariance[1][1]))) * Math.exp(-Math.pow(planet_radius[planet], 2) / (4 * Math.abs(abc_covariance[2][2]))) + Math.exp(-Math.pow((mag_a - planet_radius[planet] / Math.sqrt(2.0)), 2) / (2 * Math.abs(abc_covariance[1][1]))) + Math.exp(-Math.pow((mag_a + planet_radius[planet] / Math.sqrt(2.0)), 2) / (2 * Math.abs(abc_covariance[1][1])))) / (8 * Math.sqrt(Math.abs(abc_covariance[1][1])) * Math.sqrt(Math.abs(abc_covariance[2][2])));
            probability3 = Math.pow(planet_radius[planet], 2) * (2 * Math.exp(-Math.pow((mag_a - planet_radius[planet] / Math.sqrt(2.0)), 2) / (2 * Math.abs(abc_covariance[1][1]))) * Math.exp(-Math.pow((planet_radius[planet] / Math.sqrt(2.0)), 2) / (2 * Math.abs(abc_covariance[2][2]))) + 2 * Math.exp(-Math.pow((mag_a + planet_radius[planet] / Math.sqrt(2.0)), 2) / (2 * Math.abs(abc_covariance[1][1]))) * Math.exp(-Math.pow((planet_radius[planet] / Math.sqrt(2.0)), 2) / (2 * Math.abs(abc_covariance[2][2])))) / (8 * Math.sqrt(Math.abs(abc_covariance[1][1])) * Math.sqrt(Math.abs(abc_covariance[2][2])));

            probability = (probability1 + probability2 + probability3) / 3.0;

        }

        return probability;

    }

    void detectCollision(double object_r_at_n[], double object_rprime_at_n[], double object_r_at_n1[], double object_rprime_at_n1[], double jultime_n, double jultime_n1, double collision[][]) {

        /*
          Given the position and velocity of an object at jultime_n and jultime_n1, determine whether the object collides with a planet, or has a near miss with a planet, between time steps n and n+1 in procedure update.
          Inputs:
            object_r_at_n[], object_rprime_at_n[] = the barycentric equatorial posvel of the object at time step n;
            object_r_at_n1[], object_rprime_at_n1[] = the barycentric equatorial posvel of the object at time step n+1;
            jultime_n, jultime_n1 = the Julian dates corresponding to time steps n and n+1
          The output array "collision[][]" has two indices.  The first index accounts for multiple encounters, the total being stored in "collision[0][0]".  For the i'th encounter, entry "collision[i][1]" indicates whether a collision ( = 1) or a near miss ( = 2) occurs.  In either case,
            "collision[i][0]" gives the minimum distance in A.U.s.
            "collision[i][2]" gives the planet (1-9, Moon=10, Sun=0),
            "collision[i][3]" gives the Julian date,
            "collision[i][4]" gives the nominal distance in A.U.s.,
            "collision[i][5]" gives the estimated probability of collision,
            "collision[i][6]" gives the object's x-covariance at the time of the event,
            "collision[i][7]" gives the object's y-covariance at the time of the event, and
            "collision[i][8]" gives the object's z-covariance at the time of the event.
        */

        double[] rho = new double[4];
        double[][] planet_r_at_n = new double[11][4];
        double[][] planet_r_at_n1 = new double[11][4];
        double[][] planet_rprime_at_n = new double[11][4];
        double[][] planet_rprime_at_n1 = new double[11][4];
        double[] planet_i_r_at_n = new double[4];
        double[] planet_i_rprime_at_n = new double[4];
        double[] planet_i_r_at_time = new double[4];
        double[] planet_i_rprime_at_time = new double[4];
        double[] r_at_time = new double[4];
        double[] rprime_at_time = new double[4];
        double[] r_at_n = new double[4];
        double[] rprime_at_n = new double[4];
        double[] r_at_n1 = new double[4];
        double[] rprime_at_n1 = new double[4];
        double[][] covariance = new double[7][7];
        double[][] collision_variant = new double[2000][9];

        double distance_n1 = 0, upper_time = 0, lower_time = 0, time = 0, distance = 0, distance_n = 0, deriv_1_at_n = 0, deriv_1_at_n1 = 0, deriv_1_at_time = 0;

        int i = 0, j = 0, collision_counter = 0, counter = 0;


        collision_counter = (int) (collision[0][0]);

        /*  Get the ephemerides of the planets at each time step, without correction for light time-of-flight  */
        planetaryEphemeris(jultime_n, rho, 0);
        /* Initialize barycentric planetary posvel arrays at time step n */
        for (i = 0; i <= 10; i++) {
            for (j = 1; j <= 3; j++) {
                planet_r_at_n[i][j] = planet_r[i][j];
                planet_rprime_at_n[i][j] = planet_rprime[i][j];
            }
        }
        /* Initialize barycentric object posvel arrays at time step n */
        for (j = 1; j <= 3; j++) {
            r_at_n[j] = object_r_at_n[j];
            rprime_at_n[j] = object_rprime_at_n[j];
        }
        planetaryEphemeris(jultime_n1, rho, 0);
        /* Initialize barycentric planetary posvel arrays at time step n+1 */
        for (i = 0; i <= 10; i++) {
            for (j = 1; j <= 3; j++) {
                planet_r_at_n1[i][j] = planet_r[i][j];
                planet_rprime_at_n1[i][j] = planet_rprime[i][j];
            }
        }
        /* Initialize barycentric object posvel arrays at time step n+1 */
        for (j = 1; j <= 3; j++) {
            r_at_n1[j] = object_r_at_n1[j];
            rprime_at_n1[j] = object_rprime_at_n1[j];
        }

        /*  Check each planet (and the Sun) for collision  */
        for (i = 0; i <= 10; i++) {
            if ((!earth_only) || ((earth_only) && (i == 3))) {
                /*  Initialize barycentric posvel arrays for planet i  */
                for (j = 1; j <= 3; j++) {
                    planet_i_r_at_n[j] = planet_r_at_n[i][j];
                    planet_i_rprime_at_n[j] = planet_rprime_at_n[i][j];
                }
                /*  Check for collision at time step n+1.  */
                distance_n1 = Math.sqrt(Math.pow((object_r_at_n1[1] - planet_r_at_n1[i][1]), 2) + Math.pow((object_r_at_n1[2] - planet_r_at_n1[i][2]), 2) + Math.pow((object_r_at_n1[3] - planet_r_at_n1[i][3]), 2));

                if (distance_n1 <= planet_radius[i]) {
                    /*  Collision.  Increment collision counter and use bisection to find it.  */
                    collision_counter = collision_counter + 1;
                    collision[0][0] = collision[0][0] + 1;
                    upper_time = jultime_n1;
                    lower_time = jultime_n;
                    collision[collision_counter][0] = 0;
                    collision[collision_counter][1] = 1;
                    collision[collision_counter][2] = i;
                    collision[collision_counter][3] = jultime_n1;
                    collision[collision_counter][4] = distance_n1;
                    while ((Math.abs(upper_time - lower_time) > event_localization) && (counter < 100)) {
                        counter = counter + 1;
                        time = lower_time + (upper_time - lower_time) / 2;
                        /* Set VIflag5 to prevent this bisection call to update from resetting the VI_trajectory_r and _prime vectors */
                        VIflag5 = true;
                        update(r_at_n, rprime_at_n, jultime_n, r_at_time, rprime_at_time, time, false, collision_variant);
                        VIflag5 = false;
                        planetaryEphemeris(time, rho, 0);
                        for (j = 1; j <= 3; j++) {
                            planet_i_r_at_time[j] = planet_r[i][j];
                            planet_i_rprime_at_time[j] = planet_rprime[i][j];
                        }
                        distance = Math.sqrt(Math.pow((r_at_time[1] - planet_i_r_at_time[1]), 2) + Math.pow((r_at_time[2] - planet_i_r_at_time[2]), 2) + Math.pow((r_at_time[3] - planet_i_r_at_time[3]), 2));
                        if (distance <= planet_radius[i]) {
                            collision[collision_counter][3] = time;
                            collision[collision_counter][4] = distance;
                            upper_time = time;
                        } else
                            /*  Collision has not yet occurred.  Increase lower limit.  */
                            lower_time = time;
                    }
                    System.out.println("COLLISION with planet " + i + " at " + time + " distance = " + distance);
                    if (!VIflag3 && !VIflag4) {
                        /* At this point, we know that this is either a standard search, or it is a VI search and the (current) nominal trajectory is a VI; temporarily set VIflag2 to prevent probability_of_collision from attempting collision detection of the variant trajectories. */
                        VIflag2 = true;
                        collision[collision_counter][5] = probabilityOfCollision(i, r_at_time, rprime_at_time, time, covariance, collision_counter);
                        VIflag2 = false;
                        collision[collision_counter][6] = Math.sqrt(Math.abs(covariance[1][1]));
                        collision[collision_counter][7] = Math.sqrt(Math.abs(covariance[2][2]));
                        collision[collision_counter][8] = Math.sqrt(Math.abs(covariance[3][3]));
                        if (!VIflag1)
                            collision[collision_counter][0] = 3.0 * covarianceSecondEigenvalue(covariance);
                    } else {
                        /* If VIflag3, this is a VI search where nominal trajectory is a near-miss and variant trajectories are now being tested for collision; avoid a recursive call to probability_of_collision. */
                        collision[collision_counter][5] = 0;
                        collision[collision_counter][6] = 0;
                        collision[collision_counter][7] = 0;
                        collision[collision_counter][8] = 0;
                    }
                    if (VIflag4) {
                        /* If VIflag4, we want collision detection without probability_of_collision and its six variant trajectories.  We also want to archive the state vector at time. */
                        for (j = 1; j <= 3; j++) {
                            collision_nominal_state[collision_counter][j] = r_at_time[j];
                            collision_nominal_state[collision_counter][j + 3] = rprime_at_time[j];
                        }
                        collision[collision_counter][5] = 0;
                        collision[collision_counter][6] = 0;
                        collision[collision_counter][7] = 0;
                        collision[collision_counter][8] = 0;
                    }
                } else {
                    /* Not in collision at time step n+1; evaluate intermediate points */
                    /*  Calculate the first derivative of distance at time steps n and n+1; check for a minimum that is less than the planetary radius for collision, or less than near_miss_threshold for a near miss.  */
                    distance_n = Math.sqrt(Math.pow((object_r_at_n[1] - planet_r_at_n[i][1]), 2) + Math.pow((object_r_at_n[2] - planet_r_at_n[i][2]), 2) + Math.pow((object_r_at_n[3] - planet_r_at_n[i][3]), 2));
                    deriv_1_at_n = ((object_r_at_n[1] - planet_r_at_n[i][1]) * (object_rprime_at_n[1] - planet_rprime_at_n[i][1]) + (object_r_at_n[2] - planet_r_at_n[i][2]) * (object_rprime_at_n[2] - planet_rprime_at_n[i][2]) + (object_r_at_n[3] - planet_r_at_n[i][3]) * (object_rprime_at_n[3] - planet_rprime_at_n[i][3])) / distance_n;
                    deriv_1_at_n1 = ((object_r_at_n1[1] - planet_r_at_n1[i][1]) * (object_rprime_at_n1[1] - planet_rprime_at_n1[i][1]) + (object_r_at_n1[2] - planet_r_at_n1[i][2]) * (object_rprime_at_n1[2] - planet_rprime_at_n1[i][2]) + (object_r_at_n1[3] - planet_r_at_n1[i][3]) * (object_rprime_at_n1[3] - planet_rprime_at_n1[i][3])) / distance_n1;
                    if ((deriv_1_at_n < 0) && (deriv_1_at_n1 > 0) && (distance_n < (1.2 * near_miss_threshold))) {
                        /*  A relevant minimum has occurred.  Use bisection on the first derivative of distance to find it.  */
                        upper_time = jultime_n1;
                        lower_time = jultime_n;
                        while ((Math.abs(upper_time - lower_time) > event_localization) && (counter < 100)) {
                            counter = counter + 1;
                            time = lower_time + (upper_time - lower_time) / 2;
                            /* Set VIflag5 to prevent this bisection call to update from resetting the VI_trajectory_r and _prime vectors */
                            VIflag5 = true;
                            update(r_at_n, rprime_at_n, jultime_n, r_at_time, rprime_at_time, time, false, collision_variant);
                            VIflag5 = false;
                            planetaryEphemeris(time, rho, 0);
                            for (j = 1; j <= 3; j++) {
                                planet_i_r_at_time[j] = planet_r[i][j];
                                planet_i_rprime_at_time[j] = planet_rprime[i][j];
                            }
                            distance = Math.sqrt(Math.pow((r_at_time[1] - planet_i_r_at_time[1]), 2) + Math.pow((r_at_time[2] - planet_i_r_at_time[2]), 2) + Math.pow((r_at_time[3] - planet_i_r_at_time[3]), 2));
                            deriv_1_at_time = ((r_at_time[1] - planet_i_r_at_time[1]) * (rprime_at_time[1] - planet_i_rprime_at_time[1]) + (r_at_time[2] - planet_i_r_at_time[2]) * (rprime_at_time[2] - planet_i_rprime_at_time[2]) + (r_at_time[3] - planet_i_r_at_time[3]) * (rprime_at_time[3] - planet_i_rprime_at_time[3])) / distance;
                            if (deriv_1_at_time < 0)
                                /*  Minimum has not yet occurred.  Increase lower limit.  */
                                lower_time = time;
                            else
                                /*  Minimum has already occurred.  Reduce upper limit.  */
                                upper_time = time;
                        }
                        /*  We've now located the minimum to great accuracy.  Is it a collision, or a near miss?  */
                        if (distance <= planet_radius[i]) {
                            /*  Collision.  Increment collision counter and use bisection to find it.  */
                            collision_counter = collision_counter + 1;
                            collision[0][0] = collision[0][0] + 1;
                            upper_time = time;
                            lower_time = jultime_n;
                            collision[collision_counter][0] = 0;
                            collision[collision_counter][1] = 1;
                            collision[collision_counter][2] = i;
                            collision[collision_counter][3] = time;
                            collision[collision_counter][4] = distance;
                            while ((Math.abs(upper_time - lower_time) > event_localization) && (counter < 200)) {
                                counter = counter + 1;
                                time = lower_time + (upper_time - lower_time) / 2;
                                /* Set VIflag5 to prevent this bisection call to update from resetting the VI_trajectory_r and _prime vectors */
                                VIflag5 = true;
                                update(r_at_n, rprime_at_n, jultime_n, r_at_time, rprime_at_time, time, false, collision_variant);
                                VIflag5 = false;
                                planetaryEphemeris(time, rho, 0);
                                for (j = 1; j <= 3; j++) {
                                    planet_i_r_at_time[j] = planet_r[i][j];
                                    planet_i_rprime_at_time[j] = planet_rprime[i][j];
                                }
                                distance = Math.sqrt(Math.pow((r_at_time[1] - planet_i_r_at_time[1]), 2) + Math.pow((r_at_time[2] - planet_i_r_at_time[2]), 2) + Math.pow((r_at_time[3] - planet_i_r_at_time[3]), 2));
                                if (distance <= planet_radius[i]) {
                                    collision[collision_counter][3] = time;
                                    collision[collision_counter][4] = distance;
                                    upper_time = time;
                                } else
                                    /*  Collision has not yet occurred.  Increase lower limit.  */
                                    lower_time = time;
                            }
                            System.out.println("COLLISION with planet " + i + " at " + time + " distance = " + distance);
                            if (!VIflag3 && !VIflag4) {
                                /* At this point, we know that this is either a standard search, or it is a VI search and the (current) nominal trajectory is a VI; temporarily set VIflag2 to prevent probability_of_collision from attempting collision detection of the variant trajectories. */
                                VIflag2 = true;
                                collision[collision_counter][5] = probabilityOfCollision(i, r_at_time, rprime_at_time, time, covariance, collision_counter);
                                VIflag2 = false;
                                collision[collision_counter][6] = Math.sqrt(Math.abs(covariance[1][1]));
                                collision[collision_counter][7] = Math.sqrt(Math.abs(covariance[2][2]));
                                collision[collision_counter][8] = Math.sqrt(Math.abs(covariance[3][3]));
                                if (!VIflag1)
                                    collision[collision_counter][0] = 3.0 * covarianceSecondEigenvalue(covariance);
                            } else {
                                /* If VIflag3, this is a VI search where nominal trajectory is a near-miss and variant trajectories are now being tested for collision; avoid a recursive call to probability_of_collision. */
                                collision[collision_counter][5] = 0;
                                collision[collision_counter][6] = 0;
                                collision[collision_counter][7] = 0;
                                collision[collision_counter][8] = 0;
                            }
                            if (VIflag4) {
                                /* If VIflag4, we want collision detection without probability_of_collision and its six variant trajectories.  We also want to archive the state vector at time.  */
                                for (j = 1; j <= 3; j++) {
                                    collision_nominal_state[collision_counter][j] = r_at_time[j];
                                    collision_nominal_state[collision_counter][j + 3] = rprime_at_time[j];
                                }
                                collision[collision_counter][5] = 0;
                                collision[collision_counter][6] = 0;
                                collision[collision_counter][7] = 0;
                                collision[collision_counter][8] = 0;
                            }
                        } else if (Math.abs(distance - planet_radius[i]) < near_miss_threshold) {
                            /*  Near miss.  Increment collision counter and record data.  */
                            collision_counter = collision_counter + 1;
                            collision[0][0] = collision[0][0] + 1;
                            collision[collision_counter][1] = 2;
                            collision[collision_counter][2] = i;
                            collision[collision_counter][3] = time;
                            collision[collision_counter][4] = distance;
                            if (!VIflag3 && !VIflag4) {
                                /* We know that this is a near-miss.  If it is a standard search, temporarily set VIflag2 to prevent probability_of_collision from attempting collision detection of the variant trajectories.  My initial feeling was that, if it is a VI search, do not set VIflag2; probability_of_collision will attempt collision detection of the variant trajectories.  Upon further consideration, however, it is unlikely that the variants created by probability_of_collision would lie near the LOV, or have comparably minimized residuals.  Additionally, collision detection takes time.  I'll therefore set VIflag2 in all cases.  */
                                /*	if (!VIflag1) VIflag2 = true;		*/
                                VIflag2 = true;
                                collision[collision_counter][5] = probabilityOfCollision(i, r_at_time, rprime_at_time, time, covariance, collision_counter);
                                VIflag2 = false;
                                collision[collision_counter][6] = Math.sqrt(Math.abs(covariance[1][1]));
                                collision[collision_counter][7] = Math.sqrt(Math.abs(covariance[2][2]));
                                collision[collision_counter][8] = Math.sqrt(Math.abs(covariance[3][3]));
                                if (!VIflag1)
                                    collision[collision_counter][0] = 3.0 * covarianceSecondEigenvalue(covariance);
                                if (collision[collision_counter][0] < 0.0)
                                    collision[collision_counter][0] = 0.0;
                                if (collision[collision_counter][5] == -999.9) {
                                    /* A variant trajectory has resulted in an impact.  Reset the number of events and the probability so update will note the event and overwrite the Monte Carlo trajectory with the variant VI. */
                                    collision[0][0] = 1;
                                    collision[1][1] = 1;
                                    collision[1][5] = -999.9;
                                }
                            } else {
                                /* If VIflag3, this is a VI search where nominal trajectory is a near-miss and variant trajectories are now being tested for collision; avoid a recursive call to probability_of_collision. */
                                collision[collision_counter][5] = 0;
                                collision[collision_counter][6] = 0;
                                collision[collision_counter][7] = 0;
                                collision[collision_counter][8] = 0;
                            }
                            if (VIflag4) {
                                /* If VIflag4, we want collision detection without probability_of_collision and its six variant trajectories.  We also want to archive the state vector at time.  */
                                for (j = 1; j <= 3; j++) {
                                    collision_nominal_state[collision_counter][j] = r_at_time[j];
                                    collision_nominal_state[collision_counter][j + 3] = rprime_at_time[j];
                                }
                                collision[collision_counter][5] = 0;
                                collision[collision_counter][6] = 0;
                                collision[collision_counter][7] = 0;
                                collision[collision_counter][8] = 0;
                            }
                        }
                    }
                }
            }
        }
    }


    public void getMinorPlanetStateVector(double classical_elements[], double time1, double r[], double rprime[]) {

        /*
           Method to convert the classical heliocentric ecliptic elements of a minor planet at TDB time1 into a barycentric equatorial state vector at that same epoch.
           Inputs:
              classical elements[1] - a in AU
                          [2] - e
                          [3] - i in radians
                          [4] - w in radians
                          [5] - omega in radians
                          [6] - T expressed as a TDB julian day
                          [7] - mean anomaly in radians
                          [8] - q in AU
              time1 - TDB epoch to which the elements refer, expressed as a julday
          Outputs:
              r, rprime - barycentric equatorial position/velocity of the body at epoch
          Tested and verified 10 August 1999.
          */

        double[] sun_r = new double[4];
        double[] sun_rprime = new double[4];

        int i = 0;

        /* First, convert classical heliocentric ecliptic elements at TDB time1 to pos/vel */
        convertKeplerianElementsToStateVector(classical_elements, r, rprime, time1);

        /* Two-body routines function in modified time; convert velocity to AU/day */
        for (i = 1; i <= 3; i++)
            rprime[i] = 0.01720209895 * rprime[i];

        /* Rotate asteroid heliocentric state vector from ecliptic to equatorial */
        TimeUtils.convertEclipticToEquatorial(r);
        TimeUtils.convertEclipticToEquatorial(rprime);

        /* Get Sun's position at TDB time1 */
        getPlanetPosVel(time1, 11, sun_r, sun_rprime);

        /* Add Sun's posvel to convert asteroid state vector from helio- to barycentric */
        for (i = 1; i <= 3; i++) {
            r[i] = r[i] + sun_r[i];
            rprime[i] = rprime[i] + sun_rprime[i];
        }


    }

    void getObservedRadec(double state_r[], double state_rprime[], double time1, boolean geocentric, double latitude, double longitude, double altitude, double ut1_minus_tai, double observer_r[], double observer_rprime[], double time2, double radec[]) {

        /*
            Method that uses the barycentric equatorial state vector of a body at TDB time1 to calculate its observed radec from a site at TDT time2.
            Inputs: state_r and state_rprime in AU and AU/day
                      time1 is a TDB julian day
                  geocentric is a boolean indicating whether the radec should be geocentric or relative to latitude/longitude/altitude
                    latitude and longitude are in radians, with longitude measured eastward
                    altitude is in meters
                  ut1_minus_tai is in seconds
                    observer_r and observer_rprime are arrays containing the barycentric equatorial rectangular position and velocity of the observing site, in AU and AU/day
                        time2 is a TDT julian day
            Output: radec is an array containing ra and dec, in radians
            Tested and verified 10 August 1999.
          */

        /*
           This method conforms to the outline of "Explanatory Supplement to the Astronomical Almanac", 1992, section 3.31 (Apparent-Place Algorithm for Planets).
           Note: time1 is the TDB epoch of the asteroid's orbital elements
                 time2 is the TDT time of observation on Earth
          */

        double dist = 0, time3 = 0, ra = 0, dec = 0, lat = 0, longit = 0;
        double rho_dot = 0, r_mag = 0, r_dot = 0, oldtime3 = 10;
        double dist_u = 0, dist_e = 0, dist_q = 0, tau = 0;

        double[] EOP = new double[8];
        double[] r = new double[4];
        double[] rprime = new double[4];
        double[] r2 = new double[4];
        double[] r2prime = new double[4];
        double[] r3 = new double[4];
        double[] r3prime = new double[4];
        double[] rho = new double[4];
        double[] latlong = new double[3];
        double[] vector_u = new double[4];
        double[] vector_q = new double[4];
        double[] vector_e = new double[4];
        double[][] collision = new double[2000][9];
        double[] sun_r = new double[4];
        double[] sun_rprime = new double[4];

        int i = 0, j = 0, counter = 0;

        /* Section 3.311 */
        /* Convert TDT time2 to its TDB equivalent; if (UT1-TAI) is zero (unknown), tdt_to_tdb algorithm will estimate it */
        time2 = TimeUtils.convertTDTtoTDB(time2, latitude, longitude, altitude, ut1_minus_tai);

        /* Section 3.312 */
        if (geocentric == true) {
            /* Geocentric case - get Earth barycentric equatorial position and velocity at TDB time2 */
            planetaryEphemeris(time2, rho, 0);
            for (i = 1; i <= 3; i++) {
                observer_r[i] = planet_r[3][i];
                observer_rprime[i] = planet_rprime[3][i];
            }
        } else {
            /* Topocentric case */
            /* The observer coordinates determined when the observation was entered may have been passed as an argument; these may have used precise values of the EOPs, so we don't want to recalculate.  On the other hand, the observer coordinates may be zero; this method may be creating an ephemeris, so precise values of the EOPs may not be available.  We'll test and act accordingly.  */
            if (observer_r[1] == 0.0) {
                /* Observer coordinates are unavailable; calculate them. */
                /* Set EOPs to zero so that method get_observer_position will use estimates and defaults.  However, allow for the use of ut1_minus_tai if it is provided. */
                for (i = 1; i <= 7; i++)
                    EOP[i] = 0.0;
                EOP[3] = ut1_minus_tai;
                /* get observer barycentric equatorial position and velocity at TDB time2 */
                getObserverPosition(latitude, longitude, altitude, time2, EOP, observer_r, observer_rprime);
            }
            /* If observer_r[1] is not equal to 0.0, observer coordinates are available; use them. */
        }

        /* Create and normalize Earth heliocentric position vector at TDB time2 */
        getPlanetPosVel(time2, 11, sun_r, sun_rprime);
        for (i = 1; i <= 3; i++)
            vector_e[i] = observer_r[i] - sun_r[i];
        dist_e = Math.sqrt(Math.pow(vector_e[1], 2) + Math.pow(vector_e[2], 2) + Math.pow(vector_e[3], 2));
        for (i = 1; i <= 3; i++)
            vector_e[i] = vector_e[i] / dist_e;

        /* Section 3.313 */
        /* Get asteroid barycentric equatorial state vector at time of observation TDB time2 */
        update(state_r, state_rprime, time1, r3, r3prime, time2, false, collision);

        /* Sections 3.314 and 3.315 */
        /* Iterative correction for light time-of-flight */
        for (i = 1; i <= 3; i++) {
            /* r[] and rprime[] will represent state vector at time3, the time at which light left asteroid to be observed on Earth at time2 */
            r[i] = r3[i];
            rprime[i] = r3prime[i];
        }
        /* Create and normalize the line-of-sight vector from observer at time2 to asteroid at time3 */
        for (i = 1; i <= 3; i++)
            vector_u[i] = r[i] - observer_r[i];
        dist_u = Math.sqrt(Math.pow(vector_u[1], 2) + Math.pow(vector_u[2], 2) + Math.pow(vector_u[3], 2));

        /* First approximation: time3 = time2 - dist_u/speed_of_light */
        time3 = time2 - dist_u / speed_of_light;

        /* Get asteroid barycentric equatorial state vector at TDB time3 */
        update(r3, r3prime, time2, r, rprime, time3, false, collision);

        while ((Math.abs(time3 - oldtime3) > 1e-6) && (counter < 100)) {
            oldtime3 = time3;
            counter = counter + 1;

            /* Create and normalize the line-of-sight vector from observer at time2 to asteroid at time3 */
            for (i = 1; i <= 3; i++)
                vector_u[i] = r[i] - observer_r[i];
            dist_u = Math.sqrt(Math.pow(vector_u[1], 2) + Math.pow(vector_u[2], 2) + Math.pow(vector_u[3], 2));
            for (i = 1; i <= 3; i++)
                vector_u[i] = vector_u[i] / dist_u;

            /* Create and normalize asteroid's heliocentric position vector at TDB time3 */
            getPlanetPosVel(time3, 11, sun_r, sun_rprime);
            for (i = 1; i <= 3; i++)
                vector_q[i] = r[i] - sun_r[i];
            dist_q = Math.sqrt(Math.pow(vector_q[1], 2) + Math.pow(vector_q[2], 2) + Math.pow(vector_q[3], 2));
            for (i = 1; i <= 3; i++)
                vector_q[i] = vector_q[i] / dist_q;

            /* Recalculate time3 based on updated estimate of light time-of-flight */
            tau = dist_u / speed_of_light + (2 * Math.pow(kappa, 2) / Math.pow(speed_of_light, 3)) * Math.log((dist_e + dist_u + dist_q) / (dist_e - dist_u + dist_q));
            time3 = time2 - tau;

            /* Find asteroid position at time3 */
            update(r3, r3prime, time2, r, rprime, time3, false, collision);

        }

        /* Section 3.3110 */
        /* Calculate ra/dec */
        radec[2] = Math.atan2(vector_u[3], Math.sqrt(Math.pow(vector_u[1], 2) + Math.pow(vector_u[2], 2)));
        radec[1] = Math.atan2(vector_u[2], vector_u[1]);


    }

    public void update(double r1[], double r1prime[], double time1, double r2[], double r2prime[], double time2, boolean check_for_collision, double collision[][]) {

        /*
          Procedure to calculate the position and velocity of a body at time2 given the position and velocity of the body at time1.  The Cowell method is used, with integration performed using Runge-Kutta Prince-Dormand 8(7).  Local error per unit step is controlled by variable step size.
          Positions and velocities are given in A.U.s and A.U.s/day, while the times are Julian dates.
          The procedure also checks for collisions with planets, if the Boolean check_for_collision is set.  The output array "collision[][]" has two indices.  The first index accounts for multiple encounters from time1 to time2, the total being stored in "collision[0][0]".  For the i'th encounter, entry "collision[i][1]" indicates whether a collision ( = 1) or a near miss ( = 2) occurs.  In either case,
            "collision[i][2]" gives the planet (1-9, Moon=10, Sun=0),
            "collision[i][3]" gives the Julian date,
            "collision[i][4]" gives the nominal distance in A.U.s.,
            "collision[i][5]" gives the estimated probability of collision,
            "collision[i][6]" gives the object's x-covariance at the time of the event,
            "collision[i][7]" gives the object's y-covariance at the time of the event, and
            "collision[i][8]" gives the object's z-covariance at the time of the event.
            "collision[i][0]" gives the semi-width of the 3-sigma uncertainty region in A.U.s.
          Reference for the Cowell method is Bate, Mueller, and White, "Fundamentals of Astrodynamics", pp 386-390.
          Reference for RK dopri 8(7) is P.J. Prince and J.R. Dormand, "High-order embedded Runge-Kutta formulae", J. Comp. Appl. Math., vol 7, 1981, p. 67-75.
          The error control is adapted from the class notes of Math 551 taught at The Pennsylvania State University during Fall 1989 by Prof. Douglas Arnold.
        */

        double[] fatn = new double[4];
        double[] r = new double[4];
        double[] rprime = new double[4];
        double[] r_at_n = new double[4];
        double[] rprime_at_n = new double[4];
        double[] fatnp1 = new double[4];
        double[] rho = new double[4];

        double toler = 0, h = 0, n1time = 0, ntime = 0, localerror = 0, ratio = 0, test_distance = 0, min_distance = 0;

        int i = 0, j = 0, k = 0, istep = 0, collision_counter = 0, counter = 0, counter_limit = 0, candidate = 0;

        /*  Initialize output collision array  */
        for (i = 0; i <= 1999; i++) {
            for (j = 0; j <= 8; j++) {
                collision[i][j] = 0;
            }
        }

        if (time1 == time2) {
            /* Return the state vector unchanged */
            for (k = 1; k <= 3; k++) {
                r2[k] = r1[k];
                r2prime[k] = r1prime[k];
            }
        } else {

            /*  Set local error per unit step tolerance  */
            toler = integration_error / Math.abs(time2 - time1);

            /* Determine the maximum number of attempted steps to be permitted */
            /* counter_limit = (int)(Math.abs(time2 - time1)/0.01); */
            counter_limit = 100;

            /*  Initialize variables for first time step  */
            /*  istep is the step being calculated */
            istep = 1;
            /*  Set initial step size  */
            h = time2 - time1;
            /*  Limit initial h to +/- 20 days */
            if (Math.abs(h) > 20.0)
                h = 20.0 * h / Math.abs(h);
            /*  n1time is the time at the (n+1)'st step, ntime is the time at the n'th step  */
            n1time = time1;
            ntime = time1;
            /* Initialize position, velocity, and acceleration vectors */
            for (k = 1; k <= 3; k++) {
                r[k] = r1[k];
                r_at_n[k] = r1[k];
                rprime[k] = r1prime[k];
                rprime_at_n[k] = r1prime[k];
                /* During VI search, a great many Monte Carlo trajectories are created in a normal distribution about the nominal epoch state vector; differential correction then attempts to force a VI.  In calculating the differential partial derivatives, probability_of_collision must create variant trajectories centered on the Monte Carlo trajectory, rather than the nominal epoch state vector.  Thus, we want to save the Monte Carlo trajectory for use by probability_of_collision.  */
                if ((VIflag1) && (!VIflag2) && (!VIflag3) && (!VIflag5)) {
                    VI_trajectory_r[k] = r1[k];
                    VI_trajectory_rprime[k] = r1prime[k];
                }
            }
            getAcceleration(ntime, r_at_n, rprime_at_n, fatn);

            /*  Begin integration loop  */
            integrationloop:
            while ((((n1time < time2) && (time2 > time1)) || ((n1time > time2) && (time2 < time1))) && (collision[collision_counter][1] != 1.0) && (counter < counter_limit)) {
                /* So long as we've not reached time2 or slammed into a planet, ... */

                /*  Increment time to the (n+1)'st step  */
                n1time = ntime + h;
                counter = counter + 1;
/* if (Math.abs(h) < 0.01) System.out.println("step size = " + h);  */
/* System.out.println("step size = " + h); */

                /*  Integrate to get r and rprime at the (n+1)'st time step  */
                /*  Use Runge-Kutta Dormand-Prince 8(7)  */
                localerror = dopri8(ntime, h, fatn, rprime_at_n, r_at_n, rprime, r);

                /* If a variant trajectory collides with a planet, the error may go to infinite or NaN.  If so, end the integration. */
                if (!(Math.abs(localerror) < 1e+10)) counter = counter_limit;

                /*  Compute ratio of projected new step size to present step size  */
                if (Math.abs(localerror / h) > 0.0)
                    /*  Avoid a division by zero for small step sizes  */
                    ratio = .8 * Math.pow(Math.abs(toler / (localerror / h)), (1.0 / 7.0));
                else
                    /*  Error and step size are very small; allow step size to double */
                    ratio = 2;

                /*  Test to see if step will be accepted; if already at minimum step size, ignore  */
                if ((Math.abs(localerror / h) > toler) && (Math.abs(h) > 0.001)) {
                    /*  Step has failed  */
                    /*  Determine a new step size  */
                    if ((ratio < .2) && (istep > 1))
                        ratio = .2;
                    /*  Note that new step size is limited to one-fifth of the previous one, except on the first step, which allows faster determination of an initial step size  */
                    n1time = ntime;
                    h = ratio * h;
                    /* Enforce minimum step size to prevent ultrasmall steps near collision  */
                    if (Math.abs(h) < 0.001) h = 0.001 * (h / Math.abs(h));
                    continue integrationloop;
                }

                /*  Step has been accepted, or we're at the minimum step size.  In either case, reset the counter.  */
                counter = 0;

                /* Check for collision, if so desired  */
                /* Note: This check for collision must occur after a step has been accepted; otherwise, the check will be made more than once for failed steps. */
                if (check_for_collision == true) {
/* TESTING PURPOSES ONLY!! */
/* System.out.println("check for collision jultime = " + ntime); */
/* TESTING PURPOSES ONLY!! */
                    detectCollision(r_at_n, rprime_at_n, r, rprime, ntime, n1time, collision);
                    collision_counter = (int) (collision[0][0]);
                    if (collision[1][5] == -999.9) {
                        /* During a VI search, a variant trajectory has resulted in an impact.  Overwrite the Monte Carlo trajectory with the variant VI. Since detect_collision has set collision[1][1] = 1.0, this method will end, and we will return to the VI search algorithm. */
                        for (k = 1; k <= 3; k++) {
                            r1[k] = VI_trajectory_r[k];
                            r1prime[k] = VI_trajectory_rprime[k];
                        }
                    }
                }

                if ((check_for_collision == false) || ((check_for_collision == true) && (collision[collision_counter][1] != 1.0))) {

                    /*  Determine a new step size  */
                    if (ratio > 2)
                        ratio = 2;
                    /*  Note that new step size is limited to double the previous one  */
                    h = ratio * h;
                    /* Enforce minimum step size to prevent ultrasmall steps near collision  */
                    if (Math.abs(h) < 0.001) h = 0.001 * (h / Math.abs(h));

                    /*  Calculate second derivative of r at time n+1  */
                    getAcceleration(n1time, r, rprime, fatnp1);

                    /*  Increment step counter  */
                    istep = istep + 1;

                    /*  Check to prevent overrunning endpoint of integration  */
                    if (((((n1time + h) > time2) && (time2 > time1)) || (((n1time + h) < time2) && (time2 < time1))) && (n1time != time2))
                        /*  Reduce step size to avoid overrunning endpoint of integration  */
                        h = time2 - n1time;

                    /* If a variant trajectory collides with a planet, the acceleration may go to infinite or NaN.  If so, end the integration. */
                    if (!(Math.abs(fatnp1[1]) < 1e+10) || !(Math.abs(fatnp1[2]) < 1e+10) || !(Math.abs(fatnp1[3]) < 1e+10))
                        counter = counter_limit;

                    /*  Update integration variables */
                    ntime = n1time;
                    for (k = 1; k <= 3; k++) {
                        r_at_n[k] = r[k];
                        rprime_at_n[k] = rprime[k];
                        fatn[k] = fatnp1[k];
                    }

                }

            }

            if (!(counter < counter_limit)) {
                System.out.println("WARNING: update exceeds max steps");
                /* Proceeding on the assumption that the excessively high local error was caused by an imminent collision, make a guess as to which planet was responsible, and populate the collision array accordingly */
                planetaryEphemeris(n1time, rho, 0);
                min_distance = 1;
                for (k = 0; k <= 10; k++) {
                    test_distance = Math.sqrt(Math.pow((r[1] - planet_r[k][1]), 2) + Math.pow((r[2] - planet_r[k][2]), 2) + Math.pow((r[3] - planet_r[k][3]), 2));
                    if (test_distance < min_distance) {
                        min_distance = test_distance;
                        candidate = k;
                    }
                }
                if (min_distance < 0.01) {
                    collision[0][0] = collision[0][0] + 1;
                    collision[(int) collision[0][0]][0] = 0;
                    collision[(int) collision[0][0]][1] = 1;
                    collision[(int) collision[0][0]][2] = candidate;
                    collision[(int) collision[0][0]][3] = n1time;
                    collision[(int) collision[0][0]][4] = min_distance;
                    collision[(int) collision[0][0]][5] = 0;
                    collision[(int) collision[0][0]][6] = 0;
                    collision[(int) collision[0][0]][7] = 0;
                    collision[(int) collision[0][0]][8] = 0;
                }
            }

            /*  Integration complete; return new state vector, and reset collision_sensitivity state vectors, if necessary */
            for (k = 1; k <= 3; k++) {
                r2[k] = r[k];
                r2prime[k] = rprime[k];
                if ((check_for_collision == true) && (!VIflag3)) {
                    /* Don't reset the variant trajectories if a variant is being checked for collision during VI search */
                    for (j = 1; j <= 8; j++) {
                        collision_sensitivity_r_variant[j][k] = 0;
                        collision_sensitivity_rprime_variant[j][k] = 0;
                    }
                    collision_sensitivity_time = 0;
                }
            }
        }
    }

    double dopri8(double ntime, double h, double fatn[], double rprimen[], double rn[], double rprime[], double r[]) {

        /*
          Procedure to integrate the equations of motion (Cowell's method) from ntime to ntime+h using the Runge-Kutta-type Prince-Dormand 8(7) method (see procedure update).  Also finds local error in the integration step, which is returned.
          Reference: P.J. Prince and J.R. Dormand, "High-order embedded Runge-Kutta formulae", J. Comp. Appl. Math., vol 7, 1981, p. 67-75.  Also, E. Hairer, S.P. Norsett, G. Wanner, "Solving Ordinary Differential Equations I (Non-stiff problems), Springer-Verlag, 1987, pp. 132-133, 167-171, 193-195, and 260.
        */

        double[][] dopri8_a = new double[14][14];
        double[][] dopri8_kprime = new double[14][4];
        double[][] dopri8_k = new double[14][4];
        double[] dopri8_c = new double[14];
        double[] dopri8_b = new double[14];
        double[] dopri8_bhat = new double[14];
        double[] low_r = new double[4];
        double[] rvariant = new double[4];
        double[] rprimevariant = new double[4];
        double[] kprime_temp = new double[4];

        double localerror = 0;

        int i = 0, j = 0, n = 0;

        /*  Initialize arrays  */

        dopri8_a[2][1] = 1.0 / 18.0;
        dopri8_a[3][1] = 1.0 / 48.0;
        dopri8_a[3][2] = 1.0 / 16.0;
        dopri8_a[4][1] = 1.0 / 32.0;
        dopri8_a[4][2] = 0;
        dopri8_a[4][3] = 3.0 / 32.0;
        dopri8_a[5][1] = 5.0 / 16.0;
        dopri8_a[5][2] = 0;
        dopri8_a[5][3] = -75.0 / 64.0;
        dopri8_a[5][4] = 75.0 / 64.0;
        dopri8_a[6][1] = 3.0 / 80.0;
        dopri8_a[6][2] = 0;
        dopri8_a[6][3] = 0;
        dopri8_a[6][4] = 3.0 / 16.0;
        dopri8_a[6][5] = 3.0 / 20.0;
        dopri8_a[7][1] = 29443.841 / 614563.906;
        dopri8_a[7][2] = 0;
        dopri8_a[7][3] = 0;
        dopri8_a[7][4] = 77736.538 / 692538.347;
        dopri8_a[7][5] = -28693.883 / 1125000.000;
        dopri8_a[7][6] = 23124.283 / 1800000.000;
        dopri8_a[8][1] = 16016.141 / 946692.911;
        dopri8_a[8][2] = 0;
        dopri8_a[8][3] = 0;
        dopri8_a[8][4] = 61564.180 / 158732.637;
        dopri8_a[8][5] = 22789.713 / 633445.777;
        dopri8_a[8][6] = 545815.736 / 2771057.229;
        dopri8_a[8][7] = -180193.667 / 1043307.555;
        dopri8_a[9][1] = 39632.708 / 573591.083;
        dopri8_a[9][2] = 0;
        dopri8_a[9][3] = 0;
        dopri8_a[9][4] = -433636.366 / 683701.615;
        dopri8_a[9][5] = -421739.975 / 2616292.301;
        dopri8_a[9][6] = 100302.831 / 723423.059;
        dopri8_a[9][7] = 790204.164 / 839813.087;
        dopri8_a[9][8] = 800635.310 / 3783071.287;
        dopri8_a[10][1] = 246121.993 / 1340847.787;
        dopri8_a[10][2] = 0;
        dopri8_a[10][3] = 0;
        dopri8_a[10][4] = -37695042.795 / 15268766.246;
        dopri8_a[10][5] = -309121.744 / 1061227.803;
        dopri8_a[10][6] = -12992.083 / 490766.935;
        dopri8_a[10][7] = 6005943.493 / 2108947.869;
        dopri8_a[10][8] = 393006.217 / 1396673.457;
        dopri8_a[10][9] = 123872.331 / 1001029.789;
        dopri8_a[11][1] = -1028468.189 / 846180.014;
        dopri8_a[11][2] = 0;
        dopri8_a[11][3] = 0;
        dopri8_a[11][4] = 8478235.783 / 508512.852;
        dopri8_a[11][5] = 1311729.495 / 1432422.823;
        dopri8_a[11][6] = -10304129.995 / 1701304.382;
        dopri8_a[11][7] = -48777925.059 / 3047939.560;
        dopri8_a[11][8] = 15336726.248 / 1032824.649;
        dopri8_a[11][9] = -45442868.181 / 3398467.696;
        dopri8_a[11][10] = 3065993.473 / 597172.653;
        dopri8_a[12][1] = 185892.177 / 718116.043;
        dopri8_a[12][2] = 0;
        dopri8_a[12][3] = 0;
        dopri8_a[12][4] = -3185094.517 / 667107.341;
        dopri8_a[12][5] = -477755.414 / 1098053.517;
        dopri8_a[12][6] = -703635.378 / 230739.211;
        dopri8_a[12][7] = 5731566.787 / 1027545.527;
        dopri8_a[12][8] = 5232866.602 / 850066.563;
        dopri8_a[12][9] = -4093664.535 / 808688.257;
        dopri8_a[12][10] = 3962137.247 / 1805957.418;
        dopri8_a[12][11] = 65686.358 / 487910.083;
        dopri8_a[13][1] = 403863.854 / 491063.109;
        dopri8_a[13][2] = 0;
        dopri8_a[13][3] = 0;
        dopri8_a[13][4] = -5068492.393 / 434740.067;
        dopri8_a[13][5] = -411421.997 / 543043.805;
        dopri8_a[13][6] = 652783.627 / 914296.604;
        dopri8_a[13][7] = 11173962.825 / 925320.556;
        dopri8_a[13][8] = -13158990.841 / 6184727.034;
        dopri8_a[13][9] = 3936647.629 / 1978049.680;
        dopri8_a[13][10] = -160528.059 / 685178.525;
        dopri8_a[13][11] = 248638.103 / 1413531.060;
        dopri8_a[13][12] = 0;
        dopri8_c[1] = 0;
        dopri8_c[2] = 1.0 / 18.0;
        dopri8_c[3] = 1.0 / 12.0;
        dopri8_c[4] = 1.0 / 8.0;
        dopri8_c[5] = 5.0 / 16.0;
        dopri8_c[6] = 3.0 / 8.0;
        dopri8_c[7] = 59.0 / 400.0;
        dopri8_c[8] = 93.0 / 200.0;
        dopri8_c[9] = 5490023.248 / 9719169.821;
        dopri8_c[10] = 13.0 / 20.0;
        dopri8_c[11] = 1201146.811 / 1299019.798;
        dopri8_c[12] = 1;
        dopri8_c[13] = 1;
        dopri8_b[1] = 13451.932 / 455176.623;
        dopri8_b[2] = 0;
        dopri8_b[3] = 0;
        dopri8_b[4] = 0;
        dopri8_b[5] = 0;
        dopri8_b[6] = -808719.846 / 976000.145;
        dopri8_b[7] = 1757004.468 / 5645159.321;
        dopri8_b[8] = 656045.339 / 265891.186;
        dopri8_b[9] = -3867574.721 / 1518517.206;
        dopri8_b[10] = 465885.868 / 322736.535;
        dopri8_b[11] = 53011.238 / 667516.719;
        dopri8_b[12] = 2.0 / 45.0;
        dopri8_b[13] = 0;
        dopri8_bhat[1] = 14005.451 / 335480.064;
        dopri8_bhat[2] = 0;
        dopri8_bhat[3] = 0;
        dopri8_bhat[4] = 0;
        dopri8_bhat[5] = 0;
        dopri8_bhat[6] = -59238.493 / 1068277.825;
        dopri8_bhat[7] = 181606.767 / 758867.731;
        dopri8_bhat[8] = 561292.985 / 797845.732;
        dopri8_bhat[9] = -1041891.430 / 1371343.529;
        dopri8_bhat[10] = 760417.239 / 1151165.299;
        dopri8_bhat[11] = 118820.643 / 751138.087;
        dopri8_bhat[12] = -528747.749 / 2220607.170;
        dopri8_bhat[13] = 0.25;

        /*  Begin the integration from ntime to ntime+h  */

        /*  Stage 1  */
        for (n = 1; n <= 3; n++) {
            /* Initialize the Runge-Kutta variables, and the pos/vel vectors. */
            dopri8_kprime[1][n] = fatn[n];
            dopri8_k[1][n] = rprimen[n];
            /* r is the 8'th order position solution; low_r is the 7'th order solution */
            r[n] = rn[n] + h * dopri8_bhat[1] * dopri8_k[1][n];
            low_r[n] = rn[n] + h * dopri8_b[1] * dopri8_k[1][n];
            rprime[n] = rprimen[n] + h * dopri8_bhat[1] * dopri8_kprime[1][n];
        }

        /*  Stages 2-13  */
        for (i = 2; i <= 13; i++) {
            for (n = 1; n <= 3; n++) {
                /* rvariant and rprimevariant are the intermediate pos/vel used to evaluate the acceleration function at each stage */
                rvariant[n] = rn[n];
                rprimevariant[n] = rprimen[n];
                dopri8_k[i][n] = rprimen[n];
                for (j = 1; j <= (i - 1); j++) {
                    /* Sum the terms in k, rvariant, and rprimevariant needed to evaluate the acceleration function and determine kprime at this stage */
                    rvariant[n] = rvariant[n] + h * (dopri8_a[i][j] * dopri8_k[j][n]);
                    rprimevariant[n] = rprimevariant[n] + h * (dopri8_a[i][j] * dopri8_kprime[j][n]);
                    dopri8_k[i][n] = dopri8_k[i][n] + h * (dopri8_a[i][j] * dopri8_kprime[j][n]);
                }
            }
            getAcceleration(ntime + dopri8_c[i] * h, rvariant, rprimevariant, kprime_temp);
            for (n = 1; n <= 3; n++) {
                dopri8_kprime[i][n] = kprime_temp[n];
                r[n] = r[n] + h * dopri8_bhat[i] * dopri8_k[i][n];
                low_r[n] = low_r[n] + h * dopri8_b[i] * dopri8_k[i][n];
                rprime[n] = rprime[n] + h * dopri8_bhat[i] * dopri8_kprime[i][n];
            }
        }

        /*  Compute the local error in position  */
        localerror = 0;
        for (n = 1; n <= 3; n++)
            localerror = localerror + Math.pow((r[n] - low_r[n]), 2);

        localerror = Math.sqrt(localerror);

        return localerror;

    }


    void getAcceleration(double jultime, double r[], double rprime[], double acceleration[]) {

        /*
          Procedure to calculate the accelerations on a body for use in the differential equations of motion in Cowell's method.  First, we calculate the gravitational perturbations due to the planets, using the barycentric relativistic equation for point-mass acceleration (Ref:  P. Kenneth Seidelmann, "Explanatory Supplement to the Astronomical Almanac", 1992, p. 281), and then the perturbing acceleration on a comet due to reactive water vaporization (Refs:  Brian G. Marsden, "Comets and Non-Gravitational Forces II", The Astronomical Journal Vol 74 No. 5, pp. 720-734; B.G. Marsden, Z. Sekanina, D.K. Yeomans, "Comets and Non-Gravitational Forces V", The Astronomical Journal, Vol 78 No. 2, pp. 211-225; D.K. Yeomans and P.W. Chodas, "An Asymmetric Outgassing Model for Cometary Nongravitational Accelerations", The Astronomical Journal, Vol 98 No. 3, pp. 1083-1093).
          Note that jultime is a Julian date, r[] is the barycentric position vector in A.U.s of the body in question, rprime[] is the barycentric velocity in AU/day, and acceleration() is the acceleration vector on the body given in AU/day^3.
          Note also that planet_r(I,j) represent the j'th component of the position of the I'th solar system body, planet_rprime(I,j) represent the j'th component of the velocity of the I'th solar system body, and planet_r2prime(I,j) represent the j'th component of the Newtonian acceleration of the I'th solar system body.
          Tested and verified 7-29-99.
        */

        double[] dist = new double[321];
        double[] vel = new double[321];
        double[] rtemp = new double[4];
        double[] rprimetemp = new double[4];
        double[] sun_r = new double[4];
        double[] sun_rprime = new double[4];
        double[] heliocentric_r = new double[4];
        double[] heliocentric_rprime = new double[4];
        double[] solar_pressure = new double[4];
        double[] termj = new double[4];
        double[] ecliptic_r = new double[4];
        double[] ecliptic_sun = new double[4];
        double[] normal = new double[4];

        double velocity = 0, sum1 = 0, term1 = 0, sum2 = 0, term2 = 0, term3 = 0, term4 = 0, term5 = 0, term6 = 0, term7 = 0, bracket1 = 0, bracket2 = 0, angular_velocity = 0, r_mag = 0, rtemp_mag = 0, g = 0;
        double S = 0, A = 0, albedo = 0, Cr = 0, density = 0, mass = 0;
        double sun_J2 = 1.9E-7;
        double earth_J2 = 1.08262699E-3;
        double jupiter_J2 = 0.014736;

        int j = 0, k = 0;

        /*  Get planetary and asteroid ephemeris data */
/*	planetary_ephemeris(jultime,r,1);
	asteroid_ephemeris(jultime,1,r);   */
/*  Note: My initial algorithm required light-time corrections for the positions of perturbing planets and asteroids, in order to account for gravity moving at the speed of light.  However, in a private communication, Jon Giorgini of JPL stated "There should be no correction for light-time for perturbing body gravitation; to first order, gravitational force comes out as directed toward the instantaneous source at the same Coordinate or Ephemeris Time.  It may be counterintuitive, but there's no strange physics involved (beyond gravity itself, which is of course an open issue); it turns out that way when computing the derivatives of time retarded vector & scalar potentials. Higher-order terms exist but are insignificant at solar system speeds (much less than speed of light).  Newton didn't know it at the time; his laws were empirical and took this into account implicitly, not explicitly.*/
        planetaryEphemeris(jultime, r, 0);
        asteroidEphemeris(jultime, 0, r);

        /*  First, calculate several quantities to be used later  */
        velocity = Math.sqrt(Math.pow(rprime[1], 2) + Math.pow(rprime[2], 2) + Math.pow(rprime[3], 2));
        for (j = 0; j <= number_of_planets; j++) {
            dist[j] = Math.sqrt(Math.pow((r[1] - planet_r[j][1]), 2) + Math.pow((r[2] - planet_r[j][2]), 2) + Math.pow((r[3] - planet_r[j][3]), 2));
            vel[j] = Math.sqrt(Math.pow(planet_rprime[j][1], 2) + Math.pow(planet_rprime[j][2], 2) + Math.pow(planet_rprime[j][3], 2));
        }

        /*  Initialize the acceleration vector  */
        for (j = 1; j <= 3; j++) {
            acceleration[j] = 0;
        }

        /*  Calculate each term in the brackets separately, then combine them  */
        for (j = 0; j <= number_of_planets; j++) {
            sum1 = 0;
            for (k = 0; k <= number_of_planets; k++)
                sum1 = sum1 + mu_planet[k] / dist[k];
            term1 = 4 * sum1 / Math.pow(speed_of_light, 2);
            sum2 = 0;
            for (k = 0; k <= number_of_planets; k++) {
                if (k != j)
                    sum2 = sum2 + mu_planet[k] / dist[k];
            }
            term2 = sum2 / Math.pow(speed_of_light, 2);
            term3 = Math.pow(velocity, 2) / Math.pow(speed_of_light, 2);
            term4 = 2 * Math.pow(vel[j], 2) / Math.pow(speed_of_light, 2);
            term5 = 4 * (rprime[1] * planet_rprime[j][1] + rprime[2] * planet_rprime[j][2] + rprime[3] * planet_rprime[j][3]) / Math.pow(speed_of_light, 2);
            term6 = 1.5 * Math.pow((((r[1] - planet_r[j][1]) * planet_rprime[j][1] + (r[2] - planet_r[j][2]) * planet_rprime[j][2] +
                    (r[3] - planet_r[j][3]) * planet_rprime[j][3]) / dist[j]), 2) / Math.pow(speed_of_light, 2);
            term7 = ((planet_r[j][1] - r[1]) * planet_r2prime[j][1] + (planet_r[j][2] - r[2]) * planet_r2prime[j][2] +
                    (planet_r[j][3] - r[3]) * planet_r2prime[j][3]) / (2 * Math.pow(speed_of_light, 2));
            bracket1 = 1 - term1 - term2 + term3 + term4 - term5 - term6 + term7;
            bracket2 = (r[1] - planet_r[j][1]) * (4 * rprime[1] - 3 * planet_rprime[j][1]) + (r[2] - planet_r[j][2]) * (4 * rprime[2] - 3 * planet_rprime[j][2]) + (r[3] - planet_r[j][3]) * (4 * rprime[3] - 3 * planet_rprime[j][3]);
            /*  Calculate contribution to acceleration of object  */
            for (k = 1; k <= 3; k++)
                acceleration[k] = acceleration[k] + mu_planet[j] * bracket1 * (planet_r[j][k] - r[k]) / Math.pow(dist[j], 3) + mu_planet[j] * bracket2 * (rprime[k] - planet_rprime[j][k]) / (Math.pow(speed_of_light, 2) * Math.pow(dist[j], 3)) + 3.5 * mu_planet[j] * planet_r2prime[j][k] / (Math.pow(speed_of_light, 2) * dist[j]);
        }

        /* Calculate the acceleration due to the Sun's oblateness (J2); reference is Escobal pp. 50-51 */
        /* Create the object and Sun position vectors */
        for (k = 1; k <= 3; k++) {
            ecliptic_r[k] = r[k];
            ecliptic_sun[k] = planet_r[0][k];
        }
        /* Rotate the position vectors into the ecliptic; calculation of the J2 effects should occur in the ecliptic frame. */
        TimeUtils.convertEquatorialToEcliptic(ecliptic_r);
        TimeUtils.convertEquatorialToEcliptic(ecliptic_sun);
        /* Calculate the J2 effects; reference is Escobal pp. 50-51. */
        termj[1] = -mu_planet[0] * (ecliptic_r[1] - ecliptic_sun[1]) * (1.5 * sun_J2 * Math.pow(planet_radius_0, 2.0) * (1.0 - 5.0 * Math.pow((ecliptic_r[3] - ecliptic_sun[3]), 2.0) / Math.pow(dist[0], 2.0)) / Math.pow(dist[0], 2.0)) / Math.pow(dist[0], 3.0);
        termj[2] = -mu_planet[0] * (ecliptic_r[2] - ecliptic_sun[2]) * (1.5 * sun_J2 * Math.pow(planet_radius_0, 2.0) * (1.0 - 5.0 * Math.pow((ecliptic_r[3] - ecliptic_sun[3]), 2.0) / Math.pow(dist[0], 2.0)) / Math.pow(dist[0], 2.0)) / Math.pow(dist[0], 3.0);
        termj[3] = -mu_planet[0] * (ecliptic_r[3] - ecliptic_sun[3]) * (1.5 * sun_J2 * Math.pow(planet_radius_0, 2.0) * (3.0 - 5.0 * Math.pow((ecliptic_r[3] - ecliptic_sun[3]), 2.0) / Math.pow(dist[0], 2.0)) / Math.pow(dist[0], 2.0)) / Math.pow(dist[0], 3.0);
        /* Rotate the J2 effects into the equatorial frame, and add to the acceleration vector */
        TimeUtils.convertEclipticToEquatorial(termj);
        for (k = 1; k <= 3; k++) {
            acceleration[k] = acceleration[k] + termj[k];
        }

        /* Calculate the acceleration due to the Earth's oblateness (J2); reference is Escobal pp. 50-51 */
        termj[1] = -mu_planet[3] * (r[1] - planet_r[3][1]) * (1.5 * earth_J2 * Math.pow(planet_radius_3, 2.0) * (1.0 - 5.0 * Math.pow((r[3] - planet_r[3][3]), 2.0) / Math.pow(dist[3], 2.0)) / Math.pow(dist[3], 2.0)) / Math.pow(dist[3], 3.0);
        termj[2] = -mu_planet[3] * (r[2] - planet_r[3][2]) * (1.5 * earth_J2 * Math.pow(planet_radius_3, 2.0) * (1.0 - 5.0 * Math.pow((r[3] - planet_r[3][3]), 2.0) / Math.pow(dist[3], 2.0)) / Math.pow(dist[3], 2.0)) / Math.pow(dist[3], 3.0);
        termj[3] = -mu_planet[3] * (r[3] - planet_r[3][3]) * (1.5 * earth_J2 * Math.pow(planet_radius_3, 2.0) * (3.0 - 5.0 * Math.pow((r[3] - planet_r[3][3]), 2.0) / Math.pow(dist[3], 2.0)) / Math.pow(dist[3], 2.0)) / Math.pow(dist[3], 3.0);
        for (k = 1; k <= 3; k++) {
            acceleration[k] = acceleration[k] + termj[k];
        }

        /* Calculate the acceleration due to Jupiter's oblateness (J2); reference is Escobal pp. 50-51 */
        /* Create position vectors for the object and Jupiter */
        for (k = 1; k <= 3; k++) {
            ecliptic_r[k] = r[k];
            ecliptic_sun[k] = planet_r[5][k];
        }
        /* Rotate the position vectors into the ecliptic; calculation of the J2 effects will be performed in the ecliptic frame as a good approximation. */
        TimeUtils.convertEquatorialToEcliptic(ecliptic_r);
        TimeUtils.convertEquatorialToEcliptic(ecliptic_sun);
        /* Calculate the J2 effects; reference is Escobal pp. 50-51. */
        termj[1] = -mu_planet[5] * (ecliptic_r[1] - ecliptic_sun[1]) * (1.5 * jupiter_J2 * Math.pow(planet_radius_5, 2.0) * (1.0 - 5.0 * Math.pow((ecliptic_r[3] - ecliptic_sun[3]), 2.0) / Math.pow(dist[5], 2.0)) / Math.pow(dist[5], 2.0)) / Math.pow(dist[5], 3.0);
        termj[2] = -mu_planet[5] * (ecliptic_r[2] - ecliptic_sun[2]) * (1.5 * jupiter_J2 * Math.pow(planet_radius_5, 2.0) * (1.0 - 5.0 * Math.pow((ecliptic_r[3] - ecliptic_sun[3]), 2.0) / Math.pow(dist[5], 2.0)) / Math.pow(dist[5], 2.0)) / Math.pow(dist[5], 3.0);
        termj[3] = -mu_planet[5] * (ecliptic_r[3] - ecliptic_sun[3]) * (1.5 * jupiter_J2 * Math.pow(planet_radius_5, 2.0) * (3.0 - 5.0 * Math.pow((ecliptic_r[3] - ecliptic_sun[3]), 2.0) / Math.pow(dist[5], 2.0)) / Math.pow(dist[5], 2.0)) / Math.pow(dist[5], 3.0);
        /* Rotate the J2 effects into the equatorial frame, and add to the acceleration vector */
        TimeUtils.convertEclipticToEquatorial(termj);
        for (k = 1; k <= 3; k++) {
            acceleration[k] = acceleration[k] + termj[k];
        }

        /* Begin estimation of solar raditation pressure */
        /* Calculate mean solar flux at 1 AU (in Watts per sq m) */
        S = 1350.0 + 650.0 * Math.sin(2.0 * Math.PI * (jultime - 2450358.0) / 4017.75);

        /* Calculate the exposed surface are in sq m */
        A = 2.0 * Math.PI * Math.pow((minor_planet_radius * au * 1000.0), 2.0);

        /* Find the albedo */
        if (comet_identifier) {
            /* Use best guess from Rosetta and Deep Space 1 */
            albedo = 0.04;
        } else {
            if (slope_parameter_G == 0.0)
                /* Calculate albedo assuming slope_parameter_G of 0.15 */
                albedo = 0.108;
            else
                albedo = 0.048 + (slope_parameter_G - 0.09);
            if (albedo < 0.029) albedo = 0.029;
            if (albedo > 0.3) albedo = 0.3;
        }
        Cr = 1.0 + albedo;

        /* Find the mass */
        if (comet_identifier) {
            /* Assume C-class density */
            density = 1.34;
        } else {
            /* C-class density = 1.34 grams per cubic centimeter */
            /* S-class density = 2.7 grams per cubic centimeter */
            /* M-class density = 5.3 grams per cubic centimeter */
            /* Assume density decreases linearly from M class to S class */
            if (albedo < 0.08)
                density = 1.34;
            else if (albedo > 0.08)
                density = 5.3 - 2.6 * (albedo - 0.08) / 0.22;
        }
        /* Convert density to MKS units */
        density = density * 1000.0;
        mass = density * (4.0 / 3.0) * Math.PI * Math.pow((minor_planet_radius * au * 1000.0), 3.0);

        for (k = 1; k <= 3; k++) {
            normal[k] = (r[k] - planet_r[0][k]) / dist[0];
        }
        for (k = 1; k <= 3; k++) {
            /* Calculate the Solar Radiation Pressure in meters/sec^2.  Reference: "On the Observability of radiation forces acting on near-Earth asteroids", Vokrouhlicky, Chesley, and Milani, 2000; also, the NASA Goddard Trajectory Determination System user's manual, pp. 4-64 through 4-66. */
            if (minor_planet_radius != 0.0) {
                solar_pressure[k] = (r[k] - planet_r[0][k]) * (S / 299792458.0) * (Cr * A / mass) / Math.pow(dist[0], 3.0);
            } else {
                solar_pressure[k] = 0.0;
            }
            /* Convert the Solar Radiation Pressure to au/day^2 */
            solar_pressure[k] = solar_pressure[k] * 86400.0 * 86400.0 / (au * 1000.0);
            /* Add the Solar Radiation Pressure to the acceleration */
            acceleration[k] = acceleration[k] + solar_pressure[k];
            /* Account for the relativistic Poynting-Robertson effect */
            if (minor_planet_radius != 0.0) {
                solar_pressure[k] = -(S / 299792458.0) * (Cr * A / mass) * (rprime[k] + normal[k] * (rprime[1] * normal[1] + rprime[2] * normal[2] + rprime[3] * normal[3])) / (speed_of_light * Math.pow(dist[0], 2.0));
            } else {
                solar_pressure[k] = 0.0;
            }
            /* Convert the P-R Pressure to au/day^2 */
            solar_pressure[k] = solar_pressure[k] * 86400.0 * 86400.0 / (au * 1000.0);
            /* Add the P-R Pressure to the acceleration */
            acceleration[k] = acceleration[k] + solar_pressure[k];
        }

        /*  Now add the perturbing acceleration on a comet due to reactive water vaporization  */
        if ((A1_A2_DT[1] != 0.0) || (A1_A2_DT[2] != 0.0) || (A1_A2_DT[3] != 0.0)) {

            /* Create heliocentric pos/vel vectors */
            getPlanetPosVel(jultime, 11, sun_r, sun_rprime);
            for (j = 1; j <= 3; j++) {
                heliocentric_r[j] = r[j] - sun_r[j];
                heliocentric_rprime[j] = rprime[j] - sun_rprime[j];
            }

            /*  calculate intermediate quantities  */
            angular_velocity = Math.sqrt(Math.pow((heliocentric_r[2] * heliocentric_rprime[3] - heliocentric_r[3] * heliocentric_rprime[2]), 2) + Math.pow((heliocentric_r[1] * heliocentric_rprime[3] - heliocentric_r[3] * heliocentric_rprime[1]), 2) + Math.pow((heliocentric_r[1] * heliocentric_rprime[2] - heliocentric_r[2] * heliocentric_rprime[1]), 2));
            r_mag = Math.sqrt(Math.pow(heliocentric_r[1], 2) + Math.pow(heliocentric_r[2], 2) + Math.pow(heliocentric_r[3], 2));

            /* Modify heliocentric velocity for use in two-body mechanics */
            for (j = 1; j <= 3; j++) {
                heliocentric_rprime[j] = heliocentric_rprime[j] / kappa;
            }
            /*  Find rtemp_mag at jultime - DT  */
            twoBodyUpdate(heliocentric_r, heliocentric_rprime, jultime, rtemp, rprimetemp, jultime - A1_A2_DT[3]);
            rtemp_mag = Math.sqrt(Math.pow(rtemp[1], 2) + Math.pow(rtemp[2], 2) + Math.pow(rtemp[3], 2));
            /* Restore heliocentric velocity for use in n-body mechanics */
            for (j = 1; j <= 3; j++) {
                heliocentric_rprime[j] = heliocentric_rprime[j] * kappa;
            }

            /*  Find the relative strength of the water vaporization curve at jultime-DT, accounting for possible assymmetry in outgassing with respect to perihelion  */
            g = 0.111262 * Math.pow((rtemp_mag / 2.808), (-2.15)) * Math.pow((1 + Math.pow((rtemp_mag / 2.808), 5.093)), -4.6142);

            /*  Calculate the radial and transverse components of the acceleration  */
            for (j = 1; j <= 3; j++)
                acceleration[j] = acceleration[j] + A1_A2_DT[1] * g * heliocentric_r[j] / r_mag + A1_A2_DT[2] * g * (r_mag * heliocentric_rprime[j] - velocity * heliocentric_r[j]) / angular_velocity;

        }
    }


    public double conditionedGauss(double recipa, double Lx[], double Ly[], double Lz[], double x[], double y[], double z[], double time1, double time2, double time3, double r1[], double r1prime[]) {

        /*
          Procedure to calculate the orbit of a heliocentric object, which constrains the orbit using the reciprocal of the user-specified semi-major axis (recipa, where a is in A.U.s).  This method is useful for objects with an elongation of less than 120 degrees, or ones for which the Gauss method gives poor results.  It would be especially effective for comets, where recipa is near zero.
        The error flag "kgaussfail" is returned in r1[0].
          Note that kgaussfail = 1 or 2 means that procedure sector has failed.
          Note that kgaussfail = 3 means that the iterative determination of rho(1] has diverged.
          Note that kgaussfail = 4 means that the entire iterative loop has failed to converge.
          Reference is P. Herget, "The Computation of Orbits", pp. 66-68.
        */

        double tau1 = 0, tau3 = 0, tau13 = 0, c1 = 0, c3 = 0, diff = 1, m = 0, bigc = 0, bigb = 0;
        double biga = 0, a = 0, b = 0, c = 0, littlea = 0, littleb = 0, littlec = 0, deltarho = 0;
        double s = 0, r1_mag = 0, r3_mag = 0, x_mag = 0, qc = 0, qd = 0, olddelta = 0, delta = 0;
        double lighttime1 = 0, lighttime2 = 0, lighttime3 = 0, y12 = 0, f1 = 0, g1 = 0, y13 = 0;
        double f2 = 0, g2 = 0, y23 = 0, f3 = 0, g3 = 0, jdepoch = 0, initial_f1 = 0, initial_g1 = 0;

        int kdivergence = 0, kiter = 0, k = 0, kgaussfail = 0;

        double[] site1 = new double[4];
        double[] site2 = new double[4];
        double[] site3 = new double[4];
        double[] p1star = new double[4];
        double[] p2star = new double[4];
        double[] p3star = new double[4];
        double[] rho = new double[4];
        double[] oldrho = new double[4];
        double[] realoldrho = new double[4];
        double[] realrealoldrho = new double[4];
        double[] v = new double[4];
        double[] p2starxv = new double[4];
        double[] r2 = new double[4];
        double[] r3 = new double[4];
        double[] y_f_g = new double[4];
        double[] initial_r1 = new double[4];
        double[] initial_r2 = new double[4];

        /*  Initialize all variables with notation that matches Herget  */
        tau1 = kappa * (time1 - time2);
        tau3 = kappa * (time3 - time2);
        tau13 = tau3 - tau1;
        c1 = tau3 / tau13;
        c3 = -tau1 / tau13;
        site1[1] = x[1];
        site1[2] = y[1];
        site1[3] = z[1];
        site2[1] = x[2];
        site2[2] = y[2];
        site2[3] = z[2];
        site3[1] = x[3];
        site3[2] = y[3];
        site3[3] = z[3];
        p1star[1] = Lx[1];
        p1star[2] = Ly[1];
        p1star[3] = Lz[1];
        p2star[1] = Lx[2];
        p2star[2] = Ly[2];
        p2star[3] = Lz[2];
        p3star[1] = Lx[3];
        p3star[2] = Ly[3];
        p3star[3] = Lz[3];

        /*  Initialize slant ranges before beginning iterative loop to solve for them subject to recipa  */
        rho[1] = 0;
        rho[2] = 0;
        rho[3] = 0;

        /*  Label "out" used to escape from loop if necessary  */
        out:
        while ((diff > 1e-08) && (kgaussfail == 0)) {
            for (k = 1; k <= 3; k++) {
                if (kiter > 2) {
                    realrealoldrho[k] = realoldrho[k];
                    realoldrho[k] = oldrho[k];
                } else if (kiter == 2) {
                    realoldrho[k] = oldrho[k];
                }
                oldrho[k] = rho[k];
                v[k] = c1 * site1[k] - site2[k] + c3 * site3[k];
            }
            p2starxv[1] = p2star[2] * v[3] - p2star[3] * v[2];
            p2starxv[2] = -(p2star[1] * v[3] - p2star[3] * v[1]);
            p2starxv[3] = p2star[1] * v[2] - p2star[2] * v[1];
            m = -(c1 / c3) * (p1star[1] * p2starxv[1] + p1star[2] * p2starxv[2] + p1star[3] * p2starxv[3]) / (p3star[1] * p2starxv[1] + p3star[2] * p2starxv[2] + p3star[3] * p2starxv[3]);
            bigc = Math.pow((m * p3star[1] - p1star[1]), 2) + Math.pow((m * p3star[2] - p1star[2]), 2) + Math.pow((m * p3star[3] - p1star[3]), 2);
            bigb = -2 * ((m * p3star[1] - p1star[1]) * (site3[1] - site1[1]) + (m * p3star[2] - p1star[2]) * (site3[2] - site1[2]) + (m * p3star[3] - p1star[3]) * (site3[3] - site1[3]));
            biga = Math.pow((site3[1] - site1[1]), 2) + Math.pow((site3[2] - site1[2]), 2) + Math.pow((site3[3] - site1[3]), 2);
            a = Math.pow(site1[1], 2) + Math.pow(site1[2], 2) + Math.pow(site1[3], 2);
            b = -2 * (site1[1] * p1star[1] + site1[2] * p1star[2] + site1[3] * p1star[3]);
            c = 1;
            littlea = Math.pow(site3[1], 2) + Math.pow(site3[2], 2) + Math.pow(site3[3], 2);
            littleb = -2 * m * (site3[1] * p3star[1] + site3[2] * p3star[2] + site3[3] * p3star[3]);
            littlec = Math.pow(m, 2);
            /*  Evaluate delta at rho[1] = 0 before beginning iterative loop, then increment rho[1] by deltarho until a sign change occurs in delta.  Use bisection to precisely locate this zero of delta, and stop when deltarho < 1D-08.  */
            deltarho = 1;
            rho[1] = 0;
            s = Math.sqrt(biga);
            r1_mag = Math.sqrt(a);
            r3_mag = Math.sqrt(littlea);
            x_mag = recipa * (r1_mag + r3_mag + s) / 4;
            qc = 1.0 + (3.0 / 10.0) * x_mag + (9.0 / 56.0) * Math.pow(x_mag, 2) + (5.0 / 48.0) * Math.pow(x_mag, 3) + (105.0 / 1408.0) * Math.pow(x_mag, 4) + (2835.0 / 49920.0) * Math.pow(x_mag, 5);
            x_mag = recipa * (r1_mag + r3_mag - s) / 4;
            qd = 1.0 + (3.0 / 10.0) * x_mag + (9.0 / 56.0) * Math.pow(x_mag, 2) + (5.0 / 48.0) * Math.pow(x_mag, 3) + (105.0 / 1408.0) * Math.pow(x_mag, 4) + (2835.0 / 49920.0) * Math.pow(x_mag, 5);
            olddelta = 6 * tau13 - qc * Math.pow((r1_mag + r3_mag + s), 1.5) + qd * Math.pow((r1_mag + r3_mag - s), 1.5);
            rho[1] = rho[1] + deltarho;
            while ((Math.abs(deltarho) > 1e-08) && (kgaussfail == 0)) {
                s = Math.sqrt(biga + bigb * rho[1] + bigc * Math.pow(rho[1], 2));
                r1_mag = Math.sqrt(a + b * rho[1] + c * Math.pow(rho[1], 2));
                r3_mag = Math.sqrt(littlea + littleb * rho[1] + littlec * Math.pow(rho[1], 2));
                x_mag = recipa * (r1_mag + r3_mag + s) / 4;
                qc = 1.0 + (3.0 / 10.0) * x_mag + (9.0 / 56.0) * Math.pow(x_mag, 2) + (5.0 / 48.0) * Math.pow(x_mag, 3) + (105.0 / 1408.0) * Math.pow(x_mag, 4) + (2835.0 / 49920.0) * Math.pow(x_mag, 5);
                x_mag = recipa * (r1_mag + r3_mag - s) / 4;
                qd = 1.0 + (3.0 / 10.0) * x_mag + (9.0 / 56.0) * Math.pow(x_mag, 2) + (5.0 / 48.0) * Math.pow(x_mag, 3) + (105.0 / 1408.0) * Math.pow(x_mag, 4) + (2835.0 / 49920.0) * Math.pow(x_mag, 5);
                delta = 6 * tau13 - qc * Math.pow((r1_mag + r3_mag + s), 1.5) + qd * Math.pow((r1_mag + r3_mag - s), 1.5);
                if ((olddelta) / Math.abs(olddelta) != (delta) / Math.abs(delta))
                    deltarho = -deltarho / 2;
                rho[1] = rho[1] + deltarho;
                olddelta = delta;
                if (rho[1] > 1000)
                    kgaussfail = 3;
            }
            if (kgaussfail != 0) break out;
            /*  Use new value of rho[1] to solve for the other rho's, correct times for light time-of-flight, and calculate new sector-triangle ratios.  Using these, repeat loop until rho's are consistent to 1D-08.  */
            rho[3] = m * rho[1];
            rho[2] = -(v[1] - c1 * rho[1] * p1star[1] - c3 * rho[3] * p3star[1]) / p2star[1];
            for (k = 1; k <= 3; k++) {
                if (rho[k] < 0)
                    rho[k] = (-1) * rho[k];
            }
            if (kiter > 3) {
                for (k = 1; k <= 3; k++) {
                    if (Math.abs(rho[k] - oldrho[k]) >= Math.abs(oldrho[k] - realoldrho[k])) kdivergence = 1;
                }
                if ((kiter >= 50) || (kdivergence == 1)) {
                    for (k = 1; k <= 3; k++) {
                        rho[k] = (.25) * (rho[k] + oldrho[k] + realoldrho[k] + realrealoldrho[k]);
                    }
                }
            }
            lighttime1 = time1 - rho[1] / speed_of_light;
            lighttime2 = time2 - rho[2] / speed_of_light;
            lighttime3 = time3 - rho[3] / speed_of_light;
            tau1 = kappa * (lighttime1 - lighttime2);
            tau3 = kappa * (lighttime3 - lighttime2);
            tau13 = tau3 - tau1;
            for (k = 1; k <= 3; k++) {
                r1[k] = rho[1] * p1star[k] - site1[k];
                if (kiter == 1)
                    initial_r1[k] = r1[k];
                r2[k] = rho[2] * p2star[k] - site2[k];
                if (kiter == 1)
                    initial_r2[k] = r2[k];
                r3[k] = rho[3] * p3star[k] - site3[k];
            }
            kgaussfail = sector(r1, r2, lighttime1, lighttime2, y_f_g);
            y12 = y_f_g[1];
            f1 = y_f_g[2];
            g1 = y_f_g[3];
            if (kgaussfail != 0) break out;
            if (kiter == 1) {
                /*  Save initial values of f1 and g1, but only if they could be computed  */
                initial_f1 = f1;
                initial_g1 = g1;
            }
            kgaussfail = sector(r1, r3, lighttime1, lighttime3, y_f_g);
            y13 = y_f_g[1];
            f2 = y_f_g[2];
            g2 = y_f_g[3];
            if (kgaussfail != 0) break out;
            kgaussfail = sector(r2, r3, lighttime2, lighttime3, y_f_g);
            y23 = y_f_g[1];
            f3 = y_f_g[2];
            g3 = y_f_g[3];
            if (kgaussfail != 0) break out;
            c1 = (y13 * tau3) / (y23 * tau13);
            c3 = -(y13 * tau1) / (y12 * tau13);
            diff = Math.abs(rho[1] - oldrho[1]);
            if (Math.abs(rho[2] - oldrho[2]) > diff)
                diff = Math.abs(rho[2] - oldrho[2]);
            if (Math.abs(rho[3] - oldrho[3]) > diff)
                diff = Math.abs(rho[3] - oldrho[3]);
            kiter = kiter + 1;
            if (kiter == 200)
                kgaussfail = 4;
        }

        if (kgaussfail != 0) {
            if (initial_g1 != 0.0) {
                /*  Return initial approximation  */
                for (k = 1; k <= 3; k++) {
                    r1[k] = initial_r1[k];
                    r1prime[k] = (initial_r2[k] - initial_f1 * initial_r1[k]) / initial_g1;
                }
            } else {
                /*  Return zero solution  */
                for (k = 1; k <= 3; k++) {
                    r1[k] = 0.0;
                    r1prime[k] = 0.0;
                }
            }
        } else {
            /*  Nominal completion - return full solution  */
            for (k = 1; k <= 3; k++) {
                r1prime[k] = (r2[k] - f1 * r1[k]) / g1;
            }
        }

        jdepoch = lighttime1;

        r1[0] = kgaussfail;

        return jdepoch;

    }


    public double gaussMethod(double Lx[], double Ly[], double Lz[], double x[], double y[], double z[], double time1, double time2, double time3, double r1[], double r1prime[]) {

        /*
          Procedure to use the Gauss method to determine a preliminary orbit using three sets of optical observations.  Algorithm uses sector-to-triangle theory for refinement of the orbit.
          Algorithm taken from Pedro Escobal, "Methods of Orbit Determination", pp. 245-261, and Brian G. Marsden, "Initial Orbit Determination: The Pragmatist's Point of View", The Astronomical Journal, Vol. 90, no. 8, Aug '85, pp. 1541-1547.
          Note:  Station coordinates (x, y, and z) should be in heliocentric units, and times in Julian days.  The state vector r1 (and r1prime) are given in heliocentric units (per canonical time unit).  The "kgaussfail" flag is an integer.  The Julian day of the epoch to which the elements refer is "jdepoch".
          Note:  Station coordinates are inward pointing; that is, vectors x, y, and z point from the observer to the barycenter of the solar system.
          Note:  In order to prevent divergence, the values of rho (slant range) are monitored.  If negative rhos occur, their sign is changed.  If convergence has not occurred by the 50th iteration, or if the rhos are diverging, the new value of rhos is taken to be the average of the indicated value with the three previous values.  This technique is due to Marsden. If convergence has not occurred by the 200th iteration, calculation is halted, the "kgaussfail" flag is set to 3, and the initial approximation is returned.
          The kgaussfail flag (returned as r1[0]) can have the following meanings:
             kgaussfail = 1 or 2 means a problem has been encountered in method sector, and the method has failed.
             kgaussfail = 3 means over 200 iterations have occurred without convergence; the initial approximation will be returned.

          Tested and verified 20 June 1999.

        */

        double[] rho = new double[4];
        double[] r2 = new double[4];
        double[] r3 = new double[4];
        double[] oldrho = new double[4];
        double[] realoldrho = new double[4];
        double[] realrealoldrho = new double[4];
        double[] initial_r1 = new double[4];
        double[] initial_r2 = new double[4];
        double[] y_f_g = new double[4];

        double tau1 = 0, tau3 = 0, tau13 = 0, c1 = 0, c3 = 0, d = 0, a11 = 0, a12 = 0, a13 = 0, a21 = 0, a22 = 0, a23 = 0, a31 = 0, a32 = 0, a33 = 0;
        double gx = 0, gy = 0, gz = 0, diff = 1, lighttime1 = 0, lighttime2 = 0, lighttime3 = 0, y12 = 0, f1 = 0, g1 = 0, initial_f1 = 0, initial_g1 = 0;
        double y13 = 0, f2 = 0, g2 = 0, y23 = 0, f3 = 0, g3 = 0, jdepoch = 0;

        int k = 0, kiter = 1, kdivergence = 0, kgaussfail = 0;

        tau1 = kappa * (time1 - time2);
        tau3 = kappa * (time3 - time2);
        tau13 = tau3 - tau1;
        c1 = tau3 / tau13;
        c3 = -tau1 / tau13;
        d = Lx[1] * (Ly[2] * Lz[3] - Ly[3] * Lz[2]) - Lx[2] * (Ly[1] * Lz[3] - Ly[3] * Lz[1]) + Lx[3] * (Ly[1] * Lz[2] - Ly[2] * Lz[1]);
        a11 = (Ly[2] * Lz[3] - Ly[3] * Lz[2]) / d;
        a12 = -(Lx[2] * Lz[3] - Lx[3] * Lz[2]) / d;
        a13 = (Lx[2] * Ly[3] - Lx[3] * Ly[2]) / d;
        a21 = -(Ly[1] * Lz[3] - Ly[3] * Lz[1]) / d;
        a22 = (Lx[1] * Lz[3] - Lx[3] * Lz[1]) / d;
        a23 = -(Lx[1] * Ly[3] - Lx[3] * Ly[1]) / d;
        a31 = (Ly[1] * Lz[2] - Ly[2] * Lz[1]) / d;
        a32 = -(Lx[1] * Lz[2] - Lx[2] * Lz[1]) / d;
        a33 = (Lx[1] * Ly[2] - Lx[2] * Ly[1]) / d;
        gx = c1 * x[1] - x[2] + c3 * x[3];
        gy = c1 * y[1] - y[2] + c3 * y[3];
        gz = c1 * z[1] - z[2] + c3 * z[3];
        rho[1] = (a11 * gx + a12 * gy + a13 * gz) / c1;
        rho[2] = -(a21 * gx + a22 * gy + a23 * gz);
        rho[3] = (a31 * gx + a32 * gy + a33 * gz) / c3;
        for (k = 1; k <= 3; k++) {
            if (rho[k] < 0) rho[k] = (-1) * rho[k];
        }
        r1[1] = rho[1] * Lx[1] - x[1];
        r1[2] = rho[1] * Ly[1] - y[1];
        r1[3] = rho[1] * Lz[1] - z[1];
        r2[1] = rho[2] * Lx[2] - x[2];
        r2[2] = rho[2] * Ly[2] - y[2];
        r2[3] = rho[2] * Lz[2] - z[2];
        r3[1] = rho[3] * Lx[3] - x[3];
        r3[2] = rho[3] * Ly[3] - y[3];
        r3[3] = rho[3] * Lz[3] - z[3];
        for (k = 1; k <= 3; k++) {
            initial_r1[k] = r1[k];
            initial_r2[k] = r2[k];
        }

        /*  Enter into sector-to-triangle loop to get rprime  */
        /*  Label "out" allows us to escape loop if kgaussfail flag set */
        out:
        while ((2 * diff >= 1e-08) && (kgaussfail == 0)) {
            for (k = 1; k <= 3; k++) {
                if (kiter > 2) {
                    realrealoldrho[k] = realoldrho[k];
                    realoldrho[k] = oldrho[k];
                } else if (kiter == 2)
                    realoldrho[k] = oldrho[k];
                oldrho[k] = rho[k];
            }
            lighttime1 = time1 - rho[1] / speed_of_light;
            lighttime2 = time2 - rho[2] / speed_of_light;
            lighttime3 = time3 - rho[3] / speed_of_light;
            tau1 = kappa * (lighttime1 - lighttime2);
            tau3 = kappa * (lighttime3 - lighttime2);
            tau13 = tau3 - tau1;
            kgaussfail = sector(r1, r2, lighttime1, lighttime2, y_f_g);
            y12 = y_f_g[1];
            f1 = y_f_g[2];
            g1 = y_f_g[3];
            if (kgaussfail != 0) break out;
            if (kiter == 1) {
                /*  Save initial values of f1 and g1, but only if they could be computed  */
                initial_f1 = f1;
                initial_g1 = g1;
            }
            kgaussfail = sector(r1, r3, lighttime1, lighttime3, y_f_g);
            y13 = y_f_g[1];
            f2 = y_f_g[2];
            g2 = y_f_g[3];
            if (kgaussfail != 0) break out;
            kgaussfail = sector(r2, r3, lighttime2, lighttime3, y_f_g);
            y23 = y_f_g[1];
            f3 = y_f_g[2];
            g3 = y_f_g[3];
            if (kgaussfail != 0) break out;
            c1 = (y13 * tau3) / (y23 * tau13);
            c3 = -(y13 * tau1) / (y12 * tau13);
            gx = c1 * x[1] - x[2] + c3 * x[3];
            gy = c1 * y[1] - y[2] + c3 * y[3];
            gz = c1 * z[1] - z[2] + c3 * z[3];
            rho[1] = (a11 * gx + a12 * gy + a13 * gz) / c1;
            rho[2] = -(a21 * gx + a22 * gy + a23 * gz);
            rho[3] = (a31 * gx + a32 * gy + a33 * gz) / c3;
            for (k = 1; k <= 3; k++) {
                if (rho[k] < 0) rho[k] = (-1) * rho[k];
            }
            diff = Math.abs(rho[1] - oldrho[1]);
            if (Math.abs(rho[2] - oldrho[2]) > diff) diff = Math.abs(rho[2] - oldrho[2]);
            if (Math.abs(rho[3] - oldrho[3]) > diff) diff = Math.abs(rho[3] - oldrho[3]);
            if (kiter > 3) {
                for (k = 1; k <= 3; k++) {
                    if (Math.abs(rho[k] - oldrho[k]) >= Math.abs(oldrho[k] - realoldrho[k])) kdivergence = 1;
                }
                if ((kiter >= 50) || (kdivergence == 1)) {
                    for (k = 1; k <= 3; k++) {
                        rho[k] = (.25) * (rho[k] + oldrho[k] + realoldrho[k] + realrealoldrho[k]);
                    }
                }
            }
            r1[1] = rho[1] * Lx[1] - x[1];
            r1[2] = rho[1] * Ly[1] - y[1];
            r1[3] = rho[1] * Lz[1] - z[1];
            r2[1] = rho[2] * Lx[2] - x[2];
            r2[2] = rho[2] * Ly[2] - y[2];
            r2[3] = rho[2] * Lz[2] - z[2];
            r3[1] = rho[3] * Lx[3] - x[3];
            r3[2] = rho[3] * Ly[3] - y[3];
            r3[3] = rho[3] * Lz[3] - z[3];
            kiter = kiter + 1;
            if (kiter == 200) kgaussfail = 3;
        }

        if (kgaussfail != 0) {
            /*  The method has failed  */
            if (initial_g1 != 0.0) {
                /*  Return initial approximation, if possible  */
                for (k = 1; k <= 3; k++) {
                    r1[k] = initial_r1[k];
                    r1prime[k] = (initial_r2[k] - initial_f1 * initial_r1[k]) / initial_g1;
                }
            } else {
                /*  Return a zero solution */
                for (k = 1; k <= 3; k++) {
                    r1[k] = 0.0;
                    r1prime[k] = 0.0;
                }
            }
        } else {
            /*  Nominal completion - return full solution  */
            for (k = 1; k <= 3; k++) {
                r1prime[k] = (r2[k] - f1 * r1[k]) / g1;
            }
        }

        jdepoch = lighttime1;
        r1[0] = kgaussfail;

        return jdepoch;

    }


    int sector(double r1[], double r2[], double time1, double time2, double y_f_g[]) {

        /*

          Program to find the ratio of sector to triangle for the given two radius vectors and times, as well as the closed form f and g coefficients.
          Note:  r1 and r2 should be in A.U.s, while the times should be in days.
          Algorithm taken from Pedro Ramon Escobal, "Methods of Orbit Determination", pp. 196-7, with universal modifications from Bate, Mueller, and White pp. 263-4.

         Note:  The outputs y, f, and g are packed into an array to ensure return to the calling routine.

         Note:  The kgaussfail indicator is returned by this method.  Possible values are:
                0 = nominal
                1 = non-convergence, probably due to widely spaced observations
                2 = observations too widely spaced

         Tested and verified 17 June 1999.

        */

        double tau = 0, radius1 = 0, radius2 = 0, nu2nu1cos = 0, nu2nu1sin = 0, nu2nu1 = 0, l = 0, m = 0, oldy = 0, littlex = 0, coef = 0, bigx = 0;
        double e2e1cos = 0, e2e1sin = 0, e2e1 = 0, f2f1cosh = 0, f2f1 = 0, f2f12sinh = 0, f2f1sinh = 0, deltaanomaly = 0, p = 0;
        double y = 0, f = 0, g = 0;
        int k = 0, counter = 0, kgaussfail = 0;

        tau = kappa * (time2 - time1);
        radius1 = Math.sqrt(Math.pow(r1[1], 2) + Math.pow(r1[2], 2) + Math.pow(r1[3], 2));
        radius2 = Math.sqrt(Math.pow(r2[1], 2) + Math.pow(r2[2], 2) + Math.pow(r2[3], 2));

        /*  Note:  nu2nu1cos is the cosine of (nu2 � nu1)  */
        nu2nu1cos = (r1[1] * r2[1] + r1[2] * r2[2] + r1[3] * r2[3]) / (radius1 * radius2);
        /*
          Note:  nu2nu1sin is the sine of (nu2 � nu1)
          Note: I am assuming here that the observations are not widely separated; if the change in nu is less than 180 degrees, then nu2nu1sin should be positive, and this choice is valid for both direct and retrograde orbits.  If the full expression as given in Escobal were used, widely-spaced observations could be handled, but only for direct orbits.  My choice seems more reasonable; attempting initial orbit determination with widely-spaced observations is unlikely.
        */
        nu2nu1sin = Math.sqrt(1 - Math.pow(nu2nu1cos, 2));
        /*  Note:  nu2nu1 = (nu2 � nu1)  */
        nu2nu1 = Math.atan2(nu2nu1sin, nu2nu1cos);

        l = -0.5 + (radius1 + radius2) / (4 * Math.sqrt(radius1 * radius2) * Math.cos(nu2nu1 / 2));
        m = (mu * Math.pow(tau, 2)) / Math.pow((2 * Math.sqrt(radius1 * radius2) * Math.cos(nu2nu1 / 2)), 3);

        /*  Begin iterative loop to find y  */
        y = 1;
        oldy = 0;
        /*  Label "out" allows us to terminate these loops if necessary  */
        out:
        while ((Math.abs(y - oldy) >= 1e-09) && (kgaussfail == 0)) {
            oldy = y;
            littlex = m / Math.pow(y, 2) - l;
            if (littlex > 1) {
                kgaussfail = 2;
                break out;
            }
            if (Math.abs(littlex) < 0.05) {
                /*  Possible near-parabolic case.  Expansion valid for ABS(littlex) < 0.05  */
                coef = 1.2;
                bigx = 1 + coef * littlex;
                for (k = 1; k <= 10; k++) {
                    coef = coef * (6.0 + 2.0 * k) / (5.0 + 2.0 * k);
                    bigx = bigx + coef * Math.pow(littlex, (k + 1));
                }
                bigx = bigx * 4.0 / 3.0;
            } else if (littlex > 0) {
                /*  Large angle elliptic case  */
                /*  Note:  e2e1cos is the cosine of (E2 � E1)/2  */
                e2e1cos = 1 - 2 * littlex;
                /*  Note:  e2e1sin is the sine of (E2 � E1)/2  */
                e2e1sin = Math.sqrt(4 * littlex * (1 - littlex));
                /*  Note:  e2e1 = E2 � E1  */
                e2e1 = Math.atan2(e2e1sin, e2e1cos);
                e2e1 = 2 * e2e1;
                bigx = (e2e1 - Math.sin(e2e1)) / Math.pow(e2e1sin, 3);
            } else {
                /*  large angle hyperbolic case  */
                /*  f2f1cosh is the cosh of (F2 � F1)/2  */
                f2f1cosh = 1 - 2 * littlex;
                /*  f2f1 = (F2 � F1)/2  */
                f2f1 = Math.log(f2f1cosh + Math.sqrt(Math.pow(f2f1cosh, 2) - 1));
                /*  f2f12sinh is the sinh of (F2 � F1)/2  */
                f2f12sinh = (Math.exp(f2f1) - Math.exp(-f2f1)) / 2;
                /*  f2f1 = F2 � F1  */
                f2f1 = 2 * f2f1;
                /*  f2f1sinh is the sinh of F2 � F1  */
                f2f1sinh = (Math.exp(f2f1) - Math.exp(-f2f1)) / 2;
                bigx = (f2f1sinh - f2f1) / Math.pow(f2f12sinh, 3);
            }
            y = 1 + bigx * (l + littlex);
            counter = counter + 1;
            if (counter > 100) kgaussfail = 1;
        }

        if (kgaussfail == 0) {
            /*  deltaanomaly is COS((E2 � E1)/2) or COSH((F2 � F1)/2)  */
            deltaanomaly = 1 - 2 * littlex;
            p = radius1 * radius2 * (1 - nu2nu1cos) / (radius1 + radius2 - 2 * Math.sqrt(radius1 * radius2) * Math.cos(nu2nu1 / 2) * deltaanomaly);
            f = 1 - radius2 * (1 - nu2nu1cos) / p;
            g = radius1 * radius2 * nu2nu1sin / Math.sqrt(mu * p);
            y_f_g[1] = y;
            y_f_g[2] = f;
            y_f_g[3] = g;
        }

        return kgaussfail;

    }


    public void differentialCorrection(double r[], double rprime[], double jdepoch, double Lx[], double Ly[], double Lz[], double x[], double y[], double z[], double time1, double time2, double time3) {

        /*

        Refines the state vector calculated by an initial orbit determination method, using differential correction.

        In the event divergence is detected (by virtue of impossibly large values for r[i], or increasing rms residuals), the r[0] component will be set to 5, and the input solution will be returned.

        Tested and verified 15 June 1999.

        */

        double[] time = new double[4];
        double[] r_variant = new double[4];
        double[] rprime_variant = new double[4];
        double[] r_input = new double[4];
        double[] rprime_input = new double[4];
        double[] ra = new double[4];
        double[] dec = new double[4];
        double[] rho_vector = new double[4];
        double[] ra_variant = new double[4];
        double[] dec_variant = new double[4];
        double[] delta_radec = new double[7];
        double[][] pd_matrix = new double[7][7];
        double[] delta_r = new double[4];
        double[] delta_rprime = new double[4];
        double[] lat = new double[4];
        double[] longit = new double[4];
        double[] latlong = new double[3];
        double[] radec = new double[3];

        double rho = 0, r_dev = 0, rprime_dev = 0, ra_dev = 0, dec_dev = 0, light_time = 0, residual = 0, old_residual = 0, diff = 0;

        int i = 0, j = 0, loop_counter = 0;

        /*  Initialize the time array  */
        time[1] = time1;
        time[2] = time2;
        time[3] = time3;

        /* First, save the input state vector, and calculate the errors in observed vs. computed radecs */

        for (i = 1; i <= 3; i++) {

            r_input[i] = r[i];
            rprime_input[i] = rprime[i];

            lat[i] = Math.asin(Lz[i]);
            longit[i] = Math.atan2(Ly[i], Lx[i]);
            latlong[1] = lat[i];
            latlong[2] = longit[i];
            TimeUtils.convertEclipticToRadec(latlong, radec);
            ra[i] = radec[1];
            dec[i] = radec[2];

            /*  Correct for light time-of-flight  */
            twoBodyUpdate(r, rprime, jdepoch, r_variant, rprime_variant, time[i]);
            light_time = time[i] - Math.sqrt(Math.pow((r_variant[1] + x[i]), 2) + Math.pow((r_variant[2] + y[i]), 2) + Math.pow((r_variant[3] + z[i]), 2)) / speed_of_light;
            twoBodyUpdate(r, rprime, jdepoch, r_variant, rprime_variant, light_time);

            rho_vector[1] = r_variant[1] + x[i];
            rho_vector[2] = r_variant[2] + y[i];
            rho_vector[3] = r_variant[3] + z[i];
            rho = Math.sqrt(Math.pow(rho_vector[1], 2) + Math.pow(rho_vector[2], 2) + Math.pow(rho_vector[3], 2));

            lat[i] = Math.asin(rho_vector[3] / rho);
            longit[i] = Math.atan2(rho_vector[2], rho_vector[1]);
            latlong[1] = lat[i];
            latlong[2] = longit[i];
            TimeUtils.convertEclipticToRadec(latlong, radec);
            ra_variant[i] = radec[1];
            dec_variant[i] = radec[2];

            delta_radec[2 * i - 1] = ra[i] - ra_variant[i];
            while (Math.abs(delta_radec[2 * i - 1]) > Math.abs(delta_radec[2 * i - 1] - 2.0 * Math.PI))
                delta_radec[2 * i - 1] = delta_radec[2 * i - 1] - 2.0 * Math.PI;
            while (Math.abs(delta_radec[2 * i - 1]) > Math.abs(delta_radec[2 * i - 1] + 2.0 * Math.PI))
                delta_radec[2 * i - 1] = delta_radec[2 * i - 1] + 2.0 * Math.PI;
            delta_radec[2 * i] = dec[i] - dec_variant[i];

            residual = residual + Math.pow(delta_radec[2 * i - 1], 2) + Math.pow(delta_radec[2 * i], 2);

        }

        residual = Math.sqrt(residual);

        /*  Repeat the differential correction process until successive RMS residuals differ by less than one percent, or until the RMS residuals are sufficiently small.  Include a counter so we exit the loop after 10 iterations.  End the loop in case of divergence.  */

        while ((residual > 5e-07) && (Math.abs(residual - old_residual) > 0.10 * residual) && (loop_counter < 10) && (r[0] != 5.0)) {

            System.out.println("residual = " + residual);
            old_residual = residual;
            loop_counter = loop_counter + 1;

            /*  Compute elements of the differential correction matrix  */

            for (i = 1; i <= 3; i++) {
                for (j = 1; j <= 3; j++) {
                    r_dev = 0.00001 * r[j];
                    rprime_dev = 0.000001 * rprime[j];

                    r[j] = r[j] + r_dev;

                    /*  Correct for light time-of-flight  */
                    twoBodyUpdate(r, rprime, jdepoch, r_variant, rprime_variant, time[i]);
                    light_time = time[i] - Math.sqrt(Math.pow((r_variant[1] + x[i]), 2) + Math.pow((r_variant[2] + y[i]), 2) + Math.pow((r_variant[3] + z[i]), 2)) / speed_of_light;
                    twoBodyUpdate(r, rprime, jdepoch, r_variant, rprime_variant, light_time);

                    rho_vector[1] = r_variant[1] + x[i];
                    rho_vector[2] = r_variant[2] + y[i];
                    rho_vector[3] = r_variant[3] + z[i];
                    rho = Math.sqrt(Math.pow(rho_vector[1], 2) + Math.pow(rho_vector[2], 2) + Math.pow(rho_vector[3], 2));

                    lat[i] = Math.asin(rho_vector[3] / rho);
                    longit[i] = Math.atan2(rho_vector[2], rho_vector[1]);
                    latlong[1] = lat[i];
                    latlong[2] = longit[i];
                    TimeUtils.convertEclipticToRadec(latlong, radec);
                    ra_dev = radec[1];
                    dec_dev = radec[2];

                    diff = ra_dev - ra_variant[i];
                    while (Math.abs(diff) > Math.abs(diff - 2.0 * Math.PI))
                        diff = diff - 2.0 * Math.PI;
                    while (Math.abs(diff) > Math.abs(diff + 2.0 * Math.PI))
                        diff = diff + 2.0 * Math.PI;
                    pd_matrix[2 * i - 1][j] = diff / r_dev;
                    pd_matrix[2 * i][j] = (dec_dev - dec_variant[i]) / r_dev;

                    r[j] = r[j] - r_dev;

                    rprime[j] = rprime[j] + rprime_dev;

                    /*  Correct for light time-of-flight  */
                    twoBodyUpdate(r, rprime, jdepoch, r_variant, rprime_variant, time[i]);
                    light_time = time[i] - Math.sqrt(Math.pow((r_variant[1] + x[i]), 2) + Math.pow((r_variant[2] + y[i]), 2) + Math.pow((r_variant[3] + z[i]), 2)) / speed_of_light;
                    twoBodyUpdate(r, rprime, jdepoch, r_variant, rprime_variant, light_time);

                    rho_vector[1] = r_variant[1] + x[i];
                    rho_vector[2] = r_variant[2] + y[i];
                    rho_vector[3] = r_variant[3] + z[i];
                    rho = Math.sqrt(Math.pow(rho_vector[1], 2) + Math.pow(rho_vector[2], 2) + Math.pow(rho_vector[3], 2));

                    lat[i] = Math.asin(rho_vector[3] / rho);
                    longit[i] = Math.atan2(rho_vector[2], rho_vector[1]);
                    latlong[1] = lat[i];
                    latlong[2] = longit[i];
                    TimeUtils.convertEclipticToRadec(latlong, radec);
                    ra_dev = radec[1];
                    dec_dev = radec[2];

                    diff = ra_dev - ra_variant[i];
                    while (Math.abs(diff) > Math.abs(diff - 2.0 * Math.PI))
                        diff = diff - 2.0 * Math.PI;
                    while (Math.abs(diff) > Math.abs(diff + 2.0 * Math.PI))
                        diff = diff + 2.0 * Math.PI;
                    pd_matrix[2 * i - 1][j + 3] = diff / rprime_dev;
                    pd_matrix[2 * i][j + 3] = (dec_dev - dec_variant[i]) / rprime_dev;

                    rprime[j] = rprime[j] - rprime_dev;

                }

            }

            /*  Now invert the matrix of partial derivatives, and solve for the improved state vector  */

            invertNxn(6, pd_matrix);

            for (i = 1; i <= 3; i++) {
                delta_r[i] = 0;
                delta_rprime[i] = 0;
                for (j = 1; j <= 6; j++) {
                    delta_r[i] = delta_r[i] + pd_matrix[i][j] * delta_radec[j];
                    delta_rprime[i] = delta_rprime[i] + pd_matrix[i + 3][j] * delta_radec[j];
                }

                r[i] = r[i] + delta_r[i];
                /*  Set flag in case of divergence  */
                if (Math.abs(r[i]) > 5.84e04) r[0] = 5.0;
                rprime[i] = rprime[i] + delta_rprime[i];

            }

            /* Now calculate the errors in observed vs. (improved) computed radecs */

            residual = 0;
            for (i = 1; i <= 3; i++) {

                /*  Correct for light time-of-flight  */
                twoBodyUpdate(r, rprime, jdepoch, r_variant, rprime_variant, time[i]);
                light_time = time[i] - Math.sqrt(Math.pow((r_variant[1] + x[i]), 2) + Math.pow((r_variant[2] + y[i]), 2) + Math.pow((r_variant[3] + z[i]), 2)) / speed_of_light;
                twoBodyUpdate(r, rprime, jdepoch, r_variant, rprime_variant, light_time);

                rho_vector[1] = r_variant[1] + x[i];
                rho_vector[2] = r_variant[2] + y[i];
                rho_vector[3] = r_variant[3] + z[i];
                rho = Math.sqrt(Math.pow(rho_vector[1], 2) + Math.pow(rho_vector[2], 2) + Math.pow(rho_vector[3], 2));

                lat[i] = Math.asin(rho_vector[3] / rho);
                longit[i] = Math.atan2(rho_vector[2], rho_vector[1]);
                latlong[1] = lat[i];
                latlong[2] = longit[i];
                TimeUtils.convertEclipticToRadec(latlong, radec);
                ra_variant[i] = radec[1];
                dec_variant[i] = radec[2];

                delta_radec[2 * i - 1] = ra[i] - ra_variant[i];
                while (Math.abs(delta_radec[2 * i - 1]) > Math.abs(delta_radec[2 * i - 1] - 2.0 * Math.PI))
                    delta_radec[2 * i - 1] = delta_radec[2 * i - 1] - 2.0 * Math.PI;
                while (Math.abs(delta_radec[2 * i - 1]) > Math.abs(delta_radec[2 * i - 1] + 2.0 * Math.PI))
                    delta_radec[2 * i - 1] = delta_radec[2 * i - 1] + 2.0 * Math.PI;
                delta_radec[2 * i] = dec[i] - dec_variant[i];

                residual = residual + Math.pow(delta_radec[2 * i - 1], 2) + Math.pow(delta_radec[2 * i], 2);


            }

            residual = Math.sqrt(residual);

            /*  Set flag in case of divergence  */
            if (residual > 1.2 * old_residual) r[0] = 5.0;

        }

        /*  In case of divergence, return input state vector  */
        if (r[0] == 5.0) {
            for (i = 1; i <= 3; i++) {
                r[i] = r_input[i];
                rprime[i] = rprime_input[i];
            }
        }

    }


    public double laplaceMethod(double Lx[], double Ly[], double Lz[], double x[], double y[], double z[], double time1, double time2, double time3, double r2[], double r2prime[]) {

        /*

          Procedure to use the Laplace method to determine a preliminary orbit using three sets of optical observations.

          Algorithm taken from Pedro Escobal, "Methods of Orbit Determination", pp. 261-270.

          Note:  Station coordinates (x, y, and z) should be in heliocentric units, and times in Julian days.  The state vector r2 (and r2prime) are given in heliocentric units (per canonical time unit).  The Julian day of the epoch to which the elements refer is "jdepoch", which is returned.

          Note:  Station coordinates are inward pointing; that is, vectors x, y, and z point from the observer to the barycenter of the solar system.

          Note:  Due to the vaguaries of the Java compiler, I must reuse variable names, as follows:
            r2_mag = s1
            increment = s2
            oldsign = s3
            sign = s4
            rho2 = s5
            rho2dot = s6

          Tested and verified 15 June 1999.

        */

        double[] L2dot = new double[4];
        double[] L2dotdot = new double[4];
        double[] R2dot = new double[4];
        double[] R2dotdot = new double[4];

        double tau1 = 0, tau3 = 0, s1 = 0, s2 = 0, s3 = 0, s4 = 0, s5 = 0, s6 = 0, delta = 0, Da = 0, Db = 0, Dc = 0, Dd = 0, A2star = 0, B2star = 0, jdepoch = 0;
        double C2star = 0, D2star = 0, Cpsi = 0, a = 0, b = 0, c = 0;


        tau1 = kappa * (time1 - time2);
        tau3 = kappa * (time3 - time2);

        s1 = -tau3 / (tau1 * (tau1 - tau3));
        s2 = -(tau3 + tau1) / (tau1 * tau3);
        s3 = -tau1 / (tau3 * (tau3 - tau1));
        s4 = 2 / (tau1 * (tau1 - tau3));
        s5 = 2 / (tau1 * tau3);
        s6 = 2 / (tau3 * (tau3 - tau1));

        L2dot[1] = s1 * Lx[1] + s2 * Lx[2] + s3 * Lx[3];
        L2dot[2] = s1 * Ly[1] + s2 * Ly[2] + s3 * Ly[3];
        L2dot[3] = s1 * Lz[1] + s2 * Lz[2] + s3 * Lz[3];
        L2dotdot[1] = s4 * Lx[1] + s5 * Lx[2] + s6 * Lx[3];
        L2dotdot[2] = s4 * Ly[1] + s5 * Ly[2] + s6 * Ly[3];
        L2dotdot[3] = s4 * Lz[1] + s5 * Lz[2] + s6 * Lz[3];

        R2dot[1] = s1 * x[1] + s2 * x[2] + s3 * x[3];
        R2dot[2] = s1 * y[1] + s2 * y[2] + s3 * y[3];
        R2dot[3] = s1 * z[1] + s2 * z[2] + s3 * z[3];
        R2dotdot[1] = s4 * x[1] + s5 * x[2] + s6 * x[3];
        R2dotdot[2] = s4 * y[1] + s5 * y[2] + s6 * y[3];
        R2dotdot[3] = s4 * z[1] + s5 * z[2] + s6 * z[3];

        delta = 2 * (Lx[2] * (L2dot[2] * L2dotdot[3] - L2dot[3] * L2dotdot[2]) - Ly[2] * (L2dot[1] * L2dotdot[3] - L2dot[3] * L2dotdot[1]) + Lz[2] * (L2dot[1] * L2dotdot[2] - L2dot[2] * L2dotdot[1]));

        Da = Lx[2] * (L2dot[2] * R2dotdot[3] - L2dot[3] * R2dotdot[2]) - Ly[2] * (L2dot[1] * R2dotdot[3] - L2dot[3] * R2dotdot[1]) + Lz[2] * (L2dot[1] * R2dotdot[2] - L2dot[2] * R2dotdot[1]);

        Db = Lx[2] * (L2dot[2] * z[2] - L2dot[3] * y[2]) - Ly[2] * (L2dot[1] * z[2] - L2dot[3] * x[2]) + Lz[2] * (L2dot[1] * y[2] - L2dot[2] * x[2]);

        Dc = Lx[2] * (R2dotdot[2] * L2dotdot[3] - R2dotdot[3] * L2dotdot[2]) - Ly[2] * (R2dotdot[1] * L2dotdot[3] - R2dotdot[3] * L2dotdot[1]) + Lz[2] * (R2dotdot[1] * L2dotdot[2] - R2dotdot[2] * L2dotdot[1]);

        Dd = Lx[2] * (y[2] * L2dotdot[3] - z[2] * L2dotdot[2]) - Ly[2] * (x[2] * L2dotdot[3] - z[2] * L2dotdot[1]) + Lz[2] * (x[2] * L2dotdot[2] - y[2] * L2dotdot[1]);

        A2star = 2 * Da / delta;
        B2star = 2 * Db / delta;
        C2star = Dc / delta;
        D2star = Dd / delta;
        Cpsi = -2 * (Lx[2] * x[2] + Ly[2] * y[2] + Lz[2] * z[2]);

        a = -(Cpsi * A2star + Math.pow(A2star, 2) + (Math.pow(x[2], 2) + Math.pow(y[2], 2) + Math.pow(z[2], 2)));
        b = -mu * (Cpsi * B2star + 2 * A2star * B2star);
        c = -Math.pow(mu, 2) * Math.pow(B2star, 2);

        /*  Solve the eighth degree polynomial to find the applicable real root  */
        s1 = .01;
        s2 = 1;
        if ((Math.pow(s1, 8) + a * Math.pow(s1, 6) + b * Math.pow(s1, 3) + c) > 0)
            s3 = 1;
        else
            s3 = -1;
        s1 = s1 + s2;
        while (s2 > 5e-09) {
            if ((Math.pow(s1, 8) + a * Math.pow(s1, 6) + b * Math.pow(s1, 3) + c) > 0)
                s4 = 1;
            else
                s4 = -1;
            if (s4 != s3) {
                s1 = s1 - s2 / 2;
                s2 = s2 / 2;
            } else
                s1 = s1 + s2;
        }

        s5 = A2star + mu * B2star / Math.pow(s1, 3);
        s6 = C2star + mu * D2star / Math.pow(s1, 3);

        r2[1] = s5 * Lx[2] - x[2];
        r2[2] = s5 * Ly[2] - y[2];
        r2[3] = s5 * Lz[2] - z[2];

        r2prime[1] = s6 * Lx[2] + s5 * L2dot[1] - R2dot[1];
        r2prime[2] = s6 * Ly[2] + s5 * L2dot[2] - R2dot[2];
        r2prime[3] = s6 * Lz[2] + s5 * L2dot[3] - R2dot[3];

        jdepoch = time2;

        return jdepoch;

    }


    public void getClassicalUncertainties(double classical_elements[], double uncertainties[]) {

        /*
            Method to calculate the heliocentric ecliptical classical orbital elements, and estimate their uncertainties, given the barycentric equatorial epoch state vector and covariance matrix (available as instance variables of the MinorPlanet object class).
            Tested and verified 11 Nov 1999.
          */

        double[] r_2body = new double[4];
        double[] rprime_2body = new double[4];
        double[] variant_elements = new double[9];
        double[] sun_r = new double[4];
        double[] sun_rprime = new double[4];
        double[][] rotation_matrix = new double[9][9];
        double[][] inter = new double[9][9];
        double[][] ecliptic_bigp = new double[9][9];
        double[][] partial_matrix = new double[9][9];
        double[][] classical_covariance = new double[9][9];

        double delta = 0, diff = 0;

        int i = 0, j = 0, k = 0;

        /* Initialize the two-body state vector */
        getPlanetPosVel(epoch_time, 11, sun_r, sun_rprime);
        for (i = 1; i <= 3; i++) {
            r_2body[i] = epoch_r[i] - sun_r[i];
            rprime_2body[i] = (epoch_rprime[i] - sun_rprime[i]) / kappa;
        }

        /* Rotate the two-body state vector into ecliptic */
        TimeUtils.convertEquatorialToEcliptic(r_2body);
        TimeUtils.convertEquatorialToEcliptic(rprime_2body);

        /* Rotate the covariances into ecliptic */
        rotation_matrix[1][1] = 1.0;
        rotation_matrix[1][2] = 0.0;
        rotation_matrix[1][3] = 0.0;
        rotation_matrix[2][1] = 0.0;
        rotation_matrix[2][2] = Math.cos(epsilon);
        rotation_matrix[2][3] = Math.sin(epsilon);
        rotation_matrix[3][1] = 0.0;
        rotation_matrix[3][2] = -Math.sin(epsilon);
        rotation_matrix[3][3] = Math.cos(epsilon);
        rotation_matrix[4][4] = 1.0 / kappa;
        rotation_matrix[4][5] = 0.0;
        rotation_matrix[4][6] = 0.0;
        rotation_matrix[5][4] = 0.0;
        rotation_matrix[5][5] = Math.cos(epsilon) / kappa;
        rotation_matrix[5][6] = Math.sin(epsilon) / kappa;
        rotation_matrix[6][4] = 0.0;
        rotation_matrix[6][5] = -Math.sin(epsilon) / kappa;
        rotation_matrix[6][6] = Math.cos(epsilon) / kappa;
        rotation_matrix[7][7] = 1.0;
        rotation_matrix[8][8] = 1.0;
        for (i = 1; i <= 8; i++) {
            for (j = 1; j <= 8; j++) {
                inter[i][j] = 0;
                for (k = 1; k <= 8; k++)
                    inter[i][j] = inter[i][j] + big_p[i][k] * rotation_matrix[j][k];
            }
        }
        for (i = 1; i <= 8; i++) {
            for (j = 1; j <= 8; j++) {
                ecliptic_bigp[i][j] = 0;
                for (k = 1; k <= 8; k++)
                    ecliptic_bigp[i][j] = ecliptic_bigp[i][j] + rotation_matrix[i][k] * inter[k][j];
            }
        }

        /* Initialize the classical elements */
        for (j = 1; j <= 8; j++)
            classical_elements[j] = 0.0;

        /* Find the classical orbital elements */
        convertStateVectorToKeplerianElements(r_2body, rprime_2body, classical_elements, epoch_time);

        /* Calculate the partial derivative matrix */
        for (i = 1; i <= 3; i++) {
            /* Vary r_2body[i] and determine the changes in the classical elements */
            delta = 0.000001 * r_2body[i];
            /* Avoid dividing by zero */
            if (Math.abs(delta) < 1e-15) delta = 0.000001;
            r_2body[i] = r_2body[i] + delta;
            convertStateVectorToKeplerianElements(r_2body, rprime_2body, variant_elements, epoch_time);
            for (j = 1; j <= 8; j++) {
                diff = variant_elements[j] - classical_elements[j];
                if ((j == 3) || (j == 4) || (j == 5) || (j == 7)) {
                    while (Math.abs(diff) > Math.abs(diff - 2.0 * Math.PI))
                        diff = diff - 2.0 * Math.PI;
                    while (Math.abs(diff) > Math.abs(diff + 2.0 * Math.PI))
                        diff = diff + 2.0 * Math.PI;
                }
                partial_matrix[j][i] = diff / delta;
            }
            r_2body[i] = r_2body[i] - delta;
            /* Vary rprime_2body[i] and determine the changes in the classical elements */
            delta = 0.000001 * rprime_2body[i];
            /* Avoid dividing by zero */
            if (Math.abs(delta) < 1e-15) delta = 0.0000001;
            rprime_2body[i] = rprime_2body[i] + delta;
            convertStateVectorToKeplerianElements(r_2body, rprime_2body, variant_elements, epoch_time);
            for (j = 1; j <= 8; j++) {
                diff = variant_elements[j] - classical_elements[j];
                if ((j == 3) || (j == 4) || (j == 5) || (j == 7)) {
                    while (Math.abs(diff) > Math.abs(diff - 2.0 * Math.PI))
                        diff = diff - 2.0 * Math.PI;
                    while (Math.abs(diff) > Math.abs(diff + 2.0 * Math.PI))
                        diff = diff + 2.0 * Math.PI;
                }
                partial_matrix[j][i + 3] = diff / delta;
            }
            rprime_2body[i] = rprime_2body[i] - delta;
        }
        /* Convert covariances from pos/vel to classical elements */
        for (i = 1; i <= 6; i++) {
            for (j = 1; j <= 8; j++) {
                inter[i][j] = 0;
                for (k = 1; k <= 6; k++)
                    inter[i][j] = inter[i][j] + ecliptic_bigp[i][k] * partial_matrix[j][k];
            }
        }
        for (i = 1; i <= 8; i++) {
            for (j = 1; j <= 8; j++) {
                classical_covariance[i][j] = 0;
                for (k = 1; k <= 6; k++)
                    classical_covariance[i][j] = classical_covariance[i][j] + partial_matrix[i][k] * inter[k][j];
            }
        }
        /* Find the classical uncertainties */
        for (j = 1; j <= 8; j++)
            uncertainties[j] = Math.sqrt(classical_covariance[j][j]);


    }


    void advanceEphemerisEpoch(double oldr[], double oldrprime[], double oldtime, double old_big_p[][], double newr[], double newrprime[], double newtime, double new_big_p[][]) {
        /*
            Method to advance an ephemeris state vector and error covariance matrix from Julian TDB oldtime to Julian TDB newtime.
            Note that this does NOT impact the epoch variables; only those explicitly passed as arguments from within an ephemeris calculation.
          */

        int i = 0, j = 0, k = 0;

        double[][] phi = new double[9][9];
        double[][] inter = new double[9][9];
        double[][] collision_variant = new double[2000][9];

        /* Advance the state vector */
        update(oldr, oldrprime, oldtime, newr, newrprime, newtime, false, collision_variant);

        /* Advance the error covariance matrix */
        getEphemerisStateTransitionMatrix(oldr, oldrprime, oldtime, newr, newrprime, newtime, phi);
        for (i = 1; i <= 8; i++) {
            for (j = 1; j <= 8; j++) {
                inter[i][j] = 0;
                for (k = 1; k <= 8; k++)
                    inter[i][j] = inter[i][j] + old_big_p[i][k] * phi[j][k];
            }
        }
        for (i = 1; i <= 8; i++) {
            for (j = 1; j <= 8; j++) {
                new_big_p[i][j] = 0;
                for (k = 1; k <= 8; k++)
                    new_big_p[i][j] = new_big_p[i][j] + phi[i][k] * inter[k][j];
            }
        }

    }


    void getEphemerisStateTransitionMatrix(double oldr[], double oldrprime[], double oldtime, double rnominal[], double rprimenominal[], double time, double phi[][]) {

        /*
            Method to calculate the state transition matrix in going from a prior state vector at oldtime, to the new nominal state vector at time.
            Note that the purpose of this method is to enable method "advance_ephemeris_epoch" to advance the ephemeris state vector from oldtime to time.  It does NOT impact the epoch state vector.
          */

        double[] rvariant = new double[4];
        double[] rprimevariant = new double[4];
        double[][] collision_variant = new double[2000][9];

        double delta = 0;

        int i = 0, j = 0;

        /* Calculate the state transition matrix phi at time */
        for (i = 1; i <= 3; i++) {
            /* Vary oldr[i], and determine the change in the state vector at time */
            delta = 0.000001 * oldr[i];
            oldr[i] = oldr[i] + delta;
            update(oldr, oldrprime, oldtime, rvariant, rprimevariant, time, false, collision_variant);
            for (j = 1; j <= 3; j++) {
                phi[j][i] = (rvariant[j] - rnominal[j]) / delta;
                phi[j + 3][i] = (rprimevariant[j] - rprimenominal[j]) / delta;
            }
            oldr[i] = oldr[i] - delta;
            /* Vary oldrprime[i], and determine the change in the state vector at time */
            delta = 0.000001 * oldrprime[i];
            oldrprime[i] = oldrprime[i] + delta;
            update(oldr, oldrprime, oldtime, rvariant, rprimevariant, time, false, collision_variant);
            for (j = 1; j <= 3; j++) {
                phi[j][i + 3] = (rvariant[j] - rnominal[j]) / delta;
                phi[j + 3][i + 3] = (rprimevariant[j] - rprimenominal[j]) / delta;
            }
            oldrprime[i] = oldrprime[i] - delta;
            /* If non-gravitational thrust parameters A1_A2_DT are being utilized, vary A1_A2_DT[i], and determine the change in the state vector at time */
            if ((Math.abs(A1_A2_DT[i]) > 0.0) && (i < 3)) {
                delta = 0.1 * A1_A2_DT[i];
                A1_A2_DT[i] = A1_A2_DT[i] + delta;
                update(oldr, oldrprime, oldtime, rvariant, rprimevariant, time, false, collision_variant);
                for (j = 1; j <= 3; j++) {
                    phi[j][i + 6] = (rvariant[j] - rnominal[j]) / delta;
                    phi[j + 3][i + 6] = (rprimevariant[j] - rprimenominal[j]) / delta;
                }
                phi[i + 6][i + 6] = 1.0;
                A1_A2_DT[i] = A1_A2_DT[i] - delta;
            }
        }

    }


    public void makeEphemeris(boolean geocentric, double latitude, double longitude, double altitude, double start_time, double end_time, double interval, double ephemeris_radec[][], double magnitudes[], double ephemeris_support_data[][]) {
        /*
            Method to create an ephemeris for the minor planet, given the observer location, start and end times, and interval between entries.
           The inputs:
              - geocentric is a boolean determining whether the ephemeris will be geocentric or topocentric;
              - if topocentric, the geodetic latitude (in radians), east longitude (in radians), and altitude (in meters) give the coordinates of the observing site;
              - the ephemeris start and end times, as well as the interval between entries, are Julian TDT dates and times.
           The outputs:
              - the radec in radians
              - the estimated visual magnitudes
              - ephemeris_support_data contains the following precovery-oriented data:
                  - ephemeris_support_data[i][1] = Linear 3-sigma Ellipse position angle
                  - ephemeris_support_data[i][2] = Linear 3-sigma Ellipse major axis
                  - ephemeris_support_data[i][3] = Linear 3-sigma Ellipse minor axis
                  - ephemeris_support_data[i][4] = apparent motion in arc seconds per minute
                  - ephemeris_support_data[i][5] = position angle of apparent motion
                  - ephemeris_support_data[i][6] = observer-minor planet distance in AUs (delta)
                  - ephemeris_support_data[i][7] = Sun-minor planet distance in AUs (r)
                  - ephemeris_support_data[i][8] = minor planet elongation in radians
            Note that this method relies upon the availability of the epoch state vector, error covariance matrix, and epoch time, plus absolute_magnitude_H and slope_parameter_G.
            The strategy will be to successively advance the state vector and covariance matrix; at each step, calculate the radec, estimated magnitude, and support date.
          */

        double[] ephemeris_r = new double[4];
        double[] ephemeris_rprime = new double[4];
        double[][] ephemeris_big_p = new double[9][9];
        double[] observer_r = new double[4];
        double[] observer_rprime = new double[4];
        double[] observed_radec = new double[3];
        double[] old_ephemeris_r = new double[4];
        double[] old_ephemeris_rprime = new double[4];
        double[][] old_ephemeris_big_p = new double[9][9];
        double[] ephemeris_data = new double[9];
        double[][] radec_covariances = new double[3][3];

        double current_tdb_time = 0, old_tdb_time = 0, ut1_minus_tai = 0, current_tdt_time = 0, old_tdt_time = 0;

        int i = 1, j = 0, k = 0, counter_limit = 0;

        /* Since state vector integrations occur in TDB but ephemeris start-stop-intervals are in TDT, we will iterate over the current_tdt_time */
        current_tdt_time = start_time;

        /* Convert current_tdt_time to TDB for use in state-vector integration */
        ut1_minus_tai = -TimeUtils.getLeapSecond(current_tdt_time);
        current_tdb_time = TimeUtils.convertTDTtoTDB(current_tdt_time, latitude, longitude, altitude, ut1_minus_tai);

        /* Initialize the ephemeris state vector and error covariance matrix  */
        advanceEphemerisEpoch(epoch_r, epoch_rprime, epoch_time, big_p, ephemeris_r, ephemeris_rprime, current_tdb_time, ephemeris_big_p);

        /* Loop over current_tdt_time, calculating radecs and uncertainties, until end_time is exceeded. */
        while (current_tdt_time <= end_time) {
            getObservedRadec(ephemeris_r, ephemeris_rprime, current_tdb_time, geocentric, latitude, longitude, altitude, ut1_minus_tai, observer_r, observer_rprime, current_tdt_time, observed_radec);
            getRadecCovariance(ephemeris_r, ephemeris_rprime, current_tdb_time, ephemeris_big_p, geocentric, latitude, longitude, altitude, ut1_minus_tai, observed_radec, current_tdt_time, radec_covariances);
            if (comet_identifier == false)
                magnitudes[i] = apparentMagnitude(current_tdb_time, ephemeris_r);
            else
                magnitudes[i] = cometApparentMagnitude(current_tdb_time, ephemeris_r);
            /* Calculate the ephemeris_support_data */
            getEphemerisSupportData(ephemeris_r, ephemeris_rprime, current_tdb_time, geocentric, latitude, longitude, altitude, ut1_minus_tai, current_tdt_time, observed_radec, radec_covariances, ephemeris_data);
            for (j = 1; j <= 2; j++) {
                ephemeris_radec[i][j] = observed_radec[j];
            }
            for (j = 1; j <= 8; j++) {
                ephemeris_support_data[i][j] = ephemeris_data[j];
            }
            /* Get ready to advance ephemeris epoch */
            for (j = 1; j <= 3; j++) {
                old_ephemeris_r[j] = ephemeris_r[j];
                old_ephemeris_rprime[j] = ephemeris_rprime[j];
                observer_r[j] = 0;
                observer_rprime[j] = 0;
            }
            for (j = 1; j <= 8; j++) {
                for (k = 1; k <= 8; k++)
                    old_ephemeris_big_p[j][k] = ephemeris_big_p[j][k];
            }
            old_tdt_time = current_tdt_time;
            old_tdb_time = current_tdb_time;
            /* Increment the TDT time and find the corresponding new TDB time */
            current_tdt_time = current_tdt_time + interval;
            i = i + 1;
            ut1_minus_tai = -TimeUtils.getLeapSecond(current_tdt_time);
            current_tdb_time = TimeUtils.convertTDTtoTDB(current_tdt_time, latitude, longitude, altitude, ut1_minus_tai);
            advanceEphemerisEpoch(old_ephemeris_r, old_ephemeris_rprime, old_tdb_time, old_ephemeris_big_p, ephemeris_r, ephemeris_rprime, current_tdb_time, ephemeris_big_p);
        }

    }


    void getRadecCovariance(double ephemeris_r[], double ephemeris_rprime[], double ephemeris_time, double ephemeris_big_p[][], boolean geocentric, double latitude, double longitude, double altitude, double ut1_minus_tai, double observed_radec[], double current_tdt_time, double radec_covariance[][]) {

        /*
            Method to calculate the covariance matrix for the observed_radec at TDT current_tdt_time, given the state vector and error covariance matrix at TDB ephemeris_time, the nominal observed_radec, and the observer coordinates.
           The inputs:
              - ephemeris_r and _rprime are the posvel components (in AU and AU/day) of the state vector at Julian TDB ephemeris_time;
              - ephemeris_big_p is the error covariance matrix at ephemeris_time;
              - geocentric is a boolean determining whether the ephemeris will be geocentric or topocentric;
              - if topocentric, the geodetic latitude (in radians), east longitude (in radians), and altitude (in meters) give the coordinates of the observing site;
              - ut1_minus_tai gives the value of (UT1-TAI) (in seconds) to be used in computing the ephemeris;
              - observed_radec are the nominal ra and dec in radians;
              - current_tdt_time is the current TDT time (corresponding to TDB ephemeris_time).
           The outputs:
              - radec_covariance is the 2x2 radec covariance matrix for TDB ephemeris_time.
          */

        double[] variant_radec = new double[3];
        double[] observer_r = new double[4];
        double[] observer_rprime = new double[4];
        double[][] partial_matrix = new double[3][9];
        double[][] inter = new double[9][3];

        double delta = 0, diff = 0;

        int i = 0, j = 0, k = 0;


        for (i = 1; i <= 3; i++) {
            /* Vary ephemeris_r[i], and determine the change in the predicted radec */
            delta = 0.000001 * ephemeris_r[i];
            ephemeris_r[i] = ephemeris_r[i] + delta;
            getObservedRadec(ephemeris_r, ephemeris_rprime, ephemeris_time, geocentric, latitude, longitude, altitude, ut1_minus_tai, observer_r, observer_rprime, current_tdt_time, variant_radec);
            diff = (variant_radec[1] - observed_radec[1]);
            while (Math.abs(diff) > Math.abs(diff - 2 * Math.PI))
                diff = diff - 2 * Math.PI;
            while (Math.abs(diff) > Math.abs(diff + 2 * Math.PI))
                diff = diff + 2 * Math.PI;
            partial_matrix[1][i] = diff / delta;
            diff = (variant_radec[2] - observed_radec[2]);
            while (Math.abs(diff) > Math.abs(diff - 2 * Math.PI))
                diff = diff - 2 * Math.PI;
            while (Math.abs(diff) > Math.abs(diff + 2 * Math.PI))
                diff = diff + 2 * Math.PI;
            partial_matrix[2][i] = diff / delta;
            ephemeris_r[i] = ephemeris_r[i] - delta;
            /* Vary ephemeris_rprime[i], and determine the change in the predicted radec */
            delta = 0.000001 * ephemeris_rprime[i];
            ephemeris_rprime[i] = ephemeris_rprime[i] + delta;
            getObservedRadec(ephemeris_r, ephemeris_rprime, ephemeris_time, geocentric, latitude, longitude, altitude, ut1_minus_tai, observer_r, observer_rprime, current_tdt_time, variant_radec);
            diff = (variant_radec[1] - observed_radec[1]);
            while (Math.abs(diff) > Math.abs(diff - 2 * Math.PI))
                diff = diff - 2 * Math.PI;
            while (Math.abs(diff) > Math.abs(diff + 2 * Math.PI))
                diff = diff + 2 * Math.PI;
            partial_matrix[1][i + 3] = diff / delta;
            diff = (variant_radec[2] - observed_radec[2]);
            while (Math.abs(diff) > Math.abs(diff - 2 * Math.PI))
                diff = diff - 2 * Math.PI;
            while (Math.abs(diff) > Math.abs(diff + 2 * Math.PI))
                diff = diff + 2 * Math.PI;
            partial_matrix[2][i + 3] = diff / delta;
            ephemeris_rprime[i] = ephemeris_rprime[i] - delta;
            /* If non-gravitational thrust parameters are being utilized, vary A1_A2_DT[i], and determine the change in the predicted radec */
            if ((Math.abs(A1_A2_DT[i]) > 0.0) && (i < 3)) {
                delta = 0.01 * A1_A2_DT[i];
                A1_A2_DT[i] = A1_A2_DT[i] + delta;
                getObservedRadec(ephemeris_r, ephemeris_rprime, ephemeris_time, geocentric, latitude, longitude, altitude, ut1_minus_tai, observer_r, observer_rprime, current_tdt_time, variant_radec);
                diff = (variant_radec[1] - observed_radec[1]);
                while (Math.abs(diff) > Math.abs(diff - 2 * Math.PI))
                    diff = diff - 2 * Math.PI;
                while (Math.abs(diff) > Math.abs(diff + 2 * Math.PI))
                    diff = diff + 2 * Math.PI;
                partial_matrix[1][i + 6] = diff / delta;
                diff = (variant_radec[2] - observed_radec[2]);
                while (Math.abs(diff) > Math.abs(diff - 2 * Math.PI))
                    diff = diff - 2 * Math.PI;
                while (Math.abs(diff) > Math.abs(diff + 2 * Math.PI))
                    diff = diff + 2 * Math.PI;
                partial_matrix[2][i + 6] = diff / delta;
                A1_A2_DT[i] = A1_A2_DT[i] - delta;
            }
        }

        /* Convert covariances from pos/vel to ra/dec */
        for (i = 1; i <= 8; i++) {
            for (j = 1; j <= 2; j++) {
                inter[i][j] = 0;
                for (k = 1; k <= 8; k++)
                    inter[i][j] = inter[i][j] + ephemeris_big_p[i][k] * partial_matrix[j][k];
            }
        }
        for (i = 1; i <= 2; i++) {
            for (j = 1; j <= 2; j++) {
                radec_covariance[i][j] = 0;
                for (k = 1; k <= 8; k++)
                    radec_covariance[i][j] = radec_covariance[i][j] + partial_matrix[i][k] * inter[k][j];
            }
        }

    }


    void getEphemerisSupportData(double ephemeris_r[], double ephemeris_rprime[], double current_tdb_time, boolean geocentric, double latitude, double longitude, double altitude, double ut1_minus_tai, double current_tdt_time, double observed_radec[], double radec_covariances[][], double ephemeris_data[]) {
        /*
            Method to calculate the secondary ephemeris data for the minor planet, given the state vector and error covariance matrix at TDB current_tdb_time, the nominal observed_radec at TDT current_tdt_time, and the observer coordinates.
           The inputs:
              - ephemeris_r and _rprime are the posvel components (in AU and AU/day) of the state vector at Julian TDB current_tdb_time;
              - geocentric is a boolean determining whether the ephemeris will be geocentric or topocentric;
              - if topocentric, the geodetic latitude (in radians), east longitude (in radians), and altitude (in meters) give the coordinates of the observing site;
              - ut1_minus_tai gives the value of (UT1-TAI) (in seconds) to be used in computing the ephemeris;
              - current_tdt_time is the current TDT time (corresponding to current_tdb_time);
              - observed_radec are the nominal ra and dec in radians;
              - radec_covariances is the radec covariance matrix at TDB current_tdb_time.
           The outputs:
                  - ephemeris_data[1] = Linear 3-sigma Ellipse position angle in radians
                  - ephemeris_data[2] = Linear 3-sigma Ellipse major axis in radians
                  - ephemeris_data[3] = Linear 3-sigma Ellipse minor axis in radians
                  - ephemeris_data[4] = apparent motion in arc seconds per minute
                  - ephemeris_data[5] = position angle of apparent motion in radians
                  - ephemeris_data[6] = observer-minor planet distance in AUs (delta)
                  - ephemeris_data[7] = Sun-minor planet distance in AUs (r)
                  - ephemeris_data[8] = minor planet elongation in radians
          Note: Position angle calculations based on Peter Van de Kamp, Principles of Astrometry, pp. 4-5, 25.
          */

        double[] observer_r = new double[4];
        double[] observer_rprime = new double[4];
        double[] variant_radec = new double[3];
        double[] sun_r = new double[4];
        double[] sun_vel = new double[4];
        double[] EOP = new double[8];

        double distance = 0, big_r = 0, little_r = 0, delta = 0, root1 = 0, root2 = 0, discriminant = 0, delta_ra = 0, delta_dec = 0, theta = 0;

        int j = 0;

        /* I derived general formulas for the eigenvalues and eigenvectors of the 2x2 radec_covariances matrix; these will be applied here to calculate the linear 3-sigma ellipse position angle and major-minor axes.
             The eigenvalues of the radec covariance matrix represent the major and minor axes of the error ellipse; the corresponding eigenvectors represent the "basis" of the major-minor axes coordinate system, expressed in terms of x and y.  The orientation of the semi-major axis with respect to x and y gives the position angle.  As part of my testing, I created a transition matrix (P) whose column vectors are the eigenvectors, and verified that P-inverse*A*P is a diagonal matrix whose entries are the original eigenvalues of the covariance matrix.
          */
        /* We will skip these calculations (and avoid division by zero) if the error covariance matrix is empty (i.e., no covariance data exists for the current orbit).  */
        if ((radec_covariances[1][1] != 0.0) || (radec_covariances[1][2] != 0.0) || (radec_covariances[2][1] != 0.0) || (radec_covariances[2][2] != 0.0)) {
            /* Begin by finding the eigenvalues of the covariance matrix */
            discriminant = Math.pow((radec_covariances[1][1] + radec_covariances[2][2]), 2) - 4.0 * (radec_covariances[1][1] * radec_covariances[2][2] - radec_covariances[1][2] * radec_covariances[2][1]);
            if (discriminant >= 0.0) {
                root1 = 0.5 * ((radec_covariances[1][1] + radec_covariances[2][2]) + Math.sqrt(Math.pow((radec_covariances[1][1] + radec_covariances[2][2]), 2) - 4.0 * (radec_covariances[1][1] * radec_covariances[2][2] - radec_covariances[1][2] * radec_covariances[2][1])));
                root2 = 0.5 * ((radec_covariances[1][1] + radec_covariances[2][2]) - Math.sqrt(Math.pow((radec_covariances[1][1] + radec_covariances[2][2]), 2) - 4.0 * (radec_covariances[1][1] * radec_covariances[2][2] - radec_covariances[1][2] * radec_covariances[2][1])));
            } else {
                root1 = 0.0;
                root2 = 0.0;
            }
            if (Math.abs(root1) > Math.abs(root2)) {
                ephemeris_data[2] = root1;
                ephemeris_data[3] = root2;
            } else {
                ephemeris_data[2] = root2;
                ephemeris_data[3] = root1;
            }
            if ((ephemeris_data[2] - radec_covariances[1][1]) != 0.0) {
                ephemeris_data[1] = Math.atan(radec_covariances[1][2] / (ephemeris_data[2] - radec_covariances[1][1]));
            } else {
                ephemeris_data[1] = Math.atan((ephemeris_data[2] - radec_covariances[2][2]) / radec_covariances[2][1]);
            }
            /* In order to get the position angle in the proper quadrant, must account for the fact that ra increases to the west */
            ephemeris_data[1] = -ephemeris_data[1];
            /* Finally, take the square root of the axes, multiply by three to correspond to the 3-sigma ellipse, and take the tangent to project onto the plane of sky. */
            ephemeris_data[2] = Math.tan(3.0 * Math.sqrt(Math.abs(ephemeris_data[2])));
            ephemeris_data[3] = Math.tan(3.0 * Math.sqrt(Math.abs(ephemeris_data[3])));
        } else {
            ephemeris_data[1] = 0.0;
            ephemeris_data[2] = 0.0;
            ephemeris_data[3] = 0.0;
        }

        /* Begin by calculating the observed radec one minute later */
        ut1_minus_tai = -TimeUtils.getLeapSecond(current_tdt_time + 1.0 / 1440.0);
        getObservedRadec(ephemeris_r, ephemeris_rprime, current_tdb_time, geocentric, latitude, longitude, altitude, ut1_minus_tai, observer_r, observer_rprime, (current_tdt_time + 1.0 / 1440.0), variant_radec);
        distance = Math.acos(Math.sin(variant_radec[2]) * Math.sin(observed_radec[2]) + Math.cos(variant_radec[2]) * Math.cos(observed_radec[2]) * Math.cos(variant_radec[1] - observed_radec[1]));
        /* Project distance travelled onto the plane of sky and convert to arc seconds */
        ephemeris_data[4] = Math.tan(distance);
        ephemeris_data[4] = ephemeris_data[4] * 180.0 * 3600.0 / Math.PI;
        /* Note: position angle measured clockwise from due north.  Need to get the correct quadrant, and account for the fact that ra increaes to the west. */
        delta_ra = variant_radec[1] - observed_radec[1];
        delta_dec = variant_radec[2] - observed_radec[2];
        if ((delta_ra > 0) && (delta_dec > 0)) {
            theta = Math.asin(Math.sin(Math.abs(delta_ra)) * Math.cos(observed_radec[2]) / Math.sin(distance));
            ephemeris_data[5] = -theta;
        } else if ((delta_ra < 0) && (delta_dec < 0)) {
            theta = Math.asin(Math.sin(Math.abs(delta_ra)) * Math.cos(variant_radec[2]) / Math.sin(distance));
            ephemeris_data[5] = Math.PI - theta;
        } else if ((delta_ra > 0) && (delta_dec < 0)) {
            theta = Math.asin(Math.sin(Math.abs(delta_ra)) * Math.cos(observed_radec[2]) / Math.sin(distance));
            ephemeris_data[5] = theta - Math.PI;
        } else {
            theta = Math.asin(Math.sin(Math.abs(delta_ra)) * Math.cos(variant_radec[2]) / Math.sin(distance));
            ephemeris_data[5] = theta;
        }

        /* Calculate the positions of the Sun and observer, and use them to calculate delta, r, and elongation. */
        getPlanetPosVel(current_tdb_time, 11, sun_r, sun_vel);
        if (geocentric) {
            planetaryEphemeris(current_tdb_time, observer_r, 0);
            for (j = 1; j <= 3; j++) {
                observer_r[j] = planet_r[3][j];
                observer_rprime[j] = planet_rprime[3][j];
            }
        } else {
            getObserverPosition(latitude, longitude, altitude, current_tdb_time, EOP, observer_r, observer_rprime);
        }
        delta = Math.sqrt(Math.pow((observer_r[1] - ephemeris_r[1]), 2) + Math.pow((observer_r[2] - ephemeris_r[2]), 2) + Math.pow((observer_r[3] - ephemeris_r[3]), 2));
        little_r = Math.sqrt(Math.pow((sun_r[1] - ephemeris_r[1]), 2) + Math.pow((sun_r[2] - ephemeris_r[2]), 2) + Math.pow((sun_r[3] - ephemeris_r[3]), 2));
        big_r = Math.sqrt(Math.pow((sun_r[1] - observer_r[1]), 2) + Math.pow((sun_r[2] - observer_r[2]), 2) + Math.pow((sun_r[3] - observer_r[3]), 2));
        ephemeris_data[6] = delta;
        ephemeris_data[7] = little_r;
        ephemeris_data[8] = Math.acos((Math.pow(delta, 2) + Math.pow(big_r, 2) - Math.pow(little_r, 2)) / (2.0 * delta * big_r));

    }


    public void readDataFromDisk(String filename) {

        /*
        Procedure to read from disk all observations and state vector data for the current minor planet.  In particular, all of the instance variables from class MinorPlanet (except the computational auxiliary variables) are read from file "filename".
        */

        int i = 0, j = 0;

        try {
            /* Read instance variables from disk */
            FileInputStream file = new FileInputStream(filename);
            BufferedInputStream buff = new BufferedInputStream(file);
            DataInputStream data = new DataInputStream(buff);

            comet_identifier = data.readBoolean();
            epoch_time = data.readDouble();

            for (i = 1; i <= 3; i++) {
                epoch_r[i] = data.readDouble();
                epoch_rprime[i] = data.readDouble();
                A1_A2_DT[i] = data.readDouble();
            }

            for (i = 1; i <= 8; i++) {
                for (j = 1; j <= 8; j++)
                    big_p[i][j] = data.readDouble();
            }

            absolute_magnitude_H = data.readDouble();
            slope_parameter_G = data.readDouble();
            comet_absolute_magnitude = data.readDouble();
            comet_slope_parameter = data.readDouble();

            number_of_optical_observations = data.readInt();
            number_of_delay_observations = data.readInt();
            number_of_doppler_observations = data.readInt();

            for (i = 1; i <= (number_of_optical_observations + number_of_delay_observations + number_of_doppler_observations); i++)
            {
                observation_type[i] = data.readInt();
                incremental_state_vector_epoch[i] = data.readDouble();
                for (j = 1; j <= 3; j++) {
                    incremental_state_vector_r[i][j] = data.readDouble();
                    incremental_state_vector_rprime[i][j] = data.readDouble();
                }
                optical_ra[i] = data.readDouble();
                ra_dev[i] = data.readDouble();
                optical_dec[i] = data.readDouble();
                dec_dev[i] = data.readDouble();
                optical_time[i] = data.readDouble();
                visual_magnitude[i] = data.readDouble();
                visual_magnitude_dev[i] = data.readDouble();
                observation_geocentric[i] = data.readBoolean();
                observation_latitude[i] = data.readDouble();
                observation_longitude[i] = data.readDouble();
                observation_altitude[i] = data.readDouble();
                for (j = 1; j <= 7; j++)
                    observation_EOP[i][j] = data.readDouble();
                for (j = 1; j <= 3; j++) {
                    observation_r[i][j] = data.readDouble();
                    observation_rprime[i][j] = data.readDouble();
                }
                radar_delay[i] = data.readDouble();
                delay_dev[i] = data.readDouble();
                radar_delay_receiver_time[i] = data.readDouble();
                radar_delay_receiver_latitude[i] = data.readDouble();
                radar_delay_receiver_longitude[i] = data.readDouble();
                radar_delay_receiver_altitude[i] = data.readDouble();
                for (j = 1; j <= 7; j++)
                    radar_delay_receiver_EOP[i][j] = data.readDouble();
                radar_delay_receiver_tdb_minus_utc[i] = data.readDouble();
                for (j = 1; j <= 3; j++) {
                    radar_delay_receiver_r[i][j] = data.readDouble();
                    radar_delay_receiver_rprime[i][j] = data.readDouble();
                }
                radar_delay_transmitter_latitude[i] = data.readDouble();
                radar_delay_transmitter_longitude[i] = data.readDouble();
                radar_delay_transmitter_altitude[i] = data.readDouble();
                for (j = 1; j <= 7; j++)
                    radar_delay_transmitter_EOP[i][j] = data.readDouble();
                radar_delay_transmitter_tdb_minus_utc[i] = data.readDouble();
                radar_delay_transmitter_frequency[i] = data.readDouble();
                radar_doppler[i] = data.readDouble();
                doppler_dev[i] = data.readDouble();
                radar_doppler_receiver_time[i] = data.readDouble();
                radar_doppler_receiver_latitude[i] = data.readDouble();
                radar_doppler_receiver_longitude[i] = data.readDouble();
                radar_doppler_receiver_altitude[i] = data.readDouble();
                for (j = 1; j <= 7; j++)
                    radar_doppler_receiver_EOP[i][j] = data.readDouble();
                radar_doppler_receiver_tdb_minus_utc[i] = data.readDouble();
                for (j = 1; j <= 3; j++) {
                    radar_doppler_receiver_r[i][j] = data.readDouble();
                    radar_doppler_receiver_rprime[i][j] = data.readDouble();
                }
                radar_doppler_transmitter_latitude[i] = data.readDouble();
                radar_doppler_transmitter_longitude[i] = data.readDouble();
                radar_doppler_transmitter_altitude[i] = data.readDouble();
                for (j = 1; j <= 7; j++)
                    radar_doppler_transmitter_EOP[i][j] = data.readDouble();
                radar_doppler_transmitter_tdb_minus_utc[i] = data.readDouble();
                radar_doppler_transmitter_frequency[i] = data.readDouble();
            }

            minor_planet_radius = data.readDouble();

            data.close();

        } catch (FileNotFoundException fnfe) {
            System.out.println("Error -- " + fnfe.toString());
        } catch (IOException ioe) {
            System.out.println("Error -- " + ioe.toString());
        } catch (SecurityException se) {
            System.out.println("Error -- " + se.toString());
        }
    }


    public void writeDataToDisk(String filename) {

        /*
        Procedure to write to disk all observations and state vector data for the current minor planet.  In particular, all of the instance variables from class MinorPlanet (except the computational auxiliary variables) are written to file "filename".
        */

        int i = 0, j = 0;

        double temp = 0;

        try {
            /* Write instance variables to disk */
            FileOutputStream file = new FileOutputStream(filename);
            BufferedOutputStream buff = new BufferedOutputStream(file);
            DataOutputStream data = new DataOutputStream(buff);

            data.writeBoolean(comet_identifier);
            data.writeDouble(epoch_time);

            for (i = 1; i <= 3; i++) {
                data.writeDouble(epoch_r[i]);
                data.writeDouble(epoch_rprime[i]);
                data.writeDouble(A1_A2_DT[i]);
            }

            for (i = 1; i <= 8; i++) {
                for (j = 1; j <= 8; j++)
                    data.writeDouble(big_p[i][j]);
            }

            data.writeDouble(absolute_magnitude_H);
            data.writeDouble(slope_parameter_G);
            data.writeDouble(comet_absolute_magnitude);
            data.writeDouble(comet_slope_parameter);

            data.writeInt(number_of_optical_observations);
            data.writeInt(number_of_delay_observations);
            data.writeInt(number_of_doppler_observations);

            for (i = 1; i <= (number_of_optical_observations + number_of_delay_observations + number_of_doppler_observations); i++)
            {
                data.writeInt(observation_type[i]);
                data.writeDouble(incremental_state_vector_epoch[i]);
                for (j = 1; j <= 3; j++) {
                    data.writeDouble(incremental_state_vector_r[i][j]);
                    data.writeDouble(incremental_state_vector_rprime[i][j]);
                }
                data.writeDouble(optical_ra[i]);
                data.writeDouble(ra_dev[i]);
                data.writeDouble(optical_dec[i]);
                data.writeDouble(dec_dev[i]);
                data.writeDouble(optical_time[i]);
                data.writeDouble(visual_magnitude[i]);
                data.writeDouble(visual_magnitude_dev[i]);
                data.writeBoolean(observation_geocentric[i]);
                data.writeDouble(observation_latitude[i]);
                data.writeDouble(observation_longitude[i]);
                data.writeDouble(observation_altitude[i]);
                for (j = 1; j <= 7; j++)
                    data.writeDouble(observation_EOP[i][j]);
                for (j = 1; j <= 3; j++) {
                    data.writeDouble(observation_r[i][j]);
                    data.writeDouble(observation_rprime[i][j]);
                }
                data.writeDouble(radar_delay[i]);
                data.writeDouble(delay_dev[i]);
                data.writeDouble(radar_delay_receiver_time[i]);
                data.writeDouble(radar_delay_receiver_latitude[i]);
                data.writeDouble(radar_delay_receiver_longitude[i]);
                data.writeDouble(radar_delay_receiver_altitude[i]);
                for (j = 1; j <= 7; j++)
                    data.writeDouble(radar_delay_receiver_EOP[i][j]);
                data.writeDouble(radar_delay_receiver_tdb_minus_utc[i]);
                for (j = 1; j <= 3; j++) {
                    data.writeDouble(radar_delay_receiver_r[i][j]);
                    data.writeDouble(radar_delay_receiver_rprime[i][j]);
                }
                data.writeDouble(radar_delay_transmitter_latitude[i]);
                data.writeDouble(radar_delay_transmitter_longitude[i]);
                data.writeDouble(radar_delay_transmitter_altitude[i]);
                for (j = 1; j <= 7; j++)
                    data.writeDouble(radar_delay_transmitter_EOP[i][j]);
                data.writeDouble(radar_delay_transmitter_tdb_minus_utc[i]);
                data.writeDouble(radar_delay_transmitter_frequency[i]);
                data.writeDouble(radar_doppler[i]);
                data.writeDouble(doppler_dev[i]);
                data.writeDouble(radar_doppler_receiver_time[i]);
                data.writeDouble(radar_doppler_receiver_latitude[i]);
                data.writeDouble(radar_doppler_receiver_longitude[i]);
                data.writeDouble(radar_doppler_receiver_altitude[i]);
                for (j = 1; j <= 7; j++)
                    data.writeDouble(radar_doppler_receiver_EOP[i][j]);
                data.writeDouble(radar_doppler_receiver_tdb_minus_utc[i]);
                for (j = 1; j <= 3; j++) {
                    data.writeDouble(radar_doppler_receiver_r[i][j]);
                    data.writeDouble(radar_doppler_receiver_rprime[i][j]);
                }
                data.writeDouble(radar_doppler_transmitter_latitude[i]);
                data.writeDouble(radar_doppler_transmitter_longitude[i]);
                data.writeDouble(radar_doppler_transmitter_altitude[i]);
                for (j = 1; j <= 7; j++)
                    data.writeDouble(radar_doppler_transmitter_EOP[i][j]);
                data.writeDouble(radar_doppler_transmitter_tdb_minus_utc[i]);
                data.writeDouble(radar_doppler_transmitter_frequency[i]);
            }

            data.writeDouble(minor_planet_radius);

            data.close();

        } catch (FileNotFoundException fnfe) {
            System.out.println("Error -- " + fnfe.toString());
        } catch (IOException ioe) {
            System.out.println("Error -- " + ioe.toString());
        } catch (SecurityException se) {
            System.out.println("Error -- " + se.toString());
        }

    }


    public void readMinorPlanetListFromDisk() {

        /*
        Procedure to read from disk (file "minor_planet_list") the list of minor planets for whom data files exist.
        */

        int i = 1;

        boolean eof = false;

        try {
            /* Read minor planet list from disk */
            FileReader file = new FileReader("minor_planet_list");
            BufferedReader buff = new BufferedReader(file);

            while (!eof) {
                mp_list[i] = buff.readLine();
                if (mp_list[i] == null)
                    eof = true;
                else
                    i++;
            }
            number_of_minor_planets = (i - 1);

            buff.close();
        } catch (IOException e) {
            System.out.println("Error -- " + e.toString());
        }
    }


    public void writeMinorPlanetListToDisk() {

        /*
        Procedure to write to disk (file "minor_planet_list") the list of minor planets for whom data files exist.
        */

        int i = 0;

        try {
            /* Write minor planet list to disk */
            FileWriter file = new FileWriter("minor_planet_list");
            BufferedWriter buff = new BufferedWriter(file);

            for (i = 1; i <= number_of_minor_planets; i++) {
                buff.write(mp_list[i], 0, mp_list[i].length());
                buff.newLine();
            }

            buff.close();
        } catch (IOException e) {
            System.out.println("Error -- " + e.toString());
        }
    }


    public void sortObservations() {

        /*
        Method to sort the data associated with a minor planet, following the addition of an observation.
        Since observations must be ordered chronologically from earliest to most recent, it's necessary to ensure that a new observation is placed properly.
        Initially, the new observation will be put into the last slot in each instance variable.  A call to this method will determine if it should actually appear sooner; if so, the new observation is moved up into an earlier slot, and later observations are shifted down one slot.
        */

        double temp = 0, new_observation_date = 0, old_observation_date = 0;

        int index = (number_of_optical_observations + number_of_delay_observations + number_of_doppler_observations), itemp = 0, i = 0, j = 0, k = 0;

        boolean btemp = true, stop_now = false;

        /* Determine the time of the new observation */
        if (observation_type[index] == 1)
            new_observation_date = optical_time[index];
        else if (observation_type[index] == 2)
            new_observation_date = radar_delay_receiver_time[index];
        else if (observation_type[index] == 3)
            new_observation_date = radar_doppler_receiver_time[index];


        for (i = 1; i < index; i++) {

            /* Determine the time of each pre-existing observation */
            if (observation_type[i] == 1)
                old_observation_date = optical_time[i];
            else if (observation_type[i] == 2)
                old_observation_date = radar_delay_receiver_time[i];
            else if (observation_type[i] == 3)
                old_observation_date = radar_doppler_receiver_time[i];

            /* If the new observation is older than the pre-existing observation, move the new observation into its slot, and move the successive pre-existing observations down a slot. */
            if ((new_observation_date < old_observation_date) && (stop_now == false)) {
                itemp = observation_type[index];
                for (j = (index - 1); j >= i; j--)
                    observation_type[j + 1] = observation_type[j];
                observation_type[i] = itemp;
                temp = incremental_state_vector_epoch[index];
                for (j = (index - 1); j >= i; j--)
                    incremental_state_vector_epoch[j + 1] = incremental_state_vector_epoch[j];
                incremental_state_vector_epoch[i] = temp;
                for (k = 1; k <= 3; k++) {
                    temp = incremental_state_vector_r[index][k];
                    for (j = (index - 1); j >= i; j--)
                        incremental_state_vector_r[j + 1][k] = incremental_state_vector_r[j][k];
                    incremental_state_vector_r[i][k] = temp;
                }
                for (k = 1; k <= 3; k++) {
                    temp = incremental_state_vector_rprime[index][k];
                    for (j = (index - 1); j >= i; j--)
                        incremental_state_vector_rprime[j + 1][k] = incremental_state_vector_rprime[j][k];
                    incremental_state_vector_rprime[i][k] = temp;
                }
                temp = optical_ra[index];
                for (j = (index - 1); j >= i; j--)
                    optical_ra[j + 1] = optical_ra[j];
                optical_ra[i] = temp;
                temp = ra_dev[index];
                for (j = (index - 1); j >= i; j--)
                    ra_dev[j + 1] = ra_dev[j];
                ra_dev[i] = temp;
                temp = optical_dec[index];
                for (j = (index - 1); j >= i; j--)
                    optical_dec[j + 1] = optical_dec[j];
                optical_dec[i] = temp;
                temp = dec_dev[index];
                for (j = (index - 1); j >= i; j--)
                    dec_dev[j + 1] = dec_dev[j];
                dec_dev[i] = temp;
                temp = optical_time[index];
                for (j = (index - 1); j >= i; j--)
                    optical_time[j + 1] = optical_time[j];
                optical_time[i] = temp;
                temp = visual_magnitude[index];
                for (j = (index - 1); j >= i; j--)
                    visual_magnitude[j + 1] = visual_magnitude[j];
                visual_magnitude[i] = temp;
                temp = visual_magnitude_dev[index];
                for (j = (index - 1); j >= i; j--)
                    visual_magnitude_dev[j + 1] = visual_magnitude_dev[j];
                visual_magnitude_dev[i] = temp;
                btemp = observation_geocentric[index];
                for (j = (index - 1); j >= i; j--)
                    observation_geocentric[j + 1] = observation_geocentric[j];
                observation_geocentric[i] = btemp;
                temp = observation_latitude[index];
                for (j = (index - 1); j >= i; j--)
                    observation_latitude[j + 1] = observation_latitude[j];
                observation_latitude[i] = temp;
                temp = observation_longitude[index];
                for (j = (index - 1); j >= i; j--)
                    observation_longitude[j + 1] = observation_longitude[j];
                observation_longitude[i] = temp;
                temp = observation_altitude[index];
                for (j = (index - 1); j >= i; j--)
                    observation_altitude[j + 1] = observation_altitude[j];
                observation_altitude[i] = temp;
                for (k = 1; k <= 7; k++) {
                    temp = observation_EOP[index][k];
                    for (j = (index - 1); j >= i; j--)
                        observation_EOP[j + 1][k] = observation_EOP[j][k];
                    observation_EOP[i][k] = temp;
                }
                for (k = 1; k <= 3; k++) {
                    temp = observation_r[index][k];
                    for (j = (index - 1); j >= i; j--)
                        observation_r[j + 1][k] = observation_r[j][k];
                    observation_r[i][k] = temp;
                }
                for (k = 1; k <= 3; k++) {
                    temp = observation_rprime[index][k];
                    for (j = (index - 1); j >= i; j--)
                        observation_rprime[j + 1][k] = observation_rprime[j][k];
                    observation_rprime[i][k] = temp;
                }
                temp = radar_delay[index];
                for (j = (index - 1); j >= i; j--)
                    radar_delay[j + 1] = radar_delay[j];
                radar_delay[i] = temp;
                temp = delay_dev[index];
                for (j = (index - 1); j >= i; j--)
                    delay_dev[j + 1] = delay_dev[j];
                delay_dev[i] = temp;
                temp = radar_delay_receiver_time[index];
                for (j = (index - 1); j >= i; j--)
                    radar_delay_receiver_time[j + 1] = radar_delay_receiver_time[j];
                radar_delay_receiver_time[i] = temp;
                temp = radar_delay_receiver_latitude[index];
                for (j = (index - 1); j >= i; j--)
                    radar_delay_receiver_latitude[j + 1] = radar_delay_receiver_latitude[j];
                radar_delay_receiver_latitude[i] = temp;
                temp = radar_delay_receiver_longitude[index];
                for (j = (index - 1); j >= i; j--)
                    radar_delay_receiver_longitude[j + 1] = radar_delay_receiver_longitude[j];
                radar_delay_receiver_longitude[i] = temp;
                temp = radar_delay_receiver_altitude[index];
                for (j = (index - 1); j >= i; j--)
                    radar_delay_receiver_altitude[j + 1] = radar_delay_receiver_altitude[j];
                radar_delay_receiver_altitude[i] = temp;
                for (k = 1; k <= 7; k++) {
                    temp = radar_delay_receiver_EOP[index][k];
                    for (j = (index - 1); j >= i; j--)
                        radar_delay_receiver_EOP[j + 1][k] = radar_delay_receiver_EOP[j][k];
                    radar_delay_receiver_EOP[i][k] = temp;
                }
                temp = radar_delay_receiver_tdb_minus_utc[index];
                for (j = (index - 1); j >= i; j--)
                    radar_delay_receiver_tdb_minus_utc[j + 1] = radar_delay_receiver_tdb_minus_utc[j];
                radar_delay_receiver_tdb_minus_utc[i] = temp;
                for (k = 1; k <= 3; k++) {
                    temp = radar_delay_receiver_r[index][k];
                    for (j = (index - 1); j >= i; j--)
                        radar_delay_receiver_r[j + 1][k] = radar_delay_receiver_r[j][k];
                    radar_delay_receiver_r[i][k] = temp;
                }
                for (k = 1; k <= 3; k++) {
                    temp = radar_delay_receiver_rprime[index][k];
                    for (j = (index - 1); j >= i; j--)
                        radar_delay_receiver_rprime[j + 1][k] = radar_delay_receiver_rprime[j][k];
                    radar_delay_receiver_rprime[i][k] = temp;
                }
                temp = radar_delay_transmitter_latitude[index];
                for (j = (index - 1); j >= i; j--)
                    radar_delay_transmitter_latitude[j + 1] = radar_delay_transmitter_latitude[j];
                radar_delay_transmitter_latitude[i] = temp;
                temp = radar_delay_transmitter_longitude[index];
                for (j = (index - 1); j >= i; j--)
                    radar_delay_transmitter_longitude[j + 1] = radar_delay_transmitter_longitude[j];
                radar_delay_transmitter_longitude[i] = temp;
                temp = radar_delay_transmitter_altitude[index];
                for (j = (index - 1); j >= i; j--)
                    radar_delay_transmitter_altitude[j + 1] = radar_delay_transmitter_altitude[j];
                radar_delay_transmitter_altitude[i] = temp;
                for (k = 1; k <= 7; k++) {
                    temp = radar_delay_transmitter_EOP[index][k];
                    for (j = (index - 1); j >= i; j--)
                        radar_delay_transmitter_EOP[j + 1][k] = radar_delay_transmitter_EOP[j][k];
                    radar_delay_transmitter_EOP[i][k] = temp;
                }
                temp = radar_delay_transmitter_tdb_minus_utc[index];
                for (j = (index - 1); j >= i; j--)
                    radar_delay_transmitter_tdb_minus_utc[j + 1] = radar_delay_transmitter_tdb_minus_utc[j];
                radar_delay_transmitter_tdb_minus_utc[i] = temp;
                temp = radar_delay_transmitter_frequency[index];
                for (j = (index - 1); j >= i; j--)
                    radar_delay_transmitter_frequency[j + 1] = radar_delay_transmitter_frequency[j];
                radar_delay_transmitter_frequency[i] = temp;
                temp = radar_doppler[index];
                for (j = (index - 1); j >= i; j--)
                    radar_doppler[j + 1] = radar_doppler[j];
                radar_doppler[i] = temp;
                temp = doppler_dev[index];
                for (j = (index - 1); j >= i; j--)
                    doppler_dev[j + 1] = doppler_dev[j];
                doppler_dev[i] = temp;
                temp = radar_doppler_receiver_time[index];
                for (j = (index - 1); j >= i; j--)
                    radar_doppler_receiver_time[j + 1] = radar_doppler_receiver_time[j];
                radar_doppler_receiver_time[i] = temp;
                temp = radar_doppler_receiver_latitude[index];
                for (j = (index - 1); j >= i; j--)
                    radar_doppler_receiver_latitude[j + 1] = radar_doppler_receiver_latitude[j];
                radar_doppler_receiver_latitude[i] = temp;
                temp = radar_doppler_receiver_longitude[index];
                for (j = (index - 1); j >= i; j--)
                    radar_doppler_receiver_longitude[j + 1] = radar_doppler_receiver_longitude[j];
                radar_doppler_receiver_longitude[i] = temp;
                temp = radar_doppler_receiver_altitude[index];
                for (j = (index - 1); j >= i; j--)
                    radar_doppler_receiver_altitude[j + 1] = radar_doppler_receiver_altitude[j];
                radar_doppler_receiver_altitude[i] = temp;
                for (k = 1; k <= 7; k++) {
                    temp = radar_doppler_receiver_EOP[index][k];
                    for (j = (index - 1); j >= i; j--)
                        radar_doppler_receiver_EOP[j + 1][k] = radar_doppler_receiver_EOP[j][k];
                    radar_doppler_receiver_EOP[i][k] = temp;
                }
                temp = radar_doppler_receiver_tdb_minus_utc[index];
                for (j = (index - 1); j >= i; j--)
                    radar_doppler_receiver_tdb_minus_utc[j + 1] = radar_doppler_receiver_tdb_minus_utc[j];
                radar_doppler_receiver_tdb_minus_utc[i] = temp;
                for (k = 1; k <= 3; k++) {
                    temp = radar_doppler_receiver_r[index][k];
                    for (j = (index - 1); j >= i; j--)
                        radar_doppler_receiver_r[j + 1][k] = radar_doppler_receiver_r[j][k];
                    radar_doppler_receiver_r[i][k] = temp;
                }
                for (k = 1; k <= 3; k++) {
                    temp = radar_doppler_receiver_rprime[index][k];
                    for (j = (index - 1); j >= i; j--)
                        radar_doppler_receiver_rprime[j + 1][k] = radar_doppler_receiver_rprime[j][k];
                    radar_doppler_receiver_rprime[i][k] = temp;
                }
                temp = radar_doppler_transmitter_latitude[index];
                for (j = (index - 1); j >= i; j--)
                    radar_doppler_transmitter_latitude[j + 1] = radar_doppler_transmitter_latitude[j];
                radar_doppler_transmitter_latitude[i] = temp;
                temp = radar_doppler_transmitter_longitude[index];
                for (j = (index - 1); j >= i; j--)
                    radar_doppler_transmitter_longitude[j + 1] = radar_doppler_transmitter_longitude[j];
                radar_doppler_transmitter_longitude[i] = temp;
                temp = radar_doppler_transmitter_altitude[index];
                for (j = (index - 1); j >= i; j--)
                    radar_doppler_transmitter_altitude[j + 1] = radar_doppler_transmitter_altitude[j];
                radar_doppler_transmitter_altitude[i] = temp;
                for (k = 1; k <= 7; k++) {
                    temp = radar_doppler_transmitter_EOP[index][k];
                    for (j = (index - 1); j >= i; j--)
                        radar_doppler_transmitter_EOP[j + 1][k] = radar_doppler_transmitter_EOP[j][k];
                    radar_doppler_transmitter_EOP[i][k] = temp;
                }
                temp = radar_doppler_transmitter_tdb_minus_utc[index];
                for (j = (index - 1); j >= i; j--)
                    radar_doppler_transmitter_tdb_minus_utc[j + 1] = radar_doppler_transmitter_tdb_minus_utc[j];
                radar_doppler_transmitter_tdb_minus_utc[i] = temp;
                temp = radar_doppler_transmitter_frequency[index];
                for (j = (index - 1); j >= i; j--)
                    radar_doppler_transmitter_frequency[j + 1] = radar_doppler_transmitter_frequency[j];
                radar_doppler_transmitter_frequency[i] = temp;

                /* Set a flag to inhibit further shifting.  */
                stop_now = true;

            }

        }

    }

    public void cometLeastSquaresSolution(double[] residuals, double[] rms_residuals) {

        /*
            Method to calculate the least squares best-fit orbit of a comet, given optical, radar delay, and or radar doppler observations.  The process is repeated until the magnitude of the corrections to the epoch state vector is less than some percentage of their corresponding covariances.
            The method presumes that an a priori state vector has been selected, and resides in epoch_r, epoch_rprime, and epoch_time.  The method also assumes that the observations have been ordered from earliest to most recent.
            The method presumes that the user has selected a value for non-gravitational thrust parameter DT; the method will initialize values for non-gravitational thrust parameters A1 and A2, and iteratively solve for best-fit values.
            The output will be the epoch state vector, A1 and A2, and the epoch error covariance matrix (all of which are instance variables of the MinorPlanet class).
            Provision is made for excluding observations whose chi or residuals exceed user-defined thresholds; although these observations will not be deleted, they will not be used in the final iterations of the algorithm.
            Adapted from: D.K. Yeomans, S.J. Ostro, and P.W. Chodas (1987). "Radar Astrometry of Near-Earth Asteroids". Astronomical Journal 94, 189-200.  Modified to the "Square Root Information Filter" algorithm from Gerald J. Bierman (1974), "Sequential Square Root Filtering and Smoothing of Discrete Linear Systems", Automatica 10, 147-158.
            NOTE: In case of divergence, epoch_r[0] will be returned as 6.0.
            Tested and verified 1-6-00.
          */

        int i = 0, j = 0, k = 0, index = 0, retained_index = 0;

        boolean convergence = false, divergence = false, edit = false;

        double rms_optical = 0, rms_delay = 0, rms_doppler = 0;
        double old_rms_optical = 0, old_rms_delay = 0, old_rms_doppler = 0;
        double convergence_factor = 0.10;
        double delta = 0, delay = 0, doppler = 0;
        double observation_residual = 0, observation_chi = 0;

        int number_of_observations = number_of_optical_observations + number_of_delay_observations + number_of_doppler_observations;

        double[] nominal_r = new double[4];
        double[] nominal_rprime = new double[4];
        double[] initial_r = new double[4];
        double[] initial_rprime = new double[4];
        double[][] r_variant = new double[9][4];
        double[][] rprime_variant = new double[9][4];
        double[] this_r = new double[4];
        double[] this_rprime = new double[4];
        double[] next_r = new double[4];
        double[] next_rprime = new double[4];
        double[] variant_delta = new double[9];
        double[] dev = new double[16002];
        double[][] matrix_a = new double[16002][9];
        double[][] collision_variant = new double[2000][9];
        double[][] phi = new double[9][9];
        double[] inter = new double[9];
        double[] sensitivity_ra = new double[9];
        double[] sensitivity_dec = new double[9];
        double[] sensitivity_delay = new double[9];
        double[] sensitivity_doppler = new double[9];
        double[] radec = new double[3];
        double[] row = new double[9];
        double[] corrections = new double[9];
        double[] observer_r = new double[4];
        double[] observer_rprime = new double[4];
        double[] delay_doppler = new double[3];
        double[] receiver_r = new double[4];
        double[] receiver_rprime = new double[4];
        double[] EOP = new double[8];
        double[][] matrix_R = new double[9][9];
        double[] matrix_z = new double[9];
        double[][] matrix_T = new double[6002][6002];
        double[][] matrix_temp = new double[16002][9];
        double[] retained_residuals = new double[16002];
        double[] initial_residuals = new double[16002];
        double[] initial_rms_residuals = new double[4];
        double[][] initial_big_p = new double[9][9];
        double[] initial_A1_A2_DT = new double[3];

        /* Initialize A1 and A2; we will solve for best-fit values  */
        A1_A2_DT[1] = 2.0E-09;
        A1_A2_DT[2] = 0.5E-09;

        /* Initialize all observations as non-excluded for get_residuals */
        for (i = 1; i <= 8000; i++)
            excluded_observations[i] = 0;

        /* Using the a priori state vector, determine initial residuals, nominal observables, and the state vector at the tdb time of each observation.  Save the a priori state vector in case of divergence. */
        getResiduals(residuals, rms_residuals);
        rms_optical = rms_residuals[1];
        rms_delay = rms_residuals[2];
        rms_doppler = rms_residuals[3];
        for (i = 1; i <= 3; i++) {
            initial_r[i] = epoch_r[i];
            initial_rprime[i] = epoch_rprime[i];
            initial_rms_residuals[i] = rms_residuals[i];
        }
        for (i = 1; i <= 8; i++) {
            for (j = 1; j <= 8; j++)
                initial_big_p[i][j] = big_p[i][j];
        }
        initial_A1_A2_DT[1] = 0.0;
        initial_A1_A2_DT[2] = 0.0;
        index = 2 * number_of_optical_observations + number_of_delay_observations + number_of_doppler_observations;
        for (i = 1; i <= index; i++) {
            initial_residuals[i] = residuals[i];
        }

        /*
          The convergence condition is: The RMS residuals on the current loop must differ by no more than some percentage from the RMS residuals on the preceding loop.
          (Note that this condition was not my original choice.  I tried requiring that the corrections to the epoch state vector must be less than some percentage of their corresponding covariances.  However, real-world data is not sufficiently smooth.  This new condition uses the observed fact that the RMS residuals decrease with successive loops until the next loop results in almost no change.  At this point, the solution has converged.)
          Clearly, this condition will not be satisfied initially, so we will go through the loop at least once.
          NOTE:  MUST ACCOUNT FOR DIVERGENCE, AS MEASURED BY INCREASING RMS RESIDUALS.  If the RMS residuals increase from one loop to the next, the divergence flag will be set, and the original solution returned.
          */
        while ((convergence == false) && (divergence == false)) {

            /* Clear the counters */
            index = 0;
            retained_index = 0;

            /* Use the observations to generate rows of matrix_a */
            for (i = 1; i <= number_of_observations; i++) {

                /* Assume that the observation will be excluded; if not, the flag will be reset below. */
                excluded_observations[i] = 1;

                /* Generate the state transition matrix corresponding to this observation time */
                for (j = 1; j <= 3; j++) {
                    nominal_r[j] = incremental_state_vector_r[i][j];
                    nominal_rprime[j] = incremental_state_vector_rprime[i][j];
                }
                /* Initialize the variant trajectories */
                if (i == 1) {
                    for (j = 1; j <= 8; j++) {
                        for (k = 1; k <= 3; k++) {
                            r_variant[j][k] = epoch_r[k];
                            rprime_variant[j][k] = epoch_rprime[k];
                        }
                    }
                    for (j = 1; j <= 3; j++) {
                        variant_delta[j] = 0.000001 * epoch_r[j];
                        r_variant[j][j] = r_variant[j][j] + variant_delta[j];
                        variant_delta[j + 3] = 0.000001 * epoch_rprime[j];
                        rprime_variant[j + 3][j] = rprime_variant[j + 3][j] + variant_delta[j + 3];
                        if (j < 3)
                            variant_delta[j + 6] = 0.1 * A1_A2_DT[j];
                    }
                }
                /* Propogate the variant trajectories, and calculate the transition matrix entries */
                for (j = 1; j <= 8; j++) {
                    for (k = 1; k <= 3; k++) {
                        this_r[k] = r_variant[j][k];
                        this_rprime[k] = rprime_variant[j][k];
                    }
                    if (j > 6)
                        A1_A2_DT[j - 6] = A1_A2_DT[j - 6] + variant_delta[j];
                    update(this_r, this_rprime, incremental_state_vector_epoch[i - 1], next_r, next_rprime, incremental_state_vector_epoch[i], false, collision_variant);
                    if (j > 6)
                        A1_A2_DT[j - 6] = A1_A2_DT[j - 6] - variant_delta[j];
                    for (k = 1; k <= 3; k++) {
                        r_variant[j][k] = next_r[k];
                        rprime_variant[j][k] = next_rprime[k];
                    }
                    for (k = 1; k <= 3; k++) {
                        phi[k][j] = (r_variant[j][k] - nominal_r[k]) / variant_delta[j];
                        phi[k + 3][j] = (rprime_variant[j][k] - nominal_rprime[k]) / variant_delta[j];
                    }
                    if (j > 6)
                        phi[j][j] = 1.0;
                }

                /* Generate the sensitivity matrix for this observation */
                if (observation_type[i] == 1) {
                    /* Optical observation */
                    for (j = 1; j <= 3; j++) {
                        observer_r[j] = observation_r[i][j];
                        observer_rprime[j] = observation_rprime[i][j];
                    }
                    for (j = 1; j <= 3; j++) {
                        delta = 0.000001 * nominal_r[j];
                        nominal_r[j] = nominal_r[j] + delta;
                        getObservedRadec(nominal_r, nominal_rprime, incremental_state_vector_epoch[i], observation_geocentric[i], observation_latitude[i], observation_longitude[i], observation_altitude[i], observation_EOP[i][3], observer_r, observer_rprime, optical_time[i], radec);
                        sensitivity_ra[j] = radec[1] - nominal_observable[index + 1];
                        while (Math.abs(sensitivity_ra[j]) > Math.abs(sensitivity_ra[j] - 2 * Math.PI))
                            sensitivity_ra[j] = sensitivity_ra[j] - 2 * Math.PI;
                        while (Math.abs(sensitivity_ra[j]) > Math.abs(sensitivity_ra[j] + 2 * Math.PI))
                            sensitivity_ra[j] = sensitivity_ra[j] + 2 * Math.PI;
                        sensitivity_ra[j] = sensitivity_ra[j] / delta;
                        sensitivity_dec[j] = (radec[2] - nominal_observable[index + 2]) / delta;
                        nominal_r[j] = nominal_r[j] - delta;
                        delta = 0.000001 * nominal_rprime[j];
                        nominal_rprime[j] = nominal_rprime[j] + delta;
                        getObservedRadec(nominal_r, nominal_rprime, incremental_state_vector_epoch[i], observation_geocentric[i], observation_latitude[i], observation_longitude[i], observation_altitude[i], observation_EOP[i][3], observer_r, observer_rprime, optical_time[i], radec);
                        sensitivity_ra[j + 3] = radec[1] - nominal_observable[index + 1];
                        while (Math.abs(sensitivity_ra[j + 3]) > Math.abs(sensitivity_ra[j + 3] - 2 * Math.PI))
                            sensitivity_ra[j + 3] = sensitivity_ra[j + 3] - 2 * Math.PI;
                        while (Math.abs(sensitivity_ra[j + 3]) > Math.abs(sensitivity_ra[j + 3] + 2 * Math.PI))
                            sensitivity_ra[j + 3] = sensitivity_ra[j + 3] + 2 * Math.PI;
                        sensitivity_ra[j + 3] = sensitivity_ra[j + 3] / delta;
                        sensitivity_dec[j + 3] = (radec[2] - nominal_observable[index + 2]) / delta;
                        nominal_rprime[j] = nominal_rprime[j] - delta;
                        if (j < 3) {
                            delta = 0.1 * A1_A2_DT[j];
                            A1_A2_DT[j] = A1_A2_DT[j] + delta;
                            getObservedRadec(nominal_r, nominal_rprime, incremental_state_vector_epoch[i], observation_geocentric[i], observation_latitude[i], observation_longitude[i], observation_altitude[i], observation_EOP[i][3], observer_r, observer_rprime, optical_time[i], radec);
                            sensitivity_ra[j + 6] = radec[1] - nominal_observable[index + 1];
                            while (Math.abs(sensitivity_ra[j + 6]) > Math.abs(sensitivity_ra[j + 6] - 2 * Math.PI))
                                sensitivity_ra[j + 6] = sensitivity_ra[j + 6] - 2 * Math.PI;
                            while (Math.abs(sensitivity_ra[j + 6]) > Math.abs(sensitivity_ra[j + 6] + 2 * Math.PI))
                                sensitivity_ra[j + 6] = sensitivity_ra[j + 6] + 2 * Math.PI;
                            sensitivity_ra[j + 6] = sensitivity_ra[j + 6] / delta;
                            sensitivity_dec[j + 6] = (radec[2] - nominal_observable[index + 2]) / delta;
                            A1_A2_DT[j] = A1_A2_DT[j] - delta;
                        }
                    }

                    /* Calculate the residuals and chi; if observations meet the applicable thresholds, calculate the resulting rows of matrix_a */
                    observation_residual = Math.acos(Math.sin(nominal_observable[index + 2]) * Math.sin(optical_dec[i]) + Math.cos(nominal_observable[index + 2]) * Math.cos(optical_dec[i]) * Math.cos(nominal_observable[index + 1] - optical_ra[i]));
                    if ((ra_dev[i] > 0.0) && (dec_dev[i] > 0.0))
                        observation_chi = Math.sqrt(Math.pow(residuals[index + 1] * Math.cos(optical_dec[i]) / ra_dev[i], 2.0) + Math.pow(residuals[index + 2] / dec_dev[i], 2.0));
                    else
                        observation_chi = 0.0;
                    if (((edit) && (chi_testing) && (observation_chi < chi_threshold) && (ra_dev[i] < 5.0 * Math.PI / (3600.0 * 180.0)) && (dec_dev[i] < 5.0 * Math.PI / (3600.0 * 180.0))) || ((edit) && (residual_testing) && (Math.abs(observation_residual) < optical_residual_threshold) && (ra_dev[i] < 5.0 * Math.PI / (3600.0 * 180.0)) && (dec_dev[i] < 5.0 * Math.PI / (3600.0 * 180.0))) || (!edit)) {

                        /* Observation is not excluded; reset flag */
                        excluded_observations[i] = 0;

                        for (j = 1; j <= 8; j++) {
                            row[j] = 0.0;
                            for (k = 1; k <= 8; k++)
                                row[j] = row[j] + sensitivity_ra[k] * phi[k][j];
                            matrix_a[retained_index + 1][j] = row[j];
                        }
                        for (j = 1; j <= 8; j++) {
                            row[j] = 0.0;
                            for (k = 1; k <= 8; k++)
                                row[j] = row[j] + sensitivity_dec[k] * phi[k][j];
                            matrix_a[retained_index + 2][j] = row[j];
                        }

                        /* Fill the dev array with standard deviations */
                        dev[retained_index + 1] = ra_dev[i];
                        dev[retained_index + 2] = dec_dev[i];

                        /* Add these residuals to the list of those being retained */
                        retained_residuals[retained_index + 1] = residuals[index + 1];
                        retained_residuals[retained_index + 2] = residuals[index + 2];

                        /* Increment the retained observation counter */
                        retained_index = retained_index + 2;

                    }

                    /* Increment the index */
                    index = index + 2;

                } else if (observation_type[i] == 2) {
                    /* Delay observation */
                    for (j = 1; j <= 3; j++) {
                        receiver_r[j] = radar_delay_receiver_r[i][j];
                        receiver_rprime[j] = radar_delay_receiver_rprime[i][j];
                    }
                    for (j = 1; j <= 7; j++)
                        EOP[j] = radar_delay_transmitter_EOP[i][j];

                    for (j = 1; j <= 3; j++) {
                        delta = 0.000001 * nominal_r[j];
                        nominal_r[j] = nominal_r[j] + delta;
                        getObservedRadarDelayDoppler(radar_delay_receiver_latitude[i], radar_delay_receiver_longitude[i], radar_delay_receiver_altitude[i], receiver_r, receiver_rprime, radar_delay_receiver_time[i], radar_delay_transmitter_latitude[i], radar_delay_transmitter_longitude[i], radar_delay_transmitter_altitude[i], EOP, radar_delay_transmitter_tdb_minus_utc[i], radar_delay_receiver_tdb_minus_utc[i], radar_delay_transmitter_frequency[i], minor_planet_radius, nominal_r, nominal_rprime, incremental_state_vector_epoch[i], delay_doppler);
                        delay = delay_doppler[1];
                        sensitivity_delay[j] = (delay - nominal_observable[index + 1]) / delta;
                        nominal_r[j] = nominal_r[j] - delta;
                        delta = 0.000001 * nominal_rprime[j];
                        nominal_rprime[j] = nominal_rprime[j] + delta;
                        getObservedRadarDelayDoppler(radar_delay_receiver_latitude[i], radar_delay_receiver_longitude[i], radar_delay_receiver_altitude[i], receiver_r, receiver_rprime, radar_delay_receiver_time[i], radar_delay_transmitter_latitude[i], radar_delay_transmitter_longitude[i], radar_delay_transmitter_altitude[i], EOP, radar_delay_transmitter_tdb_minus_utc[i], radar_delay_receiver_tdb_minus_utc[i], radar_delay_transmitter_frequency[i], minor_planet_radius, nominal_r, nominal_rprime, incremental_state_vector_epoch[i], delay_doppler);
                        delay = delay_doppler[1];
                        sensitivity_delay[j + 3] = (delay - nominal_observable[index + 1]) / delta;
                        nominal_rprime[j] = nominal_rprime[j] - delta;
                        if (j < 3) {
                            delta = 0.1 * A1_A2_DT[j];
                            A1_A2_DT[j] = A1_A2_DT[j] + delta;
                            getObservedRadarDelayDoppler(radar_delay_receiver_latitude[i], radar_delay_receiver_longitude[i], radar_delay_receiver_altitude[i], receiver_r, receiver_rprime, radar_delay_receiver_time[i], radar_delay_transmitter_latitude[i], radar_delay_transmitter_longitude[i], radar_delay_transmitter_altitude[i], EOP, radar_delay_transmitter_tdb_minus_utc[i], radar_delay_receiver_tdb_minus_utc[i], radar_delay_transmitter_frequency[i], minor_planet_radius, nominal_r, nominal_rprime, incremental_state_vector_epoch[i], delay_doppler);
                            delay = delay_doppler[1];
                            sensitivity_delay[j + 6] = (delay - nominal_observable[index + 1]) / delta;
                            A1_A2_DT[j] = A1_A2_DT[j] - delta;
                        }
                    }

                    /* Calculate the residual and chi; if observations meet the applicable threshold, calculate the resulting row of matrix_a */
                    observation_residual = Math.abs(residuals[index + 1]);
                    if (delay_dev[i] != 0.0)
                        observation_chi = Math.abs(residuals[index + 1] / delay_dev[i]);
                    else
                        observation_chi = 0.0;
                    if (((edit) && (chi_testing) && (observation_chi < chi_threshold)) || ((edit) && (residual_testing) && (Math.abs(observation_residual) < delay_residual_threshold)) || (!edit)) {

                        /* Observation is not excluded; reset flag */
                        excluded_observations[i] = 0;

                        for (j = 1; j <= 8; j++) {
                            row[j] = 0.0;
                            for (k = 1; k <= 8; k++)
                                row[j] = row[j] + sensitivity_delay[k] * phi[k][j];
                            matrix_a[retained_index + 1][j] = row[j];
                        }

                        /* Fill the dev array with standard deviations */
                        dev[retained_index + 1] = delay_dev[i];

                        /* Add this residual to the list of those being retained */
                        retained_residuals[retained_index + 1] = residuals[index + 1];

                        /* Increment the retained observation counter */
                        retained_index = retained_index + 1;

                    }

                    /* Increment the index */
                    index = index + 1;

                } else if (observation_type[i] == 3) {
                    /* Doppler observation */
                    for (j = 1; j <= 3; j++) {
                        receiver_r[j] = radar_doppler_receiver_r[i][j];
                        receiver_rprime[j] = radar_doppler_receiver_rprime[i][j];
                    }
                    for (j = 1; j <= 7; j++) {
                        EOP[j] = radar_doppler_transmitter_EOP[i][j];
                    }

                    for (j = 1; j <= 3; j++) {
                        delta = 0.000001 * nominal_r[j];
                        nominal_r[j] = nominal_r[j] + delta;
                        getObservedRadarDelayDoppler(radar_doppler_receiver_latitude[i], radar_doppler_receiver_longitude[i], radar_doppler_receiver_altitude[i], receiver_r, receiver_rprime, radar_doppler_receiver_time[i], radar_doppler_transmitter_latitude[i], radar_doppler_transmitter_longitude[i], radar_doppler_transmitter_altitude[i], EOP, radar_doppler_transmitter_tdb_minus_utc[i], radar_doppler_receiver_tdb_minus_utc[i], radar_doppler_transmitter_frequency[i], minor_planet_radius, nominal_r, nominal_rprime, incremental_state_vector_epoch[i], delay_doppler);
                        doppler = delay_doppler[2];
                        sensitivity_doppler[j] = (doppler - nominal_observable[index + 1]) / delta;
                        nominal_r[j] = nominal_r[j] - delta;
                        delta = 0.000001 * nominal_rprime[j];
                        nominal_rprime[j] = nominal_rprime[j] + delta;
                        getObservedRadarDelayDoppler(radar_doppler_receiver_latitude[i], radar_doppler_receiver_longitude[i], radar_doppler_receiver_altitude[i], receiver_r, receiver_rprime, radar_doppler_receiver_time[i], radar_doppler_transmitter_latitude[i], radar_doppler_transmitter_longitude[i], radar_doppler_transmitter_altitude[i], EOP, radar_doppler_transmitter_tdb_minus_utc[i], radar_doppler_receiver_tdb_minus_utc[i], radar_doppler_transmitter_frequency[i], minor_planet_radius, nominal_r, nominal_rprime, incremental_state_vector_epoch[i], delay_doppler);
                        doppler = delay_doppler[2];
                        sensitivity_doppler[j + 3] = (doppler - nominal_observable[index + 1]) / delta;
                        nominal_rprime[j] = nominal_rprime[j] - delta;
                        if (j < 3) {
                            delta = 0.1 * A1_A2_DT[j];
                            A1_A2_DT[j] = A1_A2_DT[j] + delta;
                            getObservedRadarDelayDoppler(radar_doppler_receiver_latitude[i], radar_doppler_receiver_longitude[i], radar_doppler_receiver_altitude[i], receiver_r, receiver_rprime, radar_doppler_receiver_time[i], radar_doppler_transmitter_latitude[i], radar_doppler_transmitter_longitude[i], radar_doppler_transmitter_altitude[i], EOP, radar_doppler_transmitter_tdb_minus_utc[i], radar_doppler_receiver_tdb_minus_utc[i], radar_doppler_transmitter_frequency[i], minor_planet_radius, nominal_r, nominal_rprime, incremental_state_vector_epoch[i], delay_doppler);
                            doppler = delay_doppler[2];
                            sensitivity_doppler[j + 6] = (doppler - nominal_observable[index + 1]) / delta;
                            A1_A2_DT[j] = A1_A2_DT[j] - delta;
                        }
                    }

                    /* Calculate the residual and chi; if observations meet the applicable thresholds, calculate the resulting row of matrix_a */
                    observation_residual = Math.abs(residuals[index + 1]);

                    if (doppler_dev[i] != 0.0)
                        observation_chi = Math.abs(residuals[index + 1] / doppler_dev[i]);
                    else
                        observation_chi = 0.0;
                    if (((edit) && (chi_testing) && (observation_chi < chi_threshold)) || ((edit) && (residual_testing) && (Math.abs(observation_residual) < doppler_residual_threshold)) || (!edit)) {

                        /* Observation is not excluded; reset flag */
                        excluded_observations[i] = 0;

                        for (j = 1; j <= 8; j++) {
                            row[j] = 0.0;
                            for (k = 1; k <= 8; k++)
                                row[j] = row[j] + sensitivity_doppler[k] * phi[k][j];
                            matrix_a[retained_index + 1][j] = row[j];
                        }

                        /* Fill the dev array with standard deviations */
                        dev[retained_index + 1] = doppler_dev[i];

                        /* Add this residual to the list of those being retained */
                        retained_residuals[retained_index + 1] = residuals[index + 1];

                        /* Increment the retained observation counter */
                        retained_index = retained_index + 1;

                    }

                    /* Increment the index */
                    index = index + 1;

                }


            }

            /* At this point, matrix A is complete.  We proceed to convert to a unity covariance form */
            /* Should be commented out. */
            for (i = 1; i <= retained_index; i++) {
                for (j = 1; j <= 8; j++)
                    matrix_temp[i][j] = matrix_a[i][j] / dev[i];
                retained_residuals[i] = retained_residuals[i] / dev[i];
            }
            for (i = 1; i <= retained_index; i++) {
                for (j = 1; j <= 8; j++)
                    matrix_a[i][j] = matrix_temp[i][j];
            }

            /* Construct an orthogonal transform T using Givens rotations, and left multiply.  matrix_a will now be upper triangular; isolate the upper 8x8 as matrix_R, and isolate the top 8x1 of T x residuals as matrix_z. */
            givensQr(retained_index, 8, matrix_a, matrix_T);

            for (i = 1; i <= 8; i++) {
                matrix_z[i] = 0;
                for (j = 1; j <= retained_index; j++)
                    matrix_z[i] = matrix_z[i] + matrix_T[i][j] * retained_residuals[j];
            }

            for (i = 1; i <= 8; i++) {
                for (j = 1; j <= 8; j++)
                    matrix_R[i][j] = matrix_a[i][j];
            }

            /* Calculate the state vector corrections and covariances. */
            invertNxn(8, matrix_R);

            for (i = 1; i <= 8; i++) {
                corrections[i] = 0;
                for (k = 1; k <= 8; k++)
                    /* Square Root Information Filtering assumes that we are in unit covariance form.  Since we're often unable to invert matrix_R in that form, we must make the conversion to unit covariance form now, so the results are valid.  In this particular calculation, we would multiply matrix_R[i][k] by dev[k] (since matrix_a has been inverted), and we would divide matrix_z by dev[k]; since these precisely cancel, no action is really needed here. */
                    corrections[i] = corrections[i] + matrix_R[i][k] * matrix_z[k];
            }

            for (i = 1; i <= 8; i++) {
                for (j = 1; j <= 8; j++) {
                    big_p[i][j] = 0;
                    for (k = 1; k <= 8; k++)
                        /* Square Root Information Filtering assumes that we are in unit covariance form.  Since we're often unable to invert matrix_R in that form, we must make the conversion to unit covariance form now, so the results are valid.  In this particular calculation, we would multiply matrix_R[i][k] by dev[k] (since matrix_a has been inverted), and we would multiply matrix_R[j][k] by dev[k] (for the same reason).  */
/*						big_p[i][j] = big_p[i][j] + matrix_R[i][k]*matrix_R[j][k]*(dev[k]*dev[k]);   */
                        big_p[i][j] = big_p[i][j] + matrix_R[i][k] * matrix_R[j][k];
                }
            }

            /* Update the epoch state vector */
            for (i = 1; i <= 3; i++) {
                epoch_r[i] = epoch_r[i] + corrections[i];
                System.out.println("epoch_r[i] = " + epoch_r[i]);
                epoch_rprime[i] = epoch_rprime[i] + corrections[i + 3];
                System.out.println("epoch_rprime[i] = " + epoch_rprime[i]);
                if (i < 3)
                    A1_A2_DT[i] = A1_A2_DT[i] + corrections[i + 6];
                System.out.println("A1_A2_DT[i] = " + A1_A2_DT[i]);
            }

            /* Recalculate the residuals using the updated epoch state vector */
            getResiduals(residuals, rms_residuals);
            old_rms_optical = rms_optical;
            old_rms_delay = rms_delay;
            old_rms_doppler = rms_doppler;
            rms_optical = rms_residuals[1];
            rms_delay = rms_residuals[2];
            rms_doppler = rms_residuals[3];

            /* Based on the RMS residuals, evaluate the convergence and divergence condition */
            convergence = true;
            if (Math.abs(old_rms_optical - rms_optical) > (convergence_factor * old_rms_optical))
                convergence = false;
            if (Math.abs(old_rms_delay - rms_delay) > (convergence_factor * old_rms_delay))
                convergence = false;
            if (Math.abs(old_rms_doppler - rms_doppler) > (convergence_factor * old_rms_doppler))
                convergence = false;
            if ((rms_optical >= 1.2 * old_rms_optical) && (rms_delay >= 1.2 * old_rms_delay) && (rms_doppler >= 1.2 * old_rms_doppler))
                divergence = true;
            if ((convergence) && (!edit) && ((chi_testing) || (residual_testing))) {
                edit = true;
                convergence = false;
            }
            /* We may not have yet converged.  However, if the residuals have been reduced, shouldn't we keep this state vector and covariance matrix as the initial data in case the next iteration diverges? */
            if ((rms_optical <= old_rms_optical) && (rms_delay <= old_rms_delay) && (rms_doppler <= old_rms_doppler)) {
                for (i = 1; i <= 3; i++) {
                    initial_r[i] = epoch_r[i];
                    initial_rprime[i] = epoch_rprime[i];
                    initial_rms_residuals[i] = rms_residuals[i];
                }
                initial_A1_A2_DT[1] = A1_A2_DT[1];
                initial_A1_A2_DT[2] = A1_A2_DT[2];
                for (i = 1; i <= 8; i++) {
                    for (j = 1; j <= 8; j++)
                        initial_big_p[i][j] = big_p[i][j];
                }
                index = 2 * number_of_optical_observations + number_of_delay_observations + number_of_doppler_observations;
                for (i = 1; i <= index; i++)
                    initial_residuals[i] = residuals[i];
            }

            /* The loop is complete.  If the convergence or divergence condition is satisfied, we will exit; if not, we will go around again */

        }

        if (divergence == true) {
            /* Return initial orbit and residuals, set non-gravitational thrust parameters to zero, and set epoch_r[0] = 6.0. */
            for (i = 1; i <= 3; i++) {
                epoch_r[i] = initial_r[i];
                epoch_rprime[i] = initial_rprime[i];
                rms_residuals[i] = initial_rms_residuals[i];
            }
            A1_A2_DT[1] = initial_A1_A2_DT[1];
            A1_A2_DT[2] = initial_A1_A2_DT[2];
            epoch_r[0] = 6.0;
            for (i = 1; i <= 8; i++) {
                for (j = 1; j <= 8; j++)
                    big_p[i][j] = initial_big_p[i][j];
            }
            index = 2 * number_of_optical_observations + number_of_delay_observations + number_of_doppler_observations;
            for (i = 1; i <= index; i++) {
                residuals[i] = initial_residuals[i];
            }
        } else if (divergence == false)
            epoch_r[0] = 0.0;

    }

    public void removeMinorPlanetFromList(String removee) {

        /*
          Procedure to remove a given minor planet (removee) from the list of minor planets for whom data files exist; the corresponding data file is then erased.
          */

        int i = 0;

        boolean flag = false;

        for (i = 1; i <= (number_of_minor_planets - 1); i++) {
            if ((mp_list[i] == removee) || (flag == true)) {
                flag = true;
                mp_list[i] = mp_list[i + 1];
            }
        }

        number_of_minor_planets = number_of_minor_planets - 1;

        try {

            /* Create File object doomed, named by string removee, and delete it */
            File doomed = new File(removee);

            boolean deleted = doomed.delete();

        } catch (SecurityException se) {
            System.out.println("Error -- " + se.toString());
        }

    }

    public void addMinorPlanetToList(String addee) {

        /*
          Procedure to add a new minor planet (addee) to the list of minor planets for whom data files exist.  The new minor planet is added in such a way as to preserve the alphabetical order of the list.
          */

        int i = 0, j = 0, flag = 0;

        for (i = 1; i <= number_of_minor_planets; i++) {
            if ((addee.compareTo(mp_list[i]) < 0) && (flag == 0)) {
                for (j = number_of_minor_planets; j >= i; j--)
                    mp_list[j + 1] = mp_list[j];
                mp_list[i] = addee;
                flag = 1;
            }
        }

        if (flag == 0)
            mp_list[number_of_minor_planets + 1] = addee;

        number_of_minor_planets = number_of_minor_planets + 1;

    }


    public void deleteAnObservation(int obs_n) {
        /* Method to remove an observation from the data file of the minor planet */

        int i = 0, index = number_of_optical_observations + number_of_delay_observations + number_of_doppler_observations, j = 0;

        if (observation_type[obs_n] == 1)
            number_of_optical_observations = number_of_optical_observations - 1;
        else if (observation_type[obs_n] == 2)
            number_of_delay_observations = number_of_delay_observations - 1;
        else if (observation_type[obs_n] == 3)
            number_of_doppler_observations = number_of_doppler_observations - 1;

        for (i = obs_n; i <= (index - 1); i++) {
            observation_type[i] = observation_type[i + 1];
            incremental_state_vector_epoch[i] = incremental_state_vector_epoch[i + 1];
            for (j = 1; j <= 3; j++) {
                incremental_state_vector_r[i][j] = incremental_state_vector_r[i + 1][j];
                incremental_state_vector_rprime[i][j] = incremental_state_vector_rprime[i + 1][j];
            }
            optical_ra[i] = optical_ra[i + 1];
            ra_dev[i] = ra_dev[i + 1];
            optical_dec[i] = optical_dec[i + 1];
            dec_dev[i] = dec_dev[i + 1];
            optical_time[i] = optical_time[i + 1];
            visual_magnitude[i] = visual_magnitude[i + 1];
            visual_magnitude_dev[i] = visual_magnitude_dev[i + 1];
            observation_geocentric[i] = observation_geocentric[i + 1];
            observation_latitude[i] = observation_latitude[i + 1];
            observation_longitude[i] = observation_longitude[i + 1];
            observation_altitude[i] = observation_altitude[i + 1];
            for (j = 1; j <= 7; j++)
                observation_EOP[i][j] = observation_EOP[i + 1][j];
            for (j = 1; j <= 3; j++) {
                observation_r[i][j] = observation_r[i + 1][j];
                observation_rprime[i][j] = observation_rprime[i + 1][j];
            }
            radar_delay[i] = radar_delay[i + 1];
            delay_dev[i] = delay_dev[i + 1];
            radar_delay_receiver_time[i] = radar_delay_receiver_time[i + 1];
            radar_delay_receiver_latitude[i] = radar_delay_receiver_latitude[i + 1];
            radar_delay_receiver_longitude[i] = radar_delay_receiver_longitude[i + 1];
            radar_delay_receiver_altitude[i] = radar_delay_receiver_altitude[i + 1];
            for (j = 1; j <= 7; j++)
                radar_delay_receiver_EOP[i][j] = radar_delay_receiver_EOP[i + 1][j];
            radar_delay_receiver_tdb_minus_utc[i] = radar_delay_receiver_tdb_minus_utc[i + 1];
            for (j = 1; j <= 3; j++) {
                radar_delay_receiver_r[i][j] = radar_delay_receiver_r[i + 1][j];
                radar_delay_receiver_rprime[i][j] = radar_delay_receiver_rprime[i + 1][j];
            }
            radar_delay_transmitter_latitude[i] = radar_delay_transmitter_latitude[i + 1];
            radar_delay_transmitter_longitude[i] = radar_delay_transmitter_longitude[i + 1];
            radar_delay_transmitter_altitude[i] = radar_delay_transmitter_altitude[i + 1];
            for (j = 1; j <= 7; j++)
                radar_delay_transmitter_EOP[i][j] = radar_delay_transmitter_EOP[i + 1][j];
            radar_delay_transmitter_tdb_minus_utc[i] = radar_delay_transmitter_tdb_minus_utc[i + 1];
            radar_delay_transmitter_frequency[i] = radar_delay_transmitter_frequency[i + 1];
            radar_doppler[i] = radar_doppler[i + 1];
            doppler_dev[i] = doppler_dev[i + 1];
            radar_doppler_receiver_time[i] = radar_doppler_receiver_time[i + 1];
            radar_doppler_receiver_latitude[i] = radar_doppler_receiver_latitude[i + 1];
            radar_doppler_receiver_longitude[i] = radar_doppler_receiver_longitude[i + 1];
            radar_doppler_receiver_altitude[i] = radar_doppler_receiver_altitude[i + 1];
            for (j = 1; j <= 7; j++)
                radar_doppler_receiver_EOP[i][j] = radar_doppler_receiver_EOP[i + 1][j];
            radar_doppler_receiver_tdb_minus_utc[i] = radar_doppler_receiver_tdb_minus_utc[i + 1];
            for (j = 1; j <= 3; j++) {
                radar_doppler_receiver_r[i][j] = radar_doppler_receiver_r[i + 1][j];
                radar_doppler_receiver_rprime[i][j] = radar_doppler_receiver_rprime[i + 1][j];
            }
            radar_doppler_transmitter_latitude[i] = radar_doppler_transmitter_latitude[i + 1];
            radar_doppler_transmitter_longitude[i] = radar_doppler_transmitter_longitude[i + 1];
            radar_doppler_transmitter_altitude[i] = radar_doppler_transmitter_altitude[i + 1];
            for (j = 1; j <= 7; j++)
                radar_doppler_transmitter_EOP[i][j] = radar_doppler_transmitter_EOP[i + 1][j];
            radar_doppler_transmitter_tdb_minus_utc[i] = radar_doppler_transmitter_tdb_minus_utc[i + 1];
            radar_doppler_transmitter_frequency[i] = radar_doppler_transmitter_frequency[i + 1];
        }

    }


    double apparentMagnitude(double jultime, double pos[]) {
        /* Method to predict the apparent magnitude of an asteroid at TDB jultime, given its barycentric equatorial position at that time.  Method uses the instance variables absolute_magnitude_H and slope_parameter_G.
Algorithm taken from Jean Meeus, Astronomical Algorithms, pp. 216-217.  Algorithm is modified in accordance with E. Bowell, "The IAU Two-Parameter Magnitude System for Asteroids", in Gehrels, "Asteroids 2", 1989, pp. 549-554.  */

        double little_r = 0, big_r = 0, delta = 0, phi1 = 0, phi2 = 0, beta = 0, magnitude = 0, phi1l = 0, phi2l = 0, phi1s = 0, phi2s = 0, w = 0;
        double[] earth_r = new double[4];
        double[] earth_rprime = new double[4];
        double[] sun_r = new double[4];
        double[] sun_rprime = new double[4];

        /* If the slope_parameter_G and absolute_magnitude_H are both zero, return '-99.0'.  Similarly, apparent magnitude estimate is valid only for slope_parameter_G between zero and one; if G is outside of that range, return '-99.0'. */
        if (((slope_parameter_G == 0.0) && (absolute_magnitude_H == 0.0)) || (slope_parameter_G < 0.0) || (slope_parameter_G > 1.0))
            magnitude = -99.0;
        else {
            /* Proceed with the algorithm. */
            /* Get the positions of the Earth and Sun at TDB jultime.  Note that I am not accounting for light time-of-flight here.  Given the approximate nature of the model and the limited precision of its parameters, I firmly believe that accounting for light time-of-flight would be a waste of CPU cycles, and would introduce an unnecessary delay into overall processing.  */
            getPlanetPosVel(jultime, 3, earth_r, earth_rprime);
            getPlanetPosVel(jultime, 11, sun_r, sun_rprime);

            /* Calculate the distances */
            big_r = Math.sqrt(Math.pow((earth_r[1] - sun_r[1]), 2) + Math.pow((earth_r[2] - sun_r[2]), 2) + Math.pow((earth_r[3] - sun_r[3]), 2));
            little_r = Math.sqrt(Math.pow((pos[1] - sun_r[1]), 2) + Math.pow((pos[2] - sun_r[2]), 2) + Math.pow((pos[3] - sun_r[3]), 2));
            delta = Math.sqrt(Math.pow((pos[1] - earth_r[1]), 2) + Math.pow((pos[2] - earth_r[2]), 2) + Math.pow((pos[3] - earth_r[3]), 2));

            /* Calculate the phase angle of the minor planet */
            beta = Math.acos((Math.pow(little_r, 2) + Math.pow(delta, 2) - Math.pow(big_r, 2)) / (2.0 * little_r * delta));

            /* The apparent magnitude estimate is valid only for phase angles between zero and 120 degrees.  If we are outside of that range, return '-99.0'. */
            if (beta > 120.0 * Math.PI / 180.0)
                magnitude = -99.0;
            else {
                /* Calculate the phis */
                /* Note that I am using Bowell's formulation here, rather than the IAU Commission 20 (1985) form, due to Bowell's contention that his is more accurate.  */
                phi1l = Math.exp(-3.332 * Math.pow(Math.tan(beta / 2.0), 0.631));
                phi2l = Math.exp(-1.862 * Math.pow(Math.tan(beta / 2.0), 1.218));
                phi1s = 1.0 - 0.986 * Math.sin(beta) / (0.119 + 1.341 * Math.sin(beta) - 0.754 * Math.pow(Math.sin(beta), 2.0));
                phi2s = 1.0 - 0.238 * Math.sin(beta) / (0.119 + 1.341 * Math.sin(beta) - 0.754 * Math.pow(Math.sin(beta), 2.0));
                w = Math.exp(-90.56 * Math.pow(Math.tan(beta / 2.0), 2.0));
                phi1 = w * phi1s + (1.0 - w) * phi1l;
                phi2 = w * phi2s + (1.0 - w) * phi2l;
                /* Calculate the apparent magnitude.  */
                magnitude = absolute_magnitude_H + 5.0 * Math.log(little_r * delta) / Math.log(10.0) - 2.5 * Math.log((1.0 - slope_parameter_G) * phi1 + slope_parameter_G * phi2) / Math.log(10.0);
            }
        }

        return magnitude;

    }


    public void solveForAbsoluteMagnitude() {
        /* Method to solve for the absolute magnitude H and slope parameter G of an asteroid, using visual-range brightness measurements.  Once these parameters are known, the future visual-range brightness of the asteroid can be predicted as part of an ephemeris.  Note that this method will run after "get_residuals"; thus, incremental_state_vector_r will be available.
The instance variables slope_parameter_G and absolute_magnitude_H are directly updated by this method.  The method also solves for estimates of asteroid radius; the instance variable minor_planet_radius is directly updated by the method. */

        /* One issue must be addressed.  Not every optical observation will include a visual-range brightness measurement.  A minimum of one measurement is needed to calculate the G and H parameters.  So I believe we are faced with three cases.  Either we have zero visual-range brightness measurements, or we have exactly one visual-range brightness measurement, or we have two or more visual-range brightness measurements.  In the first case, G and H are indeterminate.  In the second case, G and H are precisely determined.  In the third case, G and H are overdetermined, and we must use least-squares. */

        /* Note that this has been a very challenging algorithm to create.  The biggest problem is that real-world magnitude measurements are extremely imprecise.  Moreover, the sensitivity matrix is ill-conditioned; the partial wrt H is exactly one, while the partial wrt G is approx. -1.  As a result, least-squares does not converge well.  For this reason, I tried a brute-force alternative approach; for each value of G (in increments of 0.01), calculate the range of H's, then run through each H in the range (in increments of 0.1) and determine which combination gave the lowest rms residuals.  This was simply too computationally intensive. */

        /* The algorithm I'm now using is due to Dr. Ted Bowell at Lowell Observatory, who helped develop the G,H system in the mid-80's.  Reference is Ed. Bowell, "The IAU Two-Parameter Magnitude System for Asteroids" in Gehrels "Asteroids II", 1989, pp. 549-555.*/

        int number_of_brightness_observations = 0, index = number_of_optical_observations + number_of_delay_observations + number_of_doppler_observations;

        int[] pointer = new int[8001];

        double[] phi = new double[3];
        double[] earth_r = new double[4];
        double[] earth_rprime = new double[4];
        double[] sun_r = new double[4];
        double[] sun_rprime = new double[4];
        double[] reduced_magnitude = new double[8001];
        double[] epsilon = new double[8001];
        double[] big_I = new double[8001];
        double[] phi1 = new double[8001];
        double[] phi2 = new double[8001];
        double[] delta_m = new double[8001];

        double A = 0, B = 0, big_r = 0, beta = 0, r = 0, delta = 0, phi1l = 0, phi2l = 0, phi1s = 0, phi2s = 0, w = 0, h11 = 0, h12 = 0, h22 = 0, g1 = 0, g2 = 0, big_D = 0, a1 = 0, a2 = 0, top_sum = 0, bottom_sum = 0, albedo = 0, diameter = 0;

        int i = 0, j = 0, k = 0;

        /* Survey the observations, and determine the number of visual-range brightness measurements, with phase angle between 0 and 120 degrees.  */
        for (i = 1; i <= index; i++) {
            if ((observation_type[i] == 1) && (visual_magnitude[i] != -99.0)) {
                /* Calculate the phase angle */
                /* Get the positions of the Earth and Sun at incremental_state_vector_epoch[i].  Note that I am not accounting for light time-of-flight here.  Given the approximate nature of the model and the limited precision of its parameters, I firmly believe that accounting for light time-of-flight would be a waste of CPU cycles, and would introduce an unnecessary delay into overall processing.  */
                getPlanetPosVel(incremental_state_vector_epoch[i], 3, earth_r, earth_rprime);
                getPlanetPosVel(incremental_state_vector_epoch[i], 11, sun_r, sun_rprime);
                /* Calculate the distances */
                big_r = Math.sqrt(Math.pow((earth_r[1] - sun_r[1]), 2) + Math.pow((earth_r[2] - sun_r[2]), 2) + Math.pow((earth_r[3] - sun_r[3]), 2));
                r = Math.sqrt(Math.pow((incremental_state_vector_r[i][1] - sun_r[1]), 2) + Math.pow((incremental_state_vector_r[i][2] - sun_r[2]), 2) + Math.pow((incremental_state_vector_r[i][3] - sun_r[3]), 2));
                delta = Math.sqrt(Math.pow((incremental_state_vector_r[i][1] - earth_r[1]), 2) + Math.pow((incremental_state_vector_r[i][2] - earth_r[2]), 2) + Math.pow((incremental_state_vector_r[i][3] - earth_r[3]), 2));

                /* Calculate the phase angle of the asteroid */
                beta = Math.acos((Math.pow(r, 2) + Math.pow(delta, 2) - Math.pow(big_r, 2)) / (2.0 * r * delta));

                if (beta <= 120.0 * Math.PI / 180.0) {
                    /* Valid visual magnitude observation.  Compile quantities for least-squares */
                    number_of_brightness_observations = number_of_brightness_observations + 1;
                    pointer[number_of_brightness_observations] = i;
                    reduced_magnitude[number_of_brightness_observations] = visual_magnitude[i] - 5.0 * Math.log(r * delta) / Math.log(10.0);
                    epsilon[number_of_brightness_observations] = visual_magnitude_dev[i];
                    big_I[number_of_brightness_observations] = Math.pow(10.0, (-0.4 * reduced_magnitude[number_of_brightness_observations]));
                    /* Calculate the phis */
                    /* Note that I am using Bowell's formulation here, rather than the IAU Commission 20 (1985) form, due to Bowell's contention that his is more accurate.  */
                    phi1l = Math.exp(-3.332 * Math.pow(Math.tan(beta / 2.0), 0.631));
                    phi2l = Math.exp(-1.862 * Math.pow(Math.tan(beta / 2.0), 1.218));
                    phi1s = 1.0 - 0.986 * Math.sin(beta) / (0.119 + 1.341 * Math.sin(beta) - 0.754 * Math.pow(Math.sin(beta), 2.0));
                    phi2s = 1.0 - 0.238 * Math.sin(beta) / (0.119 + 1.341 * Math.sin(beta) - 0.754 * Math.pow(Math.sin(beta), 2.0));
                    w = Math.exp(-90.56 * Math.pow(Math.tan(beta / 2.0), 2.0));
                    phi1[number_of_brightness_observations] = w * phi1s + (1.0 - w) * phi1l;
                    phi2[number_of_brightness_observations] = w * phi2s + (1.0 - w) * phi2l;
                }
            }
        }
        System.out.println("number_of_brightness_observations = " + number_of_brightness_observations);

        /* Based on the number_of_brightness_observations, handle each case separately.  */
        /* Case 1: number_of_brightness_observations = 0 */
        if (number_of_brightness_observations == 0) {
            /* G and H are indeterminate; return zeros. */
            slope_parameter_G = 0;
            absolute_magnitude_H = 0;
        }

        /* Case 2: number_of_brightness_observations = 1 */
        else if (number_of_brightness_observations == 1) {
            /* G and H are precisely determined */
            /* An earlier version of this algorithm used two observations to solve for G and H.  This did not work well in practice, due to the imprecision of magnitude measurements; divide-by-zero errors were quite common.  Therefore, I am now assuming that G = 0.15 (the mid-range value for typical asteroids), and solving for H.  In fact, this appears to be the strategy employed by Marsden, judging from his MPECs.  */
            slope_parameter_G = 0.15;
            absolute_magnitude_H = reduced_magnitude[1] + 2.5 * Math.log((1.0 - slope_parameter_G) * phi1[1] + slope_parameter_G * phi2[1]) / Math.log(10.0);
            System.out.println("slope_parameter_G = " + slope_parameter_G);
            System.out.println("absolute_magnitude_H = " + absolute_magnitude_H);
        }

        /* Case 3: number_of_brightness_observations > 1. */
        else if (number_of_brightness_observations > 1) {
            /* G and H are over-determined.  Use least-squares. */
            for (i = 1; i <= number_of_brightness_observations; i++) {
                h11 = h11 + phi1[i] * phi1[i] / Math.pow(epsilon[i] * big_I[i], 2.0);
                h12 = h12 + phi1[i] * phi2[i] / Math.pow(epsilon[i] * big_I[i], 2.0);
                h22 = h22 + phi2[i] * phi2[i] / Math.pow(epsilon[i] * big_I[i], 2.0);
                /* Possible error in Bowell's paper */
                /* g1 = g1 + phi1[i]/Math.pow(epsilon[i]*big_I[i],2.0);
                       g2 = g2 + phi2[i]/Math.pow(epsilon[i]*big_I[i],2.0);  */
                g1 = g1 + phi1[i] / (Math.pow(epsilon[i], 2.0) * big_I[i]);
                g2 = g2 + phi2[i] / (Math.pow(epsilon[i], 2.0) * big_I[i]);
            }
            big_D = h11 * h22 - Math.pow(h12, 2.0);
            a1 = (h22 * g1 - h12 * g2) / big_D;
            a2 = (h11 * g2 - h12 * g1) / big_D;
            slope_parameter_G = a2 / (a1 + a2);
            absolute_magnitude_H = -2.5 * Math.log(a1 + a2) / Math.log(10.0);
        }
        System.out.println("slope_parameter_G = " + slope_parameter_G);
        System.out.println("absolute_magnitude_H = " + absolute_magnitude_H);

        /* According to Bowell, G should fall between zero and 0.5.  If the solution lies outside that range, we will adopt Marsden's practice of assuming G = 0.15, and calculating H. */
        if ((slope_parameter_G < 0.0) || (slope_parameter_G > 0.5)) {
            slope_parameter_G = 0.15;
            /* Solve for H as a function of G */
            for (i = 1; i <= number_of_brightness_observations; i++) {
                delta_m[i] = -2.5 * Math.log((1.0 - slope_parameter_G) * phi1[i] + slope_parameter_G * phi2[i]) / Math.log(10.0);
                top_sum = top_sum + (reduced_magnitude[i] - delta_m[i]) / Math.pow(epsilon[i], 2.0);
                bottom_sum = bottom_sum + 1.0 / Math.pow(epsilon[i], 2.0);
            }
            absolute_magnitude_H = top_sum / bottom_sum;
        }

        /* Knowing G and H, we can now assume an albedo (based on G), and estimate the asteroid's radius.  The albedo will be modeled as a linear function of G.  From Tedesco et al., "A Three Parameter Asteroid Taxonomy", The Astronomical Journal, Vol. 97, number 2, pp. 580-607, we have
              C-class albedo = 0.029-0.075  mean 0.048   (G = 0.09 due to Bowell)
              S-class albedo = 0.100-0.300  mean 0.186   (G = 0.23 due to Bowell)
              M-class albedo = 0.080-0.200  mean 0.141   (G = 0.21 due to Bowell)
          */
        if (number_of_brightness_observations > 0) {
            albedo = 0.048 + (slope_parameter_G - 0.09);
            if (albedo < 0.029) albedo = 0.029;
            if (albedo > 0.3) albedo = 0.3;
            diameter = Math.pow(10.0, ((6.259 - Math.log(albedo) / Math.log(10.0) - 0.4 * absolute_magnitude_H) / 2.0));
            minor_planet_radius = (diameter / 2.0) / au;
        }

    }

    public void solveForCometAbsoluteMagnitude() {
        /* Method to solve for the absolute magnitude and slope parameter of a comet, using visual-range brightness measurements.  Once these parameters are known, the future visual-range brightness of the comet can be predicted as part of an ephemeris.  Note that this method will run after "get_residuals"; thus, incremental_state_vector_r will be available.
The instance variables comet_slope_parameter and comet_absolute_magnitude are directly updated by this method. */

        /* One issue must be addressed.  Not every optical observation will include a visual-range brightness measurement.  A minimum of two measurement is needed to calculate the absolute magnitude and slope parameters.  So I believe we are faced with two cases.  Either we have less than two visual-range brightness measurements, or we have two or more visual-range brightness measurement.  In the first case, the parameters are indeterminate.  In the second case, the parameters are either precisely or over-determined, and we will use least-squares. */

        /* Algorithm based on Jean Meeus, Astronomical Algorithms, pp. 216-217.  Uses least-squares formulation from Burden and Faires, "Numerical Analysis" , 3rd edition (1985), p. 364. */

        int number_of_brightness_observations = 0, index = number_of_optical_observations + number_of_delay_observations + number_of_doppler_observations;

        int[] pointer = new int[8001];

        double[] earth_r = new double[4];
        double[] earth_rprime = new double[4];
        double[] sun_r = new double[4];
        double[] sun_rprime = new double[4];

        double r = 0, delta = 0, sumxy = 0, sumx = 0, sumy = 0, sumx2 = 0;

        int i = 0, j = 0, k = 0;

        /* Survey the observations, and determine the number of visual-range brightness measurements */
        for (i = 1; i <= index; i++) {
            if ((observation_type[i] == 1) && (visual_magnitude[i] != -99.0)) {
                /* Valid visual magnitude observation.  Compile quantities for least-squares */
                number_of_brightness_observations = number_of_brightness_observations + 1;
                pointer[number_of_brightness_observations] = i;

                /* Get the positions of the Earth and Sun at incremental_state_vector_epoch[i].  Note that I am not accounting for light time-of-flight here.  Given the approximate nature of the model and the limited precision of its parameters, I firmly believe that accounting for light time-of-flight would be a waste of CPU cycles, and would introduce an unnecessary delay into overall processing.  */
                getPlanetPosVel(incremental_state_vector_epoch[i], 3, earth_r, earth_rprime);
                getPlanetPosVel(incremental_state_vector_epoch[i], 11, sun_r, sun_rprime);
                /* Calculate the distances */
                r = Math.sqrt(Math.pow((incremental_state_vector_r[i][1] - sun_r[1]), 2) + Math.pow((incremental_state_vector_r[i][2] - sun_r[2]), 2) + Math.pow((incremental_state_vector_r[i][3] - sun_r[3]), 2));
                delta = Math.sqrt(Math.pow((incremental_state_vector_r[i][1] - earth_r[1]), 2) + Math.pow((incremental_state_vector_r[i][2] - earth_r[2]), 2) + Math.pow((incremental_state_vector_r[i][3] - earth_r[3]), 2));

                /* Calculate the least-squares quantities */
                sumxy = sumxy + (Math.log(r) / Math.log(10.0)) * (visual_magnitude[i] - 5.0 * Math.log(delta) / Math.log(10.0));
                sumx = sumx + Math.log(r) / Math.log(10.0);
                sumy = sumy + (visual_magnitude[i] - 5.0 * Math.log(delta) / Math.log(10.0));
                sumx2 = sumx2 + Math.pow(Math.log(r) / Math.log(10.0), 2.0);

            }
        }
        System.out.println("number_of_brightness_observations = " + number_of_brightness_observations);

        /* Based on the number_of_brightness_observations, handle each case separately.  */
        /* Case 1: number_of_brightness_observations < 2 */
        if (number_of_brightness_observations < 2) {
            /* parameters are indeterminate; return zeros. */
            comet_slope_parameter = 0;
            comet_absolute_magnitude = 0;
        }

        /* Case 2: number_of_brightness_observations >= 2 */
        else if (number_of_brightness_observations >= 2) {
            comet_slope_parameter = (number_of_brightness_observations * sumxy - sumx * sumy) / (number_of_brightness_observations * sumx2 - Math.pow(sumx, 2.0));
            comet_absolute_magnitude = (sumx2 * sumy - sumxy * sumx) / (number_of_brightness_observations * sumx2 - Math.pow(sumx, 2.0));
            System.out.println("comet_slope_parameter = " + comet_slope_parameter);
            System.out.println("comet_absolute_magnitude = " + comet_absolute_magnitude);
        }
        System.out.println("comet_slope_parameter = " + comet_slope_parameter);
        System.out.println("comet_absolute_magnitude = " + comet_absolute_magnitude);

        /* According to Marsden and Meeus, comet_slope_parameter should fall between zero and twenty.  If the solution lies outside that range, we will assume comet_slope_parameter = 4.0 (appears to be a Marsden default), and calculate comet_absolute_magnitude from the "b normal equation". */
        if ((comet_slope_parameter < 0.0) || (comet_slope_parameter > 20.0)) {
            comet_slope_parameter = 4.0;
            /* Solve for comet_absolute_magnitude as a function of comet_slope_parameter */
            comet_absolute_magnitude = (sumy - comet_slope_parameter * sumx) / number_of_brightness_observations;
        }

    }


    double cometApparentMagnitude(double jultime, double pos[]) {
        /* Method to predict the apparent magnitude of a comet at TDB jultime, given its barycentric equatorial position at that time.  Method uses the instance variables comet_absolute_magnitude and comet_slope_parameter.
Algorithm taken from Jean Meeus, Astronomical Algorithms, pp. 216-217. */

        double little_r = 0, delta = 0, magnitude = 0;
        double[] earth_r = new double[4];
        double[] earth_rprime = new double[4];
        double[] sun_r = new double[4];
        double[] sun_rprime = new double[4];

        /* If the comet_slope_parameter and comet_absolute_magnitude are both zero, return '-99.0'.  Similarly, apparent magnitude estimate is valid only for comet_slope_parameter between zero and twenty; if comet_slope_parameter is outside of that range, return '-99.0'. */
        if (((comet_slope_parameter == 0.0) && (comet_absolute_magnitude == 0.0)) || (comet_slope_parameter < 0.0) || (comet_slope_parameter > 20.0))
            magnitude = -99.0;
        else {
            /* Proceed with the algorithm. */
            /* Get the positions of the Earth and Sun at TDB jultime.  Note that I am not accounting for light time-of-flight here.  Given the approximate nature of the model and the limited precision of its parameters, I firmly believe that accounting for light time-of-flight would be a waste of CPU cycles, and would introduce an unnecessary delay into overall processing.  */
            getPlanetPosVel(jultime, 3, earth_r, earth_rprime);
            getPlanetPosVel(jultime, 11, sun_r, sun_rprime);

            /* Calculate the distances */
            little_r = Math.sqrt(Math.pow((pos[1] - sun_r[1]), 2) + Math.pow((pos[2] - sun_r[2]), 2) + Math.pow((pos[3] - sun_r[3]), 2));
            delta = Math.sqrt(Math.pow((pos[1] - earth_r[1]), 2) + Math.pow((pos[2] - earth_r[2]), 2) + Math.pow((pos[3] - earth_r[3]), 2));

            /* Calculate the apparent magnitude.  */
            magnitude = comet_absolute_magnitude + 5.0 * Math.log(delta) / Math.log(10.0) + comet_slope_parameter * Math.log(little_r) / Math.log(10.0);
        }

        return magnitude;

    }

    void givensQr(int m, int n, double[][] matrix_a, double[][] matrix_QT) {
        /* Method to determine the matrix_QT (dimension m x m) that, when left-multiplied against matrix_a (dimension m x n), produces an upper triangular matrix.  matrix_QT is initialized as the identity; as it is passed through repeated calls to givens_row_rot, matrix_QT is accumulated, one Givens rotation at a time.  Note that matrix_a is returned as upper triangular.
Reference is Gene H. Golub and Charles F. van Loan, "Matrix Computations", 2nd edition (1989) pp. 214-215.  */

        int i = 0, j = 0;

        double[] cs = new double[3];

        /* Initialize matrix_QT to the identity */
        for (i = 1; i <= m; i++) {
            for (j = 1; j <= m; j++) {
                matrix_QT[i][j] = 0.0;
                if (i == j)
                    matrix_QT[i][j] = 1.0;
            }
        }

        /* Execute the algorithm */
        for (j = 1; j <= n; j++) {
            for (i = m; i >= j + 1; i--) {
                /* Calculate the required rotation */
                givens(matrix_a[i - 1][j], matrix_a[i][j], cs);
                /* Rotate matrix_a and matrix_QT */
                givensRowRotation(matrix_a, matrix_QT, j, n, m, cs[1], cs[2], i - 1, i);
            }
        }
    }


    void givensRowRotation(double[][] matrix_A, double[][] matrix_Q, int p, int q, int r, double c, double s, int i, int k) {
        /* Method to implement a Givens row rotation.  The input matrix_A and matrix_Q are overwritten by the rotated output.  The only affected rows of matrix_A are i and k, from columns p to q; the only affected rows of matrix_Q are i and k, from columns 1 to r.  The matrix_Q is the cumulative Givens matrix (see method givens_qr).
Reference is Gene H. Golub and Charles F. van Loan, "Matrix Computations", 2nd edition (1989) pp. 202-203.  */

        int j = 0;

        double tau1 = 0, tau2 = 0;

        /* Execute the rotation. */
        for (j = p; j <= q; j++) {
            tau1 = matrix_A[i][j];
            tau2 = matrix_A[k][j];
            matrix_A[i][j] = c * tau1 - s * tau2;
            if (Math.abs(matrix_A[i][j]) < 2.0E-15) matrix_A[i][j] = 0.0;
            matrix_A[k][j] = s * tau1 + c * tau2;
            if (Math.abs(matrix_A[k][j]) < 2.0E-15) matrix_A[k][j] = 0.0;
        }
        for (j = 1; j <= r; j++) {
            tau1 = matrix_Q[i][j];
            tau2 = matrix_Q[k][j];
            matrix_Q[i][j] = c * tau1 - s * tau2;
            if (Math.abs(matrix_Q[i][j]) < 2.0E-15) matrix_Q[i][j] = 0.0;
            matrix_Q[k][j] = s * tau1 + c * tau2;
            if (Math.abs(matrix_Q[k][j]) < 2.0E-15) matrix_Q[k][j] = 0.0;
        }


    }

    void givens(double a, double b, double[] cs) {
        /* Method to calculate the Givens rotation factors, given scalars a and b; the objective will be to set b to zero.
Reference is Gene H. Golub and Charles F. van Loan, "Matrix Computations", 2nd edition (1989) pp. 202.  */

        double tau = 0;

        if (b == 0.0) {
            cs[1] = 1;
            cs[2] = 0;
        } else if (Math.abs(b) > Math.abs(a)) {
            tau = -a / b;
            cs[2] = 1.0 / Math.sqrt(1.0 + tau * tau);
            cs[1] = cs[2] * tau;
        } else {
            tau = -b / a;
            cs[1] = 1.0 / Math.sqrt(1.0 + tau * tau);
            cs[2] = cs[1] * tau;
        }
    }

    public void asteroidSlopeBasedAbsMag() {
        /* Method to solve for the absolute magnitude H and radius of an asteroid, given an assumed slope_parameter_G, using visual-range brightness measurements.
             NOTE that this method assumes that incremental_state_vector_r will be available; if it is not, the button permitting use of this method should not appear.  (The rationale is that this method requires all available optical observations.  If incremental_state_vector_r is available, this implies that least squares would have been run, meaning that all of the observations are available.  On the other hand, if incremental_state_vector_r is NOT available, this implies that least squares must not have been run, meaning that all of the observations may not be available.)
             The instance variables absolute_magnitude_H and minor_planet_radius are directly updated by this method. */

        /* The algorithm I'm now using is due to Dr. Ted Bowell at Lowell Observatory, who helped develop the G,H system in the mid-80's.  Reference is Ed. Bowell, "The IAU Two-Parameter Magnitude System for Asteroids" in Gehrels "Asteroids II", 1989, pp. 549-555.*/

        int number_of_brightness_observations = 0, index = number_of_optical_observations + number_of_delay_observations + number_of_doppler_observations;

        int[] pointer = new int[8001];

        double[] phi = new double[3];
        double[] earth_r = new double[4];
        double[] earth_rprime = new double[4];
        double[] sun_r = new double[4];
        double[] sun_rprime = new double[4];
        double[] reduced_magnitude = new double[8001];
        double[] epsilon = new double[8001];
        double[] phi1 = new double[8001];
        double[] phi2 = new double[8001];
        double[] delta_m = new double[8001];

        double big_r = 0, beta = 0, r = 0, delta = 0, phi1l = 0, phi2l = 0, phi1s = 0, phi2s = 0, w = 0, top_sum = 0, bottom_sum = 0, albedo = 0, diameter = 0;

        int i = 0, j = 0, k = 0;

        /* Survey the observations, and determine the number of visual-range brightness measurements, with phase angle between 0 and 120 degrees.  */
        for (i = 1; i <= index; i++) {
            if ((observation_type[i] == 1) && (visual_magnitude[i] != -99.0)) {
                /* Calculate the phase angle */
                /* Get the positions of the Earth and Sun at incremental_state_vector_epoch[i].  Note that I am not accounting for light time-of-flight here.  Given the approximate nature of the model and the limited precision of its parameters, I firmly believe that accounting for light time-of-flight would be a waste of CPU cycles, and would introduce an unnecessary delay into overall processing.  */
                getPlanetPosVel(incremental_state_vector_epoch[i], 3, earth_r, earth_rprime);
                getPlanetPosVel(incremental_state_vector_epoch[i], 11, sun_r, sun_rprime);
                /* Calculate the distances */
                big_r = Math.sqrt(Math.pow((earth_r[1] - sun_r[1]), 2) + Math.pow((earth_r[2] - sun_r[2]), 2) + Math.pow((earth_r[3] - sun_r[3]), 2));
                r = Math.sqrt(Math.pow((incremental_state_vector_r[i][1] - sun_r[1]), 2) + Math.pow((incremental_state_vector_r[i][2] - sun_r[2]), 2) + Math.pow((incremental_state_vector_r[i][3] - sun_r[3]), 2));
                delta = Math.sqrt(Math.pow((incremental_state_vector_r[i][1] - earth_r[1]), 2) + Math.pow((incremental_state_vector_r[i][2] - earth_r[2]), 2) + Math.pow((incremental_state_vector_r[i][3] - earth_r[3]), 2));

                /* Calculate the phase angle of the asteroid */
                beta = Math.acos((Math.pow(r, 2) + Math.pow(delta, 2) - Math.pow(big_r, 2)) / (2.0 * r * delta));

                if (beta <= 120.0 * Math.PI / 180.0) {
                    /* Valid visual magnitude observation.  Compile quantities for later use */
                    number_of_brightness_observations = number_of_brightness_observations + 1;
                    pointer[number_of_brightness_observations] = i;
                    reduced_magnitude[number_of_brightness_observations] = visual_magnitude[i] - 5.0 * Math.log(r * delta) / Math.log(10.0);
                    epsilon[number_of_brightness_observations] = visual_magnitude_dev[i];
                    /* Calculate the phis */
                    /* Note that I am using Bowell's formulation here, rather than the IAU Commission 20 (1985) form, due to Bowell's contention that his is more accurate.  */
                    phi1l = Math.exp(-3.332 * Math.pow(Math.tan(beta / 2.0), 0.631));
                    phi2l = Math.exp(-1.862 * Math.pow(Math.tan(beta / 2.0), 1.218));
                    phi1s = 1.0 - 0.986 * Math.sin(beta) / (0.119 + 1.341 * Math.sin(beta) - 0.754 * Math.pow(Math.sin(beta), 2.0));
                    phi2s = 1.0 - 0.238 * Math.sin(beta) / (0.119 + 1.341 * Math.sin(beta) - 0.754 * Math.pow(Math.sin(beta), 2.0));
                    w = Math.exp(-90.56 * Math.pow(Math.tan(beta / 2.0), 2.0));
                    phi1[number_of_brightness_observations] = w * phi1s + (1.0 - w) * phi1l;
                    phi2[number_of_brightness_observations] = w * phi2s + (1.0 - w) * phi2l;
                }
            }
        }
        System.out.println("number_of_brightness_observations = " + number_of_brightness_observations);

        /* Solve for H as a function of G */
        for (i = 1; i <= number_of_brightness_observations; i++) {
            delta_m[i] = -2.5 * Math.log((1.0 - slope_parameter_G) * phi1[i] + slope_parameter_G * phi2[i]) / Math.log(10.0);
            top_sum = top_sum + (reduced_magnitude[i] - delta_m[i]) / Math.pow(epsilon[i], 2.0);
            bottom_sum = bottom_sum + 1.0 / Math.pow(epsilon[i], 2.0);
        }
        absolute_magnitude_H = top_sum / bottom_sum;

        /* Knowing G and H, we can now assume an albedo (based on G), and estimate the asteroid's radius.  The albedo will be modeled as a linear function of G.  From Tedesco et al., "A Three Parameter Asteroid Taxonomy", The Astronomical Journal, Vol. 97, number 2, pp. 580-607, we have
              C-class albedo = 0.029-0.075  mean 0.048   (G = 0.09 due to Bowell)
              S-class albedo = 0.100-0.300  mean 0.186   (G = 0.23 due to Bowell)
              M-class albedo = 0.080-0.200  mean 0.141   (G = 0.21 due to Bowell)
          */
        albedo = 0.048 + (slope_parameter_G - 0.09);
        diameter = Math.pow(10.0, ((6.259 - Math.log(albedo) / Math.log(10.0) - 0.4 * absolute_magnitude_H) / 2.0));
        minor_planet_radius = (diameter / 2.0) / au;


    }

    public void cometSlopeBasedAbsMag() {
        /* Given the comet_slope_parameter, method to solve for the comet_absolute_magnitude, using visual-range brightness measurements.
             NOTE that this method assumes that incremental_state_vector_r will be available; if it is not, the button permitting use of this method should not appear.  (The rationale is that this method requires all available optical observations.  If incremental_state_vector_r is available, this implies that least squares would have been run, meaning that all of the observations are available.  On the other hand, if incremental_state_vector_r is NOT available, this implies that least squares must not have been run, meaning that all of the observations may not be available.)
                The instance variables comet_slope_parameter and comet_absolute_magnitude are directly updated by this method. */

        /* Algorithm based on Jean Meeus, Astronomical Algorithms, pp. 216-217.  Uses least-squares formulation from Burden and Faires, "Numerical Analysis" , 3rd edition (1985), p. 364. */

        int number_of_brightness_observations = 0, index = number_of_optical_observations + number_of_delay_observations + number_of_doppler_observations;

        int[] pointer = new int[8001];

        double[] earth_r = new double[4];
        double[] earth_rprime = new double[4];
        double[] sun_r = new double[4];
        double[] sun_rprime = new double[4];

        double r = 0, delta = 0, sumxy = 0, sumx = 0, sumy = 0, sumx2 = 0;

        int i = 0, j = 0, k = 0;

        /* Survey the observations, and determine the number of visual-range brightness measurements */
        for (i = 1; i <= index; i++) {
            if ((observation_type[i] == 1) && (visual_magnitude[i] != -99.0)) {
                /* Valid visual magnitude observation.  Compile quantities for least-squares */
                number_of_brightness_observations = number_of_brightness_observations + 1;
                pointer[number_of_brightness_observations] = i;

                /* Get the positions of the Earth and Sun at incremental_state_vector_epoch[i].  Note that I am not accounting for light time-of-flight here.  Given the approximate nature of the model and the limited precision of its parameters, I firmly believe that accounting for light time-of-flight would be a waste of CPU cycles, and would introduce an unnecessary delay into overall processing.  */
                getPlanetPosVel(incremental_state_vector_epoch[i], 3, earth_r, earth_rprime);
                getPlanetPosVel(incremental_state_vector_epoch[i], 11, sun_r, sun_rprime);
                /* Calculate the distances */
                r = Math.sqrt(Math.pow((incremental_state_vector_r[i][1] - sun_r[1]), 2) + Math.pow((incremental_state_vector_r[i][2] - sun_r[2]), 2) + Math.pow((incremental_state_vector_r[i][3] - sun_r[3]), 2));
                delta = Math.sqrt(Math.pow((incremental_state_vector_r[i][1] - earth_r[1]), 2) + Math.pow((incremental_state_vector_r[i][2] - earth_r[2]), 2) + Math.pow((incremental_state_vector_r[i][3] - earth_r[3]), 2));

                /* Calculate the least-squares quantities */
                sumxy = sumxy + (Math.log(r) / Math.log(10.0)) * (visual_magnitude[i] - 5.0 * Math.log(delta) / Math.log(10.0));
                sumx = sumx + Math.log(r) / Math.log(10.0);
                sumy = sumy + (visual_magnitude[i] - 5.0 * Math.log(delta) / Math.log(10.0));
                sumx2 = sumx2 + Math.pow(Math.log(r) / Math.log(10.0), 2.0);

            }
        }
        System.out.println("number_of_brightness_observations = " + number_of_brightness_observations);

        /* Solve for comet_absolute_magnitude as a function of comet_slope_parameter */
        comet_absolute_magnitude = (sumy - comet_slope_parameter * sumx) / number_of_brightness_observations;

    }

    public void getStateVectorCovariance(double classical_elements[], double classical_elements_covariance[][]) {
        /* Method to convert the classical orbital elements at epoch_time into a state vector, and to convert the covariances in the orbital elements at epoch_time to state vector covariances.  Note: As output, this method directly sets the epoch_r and epoch_rprime state vector, as well as the big_p covariances.  */

        int i = 0, j = 0, k = 0;

        double delta = 0.0;

        double[] r = new double[4];
        double[] rprime = new double[4];
        double[][] partial_matrix = new double[7][9];
        double[][] inter = new double[9][7];

        /* Now, knowing the classical elements, and their uncertainties, we use variant calculations to determine the state vector and covariances.  In the case of a parabolic orbit, a and M are undefined; thus, trying to determine the variant sensitivity due to e results in a call to convert_Keplerian_elements_to_state_vector with a hyperbolic orbit (e=1+delta) but a and M undefined.  The basic problem here is that a parabolic orbit is a discontinuous singular case for the classical elements; for all intents and purposes, the derived posvel covariances associated with a parabolic orbit are undefined.  Thus, I have decided the best course would be to set them to zero.  If the user believes he/she knows what the posvel covariances should be, they are free to specify them directly. */

        getMinorPlanetStateVector(classical_elements, epoch_time, epoch_r, epoch_rprime);
        /* Reset any divergence flag */
        epoch_r[0] = 0.0;

        if (classical_elements[2] != 1.0) {
            for (i = 1; i <= 8; i++) {
                if (i == 6)
                    /* Since the time of perihelion is a 7-digit Julian date, delta must be correspondingly small */
                    delta = 0.0000001 * classical_elements[i];
                else
                    delta = 0.001 * classical_elements[i];
                /* Prevent division by zero. */
                if (Math.abs(delta - 0.0) < 1e-15) delta = 0.0000001;
                classical_elements[i] = classical_elements[i] + delta;
                getMinorPlanetStateVector(classical_elements, epoch_time, r, rprime);
                for (j = 1; j <= 3; j++) {
                    partial_matrix[j][i] = (r[j] - epoch_r[j]) / delta;
                    partial_matrix[j + 3][i] = (rprime[j] - epoch_rprime[j]) / delta;
                }
                classical_elements[i] = classical_elements[i] - delta;
            }
        }

        /* Convert the covariances from classical element into pos/vel */
        if (classical_elements[2] == 1.0) {
            for (i = 1; i <= 6; i++) {
                for (j = 1; j <= 6; j++)
                    big_p[i][j] = 0;
            }
        } else {
            for (i = 1; i <= 8; i++) {
                for (j = 1; j <= 6; j++) {
                    inter[i][j] = 0;
                    for (k = 1; k <= 8; k++)
                        inter[i][j] = inter[i][j] + classical_elements_covariance[i][k] * partial_matrix[j][k];
                }
            }
            for (i = 1; i <= 6; i++) {
                for (j = 1; j <= 6; j++) {
                    big_p[i][j] = 0;
                    for (k = 1; k <= 8; k++)
                        big_p[i][j] = big_p[i][j] + partial_matrix[i][k] * inter[k][j];
                }
            }
        }


    }

    double normalProbabilityDensity(double x, double mean, double sigma) {

        /* Method to calculate the normal probability density function at x with mean and sigma as given.  The value of the pdf will be returned. */

        double exponent = 0, prob = 0;

        exponent = -Math.pow((x - mean), 2.0) / (2.0 * Math.pow(sigma, 2.0));
        prob = Math.exp(exponent) / (sigma * Math.sqrt(2.0 * Math.PI));

        return prob;

    }

    public void getMatrixEigenvalues(int dimension_n, double matrix_nxn[][], double eigenvalues[]) {

        /*
          Procedure to diagonalize a dimension_n-by-dimension_n matrix (matrix_nxn[][]) using Gaussian elimination.
          Note: In this particular implementation, I've dimensioned an intermediate matrix so that n is restricted to n<=8; this is not an intrinsic limitation of the algorithm, and a larger dimensionalization can handle larger values of n.
          Algorithm taken from Richard L. Burden and J. Douglas Faires, "Numerical Analysis", 3rd edition, p. 300.
          NOTE:  It is assumed that the matrix is diagonalizable!!

        */

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
            eigenvalues[k] = inter[k][k];
        }

    }


    public double getMOID() {
        /* Procedure to estimate the minimum orbital intersection distance between a minor planet and Earth. */
        /* The algorithm is due to Sitarski, Acta Astronica, 1968 (Vol 18 pp. 171-181) */

        double[][] solution = new double[20][3];
        double[][] big_h = new double[4][4];
        double[][] little_h = new double[4][4];
        double[][] inter1 = new double[4][4];
        double[][] inter2 = new double[4][4];
        double[][] inter3 = new double[4][4];
        double[] big_u = new double[4];
        double[] little_u = new double[4];
        double[] big_pos = new double[4];
        double[] little_pos = new double[4];
        double[] rho = new double[4];
        double[] sun_r = new double[4];
        double[] sun_rprime = new double[4];
        double[] mp_r_2body = new double[4];
        double[] mp_rprime_2body = new double[4];
        double[] earth_r_2body = new double[4];
        double[] earth_rprime_2body = new double[4];
        double[] earth_r = new double[4];
        double[] earth_rprime = new double[4];
        double[] little_asteroid_elements = new double[9];
        double[] big_asteroid_elements = new double[9];

        int i = 0, j = 0, big_v_counter = 0, little_v_counter = 0, solution_counter = 0, k = 0;

        double moid = 1000.0, big_v = 0, little_v = 0, big_r = 0, big_p = 0, little_p = 0, big_x = 0, big_y = 0, big_m = 0, big_n = 0, big_px = 0, big_py = 0, big_pz = 0, big_qx = 0, big_qy = 0, big_qz = 0, little_px = 0, little_py = 0, little_pz = 0, little_qx = 0, little_qy = 0, little_qz = 0, big_k = 0, big_l = 0, s = 0, t = 0, w = 0, little_m = 0, little_l = 0, solution_little_v1 = 0, solution_little_v2 = 0, old_equation_value_1 = 0, old_equation_value_2 = 0, equation_value_1 = 0, equation_value_2 = 0, little_x = 0, little_y = 0, little_r = 0, old_big_v = 0, top = 0, bottom = 0, middle = 0, bisection_equation_value_1 = 0, bisection_equation_value_2 = 0, distance = 0, longitude1 = 0, top_sign = 0, partial_big_v = 0, partial_little_v = 0, mixed_partial = 0;

        /* Calculate the osculating orbital elements of Earth and the minor planet at epoch */
        /* Begin by finding the barycentric equatorial state vectors of the Sun and Earth */
        planetaryEphemeris(epoch_time, rho, 0);
        for (i = 1; i <= 3; i++) {
            sun_r[i] = planet_r[0][i];
            sun_rprime[i] = planet_rprime[0][i];
            earth_r[i] = planet_r[3][i];
            earth_rprime[i] = planet_rprime[3][i];
        }

        /* Convert the minor planet and Earth state vectors to heliocentric equatorial */
        for (i = 1; i <= 3; i++) {
            mp_r_2body[i] = epoch_r[i] - sun_r[i];
            mp_rprime_2body[i] = (epoch_rprime[i] - sun_rprime[i]) / kappa;
            earth_r_2body[i] = earth_r[i] - sun_r[i];
            earth_rprime_2body[i] = (earth_rprime[i] - sun_rprime[i]) / kappa;
        }

        /* Rotate the heliocentric state vectors into the ecliptic frame */
        TimeUtils.convertEquatorialToEcliptic(mp_r_2body);
        TimeUtils.convertEquatorialToEcliptic(mp_rprime_2body);
        TimeUtils.convertEquatorialToEcliptic(earth_r_2body);
        TimeUtils.convertEquatorialToEcliptic(earth_rprime_2body);

        /* Find the classical orbital elements */
        convertStateVectorToKeplerianElements(mp_r_2body, mp_rprime_2body, little_asteroid_elements, epoch_time);
        convertStateVectorToKeplerianElements(earth_r_2body, earth_rprime_2body, big_asteroid_elements, epoch_time);

        /* Create the quantities needed to evaluate the key equation 11 */
        big_p = big_asteroid_elements[8] * (1.0 + big_asteroid_elements[2]);
        little_p = little_asteroid_elements[8] * (1.0 + little_asteroid_elements[2]);
        big_px = Math.cos(big_asteroid_elements[4]);
        big_py = Math.sin(big_asteroid_elements[4]) * Math.cos(big_asteroid_elements[3]);
        big_pz = Math.sin(big_asteroid_elements[4]) * Math.sin(big_asteroid_elements[3]);
        big_qx = -Math.sin(big_asteroid_elements[4]);
        big_qy = Math.cos(big_asteroid_elements[4]) * Math.cos(big_asteroid_elements[3]);
        big_qz = Math.cos(big_asteroid_elements[4]) * Math.sin(big_asteroid_elements[3]);
        little_px = Math.cos(little_asteroid_elements[4]) * Math.cos(little_asteroid_elements[5] - big_asteroid_elements[5]) - Math.sin(little_asteroid_elements[4]) * Math.cos(little_asteroid_elements[3]) * Math.sin(little_asteroid_elements[5] - big_asteroid_elements[5]);
        little_py = Math.cos(little_asteroid_elements[4]) * Math.sin(little_asteroid_elements[5] - big_asteroid_elements[5]) + Math.sin(little_asteroid_elements[4]) * Math.cos(little_asteroid_elements[3]) * Math.cos(little_asteroid_elements[5] - big_asteroid_elements[5]);
        little_pz = Math.sin(little_asteroid_elements[4]) * Math.sin(little_asteroid_elements[3]);
        little_qx = -Math.sin(little_asteroid_elements[4]) * Math.cos(little_asteroid_elements[5] - big_asteroid_elements[5]) - Math.cos(little_asteroid_elements[4]) * Math.cos(little_asteroid_elements[3]) * Math.sin(little_asteroid_elements[5] - big_asteroid_elements[5]);
        little_qy = -Math.sin(little_asteroid_elements[4]) * Math.sin(little_asteroid_elements[5] - big_asteroid_elements[5]) + Math.cos(little_asteroid_elements[4]) * Math.cos(little_asteroid_elements[3]) * Math.cos(little_asteroid_elements[5] - big_asteroid_elements[5]);
        little_qz = Math.cos(little_asteroid_elements[4]) * Math.sin(little_asteroid_elements[3]);
        big_k = big_px * little_px + big_py * little_py + big_pz * little_pz;
        big_l = big_qx * little_px + big_qy * little_py + big_qz * little_pz;
        big_m = big_px * little_qx + big_py * little_qy + big_pz * little_qz;
        big_n = big_qx * little_qx + big_qy * little_qy + big_qz * little_qz;
        /* Calculate cracovians big_h and little_h for use in evaluating the distances */
        inter1[1][1] = Math.cos(little_asteroid_elements[5] - big_asteroid_elements[5]);
        inter1[1][2] = Math.sin(little_asteroid_elements[5] - big_asteroid_elements[5]);
        inter1[1][3] = 0.0;
        inter1[2][1] = -Math.sin(little_asteroid_elements[5] - big_asteroid_elements[5]);
        inter1[2][2] = Math.cos(little_asteroid_elements[5] - big_asteroid_elements[5]);
        inter1[2][3] = 0.0;
        inter1[3][1] = 0.0;
        inter1[3][2] = 0.0;
        inter1[3][3] = 1.0;
        inter2[1][1] = 1.0;
        inter2[1][2] = 0.0;
        inter2[1][3] = 0.0;
        inter2[2][1] = 0.0;
        inter2[2][2] = Math.cos(little_asteroid_elements[3]);
        inter2[2][3] = -Math.sin(little_asteroid_elements[3]);
        inter2[3][1] = 0.0;
        inter2[3][2] = Math.sin(little_asteroid_elements[3]);
        inter2[3][3] = Math.cos(little_asteroid_elements[3]);
        /* Multiply the cracovians, using cracovian logic, rather than matrix multiplication */
        for (i = 1; i <= 3; i++) {
            for (j = 1; j <= 3; j++) {
                inter3[i][j] = 0.0;
                for (k = 1; k <= 3; k++)
                    inter3[i][j] = inter3[i][j] + inter1[k][j] * inter2[k][i];
            }
        }
        inter1[1][1] = Math.cos(little_asteroid_elements[4]);
        inter1[1][2] = -Math.sin(little_asteroid_elements[4]);
        inter1[1][3] = 0.0;
        inter1[2][1] = Math.sin(little_asteroid_elements[4]);
        inter1[2][2] = Math.cos(little_asteroid_elements[4]);
        inter1[2][3] = 0.0;
        inter1[3][1] = 0.0;
        inter1[3][2] = 0.0;
        inter1[3][3] = 1.0;
        /* Multiply the cracovians, using cracovian logic, rather than matrix multiplication */
        for (i = 1; i <= 3; i++) {
            for (j = 1; j <= 3; j++) {
                little_h[i][j] = 0.0;
                for (k = 1; k <= 3; k++)
                    little_h[i][j] = little_h[i][j] + inter3[k][j] * inter1[k][i];
            }
        }
        inter2[1][1] = 1.0;
        inter2[1][2] = 0.0;
        inter2[1][3] = 0.0;
        inter2[2][1] = 0.0;
        inter2[2][2] = Math.cos(big_asteroid_elements[3]);
        inter2[2][3] = Math.sin(big_asteroid_elements[3]);
        inter2[3][1] = 0.0;
        inter2[3][2] = -Math.sin(big_asteroid_elements[3]);
        inter2[3][3] = Math.cos(big_asteroid_elements[3]);
        inter1[1][1] = Math.cos(big_asteroid_elements[4]);
        inter1[1][2] = -Math.sin(big_asteroid_elements[4]);
        inter1[1][3] = 0.0;
        inter1[2][1] = Math.sin(big_asteroid_elements[4]);
        inter1[2][2] = Math.cos(big_asteroid_elements[4]);
        inter1[2][3] = 0.0;
        inter1[3][1] = 0.0;
        inter1[3][2] = 0.0;
        inter1[3][3] = 1.0;
        /* Multiply the cracovians, using cracovian logic, rather than matrix multiplication */
        for (i = 1; i <= 3; i++) {
            for (j = 1; j <= 3; j++) {
                big_h[i][j] = 0.0;
                for (k = 1; k <= 3; k++)
                    big_h[i][j] = big_h[i][j] + inter2[k][j] * inter1[k][i];
            }
        }

        big_v = -181 * Math.PI / 180.0;
        for (big_v_counter = -180; big_v_counter <= 180; big_v_counter++) {
            old_big_v = big_v;
            big_v = big_v_counter * Math.PI / 180.0;
            big_r = big_p / (1.0 + big_asteroid_elements[2] * Math.cos(big_v));
            big_x = big_r * Math.cos(big_v);
            big_y = big_r * Math.sin(big_v);
            s = big_asteroid_elements[2] * big_r * big_y / little_p;
            t = big_m * big_y - big_n * (big_asteroid_elements[2] * big_r + big_x);
            w = little_asteroid_elements[2] * s + big_k * big_y - big_l * (big_asteroid_elements[2] * big_r + big_x);
            little_m = t * t + w * w;
            little_l = little_m - s * s;
            solution_little_v1 = Math.atan2((-t * s + w * Math.sqrt(little_l)) / little_m, (-w * s - t * Math.sqrt(little_l)) / little_m);
            little_r = little_p / (1.0 + little_asteroid_elements[2] * Math.cos(solution_little_v1));
            little_x = little_r * Math.cos(solution_little_v1);
            little_y = little_r * Math.sin(solution_little_v1);
            equation_value_1 = (little_r / little_p) * (little_asteroid_elements[2] * little_r * little_y + little_y * (big_k * big_x + big_l * big_y) - (little_asteroid_elements[2] * little_r + little_x) * (big_m * big_x + big_n * big_y));
            solution_little_v2 = Math.atan2((-t * s - w * Math.sqrt(little_l)) / little_m, (-w * s + t * Math.sqrt(little_l)) / little_m);
            little_r = little_p / (1.0 + little_asteroid_elements[2] * Math.cos(solution_little_v2));
            little_x = little_r * Math.cos(solution_little_v2);
            little_y = little_r * Math.sin(solution_little_v2);
            equation_value_2 = (little_r / little_p) * (little_asteroid_elements[2] * little_r * little_y + little_y * (big_k * big_x + big_l * big_y) - (little_asteroid_elements[2] * little_r + little_x) * (big_m * big_x + big_n * big_y));
            if ((big_v_counter > -180) && (((old_equation_value_1 <= 0.0) && (equation_value_1 >= 0.0)) || ((old_equation_value_1 >= 0.0) && (equation_value_1 <= 0.0)))) {
                top = big_v;
                bottom = old_big_v;
                if (equation_value_1 > 0)
                    top_sign = 1.0;
                else
                    top_sign = -1.0;
                while (Math.abs(top - bottom) > 1.0e-7) {
                    middle = (top + bottom) / 2.0;
                    big_r = big_p / (1.0 + big_asteroid_elements[2] * Math.cos(middle));
                    big_x = big_r * Math.cos(middle);
                    big_y = big_r * Math.sin(middle);
                    s = big_asteroid_elements[2] * big_r * big_y / little_p;
                    t = big_m * big_y - big_n * (big_asteroid_elements[2] * big_r + big_x);
                    w = little_asteroid_elements[2] * s + big_k * big_y - big_l * (big_asteroid_elements[2] * big_r + big_x);
                    little_m = t * t + w * w;
                    little_l = little_m - s * s;
                    solution_little_v1 = Math.atan2((-t * s + w * Math.sqrt(little_l)) / little_m, (-w * s - t * Math.sqrt(little_l)) / little_m);
                    little_r = little_p / (1.0 + little_asteroid_elements[2] * Math.cos(solution_little_v1));
                    little_x = little_r * Math.cos(solution_little_v1);
                    little_y = little_r * Math.sin(solution_little_v1);
                    bisection_equation_value_1 = (little_r / little_p) * (little_asteroid_elements[2] * little_r * little_y + little_y * (big_k * big_x + big_l * big_y) - (little_asteroid_elements[2] * little_r + little_x) * (big_m * big_x + big_n * big_y));
                    if ((bisection_equation_value_1 >= 0) && (top_sign > 0))
                        top = middle;
                    else if ((bisection_equation_value_1 <= 0) && (top_sign > 0))
                        bottom = middle;
                    else if ((bisection_equation_value_1 >= 0) && (top_sign < 0))
                        bottom = middle;
                    else if ((bisection_equation_value_1 <= 0) && (top_sign < 0))
                        top = middle;
                }
                solution_counter++;
                solution[solution_counter][1] = middle;
                solution[solution_counter][2] = solution_little_v1;
                partial_big_v = (big_r / big_p) * ((big_asteroid_elements[2] * big_r * big_r / big_p) * (big_asteroid_elements[2] * big_r + big_x) + big_x * (big_k * little_x + big_m * little_y) + big_y * (big_l * little_x + big_n * little_y));
                partial_little_v = (little_r / little_p) * ((little_asteroid_elements[2] * little_r * little_r / little_p) * (little_asteroid_elements[2] * little_r + little_x) + little_x * (big_k * big_x + big_l * big_y) + little_y * (big_m * big_x + big_n * big_y));
                mixed_partial = (big_r / big_p) * (little_r / little_p) * ((little_asteroid_elements[2] * little_r + little_x) * (big_n * (big_asteroid_elements[2] * big_r + big_x) - big_m * big_y) - little_y * (big_l * (big_asteroid_elements[2] * big_r + big_x) - big_k * big_y));
            }
            if ((big_v_counter > -180) && (((old_equation_value_2 <= 0.0) && (equation_value_2 >= 0.0)) || ((old_equation_value_2 >= 0.0) && (equation_value_2 <= 0.0)))) {
                top = big_v;
                bottom = old_big_v;
                if (equation_value_2 > 0)
                    top_sign = 1.0;
                else
                    top_sign = -1.0;
                while (Math.abs(top - bottom) > 1.0e-7) {
                    middle = (top + bottom) / 2.0;
                    big_r = big_p / (1.0 + big_asteroid_elements[2] * Math.cos(middle));
                    big_x = big_r * Math.cos(middle);
                    big_y = big_r * Math.sin(middle);
                    s = big_asteroid_elements[2] * big_r * big_y / little_p;
                    t = big_m * big_y - big_n * (big_asteroid_elements[2] * big_r + big_x);
                    w = little_asteroid_elements[2] * s + big_k * big_y - big_l * (big_asteroid_elements[2] * big_r + big_x);
                    little_m = t * t + w * w;
                    little_l = little_m - s * s;
                    solution_little_v2 = Math.atan2((-t * s - w * Math.sqrt(little_l)) / little_m, (-w * s + t * Math.sqrt(little_l)) / little_m);
                    little_r = little_p / (1.0 + little_asteroid_elements[2] * Math.cos(solution_little_v2));
                    little_x = little_r * Math.cos(solution_little_v2);
                    little_y = little_r * Math.sin(solution_little_v2);
                    bisection_equation_value_2 = (little_r / little_p) * (little_asteroid_elements[2] * little_r * little_y + little_y * (big_k * big_x + big_l * big_y) - (little_asteroid_elements[2] * little_r + little_x) * (big_m * big_x + big_n * big_y));
                    if ((bisection_equation_value_2 >= 0) && (top_sign > 0))
                        top = middle;
                    else if ((bisection_equation_value_2 <= 0) && (top_sign > 0))
                        bottom = middle;
                    else if ((bisection_equation_value_2 >= 0) && (top_sign < 0))
                        bottom = middle;
                    else if ((bisection_equation_value_2 <= 0) && (top_sign < 0))
                        top = middle;
                }
                solution_counter++;
                solution[solution_counter][1] = middle;
                solution[solution_counter][2] = solution_little_v2;
                partial_big_v = (big_r / big_p) * ((big_asteroid_elements[2] * big_r * big_r / big_p) * (big_asteroid_elements[2] * big_r + big_x) + big_x * (big_k * little_x + big_m * little_y) + big_y * (big_l * little_x + big_n * little_y));
                partial_little_v = (little_r / little_p) * ((little_asteroid_elements[2] * little_r * little_r / little_p) * (little_asteroid_elements[2] * little_r + little_x) + little_x * (big_k * big_x + big_l * big_y) + little_y * (big_m * big_x + big_n * big_y));
                mixed_partial = (big_r / big_p) * (little_r / little_p) * ((little_asteroid_elements[2] * little_r + little_x) * (big_n * (big_asteroid_elements[2] * big_r + big_x) - big_m * big_y) - little_y * (big_l * (big_asteroid_elements[2] * big_r + big_x) - big_k * big_y));
            }
            old_equation_value_1 = equation_value_1;
            old_equation_value_2 = equation_value_2;
        }

        /* We now have several distance minima; determine which is smallest by direct calculation. */
        for (i = 1; i <= solution_counter; i++) {
            big_r = big_p / (1.0 + big_asteroid_elements[2] * Math.cos(solution[i][1]));
            little_r = little_p / (1.0 + little_asteroid_elements[2] * Math.cos(solution[i][2]));
            big_u[1] = big_r * Math.cos(solution[i][1]);
            big_u[2] = big_r * Math.sin(solution[i][1]);
            big_u[3] = 0.0;
            little_u[1] = little_r * Math.cos(solution[i][2]);
            little_u[2] = little_r * Math.sin(solution[i][2]);
            little_u[3] = 0.0;
            big_pos[1] = big_u[1] * big_h[1][1] + big_u[2] * big_h[2][1] + big_u[3] * big_h[3][1];
            big_pos[2] = big_u[1] * big_h[1][2] + big_u[2] * big_h[2][2] + big_u[3] * big_h[3][2];
            big_pos[3] = big_u[1] * big_h[1][3] + big_u[2] * big_h[2][3] + big_u[3] * big_h[3][3];
            little_pos[1] = little_u[1] * little_h[1][1] + little_u[2] * little_h[2][1] + little_u[3] * little_h[3][1];
            little_pos[2] = little_u[1] * little_h[1][2] + little_u[2] * little_h[2][2] + little_u[3] * little_h[3][2];
            little_pos[3] = little_u[1] * little_h[1][3] + little_u[2] * little_h[2][3] + little_u[3] * little_h[3][3];
            distance = Math.sqrt(Math.pow((big_pos[1] - little_pos[1]), 2.0) + Math.pow((big_pos[2] - little_pos[2]), 2.0) + Math.pow((big_pos[3] - little_pos[3]), 2.0));
            if (distance < moid) moid = distance;
        }

        return moid;

    }

    public void barycentricToTwoBodyHeliocentric(double jultime, double r[], double rprime[]) {
        /* Procedure to convert the barycentric state vector of a minor planet to heliocentric at jultime. */

        double[] rho = new double[4];
        double[] sun_r = new double[4];
        double[] sun_rprime = new double[4];

        int i = 0;

        /* Begin by finding the barycentric equatorial state vector of the Sun */
        planetaryEphemeris(jultime, rho, 0);
        for (i = 1; i <= 3; i++) {
            sun_r[i] = planet_r[0][i];
            sun_rprime[i] = planet_rprime[0][i];
        }

        /* Convert the minor planet state vector to heliocentric equatorial for use in two-body routines */
        for (i = 1; i <= 3; i++) {
            r[i] = r[i] - sun_r[i];
            rprime[i] = (rprime[i] - sun_rprime[i]) / kappa;
        }


    }

    public void twoBodyHeliocentricToBarycentric(double jultime, double r[], double rprime[]) {
        /* Procedure to convert the heliocentric state vector of a minor planet to barycentric at jultime. */

        double[] rho = new double[4];
        double[] sun_r = new double[4];
        double[] sun_rprime = new double[4];

        int i = 0;

        /* Begin by finding the barycentric equatorial state vector of the Sun */
        planetaryEphemeris(jultime, rho, 0);
        for (i = 1; i <= 3; i++) {
            sun_r[i] = planet_r[0][i];
            sun_rprime[i] = planet_rprime[0][i];
        }

        /* Convert the minor planet state vector to barycentric equatorial for use in n-body routines */
        for (i = 1; i <= 3; i++) {
            r[i] = r[i] + sun_r[i];
            rprime[i] = kappa * rprime[i] + sun_rprime[i];
        }


    }

    void twoBodyDetectCollision(int target, double object_r_at_n[], double object_rprime_at_n[], double jultime_n, double jultime_n1, double event[]) {

        /*
          Given the position and velocity of an object at jultime_n and jultime_n1, determine whether the object collides with a planet, or has a near miss with a planet, between time steps n and n+1.
          Inputs:
            target = the target planet
            object_r_at_n[], object_rprime_at_n[] = the barycentric equatorial posvel of the object at time step n;
            jultime_n, jultime_n1 = the Julian dates corresponding to time steps n and n+1
          The output array "event[]" has five entries.
            "event[1]" gives the Julian date,
            "event[2]" gives the nominal distance in A.U.s.,
            "event[3]" gives the object's x-position relative to the target at the time of the event,
            "event[4]" gives the object's y-position relative to the target at the time of the event, and
            "event[5]" gives the object's z-position relative to the target at the time of the event.
        */

        double[] object_r_at_n1 = new double[4];
        double[] object_rprime_at_n1 = new double[4];
        double[] rho = new double[4];
        double[][] planet_r_at_n = new double[11][4];
        double[][] planet_r_at_n1 = new double[11][4];
        double[][] planet_rprime_at_n = new double[11][4];
        double[][] planet_rprime_at_n1 = new double[11][4];
        double[] planet_i_r_at_n = new double[4];
        double[] planet_i_rprime_at_n = new double[4];
        double[] planet_i_r_at_time = new double[4];
        double[] planet_i_rprime_at_time = new double[4];
        double[] r_at_time = new double[4];
        double[] rprime_at_time = new double[4];
        double[] r_at_n = new double[4];
        double[] rprime_at_n = new double[4];
        double[] r_at_n1 = new double[4];
        double[] rprime_at_n1 = new double[4];

        double distance_n1 = 0, upper_time = 0, lower_time = 0, time = 0, distance = 0, distance_n = 0, deriv_1_at_n = 0, deriv_1_at_n1 = 0, deriv_1_at_time = 0;

        int i = 0, j = 0, counter = 0;

        /* Initialize barycentric object posvel arrays at time step n */
        for (j = 1; j <= 3; j++) {
            r_at_n[j] = object_r_at_n[j];
            rprime_at_n[j] = object_rprime_at_n[j];
        }

        /* Generate the two-body posvel of the object at time n1 */
        barycentricToTwoBodyHeliocentric(jultime_n, r_at_n, rprime_at_n);
        twoBodyUpdate(r_at_n, rprime_at_n, jultime_n, object_r_at_n1, object_rprime_at_n1, jultime_n1);
        twoBodyHeliocentricToBarycentric(jultime_n1, object_r_at_n1, object_rprime_at_n1);

        /*  Get the ephemerides of the planets at each time step, without correction for light time-of-flight  */
        planetaryEphemeris(jultime_n, rho, 0);
        /* Initialize barycentric planetary posvel arrays at time step n */
        for (i = 0; i <= 10; i++) {
            for (j = 1; j <= 3; j++) {
                planet_r_at_n[i][j] = planet_r[i][j];
                planet_rprime_at_n[i][j] = planet_rprime[i][j];
            }
        }
        planetaryEphemeris(jultime_n1, rho, 0);
        /* Initialize barycentric planetary posvel arrays at time step n+1 */
        for (i = 0; i <= 10; i++) {
            for (j = 1; j <= 3; j++) {
                planet_r_at_n1[i][j] = planet_r[i][j];
                planet_rprime_at_n1[i][j] = planet_rprime[i][j];
            }
        }
        /* Initialize barycentric object posvel arrays at time step n+1 */
        for (j = 1; j <= 3; j++) {
            r_at_n1[j] = object_r_at_n1[j];
            rprime_at_n1[j] = object_rprime_at_n1[j];
        }

        /*  Initialize barycentric posvel arrays for planet target  */
        for (j = 1; j <= 3; j++) {
            planet_i_r_at_n[j] = planet_r_at_n[target][j];
            planet_i_rprime_at_n[j] = planet_rprime_at_n[target][j];
        }
        /*  Check for collision at time step n+1.  */
        distance_n1 = Math.sqrt(Math.pow((object_r_at_n1[1] - planet_r_at_n1[target][1]), 2) + Math.pow((object_r_at_n1[2] - planet_r_at_n1[target][2]), 2) + Math.pow((object_r_at_n1[3] - planet_r_at_n1[target][3]), 2));

        if (distance_n1 <= planet_radius[target]) {
            /*  Collision.  Use bisection to find it.  */
/* System.out.println("Collision at n1"); */
            upper_time = jultime_n1;
            lower_time = jultime_n;
            event[1] = jultime_n1;
            event[2] = distance_n1;
            while ((Math.abs(upper_time - lower_time) > event_localization) && (counter < 100)) {
                counter = counter + 1;
                time = lower_time + (upper_time - lower_time) / 2;
/* System.out.println("time = " + time); */
                twoBodyUpdate(r_at_n, rprime_at_n, jultime_n, r_at_time, rprime_at_time, time);
                twoBodyHeliocentricToBarycentric(time, r_at_time, rprime_at_time);
                planetaryEphemeris(time, rho, 0);
                for (j = 1; j <= 3; j++) {
                    planet_i_r_at_time[j] = planet_r[target][j];
                    planet_i_rprime_at_time[j] = planet_rprime[target][j];
                }
                distance = Math.sqrt(Math.pow((r_at_time[1] - planet_i_r_at_time[1]), 2) + Math.pow((r_at_time[2] - planet_i_r_at_time[2]), 2) + Math.pow((r_at_time[3] - planet_i_r_at_time[3]), 2));
                if (distance <= planet_radius[target]) {
                    upper_time = time;
                } else
                    /*  Collision has not yet occurred.  Increase lower limit.  */
                    lower_time = time;
            }
            event[1] = time;
            event[2] = distance;
            event[3] = r_at_time[1] - planet_i_r_at_time[1];
            event[4] = r_at_time[2] - planet_i_r_at_time[2];
            event[5] = r_at_time[3] - planet_i_r_at_time[3];
        } else {
            /* Not in collision at time step n+1; evaluate intermediate points */
            /*  Calculate the first derivative of distance at time steps n and n+1; check for a minimum that is less than the planetary radius for collision, or less than near_miss_threshold for a near miss.  */
            distance_n = Math.sqrt(Math.pow((object_r_at_n[1] - planet_r_at_n[target][1]), 2) + Math.pow((object_r_at_n[2] - planet_r_at_n[target][2]), 2) + Math.pow((object_r_at_n[3] - planet_r_at_n[target][3]), 2));
            deriv_1_at_n = ((object_r_at_n[1] - planet_r_at_n[target][1]) * (object_rprime_at_n[1] - planet_rprime_at_n[target][1]) + (object_r_at_n[2] - planet_r_at_n[target][2]) * (object_rprime_at_n[2] - planet_rprime_at_n[target][2]) + (object_r_at_n[3] - planet_r_at_n[target][3]) * (object_rprime_at_n[3] - planet_rprime_at_n[target][3])) / distance_n;
            deriv_1_at_n1 = ((object_r_at_n1[1] - planet_r_at_n1[target][1]) * (object_rprime_at_n1[1] - planet_rprime_at_n1[target][1]) + (object_r_at_n1[2] - planet_r_at_n1[target][2]) * (object_rprime_at_n1[2] - planet_rprime_at_n1[target][2]) + (object_r_at_n1[3] - planet_r_at_n1[target][3]) * (object_rprime_at_n1[3] - planet_rprime_at_n1[target][3])) / distance_n1;
/* System.out.println("distance_n = " + distance_n);
System.out.println("deriv_1_at_n = " + deriv_1_at_n);
System.out.println("deriv_1_at_n1 = " + deriv_1_at_n1); */
            if ((deriv_1_at_n < 0) && (deriv_1_at_n1 > 0)) {
                /*  A relevant minimum has occurred.  Use bisection on the first derivative of distance to find it.  */
/* System.out.println("Relevant minimum between n and n1"); */
                upper_time = jultime_n1;
                lower_time = jultime_n;
                while ((Math.abs(upper_time - lower_time) > event_localization) && (counter < 100)) {
                    counter = counter + 1;
                    time = lower_time + (upper_time - lower_time) / 2;
/* System.out.println("time = " + time); */
                    twoBodyUpdate(r_at_n, rprime_at_n, jultime_n, r_at_time, rprime_at_time, time);
                    twoBodyHeliocentricToBarycentric(time, r_at_time, rprime_at_time);
                    planetaryEphemeris(time, rho, 0);
                    for (j = 1; j <= 3; j++) {
                        planet_i_r_at_time[j] = planet_r[target][j];
                        planet_i_rprime_at_time[j] = planet_rprime[target][j];
                    }
                    distance = Math.sqrt(Math.pow((r_at_time[1] - planet_i_r_at_time[1]), 2) + Math.pow((r_at_time[2] - planet_i_r_at_time[2]), 2) + Math.pow((r_at_time[3] - planet_i_r_at_time[3]), 2));
/* System.out.println("distance = " + distance); */
                    deriv_1_at_time = ((r_at_time[1] - planet_i_r_at_time[1]) * (rprime_at_time[1] - planet_i_rprime_at_time[1]) + (r_at_time[2] - planet_i_r_at_time[2]) * (rprime_at_time[2] - planet_i_rprime_at_time[2]) + (r_at_time[3] - planet_i_r_at_time[3]) * (rprime_at_time[3] - planet_i_rprime_at_time[3])) / distance;
                    if (deriv_1_at_time < 0)
                        /*  Minimum has not yet occurred.  Increase lower limit.  */
                        lower_time = time;
                    else
                        /*  Minimum has already occurred.  Reduce upper limit.  */
                        upper_time = time;
                }
                /*  We've now located the minimum to great accuracy.  Is it a collision, or a near miss?  */
                if (distance <= planet_radius[target]) {
                    /*  Collision.  Use bisection to find it.  */
/* System.out.println("Collision between n and n1"); */
                    upper_time = time;
                    lower_time = jultime_n;
                    event[1] = time;
                    event[2] = distance;
                    while ((Math.abs(upper_time - lower_time) > event_localization) && (counter < 200)) {
                        counter = counter + 1;
                        time = lower_time + (upper_time - lower_time) / 2;
/* System.out.println("time = " + time); */
                        twoBodyUpdate(r_at_n, rprime_at_n, jultime_n, r_at_time, rprime_at_time, time);
                        twoBodyHeliocentricToBarycentric(time, r_at_time, rprime_at_time);
                        planetaryEphemeris(time, rho, 0);
                        for (j = 1; j <= 3; j++) {
                            planet_i_r_at_time[j] = planet_r[target][j];
                            planet_i_rprime_at_time[j] = planet_rprime[target][j];
                        }
                        distance = Math.sqrt(Math.pow((r_at_time[1] - planet_i_r_at_time[1]), 2) + Math.pow((r_at_time[2] - planet_i_r_at_time[2]), 2) + Math.pow((r_at_time[3] - planet_i_r_at_time[3]), 2));
                        if (distance <= planet_radius[target]) {
                            upper_time = time;
                        } else
                            /*  Collision has not yet occurred.  Increase lower limit.  */
                            lower_time = time;
                    }
                    event[1] = time;
                    event[2] = distance;
                    event[3] = r_at_time[1] - planet_i_r_at_time[1];
                    event[4] = r_at_time[2] - planet_i_r_at_time[2];
                    event[5] = r_at_time[3] - planet_i_r_at_time[3];
                } else {
                    /*  Near miss.  Record data.  */
/* System.out.println("Near-miss between n and n1"); */
                    event[1] = time;
                    event[2] = distance;
                    event[3] = r_at_time[1] - planet_i_r_at_time[1];
                    event[4] = r_at_time[2] - planet_i_r_at_time[2];
                    event[5] = r_at_time[3] - planet_i_r_at_time[3];
                }
            }
        }
    }


    public void hergetsMethod(double[] Lx, double[] Ly, double[] Lz, double[] x, double[] y, double[] z, double[] herget_times, double rho1, double rho2) {
        /* This implementation of Herget's method is experimental, and may become the core of a statistical ranging algorithm */
        /*
        Inputs:
            rho1 = user-defined slant range at TDB Julian date herget_times[1]
            rho2 = user-defined slant range at TDB Julian date herget_times[2]
            Lx, Ly, and Lz = equatorial slant vectors
            x, y, and z = barycentric equatorial observer's position vectors
            herget_times[1] and herget_times[2] = TDB time at each observation
        Outputs:
            herget_residuals, herget_rms_residuals (created by least-squares)
            (implicit) epoch_r, epoch_rprime = barycentric equatorial state vector for epoch_time = herget_times[1] (created by least-squares)
        */

        int i = 0;

        double accel_time = 0;

        double[] r1 = new double[4];
        double[] r2 = new double[4];
        double[] r1prime = new double[4];
        double[] r1accel = new double[4];
        double[] rhalfway = new double[4];

        /* Using the user-specified slant ranges, create the corresponding barycentric equatorial position vectors */
        r1[1] = rho1 * Lx[1] + x[1];
        r1[2] = rho1 * Ly[1] + y[1];
        r1[3] = rho1 * Lz[1] + z[1];
        r2[1] = rho2 * Lx[2] + x[2];
        r2[2] = rho2 * Ly[2] + y[2];
        r2[3] = rho2 * Lz[2] + z[2];

        /* Create an estimate for the barycentric equatorial velocity vector at herget_times[1] */
        accel_time = ((herget_times[1] - rho1 / speed_of_light) + (herget_times[2] - rho2 / speed_of_light)) / 2.0;
        for (i = 1; i <= 3; i++) {
            r1prime[i] = (r2[i] - r1[i]) / ((herget_times[2] - rho2 / speed_of_light) - (herget_times[1] - rho1 / speed_of_light));
            rhalfway[i] = (r1[i] + r2[i]) / 2.0;
        }
        getAcceleration(accel_time, rhalfway, r1prime, r1accel);
        for (i = 1; i <= 3; i++)
            r1prime[i] = (r2[i] - r1[i]) / ((herget_times[2] - rho2 / speed_of_light) - (herget_times[1] - rho1 / speed_of_light)) - r1accel[i] * ((herget_times[2] - rho2 / speed_of_light) - (herget_times[1] - rho1 / speed_of_light)) / 2.0;

        /* We now have a state vector for herget_times[1] */
        epoch_time = herget_times[1] - rho1 / speed_of_light;
        for (i = 0; i <= 3; i++) {
            epoch_r[i] = r1[i];
            epoch_rprime[i] = r1prime[i];
        }

    }

    public void lovLeastSquaresSolution(double[] bvector, double[] residuals, double[] rms_residuals) {

        /*
            Method to calculate the least squares best-fit orbit of a minor planet, given optical, radar delay, and or radar doppler observations.  The algorithm is only applied once, and the first basis vector is replaced by the input bvector.
            The method presumes that an a priori state vector has been selected, and resides in epoch_r, epoch_rprime, and epoch_time.  The method also assumes that the observations have been ordered from earliest to most recent.
            The output will be the epoch state vector, and the epoch error covariance matrix.
            We will assume a maximum of 2000 optical observations, 100 radar delay observations, and 100 radar doppler observations.
            Adapted from: D.K. Yeomans, S.J. Ostro, and P.W. Chodas (1987). "Radar Astrometry of Near-Earth Asteroids". Astronomical Journal 94, 189-200.  Modified to the "Square Root Information Filter" algorithm from Gerald J. Bierman (1974), "Sequential Square Root Filtering and Smoothing of Discrete Linear Systems", Automatica 10, 147-158.
          */

        int i = 0, j = 0, k = 0, index = 0, retained_index = 0;

        boolean convergence = false, divergence = false, edit = false;

        double rms_optical = 0, rms_delay = 0, rms_doppler = 0;
        double old_rms_optical = 0, old_rms_delay = 0, old_rms_doppler = 0;
        double convergence_factor = 0.10;
        double delta = 0, delay = 0, doppler = 0;
        double observation_residual = 0, observation_chi = 0;

        int number_of_observations = number_of_optical_observations + number_of_delay_observations + number_of_doppler_observations;

        double[] nominal_r = new double[4];
        double[] nominal_rprime = new double[4];
        double[] initial_r = new double[4];
        double[] initial_rprime = new double[4];
        double[][] r_variant = new double[7][4];
        double[][] rprime_variant = new double[7][4];
        double[] this_r = new double[4];
        double[] this_rprime = new double[4];
        double[] next_r = new double[4];
        double[] next_rprime = new double[4];
        double[] variant_delta = new double[7];
        double[] dev = new double[16002];
        double[][] matrix_a = new double[16002][7];
        double[][] collision_variant = new double[2000][9];
        double[][] phi = new double[7][7];
        double[] inter = new double[7];
        double[] sensitivity_ra = new double[7];
        double[] sensitivity_dec = new double[7];
        double[] sensitivity_delay = new double[7];
        double[] sensitivity_doppler = new double[7];
        double[] radec = new double[3];
        double[] row = new double[7];
        double[] corrections = new double[7];
        double[] observer_r = new double[4];
        double[] observer_rprime = new double[4];
        double[] delay_doppler = new double[3];
        double[] receiver_r = new double[4];
        double[] receiver_rprime = new double[4];
        double[] EOP = new double[8];
        double[][] matrix_R = new double[7][7];
        double[] matrix_z = new double[7];
        double[][] matrix_T = new double[6002][6002];
        double[][] matrix_temp = new double[16002][7];
        double[] retained_residuals = new double[16002];
        double[] initial_residuals = new double[16002];
        double[] initial_rms_residuals = new double[4];
        double[] temp_r = new double[4];
        double[] temp_rprime = new double[4];
        double[][] matrix_P = new double[7][7];
        double[][] inverse_matrix_P = new double[7][7];
        double[] converted_corrections = new double[7];
        double[][] initial_big_p = new double[9][9];

        /* Initialize all observations as non-excluded for get_residuals */
        for (i = 1; i <= 8000; i++)
            excluded_observations[i] = 0;

        /* Using the a priori state vector, determine initial residuals, nominal observables, and the state vector at the tdb time of each observation.  Save the a priori state vector in case of divergence. */
        getResiduals(residuals, rms_residuals);
        rms_optical = rms_residuals[1];
        rms_delay = rms_residuals[2];
        rms_doppler = rms_residuals[3];
        for (i = 1; i <= 3; i++) {
            initial_r[i] = epoch_r[i];
            initial_rprime[i] = epoch_rprime[i];
            initial_rms_residuals[i] = rms_residuals[i];
        }
        for (i = 1; i <= 8; i++) {
            for (j = 1; j <= 8; j++)
                initial_big_p[i][j] = big_p[i][j];
        }
        index = 2 * number_of_optical_observations + number_of_delay_observations + number_of_doppler_observations;
        for (i = 1; i <= index; i++) {
            initial_residuals[i] = residuals[i];
        }

        /*
          The convergence condition is: The RMS residuals on the current loop must differ by no more than some percentage from the RMS residuals on the preceding loop.
          (Note that this condition was not my original choice.  I tried requiring that the corrections to the epoch state vector must be less than some percentage of their corresponding covariances.  However, real-world data is not sufficiently smooth.  This new condition uses the observed fact that the RMS residuals decrease with successive loops until the next loop results in almost no change.  At this point, the solution has converged.)
          Clearly, this condition will not be satisfied initially, so we will go through the loop at least once.
          NOTE:  MUST ACCOUNT FOR DIVERGENCE, AS MEASURED BY INCREASING RMS RESIDUALS.  If each of the RMS residuals increase from one loop to the next, the divergence flag will be set, and the original solution returned.
          */
        while ((convergence == false) && (divergence == false)) {

            /* Clear the counters */
            index = 0;
            retained_index = 0;

            /* Use the observations to generate rows of matrix_a */
            for (i = 1; i <= number_of_observations; i++) {

                /* Assume that the observation will be excluded; if not, the flag will be reset below. */
                excluded_observations[i] = 1;

                /* Generate the state transition matrix corresponding to this observation time */
                for (j = 1; j <= 3; j++) {
                    nominal_r[j] = incremental_state_vector_r[i][j];
                    nominal_rprime[j] = incremental_state_vector_rprime[i][j];
                }
                /* Initialize the variant trajectories */
                if (i == 1) {
                    for (j = 1; j <= 6; j++) {
                        for (k = 1; k <= 3; k++) {
                            r_variant[j][k] = epoch_r[k];
                            rprime_variant[j][k] = epoch_rprime[k];
                        }
                    }
                    for (j = 1; j <= 3; j++) {
                        variant_delta[j] = 0.000001 * epoch_r[j];
                        r_variant[j][j] = r_variant[j][j] + variant_delta[j];
                        variant_delta[j + 3] = 0.000001 * epoch_rprime[j];
                        rprime_variant[j + 3][j] = rprime_variant[j + 3][j] + variant_delta[j + 3];
                    }
                }
                /* Propogate the variant trajectories, and calculate the transition matrix entries */
                for (j = 1; j <= 6; j++) {
                    for (k = 1; k <= 3; k++) {
                        this_r[k] = r_variant[j][k];
                        this_rprime[k] = rprime_variant[j][k];
                    }
                    update(this_r, this_rprime, incremental_state_vector_epoch[i - 1], next_r, next_rprime, incremental_state_vector_epoch[i], false, collision_variant);
                    for (k = 1; k <= 3; k++) {
                        r_variant[j][k] = next_r[k];
                        rprime_variant[j][k] = next_rprime[k];
                    }
                    for (k = 1; k <= 3; k++) {
                        phi[k][j] = (r_variant[j][k] - nominal_r[k]) / variant_delta[j];
                        phi[k + 3][j] = (rprime_variant[j][k] - nominal_rprime[k]) / variant_delta[j];
                    }
                }

                /* Generate the sensitivity matrix for this observation */
                if (observation_type[i] == 1) {
                    /* Optical observation */
                    for (j = 1; j <= 3; j++) {
                        observer_r[j] = observation_r[i][j];
                        observer_rprime[j] = observation_rprime[i][j];
                    }
                    for (j = 1; j <= 3; j++) {
                        delta = 0.000001 * nominal_r[j];
                        nominal_r[j] = nominal_r[j] + delta;
                        getObservedRadec(nominal_r, nominal_rprime, incremental_state_vector_epoch[i], observation_geocentric[i], observation_latitude[i], observation_longitude[i], observation_altitude[i], observation_EOP[i][3], observer_r, observer_rprime, optical_time[i], radec);
                        sensitivity_ra[j] = radec[1] - nominal_observable[index + 1];
                        while (Math.abs(sensitivity_ra[j]) > Math.abs(sensitivity_ra[j] - 2.0 * Math.PI))
                            sensitivity_ra[j] = sensitivity_ra[j] - 2.0 * Math.PI;
                        while (Math.abs(sensitivity_ra[j]) > Math.abs(sensitivity_ra[j] + 2.0 * Math.PI))
                            sensitivity_ra[j] = sensitivity_ra[j] + 2.0 * Math.PI;
                        sensitivity_ra[j] = sensitivity_ra[j] / delta;
                        sensitivity_dec[j] = (radec[2] - nominal_observable[index + 2]) / delta;
                        nominal_r[j] = nominal_r[j] - delta;
                        delta = 0.000001 * nominal_rprime[j];
                        nominal_rprime[j] = nominal_rprime[j] + delta;
                        getObservedRadec(nominal_r, nominal_rprime, incremental_state_vector_epoch[i], observation_geocentric[i], observation_latitude[i], observation_longitude[i], observation_altitude[i], observation_EOP[i][3], observer_r, observer_rprime, optical_time[i], radec);
                        sensitivity_ra[j + 3] = radec[1] - nominal_observable[index + 1];
                        while (Math.abs(sensitivity_ra[j + 3]) > Math.abs(sensitivity_ra[j + 3] - 2.0 * Math.PI))
                            sensitivity_ra[j + 3] = sensitivity_ra[j + 3] - 2.0 * Math.PI;
                        while (Math.abs(sensitivity_ra[j + 3]) > Math.abs(sensitivity_ra[j + 3] + 2.0 * Math.PI))
                            sensitivity_ra[j + 3] = sensitivity_ra[j + 3] + 2.0 * Math.PI;
                        sensitivity_ra[j + 3] = sensitivity_ra[j + 3] / delta;
                        sensitivity_dec[j + 3] = (radec[2] - nominal_observable[index + 2]) / delta;
                        nominal_rprime[j] = nominal_rprime[j] - delta;
                    }

                    /* Calculate the residuals and chi; if observations meet the applicable thresholds, calculate the resulting rows of matrix_a */
                    observation_residual = Math.acos(Math.sin(nominal_observable[index + 2]) * Math.sin(optical_dec[i]) + Math.cos(nominal_observable[index + 2]) * Math.cos(optical_dec[i]) * Math.cos(nominal_observable[index + 1] - optical_ra[i]));
                    if ((ra_dev[i] > 0.0) && (dec_dev[i] > 0.0))
                        observation_chi = Math.sqrt(Math.pow(residuals[index + 1] * Math.cos(optical_dec[i]) / ra_dev[i], 2.0) + Math.pow(residuals[index + 2] / dec_dev[i], 2.0));
                    else
                        observation_chi = 0.0;
                    if (((edit) && (chi_testing) && (observation_chi < chi_threshold) && (ra_dev[i] < 5.0 * Math.PI / (3600.0 * 180.0)) && (dec_dev[i] < 5.0 * Math.PI / (3600.0 * 180.0))) || ((edit) && (residual_testing) && (Math.abs(observation_residual) < optical_residual_threshold) && (ra_dev[i] < 5.0 * Math.PI / (3600.0 * 180.0)) && (dec_dev[i] < 5.0 * Math.PI / (3600.0 * 180.0))) || (!edit)) {

                        /* Observation is not excluded; reset flag */
                        excluded_observations[i] = 0;

                        for (j = 1; j <= 6; j++) {
                            row[j] = 0.0;
                            for (k = 1; k <= 6; k++)
                                row[j] = row[j] + sensitivity_ra[k] * phi[k][j];
                            matrix_a[retained_index + 1][j] = row[j];
                        }
                        for (j = 1; j <= 6; j++) {
                            row[j] = 0.0;
                            for (k = 1; k <= 6; k++)
                                row[j] = row[j] + sensitivity_dec[k] * phi[k][j];
                            matrix_a[retained_index + 2][j] = row[j];
                        }

                        /* Fill the dev array with standard deviations */
                        dev[retained_index + 1] = ra_dev[i];
                        dev[retained_index + 2] = dec_dev[i];

                        /* Add these residuals to the list of those being retained */
                        retained_residuals[retained_index + 1] = residuals[index + 1];
                        retained_residuals[retained_index + 2] = residuals[index + 2];

                        /* Increment the retained observation counter */
                        retained_index = retained_index + 2;

                    }

                    /* Increment the index */
                    index = index + 2;

                } else if (observation_type[i] == 2) {
                    /* Delay observation */
                    for (j = 1; j <= 3; j++) {
                        receiver_r[j] = radar_delay_receiver_r[i][j];
                        receiver_rprime[j] = radar_delay_receiver_rprime[i][j];
                    }
                    for (j = 1; j <= 7; j++)
                        EOP[j] = radar_delay_transmitter_EOP[i][j];

                    for (j = 1; j <= 3; j++) {
                        delta = 0.000001 * nominal_r[j];
                        nominal_r[j] = nominal_r[j] + delta;
                        getObservedRadarDelayDoppler(radar_delay_receiver_latitude[i], radar_delay_receiver_longitude[i], radar_delay_receiver_altitude[i], receiver_r, receiver_rprime, radar_delay_receiver_time[i], radar_delay_transmitter_latitude[i], radar_delay_transmitter_longitude[i], radar_delay_transmitter_altitude[i], EOP, radar_delay_transmitter_tdb_minus_utc[i], radar_delay_receiver_tdb_minus_utc[i], radar_delay_transmitter_frequency[i], minor_planet_radius, nominal_r, nominal_rprime, incremental_state_vector_epoch[i], delay_doppler);
                        delay = delay_doppler[1];
                        sensitivity_delay[j] = (delay - nominal_observable[index + 1]) / delta;
                        nominal_r[j] = nominal_r[j] - delta;
                        delta = 0.000001 * nominal_rprime[j];
                        nominal_rprime[j] = nominal_rprime[j] + delta;
                        getObservedRadarDelayDoppler(radar_delay_receiver_latitude[i], radar_delay_receiver_longitude[i], radar_delay_receiver_altitude[i], receiver_r, receiver_rprime, radar_delay_receiver_time[i], radar_delay_transmitter_latitude[i], radar_delay_transmitter_longitude[i], radar_delay_transmitter_altitude[i], EOP, radar_delay_transmitter_tdb_minus_utc[i], radar_delay_receiver_tdb_minus_utc[i], radar_delay_transmitter_frequency[i], minor_planet_radius, nominal_r, nominal_rprime, incremental_state_vector_epoch[i], delay_doppler);
                        delay = delay_doppler[1];
                        sensitivity_delay[j + 3] = (delay - nominal_observable[index + 1]) / delta;
                        nominal_rprime[j] = nominal_rprime[j] - delta;
                    }

                    /* Calculate the residual and chi; if observations meet the applicable threshold, calculate the resulting row of matrix_a */
                    observation_residual = Math.abs(residuals[index + 1]);
                    if (delay_dev[i] != 0.0)
                        observation_chi = Math.abs(residuals[index + 1] / delay_dev[i]);
                    else
                        observation_chi = 0.0;
                    if (((edit) && (chi_testing) && (observation_chi < chi_threshold)) || ((edit) && (residual_testing) && (Math.abs(observation_residual) < delay_residual_threshold)) || (!edit)) {

                        /* Observation is not excluded; reset flag */
                        excluded_observations[i] = 0;

                        for (j = 1; j <= 6; j++) {
                            row[j] = 0.0;
                            for (k = 1; k <= 6; k++)
                                row[j] = row[j] + sensitivity_delay[k] * phi[k][j];
                            matrix_a[retained_index + 1][j] = row[j];
                        }

                        /* Fill the dev array with standard deviations */
                        dev[retained_index + 1] = delay_dev[i];

                        /* Add this residual to the list of those being retained */
                        retained_residuals[retained_index + 1] = residuals[index + 1];

                        /* Increment the retained observation counter */
                        retained_index = retained_index + 1;

                    }

                    /* Increment the index */
                    index = index + 1;

                } else if (observation_type[i] == 3) {
                    /* Doppler observation */
                    for (j = 1; j <= 3; j++) {
                        receiver_r[j] = radar_doppler_receiver_r[i][j];
                        receiver_rprime[j] = radar_doppler_receiver_rprime[i][j];
                    }
                    for (j = 1; j <= 7; j++) {
                        EOP[j] = radar_doppler_transmitter_EOP[i][j];
                    }

                    for (j = 1; j <= 3; j++) {
                        delta = 0.000001 * nominal_r[j];
                        nominal_r[j] = nominal_r[j] + delta;
                        getObservedRadarDelayDoppler(radar_doppler_receiver_latitude[i], radar_doppler_receiver_longitude[i], radar_doppler_receiver_altitude[i], receiver_r, receiver_rprime, radar_doppler_receiver_time[i], radar_doppler_transmitter_latitude[i], radar_doppler_transmitter_longitude[i], radar_doppler_transmitter_altitude[i], EOP, radar_doppler_transmitter_tdb_minus_utc[i], radar_doppler_receiver_tdb_minus_utc[i], radar_doppler_transmitter_frequency[i], minor_planet_radius, nominal_r, nominal_rprime, incremental_state_vector_epoch[i], delay_doppler);
                        doppler = delay_doppler[2];
                        sensitivity_doppler[j] = (doppler - nominal_observable[index + 1]) / delta;
                        nominal_r[j] = nominal_r[j] - delta;
                        delta = 0.000001 * nominal_rprime[j];
                        nominal_rprime[j] = nominal_rprime[j] + delta;
                        getObservedRadarDelayDoppler(radar_doppler_receiver_latitude[i], radar_doppler_receiver_longitude[i], radar_doppler_receiver_altitude[i], receiver_r, receiver_rprime, radar_doppler_receiver_time[i], radar_doppler_transmitter_latitude[i], radar_doppler_transmitter_longitude[i], radar_doppler_transmitter_altitude[i], EOP, radar_doppler_transmitter_tdb_minus_utc[i], radar_doppler_receiver_tdb_minus_utc[i], radar_doppler_transmitter_frequency[i], minor_planet_radius, nominal_r, nominal_rprime, incremental_state_vector_epoch[i], delay_doppler);
                        doppler = delay_doppler[2];
                        sensitivity_doppler[j + 3] = (doppler - nominal_observable[index + 1]) / delta;
                        nominal_rprime[j] = nominal_rprime[j] - delta;
                    }

                    /* Calculate the residual and chi; if observations meet the applicable thresholds, calculate the resulting row of matrix_a */
                    observation_residual = Math.abs(residuals[index + 1]);

                    if (doppler_dev[i] != 0.0)
                        observation_chi = Math.abs(residuals[index + 1] / doppler_dev[i]);
                    else
                        observation_chi = 0.0;
                    if (((edit) && (chi_testing) && (observation_chi < chi_threshold)) || ((edit) && (residual_testing) && (Math.abs(observation_residual) < doppler_residual_threshold)) || (!edit)) {

                        /* Observation is not excluded; reset flag */
                        excluded_observations[i] = 0;

                        for (j = 1; j <= 6; j++) {
                            row[j] = 0.0;
                            for (k = 1; k <= 6; k++)
                                row[j] = row[j] + sensitivity_doppler[k] * phi[k][j];
                            matrix_a[retained_index + 1][j] = row[j];
                        }

                        /* Fill the dev array with standard deviations */
                        dev[retained_index + 1] = doppler_dev[i];

                        /* Add this residual to the list of those being retained */
                        retained_residuals[retained_index + 1] = residuals[index + 1];

                        /* Increment the retained observation counter */
                        retained_index = retained_index + 1;

                    }

                    /* Increment the index */
                    index = index + 1;

                }


            }

            /* At this point, matrix A is complete.  We proceed to convert to a unity covariance form */
            /* Should be commented out */
            for (i = 1; i <= retained_index; i++) {
                for (j = 1; j <= 6; j++)
                    matrix_temp[i][j] = matrix_a[i][j] / dev[i];
                retained_residuals[i] = retained_residuals[i] / dev[i];
            }
            for (i = 1; i <= retained_index; i++) {
                for (j = 1; j <= 6; j++)
                    matrix_a[i][j] = matrix_temp[i][j];
            }

            /* Construct an orthogonal transform T using Givens rotations, and left multiply.  matrix_a will now be upper triangular; isolate the upper 6x6 as matrix_R, and isolate the top 6x1 of T x residuals as matrix_z. */
            givensQr(retained_index, 6, matrix_a, matrix_T);

            for (i = 1; i <= 6; i++) {
                matrix_z[i] = 0;
                for (j = 1; j <= retained_index; j++)
                    matrix_z[i] = matrix_z[i] + matrix_T[i][j] * retained_residuals[j];
            }

            for (i = 1; i <= 6; i++) {
                for (j = 1; j <= 6; j++)
                    matrix_R[i][j] = matrix_a[i][j];
            }

            /* Calculate the state vector corrections and covariances. */
            invertNxn(6, matrix_R);

            for (i = 1; i <= 6; i++) {
                corrections[i] = 0;
                for (k = 1; k <= 6; k++)
                    /* Square Root Information Filtering assumes that we are in unit covariance form.  Since we're often unable to invert matrix_R in that form, we must make the conversion to unit covariance form now, so the results are valid.  In this particular calculation, we would multiply matrix_R[i][k] by dev[k] (since matrix_a has been inverted), and we would divide matrix_z by dev[k]; since these precisely cancel, no action is really needed here. */
                    corrections[i] = corrections[i] + matrix_R[i][k] * matrix_z[k];
            }

            for (i = 1; i <= 6; i++) {
                for (j = 1; j <= 6; j++) {
                    big_p[i][j] = 0;
                    for (k = 1; k <= 6; k++)
                        /* Square Root Information Filtering assumes that we are in unit covariance form.  Since we're often unable to invert matrix_R in that form, we must make the conversion to unit covariance form now, so the results are valid.  In this particular calculation, we would multiply matrix_R[i][k] by dev[k] (since matrix_a has been inverted), and we would multiply matrix_R[j][k] by dev[k] (for the same reason).  */
/*						big_p[i][j] = big_p[i][j] + matrix_R[i][k]*matrix_R[j][k]*(dev[k]*dev[k]);   */
                        big_p[i][j] = big_p[i][j] + matrix_R[i][k] * matrix_R[j][k];
                }
            }

            /* At this point, for LOV sampling, we must convert the state vector corrections to a basis where the first vector is the eigenvector of the current covariance matrix (input as "bvector").  We will then set the 1st entry in the corrections array to zero, so we continue traveling along the LOV.  Finally, we'll convert this corrections array back to normal space, and apply the correction. */
            if (Math.abs(bvector[1] - 1.0) > 1.0E-10) {
                /* Create the conversion matrix P and its inverse */
                for (i = 1; i <= 6; i++) {
                    matrix_P[i][1] = bvector[i];
                    inverse_matrix_P[i][1] = bvector[i];
                }
                /* Column 2 */
                delta = 0;
                for (i = 1; i <= 6; i++) {
                    if (i == 2)
                        matrix_P[i][2] = 1.0 - bvector[2] * matrix_P[i][1];
                    else
                        matrix_P[i][2] = -bvector[2] * matrix_P[i][1];
                    delta = delta + matrix_P[i][2] * matrix_P[i][2];
                }
                for (i = 1; i <= 6; i++) {
                    matrix_P[i][2] = matrix_P[i][2] / Math.sqrt(delta);
                    inverse_matrix_P[i][2] = matrix_P[i][2];
                }
                /* Column 3 */
                delta = 0;
                for (i = 1; i <= 6; i++) {
                    if (i == 3)
                        matrix_P[i][3] = 1.0 - bvector[3] * matrix_P[i][1] - matrix_P[3][2] * matrix_P[i][2];
                    else
                        matrix_P[i][3] = -bvector[3] * matrix_P[i][1] - matrix_P[3][2] * matrix_P[i][2];
                    delta = delta + matrix_P[i][3] * matrix_P[i][3];
                }
                for (i = 1; i <= 6; i++) {
                    matrix_P[i][3] = matrix_P[i][3] / Math.sqrt(delta);
                    inverse_matrix_P[i][3] = matrix_P[i][3];
                }
                /* Column 4 */
                delta = 0;
                for (i = 1; i <= 6; i++) {
                    if (i == 4)
                        matrix_P[i][4] = 1.0 - bvector[4] * matrix_P[i][1] - matrix_P[4][2] * matrix_P[i][2] - matrix_P[4][3] * matrix_P[i][3];
                    else
                        matrix_P[i][4] = -bvector[4] * matrix_P[i][1] - matrix_P[4][2] * matrix_P[i][2] - matrix_P[4][3] * matrix_P[i][3];
                    delta = delta + matrix_P[i][4] * matrix_P[i][4];
                }
                for (i = 1; i <= 6; i++) {
                    matrix_P[i][4] = matrix_P[i][4] / Math.sqrt(delta);
                    inverse_matrix_P[i][4] = matrix_P[i][4];
                }
                /* Column 5 */
                delta = 0;
                for (i = 1; i <= 6; i++) {
                    if (i == 5)
                        matrix_P[i][5] = 1.0 - bvector[5] * matrix_P[i][1] - matrix_P[5][2] * matrix_P[i][2] - matrix_P[5][3] * matrix_P[i][3] - matrix_P[5][4] * matrix_P[i][4];
                    else
                        matrix_P[i][5] = -bvector[5] * matrix_P[i][1] - matrix_P[5][2] * matrix_P[i][2] - matrix_P[5][3] * matrix_P[i][3] - matrix_P[5][4] * matrix_P[i][4];
                    delta = delta + matrix_P[i][5] * matrix_P[i][5];
                }
                for (i = 1; i <= 6; i++) {
                    matrix_P[i][5] = matrix_P[i][5] / Math.sqrt(delta);
                    inverse_matrix_P[i][5] = matrix_P[i][5];
                }
                /* Column 6 */
                delta = 0;
                for (i = 1; i <= 6; i++) {
                    if (i == 6)
                        matrix_P[i][6] = 1.0 - bvector[6] * matrix_P[i][1] - matrix_P[6][2] * matrix_P[i][2] - matrix_P[6][3] * matrix_P[i][3] - matrix_P[6][4] * matrix_P[i][4] - matrix_P[6][5] * matrix_P[i][5];
                    else
                        matrix_P[i][6] = -bvector[6] * matrix_P[i][1] - matrix_P[6][2] * matrix_P[i][2] - matrix_P[6][3] * matrix_P[i][3] - matrix_P[6][4] * matrix_P[i][4] - matrix_P[6][5] * matrix_P[i][5];
                    delta = delta + matrix_P[i][6] * matrix_P[i][6];
                }
                for (i = 1; i <= 6; i++) {
                    matrix_P[i][6] = matrix_P[i][6] / Math.sqrt(delta);
                    inverse_matrix_P[i][6] = matrix_P[i][6];
                }

                invertNxn(6, inverse_matrix_P);
                for (i = 1; i <= 6; i++) {
                    converted_corrections[i] = inverse_matrix_P[i][1] * corrections[1] + inverse_matrix_P[i][2] * corrections[2] + inverse_matrix_P[i][3] * corrections[3] + inverse_matrix_P[i][4] * corrections[4] + inverse_matrix_P[i][5] * corrections[5] + inverse_matrix_P[i][6] * corrections[6];
                }
                converted_corrections[1] = 0.0;
                for (i = 1; i <= 6; i++) {
                    corrections[i] = matrix_P[i][1] * converted_corrections[1] + matrix_P[i][2] * converted_corrections[2] + matrix_P[i][3] * converted_corrections[3] + matrix_P[i][4] * converted_corrections[4] + matrix_P[i][5] * converted_corrections[5] + matrix_P[i][6] * converted_corrections[6];
                }
            }

            /* Update the epoch state vector */
            for (i = 1; i <= 3; i++) {
                epoch_r[i] = epoch_r[i] + corrections[i];
                epoch_rprime[i] = epoch_rprime[i] + corrections[i + 3];
            }

            /* Recalculate the residuals using the updated epoch state vector */
            getResiduals(residuals, rms_residuals);
            old_rms_optical = rms_optical;
            old_rms_delay = rms_delay;
            old_rms_doppler = rms_doppler;
            rms_optical = rms_residuals[1];
            rms_delay = rms_residuals[2];
            rms_doppler = rms_residuals[3];

            System.out.println("old_rms_optical = " + old_rms_optical);
            System.out.println("old_rms_delay = " + old_rms_delay);
            System.out.println("old_rms_doppler = " + old_rms_doppler);

            /* Calculate the asteroid's absolute magnitude, slope parameter, and radius.  This last is especially important when radar observations are used, to account for the "bounce point".  */
            solveForAbsoluteMagnitude();

            /* Based on the RMS residuals, evaluate the convergence and divergence conditions */
            convergence = true;
            if ((Math.abs(old_rms_optical - rms_optical) > (convergence_factor * old_rms_optical)) || (Math.abs(old_rms_delay - rms_delay) > (convergence_factor * old_rms_delay)) || (Math.abs(old_rms_doppler - rms_doppler) > (convergence_factor * old_rms_doppler)))
                convergence = false;
            if ((rms_optical >= 1.2 * old_rms_optical) && (rms_delay >= 1.2 * old_rms_delay) && (rms_doppler >= 1.2 * old_rms_doppler))
                divergence = true;
            if ((convergence) && (!edit) && ((chi_testing) || (residual_testing))) {
                edit = true;
                convergence = false;
            }
            /* We may not have yet converged.  However, if the residuals have been reduced, shouldn't we keep this state vector and covariance matrix as the initial data in case the next iteration diverges? */
            if ((rms_optical <= old_rms_optical) && (rms_delay <= old_rms_delay) && (rms_doppler <= old_rms_doppler)) {
                for (i = 1; i <= 3; i++) {
                    initial_r[i] = epoch_r[i];
                    initial_rprime[i] = epoch_rprime[i];
                    initial_rms_residuals[i] = rms_residuals[i];
                }
                for (i = 1; i <= 8; i++) {
                    for (j = 1; j <= 8; j++)
                        initial_big_p[i][j] = big_p[i][j];
                }
                index = 2 * number_of_optical_observations + number_of_delay_observations + number_of_doppler_observations;
                for (i = 1; i <= index; i++)
                    initial_residuals[i] = residuals[i];
            }

            System.out.println("convergence = " + convergence);
            System.out.println("divergence = " + divergence);
            /* The loop is complete.  If the convergence or divergence condition is satisfied, we will exit; if not, we will go around again */

        }

        if (divergence == true) {
            /* Return initial orbit and residuals, and set epoch_r[0] = 6.0. */
            for (i = 1; i <= 3; i++) {
                epoch_r[i] = initial_r[i];
                epoch_rprime[i] = initial_rprime[i];
                rms_residuals[i] = initial_rms_residuals[i];
            }
            epoch_r[0] = 6.0;
            for (i = 1; i <= 8; i++) {
                for (j = 1; j <= 8; j++)
                    big_p[i][j] = initial_big_p[i][j];
            }
            index = 2 * number_of_optical_observations + number_of_delay_observations + number_of_doppler_observations;
            for (i = 1; i <= index; i++)
                residuals[i] = initial_residuals[i];
        } else if (divergence == false)
            epoch_r[0] = 0.0;

    }

    public void mpGetCovarianceEigenvector(double[] eigenvector, double[][] covariance_matrix) {

        /*
          Procedure to calculate the dominant eigenvalue and (unit) eigenvector corresponding to the weak direction of the covariance matrix using the Power Method.  Note: The eigenvalue is returned as eigenvector[0].
          Algorithm taken from Richard L. Burden and J. Douglas Faires, "Numerical Analysis", 3rd edition, pp. 454-455.
        */

        int k = 0, counter = 0;
        double[] y = new double[7];
        double error = 1.0, normalizer = 0;

        for (k = 1; k <= 6; k++)
            eigenvector[k] = 1.0;

        while ((counter < 100) && (error > 1.0E-8)) {
            for (k = 1; k <= 6; k++)
                y[k] = covariance_matrix[k][1] * eigenvector[1] + covariance_matrix[k][2] * eigenvector[2] + covariance_matrix[k][3] * eigenvector[3] + covariance_matrix[k][4] * eigenvector[4] + covariance_matrix[k][5] * eigenvector[5] + covariance_matrix[k][6] * eigenvector[6];

            normalizer = 0;
            for (k = 1; k <= 6; k++)
                if (Math.abs(y[k]) > normalizer) normalizer = Math.abs(y[k]);

            error = 0;
            for (k = 1; k <= 6; k++) {
                if (Math.abs(eigenvector[k] - y[k] / normalizer) > error)
                    error = Math.abs(eigenvector[k] - y[k] / normalizer);
                eigenvector[k] = y[k] / normalizer;
            }

            counter = counter + 1;

        }

        eigenvector[0] = normalizer;
        normalizer = Math.sqrt(Math.pow(eigenvector[1], 2.0) + Math.pow(eigenvector[2], 2.0) + Math.pow(eigenvector[3], 2.0) + Math.pow(eigenvector[4], 2.0) + Math.pow(eigenvector[5], 2.0) + Math.pow(eigenvector[6], 2.0));
        for (k = 1; k <= 6; k++) {
            eigenvector[k] = eigenvector[k] / normalizer;
        }

    }

    double covarianceSecondEigenvalue(double[][] covariance) {
        /* Procedure to calculate the second largest eigenvalue of the covariance matrix.  This corresponds to the width of the uncertainty region, and can be used to determine whether a collision is possible.
               Algorithm taken from Howard Anton, "Elementary Linear Algebra", 4th edition, pg. 348.
          */

        int i = 0, j = 0, k = 0;

        double[] first_eigenvector = new double[7];
        double[] eigenvector = new double[7];
        double[][] vvt = new double[7][7];
        double[][] deflated_cov = new double[7][7];

        /* Determine the largest eigenvalue (and the corresponding eigenvector) of the covariance matrix */
        mpGetCovarianceEigenvector(first_eigenvector, covariance);

        /* Create the deflated covariance matrix */
        for (i = 1; i <= 6; i++) {
            for (j = 1; j <= 6; j++)
                vvt[i][j] = first_eigenvector[i] * first_eigenvector[j];
        }
        for (i = 1; i <= 6; i++) {
            for (j = 1; j <= 6; j++)
                deflated_cov[i][j] = covariance[i][j] - first_eigenvector[0] * vvt[i][j];
        }

        /* Determine the largest eigenvalue (and the corresponding eigenvector) of the propogated covariance matrix */
        mpGetCovarianceEigenvector(eigenvector, deflated_cov);

        return eigenvector[0];

    }

    public double getRelativeMOID(double[] big_asteroid_elements, double[] little_asteroid_elements) {
        /* Procedure to estimate the minimum orbital intersection distance between two minor planets. */
        /* The algorithm is due to Sitarski, Acta Astronica, 1968 (Vol 18 pp. 171-181) */

        double[][] solution = new double[800][3];
        double[][] big_h = new double[4][4];
        double[][] little_h = new double[4][4];
        double[][] inter1 = new double[4][4];
        double[][] inter2 = new double[4][4];
        double[][] inter3 = new double[4][4];
        double[] big_u = new double[4];
        double[] little_u = new double[4];
        double[] big_pos = new double[4];
        double[] little_pos = new double[4];

        int i = 0, j = 0, big_v_counter = 0, little_v_counter = 0, solution_counter = 0, k = 0;

        double moid = 1000.0, big_v = 0, little_v = 0, big_r = 0, big_p = 0, little_p = 0, big_x = 0, big_y = 0, big_m = 0, big_n = 0, big_px = 0, big_py = 0, big_pz = 0, big_qx = 0, big_qy = 0, big_qz = 0, little_px = 0, little_py = 0, little_pz = 0, little_qx = 0, little_qy = 0, little_qz = 0, big_k = 0, big_l = 0, s = 0, t = 0, w = 0, little_m = 0, little_l = 0, solution_little_v1 = 0, solution_little_v2 = 0, old_equation_value_1 = 0, old_equation_value_2 = 0, equation_value_1 = 0, equation_value_2 = 0, little_x = 0, little_y = 0, little_r = 0, old_big_v = 0, top = 0, bottom = 0, middle = 0, bisection_equation_value_1 = 0, bisection_equation_value_2 = 0, distance = 0, longitude1 = 0, top_sign = 0, partial_big_v = 0, partial_little_v = 0, mixed_partial = 0;

        /* Create the quantities needed to evaluate the key equation 11 */
        big_p = big_asteroid_elements[8] * (1.0 + big_asteroid_elements[2]);
        little_p = little_asteroid_elements[8] * (1.0 + little_asteroid_elements[2]);
        big_px = Math.cos(big_asteroid_elements[4]);
        big_py = Math.sin(big_asteroid_elements[4]) * Math.cos(big_asteroid_elements[3]);
        big_pz = Math.sin(big_asteroid_elements[4]) * Math.sin(big_asteroid_elements[3]);
        big_qx = -Math.sin(big_asteroid_elements[4]);
        big_qy = Math.cos(big_asteroid_elements[4]) * Math.cos(big_asteroid_elements[3]);
        big_qz = Math.cos(big_asteroid_elements[4]) * Math.sin(big_asteroid_elements[3]);
        little_px = Math.cos(little_asteroid_elements[4]) * Math.cos(little_asteroid_elements[5] - big_asteroid_elements[5]) - Math.sin(little_asteroid_elements[4]) * Math.cos(little_asteroid_elements[3]) * Math.sin(little_asteroid_elements[5] - big_asteroid_elements[5]);
        little_py = Math.cos(little_asteroid_elements[4]) * Math.sin(little_asteroid_elements[5] - big_asteroid_elements[5]) + Math.sin(little_asteroid_elements[4]) * Math.cos(little_asteroid_elements[3]) * Math.cos(little_asteroid_elements[5] - big_asteroid_elements[5]);
        little_pz = Math.sin(little_asteroid_elements[4]) * Math.sin(little_asteroid_elements[3]);
        little_qx = -Math.sin(little_asteroid_elements[4]) * Math.cos(little_asteroid_elements[5] - big_asteroid_elements[5]) - Math.cos(little_asteroid_elements[4]) * Math.cos(little_asteroid_elements[3]) * Math.sin(little_asteroid_elements[5] - big_asteroid_elements[5]);
        little_qy = -Math.sin(little_asteroid_elements[4]) * Math.sin(little_asteroid_elements[5] - big_asteroid_elements[5]) + Math.cos(little_asteroid_elements[4]) * Math.cos(little_asteroid_elements[3]) * Math.cos(little_asteroid_elements[5] - big_asteroid_elements[5]);
        little_qz = Math.cos(little_asteroid_elements[4]) * Math.sin(little_asteroid_elements[3]);
        big_k = big_px * little_px + big_py * little_py + big_pz * little_pz;
        big_l = big_qx * little_px + big_qy * little_py + big_qz * little_pz;
        big_m = big_px * little_qx + big_py * little_qy + big_pz * little_qz;
        big_n = big_qx * little_qx + big_qy * little_qy + big_qz * little_qz;
        /* Calculate cracovians big_h and little_h for use in evaluating the distances */
        inter1[1][1] = Math.cos(little_asteroid_elements[5] - big_asteroid_elements[5]);
        inter1[1][2] = Math.sin(little_asteroid_elements[5] - big_asteroid_elements[5]);
        inter1[1][3] = 0.0;
        inter1[2][1] = -Math.sin(little_asteroid_elements[5] - big_asteroid_elements[5]);
        inter1[2][2] = Math.cos(little_asteroid_elements[5] - big_asteroid_elements[5]);
        inter1[2][3] = 0.0;
        inter1[3][1] = 0.0;
        inter1[3][2] = 0.0;
        inter1[3][3] = 1.0;
        inter2[1][1] = 1.0;
        inter2[1][2] = 0.0;
        inter2[1][3] = 0.0;
        inter2[2][1] = 0.0;
        inter2[2][2] = Math.cos(little_asteroid_elements[3]);
        inter2[2][3] = -Math.sin(little_asteroid_elements[3]);
        inter2[3][1] = 0.0;
        inter2[3][2] = Math.sin(little_asteroid_elements[3]);
        inter2[3][3] = Math.cos(little_asteroid_elements[3]);
        /* Multiply the cracovians, using cracovian logic, rather than matrix multiplication */
        for (i = 1; i <= 3; i++) {
            for (j = 1; j <= 3; j++) {
                inter3[i][j] = 0.0;
                for (k = 1; k <= 3; k++)
                    inter3[i][j] = inter3[i][j] + inter1[k][j] * inter2[k][i];
            }
        }
        inter1[1][1] = Math.cos(little_asteroid_elements[4]);
        inter1[1][2] = -Math.sin(little_asteroid_elements[4]);
        inter1[1][3] = 0.0;
        inter1[2][1] = Math.sin(little_asteroid_elements[4]);
        inter1[2][2] = Math.cos(little_asteroid_elements[4]);
        inter1[2][3] = 0.0;
        inter1[3][1] = 0.0;
        inter1[3][2] = 0.0;
        inter1[3][3] = 1.0;
        /* Multiply the cracovians, using cracovian logic, rather than matrix multiplication */
        for (i = 1; i <= 3; i++) {
            for (j = 1; j <= 3; j++) {
                little_h[i][j] = 0.0;
                for (k = 1; k <= 3; k++)
                    little_h[i][j] = little_h[i][j] + inter3[k][j] * inter1[k][i];
            }
        }
        inter2[1][1] = 1.0;
        inter2[1][2] = 0.0;
        inter2[1][3] = 0.0;
        inter2[2][1] = 0.0;
        inter2[2][2] = Math.cos(big_asteroid_elements[3]);
        inter2[2][3] = Math.sin(big_asteroid_elements[3]);
        inter2[3][1] = 0.0;
        inter2[3][2] = -Math.sin(big_asteroid_elements[3]);
        inter2[3][3] = Math.cos(big_asteroid_elements[3]);
        inter1[1][1] = Math.cos(big_asteroid_elements[4]);
        inter1[1][2] = -Math.sin(big_asteroid_elements[4]);
        inter1[1][3] = 0.0;
        inter1[2][1] = Math.sin(big_asteroid_elements[4]);
        inter1[2][2] = Math.cos(big_asteroid_elements[4]);
        inter1[2][3] = 0.0;
        inter1[3][1] = 0.0;
        inter1[3][2] = 0.0;
        inter1[3][3] = 1.0;
        /* Multiply the cracovians, using cracovian logic, rather than matrix multiplication */
        for (i = 1; i <= 3; i++) {
            for (j = 1; j <= 3; j++) {
                big_h[i][j] = 0.0;
                for (k = 1; k <= 3; k++)
                    big_h[i][j] = big_h[i][j] + inter2[k][j] * inter1[k][i];
            }
        }

        big_v = -181 * Math.PI / 180.0;
        for (big_v_counter = -180; big_v_counter <= 180; big_v_counter++) {
            old_big_v = big_v;
            big_v = big_v_counter * Math.PI / 180.0;
            big_r = big_p / (1.0 + big_asteroid_elements[2] * Math.cos(big_v));
            big_x = big_r * Math.cos(big_v);
            big_y = big_r * Math.sin(big_v);
            s = big_asteroid_elements[2] * big_r * big_y / little_p;
            t = big_m * big_y - big_n * (big_asteroid_elements[2] * big_r + big_x);
            w = little_asteroid_elements[2] * s + big_k * big_y - big_l * (big_asteroid_elements[2] * big_r + big_x);
            little_m = t * t + w * w;
            little_l = little_m - s * s;
            solution_little_v1 = Math.atan2((-t * s + w * Math.sqrt(little_l)) / little_m, (-w * s - t * Math.sqrt(little_l)) / little_m);
            little_r = little_p / (1.0 + little_asteroid_elements[2] * Math.cos(solution_little_v1));
            little_x = little_r * Math.cos(solution_little_v1);
            little_y = little_r * Math.sin(solution_little_v1);
            equation_value_1 = (little_r / little_p) * (little_asteroid_elements[2] * little_r * little_y + little_y * (big_k * big_x + big_l * big_y) - (little_asteroid_elements[2] * little_r + little_x) * (big_m * big_x + big_n * big_y));
            solution_little_v2 = Math.atan2((-t * s - w * Math.sqrt(little_l)) / little_m, (-w * s + t * Math.sqrt(little_l)) / little_m);
            little_r = little_p / (1.0 + little_asteroid_elements[2] * Math.cos(solution_little_v2));
            little_x = little_r * Math.cos(solution_little_v2);
            little_y = little_r * Math.sin(solution_little_v2);
            equation_value_2 = (little_r / little_p) * (little_asteroid_elements[2] * little_r * little_y + little_y * (big_k * big_x + big_l * big_y) - (little_asteroid_elements[2] * little_r + little_x) * (big_m * big_x + big_n * big_y));
            if ((big_v_counter > -180) && (((old_equation_value_1 <= 0.0) && (equation_value_1 >= 0.0)) || ((old_equation_value_1 >= 0.0) && (equation_value_1 <= 0.0)))) {
                top = big_v;
                bottom = old_big_v;
                if (equation_value_1 > 0)
                    top_sign = 1.0;
                else
                    top_sign = -1.0;
                while (Math.abs(top - bottom) > 1.0e-7) {
                    middle = (top + bottom) / 2.0;
                    big_r = big_p / (1.0 + big_asteroid_elements[2] * Math.cos(middle));
                    big_x = big_r * Math.cos(middle);
                    big_y = big_r * Math.sin(middle);
                    s = big_asteroid_elements[2] * big_r * big_y / little_p;
                    t = big_m * big_y - big_n * (big_asteroid_elements[2] * big_r + big_x);
                    w = little_asteroid_elements[2] * s + big_k * big_y - big_l * (big_asteroid_elements[2] * big_r + big_x);
                    little_m = t * t + w * w;
                    little_l = little_m - s * s;
                    solution_little_v1 = Math.atan2((-t * s + w * Math.sqrt(little_l)) / little_m, (-w * s - t * Math.sqrt(little_l)) / little_m);
                    little_r = little_p / (1.0 + little_asteroid_elements[2] * Math.cos(solution_little_v1));
                    little_x = little_r * Math.cos(solution_little_v1);
                    little_y = little_r * Math.sin(solution_little_v1);
                    bisection_equation_value_1 = (little_r / little_p) * (little_asteroid_elements[2] * little_r * little_y + little_y * (big_k * big_x + big_l * big_y) - (little_asteroid_elements[2] * little_r + little_x) * (big_m * big_x + big_n * big_y));
                    if ((bisection_equation_value_1 >= 0) && (top_sign > 0))
                        top = middle;
                    else if ((bisection_equation_value_1 <= 0) && (top_sign > 0))
                        bottom = middle;
                    else if ((bisection_equation_value_1 >= 0) && (top_sign < 0))
                        bottom = middle;
                    else if ((bisection_equation_value_1 <= 0) && (top_sign < 0))
                        top = middle;
                }
                solution_counter++;
                solution[solution_counter][1] = middle;
                solution[solution_counter][2] = solution_little_v1;
                partial_big_v = (big_r / big_p) * ((big_asteroid_elements[2] * big_r * big_r / big_p) * (big_asteroid_elements[2] * big_r + big_x) + big_x * (big_k * little_x + big_m * little_y) + big_y * (big_l * little_x + big_n * little_y));
                partial_little_v = (little_r / little_p) * ((little_asteroid_elements[2] * little_r * little_r / little_p) * (little_asteroid_elements[2] * little_r + little_x) + little_x * (big_k * big_x + big_l * big_y) + little_y * (big_m * big_x + big_n * big_y));
                mixed_partial = (big_r / big_p) * (little_r / little_p) * ((little_asteroid_elements[2] * little_r + little_x) * (big_n * (big_asteroid_elements[2] * big_r + big_x) - big_m * big_y) - little_y * (big_l * (big_asteroid_elements[2] * big_r + big_x) - big_k * big_y));
            }
            if ((big_v_counter > -180) && (((old_equation_value_2 <= 0.0) && (equation_value_2 >= 0.0)) || ((old_equation_value_2 >= 0.0) && (equation_value_2 <= 0.0)))) {
                top = big_v;
                bottom = old_big_v;
                if (equation_value_2 > 0)
                    top_sign = 1.0;
                else
                    top_sign = -1.0;
                while (Math.abs(top - bottom) > 1.0e-7) {
                    middle = (top + bottom) / 2.0;
                    big_r = big_p / (1.0 + big_asteroid_elements[2] * Math.cos(middle));
                    big_x = big_r * Math.cos(middle);
                    big_y = big_r * Math.sin(middle);
                    s = big_asteroid_elements[2] * big_r * big_y / little_p;
                    t = big_m * big_y - big_n * (big_asteroid_elements[2] * big_r + big_x);
                    w = little_asteroid_elements[2] * s + big_k * big_y - big_l * (big_asteroid_elements[2] * big_r + big_x);
                    little_m = t * t + w * w;
                    little_l = little_m - s * s;
                    solution_little_v2 = Math.atan2((-t * s - w * Math.sqrt(little_l)) / little_m, (-w * s + t * Math.sqrt(little_l)) / little_m);
                    little_r = little_p / (1.0 + little_asteroid_elements[2] * Math.cos(solution_little_v2));
                    little_x = little_r * Math.cos(solution_little_v2);
                    little_y = little_r * Math.sin(solution_little_v2);
                    bisection_equation_value_2 = (little_r / little_p) * (little_asteroid_elements[2] * little_r * little_y + little_y * (big_k * big_x + big_l * big_y) - (little_asteroid_elements[2] * little_r + little_x) * (big_m * big_x + big_n * big_y));
                    if ((bisection_equation_value_2 >= 0) && (top_sign > 0))
                        top = middle;
                    else if ((bisection_equation_value_2 <= 0) && (top_sign > 0))
                        bottom = middle;
                    else if ((bisection_equation_value_2 >= 0) && (top_sign < 0))
                        bottom = middle;
                    else if ((bisection_equation_value_2 <= 0) && (top_sign < 0))
                        top = middle;
                }
                solution_counter++;
                solution[solution_counter][1] = middle;
                solution[solution_counter][2] = solution_little_v2;
                partial_big_v = (big_r / big_p) * ((big_asteroid_elements[2] * big_r * big_r / big_p) * (big_asteroid_elements[2] * big_r + big_x) + big_x * (big_k * little_x + big_m * little_y) + big_y * (big_l * little_x + big_n * little_y));
                partial_little_v = (little_r / little_p) * ((little_asteroid_elements[2] * little_r * little_r / little_p) * (little_asteroid_elements[2] * little_r + little_x) + little_x * (big_k * big_x + big_l * big_y) + little_y * (big_m * big_x + big_n * big_y));
                mixed_partial = (big_r / big_p) * (little_r / little_p) * ((little_asteroid_elements[2] * little_r + little_x) * (big_n * (big_asteroid_elements[2] * big_r + big_x) - big_m * big_y) - little_y * (big_l * (big_asteroid_elements[2] * big_r + big_x) - big_k * big_y));
            }
            old_equation_value_1 = equation_value_1;
            old_equation_value_2 = equation_value_2;
        }

        /* We now have several distance minima; determine which is smallest by direct calculation. */
        for (i = 1; i <= solution_counter; i++) {
            big_r = big_p / (1.0 + big_asteroid_elements[2] * Math.cos(solution[i][1]));
            little_r = little_p / (1.0 + little_asteroid_elements[2] * Math.cos(solution[i][2]));
            big_u[1] = big_r * Math.cos(solution[i][1]);
            big_u[2] = big_r * Math.sin(solution[i][1]);
            big_u[3] = 0.0;
            little_u[1] = little_r * Math.cos(solution[i][2]);
            little_u[2] = little_r * Math.sin(solution[i][2]);
            little_u[3] = 0.0;
            big_pos[1] = big_u[1] * big_h[1][1] + big_u[2] * big_h[2][1] + big_u[3] * big_h[3][1];
            big_pos[2] = big_u[1] * big_h[1][2] + big_u[2] * big_h[2][2] + big_u[3] * big_h[3][2];
            big_pos[3] = big_u[1] * big_h[1][3] + big_u[2] * big_h[2][3] + big_u[3] * big_h[3][3];
            little_pos[1] = little_u[1] * little_h[1][1] + little_u[2] * little_h[2][1] + little_u[3] * little_h[3][1];
            little_pos[2] = little_u[1] * little_h[1][2] + little_u[2] * little_h[2][2] + little_u[3] * little_h[3][2];
            little_pos[3] = little_u[1] * little_h[1][3] + little_u[2] * little_h[2][3] + little_u[3] * little_h[3][3];
            distance = Math.sqrt(Math.pow((big_pos[1] - little_pos[1]), 2.0) + Math.pow((big_pos[2] - little_pos[2]), 2.0) + Math.pow((big_pos[3] - little_pos[3]), 2.0));
            if (distance < moid) moid = distance;
        }

        return moid;

    }

}
