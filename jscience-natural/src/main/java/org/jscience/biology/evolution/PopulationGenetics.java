package org.jscience.biology.evolution;

/**
 * Population genetics models.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class PopulationGenetics {

    /**
     * Hardy-Weinberg equilibrium: Genotype frequency.
     * p + q = 1
     * p² + 2pq + q² = 1
     * 
     * @param p Frequency of dominant allele (A)
     * @return [p², 2pq, q²] representing AA, Aa, aa frequencies.
     */
    public static double[] hardyWeinbergFrequencies(double p) {
        if (p < 0 || p > 1)
            throw new IllegalArgumentException("Allele frequency p must be between 0 and 1");
        double q = 1.0 - p;
        return new double[] { p * p, 2 * p * q, q * q };
    }

    /**
     * Estimates allele frequency p from genotype counts.
     * p = (2*AA + Aa) / (2*N)
     * 
     * @param AA Count of homozygous dominant
     * @param Aa Count of heterozygous
     * @param aa Count of homozygous recessive
     */
    public static double estimateAlleleFrequency(int AA, int Aa, int aa) {
        int N = AA + Aa + aa;
        if (N == 0)
            return 0;
        return (2.0 * AA + Aa) / (2.0 * N);
    }

    /**
     * Fixation index (F_ST) between two subpopulations.
     * F_ST = (H_T - H_S) / H_T
     * where H_T is total heterozygosity and H_S is average subpopulation
     * heterozygosity.
     */
    public static double fixationIndex(double heterozygozityTotal, double heterozygozitySub) {
        if (heterozygozityTotal == 0)
            return 0;
        return (heterozygozityTotal - heterozygozitySub) / heterozygozityTotal;
    }

    /**
     * Genetic drift simulation (Wright-Fisher model).
     * Simulates the change in allele count over generation.
     * 
     * @param initialCountAllelA Number of 'A' alleles in population (max 2N)
     * @param populationSize     N (diploid individuals, so 2N gene copies)
     * @return New count of 'A' alleles in next generation
     */
    public static int wrightFisherStep(int initialCountAllelA, int populationSize) {
        int totalGenes = 2 * populationSize;
        double p = (double) initialCountAllelA / totalGenes;

        // Next generation: binomial sampling B(2N, p)
        int nextGenCount = 0;
        for (int i = 0; i < totalGenes; i++) {
            if (Math.random() < p) {
                nextGenCount++;
            }
        }
        return nextGenCount;
    }
}
