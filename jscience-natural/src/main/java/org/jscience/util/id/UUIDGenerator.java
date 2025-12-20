/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.util.id;

import java.util.UUID;

/**
 * Generates Universally Unique Identifiers (UUIDs).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class UUIDGenerator implements IdGenerator {

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
