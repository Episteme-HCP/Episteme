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

package org.jscience.computing.ai.casebasedreasoning;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class TraitValue {
    //-----------------------------------------------------------------------------
    // Attributes
    //-----------------------------------------------------------------------------
    /** DOCUMENT ME! */
    private String value;

    //-----------------------------------------------------------------------------
    /**
     * Creates a new TraitValue object.
     *
     * @param value DOCUMENT ME!
     */
    public TraitValue(String value) {
        this.value = value;
    }

    //-----------------------------------------------------------------------------
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int toInteger() {
        Integer integerValue = new Integer(value);

        return integerValue.intValue();
    } //--- toInteger

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean toBoolean() {
        Boolean booleanValue = new Boolean(value);

        return booleanValue.booleanValue();
    } //--- toBoolean

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float toFloat() {
        Float floatValue = new Float(value);

        return floatValue.floatValue();
    } //--- toFloat

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return value;
    } //--- toString

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String value() {
        return toString();
    }
} //--- TraitValue
