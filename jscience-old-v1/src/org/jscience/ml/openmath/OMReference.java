/*
 * $Id: OMReference.java,v 1.3 2007-10-23 18:21:21 virtualcall Exp $
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
import java.util.Hashtable;


/**
 * Models a OpenMath reference.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 *
 * @see "The OpenMath standard 2.0, 3.1.1 and 3.1.2"
 */
public class OMReference extends OMObject {
/**
     * Constructor. <p>
     */
    public OMReference() {
        super();
    }

/**
     * Constructor. <p>
     *
     * @param href the href. <p>
     */
    public OMReference(String href) {
        super();
        setAttribute("href", href);
    }

    /**
     * Create a clone of this OMReference.<p></p>
     *
     * @return the clone.
     */
    public Object clone() {
        OMReference clone = new OMReference();
        clone.attributes = (Hashtable) attributes.clone();

        return clone;
    }

    /**
     * Create a copy of this OMReference.<p></p>
     *
     * @return the copy.
     */
    public Object copy() {
        OMReference copy = new OMReference();
        copy.attributes = new Hashtable();

        Enumeration enumeration = attributes.keys();

        for (; enumeration.hasMoreElements();) {
            String key = (String) enumeration.nextElement();
            String value = (String) attributes.get(key);
            copy.attributes.put(new String(key), new String(value));
        }

        return copy;
    }

    /**
     * Get the type of the OpenMath object.<p></p>
     *
     * @return the type.
     */
    public String getType() {
        return "OMR";
    }

    /**
     * Return if this is an atom object.<p></p>
     *
     * @return <b>true</b>
     */
    public boolean isAtom() {
        return true;
    }

    /**
     * Return if this a composite object.<p></p>
     *
     * @return <b>false</b>
     */
    public boolean isComposite() {
        return false;
    }

    /**
     * Return if the given is the same as this object.<p></p>
     *
     * @param object DOCUMENT ME!
     *
     * @return <b>true</b> if same object, <b>false</b> if not.
     */
    public boolean isSame(OMObject object) {
        if (object instanceof OMReference) {
            OMReference reference = (OMReference) object;

            return reference.getHref().equals(getHref());
        }

        return false;
    }

    /**
     * Return if this object is a valid object.<p></p>
     *
     * @return <b>true</b> if object is valid, <b>false</b> if not.
     */
    public boolean isValid() {
        if (getAttribute("href") != null) {
            return true;
        }

        return false;
    }

    /**
     * Get the href.<p></p>
     *
     * @return the href of the reference.
     */
    public String getHref() {
        return (String) getAttribute("href");
    }

    /**
     * Set the href.<p></p>
     *
     * @param href the href of the reference.
     */
    public void setHref(String href) {
        setAttribute("href", href);
    }

    /**
     * toString.<p></p>
     *
     * @return the string representation of the object. <p>
     */
    public String toString() {
        return "<OMR href=\"" + getAttribute("href") + "\"/>";
    }
}
