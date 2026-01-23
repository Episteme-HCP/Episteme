/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.neuroml;

import java.util.*;

/** Cell morphology with segments. */
public class Morphology {
    private String id;
    private final List<Segment> segments = new ArrayList<>();
    private final List<SegmentGroup> segmentGroups = new ArrayList<>();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public void addSegment(Segment s) { if (s != null) segments.add(s); }
    public List<Segment> getSegments() { return Collections.unmodifiableList(segments); }
    
    public void addSegmentGroup(SegmentGroup g) { if (g != null) segmentGroups.add(g); }
    public List<SegmentGroup> getSegmentGroups() { return Collections.unmodifiableList(segmentGroups); }
    
    /** Get soma segment (usually segment 0). */
    public Segment getSoma() {
        return segments.isEmpty() ? null : segments.get(0);
    }
}
