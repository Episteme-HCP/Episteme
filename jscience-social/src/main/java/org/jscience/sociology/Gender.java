/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.sociology;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;
import org.jscience.util.persistence.Persistent;
import org.jscience.biology.BiologicalSex;

/**
 * Extensible gender categories for social and psychological modeling.
 * Distinguishes social gender from biological sex.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@Persistent
public final class Gender extends ExtensibleEnum {

    private static final long serialVersionUID = 2L;

    public static final EnumRegistry<Gender> REGISTRY = EnumRegistry.getRegistry(Gender.class);

    public static final Gender MALE = new Gender("MALE", true);
    public static final Gender FEMALE = new Gender("FEMALE", true);
    public static final Gender NON_BINARY = new Gender("NON_BINARY", true);
    public static final Gender TRANSGENDER = new Gender("TRANSGENDER", true);
    public static final Gender AGENDER = new Gender("AGENDER", true);
    public static final Gender OTHER = new Gender("OTHER", true);
    public static final Gender UNKNOWN = new Gender("UNKNOWN", true);

    private final boolean builtIn;

    public Gender(String name) {
        this(name, false);
    }

    private Gender(String name, boolean builtIn) {
        super(name.toUpperCase());
        this.builtIn = builtIn;
        REGISTRY.register(this);
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    /**
     * Maps social gender to biological sex.
     */
    public BiologicalSex toSex() {
        if (this == MALE) return BiologicalSex.MALE;
        if (this == FEMALE) return BiologicalSex.FEMALE;
        return BiologicalSex.UNKNOWN;
    }

    /**
     * Maps biological sex to social gender.
     */
    public static Gender fromSex(BiologicalSex sex) {
        if (sex == BiologicalSex.MALE) return MALE;
        if (sex == BiologicalSex.FEMALE) return FEMALE;
        return UNKNOWN;
    }

    public static Gender valueOf(String name) {
        return REGISTRY.valueOf(name);
    }
}
