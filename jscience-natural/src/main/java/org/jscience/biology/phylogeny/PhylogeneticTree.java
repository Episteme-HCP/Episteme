package org.jscience.biology.phylogeny;

import java.util.*;

import org.jscience.mathematics.discrete.Tree;
import org.jscience.mathematics.discrete.RootedTree;

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
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class PhylogeneticTree {

    /**
     * Node in a phylogenetic tree with branch length support.
     */
    public static class Node {
        public final String name;
        public final double height; // Height from leaves (for UPGMA)
        public final double branchLength; // Branch length to parent
        public final Node left;
        public final Node right;

        /** Leaf node constructor */
        public Node(String name) {
            this.name = name;
            this.height = 0;
            this.branchLength = 0;
            this.left = null;
            this.right = null;
        }

        /** Internal node constructor for UPGMA */
        public Node(Node left, Node right, double height) {
            this.name = ".";
            this.left = left;
            this.right = right;
            this.height = height;
            this.branchLength = 0;
        }

        /** Internal node constructor with branch length */
        public Node(String name, Node left, Node right, double branchLength) {
            this.name = name;
            this.left = left;
            this.right = right;
            this.height = 0;
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
            double leftBranch = height - left.height;
            double rightBranch = height - right.height;
            return String.format("(%s:%.4f,%s:%.4f)", leftStr, leftBranch, rightStr, rightBranch);
        }
    }

    // ========== Tree Construction Algorithms ==========

    /**
     * Builds a phylogenetic tree using UPGMA algorithm.
     * <p>
     * UPGMA assumes a molecular clock (constant evolutionary rate).
     * </p>
     *
     * @param distanceMatrix pairwise distance matrix
     * @param labels         species/sequence labels
     * @return root node of the constructed tree
     */
    public static Node buildUPGMA(double[][] distanceMatrix, List<String> labels) {
        int n = labels.size();

        // Dynamic list of distances
        List<List<Double>> dists = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Double> row = new ArrayList<>();
            for (double val : distanceMatrix[i])
                row.add(val);
            dists.add(row);
        }

        List<Node> nodes = new ArrayList<>();
        List<Integer> sizes = new ArrayList<>();

        for (String label : labels) {
            nodes.add(new Node(label));
            sizes.add(1);
        }

        while (nodes.size() > 1) {
            // Find minimum distance
            double min = Double.MAX_VALUE;
            int minI = -1, minJ = -1;

            int sz = nodes.size();
            for (int i = 0; i < sz; i++) {
                for (int j = i + 1; j < sz; j++) {
                    double d = dists.get(i).get(j);
                    if (d < min) {
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

            Node parent = new Node(ni, nj, min / 2.0);

            // Calculate new distances using UPGMA formula
            List<Double> newRow = new ArrayList<>();
            for (int k = 0; k < sz; k++) {
                if (k == minI || k == minJ) {
                    newRow.add(0.0);
                } else {
                    double dik = dists.get(minI).get(k);
                    double djk = dists.get(minJ).get(k);
                    double dnew = (dik * sizeI + djk * sizeJ) / (sizeI + sizeJ);
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
            for (List<Double> row : dists) {
                row.remove(remove1);
                row.remove(remove2);
            }

            List<Double> cleanNewRow = new ArrayList<>();
            for (int k = 0; k < sz; k++) {
                if (k != minI && k != minJ) {
                    cleanNewRow.add(newRow.get(k));
                }
            }
            cleanNewRow.add(0.0);

            nodes.add(parent);
            sizes.add(sizeI + sizeJ);

            dists.add(cleanNewRow);

            for (int i = 0; i < dists.size() - 1; i++) {
                dists.get(i).add(cleanNewRow.get(i));
            }
        }

        return nodes.get(0);
    }

    /**
     * Builds a phylogenetic tree using Neighbor-Joining algorithm.
     * <p>
     * Neighbor-Joining does not assume a molecular clock and produces
     * unrooted trees with more accurate branch lengths.
     * </p>
     *
     * @param distanceMatrix pairwise distance matrix
     * @param labels         species/sequence labels
     * @return root node of the constructed tree
     */
    public static Node buildNeighborJoining(double[][] distanceMatrix, List<String> labels) {
        int n = labels.size();
        if (n < 2) {
            return n == 1 ? new Node(labels.get(0)) : null;
        }

        // Copy distance matrix
        double[][] D = new double[n][n];
        for (int i = 0; i < n; i++) {
            D[i] = Arrays.copyOf(distanceMatrix[i], n);
        }

        List<Node> nodes = new ArrayList<>();
        for (String label : labels) {
            nodes.add(new Node(label));
        }

        List<Integer> active = new ArrayList<>();
        for (int i = 0; i < n; i++)
            active.add(i);

        while (active.size() > 2) {
            int r = active.size();

            // Calculate row sums
            double[] S = new double[n];
            for (int i : active) {
                double sum = 0;
                for (int j : active) {
                    sum += D[i][j];
                }
                S[i] = sum;
            }

            // Find minimum Q(i,j)
            double minQ = Double.MAX_VALUE;
            int minI = -1, minJ = -1;
            for (int ii = 0; ii < r; ii++) {
                for (int jj = ii + 1; jj < r; jj++) {
                    int i = active.get(ii);
                    int j = active.get(jj);
                    double q = (r - 2) * D[i][j] - S[i] - S[j];
                    if (q < minQ) {
                        minQ = q;
                        minI = i;
                        minJ = j;
                    }
                }
            }

            // Calculate branch lengths
            double dij = D[minI][minJ];
            double branchI = 0.5 * dij + (S[minI] - S[minJ]) / (2 * (r - 2));
            double branchJ = dij - branchI;

            // Create new node
            Node newNode = new Node("(" + nodes.get(minI).name + "," + nodes.get(minJ).name + ")",
                    nodes.get(minI), nodes.get(minJ), 0);

            // Update distance matrix
            int newIdx = nodes.size();
            nodes.add(newNode);

            // Extend D matrix
            double[][] newD = new double[newIdx + 1][newIdx + 1];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    newD[i][j] = D[i][j];
                }
            }

            // Calculate distances to new node
            for (int k : active) {
                if (k != minI && k != minJ) {
                    double dnew = 0.5 * (D[minI][k] + D[minJ][k] - dij);
                    newD[newIdx][k] = dnew;
                    newD[k][newIdx] = dnew;
                }
            }
            newD[newIdx][newIdx] = 0;

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
            double d = D[i][j];
            return new Node(nodes.get(i), nodes.get(j), d / 2);
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

        return new Node(left, right, 0);
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
