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

package org.jscience.social.law;

import org.jscience.core.mathematics.numbers.real.Real;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides algorithms for allocating liability and damages in multi-party legal disputes.
 * Supports comparative negligence and joint and several liability models.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class LiabilityAllocator {

    private LiabilityAllocator() {
        // Utility class
    }

    /**
     * Represents a party involved in litigation with their assigned fault and damages.
     * 
     * @param name the name of the party
     * @param faultRatio the ratio of fault assigned to this party (0.0 to 1.0)
     * @param specificDamage any damages specifically attributed to this party
     */
    public record Party(String name, double faultRatio, double specificDamage) {}

    /**
     * Distributes total damages among parties based on their fault ratios (Comparative Negligence).
     * 
     * @param totalDamage the total amount of damages to be distributed
     * @param parties the list of parties involved
     * @return a map where the key is the party name and the value is the assigned liability as a Real number
     */
    public static Map<String, Real> distributeLiability(double totalDamage, List<Party> parties) {
        if (parties == null || parties.isEmpty()) {
            return Map.of();
        }
        
        Map<String, Real> distribution = new HashMap<>();
        double totalFault = parties.stream().mapToDouble(Party::faultRatio).sum();
        
        if (totalFault == 0) {
            return distribution;
        }

        for (Party p : parties) {
            double share = (p.faultRatio() / totalFault) * totalDamage;
            distribution.put(p.name(), Real.of(share));
        }
        return distribution;
    }

    /**
     * Calculates the outcome for a party under the Joint and Several Liability rule,
     * limited by their solvent capacity.
     * 
     * @param totalDamage the total damages awarded
     * @param maxSolventCapacity the maximum amount the party can pay
     * @return the resulting damage amount as a Real number
     */
    public static Real jointAndSeveralOutcome(double totalDamage, double maxSolventCapacity) {
        return Real.of(Math.min(totalDamage, maxSolventCapacity));
    }
}

