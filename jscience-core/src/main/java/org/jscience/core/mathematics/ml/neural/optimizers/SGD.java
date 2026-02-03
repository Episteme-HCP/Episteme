package org.jscience.core.mathematics.ml.neural.optimizers;

import org.jscience.core.mathematics.ml.neural.Optimizer;
import org.jscience.core.mathematics.ml.neural.autograd.GraphNode;
import org.jscience.core.mathematics.linearalgebra.tensors.Tensor;
import org.jscience.core.mathematics.numbers.real.Real;
import java.util.Map;

/**
 * Stochastic Gradient Descent optimizer.
 */
public class SGD<T> implements Optimizer<T> {
    private double learningRate;

    public SGD(double lr) {
        this.learningRate = lr;
    }

    @Override
    public void step(Map<String, GraphNode<T>> parameters) {
        for (GraphNode<T> p : parameters.values()) {
            if (p.requiresGrad() && p.getGrad() != null) {
                // p.data = p.data - lr * p.grad
                @SuppressWarnings("unchecked")
                Tensor<T> scaledGrad = p.getGrad().map(val -> {
                    if (val instanceof Real) return (T) ((Real) val).multiply(Real.of(learningRate));
                    return val;
                });
                Tensor<T> newData = p.getData().subtract(scaledGrad);
                p.setData(newData);
            }
        }
    }
}
