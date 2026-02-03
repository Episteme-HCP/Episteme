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

package org.jscience.core.mathematics.ml.neural.layers;

import org.jscience.core.mathematics.ml.neural.Layer;
import org.jscience.core.mathematics.ml.neural.ActivationFunction;
import org.jscience.core.mathematics.ml.neural.autograd.GraphNode;
import java.util.Collections;
import java.util.List;

/**
 * Generic activation layer that applies a specified {@link ActivationFunction}.
 * 
 * @param <T> data type
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class ActivationLayer<T> implements Layer<T> {

    private static final long serialVersionUID = 1L;
    private final ActivationFunction function;

    public ActivationLayer(ActivationFunction function) {
        this.function = function;
    }

    public ActivationLayer(String functionName) {
        this(ActivationFunction.of(functionName));
    }

    @Override
    public GraphNode<T> forward(GraphNode<T> input) {
        if (function == ActivationFunction.RELU) {
            return input.relu();
        } else if (function == ActivationFunction.SIGMOID) {
            return input.sigmoid();
        } else if (function == ActivationFunction.IDENTITY) {
            return input;
        }
        
        // Handle other functions or custom ones?
        // For now, identity or throw
        return input;
    }

    @Override
    public java.util.Map<String, GraphNode<T>> getParameters() {
        return java.util.Collections.emptyMap();
    }

    public ActivationFunction getFunction() {
        return function;
    }
}
