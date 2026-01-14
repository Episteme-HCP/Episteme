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
 * A conflict set whose conflict resolution policy specifies that no rule
 * can be fired more than once with the same objects. This conflict set
 * requires a large amount of memory to store the history of rule firing, so
 * it must be used with care. It also tends to get inefficient when the
 * history grows.
 *
 * @author Carlos Figueira Filho (<a
 *         href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.01  25 May 2000
 */
public class NaturalConflictSet extends DefaultConflictSet {
    /** The history rule firing. */
    private Vector history;

/**
     * Class constructor.
     */
    public NaturalConflictSet() {
        super();
        this.history = new Vector();
    }

    /**
     * Removes all rules from this conflict set, as well as cleaning
     * any history that might have been stored.
     */
    public void flush() {
        super.flush();
        this.history = new Vector();
    }

    /**
     * Inserts a rule instantiation.
     *
     * @param element a conflict set element that holds the rule index as well
     *        as the objects bound to the rule declarations.
     */
    public void insertElement(ConflictSetElement element) {
        if (!history.contains(element)) {
            super.insertElement(element);
        }
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
        ConflictSetElement result = super.nextElement();

        if (!history.contains(result)) {
            history.addElement(result);
        }

        return result;
    }
}
