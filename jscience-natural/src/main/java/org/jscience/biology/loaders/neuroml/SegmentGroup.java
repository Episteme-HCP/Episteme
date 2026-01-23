/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.neuroml;

import java.util.*;

/** Group of segments (e.g., soma, axon, dendrite). */
public class SegmentGroup {
    private String id;
    private final List<String> memberSegmentIds = new ArrayList<>();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public void addMemberSegmentId(String segId) { memberSegmentIds.add(segId); }
    public List<String> getMemberSegmentIds() { return Collections.unmodifiableList(memberSegmentIds); }
}
