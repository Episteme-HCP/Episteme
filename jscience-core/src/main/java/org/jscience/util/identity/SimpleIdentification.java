/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.util.identity;

/**
 * A simple string-based identification.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class SimpleIdentification extends Identification {
    
    public SimpleIdentification(String value) {
        super(value);
    }

    @Override
    public String getScheme() {
        return "Simple";
    }
}
