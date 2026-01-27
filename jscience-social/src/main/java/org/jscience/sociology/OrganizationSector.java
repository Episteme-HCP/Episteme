/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.sociology;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;
import org.jscience.util.persistence.Persistent;

/**
 * Categorical classification of organizational sectors.
 * Modernized to extend ExtensibleEnum.
 *
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class OrganizationSector extends ExtensibleEnum {

    private static final long serialVersionUID = 2L;

    public static final OrganizationSector PUBLIC = new OrganizationSector("PUBLIC", true);
    public static final OrganizationSector PRIVATE = new OrganizationSector("PRIVATE", true);
    public static final OrganizationSector NON_PROFIT = new OrganizationSector("NON_PROFIT", true);
    public static final OrganizationSector GOVERNMENT = new OrganizationSector("GOVERNMENT", true);
    public static final OrganizationSector ACADEMIC = new OrganizationSector("ACADEMIC", true);
    public static final OrganizationSector MILITARY = new OrganizationSector("MILITARY", true);
    
    public static final OrganizationSector OTHER = new OrganizationSector("OTHER", true);
    public static final OrganizationSector UNKNOWN = new OrganizationSector("UNKNOWN", true);

    private final boolean builtIn;

    static {
        EnumRegistry.register(OrganizationSector.class, PUBLIC);
        EnumRegistry.register(OrganizationSector.class, PRIVATE);
        EnumRegistry.register(OrganizationSector.class, NON_PROFIT);
        EnumRegistry.register(OrganizationSector.class, GOVERNMENT);
        EnumRegistry.register(OrganizationSector.class, ACADEMIC);
        EnumRegistry.register(OrganizationSector.class, MILITARY);
        EnumRegistry.register(OrganizationSector.class, OTHER);
        EnumRegistry.register(OrganizationSector.class, UNKNOWN);
    }

    public OrganizationSector(String name) {
        super(name);
        this.builtIn = false;
        EnumRegistry.register(OrganizationSector.class, this);
    }

    private OrganizationSector(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    public static OrganizationSector valueOf(String name) {
        return EnumRegistry.getRegistry(OrganizationSector.class).valueOfRequired(name);
    }
    
    public static OrganizationSector[] values() {
        return EnumRegistry.getRegistry(OrganizationSector.class).values().toArray(new OrganizationSector[0]);
    }
}
