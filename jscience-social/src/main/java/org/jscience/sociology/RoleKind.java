/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.sociology;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;
import org.jscience.util.persistence.Persistent;

/**
 * Categorical classification of social roles.
 * Modernized to extend ExtensibleEnum.
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class RoleKind extends ExtensibleEnum {

    private static final long serialVersionUID = 2L;

    public static final RoleKind CLIENT = new RoleKind("CLIENT", "Primary role of receiving a service", true);
    public static final RoleKind SERVER = new RoleKind("SERVER", "Primary role of providing a service", true);
    public static final RoleKind SUPERVISOR = new RoleKind("SUPERVISOR", "Role of monitoring or managing others", true);
    public static final RoleKind OBSERVER = new RoleKind("OBSERVER", "Role of passive observation", true);
    
    public static final RoleKind OTHER = new RoleKind("OTHER", "Other unclassified role", true);
    public static final RoleKind UNKNOWN = new RoleKind("UNKNOWN", "Unknown role", true);

    private final String description;
    private final boolean builtIn;

    static {
        EnumRegistry.register(RoleKind.class, CLIENT);
        EnumRegistry.register(RoleKind.class, SERVER);
        EnumRegistry.register(RoleKind.class, SUPERVISOR);
        EnumRegistry.register(RoleKind.class, OBSERVER);
        EnumRegistry.register(RoleKind.class, OTHER);
        EnumRegistry.register(RoleKind.class, UNKNOWN);
    }

    public RoleKind(String name, String description) {
        super(name);
        this.description = description;
        this.builtIn = false;
        EnumRegistry.register(RoleKind.class, this);
    }

    private RoleKind(String name, String description, boolean builtIn) {
        super(name);
        this.description = description;
        this.builtIn = builtIn;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    public static RoleKind valueOf(String name) {
        return EnumRegistry.getRegistry(RoleKind.class).valueOfRequired(name);
    }
    
    public static RoleKind[] values() {
        return EnumRegistry.getRegistry(RoleKind.class).values().toArray(new RoleKind[0]);
    }
}
