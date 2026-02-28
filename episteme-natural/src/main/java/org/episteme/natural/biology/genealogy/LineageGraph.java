/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.biology.genealogy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.io.Serializable;
import org.episteme.natural.biology.Individual;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.util.persistence.Relation;

/**
 * Directed graph structure for biological lineages and successions.
 * Useful for modeling dynastic successions, family trees, or evolutionary paths.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class LineageGraph implements Serializable {

    private static final long serialVersionUID = 2L;

    /** Adjacency list representing transitions between individuals. */
    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Map<Individual, List<Individual>> adjacencyList = new HashMap<>();

    /**
     * Adds a lineage link (e.g., Parent to Child, Predecessor to Successor).
     * 
     * @param from the source individual
     * @param to the target individual
     * @throws NullPointerException if from or to is null
     */
    public void addLink(Individual from, Individual to) {
        Objects.requireNonNull(from, "Source individual cannot be null");
        Objects.requireNonNull(to, "Target individual cannot be null");
        adjacencyList.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
    }

    /**
     * Finds all descendants of an individual in this graph using Depth-First Search.
     * Note: This is separate from the biological descendants stored within the Individual objects themselves,
     * allowing for specialized lineage graphs (e.g. only royal successions).
     * 
     * @param individual the starting individual
     * @return a set of descendants
     * @throws NullPointerException if individual is null
     */
    public Set<Individual> getDescendants(Individual individual) {
        Objects.requireNonNull(individual, "Starting individual cannot be null");
        Set<Individual> descendants = new HashSet<>();
        dfs(individual, descendants);
        descendants.remove(individual); // Exclude self
        return descendants;
    }

    private void dfs(Individual current, Set<Individual> visited) {
        if (current == null || !visited.add(current)) return;
        List<Individual> children = adjacencyList.get(current);
        if (children != null) {
            for (Individual child : children) {
                dfs(child, visited);
            }
        }
    }
}


