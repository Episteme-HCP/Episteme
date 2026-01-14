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
