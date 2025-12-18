package org.jscience.computing.ai.neuralnetwork;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Interface definition for neural network activation functions.
 * 
 * @author Silvere Martin-Michiellot
 * @since 5.0
 */
public interface ActivationFunction {

    /**
     * Applies the activation function to a value.
     * 
     * @param input the input value
     * @return the activated value
     */
    Real apply(Real input);

    /**
     * Calculates the derivative of the activation function.
     * 
     * @param input the input value (often the output of the activation function
     *              itself depending on implementation)
     * @return the derivative
     */
    Real derivative(Real input);

    /**
     * Sigmoid activation function.
     */
    public static final ActivationFunction SIGMOID = new ActivationFunction() {
        @Override
        public Real apply(Real input) {
            return Real.ONE.divide(Real.ONE.add(input.negate().exp()));
        }

        @Override
        public Real derivative(Real input) {
            Real sigmoid = apply(input);
            return sigmoid.multiply(Real.ONE.subtract(sigmoid));
        }
    };

    /**
     * ReLU activation function.
     */
    public static final ActivationFunction RELU = new ActivationFunction() {
        @Override
        public Real apply(Real input) {
            return input.compareTo(Real.ZERO) > 0 ? input : Real.ZERO;
        }

        @Override
        public Real derivative(Real input) {
            return input.compareTo(Real.ZERO) > 0 ? Real.ONE : Real.ZERO;
        }
    };
}
