package org.jscience.mathematics.geometry.surfaces;

import org.jscience.mathematics.geometry.ParametricSurface;
import org.jscience.mathematics.geometry.PointND;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;

import java.util.Arrays;

/**
 * Represents a paraboloidal surface.
 * <p>
 * A paraboloid is a quadric surface with equation z = (x²/a²) + (y²/b²).
 * Parametric form: S(u, v) = (u, v, u²/a² + v²/b²)
 * </p>
 * <p>
 * Special cases:
 * - a = b: Circular paraboloid (paraboloid of revolution)
 * - a ≠ b: Elliptic paraboloid
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Paraboloid implements ParametricSurface {

    private final PointND vertex;
    private final Real scaleA;
    private final Real scaleB;

    /**
     * Creates a paraboloid with the given scale factors.
     * Vertex at origin.
     * 
     * @param scaleA the scale factor along x
     * @param scaleB the scale factor along y
     */
    public Paraboloid(Real scaleA, Real scaleB) {
        this(PointND.of(Real.ZERO, Real.ZERO, Real.ZERO), scaleA, scaleB);
    }

    /**
     * Creates a paraboloid with the given vertex and scale factors.
     * 
     * @param vertex the vertex point
     * @param scaleA the scale factor along x
     * @param scaleB the scale factor along y
     */
    public Paraboloid(PointND vertex, Real scaleA, Real scaleB) {
        if (vertex.ambientDimension() != 3) {
            throw new IllegalArgumentException("Paraboloid requires 3D vertex point");
        }
        this.vertex = vertex;
        this.scaleA = scaleA;
        this.scaleB = scaleB;
    }

    @Override
    public PointND at(Real u, Real v) {
        // S(u, v) = (u, v, u²/a² + v²/b²)
        Real x = vertex.get(0).add(u);
        Real y = vertex.get(1).add(v);

        Real termX = u.divide(scaleA).pow(Real.of(2));
        Real termY = v.divide(scaleB).pow(Real.of(2));
        Real z = vertex.get(2).add(termX.add(termY));

        return PointND.of(x, y, z);
    }

    @Override
    public Vector<Real> partialU(Real u, Real v, Real h) {
        // ∂S/∂u = (1, 0, 2u/a²)
        Real dx = Real.ONE;
        Real dy = Real.ZERO;
        Real dz = Real.of(2).multiply(u).divide(scaleA.multiply(scaleA));

        return new DenseVector<>(Arrays.asList(dx, dy, dz), org.jscience.mathematics.sets.Reals.getInstance());
    }

    @Override
    public Vector<Real> partialV(Real u, Real v, Real h) {
        // ∂S/∂v = (0, 1, 2v/b²)
        Real dx = Real.ZERO;
        Real dy = Real.ONE;
        Real dz = Real.of(2).multiply(v).divide(scaleB.multiply(scaleB));

        return new DenseVector<>(Arrays.asList(dx, dy, dz), org.jscience.mathematics.sets.Reals.getInstance());
    }

    @Override
    public Real gaussianCurvature(Real u, Real v, Real h) {
        // Gaussian curvature: K = 4/(a²b²(1 + 4u²/a⁴ + 4v²/b⁴)²)
        Real a2 = scaleA.multiply(scaleA);
        Real b2 = scaleB.multiply(scaleB);
        Real a4 = a2.multiply(a2);
        Real b4 = b2.multiply(b2);

        Real term1 = Real.of(4).multiply(u).multiply(u).divide(a4);
        Real term2 = Real.of(4).multiply(v).multiply(v).divide(b4);
        Real denom = Real.ONE.add(term1).add(term2);
        denom = denom.multiply(denom);

        return Real.of(4).divide(a2.multiply(b2).multiply(denom));
    }

    /**
     * Returns the scale factor along x.
     * 
     * @return the scale factor a
     */
    public Real getScaleA() {
        return scaleA;
    }

    /**
     * Returns the scale factor along y.
     * 
     * @return the scale factor b
     */
    public Real getScaleB() {
        return scaleB;
    }

    /**
     * Returns the vertex point.
     * 
     * @return the vertex
     */
    public PointND getVertex() {
        return vertex;
    }

    /**
     * Returns the focal length for a circular paraboloid (a = b).
     * f = a²/4
     * 
     * @return the focal length
     * @throws IllegalStateException if not a circular paraboloid
     */
    public Real focalLength() {
        if (!scaleA.equals(scaleB)) {
            throw new IllegalStateException("Focal length only defined for circular paraboloid");
        }
        return scaleA.multiply(scaleA).divide(Real.of(4));
    }
}
