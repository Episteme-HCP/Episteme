/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.sociology;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;
import org.jscience.util.persistence.Persistent;

/**
 * An extensible enumeration for social group classifications.
 * Modernized to extend ExtensibleEnum.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@Persistent
public final class GroupKind extends ExtensibleEnum {

    private static final long serialVersionUID = 2L;

    public static final GroupKind FAMILY = new GroupKind("FAMILY", true);
    public static final GroupKind COMMUNITY = new GroupKind("COMMUNITY", true);
    public static final GroupKind ORGANIZATION = new GroupKind("ORGANIZATION", true);
    public static final GroupKind NATION = new GroupKind("NATION", true);
    public static final GroupKind TRIBE = new GroupKind("TRIBE", true);
    public static final GroupKind TEAM = new GroupKind("TEAM", true);
    public static final GroupKind CLASS = new GroupKind("CLASS", true);
    public static final GroupKind NETWORK = new GroupKind("NETWORK", true);
    public static final GroupKind OTHER = new GroupKind("OTHER", true);
    public static final GroupKind UNKNOWN = new GroupKind("UNKNOWN", true);

    private final boolean builtIn;

    static {
        EnumRegistry.register(GroupKind.class, FAMILY);
        EnumRegistry.register(GroupKind.class, COMMUNITY);
        EnumRegistry.register(GroupKind.class, ORGANIZATION);
        EnumRegistry.register(GroupKind.class, NATION);
        EnumRegistry.register(GroupKind.class, TRIBE);
        EnumRegistry.register(GroupKind.class, TEAM);
        EnumRegistry.register(GroupKind.class, CLASS);
        EnumRegistry.register(GroupKind.class, NETWORK);
        EnumRegistry.register(GroupKind.class, OTHER);
        EnumRegistry.register(GroupKind.class, UNKNOWN);
    }

    public GroupKind(String name) {
        super(name);
        this.builtIn = false;
        EnumRegistry.register(GroupKind.class, this);
    }

    private GroupKind(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    public static GroupKind valueOf(String name) {
        return EnumRegistry.getRegistry(GroupKind.class).valueOfRequired(name);
    }
    
    public static GroupKind[] values() {
        return EnumRegistry.getRegistry(GroupKind.class).values().toArray(new GroupKind[0]);
    }
}
