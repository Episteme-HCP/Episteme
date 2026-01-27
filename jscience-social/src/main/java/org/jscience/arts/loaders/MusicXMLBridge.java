/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.arts.loaders;

import org.jscience.arts.music.Score;
import org.jscience.arts.music.Part;
import org.jscience.arts.music.Measure;
import org.jscience.arts.music.Note;
import org.jscience.arts.music.Pitch;
import org.jscience.arts.music.Duration;
import org.jscience.arts.music.TimeSignature;
import org.jscience.arts.music.KeySignature;
import org.jscience.arts.music.Dynamics;

import java.util.ArrayList;
import java.util.List;

/**
 * Bridge for converting MusicXML DTOs to core JScience music theory objects.
 * <p>
 * MusicXML is the standard interchange format for music notation.
 * This bridge converts parsed MusicXML to JScience music domain objects.
 * </p>
 *
 * <h2>Architecture</h2>
 * <pre>
 * MusicXML → MusicXMLReader → MusicXML DTOs → MusicXMLBridge → Core Objects
 *                                                              ├── Score
 *                                                              ├── Part
 *                                                              ├── Measure
 *                                                              ├── Note
 *                                                              └── Pitch
 * </pre>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MusicXMLBridge {

    /**
     * Converts MusicXML score-partwise to JScience Score.
     *
     * @param mxmlScore the parsed MusicXML score
     * @return a Score object with all parts and measures
     */
    public Score toScore(MusicXMLScore mxmlScore) {
        if (mxmlScore == null) {
            return null;
        }
        
        Score score = new Score(mxmlScore.getWorkTitle());
        
        // Set metadata
        score.setTrait("musicxml.version", mxmlScore.getVersion());
        score.setComposer(mxmlScore.getComposer());
        score.setTrait("copyright", mxmlScore.getCopyright());
        score.setTrait("encoding.software", mxmlScore.getEncodingSoftware());
        
        // Convert each part
        if (mxmlScore.getParts() != null) {
            for (MusicXMLPart mxmlPart : mxmlScore.getParts()) {
                Part part = convertPart(mxmlPart);
                if (part != null) {
                    score.addPart(part);
                }
            }
        }
        
        return score;
    }

    /**
     * Converts MusicXML part to JScience Part.
     */
    public Part convertPart(MusicXMLPart mxmlPart) {
        if (mxmlPart == null) {
            return null;
        }
        
        Part part = new Part(mxmlPart.getId());
        part.setName(mxmlPart.getPartName());
        part.setTrait("abbreviation", mxmlPart.getAbbreviation());
        part.setTrait("midi.instrument", mxmlPart.getMidiInstrument());
        part.setTrait("midi.channel", mxmlPart.getMidiChannel());
        
        // Convert measures
        if (mxmlPart.getMeasures() != null) {
            for (MusicXMLMeasure mxmlMeasure : mxmlPart.getMeasures()) {
                Measure measure = convertMeasure(mxmlMeasure);
                if (measure != null) {
                    part.addMeasure(measure);
                }
            }
        }
        
        return part;
    }

    /**
     * Converts MusicXML measure to JScience Measure.
     */
    public Measure convertMeasure(MusicXMLMeasure mxmlMeasure) {
        if (mxmlMeasure == null) {
            return null;
        }
        
        Measure measure = new Measure(mxmlMeasure.getNumber());
        
        // Set time signature if present
        if (mxmlMeasure.getTimeSignature() != null) {
            TimeSignature ts = new TimeSignature(
                mxmlMeasure.getTimeSignature().getBeats(),
                mxmlMeasure.getTimeSignature().getBeatType()
            );
            measure.setTimeSignature(ts);
        }
        
        // Set key signature if present
        if (mxmlMeasure.getKeySignature() != null) {
            KeySignature ks = new KeySignature(
                mxmlMeasure.getKeySignature().getFifths(),
                mxmlMeasure.getKeySignature().getMode()
            );
            measure.setKeySignature(ks);
        }
        
        // Convert notes
        if (mxmlMeasure.getNotes() != null) {
            for (MusicXMLNote mxmlNote : mxmlMeasure.getNotes()) {
                Note note = convertNote(mxmlNote);
                if (note != null) {
                    measure.addNote(note);
                }
            }
        }
        
        return measure;
    }

    /**
     * Converts MusicXML note to JScience Note.
     */
    public Note convertNote(MusicXMLNote mxmlNote) {
        if (mxmlNote == null) {
            return null;
        }
        
        // Handle rest
        if (mxmlNote.isRest()) {
            Note rest = Note.rest(convertDuration(mxmlNote.getDuration(), mxmlNote.getType()));
            return rest;
        }
        
        // Convert pitch
        Pitch pitch = null;
        if (mxmlNote.getPitch() != null) {
            pitch = new Pitch(
                mxmlNote.getPitch().getStep(),
                mxmlNote.getPitch().getOctave(),
                mxmlNote.getPitch().getAlter()
            );
        }
        
        // Convert duration
        Duration duration = convertDuration(mxmlNote.getDuration(), mxmlNote.getType());
        
        Note note = new Note(pitch, duration);
        
        // Set additional properties
        note.setTrait("voice", mxmlNote.getVoice());
        note.setTrait("staff", mxmlNote.getStaff());
        note.setTrait("chord", mxmlNote.isChord());
        note.setTrait("tie.start", mxmlNote.hasTieStart());
        note.setTrait("tie.stop", mxmlNote.hasTieStop());
        
        // Dynamics
        if (mxmlNote.getDynamics() != null) {
            note.setDynamics(Dynamics.valueOf(mxmlNote.getDynamics().toUpperCase()));
        }
        
        // Articulations
        if (mxmlNote.getArticulations() != null) {
            note.setTrait("articulations", mxmlNote.getArticulations());
        }
        
        return note;
    }

    /**
     * Converts MusicXML duration to JScience Duration.
     */
    private Duration convertDuration(int divisions, String type) {
        if (type == null) {
            return Duration.QUARTER;
        }
        
        return switch (type.toLowerCase()) {
            case "whole" -> Duration.WHOLE;
            case "half" -> Duration.HALF;
            case "quarter" -> Duration.QUARTER;
            case "eighth" -> Duration.EIGHTH;
            case "16th" -> Duration.SIXTEENTH;
            case "32nd" -> Duration.THIRTY_SECOND;
            case "64th" -> Duration.SIXTY_FOURTH;
            default -> Duration.QUARTER;
        };
    }
}
