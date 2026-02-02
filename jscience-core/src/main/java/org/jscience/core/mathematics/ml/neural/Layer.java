/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.jscience.core.mathematics.ml.neural;

import org.jscience.core.mathematics.linearalgebra.tensors.Tensor;
import java.util.Map;

/**
 * Represents a layer in a neural network.
 * <p>
 * Layers are the building blocks of neural networks. They transform input tensors
 * to output tensors (forward pass) and propagate gradients (backward pass).
 * </p>
 *
 * @param <T> the data type (e.g., Real, Complex).
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface Layer<T> {

    /**
     * Performs the forward pass.
     * 
     * @param input the input tensor.
     * @return the transformed output tensor.
     */
    Tensor<T> forward(Tensor<T> input);

    /**
     * Performs the backward pass (gradients propagation).
     * 
     * @param gradOutput the gradient of the loss with respect to the output.
     * @return the gradient of the loss with respect to the input.
     */
    Tensor<T> backward(Tensor<T> gradOutput);

    /**
     * Returns the learnable parameters of this layer.
     * Keys are parameter names (e.g., "weights", "bias").
     * 
     * @return a map of parameter tensors.
     */
    Map<String, Tensor<T>> getParameters();

    /**
     * Returns the gradients of the learnable parameters.
     * Should correspond to the keys in {@link #getParameters()}.
     * 
     * @return a map of gradient tensors.
     */
    Map<String, Tensor<T>> getGradients();
    
    /**
     * Sets the training mode.
     * Some layers (like Dropout, BatchNorm) behave differently during training.
     * 
     * @param training true for training, false for inference.
     */
    void setTraining(boolean training);
}
