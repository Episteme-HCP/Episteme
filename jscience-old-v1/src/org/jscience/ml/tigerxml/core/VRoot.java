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

package org.jscience.ml.tigerxml.core;

import org.jscience.ml.tigerxml.GraphNode;
import org.jscience.ml.tigerxml.NT;
import org.jscience.ml.tigerxml.Sentence;
import org.jscience.ml.tigerxml.tools.GeneralTools;

import java.io.Serializable;

import java.util.ArrayList;


/**
 * Represents the virtual root node in a syntax tree. This is a restricted
 * <tt>NT</tt> with some peculiarities.
 *
 * @author <a href="mailto:oeze@coli.uni-sb.de">Oezguer Demir</a>
 * @author <a href="mailto:vicky@coli.uni-sb.de">Vaclav Nemcik</a>
 * @author <a href="mailto:hajokeffer@coli.uni-sb.de">Hajo Keffer</a>
 * @version 1.84 $Id: VRoot.java,v 1.3 2007-10-23 18:21:42 virtualcall Exp $
 *
 * @see GraphNode
 * @see NT
 */
public class VRoot extends NT implements Serializable {
/**
     * Creates an empty VRoot setting all member variables to <code>null<code>
     * or empty.
     */
    public VRoot() {
        super();
        this.setMother(null);

        this.setEdge2Mother("");

        this.setCat("VROOT");

        this.setTerminal(false);
    }

/**
     * Creates a <tt>VRoot</tt>, collects and links its daughters.
     *
     * @param sent The sentence in which to insert this <tt>VRoot</tt>.
     */
    public VRoot(Sentence sent) {
        super();
        this.setMother(null);
        this.setEdge2Mother("");
        this.setCat("VROOT");
        this.setId(sent.getId() + "_" + "VROOT");
        this.setSentence(sent);
        this.setTerminal(false);

        // link all nodes without a mother to this (virtual root) node
        ArrayList allNodes = GeneralTools.sortNodes(sent.getAllGraphNodes());

        for (int i = 0; i < allNodes.size(); i++) {
            GraphNode currentNode = (GraphNode) allNodes.get(i);

            if (!currentNode.hasMother()) {
                /////FOOcurrentNode.printNodeErr();
                /////System.err.println(currentNode.getSentence());
                this.addDaughter(currentNode);
                currentNode.setMother(this);
                currentNode.setEdge2Mother("");
            } // if
        } // for i
    } // VRoot(sent)
} // class
