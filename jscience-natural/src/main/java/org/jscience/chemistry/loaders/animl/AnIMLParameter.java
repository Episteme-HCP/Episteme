/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.chemistry.loaders.animl;

public class AnIMLParameter {
    private String name;
    private Object value;

    public AnIMLParameter(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() { return name; }
    public Object getValue() { return value; }
}
