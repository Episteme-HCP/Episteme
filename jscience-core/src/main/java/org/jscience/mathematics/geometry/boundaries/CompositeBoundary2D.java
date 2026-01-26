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

package org.jscience.mathematics.geometry.boundaries;

import org.jscience.mathematics.geometry.Point2D;
import org.jscience.mathematics.numbers.real.Real;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A composite 2D boundary supporting exclaves and enclaves.
 * <p>
 * This class represents complex territorial shapes:
 * <ul>
 *   <li><b>Exclaves</b> - Multiple disjoint regions that belong to the same territory
 *       (e.g., Alaska as an exclave of the contiguous USA)</li>
 *   <li><b>Enclaves</b> - Holes within a region that don't belong to it
 *       (e.g., Vatican City as an enclave within Rome)</li>
 * </ul>
 * <p>
 * A point is considered "inside" the composite boundary if it is inside at least
 * one inclusion region AND not inside any exclusion region.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CompositeBoundary2D implements Boundary2D {

    private static final long serialVersionUID = 1L;

    /** The main outer boundaries (exclaves - disjoint regions that are IN) */
    private final List<Boundary2D> inclusions;

    /** The holes/exclusions (enclaves - regions that are OUT) */
    private final List<Boundary2D> exclusions;

    /** Cached centroid */
    private transient Point2D cachedCentroid;

    /**
     * Creates an empty composite boundary.
     */
    public CompositeBoundary2D() {
        this.inclusions = new ArrayList<>();
        this.exclusions = new ArrayList<>();
    }

    /**
     * Creates a composite boundary with a single inclusion.
     *
     * @param mainBoundary the main boundary
     */
    public CompositeBoundary2D(Boundary2D mainBoundary) {
        this();
        if (mainBoundary != null) {
            inclusions.add(mainBoundary);
        }
    }

    /**
     * Creates a composite from inclusions and exclusions.
     *
     * @param inclusions the inclusion regions (exclaves)
     * @param exclusions the exclusion regions (enclaves/holes)
     */
    public CompositeBoundary2D(List<Boundary2D> inclusions, List<Boundary2D> exclusions) {
        this.inclusions = new ArrayList<>(inclusions != null ? inclusions : List.of());
        this.exclusions = new ArrayList<>(exclusions != null ? exclusions : List.of());
    }

    // ===== Inclusion/Exclusion Management =====

    /**
     * Adds an inclusion region (exclave).
     *
     * @param region the region to include
     */
    public void addInclusion(Boundary2D region) {
        if (region != null) {
            inclusions.add(region);
            cachedCentroid = null;
        }
    }

    /**
     * Adds an exclusion region (enclave/hole).
     *
     * @param region the region to exclude
     */
    public void addExclusion(Boundary2D region) {
        if (region != null) {
            exclusions.add(region);
            cachedCentroid = null;
        }
    }

    /**
     * Returns the inclusion regions (exclaves).
     * @return unmodifiable list of inclusions
     */
    public List<Boundary2D> getInclusions() {
        return Collections.unmodifiableList(inclusions);
    }

    /**
     * Returns the exclusion regions (enclaves/holes).
     * @return unmodifiable list of exclusions
     */
    public List<Boundary2D> getExclusions() {
        return Collections.unmodifiableList(exclusions);
    }

    /**
     * Returns the number of disjoint regions (exclaves).
     * @return exclave count
     */
    public int getExclaveCount() {
        return inclusions.size();
    }

    /**
     * Returns the number of holes (enclaves).
     * @return enclave count
     */
    public int getEnclaveCount() {
        return exclusions.size();
    }

    // ===== Containment with Enclave/Exclave Support =====

    /**
     * Checks if a point is inside the composite boundary.
     * <p>
     * A point is IN if:
     * <ol>
     *   <li>It is inside at least one inclusion (exclave)</li>
     *   <li>AND it is not inside any exclusion (enclave)</li>
     * </ol>
     *
     * @param point the point to test
     * @return true if inside
     */
    @Override
    public boolean contains(Point2D point) {
        if (point == null) return false;

        // Must be in at least one inclusion
        boolean inAnyInclusion = false;
        for (Boundary2D inclusion : inclusions) {
            if (inclusion.contains(point)) {
                inAnyInclusion = true;
                break;
            }
        }

        if (!inAnyInclusion) {
            return false;
        }

        // Must not be in any exclusion
        for (Boundary2D exclusion : exclusions) {
            if (exclusion.contains(point)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Determines the containment status of a point.
     *
     * @param point the point to check
     * @return the containment result
     */
    public ContainmentResult getContainmentStatus(Point2D point) {
        if (point == null) {
            return ContainmentResult.OUTSIDE;
        }

        // Check exclusions first
        for (int i = 0; i < exclusions.size(); i++) {
            if (exclusions.get(i).contains(point)) {
                return new ContainmentResult(ContainmentResult.Status.IN_ENCLAVE, i);
            }
        }

        // Check inclusions
        for (int i = 0; i < inclusions.size(); i++) {
            if (inclusions.get(i).contains(point)) {
                return new ContainmentResult(ContainmentResult.Status.INSIDE, i);
            }
        }

        return ContainmentResult.OUTSIDE;
    }

    /**
     * Result of a containment check with detailed status.
     */
    public static class ContainmentResult {
        public enum Status {
            /** Point is inside an inclusion (exclave) */
            INSIDE,
            /** Point is inside an enclave (hole) - therefore OUTSIDE the territory */
            IN_ENCLAVE,
            /** Point is outside all regions */
            OUTSIDE
        }

        public static final ContainmentResult OUTSIDE = new ContainmentResult(Status.OUTSIDE, -1);

        private final Status status;
        private final int regionIndex;

        public ContainmentResult(Status status, int regionIndex) {
            this.status = status;
            this.regionIndex = regionIndex;
        }

        public Status getStatus() { return status; }
        public int getRegionIndex() { return regionIndex; }
        public boolean isInside() { return status == Status.INSIDE; }
        public boolean isInEnclave() { return status == Status.IN_ENCLAVE; }
        public boolean isOutside() { return status == Status.OUTSIDE || status == Status.IN_ENCLAVE; }
    }

    // ===== Boundary2D Implementation =====

    @Override
    public List<Point2D> getVertices() {
        List<Point2D> allVertices = new ArrayList<>();
        for (Boundary2D b : inclusions) {
            allVertices.addAll(b.getVertices());
        }
        return allVertices;
    }

    @Override
    public int getVertexCount() {
        return inclusions.stream().mapToInt(Boundary2D::getVertexCount).sum();
    }

    @Override
    public Point2D getVertex(int index) {
        int offset = 0;
        for (Boundary2D b : inclusions) {
            if (index < offset + b.getVertexCount()) {
                return b.getVertex(index - offset);
            }
            offset += b.getVertexCount();
        }
        throw new IndexOutOfBoundsException("Vertex index: " + index);
    }

    @Override
    public Real getArea() {
        Real totalArea = Real.ZERO;
        for (Boundary2D inclusion : inclusions) {
            totalArea = totalArea.add(inclusion.getArea());
        }
        for (Boundary2D exclusion : exclusions) {
            totalArea = totalArea.subtract(exclusion.getArea());
        }
        return totalArea;
    }

    @Override
    public Real getPerimeter() {
        Real totalPerimeter = Real.ZERO;
        for (Boundary2D b : inclusions) {
            totalPerimeter = totalPerimeter.add(b.getPerimeter());
        }
        for (Boundary2D b : exclusions) {
            totalPerimeter = totalPerimeter.add(b.getPerimeter());
        }
        return totalPerimeter;
    }

    @Override
    public boolean isConvex() {
        // A composite with holes or multiple regions is never convex
        return inclusions.size() == 1 && exclusions.isEmpty() && 
               inclusions.get(0).isConvex();
    }

    @Override
    public boolean isEmpty() {
        return inclusions.isEmpty();
    }

    @Override
    public Point2D getCentroid() {
        if (cachedCentroid != null) return cachedCentroid;
        if (inclusions.isEmpty()) return null;

        // Weighted average of inclusion centroids by area
        Real totalArea = Real.ZERO;
        Real sumX = Real.ZERO;
        Real sumY = Real.ZERO;

        for (Boundary2D b : inclusions) {
            Real area = b.getArea();
            Point2D c = b.getCentroid();
            if (c != null) {
                sumX = sumX.add(c.getX().multiply(area));
                sumY = sumY.add(c.getY().multiply(area));
                totalArea = totalArea.add(area);
            }
        }

        if (totalArea.isZero()) {
            cachedCentroid = inclusions.get(0).getCentroid();
        } else {
            cachedCentroid = Point2D.of(sumX.divide(totalArea), sumY.divide(totalArea));
        }
        return cachedCentroid;
    }

    @Override
    public BoundingBox<Point2D> getBoundingBox() {
        if (inclusions.isEmpty()) return null;
        
        BoundingBox<Point2D> box = inclusions.get(0).getBoundingBox();
        for (int i = 1; i < inclusions.size(); i++) {
            box = box.merge(inclusions.get(i).getBoundingBox());
        }
        return box;
    }

    @Override
    public boolean intersects(Boundary<Point2D> other) {
        for (Boundary2D inclusion : inclusions) {
            if (inclusion.intersects(other)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boundary<Point2D> union(Boundary<Point2D> other) {
        CompositeBoundary2D result = new CompositeBoundary2D();
        result.inclusions.addAll(this.inclusions);
        if (other instanceof CompositeBoundary2D) {
            result.inclusions.addAll(((CompositeBoundary2D) other).inclusions);
        } else if (other instanceof Boundary2D) {
            result.inclusions.add((Boundary2D) other);
        }
        return result;
    }

    @Override
    public Boundary<Point2D> intersection(Boundary<Point2D> other) {
        // Simplified - returns new composite with intersecting regions
        CompositeBoundary2D result = new CompositeBoundary2D();
        for (Boundary2D inclusion : inclusions) {
            Boundary<Point2D> intersect = inclusion.intersection(other);
            if (intersect != null && !intersect.isEmpty() && intersect instanceof Boundary2D) {
                result.addInclusion((Boundary2D) intersect);
            }
        }
        return result;
    }

    @Override
    public Boundary<Point2D> convexHull() {
        // Compute convex hull of all vertices
        List<Point2D> allPoints = new ArrayList<>();
        for (Boundary2D b : inclusions) {
            allPoints.addAll(b.getVertices());
        }
        return ConvexPolygon2D.fromPoints(allPoints);
    }

    @Override
    public Boundary<Point2D> translate(Point2D offset) {
        List<Boundary2D> newInclusions = new ArrayList<>();
        List<Boundary2D> newExclusions = new ArrayList<>();
        for (Boundary2D b : inclusions) {
            newInclusions.add((Boundary2D) b.translate(offset));
        }
        for (Boundary2D b : exclusions) {
            newExclusions.add((Boundary2D) b.translate(offset));
        }
        return new CompositeBoundary2D(newInclusions, newExclusions);
    }

    @Override
    public Boundary<Point2D> scale(Real factor) {
        List<Boundary2D> newInclusions = new ArrayList<>();
        List<Boundary2D> newExclusions = new ArrayList<>();
        for (Boundary2D b : inclusions) {
            newInclusions.add((Boundary2D) b.scale(factor));
        }
        for (Boundary2D b : exclusions) {
            newExclusions.add((Boundary2D) b.scale(factor));
        }
        return new CompositeBoundary2D(newInclusions, newExclusions);
    }

    @Override
    public Boundary2D rotate(Real angleRadians) {
        List<Boundary2D> newInclusions = new ArrayList<>();
        List<Boundary2D> newExclusions = new ArrayList<>();
        for (Boundary2D b : inclusions) {
            newInclusions.add(b.rotate(angleRadians));
        }
        for (Boundary2D b : exclusions) {
            newExclusions.add(b.rotate(angleRadians));
        }
        return new CompositeBoundary2D(newInclusions, newExclusions);
    }

    @Override
    public Boundary2D rotateAround(Real angleRadians, Point2D pivot) {
        List<Boundary2D> newInclusions = new ArrayList<>();
        List<Boundary2D> newExclusions = new ArrayList<>();
        for (Boundary2D b : inclusions) {
            newInclusions.add(b.rotateAround(angleRadians, pivot));
        }
        for (Boundary2D b : exclusions) {
            newExclusions.add(b.rotateAround(angleRadians, pivot));
        }
        return new CompositeBoundary2D(newInclusions, newExclusions);
    }

    @Override
    public Boundary3D extrude(Real height) {
        // Would create a CompositeBoundary3D
        throw new UnsupportedOperationException("Extrusion of composite boundaries not yet implemented");
    }

    @Override
    public String toString() {
        return String.format("CompositeBoundary2D[%d exclaves, %d enclaves, area=%s]",
            inclusions.size(), exclusions.size(), getArea());
    }
}
