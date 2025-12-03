package org.jscience.mathematics.analysis.vectorcalculus;

import org.jscience.mathematics.analysis.ScalarField;
import org.jscience.mathematics.analysis.VectorField;
import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.Vector;
import org.jscience.mathematics.geometry.PointND;
import java.util.ArrayList;
import java.util.List;

/**
 * Computes the divergence of a vector field.
 * <p>
 * The divergence measures the magnitude of a field's source or sink at a given
 * point.
 * </p>
 * <p>
 * Definition: ∇·F = ∂F₁/∂x₁ + ∂F₂/∂x₂ + ... + ∂Fₙ/∂xₙ
 * </p>
 * <p>
 * Physical interpretation:
 * - Positive divergence: source (field lines emanating outward)
 * - Negative divergence: sink (field lines converging inward)
 * - Zero divergence: incompressible flow
 * - Gauss's law: ∇·E = ρ/ε₀ (electric field divergence equals charge density)
 * - Continuity equation: ∂ρ/∂t + ∇·(ρv) = 0
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Divergence {

    /**
     * Computes the divergence of a vector field at a point using finite
     * differences.
     * <p>
     * Uses central difference for each component:
     * ∂Fᵢ/∂xᵢ ≈ (Fᵢ(x + h*eᵢ) - Fᵢ(x - h*eᵢ)) / (2h)
     * </p>
     * 
     * @param field the vector field
     * @param point the point at which to compute divergence
     * @param h     the step size for numerical differentiation
     * @return the divergence (scalar value)
     */
    public static Real compute(VectorField<PointND> field, PointND point, Real h) {
        int n = point.ambientDimension();
        Real divergence = Real.ZERO;

        for (int i = 0; i < n; i++) {
            // Shift point in i-th direction
            PointND pointPlus = shiftPoint(point, i, h);
            PointND pointMinus = shiftPoint(point, i, h.negate());

            // Evaluate field at shifted points
            Vector<Real> fPlus = field.evaluate(pointPlus);
            Vector<Real> fMinus = field.evaluate(pointMinus);

            // Compute ∂Fᵢ/∂xᵢ
            Real partialDerivative = fPlus.get(i).subtract(fMinus.get(i))
                    .divide(h.multiply(Real.of(2)));

            divergence = divergence.add(partialDerivative);
        }

        return divergence;
    }

    /**
     * Returns a scalar field representing the divergence of the vector field.
     * 
     * @param field the vector field
     * @param h     the step size for numerical differentiation
     * @return the divergence as a scalar field
     */
    public static ScalarField<PointND> asField(VectorField<PointND> field, Real h) {
        return ScalarField.of(
                point -> compute(field, point, h),
                field.dimension());
    }

    /**
     * Shifts a point by a given amount in a specific coordinate direction.
     */
    private static PointND shiftPoint(PointND point, int coordinateIndex, Real delta) {
        int n = point.ambientDimension();
        List<Real> newCoords = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            if (i == coordinateIndex) {
                newCoords.add(point.get(i).add(delta));
            } else {
                newCoords.add(point.get(i));
            }
        }

        return new PointND(newCoords);
    }
}

