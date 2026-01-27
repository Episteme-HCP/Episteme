/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.economics;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;
import org.jscience.util.persistence.Persistent;

/**
 * Extensible enumeration for Management Level.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class ManagementLevel extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final ManagementLevel OPERATIONAL = new ManagementLevel("OPERATIONAL", true);
    public static final ManagementLevel SUPERVISORY = new ManagementLevel("SUPERVISORY", true);
    public static final ManagementLevel MIDDLE_MANAGEMENT = new ManagementLevel("MIDDLE_MANAGEMENT", true);
    public static final ManagementLevel STRATEGIC = new ManagementLevel("STRATEGIC", true);
    public static final ManagementLevel EXECUTIVE = new ManagementLevel("EXECUTIVE", true);
    public static final ManagementLevel BOARD = new ManagementLevel("BOARD", true);

    private final boolean builtIn;

    static {
        EnumRegistry.register(ManagementLevel.class, OPERATIONAL);
        EnumRegistry.register(ManagementLevel.class, SUPERVISORY);
        EnumRegistry.register(ManagementLevel.class, MIDDLE_MANAGEMENT);
        EnumRegistry.register(ManagementLevel.class, STRATEGIC);
        EnumRegistry.register(ManagementLevel.class, EXECUTIVE);
        EnumRegistry.register(ManagementLevel.class, BOARD);
    }

    public ManagementLevel(String name) {
        super(name);
        this.builtIn = false;
        EnumRegistry.register(ManagementLevel.class, this);
    }

    private ManagementLevel(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }
    
    public static ManagementLevel valueOf(String name) {
        return EnumRegistry.getRegistry(ManagementLevel.class).valueOfRequired(name);
    }
    
    public static ManagementLevel[] values() {
        return EnumRegistry.getRegistry(ManagementLevel.class).values().toArray(new ManagementLevel[0]);
    }
}
