/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.core.media.backends;

/*
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.io.jvm.AudioPlayer;
import be.tarsos.dsp.util.fft.FFT;
*/
import com.google.auto.service.AutoService;
import org.jscience.core.media.AudioBackend;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.core.technical.backend.Backend;

import javax.sound.sampled.AudioFormat;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TarsosDSP Backend (Scientific Analysis).
 */
@AutoService({Backend.class, AudioBackend.class, AlgorithmProvider.class})
public class TarsosBackend implements AudioBackend, AlgorithmProvider {

    // ... fields ...

    @Override
    public String getAlgorithmType() {
        return "Audio/Video Engine";
    }

    // TarsosDSP code commented out due to missing Maven repository
    /*
    private AudioDispatcher dispatcher;
    private FFT fft;
    private float[] magnitudes;
    */
    private double currentTime = 0;
    // private ExecutorService executor;
    private boolean isPlaying = false;

    // ---- Backend Implementation ----

    @Override public String getType() { return "audio"; }
    @Override public String getId() { return "tarsos"; }
    @Override public String getName() { return "TarsosDSP (Scientific) [UNAVAILABLE]"; }
    @Override public String getDescription() { return "Scientific audio analysis engine. (Currently disabled due to missing dependencies)"; }
    
    @Override 
    public boolean isAvailable() { 
        return false; // Force unavailable
        /*
        try {
            Class.forName("be.tarsos.dsp.AudioDispatcher");
            return true;
        } catch (Throwable t) {
            return false;
        }
        */
    } 
    @Override public int getPriority() { return 50; }
    
    @Override 
    public Object createBackend() { 
        return new TarsosBackend(); 
    }

    // ---- AudioBackend Implementation ----

    @Override
    public void load(String path) throws Exception {
        throw new UnsupportedOperationException("TarsosDSP backend is currently disabled.");
    }

    @Override
    public void play() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void stop() {
    }

    @Override
    public double getTime() {
        return currentTime;
    }

    @Override
    public double getDuration() {
        return 0; 
    }

    @Override
    public float[] getSpectrum() {
        return new float[128];
    }

    @Override
    public String getBackendName() {
        return "TarsosDSP (Scientific)";
    }
}
