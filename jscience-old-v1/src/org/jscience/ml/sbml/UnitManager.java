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

package org.jscience.ml.sbml;

import java.util.*;

/**
 * Manages a collection of unit definitions.  This should be tied to a model, see bug #389.
 * <p/>
 * This code is licensed under the DARPA BioCOMP Open Source License.  See LICENSE for more details.
 *
 * @author Marc Vass
 * @author Nicholas Allen
 */

public final class UnitManager {
    private final static Map baseUnits;
    private final Map units;

    // These should be moved somewhere else, see bug #386
    private final Vector compartmentUnits = new Vector();
    private final Vector spatialSizeUnits = new Vector();
    private final Vector substanceUnits = new Vector();

    static {
        baseUnits = new LinkedHashMap();
        addBaseUnit("ampere");
        addBaseUnit("farad");
        addBaseUnit("joule");
        addBaseUnit("lux");
        addBaseUnit("radian");
        addBaseUnit("volt");
        addBaseUnit("becquerel");
        addBaseUnit("gram");
        addBaseUnit("katal");
        addBaseUnit("metre");
        addBaseUnit("second");
        addBaseUnit("watt");
        addBaseUnit("candela");
        addBaseUnit("gray");
        addBaseUnit("kelvin");
        addBaseUnit("mole");
        addBaseUnit("siemens");
        addBaseUnit("weber");
        addBaseUnit("Celsius");
        addBaseUnit("henry");
        addBaseUnit("kilogram");
        addBaseUnit("newton");
        addBaseUnit("sievert");
        addBaseUnit("coulomb");
        addBaseUnit("hertz");
        addBaseUnit("litre");
        addBaseUnit("ohm");
        addBaseUnit("steradian");
        addBaseUnit("dimensionless");
        addBaseUnit("item");
        addBaseUnit("lumen");
        addBaseUnit("pascal");
        addBaseUnit("tesla");
    }

    public static Unit findBaseUnit(String name) {
        return (Unit) baseUnits.get(name);
    }

    /**
     * Set[Unit] of the SBML base units.
     */

    public static Set getBaseUnits() {
        return new LinkedHashSet(baseUnits.values());
    }

    private static void addBaseUnit(String name) {
        baseUnits.put(name, new Unit(name));
    }

    /**
     * Creates a new instance of UnitManager
     */

    public UnitManager() {
        units = new LinkedHashMap();
        units.putAll(baseUnits);
        UnitDefinition substance = new UnitDefinition("substance");
        substance.getUnits().add(findBaseUnit("mole"));
        units.put("substance", substance);
        UnitDefinition volume = new UnitDefinition("volume");
        volume.getUnits().add(findBaseUnit("litre"));
        units.put("volume", volume);
        UnitDefinition area = new UnitDefinition("area");
        area.getUnits().add(new Unit(findBaseUnit("metre"), 1.0, 0, 2, 0.0));
        units.put("area", area);
        UnitDefinition length = new UnitDefinition("length");
        length.getUnits().add(findBaseUnit("metre"));
        units.put("length", length);
        UnitDefinition time = new UnitDefinition("time");
        time.getUnits().add(findBaseUnit("second"));
        units.put("time", time);
        spatialSizeUnits.add("volume");
        spatialSizeUnits.add("area");
        spatialSizeUnits.add("length");
        spatialSizeUnits.add("metre");
        spatialSizeUnits.add("litre");
        compartmentUnits.add("volume");
        compartmentUnits.add("area");
        compartmentUnits.add("length");
        compartmentUnits.add("dimensionless");
        compartmentUnits.add("metre");
        compartmentUnits.add("litre");
        substanceUnits.addAll(units.keySet());
    }

    public void addUnitDefinition(UnitDefinition definition) {
        String name = definition.getName();
        if (!isUnitDefaultName(name) && units.get(name) != null)
            throw new IllegalStateException("A unit definition named " + name + " already exists");
        units.put(name, definition);
        substanceUnits.add(name);
    }

    public Vector getCompartmentUnits() {
        return compartmentUnits;
    }

    public Vector getSpatialSizeUnits() {
        return spatialSizeUnits;
    }

    public Vector getSubstanceUnits() {
        return substanceUnits;
    }

    /**
     * A List[UnitDefinition] of known definitions.
     */

    public List getUnitDefinitions() {

        // This should be a set instead of a list, see bug #392
        return new ArrayList(units.values());
    }

    public boolean isUnit(String name) {
        return units.containsKey(name);
    }

    public void setUnitDefault(String name, UnitDefinition definition) {

        // This should be an enumerated type, see bug #387
        if (!isUnitDefaultName(name))
            throw new IllegalArgumentException(name + " is not a valid type for a unit default");
        units.put(name, definition);
    }

    private boolean isUnitDefaultName(String name) {
        return name.equals("substance") || name.equals("volume") || name.equals("area") || name.equals("length") || name.equals("time");
    }
}
