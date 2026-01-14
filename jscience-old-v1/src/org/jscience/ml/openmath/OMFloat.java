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


/**
 * Models an OpenMath float.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 *
 * @see "The OpenMath standard 2.0, 2.1.1"
 */
public class OMFloat extends OMObject {
/**
     * Constructor. <p>
     */
    public OMFloat() {
        super();
    }

/**
     * Constructor. <p>
     *
     * @param newFloat the float.
     * @param newBase  the base.
     */
    public OMFloat(String newFloat, String newBase) {
        super();

        if (newBase.equals("dec")) {
            setAttribute("dec", newFloat);
        } else {
            setAttribute("hex", newFloat);
        }
    }

    /**
     * Gets the type.<p></p>
     *
     * @return the type.
     */
    public String getType() {
        return "OMF";
    }

    /**
     * Sets the float.<p></p>
     *
     * @param newFloat the float to set.
     */
    public void setFloat(String newFloat) {
        if (getAttribute("hex") != null) {
            setAttribute("hex", newFloat);
            removeAttribute("dec");
        } else {
            removeAttribute("hex");
            setAttribute("dec", newFloat);
        }
    }

    /**
     * Sets the float.<p></p>
     *
     * @param newFloat the float.
     * @param newBase the base.
     */
    public void setFloat(String newFloat, String newBase) {
        if (newBase.equals("dec")) {
            setAttribute("dec", newFloat);
        } else {
            setAttribute("hex", newFloat);
        }
    }

    /**
     * Gets the float.<p></p>
     *
     * @return the float.
     */
    public String getFloat() {
        if (getAttribute("dec") != null) {
            return (String) getAttribute("dec");
        } else {
            return (String) getAttribute("hex");
        }
    }

    /**
     * Sets the base.<p></p>
     *
     * @param newBase the base.
     */
    public void setBase(String newBase) {
        if (newBase.equals("hex") || newBase.equals("dec")) {
            setAttribute(newBase, "");
        }
    }

    /**
     * Get the base.<p></p>
     *
     * @return the base.
     */
    public String getBase() {
        if (getAttribute("dec") != null) {
            return "dec";
        }

        if (getAttribute("hex") != null) {
            return "hex";
        }

        return null;
    }

    /**
     * Returns the float as a double.<p></p>
     *
     * @return the float (as double).
     *
     * @throws NumberFormatException DOCUMENT ME!
     */
    public double doubleValue() {
        if (getAttribute("dec") != null) {
            return new Double((String) getAttribute("dec")).doubleValue();
        }

        throw new NumberFormatException();
    }

    /**
     * Returns the float as a float.<p></p>
     *
     * @return the float (as float).
     *
     * @throws NumberFormatException DOCUMENT ME!
     */
    public float floatValue() {
        if (getAttribute("dec") != null) {
            return new Float((String) getAttribute("dec")).floatValue();
        }

        throw new NumberFormatException();
    }

    /**
     * Is this an atom object.<p></p>
     *
     * @return <b>true</b> because this is an atom.
     */
    public boolean isAtom() {
        return true;
    }

    /**
     * Is this a composite object.<p></p>
     *
     * @return <b>false</b> because this is not a composite.
     */
    public boolean isComposite() {
        return false;
    }

    /**
     * Returns a string representation of the object.<p></p>
     *
     * @return the string representation.
     */
    public String toString() {
        if (getAttribute("dec") != null) {
            return "<OMF dec=\"" + getAttribute("dec") + "\"/>";
        } else {
            return "<OMF hex=\"" + getAttribute("hex") + "\"/>";
        }
    }

    /**
     * Clones the object (shallow copy).
     *
     * @return a shallow copy.
     */
    public Object clone() {
        OMFloat cloneFloat = new OMFloat();

        Enumeration keys = attributes.keys();
        Enumeration values = attributes.elements();

        for (; keys.hasMoreElements();) {
            String key = (String) keys.nextElement();
            String value = (String) values.nextElement();

            cloneFloat.setAttribute(key, value);
        }

        return cloneFloat;
    }

    /**
     * Copies the object (full copy).<p></p>
     *
     * @return a deep copy.
     */
    public Object copy() {
        OMFloat copyFloat = new OMFloat();

        Enumeration keys = attributes.keys();
        Enumeration values = attributes.elements();

        for (; keys.hasMoreElements();) {
            String key = (String) keys.nextElement();
            String value = (String) values.nextElement();

            copyFloat.setAttribute(key, value);
        }

        return copyFloat;
    }

    /**
     * Determines if this is the same object.<p></p>
     *
     * @param object the object to test against.
     *
     * @return <b>true</b> if it is the same, <b>false</b> if it is not.
     */
    public boolean isSame(OMObject object) {
        if (object instanceof OMFloat) {
            OMFloat flt = (OMFloat) object;

            if ((flt.getAttribute("dec") != null) &&
                    flt.getAttribute("dec").equals(getAttribute("dec"))) {
                return true;
            }

            if ((flt.getAttribute("hex") != null) &&
                    flt.getAttribute("hex").equals(getAttribute("hex"))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if this is a valid object.<p></p>
     *
     * @return <b>true</b> if it is valid, <b>false</b> if it is not.
     */
    public boolean isValid() {
        if ((getAttribute("hex") != null) || (getAttribute("dec") != null)) {
            return true;
        }

        return false;
    }
}
