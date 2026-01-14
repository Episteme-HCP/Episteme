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
 * Models an OpenMath integer.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 *
 * @see "The OpenMath standard 2.0, 2.1.1"
 */
public class OMInteger extends OMObject {
    /**
     * Stores the integer.<p></p>
     */
    protected String integer;

/**
     * Constructor. <p>
     */
    public OMInteger() {
        super();
    }

/**
     * Constructor. <p>
     *
     * @param newInteger the integer to set (as a string).
     */
    public OMInteger(String newInteger) {
        super();

        integer = newInteger;
    }

/**
     * Constructor. <p>
     *
     * @param newLong the long to use for this OpenMath integer.
     */
    public OMInteger(long newLong) {
        super();

        integer = new Long(newLong).toString();
    }

/**
     * Constructor. <p>
     *
     * @param newInteger the int to use for this OpenMath integer.
     */
    public OMInteger(int newInteger) {
        super();

        integer = new Integer(newInteger).toString();
    }

/**
     * Constructor. <p>
     *
     * @param newLong the Long to use for this OpenMath integer.
     */
    public OMInteger(Long newLong) {
        super();

        integer = newLong.toString();
    }

/**
     * Constructor. <p>
     *
     * @param newInteger the Integer to use for this OpenMath integer.
     */
    public OMInteger(Integer newInteger) {
        super();

        integer = newInteger.toString();
    }

    /**
     * Gets the type.<p></p>
     *
     * @return the type.
     */
    public String getType() {
        return "OMI";
    }

    /**
     * Set the integer.<p></p>
     *
     * @param newInteger the integer to set (as string).
     */
    public void setInteger(String newInteger) {
        integer = newInteger;
    }

    /**
     * Set the integer.<p></p>
     *
     * @param newLong the integer to set (as Long).
     */
    public void setInteger(Long newLong) {
        integer = newLong.toString();
    }

    /**
     * Set the integer.<p></p>
     *
     * @param newInteger the integer to set (as Integer).
     */
    public void setInteger(Integer newInteger) {
        integer = newInteger.toString();
    }

    /**
     * Set the integer.<p></p>
     *
     * @param newLong the integer to set (as Long).
     */
    public void setInteger(long newLong) {
        integer = new Long(newLong).toString();
    }

    /**
     * Set the integer.<p></p>
     *
     * @param newInteger the integer to set (as int).
     */
    public void setInteger(int newInteger) {
        integer = new Integer(newInteger).toString();
    }

    /**
     * Get the integer.<p></p>
     *
     * @return the integer (as String).
     */
    public String getInteger() {
        return integer;
    }

    /**
     * Return the integer as a long.<p></p>
     *
     * @return the integer (as long).
     */
    public long longValue() {
        return new Long(integer).longValue();
    }

    /**
     * Returns the integer as an int.<p></p>
     *
     * @return the integer (as int).
     */
    public int intValue() {
        return new Integer(integer).intValue();
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
        return "<OMI>" + integer + "</OMI>";
    }

    /**
     * Clones the object (shallow copy).
     *
     * @return the shallow copy.
     */
    public Object clone() {
        OMInteger cloneInteger = new OMInteger();
        cloneInteger.integer = new String(integer);
        cloneInteger.attributes = attributes;

        return cloneInteger;
    }

    /**
     * Copies the object (full copy).
     *
     * @return the deep copy.
     */
    public Object copy() {
        OMInteger copyInteger = new OMInteger();
        copyInteger.integer = new String(integer);

        Enumeration keys = attributes.keys();
        Enumeration values = attributes.elements();

        for (; keys.hasMoreElements();) {
            String key = (String) keys.nextElement();
            String value = (String) values.nextElement();

            copyInteger.setAttribute(key, value);
        }

        return copyInteger;
    }

    /**
     * Determines if this is the same object.<p></p>
     *
     * @param object the object to test against.
     *
     * @return <b>true</b> if it is the same, <b>false</b> if not.
     */
    public boolean isSame(OMObject object) {
        if (object instanceof OMInteger) {
            OMInteger integer1 = (OMInteger) object;

            if (integer1.integer.equals(integer)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if this object is valid.<p></p>
     *
     * @return <b>true</b> if it is valid, <b>false</b> if it is not.
     */
    public boolean isValid() {
        if (integer != null) {
            return true;
        }

        return false;
    }
}
