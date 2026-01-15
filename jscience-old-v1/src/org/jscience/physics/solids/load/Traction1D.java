/*
 * Created on Feb 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jscience.physics.solids.load;

import org.jscience.physics.solids.AreaElement;
import org.jscience.physics.solids.AtlasLoad;
import org.jscience.physics.solids.AtlasNode;
import org.jscience.physics.solids.SolutionMatrices;

/**
 * @author Herbie
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class Traction1D extends AtlasLoad {

    protected static String TYPE = "Traction Force 1D";

    private AreaElement elem;
    private AtlasNode node1;
    private AtlasNode node2;
    private double value1;
    private double value2;

    public Traction1D(AreaElement el, AtlasNode n1, AtlasNode n2, double val1, double val2) {
        this.elem = el;
        this.node1 = n1;
        this.node2 = n2;
        this.value1 = val1;
        this.value2 = val2;
    }

    public Traction1D(AreaElement el, AtlasNode n1, AtlasNode n2, double val) {
        this.elem = el;
        this.node1 = n1;
        this.node2 = n2;
        this.value1 = val;
        this.value2 = val;
    }

    public String getType() {
        return TYPE;
    }

    public void contributeLoad(SolutionMatrices m) {
        elem.contributeTractionLoad(this, m);
    }


    /**
     * @return Returns the elem.
     */
    public AreaElement getElem() {
        return elem;
    }

    /**
     * @param elem The elem to set.
     */
    public void setElem(AreaElement elem) {
        this.elem = elem;
    }

    /**
     * @return Returns the node1.
     */
    public AtlasNode getNode1() {
        return node1;
    }

    /**
     * @param node1 The node1 to set.
     */
    public void setNode1(AtlasNode node1) {
        this.node1 = node1;
    }

    /**
     * @return Returns the node2.
     */
    public AtlasNode getNode2() {
        return node2;
    }

    /**
     * @param node2 The node2 to set.
     */
    public void setNode2(AtlasNode node2) {
        this.node2 = node2;
    }

    /**
     * @return Returns the value1.
     */
    public double getValue1() {
        return value1;
    }

    /**
     * @param value1 The value1 to set.
     */
    public void setValue1(double value1) {
        this.value1 = value1;
    }

    /**
     * @return Returns the value2.
     */
    public double getValue2() {
        return value2;
    }

    /**
     * @param value2 The value2 to set.
     */
    public void setValue2(double value2) {
        this.value2 = value2;
    }
}
