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

package org.jscience.computing.ai.neurocomputing;

/**
 * The interface for the neural network activation function.
 *
 * @author James Matthews
 */
public interface Activation {
    /**
     * The activation function.
     *
     * @param in the summed, weighted inputs from the neural network.
     *
     * @return the 'activated' result.
     */
    public abstract double function(double in);

    /**
     * A stepped activation function. If n > 0, return 1.0 otherwise
     * return 0.0.
     */
    public static class Stepped implements Activation {
        /**
         * Return 1.0 for any value greater than 0.0, otherwise
         * 0.0.
         *
         * @param in the input weight.
         *
         * @return the stepped weight.
         */
        public double function(double in) {
            if (in > 0) {
                return 1.0;
            }

            return 0.0;
        }
    }

    /**
     * No activation function. Returns the input.
     */
    public static class None implements Activation {
        /**
         * Returns the weight.
         *
         * @param in the input weight.
         *
         * @return the input weight (no change).
         */
        public double function(double in) {
            return in;
        }
    }

    /**
     * Sigmoid activation function.
     */
    public static class Sigmoid implements Activation {
        /**
         * Return f(x) = 1/(1+<i>e</i><sup>-x</sup>.
         *
         * @param in the input weight.
         *
         * @return sigmoid activation function.
         */
        public double function(double in) {
            return 1 / (1 + Math.exp(in * -1));
        }
    }

    /**
     * Hyperbolic tangent activation function.
     */
    public static class Tanh implements Activation {
        /**
         * Return f(x) =
         * <i>e</i><sup>x</sup>-<i>e</i><sup>-x</sup>)
         * /(<i>e</i><sup>x</sup>+<i>e</i><sup>-x</sup>.
         *
         * @param in the input weight.
         *
         * @return tanh activation result.
         */
        public double function(double in) {
            double p1 = Math.exp(in);
            double p2 = Math.exp(in * -1);

            return (p1 - p2) / (p1 + p2);
        }
    }
}
