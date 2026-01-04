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

package org.jscience.computing.ai.neuralnetwork;

import java.util.Random;

/**
 * Multi-layer Perceptron (MLP) neural network.
 * Supports fully-connected feedforward architecture with backpropagation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class NeuralNetwork {

    private final int[] layerSizes;
    private double[][] neurons; // Activations
    private double[][][] weights; // weights[layer][to][from]
    private double[][] biases;
    private double learningRate;
    private final Random random;
    private ActivationFunction activationFunction = ActivationFunction.SIGMOID;

    /**
     * Creates an MLP with specified layer sizes.
     * 
     * @param layerSizes Array where layerSizes[0] = input size,
     *                   layerSizes[n-1] = output size
     */
    public NeuralNetwork(int... layerSizes) {
        this.layerSizes = layerSizes.clone();
        this.learningRate = 0.1;
        this.random = new Random();

        initializeNetwork();
    }

    private void initializeNetwork() {
        int numLayers = layerSizes.length;

        neurons = new double[numLayers][];
        for (int i = 0; i < numLayers; i++) {
            neurons[i] = new double[layerSizes[i]];
        }

        weights = new double[numLayers - 1][][];
        biases = new double[numLayers - 1][];

        for (int i = 0; i < numLayers - 1; i++) {
            int fromSize = layerSizes[i];
            int toSize = layerSizes[i + 1];

            weights[i] = new double[toSize][fromSize];
            biases[i] = new double[toSize];

            // Xavier initialization
            double scale = Math.sqrt(2.0 / (fromSize + toSize));
            for (int j = 0; j < toSize; j++) {
                for (int k = 0; k < fromSize; k++) {
                    weights[i][j][k] = random.nextGaussian() * scale;
                }
                biases[i][j] = 0.0;
            }
        }
    }

    /**
     * Forward pass through the network.
     * 
     * @param input Input vector
     * @return Output vector
     */
    public double[] predict(double[] input) {
        if (input.length != layerSizes[0]) {
            throw new IllegalArgumentException("Input size mismatch");
        }

        neurons[0] = input.clone();

        for (int layer = 0; layer < weights.length; layer++) {
            int toSize = layerSizes[layer + 1];

            for (int j = 0; j < toSize; j++) {
                double sum = biases[layer][j];
                for (int k = 0; k < layerSizes[layer]; k++) {
                    sum += weights[layer][j][k] * neurons[layer][k];
                }
                neurons[layer + 1][j] = activationFunction.apply(sum);
            }
        }

        return neurons[neurons.length - 1].clone();
    }

    /**
     * Trains the network on a single example using backpropagation.
     * 
     * @param input  Input vector
     * @param target Target output vector
     * @return Mean squared error for this example
     */
    public double train(double[] input, double[] target) {
        // Forward pass
        double[] output = predict(input);

        // Compute output layer error
        int numLayers = layerSizes.length;
        double[][] deltas = new double[numLayers][];

        for (int i = 0; i < numLayers; i++) {
            deltas[i] = new double[layerSizes[i]];
        }

        // Output layer delta
        double mse = 0;
        for (int j = 0; j < layerSizes[numLayers - 1]; j++) {
            double error = target[j] - output[j];
            mse += error * error;
            deltas[numLayers - 1][j] = error * activationFunction.derivative(output[j]);
        }
        mse /= target.length;

        // Backpropagate
        for (int layer = numLayers - 2; layer >= 0; layer--) {
            for (int j = 0; j < layerSizes[layer]; j++) {
                double error = 0;
                for (int k = 0; k < layerSizes[layer + 1]; k++) {
                    error += deltas[layer + 1][k] * weights[layer][k][j];
                }
                deltas[layer][j] = error * activationFunction.derivative(neurons[layer][j]);
            }
        }

        // Update weights and biases
        for (int layer = 0; layer < weights.length; layer++) {
            for (int j = 0; j < layerSizes[layer + 1]; j++) {
                for (int k = 0; k < layerSizes[layer]; k++) {
                    weights[layer][j][k] += learningRate * deltas[layer + 1][j] * neurons[layer][k];
                }
                biases[layer][j] += learningRate * deltas[layer + 1][j];
            }
        }

        return mse;
    }

    /**
     * Trains on a dataset for multiple epochs.
     * 
     * @param inputs  Array of input vectors
     * @param targets Array of target vectors
     * @param epochs  Number of training epochs
     * @return Final average MSE
     */
    public double trainBatch(double[][] inputs, double[][] targets, int epochs) {
        double mse = 0;
        for (int epoch = 0; epoch < epochs; epoch++) {
            mse = 0;
            for (int i = 0; i < inputs.length; i++) {
                mse += train(inputs[i], targets[i]);
            }
            mse /= inputs.length;
        }
        return mse;
    }

    public static double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    public static double sigmoidDerivative(double y) {
        return y * (1.0 - y);
    }

    public void setLearningRate(double rate) {
        this.learningRate = rate;
    }

    public void setActivationFunction(ActivationFunction activation) {
        this.activationFunction = activation;
    }

    public ActivationFunction getActivationFunction() {
        return activationFunction;
    }

    public int[] getLayerSizes() {
        return layerSizes.clone();
    }
}
