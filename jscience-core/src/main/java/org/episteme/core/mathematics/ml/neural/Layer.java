/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.episteme.core.mathematics.ml.neural;

import org.episteme.core.mathematics.linearalgebra.Tensor;
import org.episteme.core.mathematics.ml.neural.autograd.GraphNode;
import org.episteme.core.util.persistence.Persistent;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

/**
 * Represents a layer in a neural network.
 * <p>
 * Unified interface supporting both automatic differentiation (training) 
 * and direct tensor operations (optimized inference).
 * </p>
 *
 * @param <T> the data type (e.g., Real, Complex).
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public interface Layer<T> extends Serializable {

    /**
     * Performs a forward pass using explicit autograd nodes.
     * 
     * @param input the input node.
     * @return the transformed output node.
     */
    GraphNode<T> forward(GraphNode<T> input);

    /**
     * Performs an optimized forward pass using raw tensors.
     * By default, wraps the tensor in a non-grad node and delegates to {@link #forward(GraphNode)}.
     * 
     * @param input the input tensor.
     * @return the transformed output tensor.
     */
    default Tensor<T> forward(Tensor<T> input) {
        GraphNode<T> inNode = new GraphNode<>(input, false);
        return forward(inNode).getData();
    }

    /**
     * Returns the learnable parameters of this layer.
     * Keys should be unique within the layer (e.g., "weights", "bias").
     * 
     * @return a map of parameter graph nodes.
     */
    Map<String, GraphNode<T>> getParameters();

    /**
     * Returns the gradients of the learnable parameters.
     * 
     * @return a map of gradient tensors.
     */
    default Map<String, Tensor<T>> getGradients() {
        Map<String, Tensor<T>> grads = new HashMap<>();
        getParameters().forEach((name, node) -> {
            if (node.requiresGrad() && node.getGrad() != null) {
                grads.put(name, node.getGrad());
            }
        });
        return grads;
    }

    /**
     * Sets the training mode.
     * 
     * @param training true for training, false for inference.
     */
    default void setTraining(boolean training) {
        // Default: do nothing, specialized layers (Dropout/BatchNorm) override this.
    }
}
