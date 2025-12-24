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
        OPTICAL, ELECTRON_SCANNING, ELECTRON_TRANSMISSION, CONFOCAL, FLUORESCENCE, ATOMIC_FORCE
    }

    Type getType();

    Real getMaxMagnification();

    Real getResolution();

    Real getCurrentMagnification();

    void setMagnification(Real magnification);

    Real getApparentSize(Real actualSize);

    boolean isResolvable(Real featureSize);
}
