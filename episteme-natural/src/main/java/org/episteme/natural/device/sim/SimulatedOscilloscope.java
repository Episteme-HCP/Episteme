/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.device.sim;

import org.episteme.core.device.sim.SimulatedDevice;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.Quantities;
import org.episteme.core.measure.Units;
import org.episteme.core.measure.quantity.ElectricPotential;
import org.episteme.core.measure.quantity.Frequency;
import org.episteme.core.measure.quantity.Time;
import org.episteme.natural.device.sensors.Oscilloscope;
import org.episteme.core.util.identity.Identification;

import java.io.IOException;

/**
 * Simulated oscilloscope.
 */
public class SimulatedOscilloscope extends SimulatedDevice implements Oscilloscope {

    private int channels = 2;
    private Quantity<Frequency> sampleRate = Quantities.create(1e6, Units.HERTZ);
    private Quantity<Time> timeBase = Quantities.create(1e-3, Units.SECOND);
    private TriggerMode triggerMode = TriggerMode.AUTO;
    private Quantity<ElectricPotential> triggerLevel = Quantities.create(0.0, Units.VOLT);

    public SimulatedOscilloscope(Identification id) {
        super(id);
    }

    @Override
    public int getChannelCount() {
        return channels;
    }

    @Override
    public Quantity<Frequency> getSampleRate() {
        return sampleRate;
    }

    @Override
    public void setSampleRate(Quantity<Frequency> samplesPerSecond) {
        this.sampleRate = samplesPerSecond;
    }

    @Override
    public Quantity<Time> getTimeBase() {
        return timeBase;
    }

    @Override
    public void setTimeBase(Quantity<Time> secondsPerDivision) {
        this.timeBase = secondsPerDivision;
    }

    @Override
    public Quantity<ElectricPotential> getVoltageScale(int channel) {
        return Quantities.create(1.0, Units.VOLT);
    }

    @Override
    public void setVoltageScale(int channel, Quantity<ElectricPotential> voltsPerDivision) {
        // No-op
    }

    @Override
    public TriggerMode getTriggerMode() {
        return triggerMode;
    }

    @Override
    public void setTriggerMode(TriggerMode mode) {
        this.triggerMode = mode;
    }

    @Override
    public Quantity<ElectricPotential> getTriggerLevel() {
        return triggerLevel;
    }

    @Override
    public void setTriggerLevel(Quantity<ElectricPotential> volts) {
        this.triggerLevel = volts;
    }

    @Override
    public double[] captureWaveform(int channel) {
        int points = 500;
        double[] wave = new double[points];
        double freqVal = 1000.0;
        double ampVal = 1.0;
        double dt = timeBase.to(Units.SECOND).getValue().doubleValue() / 10.0 / points;

        for (int i = 0; i < points; i++) {
            double t = i * dt;
            wave[i] = ampVal * Math.sin(2 * Math.PI * freqVal * t);
        }
        return wave;
    }

    @Override
    public Quantity<Frequency> getBandwidth() {
        return Quantities.create(20e6, Units.HERTZ);
    }

    @Override
    public Quantity<ElectricPotential> readValue() throws IOException {
        return triggerLevel; // Placeholder
    }
}
