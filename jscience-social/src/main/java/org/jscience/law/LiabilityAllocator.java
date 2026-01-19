package org.jscience.law;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Allocates liability in multi-party litigation.
 */
public final class LiabilityAllocator {

    private LiabilityAllocator() {}

    public record Party(String name, double faultRatio, double specificDamage) {}

    /**
     * Distributes total damages based on fault ratios (Comparative Negligence).
     */
    public static Map<String, Real> distributeLiability(double totalDamage, List<Party> parties) {
        Map<String, Real> distribution = new HashMap<>();
        double totalFault = parties.stream().mapToDouble(Party::faultRatio).sum();
        
        for (Party p : parties) {
            double share = (p.faultRatio() / totalFault) * totalDamage;
            distribution.put(p.name(), Real.of(share));
        }
        return distribution;
    }

    /**
     * Identifies Joint and Several Liability outcomes.
     */
    public static Real jointAndSeveralOutcome(double totalDamage, double maxSolventCapacity) {
        return Real.of(Math.min(totalDamage, maxSolventCapacity));
    }
}
