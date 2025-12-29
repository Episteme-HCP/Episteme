/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
