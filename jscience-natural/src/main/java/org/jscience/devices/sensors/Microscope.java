package org.jscience.devices.sensors;

import org.jscience.devices.Device;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Interface for microscopes.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Microscope extends Device {
    enum Type {
        OPTICAL("device.microscope.type.optical"),
        ELECTRON_SCANNING("device.microscope.type.sem"),
        ELECTRON_TRANSMISSION("device.microscope.type.tem"),
        CONFOCAL("device.microscope.type.confocal"),
        FLUORESCENCE("device.microscope.type.fluorescence"),
        ATOMIC_FORCE("device.microscope.type.afm");

        private final String i18nKey;

        Type(String i18nKey) {
            this.i18nKey = i18nKey;
        }

        @Override
        public String toString() {
            return org.jscience.natural.i18n.I18n.getInstance().get(i18nKey);
        }
    }

    Type getType();

    Real getMaxMagnification();

    Real getResolution();

    Real getCurrentMagnification();

    void setMagnification(Real magnification);

    Real getApparentSize(Real actualSize);

    boolean isResolvable(Real featureSize);
}
