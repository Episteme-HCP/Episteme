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

package org.jscience.mathematics.algebra.intervals;

import org.jscience.mathematics.algebra.Interval;
import org.jscience.mathematics.structures.sets.Set;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.topology.TopologicalSpace;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an N-dimensional interval (hyperrectangle) over Real numbers.
 * <p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class IntervalND implements TopologicalSpace<List<Real>> {

    private final FieldIntervalND<Real> backing;

    /**
     * Creates an N-dimensional interval from a backing FieldIntervalND.
     */
    public IntervalND(FieldIntervalND<Real> backing) {
        this.backing = backing;
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

        Real[] minArray = min.toArray(new Real[0]);
        Real[] maxArray = max.toArray(new Real[0]);
        this.backing = RealInterval.closedND(minArray, maxArray);
    }

    /**
     * Creates an N-dimensional interval from Real arrays.
     */
    public IntervalND(Real[] min, Real[] max) {
        this.backing = RealInterval.closedND(min, max);
    }

    /**
     * Gets the number of dimensions.
     */
    public int dimension() {
        return backing.getDimension();
    }

    /**
     * Gets the minimum value for a specific dimension.
     */
    public Real getMin(int dim) {
        return backing.getMin(dim);
    }

    /**
     * Gets the maximum value for a specific dimension.
     */
    public Real getMax(int dim) {
        return backing.getMax(dim);
    }

    /**
     * Gets the minimum point (lower corner).
     */
    public List<Real> getMinPoint() {
        List<Real> min = new ArrayList<>();
        for (int i = 0; i < backing.getDimension(); i++) {
            min.add(backing.getMin(i));
        }
        return min;
    }

    /**
     * Gets the maximum point (upper corner).
     */
    public List<Real> getMaxPoint() {
        List<Real> max = new ArrayList<>();
        for (int i = 0; i < backing.getDimension(); i++) {
            max.add(backing.getMax(i));
        }
        return max;
    }

    /**
     * Gets the width in a specific dimension.
     */
    public Real getWidth(int dim) {
        return backing.getWidth(dim);
    }

    /**
     * Gets the midpoint in a specific dimension.
     */
    public Real getMidpoint(int dim) {
        return backing.getMidpoint(dim);
    }

    /**
     * Gets the backing FieldIntervalND.
     */
    public FieldIntervalND<Real> getBacking() {
        return backing;
    }

    @Override
    public boolean contains(List<Real> point) {
        if (point == null || point.size() != backing.getDimension()) {
            return false;
        }
        return backing.contains(point.toArray(new Real[0]));
    }

    @Override
    public boolean containsPoint(List<Real> point) {
        return contains(point);
    }

    @Override
    public boolean isEmpty() {
        return backing.isEmpty();
    }

    @Override
    public String description() {
        return toString();
    }

    /**
     * Returns the intersection of this interval with another.
     */
    public Set<List<Real>> intersection(Set<List<Real>> other) {
        if (!(other instanceof IntervalND)) {
            throw new IllegalArgumentException("Can only intersect with another IntervalND");
        }
        IntervalND otherND = (IntervalND) other;

        Interval<Real> result = backing.intersection(otherND.backing);
        if (result == null) {
            return null;
        }
        return new IntervalND((FieldIntervalND<Real>) result);
    }

    /**
     * Returns the bounding interval containing both this and another.
     */
    public Set<List<Real>> union(Set<List<Real>> other) {
        if (!(other instanceof IntervalND)) {
            throw new IllegalArgumentException("Can only union with another IntervalND");
        }
        IntervalND otherND = (IntervalND) other;

        Interval<Real> result = backing.boundingInterval(otherND.backing);
        return new IntervalND((FieldIntervalND<Real>) result);
    }

    public Set<List<Real>> difference(Set<List<Real>> other) {
        throw new UnsupportedOperationException(
                "Difference not supported for IntervalND (result may not be a hyperrectangle)");
    }

    public boolean isSubsetOf(Set<List<Real>> other) {
        if (!(other instanceof IntervalND)) {
            return false;
        }
        return ((IntervalND) other).backing.contains(backing);
    }

    public boolean overlaps(Set<List<Real>> other) {
        return intersection(other) != null;
    }

    @Override
    public boolean isOpen() {
        return backing.isOpen();
    }

    @Override
    public boolean isClosed() {
        return backing.isClosed();
    }

    /**
     * Computes the volume (N-dimensional measure) of this interval.
     */
    public Real volume() {
        Real vol = Real.ONE;
        for (int i = 0; i < backing.getDimension(); i++) {
            vol = vol.multiply(backing.getWidth(i));
        }
        return vol;
    }

    @Override
    public String toString() {
        return backing.notation();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof IntervalND))
            return false;
        return backing.equals(((IntervalND) obj).backing);
    }

    @Override
    public int hashCode() {
        return backing.hashCode();
    }
}

