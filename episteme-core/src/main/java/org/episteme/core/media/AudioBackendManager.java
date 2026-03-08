/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.media;

import org.episteme.core.media.audio.backends.JavaSoundAudioBackend;
import org.episteme.core.technical.backend.AbstractBackendManager;

/**
 * Manager for {@link AudioBackend} instances.
 * <p>
 * Extends {@link AbstractBackendManager} to provide standard backend management
 * (discovery, registration, selection) for audio backends. Replaces the legacy
 * {@code AudioBackendSystem} and {@code MediaFactory} with a unified manager.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class AudioBackendManager extends AbstractBackendManager<AudioBackend> {

    private static final AudioBackendManager INSTANCE = new AudioBackendManager();

    private AudioBackendManager() {
        super(AudioBackend.class);
    }

    public static AudioBackendManager getInstance() {
        return INSTANCE;
    }

    // ---- Static convenience methods (matches BackendManager pattern) ----

    public static AudioBackend staticDefault() {
        try {
            return getInstance().managerDefault();
        } catch (IllegalStateException e) {
            AudioBackend fallback = new JavaSoundAudioBackend();
            getInstance().managerRegister(fallback);
            getInstance().managerSetDefault(fallback.getBackendName());
            return fallback;
        }
    }

    public static void staticSelect(String nameOrId) {
        getInstance().managerSetDefault(nameOrId);
    }

    public static java.util.List<AudioBackend> staticAll() {
        return new java.util.ArrayList<>(getInstance().managerAll());
    }
}
