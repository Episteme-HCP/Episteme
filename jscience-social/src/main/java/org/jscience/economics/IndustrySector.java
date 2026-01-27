/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.economics;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;
import org.jscience.util.persistence.Persistent;

/**
 * Extensible enumeration for Industry Sector.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class IndustrySector extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final IndustrySector PRIMARY = new IndustrySector("PRIMARY", true);
    public static final IndustrySector SECONDARY = new IndustrySector("SECONDARY", true);
    public static final IndustrySector TERTIARY = new IndustrySector("TERTIARY", true);
    public static final IndustrySector QUATERNARY = new IndustrySector("QUATERNARY", true);
    public static final IndustrySector QUINARY = new IndustrySector("QUINARY", true);

    private final boolean builtIn;

    static {
        EnumRegistry.register(IndustrySector.class, PRIMARY);
        EnumRegistry.register(IndustrySector.class, SECONDARY);
        EnumRegistry.register(IndustrySector.class, TERTIARY);
        EnumRegistry.register(IndustrySector.class, QUATERNARY);
        EnumRegistry.register(IndustrySector.class, QUINARY);
    }

    public IndustrySector(String name) {
        super(name);
        this.builtIn = false;
        EnumRegistry.register(IndustrySector.class, this);
    }

    private IndustrySector(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }
    
    public static IndustrySector valueOf(String name) {
        return EnumRegistry.getRegistry(IndustrySector.class).valueOfRequired(name);
    }
    
    public static IndustrySector[] values() {
        return EnumRegistry.getRegistry(IndustrySector.class).values().toArray(new IndustrySector[0]);
    }
}
