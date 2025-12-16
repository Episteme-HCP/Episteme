package org.jscience.mathematics.optimization;

import org.jscience.mathematics.analysis.Function;
import org.jscience.mathematics.numbers.real.Real;

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

    /**
     * Linear programming solver using Simplex method.
     * <p>
     * Maximizes c·x subject to Ax ≤ b, x ≥ 0.
     * </p>
     * 
     * @param c objective coefficients
     * @param A constraint matrix
     * @param b constraint bounds
     * @return optimal solution vector
     */
    public static Real[] linearProgramming(Real[] c, Real[][] A, Real[] b) {
        int n = c.length; // Number of variables
        int m = A.length; // Number of constraints

        // Create tableau with slack variables
        // [A | I | b]
        // [-c | 0 | 0]
        Real[][] tableau = new Real[m + 1][n + m + 1];

        // Fill constraints with slack
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                tableau[i][j] = A[i][j];
            }
            for (int j = 0; j < m; j++) {
                tableau[i][n + j] = (i == j) ? Real.ONE : Real.ZERO;
            }
            tableau[i][n + m] = b[i];
        }

        // Objective row (negated for maximization)
        for (int j = 0; j < n; j++) {
            tableau[m][j] = c[j].negate();
        }
        for (int j = n; j <= n + m; j++) {
            tableau[m][j] = Real.ZERO;
        }

        // Simplex iterations
        int maxIterations = 1000;
        for (int iter = 0; iter < maxIterations; iter++) {
            // Find pivot column (most negative in objective row)
            int pivotCol = -1;
            Real minVal = Real.ZERO;
            for (int j = 0; j < n + m; j++) {
                if (tableau[m][j].compareTo(minVal) < 0) {
                    minVal = tableau[m][j];
                    pivotCol = j;
                }
            }
            if (pivotCol == -1)
                break; // Optimal

            // Find pivot row (minimum ratio test)
            int pivotRow = -1;
            Real minRatio = Real.of(Double.MAX_VALUE);
            for (int i = 0; i < m; i++) {
                if (tableau[i][pivotCol].compareTo(Real.ZERO) > 0) {
                    Real ratio = tableau[i][n + m].divide(tableau[i][pivotCol]);
                    if (ratio.compareTo(minRatio) < 0) {
                        minRatio = ratio;
                        pivotRow = i;
                    }
                }
            }
            if (pivotRow == -1) {
                throw new IllegalStateException("Unbounded solution");
            }

            // Pivot operation
            Real pivot = tableau[pivotRow][pivotCol];
            for (int j = 0; j <= n + m; j++) {
                tableau[pivotRow][j] = tableau[pivotRow][j].divide(pivot);
            }
            for (int i = 0; i <= m; i++) {
                if (i != pivotRow) {
                    Real factor = tableau[i][pivotCol];
                    for (int j = 0; j <= n + m; j++) {
                        tableau[i][j] = tableau[i][j].subtract(factor.multiply(tableau[pivotRow][j]));
                    }
                }
            }
        }

        // Extract solution
        Real[] result = new Real[n];
        for (int j = 0; j < n; j++) {
            result[j] = Real.ZERO;
            for (int i = 0; i < m; i++) {
                if (Math.abs(tableau[i][j].doubleValue() - 1.0) < 1e-10) {
                    boolean isBasic = true;
                    for (int k = 0; k < m; k++) {
                        if (k != i && Math.abs(tableau[k][j].doubleValue()) > 1e-10) {
                            isBasic = false;
                            break;
                        }
                    }
                    if (isBasic) {
                        result[j] = tableau[i][n + m];
                        break;
                    }
                }
            }
        }

        return result;
    }

    /**
     * Nelder-Mead Simplex algorithm for multidimensional optimization.
     * <p>
     * Derivative-free method. Good for noisy functions.
     * </p>
     * 
     * @param f              objective function (Real[] -> Real)
     * @param initialSimplex initial simplex vertices (n+1 vertices for n
     *                       dimensions)
     * @param tolerance      convergence tolerance
     * @param maxIterations  maximum iterations
     * @return optimal point
     */
    public static Real[] nelderMead(java.util.function.Function<Real[], Real> f,
            Real[][] initialSimplex,
            Real tolerance,
            int maxIterations) {
        int n = initialSimplex[0].length; // Dimensions
        int nVerts = initialSimplex.length; // n+1 vertices

        Real[][] simplex = new Real[nVerts][];
        Real[] values = new Real[nVerts];

        // Copy simplex and evaluate
        for (int i = 0; i < nVerts; i++) {
            simplex[i] = initialSimplex[i].clone();
            values[i] = f.apply(simplex[i]);
        }

        Real alpha = Real.ONE; // Reflection
        Real gamma = Real.TWO; // Expansion
        Real rho = Real.of(0.5); // Contraction
        Real sigma = Real.of(0.5); // Shrink

        for (int iter = 0; iter < maxIterations; iter++) {
            // Sort vertices by function value
            for (int i = 0; i < nVerts - 1; i++) {
                for (int j = i + 1; j < nVerts; j++) {
                    if (values[j].compareTo(values[i]) < 0) {
                        Real[] tempV = simplex[i];
                        simplex[i] = simplex[j];
                        simplex[j] = tempV;
                        Real tempF = values[i];
                        values[i] = values[j];
                        values[j] = tempF;
                    }
                }
            }

            // Check convergence
            Real range = values[nVerts - 1].subtract(values[0]).abs();
            if (range.compareTo(tolerance) < 0) {
                return simplex[0];
            }

            // Compute centroid (excluding worst)
            Real[] centroid = new Real[n];
            for (int j = 0; j < n; j++) {
                centroid[j] = Real.ZERO;
                for (int i = 0; i < nVerts - 1; i++) {
                    centroid[j] = centroid[j].add(simplex[i][j]);
                }
                centroid[j] = centroid[j].divide(Real.of(nVerts - 1));
            }

            // Reflection
            Real[] reflected = new Real[n];
            for (int j = 0; j < n; j++) {
                reflected[j] = centroid[j].add(alpha.multiply(centroid[j].subtract(simplex[nVerts - 1][j])));
            }
            Real reflectedVal = f.apply(reflected);

            if (reflectedVal.compareTo(values[0]) >= 0 && reflectedVal.compareTo(values[nVerts - 2]) < 0) {
                simplex[nVerts - 1] = reflected;
                values[nVerts - 1] = reflectedVal;
                continue;
            }

            if (reflectedVal.compareTo(values[0]) < 0) {
                // Expansion
                Real[] expanded = new Real[n];
                for (int j = 0; j < n; j++) {
                    expanded[j] = centroid[j].add(gamma.multiply(reflected[j].subtract(centroid[j])));
                }
                Real expandedVal = f.apply(expanded);

                if (expandedVal.compareTo(reflectedVal) < 0) {
                    simplex[nVerts - 1] = expanded;
                    values[nVerts - 1] = expandedVal;
                } else {
                    simplex[nVerts - 1] = reflected;
                    values[nVerts - 1] = reflectedVal;
                }
                continue;
            }

            // Contraction
            Real[] contracted = new Real[n];
            for (int j = 0; j < n; j++) {
                contracted[j] = centroid[j].add(rho.multiply(simplex[nVerts - 1][j].subtract(centroid[j])));
            }
            Real contractedVal = f.apply(contracted);

            if (contractedVal.compareTo(values[nVerts - 1]) < 0) {
                simplex[nVerts - 1] = contracted;
                values[nVerts - 1] = contractedVal;
                continue;
            }

            // Shrink
            for (int i = 1; i < nVerts; i++) {
                for (int j = 0; j < n; j++) {
                    simplex[i][j] = simplex[0][j].add(sigma.multiply(simplex[i][j].subtract(simplex[0][j])));
                }
                values[i] = f.apply(simplex[i]);
            }
        }

        // Return best
        int best = 0;
        for (int i = 1; i < nVerts; i++) {
            if (values[i].compareTo(values[best]) < 0)
                best = i;
        }
        return simplex[best];
    }
}
