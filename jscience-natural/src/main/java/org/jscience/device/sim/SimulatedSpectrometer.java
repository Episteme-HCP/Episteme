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

package org.jscience.device.sim;

import org.jscience.device.sensors.Spectrometer;
import org.jscience.mathematics.numbers.real.Real;
import java.io.IOException;

/**
 * Simulated implementation of Spectrometer.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SimulatedSpectrometer extends SimulatedDevice implements Spectrometer {

    private final Spectrometer.SpectroscopyType type;
    private final double minWavelength;
    private final double maxWavelength;
    private final double resolution;
    private double integrationTime = 10.0;

    public SimulatedSpectrometer() {
        this("Spectrometer", Spectrometer.SpectroscopyType.UV_VIS, 190, 1100, 0.5);
    }

    public SimulatedSpectrometer(String name, Spectrometer.SpectroscopyType type, double min, double max, double res) {
        super(name);
        this.type = type;
        this.minWavelength = min;
        this.maxWavelength = max;
        this.resolution = res;
    }

    @Override
    public Spectrometer.SpectroscopyType getType() {
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

    private double noiseLevel = 0.05;

    @Override
    public double[][] captureSpectrum() {
        if (!isConnected())
            throw new IllegalStateException("Not connected");

        int points = (int) ((maxWavelength - minWavelength) / resolution);
        double[][] spectrum = new double[points][2];

        // Use a consistent pseudo-random seed or state if needed, but here we just
        // simulate real-time
        for (int i = 0; i < points; i++) {
            double wl = minWavelength + i * resolution;
            double intensity = calculateModelIntensity(wl);

            // Add thermal noise based on integration time (simplified)
            double noise = (Math.random() - 0.5) * noiseLevel * (1.0 / Math.sqrt(integrationTime / 10.0));

            spectrum[i][0] = wl;
            spectrum[i][1] = Math.max(0, intensity + noise);
        }
        return spectrum;
    }

    private double calculateModelIntensity(double wl) {
        // Mock spectrum: Main Gaussian peak + a smaller secondary peak
        double peak1 = (minWavelength + maxWavelength) / 2.0;
        double sigma1 = (maxWavelength - minWavelength) / 15.0;
        double amp1 = 0.8;

        double peak2 = peak1 + (maxWavelength - minWavelength) / 5.0;
        double sigma2 = sigma1 / 2.0;
        double amp2 = 0.3;

        double val = amp1 * Math.exp(-Math.pow(wl - peak1, 2) / (2 * sigma1 * sigma1));
        if (peak2 < maxWavelength) {
            val += amp2 * Math.exp(-Math.pow(wl - peak2, 2) / (2 * sigma2 * sigma2));
        }
        return val;
    }

    @Override
    public double getIntensityAt(double wavelengthNm) {
        if (!isConnected())
            throw new IllegalStateException("Not connected");
        return calculateModelIntensity(wavelengthNm) + (Math.random() - 0.5) * noiseLevel;
    }

    @Override
    public void calibrate(double[] ref, double[] measured) {
        // Logic for setting offset/gain could go here
    }

    @Override
    public Real readValue() throws IOException {
        if (!isConnected())
            throw new IOException("Not connected");
        // Return the intensity at the primary peak
        double peak1 = (minWavelength + maxWavelength) / 2.0;
        return Real.of(getIntensityAt(peak1));
    }
}


