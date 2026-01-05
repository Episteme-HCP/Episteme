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

package org.jscience.measure;

import org.jscience.device.Device;
import org.jscience.mathematics.numbers.real.Real;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.time.temporal.ChronoUnit;

/**
 * Base class for physical measurement instruments.
 * Integrates device capabilities with measurement logic.
 * <p>
 * Provides common functionality for scientific instruments including:
 * <ul>
 * <li>Instrument identification and location (Device)</li>
 * <li>Measurement capabilities and ranges</li>
 * <li>Calibration tracking</li>
 * <li>Measurement history</li>
 * </ul>
 * </p>
 *
 * @param <Q> The type of quantity measured by this instrument.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public abstract class MeasureInstrument<Q extends Quantity<Q>> implements Device {

    /**
     * Instrument status
     */
    public enum Status {
        OPERATIONAL, CALIBRATING, NEEDS_CALIBRATION, ERROR, OFFLINE, DISCONNECTED
    }

    private final String name;
    private final String identifier; // Acts as ID/Serial Number
    private final String manufacturer;
    private final String model;
    private String locationDescription;

    private Status status = Status.DISCONNECTED;
    private Instant lastCalibration;
    private final List<Calibration> calibrationHistory = new ArrayList<>();

    private Real accuracy; // Dimensionless (percentage or ratio)
    private Real resolution;

    // Measurement State
    private Quantity<Q> currentValue;
    private Quantity<Q> minRange;
    private Quantity<Q> maxRange;

    private final List<MeasurementRecord<Q>> history = new ArrayList<>();
    private final Map<String, String> metadata = new HashMap<>();

    /**
     * Creates a new measurement instrument.
     *
     * @param name       instrument name
     * @param identifier unique identifier
     */
    protected MeasureInstrument(String name, String identifier) {
        this(name, identifier, "Unknown", "Unknown");
    }

    /**
     * Creates a new measurement instrument with full details.
     */
    protected MeasureInstrument(String name, String identifier, String manufacturer, String model) {
        this.name = Objects.requireNonNull(name, "Instrument name required");
        this.identifier = Objects.requireNonNull(identifier, "Identifier required");
        this.manufacturer = manufacturer != null ? manufacturer : "Unknown";
        this.model = model != null ? model : "Unknown";
    }

    // ========== Device Implementation ==========

    @Override
    public void connect() throws IOException {
        if (this.status != Status.OFFLINE && this.status != Status.DISCONNECTED) {
            return; // Already connected
        }
        // Simulate connection
        this.status = Status.OPERATIONAL;
    }

    @Override
    public void disconnect() throws IOException {
        this.status = Status.DISCONNECTED;
    }

    @Override
    public boolean isConnected() {
        return this.status != Status.DISCONNECTED && this.status != Status.OFFLINE;
    }

    @Override
    public void close() throws Exception {
        disconnect();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return identifier;
    }

    @Override
    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    // ========== Measurement Logic ==========

    /**
     * Returns the types of quantities this instrument can measure.
     * Usually just Q, but some instruments are multi-modal.
     */
    public abstract List<Class<? extends Quantity<?>>> getMeasurableQuantities();

    public Quantity<Q> getMinRange() {
        return minRange;
    }

    public void setMinRange(Quantity<Q> minRange) {
        this.minRange = minRange;
    }

    public Quantity<Q> getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(Quantity<Q> maxRange) {
        this.maxRange = maxRange;
    }

    public Quantity<Q> getValue() {
        return currentValue;
    }

    /**
     * Takes a measurement.
     * Subclasses should implement the hardware read logic here.
     *
     * @return the measured value
     */
    public Quantity<Q> measure() {
        if (!isConnected()) {
            throw new IllegalStateException("Instrument not connected");
        }
        // Logic to read from internal sensor or simulation
        // For simple update:
        return currentValue;
    }

    /**
     * Updates the current value (e.g. from a push update or internal loop).
     */
    public void setCurrentValue(Quantity<Q> value) {
        this.currentValue = value;
        recordMeasurement(value);
    }

    /**
     * Records a measurement in history.
     */
    protected void recordMeasurement(Quantity<Q> value) {
        history.add(new MeasurementRecord<>(Instant.now(), value));
    }

    /**
     * Returns measurement history.
     */
    public List<MeasurementRecord<Q>> getHistory() {
        return Collections.unmodifiableList(history);
    }

    /**
     * Clears measurement history.
     */
    public void clearHistory() {
        history.clear();
    }

    // ========== Calibration ==========

    /**
     * Performs calibration with a reference standard.
     *
     * @param reference known reference value
     */
    public void calibrate(Quantity<Q> reference) {
        status = Status.CALIBRATING;
        try {
            performCalibration(reference);
            lastCalibration = Instant.now();
            calibrationHistory.add(new Calibration(lastCalibration, "Routine Calibration", "Auto",
                    lastCalibration.plus(365, ChronoUnit.DAYS)));
            status = Status.OPERATIONAL;
        } catch (Exception e) {
            status = Status.ERROR;
        }
    }

    /**
     * Override to implement actual calibration logic.
     */
    protected void performCalibration(Quantity<Q> reference) {
        // Default no-op
    }

    public boolean needsCalibration(int maxAgeHours) {
        if (lastCalibration == null)
            return true;
        Instant threshold = Instant.now().minusSeconds(maxAgeHours * 3600L);
        return lastCalibration.isBefore(threshold);
    }

    public List<Calibration> getCalibrationHistory() {
        return Collections.unmodifiableList(calibrationHistory);
    }

    // ========== Metadata & Accessories ==========

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Instant getLastCalibration() {
        return lastCalibration;
    }

    public Real getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Real accuracy) {
        this.accuracy = accuracy;
    }

    public Real getResolution() {
        return resolution;
    }

    public void setResolution(Real resolution) {
        this.resolution = resolution;
    }

    public void setMetadata(String key, String value) {
        metadata.put(key, value);
    }

    public String getMetadata(String key) {
        return metadata.get(key);
    }

    @Override
    public String toString() {
        return String.format("%s %s [%s] - %s",
                manufacturer, model, identifier, status);
    }

    /**
     * Record of a single measurement.
     */
    public record MeasurementRecord<Q extends Quantity<Q>>(Instant timestamp, Quantity<Q> value) {
    }
}
