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
import ddf.minim.*;
import ddf.minim.analysis.FFT;

import org.jscience.core.media.AudioBackend;
import com.google.auto.service.AutoService;
import org.jscience.core.technical.algorithm.AlgorithmProvider;

/**
 * Minim Backend (Creative Coding).
 * Requires Minim library and Java wrapper logic.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService(AlgorithmProvider.class)
public class MinimEngine implements AudioEngine, AudioBackend {

    private Minim minim;
    private AudioPlayer player;
    private FFT fft;

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("ddf.minim.Minim");
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    public MinimEngine() {
        if (isAvailable()) {
            minim = new Minim(new MinimHelper());
        } else {
            System.err.println("[Minim] Library not available.");
        }
    }

    @Override
    public void load(String path) throws Exception {
        System.out.println("[Minim] Loading " + path);
        if (player != null) {
            player.close();
        }
        player = minim.loadFile(path, 1024);
        if (player != null) {
            fft = new FFT(player.bufferSize(), player.sampleRate());
        }
    }

    @Override public void play() { 
        if (player != null) player.play(); 
    }
    
    @Override public void pause() { 
        if (player != null) player.pause(); 
    }
    
    @Override public void stop() { 
        if (player != null) {
            player.pause();
            player.rewind();
        }
    }
    
    @Override public double getTime() { 
        return (player != null) ? player.position() / 1000.0 : 0.0; 
    }

    @Override public double getDuration() { 
        return (player != null) ? player.length() / 1000.0 : 0.0; 
    }

    @Override
    public float[] getSpectrum() {
        if (player != null && fft != null) {
            fft.forward(player.mix);
            float[] spectrum = new float[fft.specSize()];
            for (int i = 0; i < fft.specSize(); i++) {
                spectrum[i] = fft.getBand(i);
            }
            return spectrum;
        }
        return new float[128];
    }

    @Override public String getBackendName() { return "Minim (Creative)"; }

    // Helper to allow Minim to run outside PApplet
    private class MinimHelper {
        @SuppressWarnings("unused")
        public String sketchPath(String fileName) {
            return new java.io.File(fileName).getAbsolutePath(); 
        }
        @SuppressWarnings("unused")
        public java.io.InputStream createInput(String fileName) {
            try { return new java.io.FileInputStream(fileName); } catch(Exception e) { return null; }
        }
    }
}

