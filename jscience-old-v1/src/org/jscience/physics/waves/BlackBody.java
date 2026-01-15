package org.jscience.physics.waves;

import org.jscience.chemistry.ChemistryConstants;

import org.jscience.physics.PhysicsConstants;


/**
 * The class defines a black body.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public class BlackBody extends Object {
    //See
    //http://en.wikipedia.org/wiki/Rayleigh-Jeans_law
    //http://en.wikipedia.org/wiki/Planck%27s_law_of_black_body_radiation
    //http://janus.astro.umd.edu/astro/equationshelp.html
    //heat can be transmitted:
    //radiation: light emission
    //convection: movements (dilatation for example)
    //conduction: from a body to another body
    //state change: for example when a melting from solid to liquid state
    //chemical reaction
    /** DOCUMENT ME! */
    private double temperature;

/**
     * Creates a new BlackBody object.
     *
     * @param temperature DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public BlackBody(double temperature) {
        if (temperature >= 0) {
            this.temperature = temperature;
        } else {
            throw new IllegalArgumentException(
                "The BlackBody constructor doesn't accept negative values.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * DOCUMENT ME!
     *
     * @param temperature DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setTemperature(double temperature) {
        if (temperature >= 0) {
            this.temperature = temperature;
        } else {
            throw new IllegalArgumentException(
                "You can't set a negative temperature.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double computeAllEmittedLight() {
        //http://janus.astro.umd.edu/astro/equationshelp.html
        return ChemistryConstants.STEFAN_BOLTZMANN * Math.pow(temperature, 4);
    }

    //Wien's displacement law states that there is an inverse relationship between the wavelength of the peak of the emission of a blackbody and its temperature.
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getLambdaMax() {
        return 0.002898 / temperature;
    }

    //Rayleigh-Jeans_law modified by planck
    /**
     * DOCUMENT ME!
     *
     * @param wavelength DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getEnergyDensity(double wavelength) {
        return (8 * Math.PI * PhysicsConstants.PLANCK * PhysicsConstants.SPEED_OF_LIGHT * Math.pow(wavelength,
            -5)) / (Math.exp((PhysicsConstants.PLANCK * PhysicsConstants.SPEED_OF_LIGHT) / (wavelength * ChemistryConstants.BOLTZMANN * temperature)) -
        1);
    }

    //the intensity spectrum of electromagnetic radiation from a black body at temperature T is given by the Planck's law of black body radiation
    /**
     * DOCUMENT ME!
     *
     * @param frequency DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getEnergyIntensity(double frequency) {
        return (2 * PhysicsConstants.PLANCK * Math.pow(frequency, 3)) / ((PhysicsConstants.SPEED_OF_LIGHT * PhysicsConstants.SPEED_OF_LIGHT) * (Math.exp((PhysicsConstants.PLANCK * frequency) / (ChemistryConstants.BOLTZMANN * temperature)) -
        1));
    }
}
