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

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.Unit;
import org.episteme.core.util.identity.ComprehensiveIdentification;
import org.episteme.core.util.identity.Identification;

/**
 * Primary interface for all hardware and software devices in the Episteme ecosystem.
 * <p>
 * A Device represents any physical or virtual component that can be identified,
 * monitored, and controlled. This includes sensors, actuators, and complex
 * scientific instruments.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Device extends ComprehensiveIdentification, AutoCloseable {

    /**
     * Device connection and operational status.
     */
    enum Status {
        OPERATIONAL, CALIBRATING, NEEDS_CALIBRATION, ERROR, OFFLINE, DISCONNECTED
    }

    /**
     * Connects to the device.
     * 
     * @throws IOException if the connection fails
     */
    void connect() throws IOException;

    /**
     * Disconnects from the device.
     * 
     * @throws IOException if the disconnection fails
     */
    void disconnect() throws IOException;

    /**
     * Checks if the device is currently connected.
     * 
     * @return true if connected, false otherwise
     */
    boolean isConnected();

    /**
     * Returns the unique identifier for this device.
     * 
     * @return the device ID
     */
    @Override
    Identification getId();

    /**
     * Returns the manufacturer name.
     * 
     * @return the manufacturer
     */
    default String getManufacturer() {
        return "Unknown";
    }

    /**
     * Returns the model name.
     * 
     * @return the model
     */
    default String getModel() {
        return "Unknown";
    }

    /**
     * Returns the firmware version of this device.
     * 
     * @return the firmware version, or "N/A" if not applicable
     */
    default String getFirmware() {
        return "N/A";
    }

    /**
     * Returns the precision description of the device.
     * 
     * @return the precision description
     */
    default String getPrecisionDescription() {
        return "N/A";
    }

    /**
     * Returns the sensitivity of the device.
     * 
     * @return the sensitivity
     */
    default Optional<Quantity<?>> getSensitivity() {
        return Optional.empty();
    }

    /**
     * Returns the accuracy of the device.
     * 
     * @return the accuracy
     */
    default Optional<Quantity<?>> getAccuracy() {
        return Optional.empty();
    }

    /**
     * Returns the resolution of the device.
     * 
     * @return the resolution
     */
    default Optional<Quantity<?>> getResolution() {
        return Optional.empty();
    }

    /**
     * Returns the minimum measurement range.
     */
    default Optional<Quantity<?>> getMinRange() {
        return Optional.empty();
    }

    /**
     * Returns the maximum measurement range.
     */
    default Optional<Quantity<?>> getMaxRange() {
        return Optional.empty();
    }

    /**
     * Returns the types of quantities this instrument can measure.
     */
    default List<Class<? extends Quantity<?>>> getMeasurableQuantities() {
        return java.util.Collections.emptyList();
    }

    /**
     * Returns the preferred unit for display.
     */
    default Optional<Unit<?>> getDisplayUnit() {
        return Optional.empty();
    }

    /**
     * Sets the preferred unit for display.
     */
    default void setDisplayUnit(Unit<?> unit) {
        // No-op by default
    }

    /**
     * Takes a measurement.
     * 
     * @return the measured value
     */
    default Optional<Quantity<?>> measure() {
        return getValue();
    }

    /**
     * Returns the location description of the device.
     * 
     * @return the location description
     */
    default String getLocationDescription() {
        return "N/A";
    }

    /**
     * Returns the timestamp of the last calibration.
     * 
     * @return the last calibration instant
     */
    default Instant getLastCalibration() {
        return null;
    }

    /**
     * Returns the calibration history.
     */
    default List<?> getCalibrationHistory() {
        return java.util.Collections.emptyList();
    }

    /**
     * Calibrates the device.
     * 
     * @throws Exception if calibration fails
     */
    default void calibrate() throws Exception {
        // No-op by default
    }

    /**
     * Calibrates the device using a reference value.
     */
    default void calibrate(Quantity<?> reference) throws Exception {
        // No-op by default
    }

    /**
     * Checks if the device needs calibration based on its maximum age.
     */
    default boolean needsCalibration(int maxAgeHours) {
        return false;
    }

    /**
     * Checks if the device is enabled.
     * 
     * @return true if enabled
     */
    default boolean isEnabled() {
        return true;
    }

    /**
     * Sets whether the device is enabled.
     * 
     * @param enabled true to enable
     */
    default void setEnabled(boolean enabled) {
        // No-op by default
    }

    /**
     * Returns a map of capabilities and their status (active/inactive).
     * 
     * @return the capabilities map
     */
    default Map<String, Boolean> getCapabilities() {
        return java.util.Collections.emptyMap();
    }

    /**
     * Returns the current status readings (Power, Uptime, etc.).
     * 
     * @return the readings map
     */
    default Map<String, String> getReadings() {
        return java.util.Collections.emptyMap();
    }

    /**
     * Returns the current status of the device as a string.
     * 
     * @return the status string
     */
    default String getStatus() {
        return isConnected() ? Status.OPERATIONAL.name() : Status.DISCONNECTED.name();
    }

    /**
     * Returns the actual status enum if available.
     */
    default Status getDeviceStatus() {
        return isConnected() ? Status.OPERATIONAL : Status.DISCONNECTED;
    }

    /**
     * Returns the current primary value of the device (e.g. sensor reading).
     * 
     * @return the current value
     */
    Optional<Quantity<?>> getValue();

    /**
     * Returns the history of values for this device.
     * 
     * @return the value history
     */
    List<? extends Record> getHistory();

    /**
     * Represents a single record in the device history.
     */
    interface Record {
        Instant getTimestamp();
        Quantity<?> getValue();
    }
}
