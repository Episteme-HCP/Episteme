/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.core.media.backends;

import com.google.auto.service.AutoService;
import org.jscience.core.media.AudioBackend;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.core.technical.backend.Backend;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

/**
 * VLCJ (Native VLC) Backend for audio/video.
 */
@AutoService({Backend.class, AudioBackend.class, AlgorithmProvider.class})
public class VLCJBackend implements AudioBackend, AlgorithmProvider {

    @Override
    public String getAlgorithmType() {
        return "Audio/Video Engine";
    }

    private MediaPlayerFactory factory;
    private MediaPlayer mediaPlayer;
    private static Boolean availableCache = null;

    public VLCJBackend() {
        // Constructor logic if needed, but createBackend calls constructor.
        // We should initialize heavy resources only if this is the actual backend instance,
        // not the discovery instance.
        // But discovery checks isAvailable().
        // createBackend() returns new instance.
        // So discovery instance is lightweight.
        // Created instance will initialize factory?
        // Let's initialize lazily or in constructor if called via createBackend.
    }

    private void initPlayer() {
        if (factory == null) {
            try {
                factory = new MediaPlayerFactory();
                mediaPlayer = factory.mediaPlayers().newMediaPlayer();
            } catch (Throwable t) {
                System.err.println("[VLCJ] Load failed: " + t.getMessage());
            }
        }
    }

    // ---- Backend Implementation ----

    @Override public String getType() { return "video"; }
    @Override public String getId() { return "vlcj"; }
    @Override public String getName() { return "VLCJ (Native VLC)"; }
    @Override public String getDescription() { return "Bindings to native VLC player (video/audio)."; }
    
    @Override 
    public boolean isAvailable() { 
        if (availableCache != null) return availableCache;
        try {
            Class.forName("uk.co.caprica.vlcj.factory.MediaPlayerFactory");
            // Try to initialize factory to check native libs
            new uk.co.caprica.vlcj.factory.MediaPlayerFactory().release();
            availableCache = true;
            return true;
        } catch (Throwable t) {
            availableCache = false;
            return false;
        }
    } 
    
    @Override public int getPriority() { return 40; }
    
    @Override 
    public Object createBackend() { 
        return new VLCJBackend(); 
    }

    // ---- AudioBackend Implementation ----

    @Override public void load(String path) throws Exception {
        initPlayer();
        if(mediaPlayer!=null) mediaPlayer.media().prepare(path);
    }
    
    @Override public void play() { if(mediaPlayer!=null) mediaPlayer.controls().play(); }
    @Override public void pause() { if(mediaPlayer!=null) mediaPlayer.controls().pause(); }
    @Override public void stop() { if(mediaPlayer!=null) mediaPlayer.controls().stop(); }
    @Override public double getTime() { return (mediaPlayer!=null) ? mediaPlayer.status().time() / 1000.0 : 0; }
    @Override public double getDuration() { return (mediaPlayer!=null) ? mediaPlayer.status().length() / 1000.0 : 0; }
    @Override public float[] getSpectrum() { return new float[128]; }
    @Override public String getBackendName() { return "VLCJ (Native VLC)"; }
}
