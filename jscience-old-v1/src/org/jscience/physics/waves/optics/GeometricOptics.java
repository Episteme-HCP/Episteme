package org.jscience.physics.waves.optics;

/**
 * The class defines several methods to describe geometric optics.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public class GeometricOptics extends Object {
/**
     * Creates a new GeometricOptics object.
     */
    public GeometricOptics() {
    }

    //reflection law of Snell-Descartes
    /**
     * DOCUMENT ME!
     *
     * @param angle DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getReflectionAngle(double angle) {
        return angle;
    }

    //refraction law of Snell-Descartes
    /**
     * DOCUMENT ME!
     *
     * @param angle DOCUMENT ME!
     * @param index1 DOCUMENT ME!
     * @param index2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getRefractionAngle(double angle, double index1, double index2) {
        return Math.asin((index1 * Math.sin(angle)) / index2);
    }

    //we could also introduce several methods on prisms and lens should I know what could be useful
    /**
     * DOCUMENT ME!
     *
     * @param sourceIntensity DOCUMENT ME!
     * @param length DOCUMENT ME!
     * @param concentration DOCUMENT ME!
     * @param absorption DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getResultingIntensity(double sourceIntensity, double length,
        double concentration, double absorption) {
        return sourceIntensity * Math.exp(length * concentration * absorption);
    }

    //Beer Law
    /**
     * DOCUMENT ME!
     *
     * @param transmittance DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getAbsorbance(double transmittance) {
        //return 2 - Math.log10(100 * transmittance);
        return 2 - (Math.log(100 * transmittance) / Math.log(10));
    }

    //Fresnel Law
    /**
     * DOCUMENT ME!
     *
     * @param indicesRatio DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getReflectionLoss(double indicesRatio) {
        return Math.pow((indicesRatio - 1), 2) / Math.pow((indicesRatio + 1), 2);
    }

    //Rayleigh scattering
    /**
     * DOCUMENT ME!
     *
     * @param nScatters DOCUMENT ME!
     * @param diameter DOCUMENT ME!
     * @param refractionIndex DOCUMENT ME!
     * @param wavelenght DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getRayleighScattering(int nScatters, float diameter,
        double refractionIndex, double wavelenght) {
        return ((2 * Math.pow(Math.PI, 5)) / 3 * nScatters * Math.pow(((refractionIndex * refractionIndex) -
            1) / ((refractionIndex * refractionIndex) + 2), 2) * Math.pow(diameter,
            6)) / Math.pow(wavelenght, 4);
    }
}
