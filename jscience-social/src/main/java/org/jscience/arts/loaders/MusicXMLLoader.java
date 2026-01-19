package org.jscience.arts.loaders;

import org.jscience.arts.music.*;
import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

/**
 * Loader for MusicXML files.
 */
public final class MusicXMLLoader {

    private MusicXMLLoader() {}

    /**
     * Loads a score from a MusicXML file.
     */
    public static List<Note> load(File file) throws Exception {
        List<Note> notes = new ArrayList<>();
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        
        NodeList noteList = doc.getElementsByTagName("note");
        
        for (int i = 0; i < noteList.getLength(); i++) {
            Element noteElem = (Element) noteList.item(i);
            
            // Check if it's a rest
            boolean isRest = noteElem.getElementsByTagName("rest").getLength() > 0;
            
            // Duration
            NodeList durationList = noteElem.getElementsByTagName("duration");
            double duration = 1.0;
            if (durationList.getLength() > 0) {
                duration = Double.parseDouble(durationList.item(0).getTextContent()) / 4.0;
            }
            
            if (!isRest) {
                // Pitch
                Element pitchElem = (Element) noteElem.getElementsByTagName("pitch").item(0);
                String step = pitchElem.getElementsByTagName("step").item(0).getTextContent();
                int octave = Integer.parseInt(pitchElem.getElementsByTagName("octave").item(0).getTextContent());
                
                int alter = 0;
                NodeList alterList = pitchElem.getElementsByTagName("alter");
                if (alterList.getLength() > 0) {
                    alter = Integer.parseInt(alterList.item(0).getTextContent());
                }
                
                int midiNumber = pitchToMidi(step, octave, alter);
                notes.add(Note.fromMidi(midiNumber, duration));
            }
            
            // Handle chord (don't advance time)
            boolean isChord = noteElem.getElementsByTagName("chord").getLength() > 0;
            if (!isChord) {
                currentTime += duration;
            }
        }
        
        return notes;
    }

    private static int pitchToMidi(String step, int octave, int alter) {
        int base = switch (step.toUpperCase()) {
            case "C" -> 0;
            case "D" -> 2;
            case "E" -> 4;
            case "F" -> 5;
            case "G" -> 7;
            case "A" -> 9;
            case "B" -> 11;
            default -> 0;
        };
        return (octave + 1) * 12 + base + alter;
    }
}
