package org.jscience.physics.classical.thermodynamics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents an ideal gas following the law $PV = nRT$.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class IdealGas implements ThermodynamicState {

    private final Real amountOfSubstance; // n (moles)
    private final Real gasConstant = Real.of(8.314462618); // R (J/(mol*K))

    private Real temperature; // T
    private Real pressure; // P
    private Real volume; // V

    // For an ideal gas: U = (f/2) nRT. We assume monatomic (f=3) for default.
    private final Real degreesOfFreedom = Real.of(3.0);

    public IdealGas(Real moles, Real temperature, Real volume) {
        this.amountOfSubstance = moles;
        this.temperature = temperature;
        this.volume = volume;
        calculatePressure();
    }

    private void calculatePressure() {
        // P = nRT / V
        this.pressure = amountOfSubstance
                .multiply(gasConstant)
                .multiply(temperature)
                .divide(volume);
    }

    private void calculateVolume() {
        // V = nRT / P
        this.volume = amountOfSubstance
                .multiply(gasConstant)
                .multiply(temperature)
                .divide(pressure);
    }

    // --- State Accessors ---

    @Override
    public Real getTemperature() {
        return temperature;
    }

    @Override
    public Real getPressure() {
        return pressure;
    }

    @Override
    public Real getVolume() {
        return volume;
    }

    @Override
    public Real getInternalEnergy() {
        // U = (f/2) nRT
        return degreesOfFreedom.divide(Real.of(2.0))
                .multiply(amountOfSubstance)
                .multiply(gasConstant)
                .multiply(temperature);
    }

    @Override
    public Real getEntropy() {
        // S = nR [ ln(V) + (f/2) ln(T) + C ] (Sackur-Tetrode simplified)
        // Calculating absolute entropy is complex without specific constants.
        // Returning a placeholder relative entropy change term: nR ln(V)
        return amountOfSubstance.multiply(gasConstant).multiply(volume.log());
    }

    // --- Mutators (Iso-process simulation) ---

    /**
     * Isochoric heating/cooling (Volume constant).
     */
    public void setTemperatureIsochoric(Real newT) {
        this.temperature = newT;
        calculatePressure();
    }

    /**
     * Isobaric expansion/compression (Pressure constant).
     */
    public void setTemperatureIsobaric(Real newT) {
        this.temperature = newT;
        calculateVolume();
    }
}
