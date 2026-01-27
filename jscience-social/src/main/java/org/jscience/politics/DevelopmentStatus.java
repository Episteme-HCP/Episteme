/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.politics;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;
import org.jscience.util.persistence.Persistent;

/**
 * Extensible enumeration for Development Status.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class DevelopmentStatus extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final DevelopmentStatus UNDERDEVELOPED = new DevelopmentStatus("UNDERDEVELOPED", true);
    public static final DevelopmentStatus DEVELOPING = new DevelopmentStatus("DEVELOPING", true);
    public static final DevelopmentStatus EMERGING = new DevelopmentStatus("EMERGING", true);
    public static final DevelopmentStatus DEVELOPED = new DevelopmentStatus("DEVELOPED", true);

    private final boolean builtIn;

    static {
        EnumRegistry.register(DevelopmentStatus.class, UNDERDEVELOPED);
        EnumRegistry.register(DevelopmentStatus.class, DEVELOPING);
        EnumRegistry.register(DevelopmentStatus.class, EMERGING);
        EnumRegistry.register(DevelopmentStatus.class, DEVELOPED);
    }

    public DevelopmentStatus(String name) {
        super(name);
        this.builtIn = false;
        EnumRegistry.register(DevelopmentStatus.class, this);
    }

    private DevelopmentStatus(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }
    
    public static DevelopmentStatus valueOf(String name) {
        return EnumRegistry.getRegistry(DevelopmentStatus.class).valueOfRequired(name);
    }
    
    public static DevelopmentStatus[] values() {
        return EnumRegistry.getRegistry(DevelopmentStatus.class).values().toArray(new DevelopmentStatus[0]);
    }
}
