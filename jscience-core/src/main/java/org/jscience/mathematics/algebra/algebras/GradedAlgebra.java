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
 * @param <F> the scalar type
 * @author Silvere Martin-Michiellot
 * @since 2.0
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
