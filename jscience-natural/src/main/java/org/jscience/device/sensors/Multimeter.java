package org.jscience.device.sensors;

import org.jscience.device.Sensor;
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
        DC_VOLTAGE("device.multimeter.func.dcv"),
        AC_VOLTAGE("device.multimeter.func.acv"),
        DC_CURRENT("device.multimeter.func.dca"),
        AC_CURRENT("device.multimeter.func.aca"),
        RESISTANCE("device.multimeter.func.resistance"),
        CAPACITANCE("device.multimeter.func.capacitance"),
        FREQUENCY("device.multimeter.func.frequency"),
        CONTINUITY("device.multimeter.func.continuity"),
        DIODE_TEST("device.multimeter.func.diode"),
        TEMPERATURE("device.multimeter.func.temperature");

        private final String i18nKey;

        Function(String i18nKey) {
            this.i18nKey = i18nKey;
        }

        @Override
        public String toString() {
            return org.jscience.ui.i18n.I18n.getInstance().get(i18nKey);
        }
    }

    Function getFunction();

    void setFunction(Function function);

    // Additional methods for unit support or ranges could go here
}
