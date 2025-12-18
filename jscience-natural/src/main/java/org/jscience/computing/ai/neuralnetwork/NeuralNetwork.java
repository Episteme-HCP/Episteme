package org.jscience.computing.ai.neuralnetwork;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a simple feed-forward neural network.
 * 
 * @author Silvere Martin-Michiellot
 * @since 5.0
 */
public class NeuralNetwork {

    private final List<Layer> layers;

    public NeuralNetwork() {
        this.layers = new ArrayList<>();
    }

    public void addLayer(Layer layer) {
        if (!layers.isEmpty()) {
            Layer lastLayer = layers.get(layers.size() - 1);
            if (lastLayer.getOutputSize() != layer.getInputSize()) {
                throw new IllegalArgumentException("Input size of new layer must match output size of previous layer.");
            }
        }
        layers.add(layer);
    }

    public List<Layer> getLayers() {
        return layers;
    }
}
