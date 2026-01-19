package org.jscience.history;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

/**
 * Directed graph structure for historical lineages and successions.
 */
public final class LineageGraph {

    private final Map<HistoricalFigure, List<HistoricalFigure>> adjacencyList = new HashMap<>();

    /**
     * Adds a lineage link (e.g., Parent to Child, Predecessor to Successor).
     */
    public void addLink(HistoricalFigure from, HistoricalFigure to) {
        adjacencyList.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
    }

    /**
     * Finds all descendants of a figure using Depth-First Search.
     */
    public Set<HistoricalFigure> getDescendants(HistoricalFigure figure) {
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
