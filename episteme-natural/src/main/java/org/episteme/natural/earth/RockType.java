/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.earth;

import org.episteme.core.util.EnumRegistry;
import org.episteme.core.util.ExtensibleEnum;
import org.episteme.core.util.persistence.Persistent;

/**
 * Extensible categorization of rocks.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@Persistent
public final class RockType extends ExtensibleEnum {

    private static final long serialVersionUID = 2L;

    public static final EnumRegistry<RockType> REGISTRY = EnumRegistry.getRegistry(RockType.class);

    public static final RockType SEDIMENTARY_CLASTIC = new RockType("SEDIMENTARY_CLASTIC", true);
    public static final RockType SEDIMENTARY_BIOGENIC = new RockType("SEDIMENTARY_BIOGENIC", true);
    public static final RockType SEDIMENTARY_CHEMICAL = new RockType("SEDIMENTARY_CHEMICAL", true);
    public static final RockType METAMORPHIC_FOLIATED = new RockType("METAMORPHIC_FOLIATED", true);
    public static final RockType METAMORPHIC_NON_FOLIATED = new RockType("METAMORPHIC_NON_FOLIATED", true);
    public static final RockType IGNEOUS_PLUTONIC = new RockType("IGNEOUS_PLUTONIC", true);
    public static final RockType IGNEOUS_VOLCANIC = new RockType("IGNEOUS_VOLCANIC", true);
    public static final RockType UNKNOWN = new RockType("UNKNOWN", true);

    private final boolean builtIn;

    public RockType(String name) {
        this(name, false);
    }

    private RockType(String name, boolean builtIn) {
        super(name.toUpperCase());
        this.builtIn = builtIn;
        REGISTRY.register(this);
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    public static RockType valueOf(String name) {
        return REGISTRY.valueOf(name);
    }

    public static RockType[] values() {
        return REGISTRY.values().toArray(new RockType[0]);
    }
}
