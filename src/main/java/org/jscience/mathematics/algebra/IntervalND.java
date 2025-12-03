package org.jscience.mathematics.algebra;

import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.topology.TopologicalSpace;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an N-dimensional interval (hyperrectangle).
 * <p>
 * An N-dimensional interval is the Cartesian product of N 1-dimensional
 * intervals:
 * [a₁,b₁] × [a₂,b₂] × ... × [aₙ,bₙ]
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class IntervalND implements Set<List<Real>>, TopologicalSpace<List<Real>> {

    private final List<Interval> intervals;

    /**
     * Creates an N-dimensional interval from a list of 1D intervals.
     * 
     * @param intervals the list of intervals for each dimension
     */
    public IntervalND(List<Interval> intervals) {
        if (intervals == null || intervals.isEmpty()) {
            throw new IllegalArgumentException("Intervals list cannot be null or empty");
        }
        this.intervals = new ArrayList<>(intervals);
    }

    /**
     * Creates an N-dimensional interval from min and max points.
     * 
     * @param min the minimum point (lower corner)
     * @param max the maximum point (upper corner)
     */
    public IntervalND(List<Real> min, List<Real> max) {
        if (min == null || max == null) {
            throw new IllegalArgumentException("Min and max cannot be null");
        }
        if (min.size() != max.size()) {
            throw new IllegalArgumentException("Min and max must have same dimension");
        }

        this.intervals = new ArrayList<>();
        for (int i = 0; i < min.size(); i++) {
            intervals.add(new Interval(min.get(i), max.get(i)));
        }
    }

    /**
     * Gets the number of dimensions.
     * 
     * @return the dimension
     */
    public int dimension() {
        return intervals.size();
    }

    /**
     * Gets the interval for a specific dimension.
     * 
     * @param dim the dimension index (0-based)
     * @return the interval for that dimension
     */
    public Interval getInterval(int dim) {
        if (dim < 0 || dim >= intervals.size()) {
            throw new IndexOutOfBoundsException("Dimension " + dim + " out of bounds");
        }
        return intervals.get(dim);
    }

    /**
     * Gets the minimum point (lower corner).
     * 
     * @return list of minimum values for each dimension
     */
    public List<Real> getMin() {
        List<Real> min = new ArrayList<>();
        for (Interval interval : intervals) {
            min.add(interval.getMin());
        }
        return min;
    }

    /**
     * Gets the maximum point (upper corner).
     * 
     * @return list of maximum values for each dimension
     */
    public List<Real> getMax() {
        List<Real> max = new ArrayList<>();
        for (Interval interval : intervals) {
            max.add(interval.getMax());
        }
        return max;
    }

    // Set interface implementation
    @Override
    public boolean contains(List<Real> point) {
        if (point == null || point.size() != intervals.size()) {
            return false;
        }

        for (int i = 0; i < intervals.size(); i++) {
            if (!intervals.get(i).contains(point.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isEmpty() {
        for (Interval interval : intervals) {
            if (interval.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String description() {
        return toString();
    }

    // Set operations
    @Override
    public Set<List<Real>> union(Set<List<Real>> other) {
        if (!(other instanceof IntervalND)) {
            throw new IllegalArgumentException("Can only union with another IntervalND");
        }
        IntervalND otherND = (IntervalND) other;
        if (otherND.dimension() != this.dimension()) {
            throw new IllegalArgumentException("Dimensions must match");
        }

        List<Interval> unionIntervals = new ArrayList<>();
        for (int i = 0; i < intervals.size(); i++) {
            unionIntervals.add((Interval) intervals.get(i).union(otherND.intervals.get(i)));
        }
        return new IntervalND(unionIntervals);
    }

    @Override
    public Set<List<Real>> intersection(Set<List<Real>> other) {
        if (!(other instanceof IntervalND)) {
            throw new IllegalArgumentException("Can only intersect with another IntervalND");
        }
        IntervalND otherND = (IntervalND) other;
        if (otherND.dimension() != this.dimension()) {
            throw new IllegalArgumentException("Dimensions must match");
        }

        List<Interval> intersectionIntervals = new ArrayList<>();
        for (int i = 0; i < intervals.size(); i++) {
            Set<Real> result = intervals.get(i).intersection(otherND.intervals.get(i));
            if (result == null) {
                return null; // No intersection
            }
            intersectionIntervals.add((Interval) result);
        }
        return new IntervalND(intersectionIntervals);
    }

    @Override
    public Set<List<Real>> difference(Set<List<Real>> other) {
        // N-dimensional difference is complex and may not result in a single
        // hyperrectangle
        throw new UnsupportedOperationException(
                "Difference not supported for IntervalND (result may not be a hyperrectangle)");
    }

    @Override
    public boolean isSubsetOf(Set<List<Real>> other) {
        if (!(other instanceof IntervalND)) {
            return false;
        }
        IntervalND otherND = (IntervalND) other;
        if (otherND.dimension() != this.dimension()) {
            return false;
        }

        for (int i = 0; i < intervals.size(); i++) {
            if (!intervals.get(i).isSubsetOf(otherND.intervals.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean overlaps(Set<List<Real>> other) {
        return intersection(other) != null;
    }

    // TopologicalSpace implementation
    @Override
    public boolean isOpen() {
        return false; // Closed hyperrectangle by default
    }

    @Override
    public boolean isClosed() {
        return true; // Closed hyperrectangle by default
    }

    /**
     * Computes the volume (N-dimensional measure) of this interval.
     * 
     * @return the volume
     */
    public Real volume() {
        Real vol = Real.ONE;
        for (Interval interval : intervals) {
            vol = vol.multiply(interval.length());
        }
        return vol;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < intervals.size(); i++) {
            if (i > 0)
                sb.append(" × ");
            sb.append(intervals.get(i).toString());
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof IntervalND))
            return false;
        IntervalND other = (IntervalND) obj;
        return intervals.equals(other.intervals);
    }

    @Override
    public int hashCode() {
        return intervals.hashCode();
    }
}

