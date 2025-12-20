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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.discrete;

import java.util.*;

/**
 * A mutable implementation of a rooted tree.
 * <p>
 * This implementation stores parent-child relationships efficiently and
 * supports dynamic tree construction. Suitable for:
 * <ul>
 * <li>Phylogenetic tree construction (UPGMA, Neighbor-Joining)</li>
 * <li>Decision tree building</li>
 * <li>Hierarchical clustering</li>
 * <li>Parse tree construction</li>
 * </ul>
 * </p>
 *
 * @param <V> the type of vertices (nodes)
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class RootedTree<V> implements Tree<V> {

    private V root;
    private final Map<V, V> parentMap = new HashMap<>();
    private final Map<V, List<V>> childrenMap = new HashMap<>();
    private final Set<V> vertices = new LinkedHashSet<>();

    /**
     * Creates an empty tree.
     */
    public RootedTree() {
    }

    /**
     * Creates a tree with the given root.
     *
     * @param root the root vertex
     */
    public RootedTree(V root) {
        this.root = root;
        vertices.add(root);
        childrenMap.put(root, new ArrayList<>());
    }

    /**
     * Sets the root of this tree.
     *
     * @param root the new root
     */
    public void setRoot(V root) {
        if (!vertices.contains(root)) {
            vertices.add(root);
            childrenMap.put(root, new ArrayList<>());
        }
        this.root = root;
    }

    /**
     * Adds a child to a parent vertex.
     *
     * @param parent the parent vertex
     * @param child  the child vertex to add
     */
    public void addChild(V parent, V child) {
        if (!vertices.contains(parent)) {
            vertices.add(parent);
            childrenMap.put(parent, new ArrayList<>());
        }
        vertices.add(child);
        parentMap.put(child, parent);
        childrenMap.computeIfAbsent(parent, k -> new ArrayList<>()).add(child);
        childrenMap.putIfAbsent(child, new ArrayList<>());
    }

    @Override
    public Optional<V> getRoot() {
        return Optional.ofNullable(root);
    }

    @Override
    public Optional<V> getParent(V vertex) {
        return Optional.ofNullable(parentMap.get(vertex));
    }

    @Override
    public List<V> getChildren(V vertex) {
        return childrenMap.getOrDefault(vertex, Collections.emptyList());
    }

    @Override
    public Set<V> vertices() {
        return Collections.unmodifiableSet(vertices);
    }

    @Override
    public Set<Edge<V>> edges() {
        Set<Edge<V>> edgeSet = new HashSet<>();
        for (Map.Entry<V, V> entry : parentMap.entrySet()) {
            V child = entry.getKey();
            V parent = entry.getValue();
            edgeSet.add(new Edge<V>() {
                @Override
                public V source() {
                    return parent;
                }

                @Override
                public V target() {
                    return child;
                }
            });
        }
        return edgeSet;
    }

    @Override
    public boolean addVertex(V vertex) {
        if (vertices.contains(vertex))
            return false;
        vertices.add(vertex);
        childrenMap.put(vertex, new ArrayList<>());
        return true;
    }

    @Override
    public boolean addEdge(V source, V target) {
        addChild(source, target);
        return true;
    }

    @Override
    public Set<V> neighbors(V vertex) {
        Set<V> neighbors = new HashSet<>();
        getParent(vertex).ifPresent(neighbors::add);
        neighbors.addAll(getChildren(vertex));
        return neighbors;
    }

    @Override
    public int degree(V vertex) {
        return neighbors(vertex).size();
    }

    @Override
    public int vertexCount() {
        return vertices.size();
    }

    /**
     * Returns the number of edges in the tree.
     * For a tree with n vertices, this is always n-1.
     *
     * @return number of edges
     */
    public int edgeCount() {
        return Math.max(0, vertices.size() - 1);
    }

    /**
     * Returns the subtree rooted at the given vertex.
     *
     * @param vertex the root of the subtree
     * @return a new tree containing the subtree
     */
    public RootedTree<V> subtree(V vertex) {
        RootedTree<V> sub = new RootedTree<>(vertex);
        Queue<V> queue = new LinkedList<>();
        queue.add(vertex);
        while (!queue.isEmpty()) {
            V current = queue.poll();
            for (V child : getChildren(current)) {
                sub.addChild(current, child);
                queue.add(child);
            }
        }
        return sub;
    }

    @Override
    public String toString() {
        if (root == null)
            return "EmptyTree";
        return toNewick(root);
    }

    /**
     * Returns Newick format representation.
     */
    private String toNewick(V node) {
        List<V> children = getChildren(node);
        if (children.isEmpty()) {
            return node.toString();
        }
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < children.size(); i++) {
            if (i > 0)
                sb.append(",");
            sb.append(toNewick(children.get(i)));
        }
        sb.append(")").append(node.toString());
        return sb.toString();
    }
}
