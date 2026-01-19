package org.jscience.economics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Simulator for central bank monetary policy.
 */
public final class MonetaryPolicySimulator {

    private MonetaryPolicySimulator() {}

    /**
     * Taylor Rule for central bank interest rates.
     * r = p + 0.5y + 0.5(p - 2) + 2
     */
    public static Real taylorRuleRate(double inflation, double outputGap) {
        return Real.of(inflation + 0.5 * outputGap + 0.5 * (inflation - 2.0) + 2.0);
    }
}
