package org.jscience.mathematics.analysis;

import org.jscience.mathematics.FiniteSet;
import org.jscience.mathematics.Set;
import org.jscience.mathematics.algebraic.numbers.ComparableNumber;
import org.jscience.mathematics.algebraic.numbers.Integer;
import org.jscience.mathematics.algebraic.numbers.Long;

import java.util.Iterator;

/**
 * A class representing an interval on N, Q, R...
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//if anyone knows how to build complex intervals...
//see intervalList for intervals on N*, Q*, R*
//we don't provide any support for primitive types (int, long, float, double) intervals as such
//this would be 4 more classes where what we provide here gives the same functionnality with minimal computing penalty

//I have had many difficulties to program this file, so please be cautious when using it
public class Interval extends Object implements Set {

    public final static Interval EMPTY = new Interval();

    private ComparableNumber startValue;
    private ComparableNumber endValue;
    private boolean startBoundIncluded;
    private boolean endBoundIncluded;

    private Interval() {
        startValue = new Integer(0);
        endValue = new Integer(0);
        startBoundIncluded = false;
        endBoundIncluded = false;
    }

    //interval will be build even if endValue < startValue (IMPORTANT: bounds are always reordered)
    //bounds are included by default
    //  intervals like [x,x[ not allowed, use either [x,x] or ]x,x[
    //intervals -infinity, -infinity or +infinity, +infinity not allowed
    public Interval(ComparableNumber startValue, ComparableNumber endValue) {

        if (startValue.getClass().equals(endValue.getClass())) {
            if (startValue.isNaN() || endValue.isNaN()) {
                throw new IllegalArgumentException("Intervals can't be defined with NaN values.");
            } else {
                this.startValue = startValue;
                this.endValue = endValue;
                reorderBounds();
                if ((startValue.isPositiveInfinity()) ||
                        (endValue.isNegativeInfinity())) {
                    throw new IllegalArgumentException("Intervals (-infinity, -infinity) or (+infinity, +infinity) are not allowed.");
                } else {
                    if (startValue.isNegativeInfinity()) {
                        startBoundIncluded = false;
                    } else {
                        startBoundIncluded = true;
                    }

                    if (endValue.isPositiveInfinity()) {
                        endBoundIncluded = false;
                    } else {
                        endBoundIncluded = true;
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("Intervals needs ComparableNumbers of the same class.");
        }
    }

    /**
     * Creates a new Interval object.
     *
     * @param i DOCUMENT ME!
     */
    public Interval(Interval i) {
        this.startValue = i.getInf();
        this.endValue = i.getSup();
        startBoundIncluded = i.isInfBoundIncluded();
        endBoundIncluded = i.isSupBoundIncluded();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ComparableNumber getInf() {
        return startValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isInfBoundIncluded() {
        return startBoundIncluded;
    }

    //you should never include the bound for ComparableNumber.NEGATIVE_INFINITY and for ComparableNumber.POSITIVE_INFINITY
    //WARNING: as bounds are reordered at construction time so that startValue < endValue you are maybe settting the WRONG bound.
    public void setInfBoundValue(boolean included) {
        if (!startValue.isNegativeInfinity()) {
            if (!startValue.equals(endValue)) {
                startBoundIncluded = included;
            } else {
                throw new IllegalArgumentException("Can't set only one bound for zero length intervals.");
            }
        } else {
            throw new IllegalArgumentException("Can't set bound for infinity.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ComparableNumber getSup() {
        return endValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSupBoundIncluded() {
        return endBoundIncluded;
    }

    //you should never include the bound for ComparableNumber.NEGATIVE_INFINITY and for ComparableNumber.POSITIVE_INFINITY
    //WARNING: as bounds are reordered at construction time so that startValue < endValue you are maybe settting the WRONG bound.
    public void setSupBoundValue(boolean included) {
        if (!endValue.isPositiveInfinity()) {
            if (!startValue.equals(endValue)) {
                endBoundIncluded = included;
            } else {
                throw new IllegalArgumentException("Can't set only one bound for zero length intervals.");
            }
        } else {
            throw new IllegalArgumentException("Can't set bound for infinity.");
        }
    }

    //you should never include the bound for ComparableNumber.NEGATIVE_INFINITY and for ComparableNumber.POSITIVE_INFINITY
    //WARNING: as bounds are reordered at construction time so that startValue < endValue you are maybe settting the WRONG bound.
    public void setBoundValues(boolean included1, boolean included2) {
        if (!startValue.equals(endValue)) {
            if (!startValue.isNegativeInfinity()) {
                startBoundIncluded = included1;
            } else {
                throw new IllegalArgumentException("Can't set bound for infinity.");
            }
            if (!endValue.isPositiveInfinity()) {
                endBoundIncluded = included2;
            } else {
                throw new IllegalArgumentException("Can't set bound for infinity.");
            }
        } else {
            if ((included1 && included2) || (!included1 && !included2)) {
                startBoundIncluded = included1;
                endBoundIncluded = included2;
            } else {
                throw new IllegalArgumentException("Intervals like [x,x[ or ]x,x] not allowed, use either [x,x] or ]x,x[.");
            }
        }
    }

    private void reorderBounds() {
        ComparableNumber temp;

        if (startValue.compareTo(endValue) == 1) {
            temp = endValue;
            endValue = startValue;
            startValue = temp;
        }
    }

    /**
     * Returns the length between bounds. Should be a positive value, as bounds are reordered so that startValue<endValue.
     *
     * @return DOCUMENT ME!
     */
    public ComparableNumber length() {
        if ((startValue.isNegativeInfinity()) ||
                (startValue.isPositiveInfinity()) ||
                (endValue.isNegativeInfinity()) ||
                (endValue.isPositiveInfinity())) {
            return startValue.getPositiveInfinity();
        } else {
            return endValue.getDistance(startValue);
        }

    }

// ]a, b[ where a and b are consecutive integers or longs is also empty

    public boolean isEmpty() {

        return cardinality() == 0;

    }

    //get the class of numbers used for this interval
    public Class getDomain() {
        return startValue.getClass();
    }

    /**
     * Returns true if this set contains the specified element.
     *
     * @param o - element whose presence in this set is to be tested.
     * @return true if this set contains the specified element.
     */
    public boolean contains(Object o) {

        if (o instanceof Interval) {
            return contains((Interval) o);
        } else {
            return false;
        }

    }

    /**
     * @param interval DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean contains(Interval interval) {
        return interval.isIncluded(this);
    }

    /**
     * @param value DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean contains(ComparableNumber value) {
        if (getDomain().equals(value.getClass())) {
            return ((value.compareTo(startValue) == 1) && (value.compareTo(endValue) == -1)) ||
                    (value.equals(startValue) && startBoundIncluded) ||
                    (value.equals(endValue) && endBoundIncluded);
        } else {
            throw new IllegalArgumentException("Comparable numbers must be of the same class.");
        }
    }

    //strict before
    public boolean happensBefore(Interval interval) {
        if (getDomain().equals(interval.getDomain())) {
            return ((endValue.compareTo(interval.getInf()) == -1) ||
                    ((endValue.equals(interval.getInf())) &&
                            ((endBoundIncluded && !interval.isInfBoundIncluded()) ||
                                    (!endBoundIncluded && interval.isInfBoundIncluded()))));
        } else {
            throw new IllegalArgumentException("Intervals don't share the same domain.");
        }
    }

    //strict after
    public boolean happensAfter(Interval interval) {
        if (getDomain().equals(interval.getDomain())) {
            return ((startValue.compareTo(interval.getSup()) == 1) ||
                    ((startValue.equals(interval.getSup())) &&
                            ((startBoundIncluded && !interval.isSupBoundIncluded()) ||
                                    (!startBoundIncluded && interval.isSupBoundIncluded()))));
        } else {
            throw new IllegalArgumentException("Intervals don't share the same domain.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param interval DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean isIncluded(Interval interval) {
        if (getDomain().equals(interval.getDomain())) {
            return ((((startValue.compareTo(interval.getInf()) == 1) &&
                    (startValue.compareTo(interval.getSup()) == -1)) ||
                    ((startValue.equals(interval.getInf())) &&
                            interval.isInfBoundIncluded())) &&
                    (((endValue.compareTo(interval.getSup()) == -1) &&
                            (endValue.compareTo(interval.getInf())) == -1) ||
                            ((endValue.equals(interval.getSup())) && interval.isSupBoundIncluded())));
        } else {
            throw new IllegalArgumentException("Intervals don't share the same domain.");
        }
    }

    //strict overlapping, (not inclusion)
    public boolean overlaps(Interval interval) {
        if (getDomain().equals(interval.getDomain())) {
            return (((startValue.compareTo(interval.getInf()) == -1) ||
                    ((startValue.equals(interval.getInf())) &&
                            (startBoundIncluded && !interval.isInfBoundIncluded()))) &&
                    (endValue.compareTo(interval.getInf()) == 1) &&
                    (endValue.compareTo(interval.getSup()) == -1) ||
                    ((startValue.compareTo(interval.getSup()) == -1) &&
                            (startValue.compareTo(interval.getInf()) == 1)) &&
                            ((endValue.compareTo(interval.getSup()) == 1) ||
                                    ((endValue.equals(interval.getSup())) &&
                                            (endBoundIncluded && !interval.isSupBoundIncluded()))));
        } else {
            throw new IllegalArgumentException("Intervals don't share the same domain.");
        }
    }

    //overlapping or inclusion
    public boolean intersects(Interval interval) {
        if (getDomain().equals(interval.getDomain())) {
            //alternative implementations
            //return overlaps(interval) || isIncluded(interval) || interval.isIncluded(this)
            //or return intersection(interval)!=Interval.EMPTY
            return !(happensBefore(interval) || happensAfter(interval));
        } else {
            throw new IllegalArgumentException("Intervals don't share the same domain.");
        }
    }

    /**
     * Returns the cardinality (the number of elements).
     */
    public int cardinality() {

        if ((startValue instanceof Integer) || (startValue instanceof Long)) {
            if ((!startValue.isNegativeInfinity()) &&
                    (!startValue.isPositiveInfinity()) &&
                    (!endValue.isNegativeInfinity()) &&
                    (!endValue.isPositiveInfinity())) {
                int result;
                result = Math.abs(startValue.getDistance(endValue).intValue()) + 1;
                if (!startBoundIncluded) {
                    result = result - 1;
                }
                if (!endBoundIncluded) {
                    result = result - 1;
                }
                return result;
            } else {
                return Integer.POSITIVE_INFINITY;
            }

        } else {
            return Integer.POSITIVE_INFINITY;
        }

    }

    //set must be an Interval
    //returned Set is an Interval, not a Set of Intervals
    public Set intersection(Set set) {

        if (set instanceof Interval) {

            Interval interval;
            Interval result;

            interval = (Interval) set;
            if (getDomain().equals(interval.getDomain())) {
                if (!intersects(interval)) {
                    result = Interval.EMPTY;
                } else {
                    result = new Interval(startValue.max(interval.getInf()),
                            endValue.min(interval.getSup()));
                    result.setInfBoundValue(startBoundIncluded &&
                            interval.isInfBoundIncluded());
                    result.setSupBoundValue(endBoundIncluded &&
                            interval.isSupBoundIncluded());
                }

                return result;
            } else {
                throw new IllegalArgumentException("Intervals don't share the same domain.");
            }

        } else {
            throw new IllegalArgumentException("Set class not recognised by this method.");
        }

    }

    //set must be an Interval
    //result is a Set of intervals
    public Set union(Set set) {

        if (set instanceof Interval) {

            Interval interval;
            FiniteSet result;
            Interval value;

            interval = (Interval) set;
            if (getDomain().equals(interval.getDomain())) {
                result = FiniteSet.EMPTY;

                if (isIncluded(interval)) {
                    result.union(interval);
                } else if (interval.isIncluded(this)) {
                    result.union(this);
                } else if (overlaps(interval)) {
                    value = new Interval(startValue.min(interval.getInf()),
                            endValue.max(interval.getSup()));
                    value.setInfBoundValue(startBoundIncluded ||
                            interval.isInfBoundIncluded());
                    value.setSupBoundValue(endBoundIncluded ||
                            interval.isSupBoundIncluded());
                    result.union(value);
                } else {
                    result.union(interval);
                    result.union(this);
                }

                return result;
            } else {
                throw new IllegalArgumentException("Intervals don't share the same domain.");
            }

        } else {
            throw new IllegalArgumentException("Set class not recognised by this method.");
        }

    }

    /**
     * Removes the specified element from this set if it is present (optional operation).
     *
     * @param o - object to be removed from this set, if present.
     * @return true if the set contained the specified element.
     */
    public boolean remove(Object o) {

        if (o instanceof ComparableNumber) {
            if (contains((ComparableNumber) o)) {
                remove((ComparableNumber) o);
                return true;
            } else {
                return false;
            }
        } else {
            if (o instanceof Interval) {
                if (contains((Interval) o)) {
                    remove((Interval) o);
                    return true;
                } else {
                    return false;
                }
            } else {
                //recommended error is ClassCastException
                throw new IllegalArgumentException("Can't remove an object of this class.");
            }
        }

    }

    /**
     * Removes the specified element from this set if it is present.
     *
     * @param c - ComparableNumber to be removed from this set, if present.
     * @return a set of intervals.
     */
    public Set remove(ComparableNumber c) {

        if (getDomain().equals(c.getClass())) {
            FiniteSet set;
            set = FiniteSet.EMPTY;
            if (contains(c)) {
                Interval i = new Interval(startValue, c);
                i.setBoundValues(startBoundIncluded, false);
                set.union(i);
                i = new Interval(c, endValue);
                i.setBoundValues(false, endBoundIncluded);
                set.union(i);
                return set;
            } else {
                set.union(this);
                return set;
            }
        } else {
            throw new IllegalArgumentException("Comparable numbers must be of the same class.");
        }

    }

    /**
     * Removes the specified interval from this set if it is present.
     *
     * @param i - Interval to be removed from this set, if present.
     * @return a set of intervals.
     */
    public Set remove(Interval i) {

        if (getDomain().equals(i.getDomain())) {
            FiniteSet set;
            set = FiniteSet.EMPTY;
            if (equals(i)) {
                return set;
            } else {
                if (intersects(i)) {
                    if ((startValue.compareTo(i.getInf()) == 1) ||
                            (startValue.compareTo(i.getInf()) == 0 && !startBoundIncluded && i.startBoundIncluded)) {
                        if ((endValue.compareTo(i.getSup()) == -1) ||
                                (endValue.compareTo(i.getSup()) == 0 && !endBoundIncluded && i.endBoundIncluded)) {
                            return set;
                        } else {
                            Interval inter = new Interval(i.getSup(), endValue);
                            inter.setBoundValues(!i.endBoundIncluded, endBoundIncluded);
                            set.union(inter);
                            return set;
                        }
                    } else {
                        if ((endValue.compareTo(i.getSup()) == -1) ||
                                (endValue.compareTo(i.getSup()) == 0 && !endBoundIncluded && i.endBoundIncluded)) {
                            Interval inter1 = new Interval(startValue, i.getInf());
                            inter1.setBoundValues(startBoundIncluded, !i.endBoundIncluded);
                            set.union(inter1);
                            Interval inter2 = new Interval(i.getSup(), endValue);
                            inter2.setBoundValues(!i.endBoundIncluded, endBoundIncluded);
                            set.union(inter2);
                            return set;
                        } else {
                            Interval inter = new Interval(startValue, i.getInf());
                            inter.setBoundValues(startBoundIncluded, !i.endBoundIncluded);
                            set.union(inter);
                            return set;
                        }
                    }
                } else {
                    set.union(this);
                    return set;
                }
            }
        } else {
            throw new IllegalArgumentException("Intervals don't share the same domain.");
        }

    }

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean equals(Object o) {
        Interval interval;

        if ((o != null) && (o instanceof Interval)) {
            interval = (Interval) o;

            return (startValue.equals(interval.getInf())) &&
                    (endValue.equals(interval.getSup())) &&
                    (startBoundIncluded == interval.isInfBoundIncluded()) &&
                    (endBoundIncluded == interval.isSupBoundIncluded());
        } else {
            return false;
        }
    }

    /**
     * Returns the hash code value for this set.
     *
     * @return the hash code value for this set.
     */
    public int hashCode() {

        //anyone for a better hashCode value ?
        return startValue.hashCode() * endValue.hashCode();

    }

    /**
     * Returns a string representing this set.
     */
    public String toString() {

        StringBuffer result;

        result = new StringBuffer();
        if (startBoundIncluded) {
            result.append("[");
        } else {
            result.append("]");
        }
        result.append(startValue.toString());
        result.append(", ");
        if (endBoundIncluded) {
            result.append("]");
        } else {
            result.append("[");
        }
        result.append(endValue.toString());

        return result.toString();

    }

    /**
     * Returns an iterator over the elements in this set.
     *
     * @return an iterator over the elements in this set.
     */
    public Iterator iterator() {

        throw new UnsupportedOperationException("You can't iterate over a possibly infinite cardinality interval.");

    }

    /**
     * Returns an array containing all of the elements in this set.
     *
     * @return an array containing all of the elements in this set.
     */
    public Object[] toArray() {

        throw new UnsupportedOperationException("You can't get an array of elements for a possibly infinite cardinality interval.");

    }

}
