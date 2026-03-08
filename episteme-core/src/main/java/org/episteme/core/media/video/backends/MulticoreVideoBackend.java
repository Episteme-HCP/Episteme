/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.media.video.backends;

import com.google.auto.service.AutoService;
import org.episteme.core.media.VideoBackend;
import org.episteme.core.media.video.SceneTransitionDetector;
import org.episteme.core.media.video.VideoAlgorithmProvider;
import org.episteme.core.media.video.VideoAnalyzer;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.HardwareAccelerator;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Multicore CPU Video Backend using Java Parallel Streams for analysis.
 */
@AutoService({Backend.class, ComputeBackend.class, VideoBackend.class, VideoAlgorithmProvider.class, CPUBackend.class})
public class MulticoreVideoBackend implements VideoBackend, CPUBackend {

    @Override
    public String getType() {
        return "video";
    }

    @Override
    public String getId() {
        return "multicore-video";
    }

    @Override
    public String getBackendName() {
        return "Multicore Video Backend";
    }

    @Override
    public String getDescription() {
        return "Parallel CPU video analysis using Java Streams.";
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public Object createBackend() {
        return this;
    }

    @Override
    public HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.CPU;
    }

    @Override
    public void load(String path) throws Exception {
        throw new UnsupportedOperationException("MulticoreVideoBackend is for analysis only.");
    }

    @Override public void play() {}
    @Override public void pause() {}
    @Override public void stop() {}
    @Override public double getTime() { return 0.0; }
    @Override public double getDuration() { return 0.0; }
    @Override public <T> T grabFrame() { return null; }

    @Override
    public SceneTransitionDetector.Transition detectMotion(float[][] prev, float[][] curr, float threshold) {
        // Parallelized version of VideoAnalyzer.detectMotion logic
        int width = prev.length;
        int height = prev[0].length;
        
        long changedPixels = IntStream.range(0, width).parallel().mapToLong(x -> {
            long count = 0;
            for (int y = 0; y < height; y++) {
                if (Math.abs(curr[x][y] - prev[x][y]) > threshold) {
                    count++;
                }
            }
            return count;
        }).sum();

        double ratio = (double) changedPixels / (width * height);
        return new SceneTransitionDetector.Transition(0, ratio, ratio > threshold ? "MOTION" : "NONE");
    }

    @Override
    public List<SceneTransitionDetector.Transition> detectTransitions(List<float[][]> frames, double threshold) {
        // Parallelized version of SceneTransitionDetector logic
        return IntStream.range(1, frames.size()).parallel().filter(i -> {
            double diff = calculateHistogramDiff(frames.get(i-1), frames.get(i));
            return diff > threshold;
        }).mapToObj(i -> new SceneTransitionDetector.Transition(i, 1.0, "HARD_CUT")).toList();
    }

    private double calculateHistogramDiff(float[][] f1, float[][] f2) {
        // This could also use parallel streams for histogram building
        return SceneTransitionDetector.calculateHistogramDiff(f1, f2);
    }

    @Override
    public void shutdown() {}
}
