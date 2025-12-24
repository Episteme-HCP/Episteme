package org.jscience.devices.sensors;

import org.jscience.devices.Sensor;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Interface for temperature probes.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface TemperatureProbe extends Sensor<Real> {
    enum ProbeType {
        THERMOCOUPLE, RTD, THERMISTOR, INFRARED
    }

    ProbeType getType();

    Real getAccuracy();

    Real getMinTemp();

    Real getMaxTemp();

    /**
     * Measures the temperature given a physical actual temperature.
     * Useful for simulation or calibration.
     */
    Real measure(Real actualTemp);
}
