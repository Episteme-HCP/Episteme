/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.mathematics.loaders.openmath;

import java.util.Collections;
import java.util.List;

/**
 * Represents an OpenMath application (OMA) - function applied to arguments.
 */
public class OpenMathApplication {
    private final Object operator;
    private final List<Object> arguments;
    
    public OpenMathApplication(Object operator, List<Object> arguments) {
        this.operator = operator;
        this.arguments = Collections.unmodifiableList(arguments);
    }
    
    public Object getOperator() { return operator; }
    public List<Object> getArguments() { return arguments; }
    
    @Override
    public String toString() {
        return "OMA(" + operator + ", " + arguments + ")";
    }
}
