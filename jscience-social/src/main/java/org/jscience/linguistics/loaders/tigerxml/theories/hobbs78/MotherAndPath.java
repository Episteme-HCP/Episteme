/*
 * MotherAndPath.java
 *
 * Created in September, 2003
 */

package org.jscience.linguistics.loaders.tigerxml.theories.hobbs78;

import org.jscience.linguistics.loaders.tigerxml.GraphNode;
import org.jscience.linguistics.loaders.tigerxml.NT;
import org.jscience.linguistics.loaders.tigerxml.tools.GeneralTools;
import org.jscience.linguistics.loaders.tigerxml.tools.SyntaxTools;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a helper class for Hobbs78.
 * A MotherAndPath object consists of an NT, the mother, and one of its daughters, the path.
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
