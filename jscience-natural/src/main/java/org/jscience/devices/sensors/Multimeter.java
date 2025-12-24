package org.jscience.devices.sensors;

import org.jscience.devices.Sensor;
import org.jscience.mathematics.numbers.real.Real; // Standardizing on Real

/**
 * Interface for multimeter devices.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Multimeter extends Sensor<Real> {
    enum Function {
        DC_VOLTAGE,
        AC_VOLTAGE,
        DC_CURRENT,
        AC_CURRENT,
        RESISTANCE,
        CAPACITANCE,
        FREQUENCY,
        CONTINUITY,
        DIODE_TEST,
        TEMPERATURE
    }

    Function getFunction();

    void setFunction(Function function);

    // Additional methods for unit support or ranges could go here
}
