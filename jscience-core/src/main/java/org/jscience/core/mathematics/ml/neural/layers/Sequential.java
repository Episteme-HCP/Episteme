package org.jscience.core.mathematics.ml.neural.layers;

import org.jscience.core.mathematics.ml.neural.autograd.GraphNode;
import java.util.ArrayList;
import java.util.List;

/**
 * Container for sequential execution of layers.
 */
public class Sequential<T> implements Layer<T> {
    private final List<Layer<T>> layers = new ArrayList<>();

    public void add(Layer<T> layer) {
        layers.add(layer);
    }

    @Override
    public GraphNode<T> forward(GraphNode<T> input) {
        GraphNode<T> current = input;
        for (Layer<T> layer : layers) {
            current = layer.forward(current);
        }
        return current;
    }

    @Override
    public List<GraphNode<T>> getParameters() {
        List<GraphNode<T>> params = new ArrayList<>();
        for (Layer<T> layer : layers) {
            params.addAll(layer.getParameters());
        }
        return params;
    }
}
