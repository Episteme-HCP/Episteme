package org.jscience.core.mathematics.ml.neural.layers;

import org.jscience.core.mathematics.ml.neural.Layer;
import org.jscience.core.mathematics.ml.neural.autograd.GraphNode;
import org.jscience.core.mathematics.linearalgebra.Tensor;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Attribute;
import java.util.Map;
import java.util.Random;

/**
 * Fully connected (linear) layer.
 */
@Persistent
public class Linear<T> implements Layer<T> {
    private static final long serialVersionUID = 1L;
    
    @Attribute
    private final GraphNode<T> weights;
    
    @Attribute
    private final GraphNode<T> bias;

    @SuppressWarnings("unchecked")
    public Linear(int inFeatures, int outFeatures) {
        Random rand = new Random();
        Real[] wData = new Real[inFeatures * outFeatures];
        // Xavier-like initialization
        double std = Math.sqrt(1.0 / inFeatures);
        for (int i = 0; i < wData.length; i++) {
            wData[i] = Real.of(rand.nextGaussian() * std);
        }
        
        Real[] bData = new Real[outFeatures];
        for (int i = 0; i < bData.length; i++) {
            bData[i] = Real.ZERO;
        }

        this.weights = new GraphNode<>((Tensor<T>) Tensor.of(wData, inFeatures, outFeatures), true);
        this.bias = new GraphNode<>((Tensor<T>) Tensor.zeros(Real.class, outFeatures), true);
    }

    @Override
    public GraphNode<T> forward(GraphNode<T> input) {
        // xW + b
        GraphNode<T> mm = input.matmul(weights);
        int batchSize = input.getData().shape()[0];
        int outFeatures = bias.getData().shape()[0];
        return mm.add(bias.broadcast(batchSize, outFeatures));
    }

    @Override
    public Map<String, GraphNode<T>> getParameters() {
        Map<String, GraphNode<T>> params = new java.util.HashMap<>();
        params.put("weights", weights);
        params.put("bias", bias);
        return params;
    }
}
