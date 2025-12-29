package org.jscience.device.actuators;

import org.jscience.device.Actuator;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Interface for centrifuges.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Centrifuge extends Actuator<Real> {
    enum RotorType {
        FIXED_ANGLE("device.centrifuge.rotor.fixed"),
        SWINGING_BUCKET("device.centrifuge.rotor.swinging"),
        VERTICAL("device.centrifuge.rotor.vertical");

        private final String i18nKey;

        RotorType(String i18nKey) {
            this.i18nKey = i18nKey;
        }

        @Override
        public String toString() {
            return org.jscience.ui.i18n.I18n.getInstance().get(i18nKey);
        }
    }

    void start(Real rpm);

    void stop();

    Real calculateRCF(Real radiusCm);

    Real getMaxRPM();

    Real getMaxRCF();

    RotorType getRotorType();

    Real getCurrentRPM();

    boolean isRunning();
}
