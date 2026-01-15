/*
 * $Id: OMString.java,v 1.3 2007-10-23 18:21:21 virtualcall Exp $
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

/**
 * Models an OpenMath string.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 */
public class OMString extends OMObject {
    /**
     * Stores the string.<p></p>
     */
    protected String string = null;

/**
     * Constructor. <p>
     */
    public OMString() {
        super();
    }

/**
     * Constructor. <p>
     *
     * @param newString the Java string to associate with thie OpenMath object.
     */
    public OMString(String newString) {
        super();

        string = newString;
    }

    /**
     * Gets the type.<p></p>
     *
     * @return the type.
     */
    public String getType() {
        return "OMSTR";
    }

    /**
     * Sets the string.<p></p>
     *
     * @param newString the string to set.
     */
    public void setString(String newString) {
        string = newString;
    }

    /**
     * Gets the string.<p></p>
     *
     * @return the string.
     */
    public String getString() {
        return string;
    }

    /**
     * Is this an atom object.<p></p>
     *
     * @return <b>true</b> because it is an atom.
     */
    public boolean isAtom() {
        return true;
    }

    /**
     * Is this a composite object.<p></p>
     *
     * @return <b>false</b> because it is not composite.
     */
    public boolean isComposite() {
        return false;
    }

    /**
     * toString.<p></p>
     *
     * @return the normal string representation.
     */
    public String toString() {
        return "<OMSTR>" + string + "</OMSTR>";
    }

    /**
     * Clone the object (shallow copy).
     *
     * @return the shallow copy.
     */
    public Object clone() {
        OMString cloneString = new OMString();
        cloneString.string = new String(string);

        return cloneString;
    }

    /**
     * Copies the object (deep copy).
     *
     * @return the deep copy
     */
    public Object copy() {
        OMString copyString = new OMString();
        copyString.string = new String(string);

        return copyString;
    }

    /**
     * Determines if this is the same object.<p></p>
     *
     * @param object the object to test against.
     *
     * @return <b>true</b> if it is the same, <b>false</b> if it is not.
     */
    public boolean isSame(OMObject object) {
        if (object instanceof OMString) {
            OMString string = (OMString) object;

            if (string.string.equals(this.string)) {
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
        if (string != null) {
            return true;
        }

        return false;
    }
}
