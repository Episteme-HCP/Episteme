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
 * Models an OpenMath string.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 */
public class OMString extends OMObject {
    /**
     * Stores the string.<p></p>
     */
    protected String string = null;

/**
     * Constructor. <p>
     */
    public OMString() {
        super();
    }

/**
     * Constructor. <p>
     *
     * @param newString the Java string to associate with thie OpenMath object.
     */
    public OMString(String newString) {
        super();

        string = newString;
    }

    /**
     * Gets the type.<p></p>
     *
     * @return the type.
     */
    public String getType() {
        return "OMSTR";
    }

    /**
     * Sets the string.<p></p>
     *
     * @param newString the string to set.
     */
    public void setString(String newString) {
        string = newString;
    }

    /**
     * Gets the string.<p></p>
     *
     * @return the string.
     */
    public String getString() {
        return string;
    }

    /**
     * Is this an atom object.<p></p>
     *
     * @return <b>true</b> because it is an atom.
     */
    public boolean isAtom() {
        return true;
    }

    /**
     * Is this a composite object.<p></p>
     *
     * @return <b>false</b> because it is not composite.
     */
    public boolean isComposite() {
        return false;
    }

    /**
     * toString.<p></p>
     *
     * @return the normal string representation.
     */
    public String toString() {
        return "<OMSTR>" + string + "</OMSTR>";
    }

    /**
     * Clone the object (shallow copy).
     *
     * @return the shallow copy.
     */
    public Object clone() {
        OMString cloneString = new OMString();
        cloneString.string = new String(string);

        return cloneString;
    }

    /**
     * Copies the object (deep copy).
     *
     * @return the deep copy
     */
    public Object copy() {
        OMString copyString = new OMString();
        copyString.string = new String(string);

        return copyString;
    }

    /**
     * Determines if this is the same object.<p></p>
     *
     * @param object the object to test against.
     *
     * @return <b>true</b> if it is the same, <b>false</b> if it is not.
     */
    public boolean isSame(OMObject object) {
        if (object instanceof OMString) {
            OMString string = (OMString) object;

            if (string.string.equals(this.string)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if this object is valid.<p></p>
     *
     * @return <b>true</b> if it is valid, <b>false</b> if it is not.
     */
    public boolean isValid() {
        if (string != null) {
            return true;
        }

        return false;
    }
}
