package org.jscience.core.mathematics.ml.neural.layers;

import org.jscience.core.mathematics.ml.neural.autograd.GraphNode;
import java.util.Collections;
import java.util.List;

/**
 * Rectified Linear Unit (ReLU) activation layer.
 */
public class ReLU<T> implements Layer<T> {
    @Override
    public GraphNode<T> forward(GraphNode<T> input) {
        return input.relu();
    }

    @Override
    public List<GraphNode<T>> getParameters() {
        return Collections.emptyList();
    }
}
