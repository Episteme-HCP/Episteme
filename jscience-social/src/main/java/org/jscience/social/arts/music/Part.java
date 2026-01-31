/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.social.arts.music;

import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a part for a single instrument in a musical score.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Part implements Serializable {
    private static final long serialVersionUID = 1L;

    @Attribute
    private String name;
    
    @Attribute
    private String instrumentName;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Measure> measures = new ArrayList<>();

    public Part() {
    }

    public Part(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    public void setInstrumentName(String instrumentName) {
        this.instrumentName = instrumentName;
    }

    public void addMeasure(Measure measure) {
        measures.add(measure);
    }

    public List<Measure> getMeasures() {
        return Collections.unmodifiableList(measures);
    }

    public void setTrait(String key, Object value) {
        traits.put(key, value);
    }

    public Object getTrait(String key) {
        return traits.get(key);
    }
}

