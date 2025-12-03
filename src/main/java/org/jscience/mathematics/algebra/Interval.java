package org.jscience.mathematics.algebra;

import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.topology.TopologicalSpace;

/**
 * Represents a 1D interval over real numbers.
 * <p>
 * Supports open, closed, and semi-open intervals:
 * - [a,b] closed
 * - (a,b) open
 * - [a,b) or (a,b] semi-open
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Interval implements Set<Real>, TopologicalSpace<Real> {

    private final Real min;
    private final Real max;
    private final boolean closedLeft;
    private final boolean closedRight;

    /**
     * Creates a new interval with specified endpoint types.
     * 
     * @param min         the minimum value
     * @param max         the maximum value
     * @param closedLeft  true for [, false for (
     * @param closedRight true for ], false for )
     */
    public Interval(Real min, Real max, boolean closedLeft, boolean closedRight) {
        if (min.compareTo(max) > 0) {
            this.min = max;
            this.max = min;
            this.closedLeft = closedRight;
            this.closedRight = closedLeft;
        } else {
            this.min = min;
            this.max = max;
            this.closedLeft = closedLeft;
            this.closedRight = closedRight;
        }
    }

    /**
     * Creates a closed interval [min, max].
     * 
     * @param min the minimum value
     * @param max the maximum value
     */
    public Interval(Real min, Real max) {
        this(min, max, true, true);
    }

    /**
     * Creates a closed interval from doubles.
     * 
     * @param min the minimum value
     * @param max the maximum value
     */
    public Interval(double min, double max) {
        this(Real.of(min), Real.of(max), true, true);
    }

    public Real getMin() {
        return min;
    }

    public Real getMax() {
        return max;
    }

    public boolean isClosedLeft() {
        return closedLeft;
    }

    public boolean isClosedRight() {
        return closedRight;
    }

    @Override
    public boolean contains(Real value) {
        int cmpMin = value.compareTo(min);
        int cmpMax = max.compareTo(value);

        boolean aboveMin = closedLeft ? (cmpMin >= 0) : (cmpMin > 0);
        boolean belowMax = closedRight ? (cmpMax >= 0) : (cmpMax > 0);

        return aboveMin && belowMax;
    }

    public boolean contains(Interval other) {
        boolean containsMin = contains(other.min) ||
                (min.equals(other.min) && (closedLeft || !other.closedLeft));

        boolean containsMax = contains(other.max) ||
                (max.equals(other.max) && (closedRight || !other.closedRight));

        return containsMin && containsMax;
    }

    @Override
    public Set<Real> intersection(Set<Real> other) {
        if (!(other instanceof Interval)) {
            throw new IllegalArgumentException("Can only intersect with another Interval");
        }
        Interval otherInterval = (Interval) other;

        Real newMin;
        boolean newClosedLeft;
        int cmpMin = this.min.compareTo(otherInterval.min);
        if (cmpMin > 0) {
            newMin = this.min;
            newClosedLeft = this.closedLeft;
        } else if (cmpMin < 0) {
            newMin = otherInterval.min;
            newClosedLeft = otherInterval.closedLeft;
        } else {
            newMin = this.min;
            newClosedLeft = this.closedLeft && otherInterval.closedLeft;
        }

        Real newMax;
        boolean newClosedRight;
        int cmpMax = this.max.compareTo(otherInterval.max);
        if (cmpMax < 0) {
            newMax = this.max;
            newClosedRight = this.closedRight;
        } else if (cmpMax > 0) {
            newMax = otherInterval.max;
            newClosedRight = otherInterval.closedRight;
        } else {
            newMax = this.max;
            newClosedRight = this.closedRight && otherInterval.closedRight;
        }

        int cmpMinMax = newMin.compareTo(newMax);
        if (cmpMinMax > 0) {
            return null;
        }
        if (cmpMinMax == 0 && !(newClosedLeft && newClosedRight)) {
            return null;
        }

        return new Interval(newMin, newMax, newClosedLeft, newClosedRight);
    }

    @Override
    public Set<Real> union(Set<Real> other) {
        if (!(other instanceof Interval)) {
            throw new IllegalArgumentException("Can only union with another Interval");
        }
        Interval otherInterval = (Interval) other;

        Real newMin;
        boolean newClosedLeft;
        int cmpMin = this.min.compareTo(otherInterval.min);
        if (cmpMin < 0) {
            newMin = this.min;
            newClosedLeft = this.closedLeft;
        } else if (cmpMin > 0) {
            newMin = otherInterval.min;
            newClosedLeft = otherInterval.closedLeft;
        } else {
            newMin = this.min;
            newClosedLeft = this.closedLeft || otherInterval.closedLeft;
        }

        Real newMax;
        boolean newClosedRight;
        int cmpMax = this.max.compareTo(otherInterval.max);
        if (cmpMax > 0) {
            newMax = this.max;
            newClosedRight = this.closedRight;
        } else if (cmpMax < 0) {
            newMax = otherInterval.max;
            newClosedRight = otherInterval.closedRight;
        } else {
            newMax = this.max;
            newClosedRight = this.closedRight || otherInterval.closedRight;
        }

        return new Interval(newMin, newMax, newClosedLeft, newClosedRight);
    }

    public Real length() {
        return max.subtract(min);
    }

    @Override
    public boolean isEmpty() {
        return min.equals(max) && !(closedLeft && closedRight);
    }

    @Override
    public String description() {
        return toString();
    }

    @Override
    public Set<Real> difference(Set<Real> other) {
        if (!(other instanceof Interval)) {
            throw new IllegalArgumentException("Can only compute difference with another Interval");
        }
        if (!overlaps(other)) {
            return this;
        }
        return null;
    }

    @Override
    public boolean isSubsetOf(Set<Real> other) {
        if (!(other instanceof Interval)) {
            return false;
        }
        return ((Interval) other).contains(this);
    }

    @Override
    public boolean overlaps(Set<Real> other) {
        if (!(other instanceof Interval)) {
            return false;
        }
        return intersection(other) != null;
    }

    @Override
    public boolean isOpen() {
        return !closedLeft && !closedRight;
    }

    @Override
    public boolean isClosed() {
        return closedLeft && closedRight;
    }

    public boolean isClopen() {
        return isOpen() && isClosed();
    }

    @Override
    public String toString() {
        String left = closedLeft ? "[" : "(";
        String right = closedRight ? "]" : ")";
        return left + min + ", " + max + right;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Interval))
            return false;
        Interval other = (Interval) obj;
        return min.equals(other.min) && max.equals(other.max) &&
                closedLeft == other.closedLeft && closedRight == other.closedRight;
    }

    @Override
    public int hashCode() {
        int result = min.hashCode();
        result = 31 * result + max.hashCode();
        result = 31 * result + (closedLeft ? 1 : 0);
        result = 31 * result + (closedRight ? 1 : 0);
        return result;
    }
}

