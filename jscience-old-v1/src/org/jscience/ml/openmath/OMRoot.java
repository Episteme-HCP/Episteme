/*
 * $Id: OMRoot.java,v 1.2 2007-10-21 17:46:57 virtualcall Exp $
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
 *  Alternatively, the contents of this file may be used under the terms
 *  of the GNU Lesser General Public license (the "LGPL license"), in which 
 *  case the provisions of the LGPL license are applicable instead of those 
 *  above. A copy of the LGPL license is available at http://www.gnu.org/
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
 * Models an OpenMath root object. <p>
 * <p/>
 * <p/>
 * Note: this object was created because the OpenMath 2.0 standard allows
 * additional attributes to be associated with the base XML element.
 * </p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.2 $
 * @see "The OpenMath standard 2.0, 3.1.1"
 */
public class OMRoot extends OMObject {
    /**
     * Stores the 'real' object. <p>
     */
    protected OMObject object;

    /**
     * Clones a OMRoot object. <p>
     *
     * @return the clone.
     */
    public Object clone() {
        OMRoot clone = new OMRoot();
        clone.object = object;
        return clone;
    }

    /**
     * Copies an OMRoot object. <p>
     *
     * @return the copy.
     */
    public Object copy() {
        OMRoot copy = new OMRoot();
        copy.object = (OMObject) object.copy();
        return copy;
    }

    /**
     * Get the type of the object. <p>
     *
     * @return the type
     */
    public String getType() {
        return "OMOBJ";
    }

    /**
     * Returns if this object is an atom object. <p>
     *
     * @return <b>false</b>
     */
    public boolean isAtom() {
        return false;
    }

    /**
     * Returns if this object is a composite object. <p>
     *
     * @return <b>true</b>
     */
    public boolean isComposite() {
        return true;
    }

    /**
     * Returns if this object is the same with the given object.
     *
     * @return <b>true</b> if it is the same, <b>false</b> if it is not.
     */
    public boolean isSame(OMObject object) {
        if (object instanceof OMRoot) {
            OMRoot root = (OMRoot) object;
            return this.object.isSame(root.object);
        }
        return false;
    }

    /**
     * Returns if this object is a valid object. <p>
     *
     * @return <b>true</b> if valid, <b>false</b> if not
     */
    public boolean isValid() {
        if (object != null) {
            return object.isValid();
        }
        return false;
    }

    /**
     * toString. <p>
     *
     * @return the string representation of the object.
     */
    public String toString() {
        return "<OMOBJ>" + object + "</OMOBJ>";
    }

    /**
     * Set the object. <p>
     */
    public void setObject(OMObject newObject) {
        object = newObject;
    }

    /**
     * Get the object. <p>
     */
    public OMObject getObject() {
        return object;
    }
}
