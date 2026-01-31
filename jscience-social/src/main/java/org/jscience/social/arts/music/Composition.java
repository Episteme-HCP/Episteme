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

package org.jscience.social.arts.music;

import org.jscience.social.arts.Artwork;
import org.jscience.social.arts.ArtForm;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.jscience.social.history.time.TimeCoordinate;
import org.jscience.natural.earth.Place;

/**
 * Represents a musical composition with multiple tracks and signatures.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Composition extends Artwork {


    private final KeySignature keySignature;
    private final double tempoBpm;
    private final TimeSignature timeSignature;
    private final List<Part> parts;

    public Composition(String name, String description, TimeCoordinate productionDate, 
                       Place productionPlace, KeySignature keySignature, double tempoBpm, 
                       TimeSignature timeSignature, List<Part> parts) {
        super(name, description, productionDate, productionPlace, ArtForm.MUSIC);
        this.keySignature = keySignature;
        this.tempoBpm = tempoBpm;
        this.timeSignature = Objects.requireNonNull(timeSignature);
        this.parts = new ArrayList<>(Objects.requireNonNull(parts));
    }

    public Composition(String name, String description, TimeCoordinate productionDate, Place productionPlace) {
        this(name, description, productionDate, productionPlace, KeySignature.C_MAJOR, 120.0, new TimeSignature(4, 4), new ArrayList<>());
    }

    public KeySignature getKeySignature() { return keySignature; }
    public double getTempoBpm() { return tempoBpm; }
    public TimeSignature getTimeSignature() { return timeSignature; }
    public List<Part> getParts() { return Collections.unmodifiableList(parts); }

    public void addPart(Part part) {
        this.parts.add(Objects.requireNonNull(part));
    }

    @Override
    public String toString() {
        return String.format("%s (Key: %s, Tempo: %.0f BPM, %s, %d parts)", 
            getName(), keySignature, tempoBpm, timeSignature, parts.size());
    }
}

