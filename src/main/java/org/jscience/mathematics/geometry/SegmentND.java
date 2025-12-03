package org.jscience.mathematics.geometry;

import org.jscience.mathematics.number.Real;

/**
 * Represents an N-dimensional line segment.
 * <p>
 * A segment is a finite portion of a line, defined by two endpoints.
 * Unlike a line which extends infinitely, a segment has a definite length.
 * </p>
 * <p>
 * Parametric form: S(t) = start + t*(end - start), where t ∈ [0,1]
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class SegmentND implements GeometricObject<PointND> {

    private final PointND start;
    private final PointND end;

    /**
     * Creates a segment from two endpoints.
     * 
     * @param start the start point
     * @param end   the end point
     */
    public SegmentND(PointND start, PointND end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Endpoints cannot be null");
        }
        if (start.ambientDimension() != end.ambientDimension()) {
            throw new IllegalArgumentException("Endpoints must have same dimension");
        }
        this.start = start;
        this.end = end;
    }

    @Override
    public int dimension() {
        return 1; // A segment is 1-dimensional
    }

    @Override
    public int ambientDimension() {
        return start.ambientDimension();
    }

    public PointND getStart() {
        return start;
    }

    public PointND getEnd() {
        return end;
    }

    /**
     * Returns the midpoint of this segment.
     * 
     * @return the midpoint
     */
    public PointND midpoint() {
        return start.midpoint(end);
    }

    /**
     * Returns the length of this segment.
     * 
     * @return the length
     */
    public Real length() {
        return start.distanceTo(end);
    }

    /**
     * Evaluates the segment at parameter t.
     * <p>
     * Returns: start + t*(end - start)
     * - t=0 gives start
     * - t=1 gives end
     * - t=0.5 gives midpoint
     * </p>
     * 
     * @param t the parameter (typically in [0,1])
     * @return the point at parameter t
     */
    public PointND at(Real t) {
        return start.interpolate(end, t);
    }

    /**
     * Converts this segment to a line.
     * 
     * @return the line containing this segment
     */
    public LineND toLine() {
        return LineND.through(start, end);
    }

    @Override
    public boolean contains(PointND p) {
        if (p.ambientDimension() != this.ambientDimension()) {
            return false;
        }

        // Check if p is on the line
        LineND line = toLine();
        if (!line.contains(p)) {
            return false;
        }

        // Check if p is between start and end
        Real distStart = start.distanceTo(p);
        Real distEnd = end.distanceTo(p);
        Real segmentLength = length();

        // p is on segment if distStart + distEnd ≈ segmentLength
        Real sum = distStart.add(distEnd);
        return Math.abs(sum.subtract(segmentLength).doubleValue()) < 1e-10;
    }

    /**
     * Finds the closest point on this segment to a given point.
     * 
     * @param p the point
     * @return the closest point on the segment
     */
    public PointND closestPoint(PointND p) {
        if (p.ambientDimension() != this.ambientDimension()) {
            throw new IllegalArgumentException("Point must have same dimension as segment");
        }

        // Project p onto the line
        LineND line = toLine();
        PointND projected = line.closestPoint(p);

        // Check if projection is within segment bounds
        if (contains(projected)) {
            return projected;
        }

        // Otherwise, closest point is one of the endpoints
        Real distStart = p.distanceTo(start);
        Real distEnd = p.distanceTo(end);

        return distStart.compareTo(distEnd) <= 0 ? start : end;
    }

    /**
     * Computes the distance from a point to this segment.
     * 
     * @param p the point
     * @return the distance
     */
    public Real distanceTo(PointND p) {
        PointND closest = closestPoint(p);
        return p.distanceTo(closest);
    }

    /**
     * Checks if this segment intersects another segment.
     * 
     * @param other the other segment
     * @return true if segments intersect
     */
    public boolean intersects(SegmentND other) {
        if (other.ambientDimension() != this.ambientDimension()) {
            return false;
        }

        // For 2D/3D, use line intersection and check if point is on both segments
        LineND line1 = this.toLine();
        LineND line2 = other.toLine();

        PointND intersection = line1.intersection(line2);
        if (intersection == null) {
            return false;
        }

        return this.contains(intersection) && other.contains(intersection);
    }

    @Override
    public String description() {
        return toString();
    }

    @Override
    public String toString() {
        return "Segment(" + start + " → " + end + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof SegmentND))
            return false;
        SegmentND other = (SegmentND) obj;
        return (start.equals(other.start) && end.equals(other.end)) ||
                (start.equals(other.end) && end.equals(other.start));
    }

    @Override
    public int hashCode() {
        // Order-independent hash
        return start.hashCode() + end.hashCode();
    }
}

