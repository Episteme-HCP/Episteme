/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.media.backends;

import org.jscience.technical.backend.BackendProvider;
import org.jscience.media.AudioBackend;
import org.bytedeco.javacv.FFmpegFrameGrabber;

public class JavaCVBackendProvider implements BackendProvider {
    @Override public String getType() { return "audio"; }
    @Override public String getId() { return "javacv"; }
    @Override public String getName() { return "JavaCV (FFmpeg)"; }
    @Override public String getDescription() { return "JavaCV/FFmpeg frame grabber."; }
    @Override public boolean isAvailable() { return true; }
    @Override public int getPriority() { return 30; }
    @Override public Object createBackend() { return new JavaCVBackend(); }
}

class JavaCVBackend implements AudioBackend {
    private FFmpegFrameGrabber grabber;

    @Override
    public void load(String path) throws Exception {
        grabber = new FFmpegFrameGrabber(path);
        grabber.start();
    }
    @Override public void play() {}
    @Override public void pause() {}
    @Override public void stop() { try{ if(grabber!=null) grabber.stop(); }catch(Exception e){} }
    @Override public double getTime() { return (grabber!=null) ? grabber.getTimestamp() / 1_000_000.0 : 0; }
    @Override public double getDuration() { return (grabber!=null) ? grabber.getLengthInTime() / 1_000_000.0 : 0; }
    @Override public float[] getSpectrum() { return new float[128]; }
    @Override public String getBackendName() { return "JavaCV (FFmpeg)"; }
}
