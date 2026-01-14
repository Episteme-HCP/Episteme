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

package org.jscience.physics.nuclear;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.sets.Reals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Solves decay chain problems using Bateman equations.
 * For chain: NÃ¢â€šÂ Ã¢â€ â€™ NÃ¢â€šâ€š Ã¢â€ â€™ NÃ¢â€šÆ’ Ã¢â€ â€™ ... Ã¢â€ â€™ Stable
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DecayChain {

    /**
     * Represents a nuclide in a decay chain.
     */
    public static class Nuclide {
        public final String name;
        public final Real decayConstant; // ÃŽÂ» in sÃ¢ÂÂ»Ã‚Â¹ (0 for stable)

        public Nuclide(String name, Real halfLife) {
            this.name = name;
            // Stable if halfLife is null, zero, or infinite (treat as stable)
            if (halfLife == null || halfLife.isZero()) {
                this.decayConstant = Real.ZERO; // Stable
            } else {
                this.decayConstant = Real.LN2.divide(halfLife);
            }
        }

        public boolean isStable() {
            return decayConstant.isZero();
        }
    }

    /** Threshold for degenerate case detection */
    private static final Real DEGENERACY_THRESHOLD = Real.of(1e-30);

    private final List<Nuclide> chain;
    private final Vector<Real> initialAmounts;

    /**
     * Creates a decay chain.
     * 
     * @param nuclides       Ordered list of nuclides in the chain
     * @param initialAmounts Initial number of atoms for each nuclide
     */
    public DecayChain(List<Nuclide> nuclides, Vector<Real> initialAmounts) {
        this.chain = new ArrayList<>(nuclides);
        this.initialAmounts = initialAmounts;
    }

    /**
     * Bateman equation solution for the amount of nuclide i at time t.
     * N_i(t) = ÃŽÂ£_{j=1}^{i} c_{ij} * exp(-ÃŽÂ»_j * t)
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
            return initialAmounts.get(0).multiply(lambda.negate().multiply(time).exp());
        }

        // General Bateman solution for i-th nuclide (0-indexed)
        Real sum = Real.ZERO;
        Real N0 = initialAmounts.get(0);

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

            // Denominator: product of (ÃŽÂ»_k - ÃŽÂ»_j) for k Ã¢â€°Â  j
            Real denom = Real.ONE;
            for (int k = 0; k <= nuclideIndex; k++) {
                if (k != j) {
                    Real lambdaK = chain.get(k).decayConstant;
                    Real diff = lambdaK.subtract(lambdaJ);
                    if (diff.abs().compareTo(DEGENERACY_THRESHOLD) < 0) {
                        // Handle degenerate case (equal decay constants)
                        diff = DEGENERACY_THRESHOLD;
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
     * A = ÃŽÂ» * N
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
        return new DecayChain(nuclides, DenseVector.of(Arrays.asList(initialParent, Real.ZERO), Reals.getInstance()));
    }

    /**
     * Creates the Uranium-238 decay chain (simplified).
     * U-238 Ã¢â€ â€™ Th-234 Ã¢â€ â€™ Pa-234 Ã¢â€ â€™ U-234 Ã¢â€ â€™ ... Ã¢â€ â€™ Pb-206 (stable)
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

        List<Real> initial = new ArrayList<>();
        initial.add(initialU238);
        for (int i = 1; i < nuclides.size(); i++) {
            initial.add(Real.ZERO);
        }

        return new DecayChain(nuclides, DenseVector.of(initial, Reals.getInstance()));
    }

    public List<Nuclide> getChain() {
        return chain;
    }
}


