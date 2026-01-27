/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.law;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;

/**
 * An extensible enumeration for statute statuses.
 *
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class StatuteStatus extends ExtensibleEnum {

    public static final StatuteStatus PROPOSED = new StatuteStatus("PROPOSED");
    public static final StatuteStatus ENACTED = new StatuteStatus("ENACTED");
    public static final StatuteStatus AMENDED = new StatuteStatus("AMENDED");
    public static final StatuteStatus REPEALED = new StatuteStatus("REPEALED");

    public StatuteStatus(String name) {
        super(name);
        EnumRegistry.register(StatuteStatus.class, this);
    }
    
    public static StatuteStatus valueOf(String name) {
        return EnumRegistry.getRegistry(StatuteStatus.class).valueOf(name);
    }
}
