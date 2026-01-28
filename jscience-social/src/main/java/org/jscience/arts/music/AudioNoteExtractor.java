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

package org.jscience.arts.music;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.analysis.transform.SignalFFT;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic transcriber for extracting musical notes from raw audio signals.
 */
public final class AudioNoteExtractor {

    private final int sampleRate;

    public AudioNoteExtractor(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    /**
     * Extracts a sequence of notes from a raw audio signal.
     * Uses a windowed FFT to find dominant frequencies.
     * 
     * @param signal The audio samples.
     * @param windowSize Size of the FFT window (must be power of 2).
     * @return List of detected notes.
     */
    public List<Note> extractNotes(Real[] signal, int windowSize) {
        List<Note> detectedNotes = new ArrayList<>();
        
        for (int i = 0; i < signal.length - windowSize; i += windowSize) {
            Real[] chunk = new Real[windowSize];
            System.arraycopy(signal, i, chunk, 0, windowSize);
            
            Real freq = findDominantFrequency(chunk);
            if (freq.compareTo(Real.of(20.0)) > 0) { // Filter out subsonic/noise
                detectedNotes.add(frequencyToNote(freq));
            }
        }
        
        return detectedNotes;
    }

    private Real findDominantFrequency(Real[] chunk) {
        Real[][] spectrum = SignalFFT.fftReal(chunk);
        Real[] magnitude = SignalFFT.magnitude(spectrum[0], spectrum[1]);
        
        int maxIndex = -1;
        Real maxMag = Real.of(-1.0);
        
        // Only look at the first half (Nyquist)
        for (int i = 1; i < magnitude.length / 2; i++) {
            if (magnitude[i].compareTo(maxMag) > 0) {
                maxMag = magnitude[i];
                maxIndex = i;
            }
        }
        
        if (maxIndex == -1) return Real.ZERO;
        
        // f = index * sampleRate / N
        return Real.of(maxIndex).multiply(Real.of(sampleRate)).divide(Real.of(chunk.length));
    }

    private Note frequencyToNote(Real frequency) {
        // midi = 69 + 12 * log2(f / 440)
        double f = frequency.doubleValue();
        double midi = 69 + 12 * (Math.log(f / 440.0) / Math.log(2.0));
        int midiInt = (int) Math.round(midi);
        int cents = (int) Math.round((midi - midiInt) * 100);
        
        int octave = (midiInt / 12) - 1;
        int pitchIndex = midiInt % 12;
        if (pitchIndex < 0) pitchIndex += 12;
        
        Pitch pitch = Pitch.values()[pitchIndex];
        return new Note(pitch, octave, Duration.QUARTER, cents, 1.0);
    }
}
