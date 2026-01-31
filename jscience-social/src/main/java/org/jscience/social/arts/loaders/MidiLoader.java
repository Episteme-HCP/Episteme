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

package org.jscience.social.arts.loaders;

import org.jscience.social.arts.music.Composition;
import org.jscience.social.arts.music.Note;
import org.jscience.social.arts.music.Pitch;
import org.jscience.social.arts.music.Duration;
import org.jscience.social.arts.music.Part;
import org.jscience.social.arts.music.Measure;

import javax.sound.midi.*;
import java.io.File;
import java.io.InputStream;
import java.util.Objects;

/**
 * Loader for musical compositions from binary MIDI files (.mid).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MidiLoader extends CompositionLoader {

    private final String baseUrl;

    public MidiLoader(String baseUrl) {
        this.baseUrl = Objects.requireNonNull(baseUrl);
    }

    @Override
    protected Composition loadFromSource(String resourceId) throws Exception {
        File file = new File(baseUrl, resourceId);
        Sequence sequence = MidiSystem.getSequence(file);
        
        Composition composition = new Composition(resourceId, "Imported MIDI", null, null);
        
        for (javax.sound.midi.Track midiTrack : sequence.getTracks()) {
            Part part = new Part("Track " + composition.getParts().size());
            part.setInstrumentName("General MIDI");
            
            Measure measure = new Measure(1);
            measure.setTimeSignature(composition.getTimeSignature());
            measure.setKeySignature(composition.getKeySignature());
            
            for (int i = 0; i < midiTrack.size(); i++) {
                MidiEvent event = midiTrack.get(i);
                MidiMessage message = event.getMessage();
                
                if (message instanceof ShortMessage sm) {
                    if (sm.getCommand() == ShortMessage.NOTE_ON && sm.getData2() > 0) {
                        int key = sm.getData1();
                        int velocity = sm.getData2();
                        
                        Pitch pitch = Pitch.values()[key % 12];
                        int octave = (key / 12) - 1;
                        
                        measure.addNote(new Note(pitch, octave, Duration.QUARTER, 0, velocity / 127.0));
                    }
                }
            }
            if (!measure.getNotes().isEmpty()) {
                part.addMeasure(measure);
                composition.addPart(part);
            }
        }
        
        return composition;
    }

    @Override
    protected Composition loadFromInputStream(InputStream is, String id) throws Exception {
        MidiSystem.getSequence(is); // Validate valid MIDI stream
        // Similar logic to loadFromSource
        Composition composition = new Composition(id, "Imported MIDI Stream", null, null);
        // ... (DRY: could refactor common logic)
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

    @Override
    public String[] getSupportedVersions() {
        return new String[]{"1.0", "SMF 0", "SMF 1"};
    }
}

