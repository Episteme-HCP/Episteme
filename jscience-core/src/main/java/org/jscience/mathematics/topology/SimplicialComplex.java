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

package org.jscience.mathematics.topology;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a Simplicial Complex.
 * <p>
 * A simplicial complex is a set of simplices that satisfies the following
 * conditions:
 * 1. Every face of a simplex from the set is also in the set.
 * 2. The non-empty intersection of any two simplices ÃÆ’1, ÃÆ’2 is a face of both
 * ÃÆ’1 and ÃÆ’2.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SimplicialComplex {

    private final Set<Simplex> simplices;

    public SimplicialComplex() {
        this.simplices = new HashSet<>();
    }

    /**
     * Adds a simplex and all its faces to the complex.
     * 
     * @param vertices the vertices defining the simplex
     */
    public void addSimplex(int... vertices) {
        Simplex s = new Simplex(vertices);
        addSimplexRecursive(s);
    }

    private void addSimplexRecursive(Simplex s) {
        if (simplices.contains(s))
            return;
        simplices.add(s);

        // Add all faces (sub-simplices)
        if (s.dimension() > 0) {
            for (Simplex face : s.faces()) {
                addSimplexRecursive(face);
            }
        }
    }

    public int dimension() {
        return simplices.stream().mapToInt(Simplex::dimension).max().orElse(-1);
    }

    public int size() {
        return simplices.size();
    }

    public Set<Simplex> getSimplices() {
        return Collections.unmodifiableSet(simplices);
    }
}


