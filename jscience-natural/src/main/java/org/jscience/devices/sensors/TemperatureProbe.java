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
        THERMOCOUPLE("device.temp.type.thermocouple"),
        RTD("device.temp.type.rtd"),
        THERMISTOR("device.temp.type.thermistor"),
        INFRARED("device.temp.type.infrared");

        private final String i18nKey;

        ProbeType(String i18nKey) {
            this.i18nKey = i18nKey;
        }

        @Override
        public String toString() {
            return org.jscience.ui.i18n.I18n.getInstance().get(i18nKey);
        }
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
