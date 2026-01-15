/*
 * $Id: OMSymbol.java,v 1.3 2007-10-23 18:21:22 virtualcall Exp $
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
