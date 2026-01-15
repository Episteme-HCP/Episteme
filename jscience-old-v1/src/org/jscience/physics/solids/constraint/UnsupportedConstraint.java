/*
 * NodeConstraint.java
 *
 * Created on December 31, 2004, 12:23 AM
 */
package org.jscience.physics.solids.constraint;

import org.jdom.Element;

import org.jscience.physics.solids.*;


/**
 * Constrains a nodes motion in the specified degrees of freedom.
 *
 * @author Wegge
 */
public class UnsupportedConstraint extends AtlasConstraint {
    /**
     * DOCUMENT ME!
     */
    protected static String TYPE = "Unsupported Constraint";

    /**
     * DOCUMENT ME!
     */
    private AtlasNode[] node;

    /**
     * DOCUMENT ME!
     */
    private int[] cdof;

/**
     * Constrains the specified node in the degree of freedom. The degree of freedom to be
     * constrained should be an integer.
     */
    public UnsupportedConstraint(String id, AtlasNode nodes, int dof) {
        setId(id);
        node = new AtlasNode[1];
        node[0] = nodes;
        cdof = new int[1];
        cdof[0] = dof;
    }

/**
     * Constrains the specified node in the degree of freedom. The degree of freedom to be
     * constrained should be an integer.
     */
    public UnsupportedConstraint(String id, AtlasNode nodes, int[] dof) {
        setId(id);
        node = new AtlasNode[1];
        node[0] = nodes;
        cdof = dof;
    }

/**
     * Constrains the specified node in the degrees of freedom.
     */
    public UnsupportedConstraint(String id, AtlasNode[] node, int[] dof) {
        setId(id);
        this.node = node;
        this.cdof = dof;
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     */
    public void contributeConstraint(SolutionMatrices m) {
        for (int j = 0; j < node.length; j++) {
            for (int i = 0; i < cdof.length; i++) {
                m.setUnsupportedDOF(node[j], cdof[i]);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getType() {
        return TYPE;
    }

    /**
     * Returns the node that is being constrained.
     *
     * @return DOCUMENT ME!
     */
    public AtlasNode[] getNode() {
        return node;
    }

    /**
     * Returns which degrees of freedom are constrained.
     *
     * @return DOCUMENT ME!
     */
    public int[] getConstrainedDOF() {
        return cdof;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String nstr = "";
        String dstr = "";
        String nLabel = "";
        String dLabel = "";
        String ret = getType() + " " + getId() + " : ";

        for (int i = 0; i < node.length; i++) {
            nstr = nstr + node[i];
        }

        for (int i = 0; i < cdof.length; i++) {
            dstr = dstr + cdof[i];
        }

        if (node.length > 1) {
            nLabel = " Nodes = ";
        } else {
            nLabel = " Node = ";
        }

        if (cdof.length > 1) {
            dLabel = " DOFs = ";
        } else {
            dLabel = " DOF = ";
        }

        ret = ret + nLabel + nstr + dLabel + dstr;

        return ret;
    }

    /**
     * Marshalls object to XML.
     *
     * @return DOCUMENT ME!
     */
    public Element loadJDOMElement() {
        Element ret = new Element(this.getClass().getName());
        ret.setAttribute("Id", this.getId());

        ret.setAttribute("Nodes", AtlasUtils.convertAtlasObjects(node));
        ret.setAttribute("DOF", AtlasUtils.convertIntegers(cdof));

        return ret;
    }

    /**
     * Unmarshalls the object from XML.
     *
     * @param parent DOCUMENT ME!
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static AtlasObject unloadJDOMElement(AtlasModel parent, Element e) {
        String id = e.getAttributeValue("Id");

        int[] dof = AtlasUtils.convertStringtoIntegers(e.getAttributeValue(
                    "DOF"));

        String[] nodeIds = AtlasUtils.convertStringtoIds(e.getAttributeValue(
                    "Nodes"));
        AtlasNode[] nodes = new AtlasNode[nodeIds.length];

        for (int i = 0; i < nodeIds.length; i++) {
            nodes[i] = parent.getNode(nodeIds[i]);
        }

        return new UnsupportedConstraint(id, nodes, dof);
    }
}
