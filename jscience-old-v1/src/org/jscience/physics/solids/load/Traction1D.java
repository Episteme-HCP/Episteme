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
