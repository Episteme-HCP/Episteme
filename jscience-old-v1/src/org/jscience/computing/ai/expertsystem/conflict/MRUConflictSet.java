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
 * A conflict set whose conflict resolution policy is one that the most
 * recently fired rules have priority over the remaining ones.
 *
 * @author Carlos Figueira Filho (<a
 *         href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.01  25 May 2000
 */
public class MRUConflictSet extends AbstractConflictSet {
    /**
     * The vector where the fireable rules are stored. It's actually a
     * vector of vectors, to simulate a bidimensional matrix: the lines
     * represent the rules and the columns will have their instantiations.
     */
    private Vector fireableRules;

    /**
     * The history of rule firings. If one number (of rule) is after
     * another one, it means that that rule was fired most recently.
     */
    private Vector history;

    /** The number of elements in this conflict set. */
    private int size;

/**
     * Class constructor.
     */
    public MRUConflictSet() {
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

        Vector rules = (Vector) fireableRules.elementAt(ruleIndex);

        if (!rules.contains(element)) {
            rules.addElement(element);
            elementAdded(element); // Callback method
            size++;
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
     *         this object, according to the policy defined in this conflict
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
            for (int i = history.size() - 1; (result == null) && (i >= 0);
                    i--) {
                Integer aux = (Integer) history.elementAt(i);
                int ruleNumber = aux.intValue();
                Vector rules = (Vector) fireableRules.elementAt(ruleNumber);

                if (rules.size() != 0) {
                    result = (ConflictSetElement) rules.elementAt(0);
                    rules.removeElementAt(0);

                    if (i != (history.size() - 1)) {
                        history.removeElement(aux);
                        history.addElement(aux);
                    }
                }
            }

            if (result == null) {
                for (int i = 0; (result == null) && (i < fireableRules.size());
                        i++) {
                    Vector rules = (Vector) fireableRules.elementAt(i);

                    if (rules.size() != 0) {
                        result = (ConflictSetElement) rules.elementAt(0);
                        rules.removeElementAt(0);

                        Integer aux = new Integer(i);
                        history.addElement(aux);
                    }
                }
            }

            size--;

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
