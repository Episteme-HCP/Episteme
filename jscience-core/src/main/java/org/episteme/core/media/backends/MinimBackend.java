/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.episteme.core.media.backends;

import com.google.auto.service.AutoService;
import java.io.File;
import org.episteme.core.media.AudioBackend;
import org.episteme.core.technical.algorithm.AlgorithmProvider;
import org.episteme.core.technical.backend.Backend;

/**
 * Minim Backend (Creative Coding).
 * Requires Minim library and Java wrapper logic.
 */
@AutoService({Backend.class, AudioBackend.class, AlgorithmProvider.class})
public class MinimBackend implements AudioBackend, AlgorithmProvider {

    private MinimEngine engine;

    @Override
    public String getAlgorithmType() {
        return "Audio/Video Engine";
    }

    public MinimBackend() {
        // Empty constructor, avoids initializing Minim unless used
    }

    // ---- Backend Implementation ----

    @Override public String getType() { return "audio"; }
    @Override public String getId() { return "minim"; }
    @Override public String getName() { return "Minim (Creative)"; }
    @Override public String getDescription() { return "High-performance creative audio engine."; }
    
    @Override 
    public boolean isAvailable() { 
        try {
            Class.forName("ddf.minim.Minim", false, getClass().getClassLoader());
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
        if (!isAvailable()) throw new IllegalStateException("Minim not available");
        if (engine == null) engine = new MinimEngine();
        engine.load(path);
    }

    @Override public void play() { 
        if (engine != null) engine.play(); 
    }
    
    @Override public void pause() { 
        if (engine != null) engine.pause(); 
    }
    
    @Override public void stop() { 
        if (engine != null) engine.stop(); 
    }
    
    @Override public double getTime() { 
        return (engine != null) ? engine.getTime() : 0.0; 
    }

    @Override public double getDuration() { 
        return (engine != null) ? engine.getDuration() : 0.0; 
    }

    @Override
    public float[] getSpectrum() {
        if (engine != null) return engine.getSpectrum();
        return new float[128];
    }

    @Override public String getBackendName() { return "Minim (Creative)"; }

    /**
     * Isolated engine to prevent NoClassDefFoundError during class loading of MinimBackend.
     */
    private static class MinimEngine {
        private ddf.minim.Minim minim;
        private ddf.minim.AudioPlayer player;
        private ddf.minim.analysis.FFT fft;

        public MinimEngine() {
            minim = new ddf.minim.Minim(new MinimHelper());
        }

        public void load(String path) throws Exception {
            if (player != null) player.close();
            player = minim.loadFile(path, 1024);
            if (player == null) throw new Exception("Minim failed to load audio file: " + path);
            fft = new ddf.minim.analysis.FFT(player.bufferSize(), player.sampleRate());
        }

        public void play() { if (player != null) player.play(); }
        public void pause() { if (player != null) player.pause(); }
        public void stop() {
            if (player != null) {
                player.pause();
                player.rewind();
            }
        }
        public double getTime() { return (player != null) ? player.position() / 1000.0 : 0.0; }
        public double getDuration() { return (player != null) ? player.length() / 1000.0 : 0.0; }

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

        @SuppressWarnings("unused")
        private class MinimHelper {
            public String sketchPath(String fileName) { return new File(fileName).getAbsolutePath(); }
            public java.io.InputStream createInput(String fileName) {
                try { return new java.io.FileInputStream(fileName); } catch(Exception e) { return null; }
            }
        }
    }
}
