/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.chemistry;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;
import org.jscience.util.persistence.Persistent;


/**
 * Categorization of chemical elements (e.g. Alkali Metal, Noble Gas).
 * Extends {@link ExtensibleEnum} to support new categories (e.g. Superheavy elements).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@Persistent
public final class ElementCategory extends ExtensibleEnum {

    private static final long serialVersionUID = 2L;

    public static final EnumRegistry<ElementCategory> REGISTRY = EnumRegistry.getRegistry(ElementCategory.class);

    public static final ElementCategory ALKALI_METAL = new ElementCategory("ALKALI_METAL", true);
    public static final ElementCategory ALKALINE_EARTH_METAL = new ElementCategory("ALKALINE_EARTH_METAL", true);
    public static final ElementCategory TRANSITION_METAL = new ElementCategory("TRANSITION_METAL", true);
    public static final ElementCategory POST_TRANSITION_METAL = new ElementCategory("POST_TRANSITION_METAL", true);
    public static final ElementCategory METALLOID = new ElementCategory("METALLOID", true);
    public static final ElementCategory NONMETAL = new ElementCategory("NONMETAL", true);
    public static final ElementCategory HALOGEN = new ElementCategory("HALOGEN", true);
    public static final ElementCategory NOBLE_GAS = new ElementCategory("NOBLE_GAS", true);
    public static final ElementCategory LANTHANIDE = new ElementCategory("LANTHANIDE", true);
    public static final ElementCategory ACTINIDE = new ElementCategory("ACTINIDE", true);
    public static final ElementCategory CHEMICALLY_UNKNOWN = new ElementCategory("CHEMICALLY_UNKNOWN", true);
    public static final ElementCategory UNKNOWN = new ElementCategory("UNKNOWN", true);

    private final boolean builtIn;

    public ElementCategory(String name) {
        this(name, false);
    }

    private ElementCategory(String name, boolean builtIn) {
        super(name.toUpperCase());
        this.builtIn = builtIn;
        REGISTRY.register(this);
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    public static ElementCategory valueOf(String name) {
        return REGISTRY.valueOf(name);
    }
}
