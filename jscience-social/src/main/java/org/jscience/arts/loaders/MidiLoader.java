package org.jscience.arts.loaders;

import org.jscience.arts.music.Composition;
import org.jscience.arts.music.Note;
import org.jscience.arts.music.Track;

import javax.sound.midi.*;
import java.io.File;

/**
 * Loader for musical compositions from binary MIDI files (.mid).
 */
public class MidiLoader extends CompositionLoader {

    private final String baseUrl;

    public MidiLoader(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public Composition load(String resourceId) throws Exception {
        Sequence sequence = MidiSystem.getSequence(new File(baseUrl, resourceId));
        Composition composition = new Composition(resourceId, "Imported MIDI", null, null);
        
        for (javax.sound.midi.Track midiTrack : sequence.getTracks()) {
            Track track = new Track("Track " + midiTrack.size(), "General MIDI");
            
            for (int i = 0; i < midiTrack.size(); i++) {
                MidiEvent event = midiTrack.get(i);
                MidiMessage message = event.getMessage();
                
                if (message instanceof ShortMessage sm) {
                    if (sm.getCommand() == ShortMessage.NOTE_ON && sm.getData2() > 0) {
                        int key = sm.getData1();
                        int velocity = sm.getData2();
                        
                        // Simplified: assuming quarter notes for now, or calculating from delta ticks
                        Note.Pitch pitch = Note.Pitch.values()[key % 12];
                        int octave = (key / 12) - 1;
                        
                        track.addNote(new Note(pitch, octave, Note.Duration.QUARTER, 0, velocity / 127.0));
                    }
                }
            }
            if (!track.getNotes().isEmpty()) {
                composition.addTrack(track);
            }
        }
        
        return composition;
    }

    @Override
    public String getResourcePath() {
        return baseUrl;
    }

    @Override
    public String getName() {
        return "MIDI File Loader";
    }

    @Override
    public String getDescription() {
        return "Extracts musical note sequences from binary MIDI (.mid) files.";
    }
}
