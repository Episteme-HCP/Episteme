/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.util.identity;

/**
 * International Standard Book Number (ISBN) identification.
 */
public class ISBNIdentification extends Identification {
    
    public ISBNIdentification(String value) {
        super(value);
    }

    @Override
    public String getScheme() {
        return "ISBN";
    }
}
