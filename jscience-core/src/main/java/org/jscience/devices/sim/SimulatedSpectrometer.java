package org.jscience.devices.sim;

import org.jscience.devices.sensors.Spectrometer;
import org.jscience.mathematics.numbers.real.Real;
import java.io.IOException;

/**
 * Simulated implementation of Spectrometer.
 */
public class SimulatedSpectrometer extends SimulatedDevice implements Spectrometer {

    private final SpectroscopyType type;
    private final double minWavelength;
    private final double maxWavelength;
    private final double resolution;
    private double integrationTime = 10.0;

    public SimulatedSpectrometer(String name, SpectroscopyType type, double min, double max, double res) {
        super(name);
        this.type = type;
        this.minWavelength = min;
        this.maxWavelength = max;
        this.resolution = res;
    }

    @Override
    public SpectroscopyType getType() {
        return type;
    }

    @Override
    public double getMinWavelength() {
        return minWavelength;
    }

    @Override
    public double getMaxWavelength() {
        return maxWavelength;
    }

    @Override
    public double getResolution() {
        return resolution;
    }

    @Override
    public void setIntegrationTime(double milliseconds) {
        this.integrationTime = milliseconds;
    }

    @Override
    public double getIntegrationTime() {
        return integrationTime;
    }

    @Override
    public double[][] captureSpectrum() {
        if (!isConnected())
            throw new IllegalStateException("Not connected");
        // Mock spectrum: Gaussian peak
        int points = (int) ((maxWavelength - minWavelength) / resolution);
        double[][] spectrum = new double[points][2];
        double peak = (minWavelength + maxWavelength) / 2.0;
        double sigma = (maxWavelength - minWavelength) / 10.0;

        for (int i = 0; i < points; i++) {
            double wl = minWavelength + i * resolution;
            double intensity = Math.exp(-Math.pow(wl - peak, 2) / (2 * sigma * sigma));
            spectrum[i][0] = wl;
            spectrum[i][1] = intensity;
        }
        return spectrum;
    }

    @Override
    public double getIntensityAt(double wavelengthNm) {
        // Simple mock
        return Math.random();
    }

    @Override
    public void calibrate(double[] ref, double[] measured) {
        // No-op for fit
    }

    @Override
    public Real readValue() throws IOException {
        // For Sensor<Real> contract, maybe return max intensity or average?
        return Real.of(0.0);
    }
}
