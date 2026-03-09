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

package org.episteme.core.device.sim;

import org.episteme.core.device.AbstractDevice;
import org.episteme.core.util.identity.Identification;
import org.episteme.core.util.identity.SimpleIdentification;
import java.io.IOException;
import java.util.Map;

/**
 * Abstract base implementation for all simulated devices.
 * Uses {@link SimulatedDeviceSupport} to provide common simulation features.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public abstract class AbstractSimulatedDevice extends AbstractDevice {

    protected final SimulatedDeviceSupport support;

    protected AbstractSimulatedDevice(String name) {
        this(new SimpleIdentification(String.format("%08X", Math.abs(name.hashCode()))));
        setTrait("name", name);
        support.setDriverClass("org.episteme.core.device.sim." + name.replace(" ", ""));
    }

    protected AbstractSimulatedDevice(Identification identification) {
        super(identification);
        this.support = new SimulatedDeviceSupport(this);
    }

    protected AbstractSimulatedDevice(String name, String manufacturer) {
        this(name);
        setManufacturer(manufacturer);
    }

    @Override
    public void connect() throws IOException { support.connect(); }

    @Override
    public void disconnect() throws IOException { support.disconnect(); }

    @Override
    public boolean isConnected() { return support.isConnected(); }

    public String getDriverClass() { return support.getDriverClass(); }
    public void setDriverClass(String driverClass) { support.setDriverClass(driverClass); }
    public boolean isPowerOn() { return support.isPowerOn(); }
    public void setPowerOn(boolean powerOn) { support.setPowerOn(powerOn); }
    public int getErrorCode() { return support.getErrorCode(); }
    public void setErrorCode(int errorCode) { support.setErrorCode(errorCode); }

    public long getUptimeSeconds() { return support.getUptimeSeconds(); }

    @Override
    public Map<String, Boolean> getCapabilities() { return support.getCapabilities(); }

    public void setCapability(String name, boolean enabled) { support.setCapability(name, enabled); }

    @Override
    public Map<String, String> getReadings() {
        Map<String, String> readings = support.getReadings();
        addCustomReadings(readings);
        return readings;
    }

    /**
     * Subclasses can override to add custom readings.
     */
    protected void addCustomReadings(Map<String, String> readings) {
        // Default: no extra readings
    }

    @Override
    public String getStatus() { return support.getStatus(); }

    public String getFormattedInfo() { return support.getFormattedInfo(); }

    @Override
    public void close() throws Exception { support.disconnect(); }
}
