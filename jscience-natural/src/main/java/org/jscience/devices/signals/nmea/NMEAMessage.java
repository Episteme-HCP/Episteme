/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.devices.signals.nmea;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents a parsed NMEA 0183 message (sentence).
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class NMEAMessage {
    
    private final String raw;
    private final String talkerId;
    private final String sentenceId;
    private final List<String> fields;
    
    public NMEAMessage(String raw) {
        this.raw = raw;
        if (!raw.startsWith("$")) {
             throw new IllegalArgumentException("Invalid NMEA sentence start: " + raw);
        }
        
        // Remove checksum if present
        String clean = raw.substring(1);
        int starIndex = clean.indexOf('*');
        if (starIndex > 0) {
            clean = clean.substring(0, starIndex);
        }
        
        String[] parts = clean.split(",");
        String address = parts[0];
        
        if (address.length() >= 5) {
            this.talkerId = address.substring(0, 2);
            this.sentenceId = address.substring(2);
        } else {
            this.talkerId = "Unknown";
            this.sentenceId = address;
        }
        
        if (parts.length > 1) {
            this.fields = Collections.unmodifiableList(Arrays.asList(Arrays.copyOfRange(parts, 1, parts.length)));
        } else {
            this.fields = Collections.emptyList();
        }
    }

    public String getRaw() {
        return raw;
    }

    public String getTalkerId() {
        return talkerId;
    }

    public String getSentenceId() {
        return sentenceId;
    }

    public List<String> getFields() {
        return fields;
    }
    
    public String getField(int index) {
        if (index >= 0 && index < fields.size()) {
            return fields.get(index);
        }
        return "";
    }
    
    @Override
    public String toString() {
        return raw;
    }
    
    /**
     * Validates checksum of a raw sentence.
     * @param sentence NMEA sentence (starting with $, ending with *CC)
     * @return true if valid
     */
    public static boolean validateChecksum(String sentence) {
        if (!sentence.startsWith("$")) return false;
        int starIndex = sentence.indexOf('*');
        if (starIndex == -1 || starIndex + 3 > sentence.length()) return false;
        
        String content = sentence.substring(1, starIndex);
        String checksumStr = sentence.substring(starIndex + 1, starIndex + 3);
        
        int calculated = 0;
        for (char c : content.toCharArray()) {
            calculated ^= c;
        }
        
        try {
            int provided = Integer.parseInt(checksumStr, 16);
            return calculated == provided;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}