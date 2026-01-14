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

package org.jscience.computing.ai.expertsystem.rete;


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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A node in the Rete network that is activated if the object belongs to
 * the class it represents. An object of this class is the entry point of the
 * Rete network.
 *
 * @author Carlos Figueira Filho (<a
 *         href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 1.0   13 Jul 2000
 */
public class ClassFilterReteNode extends ReteNode {
    /** The class object that indicates which tokens can pass by this node. */
    private Class classType;

/**
     * Class constructor.
     *
     * @param classType the class object that indicates which tokens
     *                  can pass by this node.
     */
    public ClassFilterReteNode(Class classType) {
        super(1, 1);
        this.classType = classType;
    }

    /**
     * Returns the class object associated with this node.
     *
     * @return the class object associated with this node.
     */
    public Class getClassType() {
        return classType;
    }

    /**
     * Informs this node that an object has arrived. As the type check
     * of the objects is being performed at the knowledge base, we will assume
     * that this kind of node will always propagate the objects that arrive at
     * it.
     *
     * @param obj the object that arrived at this node.
     */
    public void newObject(Object obj) {
        propagate(obj, 0);
    }

    /**
     * Informs this node that an object has arrived. As the type check
     * of the objects is being performed at the knowledge base, we will assume
     * that this kind of node will always propagate the objects that arrive at
     * it.
     *
     * @param obj the object that arrived at this node.
     * @param input DOCUMENT ME!
     */
    public void newObject(Object obj, int input) {
        propagate(obj, input);
    }

    /**
     * Returns a string representation of this object. Useful for
     * debugging.
     *
     * @return a string representation of this object.
     */
    public String toString() {
        return ("ClassFilterReteNode[classType=" + classType.getName() + "]");
    }

    /**
     * Returns the successors of this node.
     *
     * @return the successors of this node.
     */
    public List getClassFilterSuccessors() {
        List result = super.getSuccessors();

        if (result.size() != 0) {
            List aux = new ArrayList();
            result = (List) result.get(0);

            for (Iterator i = result.iterator(); i.hasNext();) {
                aux.add(((PairIntReteNode) i.next()).getNode());
            }

            return aux;
        } else {
            return result;
        }
    }
}
