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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.geometry.curves;

import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.DenseVector;
import org.jscience.mathematics.vector.Vector;
import org.jscience.mathematics.sets.Reals;

import java.util.ArrayList;
import java.util.List;

/**
 * B-spline curve (Basis spline).
 * <p>
 * A B-spline is a smooth piecewise polynomial curve defined by control points
 * and a knot vector. It provides local control - moving a control point only
 * affects a limited portion of the curve.
 * </p>
 * <p>
 * This implementation supports uniform B-splines of degree p (order p+1).
 * The curve is defined by:
 * - n+1 control points: P₀, P₁, ..., Pₙ
 * - Degree p (typically 2 for quadratic, 3 for cubic)
 * - Knot vector with m+1 values: t₀, t₁, ..., tₘ where m = n + p + 1
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class BSpline {

    private final List<Vector<Real>> controlPoints;
    private final List<Real> knotVector;
    private final int degree;
    private final int dimension;

    /**
     * Creates a uniform B-spline with the given control points and degree.
     * 
     * @param controlPoints the control points
     * @param degree        the degree (2 = quadratic, 3 = cubic)
     */
    public BSpline(List<Vector<Real>> controlPoints, int degree) {
        if (controlPoints.size() < degree + 1) {
            throw new IllegalArgumentException(
                    "B-spline requires at least " + (degree + 1) + " control points for degree " + degree);
        }
        this.controlPoints = new ArrayList<>(controlPoints);
        this.degree = degree;
        this.dimension = controlPoints.get(0).dimension();

        // Verify all points have same dimension
        for (Vector<Real> p : controlPoints) {
            if (p.dimension() != dimension) {
                throw new IllegalArgumentException("All control points must have the same dimension");
            }
        }

        // Create uniform knot vector
        this.knotVector = createUniformKnotVector(controlPoints.size(), degree);
    }

    /**
     * Creates a B-spline with custom knot vector.
     * 
     * @param controlPoints the control points
     * @param degree        the degree
     * @param knotVector    the knot vector
     */
    public BSpline(List<Vector<Real>> controlPoints, int degree, List<Real> knotVector) {
        if (controlPoints.size() < degree + 1) {
            throw new IllegalArgumentException(
                    "B-spline requires at least " + (degree + 1) + " control points for degree " + degree);
        }

        int n = controlPoints.size() - 1;
        int m = n + degree + 1;
        if (knotVector.size() != m + 1) {
            throw new IllegalArgumentException(
                    "Knot vector must have " + (m + 1) + " elements for " + controlPoints.size()
                            + " control points and degree " + degree);
        }

        this.controlPoints = new ArrayList<>(controlPoints);
        this.degree = degree;
        this.dimension = controlPoints.get(0).dimension();
        this.knotVector = new ArrayList<>(knotVector);
    }

    /**
     * Creates a uniform knot vector.
     */
    private static List<Real> createUniformKnotVector(int numControlPoints, int degree) {
        int n = numControlPoints - 1;
        int m = n + degree + 1;
        List<Real> knots = new ArrayList<>();

        // Clamped uniform knot vector: [0, 0, ..., 0, 1/(n-p+1), 2/(n-p+1), ..., 1, 1,
        // ..., 1]
        // p+1 zeros at start, p+1 ones at end
        for (int i = 0; i <= m; i++) {
            if (i <= degree) {
                knots.add(Real.ZERO);
            } else if (i >= m - degree) {
                knots.add(Real.ONE);
            } else {
                Real value = Real.of((double) (i - degree) / (m - 2 * degree));
                knots.add(value);
            }
        }

        return knots;
    }

    public Vector<Real> evaluate(Real t) {
        // Clamp t to [0, 1]
        if (t.compareTo(Real.ZERO) < 0)
            t = Real.ZERO;
        if (t.compareTo(Real.ONE) > 0)
            t = Real.ONE;

        // Find the knot span
        int span = findKnotSpan(t);

        // Compute basis functions
        List<Real> basis = computeBasisFunctions(span, t);

        // Compute curve point
        List<Real> result = new ArrayList<>();
        for (int dim = 0; dim < dimension; dim++) {
            Real sum = Real.ZERO;
            for (int i = 0; i <= degree; i++) {
                int cpIndex = span - degree + i;
                if (cpIndex >= 0 && cpIndex < controlPoints.size()) {
                    Real contribution = controlPoints.get(cpIndex).get(dim).multiply(basis.get(i));
                    sum = sum.add(contribution);
                }
            }
            result.add(sum);
        }

        return new DenseVector<>(result, Reals.getInstance());
    }

    /**
     * Finds the knot span for parameter t.
     */
    private int findKnotSpan(Real t) {
        int n = controlPoints.size() - 1;

        // Special case for t = 1
        if (t.equals(knotVector.get(n + 1))) {
            return n;
        }

        // Binary search
        int low = degree;
        int high = n + 1;

        while (high - low > 1) {
            int mid = (low + high) / 2;
            if (t.compareTo(knotVector.get(mid)) < 0) {
                high = mid;
            } else {
                low = mid;
            }
        }

        return low;
    }

    /**
     * Computes basis functions using Cox-de Boor recursion.
     */
    private List<Real> computeBasisFunctions(int span, Real t) {
        List<Real> basis = new ArrayList<>();
        for (int i = 0; i <= degree; i++) {
            basis.add(Real.ZERO);
        }

        // Initialize degree 0
        basis.set(0, Real.ONE);

        // Compute higher degrees
        List<Real> left = new ArrayList<>();
        List<Real> right = new ArrayList<>();

        for (int j = 1; j <= degree; j++) {
            left.clear();
            right.clear();

            for (int r = 0; r < j; r++) {
                left.add(t.subtract(knotVector.get(span + 1 - j + r)));
                right.add(knotVector.get(span + r + 1).subtract(t));
            }

            Real saved = Real.ZERO;
            for (int r = 0; r < j; r++) {
                Real temp = basis.get(r).divide(right.get(r).add(left.get(r)));
                basis.set(r, saved.add(right.get(r).multiply(temp)));
                saved = left.get(r).multiply(temp);
            }
            basis.set(j, saved);
        }

        return basis;
    }

    public Vector<Real> tangent(Real t) {
        // Numerical derivative
        Real epsilon = Real.of(1e-6);
        Vector<Real> p1 = evaluate(t);
        Vector<Real> p2 = evaluate(t.add(epsilon));

        List<Real> tangent = new ArrayList<>();
        for (int i = 0; i < dimension; i++) {
            Real derivative = p2.get(i).subtract(p1.get(i)).divide(epsilon);
            tangent.add(derivative);
        }

        return new DenseVector<>(tangent, Reals.getInstance());
    }

    /**
     * Returns the control points.
     * 
     * @return the control points
     */
    public List<Vector<Real>> getControlPoints() {
        return new ArrayList<>(controlPoints);
    }

    /**
     * Returns the knot vector.
     * 
     * @return the knot vector
     */
    public List<Real> getKnotVector() {
        return new ArrayList<>(knotVector);
    }

    /**
     * Returns the degree.
     * 
     * @return the degree
     */
    public int getDegree() {
        return degree;
    }

    @Override
    public String toString() {
        return "BSpline(points=" + controlPoints.size() + ", degree=" + degree + ")";
    }
}
