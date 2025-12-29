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

package org.jscience.mathematics.algebra.groups;

import org.jscience.mathematics.structures.groups.Group;
import org.jscience.mathematics.numbers.complex.Quaternion;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

/**
 * Represents the Quaternion Group Q8.
 * <p>
 * A non-abelian group of order 8: {1, -1, i, -i, j, -j, k, -k}.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class QuaternionGroup implements Group<Quaternion> {

    private static final Set<Quaternion> ELEMENTS = new HashSet<>(Arrays.asList(
            Quaternion.ONE, Quaternion.ONE.negate(),
            Quaternion.I, Quaternion.I.negate(),
            Quaternion.J, Quaternion.J.negate(),
            Quaternion.K, Quaternion.K.negate()));

    @Override
    public Quaternion operate(Quaternion left, Quaternion right) {
        Quaternion result = left.multiply(right);
        return result;
    }

    @Override
    public Quaternion identity() {
        return Quaternion.ONE;
    }

    @Override
    public Quaternion inverse(Quaternion element) {
        return element.inverse();
    }

    @Override
    public boolean isCommutative() {
        return false;
    }

    public Set<Quaternion> getElements() {
        return ELEMENTS;
    }

    @Override
    public String description() {
        return "Quaternion Group Q8";
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Quaternion element) {
        return ELEMENTS.contains(element);
    }
}
