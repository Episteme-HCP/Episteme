/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.util.identity;

import java.util.UUID;

/**
 * Universally Unique Identifier (UUID) identification.
 */
public class UUIDIdentification extends Identification {
    
    private final UUID uuid;

    public UUIDIdentification(UUID uuid) {
        super(uuid.toString());
        this.uuid = uuid;
    }

    public UUIDIdentification(String value) {
        this(UUID.fromString(value));
    }

    public UUIDIdentification() {
        this(UUID.randomUUID());
    }

    public UUID getUUID() {
        return uuid;
    }

    @Override
    public String getScheme() {
        return "UUID";
    }
}
