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

package org.jscience.mathematics.sets;

import org.jscience.mathematics.structures.rings.Semiring;
import org.jscience.mathematics.structures.sets.FiniteSet;
import org.jscience.mathematics.numbers.integers.Boolean;

/**
 * The structure of Boolean values (𝔹 = {0, 1}).
 * <p>
 * This class represents the <strong>structure</strong> of Booleans.
 * </p>
 * 
 * @see Boolean * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class Booleans implements Semiring<Boolean>, FiniteSet<Boolean> {

    private static final Booleans INSTANCE = new Booleans();

    public static Booleans getInstance() {
        return INSTANCE;
    }

    private Booleans() {
    }

    @Override
    public Boolean operate(Boolean a, Boolean b) {
        return a.add(b);
    }

    @Override
    public Boolean add(Boolean a, Boolean b) {
        return a.add(b);
    }

    @Override
    public Boolean zero() {
        return Boolean.FALSE;
    }

    @Override
    public Boolean multiply(Boolean a, Boolean b) {
        return a.multiply(b);
    }

    @Override
    public Boolean one() {
        return Boolean.TRUE;
    }

    @Override
    public boolean isMultiplicationCommutative() {
        return true;
    }

    @Override
    public boolean contains(Boolean element) {
        return element != null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public long size() {
        return 2;
    }

    @Override
    public String description() {
        return "𝔹 (Booleans)";
    }

    @Override
    public java.util.Iterator<Boolean> iterator() {
        return java.util.List.of(Boolean.FALSE, Boolean.TRUE).iterator();
    }

    @Override
    public java.util.stream.Stream<Boolean> stream() {
        return java.util.stream.Stream.of(Boolean.FALSE, Boolean.TRUE);
    }

    @Override
    public String toString() {
        return "Booleans(𝔹)";
    }
}