/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.media.backends;

import org.jscience.technical.backend.BackendProvider;

public class TarsosBackendProvider implements BackendProvider {
    @Override public String getType() { return "audio"; }
    @Override public String getId() { return "tarsos"; }
    @Override public String getName() { return "TarsosDSP (Scientific)"; }
    @Override public String getDescription() { return "Scientific audio analysis engine."; }
    @Override public boolean isAvailable() { return true; } 
    @Override public int getPriority() { return 50; }
    @Override public Object createBackend() { return new TarsosEngine(); }
}
