package org.jscience.mathematics.discrete.graph.flow;

import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.discrete.graph.Graph;
import java.util.*;

/**
 * Ford-Fulkerson algorithm for maximum flow.
 * <p>
 * Finds maximum flow from source to sink in flow network.
 * Uses DFS to find augmenting paths. O(E * max_flow) complexity.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class MaxFlow {

    /**
     * Computes maximum flow using Ford-Fulkerson with DFS.
     * 
     * @param graph  flow network
     * @param source source vertex
     * @param sink   sink vertex
     * @return maximum flow value
     */
    public static Real fordFulkerson(Graph<Real> graph, int source, int sink) {
        int n = graph.vertexCount();

        // Residual capacity graph
        Real[][] residual = new Real[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Real weight = graph.getWeight(i, j);
                residual[i][j] = (weight != null) ? weight : Real.ZERO;
            }
        }

        Real maxFlow = Real.ZERO;
        int[] parent = new int[n];

        // While there exists augmenting path
        while (hasAugmentingPath(residual, source, sink, parent)) {
            // Find minimum capacity along path
            Real pathFlow = Real.of(Double.MAX_VALUE);
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                if (residual[u][v].compareTo(pathFlow) < 0) {
                    pathFlow = residual[u][v];
                }
            }

            // Update residual capacities
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                residual[u][v] = residual[u][v].subtract(pathFlow);
                residual[v][u] = residual[v][u].add(pathFlow);
            }

            maxFlow = maxFlow.add(pathFlow);
        }

        return maxFlow;
    }

    /**
     * Finds augmenting path using DFS.
     */
    private static boolean hasAugmentingPath(Real[][] residual, int source, int sink, int[] parent) {
        int n = residual.length;
        boolean[] visited = new boolean[n];
        Arrays.fill(parent, -1);

        Stack<Integer> stack = new Stack<>();
        stack.push(source);
        visited[source] = true;

        while (!stack.isEmpty()) {
            int u = stack.pop();

            if (u == sink) {
                return true;
            }

            for (int v = 0; v < n; v++) {
                if (!visited[v] && residual[u][v].compareTo(Real.ZERO) > 0) {
                    visited[v] = true;
                    parent[v] = u;
                    stack.push(v);
                }
            }
        }

        return false;
    }

    /**
     * Edmonds-Karp algorithm (Ford-Fulkerson with BFS).
     * <p>
     * O(VE²) complexity - polynomial time.
     * Guaranteed to terminate for irrational capacities.
     * </p>
     */
    public static Real edmondsKarp(Graph<Real> graph, int source, int sink) {
        int n = graph.vertexCount();

        Real[][] residual = new Real[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Real weight = graph.getWeight(i, j);
                residual[i][j] = (weight != null) ? weight : Real.ZERO;
            }
        }

        Real maxFlow = Real.ZERO;
        int[] parent = new int[n];

        while (bfs(residual, source, sink, parent)) {
            // Find minimum capacity
            Real pathFlow = Real.of(Double.MAX_VALUE);
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                if (residual[u][v].compareTo(pathFlow) < 0) {
                    pathFlow = residual[u][v];
                }
            }

            // Update residual graph
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                residual[u][v] = residual[u][v].subtract(pathFlow);
                residual[v][u] = residual[v][u].add(pathFlow);
            }

            maxFlow = maxFlow.add(pathFlow);
        }

        return maxFlow;
    }

    private static boolean bfs(Real[][] residual, int source, int sink, int[] parent) {
        int n = residual.length;
        boolean[] visited = new boolean[n];
        Arrays.fill(parent, -1);

        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);
        visited[source] = true;

        while (!queue.isEmpty()) {
            int u = queue.poll();

            for (int v = 0; v < n; v++) {
                if (!visited[v] && residual[u][v].compareTo(Real.ZERO) > 0) {
                    visited[v] = true;
                    parent[v] = u;
                    queue.add(v);

                    if (v == sink) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
