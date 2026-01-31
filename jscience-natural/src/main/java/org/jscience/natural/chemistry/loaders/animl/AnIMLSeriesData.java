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

package org.jscience.natural.chemistry.loaders.animl;

import org.jscience.core.mathematics.linearalgebra.vectors.RealDoubleVector;

/**
 * Represents a data series in AnIML analytical data.
 * <p>
 * A series contains numerical data from an analytical measurement,
 * such as wavelengths, absorbances, retention times, or intensities.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class AnIMLSeriesData {

    private String name;
    private String seriesId;
    private String dependency; // "independent" or "dependent"
    private String plotScale;  // "linear" or "logarithmic"
    private String unitLabel;
    private String unitQuantity;
    
    private double[] values;
    private double[] autoIncrementedValues;
    private byte[] encodedData;
    
    private double startValue;
    private double increment;

    public AnIMLSeriesData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public String getDependency() {
        return dependency;
    }

    public void setDependency(String dependency) {
        this.dependency = dependency;
    }

    public String getPlotScale() {
        return plotScale;
    }

    public void setPlotScale(String plotScale) {
        this.plotScale = plotScale;
    }

    public String getUnitLabel() {
        return unitLabel;
    }

    public void setUnitLabel(String unitLabel) {
        this.unitLabel = unitLabel;
    }

    public String getUnitQuantity() {
        return unitQuantity;
    }

    public void setUnitQuantity(String unitQuantity) {
        this.unitQuantity = unitQuantity;
    }

    public double[] getValues() {
        return values;
    }

    public void setValues(double[] values) {
        this.values = values;
    }

    public double[] getAutoIncrementedValues() {
        return autoIncrementedValues;
    }

    public void setAutoIncrementedValues(double[] autoIncrementedValues) {
        this.autoIncrementedValues = autoIncrementedValues;
    }

    public byte[] getEncodedData() {
        return encodedData;
    }

    public void setEncodedData(byte[] encodedData) {
        this.encodedData = encodedData;
    }

    public double getStartValue() {
        return startValue;
    }

    public void setStartValue(double startValue) {
        this.startValue = startValue;
    }

    public double getIncrement() {
        return increment;
    }

    public void setIncrement(double increment) {
        this.increment = increment;
    }

    /**
     * Returns true if this is an independent variable (e.g., wavelength, time).
     */
    public boolean isIndependent() {
        return "independent".equalsIgnoreCase(dependency);
    }

    /**
     * Returns true if this is a dependent variable (e.g., absorbance, intensity).
     */
    public boolean isDependent() {
        return "dependent".equalsIgnoreCase(dependency);
    }

    /**
     * Returns the length of the data series.
     */
    public int getLength() {
        if (values != null) {
            return values.length;
        }
        if (autoIncrementedValues != null) {
            return autoIncrementedValues.length;
        }
        return 0;
    }

    /**
     * Returns the data as a JScience RealDoubleVector.
     */
    public RealDoubleVector toVector() {
        double[] data = getEffectiveValues();
        if (data != null && data.length > 0) {
            return RealDoubleVector.of(data);
        }
        return null;
    }

    /**
     * Returns the effective values (explicit or auto-incremented).
     */
    public double[] getEffectiveValues() {
        if (values != null && values.length > 0) {
            return values;
        }
        return autoIncrementedValues;
    }

    @Override
    public String toString() {
        return "AnIMLSeriesData{" +
                "name='" + name + '\'' +
                ", dependency='" + dependency + '\'' +
                ", unit='" + unitLabel + '\'' +
                ", length=" + getLength() +
                '}';
    }
}

