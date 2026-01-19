package org.jscience.psychology;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Models cognitive load based on Sweller's Cognitive Load Theory.
 */
public final class CognitiveLoadModel {

    private CognitiveLoadModel() {}

    public record LoadFactor(
        String name,
        double intrinsic, // Complexity of the material (0-1)
        double extraneous, // Quality of instruction (0-1, lower is better)
        double germane      // Effort dedicated to schema construction (0-1)
    ) {}

    public record CognitiveState(
        double totalLoad,
        double availableResource,
        boolean isOverloaded,
        String recommendation
    ) {}

    /**
     * Calculates total cognitive load.
     * Total = Intrinsic + Extraneous
     */
    public static CognitiveState evaluateLoad(LoadFactor factor, double workingMemoryCapacity) {
        double total = factor.intrinsic() + factor.extraneous();
        double available = workingMemoryCapacity - total;
        boolean overloaded = total > workingMemoryCapacity;

        String rec = "Optimal";
        if (overloaded) {
            rec = "Reduce extraneous load by simplifying presentation";
        } else if (factor.germane() < 0.2) {
            rec = "Increase intrinsic challenge or improve engagement";
        }

        return new CognitiveState(total, available, overloaded, rec);
    }

    /**
     * Estimates intrinsic load based on element interactivity.
     * I = log2(N_elements * N_interactions)
     */
    public static Real estimateIntrinsicLoad(int elements, int interactions) {
        if (elements <= 0) return Real.ZERO;
        return Real.of(Math.log(elements * (interactions + 1)) / Math.log(2));
    }

    /**
     * Models learning efficiency.
     * E = Germane / (Intrinsic + Extraneous)
     */
    public static Real learningEfficiency(LoadFactor f) {
        double denominator = f.intrinsic() + f.extraneous();
        if (denominator == 0) return Real.ZERO;
        return Real.of(f.germane() / denominator);
    }

    /**
     * Redundancy Effect penalty.
     * Increases extraneous load when information is presented in multiple identical formats.
     */
    public static double redundancyPenalty(int identicalFormatCount) {
        return 0.1 * (identicalFormatCount - 1);
    }
}
