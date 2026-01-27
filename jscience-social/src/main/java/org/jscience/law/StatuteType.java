/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.law;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;

/**
 * An extensible enumeration for statute types.
 *
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class StatuteType extends ExtensibleEnum {

    public static final StatuteType CONSTITUTION = new StatuteType("CONSTITUTION");
    public static final StatuteType FEDERAL_LAW = new StatuteType("FEDERAL_LAW");
    public static final StatuteType STATE_LAW = new StatuteType("STATE_LAW");
    public static final StatuteType REGULATION = new StatuteType("REGULATION");
    public static final StatuteType ORDINANCE = new StatuteType("ORDINANCE");
    public static final StatuteType TREATY = new StatuteType("TREATY");
    public static final StatuteType DIRECTIVE = new StatuteType("DIRECTIVE");
    public static final StatuteType DECREE = new StatuteType("DECREE");
    public static final StatuteType ACT = new StatuteType("ACT");
    
    public static final StatuteType OTHER = new StatuteType("OTHER");

    public StatuteType(String name) {
        super(name);
        EnumRegistry.register(StatuteType.class, this);
    }
    
    public static StatuteType valueOf(String name) {
        return EnumRegistry.getRegistry(StatuteType.class).valueOf(name);
    }
}
