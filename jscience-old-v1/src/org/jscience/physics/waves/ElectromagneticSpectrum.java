package org.jscience.physics.waves;

import org.jscience.mathematics.algebraic.numbers.Double;
import org.jscience.mathematics.analysis.Interval;

import org.jscience.physics.PhysicsConstants;


/**
 * The class defines constants and methods for electromagnetic spectrum
 * wavelength.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public final class ElectromagneticSpectrum extends Object {
    //http://en.wikipedia.org/wiki/Wavelength
    //http://en.wikipedia.org/wiki/Electromagnetic_spectrum
    //in meters
    /** DOCUMENT ME! */
    public final static Interval GAMMA_RAYS = new Interval(new Double(
                Double.MIN_VALUE), new Double(1.e-11)); //and below, also named cosmic rays (I think)

    /** DOCUMENT ME! */
    public final static Interval X_RAYS = new Interval(new Double(1.e-11),
            new Double(1e-8));

    /** DOCUMENT ME! */
    public final static Interval ULTRAVIOLET = new Interval(new Double(1e-8),
            new Double(4e-7));

    /** DOCUMENT ME! */
    public final static Interval VIOLET = new Interval(new Double(4e-7),
            new Double(4.5e-7)); //visible by human eye

    /** DOCUMENT ME! */
    public final static Interval BLUE = new Interval(new Double(4.5e-7),
            new Double(5e-7)); //visible by human eye

    /** DOCUMENT ME! */
    public final static Interval GREEN = new Interval(new Double(5e-7),
            new Double(5.5e-7)); //visible by human eye

    /** DOCUMENT ME! */
    public final static Interval YELLOW = new Interval(new Double(5.5e-7),
            new Double(6.0e-7)); //visible by human eye

    /** DOCUMENT ME! */
    public final static Interval ORANGE = new Interval(new Double(6.0e-7),
            new Double(6.2e-7)); //visible by human eye

    /** DOCUMENT ME! */
    public final static Interval RED = new Interval(new Double(6.2e-7),
            new Double(7e-7)); //visible by human eye

    // a rainbow is a multicoloured arc with red on the outside and violet on the inside; the full sequence is red, orange, yellow, green, blue, indigo and violet.
    /** DOCUMENT ME! */
    public final static Interval VISIBLE_LIGHT = new Interval(new Double(4e-7),
            new Double(7e-7)); //visible by human eye

    /** DOCUMENT ME! */
    public final static Interval INFRARED = new Interval(new Double(7e-7),
            new Double(1e-3));

    /** DOCUMENT ME! */
    public final static Interval MICROWAVES = new Interval(new Double(1e-3),
            new Double(3e-1));

    /** DOCUMENT ME! */
    public final static Interval RADIO = new Interval(new Double(3e-1),
            new Double(Double.MAX_VALUE)); //and above

    /**
     * DOCUMENT ME!
     *
     * @param length DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getFrequency(double length) {
        return PhysicsConstants.SPEED_OF_LIGHT / length;
    }

    /**
     * DOCUMENT ME!
     *
     * @param frequency DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getWaveLength(double frequency) {
        return PhysicsConstants.SPEED_OF_LIGHT / frequency;
    }

    /**
     * DOCUMENT ME!
     *
     * @param frequency DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getEnergy(double frequency) {
        //e=h u
        return PhysicsConstants.PLANCK * frequency;
    }
}
