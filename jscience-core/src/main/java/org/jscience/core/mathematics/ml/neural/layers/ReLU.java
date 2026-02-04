package org.jscience.core.mathematics.ml.neural.layers;

import org.jscience.core.mathematics.ml.neural.ActivationFunction;
import org.jscience.core.mathematics.ml.neural.autograd.GraphNode;
import java.util.Collections;
import java.util.List;

/**
 * Rectified Linear Unit (ReLU) activation layer.
 */
public class ReLU<T> extends ActivationLayer<T> {
    
    public ReLU() {
        super(ActivationFunction.RELU);
    }

    @Override
    public GraphNode<T> forward(GraphNode<T> input) {
        return input.relu();
    }

    @Override
    public java.util.Map<String, GraphNode<T>> getParameters() {
        return Collections.emptyMap();
    }
}
