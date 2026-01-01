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

package org.jscience.device.sim;

import org.jscience.device.sensors.Oscilloscope;
import org.jscience.mathematics.numbers.real.Real;
import java.io.IOException;

/**
 * Simulated implementation of Oscilloscope.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SimulatedOscilloscope extends SimulatedDevice implements Oscilloscope {

    private int channels = 2;
    private double sampleRate = 1e6;
    private double timeBase = 1e-3;
    private Oscilloscope.TriggerMode triggerMode = Oscilloscope.TriggerMode.AUTO;
    private double triggerLevel = 0.0;

    public SimulatedOscilloscope() {
        this("Oscilloscope");
    }

    public SimulatedOscilloscope(String name) {
        super(name);
    }

    @Override
    public int getChannelCount() {
        return channels;
    }

    @Override
    public double getSampleRate() {
        return sampleRate;
    }

    @Override
    public void setSampleRate(double samplesPerSecond) {
        this.sampleRate = samplesPerSecond;
    }

    @Override
    public double getTimeBase() {
        return timeBase;
    }

    @Override
    public void setTimeBase(double secondsPerDivision) {
        this.timeBase = secondsPerDivision;
    }

    @Override
    public double getVoltageScale(int channel) {
        return 1.0;
    }

    @Override
    public void setVoltageScale(int channel, double voltsPerDivision) {
    }

    @Override
    public Oscilloscope.TriggerMode getTriggerMode() {
        return triggerMode;
    }

    @Override
    public void setTriggerMode(Oscilloscope.TriggerMode mode) {
        this.triggerMode = mode;
    }

    @Override
    public double getTriggerLevel() {
        return triggerLevel;
    }

    @Override
    public void setTriggerLevel(double volts) {
        this.triggerLevel = volts;
    }

    private double frequency = 1000.0; // 1 kHz default
    private double amplitude = 1.0;
    private double phase = 0.0;

    public void setSignal(double freq, double amp) {
        this.frequency = freq;
        this.amplitude = amp;
    }

    @Override
    public double[] captureWaveform(int channel) {
        if (!isConnected())
            throw new IllegalStateException("Not connected");

        int points = 500;
        double[] wave = new double[points];
        double dt = timeBase / 10.0 / points; // 10 divisions wide?

        for (int i = 0; i < points; i++) {
            double t = i * dt + phase;
            // Add some noise and harmonics
            double signal = amplitude * Math.sin(2 * Math.PI * frequency * t);
            signal += 0.05 * Math.sin(2 * Math.PI * frequency * 3 * t); // 3rd harmonic
            signal += (Math.random() - 0.5) * 0.02 * amplitude; // noise
            wave[i] = signal;
        }
        // Advance phase for next capture to simulate "moving" wave if not triggered
        phase += dt * points * 1.5;

        return wave;
    }

    @Override
    public double getBandwidth() {
        return 20e6;
    } // 20 MHz default

    @Override
    public Real readValue() throws IOException {
        return Real.ZERO;
    }
}


