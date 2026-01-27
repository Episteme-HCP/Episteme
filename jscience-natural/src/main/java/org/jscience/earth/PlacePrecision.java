/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.earth;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;
import org.jscience.util.persistence.Persistent;


/**
 * Categorical classification of geographical precision.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class PlacePrecision extends ExtensibleEnum {

    private static final long serialVersionUID = 2L;

    public static final EnumRegistry<PlacePrecision> REGISTRY = EnumRegistry.getRegistry(PlacePrecision.class);

    public static final PlacePrecision EXACT = new PlacePrecision("EXACT", true);
    public static final PlacePrecision APPROXIMATE = new PlacePrecision("APPROXIMATE", true);
    public static final PlacePrecision CITY_LEVEL = new PlacePrecision("CITY_LEVEL", true);
    public static final PlacePrecision REGION_LEVEL = new PlacePrecision("REGION_LEVEL", true);
    public static final PlacePrecision COUNTRY_LEVEL = new PlacePrecision("COUNTRY_LEVEL", true);
    public static final PlacePrecision CONTINENT_LEVEL = new PlacePrecision("CONTINENT_LEVEL", true);
    public static final PlacePrecision GLOBAL_LEVEL = new PlacePrecision("GLOBAL_LEVEL", true);
    public static final PlacePrecision UNKNOWN = new PlacePrecision("UNKNOWN", true);

    private final boolean builtIn;

    public PlacePrecision(String name) {
        this(name, false);
    }

    private PlacePrecision(String name, boolean builtIn) {
        super(name.toUpperCase());
        this.builtIn = builtIn;
        REGISTRY.register(this);
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    public static PlacePrecision valueOf(String name) {
        return REGISTRY.valueOf(name);
    }
}
