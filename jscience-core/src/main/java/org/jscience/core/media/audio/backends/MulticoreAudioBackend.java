/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.core.media.audio.backends;

import com.google.auto.service.AutoService;
import org.jscience.core.media.audio.AudioBuffer;
import org.jscience.core.media.audio.AudioOp;
import org.jscience.core.media.audio.AudioAlgorithmBackend;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.core.technical.backend.Backend;
import org.jscience.core.technical.backend.ComputeBackend;
import org.jscience.core.technical.backend.ExecutionContext;
import org.jscience.core.technical.backend.Operation;
import org.jscience.core.technical.backend.HardwareAccelerator;

import java.util.stream.IntStream;

/**
 * High-performance AudioBackend using Java Parallel Streams.
 */
@AutoService({Backend.class, ComputeBackend.class, AlgorithmProvider.class, AudioAlgorithmBackend.class})
public class MulticoreAudioBackend implements ComputeBackend, AudioAlgorithmBackend<AudioBuffer> {


    @Override
    public String getType() {
        return "audio";
    }

    @Override
    public String getId() {
        return "native-multicore-audio";
    }

    @Override
    public String getName() {
        return "Native Multicore Audio Backend";
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
        return 100; // High priority for native
    }

    @Override
    public ExecutionContext createContext() {
        return new MulticoreAudioContext();
    }

    @Override
    public AudioBuffer apply(AudioBuffer audio, AudioOp<AudioBuffer> op) {
        return op.process(audio);
    }

    @Override
    public AudioBuffer createAudio(Object data, int channels, int sampleRate) {
        if (data instanceof double[]) {
            return new AudioBuffer((double[]) data, channels, sampleRate);
        }
        throw new IllegalArgumentException("Unsupported data type for MulticoreAudioBackend");
    }

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
