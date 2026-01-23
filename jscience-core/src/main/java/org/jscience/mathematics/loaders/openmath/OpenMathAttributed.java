/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.mathematics.loaders.openmath;

import java.util.Collections;
import java.util.Map;

/**
 * Represents an OpenMath attributed object (OMATTR).
 */
public class OpenMathAttributed {
    private final Object content;
    private final Map<OpenMathSymbol, Object> attributes;
    
    public OpenMathAttributed(Object content, Map<OpenMathSymbol, Object> attributes) {
        this.content = content;
        this.attributes = Collections.unmodifiableMap(attributes);
    }
    
    public Object getContent() { return content; }
    public Map<OpenMathSymbol, Object> getAttributes() { return attributes; }
    public Object getAttribute(OpenMathSymbol key) { return attributes.get(key); }
    
    @Override
    public String toString() {
        return "OMATTR(" + content + ", attrs=" + attributes.size() + ")";
    }
}
