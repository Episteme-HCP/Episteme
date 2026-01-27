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

package org.jscience.psychology;

import java.io.Serializable;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;

/**
 * Models cognitive load based on Sweller's Cognitive Load Theory (CLT).
 * Provides methods to estimate intrinsic, extraneous, and germane load, and evaluate learning efficiency.
 * Modernized to use Real for high-precision cognitive modeling.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 * @since 1.0
 */
public final class CognitiveLoadModel {

    private CognitiveLoadModel() {}

    /**
     * Represents the components of cognitive load involved in a task.
     */
    @Persistent
    public static class LoadFactor implements Serializable {
        private static final long serialVersionUID = 1L;

        @Attribute
        private String name;
        @Attribute
        private Real intrinsic;
        @Attribute
        private Real extraneous;
        @Attribute
        private Real germane;

        public LoadFactor() {}

        public LoadFactor(String name, Real intrinsic, Real extraneous, Real germane) {
            this.name = name;
            this.intrinsic = intrinsic;
            this.extraneous = extraneous;
            this.germane = germane;
        }

        public String getName() { return name; }
        public Real getIntrinsic() { return intrinsic; }
        public Real getExtraneous() { return extraneous; }
        public Real getGermane() { return germane; }
    }

    /**
     * Represents the resulting cognitive state of an individual performing a task.
     */
    @Persistent
    public static class CognitiveState implements Serializable {
        private static final long serialVersionUID = 1L;

        @Attribute
        private Real totalLoad;
        @Attribute
        private Real availableResource;
        @Attribute
        private boolean overloaded;
        @Attribute
        private String recommendation;

        public CognitiveState() {}

        public CognitiveState(Real totalLoad, Real availableResource, boolean overloaded, String recommendation) {
            this.totalLoad = totalLoad;
            this.availableResource = availableResource;
            this.overloaded = overloaded;
            this.recommendation = recommendation;
        }

        public Real getTotalLoad() { return totalLoad; }
        public Real getAvailableResource() { return availableResource; }
        public boolean isOverloaded() { return overloaded; }
        public String getRecommendation() { return recommendation; }
    }

    /**
     * Evaluates the total cognitive load against a working memory capacity.
     * 
     * @param factor                the load factors of the task
     * @param workingMemoryCapacity the capacity of the learner
     * @return the evaluated cognitive state
     */
    public static CognitiveState evaluateLoad(LoadFactor factor, Real workingMemoryCapacity) {
        Real total = factor.getIntrinsic().add(factor.getExtraneous()).add(factor.getGermane());
        Real available = workingMemoryCapacity.subtract(total);
        boolean overloaded = total.compareTo(workingMemoryCapacity) > 0;

        String rec = "Optimal load balance.";
        if (overloaded) {
            rec = "Reduce extraneous load by simplifying presentation or reduce intrinsic load by segmentation.";
        } else if (factor.getGermane().compareTo(Real.of(0.2)) < 0 && available.compareTo(Real.of(0.3)) > 0) {
            rec = "Potential to increase challenge to foster schema construction (increase germane load).";
        }

        return new CognitiveState(total, available, overloaded, rec);
    }

    /**
     * Estimates intrinsic load based on element interactivity.
     * Formula: I = log2(N_elements * N_interactions)
     * 
     * @param elements     number of distinct information elements
     * @param interactions number of interacting relationships between elements
     * @return estimated intrinsic load score
     */
    public static Real estimateIntrinsicLoad(int elements, int interactions) {
        if (elements <= 0) return Real.ZERO;
        // log2(x) = log(x) / log(2)
        double val = Math.log(elements * (interactions + 1.0)) / Math.log(2.0);
        return Real.of(val);
    }

    /**
     * Calculates the instructional efficiency of the learning task.
     * Efficiency = Germane / (Intrinsic + Extraneous)
     * 
     * @param f the load factors
     * @return efficiency ratio
     */
    public static Real learningEfficiency(LoadFactor f) {
        Real denominator = f.getIntrinsic().add(f.getExtraneous());
        if (denominator.isZero()) return Real.ZERO;
        return f.getGermane().divide(denominator);
    }

    /**
     * Calculates a penalty for the Redundancy Effect.
     *
     * @param identicalFormatCount number of redundant streams
     * @return calculated extraneous load penalty
     */
    public static Real redundancyPenalty(int identicalFormatCount) {
        if (identicalFormatCount <= 1) return Real.ZERO;
        return Real.of(0.1).multiply(Real.of(identicalFormatCount - 1));
    }
}
