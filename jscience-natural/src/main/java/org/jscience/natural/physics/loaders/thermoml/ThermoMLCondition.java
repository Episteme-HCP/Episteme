/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.physics.loaders.thermoml;

public class ThermoMLCondition {
    private String name;
    private double value;
    private String unit;

    public String getName() { return name; }
    public void setName(String n) { this.name = n; }
    public double getValue() { return value; }
    public void setValue(double v) { this.value = v; }
    public String getUnit() { return unit; }
    public void setUnit(String u) { this.unit = u; }
}

