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

package org.jscience.core.media.backends;

import org.jscience.core.media.AudioEngine;
import java.io.File;
import javax.sound.sampled.*;

/**
 * Standard backend using JavaSound API.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class JavaSoundEngine implements AudioEngine {

    private Clip clip;


    @Override
    public void load(String path) throws Exception {
        File file = new File(path);
        if (clip != null && clip.isOpen()) clip.close();
        AudioInputStream ais = AudioSystem.getAudioInputStream(file);
        clip = AudioSystem.getClip();
        clip.open(ais);
    }

    @Override public void play() { if (clip != null) clip.start(); }
    @Override public void pause() { if (clip != null) clip.stop(); }
    @Override public void stop() { if (clip != null) { clip.stop(); clip.setFramePosition(0); } }
    @Override public double getTime() { return (clip == null) ? 0.0 : clip.getMicrosecondPosition() / 1_000_000.0; }
    @Override public double getDuration() { return (clip == null) ? 0.0 : clip.getMicrosecondLength() / 1_000_000.0; }
    
    @Override
    public float[] getSpectrum() {
        // Dummy spectrum for standard JavaSound
        float[] dummy = new float[128];
        for(int i=0; i<dummy.length; i++) dummy[i] = (float)Math.random(); 
        return dummy;
    }
    
    @Override public String getBackendName() { return "Standard JavaSound"; }
}

