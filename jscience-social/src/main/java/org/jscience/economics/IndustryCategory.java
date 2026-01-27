/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.economics;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;
import org.jscience.util.persistence.Persistent;

/**
 * Extensible enumeration for Industry Category.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class IndustryCategory extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final IndustryCategory TRANSPORT = new IndustryCategory("TRANSPORT", true);
    public static final IndustryCategory HYGIENE = new IndustryCategory("HYGIENE", true);
    public static final IndustryCategory ALIMENTATION = new IndustryCategory("ALIMENTATION", true);
    public static final IndustryCategory HABITAT = new IndustryCategory("HABITAT", true);
    public static final IndustryCategory TECHNOLOGY = new IndustryCategory("TECHNOLOGY", true);
    public static final IndustryCategory ENERGY = new IndustryCategory("ENERGY", true);
    public static final IndustryCategory SOCIETY = new IndustryCategory("SOCIETY", true);
    public static final IndustryCategory FINANCE = new IndustryCategory("FINANCE", true);
    public static final IndustryCategory EDUCATION = new IndustryCategory("EDUCATION", true);
    public static final IndustryCategory HEALTHCARE = new IndustryCategory("HEALTHCARE", true);
    public static final IndustryCategory OTHER = new IndustryCategory("OTHER", true);

    private final boolean builtIn;

    static {
        EnumRegistry.register(IndustryCategory.class, TRANSPORT);
        EnumRegistry.register(IndustryCategory.class, HYGIENE);
        EnumRegistry.register(IndustryCategory.class, ALIMENTATION);
        EnumRegistry.register(IndustryCategory.class, HABITAT);
        EnumRegistry.register(IndustryCategory.class, TECHNOLOGY);
        EnumRegistry.register(IndustryCategory.class, ENERGY);
        EnumRegistry.register(IndustryCategory.class, SOCIETY);
        EnumRegistry.register(IndustryCategory.class, FINANCE);
        EnumRegistry.register(IndustryCategory.class, EDUCATION);
        EnumRegistry.register(IndustryCategory.class, HEALTHCARE);
        EnumRegistry.register(IndustryCategory.class, OTHER);
    }

    public IndustryCategory(String name) {
        super(name);
        this.builtIn = false;
        EnumRegistry.register(IndustryCategory.class, this);
    }

    private IndustryCategory(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }
    
    public static IndustryCategory valueOf(String name) {
        return EnumRegistry.getRegistry(IndustryCategory.class).valueOfRequired(name);
    }
    
    public static IndustryCategory[] values() {
        return EnumRegistry.getRegistry(IndustryCategory.class).values().toArray(new IndustryCategory[0]);
    }
}
