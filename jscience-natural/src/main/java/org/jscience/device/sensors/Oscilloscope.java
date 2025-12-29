package org.jscience.device.sensors;

import org.jscience.device.Sensor;
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
        AUTO("device.oscilloscope.trigger.auto"),
        NORMAL("device.oscilloscope.trigger.normal"),
        SINGLE("device.oscilloscope.trigger.single");

        private final String i18nKey;

        TriggerMode(String i18nKey) {
            this.i18nKey = i18nKey;
        }

        @Override
        public String toString() {
            return org.jscience.ui.i18n.I18n.getInstance().get(i18nKey);
        }
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
