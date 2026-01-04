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

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.matrices.MatrixFactory;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.linearalgebra.vectors.VectorFactory;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents a layer in a neural network.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Layer {

    private final int inputSize;
    private final int outputSize;
    private final ActivationFunction activationFunction;

    private Matrix<Real> weights;
    private Vector<Real> biases;

    /**
     * Creates a new layer.
     * 
     * @param inputSize          number of inputs
     * @param outputSize         number of neurons in this layer
     * @param activationFunction activation function to use
     */
    public Layer(int inputSize, int outputSize, ActivationFunction activationFunction) {
        this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.activationFunction = activationFunction;
        initializeWeights();
    }

    private void initializeWeights() {
        // Simple random initialization for now
        // Creating a dense matrix filled with zeros or randoms.
        // For now, let's create zeros.
        // MatrixFactory.create takes 2D array or List<List<E>>.

        double[][] data = new double[outputSize][inputSize];
        // Fill with small random numbers
        for (int i = 0; i < outputSize; i++) {
            for (int j = 0; j < inputSize; j++) {
                data[i][j] = Math.random() * 0.1;
            }
        }

        // Use Reals.getInstance() or Real.ZERO
        this.weights = MatrixFactory.create(
                box(data),
                Real.ZERO,
                MatrixFactory.Storage.DENSE);

        // Initialize biases to zero
        List<Real> zeroBiases = new ArrayList<>(outputSize);
        for (int i = 0; i < outputSize; i++)
            zeroBiases.add(Real.ZERO);
        this.biases = VectorFactory.<Real>create(zeroBiases, Real.ZERO);
    }

    // Helper to box primitive array for MatrixFactory which expects Real[][]
    private Real[][] box(double[][] data) {
        int r = data.length;
        int c = data[0].length;
        Real[][] res = new Real[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                res[i][j] = Real.of(data[i][j]);
            }
        }
        return res;
    }

    /**
     * Propagates the input through this layer.
     * Output = Activation(Weights * Input + Bias)
     */
    public Vector<Real> forward(Vector<Real> input) {
        // Z = Weights * Input
        Vector<Real> z = weights.multiply(input);

        // Add Biases (if present, here we assume biases initialized)
        if (biases != null) {
            z = z.add(biases);
        }

        // Apply Activation Function
        List<Real> activatedElements = new ArrayList<>();
        for (int i = 0; i < z.dimension(); i++) {
            activatedElements.add(Real.of(activationFunction.apply(z.get(i).doubleValue())));
        }

        return DenseVector.of(activatedElements, Real.ZERO);
    }

    public int getInputSize() {
        return inputSize;
    }

    public int getOutputSize() {
        return outputSize;
    }
}
