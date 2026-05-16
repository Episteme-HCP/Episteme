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

package org.episteme.natural.biology;

import org.episteme.core.util.EnumRegistry;
import org.episteme.core.util.ExtensibleEnum;

/**
 * Represents the nucleobases found in DNA and RNA.
 * Uses EnumRegistry pattern to allow dynamic extension (e.g. artificial bases X/Y).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public class Base extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final Base ADENINE = register("ADENINE");
    public static final Base CYTOSINE = register("CYTOSINE");
    public static final Base GUANINE = register("GUANINE");
    public static final Base THYMINE = register("THYMINE");
    public static final Base URACIL = register("URACIL");

    protected Base(String name) {
        super(name);
    }

    private static Base register(String name) {
        Base base = new Base(name);
        EnumRegistry.register(Base.class, base);
        return base;
    }

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
        if (this.equals(ADENINE)) return isRNA ? URACIL : THYMINE;
        if (this.equals(CYTOSINE)) return GUANINE;
        if (this.equals(GUANINE)) return CYTOSINE;
        if (this.equals(THYMINE)) return ADENINE;
        if (this.equals(URACIL)) return ADENINE;
        return null;
    }
}




