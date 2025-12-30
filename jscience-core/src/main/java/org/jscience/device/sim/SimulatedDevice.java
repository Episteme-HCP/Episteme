/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.device.sim;

import org.jscience.device.Device;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Abstract base class for simulated devices with full metadata support.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public abstract class SimulatedDevice implements Device {

    private final String name;
    private final String id;
    private boolean connected;
    private long connectTime;
    private String manufacturer = "JScience Sims Inc.";
    private String firmware = "v2.1.4";
    private String driverClass;
    private boolean powerOn = true;
    private int errorCode = 0x00;

    protected final Map<String, Boolean> capabilities = new LinkedHashMap<>();

    protected SimulatedDevice(String name) {
        this.name = name;
        this.id = String.format("%08X", Math.abs(name.hashCode()));
        this.driverClass = "org.jscience.device.sim." + name.replace(" ", "");
        initDefaultCapabilities();
    }

    protected SimulatedDevice(String name, String manufacturer) {
        this(name);
        this.manufacturer = manufacturer;
    }

    private void initDefaultCapabilities() {
        capabilities.put("Data Logging", true);
        capabilities.put("Remote Control", true);
        capabilities.put("Asynchronous I/O", true);
        capabilities.put("High Voltage Protection", false);
    }

    @Override
    public void connect() throws IOException {
        this.connected = true;
        this.connectTime = System.currentTimeMillis();
        this.powerOn = true;
    }

    @Override
    public void disconnect() throws IOException {
        this.connected = false;
        this.powerOn = false;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getManufacturer() {
        return manufacturer;
    }

    @Override
    public String getFirmware() {
        return firmware;
    }

    public void setFirmware(String firmware) {
        this.firmware = firmware;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public boolean isPowerOn() {
        return powerOn;
    }

    public void setPowerOn(boolean powerOn) {
        this.powerOn = powerOn;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Returns uptime in seconds since connection.
     */
    public long getUptimeSeconds() {
        if (!connected)
            return 0;
        return (System.currentTimeMillis() - connectTime) / 1000;
    }

    @Override
    public Map<String, Boolean> getCapabilities() {
        return new LinkedHashMap<>(capabilities);
    }

    public void setCapability(String name, boolean enabled) {
        capabilities.put(name, enabled);
    }

    @Override
    public Map<String, String> getReadings() {
        Map<String, String> readings = new LinkedHashMap<>();
        readings.put("Power", powerOn ? "ON" : "OFF");
        readings.put("Uptime", getUptimeSeconds() + "s");
        readings.put("Error Code", String.format("0x%02X", errorCode));
        addCustomReadings(readings);
        return readings;
    }

    /**
     * Subclasses can override to add custom readings.
     */
    protected void addCustomReadings(Map<String, String> readings) {
        // Default: no extra readings
    }

    /**
     * Returns the device status for display.
     */
    public String getStatus() {
        return connected ? "Connected (Simulated)" : "Disconnected";
    }

    /**
     * Generates a formatted info string for dashboard display.
     */
    /**
     * Generates a formatted info string for dashboard display.
     */
    public String getFormattedInfo() {
        org.jscience.ui.i18n.I18n i18n = org.jscience.ui.i18n.I18n.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append(i18n.get("dashboard.tab.devices", "Device")).append(": ").append(name).append("\n");

        String statusStr = connected
                ? i18n.get("dashboard.devices.connected", "Connected") + " ("
                        + i18n.get("dashboard.devices.simulated", "Simulated") + ")"
                : i18n.get("dashboard.devices.disconnected", "Disconnected");

        sb.append(i18n.get("dashboard.devices.status", "Status")).append(": ").append(statusStr).append("\n");
        sb.append(i18n.get("dashboard.devices.driver", "Driver")).append(": ").append(driverClass).append("\n");
        sb.append(i18n.get("dashboard.devices.id", "ID")).append(": ").append(id).append("\n");
        sb.append(i18n.get("dashboard.devices.manufacturer", "Manufacturer")).append(": ").append(manufacturer)
                .append("\n");
        sb.append(i18n.get("dashboard.devices.firmware", "Firmware")).append(": ").append(firmware).append("\n\n");

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

    @Override
    public void close() throws Exception {
        disconnect();
    }
}
