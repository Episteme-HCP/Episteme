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
 * An abstract class for the feedforward layers.
 *
 * @author James Matthews
 */
public abstract class FFNLayer {
    /**
     * The outputs for this layer.
     */
    protected double[] outputs;

    /**
     * The weights for this layer.
     */
    protected double[][] weights;

    /**
     * The next layer that this layer is connected to.
     */
    protected FFNLayer connected;

    /**
     * The network this layer belongs to.
     */
    protected FeedForwardNN myNetwork;

    /**
     * The activation function this layer uses.
     */
    protected Activation activate = new Activation.Sigmoid();

    /**
     * Creates a new instance of FFNLayer
     */
    public FFNLayer() {
    }

    /**
     * Set the network for this layer.
     *
     * @param network the network to be used.
     */
    public void setNetwork(FeedForwardNN network) {
        myNetwork = network;
    }

    /**
     * Connects this layer with another layer. This method is should normally only be
     * called <code>FeedForwardNN.initialize</code>.
     *
     * @param connect the connecting layer.
     * @see FeedForwardNN#initialize()
     */
    public void connectWith(FFNLayer connect) {
        connected = connect;
        weights = new double[getUnitCount()][connected.getUnitCount()];
    }

    /**
     * Set the weights for this layer. This method iteratively calls:
     * <code>
     * for (int u=0; u<weights.length; u++) {
     * setWeights(u, newWeights[u]);
     * }
     * </code>
     *
     * @param newWeights the new weights.
     */
    public void setWeights(double[][] newWeights) {
        if (weights.length != newWeights.length) {
            throw new IllegalArgumentException("new weights invalid length");
        }

        for (int u = 0; u < weights.length; u++) {
            setWeights(u, newWeights[u]);
        }
    }

    /**
     * Set the weights for a given units.
     *
     * @param unit       the unit to set the weights for.
     * @param newWeights the new weight array.
     */
    public void setWeights(int unit, double[] newWeights) {
        if (weights[unit].length != newWeights.length) {
            throw new IllegalArgumentException("new weights invalid length");
        }

        for (int w = 0; w < weights[unit].length; w++) {
            weights[unit][w] = newWeights[w];
        }
    }

    /**
     * Randomize the weights between the given range.
     *
     * @param min the range minimum.
     * @param max the range maximum.
     */
    public void randomizeWeights(double min, double max) {
        for (int u = 0; u < weights.length; u++) {
            for (int w = 0; w < weights[u].length; w++) {
                weights[u][w] = min + (Math.random() * (max - min));
            }
        }
    }

    /**
     * Get the number of units in this layer.
     *
     * @return the number of units.
     */
    public abstract int getUnitCount();

    /**
     * Retrieve the outputs for this layer.
     *
     * @return the layer output.
     */
    public double[] getOutput() {
        return outputs;
    }

    /**
     * Run this layer on the given inputs.
     *
     * @param input the inputs for this layer.
     * @throws NeuralNetworkException unused.
     */
    public void run(double[] input) throws NeuralNetworkException {
        // FIXME: I'm not happy with this at all...
        if (connected != null) {
            outputs = new double[connected.getUnitCount()];

            for (int o = 0; o < outputs.length; o++) {
                outputs[o] = 0;

                for (int i = 0; i < getUnitCount(); i++)
                    outputs[o] += (input[i] * weights[i][o]);

                // Run through the activation function
                outputs[o] = activate.function(outputs[o]);
            }
        } else {
            outputs = new double[input.length];

            for (int i = 0; i < input.length; i++)
                outputs[i] = input[i];
        }
    }
}
