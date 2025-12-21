/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * Represents a Functor between two categories.
 * <p>
 * A functor F: C -> D maps objects of C to objects of D and morphisms of C to
 * morphisms of D,
 * preserving identity and composition.
 * </p>
 * 
 * @param <O1> Object type of source category
 * @param <M1> Morphism type of source category
 * @param <O2> Object type of target category
 * @param <M2> Morphism type of target category
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Functor<O1, M1, O2, M2> {

    /**
     * Maps an object from the source category to the target category.
     * 
     * @param object the object in C
     * @return F(object) in D
     */
    O2 applyObject(O1 object);

    /**
     * Maps a morphism from the source category to the target category.
     * 
     * @param morphism the morphism f: A -> B in C
     * @return F(f): F(A) -> F(B) in D
     */
    M2 applyMorphism(M1 morphism);

    /**
     * Returns the source category.
     * 
     * @return the domain category
     */
    Category<O1, M1> getSourceCategory();

    /**
     * Returns the target category.
     * 
     * @return the codomain category
     */
    Category<O2, M2> getTargetCategory();
}