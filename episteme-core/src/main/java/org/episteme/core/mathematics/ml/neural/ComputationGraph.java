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
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a neural network as a sequence of layers (Computational Graph).
 * <p>
 * This class orchestrates the forward and backward passes across all layers.
 * </p>
 *
 * @param <T> the data type.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class ComputationGraph<T> {

    private final List<Layer<T>> layers = new ArrayList<>();

    /**
     * Adds a layer to the network.
     * 
     * @param layer the layer to add.
     * @return this graph for chaining.
     */
    public ComputationGraph<T> add(Layer<T> layer) {
        layers.add(layer);
        return this;
    }

    /**
     * Performs inference (forward pass) on the input.
     * 
     * @param input the input tensor.
     * @return the output tensor (prediction).
     */
    public Tensor<T> predict(Tensor<T> input) {
        GraphNode<T> current = new GraphNode<>(input, false);
        for (Layer<T> layer : layers) {
            layer.setTraining(false);
            current = layer.forward(current);
        }
        return current.getData();
    }

    /**
     * Performs a training step (forward + backward + optimize).
     * 
     * @param input input batch.
     * @param target target values.
     * @param optimizer optimizer to update weights.
     */
    public void trainStep(Tensor<T> input, Tensor<T> target, Optimizer<T> optimizer) {
        // Forward
        GraphNode<T> inNode = new GraphNode<>(input, false);
        GraphNode<T> current = inNode;
        for (Layer<T> layer : layers) {
            layer.setTraining(true);
            current = layer.forward(current);
        }
        
        // Calculate loss (example: Mean Squared Error)
        // Note: Real loss calculation should be external, this is for demonstration
        GraphNode<T> targetNode = new GraphNode<>(target, false);
        GraphNode<T> diff = current.subtract(targetNode);
        GraphNode<T> loss = diff.multiply(diff).mean();

        // Backward
        loss.backward();
        
        // Update parameters
        for (Layer<T> layer : layers) {
            optimizer.step(layer.getParameters());
        }
    }
    
    /**
     * Returns the list of layers.
     */
    public List<Layer<T>> getLayers() {
        return layers;
    }
}
