/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.sociology;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;
import org.jscience.util.persistence.Persistent;

/**
 * Extensible enumeration for school levels (tiers).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@Persistent
public final class SchoolLevel extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final SchoolLevel PRESCHOOL = new SchoolLevel("PRESCHOOL", true);
    public static final SchoolLevel ELEMENTARY = new SchoolLevel("ELEMENTARY", true);
    public static final SchoolLevel MIDDLE = new SchoolLevel("MIDDLE", true);
    public static final SchoolLevel HIGH = new SchoolLevel("HIGH", true);
    public static final SchoolLevel UNDERGRADUATE = new SchoolLevel("UNDERGRADUATE", true);
    public static final SchoolLevel GRADUATE = new SchoolLevel("GRADUATE", true);
    public static final SchoolLevel DOCTORAL = new SchoolLevel("DOCTORAL", true);

    private final boolean builtIn;

    static {
        EnumRegistry.register(SchoolLevel.class, PRESCHOOL);
        EnumRegistry.register(SchoolLevel.class, ELEMENTARY);
        EnumRegistry.register(SchoolLevel.class, MIDDLE);
        EnumRegistry.register(SchoolLevel.class, HIGH);
        EnumRegistry.register(SchoolLevel.class, UNDERGRADUATE);
        EnumRegistry.register(SchoolLevel.class, GRADUATE);
        EnumRegistry.register(SchoolLevel.class, DOCTORAL);
    }

    public SchoolLevel(String name) {
        super(name);
        this.builtIn = false;
        EnumRegistry.register(SchoolLevel.class, this);
    }
    
    private SchoolLevel(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }
    
    public static SchoolLevel valueOf(String name) {
        return EnumRegistry.getRegistry(SchoolLevel.class).valueOfRequired(name);
    }
    
    public static SchoolLevel[] values() {
        return EnumRegistry.getRegistry(SchoolLevel.class).values().toArray(new SchoolLevel[0]);
    }
}
