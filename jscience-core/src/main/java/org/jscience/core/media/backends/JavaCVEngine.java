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
import org.bytedeco.javacv.FFmpegFrameGrabber;

/**
 * JavaCV Backend (FFmpeg/OpenCV).
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService(AlgorithmProvider.class)
public class JavaCVEngine implements AudioEngine {

    @Override 
    public boolean isAvailable() { 
        try {
            Class.forName("org.bytedeco.javacv.FFmpegFrameGrabber");
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    private FFmpegFrameGrabber grabber;

    @Override
    public void load(String path) throws Exception {
        System.out.println("[JavaCV] Grabbing frames from: " + path);
        grabber = new FFmpegFrameGrabber(path);
        grabber.start();
    }

    @Override 
    public void play() { 
        // JavaCV is a grabber, not a player. It needs a loop to pull frames.
        // This engine would typically act as a decoder for another playback loop.
        System.out.println("[JavaCV] Ready to grab packets"); 
    }
    
    @Override 
    public void pause() { }
    
    @Override 
    public void stop() { 
        try { if(grabber!=null) grabber.stop(); } catch(Exception e) { e.printStackTrace(); } 
    }
    
    @Override 
    public double getTime() { 
        return (grabber!=null) ? grabber.getTimestamp() / 1_000_000.0 : 0.0; 
    }
    
    @Override 
    public double getDuration() { 
        return (grabber!=null) ? grabber.getLengthInTime() / 1_000_000.0 : 0.0; 
    }

    @Override
    public float[] getSpectrum() {
        return new float[128];
    }

    @Override public String getBackendName() { return "JavaCV (FFmpeg)"; }
}

