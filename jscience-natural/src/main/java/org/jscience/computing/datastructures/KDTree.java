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

package org.jscience.computing.datastructures;

/**
 * A K-Dimensional Tree for efficient spatial searching.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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
        @SuppressWarnings("unused")
        T value;
        Node left, right;

        Node(double[] point, T value) {
            this.point = point;
            this.value = value;
        }
    }
}
