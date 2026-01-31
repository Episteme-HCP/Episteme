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
 * Advanced engine for counterpoint analysis and generation.
 */
public final class CounterpointEngine {

    private CounterpointEngine() {}

    /**
     * Checks a sequence of notes against basic Fux counterpoint rules.
     */
    public static List<CounterpointChecker.Violation> analyze(List<Note> cantusFirmus, List<Note> counterpoint) {
        return CounterpointChecker.checkParallelism(cantusFirmus, counterpoint);
    }

    /**
     * Validates melodic motion (conjunct vs disjunct).
     */
    public static boolean isMelodicallyNatural(List<Note> melody) {
        if (melody.size() < 2) return true;
        int largeLeaps = 0;
        for (int i = 1; i < melody.size(); i++) {
            int interval = Math.abs(melody.get(i).getMidiNote() - melody.get(i-1).getMidiNote());
            if (interval > 12) return false; // More than an octave is forbidden
            if (interval > 7) largeLeaps++;
        }
        return largeLeaps <= melody.size() / 4;
    }
}

