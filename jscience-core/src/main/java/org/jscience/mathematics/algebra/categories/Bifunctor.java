package org.jscience.mathematics.algebra.categories;

/**
 * Represents a Bifunctor.
 * <p>
 * A bifunctor is a functor whose domain is a product category C1 x C2.
 * F: C1 x C2 -> D
 * </p>
 * 
 * @param <O1> Object type of category C1
 * @param <M1> Morphism type of category C1
 * @param <O2> Object type of category C2
 * @param <M2> Morphism type of category C2
 * @param <O3> Object type of target category D
 * @param <M3> Morphism type of target category D
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Bifunctor<O1, M1, O2, M2, O3, M3> {

    /**
     * Maps a pair of objects to an object in the target category.
     */
    O3 applyObject(O1 obj1, O2 obj2);

    /**
     * Maps a pair of morphisms to a morphism in the target category.
     */
    M3 applyMorphism(M1 mor1, M2 mor2);
}
