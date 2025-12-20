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
public class RegionBSPTree {

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

    }

    /**
     * Creates an empty region (void).
     */
    public RegionBSPTree() {
        this.root = new Node(false);
    }

    /**
     * Creates a region representing the whole space.
     * 
     * @param full if true, represents the whole space; if false, represents empty
     *             space
     */
    public RegionBSPTree(boolean full) {
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
    /**
     * Computes the union of this region and another.
     * 
     * @param other the other region
     * @return a new region representing the union
     */
    public RegionBSPTree union(RegionBSPTree other) {
        // Trivial optimizations
        if (this.root.cut == null && this.root.isInside)
            return this;
        if (other.root.cut == null && other.root.isInside)
            return other;
        if (this.root.cut == null && !this.root.isInside)
            return other;
        if (other.root.cut == null && !other.root.isInside)
            return this;

        // Full CSG implementation requires merging trees
        throw new UnsupportedOperationException("CSG Union for complex shapes not yet implemented");
    }

    /**
     * Computes the intersection of this region and another.
     * 
     * @param other the other region
     * @return a new region representing the intersection
     */
    public RegionBSPTree intersection(RegionBSPTree other) {
        if (this.root.cut == null && this.root.isInside)
            return other;
        if (other.root.cut == null && other.root.isInside)
            return this;
        if (this.root.cut == null && !this.root.isInside)
            return this;
        if (other.root.cut == null && !other.root.isInside)
            return other;

        throw new UnsupportedOperationException("CSG Intersection for complex shapes not yet implemented");
    }

    /**
     * Computes the difference of this region and another (this - other).
     * 
     * @param other the other region
     * @return a new region representing the difference
     */
    public RegionBSPTree difference(RegionBSPTree other) {
        if (other.root.cut == null && !other.root.isInside)
            return this; // A - Empty = A
        if (other.root.cut == null && other.root.isInside)
            return new RegionBSPTree(false); // A - Full = Empty

        throw new UnsupportedOperationException("CSG Difference for complex shapes not yet implemented");
    }
}
