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

package org.episteme.jni.jni.devices;

import org.episteme.natural.device.sensors.Spectrometer;
import org.episteme.core.device.AbstractDevice;
import org.episteme.core.util.identity.SimpleIdentification;
import org.episteme.jni.jni.NativeDeviceBridge;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.Quantities;
import org.episteme.core.measure.Units;
import org.episteme.core.measure.quantity.Dimensionless;
import org.episteme.core.measure.quantity.Length;
import org.episteme.core.measure.quantity.Time;

/**
 * Implementation of a Mass Spectrometer interacting via JNI.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MassSpectra extends AbstractDevice implements Spectrometer {

    private final NativeDeviceBridge bridge;
    private final int deviceId;
    private Quantity<Time> integrationTime = Quantities.create(100.0, Units.MILLISECOND);

    public MassSpectra(int deviceId) {
        super(new SimpleIdentification("JNI-MS-" + deviceId));
        this.deviceId = deviceId;
        this.bridge = new NativeDeviceBridge();
        getTraits().put("name", getName());
    }

    @Override
    public Spectrometer.SpectroscopyType getType() {
        return Spectrometer.SpectroscopyType.MASS;
    }

    @Override
    public Quantity<Length> getMinWavelength() {
        return Quantities.create(0, Units.NANOMETER); // Mass Spec uses m/z, mapped to wavelength domain
    }

    @Override
    public Quantity<Length> getMaxWavelength() {
        return Quantities.create(2000, Units.NANOMETER);
    }

    @Override
    public Quantity<Length> getSpectralResolution() {
        return Quantities.create(0.1, Units.NANOMETER);
    }

    @Override
    public void setIntegrationTime(Quantity<Time> time) {
        this.integrationTime = time;
    }

    @Override
    public Quantity<Time> getIntegrationTime() {
        return integrationTime;
    }

    @Override
    public double[][] captureSpectrum() {
        // Native call to acquire data
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
    public Quantity<Dimensionless> getIntensityAt(Quantity<Length> wavelength) {
        // Simple stub - would do interpolation on actual spectrum data
        return Quantities.create(0.0, Units.ONE);
    }

    @Override
    public void calibrate(Quantity<Length>[] referenceWavelengths, Quantity<Length>[] measuredWavelengths) {
        // Calibration logic - would send to native side
    }

    @Override
    public Quantity<Dimensionless> readValue() throws java.io.IOException {
        // Sensor<Dimensionless> method - returns total ion count as dimensionless
        return Quantities.create(0.0, Units.ONE);
    }

    @Override
    public String getName() {
        return "MassSpectra Model X (JNI)";
    }

    @Override
    public String getManufacturer() {
        return "Episteme Instruments";
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
