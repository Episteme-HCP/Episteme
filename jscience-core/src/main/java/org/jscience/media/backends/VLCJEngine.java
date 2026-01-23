/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.media.backends;

import org.jscience.media.AudioEngine;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
// import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;

/**
 * VLCJ Backend (VideoLAN Client).
 * Note: Requires VLC installed and VLCJ library.
 */
public class VLCJEngine implements AudioEngine {

    private MediaPlayerFactory factory;
    private MediaPlayer mediaPlayer;

    public VLCJEngine() {
        try {
            factory = new MediaPlayerFactory();
            mediaPlayer = factory.mediaPlayers().newMediaPlayer();
        } catch (Throwable t) {
            System.err.println("[VLCJ] Load faled: " + t.getMessage());
        }
    }

    @Override
    public void load(String path) throws Exception {
        if (mediaPlayer == null) throw new Exception("VLCJ not initialized (Check native VLC installation)");
        System.out.println("[VLCJ] Preparing media instance for: " + path);
        mediaPlayer.media().prepare(path);
    }

    @Override public void play() { if (mediaPlayer!=null) mediaPlayer.controls().play(); }
    @Override public void pause() { if (mediaPlayer!=null) mediaPlayer.controls().pause(); }
    @Override public void stop() { if (mediaPlayer!=null) mediaPlayer.controls().stop(); }
    
    @Override 
    public double getTime() { 
        return (mediaPlayer!=null) ? mediaPlayer.status().time() / 1000.0 : 0.0; 
    }
    
    @Override 
    public double getDuration() { 
        return (mediaPlayer!=null) ? mediaPlayer.status().length() / 1000.0 : 0.0; 
    }

    @Override
    public float[] getSpectrum() {
        // VLC supports audio equalizer, but getting raw FFT is complex via standard API
        // Returning dummy for visualization
        return new float[128];
    }

    @Override public String getBackendName() { return "VLCJ (Universal)"; }
}
