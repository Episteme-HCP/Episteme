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
package org.episteme.core.mathematics.loaders.openmath;

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
    protected Vector<OMObject> elements = new Vector<>();

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
        Enumeration<OMObject> enumeration = elements.elements();

        result.append("<OMA>");

        for (; enumeration.hasMoreElements();) {
            OMObject object = enumeration.nextElement();
            result.append(object.toString());
        }

        result.append("</OMA>");
        return result.toString();
    }

    public Object clone() {
        OMApplication application = new OMApplication();
        application.elements = new Vector<>(this.elements);

        return application;
    }

    public Object copy() {
        OMApplication application = new OMApplication();
        Enumeration<OMObject> enumeration = this.elements.elements();
        OMObject object = null;

        for (; enumeration.hasMoreElements();) {
            object = enumeration.nextElement();
            application.addElement((OMObject) object.copy());
        }

        return application;
    }

    public boolean isComposite() {
        return true;
    }

    public boolean isAtom() {
        return false;
    }

    public int getLength() {
        return elements.size();
    }

    public Vector<OMObject> getElements() {
        return elements;
    }

    public void setElements(Vector<OMObject> newElements) {
        elements = newElements;
    }

    public OMObject getElementAt(int index) {
        return elements.elementAt(index);
    }

    public void setElementAt(OMObject object, int index) {
        elements.setElementAt(object, index);
    }

    public void insertElementAt(OMObject object, int index) {
        elements.insertElementAt(object, index);
    }

    public void removeElementAt(int index) {
        elements.removeElementAt(index);
    }

    public void addElement(OMObject object) {
        elements.addElement(object);
    }

    public boolean removeElement(OMObject object) {
        return elements.removeElement(object);
    }

    public void removeAllElements() {
        elements.removeAllElements();
    }

    public OMObject firstElement() {
        return elements.firstElement();
    }

    public OMObject lastElement() {
        return elements.lastElement();
    }

    public boolean isSame(OMObject object) {
        if (object instanceof OMApplication) {
            OMApplication application = (OMApplication) object;

            if (application.getLength() == getLength()) {
                Enumeration<OMObject> enumeration1 = application.getElements().elements();
                Enumeration<OMObject> enumeration2 = elements.elements();

                for (; enumeration1.hasMoreElements();) {
                    OMObject object1 = enumeration1.nextElement();
                    OMObject object2 = enumeration2.nextElement();

                    if (!object1.isSame(object2))
                        return false;
                }
                return true;
            }
        }
        return false;
    }

    public boolean isValid() {
        if (elements.size() > 0) {
            Enumeration<OMObject> enumeration = elements.elements();

            for (; enumeration.hasMoreElements();) {
                OMObject object = enumeration.nextElement();
                if (!object.isValid())
                    return false;
            }

            return true;
        }
        return false;
    }

    public OMObject replace(OMObject source, OMObject dest) {
        int i = 0;
        for (Enumeration<OMObject> enumeration = elements.elements(); enumeration.hasMoreElements();) {
            OMObject object = enumeration.nextElement();
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

