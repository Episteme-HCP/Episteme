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

package org.jscience.physics.loaders.thermoml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a ThermoML data report containing thermodynamic property data.
 * <p>
 * A data report encapsulates all information from a ThermoML document including:
 * <ul>
 *   <li>Compound information (names, formulas, identifiers)</li>
 *   <li>Property values with associated conditions and uncertainties</li>
 *   <li>Citation and provenance information</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ThermoMLDataReport {

    private final List<ThermoMLCompound> compounds = new ArrayList<>();
    private final List<ThermoMLPropertyValue> propertyValues = new ArrayList<>();
    private String title;
    private String source;
    private String doi;

    /**
     * Creates an empty data report.
     */
    public ThermoMLDataReport() {
    }

    /**
     * Returns the title of this data report.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this data report.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the source/citation of this data.
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the source/citation of this data.
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Returns the DOI of the source publication.
     */
    public String getDoi() {
        return doi;
    }

    /**
     * Sets the DOI of the source publication.
     */
    public void setDoi(String doi) {
        this.doi = doi;
    }

    /**
     * Returns an unmodifiable list of compounds in this report.
     */
    public List<ThermoMLCompound> getCompounds() {
        return Collections.unmodifiableList(compounds);
    }

    /**
     * Adds a compound to this report.
     */
    public void addCompound(ThermoMLCompound compound) {
        if (compound != null) {
            compounds.add(compound);
        }
    }

    /**
     * Returns an unmodifiable list of property values.
     */
    public List<ThermoMLPropertyValue> getPropertyValues() {
        return Collections.unmodifiableList(propertyValues);
    }

    /**
     * Adds a property value to this report.
     */
    public void addPropertyValue(ThermoMLPropertyValue value) {
        if (value != null) {
            propertyValues.add(value);
        }
    }

    /**
     * Adds multiple property values to this report.
     */
    public void addPropertyValues(List<ThermoMLPropertyValue> values) {
        if (values != null) {
            for (ThermoMLPropertyValue value : values) {
                addPropertyValue(value);
            }
        }
    }

    /**
     * Returns the number of compounds in this report.
     */
    public int getCompoundCount() {
        return compounds.size();
    }

    /**
     * Returns the number of property values in this report.
     */
    public int getPropertyValueCount() {
        return propertyValues.size();
    }

    @Override
    public String toString() {
        return "ThermoMLDataReport{" +
                "compounds=" + compounds.size() +
                ", propertyValues=" + propertyValues.size() +
                ", title='" + title + '\'' +
                '}';
    }
}
