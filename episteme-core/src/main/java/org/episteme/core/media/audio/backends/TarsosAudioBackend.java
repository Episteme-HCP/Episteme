/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.episteme.core.media.audio.backends;



import com.google.auto.service.AutoService;
import org.episteme.core.media.AudioBackend;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.cpu.CPUBackend;

import java.io.File;


/**
 * TarsosDSP Backend (Scientific Analysis).
 */
@AutoService({Backend.class, AudioBackend.class, CPUBackend.class})
public class TarsosAudioBackend implements AudioBackend, CPUBackend {

    private TarsosEngine engine;
    private float[] dummySpectrum = new float[128];

    // ---- Backend Implementation ----

    @Override public String getType() { return "audio"; }
    @Override public String getId() { return "tarsos"; }
    @Override public String getName() { return "TarsosDSP (Scientific)"; }
    @Override public String getDescription() { return "Scientific audio analysis engine (FFT, Pitch)."; }
    
    @Override 
    public boolean isAvailable() { 
        try {
            Class.forName("be.tarsos.dsp.AudioDispatcher", false, getClass().getClassLoader());
            return true;
        } catch (Throwable t) {
            return false;
        }
    } 
    @Override public int getPriority() { return 50; }
    
    @Override 
    public Object createBackend() { 
        return new TarsosAudioBackend(); 
    }

    // ---- AudioBackend Implementation ----

    @Override
    public void load(String path) throws Exception {
        if (!isAvailable()) throw new IllegalStateException("TarsosDSP not available");
        if (engine == null) engine = new TarsosEngine();
        engine.load(path);
    }

    @Override
    public void play() {
        if (engine != null) engine.play();
    }

    @Override
    public void pause() {
        if (engine != null) engine.pause();
    }

    @Override
    public void stop() {
        if (engine != null) engine.stop();
    }

    @Override
    public double getTime() {
        return (engine != null) ? engine.currentTime : 0;
    }

    @Override
    public double getDuration() {
        // TarsosDSP doesn't provide easy duration for streaming/raw files without full processing
        return 0; 
    }

    @Override
    public float[] getSpectrum() {
        if (engine == null || engine.magnitudes == null) return dummySpectrum;
        return engine.magnitudes;
    }

    @Override
    public String getBackendName() {
        return "TarsosDSP (Scientific)";
    }

    @Override
    public void shutdown() {
        if (engine != null) {
            engine.stop();
        }
    }
    
    // AlgorithmProvider methods
    @Override
    public String getAlgorithmType() {
        return "Audio Engine";
    }

    /**
     * Isolated engine to prevent NoClassDefFoundError during class loading of TarsosBackend.
     */
    private static class TarsosEngine {
        private be.tarsos.dsp.AudioDispatcher dispatcher;
        private be.tarsos.dsp.util.fft.FFT fft;
        private float[] magnitudes;
        private double currentTime = 0;
        private final java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newSingleThreadExecutor();

        public void load(String path) throws Exception {
            File audioFile = new File(path);
            if (!audioFile.exists()) {
                 throw new java.io.FileNotFoundException("Audio file not found: " + path);
            }
            
            dispatcher = be.tarsos.dsp.io.jvm.AudioDispatcherFactory.fromFile(audioFile, 2048, 0);
            
            try {
                dispatcher.addAudioProcessor(new be.tarsos.dsp.io.jvm.AudioPlayer(dispatcher.getFormat()));
            } catch (javax.sound.sampled.LineUnavailableException e) {
                // Playback unavailable
            }

            fft = new be.tarsos.dsp.util.fft.FFT(2048);
            dispatcher.addAudioProcessor(new be.tarsos.dsp.AudioProcessor() {
                @Override
                public boolean process(be.tarsos.dsp.AudioEvent audioEvent) {
                    float[] buffer = audioEvent.getFloatBuffer();
                    fft.forwardTransform(buffer);
                    magnitudes = new float[buffer.length / 2];
                    fft.modulus(buffer, magnitudes);
                    currentTime = audioEvent.getTimeStamp();
                    return true;
                }
                @Override
                public void processingFinished() {}
            });
        }

        public void play() {
            if (dispatcher != null && !dispatcher.isStopped()) {
                 executor.submit(dispatcher);
            }
        }

        public void pause() {
            if (dispatcher != null) dispatcher.stop();
        }

        public void stop() {
            if (dispatcher != null) dispatcher.stop();
        }
    }

    @Override
    public org.episteme.core.technical.backend.HardwareAccelerator getAcceleratorType() {
        return org.episteme.core.technical.backend.HardwareAccelerator.CPU;
    }
}
