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
 * A default conflict set, whose conflict resolution policy is not
 * specified. In other words, any of the instantiations can be returned.
 *
 * @author Carlos Figueira Filho (<a
 *         href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.01  29 Mar 2000
 */
public class DefaultConflictSet extends AbstractConflictSet {
    /** The vector where the fireable rules are stored. */
    private Vector fireableRules;

/**
     * Class constructor.
     */
    public DefaultConflictSet() {
        this.fireableRules = new Vector();
    }

    /**
     * Removes all rules from this conflict set, as well as cleaning
     * any history that might have been stored.
     */
    public void flush() {
        this.fireableRules = new Vector();
    }

    /**
     * Inserts a rule instantiation.
     *
     * @param element a conflict set element that holds the rule index as well
     *        as the objects bound to the rule declarations.
     */
    public void insertElement(ConflictSetElement element) {
        if (!fireableRules.contains(element)) {
            fireableRules.addElement(element);
            elementAdded(element); // the callback method.
        }
    }

    /**
     * Checks whether this set has any elements.
     *
     * @return <code>false</code> if there is at least one fireable rule in
     *         this set; <code>true</code> otherwise.
     */
    public boolean isEmpty() {
        return (fireableRules.size() == 0);
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
        if (fireableRules.size() == 0) {
            throw new NoMoreElementsException("Empty conflict set");
        } else {
            ConflictSetElement result = (ConflictSetElement) fireableRules.elementAt(0);
            fireableRules.removeElementAt(0);

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
        removeElementsWith_1D(fireableRules, obj);
    }
}
