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

package org.jscience.mathematics.structures.categories;

/**
 * Represents a Natural Transformation between two functors.
 * <p>
 * A natural transformation ÃŽÂ·: F => G provides a way to transform one functor
 * into another
 * while respecting the internal structure of the categories involved.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface NaturalTransformation<O1, M1, O2, M2> {

    /**
     * Returns the component of the natural transformation at a given object.
     * <p>
     * For an object X in C, the component ÃŽÂ·_X is a morphism in D from F(X) to G(X).
     * </p>
     * 
     * @param object the object X in C
     * @return the morphism ÃŽÂ·_X: F(X) -> G(X) in D
     */
    M2 component(O1 object);

    /**
     * Returns the source functor F.
     */
    Functor<O1, M1, O2, M2> getSourceFunctor();

    /**
     * Returns the target functor G.
     */
    Functor<O1, M1, O2, M2> getTargetFunctor();
}

