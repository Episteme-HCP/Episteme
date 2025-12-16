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
 * 2. The non-empty intersection of any two simplices σ1, σ2 is a face of both
 * σ1 and σ2.
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
