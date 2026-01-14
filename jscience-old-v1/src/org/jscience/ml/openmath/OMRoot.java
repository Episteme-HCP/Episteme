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
