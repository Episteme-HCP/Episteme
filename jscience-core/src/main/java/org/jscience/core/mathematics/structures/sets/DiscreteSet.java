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

package org.jscience.core.mathematics.structures.sets;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of a finite set using a backing java.util.Set.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public final class DiscreteSet<E> implements FiniteSet<E> {

    private final Set<E> elements;

    /**
     * Constructs a set containing the specified elements.
     * 
     * @param elements the elements
     */
    public DiscreteSet(Set<E> elements) {
        this.elements = new HashSet<>(elements);
    }

    /**
     * Constructs a set containing a single element.
     * 
     * @param element the element
     */
    public DiscreteSet(E element) {
        this.elements = new HashSet<>();
        this.elements.add(element);
    }

    /**
     * Returns an unmodifiable view of the elements in this set.
     * 
     * @return the elements
     */
    public Set<E> getElements() {
        return Collections.unmodifiableSet(elements);
    }

    @Override
    public long size() {
        return elements.size();
    }

    @Override
    public boolean contains(E element) {
        return elements.contains(element);
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public Iterator<E> iterator() {
        return elements.iterator();
    }

    @Override
    public String description() {
        return "Discrete Finite Set with " + size() + " elements";
    }

    @Override
    public String toString() {
        return elements.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof DiscreteSet)) return false;
        return elements.equals(((DiscreteSet<?>) obj).elements);
    }

    @Override
    public int hashCode() {
        return elements.hashCode();
    }
}

