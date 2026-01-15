/*
 * $Id: OMObject.java,v 1.2 2007-10-21 17:46:57 virtualcall Exp $
 *
 * Copyright (c) 2001-2004, RIACA, Technische Universiteit Eindhoven (TU/e).
 * All Rights Reserved.
 *
 * ---------------------------------------------------------------------------
 *
 *  The contents of this file are subject to the RIACA Public License
 *  Version 1.0 (the "License"). You may not use this file except in
 *  compliance with the License. A copy of the License is available at
 *  http://www.riaca.win.tue.nl/
 *
 *  The Original Code is ROML -- the RIACA OpenMath Library. The Initial
 *  Developer of the Original Code is Manfred N. Riem.  Portions created
 *  by Manfred N. Riem are Copyright (c) 2001. All Rights Reserved.
 *
 *  Alternatively, the contents of this file may be used under the terms
 *  of the GNU Lesser General Public license (the "LGPL license"), in which 
 *  case the provisions of the LGPL license are applicable instead of those 
 *  above. A copy of the LGPL license is available at http://www.gnu.org/
 *
 *  Contributor(s):
 *
 *      Ernesto Reinaldo Barreiro, Arjeh M. Cohen, Hans Cuypers, Hans Sterk,
 *      Olga Caprotti
 *
 * ---------------------------------------------------------------------------
 */
package org.jscience.ml.openmath;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * Models an abstract OpenMath object. <p>
 * <p/>
 * <p/>
 * <i>Note:</i> every other OpenMath object inherits from this object. So each
 * OpenMath object will have a hashtable of attributes.
 * </p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.2 $
 */
public abstract class OMObject implements Serializable, Cloneable {
    /**
     * Stores the attribute table. <p>
     */
    protected Hashtable attributes = new Hashtable();

    /**
     * Constructor. <p>
     */
    public OMObject() {
        super();
    }

    /**
     * Get the type of this element. <p>
     *
     * @return the type.
     */
    public abstract String getType();

    /**
     * Is this an atom. <p>
     *
     * @return <b>true</b> if it is an atom, <b>false</b> if it is not.
     */
    public abstract boolean isAtom();

    /**
     * Is this a composite object. <p>
     *
     * @return <b>true</b> if it is composite, <b>false</b> if it is not.
     */
    public abstract boolean isComposite();

    /**
     * Is this the same object. <p>
     *
     * @param object the object to test against.
     * @return <b>true</b> if it is the same, <b>false</b> if it is not.
     */
    public abstract boolean isSame(OMObject object);

    /**
     * Is this a valid object. <p>
     *
     * @return <b>true</b> if it is valid, <b>false</b> if it is not.
     */
    public abstract boolean isValid();

    /**
     * This will perform a shallow copy of the object.
     *
     * @return a copy of the object.
     */
    public abstract Object clone();

    /**
     * Returns a string representation of the object. <p>
     *
     * @return the string representation.
     */
    public abstract String toString();

    /**
     * Creates a copy of the OMObject. <p>
     *
     * @return a deep copy of the object.
     */
    public abstract Object copy();

    /**
     * Gets the attributes. <p>
     * <p/>
     * <p/>
     * <i>Note</i>: this is an extension to the OpenMath standard 1.0. For the
     * OpenMath standard 2.0 this is needed. So if you want 1.0 compliant code
     * do not use attributes.
     * </p>
     *
     * @return the hashtable with attributes.
     */
    public Hashtable getAttributes() {
        return this.attributes;
    }

    /**
     * Sets the attributes. <p>
     * <p/>
     * <p/>
     * <i>Note</i>: this is an extension to the OpenMath standard 1.0. For the
     * OpenMath standard 2.0 this is needed. So if you want 1.0 compliant code
     * do not use attributes.
     * </p>
     *
     * @param newAttributes the attributes to set.
     */
    public void setAttributes(Hashtable newAttributes) {
        attributes = newAttributes;
    }

    /**
     * Get an attribute. <p>
     * <p/>
     * <p/>
     * <i>Note</i>: this is an extension to the OpenMath standard 1.0. For the
     * OpenMath standard 2.0 this is needed. So if you want 1.0 compliant code
     * do not use attributes.
     * </p>
     *
     * @param name the name of the attribute to get.
     * @return the value of the attribute.
     */
    public Object getAttribute(String name) {
        return (Object) attributes.get(name);
    }

    /**
     * Set an attribute. <p>
     * <p/>
     * <p/>
     * <i>Note</i>: this is an extension to the OpenMath standard 1.0. For the
     * OpenMath standard 2.0 this is needed. So if you want 1.0 compliant code
     * do not use attributes.
     * </p>
     *
     * @param name  the name of the attribute to set.
     * @param value the value of the attribute.
     */
    public void setAttribute(String name, String value) {
        attributes.put(name, value);
    }

    /**
     * Remove an attribute. <p>
     * <p/>
     * <p/>
     * <i>Note</i>: this is an extension to the OpenMath standard 1.0. For the
     * OpenMath standard 2.0 this is needed. So if you want 1.0 compliant code
     * do not use attributes.
     * </p>
     *
     * @param name the name of the attribute to remove.
     */
    public void removeAttribute(String name) {
        attributes.remove(name);
    }
}
