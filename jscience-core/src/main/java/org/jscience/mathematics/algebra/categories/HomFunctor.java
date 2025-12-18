package org.jscience.mathematics.algebra.categories;

/**
 * Represents a Hom-Functor.
 * <p>
 * Hom(A, -): C -> Set (Covariant)
 * Hom(-, B): C^op -> Set (Contravariant)
 * </p>
 * 
 * @param <O> Object type of category C
 * @param <M> Morphism type of category C
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface HomFunctor<O, M> extends Bifunctor<O, M, O, M, Object, Object> {
    // Marker interface for now, specific Hom logic usually requires
    // access to the set of morphisms between two objects
}
