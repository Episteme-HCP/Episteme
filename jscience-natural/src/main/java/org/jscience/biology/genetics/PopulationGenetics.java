package org.jscience.biology.genetics;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Models population genetics and evolutionary equilibrium.
 */
public final class PopulationGenetics {

    private PopulationGenetics() {}

    public record GenotypeFrequencies(
        double aa, // homozygous dominant
        double aa_alt, // heterozygous (Aa)
        double lower_aa // homozygous recessive
    ) {}

    public record AlleleFrequencies(
        double p, // dominant
        double q  // recessive
    ) {}

    /**
     * Hardy-Weinberg Equilibrium: p² + 2pq + q² = 1.
     * Calculates genotype frequencies from allele frequencies.
     */
    public static GenotypeFrequencies calculateHardyWeinberg(AlleleFrequencies alleles) {
        double p = alleles.p();
        double q = alleles.q();
        return new GenotypeFrequencies(p * p, 2 * p * q, q * q);
    }

    /**
     * Estimates allele frequencies from observed genotypes using maximum likelihood.
     */
    public static AlleleFrequencies estimateAlleles(GenotypeFrequencies observed) {
        double p = observed.aa() + 0.5 * observed.aa_alt();
        return new AlleleFrequencies(p, 1 - p);
    }

    /**
     * Calculates the Selection Coefficient 's'.
     * Fitness W = 1 - s.
     */
    public static Real selectionCoefficient(double fitness) {
        return Real.of(1.0 - fitness);
    }

    /**
     * Projects allele frequency change due to selection.
     * Δp = (p * q * [p(w11-w12) + q(w12-w22)]) / W_mean
     */
    public static List<AlleleFrequencies> simulateSelection(AlleleFrequencies initial,
            double wAA, double wAa, double waa, int generations) {
        
        List<AlleleFrequencies> history = new ArrayList<>();
        double p = initial.p();
        double q = initial.q();
        
        for (int i = 0; i < generations; i++) {
            history.add(new AlleleFrequencies(p, q));
            
            double wMean = p * p * wAA + 2 * p * q * wAa + q * q * waa;
            double pNext = (p * p * wAA + p * q * wAa) / wMean;
            
            p = pNext;
            q = 1 - p;
        }
        
        return history;
    }

    /**
     * Calculates Inbreeding Coefficient (F).
     * F = (He - Ho) / He
     */
    public static Real inbreedingCoefficient(double expectedHet, double observedHet) {
        if (expectedHet == 0) return Real.ZERO;
        return Real.of((expectedHet - observedHet) / expectedHet);
    }

    /**
     * Checks for genetic drift probability using Wright-Fisher model.
     * Variance in allele frequency Δp = p*q / 2N
     */
    public static Real driftVariance(AlleleFrequencies alleles, int populationSize) {
        double var = (alleles.p() * alleles.q()) / (2.0 * populationSize);
        return Real.of(var);
    }
}
