/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.computing.ml;

import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;

import java.io.Serializable;

/**
 * Represents a data field in an ML model.
 */
@Persistent
public class DataField implements Serializable {
    private static final long serialVersionUID = 1L;

    @Attribute
    private String name;
    @Attribute
    private String dataType;
    @Attribute
    private String opType;

    public DataField() {
    }

    public DataField(String name, String dataType, String opType) {
        this.name = name;
        this.dataType = dataType;
        this.opType = opType;
    }
}

