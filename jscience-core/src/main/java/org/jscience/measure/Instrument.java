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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a physical measuring instrument.
 * Contains metadata about the instrument itself, such as serial number,
 * location, and calibration history.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public abstract class Instrument {

    private final String name;
    private final String serialNumber;
    private final String manufacturer;
    private final String model;

    private String locationDescription; // Lab room, shelf, etc.

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

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }
}
