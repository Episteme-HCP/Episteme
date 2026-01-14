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
 * An implementation of a perceptron.
 *
 * @author James Matthews
 */
public class PerceptronNN extends NeuralNetwork {
    /** The weights for the inputs. */
    protected double[] weights;

    /** The learning rate. */
    protected double learningRate = 1.0;

    /** The number of correctly */
    protected int correctlyClassified = 0;

/**
     * Create a new instance of the Perceptron with the given number of inputs.
     * Note that the perceptron will be created with one additional weight,
     * with a constant input of 1.0 to correspond to the bias.
     *
     * @param numInputs the number of inputs.
     */
    public PerceptronNN(int numInputs) {
        // +1 for bias
        weights = new double[numInputs + 1];
    }

    /**
     * A simple test perceptron that learns the logic-OR gate.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PerceptronNN perceptron = new PerceptronNN(2);

        // Logic-OR data
        double[][] dt = {
                { 0, 0 }
            };
        double[][] df = {
                { 1, 0 },
                { 0, 1 },
                { 1, 1 }
            };

        try {
            perceptron.initialize();

            // Train the data. train() returns the number of
            // correctly classified items, so we can use that.
            int iterations = 0;
            int maximumIterations = 2000;

            do {
                for (int t = 0; t < dt.length; t++)
                    perceptron.train(dt[t], 0);

                for (int f = 0; f < df.length; f++)
                    perceptron.train(df[f], 1);

                if (perceptron.getCorrectlyClassified() == (dt.length +
                        df.length)) {
                    break;
                }

                iterations++;
            } while (iterations < maximumIterations);

            if (iterations >= maximumIterations) {
                System.out.println(
                    "Maximum iterations exceed. Non-linearly separable data?");

                return;
            } else {
                System.out.println(
                    "Neural network correctly classified all test data in " +
                    iterations + " iterations.");
            }
        } catch (NeuralNetworkException e) {
            System.err.println(e);
        }

        double[][] orTest = {
                { 1, 1 },
                { 1, 0 },
                { 1, 0 },
                { 0, 0 }
            };

        for (int i = 0; i < orTest.length; i++)
            System.out.println(orTest[i][0] + " OR " + orTest[i][1] + " = " +
                perceptron.run(orTest[i]));
    }

    /**
     * Calculate the sum of the weighted inputs.
     *
     * @param inputs the inputs.
     *
     * @return the return value of the network (passed through the activation
     *         function).
     */
    protected double calculateNet(double[] inputs) {
        double sum = 1.0 * weights[0];

        for (int i = 0; i < inputs.length; i++) {
            sum += (inputs[i] * weights[i + 1]);
        }

        return activation.function(sum);
    }

    /**
     * Run the network on the given set of inputs.
     *
     * @param inputData the input data.
     *
     * @return the return value of the network.
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public double run(double[] inputData) {
        if (inputData.length != (weights.length - 1)) {
            throw new IllegalArgumentException("input data length invalid!");
        }

        return calculateNet(inputData);
    }

    /**
     * Initialize the perceptron.
     */
    public void initialize() {
        correctlyClassified = 0;

        for (int i = 1; i < weights.length; i++) {
            weights[i] = Math.random();
        }
    }

    /**
     * Train the perceptron using the delta rule.
     *
     * @param inputData the input data.
     * @param expectedOutput the expected output.
     *
     * @return the number of correctly classified data sets.
     */
    public double train(double[] inputData, double[] expectedOutput) {
        double actualOutput = calculateNet(inputData);
        double desiredOutput = expectedOutput[0];

        if (actualOutput != desiredOutput) {
            weights[0] += (learningRate * 1.0 * (desiredOutput - actualOutput));

            for (int w = 1; w < weights.length; w++) {
                weights[w] += (learningRate * inputData[w - 1] * (desiredOutput -
                actualOutput));
            }
        } else {
            correctlyClassified++;
        }

        return correctlyClassified;
    }

    /**
     * Return the number of correctly classified training instances.
     * Calling this method automatically resets the value after being called,
     * to ease the training loop. <code><b>return</b>
     * getCorrectlyClassified(<b>true</b>); </code>
     *
     * @return the number of correctly classified training instances.
     *
     * @see #getCorrectlyClassified(boolean)
     */
    public int getCorrectlyClassified() {
        return getCorrectlyClassified(true);
    }

    /**
     * Return the number of correctly classified training instances.
     *
     * @param resetOnCall should <code>correclyClassified</code> be reset to
     *        zero after the method is called?
     *
     * @return the number of correctly classified training instances.
     */
    public int getCorrectlyClassified(boolean resetOnCall) {
        int cc = correctlyClassified;

        if (resetOnCall) {
            correctlyClassified = 0;
        }

        return cc;
    }

    /**
     * Return the perceptron weight at the given index. Note that the
     * bias is assigned to the 0<sup>th</sup> weight.
     *
     * @param i the index of the weight to return.
     *
     * @return the weight.
     */
    public double getWeight(int i) {
        return weights[i];
    }
}
