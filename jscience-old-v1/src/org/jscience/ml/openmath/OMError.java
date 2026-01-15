/*
 * $Id: OMError.java,v 1.2 2007-10-21 17:46:56 virtualcall Exp $
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
 * Models an OpenMath error object. <p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.2 $
 * @see "The OpenMath standard 2.0, 2.1.3"
 */
public class OMError extends OMObject {
    /**
     * Stores the symbol. <p>
     */
    protected OMSymbol symbol;

    /**
     * Stores the errors. <p>
     */
    protected Vector elements = new Vector();

    /**
     * Constructor. <p>
     *
     * @param newSymbol the error symbol
     */
    public OMError(OMSymbol newSymbol) {
        super();

        setSymbol(newSymbol);
    }

    /**
     * Constructor. <p>
     */
    public OMError() {
        super();
    }

    /**
     * Gets the type. <p>
     *
     * @return the type.
     */
    public String getType() {
        return "OME";
    }

    /**
     * Set the symbol. <p>
     *
     * @param newSymbol the error symbol to set.
     */
    public void setSymbol(OMSymbol newSymbol) {
        symbol = newSymbol;
    }

    /**
     * Gets the symbol. <p>
     *
     * @return the error symbol
     */
    public OMSymbol getSymbol() {
        return symbol;
    }

    /**
     * Get the elements. <p>
     *
     * @return the elements
     */
    public Vector getElements() {
        return elements;
    }

    /**
     * Set the elements. <p>
     *
     * @param newElements the elements to set.
     */
    public void setElements(Vector newElements) {
        elements = newElements;
    }

    /**
     * Get element. <p>
     *
     * @param index the index of the element to get.
     * @return the element at the given index.
     */
    public OMObject getElementAt(int index) {
        return (OMObject) elements.elementAt(index);
    }

    /**
     * Set element. <p>
     *
     * @param object the object to set.
     * @param index  the index to set at.
     */
    public void setElementAt(OMObject object, int index) {
        elements.setElementAt(object, index);
    }

    /**
     * Insert element at. <p>
     *
     * @param object the object to insert.
     * @param index  the index to insert at.
     */
    public void insertElementAt(OMObject object, int index) {
        elements.insertElementAt(object, index);
    }

    /**
     * Remove element at. <p>
     *
     * @param index the index to remove the object from.
     */
    public void removeElementAt(int index) {
        elements.removeElementAt(index);
    }

    /**
     * Add element. <p>
     *
     * @param object the object to add.
     */
    public void addElement(OMObject object) {
        elements.addElement(object);
    }

    /**
     * Remove element. <p>
     * <p/>
     * <p/>
     * <i>Note: This removes the first occurence of the given
     * element. If you want to remove all the references to
     * the given object, continue remove until this returns
     * false.</i>
     * </p>
     *
     * @param object the object to remove.
     * @return <b>true</b> if more objects exist, <b>false</b> otherwise.
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
     * Get the first element. <p>
     *
     * @return the first element.
     */
    public OMObject firstElement() {
        return (OMObject) elements.firstElement();
    }

    /**
     * Get the last element. <p>
     *
     * @return the last element.
     */
    public OMObject lastElement() {
        return (OMObject) elements.lastElement();
    }

    /**
     * toString. <p>
     *
     * @return the string representation.
     */
    public String toString() {
        StringBuffer result = new StringBuffer();
        Enumeration enumeration = elements.elements();

        result.append("<OME>");
        result.append(symbol.toString());

        for (; enumeration.hasMoreElements();) {
            OMObject object = (OMObject) enumeration.nextElement();
            result.append(object.toString());
        }

        result.append("</OME>");

        return result.toString();
    }

    /**
     * Clones the object (shallow copy).
     *
     * @return the shallow copy.
     */
    public Object clone() {
        OMError error = new OMError();
        error.symbol = this.symbol;
        error.elements = this.elements;

        return error;
    }

    /**
     * Copies the object (deep copy). <p>
     *
     * @return the deep copy.
     */
    public Object copy() {
        OMError error = new OMError();
        error.symbol = (OMSymbol) this.symbol.copy();

        Enumeration errors = this.elements.elements();
        for (; errors.hasMoreElements();) {
            error.elements.addElement(((OMObject) errors.nextElement()).copy());
        }

        return error;
    }

    /**
     * Are we a composite object.
     *
     * @return <b>true</b> because we are a composite object.
     */
    public boolean isComposite() {
        return true;
    }

    /**
     * Are we an atom object.
     *
     * @return <b>false</b> because we are not an atom object.
     */
    public boolean isAtom() {
        return false;
    }

    /**
     * Determines if this is the same object. <p>
     *
     * @param object the object to check against.
     * @return <b>true</b> if it is the same object, <b>false</b> otherwise.
     */
    public boolean isSame(OMObject object) {
        if (object instanceof OMError) {
            OMError error = (OMError) object;
            if (!error.getSymbol().isSame(getSymbol()))
                return false;

            Enumeration enumeration1 = error.elements.elements();
            Enumeration enumeration2 = elements.elements();

            for (; enumeration1.hasMoreElements();) {
                OMObject object1 = (OMObject) enumeration1.nextElement();
                OMObject object2 = (OMObject) enumeration2.nextElement();

                if (!object1.isSame(object2))
                    return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Determines if this is a valid object. <p>
     *
     * @return <b>true</b> if valid, <b>false</b> otherwise.
     */
    public boolean isValid() {
        if (symbol != null && symbol.isValid()) {
            for (Enumeration enumeration = elements.elements(); enumeration.hasMoreElements();) {
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
            }
            i++;
        }
        return this;
    }
}
