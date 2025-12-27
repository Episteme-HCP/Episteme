package org.jscience.devices.sim;

import org.jscience.devices.sensors.Oscilloscope;
import org.jscience.mathematics.numbers.real.Real;
import java.io.IOException;

/**
 * Simulated implementation of Oscilloscope.
 */
public class SimulatedOscilloscope extends SimulatedDevice implements Oscilloscope {

    private int channels = 2;
    private double sampleRate = 1e6;
    private double timeBase = 1e-3;
    private TriggerMode triggerMode = TriggerMode.AUTO;
    private double triggerLevel = 0.0;

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
    public TriggerMode getTriggerMode() {
        return triggerMode;
    }

    @Override
    public void setTriggerMode(TriggerMode mode) {
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
