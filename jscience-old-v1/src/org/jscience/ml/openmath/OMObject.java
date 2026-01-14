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
