package org.jscience.sociology;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Measures social capital and network density.
 */
public final class SocialCapitalModel {

    private SocialCapitalModel() {}

    /**
     * Calculates Network Density: Actual Connections / Possible Connections.
     */
    public static Real calculateDensity(int nodes, int edges, boolean directed) {
        if (nodes < 2) return Real.ZERO;
        double possible = directed ? nodes * (nodes - 1) : nodes * (nodes - 1) / 2.0;
        return Real.of(edges / possible);
    }

    /**
     * Calculates Trust Index based on reciprocity.
     */
    public static Real reciprocityIndex(int mutualLinks, int totalLinks) {
        if (totalLinks == 0) return Real.ZERO;
        return Real.of((double) 2 * mutualLinks / totalLinks);
    }
}
