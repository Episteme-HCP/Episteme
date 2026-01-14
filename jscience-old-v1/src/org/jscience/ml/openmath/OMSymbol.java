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
 * Models an OpenMath symbol.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 *
 * @see "The OpenMath standard 2.0, 2.1.1"
 */
public class OMSymbol extends OMObject {
/**
     * Constructor. <p>
     */
    public OMSymbol() {
        super();
    }

/**
     * Constructor. <p>
     *
     * @param cd   the CD of the symbol.
     * @param name the name of the symbol.
     */
    public OMSymbol(String cd, String name) {
        super();

        attributes.put("cd", cd);
        attributes.put("name", name);
    }

    /**
     * Gets the CD for this OpenMath symbol.<p></p>
     *
     * @return the CD of the symbol.
     *
     * @deprecated use the getCd method instead.
     */
    public String getCD() {
        return getCd();
    }

    /**
     * Gets the CD for this OpenMath symbol.<p></p>
     *
     * @return the CD of the symbol, or <b>null</b> if not set.
     */
    public String getCd() {
        if (attributes.get("cd") != null) {
            return (String) attributes.get("cd");
        }

        return null;
    }

    /**
     * Sets the CD for this OpenMath symbol.<p></p>
     *
     * @param cd the CD of the symbol.
     */
    public void setCD(String cd) {
        attributes.put("cd", cd);
    }

    /**
     * Gets the name for this OpenMath symbol.<p></p>
     *
     * @return the name of the symbol, or <b>null</b> if not set.
     */
    public String getName() {
        if (attributes.get("name") != null) {
            return (String) attributes.get("name");
        }

        return null;
    }

    /**
     * Sets the name for this OpenMath symbol.<p></p>
     *
     * @param name the name of the symbol.
     */
    public void setName(String name) {
        attributes.put("name", name);
    }

    /**
     * Gets the type.<p></p>
     *
     * @return the type of the symbol.
     */
    public String getType() {
        return "OMS";
    }

    /**
     * Is this an atom object.<p></p>
     *
     * @return <b>true</b> if this is an atom object, <b>false</b> if it is
     *         not.
     */
    public boolean isAtom() {
        return true;
    }

    /**
     * Is this a composite object.<p></p>
     *
     * @return <b>true</b> if this is a composite object, <b>false</b> if it is
     *         not.
     */
    public boolean isComposite() {
        return false;
    }

    /**
     * Returns a string representation of the object.<p></p>
     *
     * @return the string representation of the object.
     */
    public String toString() {
        return "<OMS cd=\"" + attributes.get("cd") + "\" name=\"" +
        attributes.get("name") + "\"/>";
    }

    /**
     * Clones the object (shallow copy).
     *
     * @return the cloned object.
     */
    public Object clone() {
        OMSymbol symbol = new OMSymbol();
        symbol.attributes = (Hashtable) attributes.clone();

        return symbol;
    }

    /**
     * Copies the object (full copy).
     *
     * @return the copied object.
     */
    public Object copy() {
        OMSymbol symbol = new OMSymbol();
        Enumeration keys = attributes.keys();
        Enumeration values = attributes.elements();

        for (; keys.hasMoreElements();) {
            String key = (String) keys.nextElement();
            String value = (String) values.nextElement();

            symbol.setAttribute(key, value);
        }

        return symbol;
    }

    /**
     * Determines if this is the same object.<p></p>
     *
     * @param object the object to test against.
     *
     * @return <b>true</b> if this is semantically the same object,
     *         <b>false</b> if it is not.
     */
    public boolean isSame(OMObject object) {
        if (object instanceof OMSymbol) {
            OMSymbol symbol = (OMSymbol) object;

            if (getCd().equals(symbol.getCd()) &&
                    getName().equals(symbol.getName())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if this object is valid.<p></p>
     *
     * @return <b>true</b> if this is a valid object, <b>false</b> if it is
     *         not.
     */
    public boolean isValid() {
        if ((getCd() != null) && (getName() != null)) {
            return true;
        }

        return false;
    }
}
