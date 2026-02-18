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
import javax.sound.sampled.LineUnavailableException;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TarsosDSP Backend (Scientific Analysis).
 */
@AutoService({Backend.class, AudioBackend.class, AlgorithmProvider.class})
public class TarsosBackend implements AudioBackend, AlgorithmProvider {

    private AudioDispatcher dispatcher;
    private FFT fft;
    private float[] magnitudes;
    private double currentTime = 0;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    // ---- Backend Implementation ----

    @Override public String getType() { return "audio"; }
    @Override public String getId() { return "tarsos"; }
    @Override public String getName() { return "TarsosDSP (Scientific)"; }
    @Override public String getDescription() { return "Scientific audio analysis engine (FFT, Pitch)."; }
    
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
        // Verify file exists
        File audioFile = new File(path);
        if (!audioFile.exists()) {
             throw new java.io.FileNotFoundException("Audio file not found: " + path);
        }
        
        // Create dispatcher from file (using JVM implementation)
        // Sample rate: 44100, buffer size: 2048, overlap: 0
        dispatcher = AudioDispatcherFactory.fromFile(audioFile, 2048, 0);
        
        // Add AudioPlayer processor for playback
        try {
            dispatcher.addAudioProcessor(new AudioPlayer(dispatcher.getFormat()));
        } catch (LineUnavailableException e) {
            // Playback unavailable, but analysis might work
            // System.err.println("Tarsos playback unavailable: " + e.getMessage());
        }

        // Add FFT processor for visualization
        fft = new FFT(2048);
        dispatcher.addAudioProcessor(new AudioProcessor() {
            @Override
            public boolean process(AudioEvent audioEvent) {
                float[] buffer = audioEvent.getFloatBuffer();
                fft.forwardTransform(buffer);
                magnitudes = new float[buffer.length / 2];
                fft.modulus(buffer, magnitudes);
                currentTime = audioEvent.getTimeStamp();
                return true;
            }
            @Override
            public void processingFinished() {
            }
        });
    }

    @Override
    public void play() {
        if (dispatcher != null && !dispatcher.isStopped()) {
             // Dispatcher runs in its own thread
             executor.submit(dispatcher);
        }
    }

    @Override
    public void pause() {
        if (dispatcher != null) {
            dispatcher.stop(); // Tarsos doesn't support pause/resume natively easily without recreation
        }
    }

    @Override
    public void stop() {
        if (dispatcher != null) {
            dispatcher.stop();
        }
    }

    @Override
    public double getTime() {
        return currentTime;
    }

    @Override
    public double getDuration() {
        // Tarsos doesn't provide easy random access duration without decoding
        return 0; 
    }

    @Override
    public float[] getSpectrum() {
        if (magnitudes == null) return new float[128];
        return magnitudes;
    }

    @Override
    public String getBackendName() {
        return "TarsosDSP (Scientific)";
    }
    
    // AlgorithmProvider methods
    @Override
    public String getAlgorithmType() {
        return "Audio/Video Engine";
    }
}
