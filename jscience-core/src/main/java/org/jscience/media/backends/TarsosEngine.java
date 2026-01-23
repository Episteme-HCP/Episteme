/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.media.backends;

import be.tarsos.dsp.*;
import be.tarsos.dsp.io.jvm.AudioPlayer;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.util.fft.FFT;
import org.jscience.media.AudioEngine;
import org.jscience.media.AudioBackend;

import javax.sound.sampled.AudioFormat;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TarsosDSP Backend (Scientific Analysis).
 */
public class TarsosEngine implements AudioEngine, AudioBackend {

    private AudioDispatcher dispatcher;
    private FFT fft;
    private float[] magnitudes;
    private ExecutorService executor;
    private boolean isPlaying = false;


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
        return (dispatcher != null) ? dispatcher.secondsProcessed() : 0;
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
