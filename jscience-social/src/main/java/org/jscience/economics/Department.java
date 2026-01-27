/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.economics;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;
import org.jscience.util.persistence.Persistent;

/**
 * Extensible enumeration for Organizational Department.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class Department extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final Department HUMAN_RESOURCES = new Department("HUMAN_RESOURCES", true);
    public static final Department SALES = new Department("SALES", true);
    public static final Department MARKETING = new Department("MARKETING", true);
    public static final Department ACCOUNTING = new Department("ACCOUNTING", true);
    public static final Department FINANCE = new Department("FINANCE", true);
    public static final Department RESEARCH_AND_DEVELOPMENT = new Department("RESEARCH_AND_DEVELOPMENT", true);
    public static final Department PRODUCTION = new Department("PRODUCTION", true);
    public static final Department LOGISTICS = new Department("LOGISTICS", true);
    public static final Department DISTRIBUTION = new Department("DISTRIBUTION", true);
    public static final Department PROCUREMENT = new Department("PROCUREMENT", true);
    public static final Department LEGAL = new Department("LEGAL", true);
    public static final Department INFRASTRUCTURE = new Department("INFRASTRUCTURE", true);
    public static final Department INFORMATION_TECHNOLOGY = new Department("INFORMATION_TECHNOLOGY", true);
    public static final Department CUSTOMER_SERVICE = new Department("CUSTOMER_SERVICE", true);
    public static final Department QUALITY_ASSURANCE = new Department("QUALITY_ASSURANCE", true);

    private final boolean builtIn;

    static {
        EnumRegistry.register(Department.class, HUMAN_RESOURCES);
        EnumRegistry.register(Department.class, SALES);
        EnumRegistry.register(Department.class, MARKETING);
        EnumRegistry.register(Department.class, ACCOUNTING);
        EnumRegistry.register(Department.class, FINANCE);
        EnumRegistry.register(Department.class, RESEARCH_AND_DEVELOPMENT);
        EnumRegistry.register(Department.class, PRODUCTION);
        EnumRegistry.register(Department.class, LOGISTICS);
        EnumRegistry.register(Department.class, DISTRIBUTION);
        EnumRegistry.register(Department.class, PROCUREMENT);
        EnumRegistry.register(Department.class, LEGAL);
        EnumRegistry.register(Department.class, INFRASTRUCTURE);
        EnumRegistry.register(Department.class, INFORMATION_TECHNOLOGY);
        EnumRegistry.register(Department.class, CUSTOMER_SERVICE);
        EnumRegistry.register(Department.class, QUALITY_ASSURANCE);
    }

    public Department(String name) {
        super(name);
        this.builtIn = false;
        EnumRegistry.register(Department.class, this);
    }

    private Department(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }
    
    public static Department valueOf(String name) {
        return EnumRegistry.getRegistry(Department.class).valueOfRequired(name);
    }
    
    public static Department[] values() {
        return EnumRegistry.getRegistry(Department.class).values().toArray(new Department[0]);
    }
}
