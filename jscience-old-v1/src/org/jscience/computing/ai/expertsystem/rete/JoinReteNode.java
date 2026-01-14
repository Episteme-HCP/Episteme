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
import org.jscience.computing.ai.expertsystem.AbstractRuleBase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A node in the Rete network that has more than one input, so that objects
 * coming from distinct parts of the network are joined together in instances
 * of this class.
 *
 * @author Carlos Figueira Filho (<a
 *         href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 1.0   14 Jul 2000
 */
public class JoinReteNode extends ReteNode {
    /**
     * The objects that are "waiting" at some inputs of this node for
     * the arrival of objects in other inputs, in order to be propagated at
     * this node. It's actually a list of lists, where for every input of this
     * node there is a list of the objects entered by that input.
     */
    private List[] waitingObjects;

    /**
     * The rule base used to check the validity of the property of the
     * incoming objects.
     */
    private AbstractRuleBase ruleBase;

    /**
     * The index of the rule that contains the condition to be checked
     * by this node.
     */
    private int ruleIndex;

    /*
      * The index of the declaration whose property is checked by this
      * node.
      */
    private int declIndex;

    /** Object array, defined as an attribute for efficiency purposes only. */
    private final Object[] ARRAY;

    /** Iterator array, defined as an attribute for efficiency purposes only. */
    private final Iterator[] its;

/**
     * Class constructor.
     *
     * @param numberInputs the number of inputs of this node.
     * @param ruleBase     the rule base used to check if this node can be
     *                     activated.
     * @param ruleIndex    the index of the rule that contains the
     *                     condition to be checked by this node.
     * @param declIndex    the index of the declaration being joined by
     *                     this node.
     */
    public JoinReteNode(int numberInputs, AbstractRuleBase ruleBase,
        int ruleIndex, int declIndex) {
        super(numberInputs, numberInputs);

        if (numberInputs < 2) {
            throw new IllegalArgumentException(
                "Join nodes must have at least 2 inputs.");
        }

        waitingObjects = new List[numberInputs];

        for (int i = 0; i < numberInputs; i++) {
            waitingObjects[i] = new ArrayList();
        }

        ARRAY = new Object[ruleBase.getNumberOfDeclarations()[ruleIndex]];
        its = new Iterator[numberInputs - 1];
        this.ruleBase = ruleBase;
        this.ruleIndex = ruleIndex;
        this.declIndex = declIndex;
    }

    /**
     * Remove all objects that may be stored in this node.
     */
    public void flush() {
        int numberInputs = getNumberInputs();

        for (int i = 0; i < numberInputs; i++) {
            waitingObjects[i] = new ArrayList();
        }
    }

    /**
     * Informs this node that an object has arrived.
     *
     * @param obj the object that arrived at this node.
     * @param input the input number of this node that is to receive the
     *        object.
     */
    public void newObject(Object obj, int input) {
        final int numberInputs = getNumberInputs();
        waitingObjects[input].add(obj);

        if (input == (numberInputs - 1)) {
            for (int i = 0; i < waitingObjects[0].size(); i++) {
                for (int j = 0; j < input; j++) {
                    ARRAY[j] = waitingObjects[j].get(i);
                }

                ARRAY[input] = obj;
                ruleBase.setObjects(ruleIndex, ARRAY);

                boolean condOk;

                try {
                    condOk = ruleBase.checkCondForDeclaration(ruleIndex,
                            numberInputs - 1);
                } catch (Exception e) {
                    condOk = false;
                }

                if (condOk) {
                    for (int j = 0; j < numberInputs; j++) {
                        propagate(ARRAY[j], j);
                    }
                }
            }
        } else if (input == (numberInputs - 2)) {
            int aux = numberInputs - 1;
            int column = waitingObjects[0].size() - 1;

            for (int j = 0; j < aux; j++) {
                ARRAY[j] = waitingObjects[j].get(column);
            }

            for (Iterator i = waitingObjects[aux].iterator(); i.hasNext();) {
                ARRAY[aux] = i.next();
                ruleBase.setObjects(ruleIndex, ARRAY);

                boolean condOk;

                try {
                    condOk = ruleBase.checkCondForDeclaration(ruleIndex,
                            numberInputs - 1);
                } catch (Exception e) {
                    condOk = false;
                }

                if (condOk) {
                    for (int j = 0; j < numberInputs; j++) {
                        propagate(ARRAY[j], j);
                    }
                }
            }
        }
    }

    /**
     * Remove the given object from the memory of this node.
     *
     * @param obj the object to be removed.
     */
    public void remove(Object obj) {
        final int numberInputs = getNumberInputs();
        List l = waitingObjects[numberInputs - 1];
        l.remove(obj);

        for (int i = 0; i < (numberInputs - 1); i++) {
            its[i] = waitingObjects[i].iterator();
        }

        for (; its[0].hasNext();) {
            boolean toBeRemoved = false;

            for (int i = 0; i < (numberInputs - 1); i++) {
                ARRAY[i] = its[i].next();

                if (obj.equals(ARRAY[i])) {
                    toBeRemoved = true;
                }
            }

            if (toBeRemoved) {
                for (int i = 0; i < (numberInputs - 1); i++) {
                    its[i].remove();
                }
            }
        }
    }

    /**
     * Returns a string representation of this object. Useful for
     * debugging.
     *
     * @return a string representation of this object.
     */
    public String toString() {
        return ("JoinReteNode[ruleIndex=" + ruleIndex + ",declIndex=" +
        declIndex + "]");
    }
}
