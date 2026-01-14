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
import org.jscience.computing.ai.expertsystem.AbstractKnowledgeBase;


/**
 * A final node in the Rete network. There will be one final node for each
 * rule in the rule base; when objects arrive at a final node, they are stored
 * in a conflict set element and inserted into the conflict set of the
 * knowledge base.
 *
 * @author Carlos Figueira Filho (<a
 *         href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 1.0   14 Jul 2000
 */
public class FinalReteNode extends ReteNode {
    /**
     * The knowledge base that will receive the elements to pass them
     * to the conflict set. As the conflict set can be changed when the
     * program is being executed, it is easier to concentrate the insertion of
     * elements in the conflict set at one single point (the knowledge base),
     * and have all final nodes to refer that point.
     */
    private AbstractKnowledgeBase knowledgeBase;

    /** The index of the rule correspondent to this node. */
    private int ruleIndex;

    /** Object array, defined as an attribute for efficiency purposes only. */
    private final Object[] ARRAY;

/**
     * Creates a new discriminant rete node.
     *
     * @param numberInputs  the number of inputs of this node.
     * @param knowledgeBase the knowledge base to where the elements
     *                      will be sent.
     * @param ruleIndex     the index of the rule that will be stored in
     *                      the conflict set by the propagation of objects
     *                      arriving at this final node.
     */
    public FinalReteNode(int numberInputs, AbstractKnowledgeBase knowledgeBase,
        int ruleIndex) {
        super(numberInputs, 0);
        this.ruleIndex = ruleIndex;
        this.knowledgeBase = knowledgeBase;
        ARRAY = new Object[numberInputs];
    }

    /**
     * Remove all objects that may be stored in this node. As no
     * information is stored in this node, and it has no successors, this
     * method has been overriden for performance reasons.
     */
    public void flush() {
    }

    /**
     * Informs this node that an object has arrived.
     *
     * @param obj the object that arrived at this node.
     * @param input the input number of this node that is to receive the
     *        object. This node has only one input, so this parameter is not
     *        considered in objects of this class.
     */
    public void newObject(Object obj, int input) {
        ARRAY[input] = obj;

        if (input == (getNumberInputs() - 1)) {
            knowledgeBase.newFirableRule(ruleIndex, ARRAY);
        }
    }

    /**
     * Remove the following objects that may be stored in this node. As
     * no information is stored in this node, and it has no successors, this
     * method has been overriden for performance reasons.
     *
     * @param obj DOCUMENT ME!
     */
    public void remove(Object obj) {
    }

    /**
     * Returns a string representation of this object. Useful for
     * debugging.
     *
     * @return a string representation of this object.
     */
    public String toString() {
        return ("FinalReteNode[ruleIndex=" + ruleIndex + "]");
    }
}
