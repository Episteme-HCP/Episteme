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

import org.jscience.util.Visualizable;


/**
 * A neural network framework class. The class is meant to help create
 * simple neural networks, such as perceptrons and self-organizing networks.
 *
 * @author James Matthews
 */
public abstract class NeuralNetwork implements Visualizable {
    /**
     * Allows for a visualizer to be assigned to a neural network. This
     * may be a data plotter, a bitmapped representation of the neural network
     * etc.
     */
    protected Visualizable visualization = null;

    /** The activation function. */
    protected Activation activation = new Activation.Stepped();

    /**
     * Initialize the neural network.
     *
     * @throws NeuralNetworkException This
     */
    public abstract void initialize() throws NeuralNetworkException;

    /**
     * Train the neural network on the given training data.
     *
     * @param inputData the input data.
     * @param expectedOutput the expect output.
     *
     * @return a double value. This actual interpretation of this return is
     *         class-dependent.
     *
     * @throws NeuralNetworkException if an error occurs with training, this
     *         method should throw a <code>NeuralNetworkException</code>.
     */
    public abstract double train(double[] inputData, double[] expectedOutput)
        throws NeuralNetworkException;

    /**
     * This method is provided as a helper for networks that only have
     * one output, therefore only one expected output. The method is defined
     * as: <code><b>public double</b> train(<b>double</b>[] inputData,
     * <b>double</b> expectedOutput) <b>throws</b> NeuralNetworkException
     * {<b>return</b> train(inputData, <b>new double</b>[] { expectedOutput
     * }); }</code>
     *
     * @param inputData the input data.
     * @param expectedOutput the expected output.
     *
     * @return the value returned by <code>train(double[], double[])</code>.
     *
     * @throws NeuralNetworkException the exception thrown if any training
     *         errors occur.
     *
     * @see #train(double[],double[])
     */
    public double train(double[] inputData, double expectedOutput)
        throws NeuralNetworkException {
        return train(inputData, new double[] { expectedOutput });
    }

    /**
     * This method is provided as a helper for networks that have no
     * expected output, such as unsupervised networks. The method is defined
     * as: <code><b>public double</b> train(<b>double</b>[] inputData)
     * <b>throws</b> NeuralNetworkException {<b>return</b> train(inputData,
     * <b>null</b>); }</code>
     *
     * @param inputData the input data.
     *
     * @return the value returned by <code>train(double[], double[])</code>.
     *
     * @throws NeuralNetworkException the exception thrown if any training
     *         errors occur.
     */
    public double train(double[] inputData) throws NeuralNetworkException {
        return train(inputData, null);
    }

    /**
     * Run the neural network on the given inputs.
     *
     * @param inputData the input data.
     *
     * @return the returned value of the neural network.
     *
     * @throws NeuralNetworkException an exception is thrown if an error
     *         occurs.
     */
    public abstract double run(double[] inputData)
        throws NeuralNetworkException;

    /**
     * Set the activation function.
     *
     * @param ac the activation function.
     */
    public void setActivation(Activation ac) {
        activation = ac;
    }

    /**
     * Render the neural network using the assigned visualization.
     *
     * @param g the graphics context.
     * @param width the width of the graphics context.
     * @param height the height of the graphics context.
     */
    public void render(java.awt.Graphics g, int width, int height) {
        if (visualization != null) {
            visualization.render(g, width, height);
        }
    }

    /**
     * Write an image of the neural network using the assigned
     * visualization.
     *
     * @param s the filename of the image to write.
     * @param width the width of the image.
     * @param height the height of the image.
     */
    public void writeImage(String s, int width, int height) {
        if (visualization != null) {
            visualization.writeImage(s, width, height);
        }
    }

    /**
     * Return the current visualization.
     *
     * @return the visualization currently assigned.
     */
    public Visualizable getVisualization() {
        return visualization;
    }

    /**
     * Set the visualization to use.
     *
     * @param vis the visualization class to use.
     */
    public void setVisualization(Visualizable vis) {
        visualization = vis;
    }
}
