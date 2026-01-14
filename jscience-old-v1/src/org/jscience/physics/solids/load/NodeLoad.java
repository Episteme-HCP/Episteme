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

/*
 * NodeLoad.java
 *
 * Created on December 31, 2004, 12:23 AM
 */
package org.jscience.physics.solids.load;

import org.jdom.Element;

import org.jscience.mathematics.algebraic.matrices.DoubleMatrix;

import org.jscience.physics.solids.*;
import org.jscience.physics.solids.geom.AtlasVector;


/**
 * Defines a point load at a node.  Point load has a translational and a
 * rotaional component.
 *
 * @author Wegge
 */
public class NodeLoad extends AtlasLoad {
    /**
     * DOCUMENT ME!
     */
    protected static String TYPE = "Nodal Force";

    /**
     * DOCUMENT ME!
     */
    private AtlasNode node;

    /**
     * DOCUMENT ME!
     */
    private AtlasVector force;

    /**
     * DOCUMENT ME!
     */
    private AtlasVector moment;

/**
     * Loads the specified node with a force and a moment.
     */
    public NodeLoad(String id, AtlasNode node, AtlasVector force,
        AtlasVector moment) {
        setId(id);
        this.node = node;
        this.force = force;
        this.moment = moment;
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
    public AtlasNode getNode() {
        return node;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String ret = getType() + " " + getId() + " : ";
        ret = ret + " Node = " + this.getNode().getId() + " Force=( ";

        double[] val = force.getGlobalDirection();
        ret = ret + val[0] + " , " + val[1] + " , " + val[2] + " ) Moment=( ";
        val = moment.getGlobalDirection();
        ret = ret + val[0] + " , " + val[1] + " , " + val[2] + " )";

        return ret;
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     */
    public void contributeLoad(SolutionMatrices m) {
        DoubleMatrix R = m.getForceMatrix();

        double[] f = force.getGlobalDirection();

        for (int i = 0; i < 3; i++) {
            int ind = m.getDOFIndex(this.getNode(), i + 1);
            double value = R.getPrimitiveElement(ind, 0);
            value = value + f[i];
            R.setElement(ind, 0, value);
        }

        f = moment.getGlobalDirection();

        for (int i = 0; i < 3; i++) {
            int ind = m.getDOFIndex(this.getNode(), i + 4);
            double value = R.getPrimitiveElement(ind, 0);
            value = value + f[i];
            R.setElement(ind, 0, value);
        }
    }

    /**
     * Marshalls object to XML.
     *
     * @return DOCUMENT ME!
     */
    public Element loadJDOMElement() {
        Element ret = new Element(this.getClass().getName());
        ret.setAttribute("Id", this.getId());

        ret.setAttribute("Node", node.getId());
        ret.setAttribute("Force",
            AtlasUtils.convertDoubles(force.getGlobalDirection()));
        ret.setAttribute("Moment",
            AtlasUtils.convertDoubles(moment.getGlobalDirection()));

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

        String nodeId = e.getAttributeValue("Node");
        AtlasNode node = parent.getNode(nodeId);

        double[] f = AtlasUtils.convertStringtoDoubles(e.getAttributeValue(
                    "Force"));
        double[] m = AtlasUtils.convertStringtoDoubles(e.getAttributeValue(
                    "Moment"));

        AtlasVector force = new AtlasVector(f[0], f[1], f[2]);
        AtlasVector moment = new AtlasVector(m[0], m[1], m[2]);

        return new NodeLoad(id, node, force, moment);
    }
}
