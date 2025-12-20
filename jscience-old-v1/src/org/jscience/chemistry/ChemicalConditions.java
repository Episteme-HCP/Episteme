package org.jscience.chemistry;

/**
 * The ChemicalConditions class is the class that defines a chemical environment at a time.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

public class ChemicalConditions extends Object {

    public final static double DEFAULT_TEMPERATURE = ChemistryConstants.ZERO_CELCIUS + 25;
    public final static double DEFAULT_VOLUME = ChemistryConstants.MOLAR_VOLUME_25;
    public final static double DEFAULT_PRESSURE = ChemistryConstants.ATMOSPHERE;
    public final static double DEFAULT_POTENTIAL = ChemistryConstants
    .1e-7;//probably only valid for water

    private double temperature;
    private double volume;
    private double pressure;
    private double potential;//the ionisation potential of this solution, pH for water

    public ChemicalConditions(double t, double v, double p, double c) {

        this.temperature = t;
        this.volume = v;
        this.pressure = p;
        this.potential = c;

    }

    public double getTemperature() {

        return temperature;
    }

    public void setTemperature(double t) {

        temperature = t;

    }

    public double getVolume() {

        return volume;
    }

    public void setVolume(double v) {

        volume = v;

    }

    public double getPressure() {

        return pressure;
    }

    public void setPressure(double p) {

        pressure = p;

    }

    public double getPotential() {

        return potential;
    }

    public void setPotential(double c) {

        this.potential = c;

    }

    public static ChemicalConditions getDefaultChemicalConditions() {

        return new ChemicalConditions(DEFAULT_TEMPERATURE, DEFAULT_VOLUME, DEFAULT_PRESSURE, DEFAULT_POTENTIAL);

    }

}
