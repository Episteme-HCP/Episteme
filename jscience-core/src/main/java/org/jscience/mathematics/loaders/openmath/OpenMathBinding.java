/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.mathematics.loaders.openmath;

import java.util.Collections;
import java.util.List;

/**
 * Represents an OpenMath binding (OMBIND) - quantifier or lambda.
 */
public class OpenMathBinding {
    private final Object binder;
    private final List<OpenMathVariable> variables;
    private final Object body;
    
    public OpenMathBinding(Object binder, List<OpenMathVariable> variables, Object body) {
        this.binder = binder;
        this.variables = Collections.unmodifiableList(variables);
        this.body = body;
    }
    
    public Object getBinder() { return binder; }
    public List<OpenMathVariable> getVariables() { return variables; }
    public Object getBody() { return body; }
    
    @Override
    public String toString() {
        return "OMBIND(" + binder + ", " + variables + ", " + body + ")";
    }
}
