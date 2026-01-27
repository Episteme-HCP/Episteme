/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;
import org.jscience.util.persistence.Persistent;


/**
 * Extensible categorization of biological tissues (e.g. Muscle, Nerve, Epithelial).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@Persistent
public final class TissueType extends ExtensibleEnum {

    private static final long serialVersionUID = 2L;

    public static final EnumRegistry<TissueType> REGISTRY = EnumRegistry.getRegistry(TissueType.class);

    public static final TissueType EPITHELIAL = new TissueType("EPITHELIAL", true);
    public static final TissueType CONNECTIVE = new TissueType("CONNECTIVE", true);
    public static final TissueType MUSCLE = new TissueType("MUSCLE", true);
    public static final TissueType NERVOUS = new TissueType("NERVOUS", true);
    public static final TissueType XYLEM = new TissueType("XYLEM", true);
    public static final TissueType PHLOEM = new TissueType("PHLOEM", true);
    public static final TissueType PARENCHYMA = new TissueType("PARENCHYMA", true);
    public static final TissueType UNKNOWN = new TissueType("UNKNOWN", true);

    private final boolean builtIn;

    public TissueType(String name) {
        this(name, false);
    }

    private TissueType(String name, boolean builtIn) {
        super(name.toUpperCase());
        this.builtIn = builtIn;
        REGISTRY.register(this);
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    public static TissueType valueOf(String name) {
        return REGISTRY.valueOf(name);
    }
}
