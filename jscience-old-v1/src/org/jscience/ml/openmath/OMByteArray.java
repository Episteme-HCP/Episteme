/*
 * $Id: OMByteArray.java,v 1.3 2007-10-23 18:21:21 virtualcall Exp $
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
 *  Alternatively, the contents of this file may be used under the terms
 *  of the GNU Lesser General Public license (the "LGPL license"), in which
 *  case the provisions of the LGPL license are applicable instead of those
 *  above. A copy of the LGPL license is available at http://www.gnu.org
 *
 *  The Original Code is ROML -- the RIACA OpenMath Library. The Initial
 *  Developer of the Original Code is Manfred N. Riem.  Portions created
 *  by Manfred N. Riem are Copyright (c) 2001. All Rights Reserved.
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
