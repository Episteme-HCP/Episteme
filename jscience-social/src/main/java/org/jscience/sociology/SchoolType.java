/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.sociology;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;
import org.jscience.util.persistence.Persistent;

/**
 * Extensible enumeration for school types.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@Persistent
public final class SchoolType extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final SchoolType PRIMARY = new SchoolType("PRIMARY", true);
    public static final SchoolType SECONDARY = new SchoolType("SECONDARY", true);
    public static final SchoolType HIGH_SCHOOL = new SchoolType("HIGH_SCHOOL", true);
    public static final SchoolType COLLEGE = new SchoolType("COLLEGE", true);
    public static final SchoolType UNIVERSITY = new SchoolType("UNIVERSITY", true);
    public static final SchoolType VOCATIONAL = new SchoolType("VOCATIONAL", true);
    public static final SchoolType ONLINE = new SchoolType("ONLINE", true);
    public static final SchoolType PRIVATE = new SchoolType("PRIVATE", true);
    public static final SchoolType PUBLIC = new SchoolType("PUBLIC", true);
    public static final SchoolType CHARTER = new SchoolType("CHARTER", true);

    private final boolean builtIn;

    static {
        EnumRegistry.register(SchoolType.class, PRIMARY);
        EnumRegistry.register(SchoolType.class, SECONDARY);
        EnumRegistry.register(SchoolType.class, HIGH_SCHOOL);
        EnumRegistry.register(SchoolType.class, COLLEGE);
        EnumRegistry.register(SchoolType.class, UNIVERSITY);
        EnumRegistry.register(SchoolType.class, VOCATIONAL);
        EnumRegistry.register(SchoolType.class, ONLINE);
        EnumRegistry.register(SchoolType.class, PRIVATE);
        EnumRegistry.register(SchoolType.class, PUBLIC);
        EnumRegistry.register(SchoolType.class, CHARTER);
    }

    public SchoolType(String name) {
        super(name);
        this.builtIn = false;
        EnumRegistry.register(SchoolType.class, this);
    }
    
    private SchoolType(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }
    
    public static SchoolType valueOf(String name) {
        return EnumRegistry.getRegistry(SchoolType.class).valueOfRequired(name);
    }
    
    public static SchoolType[] values() {
        return EnumRegistry.getRegistry(SchoolType.class).values().toArray(new SchoolType[0]);
    }
}
