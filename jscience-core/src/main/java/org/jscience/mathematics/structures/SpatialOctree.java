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

package org.jscience.mathematics.structures;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Generic Spatial Octree structure for high-performance spatial partitioning.
 * 
 * @param <T> The type of objects stored in the octree, must implement SpatialObject.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SpatialOctree<T extends SpatialOctree.SpatialObject> {

    /**
     * Interface for objects that can be stored in a SpatialOctree.
     */
    public interface SpatialObject {
        double getX();
        double getY();
        double getZ();
        double getMassValue();
    }

    private final Node<T> root;

    /**
     * Creates a new Spatial Octree with given bounds.
     * 
     * @param centerX Center X coordinate
     * @param centerY Center Y coordinate
     * @param centerZ Center Z coordinate
     * @param size Size of the cube (edge length)
     */
    public SpatialOctree(Real centerX, Real centerY, Real centerZ, Real size) {
        this.root = new Node<>(centerX, centerY, centerZ, size);
    }

    /**
     * Inserts an object into the octree.
     * 
     * @param object The object to insert
     */
    public void insert(T object) {
        root.insert(object);
    }

    /**
     * Recursively computes the center of mass for all nodes.
     */
    public void computeCenterOfMass() {
        root.computeCenterOfMass();
    }

    /**
     * Returns the root node of the octree.
     * 
     * @return The root node
     */
    public Node<T> getRoot() {
        return root;
    }

    /**
     * Represents a node in the Spatial Octree.
     */
    public static class Node<T extends SpatialObject> {
        public Real centerX, centerY, centerZ;
        public Real size;
        public Real mass;
        public Real comX, comY, comZ;
        public T object;
        public Node<T>[] children;

        @SuppressWarnings("unchecked")
        Node(Real x, Real y, Real z, Real s) {
            this.centerX = x;
            this.centerY = y;
            this.centerZ = z;
            this.size = s;
            this.mass = Real.ZERO;
            this.comX = Real.ZERO;
            this.comY = Real.ZERO;
            this.comZ = Real.ZERO;
            this.children = (Node<T>[]) new Node[8];
        }

        public boolean isLeaf() {
            for (Node<T> ch : children)
                if (ch != null)
                    return false;
            return true;
        }

        private int octant(T p) {
            int o = 0;
            if (Real.of(p.getX()).compareTo(centerX) > 0) o |= 1;
            if (Real.of(p.getY()).compareTo(centerY) > 0) o |= 2;
            if (Real.of(p.getZ()).compareTo(centerZ) > 0) o |= 4;
            return o;
        }

        void insert(T p) {
            if (mass.equals(Real.ZERO) && object == null) {
                object = p;
                mass = Real.of(p.getMassValue());
                comX = Real.of(p.getX());
                comY = Real.of(p.getY());
                comZ = Real.of(p.getZ());
            } else if (object != null) {
                T e = object;
                object = null;
                insertChild(e);
                insertChild(p);
            } else {
                insertChild(p);
            }
        }

        private void insertChild(T p) {
            int o = octant(p);
            if (children[o] == null) {
                Real h = size.divide(Real.TWO);
                Real offset = h.divide(Real.TWO);
                Real nx = centerX.add(((o & 1) != 0) ? offset : offset.negate());
                Real ny = centerY.add(((o & 2) != 0) ? offset : offset.negate());
                Real nz = centerZ.add(((o & 4) != 0) ? offset : offset.negate());
                children[o] = new Node<>(nx, ny, nz, h);
            }
            children[o].insert(p);
        }

        void computeCenterOfMass() {
            if (object != null) {
                mass = Real.of(object.getMassValue());
                comX = Real.of(object.getX());
                comY = Real.of(object.getY());
                comZ = Real.of(object.getZ());
            } else {
                mass = Real.ZERO;
                comX = Real.ZERO;
                comY = Real.ZERO;
                comZ = Real.ZERO;
                for (Node<T> ch : children) {
                    if (ch != null) {
                        ch.computeCenterOfMass();
                        mass = mass.add(ch.mass);
                        comX = comX.add(ch.mass.multiply(ch.comX));
                        comY = comY.add(ch.mass.multiply(ch.comY));
                        comZ = comZ.add(ch.mass.multiply(ch.comZ));
                    }
                }
                if (mass.compareTo(Real.ZERO) > 0) {
                    comX = comX.divide(mass);
                    comY = comY.divide(mass);
                    comZ = comZ.divide(mass);
                }
            }
        }
    }
}
