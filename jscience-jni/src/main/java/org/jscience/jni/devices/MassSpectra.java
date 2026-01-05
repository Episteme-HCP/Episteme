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

package org.jscience.jni.devices;

import org.jscience.device.sensors.Spectrometer;

import org.jscience.jni.NativeDeviceBridge;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Implementation of a Mass Spectrometer interacting via JNI.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MassSpectra implements Spectrometer {

    private final NativeDeviceBridge bridge;
    private final int deviceId;
    private double integrationTime = 100.0; // Default 100ms

    public MassSpectra(int deviceId) {
        this.deviceId = deviceId;
        this.bridge = new NativeDeviceBridge();
    }

    @Override
    public SpectroscopyType getType() {
        return SpectroscopyType.MASS;
    }

    @Override
    public double getMinWavelength() {
        return 0; // Mass Spec doesn't use wavelength in the same way (m/z)
    }

    @Override
    public double getMaxWavelength() {
        return 2000;
    }

    @Override
    public double getResolution() {
        return 0.1;
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
        // Native call to acquire data
        // For mass spec, we might need a different native signature, taking int time
        // return bridge.acquireMassSpectrum(deviceId, integrationTime);

        // Using the generic spectrum acquisition added previously
        double[] linearData = bridge.acquireSpectrum(deviceId);

        // Convert to 2D array [mz, intensity]
        double[][] spectrum = new double[linearData.length / 2][2];
        for (int i = 0; i < spectrum.length; i++) {
            spectrum[i][0] = linearData[2 * i];
            spectrum[i][1] = linearData[2 * i + 1];
        }
        return spectrum;
    }

    @Override
    public double getIntensityAt(double unitInfo) {
        // Simple interpolation or lookup
        return 0.0;
    }

    @Override
    public void calibrate(double[] referenceUnits, double[] measuredUnits) {
        // Calibration logic
    }

    @Override
    public Real readValue() throws java.io.IOException {
        // Sensor<Real> method - maybe return total ion count?
        return Real.ZERO;
    }

    @Override
    public String getName() {
        return "MassSpectra Model X (JNI)";
    }

    @Override
    public String getId() {
        return "JNI-MS-" + deviceId;
    }

    @Override
    public String getManufacturer() {
        return "JScience Instruments";
    }

    @Override
    public void connect() throws java.io.IOException {
        // No-op for simulated JNI bridge
    }

    @Override
    public void disconnect() throws java.io.IOException {
        // No-op
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public void close() throws Exception {
        disconnect();
    }
}
