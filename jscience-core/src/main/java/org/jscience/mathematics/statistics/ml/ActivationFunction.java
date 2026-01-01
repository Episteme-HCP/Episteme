/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.mathematics.statistics.ml;

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public enum ActivationFunction {

    SIGMOID {
        @Override
        public double apply(double x) {
            return 1.0 / (1.0 + Math.exp(-x));
        }

        @Override
        public double derivative(double y) {
            // Derivative given output y = sigmoid(x)
            return y * (1.0 - y);
        }
    },

    TANH {
        @Override
        public double apply(double x) {
            return Math.tanh(x);
        }

        @Override
        public double derivative(double y) {
            return 1.0 - y * y;
        }
    },

    RELU {
        @Override
        public double apply(double x) {
            return Math.max(0, x);
        }

        @Override
        public double derivative(double y) {
            return y > 0 ? 1.0 : 0.0;
        }
    },

    LEAKY_RELU {
        private static final double ALPHA = 0.01;

        @Override
        public double apply(double x) {
            return x > 0 ? x : ALPHA * x;
        }

        @Override
        public double derivative(double y) {
            return y > 0 ? 1.0 : ALPHA;
        }
    },

    SOFTPLUS {
        @Override
        public double apply(double x) {
            return Math.log(1 + Math.exp(x));
        }

        @Override
        public double derivative(double y) {
            // d/dx softplus(x) = sigmoid(x) = 1 - exp(-y) for y = softplus(x)
            return 1.0 - Math.exp(-y);
        }
    },

    LINEAR {
        @Override
        public double apply(double x) {
            return x;
        }

        @Override
        public double derivative(double y) {
            return 1.0;
        }
    };

    public abstract double apply(double x);

    public abstract double derivative(double output);
}


