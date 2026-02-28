/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.earth;

import org.episteme.core.util.EnumRegistry;
import org.episteme.core.util.ExtensibleEnum;
import org.episteme.core.util.persistence.Persistent;

/**
 * Categorical classification of geographical places.
 * Extensible to support custom regional subdivisions or celestial features.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class PlaceType extends ExtensibleEnum {

    private static final long serialVersionUID = 2L;
    
    public static final PlaceType COUNTRY = new PlaceType("COUNTRY", true);
    public static final PlaceType REGION = new PlaceType("REGION", true);
    public static final PlaceType PROVINCE = new PlaceType("PROVINCE", true);
    public static final PlaceType STATE = new PlaceType("STATE", true);
    public static final PlaceType COUNTY = new PlaceType("COUNTY", true);
    public static final PlaceType CITY = new PlaceType("CITY", true);
    public static final PlaceType TOWN = new PlaceType("TOWN", true);
    public static final PlaceType VILLAGE = new PlaceType("VILLAGE", true);
    public static final PlaceType SETTLEMENT = new PlaceType("SETTLEMENT", true);
    public static final PlaceType BUILDING = new PlaceType("BUILDING", true);
    public static final PlaceType ADDRESS = new PlaceType("ADDRESS", true);
    public static final PlaceType NATURAL_FEATURE = new PlaceType("NATURAL_FEATURE", true);
    public static final PlaceType CELESTIAL_BODY = new PlaceType("CELESTIAL_BODY", true);
    public static final PlaceType GLOBAL = new PlaceType("GLOBAL", true);
    public static final PlaceType CONTINENT = new PlaceType("CONTINENT", true);
    public static final PlaceType OCEAN = new PlaceType("OCEAN", true);
    public static final PlaceType SEA = new PlaceType("SEA", true);
    public static final PlaceType RIVER = new PlaceType("RIVER", true);
    public static final PlaceType LAKE = new PlaceType("LAKE", true);
    public static final PlaceType MOUNTAIN = new PlaceType("MOUNTAIN", true);
    public static final PlaceType PARK = new PlaceType("PARK", true);
    public static final PlaceType LANDMARK = new PlaceType("LANDMARK", true);
    
    public static final PlaceType OTHER = new PlaceType("OTHER", true);
    public static final PlaceType UNKNOWN = new PlaceType("UNKNOWN", true);



    static {
        EnumRegistry.register(PlaceType.class, COUNTRY);
        EnumRegistry.register(PlaceType.class, REGION);
        EnumRegistry.register(PlaceType.class, PROVINCE);
        EnumRegistry.register(PlaceType.class, STATE);
        EnumRegistry.register(PlaceType.class, COUNTY);
        EnumRegistry.register(PlaceType.class, CITY);
        EnumRegistry.register(PlaceType.class, TOWN);
        EnumRegistry.register(PlaceType.class, VILLAGE);
        EnumRegistry.register(PlaceType.class, SETTLEMENT);
        EnumRegistry.register(PlaceType.class, BUILDING);
        EnumRegistry.register(PlaceType.class, ADDRESS);
        EnumRegistry.register(PlaceType.class, NATURAL_FEATURE);
        EnumRegistry.register(PlaceType.class, CELESTIAL_BODY);
        EnumRegistry.register(PlaceType.class, GLOBAL);
        EnumRegistry.register(PlaceType.class, CONTINENT);
        EnumRegistry.register(PlaceType.class, OCEAN);
        EnumRegistry.register(PlaceType.class, SEA);
        EnumRegistry.register(PlaceType.class, RIVER);
        EnumRegistry.register(PlaceType.class, LAKE);
        EnumRegistry.register(PlaceType.class, MOUNTAIN);
        EnumRegistry.register(PlaceType.class, PARK);
        EnumRegistry.register(PlaceType.class, LANDMARK);
        EnumRegistry.register(PlaceType.class, OTHER);
        EnumRegistry.register(PlaceType.class, UNKNOWN);
    }

    /**
     * Creates a new user-defined (non-built-in) place type.
     * @param name Name of the place type.
     */
    public PlaceType(String name) {
        super(name);
        /* builtIn ignored as it is handled by ExtensibleEnum or ignored */
        EnumRegistry.register(PlaceType.class, this);
    }

    private PlaceType(String name, boolean builtIn) {
        super(name);
        /* builtIn ignored as it is handled by ExtensibleEnum or ignored */
    }

    public static PlaceType valueOf(String name) {
        return EnumRegistry.getRegistry(PlaceType.class).valueOfRequired(name);
    }

    public static PlaceType[] values() {
        return EnumRegistry.getRegistry(PlaceType.class).values().toArray(new PlaceType[0]);
    }
}

