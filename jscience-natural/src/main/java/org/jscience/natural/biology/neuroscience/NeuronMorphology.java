/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.biology.neuroscience;

import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the morphology of a neuron (dendrites, axons, segments).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class NeuronMorphology {
    @Attribute
    private String id;

    @Attribute
    private final List<Segment> segments = new ArrayList<>();

    @Attribute
    private final Map<String, List<String>> segmentGroups = new HashMap<>();

    public NeuronMorphology(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void addSegment(String segmentId, String name, double[] proximal, double[] distal, String parentId) {
        segments.add(new Segment(segmentId, name, proximal, distal, parentId));
    }

    public void addSegmentGroup(String groupId, List<String> memberIds) {
        segmentGroups.put(groupId, new ArrayList<>(memberIds));
    }

    public List<Segment> getSegments() {
        return segments;
    }

    @Persistent
    public static class Segment {
        @Attribute
        private final String id;
        @Attribute
        private final String name;
        @Attribute
        private final double[] proximal;
        @Attribute
        private final double[] distal;
        @Attribute
        private final String parentId;

        public Segment(String id, String name, double[] proximal, double[] distal, String parentId) {
            this.id = id;
            this.name = name;
            this.proximal = proximal;
            this.distal = distal;
            this.parentId = parentId;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public double[] getProximal() { return proximal; }
        public double[] getDistal() { return distal; }
        public String getParentId() { return parentId; }
    }
}

