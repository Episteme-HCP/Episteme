/*
 * VRoot.java
 *
 * Created on December 17, 2003, 10:48 PM
 *
 * Copyright (C) 2003 Oezguer Demir <oeze@coli.uni-sb.de>,
 *                    Vaclav Nemcik <vicky@coli.uni-sb.de>,
 *                    Hajo Keffer <hajokeffer@coli.uni-sb.de>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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
