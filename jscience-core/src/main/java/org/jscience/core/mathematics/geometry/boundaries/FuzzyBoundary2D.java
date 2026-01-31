/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.core.mathematics.geometry.boundaries;

import org.jscience.core.mathematics.geometry.Point2D;
import org.jscience.core.mathematics.numbers.real.Real;

import java.util.function.Function;

/**
 * A 2D boundary with fuzzy (graded) membership.
 * <p>
 * Unlike crisp boundaries where a point is either IN (1.0) or OUT (0.0),
 * fuzzy boundaries return a membership degree in the range [0, 1].
 * <p>
 * This is useful for:
 * <ul>
 *   <li>Uncertainty in boundary definition</li>
 *   <li>Transition zones (e.g., ecological gradients)</li>
 *   <li>Influence zones (diminishing influence with distance)</li>
 * </ul>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FuzzyBoundary2D implements Boundary2D {

    private static final long serialVersionUID = 1L;

    /** The core boundary (full membership) */
    private final Boundary2D core;

    /** The support boundary (where membership > 0) */
    private final Boundary2D support;

    /** Membership function based on distance to core */
    private final Function<Real, Real> membershipFunction;

    /**
     * Creates a fuzzy boundary from core and support boundaries.
     * Points in core have membership 1.0, points outside support have 0.0,
     * and points between have graded membership.
     *
     * @param core    the core boundary (full membership)
     * @param support the support boundary (membership > 0)
     */
    public FuzzyBoundary2D(Boundary2D core, Boundary2D support) {
        this(core, support, FuzzyBoundary2D::linearMembership);
    }

    /**
     * Creates a fuzzy boundary with custom membership function.
     *
     * @param core               the core boundary
     * @param support            the support boundary
     * @param membershipFunction maps distance ratio to membership [0,1]
     */
    public FuzzyBoundary2D(Boundary2D core, Boundary2D support,
                           Function<Real, Real> membershipFunction) {
        this.core = core;
        this.support = support;
        this.membershipFunction = membershipFunction;
    }

    /**
     * Creates a fuzzy boundary with a buffer zone around a crisp boundary.
     *
     * @param crisp       the crisp boundary
     * @param bufferWidth the width of the transition zone
     * @return fuzzy boundary
     */
    public static FuzzyBoundary2D withBuffer(Boundary2D crisp, Real bufferWidth) {
        // Create support by scaling the boundary
        Boundary2D support = (Boundary2D) crisp.scale(Real.ONE.add(bufferWidth));
        return new FuzzyBoundary2D(crisp, support);
    }

    /**
     * Returns the membership degree for a point.
     *
     * @param point the point to test
     * @return membership in [0, 1]: 1.0 = definitely inside, 0.0 = definitely outside
     */
    public Real getMembership(Point2D point) {
        if (point == null) return Real.ZERO;

        // Fully inside core
        if (core.contains(point)) {
            return Real.ONE;
        }

        // Outside support
        if (!support.contains(point)) {
            return Real.ZERO;
        }

        // In transition zone - compute based on relative distance
        Real distToCore = estimateDistanceToBoundary(point, core);
        Real distToSupport = estimateDistanceToBoundary(point, support);
        
        Real total = distToCore.add(distToSupport);
        if (total.isZero()) {
            return Real.of(0.5);
        }
        
        Real ratio = distToSupport.divide(total);
        return membershipFunction.apply(ratio);
    }

    private Real estimateDistanceToBoundary(Point2D point, Boundary2D boundary) {
        // Simplified: distance to centroid as proxy
        Point2D centroid = boundary.getCentroid();
        if (centroid == null) return Real.ZERO;
        return point.distanceTo(centroid);
    }

    /**
     * Linear membership function: f(r) = r
     */
    public static Real linearMembership(Real ratio) {
        return ratio;
    }

    /**
     * Sigmoid membership function for smooth transition.
     */
    public static Real sigmoidMembership(Real ratio) {
        double r = ratio.doubleValue();
        double sigmoid = 1.0 / (1.0 + Math.exp(-12.0 * (r - 0.5)));
        return Real.of(sigmoid);
    }

    /**
     * Gaussian membership function.
     */
    public static Real gaussianMembership(Real ratio) {
        double r = ratio.doubleValue();
        double gaussian = Math.exp(-Math.pow((1 - r) * 3, 2));
        return Real.of(gaussian);
    }

    // ===== Fuzzy Set Operations =====

    /**
     * Fuzzy AND (intersection) - minimum membership.
     */
    public Real fuzzyAnd(Point2D point, FuzzyBoundary2D other) {
        Real m1 = this.getMembership(point);
        Real m2 = other.getMembership(point);
        return m1.compareTo(m2) < 0 ? m1 : m2;
    }

    /**
     * Fuzzy OR (union) - maximum membership.
     */
    public Real fuzzyOr(Point2D point, FuzzyBoundary2D other) {
        Real m1 = this.getMembership(point);
        Real m2 = other.getMembership(point);
        return m1.compareTo(m2) > 0 ? m1 : m2;
    }

    /**
     * Fuzzy NOT - complement membership.
     */
    public Real fuzzyNot(Point2D point) {
        return Real.ONE.subtract(getMembership(point));
    }

    // ===== Crisp Boundary via Alpha-Cut =====

    /**
     * Returns a crisp boundary for a given alpha level.
     * Points with membership >= alpha are included.
     *
     * @param alpha the threshold (0 to 1)
     * @return the alpha-cut boundary
     */
    public Boundary2D alphaCut(Real alpha) {
        if (alpha.compareTo(Real.ONE) >= 0) {
            return core;
        }
        if (alpha.compareTo(Real.ZERO) <= 0) {
            return support;
        }
        // Interpolate between core and support
        // Simplified: scale core toward support
        Real factor = Real.ONE.subtract(alpha);
        return (Boundary2D) core.scale(Real.ONE.add(factor)); 
    }

    // ===== Boundary2D Delegation =====

    @Override
    public boolean contains(Point2D point) {
        // Crisp containment: membership > 0.5
        return getMembership(point).compareTo(Real.of(0.5)) >= 0;
    }

    @Override
    public java.util.List<Point2D> getVertices() {
        return support.getVertices();
    }

    @Override
    public int getVertexCount() {
        return support.getVertexCount();
    }

    @Override
    public Point2D getVertex(int index) {
        return support.getVertex(index);
    }

    @Override
    public Real getArea() {
        return support.getArea();
    }

    @Override
    public Real getPerimeter() {
        return support.getPerimeter();
    }

    @Override
    public boolean isConvex() {
        return core.isConvex() && support.isConvex();
    }

    @Override
    public boolean isEmpty() {
        return core.isEmpty() && support.isEmpty();
    }

    @Override
    public Point2D getCentroid() {
        return core.getCentroid();
    }

    @Override
    public BoundingBox<Point2D> getBoundingBox() {
        return support.getBoundingBox();
    }

    @Override
    public boolean intersects(Boundary<Point2D> other) {
        return support.intersects(other);
    }

    @Override
    public Boundary<Point2D> union(Boundary<Point2D> other) {
        return support.union(other);
    }

    @Override
    public Boundary<Point2D> intersection(Boundary<Point2D> other) {
        return core.intersection(other);
    }

    @Override
    public Boundary<Point2D> convexHull() {
        return support.convexHull();
    }

    @Override
    public Boundary<Point2D> translate(Point2D offset) {
        return new FuzzyBoundary2D(
            (Boundary2D) core.translate(offset),
            (Boundary2D) support.translate(offset),
            membershipFunction
        );
    }

    @Override
    public Boundary<Point2D> scale(Real factor) {
        return new FuzzyBoundary2D(
            (Boundary2D) core.scale(factor),
            (Boundary2D) support.scale(factor),
            membershipFunction
        );
    }

    @Override
    public Boundary2D rotate(Real angleRadians) {
        return new FuzzyBoundary2D(
            core.rotate(angleRadians),
            support.rotate(angleRadians),
            membershipFunction
        );
    }

    @Override
    public Boundary2D rotateAround(Real angleRadians, Point2D pivot) {
        return new FuzzyBoundary2D(
            core.rotateAround(angleRadians, pivot),
            support.rotateAround(angleRadians, pivot),
            membershipFunction
        );
    }

    @Override
    public Boundary3D extrude(Real height) {
        throw new UnsupportedOperationException("Cannot extrude fuzzy boundary");
    }

    /**
     * Returns the core boundary.
     * @return the core
     */
    public Boundary2D getCore() {
        return core;
    }

    /**
     * Returns the support boundary.
     * @return the support
     */
    public Boundary2D getSupport() {
        return support;
    }

    @Override
    public String toString() {
        return String.format("FuzzyBoundary2D[core=%s, support=%s]", core, support);
    }
}

