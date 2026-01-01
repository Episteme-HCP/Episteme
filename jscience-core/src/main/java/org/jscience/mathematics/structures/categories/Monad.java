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

/**
 * Represents a Monad in Category Theory.
 * <p>
 * A Monad on a category C is an endofunctor T: C Ã¢â€ â€™ C equipped with two natural
 * transformations:
 * <ul>
 * <li>Unit (ÃŽÂ·): Id Ã¢â€ â€™ T</li>
 * <li>Multiplication (ÃŽÂ¼): TÃ‚Â² Ã¢â€ â€™ T</li>
 * </ul>
 * satisfying associativity and identity laws.
 * </p>
 * <p>
 * In computer science, Monads are used to model computations with side effects
 * (state, I/O, exceptions, non-determinism).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Monad<O, M> extends Functor<O, M, O, M> {

    /**
     * The unit natural transformation (ÃŽÂ·).
     * Lifts a value into the monadic context.
     * Also known as 'return' or 'pure'.
     * 
     * @param object the object to lift
     * @return the morphism from object to T(object)
     */
    M unit(O object);

    /**
     * The multiplication natural transformation (ÃŽÂ¼).
     * Flattens a nested monadic value.
     * Also known as 'join' or 'flatten'.
     * 
     * @param nestedObject the object representing T(T(x))
     * @return the morphism from T(T(x)) to T(x)
     */
    M join(O nestedObject);

    /**
     * Bind operation (>>=).
     * Maps a function over the monadic value and flattens the result.
     * 
     * @param object   the monadic object T(x)
     * @param morphism the morphism from x to T(y)
     * @return the result T(y)
     */
    // Note: Signature is tricky without HKTs. This is a conceptual definition.
    // In a concrete implementation (like ListMonad), this would be:
    // List<Y> bind(List<X> list, Function<X, List<Y>> f)
}

