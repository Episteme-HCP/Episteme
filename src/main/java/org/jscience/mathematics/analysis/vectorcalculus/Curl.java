package org.jscience.mathematics.analysis.vectorcalculus;

import org.jscience.mathematics.analysis.VectorField;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.geometry.PointND;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Computes the curl of a 3D vector field.
 * <p>
 * The curl measures the rotation or circulation of a vector field.
 * </p>
 * <p>
 * Definition (3D only): ∇×F = (∂F₃/∂y - ∂F₂/∂z, ∂F₁/∂z - ∂F₃/∂x, ∂F₂/∂x -
 * ∂F₁/∂y)
 * </p>
 * <p>
 * Physical interpretation:
 * - Measures local rotation/vorticity
 * - Fluid dynamics: vorticity ω = ∇×v
 * - Electromagnetism: Faraday's law ∇×E = -∂B/∂t
 * - Ampère's law: ∇×B = μ₀J + μ₀ε₀∂E/∂t
 * - Conservative fields have zero curl: ∇×(∇φ) = 0
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Curl {

    /**
     * Computes the curl of a 3D vector field at a point.
     * <p>
     * The curl is only defined for 3D vector fields.
     * </p>
     * 
     * @param field the vector field (must be 3D)
     * @param point the point at which to compute curl
     * @param h     the step size for numerical differentiation
     * @return the curl vector
     * @throws IllegalArgumentException if field is not 3D
     */
    public static Vector<Real> compute(VectorField<PointND> field, PointND point, Real h) {
        if (point.ambientDimension() != 3) {
            throw new IllegalArgumentException("Curl is only defined for 3D vector fields");
        }

        // curl = (∂F₃/∂y - ∂F₂/∂z, ∂F₁/∂z - ∂F₃/∂x, ∂F₂/∂x - ∂F₁/∂y)
        // = (∂F₂/∂x₁ - ∂F₁/∂x₂, ∂F₀/∂x₂ - ∂F₂/∂x₀, ∂F₁/∂x₀ - ∂F₀/∂x₁)
        // where x₀=x, x₁=y, x₂=z and F₀=Fx, F₁=Fy, F₂=Fz

        // curlX = ∂Fz/∂y - ∂Fy/∂z = ∂F₂/∂x₁ - ∂F₁/∂x₂
        Real curlX = partialDerivative(field, point, 2, 1, h)
                .subtract(partialDerivative(field, point, 1, 2, h));

        // curlY = ∂Fx/∂z - ∂Fz/∂x = ∂F₀/∂x₂ - ∂F₂/∂x₀
        Real curlY = partialDerivative(field, point, 0, 2, h)
                .subtract(partialDerivative(field, point, 2, 0, h));

        // curlZ = ∂Fy/∂x - ∂Fx/∂y = ∂F₁/∂x₀ - ∂F₀/∂x₁
        Real curlZ = partialDerivative(field, point, 1, 0, h)
                .subtract(partialDerivative(field, point, 0, 1, h));

        return new DenseVector<>(
                Arrays.asList(curlX, curlY, curlZ),
                org.jscience.mathematics.sets.Reals.getInstance());
    }

    /**
     * Returns a vector field representing the curl of the input field.
     * 
     * @param field the vector field (must be 3D)
     * @param h     the step size for numerical differentiation
     * @return the curl as a vector field
     */
    public static VectorField<PointND> asField(VectorField<PointND> field, Real h) {
        return VectorField.of(
                point -> compute(field, point, h),
                3);
    }

    /**
     * Computes the partial derivative ∂Fᵢ/∂xⱼ at a point.
     * 
     * @param field          the vector field
     * @param point          the point
     * @param componentIndex the component index i (which component of F)
     * @param variableIndex  the variable index j (which coordinate)
     * @param h              the step size
     * @return the partial derivative
     */
    private static Real partialDerivative(
            VectorField<PointND> field,
            PointND point,
            int componentIndex,
            int variableIndex,
            Real h) {

        PointND pointPlus = shiftPoint(point, variableIndex, h);
        PointND pointMinus = shiftPoint(point, variableIndex, h.negate());

        Real fPlus = field.evaluate(pointPlus).get(componentIndex);
        Real fMinus = field.evaluate(pointMinus).get(componentIndex);

        return fPlus.subtract(fMinus).divide(h.multiply(Real.of(2)));
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
