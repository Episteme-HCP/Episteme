package org.jscience.physics.classical.thermodynamics;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Temperature;
import org.jscience.measure.quantity.Pressure;
import org.jscience.measure.quantity.Volume;
import org.jscience.measure.quantity.Energy;

/**
 * Represents an ideal gas following the law $PV = nRT$.
 * <p>
 * Uses type-safe Quantity classes for all physical properties.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class IdealGas implements ThermodynamicState {

    private static final double R = 8.314462618; // Gas constant (J/(mol*K))
    private static final double DEGREES_OF_FREEDOM = 3.0; // Monatomic default

    private final double amountOfSubstance; // n (moles)

    private Quantity<Temperature> temperature;
    private Quantity<Pressure> pressure;
    private Quantity<Volume> volume;

    /**
     * Creates an ideal gas with specified moles, temperature, and volume.
     * Pressure is calculated using PV = nRT.
     *
     * @param moles       amount of substance in moles
     * @param temperature initial temperature
     * @param volume      initial volume
     */
    public IdealGas(double moles, Quantity<Temperature> temperature, Quantity<Volume> volume) {
        this.amountOfSubstance = moles;
        this.temperature = temperature;
        this.volume = volume;
        calculatePressure();
    }

    /**
     * Legacy constructor using Real values.
     */
    public IdealGas(Real moles, Real temperatureK, Real volumeM3) {
        this(moles.doubleValue(),
                Quantities.create(temperatureK.doubleValue(), Units.KELVIN),
                Quantities.create(volumeM3.doubleValue(), Units.CUBIC_METER));
    }

    private void calculatePressure() {
        // P = nRT / V
        double T = temperature.getValue(Units.KELVIN).doubleValue();
        double V = volume.getValue(Units.CUBIC_METER).doubleValue();
        double P = amountOfSubstance * R * T / V;
        this.pressure = Quantities.create(P, Units.PASCAL);
    }

    private void calculateVolume() {
        // V = nRT / P
        double T = temperature.getValue(Units.KELVIN).doubleValue();
        double P = pressure.getValue(Units.PASCAL).doubleValue();
        double V = amountOfSubstance * R * T / P;
        this.volume = Quantities.create(V, Units.CUBIC_METER);
    }

    // --- State Accessors ---

    @Override
    public Quantity<Temperature> getTemperature() {
        return temperature;
    }

    @Override
    public Quantity<Pressure> getPressure() {
        return pressure;
    }

    @Override
    public Quantity<Volume> getVolume() {
        return volume;
    }

    @Override
    public Quantity<Energy> getInternalEnergy() {
        // U = (f/2) nRT
        double T = temperature.getValue(Units.KELVIN).doubleValue();
        double U = (DEGREES_OF_FREEDOM / 2.0) * amountOfSubstance * R * T;
        return Quantities.create(U, Units.JOULE);
    }

    @Override
    public Quantity<org.jscience.measure.quantity.Entropy> getEntropy() {
        // S = nR ln(V) (simplified relative entropy)
        double V = volume.getValue(Units.CUBIC_METER).doubleValue();
        double S = amountOfSubstance * R * Math.log(V);
        // Units.JOULE_PER_KELVIN is not defined, deriving it
        @SuppressWarnings("unchecked")
        org.jscience.measure.Unit<org.jscience.measure.quantity.Entropy> entropyUnit = (org.jscience.measure.Unit<org.jscience.measure.quantity.Entropy>) Units.JOULE
                .divide(Units.KELVIN);
        return Quantities.create(S, entropyUnit);
    }

    @Override
    public Quantity<Energy> getEnthalpy() {
        // H = U + PV
        double U = getInternalEnergy().getValue(Units.JOULE).doubleValue();
        double P = pressure.getValue(Units.PASCAL).doubleValue();
        double V = volume.getValue(Units.CUBIC_METER).doubleValue();
        double H = U + P * V;
        return Quantities.create(H, Units.JOULE);
    }

    // --- Mutators (Iso-process simulation) ---

    /**
     * Isochoric heating/cooling (Volume constant).
     * 
     * @param newT new temperature
     */
    public void setTemperatureIsochoric(Quantity<Temperature> newT) {
        this.temperature = newT;
        calculatePressure();
    }

    /**
     * Isobaric expansion/compression (Pressure constant).
     * 
     * @param newT new temperature
     */
    public void setTemperatureIsobaric(Quantity<Temperature> newT) {
        this.temperature = newT;
        calculateVolume();
    }

    /**
     * Legacy method using Real.
     */
    public void setTemperatureIsochoric(Real newT) {
        setTemperatureIsochoric(Quantities.create(newT.doubleValue(), Units.KELVIN));
    }

    /**
     * Legacy method using Real.
     */
    public void setTemperatureIsobaric(Real newT) {
        setTemperatureIsobaric(Quantities.create(newT.doubleValue(), Units.KELVIN));
    }
}
