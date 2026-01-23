/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.mathematics.loaders.openmath;

import java.util.Objects;

/**
 * Represents an OpenMath variable (OMV).
 */
public class OpenMathVariable {
    private final String name;
    
    public OpenMathVariable(String name) {
        this.name = name;
    }
    
    public String getName() { return name; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OpenMathVariable)) return false;
        return Objects.equals(name, ((OpenMathVariable) o).name);
    }
    
    @Override
    public int hashCode() { return Objects.hashCode(name); }
    
    @Override
    public String toString() { return "OMV(" + name + ")"; }
}
