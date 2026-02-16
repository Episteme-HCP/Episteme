/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.core.media.backends;

import com.google.auto.service.AutoService;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;
import org.jscience.core.media.AudioBackend;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.core.technical.backend.Backend;

/**
 * Minim Backend (Creative Coding).
 * Requires Minim library and Java wrapper logic.
 */
@AutoService({Backend.class, AudioBackend.class, AlgorithmProvider.class})
public class MinimBackend implements AudioBackend, AlgorithmProvider {

    // ...

    @Override
    public String getAlgorithmType() {
        return "Audio/Video Engine";
    }

    private Minim minim;
    private AudioPlayer player;
    private FFT fft;

    public MinimBackend() {
        if (isAvailable()) {
            minim = new Minim(new MinimHelper());
        } else {
            System.err.println("[Minim] Library not available.");
        }
    }

    // ---- Backend Implementation ----

    @Override public String getType() { return "audio"; }
    @Override public String getId() { return "minim"; }
    @Override public String getName() { return "Minim (Creative)"; }
    @Override public String getDescription() { return "High-performance creative audio engine."; }
    
    @Override 
    public boolean isAvailable() { 
        try {
            Class.forName("ddf.minim.Minim");
            return true;
        } catch (Throwable t) {
            return false;
        }
    }
    
    @Override public int getPriority() { return 20; }
    
    @Override 
    public Object createBackend() { 
        return new MinimBackend(); 
    }

    // ---- AudioBackend Implementation ----

    @Override
    public void load(String path) throws Exception {
        System.out.println("[Minim] Loading " + path);
        if (player != null) {
            player.close();
        }
        player = minim.loadFile(path, 1024);
        if (player == null) {
            throw new Exception("Minim failed to load audio file: " + path);
        }
        fft = new FFT(player.bufferSize(), player.sampleRate());
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
