/*
 * $Id: OMApplication.java,v 1.2 2007-10-21 17:46:56 virtualcall Exp $
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

import java.util.Enumeration;
import java.util.Vector;

/**
 * Models an OpenMath application object. <p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.2 $
 * @see "The OpenMath standard 2.0, 2.1.3"
 */
public class OMApplication extends OMObject {
    /**
     * Stores the elements. <p>
     */
    protected Vector elements = new Vector();

    /**
     * Constructor. <p>
     */
    public OMApplication() {
        super();
    }

    /**
     * Gets the type. <p>
     *
     * @return the type as a String.
     */
    public String getType() {
        return "OMA";
    }

    /**
     * Returns a string representation of the object. <p>
     * <p/>
     * <p/>
     * <i>Note:</i> this is quite similar to the OpenMath XML-encoding,
     * but it is not complete. It returns the OpenMath object without
     * the outer OMOBJ element.
     * </p>
     *
     * @return a string representation of the OpenMath application.
     */
    public String toString() {
        StringBuffer result = new StringBuffer();
        Enumeration enumeration = elements.elements();

        result.append("<OMA>");

        for (; enumeration.hasMoreElements();) {
            OMObject object = (OMObject) enumeration.nextElement();
            result.append(object.toString());
        }

        result.append("</OMA>");
        return result.toString();
    }

    /**
     * Clones the object performing a shallow copy. <p>
     * <p/>
     * <p/>
     * <i>Note:</i> a shallow does not create separate copies of the child
     * elements. So if you change anything in one of the child elements you
     * are possibly changing some other object as well.
     * </p>
     *
     * @return a clone of the object.
     */
    public Object clone() {
        OMApplication application = new OMApplication();
        application.elements = this.elements;

        return application;
    }

    /**
     * Copies the object performing a full copy. <p>
     * <p/>
     * <p/>
     * <i>Note:</i> a full copy will create separate copies of all its child
     * elements as well.
     * </p>
     *
     * @return a copy of the object.
     */
    public Object copy() {
        OMApplication application = new OMApplication();
        Enumeration enumeration = this.elements.elements();
        OMObject object = null;

        for (; enumeration.hasMoreElements();) {
            object = (OMObject) enumeration.nextElement();
            application.addElement((OMObject) object.copy());
        }

        return application;
    }

    /**
     * Are we a composite object. <p>
     *
     * @return <b>true</b> if we are a composite object,
     *         <b>false</b> if we are not.
     */
    public boolean isComposite() {
        return true;
    }

    /**
     * Are we an atom object. <p>
     *
     * @return <b>true</b> if we are an atom object,
     *         <b>false</b> if we are not.
     */
    public boolean isAtom() {
        return false;
    }

    /**
     * Get the length of the application (aka the number of elements). <p>
     *
     * @return the number of elements.
     */
    public int getLength() {
        return elements.size();
    }

    /**
     * Get the elements. <p>
     *
     * @return the vector of elements.
     */
    public Vector getElements() {
        return elements;
    }

    /**
     * Set the elements. <p>
     *
     * @param newElements a vector with elements.
     */
    public void setElements(Vector newElements) {
        elements = newElements;
    }

    /**
     * Gets the element at the given index. <p>
     *
     * @param index the index of the elements to get.
     * @return the element at the given index.
     */
    public OMObject getElementAt(int index) {
        return (OMObject) elements.elementAt(index);
    }

    /**
     * Sets an element. <p>
     *
     * @param object the object.
     * @param index  the index.
     */
    public void setElementAt(OMObject object, int index) {
        elements.setElementAt(object, index);
    }

    /**
     * Inserts an element at. <p>
     *
     * @param object the object to insert at index
     * @param index  the index to insert at.
     */
    public void insertElementAt(OMObject object, int index) {
        elements.insertElementAt(object, index);
    }

    /**
     * Removes an element at. <p>
     *
     * @param index the index to remove the object.
     */
    public void removeElementAt(int index) {
        elements.removeElementAt(index);
    }

    /**
     * Adds an element. <p>
     *
     * @param object adds an object (at the end).
     */
    public void addElement(OMObject object) {
        elements.addElement(object);
    }

    /**
     * Removes an element. <p>
     * <p/>
     * <p/>
     * <i>Note: This removes the first occurence of the given element. If you
     * want to remove all the references to the given object, continue to
     * use this method until it returns <b>false</b>.</i>
     * </p>
     *
     * @param object removes the first occurence of the given object.
     * @return <b>true</b> if the object was found (and removed),
     *         <b>false</b> otherwise.
     */

    public boolean removeElement(OMObject object) {
        return elements.removeElement(object);
    }

    /**
     * Remove all elements. <p>
     */
    public void removeAllElements() {
        elements.removeAllElements();
    }

    /**
     * Gets the first element. <p>
     *
     * @return the first element.
     */
    public OMObject firstElement() {
        return (OMObject) elements.firstElement();
    }

    /**
     * Gets the last element. <p>
     *
     * @return the last element.
     */
    public OMObject lastElement() {
        return (OMObject) elements.lastElement();
    }

    /**
     * Determines if this is the same object. <p>
     *
     * @param object the object to compare against.
     * @return <b>true</b> if this is the same object,
     *         <b>false</b> if it is not.
     */
    public boolean isSame(OMObject object) {
        if (object instanceof OMApplication) {
            OMApplication application = (OMApplication) object;

            if (application.getLength() == getLength()) {
                Enumeration enumeration1 = application.getElements().elements();
                Enumeration enumeration2 = elements.elements();

                for (; enumeration1.hasMoreElements();) {
                    OMObject object1 = (OMObject) enumeration1.nextElement();
                    OMObject object2 = (OMObject) enumeration2.nextElement();

                    if (!object1.isSame(object2))
                        return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if this is a valid object. <p>
     *
     * @return <b>true</b> if this is valid object,
     *         <b>false</b> if it is not.
     */
    public boolean isValid() {
        if (elements.size() > 0) {
            Enumeration enumeration = elements.elements();

            for (; enumeration.hasMoreElements();) {
                OMObject object = (OMObject) enumeration.nextElement();
                if (!object.isValid())
                    return false;
            }

            return true;
        }
        return false;
    }

    /**
     * Replace any occurrence of source to destination. <p>
     *
     * @param source the source object.
     * @param dest   the destination object.
     * @return the application with the replacing.
     */
    public OMObject replace(OMObject source, OMObject dest) {
        int i = 0;
        for (Enumeration enumeration = elements.elements(); enumeration.hasMoreElements();) {
            OMObject object = (OMObject) enumeration.nextElement();
            if (source.isSame(object)) {
                elements.setElementAt(dest, i);
            } else if (object instanceof OMApplication) {
                OMApplication application = (OMApplication) object;
                application.replace(source, dest);
            } else if (object instanceof OMAttribution) {
                OMAttribution attribution = (OMAttribution) object;
                attribution.replace(source, dest);
            } else if (object instanceof OMBinding) {
                OMBinding binding = (OMBinding) object;
                binding.replace(source, dest);
            } else if (object instanceof OMError) {
                OMError error = (OMError) object;
                error.replace(source, dest);
            }
            i++;
        }
        return this;
    }
}
