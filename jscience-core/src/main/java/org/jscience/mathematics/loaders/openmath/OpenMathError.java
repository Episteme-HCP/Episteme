/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.mathematics.loaders.openmath;

import java.util.Collections;
import java.util.List;

/**
 * Represents an OpenMath error (OME).
 */
public class OpenMathError {
    private final OpenMathSymbol errorSymbol;
    private final List<Object> arguments;
    
    public OpenMathError(OpenMathSymbol errorSymbol, List<Object> arguments) {
        this.errorSymbol = errorSymbol;
        this.arguments = Collections.unmodifiableList(arguments);
    }
    
    public OpenMathSymbol getErrorSymbol() { return errorSymbol; }
    public List<Object> getArguments() { return arguments; }
    
    @Override
    public String toString() {
        return "OME(" + errorSymbol + ", " + arguments + ")";
    }
}
