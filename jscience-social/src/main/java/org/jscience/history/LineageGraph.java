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

package org.jscience.history;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.io.Serializable;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Directed graph structure for historical lineages and successions.
 * Useful for modeling royal dynasties, family trees, or institutional succession.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public final class LineageGraph implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Adjacency list representing transitions between historical figures. */
    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Map<HistoricalFigure, List<HistoricalFigure>> adjacencyList = new HashMap<>();

    /**
     * Adds a lineage link (e.g., Parent to Child, Predecessor to Successor).
     * 
     * @param from the source figure
     * @param to the target figure
     * @throws NullPointerException if from or to is null
     */
    public void addLink(HistoricalFigure from, HistoricalFigure to) {
        Objects.requireNonNull(from, "Source figure cannot be null");
        Objects.requireNonNull(to, "Target figure cannot be null");
        adjacencyList.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
    }

    /**
     * Finds all descendants of a figure using Depth-First Search.
     * 
     * @param figure the starting figure
     * @return a set of descendants
     * @throws NullPointerException if figure is null
     */
    public Set<HistoricalFigure> getDescendants(HistoricalFigure figure) {
        Objects.requireNonNull(figure, "Starting figure cannot be null");
        Set<HistoricalFigure> descendants = new HashSet<>();
        dfs(figure, descendants);
        descendants.remove(figure); // Exclude self
        return descendants;
    }

    private void dfs(HistoricalFigure current, Set<HistoricalFigure> visited) {
        if (current == null || !visited.add(current)) return;
        List<HistoricalFigure> children = adjacencyList.get(current);
        if (children != null) {
            for (HistoricalFigure child : children) {
                dfs(child, visited);
            }
        }
    }
}
