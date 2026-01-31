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

package org.jscience.core.media;

/**
 * Interface definition for a generic Audio Engine.
 * Allows switching between JavaSound, TarsosDSP, or other backends.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface AudioEngine {
    
    /**
     * Loads an audio file from a path.
     * @param path Absolute path or URI to the audio file.
     * @throws Exception if loading fails.
     */
    void load(String path) throws Exception;

    /**
     * Starts or resumes playback.
     */
    void play();

    /**
     * Pauses playback.
     */
    void pause();

    /**
     * Stops playback and rewinds.
     */
    void stop();

    /**
     * Returns the current playback position in seconds.
     */
    double getTime();

    /**
     * Returns the total duration in seconds.
     */
    double getDuration();

    /**
     * Gets the Fast Fourier Transform (FFT) data for visualization.
     * @return array of magnitudes.
     */
    float[] getSpectrum();

    /**
     * Returns the name of the backend engine (e.g., "JavaSound", "TarsosDSP").
     */
    String getBackendName();
}

