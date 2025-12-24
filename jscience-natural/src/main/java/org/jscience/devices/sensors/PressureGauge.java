package org.jscience.devices.sensors;

import org.jscience.devices.Sensor;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Interface for pressure gauge devices.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface PressureGauge extends Sensor<Real> {
    enum GaugeType {
        BOURDON, DIAPHRAGM, PIEZOELECTRIC, CAPACITIVE
    }

    GaugeType getType();

    Real getAccuracy();

    Real getMinPressure();

    Real getMaxPressure();

    /**
     * Gets the pressure unit (e.g., "Pa", "bar", "psi").
     */
    String getPressureUnit();

    /**
     * Measures the pressure given a physical actual pressure.
     * Useful for simulation or calibration.
     */
    Real measure(Real actualPressure);
}
