/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util;

import java.util.*;


/**
 * utility class for calculations on sets
 *
 * @author Holger Antelmann
 */
public class CollectionUtils {
    /**
     * Creates a new CollectionUtils object.
     */
    private CollectionUtils() {
    }

    /**
     * convenience method to display a collection in a JList for
     * example
     *
     * @param <T> DOCUMENT ME!
     * @param col DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static <T> List<T> asList(Collection<T> col) {
        if (col instanceof List) {
            return (List<T>) col;
        }

        ArrayList<T> list = new ArrayList<T>(col.size());

        for (T e : col)
            list.add(e);

        return list;
    }

    /**
     * filters all objects out of the given set that are not accepted
     * by the filter
     *
     * @param <T>    DOCUMENT ME!
     * @param col    DOCUMENT ME!
     * @param filter DOCUMENT ME!
     */
    public static <T> void filter(Collection<T> col, Filter<T> filter) {
        for (T e : col) {
            if (!filter.accept(e)) {
                col.remove(e);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param <T>   DOCUMENT ME!
     * @param array DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws IllegalArgumentException if the array contains duplicates
     */
    public static <T> HashSet<T> makeSet(T[] array)
            throws IllegalArgumentException {
        return makeSet(array, false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param <T>              DOCUMENT ME!
     * @param array            DOCUMENT ME!
     * @param ignoreDuplicates DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws IllegalArgumentException if the array contains duplicates and
     *                                  ignoreduplicates is true
     */
    public static <T> HashSet<T> makeSet(T[] array, boolean ignoreDuplicates)
            throws IllegalArgumentException {
        HashSet<T> set = new HashSet<T>(array.length);

        for (T element : array) {
            if (ignoreDuplicates) {
                set.add(element);
            } else {
                if (!set.add(element)) {
                    throw new IllegalArgumentException(
                            "duplicate entry in array: " + element);
                }
            }
        }

        return set;
    }

    /**
     * returns a set containing all elements from both parameters
     *
     * @param <T> DOCUMENT ME!
     * @param set1 DOCUMENT ME!
     * @param set2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    /*
    public static Set union (Set set1, Set set2) {
        HashSet set = new HashSet(set1.size() + set2.size());
        Iterator i = set1.iterator();
        while (i.hasNext()) {
            set.add(i.next());
        }
        i = set2.iterator();
        while (i.hasNext()) {
            set.add(i.next());
        }
        return set;
    }
    */
    /**
     * returns a set that contains all elements from set1 that are not
     * in set2
     *
     * @param <T>  DOCUMENT ME!
     * @param set1 DOCUMENT ME!
     * @param set2 DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static <T> Set<? extends T> difference(Set<? extends T> set1,
                                                  Set<? extends T> set2) {
        HashSet<T> set = new HashSet<T>(set1.size());
        Iterator<? extends T> i = set1.iterator();

        while (i.hasNext()) {
            set.add(i.next());
        }

        i = set2.iterator();

        while (i.hasNext()) {
            set.remove(i.next());
        }

        return set;
    }

/**
 * returns a set containing all elements that are in set1 as well as in set2
 * @see java.util.Collection#retainAll(Collection)
 */

    /*
    public static Set intersection (Set set1, Set set2) {
        HashSet set = new HashSet(
            (set1.size() > set2.size())? set1.size() : set2.size());
        Iterator i = set1.iterator();
        while (i.hasNext()) {
            Object obj = i.next();
            if (set2.contains(obj)) set.add(obj);
        }
        return set;
    }
    */
/** returns a set containing only those elements that appear in only one of the given sets */

    /*
    public static Set unionMinusIntersection (Set set1, Set set2) {
        HashSet set = new HashSet(
            (set1.size() > set2.size())? set1.size() : set2.size());
        Iterator i = set1.iterator();
        while (i.hasNext()) {
            Object obj = i.next();
            if (!set2.contains(obj)) set.add(obj);
        }
        i = set2.iterator();
        while (i.hasNext()) {
            Object obj = i.next();
            if (!set1.contains(obj)) set.add(obj);
        }
        return set;
    }
    */
}
