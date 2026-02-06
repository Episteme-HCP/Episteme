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

import org.jscience.core.technical.backend.Backend;
import org.jscience.core.media.AudioBackend;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

public class VLCJBackendProvider implements Backend {
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

