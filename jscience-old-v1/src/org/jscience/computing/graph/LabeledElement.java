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

/* -------------------
 * LabeledElement.java
 * -------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: LabeledElement.java,v 1.3 2007-10-23 18:16:25 virtualcall Exp $
 *
 * Changes
 * -------
 * 24-Jul-2003 : Initial revision (BN);
 *
 */
package org.jscience.computing.graph;

/**
 * An graph element (vertex or edge) that can have a label.
 *
 * @author Barak Naveh
 * @since Jul 14, 2003
 */
public interface LabeledElement {
    /**
     * Sets the specified label object to this element.
     *
     * @param label a label to set to this element.
     */
    public void setLabel(Object label);

    /**
     * Returns the element's label, or <code>null</code> if element has
     * no label.
     *
     * @return the element's label, or <code>null</code> if element has no
     *         label.
     */
    public Object getLabel();

    /**
     * Tests if this element has a label.
     *
     * @return <code>true</code> if the element has a label, otherwise
     *         <code>false</code>.
     */
    public boolean hasLabel();
}
