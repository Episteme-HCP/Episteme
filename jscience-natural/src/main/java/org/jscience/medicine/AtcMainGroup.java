/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.medicine;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;
import org.jscience.util.persistence.Persistent;

/**
 * Extensible enumeration for ATC main groups.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@Persistent
public final class AtcMainGroup extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final AtcMainGroup A = new AtcMainGroup("A", "Alimentary tract and metabolism", true);
    public static final AtcMainGroup B = new AtcMainGroup("B", "Blood and blood forming organs", true);
    public static final AtcMainGroup C = new AtcMainGroup("C", "Cardiovascular system", true);
    public static final AtcMainGroup D = new AtcMainGroup("D", "Dermatologicals", true);
    public static final AtcMainGroup G = new AtcMainGroup("G", "Genito-urinary system and sex hormones", true);
    public static final AtcMainGroup H = new AtcMainGroup("H", "Systemic hormonal preparations, excluding sex hormones and insulins", true);
    public static final AtcMainGroup J = new AtcMainGroup("J", "Anti-infectives for systemic use", true);
    public static final AtcMainGroup L = new AtcMainGroup("L", "Antineoplastic and immunomodulating agents", true);
    public static final AtcMainGroup M = new AtcMainGroup("M", "Musculo-skeletal system", true);
    public static final AtcMainGroup N = new AtcMainGroup("N", "Nervous system", true);
    public static final AtcMainGroup P = new AtcMainGroup("P", "Antiparasitic products, insecticides and repellents", true);
    public static final AtcMainGroup Q = new AtcMainGroup("Q", "Veterinary drugs", true);
    public static final AtcMainGroup R = new AtcMainGroup("R", "Respiratory system", true);
    public static final AtcMainGroup S = new AtcMainGroup("S", "Sensory organs", true);
    public static final AtcMainGroup V = new AtcMainGroup("V", "Various", true);

    private final String description;
    private final boolean builtIn;

    static {
        EnumRegistry.register(AtcMainGroup.class, A);
        EnumRegistry.register(AtcMainGroup.class, B);
        EnumRegistry.register(AtcMainGroup.class, C);
        EnumRegistry.register(AtcMainGroup.class, D);
        EnumRegistry.register(AtcMainGroup.class, G);
        EnumRegistry.register(AtcMainGroup.class, H);
        EnumRegistry.register(AtcMainGroup.class, J);
        EnumRegistry.register(AtcMainGroup.class, L);
        EnumRegistry.register(AtcMainGroup.class, M);
        EnumRegistry.register(AtcMainGroup.class, N);
        EnumRegistry.register(AtcMainGroup.class, P);
        EnumRegistry.register(AtcMainGroup.class, Q);
        EnumRegistry.register(AtcMainGroup.class, R);
        EnumRegistry.register(AtcMainGroup.class, S);
        EnumRegistry.register(AtcMainGroup.class, V);
    }

    public AtcMainGroup(String name, String description) {
        super(name);
        this.description = description;
        this.builtIn = false;
        EnumRegistry.register(AtcMainGroup.class, this);
    }
    
    private AtcMainGroup(String name, String description, boolean builtIn) {
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
    
    public static AtcMainGroup valueOf(String name) {
        return EnumRegistry.getRegistry(AtcMainGroup.class).valueOfRequired(name);
    }
    
    public static AtcMainGroup[] values() {
        return EnumRegistry.getRegistry(AtcMainGroup.class).values().toArray(new AtcMainGroup[0]);
    }
}
