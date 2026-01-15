package org.jscience.tests.computing.ai.expertsystem.monkeys;

import org.jscience.tests.computing.ai.expertsystem.conflict.AbstractConflictSet;
import org.jscience.tests.computing.ai.expertsystem.conflict.ConflictSetElement;
import org.jscience.tests.computing.ai.expertsystem.conflict.NoMoreElementsException;

import java.io.Serializable;

import java.util.*;


/**
 * The conflict resolution policy used to solve the monkeys and bananas
 * problem.
 *
 * @author Carlos Figueira Filho (<a
 *         href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.01  29 Sep 2000
 */
public class MEAConflictSet extends AbstractConflictSet {
    /** The actual conflict set. */
    private SortedSet set;

/**
     * Class constructor.
     */
    public MEAConflictSet() {
        this.set = new TreeSet(new CSEComparator());
    }

    /**
     * Removes all rules from this conflict set, as well as cleaning
     * any history that might have been stored.
     */
    public void flush() {
        set.clear();
    }

    /**
     * Inserts a rule instantiation.
     *
     * @param element a conflict set element that holds the rule index as well
     *        as the objects bound to the rule declarations.
     */
    public void insertElement(ConflictSetElement element) {
        if (set.add(element)) {
            elementAdded(element);
        }
    }

    /**
     * Checks whether this set has any elements.
     *
     * @return <code>false</code> if there is at least one fireable rule in
     *         this set; <code>true</code> otherwise.
     */
    public boolean isEmpty() {
        return (set.size() == 0);
    }

    /**
     * Returns the next rule to be fired.
     *
     * @return a conflict set element among those that have been inserted in
     *         this object, according to the policy defined in the conflict
     *         set.
     *
     * @throws NoMoreElementsException if there aren't any more elements in
     *         this conflict set.
     */
    public ConflictSetElement nextElement() throws NoMoreElementsException {
        try {
            ConflictSetElement e = (ConflictSetElement) set.last();
            set.remove(e);

            return e;
        } catch (NoSuchElementException e) {
            throw new NoMoreElementsException();
        }
    }

    /**
     * Remove all elements from this set that uses the given object in
     * its instantiations.
     *
     * @param obj the given object
     */
    public void removeElementsWith(Object obj) {
        for (Iterator i = set.iterator(); i.hasNext();) {
            ConflictSetElement e = (ConflictSetElement) i.next();

            if (e.isDeclared(obj)) {
                i.remove();
            }
        }
    }

    /**
     * Inner class used to compare two conflict set elements (CSEs)
     */
    private static class CSEComparator implements Comparator, Serializable {
        /**
         * Compares two conflict set elements (CSEs)
         *
         * @param o1 the first CSE.
         * @param o2 the second CSE.
         *
         * @return a negative integer, zero, or a positive integer if the first
         *         argument is less than, equal to or greater than the second
         *         one.
         */
        public int compare(Object o1, Object o2) {
            ConflictSetElement cse1 = (ConflictSetElement) o1;
            ConflictSetElement cse2 = (ConflictSetElement) o2;

            if (cse1.getTimestamp() < cse2.getTimestamp()) {
                return -1;
            } else if (cse1.getTimestamp() == cse2.getTimestamp()) {
                return 0;
            } else {
                return 1;
            }
        }

        /**
         * Indicates whether some other object is "equal to" this
         * Comparator.
         *
         * @param obj the reference object with which to compare.
         *
         * @return <code>true</code> if the specified object is also a
         *         comparator and it imposes the same ordering as this
         *         comparator; <code>false</code> otherwise.
         */
        public boolean equals(Object obj) {
            return (obj instanceof CSEComparator);
        }
    }
}
