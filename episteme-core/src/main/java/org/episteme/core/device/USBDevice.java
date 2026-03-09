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

package org.episteme.core.device;

import org.episteme.core.util.identity.SimpleIdentification;
import java.io.IOException;

/**
 * Abstract base class for USB-connected devices.
 * <p>
 * This class provides the foundation for communication with USB-based
 * laboratory devices including sensors (input) and actuators (output).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public abstract class USBDevice extends AbstractDevice {

    protected int vendorId;
    protected int productId;
    protected String serialNumber;

    protected USBDevice(String name) {
        super(new SimpleIdentification("USB:UNKNOWN"));
        super.setTrait("name", name);
    }

    protected USBDevice(String name, int vendorId, int productId) {
        super(new SimpleIdentification(String.format("USB:%04X:%04X", vendorId, productId)));
        super.setTrait("name", name);
        this.vendorId = vendorId;
        this.productId = productId;
    }

    /**
     * Returns the USB Vendor ID.
     */
    public int getVendorId() {
        return vendorId;
    }

    /**
     * Returns the USB Product ID.
     */
    public int getProductId() {
        return productId;
    }

    /**
     * Returns the USB serial number.
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * Sends raw bytes to the USB device.
     */
    public abstract void write(byte[] data) throws IOException;

    /**
     * Reads raw bytes from the USB device.
     */
    public abstract byte[] read(int length) throws IOException;

    /**
     * Sends a command string to the device.
     */
    public void sendCommand(String command) throws IOException {
        write(command.getBytes());
    }

    /**
     * Reads a response string from the device.
     */
    public String readResponse(int maxLength) throws IOException {
        byte[] data = read(maxLength);
        return new String(data).trim();
    }

    @Override
    public boolean isConnected() {
        return getDeviceStatus() != Device.Status.DISCONNECTED && getDeviceStatus() != Device.Status.OFFLINE;
    }

    @Override
    public void close() throws Exception {
        disconnect();
    }
}



