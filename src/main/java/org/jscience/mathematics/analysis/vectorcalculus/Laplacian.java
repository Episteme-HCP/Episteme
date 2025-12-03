package org.jscience.mathematics.analysis.vectorcalculus;

import org.jscience.mathematics.analysis.ScalarField;
import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.geometry.PointND;
import java.util.ArrayList;
import java.util.List;

/**
 * Computes the Laplacian of a scalar field.
 * <p>
 * The Laplacian is the divergence of the gradient: ∇²f = ∇·(∇f)
 * </p>
 * <p>
 * Definition: ∇²f = ∂²f/∂x₁² + ∂²f/∂x₂² + ... + ∂²f/∂xₙ²
 * </p>
 * <p>
 * Physical interpretation and applications:
 * - Heat equation: ∂T/∂t = α∇²T (heat diffusion)
 * - Wave equation: ∂²u/∂t² = c²∇²u (wave propagation)
 * - Laplace's equation: ∇²φ = 0 (electrostatics, gravity)
 * - Poisson's equation: ∇²φ = -ρ/ε₀ (electrostatics with charges)
 * - Schrödinger equation: iℏ∂ψ/∂t = -ℏ²/(2m)∇²ψ + Vψ
 * - Harmonic functions: functions where ∇²f = 0
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Laplacian {

    /**
     * Computes the Laplacian of a scalar field at a point.
     * <p>
     * Uses second-order central difference:
     * ∂²f/∂xᵢ² ≈ (f(x + h*eᵢ) - 2f(x) + f(x - h*eᵢ)) / h²
     * </p>
     * 
     * @param field the scalar field
     * @param point the point at which to compute Laplacian
     * @param h     the step size for numerical differentiation
     * @return the Laplacian (scalar value)
     */
    public static Real compute(ScalarField<PointND> field, PointND point, Real h) {
        int n = point.ambientDimension();
        Real laplacian = Real.ZERO;

        Real fCenter = field.evaluate(point);
        Real hSquared = h.multiply(h);

        for (int i = 0; i < n; i++) {
            // Shift point in i-th direction
            PointND pointPlus = shiftPoint(point, i, h);
            PointND pointMinus = shiftPoint(point, i, h.negate());

            // Evaluate field at shifted points
            Real fPlus = field.evaluate(pointPlus);
            Real fMinus = field.evaluate(pointMinus);

            // Compute ∂²f/∂xᵢ² using three-point stencil
            Real secondDerivative = fPlus.add(fMinus).subtract(fCenter.multiply(Real.of(2)))
                    .divide(hSquared);

            laplacian = laplacian.add(secondDerivative);
        }

        return laplacian;
    }

    /**
     * Returns a scalar field representing the Laplacian of the input field.
     * 
     * @param field the scalar field
     * @param h     the step size for numerical differentiation
     * @return the Laplacian as a scalar field
     */
    public static ScalarField<PointND> asField(ScalarField<PointND> field, Real h) {
        return ScalarField.of(
                point -> compute(field, point, h),
                field.dimension());
    }

    /**
     * Computes the vector Laplacian of a vector field.
     * <p>
     * For a vector field F, the vector Laplacian is:
     * ∇²F = (∇²F₁, ∇²F₂, ..., ∇²Fₙ)
     * </p>
     * <p>
     * Note: In some contexts, ∇²F = ∇(∇·F) - ∇×(∇×F), but this is only
     * equivalent in 3D and for certain coordinate systems.
     * </p>
     * 
     * @param field the vector field
     * @param point the point
     * @param h     the step size
     * @return the vector Laplacian
     */
    public static org.jscience.mathematics.vector.Vector<Real> computeVector(
            org.jscience.mathematics.analysis.VectorField<PointND> field,
            PointND point,
            Real h) {

        int n = point.ambientDimension();
        List<Real> laplacianComponents = new ArrayList<>(n);

        // Compute Laplacian of each component
        for (int component = 0; component < n; component++) {
            final int comp = component;
            ScalarField<PointND> componentField = ScalarField.of(
                    p -> field.evaluate(p).get(comp),
                    n);
            laplacianComponents.add(compute(componentField, point, h));
        }

        return new org.jscience.mathematics.vector.DenseVector<>(
                laplacianComponents,
                org.jscience.mathematics.sets.Reals.getInstance());
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

