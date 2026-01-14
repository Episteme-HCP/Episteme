/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
