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

package org.jscience.mathematics.analysis;

import org.jscience.mathematics.Set;
import org.jscience.mathematics.algebraic.numbers.ComparableNumber;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * This class represents an intervals list.
 * <p/>
 * <p/>
 * An interval list represent a list of contiguous regions on the real line.
 * All intervals of the list are disjoints to each other, they are stored in
 * ascending order.
 * </p>
 * Empty intervals are discarted. All intervals in the list must use the same
 * ComparableNumber class.
 * <p/>
 * <p/>
 * The class supports the main set operations like union and intersection.
 * </p>
 *
 * @author Luc Maisonobe, Silvere Martin-Michiellot
 * @version $Id: IntervalsList.java,v 1.2 2007-10-21 17:45:32 virtualcall Exp $
 * @see Interval
 */

//I have had many difficulties to program this file, so please be cautious when using it
public class IntervalsList implements Set {
    /**
     * The list of intervals.
     */
    private List intervals;

    /**
     * Build an empty intervals list.
     */
    public IntervalsList() {
        intervals = new ArrayList();
    }

    /**
     * Build an intervals list containing only one interval.
     *
     * @param i interval
     */
    public IntervalsList(Interval i) {
        intervals = new ArrayList();

        if (!i.isEmpty()) {
            intervals.add(i);
        }
    }

    /**
     * Build an intervals list containing two intervals.
     *
     * @param i1 first interval
     * @param i2 second interval
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public IntervalsList(Interval i1, Interval i2) {
        intervals = new ArrayList();

        if (i1.getDomain().equals(i2.getDomain())) {
            if (i1.intersection(i2) != Interval.EMPTY) {
                intervals.add(i1.union(i2));
            } else if (i1.getSup().compareTo(i2.getInf()) == -1) {
                if (!i1.isEmpty()) {
                    intervals.add(i1);
                }

                if (!i2.isEmpty()) {
                    intervals.add(i2);
                }
            } else {
                if (!i2.isEmpty()) {
                    intervals.add(i2);
                }

                if (!i1.isEmpty()) {
                    intervals.add(i1);
                }
            }
        } else {
            throw new IllegalArgumentException(
                    "Intervals are not based on the same domain.");
        }
    }

    /**
     * Copy constructor.
     * <p/>
     * <p/>
     * The copy operation is a deep copy: the underlying intervals are
     * independant of the instances of the copied list.
     * </p>
     *
     * @param list intervals list to copy
     */
    public IntervalsList(IntervalsList list) {
        intervals = new ArrayList(list.intervals.size());

        for (Iterator iterator = list.intervals.iterator(); iterator.hasNext();) {
            intervals.add(new Interval((Interval) iterator.next()));
        }
    }

    /**
     * Check if the instance is empty.
     *
     * @return true if the instance is empty
     */
    public boolean isEmpty() {
        return intervals.isEmpty();
    }

    //get the common class of intervals in the list
    public Class getDomain() {
        if (intervals.size() > 0) {
            return ((Interval) intervals.get(0)).getDomain();
        } else {
            return null;
        }
    }

    /**
     * Check if the instance is connected.
     * <p/>
     * <p/>
     * An interval list is connected if it contains only one interval.
     * </p>
     *
     * @return true is the instance is connected
     */
    public boolean isConnex() {
        return intervals.size() == 1;
    }

    /**
     * Get the lower bound of the list.
     *
     * @return lower bound of the list or Double.NaN if the list does not
     *         contain any interval
     */

    //    public ComparableNumber getInf() {
    //        return intervals.isEmpty()
    //                ? startValue.getNaN() : ((Interval) intervals.get(0)).getInf();
    //    }

    /**
     * Get the upper bound of the list.
     *
     * @return upper bound of the list or Double.NaN if the list does not
     *         contain any interval
     */

    //    public ComparableNumber getSup() {
    //        return intervals.isEmpty()
    //                ? NaN : ((Interval) intervals.get(intervals.size() - 1)).getSup();
    //    }

    /**
     * Get the number of intervals of the list.
     *
     * @return number of intervals in the list
     */
    public int getSize() {
        return intervals.size();
    }

    /**
     * Get an interval from the list.
     *
     * @param i index of the interval
     * @return interval at index i
     */
    public Interval getInterval(int i) {
        return (Interval) intervals.get(i);
    }

    /**
     * Get the ordered list of disjoints intervals.
     *
     * @return list of disjoints intervals in ascending order
     */
    public List getIntervals() {
        return intervals;
    }

    /**
     * Returns the cardinality (the number of elements).
     *
     * @return DOCUMENT ME!
     */
    public int cardinality() {
        int result;
        result = 0;

        for (Iterator iterator = intervals.iterator(); iterator.hasNext();) {
            result += ((Interval) iterator.next()).cardinality();
        }

        return result;
    }

    /**
     * Check if contains the specified element in this set.
     *
     * @param o - object to be checked.
     * @return true if the set contained the specified element.
     */
    public boolean contains(Object o) {
        if (o instanceof ComparableNumber) {
            return contains((ComparableNumber) o);
        } else {
            if (o instanceof Interval) {
                return contains((Interval) o);
            } else {
                return false;
            }
        }
    }

    /**
     * Check if the list contains a point.
     *
     * @param x point to check
     * @return true if the list contains x
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public boolean contains(ComparableNumber x) {
        if (isEmpty()) {
            return false;
        } else {
            if (getDomain().equals(x.getClass())) {
                for (Iterator iterator = intervals.iterator();
                     iterator.hasNext();) {
                    if (((Interval) iterator.next()).contains(x)) {
                        return true;
                    }
                }

                return false;
            } else {
                throw new IllegalArgumentException(
                        "Comparable numbers must be of the same class.");
            }
        }
    }

    /**
     * Check if the list contains an interval.
     *
     * @param i interval to check
     * @return true if i is completely included in the instance
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public boolean contains(Interval i) {
        if (isEmpty()) {
            return false;
        } else {
            if (getDomain().equals(i.getDomain())) {
                for (Iterator iterator = intervals.iterator();
                     iterator.hasNext();) {
                    if (((Interval) iterator.next()).contains(i)) {
                        return true;
                    }
                }

                return false;
            } else {
                throw new IllegalArgumentException(
                        "Intervals don't share the same domain.");
            }
        }
    }

    /**
     * Check if an interval intersects the instance.
     *
     * @param i interval to check
     * @return true if i intersects the instance
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public boolean intersects(Interval i) {
        if (isEmpty()) {
            return false;
        } else {
            if (getDomain().equals(i.getDomain())) {
                for (Iterator iterator = intervals.iterator();
                     iterator.hasNext();) {
                    if (((Interval) iterator.next()).intersects(i)) {
                        return true;
                    }
                }

                return false;
            } else {
                throw new IllegalArgumentException(
                        "Intervals don't share the same domain.");
            }
        }
    }

    /**
     * Inserts the specified element in this set if it is present.
     *
     * @param o - object to be inserted from this set, if present.
     * @return true if the set contained the specified element.
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public boolean insert(Object o) {
        if (o instanceof ComparableNumber) {
            if (contains((ComparableNumber) o)) {
                return true;
            } else {
                insert((ComparableNumber) o);

                return false;
            }
        } else {
            if (o instanceof Interval) {
                if (contains((Interval) o)) {
                    return true;
                } else {
                    insert((Interval) o);

                    return false;
                }
            } else {
                //recommended error is ClassCastException
                throw new IllegalArgumentException(
                        "Can't insert an object of this class.");
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public IntervalsList insert(ComparableNumber c) {
        Interval i;
        i = new Interval(c, c);
        i.setBoundValues(true, true);

        return insert(i);
    }

    /**
     * Add an interval to the instance.
     * <p/>
     * <p/>
     * This method expands the instance.
     * </p>
     * <p/>
     * <p/>
     * This operation is a union operation. The number of intervals in the list
     * can decrease if the interval fills some holes between existing
     * intervals in the list.
     * </p>
     *
     * @param i interval to add to the instance
     * @return DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public IntervalsList insert(Interval i) {
        if (getDomain().equals(i.getDomain())) {
            if (isEmpty()) {
                return new IntervalsList(i);
            } else {
                List resultList = new ArrayList();
                Iterator iterator = intervals.iterator();
                boolean found = false;

                while (iterator.hasNext() && !found) {
                    Interval currentInterval = (Interval) iterator.next();

                    if ((currentInterval.getSup().compareTo(i.getInf()) == -1) ||
                            ((currentInterval.getSup().compareTo(i.getInf()) == 0) &&
                                    !currentInterval.isSupBoundIncluded() &&
                                    !i.isInfBoundIncluded())) {
                        //currentInterval before i
                        found = false;
                        resultList.add(currentInterval);
                    } else {
                        found = true;

                        if ((currentInterval.getSup().compareTo(i.getInf()) == 0) &&
                                ((currentInterval.isSupBoundIncluded() &&
                                        !i.isInfBoundIncluded()) ||
                                        (!currentInterval.isSupBoundIncluded() &&
                                                i.isInfBoundIncluded()))) {
                            //currentInterval just before to i
                            boolean found2 = false;
                            Interval currentInterval2 = null;

                            while (iterator.hasNext() && !found2) {
                                currentInterval2 = (Interval) iterator.next();
                                found2 = ((currentInterval2.getSup().compareTo(i.getSup()) == 1) ||
                                        ((currentInterval2.getSup().compareTo(i.getSup()) == 0) &&
                                                currentInterval2.isSupBoundIncluded() &&
                                                !i.isSupBoundIncluded()));
                            }

                            if (found2) {
                                if ((currentInterval2.getInf().compareTo(i.getSup()) == 0) &&
                                        (currentInterval2.isInfBoundIncluded() ||
                                                i.isSupBoundIncluded())) {
                                    Interval resultInterval = new Interval(currentInterval.getInf(),
                                            currentInterval2.getSup());
                                    resultInterval.setBoundValues(currentInterval.isInfBoundIncluded(),
                                            i.isSupBoundIncluded() ||
                                                    currentInterval2.isSupBoundIncluded());
                                    resultList.add(resultInterval);
                                } else {
                                    Interval resultInterval1 = new Interval(currentInterval.getInf(),
                                            currentInterval.getSup());
                                    resultInterval1.setBoundValues(currentInterval.isInfBoundIncluded(),
                                            currentInterval.isSupBoundIncluded());
                                    resultList.add(resultInterval1);

                                    Interval resultInterval2 = new Interval(currentInterval2.getInf(),
                                            currentInterval2.getSup());
                                    resultInterval2.setBoundValues(currentInterval2.isInfBoundIncluded(),
                                            currentInterval2.isSupBoundIncluded());
                                    resultList.add(resultInterval2);
                                }
                            } else {
                                Interval resultInterval = new Interval(currentInterval.getInf(),
                                        i.getSup());
                                resultInterval.setBoundValues(currentInterval.isInfBoundIncluded(),
                                        i.isSupBoundIncluded());
                                resultList.add(resultInterval);
                            }
                        } else {
                            //((currentInterval.getSup().compareTo(i.getInf())==1) || (currentInterval.getSup().compareTo(i)==0 && currentInterval.isSupBoundIncluded()  && i.isInfBoundIncluded()))
                            if ((currentInterval.getSup().compareTo(i.getSup()) == -1) ||
                                    ((currentInterval.getSup().compareTo(i.getSup()) == 0) &&
                                            !currentInterval.isSupBoundIncluded() &&
                                            i.isSupBoundIncluded())) {
                                //currentInterval ends before i
                                boolean found2 = false;
                                Interval currentInterval2 = null;

                                while (iterator.hasNext() && !found2) {
                                    currentInterval2 = (Interval) iterator.next();
                                    found2 = ((currentInterval2.getSup()
                                            .compareTo(i.getSup()) == 1) ||
                                            ((currentInterval2.getSup().compareTo(i.getSup()) == 0) &&
                                                    currentInterval2.isSupBoundIncluded() &&
                                                    !i.isSupBoundIncluded()));
                                }

                                if (found2) {
                                    if ((currentInterval2.getInf().compareTo(i.getSup()) == 0) &&
                                            (currentInterval2.isInfBoundIncluded() ||
                                                    i.isSupBoundIncluded())) {
                                        Interval resultInterval = new Interval(currentInterval.getInf(),
                                                currentInterval2.getSup());
                                        resultInterval.setBoundValues(currentInterval.isInfBoundIncluded(),
                                                i.isSupBoundIncluded() ||
                                                        currentInterval2.isSupBoundIncluded());
                                        resultList.add(resultInterval);
                                    } else {
                                        Interval resultInterval1 = new Interval(currentInterval.getInf(),
                                                currentInterval.getSup());
                                        resultInterval1.setBoundValues(currentInterval.isInfBoundIncluded(),
                                                currentInterval.isSupBoundIncluded());
                                        resultList.add(resultInterval1);

                                        Interval resultInterval2 = new Interval(currentInterval2.getInf(),
                                                currentInterval2.getSup());
                                        resultInterval2.setBoundValues(currentInterval2.isInfBoundIncluded(),
                                                currentInterval2.isSupBoundIncluded());
                                        resultList.add(resultInterval2);
                                    }
                                } else {
                                    Interval resultInterval = new Interval(currentInterval.getInf(),
                                            i.getSup());
                                    resultInterval.setBoundValues(currentInterval.isInfBoundIncluded(),
                                            i.isSupBoundIncluded());
                                    resultList.add(resultInterval);
                                }
                            } else {
                                //if (currentInterval.getSup().compareTo(i.getSup())==0 && ((currentInterval.isSupBoundIncluded()  && i.isSupBoundIncluded()) || (!currentInterval.isSupBoundIncluded()  && !i.isSupBoundIncluded()))) {
                                //currentInterval ends at i
                                resultList.add(currentInterval);

                                //} else {
                                //currentInterval ends after i
                                //    resultList.add(currentInterval);
                                //}
                            }
                        }
                    }
                }

                while (iterator.hasNext()) {
                    Interval currentInterval = (Interval) iterator.next();
                    resultList.add(currentInterval);
                }

                IntervalsList resultIntervals;
                resultIntervals = new IntervalsList();
                resultIntervals.intervals = resultList;

                return resultIntervals;
            }
        } else {
            throw new IllegalArgumentException(
                    "Intervals don't share the same domain.");
        }
    }

    /**
     * Removes the specified element from this set if it is present (optional
     * operation).
     *
     * @param o - object to be removed from this set, if present.
     * @return true if the set contained the specified element.
     * @throws IllegalArgumentException DOCUMENT ME!
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
                throw new IllegalArgumentException(
                        "Can't remove an object of this class.");
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public IntervalsList remove(ComparableNumber c) {
        Interval i;
        i = new Interval(c, c);
        i.setBoundValues(true, true);

        return remove(i);
    }

    /**
     * Remove an interval from the list.
     * <p/>
     * <p/>
     * This method reduces the instance. This operation is defined in terms of
     * points set operation. As an example, if the [2, 3] interval is
     * subtracted from the list containing only the [0, 10] interval, the
     * result will be the [0, 2[ U ]3, 10] intervals list.
     * </p>
     *
     * @param i interval to remove
     * @return DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public IntervalsList remove(Interval i) {
        if (getDomain().equals(i.getDomain())) {
            if (isEmpty()) {
                return new IntervalsList();
            } else {
                List resultList = new ArrayList();
                Iterator iterator = intervals.iterator();
                boolean found = false;

                while (iterator.hasNext() && !found) {
                    Interval currentInterval = (Interval) iterator.next();

                    if ((currentInterval.getSup().compareTo(i.getInf()) == -1) ||
                            ((currentInterval.getSup().compareTo(i.getInf()) == 0) &&
                                    !currentInterval.isSupBoundIncluded() &&
                                    !i.isInfBoundIncluded())) {
                        //currentInterval before i
                        found = false;
                        resultList.add(currentInterval);
                    } else {
                        found = true;

                        if ((currentInterval.getSup().compareTo(i.getInf()) == 0) &&
                                ((currentInterval.isSupBoundIncluded() &&
                                        !i.isInfBoundIncluded()) ||
                                        (!currentInterval.isSupBoundIncluded() &&
                                                i.isInfBoundIncluded()))) {
                            //currentInterval just before to i
                            boolean found2 = false;
                            Interval currentInterval2 = null;

                            while (iterator.hasNext() && !found2) {
                                currentInterval2 = (Interval) iterator.next();
                                found2 = ((currentInterval2.getSup().compareTo(i.getSup()) == 1) ||
                                        ((currentInterval2.getSup().compareTo(i.getSup()) == 0) &&
                                                currentInterval2.isSupBoundIncluded() &&
                                                !i.isSupBoundIncluded()));
                            }

                            Interval resultInterval1 = new Interval(currentInterval.getInf(),
                                    currentInterval.getSup());
                            resultInterval1.setBoundValues(currentInterval.isInfBoundIncluded(),
                                    currentInterval.isSupBoundIncluded());
                            resultList.add(resultInterval1);

                            if (found2) {
                                if ((currentInterval2.getInf().compareTo(i.getSup()) == 1) ||
                                        ((currentInterval2.getInf().compareTo(i.getSup()) == 0) &&
                                                !(currentInterval2.isInfBoundIncluded() &&
                                                        i.isSupBoundIncluded()))) {
                                    Interval resultInterval2 = new Interval(currentInterval2.getInf(),
                                            currentInterval2.getSup());
                                    resultInterval2.setBoundValues(currentInterval2.isInfBoundIncluded(),
                                            currentInterval2.isSupBoundIncluded());
                                    resultList.add(resultInterval2);
                                } else {
                                    Interval resultInterval2 = new Interval(i.getSup(),
                                            currentInterval2.getSup());
                                    resultInterval2.setBoundValues(!i.isSupBoundIncluded(),
                                            currentInterval2.isSupBoundIncluded());
                                    resultList.add(resultInterval2);
                                }
                            }
                        } else {
                            //((currentInterval.getSup().compareTo(i.getInf())==1) || (currentInterval.getSup().compareTo(i)==0 && currentInterval.isSupBoundIncluded()  && i.isInfBoundIncluded()))
                            if ((currentInterval.getSup().compareTo(i.getSup()) == -1) ||
                                    ((currentInterval.getSup().compareTo(i.getSup()) == 0) &&
                                            !currentInterval.isSupBoundIncluded() &&
                                            i.isSupBoundIncluded())) {
                                //currentInterval ends before i
                                boolean found2 = false;
                                Interval currentInterval2 = null;

                                while (iterator.hasNext() && !found2) {
                                    currentInterval2 = (Interval) iterator.next();
                                    found2 = ((currentInterval2.getSup()
                                            .compareTo(i.getSup()) == 1) ||
                                            ((currentInterval2.getSup().compareTo(i.getSup()) == 0) &&
                                                    currentInterval2.isSupBoundIncluded() &&
                                                    !i.isSupBoundIncluded()));
                                }

                                Interval resultInterval1 = new Interval(currentInterval.getInf(),
                                        i.getInf());
                                resultInterval1.setBoundValues(currentInterval.isInfBoundIncluded(),
                                        !i.isInfBoundIncluded());
                                resultList.add(resultInterval1);

                                if (found2) {
                                    if ((currentInterval2.getInf().compareTo(i.getSup()) == 1) ||
                                            ((currentInterval2.getInf()
                                                    .compareTo(i.getSup()) == 0) &&
                                                    !(currentInterval2.isInfBoundIncluded() &&
                                                            i.isSupBoundIncluded()))) {
                                        Interval resultInterval2 = new Interval(currentInterval2.getInf(),
                                                currentInterval2.getSup());
                                        resultInterval2.setBoundValues(currentInterval2.isInfBoundIncluded(),
                                                currentInterval2.isSupBoundIncluded());
                                        resultList.add(resultInterval2);
                                    } else {
                                        Interval resultInterval2 = new Interval(i.getSup(),
                                                currentInterval2.getSup());
                                        resultInterval2.setBoundValues(!i.isSupBoundIncluded(),
                                                currentInterval2.isSupBoundIncluded());
                                        resultList.add(resultInterval2);
                                    }
                                }
                            } else {
                                if ((currentInterval.getSup().compareTo(i.getSup()) == 0) &&
                                        ((currentInterval.isSupBoundIncluded() &&
                                                i.isSupBoundIncluded()) ||
                                                (!currentInterval.isSupBoundIncluded() &&
                                                        !i.isSupBoundIncluded()))) {
                                    //currentInterval ends at i
                                    Interval resultInterval = new Interval(currentInterval.getInf(),
                                            i.getInf());
                                    resultInterval.setBoundValues(currentInterval.isInfBoundIncluded(),
                                            !i.isInfBoundIncluded());
                                    resultList.add(resultInterval);
                                } else {
                                    //currentInterval ends after i
                                    Interval resultInterval1 = new Interval(currentInterval.getInf(),
                                            i.getInf());
                                    resultInterval1.setBoundValues(currentInterval.isInfBoundIncluded(),
                                            !i.isInfBoundIncluded());
                                    resultList.add(resultInterval1);

                                    Interval resultInterval2 = new Interval(i.getSup(),
                                            currentInterval.getSup());
                                    resultInterval2.setBoundValues(!i.isInfBoundIncluded(),
                                            currentInterval.isSupBoundIncluded());
                                    resultList.add(resultInterval2);
                                }
                            }
                        }
                    }
                }

                while (iterator.hasNext()) {
                    Interval currentInterval = (Interval) iterator.next();
                    resultList.add(currentInterval);
                }

                IntervalsList resultIntervals;
                resultIntervals = new IntervalsList();
                resultIntervals.intervals = resultList;

                return resultIntervals;
            }
        } else {
            throw new IllegalArgumentException(
                    "Intervals don't share the same domain.");
        }
    }

    /**
     * Intersects the instance and an interval.
     *
     * @param i interval
     * @return DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public IntervalsList intersect(Interval i) {
        if (getDomain().equals(i.getDomain())) {
            if (isEmpty()) {
                return new IntervalsList();
            } else {
                IntervalsList resultList = new IntervalsList();

                for (Iterator iterator = intervals.iterator();
                     iterator.hasNext();) {
                    resultList = resultList.insert((Interval) ((Interval) iterator.next()).intersection(
                            i));
                }

                return resultList;
            }
        } else {
            throw new IllegalArgumentException(
                    "Intervals don't share the same domain.");
        }
    }

    /**
     * Add an intervals list to the instance.
     * <p/>
     * <p/>
     * This method expands the instance.
     * </p>
     * <p/>
     * <p/>
     * This operation is a union operation. The number of intervals in the list
     * can decrease if the list fills some holes between existing intervals in
     * the instance.
     * </p>
     *
     * @param list intervals list to add to the instance
     * @return DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public IntervalsList insert(IntervalsList list) {
        if (getDomain().equals(list.getDomain())) {
            if (isEmpty()) {
                return new IntervalsList(list);
            } else {
                IntervalsList resultList = new IntervalsList(this);

                for (Iterator iterator = list.intervals.iterator();
                     iterator.hasNext();) {
                    resultList = resultList.insert((Interval) iterator.next());
                }

                return resultList;
            }
        } else {
            throw new IllegalArgumentException(
                    "IntervalsLists don't share the same domain.");
        }
    }

    /**
     * Remove an intervals list from the instance.
     *
     * @param list intervals list to remove
     * @return DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public IntervalsList remove(IntervalsList list) {
        if (getDomain().equals(list.getDomain())) {
            if (isEmpty()) {
                return new IntervalsList();
            } else {
                IntervalsList resultList = new IntervalsList(this);

                for (Iterator iterator = list.intervals.iterator();
                     iterator.hasNext();) {
                    resultList = resultList.remove((Interval) iterator.next());
                }

                return resultList;
            }
        } else {
            throw new IllegalArgumentException(
                    "IntervalsLists don't share the same domain.");
        }
    }

    /**
     * Intersect the instance and another intervals list.
     *
     * @param list list to intersect with the instance
     * @return DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public IntervalsList intersect(IntervalsList list) {
        if (getDomain().equals(list.getDomain())) {
            if (isEmpty()) {
                return new IntervalsList();
            } else {
                IntervalsList resultList = new IntervalsList();

                for (Iterator iterator = list.intervals.iterator();
                     iterator.hasNext();) {
                    resultList = resultList.insert(intersect(
                            (Interval) iterator.next()));
                }

                return resultList;
            }
        } else {
            throw new IllegalArgumentException(
                    "IntervalsLists don't share the same domain.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param set DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public Set intersection(Set set) {
        if (set instanceof IntervalsList) {
            return intersect((IntervalsList) set);
        } else {
            if (set instanceof Interval) {
                return intersect((Interval) set);
            } else {
                throw new IllegalArgumentException(
                        "Set class not recognised by this method.");
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param set DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public Set union(Set set) {
        if (set instanceof IntervalsList) {
            return insert((IntervalsList) set);
        } else {
            if (set instanceof Interval) {
                return insert((Interval) set);
            } else {
                throw new IllegalArgumentException(
                        "Set class not recognised by this method.");
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param set DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public Set subtraction(Set set) {
        if (set instanceof IntervalsList) {
            return remove((IntervalsList) set);
        } else {
            if (set instanceof Interval) {
                return remove((Interval) set);
            } else {
                throw new IllegalArgumentException(
                        "Set class not recognised by this method.");
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean equals(Object o) {
        if ((o != null) && (o instanceof IntervalsList)) {
            IntervalsList intervalsList = (IntervalsList) o;
            boolean result = true;
            int i = 0;

            while ((i < intervalsList.intervals.size()) && result) {
                result = intervals.get(i).equals(intervalsList.intervals.get(i));
            }

            return result;
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
        return intervals.hashCode();
    }

    /**
     * Returns a string representing this set.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer result;

        result = new StringBuffer();
        result.append("(");

        for (Iterator iterator = intervals.iterator(); iterator.hasNext();) {
            result.append(((Interval) iterator.next()).toString());
        }

        result.append(")");

        return result.toString();
    }

    /**
     * Returns an iterator over the elements in this set.
     *
     * @return an iterator over the elements in this set.
     */
    public Iterator iterator() {
        return intervals.iterator();
    }

    /**
     * Returns an array containing all of the elements in this set.
     *
     * @return an array containing all of the elements in this set.
     */
    public Object[] toArray() {
        return intervals.toArray();
    }
}
