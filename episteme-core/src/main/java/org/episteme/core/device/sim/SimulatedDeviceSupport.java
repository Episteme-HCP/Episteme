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
import org.episteme.core.device.Device;
import org.episteme.core.util.identity.Identification;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Helper class to provide common logic for simulated devices.
 * This avoids code duplication between SimulatedDevice, SimulatedSensor, and SimulatedActuator.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class SimulatedDeviceSupport {

    private final AbstractDevice owner;
    private long connectTime;
    private String driverClass;
    private boolean powerOn = true;
    private int errorCode = 0x00;
    private final Map<String, Boolean> capabilities = new LinkedHashMap<>();

    public SimulatedDeviceSupport(AbstractDevice owner) {
        this.owner = owner;
        initDefaultCapabilities();
        this.driverClass = "org.episteme.core.device.sim." + owner.getName().replace(" ", "");
    }

    private void initDefaultCapabilities() {
        capabilities.put("Data Logging", true);
        capabilities.put("Remote Control", true);
        capabilities.put("Asynchronous I/O", true);
        capabilities.put("High Voltage Protection", false);
    }

    public void connect() throws IOException {
        owner.setStatus(Device.Status.OPERATIONAL);
        this.connectTime = System.currentTimeMillis();
        this.powerOn = true;
    }

    public void disconnect() throws IOException {
        owner.setStatus(Device.Status.DISCONNECTED);
        this.powerOn = false;
    }

    public boolean isConnected() {
        return owner.getDeviceStatus() != Device.Status.DISCONNECTED && owner.getDeviceStatus() != Device.Status.OFFLINE;
    }

    public String getDriverClass() { return driverClass; }
    public void setDriverClass(String driverClass) { this.driverClass = driverClass; }
    public boolean isPowerOn() { return powerOn; }
    public void setPowerOn(boolean powerOn) { this.powerOn = powerOn; }
    public int getErrorCode() { return errorCode; }
    public void setErrorCode(int errorCode) { this.errorCode = errorCode; }

    public long getUptimeSeconds() {
        if (!isConnected()) return 0;
        return (System.currentTimeMillis() - connectTime) / 1000;
    }

    public Map<String, Boolean> getCapabilities() {
        return new LinkedHashMap<>(capabilities);
    }

    public void setCapability(String name, boolean enabled) {
        capabilities.put(name, enabled);
    }

    public String getStatus() {
        return isConnected() ? "Connected (Simulated)" : "Disconnected";
    }

    public String getFormattedInfo() {
        org.episteme.core.ui.i18n.I18N i18n = org.episteme.core.ui.i18n.I18N.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append(i18n.get("dashboard.devices.title", "Simulated Device")).append(": ").append(owner.getName()).append("\n");

        String statusStr = isConnected()
                ? i18n.get("dashboard.devices.connected", "Connected") + " ("
                        + i18n.get("dashboard.devices.simulated", "Simulated") + ")"
                : i18n.get("dashboard.devices.disconnected", "Disconnected");

        sb.append(i18n.get("dashboard.devices.status", "Status")).append(": ").append(statusStr).append("\n");
        sb.append(i18n.get("dashboard.devices.driver", "Driver")).append(": ").append(driverClass).append("\n");
        sb.append(i18n.get("dashboard.devices.id", "ID")).append(": ").append(owner.getId().toString()).append("\n");

        sb.append(i18n.get("dashboard.devices.manufacturer", "Manufacturer")).append(": ").append(owner.getManufacturer())
                .append("\n");
        sb.append(i18n.get("dashboard.devices.firmware", "Firmware")).append(": ").append(owner.getFirmware()).append("\n\n");

        sb.append("=== ").append(i18n.get("dashboard.devices.capabilities", "Capabilities")).append(" ===\n");
        for (Map.Entry<String, Boolean> cap : capabilities.entrySet()) {
            sb.append(" [").append(cap.getValue() ? "x" : " ").append("] ");
            sb.append(cap.getKey()).append("\n");
        }

        sb.append("\n=== ").append(i18n.get("dashboard.devices.readings", "Current Readings")).append(" ===\n");
        for (Map.Entry<String, String> reading : getReadings().entrySet()) {
            sb.append(" ").append(reading.getKey()).append(": ").append(reading.getValue()).append("\n");
        }

        return sb.toString();
    }

    public Map<String, String> getReadings() {
        Map<String, String> readings = new LinkedHashMap<>();
        readings.put("Power", powerOn ? "ON" : "OFF");
        readings.put("Uptime", getUptimeSeconds() + "s");
        readings.put("Error Code", String.format("0x%02X", errorCode));
        return readings;
    }
}
