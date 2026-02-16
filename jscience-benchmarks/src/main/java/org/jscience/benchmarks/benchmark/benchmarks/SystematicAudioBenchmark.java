package org.jscience.benchmarks.benchmark.benchmarks;

import org.jscience.benchmarks.benchmark.RunnableBenchmark;
import com.google.auto.service.AutoService;
import org.jscience.core.media.audio.AudioAlgorithmBackend;
import org.jscience.core.media.audio.AudioOp;

/**
 * Benchmark for Audio Processing Providers (Tarsos, Native, etc.).
 * Measures throughput for basic audio operations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
@AutoService(RunnableBenchmark.class)
public class SystematicAudioBenchmark implements SystematicBenchmark<AudioAlgorithmBackend<?>> {

    private AudioAlgorithmBackend<Object> provider;
    private Object audio;
    private final AudioOp<Object> identityOp = (aud) -> aud;

    @Override
    @SuppressWarnings("unchecked")
    public Class<AudioAlgorithmBackend<?>> getProviderClass() { 
        return (Class<AudioAlgorithmBackend<?>>) (Class<?>) AudioAlgorithmBackend.class; 
    }
    @Override public String getIdPrefix() { return "audio-throughput"; }
    @Override public String getNameBase() { return "Audio Processing Throughput"; }

    @Override public String getId() { return getIdPrefix() + "-default"; }
    @Override public String getName() { return getNameBase(); }
    @Override public String getDescription() { return "Measures basic audio processing throughput on " + (provider != null ? provider.getName() : "default backend"); }
    @Override public String getDomain() { return "Audio Processing"; }
    @Override public String getAlgorithmType() { return "Signal Processing"; }
    @Override public String getAlgorithmProvider() { return provider != null ? provider.getName() : "None"; }

    @Override
    @SuppressWarnings("unchecked")
    public void setProvider(AudioAlgorithmBackend<?> provider) {
        this.provider = (AudioAlgorithmBackend<Object>) provider;
    }

    @Override
    public boolean isAvailable() {
        return provider != null && provider.isAvailable();
    }

    @Override
    public void setup() {
        if (provider == null) throw new IllegalStateException("Provider not set");
        try {
            // Create 1s stereo float audio (44.1kHz * 2 channels)
            float[] data = new float[44100 * 2];
            audio = provider.createAudio(data, 2, 44100);
        } catch (Exception e) {
            audio = null;
        }
    }

    @Override
    public void run() {
        if (audio != null) {
            provider.apply(audio, identityOp);
        }
    }

    @Override
    public void teardown() {
        audio = null;
    }

    @Override
    public int getSuggestedIterations() {
        return 50;
    }
}
