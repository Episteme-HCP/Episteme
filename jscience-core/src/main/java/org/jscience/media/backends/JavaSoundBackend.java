/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.media.backends;

import org.jscience.media.AudioBackend;
import javax.sound.sampled.*;
import java.io.File;

/**
 * Standard JavaSound implementation.
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
