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

package org.jscience.mathematics.geometry;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a region in 3D space using a Binary Space Partitioning (BSP) tree.
 * <p>
 * This is the core data structure for Constructive Solid Geometry (CSG).
 * Each node represents a partitioning plane.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class RegionBSPTree3D {

    private Node root;

    /**
     * Represents a node in the BSP tree.
     */
    private static class Node {
        Plane3D cut; // The hyperplane partitioning the space
        Node plus; // The subspace on the positive side of the plane (front)
        Node minus; // The subspace on the negative side of the plane (back)

        // Leaf attributes (if cut is null)
        boolean isInside; // True if this leaf represents volume inside the region

        Node(boolean isInside) {
            this.isInside = isInside;
        }

        Node(Plane3D cut, Node plus, Node minus) {
            this.cut = cut;
            this.plus = plus;
            this.minus = minus;
        }
    }

    /**
     * Creates an empty region (void).
     */
    public RegionBSPTree3D() {
        this.root = new Node(false);
    }

    /**
     * Creates a region representing the whole space.
     * 
     * @param full if true, represents the whole space; if false, represents empty
     *             space
     */
    public RegionBSPTree3D(boolean full) {
        this.root = new Node(full);
    }

    /**
     * Checks if a point is inside the region.
     * 
     * @param point the point to check
     * @return true if inside
     */
    public boolean contains(Vector3D point) {
        return containsRec(root, point);
    }

    private boolean containsRec(Node node, Vector3D point) {
        if (node.cut == null) {
            return node.isInside;
        }

        Real dist = node.cut.distance(point);
        if (dist.doubleValue() >= 0) { // On or in front
            return containsRec(node.plus, point);
        } else { // Behind
            return containsRec(node.minus, point);
        }
    }

    /**
     * Computes the union of this region and another.
     * 
     * @param other the other region
     * @return a new region representing the union
     */
    public RegionBSPTree3D union(RegionBSPTree3D other) {
        RegionBSPTree3D result = new RegionBSPTree3D();
        result.root = unionRec(this.root, other.root);
        return result;
    }

    private Node unionRec(Node a, Node b) {
        if (a.cut == null)
            return a.isInside ? a : b;
        if (b.cut == null)
            return b.isInside ? b : a;

        // Merge trees: split b by a's plane
        Node newPlus = unionRec(a.plus, b);
        Node newMinus = unionRec(a.minus, b);
        return new Node(a.cut, newPlus, newMinus);
    }

    /**
     * Computes the intersection of this region and another.
     * 
     * @param other the other region
     * @return a new region representing the intersection
     */
    public RegionBSPTree3D intersection(RegionBSPTree3D other) {
        RegionBSPTree3D result = new RegionBSPTree3D();
        result.root = intersectionRec(this.root, other.root);
        return result;
    }

    private Node intersectionRec(Node a, Node b) {
        if (a.cut == null)
            return a.isInside ? b : a;
        if (b.cut == null)
            return b.isInside ? a : b;

        // Merge trees for intersection
        Node newPlus = intersectionRec(a.plus, b);
        Node newMinus = intersectionRec(a.minus, b);
        return new Node(a.cut, newPlus, newMinus);
    }

    /**
     * Computes the difference of this region and another (this - other).
     * 
     * @param other the other region
     * @return a new region representing the difference
     */
    public RegionBSPTree3D difference(RegionBSPTree3D other) {
        // A - B = A ∩ ¬B
        RegionBSPTree3D notOther = new RegionBSPTree3D();
        notOther.root = negateRec(other.root);
        return intersection(notOther);
    }

    private Node negateRec(Node node) {
        if (node.cut == null) {
            return new Node(!node.isInside);
        }
        // Negate by swapping plus/minus and flipping plane
        return new Node(node.cut.negate(), negateRec(node.minus), negateRec(node.plus));
    }
}