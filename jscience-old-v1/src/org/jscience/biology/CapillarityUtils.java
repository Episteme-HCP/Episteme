package org.jscience.biology;

/**
 * The CapillarityUtils class provides useful vascular biology related
 * methods.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//also see org.jscience.physics.fluids.Electrophoresis 
public final class CapillarityUtils extends Object {
    //http://en.wikipedia.org/wiki/Capillary_wave
    /**
     * DOCUMENT ME!
     *
     * @param gamma DOCUMENT ME!
     * @param rho DOCUMENT ME!
     * @param rhoprime DOCUMENT ME!
     * @param k DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getDispertionFrequency(double gamma, double rho,
        double rhoprime, double k) {
        return Math.sqrt((k * k * k * gamma) / (rho + rhoprime));
    }

    //The waves with large wavelengths are generally also affected by gravity and are then called gravity-capillary waves. Their dispersion relation reads, for infinite depth of the two fluids,
    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param gamma DOCUMENT ME!
     * @param rho DOCUMENT ME!
     * @param rhoprime DOCUMENT ME!
     * @param k DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getLargeWavesDispertionFrequency(double g, double gamma,
        double rho, double rhoprime, double k) {
        return Math.sqrt(((g * (rho - rhoprime) * k) / rho) +
            ((k * k * k * gamma) / (rho + rhoprime)));
    }

    //http://en.wikipedia.org/wiki/Washburn%27s_equation
    /**
     * DOCUMENT ME!
     *
     * @param eta DOCUMENT ME!
     * @param gamma DOCUMENT ME!
     * @param diameter DOCUMENT ME!
     * @param t DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getWashburnPenetrationDistance(double eta, double gamma,
        double diameter, double t) {
        return Math.sqrt((gamma * diameter * t) / (4 * eta));
    }

    //http://en.wikipedia.org/wiki/Capillary_action
    /**
     * DOCUMENT ME!
     *
     * @param tension DOCUMENT ME!
     * @param theta DOCUMENT ME!
     * @param rho DOCUMENT ME!
     * @param g DOCUMENT ME!
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMotionHeight(double tension, double theta, double rho,
        double g, double r) {
        return Math.sqrt((2 * tension * Math.cos(theta)) / (rho * g * r));
    }
}
