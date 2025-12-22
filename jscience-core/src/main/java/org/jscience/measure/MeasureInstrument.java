/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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

import org.jscience.mathematics.numbers.real.Real;

import java.time.Instant;
import java.util.*;

/**
 * Base class for physical measurement instruments.
 * <p>
 * Provides common functionality for scientific instruments including:
 * <ul>
 * <li>Instrument identification and location</li>
 * <li>Measurement capabilities and ranges</li>
 * <li>Calibration tracking</li>
 * <li>Measurement history</li>
 * </ul>
 * </p>
 * <p>
 * Inspired by jscience-old-v1 MeasureInstrument design.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public abstract class MeasureInstrument {

    /** Instrument status */
    public enum Status {
        OPERATIONAL, CALIBRATING, NEEDS_CALIBRATION, ERROR, OFFLINE
    }

    private final String name;
    private final String identifier;
    private final String manufacturer;
    private final String model;
    private Status status = Status.OPERATIONAL;
    private Instant lastCalibration;
    private Real accuracy; // Dimensionless (percentage or ratio)
    private Real resolution;
    private final List<MeasurementRecord> history = new ArrayList<>();
    private final Map<String, String> metadata = new HashMap<>();

    /**
     * Creates a new measurement instrument.
     *
     * @param name       instrument name
     * @param identifier unique identifier
     */
    protected MeasureInstrument(String name, String identifier) {
        this.name = Objects.requireNonNull(name, "Instrument name required");
        this.identifier = Objects.requireNonNull(identifier, "Identifier required");
        this.manufacturer = "Unknown";
        this.model = "Unknown";
    }

    /**
     * Creates a new measurement instrument with full details.
     */
    protected MeasureInstrument(String name, String identifier, String manufacturer, String model) {
        this.name = Objects.requireNonNull(name);
        this.identifier = Objects.requireNonNull(identifier);
        this.manufacturer = manufacturer != null ? manufacturer : "Unknown";
        this.model = model != null ? model : "Unknown";
    }

    // ========== Abstract Methods ==========

    /**
     * Returns the types of quantities this instrument can measure.
     */
    public abstract List<Class<? extends Quantity<?>>> getMeasurableQuantities();

    /**
     * Returns the minimum value this instrument can measure.
     */
    public abstract Real getMinRange();

    /**
     * Returns the maximum value this instrument can measure.
     */
    public abstract Real getMaxRange();

    // ========== Measurement ==========

    /**
     * Takes a measurement.
     *
     * @param <Q> quantity type
     * @return the measured value
     */
    public abstract <Q extends Quantity<Q>> Quantity<Q> measure();

    /**
     * Records a measurement in history.
     */
    protected void recordMeasurement(Quantity<?> value) {
        history.add(new MeasurementRecord(Instant.now(), value));
    }

    /**
     * Returns measurement history.
     */
    public List<MeasurementRecord> getHistory() {
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
    public void calibrate(Quantity<?> reference) {
        status = Status.CALIBRATING;
        // Subclasses implement actual calibration
        performCalibration(reference);
        lastCalibration = Instant.now();
        status = Status.OPERATIONAL;
    }

    /**
     * Override to implement actual calibration logic.
     */
    protected void performCalibration(Quantity<?> reference) {
        // Default no-op
    }

    /**
     * Checks if calibration is needed based on time threshold.
     *
     * @param maxAgeHours maximum hours since last calibration
     */
    public boolean needsCalibration(int maxAgeHours) {
        if (lastCalibration == null)
            return true;
        Instant threshold = Instant.now().minusSeconds(maxAgeHours * 3600L);
        return lastCalibration.isBefore(threshold);
    }

    // ========== Accessors ==========

    public String getName() {
        return name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
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
    public record MeasurementRecord(Instant timestamp, Quantity<?> value) {
    }
}
