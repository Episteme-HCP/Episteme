package org.jscience.mathematics.statistics;

import org.jscience.mathematics.numbers.real.Real;
import java.util.Map;
import java.util.HashMap;

/**
 * Information theory primitives.
 */
public final class InformationTheory {

    private InformationTheory() {}

    /**
     * Calculates the Shannon entropy of a discrete probability distribution.
     * H(X) = -sum(p(x) * log2(p(x)))
     * 
     * @param probabilities Map of outcomes to their probabilities.
     * @return Entropy in bits.
     */
    public static Real shannonEntropy(Map<?, Real> probabilities) {
        Real entropy = Real.of(0.0);
        Real log2 = Real.of(2.0).log();
        for (Real p : probabilities.values()) {
            if (p.compareTo(Real.of(0.0)) > 0) {
                entropy = entropy.subtract(p.multiply(p.log().divide(log2)));
            }
        }
        return entropy;
    }

    /**
     * Calculates the entropy of a string based on character frequency.
     * 
     * @param s The input string.
     * @return Entropy in bits per character.
     */
    public static Real stringEntropy(String s) {
        if (s == null || s.isEmpty()) return Real.ZERO;
        
        Map<Character, Integer> counts = new HashMap<>();
        for (char c : s.toCharArray()) {
            counts.put(c, counts.getOrDefault(c, 0) + 1);
        }
        
        Real len = Real.of(s.length());
        Map<Character, Real> probs = new HashMap<>();
        for (Map.Entry<Character, Integer> entry : counts.entrySet()) {
            probs.put(entry.getKey(), Real.of(entry.getValue()).divide(len));
        }
        
        return shannonEntropy(probs);
    }
}
