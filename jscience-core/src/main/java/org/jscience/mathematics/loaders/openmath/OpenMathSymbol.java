/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.mathematics.loaders.openmath;

import java.util.Objects;

/**
 * Represents an OpenMath symbol (OMS) from a Content Dictionary.
 */
public class OpenMathSymbol {
    private final String contentDictionary;
    private final String name;
    
    public OpenMathSymbol(String contentDictionary, String name) {
        this.contentDictionary = contentDictionary;
        this.name = name;
    }
    
    public String getContentDictionary() { return contentDictionary; }
    public String getName() { return name; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OpenMathSymbol)) return false;
        OpenMathSymbol that = (OpenMathSymbol) o;
        return Objects.equals(contentDictionary, that.contentDictionary) && Objects.equals(name, that.name);
    }
    
    @Override
    public int hashCode() { return Objects.hash(contentDictionary, name); }
    
    @Override
    public String toString() { return "OMS(cd=" + contentDictionary + ", name=" + name + ")"; }
}
