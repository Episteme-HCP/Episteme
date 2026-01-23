/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.media.backends;

import org.jscience.media.AudioEngine;
import org.bytedeco.javacv.FFmpegFrameGrabber;

/**
 * JavaCV Backend (FFmpeg/OpenCV).
 */
public class JavaCVEngine implements AudioEngine {

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
