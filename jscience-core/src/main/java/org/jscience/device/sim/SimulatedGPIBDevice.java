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

import java.io.IOException;
import java.util.Map;

/**
 * Simulated GPIB device for testing without physical hardware.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SimulatedGPIBDevice extends SimulatedDevice {

    private int gpibAddress;
    private int boardIndex = 0;
    private String lastCommand = "";
    private String simulatedResponse = "";

    public SimulatedGPIBDevice() {
        this("Generic GPIB Device", 1);
    }

    public SimulatedGPIBDevice(String name, int gpibAddress) {
        super(name);
        this.gpibAddress = gpibAddress;
        setDriverClass("org.jscience.device.sim.SimulatedGPIBDevice");

        // GPIB-specific capabilities
        setCapability("SCPI Commands", true);
        setCapability("IEEE-488.2 Compliant", true);
        setCapability("Remote Programming", true);
        setCapability("Status Byte Polling", true);
        setCapability("Service Request", true);
    }

    public int getGpibAddress() {
        return gpibAddress;
    }

    public void setGpibAddress(int address) {
        this.gpibAddress = address;
    }

    public int getBoardIndex() {
        return boardIndex;
    }

    public void setBoardIndex(int boardIndex) {
        this.boardIndex = boardIndex;
    }

    /**
     * Simulates sending a GPIB command.
     */
    public void write(String command) throws IOException {
        if (!isConnected()) {
            throw new IOException("Device not connected");
        }
        this.lastCommand = command;

        // Simulate responses for common commands
        if ("*IDN?".equals(command)) {
            simulatedResponse = "JScience Sims,SimGPIB," + getId() + "," + getFirmware();
        } else if ("*RST".equals(command)) {
            simulatedResponse = "";
        } else if ("*OPC?".equals(command)) {
            simulatedResponse = "1";
        } else if ("*STB?".equals(command)) {
            simulatedResponse = "0";
        } else if (command.endsWith("?")) {
            simulatedResponse = "0.0"; // Generic query response
        } else {
            simulatedResponse = "";
        }
    }

    /**
     * Simulates reading from the GPIB device.
     */
    public String read() throws IOException {
        if (!isConnected()) {
            throw new IOException("Device not connected");
        }
        return simulatedResponse;
    }

    /**
     * Writes a command and reads the response.
     */
    public String query(String command) throws IOException {
        write(command);
        return read();
    }

    @Override
    public String getId() {
        return String.format("GPIB%d::%d::INSTR", boardIndex, gpibAddress);
    }

    @Override
    protected void addCustomReadings(Map<String, String> readings) {
        readings.put("GPIB Address", String.valueOf(gpibAddress));
        readings.put("Board Index", String.valueOf(boardIndex));
        readings.put("Last Command", lastCommand.isEmpty() ? "N/A" : lastCommand);
        readings.put("Bus Status", "IDLE");
    }
}


