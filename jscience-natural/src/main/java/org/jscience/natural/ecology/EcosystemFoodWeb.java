package org.jscience.natural.ecology;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Models ecosystem food webs and energy flow.
 */
public final class EcosystemFoodWeb {

    private EcosystemFoodWeb() {}

    /**
     * Lindeman's Efficiency (10% rule).
     * Energy at Nth level = Initial * 0.1^(N-1)
     */
    public static Real energyAtTrophicLevel(double initialEnergy, int level) {
        return Real.of(initialEnergy * Math.pow(0.1, level - 1));
    }

    /**
     * Biodiversity Index (Shannon-Wiener).
     * H = -Sum(pi * ln(pi))
     */
    public static Real shannonIndex(double[] speciesCounts) {
        double total = Arrays.stream(speciesCounts).sum();
        double h = 0;
        for (double c : speciesCounts) {
            double p = c / total;
            if (p > 0) h -= p * Math.log(p);
        }
        return Real.of(h);
    }
}
