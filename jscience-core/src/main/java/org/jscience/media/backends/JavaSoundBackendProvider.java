/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.media.backends;

import org.jscience.technical.backend.BackendProvider;

public class JavaSoundBackendProvider implements BackendProvider {
    @Override public String getType() { return "audio"; }
    @Override public String getId() { return "javasound"; }
    @Override public String getName() { return "Standard JavaSound"; }
    @Override public String getDescription() { return "Default JavaSound API backend."; }
    @Override public boolean isAvailable() { return true; }
    @Override public int getPriority() { return 100; }
    @Override public Object createBackend() { return new JavaSoundBackend(); }
}
