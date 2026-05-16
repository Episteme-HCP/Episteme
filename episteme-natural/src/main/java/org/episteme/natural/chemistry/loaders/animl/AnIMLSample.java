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

package org.episteme.natural.chemistry.loaders.animl;

/**
 * Represents a sample in AnIML analytical data.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class AnIMLSample {

    private String id;
    private String name;
    private String containerId;
    private String barcode;
    private String sourceDataLocation;

    public AnIMLSample() {
    }

    public String getSampleId() {
        return id;
    }

    public void setSampleId(String id) {
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContainerId(String id) {
        this.containerId = id;
    }

    public void setSourceDataLocation(String loc) {
        this.sourceDataLocation = loc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getContainerType() {
        return containerId; // Mapping containerId to containerType for now
    }

    public String getSourceType() {
        return "Unknown"; // Default
    }

    public String getLocation() {
        return sourceDataLocation;
    }

    @Override
    public String toString() {
        return "AnIMLSample{id='" + id + "', name='" + name + "'}";
    }
}

