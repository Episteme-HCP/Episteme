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

import org.jscience.core.mathematics.geometry.Point3D;
import org.jscience.core.mathematics.geometry.Vector3D;
import org.jscience.core.mathematics.numbers.real.Real;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A composite 3D boundary supporting exclaves and enclaves.
 * <p>
 * Similar to {@link CompositeBoundary2D} but for 3D volumes.
 * Supports multiple disjoint volumes (exclaves) and
 * excluded volumes (enclaves/cavities).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CompositeBoundary3D implements Boundary3D {

    private static final long serialVersionUID = 1L;

    private final List<Boundary3D> inclusions;
    private final List<Boundary3D> exclusions;
    private transient Point3D cachedCentroid;

    public CompositeBoundary3D() {
        this.inclusions = new ArrayList<>();
        this.exclusions = new ArrayList<>();
    }

    public CompositeBoundary3D(Boundary3D mainBoundary) {
        this();
        if (mainBoundary != null) {
            inclusions.add(mainBoundary);
        }
    }

    public CompositeBoundary3D(List<Boundary3D> inclusions, List<Boundary3D> exclusions) {
        this.inclusions = new ArrayList<>(inclusions != null ? inclusions : List.of());
        this.exclusions = new ArrayList<>(exclusions != null ? exclusions : List.of());
    }

    // ===== Inclusion/Exclusion Management =====

    public void addInclusion(Boundary3D region) {
        if (region != null) {
            inclusions.add(region);
            cachedCentroid = null;
        }
    }

    public void addExclusion(Boundary3D region) {
        if (region != null) {
            exclusions.add(region);
            cachedCentroid = null;
        }
    }

    public List<Boundary3D> getInclusions() {
        return Collections.unmodifiableList(inclusions);
    }

    public List<Boundary3D> getExclusions() {
        return Collections.unmodifiableList(exclusions);
    }

    public int getExclaveCount() {
        return inclusions.size();
    }

    public int getEnclaveCount() {
        return exclusions.size();
    }

    // ===== Containment =====

    @Override
    public boolean contains(Point3D point) {
        if (point == null) return false;

        // Must be in at least one inclusion
        boolean inAnyInclusion = false;
        for (Boundary3D inclusion : inclusions) {
            if (inclusion.contains(point)) {
                inAnyInclusion = true;
                break;
            }
        }

        if (!inAnyInclusion) {
            return false;
        }

        // Must not be in any exclusion
        for (Boundary3D exclusion : exclusions) {
            if (exclusion.contains(point)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Containment result with detailed status.
     */
    public static class ContainmentResult {
        public enum Status { INSIDE, IN_ENCLAVE, OUTSIDE }
        
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
        public boolean isOutside() { return status == Status.OUTSIDE || status == Status.IN_ENCLAVE; }
    }

    public ContainmentResult getContainmentStatus(Point3D point) {
        if (point == null) return ContainmentResult.OUTSIDE;

        for (int i = 0; i < exclusions.size(); i++) {
            if (exclusions.get(i).contains(point)) {
                return new ContainmentResult(ContainmentResult.Status.IN_ENCLAVE, i);
            }
        }

        for (int i = 0; i < inclusions.size(); i++) {
            if (inclusions.get(i).contains(point)) {
                return new ContainmentResult(ContainmentResult.Status.INSIDE, i);
            }
        }

        return ContainmentResult.OUTSIDE;
    }

    // ===== Boundary3D Implementation =====

    @Override
    public List<Point3D> getVertices() {
        List<Point3D> all = new ArrayList<>();
        for (Boundary3D b : inclusions) {
            all.addAll(b.getVertices());
        }
        return all;
    }

    @Override
    public List<Face> getFaces() {
        List<Face> all = new ArrayList<>();
        for (Boundary3D b : inclusions) {
            all.addAll(b.getFaces());
        }
        return all;
    }

    @Override
    public Real getVolume() {
        Real total = Real.ZERO;
        for (Boundary3D b : inclusions) {
            total = total.add(b.getVolume());
        }
        for (Boundary3D b : exclusions) {
            total = total.subtract(b.getVolume());
        }
        return total;
    }

    @Override
    public Real getSurfaceArea() {
        Real total = Real.ZERO;
        for (Boundary3D b : inclusions) {
            total = total.add(b.getSurfaceArea());
        }
        for (Boundary3D b : exclusions) {
            total = total.add(b.getSurfaceArea());
        }
        return total;
    }

    @Override
    public boolean isConvex() {
        return inclusions.size() == 1 && exclusions.isEmpty() && 
               inclusions.get(0).isConvex();
    }

    @Override
    public boolean isEmpty() {
        return inclusions.isEmpty();
    }

    @Override
    public Point3D getCentroid() {
        if (cachedCentroid != null) return cachedCentroid;
        if (inclusions.isEmpty()) return null;

        Real totalVolume = Real.ZERO;
        Real sumX = Real.ZERO, sumY = Real.ZERO, sumZ = Real.ZERO;

        for (Boundary3D b : inclusions) {
            Real vol = b.getVolume();
            Point3D c = b.getCentroid();
            if (c != null) {
                sumX = sumX.add(c.getX().multiply(vol));
                sumY = sumY.add(c.getY().multiply(vol));
                sumZ = sumZ.add(c.getZ().multiply(vol));
                totalVolume = totalVolume.add(vol);
            }
        }

        if (totalVolume.isZero()) {
            cachedCentroid = inclusions.get(0).getCentroid();
        } else {
            cachedCentroid = Point3D.of(
                sumX.divide(totalVolume),
                sumY.divide(totalVolume),
                sumZ.divide(totalVolume)
            );
        }
        return cachedCentroid;
    }

    @Override
    public BoundingBox<Point3D> getBoundingBox() {
        if (inclusions.isEmpty()) return null;
        
        BoundingBox<Point3D> box = inclusions.get(0).getBoundingBox();
        for (int i = 1; i < inclusions.size(); i++) {
            box = box.merge(inclusions.get(i).getBoundingBox());
        }
        return box;
    }

    @Override
    public boolean intersects(Boundary<Point3D> other) {
        for (Boundary3D inclusion : inclusions) {
            if (inclusion.intersects(other)) return true;
        }
        return false;
    }

    @Override
    public Boundary<Point3D> union(Boundary<Point3D> other) {
        CompositeBoundary3D result = new CompositeBoundary3D();
        result.inclusions.addAll(this.inclusions);
        if (other instanceof CompositeBoundary3D) {
            result.inclusions.addAll(((CompositeBoundary3D) other).inclusions);
        } else if (other instanceof Boundary3D) {
            result.inclusions.add((Boundary3D) other);
        }
        return result;
    }

    @Override
    public Boundary<Point3D> intersection(Boundary<Point3D> other) {
        throw new UnsupportedOperationException("3D intersection not yet implemented");
    }

    @Override
    public Boundary<Point3D> convexHull() {
        // Compute convex hull of all vertices
        throw new UnsupportedOperationException("3D convex hull not yet implemented");
    }

    @Override
    public Boundary<Point3D> translate(Point3D offset) {
        List<Boundary3D> newInc = new ArrayList<>();
        List<Boundary3D> newExc = new ArrayList<>();
        for (Boundary3D b : inclusions) {
            newInc.add((Boundary3D) b.translate(offset));
        }
        for (Boundary3D b : exclusions) {
            newExc.add((Boundary3D) b.translate(offset));
        }
        return new CompositeBoundary3D(newInc, newExc);
    }

    @Override
    public Boundary<Point3D> scale(Real factor) {
        List<Boundary3D> newInc = new ArrayList<>();
        List<Boundary3D> newExc = new ArrayList<>();
        for (Boundary3D b : inclusions) {
            newInc.add((Boundary3D) b.scale(factor));
        }
        for (Boundary3D b : exclusions) {
            newExc.add((Boundary3D) b.scale(factor));
        }
        return new CompositeBoundary3D(newInc, newExc);
    }

    @Override
    public Boundary3D rotateX(Real angleRadians) {
        throw new UnsupportedOperationException("Rotation not yet implemented");
    }

    @Override
    public Boundary3D rotateY(Real angleRadians) {
        throw new UnsupportedOperationException("Rotation not yet implemented");
    }

    @Override
    public Boundary3D rotateZ(Real angleRadians) {
        throw new UnsupportedOperationException("Rotation not yet implemented");
    }

    @Override
    public Boundary3D rotateAround(Vector3D axis, Real angleRadians) {
        throw new UnsupportedOperationException("Rotation not yet implemented");
    }

    @Override
    public Boundary2D projectXY() {
        CompositeBoundary2D result = new CompositeBoundary2D();
        for (Boundary3D b : inclusions) {
            result.addInclusion(b.projectXY());
        }
        for (Boundary3D b : exclusions) {
            result.addExclusion(b.projectXY());
        }
        return result;
    }

    @Override
    public Boundary2D projectXZ() {
        CompositeBoundary2D result = new CompositeBoundary2D();
        for (Boundary3D b : inclusions) {
            result.addInclusion(b.projectXZ());
        }
        return result;
    }

    @Override
    public Boundary2D projectYZ() {
        CompositeBoundary2D result = new CompositeBoundary2D();
        for (Boundary3D b : inclusions) {
            result.addInclusion(b.projectYZ());
        }
        return result;
    }

    @Override
    public Boundary2D slice(Vector3D planeNormal, Point3D pointOnPlane) {
        throw new UnsupportedOperationException("Slicing not yet implemented");
    }

    @Override
    public String toString() {
        return String.format("CompositeBoundary3D[%d exclaves, %d enclaves, volume=%s]",
            inclusions.size(), exclusions.size(), getVolume());
    }
}

