package org.jscience.astronomy;

import org.jscience.mathematics.MathConstants;


/**
 * The AstronomyConstants class provides several useful constatnts for
 * astronomy.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//for time, see http://en.wikipedia.org/wiki/Time
//also pay attention to http://en.wikipedia.org/wiki/Ephemeris_time
public final class AstronomyConstants extends Object {
    /** Length of an Astronomical Unit, in meters. */
    public final static double AU = 149597870691D;

    /** Length of a parsec, in meters. */
    public final static double PARSEC = 3.0856775807E+16;

    /** Length of a (Julian) light year (defined), in meters. */
    public final static double LIGHT_YEAR = 9460730472580800D;

    /** A earth year in days. */
    public static final double EARTH_YEAR = 365.242190;

    /**
     * A earth sideral year in (earth) days (at the epoch J2000 = 1
     * Jan. 2000 12h Terrestrial Time).
     */
    public static final double SIDERAL_YEAR = 365.256363051;

    /**
     * A earth tropical year in (earth) days, or the period for the
     * Earth to complete one revolution with respect to the framework provided
     * by the intersection of the ecliptic (the plane of the orbit of the
     * Earth) and the plane of the equator (the plane perpendicular to the
     * rotation axis of the Earth).
     */
    public static final double TROPICAL_YEAR = 365.242190;

    /** Length of a Julian year (defined) in (earth) days. */
    public final static double JULIAN_YEAR = 365.25;

    /** Length of sideral day in seconds. */
    public final static double SIDERAL_DAY = 86164.09;

    /**
     * Length of the standard minute in seconds (duration of
     * 9,192,631,770 cycles of microwave light absorbed or emitted by the
     * hyperfine transition of cesium-133 atoms in their ground state
     * undisturbed by external fields).
     */
    public final static double MINUTE = 60;

    /** Length of the standard hour in seconds. */
    public final static double HOUR = 60 * MINUTE;

    /** Length of day in seconds. */
    public final static double EARTH_DAY = 24 * HOUR;

    /**
     * Gaussian Gravitational Constant in A3/2S-1/2D-1 with length A:
     * astronomical unit (the mean axis of the orbit of the Earth around the
     * Sun); time D: mean solar day (the mean rotation period of the Earth
     * around its axis, with respect to the Sun); mass S: the mass of the Sun.
     */
    public final static double GAUSSIAN_GRAVITATIONAL_CONSTANT = 0.01720209895;

    /** Gaussian year in (earth) days (defined). */
    public final static double GAUSSIAN_YEAR = MathConstants.TWO_PI / GAUSSIAN_GRAVITATIONAL_CONSTANT;
}
