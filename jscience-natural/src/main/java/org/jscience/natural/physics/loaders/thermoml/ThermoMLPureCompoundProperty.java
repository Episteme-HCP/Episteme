/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.physics.loaders.thermoml;

import java.util.ArrayList;
import java.util.List;

public class ThermoMLPureCompoundProperty {
    private double value;
    private String unit;
    private final List<ThermoMLCondition> conditions = new ArrayList<>();

    private String propertyName;
    private String propertyGroup;
    private String compoundRegNum;
    private double temperature;
    private double pressure;
    private double uncertainty;

    public double getValue() { return value; }
    public void setValue(double v) { this.value = v; }

    public String getUnit() { return unit; }
    public void setUnit(String u) { this.unit = u; }

    public String getPropertyName() { return propertyName; }
    public void setPropertyName(String n) { this.propertyName = n; }

    public String getPropertyGroup() { return propertyGroup; }
    public void setPropertyGroup(String g) { this.propertyGroup = g; }

    public String getCompoundRegNum() { return compoundRegNum; }
    public void setCompoundRegNum(String r) { this.compoundRegNum = r; }

    public double getTemperature() { return temperature; }
    public void setTemperature(double t) { this.temperature = t; }

    public double getPressure() { return pressure; }
    public void setPressure(double p) { this.pressure = p; }

    public double getUncertainty() { return uncertainty; }
    public void setUncertainty(double u) { this.uncertainty = u; }
    public List<ThermoMLCondition> getConditions() { return conditions; }
    public void addCondition(ThermoMLCondition c) { conditions.add(c); }
}

