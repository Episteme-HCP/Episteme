/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.methodology.experiment;

import java.io.Serializable;
import java.util.Objects;
import org.jscience.util.Named;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.Identified;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;

/**
 * Represents a measurable or manipulable variable in a scientific experiment.
 * Suitable for psychology, clinical trials, and other experimental sciences.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public class Variable implements Named, Identified<Identification>, Serializable {

    private static final long serialVersionUID = 2L;

    @Id
    private final Identification id;

    @Attribute
    private final String name;

    @Attribute
    private final String unit;

    public Variable(String name, String unit) {
        this.id = new org.jscience.util.identity.UUIDIdentification(java.util.UUID.randomUUID());
        this.name = Objects.requireNonNull(name, "Variable name cannot be null");
        this.unit = Objects.requireNonNull(unit, "Variable unit cannot be null");
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return name + " (" + unit + ")";
    }
}
