/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.computing.ml;

import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a neural network model.
 */
@Persistent
public class NeuralNetworkModel extends Model {
    private static final long serialVersionUID = 1L;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Layer> layers = new ArrayList<>();

    public NeuralNetworkModel() {
    }

    public NeuralNetworkModel(String name) {
        super(name);
    }

    public void addLayer(Layer layer) {
        layers.add(layer);
    }

    @Persistent
    public static class Layer implements Serializable {
        @Attribute
        private int neuronCount;
        @Attribute
        private String activation;

        @Relation(type = Relation.Type.ONE_TO_MANY)
        private final List<Neuron> neurons = new ArrayList<>();

        public Layer() {}
        public Layer(int count) { this.neuronCount = count; }
        public void setActivation(String activation) { this.activation = activation; }
        public void addNeuron(Neuron neuron) { neurons.add(neuron); }
    }

    @Persistent
    public static class Neuron implements Serializable {
        @Attribute
        private String id;
        @Attribute
        private double bias;
        @Attribute
        private final java.util.Map<String, Double> weights = new java.util.HashMap<>();

        public Neuron() {}
        public Neuron(String id) { this.id = id; }
        public void setBias(double bias) { this.bias = bias; }
        public void addWeight(String from, double weight) { weights.put(from, weight); }
    }
}

