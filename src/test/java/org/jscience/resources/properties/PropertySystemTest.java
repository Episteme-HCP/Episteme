/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.resources.properties;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.physics.matter.Material;
import org.jscience.measure.quantity.MassDensity;

public class PropertySystemTest {

    @Test
    public void testMaterialLoading() {
        Material copper = Material.COPPER;
        assertNotNull(copper);
        assertEquals("Copper", copper.getName());

        Quantity<MassDensity> density = copper.getDensity();
        assertNotNull(density, "Density should not be null");
        assertEquals(8960.0, density.getValue().doubleValue(), 0.001);
        assertEquals(Units.KILOGRAM_PER_CUBIC_METER, density.getUnit());
    }

    @Test
    public void testUnitParsing() {
        PropertySet props = new PropertySet();
        props.set(Material.DENSITY, (Quantity) org.jscience.measure.Quantities.parse("2700 kg/m^3"));

        Quantity<MassDensity> density = props.get(Material.DENSITY);
        assertEquals(2700.0, density.getValue().doubleValue(), 0.001);
        assertEquals(Units.KILOGRAM_PER_CUBIC_METER, density.getUnit());
    }

    @Test
    public void testAutomaticConversion() {
        PropertySet props = new PropertySet();
        // Set as String, get as Quantity
        // We need to use a raw set or a helper because set(Key, Value) enforces type.
        // But PropertySet.set is generic: <T> void set(PropertyKey<T> key, T value).
        // So we can't easily set a String for a PropertyKey<Quantity>.
        // However, PropertiesLoader does exactly this by using PropertyKey.of(name,
        // String.class) or Object.class.

        // Let's simulate what PropertiesLoader does:
        PropertyKey<String> rawKey = PropertyKey.of("density", String.class);
        props.set(rawKey, "1000 kg/m^3");

        // Now retrieve using the typed key
        Quantity<MassDensity> density = props.get(Material.DENSITY);
        assertNotNull(density);
        assertEquals(1000.0, density.getValue().doubleValue(), 0.001);
    }
}

