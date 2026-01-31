/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.physics.loaders.thermoml;

import java.util.ArrayList;
import java.util.List;

public class ThermoMLMixtureProperty {
    private String propertyName;
    private double value;
    private String unit;
    private double temperature;
    private double pressure;
    private final List<ThermoMLCondition> conditions = new ArrayList<>();
    private final List<ThermoMLMixtureComponent> components = new ArrayList<>();

    public String getPropertyName() { return propertyName; }
    public void setPropertyName(String n) { this.propertyName = n; }

    public double getValue() { return value; }
    public void setValue(double v) { this.value = v; }

    public String getUnit() { return unit; }
    public void setUnit(String u) { this.unit = u; }

    public double getTemperature() { return temperature; }
    public void setTemperature(double t) { this.temperature = t; }

    public double getPressure() { return pressure; }
    public void setPressure(double p) { this.pressure = p; }

    public List<ThermoMLCondition> getConditions() { return conditions; }
    public void addCondition(ThermoMLCondition c) { conditions.add(c); }

    public List<ThermoMLMixtureComponent> getComponents() { return components; }
    public void addComponent(ThermoMLMixtureComponent c) { components.add(c); }
}

