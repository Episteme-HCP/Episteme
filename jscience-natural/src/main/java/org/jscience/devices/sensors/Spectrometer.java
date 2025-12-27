package org.jscience.devices.sensors;

import org.jscience.devices.Sensor;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Interface for spectrometer devices.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Spectrometer extends Sensor<Real> {
    enum SpectroscopyType {
        UV_VIS("device.spectrometer.type.uvvis"),
        INFRARED("device.spectrometer.type.ir"),
        RAMAN("device.spectrometer.type.raman"),
        MASS("device.spectrometer.type.mass"),
        NMR("device.spectrometer.type.nmr"),
        FLUORESCENCE("device.spectrometer.type.fluorescence"),
        ATOMIC_ABSORPTION("device.spectrometer.type.aa");

        private final String i18nKey;

        SpectroscopyType(String i18nKey) {
            this.i18nKey = i18nKey;
        }

        @Override
        public String toString() {
            return org.jscience.ui.i18n.I18n.getInstance().get(i18nKey);
        }
    }

    SpectroscopyType getType();

    double getMinWavelength();

    double getMaxWavelength();

    double getResolution();

    void setIntegrationTime(double milliseconds);

    double getIntegrationTime();

    double[][] captureSpectrum();

    double getIntensityAt(double wavelengthNm);

    void calibrate(double[] referenceWavelengths, double[] measuredWavelengths);
}
