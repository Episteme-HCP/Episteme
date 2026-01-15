/*
 * $Id: OMVariable.java,v 1.3 2007-10-23 18:21:22 virtualcall Exp $
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
