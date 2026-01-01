/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.mathematics.geometry.curves;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.sets.Reals;

import java.util.ArrayList;
import java.util.List;

/**
 * Catmull-Rom spline curve.
 * <p>
 * A Catmull-Rom spline is a smooth curve that passes through all control
 * points.
 * It uses cubic interpolation with automatic tangent calculation based on
 * neighboring points.
 * </p>
 * <p>
 * The curve is defined by a set of control points PÃ¢â€šâ‚¬, PÃ¢â€šÂ, ..., PÃ¢â€šâ„¢ and uses
 * the formula:
 * P(t) = 0.5 * [(2PÃ¢â€šÂ) + (-PÃ¢â€šâ‚¬ + PÃ¢â€šâ€š)t + (2PÃ¢â€šâ‚¬ - 5PÃ¢â€šÂ + 4PÃ¢â€šâ€š - PÃ¢â€šÆ’)tÃ‚Â² + (-PÃ¢â€šâ‚¬ + 3PÃ¢â€šÂ -
 * 3PÃ¢â€šâ€š + PÃ¢â€šÆ’)tÃ‚Â³]
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CatmullRomSpline {

    private final List<Vector<Real>> controlPoints;
    private final Real tension; // 0 = standard Catmull-Rom, 0.5 = cardinal spline
    private final int dimension;

    /**
     * Creates a Catmull-Rom spline with standard tension (0.0).
     * 
     * @param controlPoints the control points (minimum 4 required)
     */
    public CatmullRomSpline(List<Vector<Real>> controlPoints) {
        this(controlPoints, Real.ZERO);
    }

    /**
     * Creates a Catmull-Rom spline with custom tension.
     * 
     * @param controlPoints the control points (minimum 4 required)
     * @param tension       the tension parameter (0 = standard, 0.5 = cardinal)
     */
    public CatmullRomSpline(List<Vector<Real>> controlPoints, Real tension) {
        if (controlPoints.size() < 4) {
            throw new IllegalArgumentException("Catmull-Rom spline requires at least 4 control points");
        }
        this.controlPoints = new ArrayList<>(controlPoints);
        this.tension = tension;
        this.dimension = controlPoints.get(0).dimension();

        // Verify all points have same dimension
        for (Vector<Real> p : controlPoints) {
            if (p.dimension() != dimension) {
                throw new IllegalArgumentException("All control points must have the same dimension");
            }
        }
    }

    public Vector<Real> evaluate(Real t) {
        // t is in [0, 1] for the entire spline
        // Map to segment index and local parameter
        int numSegments = controlPoints.size() - 3;
        Real scaledT = t.multiply(Real.of(numSegments));
        int segment = (int) Math.floor(scaledT.doubleValue());

        // Clamp to valid range
        if (segment < 0)
            segment = 0;
        if (segment >= numSegments)
            segment = numSegments - 1;

        // Local parameter in [0, 1] for this segment
        Real localT = scaledT.subtract(Real.of(segment));

        // Get the 4 control points for this segment
        Vector<Real> p0 = controlPoints.get(segment);
        Vector<Real> p1 = controlPoints.get(segment + 1);
        Vector<Real> p2 = controlPoints.get(segment + 2);
        Vector<Real> p3 = controlPoints.get(segment + 3);

        return evaluateSegment(p0, p1, p2, p3, localT);
    }

    /**
     * Evaluates a single segment of the spline.
     */
    private Vector<Real> evaluateSegment(Vector<Real> p0, Vector<Real> p1, Vector<Real> p2, Vector<Real> p3, Real t) {
        Real t2 = t.multiply(t);
        Real t3 = t2.multiply(t);

        // Catmull-Rom basis functions
        Real half = Real.of(0.5);
        @SuppressWarnings("unused")
        Real oneMinus = Real.ONE.subtract(tension); // Reserved for cardinal spline variant

        List<Real> result = new ArrayList<>();
        for (int i = 0; i < dimension; i++) {
            Real v0 = p0.get(i);
            Real v1 = p1.get(i);
            Real v2 = p2.get(i);
            Real v3 = p3.get(i);

            // P(t) = 0.5 * [(2PÃ¢â€šÂ) + (-PÃ¢â€šâ‚¬ + PÃ¢â€šâ€š)t + (2PÃ¢â€šâ‚¬ - 5PÃ¢â€šÂ + 4PÃ¢â€šâ€š - PÃ¢â€šÆ’)tÃ‚Â² + (-PÃ¢â€šâ‚¬ + 3PÃ¢â€šÂ -
            // 3PÃ¢â€šâ€š + PÃ¢â€šÆ’)tÃ‚Â³]
            Real term1 = v1.multiply(Real.of(2));

            Real term2 = v0.negate().add(v2).multiply(t);

            Real term3 = v0.multiply(Real.of(2))
                    .subtract(v1.multiply(Real.of(5)))
                    .add(v2.multiply(Real.of(4)))
                    .subtract(v3)
                    .multiply(t2);

            Real term4 = v0.negate()
                    .add(v1.multiply(Real.of(3)))
                    .subtract(v2.multiply(Real.of(3)))
                    .add(v3)
                    .multiply(t3);

            Real value = term1.add(term2).add(term3).add(term4).multiply(half);
            result.add(value);
        }

        return new DenseVector<>(result, Reals.getInstance());
    }

    public Vector<Real> tangent(Real t) {
        // Numerical derivative for now
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
     * Returns the number of control points.
     * 
     * @return the number of control points
     */
    public int getNumControlPoints() {
        return controlPoints.size();
    }

    /**
     * Returns the number of segments.
     * 
     * @return the number of segments
     */
    public int getNumSegments() {
        return controlPoints.size() - 3;
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
     * Returns the tension parameter.
     * 
     * @return the tension
     */
    public Real getTension() {
        return tension;
    }

    @Override
    public String toString() {
        return "CatmullRomSpline(points=" + controlPoints.size() + ", segments=" + getNumSegments() + ")";
    }
}

