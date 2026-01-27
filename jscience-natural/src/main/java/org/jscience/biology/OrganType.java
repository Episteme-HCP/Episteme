/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;
import org.jscience.util.persistence.Persistent;


/**
 * Extensible categorization of biological organs (e.g. Heart, Brain, Root, Leaf).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@Persistent
public final class OrganType extends ExtensibleEnum {

    private static final long serialVersionUID = 2L;

    public static final EnumRegistry<OrganType> REGISTRY = EnumRegistry.getRegistry(OrganType.class);

    public static final OrganType HEART = new OrganType("HEART", true);
    public static final OrganType LUNG = new OrganType("LUNG", true);
    public static final OrganType BRAIN = new OrganType("BRAIN", true);
    public static final OrganType LIVER = new OrganType("LIVER", true);
    public static final OrganType KIDNEY = new OrganType("KIDNEY", true);
    public static final OrganType STOMACH = new OrganType("STOMACH", true);
    public static final OrganType INTESTINE = new OrganType("INTESTINE", true);
    public static final OrganType SKIN = new OrganType("SKIN", true);
    public static final OrganType ROOT = new OrganType("ROOT", true);
    public static final OrganType STEM = new OrganType("STEM", true);
    public static final OrganType LEAF = new OrganType("LEAF", true);
    public static final OrganType FLOWER = new OrganType("FLOWER", true);
    public static final OrganType UNKNOWN = new OrganType("UNKNOWN", true);

    private final boolean builtIn;

    public OrganType(String name) {
        this(name, false);
    }

    private OrganType(String name, boolean builtIn) {
        super(name.toUpperCase());
        this.builtIn = builtIn;
        REGISTRY.register(this);
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    public static OrganType valueOf(String name) {
        return REGISTRY.valueOf(name);
    }
}
