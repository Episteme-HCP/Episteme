package org.jscience.measure;

import org.jscience.geography.Coordinate;
import org.jscience.util.Named; // Assuming Named exists or will be created/used
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a physical measuring instrument.
 * Contains metadata about the instrument itself, such as serial number,
 * location, and calibration history.
 */
public abstract class Instrument {

    private final String name;
    private final String serialNumber;
    private final String manufacturer;
    private final String model;

    // Location can be a precise coordinate or a descriptive name (lab room, etc.)
    private Coordinate coordinate; // Precise location
    private String locationDescription; // Fuzzy location (e.g. "Lab 3, Shelf B")

    private final List<Calibration> calibrationHistory = new ArrayList<>();

    protected Instrument(String name, String serialNumber, String manufacturer, String model) {
        this.name = name;
        this.serialNumber = serialNumber;
        this.manufacturer = manufacturer;
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public List<Calibration> getCalibrationHistory() {
        return Collections.unmodifiableList(calibrationHistory);
    }

    public void addCalibration(Calibration calibration) {
        this.calibrationHistory.add(calibration);
    }

    public Calibration getLastCalibration() {
        if (calibrationHistory.isEmpty())
            return null;
        return calibrationHistory.get(calibrationHistory.size() - 1);
    }
}
