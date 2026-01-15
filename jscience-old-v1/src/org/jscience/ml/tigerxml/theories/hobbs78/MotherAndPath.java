/*
 * MotherAndPath.java
 *
 * Created in September, 2003
 *
 * Copyright (C) 2003 Hajo Keffer <hajokeffer@coli.uni-sb.de>
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

package org.jscience.ml.tigerxml.theories.hobbs78;

import org.jscience.ml.tigerxml.GraphNode;
import org.jscience.ml.tigerxml.NT;
import org.jscience.ml.tigerxml.tools.GeneralTools;
import org.jscience.ml.tigerxml.tools.SyntaxTools;

import java.util.ArrayList;

/**
 * This class is a helper class for Hobbs78.
 * <p/>
 * A MotherAndPath object consists of an NT, the
 * mother, and one of its daughters, the path.
 *
 * @author <a href="mailto:hajokeffer@coli.uni-sb.de">Hajo Keffer</a>
 * @version 1.84
 *          $Id: MotherAndPath.java,v 1.2 2007-10-21 17:40:33 virtualcall Exp $
 */

public class MotherAndPath {
    private NT mother;
    private GraphNode path;

    public MotherAndPath() {
        mother = null;
        path = null;
    }

    public void setMother(NT node) {
        mother = node;
    }

    public NT getMother() {
        return mother;
    }

    public GraphNode getPath() {
        return path;
    }

    public void setPath(GraphNode node) {
        path = node;
    }

    /**
     * This method returns the daughters of mother
     * that are located either to the right hand
     * side or to the left hand side of path.
     * If the parameter left is set to true it returns
     * the left hand side daughters.
     *
     * @param left If true: return the left hand side daughters, otherwise the
     *             right hand side daughters.
     * @return The daughters of this MotherAndPath that are on the right/left
     *         hand side of the path.
     */
    public ArrayList getOneSide(boolean left) {
        ArrayList return_array = new ArrayList();
        boolean get = left;
        ArrayList daughters = mother.getDaughters();
        daughters = GeneralTools.sortNodes(daughters);
        for (int i = 0;
             i < daughters.size();
             i++) {
            GraphNode current =
                    (GraphNode) daughters.get(i);
            if (path.equals(current)) {
                get = (!get);
            } else if (get) {
                return_array.add(current);
            }
        } // for i
        return return_array;
    }

    /**
     * This static method returns a MotherAndPath
     * Object that corresponds to the first np or
     * s-like node above my_node.
     * The mother node corresponds to the np or
     * s-like node in question and the path node
     * corresponds to the path that leads from my_node
     * to mother.
     *
     * @param my_node The node for which to find a corresponding MotherAndPath.
     * @return The MotherAndPath that corresponds to the first np or s-like node
     *         above my_node.
     */
    public static final MotherAndPath getFirstNpOrSLike(GraphNode my_node) {
        MotherAndPath m_p = new MotherAndPath();
        boolean cont = true;
        while (cont) {
            NT my_mother =
                    my_node.getMother();
            m_p.setMother(my_mother);
            m_p.setPath(my_node);
            if ((SyntaxTools.isNpLikeNode(my_mother)) ||
                    (SyntaxTools.isSLikeNode(my_mother))) {
                cont = false;
            }
            my_node = my_mother;
        }
        return m_p;
    } // method getFirstNpOrSLike
} // class MotherAndPath
