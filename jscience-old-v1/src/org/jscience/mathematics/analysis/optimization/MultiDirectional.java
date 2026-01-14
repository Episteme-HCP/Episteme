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

package org.jscience.mathematics.analysis.optimization;

/**
 * This class implements the multi-directional direct search method.
 *
 * @author Luc Maisonobe
 * @version $Id: MultiDirectional.java,v 1.3 2007-10-23 18:19:20 virtualcall Exp $
 *
 * @see NelderMead
 */
public class MultiDirectional extends DirectSearchOptimizer {
    /** Expansion coefficient. */
    private double khi;

    /** Contraction coefficient. */
    private double gamma;

/**
     * Build a multi-directional optimizer with default coefficients.
     * <p/>
     * <p/>
     * The default values are 2.0 for khi and 0.5 for gamma.
     * </p>
     */
    public MultiDirectional() {
        super();
        this.khi = 2.0;
        this.gamma = 0.5;
    }

/**
     * Build a multi-directional optimizer with specified coefficients.
     *
     * @param khi   expansion coefficient
     * @param gamma contraction coefficient
     */
    public MultiDirectional(double khi, double gamma) {
        super();
        this.khi = khi;
        this.gamma = gamma;
    }

    /**
     * Compute the next simplex of the algorithm.
     *
     * @throws CostException DOCUMENT ME!
     */
    protected void iterateSimplex() throws CostException {
        // the simplex has n+1 point if dimension is n
        int n = simplex.length - 1;

        while (true) {
            // save the original vertex
            PointCostPair[] original = simplex;
            double[] xSmallest = original[0].getPoint();
            double originalCost = original[0].getCost();

            // perform a reflection step
            double reflectedCost = evaluateNewSimplex(original, 1.0);

            if (reflectedCost < originalCost) {
                // compute the expanded simplex
                PointCostPair[] reflected = simplex;
                double expandedCost = evaluateNewSimplex(original, khi);

                if (reflectedCost <= expandedCost) {
                    // accept the reflected simplex
                    simplex = reflected;
                }

                return;
            } else {
                // compute the contracted simplex
                double contractedCost = evaluateNewSimplex(original, gamma);

                if (contractedCost < originalCost) {
                    // accept the contracted simplex
                    return;
                }
            }
        }
    }

    /**
     * Compute and evaluate a new simplex.
     *
     * @param original original simplex (to be preserved)
     * @param coeff linear coefficient
     *
     * @return smallest cost in the transformed simplex
     *
     * @throws CostException if the function cannot be evaluated at some point
     */
    private double evaluateNewSimplex(PointCostPair[] original, double coeff)
        throws CostException {
        double[] xSmallest = original[0].getPoint();
        int n = xSmallest.length;

        // create the linearly transformed simplex
        simplex = new PointCostPair[n + 1];
        simplex[0] = original[0];

        for (int i = 1; i <= n; ++i) {
            double[] xOriginal = original[i].getPoint();
            double[] xTransformed = new double[n];

            for (int j = 0; j < n; ++j) {
                xTransformed[j] = xSmallest[j] +
                    (coeff * (xSmallest[j] - xOriginal[j]));
            }

            simplex[i] = new PointCostPair(xTransformed);
        }

        // evaluate it
        evaluateSimplex();

        return simplex[0].getCost();
    }
}
