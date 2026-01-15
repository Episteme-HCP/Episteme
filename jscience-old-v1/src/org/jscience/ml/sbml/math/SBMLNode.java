package org.jscience.ml.sbml.math;

import java.util.Vector;


/**
 * A class representing a node in a parse tree. This code is licensed under
 * the DARPA BioCOMP Open Source License.  See LICENSE for more details.
 *
 * @author Marc Vass
 * @author Nicholas Allen
 */
public class SBMLNode {
    /** DOCUMENT ME! */
    public MathElement element;

    /** DOCUMENT ME! */
    public SBMLNode parent;

    /** DOCUMENT ME! */
    public Vector children = new Vector();

/**
     * Creates a new instance of SBMLNode
     *
     * @param parent DOCUMENT ME!
     */
    public SBMLNode(SBMLNode parent) {
        this.parent = parent;
    }

/**
     * DOCUMENT ME!
     *
     * @param parent
     * @param m
     */
    public SBMLNode(SBMLNode parent, MathElement m) {
        this.parent = parent;
        this.element = m;
    }

/**
     * DOCUMENT ME!
     *
     * @param m
     */
    public SBMLNode(MathElement m) {
        this.parent = null;
        this.element = m;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n
     */
    public void addChild(SBMLNode n) {
        children.add(n);
    }

    /**
     * Getter for property element.
     *
     * @return Value of property element.
     */
    public MathElement getElement() {
        return element;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public SBMLNode getParent() {
        return parent;
    }

    /**
     * DOCUMENT ME!
     *
     * @param m
     */
    public void setElement(MathElement m) {
        element = m;
    }
}
