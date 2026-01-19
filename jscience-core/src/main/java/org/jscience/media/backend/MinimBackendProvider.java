/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.media.backend;

import org.jscience.technical.backend.BackendProvider;

public class MinimBackendProvider implements BackendProvider {
    @Override public String getType() { return "audio"; }
    @Override public String getId() { return "minim"; }
    @Override public String getName() { return "Minim (Creative)"; }
    @Override public String getDescription() { return "High-performance creative audio engine."; }
    @Override public boolean isAvailable() { return true; }
    @Override public int getPriority() { return 20; }
    @Override public Object createBackend() { return new MinimEngine(); }
}
