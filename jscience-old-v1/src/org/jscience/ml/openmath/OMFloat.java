/*
 * $Id: OMFloat.java,v 1.3 2007-10-23 18:21:21 virtualcall Exp $
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
