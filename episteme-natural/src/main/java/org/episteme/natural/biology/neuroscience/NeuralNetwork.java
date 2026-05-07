/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.biology.neuroscience;

import org.episteme.core.util.identity.ComprehensiveIdentification;
import org.episteme.core.util.identity.Identification;
import org.episteme.core.util.identity.SimpleIdentification;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Id;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.util.persistence.Relation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a neural network composed of neurons and synapses.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class NeuralNetwork implements ComprehensiveIdentification {
    private static final long serialVersionUID = 1L;

    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Neuron> neurons = new ArrayList<>();

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Synapse> synapses = new ArrayList<>();

    public NeuralNetwork(String name) {
        this.id = new SimpleIdentification("NETWORK:" + UUID.randomUUID());
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

    public void addNeuron(Neuron neuron) {
        neurons.add(neuron);
    }

    public void addSynapse(Synapse synapse) {
        synapses.add(synapse);
    }

    public List<Neuron> getNeurons() {
        return neurons;
    }

    public List<Synapse> getSynapses() {
        return synapses;
    }

    @Override
    public String toString() {
        return String.format("NeuralNetwork[%s, %d neurons, %d synapses]", 
            getName(), neurons.size(), synapses.size());
    }
}

