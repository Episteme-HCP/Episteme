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
        UV_VIS, INFRARED, RAMAN, MASS, NMR, FLUORESCENCE, ATOMIC_ABSORPTION
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
