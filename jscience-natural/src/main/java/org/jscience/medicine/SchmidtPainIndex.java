/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.medicine;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;
import org.jscience.util.persistence.Persistent;

/**
 * Schmidt Sting Pain Index.
 * Modernized to use High-Precision Real and ExtensibleEnum.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@Persistent
public final class SchmidtPainIndex extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final SchmidtPainIndex SWEAT_BEE = new SchmidtPainIndex("SWEAT_BEE", Real.of("1.0"), "Light, ephemeral, almost fruity.", true);
    public static final SchmidtPainIndex FIRE_ANT = new SchmidtPainIndex("FIRE_ANT", Real.of("1.2"), "Sharp, sudden, mildly alarming.", true);
    public static final SchmidtPainIndex BULLHORN_ACACIA_ANT = new SchmidtPainIndex("BULLHORN_ACACIA_ANT", Real.of("1.8"), "Piercing, elevated. Like a staple in the cheek.", true);
    public static final SchmidtPainIndex BALD_FACED_HORNET = new SchmidtPainIndex("BALD_FACED_HORNET", Real.of("2.0"), "Rich, hearty, slightly crunchy.", true);
    public static final SchmidtPainIndex YELLOW_JACKET = new SchmidtPainIndex("YELLOW_JACKET", Real.of("2.0"), "Hot and smoky, almost irreverent.", true);
    public static final SchmidtPainIndex RED_HARVESTER_ANT = new SchmidtPainIndex("RED_HARVESTER_ANT", Real.of("3.0"), "Bold and unrelenting.", true);
    public static final SchmidtPainIndex PAPER_WASP = new SchmidtPainIndex("PAPER_WASP", Real.of("3.0"), "Caustic and burning.", true);
    public static final SchmidtPainIndex PEPSIS_WASP = new SchmidtPainIndex("PEPSIS_WASP", Real.of("4.0"), "Blinding, fierce, shockingly electric.", true);
    public static final SchmidtPainIndex BULLET_ANT = new SchmidtPainIndex("BULLET_ANT", Real.of("4.0"), "Pure, intense, brilliant pain.", true);

    private final Real value;
    private final String description;
    private final boolean builtIn;

    static {
        EnumRegistry.register(SchmidtPainIndex.class, SWEAT_BEE);
        EnumRegistry.register(SchmidtPainIndex.class, FIRE_ANT);
        EnumRegistry.register(SchmidtPainIndex.class, BULLHORN_ACACIA_ANT);
        EnumRegistry.register(SchmidtPainIndex.class, BALD_FACED_HORNET);
        EnumRegistry.register(SchmidtPainIndex.class, YELLOW_JACKET);
        EnumRegistry.register(SchmidtPainIndex.class, RED_HARVESTER_ANT);
        EnumRegistry.register(SchmidtPainIndex.class, PAPER_WASP);
        EnumRegistry.register(SchmidtPainIndex.class, PEPSIS_WASP);
        EnumRegistry.register(SchmidtPainIndex.class, BULLET_ANT);
    }

    public SchmidtPainIndex(String name, Real value, String description) {
        super(name);
        this.value = value;
        this.description = description;
        this.builtIn = false;
        EnumRegistry.register(SchmidtPainIndex.class, this);
    }
    
    private SchmidtPainIndex(String name, Real value, String description, boolean builtIn) {
        super(name);
        this.value = value;
        this.description = description;
        this.builtIn = builtIn;
    }

    public Real getValue() { return value; }
    public String getDescription() { return description; }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }
    
    public static SchmidtPainIndex valueOf(String name) {
        return EnumRegistry.getRegistry(SchmidtPainIndex.class).valueOfRequired(name);
    }
    
    public static SchmidtPainIndex[] values() {
        return EnumRegistry.getRegistry(SchmidtPainIndex.class).values().toArray(new SchmidtPainIndex[0]);
    }
}
