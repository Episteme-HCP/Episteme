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

    @Override
    public double[] captureWaveform(int channel) {
        if (!isConnected())
            throw new IllegalStateException("Not connected");
        // Mock Sine wave
        double[] wave = new double[100];
        for (int i = 0; i < 100; i++) {
            wave[i] = Math.sin(i * 0.1);
        }
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
