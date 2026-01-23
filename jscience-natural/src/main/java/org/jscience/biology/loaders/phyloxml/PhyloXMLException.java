/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.phyloxml;

/**
 * Exception thrown when PhyloXML parsing fails.
 */
public class PhyloXMLException extends Exception {
    
    public PhyloXMLException(String message) {
        super(message);
    }
    
    public PhyloXMLException(String message, Throwable cause) {
        super(message, cause);
    }
}
