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
