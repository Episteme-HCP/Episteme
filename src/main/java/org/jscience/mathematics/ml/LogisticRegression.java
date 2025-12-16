package org.jscience.mathematics.ml;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Logistic Regression - binary classification using sigmoid function.
 * <p>
 * Model: P(y=1|x) = 1/(1 + e^(-w·x))
 * Training: Gradient descent on log-likelihood
 * Applications: Classification, probability estimation
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class LogisticRegression {

    private Real[] weights;
    private Real bias;
    private Real learningRate = Real.of(0.01);
    private int maxIterations = 1000;

    /**
     * Trains logistic regression model.
     * 
     * @param X training data (n samples × d features)
     * @param y binary labels (0 or 1)
     */
    public void fit(Real[][] X, Real[] y) {
        int n = X.length;
        int d = X[0].length;

        // Initialize weights
        weights = new Real[d];
        for (int i = 0; i < d; i++) {
            weights[i] = Real.ZERO;
        }
        bias = Real.ZERO;

        // Gradient descent
        for (int iter = 0; iter < maxIterations; iter++) {
            // Compute gradients
            Real[] gradW = new Real[d];
            for (int i = 0; i < d; i++) {
                gradW[i] = Real.ZERO;
            }
            Real gradB = Real.ZERO;

            for (int i = 0; i < n; i++) {
                // Prediction
                Real z = bias;
                for (int j = 0; j < d; j++) {
                    z = z.add(weights[j].multiply(X[i][j]));
                }
                Real pred = sigmoid(z);

                // Error
                Real error = pred.subtract(y[i]);

                // Accumulate gradients
                gradB = gradB.add(error);
                for (int j = 0; j < d; j++) {
                    gradW[j] = gradW[j].add(error.multiply(X[i][j]));
                }
            }

            // Update weights
            for (int j = 0; j < d; j++) {
                weights[j] = weights[j].subtract(
                        learningRate.multiply(gradW[j]).divide(Real.of(n)));
            }
            bias = bias.subtract(learningRate.multiply(gradB).divide(Real.of(n)));

            // Optional: compute loss for monitoring
            if (iter % 100 == 0) {
                @SuppressWarnings("unused")
                Real loss = computeLoss(X, y);
                // Loss computed for monitoring purposes (can be logged if needed)
            }
        }
    }

    /**
     * Predicts probability P(y=1|x).
     */
    public Real predictProba(Real[] x) {
        Real z = bias;
        for (int i = 0; i < weights.length; i++) {
            z = z.add(weights[i].multiply(x[i]));
        }
        return sigmoid(z);
    }

    /**
     * Predicts class (0 or 1) using threshold 0.5.
     */
    public Real predict(Real[] x) {
        Real proba = predictProba(x);
        return proba.compareTo(Real.of(0.5)) >= 0 ? Real.ONE : Real.ZERO;
    }

    /**
     * Sigmoid function: σ(z) = 1/(1 + e^(-z))
     */
    private Real sigmoid(Real z) {
        // σ(z) = 1 / (1 + exp(-z))
        Real expNegZ = Real.of(Math.exp(-z.doubleValue()));
        return Real.ONE.divide(Real.ONE.add(expNegZ));
    }

    /**
     * Computes log-loss (cross-entropy).
     * <p>
     * Loss = -1/n Σ [y log(ŷ) + (1-y) log(1-ŷ)]
     * </p>
     */
    private Real computeLoss(Real[][] X, Real[] y) {
        Real loss = Real.ZERO;
        int n = X.length;

        for (int i = 0; i < n; i++) {
            Real pred = predictProba(X[i]);

            // Avoid log(0)
            Real epsilon = Real.of(1e-15);
            pred = max(min(pred, Real.ONE.subtract(epsilon)), epsilon);

            Real term1 = y[i].multiply(pred.log());
            Real term2 = Real.ONE.subtract(y[i]).multiply(Real.ONE.subtract(pred).log());
            loss = loss.add(term1.add(term2));
        }

        return loss.negate().divide(Real.of(n));
    }

    /**
     * Computes accuracy on test set.
     */
    public Real score(Real[][] X, Real[] y) {
        int n = X.length;
        int correct = 0;

        for (int i = 0; i < n; i++) {
            Real pred = predict(X[i]);
            if (pred.equals(y[i])) {
                correct++;
            }
        }

        return Real.of(correct).divide(Real.of(n));
    }

    /**
     * L2 regularization (Ridge): adds λ||w||² to loss.
     */
    public void fitWithRegularization(Real[][] X, Real[] y, Real lambda) {
        int n = X.length;
        int d = X[0].length;

        weights = new Real[d];
        for (int i = 0; i < d; i++) {
            weights[i] = Real.ZERO;
        }
        bias = Real.ZERO;

        for (int iter = 0; iter < maxIterations; iter++) {
            Real[] gradW = new Real[d];
            for (int i = 0; i < d; i++) {
                gradW[i] = Real.ZERO;
            }
            Real gradB = Real.ZERO;

            // Data term
            for (int i = 0; i < n; i++) {
                Real z = bias;
                for (int j = 0; j < d; j++) {
                    z = z.add(weights[j].multiply(X[i][j]));
                }
                Real pred = sigmoid(z);
                Real error = pred.subtract(y[i]);

                gradB = gradB.add(error);
                for (int j = 0; j < d; j++) {
                    gradW[j] = gradW[j].add(error.multiply(X[i][j]));
                }
            }

            // Regularization term (not for bias)
            for (int j = 0; j < d; j++) {
                gradW[j] = gradW[j].add(lambda.multiply(weights[j]));
            }

            // Update
            for (int j = 0; j < d; j++) {
                weights[j] = weights[j].subtract(
                        learningRate.multiply(gradW[j]).divide(Real.of(n)));
            }
            bias = bias.subtract(learningRate.multiply(gradB).divide(Real.of(n)));
        }
    }

    // Setters
    public void setLearningRate(Real lr) {
        this.learningRate = lr;
    }

    public void setMaxIterations(int iter) {
        this.maxIterations = iter;
    }

    public Real[] getWeights() {
        return weights;
    }

    public Real getBias() {
        return bias;
    }

    private Real min(Real a, Real b) {
        return a.compareTo(b) < 0 ? a : b;
    }

    private Real max(Real a, Real b) {
        return a.compareTo(b) > 0 ? a : b;
    }
}
