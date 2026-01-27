/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.medicine;

import org.jscience.device.Sensor;

/**
 * Interface for vital signs monitoring devices (e.g., bedside monitors).
 * <p>
 * Provides streamable access to real-time vital signs data and associated waveforms.
 * </p>
 * * @see VitalSigns
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface VitalSignsMonitor extends Sensor<VitalSigns> {

    /**
     * Gets the current vital signs readings.
     * Shortcut for {@code readValue()}.
     *
     * @return current vital signs
     */
    default VitalSigns getVitalSigns() {
        try {
            return readValue();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets the ECG (Electrocardiogram) waveform data.
     *
     * @return array of ECG amplitude values
     */
    double[] getECGWaveform();

    /**
     * Gets the SpO2 (Plethysmograph) waveform data.
     *
     * @return array of pleth amplitude values
     */
    double[] getPlethWaveform();

    /**
     * Gets the sample rate for waveforms.
     *
     * @return samples per second in Hz
     */
    double getSampleRate();
    
    /**
     * Gets the number of data channels provided by this monitor.
     *
     * @return channel count
     */
    int getChannelCount();

    /**
     * Checks if the monitor is currently alarming.
     * 
     * @return true if an alert condition is active
     */
    boolean isAlarming();
}
