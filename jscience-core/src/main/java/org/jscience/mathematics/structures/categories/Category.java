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

package org.jscience.mathematics.structures.categories;

import org.jscience.mathematics.structures.sets.Set;

/**
 * Represents a Category in category theory.
 * <p>
 * A category consists of:
 * <ul>
 * <li>A collection of objects (O).</li>
 * <li>A collection of morphisms (arrows) between objects (M).</li>
 * <li>A composition operation for morphisms.</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Category<O, M> {

    /**
     * Returns the identity morphism for a given object.
     * 
     * @param object the object
     * @return the identity morphism 1_object
     */
    M identity(O object);

    /**
     * Composes two morphisms.
     * <p>
     * compose(f, g) corresponds to f Ã¢Ë†Ëœ g (f after g).
     * </p>
     * 
     * @param f the second morphism to apply
     * @param g the first morphism to apply
     * @return f Ã¢Ë†Ëœ g
     * @throws IllegalArgumentException if the domain of f does not match the
     *                                  codomain of g
     */
    M compose(M f, M g);

    /**
     * Returns the domain (source) of a morphism.
     * 
     * @param morphism the morphism
     * @return the domain object
     */
    O domain(M morphism);

    /**
     * Returns the codomain (target) of a morphism.
     * 
     * @param morphism the morphism
     * @return the codomain object
     */
    O codomain(M morphism);

    /**
     * Returns the hom-set between two objects (set of all morphisms from source to
     * target).
     * 
     * @param source the source object
     * @param target the target object
     * @return the set of morphisms Hom(source, target)
     */
    Set<M> hom(O source, O target);
}


