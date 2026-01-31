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

package org.jscience.social.linguistics.loaders.tigerxml.theories.hobbs78;

import org.jscience.social.linguistics.loaders.tigerxml.GraphNode;
import org.jscience.social.linguistics.loaders.tigerxml.NT;
import org.jscience.social.linguistics.loaders.tigerxml.tools.GeneralTools;
import org.jscience.social.linguistics.loaders.tigerxml.tools.SyntaxTools;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a helper class for Hobbs78.
 * A MotherAndPath object consists of an NT, the mother, and one of its daughters, the path.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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
     * This method returns the daughters of mother that are located either to the right hand 
     * side or to the left hand side of path.
     */
    public List<GraphNode> getOneSide(boolean left) {
        List<GraphNode> return_array = new ArrayList<>();
        boolean get = left;
        List<GraphNode> daughters = mother.getDaughters();
        daughters = GeneralTools.sortNodes(daughters);
        for (GraphNode current : daughters) {
            if (path.equals(current)) {
                get = (!get);
            } else if (get) {
                return_array.add(current);
            }
        }
        return return_array;
    }

    /**
     * Finds the first NP or S-like node above my_node and returns it as MotherAndPath.
     */
    public static final MotherAndPath getFirstNpOrSLike(GraphNode my_node) {
        MotherAndPath m_p = new MotherAndPath();
        while (my_node != null && my_node.hasMother()) {
            NT my_mother = my_node.getMother();
            m_p.setMother(my_mother);
            m_p.setPath(my_node);
            if (SyntaxTools.isNpLikeNode(my_mother) || SyntaxTools.isSLikeNode(my_mother)) {
                break;
            }
            my_node = my_mother;
        }
        return m_p;
    }
}

