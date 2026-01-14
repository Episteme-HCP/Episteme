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

package org.jscience.mathematics.structures.sets;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Represents a set with a finite number of elements.
 * <p>
 * Finite sets allow iteration over their elements and have a definite size.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface FiniteSet<E> extends Set<E>, Iterable<E> {

    /**
     * Returns the number of elements in this set (its cardinality).
     * 
     * @return the cardinality of this set
     */
    long size();

    /**
     * Returns a sequential {@code Stream} with this set as its source.
     * 
     * @return a stream over the elements in this set
     */
    default Stream<E> stream() {
        return java.util.stream.StreamSupport.stream(spliterator(), false);
    }

    /**
     * Returns an iterator over the elements in this set.
     * 
     * @return an iterator over the elements in this set
     */
    @Override
    Iterator<E> iterator();
}

