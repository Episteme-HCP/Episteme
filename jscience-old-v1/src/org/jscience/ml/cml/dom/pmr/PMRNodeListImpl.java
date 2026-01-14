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

package org.jscience.ml.cml.dom.pmr;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

/**
 * The <code>NodeList</code> interface provides the abstraction of an ordered
 * collection of nodes, without defining or constraining how this collection
 * is implemented.
 * <p>The items in the <code>NodeList</code> are accessible via an integral
 * index, starting from 0.
 */
public class PMRNodeListImpl implements NodeList {

    protected ArrayList<Node> nodes;

    public PMRNodeListImpl(NodeList nodeList) {
// copy of node list, probably mistake
        nodes = new ArrayList<Node>();
        int l = nodeList.getLength();
        for (int i = 0; i < l; i++) {
            nodes.add(nodeList.item(i));
        }
    }

    public PMRNodeListImpl(ArrayList<Node> l) {
// nodes is a live list, so shallow copy
        nodes = (l == null) ? new ArrayList<Node>() : l;
/*--
        if (nodes != null) {
            int l = nodes.size();
            for (int i = 0; i < l; i++) {
//                System.out.println(nodes.elementAt(i).getClass());
                this.nodeVector.addElement(nodes.elementAt(i));
            }
        }
--*/
    }

    /**
     * Returns the <code>index</code>th item in the collection. If
     * <code>index</code> is greater than or equal to the number of nodes in
     * the list, this returns <code>null</code>.
     *
     * @param index Index into the collection.
     * @return The node at the <code>index</code>th position in the
     *         <code>NodeList</code>, or <code>null</code> if that is not a valid
     *         index.
     */
    public Node item(int index) {
        int l = nodes.size();
        return (index < 0 || index >= l) ? null : nodes.get(index);
    }

    /**
     * The number of nodes in the list. The range of valid child node indices is
     * 0 to <code>length-1</code> inclusive.
     */
    public int getLength() {
        return nodes.size();
    }

}

