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

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.Unit;
import org.episteme.core.measure.Calibration;
import org.episteme.core.util.identity.Identification;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Id;
import org.episteme.core.util.persistence.Persistent;

/**
 * Base implementation for the {@link Device} interface.
 * Delegates identification and naming to an internal {@link Identification} object.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public abstract class AbstractDevice implements Device {

    @Id
    protected final Identification identification;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    // Device metadata
    @Attribute
    protected String manufacturer = "Unknown";
    @Attribute
    protected String model = "Unknown";
    @Attribute
    protected String locationDescription = "N/A";
    @Attribute
    protected String firmware = "N/A";
    @Attribute
    protected String precisionDescription = "N/A";
    @Attribute
    protected Quantity<?> sensitivity;
    @Attribute
    protected Quantity<?> accuracy;
    @Attribute
    protected Quantity<?> resolution;
    @Attribute
    protected Quantity<?> minRange;
    @Attribute
    protected Quantity<?> maxRange;

    // Device state
    @Attribute
    protected Device.Status status = Device.Status.DISCONNECTED;
    @Attribute
    protected Instant lastCalibration;
    @Attribute
    protected final List<Calibration> calibrationHistory = new ArrayList<>();
    @Attribute
    protected final List<Record> history = new ArrayList<>();
    
    @Attribute
    protected Unit<?> displayUnit;

    protected Quantity<?> currentValue;
    private boolean enabled = true;

    protected AbstractDevice(Identification identification) {
        if (identification == null) {
            throw new IllegalArgumentException("Identification cannot be null");
        }
        this.identification = identification;
    }

    @Override
    public Identification getId() {
        return identification;
    }


    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    @Override
    public Object getTrait(String key) {
        return traits.get(key);
    }

    /**
     * Returns a trait value wrapped in an Optional.
     * @param key the trait key
     * @return an Optional containing the trait value, or empty if not found
     */
    public Optional<Object> getTraitOptional(String key) {
        return Optional.ofNullable(traits.get(key));
    }

    @Override
    public String getManufacturer() {
        return manufacturer;
    }

    protected void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String getModel() {
        return model;
    }

    protected void setModel(String model) {
        this.model = model;
    }

    @Override
    public String getLocationDescription() {
        return locationDescription;
    }

    protected void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    @Override
    public String getFirmware() {
        return firmware;
    }

    protected void setFirmware(String firmware) {
        this.firmware = firmware;
    }

    @Override
    public String getPrecisionDescription() {
        return precisionDescription;
    }

    protected void setPrecisionDescription(String precisionDescription) {
        this.precisionDescription = precisionDescription;
    }

    @Override
    public Optional<Quantity<?>> getSensitivity() {
        return Optional.ofNullable(sensitivity);
    }

    protected void setSensitivity(Quantity<?> sensitivity) {
        this.sensitivity = sensitivity;
    }

    @Override
    public Optional<Quantity<?>> getAccuracy() {
        return Optional.ofNullable(accuracy);
    }

    protected void setAccuracy(Quantity<?> accuracy) {
        this.accuracy = accuracy;
    }

    @Override
    public Optional<Quantity<?>> getResolution() {
        return Optional.ofNullable(resolution);
    }

    protected void setResolution(Quantity<?> resolution) {
        this.resolution = resolution;
    }

    @Override
    public Optional<Quantity<?>> getMinRange() {
        return Optional.ofNullable(minRange);
    }

    protected void setMinRange(Quantity<?> minRange) {
        this.minRange = minRange;
    }

    @Override
    public Optional<Quantity<?>> getMaxRange() {
        return Optional.ofNullable(maxRange);
    }

    protected void setMaxRange(Quantity<?> maxRange) {
        this.maxRange = maxRange;
    }

    @Override
    public String getStatus() {
        return status.name();
    }

    @Override
    public Device.Status getDeviceStatus() {
        return status;
    }

    public void setStatus(Device.Status status) {
        this.status = status;
    }

    @Override
    public Instant getLastCalibration() {
        return lastCalibration;
    }

    protected void setLastCalibration(Instant lastCalibration) {
        this.lastCalibration = lastCalibration;
    }

    @Override
    public List<Calibration> getCalibrationHistory() {
        return Collections.unmodifiableList(calibrationHistory);
    }

    @Override
    public Optional<Unit<?>> getDisplayUnit() {
        return Optional.ofNullable(displayUnit);
    }

    @Override
    public void setDisplayUnit(Unit<?> unit) {
        this.displayUnit = unit;
    }

    @Override
    public void calibrate(Quantity<?> reference) throws Exception {
        setStatus(Device.Status.CALIBRATING);
        try {
            performCalibration(reference);
            setLastCalibration(Instant.now());
            setStatus(Device.Status.OPERATIONAL);
        } catch (Exception e) {
            setStatus(Device.Status.ERROR);
            throw e;
        }
    }

    /**
     * Performs actual calibration logic. Subclasses should override this.
     */
    protected void performCalibration(Quantity<?> reference) throws Exception {
        // Default no-op
    }

    @Override
    public boolean needsCalibration(int maxAgeHours) {
        if (lastCalibration == null) {
            return true;
        }
        Instant threshold = Instant.now().minusSeconds(maxAgeHours * 3600L);
        return lastCalibration.isBefore(threshold);
    }

    @Override
    public Optional<Quantity<?>> getValue() {
        return Optional.ofNullable(currentValue);
    }

    @Override
    public Optional<Quantity<?>> measure() {
        return getValue();
    }

    /**
     * Updates the current value and records it in history.
     */
    protected void setCurrentValue(Quantity<?> value) {
        this.currentValue = value;
        recordMeasurement(value);
    }

    @Override
    public List<Record> getHistory() {
        return Collections.unmodifiableList(history);
    }

    /**
     * Records a value in the device history with a timestamp.
     */
    protected void recordMeasurement(Quantity<?> value) {
        history.add(new Record(Instant.now(), value));
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public List<Class<? extends Quantity<?>>> getMeasurableQuantities() {
        return Collections.emptyList();
    }

    /**
     * Represents a single record in the device history.
     */
    public record Record(Instant timestamp, Quantity<?> value) implements Device.Record {
        @Override
        public Instant getTimestamp() {
            return timestamp;
        }

        @Override
        public Quantity<?> getValue() {
            return value;
        }
    }
}
