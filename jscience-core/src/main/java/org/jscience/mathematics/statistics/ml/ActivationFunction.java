package org.jscience.mathematics.statistics.ml;

/**
 * Activation functions for neural networks.
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
