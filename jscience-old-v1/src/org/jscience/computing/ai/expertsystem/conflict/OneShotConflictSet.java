package org.jscience.computing.ai.expertsystem.conflict;


/*
 * JEOPS - The Java Embedded Object Production System
 * Copyright (c) 2000   Carlos Figueira Filho
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Contact: Carlos Figueira Filho (csff@cin.ufpe.br)
 */
import java.util.Vector;


/**
 * A conflict set whose conflict resolution policy specifies that no rule
 * can be fired more than once.
 *
 * @author Carlos Figueira Filho (<a
 *         href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.01  24 May 2000
 */
public class OneShotConflictSet extends AbstractConflictSet {
    /**
     * The vector where the fireable rules are stored. It's actually a
     * vector of vectors, to simulate a bidimensional matrix: the lines
     * represent the rules and the columns will have their instantiations.
     */
    private Vector fireableRules;

    /**
     * The history of rule firings. This vector stores boolean values
     * indicating whether a rule has already been fired.
     */
    private Vector history;

    /** The number of elements in this conflict set. */
    private int size;

/**
     * Class constructor.
     */
    public OneShotConflictSet() {
        flush();
    }

    /**
     * Auxiliar method that ensures that the fireable rules vector has
     * as many elements as the given integer.
     *
     * @param size the size the vector must have.
     */
    private void ensureFireableRulesCapacity(int size) {
        while (fireableRules.size() < size) {
            fireableRules.addElement(new Vector());
            history.addElement(Boolean.FALSE);
        }
    }

    /**
     * Removes all rules from this conflict set, as well as cleaning
     * any history that might have been stored.
     */
    public void flush() {
        this.fireableRules = new Vector();
        this.history = new Vector();
        this.size = 0;
    }

    /**
     * Inserts a rule instantiation.
     *
     * @param element a conflict set element that holds the rule index as well
     *        as the objects bound to the rule declarations.
     */
    public void insertElement(ConflictSetElement element) {
        int ruleIndex = element.getRuleIndex();
        ensureFireableRulesCapacity(ruleIndex + 1);

        boolean alreadyFired = ((Boolean) history.elementAt(ruleIndex)).booleanValue();

        if (!alreadyFired) {
            Vector rules = (Vector) fireableRules.elementAt(ruleIndex);

            if (!rules.contains(element)) {
                rules.addElement(element);
                elementAdded(element); // the callback method.
                size++;
            }
        }
    }

    /**
     * Checks whether this set has any elements.
     *
     * @return <code>false</code> if there is at least one fireable rule in
     *         this set; <code>true</code> otherwise.
     */
    public boolean isEmpty() {
        return (size == 0);
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
        ConflictSetElement result = null;

        if (size == 0) {
            throw new NoMoreElementsException("Empty conflict set");
        } else {
            for (int i = 0; (result == null) && (i < fireableRules.size());
                    i++) {
                Vector rules = (Vector) fireableRules.elementAt(i);

                if (rules.size() != 0) {
                    result = (ConflictSetElement) rules.elementAt(0);
                    size -= rules.size();

                    if (debug) {
                        for (int j = 1; j < rules.size(); j++) {
                            elementRemoved((ConflictSetElement) rules.elementAt(
                                    j)); // Callback method
                        }
                    }

                    rules.removeAllElements();
                    history.setElementAt(Boolean.TRUE, i);
                }
            }

            return result;
        }
    }

    /**
     * Remove all elements from this set that uses the given object in
     * its instantiations.
     *
     * @param obj the given object
     */
    public void removeElementsWith(Object obj) {
        size -= removeElementsWith_2D(fireableRules, obj);
    }
}
