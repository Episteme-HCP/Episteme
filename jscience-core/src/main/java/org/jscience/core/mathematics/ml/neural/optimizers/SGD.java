package org.jscience.core.mathematics.ml.neural.optimizers;

import org.jscience.core.mathematics.ml.neural.autograd.GraphNode;
import org.jscience.core.mathematics.linearalgebra.tensors.Tensor;
import org.jscience.core.mathematics.numbers.real.Real;
import java.util.List;

/**
 * Stochastic Gradient Descent optimizer.
 */
public class SGD<T> {
    private final List<GraphNode<T>> parameters;
    private double learningRate;

    public SGD(List<GraphNode<T>> parameters, double lr) {
        this.parameters = parameters;
        this.learningRate = lr;
    }

    /**
     * Updates parameters using gradients.
     */
    public void step() {
        for (GraphNode<T> p : parameters) {
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

    /**
     * Clears gradients.
     */
    public void zeroGrad() {
        for (GraphNode<T> p : parameters) {
            p.setGrad(null);
        }
    }
}
