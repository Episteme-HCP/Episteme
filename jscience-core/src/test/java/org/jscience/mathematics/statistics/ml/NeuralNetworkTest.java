/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NeuralNetworkTest {

    @Test
    public void testXorWithSigmoid() {
        // XOR problem: Classic non-linear separability test
        NeuralNetwork nn = new NeuralNetwork(2, 4, 1);
        nn.setActivationFunction(ActivationFunction.SIGMOID);
        nn.setLearningRate(0.5);

        double[][] inputs = {
                { 0, 0 },
                { 0, 1 },
                { 1, 0 },
                { 1, 1 }
        };
        double[][] targets = {
                { 0 },
                { 1 },
                { 1 },
                { 0 }
        };

        // Train for many epochs
        double mse = nn.trainBatch(inputs, targets, 5000);
        System.out.println("XOR Training MSE: " + mse);

        // Test predictions
        for (int i = 0; i < inputs.length; i++) {
            double[] out = nn.predict(inputs[i]);
            double expected = targets[i][0];
            double actual = out[0] > 0.5 ? 1 : 0;
            System.out.printf("Input: [%.0f, %.0f] -> Output: %.3f (Expected: %.0f)%n",
                    inputs[i][0], inputs[i][1], out[0], expected);
            assertEquals(expected, actual, "XOR prediction failed for input " + i);
        }
    }

    @Test
    public void testActivationFunctions() {
        // Test each activation function applies correctly
        assertEquals(0.5, ActivationFunction.SIGMOID.apply(0), 1e-9);
        assertEquals(0.0, ActivationFunction.TANH.apply(0), 1e-9);
        assertEquals(0.0, ActivationFunction.RELU.apply(-5), 1e-9);
        assertEquals(5.0, ActivationFunction.RELU.apply(5), 1e-9);
        assertEquals(-0.05, ActivationFunction.LEAKY_RELU.apply(-5), 1e-9);
        assertEquals(0.0, ActivationFunction.LINEAR.apply(0), 1e-9);
        assertEquals(5.0, ActivationFunction.LINEAR.apply(5), 1e-9);
    }
}
