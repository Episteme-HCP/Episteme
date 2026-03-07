/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.episteme.core.media.audio.backends;

import com.google.auto.service.AutoService;
import org.episteme.core.media.AudioBackend;
import org.episteme.core.media.audio.AudioBuffer;
import org.episteme.core.media.audio.AudioOp;
import org.episteme.core.media.audio.AudioAlgorithmBackend;
import org.episteme.core.technical.backend.Backend;

import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.ExecutionContext;
import org.episteme.core.technical.backend.HardwareAccelerator;
import org.episteme.core.technical.backend.Operation;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

/**
 * Standard backend using JavaSound API.
 * Consolidates discovery, playback engine, and specialized audio algorithms.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
@AutoService({Backend.class, ComputeBackend.class, AudioBackend.class, AudioAlgorithmBackend.class})
public class JavaSoundBackend implements AudioBackend, ComputeBackend, AudioAlgorithmBackend<AudioBuffer> {

    private Clip clip;

    // ---- Backend Implementation ----

    @Override public String getType() { return "audio"; }
    @Override public String getId() { return "javasound"; }
    @Override public String getName() { return "Standard JavaSound"; }
    @Override public String getDescription() { return "Default JavaSound API backend for playback and basic processing."; }
    @Override public boolean isAvailable() { return true; }
    @Override public int getPriority() { return 100; }
    
    @Override 
    public Object createBackend() { 
        // Return a new instance for usage, or this?
        // Let's return new instance to be safe for playback state.
        return new JavaSoundBackend(); 
    }

    @Override
    public ExecutionContext createContext() {
        return new JavaSoundContext();
    }

    @Override
    public HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.CPU;
    }

    // ---- AudioBackend Implementation (Playback) ----

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

    @Override
    public void shutdown() {
        if (clip != null && clip.isOpen()) {
            clip.close();
        }
    }

    // ---- AudioAlgorithmBackend Implementation (Processing) ----

    @Override
    public AudioBuffer apply(AudioBuffer audio, AudioOp<AudioBuffer> op) {
        return op.process(audio);
    }

    @Override
    public AudioBuffer createAudio(Object data, int channels, int sampleRate) {
        if (data instanceof double[]) {
            return new AudioBuffer((double[]) data, channels, sampleRate);
        }
        throw new IllegalArgumentException("Unsupported data type for JavaSoundBackend");
    }

    private static class JavaSoundContext implements ExecutionContext {
        @Override
        public <R> R execute(Operation<R> operation) {
            return operation.compute(this);
        }

        @Override
        public void close() {
            // No-op
        }
    }
}
