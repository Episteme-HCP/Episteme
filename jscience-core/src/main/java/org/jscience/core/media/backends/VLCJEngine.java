/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.core.media.backends;

import org.jscience.core.media.AudioEngine;
import com.google.auto.service.AutoService;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
// import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;

/**
 * VLCJ Backend (VideoLAN Client).
 * Note: Requires VLC installed and VLCJ library.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService(AlgorithmProvider.class)
public class VLCJEngine implements AudioEngine {

    @Override 
    public boolean isAvailable() { 
        try {
            Class.forName("uk.co.caprica.vlcj.factory.MediaPlayerFactory");
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

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

