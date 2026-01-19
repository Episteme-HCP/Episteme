package org.jscience.arts.music;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A single track in a musical composition, typically for one instrument.
 */
public class Track {
    private final String name;
    private final String instrument;
    private final List<Note> notes;

    public Track(String name, String instrument) {
        this.name = name;
        this.instrument = instrument;
        this.notes = new ArrayList<>();
    }

    public void addNote(Note note) {
        notes.add(Objects.requireNonNull(note));
    }

    public String getName() { return name; }
    public String getInstrument() { return instrument; }
    public List<Note> getNotes() { return Collections.unmodifiableList(notes); }
}
