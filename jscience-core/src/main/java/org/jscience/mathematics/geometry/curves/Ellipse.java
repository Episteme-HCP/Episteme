package org.jscience.mathematics.geometry.curves;

import org.jscience.mathematics.geometry.ParametricCurve;
import org.jscience.mathematics.geometry.PointND;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;

import java.util.Arrays;

/**
 * Represents an ellipse curve.
 * <p>
 * An ellipse is a conic section with two focal points.
 * Parametric form: C(t) = (a·cos(t), b·sin(t))
 * where a is the semi-major axis and b is the semi-minor axis.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Ellipse implements ParametricCurve {

    private final PointND center;
    private final Real semiMajorAxis;
    private final Real semiMinorAxis;
    private final int dimension;

    /**
     * Creates a 2D ellipse with the given semi-axes.
     * 
     * @param semiMajorAxis the semi-major axis (a)
     * @param semiMinorAxis the semi-minor axis (b)
     */
    public Ellipse(Real semiMajorAxis, Real semiMinorAxis) {
        this(PointND.of(Real.ZERO, Real.ZERO), semiMajorAxis, semiMinorAxis);
    }

    /**
     * Creates a 2D ellipse with the given center and semi-axes.
     * 
     * @param center        the center point
     * @param semiMajorAxis the semi-major axis (a)
     * @param semiMinorAxis the semi-minor axis (b)
     */
    public Ellipse(PointND center, Real semiMajorAxis, Real semiMinorAxis) {
        if (center.ambientDimension() != 2) {
            throw new IllegalArgumentException("Ellipse requires 2D center point");
        }
        this.center = center;
        this.semiMajorAxis = semiMajorAxis;
        this.semiMinorAxis = semiMinorAxis;
        this.dimension = 2;
    }

    @Override
    public PointND at(Real t) {
        Real x = center.get(0).add(semiMajorAxis.multiply(Real.of(Math.cos(t.doubleValue()))));
        Real y = center.get(1).add(semiMinorAxis.multiply(Real.of(Math.sin(t.doubleValue()))));
        return PointND.of(x, y);
    }

    @Override
    public int dimension() {
        return dimension;
    }

    @Override
    public Vector<Real> tangent(Real t, Real h) {
        // Analytical tangent: C'(t) = (-a·sin(t), b·cos(t))
        Real dx = semiMajorAxis.multiply(Real.of(Math.sin(t.doubleValue()))).negate();
        Real dy = semiMinorAxis.multiply(Real.of(Math.cos(t.doubleValue())));
        return new DenseVector<>(Arrays.asList(dx, dy), org.jscience.mathematics.sets.Reals.getInstance());
    }

    @Override
    public Real curvature(Real t, Real h) {
        // Analytical curvature: κ = ab / (a²sin²(t) + b²cos²(t))^(3/2)
        Real a = semiMajorAxis;
        Real b = semiMinorAxis;
        Real sinT = Real.of(Math.sin(t.doubleValue()));
        Real cosT = Real.of(Math.cos(t.doubleValue()));

        Real a2sin2 = a.multiply(a).multiply(sinT).multiply(sinT);
        Real b2cos2 = b.multiply(b).multiply(cosT).multiply(cosT);
        Real denom = a2sin2.add(b2cos2).pow(Real.of(1.5));

        return a.multiply(b).divide(denom);
    }

    /**
     * Returns the semi-major axis.
     * 
     * @return the semi-major axis
     */
    public Real getSemiMajorAxis() {
        return semiMajorAxis;
    }

    /**
     * Returns the semi-minor axis.
     * 
     * @return the semi-minor axis
     */
    public Real getSemiMinorAxis() {
        return semiMinorAxis;
    }

    /**
     * Returns the center point.
     * 
     * @return the center
     */
    public PointND getCenter() {
        return center;
    }

    /**
     * Returns the eccentricity of the ellipse.
     * e = √(1 - (b/a)²)
     * 
     * @return the eccentricity
     */
    public Real eccentricity() {
        Real ratio = semiMinorAxis.divide(semiMajorAxis);
        return Real.ONE.subtract(ratio.multiply(ratio)).sqrt();
    }

    /**
     * Returns the area of the ellipse.
     * A = πab
     * 
     * @return the area
     */
    public Real area() {
        return Real.of(Math.PI).multiply(semiMajorAxis).multiply(semiMinorAxis);
    }

    /**
     * Returns the approximate perimeter using Ramanujan's approximation.
     * P ≈ π(3(a+b) - √((3a+b)(a+3b)))
     * 
     * @return the approximate perimeter
     */
    public Real perimeter() {
        Real a = semiMajorAxis;
        Real b = semiMinorAxis;
        Real sum = a.add(b);
        Real term1 = Real.of(3).multiply(a).add(b);
        Real term2 = a.add(Real.of(3).multiply(b));
        Real sqrt = term1.multiply(term2).sqrt();
        return Real.of(Math.PI).multiply(Real.of(3).multiply(sum).subtract(sqrt));
    }
}
