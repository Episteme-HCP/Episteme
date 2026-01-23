/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.media.backends;

import org.jscience.media.AudioEngine;
import java.io.File;
import javax.sound.sampled.*;

/**
 * Standard backend using JavaSound API.
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
