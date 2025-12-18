package org.jscience.physics.relativity;

import org.jscience.mathematics.linearalgebra.tensors.Tensor;
import org.jscience.mathematics.linearalgebra.tensors.DenseTensor;
import org.jscience.mathematics.numbers.real.Real;

/**
 * The Schwarzschild metric describing spacetime around a static, spherically
 * summetric mass.
 * <p>
 * Coordinates must be spherical: $(ct, r, \theta, \phi)$.
 * Metric signature: $(-, +, +, +)$ used here for consistency with typical
 * particle physics conventions,
 * or $(+, -, -, -)$ widely used in GR texts.
 * To be specific:
 * $ds^2 = -(1 - r_s/r)(c dt)^2 + (1 - r_s/r)^{-1} dr^2 + r^2 d\Omega^2$
 * using $(-+++)$ signature.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 3.2
 */
public class SchwarzschildMetric implements SpacetimeMetric {

    private final Real schwarzschildRadius;

    /**
     * @param schwarzschildRadius ($r_s = 2GM/c^2$)
     */
    public SchwarzschildMetric(Real schwarzschildRadius) {
        this.schwarzschildRadius = schwarzschildRadius;
    }

    @Override
    public Tensor<Real> getMetricTensor(Vector4D point) {
        // point: (ct, r, theta, phi)
        // Indices: 0=t, 1=r, 2=theta, 3=phi

        Real r = point.x(); // x^1 is r
        Real theta = point.y(); // x^2 is theta

        // Components
        // g_00 = -(1 - r_s/r)
        // g_11 = (1 - r_s/r)^-1
        // g_22 = r^2
        // g_33 = r^2 * sin^2(theta)

        // Avoid division by zero at singularity or horizon if handled strictly
        // For MVP, simplistic calculation.

        Real ratio = schwarzschildRadius.divide(r);
        Real factor = Real.ONE.subtract(ratio);

        Real g00 = factor.negate(); // -(1 - rs/r)
        Real g11 = factor.inverse(); // 1/(1 - rs/r)
        Real g22 = r.pow(2);
        Real g33 = r.pow(2).multiply(theta.sin().pow(2));

        // Construct 4x4 diagonal matrix (rank 2 tensor)
        Real zero = Real.ZERO;

        // Row 0
        // Row 1
        // Row 2
        // Row 3
        // Flat array for DenseTensor [4x4 = 16 elements]
        Real[] data = new Real[] {
                g00, zero, zero, zero,
                zero, g11, zero, zero,
                zero, zero, g22, zero,
                zero, zero, zero, g33
        };

        return new DenseTensor<>(data, 4, 4);
    }

    // --- Set / TopologicalSpace Defaults ---

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Vector4D item) {
        return true;
    }
}
