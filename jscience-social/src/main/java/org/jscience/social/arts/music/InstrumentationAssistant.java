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

import java.util.*;

/**
 * Suggests instrumentation based on range, timbre, and historical context.
 */
public final class InstrumentationAssistant {

    private InstrumentationAssistant() {}

    public record Instrument(String name, int minMidi, int maxMidi, String family, String era) {}

    private static final List<Instrument> ORCHESTRA = List.of(
        new Instrument("Flute", 60, 96, "Woodwind", "Baroque-Modern"),
        new Instrument("Oboe", 58, 91, "Woodwind", "Baroque-Modern"),
        new Instrument("Clarinet", 50, 94, "Woodwind", "Classical-Modern"),
        new Instrument("Bassoon", 34, 75, "Woodwind", "Baroque-Modern"),
        new Instrument("Trumpet", 52, 80, "Brass", "Baroque-Modern"),
        new Instrument("Violin", 55, 103, "Strings", "Baroque-Modern"),
        new Instrument("Cello", 36, 76, "Strings", "Baroque-Modern")
    );

    /**
     * Suggests instruments that can play a given note range.
     */
    public static List<Instrument> suggestInstruments(int minNote, int maxNote) {
        return ORCHESTRA.stream()
            .filter(i -> i.minMidi() <= minNote && i.maxMidi() >= maxNote)
            .toList();
    }
}

