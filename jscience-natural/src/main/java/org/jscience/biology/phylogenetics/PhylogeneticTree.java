package org.jscience.biology.phylogenetics;

/**
 * Phylogenetic tree construction using UPGMA.
 */
public class PhylogeneticTree {

    private PhylogeneticTree() {
    }

    /**
     * UPGMA (Unweighted Pair Group Method with Arithmetic Mean).
     * Builds a tree from a distance matrix.
     * 
     * @param distanceMatrix NxN symmetric matrix of pairwise distances
     * @param labels         Species/sequence labels
     * @return Newick format tree string
     */
    public static String upgma(double[][] distanceMatrix, String[] labels) {
        int n = labels.length;
        if (distanceMatrix.length != n) {
            throw new IllegalArgumentException("Matrix size must match labels length");
        }

        // Working copies
        double[][] d = new double[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(distanceMatrix[i], 0, d[i], 0, n);
        }

        String[] nodes = labels.clone();
        int[] clusterSizes = new int[n];
        double[] heights = new double[n];
        for (int i = 0; i < n; i++) {
            clusterSizes[i] = 1;
            heights[i] = 0;
        }

        boolean[] active = new boolean[n];
        java.util.Arrays.fill(active, true);

        for (int step = 0; step < n - 1; step++) {
            // Find minimum distance
            double minDist = Double.MAX_VALUE;
            int minI = -1, minJ = -1;
            for (int i = 0; i < n; i++) {
                if (!active[i])
                    continue;
                for (int j = i + 1; j < n; j++) {
                    if (!active[j])
                        continue;
                    if (d[i][j] < minDist) {
                        minDist = d[i][j];
                        minI = i;
                        minJ = j;
                    }
                }
            }

            double newHeight = minDist / 2;
            double branchI = newHeight - heights[minI];
            double branchJ = newHeight - heights[minJ];

            // Create new node
            String newNode = String.format("(%s:%.4f,%s:%.4f)",
                    nodes[minI], branchI, nodes[minJ], branchJ);

            // Update distances (UPGMA formula)
            int sizeI = clusterSizes[minI];
            int sizeJ = clusterSizes[minJ];
            for (int k = 0; k < n; k++) {
                if (k == minI || k == minJ || !active[k])
                    continue;
                double newDist = (d[minI][k] * sizeI + d[minJ][k] * sizeJ) / (sizeI + sizeJ);
                d[minI][k] = newDist;
                d[k][minI] = newDist;
            }

            nodes[minI] = newNode;
            heights[minI] = newHeight;
            clusterSizes[minI] = sizeI + sizeJ;
            active[minJ] = false;
        }

        // Find final active node
        for (int i = 0; i < n; i++) {
            if (active[i])
                return nodes[i] + ";";
        }
        return "";
    }

    /**
     * Calculates Jukes-Cantor distance from fraction of differing sites.
     * d = -3/4 * ln(1 - 4p/3)
     */
    public static double jukesCantor(double fractionDifferent) {
        if (fractionDifferent >= 0.75)
            return Double.POSITIVE_INFINITY;
        return -0.75 * Math.log(1 - 4 * fractionDifferent / 3);
    }

    /**
     * Simple p-distance (fraction of differing positions).
     */
    public static double pDistance(String seq1, String seq2) {
        if (seq1.length() != seq2.length()) {
            throw new IllegalArgumentException("Sequences must be same length");
        }
        int diff = 0;
        for (int i = 0; i < seq1.length(); i++) {
            if (seq1.charAt(i) != seq2.charAt(i))
                diff++;
        }
        return (double) diff / seq1.length();
    }
}
