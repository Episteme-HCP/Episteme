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

package org.jscience.ml.openmath;

import java.util.Enumeration;
import java.util.Hashtable;


/**
 * Models an OpenMath variable.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 *
 * @see "The OpenMath standard 2.0, 2.1.1"
 */
public class OMVariable extends OMObject {
/**
     * Constructor. <p>
     */
    public OMVariable() {
        super();
    }

/**
     * Constructor. <p>
     *
     * @param newName set the name.
     */
    public OMVariable(String newName) {
        super();

        setName(newName);
    }

    /**
     * Gets the name.<p></p>
     *
     * @return the name.
     */
    public String getName() {
        if (attributes.get("name") != null) {
            return (String) attributes.get("name");
        }

        return null;
    }

    /**
     * Sets the name.<p></p>
     *
     * @param newName set the variable name.
     */
    public void setName(String newName) {
        attributes.put("name", newName);
    }

    /**
     * Gets the type.<p></p>
     *
     * @return the type of the object.
     */
    public String getType() {
        return "OMV";
    }

    /**
     * Is this an atom object.<p></p>
     *
     * @return if we are an atomic object.
     */
    public boolean isAtom() {
        return true;
    }

    /**
     * Is this a composite object.<p></p>
     *
     * @return if we are a composite object.
     */
    public boolean isComposite() {
        return false;
    }

    /**
     * Returns a string representation of the object.<p></p>
     *
     * @return a string representation.
     */
    public String toString() {
        return "<OMV name=\"" + getName() + "\"/>";
    }

    /**
     * Clones the object (shallow copy).<p></p>
     *
     * @return a shallow copy.
     */
    public Object clone() {
        OMVariable variable = new OMVariable();
        variable.attributes = (Hashtable) attributes.clone();

        return variable;
    }

    /**
     * Copies the object (full copy).<p></p>
     *
     * @return a full copy.
     */
    public Object copy() {
        OMVariable variable = new OMVariable();
        Enumeration keys = attributes.keys();
        Enumeration values = attributes.elements();

        for (; keys.hasMoreElements();) {
            String key = (String) keys.nextElement();
            String value = (String) values.nextElement();

            variable.setAttribute(key, value);
        }

        return variable;
    }

    /**
     * Determines if this is the same object.<p></p>
     *
     * @param object the object to test against.
     *
     * @return <b>true</b> if the object is the same, <b>false</b> if it is
     *         not.
     */
    public boolean isSame(OMObject object) {
        if (object instanceof OMVariable) {
            OMVariable variable = (OMVariable) object;

            return getName().equals(variable.getName());
        }

        return false;
    }

    /**
     * Determines if this is a valid object.<p></p>
     *
     * @return <b>true</b> if the object is valid, <b>false</b> if it is not.
     */
    public boolean isValid() {
        if ((getName() != null) && !getName().equals("")) {
            return true;
        }

        return false;
    }
}
