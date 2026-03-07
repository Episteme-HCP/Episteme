/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.episteme.core.media.backends;

import com.google.auto.service.AutoService;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.episteme.core.media.AudioBackend;
import org.episteme.core.technical.algorithm.AlgorithmProvider;
import org.episteme.core.technical.backend.Backend;

/**
 * JavaCV (FFmpeg) Backend for audio/video.
 */
@AutoService({Backend.class, AudioBackend.class, AlgorithmProvider.class})
public class JavaCVBackend implements AudioBackend, AlgorithmProvider {

    @Override
    public String getAlgorithmType() {
        return "Audio/Video Engine";
    }

    private FFmpegFrameGrabber grabber;

    // ---- Backend Implementation ----

    @Override public String getType() { return "video"; }
    @Override public String getId() { return "javacv"; }
    @Override public String getName() { return "JavaCV (FFmpeg)"; }
    @Override public String getDescription() { return "JavaCV/FFmpeg backend for media processing (video/audio)."; }
    
    @Override 
    public boolean isAvailable() { 
        try {
            Class.forName("org.bytedeco.javacv.FFmpegFrameGrabber");
            return true;
        } catch (Throwable t) {
            return false;
        }
    }
    
    @Override public int getPriority() { return 30; }
    
    @Override 
    public Object createBackend() { 
        return new JavaCVBackend(); 
    }

    // ---- AudioBackend Implementation ----

    @Override
    public void load(String path) throws Exception {
        grabber = new FFmpegFrameGrabber(path);
        grabber.start();
    }
    
    @Override public void play() { throw new UnsupportedOperationException("Playback not supported by JavaCV backend"); }
    @Override public void pause() { throw new UnsupportedOperationException("Pause not supported by JavaCV backend"); }
    @Override public void stop() { try{ if(grabber!=null) grabber.stop(); }catch(Exception e){} }
    @Override public double getTime() { return (grabber!=null) ? grabber.getTimestamp() / 1_000_000.0 : 0; }
    @Override public double getDuration() { return (grabber!=null) ? grabber.getLengthInTime() / 1_000_000.0 : 0; }
    @Override public float[] getSpectrum() { return new float[128]; }
    @Override public String getBackendName() { return "JavaCV (FFmpeg)"; }

    @Override
    public void shutdown() {
        stop();
    }
}
