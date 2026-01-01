/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.biology;

/**
 * Represents the nucleobases found in DNA and RNA.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public enum Base {
    ADENINE,
    CYTOSINE,
    GUANINE,
    THYMINE,
    URACIL;

    /**
     * Returns the complementary base.
     * DNA: A-T, C-G
     * RNA: A-U, C-G
     * 
     * @param isRNA if true, returns complement for RNA (A -> U), else for DNA (A ->
     *              T).
     * @return the complementary base.
     */
    public Base getComplementary(boolean isRNA) {
        switch (this) {
            case ADENINE:
                return isRNA ? URACIL : THYMINE;
            case CYTOSINE:
                return GUANINE;
            case GUANINE:
                return CYTOSINE;
            case THYMINE:
                return ADENINE; // Only in DNA
            case URACIL:
                return ADENINE; // Only in RNA
            default:
                return null;
        }
    }
}


