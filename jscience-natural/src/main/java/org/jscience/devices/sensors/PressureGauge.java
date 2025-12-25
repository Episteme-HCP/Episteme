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
        BOURDON("device.pressure.type.bourdon"),
        DIAPHRAGM("device.pressure.type.diaphragm"),
        PIEZOELECTRIC("device.pressure.type.piezo"),
        CAPACITIVE("device.pressure.type.capacitive");

        private final String i18nKey;

        GaugeType(String i18nKey) {
            this.i18nKey = i18nKey;
        }

        @Override
        public String toString() {
            return org.jscience.natural.i18n.I18n.getInstance().get(i18nKey);
        }
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
