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
 * NodeConstraint.java
 *
 * Created on December 31, 2004, 12:23 AM
 */
package org.jscience.physics.solids.constraint;

import org.jscience.physics.solids.AtlasConstraint;
import org.jscience.physics.solids.AtlasNode;
import org.jscience.physics.solids.SolutionMatrices;


/**
 * Constrains a nodes motion in the specified degrees of freedom.
 *
 * @author Wegge
 */
public class EnforcedDisplacement extends AtlasConstraint {
    /**
     * DOCUMENT ME!
     */
    protected static String TYPE = "Enforced Displacement";

    /**
     * DOCUMENT ME!
     */
    private AtlasNode[] node;

    /**
     * DOCUMENT ME!
     */
    private int[] cdof;

    /**
     * DOCUMENT ME!
     */
    private double value;

/**
     * Constrains the specified node in the degree of freedom. The degree of freedom to be
     * constrained should be an integer.
     */
    public EnforcedDisplacement(String id, AtlasNode nodes, int dof, double val) {
        setId(id);
        node = new AtlasNode[1];
        node[0] = nodes;
        cdof = new int[1];
        cdof[0] = dof;
        this.value = val;
    }

/**
     * Constrains the specified node in the degrees of freedom.
     */
    public EnforcedDisplacement(String id, AtlasNode[] node, int[] dof,
        double val) {
        setId(id);
        this.node = node;
        this.cdof = dof;
        this.value = val;
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     */
    public void contributeConstraint(SolutionMatrices m) {
        for (int j = 0; j < node.length; j++) {
            for (int i = 0; i < cdof.length; i++) {
                m.setConstrainedDOF(node[j], cdof[i], value);
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

        ret = ret + nLabel + nstr + dLabel + dstr + value;

        return ret;
    }
}
