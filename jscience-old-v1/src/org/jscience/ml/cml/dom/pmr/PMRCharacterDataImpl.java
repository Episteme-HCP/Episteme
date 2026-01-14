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

package org.jscience.ml.cml.dom.pmr;

import org.w3c.dom.CharacterData;


/**
 * 
 */
public class PMRCharacterDataImpl extends PMRNodeImpl implements CharacterData {
/**
     * Creates a new PMRCharacterDataImpl object.
     */
    public PMRCharacterDataImpl() {
        super();
    }

/**
     * Creates a new PMRCharacterDataImpl object.
     *
     * @param cd  DOCUMENT ME!
     * @param doc DOCUMENT ME!
     */
    public PMRCharacterDataImpl(CharacterData cd, PMRDocument doc) {
        super(cd, doc);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     */
    public void deleteData(int i, int j) {
        ((CharacterData) delegateNode).deleteData(i, j);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getData() {
        return ((CharacterData) delegateNode).getData();
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String substringData(int i, int j) {
        return ((CharacterData) delegateNode).substringData(i, j);
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     */
    public void appendData(String s) {
        ((CharacterData) delegateNode).appendData(s);
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     */
    public void setData(String s) {
        ((CharacterData) delegateNode).setData(s);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLength() {
        return ((CharacterData) delegateNode).getLength();
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param s DOCUMENT ME!
     */
    public void replaceData(int i, int j, String s) {
        ((CharacterData) delegateNode).replaceData(i, j, s);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param s DOCUMENT ME!
     */
    public void insertData(int i, String s) {
        ((CharacterData) delegateNode).insertData(i, s);
    }
}
