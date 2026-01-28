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
import org.jscience.util.UniversalDataModel;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * Models cognitive load based on Sweller's Cognitive Load Theory (CLT).
 */
public final class CognitiveLoadModel implements UniversalDataModel {

    private final String learnerName;
    private final List<LoadFactor> tasks = new ArrayList<>();
    private Real workingMemoryCapacity = Real.of(7.0); // Miller's Magic Number

    public CognitiveLoadModel(String learnerName) {
        this.learnerName = learnerName;
    }

    public void setWorkingMemoryCapacity(Real capacity) { this.workingMemoryCapacity = capacity; }
    public void addTask(LoadFactor task) { tasks.add(task); }

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

    public CognitiveState evaluateCurrentLoad() {
        if (tasks.isEmpty()) return null;
        LoadFactor current = tasks.get(tasks.size() - 1);
        Real total = current.getIntrinsic().add(current.getExtraneous()).add(current.getGermane());
        Real available = workingMemoryCapacity.subtract(total);
        boolean overloaded = total.compareTo(workingMemoryCapacity) > 0;

        String rec = "Optimal load balance.";
        if (overloaded) {
            rec = "Reduce extraneous load.";
        }
        return new CognitiveState(total, available, overloaded, rec);
    }

    @Override
    public String getModelType() {
        return "COGNITIVE_LOAD_SWELLER";
    }

    @Override
    public Map<String, Object> getMetadata() {
        Map<String, Object> meta = new HashMap<>();
        meta.put("learner_name", learnerName);
        meta.put("task_count", tasks.size());
        meta.put("wm_capacity", workingMemoryCapacity.doubleValue());
        return meta;
    }

    @Override
    public Map<String, Quantity<?>> getQuantities() {
        Map<String, Quantity<?>> q = new HashMap<>();
        CognitiveState state = evaluateCurrentLoad();
        if (state != null) {
            q.put("total_cognitive_load", Quantities.create(state.getTotalLoad().doubleValue(), Units.ONE));
            q.put("available_resource", Quantities.create(state.getAvailableResource().doubleValue(), Units.ONE));
        }
        return q;
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
}

