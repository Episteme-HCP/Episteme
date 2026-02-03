package org.jscience.core.mathematics.ml.neural.layers;

import org.jscience.core.mathematics.ml.neural.autograd.GraphNode;
import java.util.List;

/**
 * Base interface for neural network layers.
 */
public interface Layer<T> {
    /**
     * Performs forward pass.
     */
    GraphNode<T> forward(GraphNode<T> input);

    /**
     * Returns learnable parameters of the layer.
     */
    List<GraphNode<T>> getParameters();
}
