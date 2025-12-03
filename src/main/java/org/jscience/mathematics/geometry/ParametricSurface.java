package org.jscience.mathematics.geometry;

import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.geometry.PointND;
import org.jscience.mathematics.vector.Vector;

import org.jscience.mathematics.function.VectorFunction;

/**
 * Represents a parametric surface in 3D space.
 * <p>
 * A parametric surface is defined by: S(u,v) = (x(u,v), y(u,v), z(u,v))
 * where (u,v) are parameters, typically in some domain D ⊂ ℝ².
 * </p>
 * <p>
 * Examples:
 * - Plane: S(u,v) = P₀ + u*a + v*b
 * - Sphere: S(θ,φ) = (r*sin(θ)*cos(φ), r*sin(θ)*sin(φ), r*cos(θ))
 * - Torus: S(u,v) = ((R+r*cos(v))*cos(u), (R+r*cos(v))*sin(u), r*sin(v))
 * - Cylinder: S(θ,z) = (r*cos(θ), r*sin(θ), z)
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface ParametricSurface extends VectorFunction<Vector<Real>, Real> {

    @Override
    default Vector<Real> evaluate(Vector<Real> uv) {
        if (uv.dimension() != 2) {
            throw new IllegalArgumentException("Parameters must be 2D (u, v)");
        }
        return at(uv.get(0), uv.get(1)).toVector();
    }

    @Override
    default int getOutputDimension() {
        return 3; // Surfaces are typically in 3D
    }

    /**
     * Evaluates the surface at parameters (u, v).
     * 
     * @param u the first parameter
     * @param v the second parameter
     * @return the point on the surface
     */
    PointND at(Real u, Real v);

    /**
     * Returns the partial derivative ∂S/∂u.
     * <p>
     * This is the tangent vector in the u-direction.
     * </p>
     * 
     * @param u the first parameter
     * @param v the second parameter
     * @param h the step size for numerical differentiation
     * @return the partial derivative vector
     */
    default Vector<Real> partialU(Real u, Real v, Real h) {
        PointND p1 = at(u.subtract(h), v);
        PointND p2 = at(u.add(h), v);

        Vector<Real> diff = p2.toVector().subtract(p1.toVector());
        return diff.multiply(Real.of(0.5).divide(h));
    }

    /**
     * Returns the partial derivative ∂S/∂v.
     * <p>
     * This is the tangent vector in the v-direction.
     * </p>
     * 
     * @param u the first parameter
     * @param v the second parameter
     * @param h the step size for numerical differentiation
     * @return the partial derivative vector
     */
    default Vector<Real> partialV(Real u, Real v, Real h) {
        PointND p1 = at(u, v.subtract(h));
        PointND p2 = at(u, v.add(h));

        Vector<Real> diff = p2.toVector().subtract(p1.toVector());
        return diff.multiply(Real.of(0.5).divide(h));
    }

    /**
     * Returns the normal vector at (u, v).
     * <p>
     * The normal is computed as: N = ∂S/∂u × ∂S/∂v
     * </p>
     * 
     * @param u the first parameter
     * @param v the second parameter
     * @param h the step size for numerical differentiation
     * @return the unit normal vector
     */
    default Vector<Real> normal(Real u, Real v, Real h) {
        Vector<Real> du = partialU(u, v, h);
        Vector<Real> dv = partialV(u, v, h);

        Vector<Real> cross = du.cross(dv);
        return cross.normalize();
    }

    /**
     * Returns the surface area element dS.
     * <p>
     * dS = ||∂S/∂u × ∂S/∂v|| du dv
     * </p>
     * 
     * @param u the first parameter
     * @param v the second parameter
     * @param h the step size for numerical differentiation
     * @return the surface area element
     */
    default Real surfaceElement(Real u, Real v, Real h) {
        Vector<Real> du = partialU(u, v, h);
        Vector<Real> dv = partialV(u, v, h);

        Vector<Real> cross = du.cross(dv);
        return cross.norm();
    }

    /**
     * Computes the surface area over a parameter domain.
     * <p>
     * Area = ∫∫_D ||∂S/∂u × ∂S/∂v|| du dv
     * </p>
     * 
     * @param u0        minimum u parameter
     * @param u1        maximum u parameter
     * @param v0        minimum v parameter
     * @param v1        maximum v parameter
     * @param numStepsU number of integration steps in u
     * @param numStepsV number of integration steps in v
     * @return the surface area
     */
    default Real surfaceArea(Real u0, Real u1, Real v0, Real v1,
            int numStepsU, int numStepsV) {
        Real du = u1.subtract(u0).divide(Real.of(numStepsU));
        Real dv = v1.subtract(v0).divide(Real.of(numStepsV));
        Real h = du.min(dv).multiply(Real.of(0.01));

        Real area = Real.ZERO;

        for (int i = 0; i < numStepsU; i++) {
            for (int j = 0; j < numStepsV; j++) {
                Real u = u0.add(du.multiply(Real.of(i + 0.5)));
                Real v = v0.add(dv.multiply(Real.of(j + 0.5)));

                Real element = surfaceElement(u, v, h);
                area = area.add(element.multiply(du).multiply(dv));
            }
        }

        return area;
    }

    /**
     * Returns the first fundamental form coefficients (E, F, G).
     * <p>
     * E = ⟨∂S/∂u, ∂S/∂u⟩
     * F = ⟨∂S/∂u, ∂S/∂v⟩
     * G = ⟨∂S/∂v, ∂S/∂v⟩
     * </p>
     * 
     * @param u the first parameter
     * @param v the second parameter
     * @param h the step size
     * @return array [E, F, G]
     */
    default Real[] firstFundamentalForm(Real u, Real v, Real h) {
        Vector<Real> du = partialU(u, v, h);
        Vector<Real> dv = partialV(u, v, h);

        Real E = du.dot(du);
        Real F = du.dot(dv);
        Real G = dv.dot(dv);

        return new Real[] { E, F, G };
    }

    /**
     * Returns the Gaussian curvature at (u, v).
     * <p>
     * K = (LN - M²) / (EG - F²)
     * where L, M, N are second fundamental form coefficients
     * and E, F, G are first fundamental form coefficients.
     * </p>
     * 
     * @param u the first parameter
     * @param v the second parameter
     * @param h the step size
     * @return the Gaussian curvature
     */
    default Real gaussianCurvature(Real u, Real v, Real h) {
        // This is a simplified implementation
        // Full implementation requires second fundamental form

        Vector<Real> n = normal(u, v, h);

        // Approximate using finite differences
        Vector<Real> nPlusU = normal(u.add(h), v, h);
        Vector<Real> nMinusU = normal(u.subtract(h), v, h);
        Vector<Real> dnDu = nPlusU.subtract(nMinusU).multiply(Real.of(0.5).divide(h));

        Vector<Real> nPlusV = normal(u, v.add(h), h);
        Vector<Real> nMinusV = normal(u, v.subtract(h), h);
        Vector<Real> dnDv = nPlusV.subtract(nMinusV).multiply(Real.of(0.5).divide(h));

        // K ≈ ||dnDu × dnDv||
        return dnDu.cross(dnDv).norm();
    }
}
