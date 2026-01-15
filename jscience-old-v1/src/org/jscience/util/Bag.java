package org.jscience.util;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Vector;


/**
 * A class representing a bag data structure (a list that does not permit
 * null).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Bag extends AbstractList {
    /** DOCUMENT ME! */
    private Vector vector;

/**
     * Creates a new Bag object.
     */
    public Bag() {
        vector = new Vector();
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     * @param element DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void add(int index, Object element) {
        if (element != null) {
            vector.add(index, element);
        } else {
            throw new IllegalArgumentException("Bag don't allow null element.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public boolean add(Object o) {
        if (o != null) {
            return vector.add(o);
        } else {
            throw new IllegalArgumentException("Bags don't allow null element.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public boolean addAll(int index, Collection c) {
        if (!c.contains(null)) {
            return vector.addAll(index, c);
        } else {
            throw new IllegalArgumentException("Bags don't allow null element.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object remove(int index) {
        return vector.remove(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     * @param element DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Object set(int index, Object element) {
        if (element != null) {
            return vector.set(index, element);
        } else {
            throw new IllegalArgumentException("Bags don't allow null element.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object get(int index) {
        return vector.get(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int size() {
        return vector.size();
    }

    //it is expected that the other operations will rely on the methods defined here
}
