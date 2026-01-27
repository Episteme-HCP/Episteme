/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.economics;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;
import org.jscience.util.persistence.Persistent;

/**
 * Extensible enumeration for Economic Process.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class EconomicProcess extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final EconomicProcess PRODUCTION = new EconomicProcess("PRODUCTION", true);
    public static final EconomicProcess TRANSFORMATION = new EconomicProcess("TRANSFORMATION", true);
    public static final EconomicProcess DISTRIBUTION = new EconomicProcess("DISTRIBUTION", true);
    public static final EconomicProcess CONSUMPTION = new EconomicProcess("CONSUMPTION", true);
    public static final EconomicProcess RECYCLING = new EconomicProcess("RECYCLING", true);

    private final boolean builtIn;

    static {
        EnumRegistry.register(EconomicProcess.class, PRODUCTION);
        EnumRegistry.register(EconomicProcess.class, TRANSFORMATION);
        EnumRegistry.register(EconomicProcess.class, DISTRIBUTION);
        EnumRegistry.register(EconomicProcess.class, CONSUMPTION);
        EnumRegistry.register(EconomicProcess.class, RECYCLING);
    }

    public EconomicProcess(String name) {
        super(name);
        this.builtIn = false;
        EnumRegistry.register(EconomicProcess.class, this);
    }

    private EconomicProcess(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }
    
    public static EconomicProcess valueOf(String name) {
        return EnumRegistry.getRegistry(EconomicProcess.class).valueOfRequired(name);
    }
    
    public static EconomicProcess[] values() {
        return EnumRegistry.getRegistry(EconomicProcess.class).values().toArray(new EconomicProcess[0]);
    }
}
