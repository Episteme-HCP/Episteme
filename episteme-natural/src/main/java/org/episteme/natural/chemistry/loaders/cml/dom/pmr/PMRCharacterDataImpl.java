/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.chemistry.loaders.cml.dom.pmr;

import org.w3c.dom.CharacterData;


/**
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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
     * @param cd  the generic W3C character data to delegate to
     * @param doc the owner CML document
     */
    public PMRCharacterDataImpl(CharacterData cd, PMRDocument doc) {
        super(cd, doc);
    }

    /**
     * Deletes a range of characters from the node.
     *
     * @param i the character offset at which to start deleting
     * @param j the number of characters to delete
     */
    public void deleteData(int i, int j) {
        ((CharacterData) delegateNode).deleteData(i, j);
    }

    /**
     * Returns the character data of the node.
     *
     * @return the character data
     */
    public String getData() {
        return ((CharacterData) delegateNode).getData();
    }

    /**
     * Returns a specified substring of the character data.
     *
     * @param i the offset of the first character to extract
     * @param j the number of characters to extract
     *
     * @return the specified substring
     */
    public String substringData(int i, int j) {
        return ((CharacterData) delegateNode).substringData(i, j);
    }

    /**
     * Appends a string to the end of the character data.
     *
     * @param s the string to append
     */
    public void appendData(String s) {
        ((CharacterData) delegateNode).appendData(s);
    }

    /**
     * Sets the new character data for the node.
     *
     * @param s the new character data
     */
    public void setData(String s) {
        ((CharacterData) delegateNode).setData(s);
    }

    /**
     * Returns the number of characters in the node.
     *
     * @return the character count
     */
    public int getLength() {
        return ((CharacterData) delegateNode).getLength();
    }

    /**
     * Replaces a range of characters with a new string.
     *
     * @param i the offset at which to start replacing
     * @param j the number of characters to replace
     * @param s the replacement string
     */
    public void replaceData(int i, int j, String s) {
        ((CharacterData) delegateNode).replaceData(i, j, s);
    }

    /**
     * Inserts a string at the specified offset.
     *
     * @param i the character offset at which to insert
     * @param s the string to insert
     */
    public void insertData(int i, String s) {
        ((CharacterData) delegateNode).insertData(i, s);
    }
}

