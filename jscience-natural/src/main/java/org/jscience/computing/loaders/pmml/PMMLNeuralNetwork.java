/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.computing.loaders.pmml;

import java.util.ArrayList;
import java.util.List;

public class PMMLNeuralNetwork extends PMMLModel {
    private String activationFunction;
    private final List<PMMLNeuralLayer> neuralLayers = new ArrayList<>();

    public String getActivationFunction() { return activationFunction; }
    public void setActivationFunction(String f) { this.activationFunction = f; }
    public List<PMMLNeuralLayer> getNeuralLayers() { return neuralLayers; }
    public void addNeuralLayer(PMMLNeuralLayer l) { neuralLayers.add(l); }
}

class PMMLNeuralLayer {
    private int numberOfNeurons;
    private String activationFunction;
    private final List<PMMLNeuron> neurons = new ArrayList<>();

    public int getNumberOfNeurons() { return numberOfNeurons; }
    public void setNumberOfNeurons(int n) { this.numberOfNeurons = n; }
    public String getActivationFunction() { return activationFunction; }
    public void setActivationFunction(String f) { this.activationFunction = f; }
    public List<PMMLNeuron> getNeurons() { return neurons; }
    public void addNeuron(PMMLNeuron n) { neurons.add(n); }
}

class PMMLNeuron {
    private String id;
    private double bias;
    private final List<PMMLConnection> connections = new ArrayList<>();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public double getBias() { return bias; }
    public void setBias(double b) { this.bias = b; }
    public List<PMMLConnection> getConnections() { return connections; }
    public void addConnection(PMMLConnection c) { connections.add(c); }
}

class PMMLConnection {
    private String from;
    private double weight;

    public String getFrom() { return from; }
    public void setFrom(String f) { this.from = f; }
    public double getWeight() { return weight; }
    public void setWeight(double w) { this.weight = w; }
}
