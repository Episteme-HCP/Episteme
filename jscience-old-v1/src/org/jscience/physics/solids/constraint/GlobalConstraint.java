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

package org.jscience.physics.solids.constraint;

import org.jdom.Element;

import org.jscience.physics.solids.*;


/**
 * Constrains an entire model's motion in the specified degrees of freedom.
 * The model to be constrained is the owning parent of this AtlasObject.
 *
 * @author Wegge
 */
public class GlobalConstraint extends AtlasConstraint {
    /**
     * DOCUMENT ME!
     */
    protected static String TYPE = "Global Constraint";

    /**
     * DOCUMENT ME!
     */
    private int[] cdof;

/**
     * Constrains the specified model in the degree of freedom. The degree of freedom to be
     * constrained should be an integer.
     */
    public GlobalConstraint(String id, int dof) {
        setId(id);

        cdof = new int[1];
        cdof[0] = dof;
    }

/**
     * Constrains a parent model in the degree of freedom. The degree of freedom to be
     * constrained should be an integer.
     */
    public GlobalConstraint(String id, int[] dof) {
        setId(id);

        cdof = dof;
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     */
    public void contributeConstraint(SolutionMatrices m) {
        if (this.getParentModel() == null) {
            return;
        }

        AtlasNode[] node = this.getParentModel().getNodes();

        for (int j = 0; j < node.length; j++) {
            for (int i = 0; i < cdof.length; i++) {
                m.setConstrainedDOF(node[j], cdof[i], 0.0);
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

        for (int i = 0; i < cdof.length; i++) {
            dstr = dstr + cdof[i];
        }

        if (cdof.length > 1) {
            dLabel = " DOFs = ";
        } else {
            dLabel = " DOF = ";
        }

        ret = ret + nLabel + dLabel + dstr;

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

        return new GlobalConstraint(id, dof);
    }
}
