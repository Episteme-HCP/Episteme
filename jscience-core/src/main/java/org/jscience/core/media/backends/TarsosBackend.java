/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.core.media.backends;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.io.jvm.AudioPlayer;
import be.tarsos.dsp.util.fft.FFT;
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

    // ... existing ...
    private AudioDispatcher dispatcher;
    private FFT fft;
    private float[] magnitudes;
    private double currentTime = 0;
    private ExecutorService executor;
    private boolean isPlaying = false;

    // ---- Backend Implementation ----

    @Override public String getType() { return "audio"; }
    @Override public String getId() { return "tarsos"; }
    @Override public String getName() { return "TarsosDSP (Scientific)"; }
    @Override public String getDescription() { return "Scientific audio analysis engine."; }
    
    @Override 
    public boolean isAvailable() { 
        try {
            Class.forName("be.tarsos.dsp.AudioDispatcher");
            return true;
        } catch (Throwable t) {
            return false;
        }
    } 
    @Override public int getPriority() { return 50; }
    
    @Override 
    public Object createBackend() { 
        return new TarsosBackend(); 
    }

    // ---- AudioBackend Implementation ----

    @Override
    public void load(String path) throws Exception {
        File file = new File(path);
        
        // Stop current if any
        stop();

        // TarsosDSP 2.5 uses different factory methods
        dispatcher = AudioDispatcherFactory.fromPipe(file.getAbsolutePath(), 44100, 1024, 0);
        
        final AudioFormat format = new AudioFormat(44100, 16, 1, true, false);
        dispatcher.addAudioProcessor(new AudioPlayer(format));
        
        fft = new FFT(1024);
        magnitudes = new float[512]; // Half of FFT size
        
        dispatcher.addAudioProcessor(new AudioProcessor() {
            @Override
            public boolean process(AudioEvent audioEvent) {
                currentTime = audioEvent.getTimeStamp();
                float[] buffer = audioEvent.getFloatBuffer();
                float[] fftBuffer = buffer.clone();
                fft.forwardTransform(fftBuffer);
                fft.modulus(fftBuffer, magnitudes);
                return true;
            }

            @Override
            public void processingFinished() {}
        });
    }

    @Override
    public void play() {
        if (dispatcher != null && !isPlaying) {
            executor = Executors.newSingleThreadExecutor();
            executor.execute(dispatcher);
            isPlaying = true;
        }
    }

    @Override
    public void pause() {
        if (dispatcher != null) {
            dispatcher.stop();
            isPlaying = false;
        }
    }

    @Override
    public void stop() {
        pause();
        // Tarsos doesn't easily support rewind without reloading
    }

    @Override
    public double getTime() {
        return currentTime;
    }

    @Override
    public double getDuration() {
        return 0; // Not easily available in Tarsos without reading the whole file
    }

    @Override
    public float[] getSpectrum() {
        return magnitudes != null ? magnitudes : new float[128];
    }

    @Override
    public String getBackendName() {
        return "TarsosDSP (Scientific)";
    }
}
