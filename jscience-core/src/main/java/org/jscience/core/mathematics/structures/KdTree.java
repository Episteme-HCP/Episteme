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

package org.jscience.core.mathematics.structures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Generic K-d Tree implementation for organizing points in a k-dimensional space.
 * Useful for nearest neighbor searches.
 *
 * @param <T> The type of object stored, must carry coordinates.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class KdTree<T extends KdTree.KdNode> {

    public interface KdNode {
        double getCoordinate(int dimension);
        int getDimensionCount();
    }

    private Node root;
    private final int k;

    public KdTree(int k) {
        this.k = k;
    }

    public void insert(T item) {
        if (item.getDimensionCount() != k) {
            throw new IllegalArgumentException("Item dimension " + item.getDimensionCount() + " does not match tree dimension " + k);
        }
        root = insertRec(root, item, 0);
    }

    private Node insertRec(Node node, T item, int depth) {
        if (node == null) {
            return new Node(item);
        }

        int axis = depth % k;
        if (item.getCoordinate(axis) < node.item.getCoordinate(axis)) {
            node.left = insertRec(node.left, item, depth + 1);
        } else {
            node.right = insertRec(node.right, item, depth + 1);
        }

        return node;
    }

    public T findNearest(T target) {
        if (root == null) return null;
        return findNearestRec(root, target, root.item, 0);
    }

    private T findNearestRec(Node node, T target, T best, int depth) {
        if (node == null) return best;

        double d = distanceSquared(node.item, target);
        double bestDist = distanceSquared(best, target);

        if (d < bestDist) {
            best = node.item;
            bestDist = d;
        }

        int axis = depth % k;
        double diff = target.getCoordinate(axis) - node.item.getCoordinate(axis);
        
        Node near = diff < 0 ? node.left : node.right;
        Node far = diff < 0 ? node.right : node.left;

        best = findNearestRec(near, target, best, depth + 1);
        bestDist = distanceSquared(best, target); // update bestDist

        // Check if we need to search the other side
        if (diff * diff < bestDist) {
            best = findNearestRec(far, target, best, depth + 1);
        }

        return best;
    }

    private double distanceSquared(T p1, T p2) {
        double sum = 0;
        for (int i = 0; i < k; i++) {
            double d = p1.getCoordinate(i) - p2.getCoordinate(i);
            sum += d * d;
        }
        return sum;
    }
    
    /**
     * Rebalances the tree by rebuilding it from a list of nodes.
     * @param items List of all items to be in the tree
     */
    public void rebuild(List<T> items) {
        root = buildRec(items, 0);
    }
    
    private Node buildRec(List<T> items, int depth) {
        if (items.isEmpty()) return null;
        
        int axis = depth % k;
        // Sort by current axis
        items.sort(Comparator.comparingDouble(a -> a.getCoordinate(axis)));
        
        int mid = items.size() / 2;
        Node node = new Node(items.get(mid));
        
        node.left = buildRec(new ArrayList<>(items.subList(0, mid)), depth + 1);
        node.right = buildRec(new ArrayList<>(items.subList(mid + 1, items.size())), depth + 1);
        
        return node;
    }

    private class Node {
        T item;
        Node left, right;

        Node(T item) {
            this.item = item;
        }
    }
}
