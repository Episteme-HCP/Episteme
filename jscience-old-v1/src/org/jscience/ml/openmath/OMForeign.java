/*
 * $Id: OMForeign.java,v 1.2 2007-10-21 17:46:56 virtualcall Exp $
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

/**
 * Models an OpenMath foreign object. <p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.2 $
 */
public class OMForeign extends OMObject {
    /**
     * Stores the foreign object. <p>
     */
    protected Object object;

    /**
     * Constructor. <p>
     */
    public OMForeign() {
    }

    /**
     * Clones the object. <p>
     *
     * @return the clone.
     */
    public Object clone() {
        OMForeign clone = new OMForeign();
        clone.object = object;
        return clone;
    }

    /**
     * Copies the object. <p>
     * <p/>
     * <p/>
     * Note: the copy method of this object does NOT deep copy the foreign
     * object. It does basically the same thing as clone. This behavior is
     * intended. If you want a copy of the foreign object you will have to
     * make your own copy!
     * </p>
     *
     * @return the copy.
     */
    public Object copy() {
        OMForeign copy = new OMForeign();
        copy.object = object;
        return copy;
    }

    /**
     * Returns the type of the object. <p>
     *
     * @return the type
     */
    public String getType() {
        return "OMFOREIGN";
    }

    /**
     * Returns if this is an atom. <p>
     *
     * @return <b>true</b> if an atom, <b>false</b> if not.
     */
    public boolean isAtom() {
        return true;
    }

    /**
     * Returns if this is a composite. <p>
     *
     * @return <b>true</b> if an composite, <b>false</b> if not.
     */
    public boolean isComposite() {
        return false;
    }

    /**
     * Returns if this is the same object.
     *
     * @return <b>true</b> if the same, <b>false</b> if not.
     */
    public boolean isSame(OMObject object) {
        if (object instanceof OMForeign) {
            OMForeign foreign = (OMForeign) object;
            if (foreign.object == null &&
                    this.object == null) {
                return true;
            } else
                return foreign.object.equals(this.object);
        }
        return false;
    }

    /**
     * Returns if the OMForeign is valid. <p>
     *
     * @return <b>true</b> if valid, <b>false</b> if not.
     */
    public boolean isValid() {
        return object != null;
    }

    /**
     * Returns a string representation.
     *
     * @return a string.
     */
    public String toString() {
        return "<OMFOREIGN>" + "</OMFOREIGN>";
    }

    /**
     * Set the object. <p>
     *
     * @param object the object to set.
     */
    public void setObject(Object object) {
        this.object = object;
    }

    /**
     * Get the object. <p>
     *
     * @return the foreign object
     */
    public Object getObject() {
        return object;
    }
}
