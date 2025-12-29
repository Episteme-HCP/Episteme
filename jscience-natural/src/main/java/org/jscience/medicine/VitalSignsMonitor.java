/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.medicine;

import org.jscience.device.Sensor;

/**
 * Interface for vital signs monitoring devices.
 * <p>
 * Provides access to real-time vital signs data including heart rate,
 * blood pressure, oxygen saturation, respiration rate, and temperature.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface VitalSignsMonitor extends Sensor<VitalSigns> {

    /**
     * Gets the current vital signs readings.
     *
     * @return current vital signs
     */
    VitalSigns getVitalSigns();

    /**
     * Gets the ECG waveform data for display.
     *
     * @return array of ECG amplitude values
     */
    double[] getECGWaveform();

    /**
     * Gets the plethysmograph (SpO2) waveform data.
     *
     * @return array of plethysmograph amplitude values
     */
    double[] getPlethWaveform();

    /**
     * Gets the number of channels available.
     *
     * @return channel count
     */
    int getChannelCount();

    /**
     * Gets the sample rate in Hz.
     *
     * @return samples per second
     */
    double getSampleRate();
}
