package org.jscience.computing.datastructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A K-Dimensional Tree for efficient spatial searching.
 * 
 * @param <T> The type of data stored in the tree.
 * @author Silvere Martin-Michiellot
 * @since 5.0
 */
public class KDTree<T> {

    private final int k;
    private Node root;

    public KDTree(int k) {
        this.k = k;
    }

    public void insert(double[] point, T value) {
        if (point.length != k) {
            throw new IllegalArgumentException("Point dimensionality must match tree k=" + k);
        }
        root = insert(root, point, value, 0);
    }

    private Node insert(Node node, double[] point, T value, int depth) {
        if (node == null) {
            return new Node(point, value);
        }

        int axis = depth % k;
        if (point[axis] < node.point[axis]) {
            node.left = insert(node.left, point, value, depth + 1);
        } else {
            node.right = insert(node.right, point, value, depth + 1);
        }
        return node;
    }

    /**
     * Simple nearest neighbor search (approximate start).
     */
    // public T findNearest(double[] target) { ... }

    private class Node {
        double[] point;
        T value;
        Node left, right;

        Node(double[] point, T value) {
            this.point = point;
            this.value = value;
        }
    }
}
