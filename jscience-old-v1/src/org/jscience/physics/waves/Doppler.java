package org.jscience.physics.waves;

import org.jscience.physics.PhysicsConstants;


/**
 * The class defines doppler laws.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public final class Doppler extends Object {
/**
     * Creates a new Doppler object.
     */
    private Doppler() {
    }

    //Doppler
    /**
     * DOCUMENT ME!
     *
     * @param sourceFrequency DOCUMENT ME!
     * @param sourceVelocity DOCUMENT ME!
     * @param waveVelocity DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getDopplerFrequency(double sourceFrequency,
        double sourceVelocity, double waveVelocity) {
        return sourceFrequency * (waveVelocity / (waveVelocity -
        sourceVelocity));
    }

    //relativistic Doppler effect is change in the observed frequency of light due to the relative motion of source and observer when taking into account the Special Theory of Relativity.
    /**
     * DOCUMENT ME!
     *
     * @param sourceFrequency DOCUMENT ME!
     * @param relativeVelocity DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getRelativisticDopplerFrequency(
        double sourceFrequency, double relativeVelocity) {
        return sourceFrequency * Math.sqrt((1 -
            (relativeVelocity / PhysicsConstants.SPEED_OF_LIGHT)) / (1 +
            (relativeVelocity / PhysicsConstants.SPEED_OF_LIGHT)));
    }
}
