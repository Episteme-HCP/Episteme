/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.computing.ml;

import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Persistent;

import java.io.Serializable;

/**
 * Represents a mining field in an ML model.
 */
@Persistent
public class MiningField implements Serializable {
    private static final long serialVersionUID = 1L;

    @Attribute
    private String name;
    @Attribute
    private String usageType;

    public MiningField() {
    }

    public MiningField(String name, String usageType) {
        this.name = name;
        this.usageType = usageType;
    }
}

