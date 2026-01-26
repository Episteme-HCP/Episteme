package org.jscience.arts.music;

import org.jscience.arts.Artwork;
import org.jscience.arts.ArtForm;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.jscience.history.time.TimeCoordinate;
import org.jscience.earth.Place;

/**
 * Represents a musical composition with multiple tracks and signatures.
 */
public class Composition extends Artwork {

    public record TimeSignature(int beatsPerMeasure, int beatValue) {
        @Override
        public String toString() { return beatsPerMeasure + "/" + beatValue; }
    }

    private final String keySignature; // e.g., "C Major"
    private final double tempoBpm;
    private final TimeSignature timeSignature;
    private final List<Track> tracks;

    public Composition(String name, String description, TimeCoordinate productionDate, 
                       Place productionPlace, String keySignature, double tempoBpm, 
                       TimeSignature timeSignature, List<Track> tracks) {
        super(name, description, productionDate, productionPlace, ArtForm.MUSIC);
        this.keySignature = keySignature;
        this.tempoBpm = tempoBpm;
        this.timeSignature = Objects.requireNonNull(timeSignature);
        this.tracks = new ArrayList<>(Objects.requireNonNull(tracks));
    }

    public Composition(String name, String description, TimeCoordinate productionDate, Place productionPlace) {
        this(name, description, productionDate, productionPlace, "C Major", 120.0, new TimeSignature(4, 4), new ArrayList<>());
    }

    public String getKeySignature() { return keySignature; }
    public double getTempoBpm() { return tempoBpm; }
    public TimeSignature getTimeSignature() { return timeSignature; }
    public List<Track> getTracks() { return Collections.unmodifiableList(tracks); }

    public void addTrack(Track track) {
        this.tracks.add(Objects.requireNonNull(track));
    }

    @Override
    public String toString() {
        return String.format("%s (Key: %s, Tempo: %.0f BPM, %s, %d tracks)", 
            getName(), keySignature, tempoBpm, timeSignature, tracks.size());
    }
}
