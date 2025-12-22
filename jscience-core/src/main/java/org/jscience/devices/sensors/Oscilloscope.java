package org.jscience.devices.sensors;

import org.jscience.devices.Sensor;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Interface for oscilloscope devices.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Oscilloscope extends Sensor<Real> {
    enum TriggerMode {
        AUTO, NORMAL, SINGLE
    }

    int getChannelCount();

    double getSampleRate();

    void setSampleRate(double samplesPerSecond);

    double getTimeBase();

    void setTimeBase(double secondsPerDivision);

    double getVoltageScale(int channel);

    void setVoltageScale(int channel, double voltsPerDivision);

    TriggerMode getTriggerMode();

    void setTriggerMode(TriggerMode mode);

    double getTriggerLevel();

    void setTriggerLevel(double volts);

    double[] captureWaveform(int channel);

    double getBandwidth();
}
