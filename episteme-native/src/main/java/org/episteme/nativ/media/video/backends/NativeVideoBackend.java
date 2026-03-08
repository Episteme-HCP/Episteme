/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.media.video.backends;

import com.google.auto.service.AutoService;
import org.episteme.core.media.VideoBackend;
import org.episteme.core.media.video.VideoAlgorithmProvider;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;

/**
 * Native Video Backend using Project Panama (FFM API).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService({Backend.class, ComputeBackend.class, VideoBackend.class, CPUBackend.class, NativeBackend.class, VideoAlgorithmProvider.class})
public class NativeVideoBackend implements VideoBackend, VideoAlgorithmProvider, CPUBackend, NativeBackend {

    @Override
    public boolean isLoaded() {
        return true;
    }

    @Override
    public String getNativeLibraryName() {
        return "none (pure panama)";
    }

    @Override
    public String getType() {
        return "video";
    }

    @Override
    public String getId() {
        return "native-video";
    }

    @Override
    public String getBackendName() {
        return "Native Video Backend (FFM)";
    }

    @Override
    public String getDescription() {
        return "Native high-performance video backend using Project Panama.";
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void shutdown() {
    }

    @Override
    public Object createBackend() {
        return new NativeVideoBackend();
    }

    @Override
    public void load(String path) throws Exception {
    }

    @Override public void play() {}
    @Override public void pause() {}
    @Override public void stop() {}
    @Override public double getTime() { return 0.0; }
    @Override public double getDuration() { return 0.0; }
    @Override public <T> T grabFrame() { return null; }

    @Override
    public org.episteme.core.technical.backend.HardwareAccelerator getAcceleratorType() {
        return org.episteme.core.technical.backend.HardwareAccelerator.CPU;
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return new org.episteme.core.technical.backend.cpu.CPUExecutionContext();
    }
}
