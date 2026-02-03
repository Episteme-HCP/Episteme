package org.jscience.core.mathematics.ml.neural.layers;

import org.jscience.core.mathematics.ml.neural.Layer;
import org.jscience.core.mathematics.ml.neural.autograd.GraphNode;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;
import java.util.ArrayList;
import java.util.List;

/**
 * Container for sequential execution of layers.
 */
@Persistent
public class Sequential<T> implements Layer<T> {
    private static final long serialVersionUID = 1L;
    
    @Relation(type = Relation.Type.ONE_TO_MANY)
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
    public java.util.Map<String, org.jscience.core.mathematics.ml.neural.autograd.GraphNode<T>> getParameters() {
        java.util.Map<String, org.jscience.core.mathematics.ml.neural.autograd.GraphNode<T>> params = new java.util.HashMap<>();
        for (int i = 0; i < layers.size(); i++) {
            Layer<T> layer = layers.get(i);
            java.util.Map<String, org.jscience.core.mathematics.ml.neural.autograd.GraphNode<T>> layerParams = layer.getParameters();
            final int layerIdx = i;
            layerParams.forEach((name, node) -> params.put("layer_" + layerIdx + "." + name, node));
        }
        return params;
    }
}
