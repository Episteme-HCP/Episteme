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
package org.jscience.physics.relativity;

import org.jscience.mathematics.linearalgebra.tensors.Tensor;
import org.jscience.mathematics.topology.MetricSpace;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Interface representing a metric tensor field in spacetime.
 * <p>
 * Implementations define how the metric varies with coordinates.
 * Coordinates are provided as a {@link Vector4D} which represents the
 * tuple $(x^0, x^1, x^2, x^3)$ in the specific coordinate system used by the
 * metric.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface SpacetimeMetric extends MetricSpace<Vector4D> {

    /**
     * Calculates the covariant metric tensor $g_{\mu\nu}$ at the given event
     * coordinates.
     * <p>
     * Returns a rank-2 tensor of shape [4, 4].
     * </p>
     * 
     * @param point the spacetime coordinates
     * @return the metric tensor instance
     */
    Tensor<Real> getMetricTensor(Vector4D point);

    /**
     * Calculates the Christoffel symbols $\Gamma^\lambda_{\mu\nu}$ at the given
     * event coordinates.
     * <p>
     * Returns a rank-3 tensor of shape [4, 4, 4].
     * First index is upper ($\lambda$), second and third are lower ($\mu, \nu$).
     * </p>
     * 
     * @param point the spacetime coordinates
     * @return the Christoffel symbols tensor
     */
    /**
     * Default implementation returns null.
     * <p>
     * Calculating Christoffel symbols requires tensor inversion and numerical
     * differentiation. Concrete implementations (e.g., Schwarzschild, Kerr)
     * should override with analytic expressions.
     * </p>
     */
    default Tensor<Real> getChristoffelSymbols(Vector4D point) {
        // Christoffel symbols of the second kind: Gamma^k_ij
        // gamma^k_ij = 0.5 * g^kl * (d_i g_jl + d_j g_il - d_l g_ij)

        // 1. Calculate metric at point g_uv
        Tensor<Real> g = getMetricTensor(point);
        int dim = 4;

        // 2. Calculate inverse metric g^uv
        // Since we don't have a direct tensor inverse method for rank 2 tensor in the
        // interface yet (or it might be missing),
        // we can convert to Matrix, invert, and convert back, or implement 4x4 inverse
        // manually/helper.
        // Given this is a default method in an interface, we should rely on standard
        // tools.
        // Tensor has no inverse(). Matrix has.
        // Let's try to wrap tensor as Matrix if possible, or build a Matrix from it.

        // Build Matrix from Tensor
        org.jscience.mathematics.structures.rings.Field<Real> field = org.jscience.mathematics.sets.Reals.getInstance();
        Real[][] gData = new Real[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                gData[i][j] = g.get(i, j);
            }
        }
        org.jscience.mathematics.linearalgebra.Matrix<Real> gMatrix = org.jscience.mathematics.linearalgebra.matrices.MatrixFactory
                .create(gData, field);
        org.jscience.mathematics.linearalgebra.Matrix<Real> gInvMatrix = gMatrix.inverse();

        // Convert back to Tensor (g^kl)
        // We really need TensorFactory.of(Matrix) or similar but we can just use set.
        Tensor<Real> gUp = org.jscience.mathematics.linearalgebra.tensors.TensorFactory.zeros(Real.class, dim, dim);
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                gUp.set(gInvMatrix.get(i, j), i, j);
            }
        }

        // 3. Calculate partial derivatives d_k g_ij
        // We need derivatives with respect to x0, x1, x2, x3.
        // d_k g_ij approx (g_ij(x+h_k) - g_ij(x-h_k)) / 2h

        double hVal = 1e-8;
        Real h = Real.of(hVal);
        Real twoH = Real.of(2 * hVal);

        // We need a structure to hold d_k g_ij. Rank 3: [k, i, j]
        // Let's store as Tensor<Real> [dim] where array index is k, tensor is g_ij. No.
        // Let's conceptually compute it on fly or store in a [4][4][4] array?
        // Let's store in a Rank 3 tensor dg[k][i][j] = d_k g_ij

        Tensor<Real> dg = org.jscience.mathematics.linearalgebra.tensors.TensorFactory.zeros(Real.class, dim, dim, dim);

        for (int k = 0; k < dim; k++) {
            // Perturb coordinates
            // We need to create perturbed Vector4D
            Real[] coordsPlus = new Real[] { point.ct(), point.x(), point.y(), point.z() };
            Real[] coordsMinus = new Real[] { point.ct(), point.x(), point.y(), point.z() };

            coordsPlus[k] = coordsPlus[k].add(h);
            coordsMinus[k] = coordsMinus[k].subtract(h);

            Vector4D pPlus = new Vector4D(coordsPlus[0], coordsPlus[1], coordsPlus[2], coordsPlus[3]);
            Vector4D pMinus = new Vector4D(coordsMinus[0], coordsMinus[1], coordsMinus[2], coordsMinus[3]);

            Tensor<Real> gPlus = getMetricTensor(pPlus);
            Tensor<Real> gMinus = getMetricTensor(pMinus);

            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    Real val = gPlus.get(i, j).subtract(gMinus.get(i, j)).divide(twoH);
                    dg.set(val, k, i, j); // k is derivative index
                }
            }
        }

        // 4. Assemble Gamma^k_ij
        Tensor<Real> gamma = org.jscience.mathematics.linearalgebra.tensors.TensorFactory.zeros(Real.class, dim, dim,
                dim);
        Real half = Real.of(0.5);

        for (int lambda = 0; lambda < dim; lambda++) { // k in formula
            for (int mu = 0; mu < dim; mu++) { // i
                for (int nu = 0; nu < dim; nu++) { // j
                    Real sum = Real.ZERO;
                    for (int sigma = 0; sigma < dim; sigma++) { // l
                        // (d_mu g_sigma_nu + d_nu g_mu_sigma - d_sigma g_mu_nu)
                        // indices in dg are [derivative_idx, metric_row, metric_col]
                        // d_mu g_sigma_nu -> dg.get(mu, sigma, nu)
                        Real term1 = dg.get(mu, sigma, nu);
                        Real term2 = dg.get(nu, mu, sigma);
                        Real term3 = dg.get(sigma, mu, nu);

                        sum = sum.add(gUp.get(lambda, sigma).multiply(term1.add(term2).subtract(term3)));
                    }
                    gamma.set(half.multiply(sum), lambda, mu, nu);
                }
            }
        }

        return gamma;
    }

    /**
     * Calculates the spacetime interval (distance) between two events.
     * <p>
     * Note: In Lorentzian geometry, this is the proper time/distance.
     * </p>
     */
    /**
     * Calculates approximate spacetime interval using metric at midpoint.
     * <p>
     * For accurate geodesic distance in curved spacetime, use numerical
     * integration.
     * This provides a first-order approximation suitable for nearby events.
     * </p>
     */
    @Override
    default Real distance(Vector4D a, Vector4D b) {
        // Midpoint for metric evaluation
        Vector4D mid = new Vector4D(
                (a.ct().add(b.ct())).divide(Real.of(2)),
                (a.x().add(b.x())).divide(Real.of(2)),
                (a.y().add(b.y())).divide(Real.of(2)),
                (a.z().add(b.z())).divide(Real.of(2)));

        // Displacement vector
        Real[] dx = {
                b.ct().subtract(a.ct()),
                b.x().subtract(a.x()),
                b.y().subtract(a.y()),
                b.z().subtract(a.z())
        };

        // Get metric at midpoint
        Tensor<Real> g = getMetricTensor(mid);

        // Calculate ds^2 = g_uv dx^u dx^v
        Real ds2 = Real.ZERO;
        for (int u = 0; u < 4; u++) {
            for (int v = 0; v < 4; v++) {
                Real guv = g.get(u, v);
                ds2 = ds2.add(guv.multiply(dx[u]).multiply(dx[v]));
            }
        }

        // Return proper distance/time (sqrt of |ds^2|)
        return ds2.abs().sqrt();
    }

    // --- TopologicalSpace Defaults ---

    @Override
    default boolean containsPoint(Vector4D point) {
        return true;
    } // Defined everywhere usually

    @Override
    default boolean isOpen() {
        return true;
    }

    @Override
    default boolean isClosed() {
        return true;
    }

    @Override
    default String description() {
        return "Spacetime manifold equipped with a metric tensor";
    }
}
