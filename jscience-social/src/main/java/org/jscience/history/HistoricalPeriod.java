/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.history;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;

/**
 * An extensible enumeration of major historical periods.
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class HistoricalPeriod extends ExtensibleEnum {

    public static final EnumRegistry<HistoricalPeriod> REGISTRY = new EnumRegistry<>();

    public static final HistoricalPeriod PREHISTORY = new HistoricalPeriod("PREHISTORY", true);
    public static final HistoricalPeriod ANCIENT_HISTORY = new HistoricalPeriod("ANCIENT_HISTORY", true);
    public static final HistoricalPeriod POST_CLASSICAL = new HistoricalPeriod("POST_CLASSICAL", true);
    public static final HistoricalPeriod EARLY_MODERN = new HistoricalPeriod("EARLY_MODERN", true);
    public static final HistoricalPeriod LATE_MODERN = new HistoricalPeriod("LATE_MODERN", true);
    public static final HistoricalPeriod CONTEMPORARY = new HistoricalPeriod("CONTEMPORARY", true);
    
    public static final HistoricalPeriod OTHER = new HistoricalPeriod("OTHER", true);
    public static final HistoricalPeriod UNKNOWN = new HistoricalPeriod("UNKNOWN", true);

    public HistoricalPeriod(String name) {
        this(name, false);
    }

    private HistoricalPeriod(String name, boolean builtIn) {
        super(name);
        REGISTRY.register(this);
    }
}
