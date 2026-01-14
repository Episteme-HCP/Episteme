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

package org.jscience.linguistics.braille;

/**
 * Indicates an error in the creation of a new BrailleLanguageUnicode object.  This
 * is thrown when one of the BrailleLanguageUnicode <CODE>set</CODE> methods is used
 * to add data (character rules, translation rules, state descriptions and so
 * on) to a new BrailleLanguageUnicode (created by <CODE>new BrailleLanguageUnicode()</CODE>
 * but the methods are used with invalid parameters or in the incorrect order.
 * <p/>
 * <p><small>Copyright 1999, 2004 Alasdair King. This program is free software
 * under the terms of the GNU General Public License. </small>
 *
 * @author Alasdair King, alasdairking@yahoo.co.uk
 * @version 1.0 09/01/2001
 */
public class BrailleLanguageDefinitionException extends Exception {
    /**
     * Creates a new default BrailleLanguageDefinitionException
     */
    public BrailleLanguageDefinitionException() {
        super();
    }

    /**
     * Creates a new BrailleLanguageDefinitionException with the error text held
     * in <CODE>description</CODE>.
     *
     * @param description <CODE>String</CODE> containing description of error.
     */
    public BrailleLanguageDefinitionException(String description) {
        super(description);
    }
}
