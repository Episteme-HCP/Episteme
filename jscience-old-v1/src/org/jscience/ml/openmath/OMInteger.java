/*
 * $Id: OMInteger.java,v 1.3 2007-10-23 18:21:21 virtualcall Exp $
 *
 * Copyright (c) 2001-2004, RIACA, Technische Universiteit Eindhoven (TU/e).
 * All Rights Reserved.
 *
 * ---------------------------------------------------------------------------
 *
 *  The contents of this file are subject to the RIACA Public License
 *  Version 1.0 (the "License"). You may not use this file except in
 *  compliance with the License. A copy of the License is available at
 *  http://www.riaca.win.tue.nl
 *
 *  The Original Code is ROML -- the RIACA OpenMath Library. The Initial
 *  Developer of the Original Code is Manfred N. Riem.  Portions created
 *  by Manfred N. Riem are Copyright (c) 2001. All Rights Reserved.
 *
 *  Alternatively, the contents of this file may be used under the terms
 *  of the GNU Lesser General Public license (the "LGPL license"), in which
 *  case the provisions of the LGPL license are applicable instead of those
 *  above. A copy of the LGPL license is available at http://www.gnu.org
 *
 *  Contributor(s):
 *
 *      Ernesto Reinaldo Barreiro, Arjeh M. Cohen, Hans Cuypers, Hans Sterk,
 *      Olga Caprotti
 *
 * ---------------------------------------------------------------------------
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
