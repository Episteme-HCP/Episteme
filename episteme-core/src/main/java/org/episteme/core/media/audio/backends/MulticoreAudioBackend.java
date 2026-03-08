/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.episteme.core.media.audio.backends;

import com.google.auto.service.AutoService;
import org.episteme.core.media.audio.AudioBuffer;
import org.episteme.core.media.AudioBackend;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.ExecutionContext;
import org.episteme.core.technical.backend.Operation;
import org.episteme.core.technical.backend.HardwareAccelerator;
import org.episteme.core.technical.backend.cpu.CPUBackend;

import java.util.stream.IntStream;

/**
 * High-performance AudioBackend using Java Parallel Streams.
 */
@AutoService({Backend.class, ComputeBackend.class, AudioBackend.class, CPUBackend.class})
public class MulticoreAudioBackend implements org.episteme.core.media.AudioBackend, CPUBackend {


    @Override
    public String getType() {
        return "audio";
    }

    @Override
    public String getId() {
        return "multicore-audio";
    }

    @Override
    public String getName() {
        return "Multicore Audio Backend";
    }

    @Override
    public String getDescription() {
        return "High-performance CPU-based audio processing using Java Parallel Streams";
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public int getPriority() {
        return 100; // High priority for optimized multicore
    }

    @Override
    public void shutdown() {
        // Multicore Java Streams backend does not require explicit shutdown
    }

    @Override
    public ExecutionContext createContext() {
        return new MulticoreAudioContext();
    }


    @Override public void load(String path) throws Exception {}
    @Override public void play() {}
    @Override public void pause() {}
    @Override public void stop() {}
    @Override public double getTime() { return 0.0; }
    @Override public double getDuration() { return 0.0; }
    @Override public float[] getSpectrum() { return new float[0]; }
    @Override public String getBackendName() { return getName(); }

    /**
     * Specialized parallel gain application.
     */
    public AudioBuffer parallelGain(AudioBuffer input, double multiplier) {
        double[] samples = input.getSamples();
        double[] result = new double[samples.length];
        
        IntStream.range(0, samples.length).parallel().forEach(i -> {
            result[i] = samples[i] * multiplier;
        });
        
        return new AudioBuffer(result, input.getChannels(), input.getSampleRate());
    }

    private class MulticoreAudioContext implements ExecutionContext {
        @Override
        public <R> R execute(Operation<R> operation) {
            return operation.compute(this);
        }

        @Override
        public void close() {
            // No-op
        }
    }

    @Override
    public HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.CPU;
    }
}
