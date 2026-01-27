/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.util.identity;

/**
 * Digital Object Identifier (DOI) identification.
 */
public class DOIIdentification extends Identification {
    
    public DOIIdentification(String value) {
        super(value);
    }

    @Override
    public String getScheme() {
        return "DOI";
    }
}
