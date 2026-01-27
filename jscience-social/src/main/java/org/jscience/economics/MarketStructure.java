/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.economics;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;
import org.jscience.util.persistence.Persistent;

/**
 * Extensible enumeration for Market Structure.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class MarketStructure extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final MarketStructure PERFECT_COMPETITION = new MarketStructure("PERFECT_COMPETITION", true);
    public static final MarketStructure MONOPOLISTIC_COMPETITION = new MarketStructure("MONOPOLISTIC_COMPETITION", true);
    public static final MarketStructure OLIGOPOLY = new MarketStructure("OLIGOPOLY", true);
    public static final MarketStructure MONOPOLY = new MarketStructure("MONOPOLY", true);
    public static final MarketStructure MONOPSONY = new MarketStructure("MONOPSONY", true);

    private final boolean builtIn;

    static {
        EnumRegistry.register(MarketStructure.class, PERFECT_COMPETITION);
        EnumRegistry.register(MarketStructure.class, MONOPOLISTIC_COMPETITION);
        EnumRegistry.register(MarketStructure.class, OLIGOPOLY);
        EnumRegistry.register(MarketStructure.class, MONOPOLY);
        EnumRegistry.register(MarketStructure.class, MONOPSONY);
    }

    public MarketStructure(String name) {
        super(name);
        this.builtIn = false;
        EnumRegistry.register(MarketStructure.class, this);
    }

    private MarketStructure(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }
    
    public static MarketStructure valueOf(String name) {
        return EnumRegistry.getRegistry(MarketStructure.class).valueOfRequired(name);
    }
    
    public static MarketStructure[] values() {
        return EnumRegistry.getRegistry(MarketStructure.class).values().toArray(new MarketStructure[0]);
    }
}
