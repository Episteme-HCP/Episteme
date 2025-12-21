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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jscience.biology.phylogeny;

import java.util.*;

import org.jscience.mathematics.discrete.Tree;
import org.jscience.mathematics.discrete.RootedTree;
import org.jscience.biology.taxonomy.Species;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Phylogenetic tree construction and representation.
 * <p>
 * Supports multiple tree construction algorithms:
 * <ul>
 * <li>UPGMA (Unweighted Pair Group Method with Arithmetic Mean)</li>
 * <li>Neighbor-Joining</li>
 * </ul>
 * </p>
 * <p>
 * Integrates with the JScience {@link Tree} interface for graph-theoretic
 * operations.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PhylogeneticTree {

    /**
     * Node in a phylogenetic tree with branch length support.
     */
    public static class Node {
        public final String name;
        public final Species species; // Linked Species object (optional)
        public final Real height; // Height from leaves (for UPGMA)
        public final Real branchLength; // Branch length to parent
        public final Node left;
        public final Node right;

        /** Leaf node constructor from Name */
        public Node(String name) {
            this.name = name;
            this.species = null;
            this.height = Real.ZERO;
            this.branchLength = Real.ZERO;
            this.left = null;
            this.right = null;
        }

        /** Leaf node constructor from Species */
        public Node(Species species) {
            this.name = species.getScientificName();
            this.species = species;
            this.height = Real.ZERO;
            this.branchLength = Real.ZERO;
            this.left = null;
            this.right = null;
        }

        /** Internal node constructor for UPGMA */
        public Node(Node left, Node right, Real height) {
            this.name = ".";
            this.species = null;
            this.left = left;
            this.right = right;
            this.height = height;
            this.branchLength = Real.ZERO;
        }

        /** Internal node constructor with branch length */
        public Node(String name, Node left, Node right, Real branchLength) {
            this.name = name;
            this.species = null;
            this.left = left;
            this.right = right;
            this.height = Real.ZERO;
            this.branchLength = branchLength;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        @Override
        public String toString() {
            return toNewick();
        }

        public String toNewick() {
            if (isLeaf()) {
                return name;
            }
            String leftStr = left.toNewick();
            String rightStr = right.toNewick();
            Real leftBranch = height.subtract(left.height);
            Real rightBranch = height.subtract(right.height);
            return String.format("(%s:%.4f,%s:%.4f)", leftStr, leftBranch.doubleValue(), rightStr,
                    rightBranch.doubleValue());
        }
    }

    // ========== Tree Construction Algorithms ==========

    public static Node buildUPGMA(Real[][] distanceMatrix, List<Species> speciesList) {
        List<Node> nodes = new ArrayList<>();
        for (Species s : speciesList) {
            nodes.add(new Node(s));
        }
        return runUPGMA(distanceMatrix, nodes);
    }

    /**
     * Builds a phylogenetic tree using UPGMA algorithm (String labels).
     */
    public static Node buildUPGMAFromLabels(Real[][] distanceMatrix, List<String> labels) {
        List<Node> nodes = new ArrayList<>();
        for (String label : labels) {
            nodes.add(new Node(label));
        }
        return runUPGMA(distanceMatrix, nodes);
    }

    private static Node runUPGMA(Real[][] distanceMatrix, List<Node> inputNodes) {
        int n = inputNodes.size();

        // Dynamic list of distances
        List<List<Real>> dists = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Real> row = new ArrayList<>();
            for (Real val : distanceMatrix[i])
                row.add(val);
            dists.add(row);
        }

        List<Node> nodes = new ArrayList<>(inputNodes);
        List<Integer> sizes = new ArrayList<>();
        for (int i = 0; i < n; i++)
            sizes.add(1);

        while (nodes.size() > 1) {
            // Find minimum distance
            Real min = Real.POSITIVE_INFINITY;
            int minI = -1, minJ = -1;

            int sz = nodes.size();
            for (int i = 0; i < sz; i++) {
                for (int j = i + 1; j < sz; j++) {
                    Real d = dists.get(i).get(j);
                    if (d.compareTo(min) < 0) {
                        min = d;
                        minI = i;
                        minJ = j;
                    }
                }
            }

            // Merge I and J
            Node ni = nodes.get(minI);
            Node nj = nodes.get(minJ);
            int sizeI = sizes.get(minI);
            int sizeJ = sizes.get(minJ);

            Node parent = new Node(ni, nj, min.divide(Real.TWO));

            // Calculate new distances using UPGMA formula
            List<Real> newRow = new ArrayList<>();
            for (int k = 0; k < sz; k++) {
                if (k == minI || k == minJ) {
                    newRow.add(Real.ZERO);
                } else {
                    Real dik = dists.get(minI).get(k);
                    Real djk = dists.get(minJ).get(k);

                    // (dik * sizeI + djk * sizeJ) / (sizeI + sizeJ)
                    Real sumI = dik.multiply(Real.of(sizeI));
                    Real sumJ = djk.multiply(Real.of(sizeJ));
                    Real totalSize = Real.of(sizeI + sizeJ);

                    Real dnew = sumI.add(sumJ).divide(totalSize);
                    newRow.add(dnew);
                }
            }

            // Update structures
            int remove1 = Math.max(minI, minJ);
            int remove2 = Math.min(minI, minJ);

            nodes.remove(remove1);
            nodes.remove(remove2);
            sizes.remove(remove1);
            sizes.remove(remove2);

            dists.remove(remove1);
            dists.remove(remove2);
            for (List<Real> row : dists) {
                row.remove(remove1);
                row.remove(remove2);
            }

            List<Real> cleanNewRow = new ArrayList<>();
            for (int k = 0; k < sz; k++) {
                if (k != minI && k != minJ) {
                    cleanNewRow.add(newRow.get(k));
                }
            }
            cleanNewRow.add(Real.ZERO);

            nodes.add(parent);
            sizes.add(sizeI + sizeJ);

            dists.add(cleanNewRow);

            for (int i = 0; i < dists.size() - 1; i++) {
                dists.get(i).add(cleanNewRow.get(i));
            }
        }

        return nodes.get(0);
    }

    public static Node buildNeighborJoining(Real[][] distanceMatrix, List<Species> speciesList) {
        List<Node> nodes = new ArrayList<>();
        for (Species s : speciesList)
            nodes.add(new Node(s));
        return runNeighborJoining(distanceMatrix, nodes);
    }

    /**
     * Builds a phylogenetic tree using Neighbor-Joining algorithm (String labels).
     */
    public static Node buildNeighborJoiningFromLabels(Real[][] distanceMatrix, List<String> labels) {
        List<Node> nodes = new ArrayList<>();
        for (String label : labels)
            nodes.add(new Node(label));
        return runNeighborJoining(distanceMatrix, nodes);
    }

    private static Node runNeighborJoining(Real[][] distanceMatrix, List<Node> inputNodes) {
        int n = inputNodes.size();
        if (n < 2) {
            return n == 1 ? inputNodes.get(0) : null;
        }

        // Copy distance matrix
        Real[][] D = new Real[n][n];
        for (int i = 0; i < n; i++) {
            D[i] = Arrays.copyOf(distanceMatrix[i], n);
        }

        List<Node> nodes = new ArrayList<>(inputNodes);
        List<Integer> active = new ArrayList<>();
        for (int i = 0; i < n; i++)
            active.add(i);

        while (active.size() > 2) {
            int r = active.size();
            Real factor = Real.of(r - 2);

            // Calculate row sums
            Real[] S = new Real[n];
            for (int i : active) {
                Real sum = Real.ZERO;
                for (int j : active) {
                    sum = sum.add(D[i][j]);
                }
                S[i] = sum;
            }

            // Find minimum Q(i,j)
            Real minQ = Real.POSITIVE_INFINITY;
            int minI = -1, minJ = -1;

            for (int ii = 0; ii < r; ii++) {
                for (int jj = ii + 1; jj < r; jj++) {
                    int i = active.get(ii);
                    int j = active.get(jj);
                    // (r - 2) * D[i][j] - S[i] - S[j]
                    Real q = factor.multiply(D[i][j]).subtract(S[i]).subtract(S[j]);

                    if (q.compareTo(minQ) < 0) {
                        minQ = q;
                        minI = i;
                        minJ = j;
                    }
                }
            }

            // Calculate branch lengths
            Real dij = D[minI][minJ];
            // 0.5 * dij + (S[minI] - S[minJ]) / (2 * (r - 2))
            Real term1 = dij.divide(Real.TWO);
            Real term2 = S[minI].subtract(S[minJ]).divide(Real.TWO.multiply(factor));

            Real branchI = term1.add(term2);
            dij.subtract(branchI);

            // Create new node
            Node newNode = new Node("(" + nodes.get(minI).name + "," + nodes.get(minJ).name + ")",
                    nodes.get(minI), nodes.get(minJ), Real.ZERO);

            // Update distance matrix
            int newIdx = nodes.size();
            nodes.add(newNode);

            // Extend D matrix
            Real[][] newD = new Real[newIdx + 1][newIdx + 1];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    newD[i][j] = D[i][j];
                }
            }

            // Calculate distances to new node
            for (int k : active) {
                if (k != minI && k != minJ) {
                    // 0.5 * (D[minI][k] + D[minJ][k] - dij)
                    Real dnew = D[minI][k].add(D[minJ][k]).subtract(dij).divide(Real.TWO);
                    newD[newIdx][k] = dnew;
                    newD[k][newIdx] = dnew;
                }
            }
            newD[newIdx][newIdx] = Real.ZERO;

            D = newD;
            n = newIdx + 1;

            // Update active list
            active.remove(Integer.valueOf(minI));
            active.remove(Integer.valueOf(minJ));
            active.add(newIdx);
        }

        // Join last two nodes
        if (active.size() == 2) {
            int i = active.get(0);
            int j = active.get(1);
            Real d = D[i][j];
            return new Node(nodes.get(i), nodes.get(j), d.divide(Real.TWO));
        }

        return nodes.get(active.get(0));
    }

    // ========== Tree Interface Bridge ==========

    /**
     * Converts a phylogenetic tree to a {@link Tree} interface for graph
     * operations.
     *
     * @param root the root node
     * @return a Tree implementation
     */
    public static Tree<Node> asTree(Node root) {
        RootedTree<Node> tree = new RootedTree<>(root);
        buildTreeRecursive(tree, root);
        return tree;
    }

    private static void buildTreeRecursive(RootedTree<Node> tree, Node node) {
        if (node.left != null) {
            tree.addChild(node, node.left);
            buildTreeRecursive(tree, node.left);
        }
        if (node.right != null) {
            tree.addChild(node, node.right);
            buildTreeRecursive(tree, node.right);
        }
    }

    /**
     * Parses a Newick format string into a phylogenetic tree.
     *
     * @param newick the Newick format string
     * @return root node of the parsed tree
     */
    public static Node parseNewick(String newick) {
        return parseNewickRecursive(newick.trim().replaceAll(";$", ""));
    }

    private static Node parseNewickRecursive(String s) {
        if (!s.startsWith("(")) {
            // Leaf node, possibly with branch length
            String[] parts = s.split(":");
            return new Node(parts[0]);
        }

        // Find matching parentheses
        int depth = 0;
        int commaPos = -1;
        for (int i = 1; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(')
                depth++;
            else if (c == ')')
                depth--;
            else if (c == ',' && depth == 0) {
                commaPos = i;
                break;
            }
        }

        if (commaPos < 0) {
            throw new IllegalArgumentException("Invalid Newick format: " + s);
        }

        String leftStr = s.substring(1, commaPos);
        int closePos = s.lastIndexOf(')');
        String rightStr = s.substring(commaPos + 1, closePos);

        Node left = parseNewickRecursive(leftStr.split(":")[0]);
        Node right = parseNewickRecursive(rightStr.split(":")[0]);

        return new Node(left, right, Real.ZERO);
    }

    /**
     * Counts the number of leaves in the tree.
     */
    public static int countLeaves(Node node) {
        if (node.isLeaf())
            return 1;
        return countLeaves(node.left) + countLeaves(node.right);
    }

    /**
     * Gets all leaf names from the tree.
     */
    public static List<String> getLeafNames(Node node) {
        List<String> names = new ArrayList<>();
        collectLeafNames(node, names);
        return names;
    }

    private static void collectLeafNames(Node node, List<String> names) {
        if (node.isLeaf()) {
            names.add(node.name);
        } else {
            if (node.left != null)
                collectLeafNames(node.left, names);
            if (node.right != null)
                collectLeafNames(node.right, names);
        }
    }
}
