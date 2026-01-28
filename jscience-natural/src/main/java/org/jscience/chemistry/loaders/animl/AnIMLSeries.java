/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.chemistry.loaders.animl;

public class AnIMLSeries {
    private String name;
    private String seriesType;
    private double[] data;
    private String unit;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSeriesType() { return seriesType; }
    public void setSeriesType(String type) { this.seriesType = type; }

    public double[] getData() { return data; }
    public void setData(double[] data) { this.data = data; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}
