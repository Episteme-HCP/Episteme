package org.jscience.device;

import java.util.List;

/**
 * Represents a complex instrument that may contain multiple sensors or
 * actuators.
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public interface Instrument extends Device {

    /**
     * return The list of sensors attached to this instrument.
     */
    List<Sensor<?>> getSensors();

    /**
     * Calibrates the instrument.
     */
    void calibrate();
}
