package org.jscience.mathematics.optimization;

import org.jscience.mathematics.analysis.Function;
import org.jscience.mathematics.number.Real;

/**
 * Optimization algorithms for finding minima/maxima.
 * <p>
 * CPU-optimized implementations. For large-scale problems, use GPU backend.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class Optimizer {

    /**
     * Gradient Descent: x_{n+1} = x_n - α∇f(x_n)
     * <p>
     * Finds local minimum iteratively.
     * </p>
     * 
     * @param f             objective function (to minimize)
     * @param gradient      gradient of f
     * @param initialGuess  starting point
     * @param learningRate  step size α
     * @param tolerance     convergence criterion
     * @param maxIterations max iterations
     * @return approximate minimum
     */
    public static Real gradientDescent(Function<Real, Real> f, Function<Real, Real> gradient,
            Real initialGuess, Real learningRate,
            Real tolerance, int maxIterations) {
        Real x = initialGuess;

        for (int i = 0; i < maxIterations; i++) {
            Real grad = gradient.evaluate(x);

            if (grad.abs().compareTo(tolerance) < 0) {
                return x; // Converged
            }

            x = x.subtract(learningRate.multiply(grad));
        }

        return x;
    }

    /**
     * Newton's method for optimization: x_{n+1} = x_n - H^{-1}∇f(x_n)
     * <p>
     * Uses second derivatives (Hessian). Quadratic convergence near minimum.
     * </p>
     */
    public static Real newtonOptimization(Function<Real, Real> f,
            Function<Real, Real> gradient,
            Function<Real, Real> hessian,
            Real initialGuess,
            Real tolerance,
            int maxIterations) {
        Real x = initialGuess;

        for (int i = 0; i < maxIterations; i++) {
            Real grad = gradient.evaluate(x);

            if (grad.abs().compareTo(tolerance) < 0) {
                return x;
            }

            Real h = hessian.evaluate(x);
            if (h.abs().compareTo(Real.of(1e-10)) < 0) {
                return x; // Hessian too small
            }

            x = x.subtract(grad.divide(h));
        }

        return x;
    }

    /**
     * Golden Section Search for 1D unimodal functions.
     * <p>
     * Bracket minimum without derivatives. O(log n) convergence.
     * </p>
     */
    public static Real goldenSectionSearch(Function<Real, Real> f,
            Real a, Real b,
            Real tolerance) {
        Real phi = Real.of((1 + Math.sqrt(5)) / 2); // Golden ratio
        Real resphi = Real.of(2 - phi.doubleValue());

        Real x1 = a.add(resphi.multiply(b.subtract(a)));
        Real x2 = b.subtract(resphi.multiply(b.subtract(a)));
        Real f1 = f.evaluate(x1);
        Real f2 = f.evaluate(x2);

        while (b.subtract(a).abs().compareTo(tolerance) > 0) {
            if (f1.compareTo(f2) < 0) {
                b = x2;
                x2 = x1;
                f2 = f1;
                x1 = a.add(resphi.multiply(b.subtract(a)));
                f1 = f.evaluate(x1);
            } else {
                a = x1;
                x1 = x2;
                f1 = f2;
                x2 = b.subtract(resphi.multiply(b.subtract(a)));
                f2 = f.evaluate(x2);
            }
        }

        return a.add(b).divide(Real.of(2));
    }

    /**
     * Simulated Annealing - global optimization.
     * <p>
     * Probabilistic method that can escape local minima.
     * </p>
     */
    public static Real simulatedAnnealing(Function<Real, Real> f,
            Real initialGuess,
            Real temperature,
            Real coolingRate,
            int maxIterations) {
        java.util.Random random = new java.util.Random();
        Real current = initialGuess;
        Real best = current;
        Real bestValue = f.evaluate(best);
        Real temp = temperature;

        for (int i = 0; i < maxIterations; i++) {
            // Random neighbor
            Real neighbor = current.add(Real.of((random.nextDouble() - 0.5) * 2));
            Real currentValue = f.evaluate(current);
            Real neighborValue = f.evaluate(neighbor);

            Real delta = neighborValue.subtract(currentValue);

            // Accept if better, or probabilistically if worse
            if (delta.compareTo(Real.ZERO) < 0 ||
                    random.nextDouble() < Math.exp(-delta.doubleValue() / temp.doubleValue())) {
                current = neighbor;

                if (neighborValue.compareTo(bestValue) < 0) {
                    best = neighbor;
                    bestValue = neighborValue;
                }
            }

            temp = temp.multiply(coolingRate); // Cool down
        }

        return best;
    }

    /**
     * Nelder-Mead Simplex algorithm for multidimensional optimization.
     * <p>
     * Derivative-free method. Good for noisy functions.
     * Note: This is 1D version; full implementation needs vector support.
     * </p>
     */
    public static Real nelderMead(Function<Real, Real> f,
            Real[] initialSimplex,
            Real tolerance,
            int maxIterations) {
        // Basic implementation for 1D
        // Full multidimensional version would use Real[] arrays

        int n = initialSimplex.length;
        Real[] values = new Real[n];

        for (int i = 0; i < n; i++) {
            values[i] = f.evaluate(initialSimplex[i]);
        }

        for (int iter = 0; iter < maxIterations; iter++) {
            // Find best, worst, second worst
            int best = 0, worst = 0;
            for (int i = 1; i < n; i++) {
                if (values[i].compareTo(values[best]) < 0)
                    best = i;
                if (values[i].compareTo(values[worst]) > 0)
                    worst = i;
            }

            // Check convergence
            Real range = values[worst].subtract(values[best]);
            if (range.abs().compareTo(tolerance) < 0) {
                return initialSimplex[best];
            }

            // Compute centroid (excluding worst)
            Real centroid = Real.ZERO;
            for (int i = 0; i < n; i++) {
                if (i != worst) {
                    centroid = centroid.add(initialSimplex[i]);
                }
            }
            centroid = centroid.divide(Real.of(n - 1));

            // Reflection
            Real reflected = centroid.add(centroid.subtract(initialSimplex[worst]));
            Real reflectedValue = f.evaluate(reflected);

            if (reflectedValue.compareTo(values[best]) < 0) {
                // Expansion
                Real expanded = centroid.add(centroid.subtract(initialSimplex[worst]).multiply(Real.of(2)));
                Real expandedValue = f.evaluate(expanded);

                if (expandedValue.compareTo(reflectedValue) < 0) {
                    initialSimplex[worst] = expanded;
                    values[worst] = expandedValue;
                } else {
                    initialSimplex[worst] = reflected;
                    values[worst] = reflectedValue;
                }
            } else {
                // Contraction
                Real contracted = centroid.add(initialSimplex[worst].subtract(centroid).divide(Real.of(2)));
                Real contractedValue = f.evaluate(contracted);

                if (contractedValue.compareTo(values[worst]) < 0) {
                    initialSimplex[worst] = contracted;
                    values[worst] = contractedValue;
                } else {
                    // Shrink
                    for (int i = 0; i < n; i++) {
                        if (i != best) {
                            initialSimplex[i] = initialSimplex[best].add(
                                    initialSimplex[i].subtract(initialSimplex[best]).divide(Real.of(2)));
                            values[i] = f.evaluate(initialSimplex[i]);
                        }
                    }
                }
            }
        }

        // Return best
        int best = 0;
        for (int i = 1; i < n; i++) {
            if (values[i].compareTo(values[best]) < 0)
                best = i;
        }
        return initialSimplex[best];
    }
}
