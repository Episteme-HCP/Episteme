/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.media.backends;

import org.jscience.technical.backend.BackendProvider;
import org.jscience.media.AudioBackend;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

public class VLCJBackendProvider implements BackendProvider {
    @Override public String getType() { return "audio"; }
    @Override public String getId() { return "vlcj"; }
    @Override public String getName() { return "VLCJ (Native VLC)"; }
    @Override public String getDescription() { return "Bindings to native VLC player."; }
    @Override public boolean isAvailable() { return true; } 
    @Override public int getPriority() { return 40; }
    @Override public Object createBackend() { return new VLCJBackend(); }
}

class VLCJBackend implements AudioBackend {
    private MediaPlayerFactory factory;
    private MediaPlayer mediaPlayer;
    
    public VLCJBackend() {
        try {
            factory = new MediaPlayerFactory();
            mediaPlayer = factory.mediaPlayers().newMediaPlayer();
        } catch (Throwable t) {
            System.err.println("[VLCJ] Load failed: " + t.getMessage());
        }
    }

    @Override public void load(String path) throws Exception {
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
