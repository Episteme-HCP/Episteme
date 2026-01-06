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

package org.jscience.physics.classical.matter;

import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.ElectricConductivity;
import org.jscience.measure.quantity.MassDensity;
import org.jscience.measure.quantity.Pressure;
import org.jscience.measure.quantity.ThermalConductivity;

import org.jscience.io.properties.PropertiesReader;
import org.jscience.io.properties.PropertyKey;
import org.jscience.io.properties.PropertySet;

/**
 * Represents a physical material with standard properties.
 * Properties are loaded dynamically from JSON resources.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Material {

    // Standard Property Keys
    @SuppressWarnings("unchecked")
    public static final PropertyKey<Quantity<MassDensity>> DENSITY = PropertyKey.of("density",
            (Class<Quantity<MassDensity>>) (Class<?>) Quantity.class);

    @SuppressWarnings("unchecked")
    public static final PropertyKey<Quantity<?>> SPECIFIC_HEAT = PropertyKey.of("specific_heat",
            (Class<Quantity<?>>) (Class<?>) Quantity.class);

    @SuppressWarnings("unchecked")
    public static final PropertyKey<Quantity<ThermalConductivity>> THERMAL_CONDUCTIVITY = PropertyKey
            .of("thermal_conductivity", (Class<Quantity<ThermalConductivity>>) (Class<?>) Quantity.class);

    @SuppressWarnings("unchecked")
    public static final PropertyKey<Quantity<ElectricConductivity>> ELECTRICAL_CONDUCTIVITY = PropertyKey
            .of("electrical_conductivity", (Class<Quantity<ElectricConductivity>>) (Class<?>) Quantity.class);

    @SuppressWarnings("unchecked")
    public static final PropertyKey<Quantity<Pressure>> YOUNGS_MODULUS = PropertyKey.of("youngs_modulus",
            (Class<Quantity<Pressure>>) (Class<?>) Quantity.class);

    private final String name;
    private final PropertySet properties;

    /**
     * Creates a new Material by loading properties from the default resource.
     * 
     * @param name The name of the material (must match an entry in materials.json).
     */
    public Material(String name) {
        this.name = name;
        this.properties = PropertiesReader.load("/org/jscience/physics/classical/matter/materials.json", name);
    }

    /**
     * Creates a custom Material with manually defined properties.
     */
    public Material(String name, PropertySet properties) {
        this.name = name;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public <T> T get(PropertyKey<T> key) {
        return properties.get(key);
    }

    // Convenience methods for standard properties
    public Quantity<MassDensity> getDensity() {
        return get(DENSITY);
    }

    public Quantity<?> getSpecificHeat() {
        return get(SPECIFIC_HEAT);
    }

    public Quantity<ThermalConductivity> getThermalConductivity() {
        return get(THERMAL_CONDUCTIVITY);
    }

    public Quantity<ElectricConductivity> getElectricalConductivity() {
        return get(ELECTRICAL_CONDUCTIVITY);
    }

    public Quantity<Pressure> getYoungsModulus() {
        return get(YOUNGS_MODULUS);
    }

    // Common Materials (Loaded on demand or statically)
    public static final Material COPPER = new Material("Copper");
    public static final Material ALUMINUM = new Material("Aluminum");
    public static final Material IRON = new Material("Iron");
    public static final Material STEEL = new Material("Steel");
    public static final Material SILICON = new Material("Silicon");
    public static final Material WATER = new Material("Water");
    public static final Material AIR = new Material("Air");
}
// IDE re-indexing touch
