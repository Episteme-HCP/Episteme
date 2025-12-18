package org.jscience.mathematics.geometry.curves;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.geometry.PointND;
import org.jscience.mathematics.geometry.ParametricCurve;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Bézier curve.
 * <p>
 * A Bézier curve is defined by control points P₀, P₁, ..., Pₙ.
 * The curve is computed using the Bernstein polynomial basis:
 * </p>
 * <p>
 * C(t) = Σᵢ Bᵢ,ₙ(t) * Pᵢ
 * where Bᵢ,ₙ(t) = C(n,i) * t^i * (1-t)^(n-i)
 * </p>
 * <p>
 * Properties:
 * - Passes through first and last control points
 * - Tangent at endpoints determined by adjacent control points
 * - Lies within convex hull of control points
 * - Affine invariant
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class BezierCurve implements ParametricCurve {

    private final List<PointND> controlPoints;
    private final int degree;
    private final int dimension;

    /**
     * Creates a Bézier curve from control points.
     * 
     * @param controlPoints the control points (must have at least 2)
     */
    public BezierCurve(List<PointND> controlPoints) {
        if (controlPoints == null || controlPoints.size() < 2) {
            throw new IllegalArgumentException("Need at least 2 control points");
        }

        // Verify all points have same dimension
        int dim = controlPoints.get(0).ambientDimension();
        for (PointND p : controlPoints) {
            if (p.ambientDimension() != dim) {
                throw new IllegalArgumentException("All control points must have same dimension");
            }
        }

        this.controlPoints = new ArrayList<>(controlPoints);
        this.degree = controlPoints.size() - 1;
        this.dimension = dim;
    }

    @Override
    public PointND at(Real t) {
        // Use De Casteljau's algorithm for numerical stability
        return deCasteljau(controlPoints, t);
    }

    @Override
    public int dimension() {
        return dimension;
    }

    /**
     * De Casteljau's algorithm for evaluating Bézier curves.
     * <p>
     * This is numerically stable and works by recursive linear interpolation.
     * </p>
     * 
     * @param points the control points
     * @param t      the parameter
     * @return the point on the curve
     */
    private static PointND deCasteljau(List<PointND> points, Real t) {
        if (points.size() == 1) {
            return points.get(0);
        }

        // Linear interpolation between consecutive points
        List<PointND> newPoints = new ArrayList<>(points.size() - 1);

        for (int i = 0; i < points.size() - 1; i++) {
            PointND p0 = points.get(i);
            PointND p1 = points.get(i + 1);

            // Lerp: (1-t)*p0 + t*p1
            PointND interpolated = p0.interpolate(p1, t);
            newPoints.add(interpolated);
        }

        return deCasteljau(newPoints, t);
    }

    /**
     * Returns the control points.
     * 
     * @return the control points
     */
    public List<PointND> getControlPoints() {
        return new ArrayList<>(controlPoints);
    }

    /**
     * Returns the degree of the Bézier curve.
     * 
     * @return the degree (number of control points - 1)
     */
    public int getDegree() {
        return degree;
    }

    /**
     * Elevates the degree of the Bézier curve.
     * <p>
     * Degree elevation adds a control point while keeping the curve shape
     * unchanged.
     * </p>
     * 
     * @return a new Bézier curve with elevated degree
     */
    public BezierCurve elevateDegree() {
        int n = degree;
        List<PointND> newPoints = new ArrayList<>(n + 2);

        // First point stays the same
        newPoints.add(controlPoints.get(0));

        // Interior points
        for (int i = 1; i <= n; i++) {
            Real alpha = Real.of(i).divide(Real.of(n + 1));
            PointND p = controlPoints.get(i - 1).interpolate(controlPoints.get(i), alpha);
            newPoints.add(p);
        }

        // Last point stays the same
        newPoints.add(controlPoints.get(n));

        return new BezierCurve(newPoints);
    }

    /**
     * Subdivides the Bézier curve at parameter t.
     * <p>
     * Returns two Bézier curves that together form the original curve.
     * </p>
     * 
     * @param t the parameter at which to subdivide
     * @return array of two Bézier curves [left, right]
     */
    public BezierCurve[] subdivide(Real t) {
        // Use De Casteljau's algorithm to get subdivision points
        List<List<PointND>> pyramid = new ArrayList<>();
        pyramid.add(new ArrayList<>(controlPoints));

        // Build the De Casteljau pyramid
        for (int level = 1; level <= degree; level++) {
            List<PointND> prevLevel = pyramid.get(level - 1);
            List<PointND> currentLevel = new ArrayList<>();

            for (int i = 0; i < prevLevel.size() - 1; i++) {
                PointND interpolated = prevLevel.get(i).interpolate(prevLevel.get(i + 1), t);
                currentLevel.add(interpolated);
            }

            pyramid.add(currentLevel);
        }

        // Left curve: first point of each level
        List<PointND> leftPoints = new ArrayList<>();
        for (List<PointND> level : pyramid) {
            leftPoints.add(level.get(0));
        }

        // Right curve: last point of each level (in reverse order)
        List<PointND> rightPoints = new ArrayList<>();
        for (int i = pyramid.size() - 1; i >= 0; i--) {
            List<PointND> level = pyramid.get(i);
            rightPoints.add(level.get(level.size() - 1));
        }

        return new BezierCurve[] {
                new BezierCurve(leftPoints),
                new BezierCurve(rightPoints)
        };
    }

    @Override
    public String toString() {
        return "BezierCurve(degree=" + degree + ", points=" + controlPoints.size() + ")";
    }
}
