package org.jscience.physics.nuclear;

import org.jscience.mathematics.numbers.real.Real;
import java.util.ArrayList;
import java.util.List;

/**
 * Solves decay chain problems using Bateman equations.
 * For chain: N₁ → N₂ → N₃ → ... → Stable
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class DecayChain {

    /**
     * Represents a nuclide in a decay chain.
     */
    public static class Nuclide {
        public final String name;
        public final Real decayConstant; // λ in s⁻¹ (0 for stable)

        public Nuclide(String name, Real halfLife) {
            this.name = name;
            if (halfLife == null || halfLife.isZero() || halfLife.doubleValue() == Double.POSITIVE_INFINITY) {
                this.decayConstant = Real.ZERO; // Stable
            } else {
                this.decayConstant = Real.of(Math.log(2)).divide(halfLife);
            }
        }

        public boolean isStable() {
            return decayConstant.isZero();
        }
    }

    private final List<Nuclide> chain;
    private final Real[] initialAmounts;

    /**
     * Creates a decay chain.
     * 
     * @param nuclides       Ordered list of nuclides in the chain
     * @param initialAmounts Initial number of atoms for each nuclide
     */
    public DecayChain(List<Nuclide> nuclides, Real[] initialAmounts) {
        this.chain = new ArrayList<>(nuclides);
        this.initialAmounts = initialAmounts.clone();
    }

    /**
     * Bateman equation solution for the amount of nuclide i at time t.
     * N_i(t) = Σ_{j=1}^{i} c_{ij} * exp(-λ_j * t)
     * 
     * For simplicity, this implementation handles the common case where
     * only the parent nuclide is initially present.
     * 
     * @param nuclideIndex Index of nuclide in chain (0 = parent)
     * @param time         Time elapsed (seconds)
     * @return Number of atoms of the specified nuclide
     */
    public Real getAmount(int nuclideIndex, Real time) {
        if (nuclideIndex == 0) {
            // Simple exponential decay for parent
            Real lambda = chain.get(0).decayConstant;
            return initialAmounts[0].multiply(lambda.negate().multiply(time).exp());
        }

        // General Bateman solution for i-th nuclide (0-indexed)
        Real sum = Real.ZERO;
        Real N0 = initialAmounts[0];

        // Product of decay constants for nuclides 1 to i
        Real lambdaProduct = Real.ONE;
        for (int j = 0; j < nuclideIndex; j++) {
            lambdaProduct = lambdaProduct.multiply(chain.get(j).decayConstant);
        }

        // Sum over j = 0 to i
        for (int j = 0; j <= nuclideIndex; j++) {
            Real lambdaJ = chain.get(j).decayConstant;
            if (lambdaJ.isZero())
                continue; // Stable nuclide

            // Denominator: product of (λ_k - λ_j) for k ≠ j
            Real denom = Real.ONE;
            for (int k = 0; k <= nuclideIndex; k++) {
                if (k != j) {
                    Real lambdaK = chain.get(k).decayConstant;
                    Real diff = lambdaK.subtract(lambdaJ);
                    if (diff.abs().doubleValue() < 1e-30) {
                        // Handle degenerate case (equal decay constants)
                        diff = Real.of(1e-30);
                    }
                    denom = denom.multiply(diff);
                }
            }

            Real expTerm = lambdaJ.negate().multiply(time).exp();
            sum = sum.add(expTerm.divide(denom));
        }

        return N0.multiply(lambdaProduct).multiply(sum);
    }

    /**
     * Get activity of a specific nuclide at time t.
     * A = λ * N
     */
    public Real getActivity(int nuclideIndex, Real time) {
        Real lambda = chain.get(nuclideIndex).decayConstant;
        return lambda.multiply(getAmount(nuclideIndex, time));
    }

    /**
     * Creates a simple two-nuclide decay chain.
     */
    public static DecayChain twoNuclide(String parent, Real parentHalfLife,
            String daughter, Real daughterHalfLife,
            Real initialParent) {
        List<Nuclide> nuclides = new ArrayList<>();
        nuclides.add(new Nuclide(parent, parentHalfLife));
        nuclides.add(new Nuclide(daughter, daughterHalfLife));
        return new DecayChain(nuclides, new Real[] { initialParent, Real.ZERO });
    }

    /**
     * Creates the Uranium-238 decay chain (simplified).
     * U-238 → Th-234 → Pa-234 → U-234 → ... → Pb-206 (stable)
     */
    public static DecayChain uranium238Chain(Real initialU238) {
        List<Nuclide> nuclides = new ArrayList<>();

        // Half-lives in seconds
        nuclides.add(new Nuclide("U-238", Real.of(4.468e9 * 365.25 * 24 * 3600)));
        nuclides.add(new Nuclide("Th-234", Real.of(24.1 * 24 * 3600))); // 24.1 days
        nuclides.add(new Nuclide("Pa-234", Real.of(1.17 * 60))); // 1.17 min
        nuclides.add(new Nuclide("U-234", Real.of(2.455e5 * 365.25 * 24 * 3600)));
        nuclides.add(new Nuclide("Th-230", Real.of(7.54e4 * 365.25 * 24 * 3600)));
        nuclides.add(new Nuclide("Ra-226", Real.of(1600 * 365.25 * 24 * 3600)));
        nuclides.add(new Nuclide("Rn-222", Real.of(3.8235 * 24 * 3600))); // 3.8 days
        nuclides.add(new Nuclide("Pb-206", null)); // Stable

        Real[] initial = new Real[nuclides.size()];
        initial[0] = initialU238;
        for (int i = 1; i < initial.length; i++) {
            initial[i] = Real.ZERO;
        }

        return new DecayChain(nuclides, initial);
    }

    public List<Nuclide> getChain() {
        return chain;
    }
}
