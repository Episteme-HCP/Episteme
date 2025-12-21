/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.devices.instruments;

import org.jscience.devices.Sensor;

/**
 * Interface for oscilloscope devices.
 * <p>
 * An oscilloscope is a measurement instrument that displays varying signal
 * voltages as a two-dimensional plot.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Oscilloscope extends Sensor {

    /**
     * Trigger modes for signal capture.
     */
    enum TriggerMode {
        AUTO, NORMAL, SINGLE
    }

    /**
     * Gets the number of available channels.
     */
    int getChannelCount();

    /**
     * Gets the current sample rate in samples per second.
     */
    double getSampleRate();

    /**
     * Sets the sample rate.
     */
    void setSampleRate(double samplesPerSecond);

    /**
     * Gets the time base (time per division) in seconds.
     */
    double getTimeBase();

    /**
     * Sets the time base.
     */
    void setTimeBase(double secondsPerDivision);

    /**
     * Gets the voltage scale for a channel in volts per division.
     */
    double getVoltageScale(int channel);

    /**
     * Sets the voltage scale for a channel.
     */
    void setVoltageScale(int channel, double voltsPerDivision);

    /**
     * Gets the trigger mode.
     */
    TriggerMode getTriggerMode();

    /**
     * Sets the trigger mode.
     */
    void setTriggerMode(TriggerMode mode);

    /**
     * Gets the trigger level in volts.
     */
    double getTriggerLevel();

    /**
     * Sets the trigger level.
     */
    void setTriggerLevel(double volts);

    /**
     * Captures waveform data from a channel.
     *
     * @param channel channel number (0-based)
     * @return array of voltage samples
     */
    double[] captureWaveform(int channel);

    /**
     * Gets the bandwidth in Hz.
     */
    double getBandwidth();
}
