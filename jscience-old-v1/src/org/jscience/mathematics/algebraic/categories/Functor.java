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

package org.jscience.mathematics.algebraic.categories;

/**
 * This interface defines a functor.
 *
 * @author Mark Hale
 * @version 1.0
 * @planetmath Functor
 */
public interface Functor extends Category.Morphism {
    /**
     * Maps an object from one category to another.
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Object map(Object o);

    /**
     * Maps a morphism from one category to another.
     *
     * @param m DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Category.Morphism map(Category.Morphism m);

    /**
     * Returns the composition of this functor with another.
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Functor compose(Functor f);
}
