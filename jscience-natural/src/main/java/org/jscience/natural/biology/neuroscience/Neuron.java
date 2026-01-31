/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.biology.neuroscience;

import org.jscience.core.util.identity.ComprehensiveIdentification;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.SimpleIdentification;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Id;
import org.jscience.core.util.persistence.Persistent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a neuron in a neural network.
 * <p>
 * This class models the biological neuron with morphological and biophysical properties.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Neuron implements ComprehensiveIdentification {
    private static final long serialVersionUID = 1L;

    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    @Attribute
    private NeuronMorphology morphology;

    public Neuron(String name) {
        this.id = new SimpleIdentification("NEURON:" + UUID.randomUUID());
        setName(name);
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    public NeuronMorphology getMorphology() {
        return morphology;
    }

    public void setMorphology(NeuronMorphology morphology) {
        this.morphology = morphology;
    }

    public Neuron copy() {
        Neuron copy = new Neuron(getName());
        copy.traits.putAll(this.traits);
        copy.morphology = this.morphology;
        return copy;
    }

    @Override
    public String toString() {
        return String.format("Neuron[%s]", getName());
    }
}

