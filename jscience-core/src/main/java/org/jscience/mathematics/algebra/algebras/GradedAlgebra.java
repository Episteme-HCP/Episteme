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
package org.jscience.mathematics.algebra.algebras;

import org.jscience.mathematics.algebra.Algebra;

/**
 * Represents a graded algebra - an algebra that can be decomposed
 * into a direct sum of subspaces indexed by a grading group (typically
 * integers).
 * <p>
 * A graded algebra A = ⊕ᵢ Aᵢ where the multiplication respects the grading:
 * Aᵢ · Aⱼ ⊆ Aᵢ₊ⱼ
 * </p>
 * 
 * @param <E> the element type
 * @param <F> the scalar type * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface GradedAlgebra<E, F> extends Algebra<E, F> {

    /**
     * Returns the grade (degree) of the given element.
     */
    int grade(E element);

    /**
     * Projects an element onto the homogeneous component of the specified grade.
     */
    E projectToGrade(E element, int grade);

    /**
     * Returns true if the element is homogeneous (has a single grade).
     */
    boolean isHomogeneous(E element);

    /**
     * Returns the maximum grade present in this algebra, or -1 if infinite.
     */
    int maxGrade();

    /**
     * Returns the minimum grade present in this algebra.
     */
    default int minGrade() {
        return 0;
    }
}
