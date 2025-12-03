package org.jscience.mathematics.geometry;

import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.Vector;

/**
 * Represents a line segment in 3D space.
 * Defined by two points: start and end.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Segment3D {

    private final Vector3D start;
    private final Vector3D end;

    /**
     * Creates a new segment.
     * 
     * @param start the start point
     * @param end   the end point
     */
    public Segment3D(Vector3D start, Vector3D end) {
        this.start = start;
        this.end = end;
    }

    public Vector3D getStart() {
        return start;
    }

    public Vector3D getEnd() {
        return end;
    }

    public Real length() {
        return start.distance(end);
    }

    /**
     * Checks if a point lies on the segment.
     * 
     * @param p         the point
     * @param tolerance the tolerance
     * @return true if p is on the segment
     */
    public boolean contains(Vector3D p, double tolerance) {
        // Check if collinear
        // end.subtract(start) returns Vector<Real>, need to convert to Vector3D
        Vector<Real> dirVec = end.subtract(start);
        Vector3D dir = new Vector3D(
                (Real) dirVec.get(0),
                (Real) dirVec.get(1),
                (Real) dirVec.get(2));

        Line3D line = new Line3D(start, dir);
        if (!line.contains(p, tolerance)) {
            return false;
        }

        // Check if within bounds
        // Project p onto the line segment vector
        Vector3D v = dir;
        Vector<Real> spVec = p.subtract(start);
        Vector3D sp = new Vector3D(
                (Real) spVec.get(0),
                (Real) spVec.get(1),
                (Real) spVec.get(2));

        Real dot = sp.dot(v);
        Real lenSq = v.dot(v);

        // t = dot / lenSq
        // 0 <= t <= 1

        if (lenSq.equals(Real.ZERO)) {
            // Segment is a point
            // Use manual distance calculation if distance() is not available or problematic
            double dx = start.getX() - p.getX();
            double dy = start.getY() - p.getY();
            double dz = start.getZ() - p.getZ();
            return Math.sqrt(dx * dx + dy * dy + dz * dz) < tolerance;
        }

        Real t = dot.divide(lenSq);
        return t.doubleValue() >= -tolerance && t.doubleValue() <= 1.0 + tolerance;
    }

    /**
     * Clips this segment against a box defined by min and max points.
     * Uses a simplified Cohen-Sutherland or Liang-Barsky approach logic.
     * 
     * @param min box min point
     * @param max box max point
     * @return the clipped segment, or null if outside
     */
    public Segment3D clip(Vector3D min, Vector3D max) {
        // Liang-Barsky algorithm for 3D clipping
        // P(t) = P0 + t(P1 - P0)
        // min <= P(t) <= max

        double t0 = 0.0;
        double t1 = 1.0;

        double[] p = new double[] {
                -(end.getX() - start.getX()),
                (end.getX() - start.getX()),
                -(end.getY() - start.getY()),
                (end.getY() - start.getY()),
                -(end.getZ() - start.getZ()),
                (end.getZ() - start.getZ())
        };

        double[] q = new double[] {
                start.getX() - min.getX(),
                max.getX() - start.getX(),
                start.getY() - min.getY(),
                max.getY() - start.getY(),
                start.getZ() - min.getZ(),
                max.getZ() - start.getZ()
        };

        for (int i = 0; i < 6; i++) {
            if (p[i] == 0) {
                if (q[i] < 0)
                    return null; // Parallel and outside
            } else {
                double t = q[i] / p[i];
                if (p[i] < 0) {
                    if (t > t1)
                        return null;
                    if (t > t0)
                        t0 = t;
                } else {
                    if (t < t0)
                        return null;
                    if (t < t1)
                        t1 = t;
                }
            }
        }

        if (t0 > t1)
            return null;

        Vector3D newStart = new Vector3D(
                Real.of(start.getX() + p[1] * t0),
                Real.of(start.getY() + p[3] * t0),
                Real.of(start.getZ() + p[5] * t0));

        Vector3D newEnd = new Vector3D(
                Real.of(start.getX() + p[1] * t1),
                Real.of(start.getY() + p[3] * t1),
                Real.of(start.getZ() + p[5] * t1));

        return new Segment3D(newStart, newEnd);
    }
}

