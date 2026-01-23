/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.media.backends;

import org.jscience.media.AudioEngine;
import ddf.minim.*;
import ddf.minim.analysis.FFT;

import org.jscience.media.AudioBackend;

/**
 * Minim Backend (Creative Coding).
 * Requires Minim library and Java wrapper logic.
 */
public class MinimEngine implements AudioEngine, AudioBackend {

    private Minim minim;
    private AudioPlayer player;
    private FFT fft;

    public MinimEngine() {
        minim = new Minim(new MinimHelper());
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
