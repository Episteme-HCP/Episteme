/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.chemistry.thermodynamics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.episteme.natural.chemistry.Molecule;
import org.episteme.core.measure.Quantity;
import org.episteme.core.mathematics.numbers.real.Real;

/**
 * Represents a single thermodynamic property measurement.
 */
public class ThermodynamicProperty {
    private String name;
    private Real value;
    private String unit;
    private Quantity<?> temperature;
    private Quantity<?> pressure;
    private Real uncertainty;
    private Molecule compound;
    private List<Molecule> mixtureComponents;
    private final Map<String, Object> traits = new HashMap<>();

    public ThermodynamicProperty(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Real getValue() { return value; }
    public void setValue(Real value) { this.value = value; }
    public void setValue(double value) { this.value = Real.of(value); }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public Quantity<?> getTemperature() { return temperature; }
    public void setTemperature(Quantity<?> temperature) { this.temperature = temperature; }

    public Quantity<?> getPressure() { return pressure; }
    public void setPressure(Quantity<?> pressure) { this.pressure = pressure; }

    public Real getUncertainty() { return uncertainty; }
    public void setUncertainty(Real uncertainty) { this.uncertainty = uncertainty; }
    public void setUncertainty(double uncertainty) { this.uncertainty = Real.of(uncertainty); }

    public Molecule getCompound() { return compound; }
    public void setCompound(Molecule compound) { this.compound = compound; }

    public List<Molecule> getMixtureComponents() { return mixtureComponents; }
    public void setMixtureComponents(List<Molecule> mixtureComponents) { this.mixtureComponents = mixtureComponents; }

    public void setTrait(String key, Object value) {
        traits.put(key, value);
    }

    public Object getTrait(String key) {
        return traits.get(key);
    }
}


