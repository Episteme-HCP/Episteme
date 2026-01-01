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

import java.io.IOException;
import java.util.Map;
import java.util.Random;

/**
 * Simulated USB device for testing without physical hardware.
 * <p>
 * Can act as either a sensor (input) or actuator (output) depending on
 * configuration.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SimulatedUSBDevice extends SimulatedDevice {

    public enum DeviceType {
        SENSOR, ACTUATOR, BIDIRECTIONAL
    }

    private int vendorId = 0x1234;
    private int productId = 0x5678;
    private DeviceType deviceType = DeviceType.SENSOR;
    private String unit = "V";
    private double baseValue = 0.0;
    private double noiseLevel = 0.1;
    private int samplingRateHz = 10;
    private boolean calibrated = false;
    private double actuatorValue = 0.0;
    private final Random random = new Random();

    public SimulatedUSBDevice() {
        this("Generic USB Device");
    }

    public SimulatedUSBDevice(String name) {
        super(name);
        setDriverClass("org.jscience.device.sim.SimulatedUSBDevice");

        // USB device capabilities
        setCapability("Data Logging", true);
        setCapability("Continuous Sampling", true);
        setCapability("Auto-Calibration", true);
        setCapability("USB 2.0 High-Speed", true);
        setCapability("Bidirectional I/O", false);
    }

    public SimulatedUSBDevice(String name, int vendorId, int productId) {
        this(name);
        this.vendorId = vendorId;
        this.productId = productId;
    }

    public SimulatedUSBDevice(String name, DeviceType type) {
        this(name);
        this.deviceType = type;
        if (type == DeviceType.ACTUATOR) {
            setCapability("Continuous Sampling", false);
            setCapability("Output Control", true);
        } else if (type == DeviceType.BIDIRECTIONAL) {
            setCapability("Bidirectional I/O", true);
            setCapability("Output Control", true);
        }
    }

    public int getVendorId() {
        return vendorId;
    }

    public int getProductId() {
        return productId;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(double baseValue) {
        this.baseValue = baseValue;
    }

    public double getNoiseLevel() {
        return noiseLevel;
    }

    public void setNoiseLevel(double noiseLevel) {
        this.noiseLevel = noiseLevel;
    }

    public int getSamplingRateHz() {
        return samplingRateHz;
    }

    public void setSamplingRateHz(int samplingRateHz) {
        this.samplingRateHz = samplingRateHz;
    }

    public boolean isCalibrated() {
        return calibrated;
    }

    // === Sensor (Input) Methods ===

    /**
     * Simulates reading a sample from the device (sensor mode).
     */
    public double readSample() throws IOException {
        if (!isConnected()) {
            throw new IOException("Device not connected");
        }
        if (deviceType == DeviceType.ACTUATOR) {
            throw new IOException("Device is in actuator mode, cannot read samples");
        }
        // Simulated reading with noise
        return baseValue + (random.nextGaussian() * noiseLevel);
    }

    /**
     * Reads multiple samples.
     */
    public double[] readSamples(int count) throws IOException {
        double[] samples = new double[count];
        for (int i = 0; i < count; i++) {
            samples[i] = readSample();
        }
        return samples;
    }

    /**
     * Simulates sensor calibration.
     */
    public void calibrate() throws IOException {
        if (!isConnected()) {
            throw new IOException("Device not connected");
        }
        // Simulate calibration delay
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        this.calibrated = true;
        this.baseValue = 0.0; // Reset to zero after calibration
    }

    // === Actuator (Output) Methods ===

    /**
     * Sets the actuator output value.
     */
    public void setOutput(double value) throws IOException {
        if (!isConnected()) {
            throw new IOException("Device not connected");
        }
        if (deviceType == DeviceType.SENSOR) {
            throw new IOException("Device is in sensor mode, cannot set output");
        }
        this.actuatorValue = value;
    }

    /**
     * Gets the current actuator output value.
     */
    public double getOutput() {
        return actuatorValue;
    }

    @Override
    public String getId() {
        return String.format("USB:%04X:%04X", vendorId, productId);
    }

    @Override
    protected void addCustomReadings(Map<String, String> readings) {
        readings.put("Vendor ID", String.format("0x%04X", vendorId));
        readings.put("Product ID", String.format("0x%04X", productId));
        readings.put("Device Type", deviceType.name());
        readings.put("Sampling Rate", samplingRateHz + " Hz");
        readings.put("Unit", unit);
        readings.put("Calibrated", calibrated ? "Yes" : "No");

        if (deviceType != DeviceType.ACTUATOR) {
            try {
                if (isConnected()) {
                    readings.put("Last Reading", String.format("%.4f %s", readSample(), unit));
                }
            } catch (IOException e) {
                readings.put("Last Reading", "Error");
            }
        }

        if (deviceType != DeviceType.SENSOR) {
            readings.put("Output Value", String.format("%.4f %s", actuatorValue, unit));
        }
    }
}


