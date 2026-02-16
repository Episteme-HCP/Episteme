/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.core.media.backends;

import com.google.auto.service.AutoService;
import org.jscience.core.media.AudioBackend;
import org.jscience.core.media.audio.AudioBuffer;
import org.jscience.core.media.audio.AudioOp;
import org.jscience.core.media.audio.AudioProvider;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.core.technical.backend.Backend;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

/**
 * Standard backend using JavaSound API.
 * Consolidates discovery, playback engine, and basic algorithms.
 */
@AutoService({Backend.class, AudioBackend.class, AlgorithmProvider.class, AudioProvider.class})
public class JavaSoundBackend implements AudioBackend, AudioProvider<AudioBuffer> {

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
        // Backend pattern usually returns a new instance if it's a factory, or this if it's singleton/service.
        // Since we have state (clip), we might want new instances.
        // But getAlgorithmProviders call createBackend. 
        // Let's return new instance to be safe for playback state.
        return new JavaSoundBackend(); 
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

    // ---- AudioProvider Implementation (Algorithms) ----

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
}
