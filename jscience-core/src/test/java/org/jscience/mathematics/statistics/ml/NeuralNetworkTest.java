package org.jscience.mathematics.statistics.ml;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NeuralNetworkTest {

    @Test
    public void testXorWithTanh() {
        // XOR problem: Classic non-linear separability test
        NeuralNetwork nn = new NeuralNetwork(2, 4, 1);
        nn.setActivationFunction(ActivationFunction.TANH);
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
