/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.social.arts.loaders;

import java.util.ArrayList;
import java.util.List;

public class MusicXMLPart {
    private String id;
    private String partName;
    private String abbreviation;
    private String midiInstrument;
    private String midiChannel;
    private List<MusicXMLMeasure> measures = new ArrayList<>();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPartName() { return partName; }
    public void setPartName(String partName) { this.partName = partName; }

    public String getAbbreviation() { return abbreviation; }
    public void setAbbreviation(String abbreviation) { this.abbreviation = abbreviation; }

    public String getMidiInstrument() { return midiInstrument; }
    public void setMidiInstrument(String midiInstrument) { this.midiInstrument = midiInstrument; }

    public String getMidiChannel() { return midiChannel; }
    public void setMidiChannel(String midiChannel) { this.midiChannel = midiChannel; }

    public List<MusicXMLMeasure> getMeasures() { return measures; }
    public void addMeasure(MusicXMLMeasure measure) { measures.add(measure); }
}

