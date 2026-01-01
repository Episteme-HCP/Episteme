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

package org.jscience.mathematics.structures.groups;

/**
 * An abelian group is a commutative group.
 * <p>
 * Abelian groups are named after Niels Henrik Abel and are fundamental
 * in many areas of mathematics. The addition operation in most number
 * systems forms an abelian group.
 * </p>
 *
 * <h2>Mathematical Definition</h2>
 * <p>
 * An abelian group (G, +) satisfies all group axioms plus:
 * <ul>
 * <li><strong>Commutativity</strong>: Ã¢Ë†â‚¬ a, b Ã¢Ë†Ë† G: a + b = b + a</li>
 * </ul>
 * </p>
 * <p>
 * <strong>Convention</strong>: Abelian groups are typically written with
 * additive notation (+, 0, Ã¢Ë†â€™a) rather than multiplicative (Ã¢Ë†â€”, e, aÃ¢ÂÂ»Ã‚Â¹).
 * </p>
 *
 * <h2>Examples</h2>
 * <ul>
 * <li>(Ã¢â€žÂ¤, +) - Integers with addition</li>
 * <li>(Ã¢â€žÅ¡, +) - Rationals with addition</li>
 * <li>(Ã¢â€žÂ, +) - Reals with addition</li>
 * <li>(Ã¢â€žâ€š, +) - Complex numbers with addition</li>
 * <li>(Ã¢â€žÂÃ¢ÂÂ¿, +) - n-dimensional vectors with addition</li>
 * <li>(Ã¢â€žÂ¤/nÃ¢â€žÂ¤, +) - Integers modulo n</li>
 * </ul>
 *
 * <h2>Additive Notation</h2>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface AbelianGroup<E> extends Group<E>, AbelianMonoid<E> {

    /**
     * Returns the additive inverse (negation) of an element.
     * <p>
     * Satisfies: a + (-a) = (-a) + a = 0
     * </p>
     * 
     * @param element the element to negate
     * @return -element
     * @throws NullPointerException if element is null
     * 
     * @see #inverse(Object)
     */
    default E negate(E element) {
        return inverse(element);
    }

    /**
     * Returns the difference of two elements.
     * <p>
     * Defined as: a - b = a + (-b)
     * </p>
     * 
     * @param a the minuend
     * @param b the subtrahend
     * @return a - b
     * @throws NullPointerException if either argument is null
     * 
     * @see #add(Object, Object)
     * @see #negate(Object)
     */
    default E subtract(E a, E b) {
        return add(a, negate(b));
    }

    /**
     * Abelian groups are always commutative by definition.
     * 
     * @return always {@code true}
     */
    @Override
    default boolean isCommutative() {
        return true;
    }
}

