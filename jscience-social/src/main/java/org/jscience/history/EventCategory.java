/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.history;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;

/**
 * Categorization of historical or scientific events.
 * Implements the ExtensibleEnum pattern to allow new categories to be defined dynamically.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class EventCategory extends ExtensibleEnum {

    // Built-in categories
    public static final EventCategory POLITICAL = new EventCategory("POLITICAL");
    public static final EventCategory MILITARY = new EventCategory("MILITARY");
    public static final EventCategory CULTURAL = new EventCategory("CULTURAL");
    public static final EventCategory SCIENTIFIC = new EventCategory("SCIENTIFIC");
    public static final EventCategory ECONOMIC = new EventCategory("ECONOMIC");
    public static final EventCategory RELIGIOUS = new EventCategory("RELIGIOUS");
    public static final EventCategory NATURAL = new EventCategory("NATURAL");
    public static final EventCategory OTHER = new EventCategory("OTHER");
    public static final EventCategory UNKNOWN = new EventCategory("UNKNOWN");

    /**
     * Creates a new EventCategory.
     * Use this constructor to define new, custom categories.
     *
     * @param name the unique name of the category
     */
    public EventCategory(String name) {
        super(name);
        EnumRegistry.register(EventCategory.class, this);
    }
}
