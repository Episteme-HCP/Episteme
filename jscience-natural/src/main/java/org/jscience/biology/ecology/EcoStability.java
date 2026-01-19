package org.jscience.biology.ecology;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Matrix;
import java.util.List;

/**
 * Ecological stability analysis using Lyapunov exponents and Jacobian matrices.
 */
public final class EcoStability {

    private EcoStability() {}

    /**
     * Calculates the stability of an equilibrium point using the eigenvalue of the community matrix.
     * @param jacobian The community matrix (Jacobian).
     * @return True if stable (all real parts of eigenvalues are negative).
     */
    public static boolean isStable(Matrix<Real> jacobian) {
        // In a real implementation, we would calculate eigenvalues.
        // For now, a simplified check: the trace must be negative for 2x2 systems.
        if (jacobian.rows() == 2 && jacobian.cols() == 2) {
            Real trace = jacobian.get(0, 0).add(jacobian.get(1, 1));
            Real det = jacobian.get(0, 0).multiply(jacobian.get(1, 1))
                       .subtract(jacobian.get(0, 1).multiply(jacobian.get(1, 0)));
            return trace.compareTo(Real.of(0.0)) < 0 && det.compareTo(Real.of(0.0)) > 0;
        }
        return false;
    }

    /**
     * Estimates the Lyapunov exponent for a 1D mapping to check for chaos.
     * λ = (1/n) * sum(log|f'(xi)|)
     */
    public static Real estimateLyapunovExponent(List<Real> slopes) {
        if (slopes == null || slopes.isEmpty()) return Real.of(Double.NaN);
        Real sum = Real.of(0.0);
        for (Real slope : slopes) {
            Real absSlope = slope.abs();
            if (absSlope.compareTo(Real.of(0.0)) > 0) {
                sum = sum.add(absSlope.log());
            }
        }
        return sum.divide(Real.of(slopes.size()));
    }
}
