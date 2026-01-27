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

package org.jscience.media.backends;

import org.jscience.media.AudioBackend;
import javax.sound.sampled.*;
import java.io.File;

/**
 * Standard JavaSound implementation.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class JavaSoundBackend implements AudioBackend {
    private Clip clip;
    
    @Override
    public void load(String path) throws Exception {
        AudioInputStream ais = AudioSystem.getAudioInputStream(new File(path));
        clip = AudioSystem.getClip();
        clip.open(ais);
    }

    @Override public void play() { if(clip!=null) clip.start(); }
    @Override public void pause() { if(clip!=null) clip.stop(); }
    @Override public void stop() { if(clip!=null) { clip.stop(); clip.setFramePosition(0); } }
    @Override public double getTime() { return (clip!=null) ? clip.getMicrosecondPosition() / 1_000_000.0 : 0; }
    @Override public double getDuration() { return (clip!=null) ? clip.getMicrosecondLength() / 1_000_000.0 : 0; }
    
    @Override
    public float[] getSpectrum() {
        // Real FFT implementation requires buffering the audio stream.
        // Returning empty spectrum for now to avoid fake data.
        return new float[128];
    }

    @Override public String getBackendName() { return "Standard JavaSound"; }
}
