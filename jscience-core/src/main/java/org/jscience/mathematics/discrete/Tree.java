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

package org.jscience.mathematics.discrete;

import java.util.List;
import java.util.Optional;

/**
 * Represents a tree data structure - a connected acyclic graph.
 * <p>
 * A tree is a graph where any two vertices are connected by exactly one path.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Tree<V> extends Graph<V> {

    /**
     * Returns the root of this tree.
     *
     * @return the root vertex, or empty if the tree is unrooted or empty
     */
    Optional<V> getRoot();

    /**
     * Returns the parent of the given vertex.
     *
     * @param vertex the vertex to find the parent of
     * @return the parent vertex, or empty if vertex is root
     */
    Optional<V> getParent(V vertex);

    /**
     * Returns all children of the given vertex.
     *
     * @param vertex the parent vertex
     * @return list of child vertices (empty if leaf)
     */
    List<V> getChildren(V vertex);

    /**
     * Checks if the given vertex is a leaf (has no children).
     *
     * @param vertex the vertex to check
     * @return true if vertex has no children
     */
    default boolean isLeaf(V vertex) {
        return getChildren(vertex).isEmpty();
    }

    /**
     * Checks if the given vertex is the root.
     *
     * @param vertex the vertex to check
     * @return true if vertex is the root
     */
    default boolean isRoot(V vertex) {
        return getRoot().map(r -> r.equals(vertex)).orElse(false);
    }

    /**
     * Returns the depth of a vertex (distance from root).
     *
     * @param vertex the vertex
     * @return depth (0 for root)
     */
    default int depth(V vertex) {
        int d = 0;
        Optional<V> current = Optional.of(vertex);
        while (current.isPresent() && !isRoot(current.get())) {
            current = getParent(current.get());
            d++;
        }
        return d;
    }

    /**
     * Returns the height of the tree (maximum depth).
     *
     * @return the height of the tree
     */
    default int height() {
        return vertices().stream()
                .filter(this::isLeaf)
                .mapToInt(this::depth)
                .max()
                .orElse(0);
    }

    /**
     * Returns all leaf vertices.
     *
     * @return list of leaf vertices
     */
    default List<V> getLeaves() {
        return vertices().stream()
                .filter(this::isLeaf)
                .toList();
    }

    /**
     * Returns all ancestors of a vertex (path to root).
     *
     * @param vertex the vertex
     * @return list of ancestors from parent to root
     */
    default List<V> getAncestors(V vertex) {
        java.util.List<V> ancestors = new java.util.ArrayList<>();
        Optional<V> current = getParent(vertex);
        while (current.isPresent()) {
            ancestors.add(current.get());
            current = getParent(current.get());
        }
        return ancestors;
    }

    /**
     * Returns all descendants of a vertex.
     *
     * @param vertex the vertex
     * @return list of all descendants
     */
    default List<V> getDescendants(V vertex) {
        java.util.List<V> descendants = new java.util.ArrayList<>();
        java.util.Queue<V> queue = new java.util.LinkedList<>(getChildren(vertex));
        while (!queue.isEmpty()) {
            V current = queue.poll();
            descendants.add(current);
            queue.addAll(getChildren(current));
        }
        return descendants;
    }

    /**
     * Finds the lowest common ancestor of two vertices.
     *
     * @param v1 first vertex
     * @param v2 second vertex
     * @return the LCA, or empty if not found
     */
    default Optional<V> lowestCommonAncestor(V v1, V v2) {
        java.util.Set<V> ancestors1 = new java.util.HashSet<>(getAncestors(v1));
        ancestors1.add(v1);

        Optional<V> current = Optional.of(v2);
        while (current.isPresent()) {
            if (ancestors1.contains(current.get())) {
                return current;
            }
            current = getParent(current.get());
        }
        return Optional.empty();
    }

    /**
     * Pre-order traversal of the tree.
     *
     * @param visitor the visitor to call for each vertex
     */
    default void preOrder(java.util.function.Consumer<V> visitor) {
        getRoot().ifPresent(root -> preOrderFrom(root, visitor));
    }

    /**
     * Pre-order traversal starting from a vertex.
     */
    default void preOrderFrom(V vertex, java.util.function.Consumer<V> visitor) {
        visitor.accept(vertex);
        for (V child : getChildren(vertex)) {
            preOrderFrom(child, visitor);
        }
    }

    /**
     * Post-order traversal of the tree.
     *
     * @param visitor the visitor to call for each vertex
     */
    default void postOrder(java.util.function.Consumer<V> visitor) {
        getRoot().ifPresent(root -> postOrderFrom(root, visitor));
    }

    /**
     * Post-order traversal starting from a vertex.
     */
    default void postOrderFrom(V vertex, java.util.function.Consumer<V> visitor) {
        for (V child : getChildren(vertex)) {
            postOrderFrom(child, visitor);
        }
        visitor.accept(vertex);
    }

    /**
     * Returns this tree as a graph (already is a graph).
     */
    @Override
    default boolean isDirected() {
        return false; // Trees are inherently undirected graphs
    }
}