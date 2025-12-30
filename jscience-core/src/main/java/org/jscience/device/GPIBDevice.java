/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.device;

import java.io.IOException;

/**
 * Abstract base class for GPIB (IEEE-488) devices.
 * <p>
 * GPIB (General Purpose Interface Bus) is a standard for connecting
 * laboratory instruments to computers. This class provides the foundation
 * for GPIB device communication.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public abstract class GPIBDevice implements Device {

    protected int gpibAddress;
    protected int boardIndex = 0;
    protected boolean connected = false;
    protected String name;
    protected String manufacturer = "Unknown";
    protected String firmware = "N/A";

    protected GPIBDevice(String name, int gpibAddress) {
        this.name = name;
        this.gpibAddress = gpibAddress;
    }

    protected GPIBDevice(String name, int gpibAddress, int boardIndex) {
        this(name, gpibAddress);
        this.boardIndex = boardIndex;
    }

    /**
     * Returns the GPIB address (0-30).
     */
    public int getGpibAddress() {
        return gpibAddress;
    }

    /**
     * Returns the GPIB board/controller index.
     */
    public int getBoardIndex() {
        return boardIndex;
    }

    /**
     * Sends a GPIB command string to the device.
     *
     * @param command the SCPI/GPIB command
     * @throws IOException if communication fails
     */
    public abstract void write(String command) throws IOException;

    /**
     * Reads a response from the device.
     *
     * @return the response string
     * @throws IOException if communication fails
     */
    public abstract String read() throws IOException;

    /**
     * Writes a command and reads the response (query).
     *
     * @param command the query command
     * @return the response
     * @throws IOException if communication fails
     */
    public String query(String command) throws IOException {
        write(command);
        return read();
    }

    /**
     * Sends the standard *IDN? identification query.
     *
     * @return the device identification string
     * @throws IOException if communication fails
     */
    public String getIdentification() throws IOException {
        return query("*IDN?");
    }

    /**
     * Resets the device using *RST command.
     *
     * @throws IOException if communication fails
     */
    public void reset() throws IOException {
        write("*RST");
    }

    /**
     * Clears device status using *CLS command.
     *
     * @throws IOException if communication fails
     */
    public void clearStatus() throws IOException {
        write("*CLS");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return String.format("GPIB%d::%d::INSTR", boardIndex, gpibAddress);
    }

    @Override
    public String getManufacturer() {
        return manufacturer;
    }

    @Override
    public String getFirmware() {
        return firmware;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void close() throws Exception {
        disconnect();
    }
}
