package org.jscience.devices.actuators;

import org.jscience.devices.Device;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Interface for centrifuges.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Centrifuge extends Device {
    enum RotorType {
        FIXED_ANGLE, SWINGING_BUCKET, VERTICAL
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
