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
 * Models an OpenMath byte array.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 *
 * @see "The OpenMath standard 2.0, 2.1.1"
 */
public class OMByteArray extends OMObject {
    /**
     * Stores the byte array.<p></p>
     */
    protected byte[] byteArray = null;

/**
     * Constructor. <p>
     */
    public OMByteArray() {
        super();
    }

/**
     * Constructor. <p>
     */
    public OMByteArray(String newByteArray) {
        super();
        setByteArray(newByteArray);
    }

    /**
     * Gets the type.<p></p>
     *
     * @return the type.
     */
    public String getType() {
        return "OMB";
    }

    /**
     * Sets the byte array.<p></p>
     *
     * @param newByteArray the byte array to set (as string).
     */
    public void setByteArray(String newByteArray) {
        byteArray = newByteArray.getBytes();
    }

    /**
     * Sets the byte array.<p></p>
     *
     * @param newByteArray the byte array to set (as byte[]).
     */
    public void setByteArray(byte[] newByteArray) {
        byteArray = newByteArray;
    }

    /**
     * Gets the byte array.<p></p>
     *
     * @return the byte array.
     */
    public byte[] getByteArray() {
        return byteArray;
    }

    /**
     * Get the byte array as string.<p></p>
     *
     * @return the byte array (as string).
     */
    public String getByteArrayAsString() {
        if (byteArray != null) {
            return new String(byteArray);
        }

        return null;
    }

    /**
     * Is this an atom object.<p></p>
     *
     * @return <b>true</b> because we are an atom.
     */
    public boolean isAtom() {
        return true;
    }

    /**
     * Is this a composite object.<p></p>
     *
     * @return <b>false</b> because we are not composite.
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
        return "<OMB>" + getByteArrayAsString() + "</OMB>";
    }

    /**
     * Clones the object (shallow copy).
     *
     * @return the shallow copy.
     */
    public Object clone() {
        OMByteArray array = new OMByteArray();
        array.byteArray = this.byteArray;

        Enumeration keys = attributes.keys();
        Enumeration values = attributes.elements();

        for (; keys.hasMoreElements();) {
            String key = (String) keys.nextElement();
            String value = (String) values.nextElement();

            array.setAttribute(key, value);
        }

        return array;
    }

    /**
     * Copies the object (full copy).<p></p>
     *
     * @return the full copy.
     */
    public Object copy() {
        OMByteArray array = new OMByteArray();
        array.byteArray = this.byteArray;

        Enumeration keys = attributes.keys();
        Enumeration values = attributes.elements();

        for (; keys.hasMoreElements();) {
            String key = (String) keys.nextElement();
            String value = (String) values.nextElement();

            array.setAttribute(key, value);
        }

        return array;
    }

    /**
     * Determines if this is the same object.<p></p>
     *
     * @param object the object to test against.
     *
     * @return <b>true</b> if it is the same, <b>false</b> otherwise.
     */
    public boolean isSame(OMObject object) {
        if (object instanceof OMByteArray) {
            OMByteArray bytearray1 = (OMByteArray) object;

            if (bytearray1.byteArray.length == byteArray.length) {
                for (int i = 0; i < byteArray.length; i++) {
                    if (byteArray[i] != bytearray1.byteArray[i]) {
                        return false;
                    }
                }

                return true;
            }
        }

        return false;
    }

    /**
     * Determines if this is a valid object.<p></p>
     *
     * @return <b>true</b> if it is valid, <b>false</b> otherwise.
     */
    public boolean isValid() {
        if (byteArray != null) {
            return true;
        }

        return false;
    }
}
